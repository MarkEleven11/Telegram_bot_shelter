package com.example.shelter_bot.listner;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

public class TelegramBotUpdateListnerTest {
    @ExtendWith(MockitoExtension.class)
    class TelegramBotUpdateListenerTest {
        @Mock
        TelegramBot telegramBot;
        @Mock
        KeyBoard keyBoard;
        @Mock
        ContextService contextService;
        @Mock
        CatOwnersService catOwnersService;
        @Mock
        DogOwnerService dogOwnerService;
        @Mock
        ReportDataService reportDataService;
        @Mock
        Context contextMock;
        @Mock
        DogOwner dogOwnerMock;
        @Mock
        Dog dogMock;
        @Mock
        CatOwners catOwnerMock;
        @Mock
        Cat catMock;
        @InjectMocks
        TelegramBotUpdateListener telegramBotUpdateListener;

        @Test
        public void handleStartCommandWithContextEmptyTest() throws URISyntaxException, IOException {
            Update update = getUpdate("/start");
            long chatId = update.message().chat().id();
            Context context = new Context();
            context.setChatId(chatId);

            when(contextService.getByChatId(chatId)).thenReturn(Optional.empty());
            when(contextService.saveContext(context)).thenReturn(context);

            SendMessage actual = getSendMessage(update);

            Assertions.assertThat(actual.getParameters().get("chat_id"))
                    .isEqualTo(chatId);
            Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Привет! Я могу показать информацию о приютах," +
                    "как взять животное из приюта и принять отчет о питомце");
            verify(contextService, times(1)).getByChatId(chatId);
            verify(contextService, times(1)).saveContext(context);
            verify(keyBoard, times(1)).chooseMenu(chatId);
        }

