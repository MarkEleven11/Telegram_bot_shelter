package com.example.shelter_bot.listener;

import com.example.shelter_bot.entity.*;
import com.example.shelter_bot.enums.Menu;
import com.example.shelter_bot.enums.PetType;
import com.example.shelter_bot.keyboard.KeyBoard;
import com.example.shelter_bot.service.*;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.shelter_bot.enums.PetType.CAT;
/*
@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private static final String REGEX_MESSAGE = "(Рацион:)(\\s)(\\W+)(;)\n" +
            "(Самочувствие:)(\\s)(\\W+)(;)\n" +
            "(Поведение:)(\\s)(\\W+)(;)";
    private final Pattern pattern = Pattern.compile(REGEX_MESSAGE);
    private final ReportDataService reportDataService;
    private final ClientService clientService;
    private final KeyBoard keyBoard;
    private final ContextService contextService;
    @Value("${volunteer-chat-id}")
    private Long volunteerChatId;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, ReportDataService reportDataService, ClientService clientService, KeyBoard keyBoard, ContextService contextService) {
        this.telegramBot = telegramBot;
        this.reportDataService = reportDataService;
        this.clientService = clientService;
        this.keyBoard = keyBoard;
        this.contextService = contextService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Метод предназначенный для switch-case,
     * который принимает текст сообщения пользователя и сравнивает со значениями enum класса ButtonCommand
     */
