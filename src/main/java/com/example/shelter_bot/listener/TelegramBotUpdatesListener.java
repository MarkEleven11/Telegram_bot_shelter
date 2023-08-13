package com.example.shelter_bot.listener;

import com.example.shelter_bot.entity.Client;
import com.example.shelter_bot.enums.PetType;
import com.example.shelter_bot.entity.Shelter;
import com.example.shelter_bot.service.*;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.example.shelter_bot.entity.User;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.example.shelter_bot.service.StartService;
import com.example.shelter_bot.service.SendMessageService;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;

    /**
     * HashMap для сохранения выбора приюта потенциальным клиентом.
     */
    @Getter
    private static final HashMap<Long, Shelter> clientIdToShelter = new HashMap<>();

    private final UserService userService;
    private final ShelterService shelterService;
    private final VolunteerService volunteerService;
    private final ClientService clientService;
    private final StartService startService = new StartServiceImpl();
    private final SendMessageService sendMessageService = new SendMessageServiceImpl();
    private final Pattern pattern = Pattern.compile("(\"\\D+\")\\s+(\"\\d{10,11}\")");
    boolean contactUserFlag;
    boolean contactClientFlag;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, UserService userService,
                                      ShelterService shelterService, VolunteerService volunteerService,
                                      ClientService clientService) {
        this.telegramBot = telegramBot;
        this.userService = userService;
        this.shelterService = shelterService;
        this.volunteerService = volunteerService;
        this.clientService = clientService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }


    /**
     * Метод первичной обработки поступивших в бот апдейтов.
     *
     * @param updates список всех поступивших апдейтов.
     * @return количество обработанных апдейтов.
     */
    @Override
    public int process(List<Update> updates) {
        updates.stream()
                .filter(update -> update.message() != null || update.callbackQuery() != null)
                .forEach(this::handleUpdate);

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Метод обрабатывает поступившие сообщения.
     *
     * @param update сообщение, поступившее в бот.
     */
    private void handleUpdate(Update update) {
        Message message = update.message();
        Long id = message.chat().id();
        String text = message.text();
        SendResponse sendResponse;
        logger.info("Processing update: {}", update);
            try {
                switch (text) {
                    case "/start" -> this.telegramBot.execute(startService.start(id));
                    case "/catShelter" -> {
                        clientIdToShelter.put(id, shelterService.chooseShelter(PetType.CAT));
                        telegramBot.execute(shelterService.giveMenu(id));
                    }
                    case "/dogShelter" -> {
                        clientIdToShelter.put(id, shelterService.chooseShelter(PetType.DOG));
                        telegramBot.execute(shelterService.giveMenu(id));
                    }
                    case "/stage" -> {
                        telegramBot.execute(shelterService.start(clientIdToShelter.get(id), id));
                    }

                    case "/about" -> sendResponse = clientIdToShelter.containsKey(id) ? telegramBot
                            .execute(shelterService.aboutShelter(clientIdToShelter.get(id), id)) :
                            telegramBot.execute(sendMessageService.shelterNotChoose(id));
                    case "/info" -> sendResponse = clientIdToShelter.containsKey(id) ?
                            telegramBot.execute(shelterService.infoShelter(clientIdToShelter.get(id), id)) :
                            telegramBot.execute(sendMessageService.shelterNotChoose(id));

                    case "/guard" -> sendResponse = clientIdToShelter.containsKey(id) ?
                            telegramBot.execute(shelterService.getGuardContact(clientIdToShelter.get(id), id)) :
                            telegramBot.execute(sendMessageService.shelterNotChoose(id));
                    case "/contact" -> {
                        if (clientIdToShelter.containsKey(id)) {
                            telegramBot.execute(sendMessageService.send(id,
                                    "Введите контактные данные в формате: \"Фамилия Имя Отчество\" " +
                                            "\"номер телефона\".\nК примеру: \"Иванов Иван Иванович\" \"89990001122\""));
                            contactUserFlag = true;
                        } else {
                            contactUserFlag = false;
                        }
                    }

                    case "/volunteer" -> {
                        sendResponse = clientIdToShelter.containsKey(id) ?
                                telegramBot.execute(volunteerService.callVolunteer(id, clientIdToShelter.get(id))) :
                                telegramBot.execute(sendMessageService.shelterNotChoose(id));
                    }

                    case "/giveDataToBot" -> {
                        if (clientIdToShelter.containsKey(id)) {
                            telegramBot.execute(sendMessageService.send(id,
                                    "Отправь боту свои фамилию, имя, телефонный номер (из 11 цифр) " +
                                            "и адрес в следующем формате: " +
                                            "Иванов Иван 89991234567 Москва, ул. Дальняя, д.5, кв. 10 "));
                            contactClientFlag = true;
                        } else {
                            contactClientFlag = false;
                        }
                    }

                    default -> {
                        Matcher matcher = pattern.matcher((CharSequence) message);

                        if (contactUserFlag && matcher.find()) {
                            User user = new User(matcher.group(1).replace("\"", ""),
                                    matcher.group(2).replace("\"", ""), clientIdToShelter.get(id));
                            userService.writeContact(user);
                            telegramBot.execute(sendMessageService.send(id, "Спасибо! С Вами свяжутся"));
                        } else if (contactClientFlag) {
                            Client client = clientService.parseClientData(id, String.valueOf(message));
                            clientService.saveClientToRepository(client);
                            telegramBot.execute(sendMessageService.send(id, "Спасибо! С Вами свяжутся"));
                        } else {
                            telegramBot.execute(sendMessageService.commandIncorrect(id));
                        }
                    }


                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
}