        @Test
        public void handleStartCommandWithContextExistsTest() throws URISyntaxException, IOException {
            Update update = getUpdate("/start");
            long chatId = update.message().chat().id();

            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));

            telegramBotUpdateListener.process(Collections.singletonList(update));

            verify(contextService, times(1)).getByChatId(chatId);
            verify(keyBoard, times(1)).chooseMenu(chatId);
            verify(contextService, never()).saveContext(any(Context.class));
        }

        @Test
        public void handleCatShelterCommandTest() throws URISyntaxException, IOException {
            Update update = getUpdate("Кошачий");
            long chatId = update.message().chat().id();


            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));
            when(contextService.saveContext(contextMock)).thenReturn(contextMock);

            SendMessage actual = getSendMessage(update);

            Assertions.assertThat(actual.getParameters().get("chat_id"))
                    .isEqualTo(chatId);
            Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Вы выбрали кошачий приют.");
            verify(contextService, times(2)).getByChatId(chatId);
            verify(contextService, times(1)).saveContext(contextMock);
            verify(keyBoard, times(1)).shelterMainMenu(chatId);
        }

        @Test
        public void handleDogShelterCommandTest() throws URISyntaxException, IOException {
            Update update = getUpdate("Собачий");
            long chatId = update.message().chat().id();


            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));
            when(contextService.saveContext(contextMock)).thenReturn(contextMock);

            SendMessage actual = getSendMessage(update);

            Assertions.assertThat(actual.getParameters().get("chat_id"))
                    .isEqualTo(chatId);
            Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Вы выбрали собачий приют.");
            verify(contextService, times(2)).getByChatId(chatId);
            verify(contextService, times(1)).saveContext(contextMock);
            verify(keyBoard, times(1)).shelterMainMenu(chatId);
        }

        @Test
        public void handleMainMenuTest() throws URISyntaxException, IOException {
            Update update = getUpdate("Главное меню");
            long chatId = update.message().chat().id();

            telegramBotUpdateListener.process(Collections.singletonList(update));

            verify(keyBoard, times(1)).shelterMainMenu(chatId);
        }

        @Test
        public void handleShelterInfoMenuTest() throws URISyntaxException, IOException {
            Update update = getUpdate("Узнать информацию о приюте");
            long chatId = update.message().chat().id();

            telegramBotUpdateListener.process(Collections.singletonList(update));

            verify(keyBoard, times(1)).shelterInfoMenu(chatId);
        }

        @Test
        public void handleHowAdoptPetInfoMenuTest() throws URISyntaxException, IOException {
            Update update = getUpdate("Как взять животного из приюта");
            long chatId = update.message().chat().id();

            telegramBotUpdateListener.process(Collections.singletonList(update));

            verify(keyBoard, times(1)).shelterInfoHowAdoptPetMenu(chatId);
        }

        @Test
        public void handleCatShelterInfoTest() throws URISyntaxException, IOException {
            Update update = getUpdate("Общая информация");
            long chatId = update.message().chat().id();

            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));
            when(contextMock.getShelterType()).thenReturn(ShelterType.CAT);

            SendMessage actual = getSendMessage(update);

            Assertions.assertThat(actual.getParameters().get("chat_id"))
                    .isEqualTo(chatId);
            Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("""
                    Информация о кошачем приюте - ...
                    Рекомендации о технике безопасности на территории кошачего приюта - ...
                    Контактные данные охраны - ...
                    """);
            verify(contextService, times(2)).getByChatId(chatId);
        }

        @Test
        public void handleDogShelterInfoTest() throws URISyntaxException, IOException {
            Update update = getUpdate("Общая информация");
            long chatId = update.message().chat().id();

            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));
            when(contextMock.getShelterType()).thenReturn(ShelterType.DOG);

            SendMessage actual = getSendMessage(update);

            Assertions.assertThat(actual.getParameters().get("chat_id"))
                    .isEqualTo(chatId);
            Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("""
                    Информация о собачем приюте - ...
                    Рекомендации о технике безопасности на территории собачего приюта - ...
                    Контактные данные охраны - ...
                    """);
            verify(contextService, times(2)).getByChatId(chatId);
        }

        @Test
        public void handleCatShelterAddressAndScheduleInfoTest() throws URISyntaxException, IOException {
            Update update = getUpdate("Адрес и график работы приюта");
            long chatId = update.message().chat().id();

            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));
            when(contextMock.getShelterType()).thenReturn(ShelterType.CAT);

            SendMessage actual = getSendMessage(update);

            Assertions.assertThat(actual.getParameters().get("chat_id"))
                    .isEqualTo(chatId);
            Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("""
                    Адрес кошачего приюта - ...
                    График работы - ...
                    """);
            verify(contextService, times(2)).getByChatId(chatId);
        }

        @Test
        public void handleDogShelterAddressAndScheduleInfoTest() throws URISyntaxException, IOException {
            Update update = getUpdate("Адрес и график работы приюта");
            long chatId = update.message().chat().id();

            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));
            when(contextMock.getShelterType()).thenReturn(ShelterType.DOG);

            SendMessage actual = getSendMessage(update);

            Assertions.assertThat(actual.getParameters().get("chat_id"))
                    .isEqualTo(chatId);
            Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("""
                    Адрес собачего приюта - ...
                    График работы - ...
                    """);
            verify(contextService, times(2)).getByChatId(chatId);
        }

        @Test
        public void handleCatShelterRecommendationListTest() throws URISyntaxException, IOException {
            Update update = getUpdate("Список рекомендаций и советов");
            long chatId = update.message().chat().id();

            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));
            when(contextMock.getShelterType()).thenReturn(ShelterType.CAT);

            SendMessage actual = getSendMessage(update);

            Assertions.assertThat(actual.getParameters().get("chat_id"))
                    .isEqualTo(chatId);
            Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("""
                    Правила знакомства с животным - ...
                    Список рекомендаций - ...
                    Список причин отказа в выдаче животного - ...
                    """);
            verify(contextService, times(2)).getByChatId(chatId);
        }

        @Test
        public void handleDogShelterRecommendationListTest() throws URISyntaxException, IOException {
            Update update = getUpdate("Список рекомендаций и советов");
            long chatId = update.message().chat().id();

            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));
            when(contextMock.getShelterType()).thenReturn(ShelterType.DOG);

            SendMessage actual = getSendMessage(update);

            Assertions.assertThat(actual.getParameters().get("chat_id"))
                    .isEqualTo(chatId);
            Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("""
                    Правила знакомства с животным - ...
                    Список рекомендаций - ...
                    Советы кинолога по первичному общению с собакой - ...
                    Рекомендации по проверенным кинологам для дальнейшего обращения к ним
                    Список причин отказа в выдаче животного - ...
                    """);
            verify(contextService, times(2)).getByChatId(chatId);
        }

        @Test
        public void handleCatShelterDocumentsListTest() throws URISyntaxException, IOException {
            Update update = getUpdate("Список необходимых документов");
            long chatId = update.message().chat().id();

            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));
            when(contextMock.getShelterType()).thenReturn(ShelterType.CAT);

            SendMessage actual = getSendMessage(update);

            Assertions.assertThat(actual.getParameters().get("chat_id"))
                    .isEqualTo(chatId);
            Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Для взятия кота из приюта необходимы такие документы: ...");
            verify(contextService, times(2)).getByChatId(chatId);
        }

        @Test
        public void handleDogShelterDocumentsListTest() throws URISyntaxException, IOException {
            Update update = getUpdate("Список необходимых документов");
            long chatId = update.message().chat().id();

            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));
            when(contextMock.getShelterType()).thenReturn(ShelterType.DOG);

            SendMessage actual = getSendMessage(update);

            Assertions.assertThat(actual.getParameters().get("chat_id"))
                    .isEqualTo(chatId);
            Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Для взятия собаки из приюта необходимы такие документы: ...");
            verify(contextService, times(2)).getByChatId(chatId);
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

            telegramBotUpdateListener.process(Collections.singletonList(update));

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
        public void handleUnknownCommandTest() throws URISyntaxException, IOException {
            Update update = getUpdate("Как дела?");
            long chatId = update.message().chat().id();

            SendMessage actual = getSendMessage(update);

            Assertions.assertThat(actual.getParameters().get("chat_id"))
                    .isEqualTo(chatId);
            Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Неизвестная команда!");
        }

        @Test
        public void testCatShelterGetReport() throws URISyntaxException, IOException {
            String json = Files.readString(
                    Path.of(TelegramBotUpdateListenerTest.class.getResource("report.json").toURI()));
            Update update = BotUtils.fromJson(json, Update.class);
            byte[] photo = Files.readAllBytes(
                    Path.of(TelegramBotUpdateListenerTest.class.getResource("image/cat.jpg").toURI()));
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
            when(contextMock.getCatOwner()).thenReturn(catOwnerMock);
            when(contextMock.getShelterType()).thenReturn(ShelterType.CAT);
            when(catOwnerMock.getCat()).thenReturn(catMock);
            when(catMock.getName()).thenReturn(petName);
            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));

            telegramBotUpdateListener.process(Collections.singletonList(update));

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
            verify(contextService, times(1)).getByChatId(chatId);

        }

        @Test
        public void testDogShelterGetReport() throws URISyntaxException, IOException {
            String json = Files.readString(
                    Path.of(TelegramBotUpdateListenerTest.class.getResource("report.json").toURI()));
            Update update = BotUtils.fromJson(json, Update.class);
            byte[] photo = Files.readAllBytes(
                    Path.of(TelegramBotUpdateListenerTest.class.getResource("image/dog.jpeg").toURI()));
            long chatId = update.message().chat().id();
            String petName = "Жорик";
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
            when(contextMock.getDogOwner()).thenReturn(dogOwnerMock);
            when(contextMock.getShelterType()).thenReturn(ShelterType.DOG);
            when(dogOwnerMock.getDog()).thenReturn(dogMock);
            when(dogMock.getName()).thenReturn(petName);
            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));

            telegramBotUpdateListener.process(Collections.singletonList(update));

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
            verify(contextService, times(1)).getByChatId(chatId);

        }

        @Test
        public void testCatShelterGetContact() throws URISyntaxException, IOException {
            String json = Files.readString(
                    Path.of(TelegramBotUpdateListenerTest.class.getResource("contact.json").toURI()));
            Update update = BotUtils.fromJson(json, Update.class);
            long chatId = update.message().chat().id();
            SendResponse sendResponse = BotUtils.fromJson("""
                    {
                        "ok":true
                    }
                    """, SendResponse.class);


            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));
            when(contextMock.getShelterType()).thenReturn(ShelterType.CAT);
            when(contextMock.getCatOwner()).thenReturn(catOwnerMock);
            when(catOwnersService.update(any(CatOwners.class))).thenReturn(catOwnerMock);
            when(telegramBot.execute(any())).thenReturn(sendResponse);

            telegramBotUpdateListener.process(Collections.singletonList(update));

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
            verify(contextService, times(2)).getByChatId(chatId);
            verify(catOwnersService, times(1)).update(any(CatOwners.class));
            verify(dogOwnerService, never()).save(any(DogOwner.class));
        }

        @Test
        public void testDogShelterGetContact() throws URISyntaxException, IOException {
            String json = Files.readString(
                    Path.of(TelegramBotUpdateListenerTest.class.getResource("contact.json").toURI()));
            Update update = BotUtils.fromJson(json, Update.class);
            long chatId = update.message().chat().id();
            SendResponse sendResponse = BotUtils.fromJson("""
                    {
                        "ok":true
                    }
                    """, SendResponse.class);


            when(contextService.getByChatId(chatId)).thenReturn(Optional.of(contextMock));
            when(contextMock.getShelterType()).thenReturn(ShelterType.DOG);
            when(contextMock.getDogOwner()).thenReturn(dogOwnerMock);
            when(dogOwnerService.save(any(DogOwner.class))).thenReturn(dogOwnerMock);
            when(telegramBot.execute(any())).thenReturn(sendResponse);

            telegramBotUpdateListener.process(Collections.singletonList(update));

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
            verify(contextService, times(2)).getByChatId(chatId);
            verify(dogOwnerService, times(1)).save(any(DogOwner.class));
            verify(catOwnersService, never()).update(any(CatOwners.class));
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

        private SendMessage getSendMessage(Update update) {
            SendResponse sendResponse = BotUtils.fromJson("""
                    {
                        "ok":true
                    }
                    """, SendResponse.class);
            when(telegramBot.execute(any())).thenReturn(sendResponse);
            telegramBotUpdateListener.process(Collections.singletonList(update));
            ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
            verify(telegramBot).execute(argumentCaptor.capture());
            reset(telegramBot);
            return argumentCaptor.getValue();
        }

        private Update getUpdate(String text) throws URISyntaxException, IOException {
            String json = Files.readString(
                    Path.of(TelegramBotUpdateListenerTest.class.getResource("update.json").toURI()));
            return BotUtils.fromJson(json.replace("%text%", text), Update.class);
        }
    }
}