/*
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
/*
    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Handles update: {}", update);
                Message message = update.message();
                long id = message.chat().id();
                String text = message.text();
                int messageId = message.messageId();
                Contact contact = update.message().contact();
                if (text != null && update.message().photo() == null && contact == null) {
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
                            Context context = contextService.getByChatId(id).get();
                            context.setPetType(CAT);
                            contextService.saveContext(context);
                            sendResponseMessage(id, "Вы выбрали кошачий приют.");
                            keyBoard.shelterMainMenu(id);
                        }
                    case CHOOSE_DOG -> {
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
                                            Информация о приюте для кошек "Я живой"
                                            *Рекомендации о технике безопасности на территории приюта для кошек:*
                                            Запрещается нахождение на территории приют:
                                            1. детей до 14 лет без сопровождения взрослых
                                            2. лиц в состоянии алкогольного или наркотического опьянения;
                                            3. лиц в агрессивном или неадекватном состоянии.
                                            Посетителям приюта запрещается:
                                            1. кормить животных кормами и продуктами;
                                            2. посещать блок карантина и изолятор;
                                            3. давать животным самостоятельно какие-либо ветеринарные или медицинские препараты;
                                            4. находится без сопровождения сотрудника на территории приюта;
                                            5. посещать приют со своими животными.
                                            *Контактные данные охраны:*
                                            тел.+7‒702‒029‒91‒41
                                            """);
                                } else if (context.getPetType().equals(PetType.DOG)) {
                                    sendResponseMessage(id, """
                                            Информация о приюте для собак "Любимчик"
                                            *Рекомендации о технике безопасности на территории собачьего приюта:*
                                            I. Запрещается нахождение на территории приют:
                                            1. детей до 14 лет без сопровождения взрослых
                                            2. лиц в состоянии алкогольного или наркотического опьянения;
                                            3. лиц в агрессивном или неадекватном состоянии.
                                            II. Посетителям приюта запрещается:
                                            1. кормить животных кормами и продуктами;
                                            2. посещать блок карантина и изолятор;
                                            3. давать животным самостоятельно какие-либо ветеринарные или медицинские препараты;
                                            4. находится без сопровождения сотрудника на территории приюта;
                                            5. во время выгула собак допускать ситуации, влекущие за собой, как следствие стрессовое состояние собак;
                                            6. выгуливать животных без поводка;
                                            7. посещать приют со своими животными.
                                            *Контактные данные охраны:*
                                            тел.+7‒778‒162‒99‒70.
                                            """);
                                }
                            }
                        }
                        case ADDRESS_INFO -> {
                            if (contextService.getByChatId(id).isPresent()) {
                                Context context = contextService.getByChatId(id).get();
                                if (context.getPetType().equals(CAT)) {
                                    sendResponseMessage(id, """
                                            *Адрес кошачьего приюта:*
                                            Астана, район Сарыарка, пос. Коктал-1, улица Аккорган, 5в.
                                            *График работы:*
                                            Ежедневно с 11:00 до 18:00
                                            """);
                                } else if (context.getPetType().equals(PetType.DOG)) {
                                    sendResponseMessage(id, """
                                            *Адрес собачьего приюта:* Астана, улица Куйши Дина, 26.
                                            *График работы:*  Пн-Пт 09:00–21:00; Сб-Вс 10:00–18:00
                                            """);
                                }
                            }
                        }
                        case RECOMMENDATIONS_LIST -> {
                            if (contextService.getByChatId(id).isPresent()) {
                                Context context = contextService.getByChatId(id).get();
                                if (context.getPetType().equals(CAT)) {
                                    sendResponseMessage(id, """
                                              *Правила знакомства с животным:*
                                            1. Проведите время с кошкой.
                                            2. Постарайтесь увидеть кошку несколько раз, прежде чем забрать ее,
                                            3. уделите время, чтобы она могла познакомиться с вами.
                                            *Список рекомендаций:*
                                             1. Вначале ограничьте знакомство одной комнатой,
                                             2. обеспечьте тихую и спокойную обстановку.
                                             3. постепенно знакомьте ее с другими питомцами.
                                             4. можете предложить им ее игрушки или вещи, «пропитанные» ее запахом.
                                             5. убедитесь, что ваш дом не представляет для кошки опасности.
                                            *Список причин отказа в выдаче животного:*
                                             1. проживание в съемном жилье,
                                             2.наличие в семье детей до 3 лет,
                                             3. животное берется «в подарок»,
                                             4. большое количество животных в доме.
                                            """);
                                } else if (context.getPetType().equals(PetType.DOG)) {
                                    sendResponseMessage(id, """
                                            *Правила знакомства с животным:*
                                             1. каждое животное имеет собственные особенности и историю, которую важно узнать при выборе питомца,
                                             2. начните знакомство и общение с будущим подопечным заранее,
                                             3. начните навещать в приюте, строить с ним доверительные отношения.
                                             4. приносить лакомства, начать выводить её на прогулки,
                                             5. аккуратно гладить спокойно и ненавязчиво.
                                            *Список рекомендаций:*
                                             1. убедитесь что обладаете достаточным терпением, временем и сил для воспитания питомца из приюта,
                                             2. приобретите необходимый инвентарь по уходу за собакой,
                                             3. приготовьте заранее ей место проживания.
                                            *Советы кинолога по первичному общению с собакой:*
                                             1. Не навязывайте собаке своё общество,
                                             2. Не мешайте животному самостоятельно исследовать новое окружение
                                             3. Не устраивайте “смотрины”,
                                             4. Не торопитесь ухаживать за собакой,
                                             5. Если вы столкнулись со страхами собаки, действуйте спокойно и ласково.
                                            *Рекомендации по проверенным кинологам для дальнейшего обращения к ним:*
                                             Кинологический центр Excellent тел. +7‒777‒169‒13‒99
                                            *Список причин отказа в выдаче животного:*
                                              1. проживание в съемном жилье,
                                              2. наличие в семье детей до 3 лет,
                                              3. животное «в подарок»,
                                              4. большое количество животных дома,
                                              5. физическое состояние потенциального хозяина несовместимое с уходом за собакой.
                                            """);
                                }
                            }
                        }
                        case DOCUMENTS_LIST -> {
                            if (contextService.getByChatId(id).isPresent()) {
                                Context context = contextService.getByChatId(id).get();
                                if (context.getPetType().equals(CAT)) {
                                    sendResponseMessage(id,
                                            "*Для взятия кота из приюта необходимы следующие документы:*  " +
                                                    "паспорт, документ, подтверждающий наличие собственного жилья, " +
                                                    "согласие на обработку персональных данных, " +
                                                    "согласие проживающих с Вами лиц о проживании в дальнейшем животного в квартире, " +
                                                    "подписанный с Вашей стороны договор о передачи из приюта животного");
                                } else if (context.getPetType().equals(PetType.DOG)) {
                                    sendResponseMessage(id,
                                            "Д*Для взятия собаки из приюта необходимы следующие документы:*  " +
                                                    "1. паспорт," +
                                                    "2. документ,  " +
                                                    "3. подтверждающий наличие собственного жилья,  " +
                                                    "4. согласие на обработку персональных данных,  " +
                                                    "5. согласие проживающих с Вами лиц о проживании в дальнейшем животного в квартире,  " +
                                                    "6. подписанный с Вашей стороны договор о передачи из приюта животного");
                                }
                            }
                        }
                        case SEND_ANIMAL_REPORT -> {
                            sendResponseMessage(id,
                                    "Введите информацию о питомце: \"кличку питомца\" " +
                                            "\"фотографию питомца\"" + "\"рацион питомца\"" +
                                            "\"описание самочувствия питомца\"" + "\"особенности поведения\"" + "\"дату отчета\"");

                        }
                        case CALL_VOLUNTEER -> {
                            sendResponseMessage(id, "Мы передали ваше сообщение волонтеру. " +
                                    "Если у вас закрытый профиль отправьте контактные данные," +
                                    "с помощью кнопки в меню - Отправить контактные данные");
                            sendMessageToVolunteer(id, messageId);
                        }
                        default -> sendResponseMessage(id, "Неизвестная команда! (да)");
                    }
                }
                else if (update.message().contact() != null && contextService
                        .getByChatId(id).isPresent()) {
                    Client client = clientService.createClient(new Client());
                    clientService.createClient(client);
                    sendResponseMessage(id, "Мы получили ваши контактные данные");
                    sendResponseMessage(id, "Спасибо! С Вами свяжутся");
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
        });
    }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Метод отправки текстовых сообщений.
     */
/*
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
/*
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
/*
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
/*
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
}*/