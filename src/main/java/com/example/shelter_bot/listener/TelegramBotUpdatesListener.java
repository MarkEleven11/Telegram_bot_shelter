package com.example.shelter_bot.listener;

import com.example.shelter_bot.enums.Menu;
import com.example.shelter_bot.keyboard.KeyBoard;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final KeyBoard keyBoard;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, KeyBoard keyBoard) {
        this.telegramBot = telegramBot;
        this.keyBoard = keyBoard;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    //вывод сообщения для пользователя по командам-меню.
    //здесь прописаны условия, при которых пользователь выбирает команды, как я понимаю.
    @Override
    public int process(List<Update> updates) {
        for (Update update1 : updates) {
            for (Update update : updates) {
                if (update.message() != null) {
                    Message message = update.message();
                    Long chatId = message.chat().id();
                    String text = message.text();
                    logger.info("Processing update: {}", update);
                    if (message.equals(Menu.START) && "/start".equals(text)) {
                        this.telegramBot.execute(new SendMessage(chatId, Menu.START.getText()));
                        if (message.equals(Menu.CHOOSE_CAT)) {
                            this.telegramBot.execute(new SendMessage(chatId, Menu.CHOOSE_CAT.getText()));
                        }
                        if (message.equals(Menu.CHOOSE_DOG)) {
                            this.telegramBot.execute(new SendMessage(chatId, Menu.CHOOSE_DOG.getText()));
                        }
                        if (message.equals(Menu.BASIC_INFO)) {
                            this.telegramBot.execute(new SendMessage(chatId, Menu.BASIC_INFO.getText()));
                        }
                        if (message.equals(Menu.TAKE_ANIMAL_HOME)) {
                            this.telegramBot.execute(new SendMessage(chatId, Menu.TAKE_ANIMAL_HOME.getText()));
                        }
                        if (message.equals(Menu.SEND_ANIMAL_REPORT)) {
                            this.telegramBot.execute(new SendMessage(chatId, Menu.SEND_ANIMAL_REPORT.getText()));
                        }
                        if (message.equals(Menu.CALL_VOLUNTEER)) {
                            this.telegramBot.execute(new SendMessage(chatId, Menu.CALL_VOLUNTEER.getText()));
                        }
                    } else {
                        this.telegramBot.execute(new SendMessage(chatId, "Некорректный формат сообщения"));
                    }
                }
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }

        private void sendMessage(Long chatId,String message){
            SendMessage sendMessage = new SendMessage(chatId,message);
            SendResponse sendResponse = telegramBot.execute(sendMessage);
            if (!sendResponse.isOk()) {
                logger.error("Ошибка в процессе отправки сообщения: {}", sendResponse.description());
            }

        }
    }

