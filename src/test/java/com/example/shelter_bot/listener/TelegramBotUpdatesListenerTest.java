package com.example.shelter_bot.listener;

import com.example.shelter_bot.entity.Client;
import com.example.shelter_bot.entity.Context;
import com.example.shelter_bot.entity.Pet;
import com.example.shelter_bot.entity.ReportData;
import com.example.shelter_bot.enums.PetType;
import com.example.shelter_bot.keyboard.KeyBoard;
import com.example.shelter_bot.service.ClientService;
import com.example.shelter_bot.service.ContextService;
import com.example.shelter_bot.service.ReportDataService;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import com.pengrad.telegrambot.request.GetFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotUpdatesListenerTest {

    @Mock
    private TelegramBot telegramBot;
    @Mock
    private ContextService contextMock;
    @Mock
    Context context;
    @Mock
    KeyBoard keyBoard;
    @Mock
    ReportDataService reportDataService;
    @Mock
    Client clientMock;
    @Mock
    ClientService clientService;


    @InjectMocks
    //private TelegramBotUpdatesListener telegramBotUpdatesListener;
    private TelegramBotUpdateListener telegramBotUpdatesListener;

    @Test
    public void processingStartTest() throws URISyntaxException, IOException {

        String json = Files.readString(Path.of(getClass().getClassLoader().getResource("update.json")
                .toURI()));
        Update update = BotUtils.fromJson(json.replace("%text%", "/start"), Update.class);
        SendResponse sendResponse = BotUtils.fromJson("""
                {
                "ok": true
                }
                """, SendResponse.class);
        when(telegramBot.execute(any())).thenReturn(sendResponse);

        telegramBotUpdatesListener.process((List<Update>) update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(update.message().chat().id());

        Assertions.assertThat(actual.getParameters().get("text"));
    }

    @Test
    public void handleShelterCommandTest() throws URISyntaxException, IOException {
        Update update = getUpdate("Кошачий");
        long chatId = update.message().chat().id();
        when(contextMock.getByChatId(chatId)).thenReturn(Optional.ofNullable(context));
        when(contextMock.saveContext(context)).thenReturn(context);

        SendMessage actual = getSendMessage(update);

        Assertions.assertThat(actual.getParameters().get("chat_id"))
                .isEqualTo(chatId);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Вы выбрали кошачий приют.");
        verify(contextMock, times(2)).getByChatId(chatId);
        verify(contextMock, times(1)).saveContext(context);
        verify(keyBoard, times(1)).shelterMainMenu(chatId);
    }


    @Test
    public void handleMainMenuTest() throws URISyntaxException, IOException {
        Update update = getUpdate("Главное меню");
        long chatId = update.message().chat().id();

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(keyBoard, times(1)).shelterMainMenu(chatId);
    }

    @Test
    public void handleShelterInfoMenuTest() throws URISyntaxException, IOException {
        Update update = getUpdate("Узнать информацию о приюте");
        long chatId = update.message().chat().id();

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(keyBoard, times(1)).shelterInfoMenu(chatId);
    }

    @Test
    public void handleHowAdoptPetInfoMenuTest() throws URISyntaxException, IOException {
        Update update = getUpdate("Как взять животного из приюта");
        long chatId = update.message().chat().id();

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(keyBoard, times(1)).shelterInfoHowAdoptPetMenu(chatId);
    }

    //метод на проверку работы приюта для собак
    @Test
    public void handleDOGShelterAddressAndScheduleInfoTest() throws URISyntaxException, IOException {
        Update update = getUpdate("Адрес и график работы приюта");
        long chatId = update.message().chat().id();

        when(contextMock.getByChatId(chatId)).thenReturn(Optional.of(context));
        when(context.getPetType()).thenReturn(PetType.DOG);

        SendMessage actual = getSendMessage(update);

        Assertions.assertThat(actual.getParameters().get("chat_id"))
                .isEqualTo(chatId);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("""
                    Адрес собачего приюта - ...
                    График работы - ...
                    """);
        verify(contextMock, times(2)).getByChatId(chatId);
    }

    @Test
    public void handleCATShelterRecommendationListTest() throws URISyntaxException, IOException {
        Update update = getUpdate("Список рекомендаций и советов");
        long chatId = update.message().chat().id();

        when(contextMock.getByChatId(chatId)).thenReturn(Optional.of(context));
        when(context.getPetType()).thenReturn(PetType.CAT);

        SendMessage actual = getSendMessage(update);

        Assertions.assertThat(actual.getParameters().get("chat_id"))
                .isEqualTo(chatId);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("""
                    Правила знакомства с животным - ...
                    Список рекомендаций - ...
                    Список причин отказа в выдаче животного - ...
                    """);
        verify(contextMock, times(2)).getByChatId(chatId);
    }

    @Test
    public void handleCATShelterDocumentsListTest() throws URISyntaxException, IOException {
        Update update = getUpdate("Список необходимых документов");
        long chatId = update.message().chat().id();

        when(contextMock.getByChatId(chatId)).thenReturn(Optional.of(context));
        when(context.getPetType()).thenReturn(PetType.CAT);

        SendMessage actual = getSendMessage(update);

        Assertions.assertThat(actual.getParameters().get("chat_id"))
                .isEqualTo(chatId);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Для взятия кота из приюта необходимы такие документы: ...");
        verify(contextMock, times(2)).getByChatId(chatId);
    }

    @Test
    public void handleVolunteerTest() throws URISyntaxException, IOException {
        Update update = getUpdate("Связаться с волонтером");
        long chatId = update.message().chat().id();
        SendResponse sendResponse = BotUtils.fromJson("""
                    {
                        "ok":true
                    }
                    """, SendResponse.class);
        when(telegramBot.execute(any())).thenReturn(sendResponse);

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> sendMessageArgumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        ArgumentCaptor<ForwardMessage> forwardMessageArgumentCaptor = ArgumentCaptor.forClass(ForwardMessage.class);
        verify(telegramBot, times(2)).execute(sendMessageArgumentCaptor.capture());
        verify(telegramBot, times(2)).execute(forwardMessageArgumentCaptor.capture());
        SendMessage actual = sendMessageArgumentCaptor.getAllValues().get(0);
        ForwardMessage actualForward = forwardMessageArgumentCaptor.getAllValues().get(1);

        Assertions.assertThat(actual.getParameters().get("chat_id"))
                .isEqualTo(chatId);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Мы передали ваше сообщение волонтеру. " +
                "Если у вас закрытый профиль отправьте контактные данные," +
                "с помощью кнопки в меню - Отправить контактные данные");

        Assertions.assertThat(actualForward.getParameters().get("from_chat_id"))
                .isEqualTo(chatId);
        Assertions.assertThat(actualForward.getParameters().get("message_id")).isEqualTo(1);
    }

    @Test
    public void handleSendReportTest() throws URISyntaxException, IOException {
        Update update = getUpdate("Прислать отчет о питомце");
        long chatId = update.message().chat().id();

        SendMessage actual = getSendMessage(update);

        Assertions.assertThat(actual.getParameters().get("chat_id"))
                .isEqualTo(chatId);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("""
                    Для отчета необходима фотография, рацион,
                    самочувствие и изменение в поведении питомца.
                    Загрузите фото, а в подписи к нему, скопируйте и заполните текст ниже.
                                                        
                    Рацион: ваш текст;
                    Самочувствие: ваш текст;
                    Поведение: ваш текст;
                    """);
    }

    @Test
    public void testCATShelterGetReport() throws URISyntaxException, IOException {
        String json = Files.readString(
                Path.of(TelegramBotUpdatesListenerTest.class.getResource("report.json").toURI()));
        Update update = BotUtils.fromJson(json, Update.class);
        byte[] photo = Files.readAllBytes(
                Path.of(TelegramBotUpdatesListenerTest.class.getResource("image/cat.jpg").toURI()));
        long chatId = update.message().chat().id();
        String petName = "Барсик";
        Date lastMessage = new Date(update.message().date() * 1000);
        SendResponse sendResponse = BotUtils.fromJson("""
                    {
                        "ok":true
                    }
                    """, SendResponse.class);
        GetFileResponse getFileResponse = BotUtils.fromJson("""
                    {
                    "ok": true,
                    "file": {
                        "file_id": "1"
                        }
                    }
                    """, GetFileResponse.class);
        List<String> captionMatcher = splitCaption(update.message().caption());
        String ration = captionMatcher.get(0);
        String health = captionMatcher.get(1);
        String behaviour = captionMatcher.get(2);
        ReportData report = new ReportData(
                chatId, petName, ration, health, behaviour, lastMessage, photo);

        when(telegramBot.execute(any(SendMessage.class))).thenReturn(sendResponse);
        when(telegramBot.execute(any(ForwardMessage.class))).thenReturn(sendResponse);
        when(telegramBot.execute(any(GetFile.class))).thenReturn(getFileResponse);
        when(telegramBot.getFileContent(any())).thenReturn(photo);
        when(reportDataService.getAll()).thenReturn(Collections.singletonList(report));
        when(context.getClient()).thenReturn(clientMock);
        when(context.getPetType()).thenReturn(PetType.CAT);
        when(clientMock.getName()).thenReturn(petName);
        when(contextMock.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(reportDataService, times(1)).uploadReportData(chatId, petName, photo, ration,
                health, behaviour, lastMessage);
        ArgumentCaptor<GetFile> getFileArgumentCaptor = ArgumentCaptor.forClass(GetFile.class);
        ArgumentCaptor<SendMessage> sendMessageArgumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        ArgumentCaptor<ForwardMessage> forwardMessageArgumentCaptor = ArgumentCaptor.forClass(ForwardMessage.class);
        verify(telegramBot, times(3)).execute(getFileArgumentCaptor.capture());
        verify(telegramBot, times(3)).execute(sendMessageArgumentCaptor.capture());
        verify(telegramBot, times(3)).execute(forwardMessageArgumentCaptor.capture());
        SendMessage actual = sendMessageArgumentCaptor.getAllValues().get(2);
        ForwardMessage actualForward = forwardMessageArgumentCaptor.getAllValues().get(1);

        Assertions.assertThat(actual.getParameters().get("chat_id"))
                .isEqualTo(chatId);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Ваш отчет принят!");

        Assertions.assertThat(actualForward.getParameters().get("from_chat_id"))
                .isEqualTo(chatId);
        Assertions.assertThat(actualForward.getParameters().get("message_id")).isEqualTo(2);
        verify(contextMock, times(1)).getByChatId(chatId);

    }

    @Test
        public void testCatShelterGetContact() throws URISyntaxException, IOException {
            String json = Files.readString(
                    Path.of(TelegramBotUpdatesListenerTest.class.getResource("contact.json").toURI()));
            Update update = BotUtils.fromJson(json, Update.class);
            long chatId = update.message().chat().id();
            SendResponse sendResponse = BotUtils.fromJson("""
                    {
                        "ok":true
                    }
                    """, SendResponse.class);


            when(contextMock.getByChatId(chatId)).thenReturn(Optional.of(context));
            when(context.getPetType()).thenReturn(PetType.CAT);
            when(context.getClient()).thenReturn(clientMock);
            when(clientService.updateClient(any(Client.class))).thenReturn(clientMock);
            when(telegramBot.execute(any())).thenReturn(sendResponse);

            telegramBotUpdatesListener.process(Collections.singletonList(update));

            ArgumentCaptor<SendMessage> sendMessageArgumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
            ArgumentCaptor<ForwardMessage> forwardMessageArgumentCaptor = ArgumentCaptor.forClass(ForwardMessage.class);
            verify(telegramBot, times(2)).execute(sendMessageArgumentCaptor.capture());
            verify(telegramBot, times(2)).execute(forwardMessageArgumentCaptor.capture());
            SendMessage actual = sendMessageArgumentCaptor.getAllValues().get(1);
            ForwardMessage actualForward = forwardMessageArgumentCaptor.getAllValues().get(0);

            Assertions.assertThat(actual.getParameters().get("chat_id"))
                    .isEqualTo(chatId);
            Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Мы получили ваши контактные данные");

            Assertions.assertThat(actualForward.getParameters().get("from_chat_id"))
                    .isEqualTo(chatId);
            Assertions.assertThat(actualForward.getParameters().get("message_id")).isEqualTo(1);
            verify(contextMock, times(2)).getByChatId(chatId);
            verify(clientService, times(1)).updateClient(any(Client.class));
        }

    private Update getUpdate(String text) throws URISyntaxException, IOException {
        String json = Files.readString(
                Path.of(TelegramBotUpdatesListenerTest.class.getResource("update.json").toURI()));
        return BotUtils.fromJson(json.replace("%text%", text), Update.class);
    }

    private SendMessage getSendMessage(Update update) {
        SendResponse sendResponse = BotUtils.fromJson("""
                    {
                        "ok":true
                    }
                    """, SendResponse.class);
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        reset(telegramBot);
        return argumentCaptor.getValue();
    }

    private List<String> splitCaption(String caption) {
        Pattern pattern = Pattern.compile("(Рацион:)(\\s)(\\W+)(;)\n" +
                "(Самочувствие:)(\\s)(\\W+)(;)\n" +
                "(Поведение:)(\\s)(\\W+)(;)");
        Matcher matcher = pattern.matcher(caption);
        if (matcher.find()) {
            return new ArrayList<>(List.of(matcher.group(3), matcher.group(7), matcher.group(11)));
        } else {
            throw new IllegalArgumentException("Совпадения не найдены");
        }
    }
}
