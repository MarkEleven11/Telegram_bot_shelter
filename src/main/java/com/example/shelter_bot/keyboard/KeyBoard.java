package com.example.shelter_bot.keyboard;

import com.example.shelter_bot.enums.Menu;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Класс с методами отображения меню для пользователя в виде клавиатуры
 *
 * @author Riyaz Karimullin
 */
@Component
public class KeyBoard {
    private final Logger logger = LoggerFactory.getLogger(KeyBoard.class);
    private TelegramBot telegramBot;

    public KeyBoard(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Метод отображает меню, где выбирается приют.
     *
     * @param chatId
     */
    public void pickMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                Menu.CHOOSE_CAT.getText(), Menu.CHOOSE_DOG.getText());
        sendResponseMenu(chatId, replyKeyboardMarkup, "Выберите приют в меню ниже.");
    }

    /**
     * Метод отображает главное меню приюта.
     *
     * @param chatId
     */
    public void shelterMainMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{Menu.BASIC_INFO.getText(), Menu.TAKE_ANIMAL_HOME.getText()},
                new String[]{Menu.SEND_ANIMAL_REPORT.getText(), Menu.CALL_VOLUNTEER.getText()});
        sendResponseMenu(chatId, replyKeyboardMarkup, "Ниже представлено главное меню приюта. " +
                "Чтобы вернуться к выбору приюта, напишите команду /start");
    }

    /**
     * Метод отображает меню, с информацией о приюте.
     *
     * @param chatId
     */
    public void shelterInfoMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                Menu.SHELTER_INFO.getText(),
                Menu.ADDRESS_INFO.getText());
        replyKeyboardMarkup.addRow(new KeyboardButton(Menu.CALL_VOLUNTEER.getText()),
                new KeyboardButton(Menu.SEND_DATA.getText()).requestContact(true));
        replyKeyboardMarkup.addRow(Menu.START.getText());
        sendResponseMenu(chatId, replyKeyboardMarkup, "Вы можете получить информацию о приюте в меню.");
    }

    /**
     * Метод отображает меню, с информацией о том, как взять питомца из приюта.
     *
     * @param chatId
     */
    public void shelterInfoHowAdoptPetMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(Menu.RECOMMENDATIONS_LIST.getText(),
                Menu.DOCUMENTS_LIST.getText());
        replyKeyboardMarkup.addRow(new KeyboardButton(Menu.CALL_VOLUNTEER.getText()),
                new KeyboardButton(Menu.SEND_DATA.getText()).requestContact(true));
        replyKeyboardMarkup.addRow(Menu.START.getText());
        sendResponseMenu(chatId, replyKeyboardMarkup, "Информация о том, как взять животное из приюта");
    }

    /**
     * Метод принимает клавиатуру и текст, и отправляет ответ в чат по chatId.
     *
     * @param chatId
     * @param replyKeyboardMarkup
     * @param text
     */
    public void sendResponseMenu(long chatId, ReplyKeyboardMarkup replyKeyboardMarkup, String text) {
        SendMessage sendMessage = new SendMessage(
                chatId, text).replyMarkup(replyKeyboardMarkup.resizeKeyboard(true));
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }
}
