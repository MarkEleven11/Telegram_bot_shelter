package com.example.shelter_bot.listener;

import com.example.shelter_bot.entity.*;
import com.example.shelter_bot.enums.Menu;
import com.example.shelter_bot.enums.PetType;
import com.example.shelter_bot.keyboard.KeyBoard;
import com.example.shelter_bot.service.*;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.example.shelter_bot.service.SendMessageService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.shelter_bot.enums.PetType.CAT;

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
    private final ReportDataService reportDataService;
    private final ShelterService shelterService;
    private final ClientService clientService;
    private final SendMessageService sendMessageService = new SendMessageServiceImpl();
    final Pattern pattern = Pattern.compile("(\"\\D+\")\\s+(\"\\d{10,11}\")");
    boolean contactUserFlag;
    boolean contactClientFlag;
    KeyBoard keyBoard;
    ContextService contextService;
    @Value("${volunteer-chat-id}")
    private Long volunteerChatId;
    VolunteerService volunteerService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, UserService userService,
                                      ReportDataService reportDataService, ShelterService shelterService,
                                      ClientService clientService) {
        this.telegramBot = telegramBot;
        this.userService = userService;
        this.reportDataService = reportDataService;
        this.shelterService = shelterService;
        this.clientService = clientService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Метод предназначенный для switch-case,
     * который принимает текст сообщения пользователя и сравнивает со значениями enum класса ButtonCommand
     */
    public static Menu parse(String buttonText) {
        Menu[] values = Menu.values();
        for (Menu text : values) {
            if (text.getText().equals(buttonText)) {
                return text;
            }
        }
        return Menu.BACK;
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
        logger.info("Processing update: {}", update);
        System.out.println(Menu.GREETINGS.getText());
        if (text != null && update.message().photo() == null) {
            try {
                switch (parse(text)) {
                    case START -> {
                        if (contextService.getByChatId(id).isEmpty()) {
                            sendResponseMessage(id, "Привет! Я могу показать информацию о приютах," +
                                    "как взять животное из приюта и принять отчет о питомце");
                            Context context = new Context();
                            context.setChatId(id);
                            contextService.saveContext(context);
                        }
                        keyBoard.pickMenu(id);
                    }
                    case CHOOSE_CAT -> {
                        clientIdToShelter.put(id, shelterService.chooseShelter(CAT));
                        telegramBot.execute(shelterService.giveMenu(id));
                        Context context = contextService.getByChatId(id).get();
                        context.setPetType(CAT);
                        contextService.saveContext(context);
                        sendResponseMessage(id, "Вы выбрали кошачий приют.");
                        keyBoard.shelterMainMenu(id);
                    }
                    case CHOOSE_DOG -> {
                        clientIdToShelter.put(id, shelterService.chooseShelter(PetType.DOG));
                        telegramBot.execute(shelterService.giveMenu(id));
                        Context context = contextService.getByChatId(id).get();
                        context.setPetType(PetType.DOG);
                        contextService.saveContext(context);
                        sendResponseMessage(id, "Вы выбрали собачий приют.");
                        keyBoard.shelterMainMenu(id);
                    }
                    case CHOOSE_ACTION -> keyBoard.shelterMainMenu(id);
                    case TAKE_ANIMAL_HOME -> keyBoard.shelterInfoHowAdoptPetMenu(id);
                    case SHELTER_INFO -> keyBoard.shelterInfoMenu(id);
                    case BASIC_INFO -> {
                        if (contextService.getByChatId(id).isPresent()) {
                            Context context = contextService.getByChatId(id).get();
                            if (context.getPetType().equals(CAT)) {
                                sendResponseMessage(id, """
                                        Информация о кошачем приюте - ...
                                        Рекомендации о технике безопасности на территории кошачего приюта - ...
                                        Контактные данные охраны - ...
                                        """);
                            } else if (context.getPetType().equals(PetType.DOG)) {
                                sendResponseMessage(id, """
                                        Информация о собачем приюте - ...
                                        Рекомендации о технике безопасности на территории собачего приюта - ...
                                        Контактные данные охраны - ...
                                        """);
                            }
                        }
                    }
                    case ADDRESS_INFO -> {
                        if (contextService.getByChatId(id).isPresent()) {
                            Context context = contextService.getByChatId(id).get();
                            if (context.getPetType().equals(CAT)) {
                                sendResponseMessage(id, """
                                        Адрес кошачего приюта - ...
                                        График работы - ...
                                        """);
                            } else if (context.getPetType().equals(PetType.DOG)) {
                                sendResponseMessage(id, """
                                        Адрес собачего приюта - ...
                                        График работы - ...
                                        """);
                            }
                        }
                    }
                    case RECOMMENDATIONS_LIST -> {
                        if (contextService.getByChatId(id).isPresent()) {
                            Context context = contextService.getByChatId(id).get();
                            if (context.getPetType().equals(CAT)) {
                                sendResponseMessage(id, """
                                        Правила знакомства с животным - ...
                                        Список рекомендаций - ...
                                        Список причин отказа в выдаче животного - ...
                                        """);
                            } else if (context.getPetType().equals(PetType.DOG)) {
                                sendResponseMessage(id, """
                                        Правила знакомства с животным - ...
                                        Список рекомендаций - ...
                                        Советы кинолога по первичному общению с собакой - ...
                                        Рекомендации по проверенным кинологам для дальнейшего обращения к ним
                                        Список причин отказа в выдаче животного - ...
                                        """);
                            }
                        }
                    }
                    case DOCUMENTS_LIST -> {
                        if (contextService.getByChatId(id).isPresent()) {
                            Context context = contextService.getByChatId(id).get();
                            if (context.getPetType().equals(CAT)) {
                                sendResponseMessage(id,
                                        "Для взятия кота из приюта необходимы такие документы: ...");
                            } else if (context.getPetType().equals(PetType.DOG)) {
                                sendResponseMessage(id,
                                        "Для взятия собаки из приюта необходимы такие документы: ...");
                            }
                        }
                    }
                    case SEND_ANIMAL_REPORT -> {
                        telegramBot.execute(sendMessageService.send(id,
                                "Введите информацию о питомце: \"кличку питомца\" " +
                                        "\"фотографию питомца\"" + "\"рацион питомца\"" +
                                        "\"описание самочувствия питомца\"" + "\"особенности поведения\"" + "\"дату отчета\""));

                    }
                    case CALL_VOLUNTEER -> {
                        Context context = contextService.getByChatId(id).get();
                        volunteerService.callVolunteer(id, shelterService.chooseShelter(context.getPetType()));
                        sendResponseMessage(id, "Мы передали ваше сообщение волонтеру. " +
                                "Если у вас закрытый профиль отправьте контактные данные," +
                                "с помощью кнопки в меню - Отправить контактные данные");
                        sendMessageToVolunteer(id, message.messageId());
                    }
                    case SEND_DATA -> {
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
                    default -> sendResponseMessage(id, "Неизвестная команда!");
                }
                Matcher matcher = pattern.matcher((CharSequence) message);
                if (contactUserFlag && matcher.find()) {
                    User user = new User(matcher.group(1).replace("\"", ""),
                            matcher.group(2).replace("\"", ""), clientIdToShelter.get(id));
                    userService.writeContact(user);
                    telegramBot.execute(sendMessageService.send(id, "Спасибо! С Вами свяжутся"));
                } else if (contactClientFlag) {
                    Client client = clientService.createClient(new Client());
                    clientService.createClient(client);
                    telegramBot.execute(sendMessageService.send(id, "Спасибо! С Вами свяжутся"));
                } else {
                    telegramBot.execute(sendMessageService.commandIncorrect(id));
                }
                if (update.message().photo() != null && update.message().caption() != null) {
                    Calendar calendar = new GregorianCalendar();
                    long compareTime = calendar.get(Calendar.DAY_OF_MONTH);
                    long daysOfReports = reportDataService.getAll().stream()
                            .filter(s -> Objects.equals(s.getChatId(), id))
                            .count();
                    Date lastMessageDate = reportDataService.getAll().stream()
                            .filter(s -> Objects.equals(s.getChatId(), id))
                            .map(ReportData::getLastMessage)
                            .max(Date::compareTo)
                            .orElse(null);
                    long numberOfDay = 0L;
                    if (lastMessageDate != null) {
                        numberOfDay = lastMessageDate.getDate();
                    } else {
                        numberOfDay = message.date();
                    }
                    if (daysOfReports < 30) {
                        if (compareTime != numberOfDay) {
                            Context context = contextService.getByChatId(id).get();
                                Long petId = context.getClient().getPetId();
                                getReport(message, petId);
                                daysOfReports++;
                            } else {
                                sendResponseMessage(id, "У вас нет животного!");
                            }
                        } else {
                            sendResponseMessage(id, "Вы уже отправляли сегодня отчет");
                        }
                    if (daysOfReports == 30) {
                        sendResponseMessage(id, "Вы прошли испытательный срок!");
                        sendResponseMessage(volunteerChatId, "Владелец животного с chatId " + id
                                + " прошел испытательный срок!");
                    }

                } else if (update.message().photo() != null && update.message().caption() == null) {
                    sendResponseMessage(id, "Отчет нужно присылать с описанием!");
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Метод отправки текстовых сообщений.
     */
    public void sendResponseMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Ошибка в процессе отправки: {}", sendResponse.description());
        }
    }

    /**
     * Метод пересылки сообщения волонтеру
     *
     */
    public void sendMessageToVolunteer(long chatId, int messageId) {
        Volunteer volunteer = new Volunteer();
        ForwardMessage forwardMessage = new ForwardMessage(volunteer.getId(), chatId, messageId);
        SendResponse sendResponse = telegramBot.execute(forwardMessage);
        if (!sendResponse.isOk()) {
            logger.error("Ошибка в процессе отправки: {}", sendResponse.description());
        }
    }

    /**
     * Метод получения отчета и отправки его волонтеру
     */
    public void getReport(Message message, Long petId) {
        PhotoSize photo = message.photo()[0];
        String caption = message.caption();
        Long chatId = message.chat().id();

        List<String> captionMatcher = splitCaption(caption);

        String ration = captionMatcher.get(0);
        String health = captionMatcher.get(1);
        String behaviour = captionMatcher.get(2);

        GetFile getFile = new GetFile(photo.fileId());
        GetFileResponse getFileResponse = telegramBot.execute(getFile);

        try {
            File file = getFileResponse.file();
            byte[] fileContent = telegramBot.getFileContent(file);

            long date = message.date();
            Date lastMessage = new Date(date * 1000);
            reportDataService.uploadReportData(
                    chatId, String.valueOf(petId), fileContent, ration,
                    health, behaviour, lastMessage);
            sendMessageToVolunteer(chatId, message.messageId());
            sendResponseMessage(chatId, "Ваш отчет принят!");
        } catch (IOException e) {
            System.out.println("Ошибка загрузки фото");
        }

    }

    /**
     * Метод для разбивки описания под фотографии для добавления полученного текста в отчет
     * @return полученный текст в отчет
     */
        private List<String> splitCaption(String caption) {
        if (caption == null || caption.isBlank()) {
            throw new IllegalArgumentException("Описание фото не должно быть пустым");
        }
        Matcher matcher = pattern.matcher(caption);
        if (matcher.find()) {
            return new ArrayList<>(List.of(matcher.group(3), matcher.group(7), matcher.group(11)));
        } else {
            throw new IllegalArgumentException("Проверьте правильность заполнения и повторите отправку");
        }
    }
}