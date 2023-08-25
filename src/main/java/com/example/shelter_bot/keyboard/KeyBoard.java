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

@Component
public class KeyBoard {

    private final Logger logger = LoggerFactory.getLogger(KeyBoard.class);
    private TelegramBot telegramBot;

    public KeyBoard(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    //Отображает меню
    public void pickMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                Menu.CHOOSE_CAT.getText(), Menu.CHOOSE_DOG.getText());
        sendResponseMenu(chatId, replyKeyboardMarkup, "Выберите кого вы хотите взять из приюта: ");
    }

    //Главное меню
    public void shelterMainMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                Menu.BASIC_INFO.getText(),
                Menu.TAKE_ANIMAL_HOME.getText(),
                Menu.SEND_ANIMAL_REPORT.getText(),
                Menu.CALL_VOLUNTEER.getText());
        sendResponseMenu(chatId, replyKeyboardMarkup, "Главное меню приюта: "
         + "Чтобы вернуться назад нажмите команду /back");
    }

    //Метод для отображения меню о приюте
    //аналогично прошлому методу: выбор между коммандами меню с информацией
    //public void shelterInfoMenu
    public void shelterInfoMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                Menu.BASIC_INFO.getText(),
                Menu.ADDRESS_INFO.getText());
        replyKeyboardMarkup.addRow(new KeyboardButton(Menu.CALL_VOLUNTEER.getText()),
                new KeyboardButton(Menu.SEND_DATA.getText()).requestContact(true));
        replyKeyboardMarkup.addRow(Menu.START.getText());
        sendResponseMenu(chatId, replyKeyboardMarkup, "Вы можете получить информацию о приюте в меню.");
    }

    //метод для инфо как приютить питомца
    public void shelterInfoHowAdoptPetMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(Menu.RECOMMENDATIONS_LIST.getText(),
                Menu.DOCUMENTS_LIST.getText());
        replyKeyboardMarkup.addRow(new KeyboardButton(Menu.CALL_VOLUNTEER.getText()),
                new KeyboardButton(Menu.SEND_DATA.getText()).requestContact(true));
        replyKeyboardMarkup.addRow(Menu.START.getText());
        sendResponseMenu(chatId, replyKeyboardMarkup, "Информация о том, как взять животное из приюта");
    }

    //Метод для укорочения отправки ответа в меню
    public void sendResponseMenu(long chatId, ReplyKeyboardMarkup replyKeyboardMarkup, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text).replyMarkup(replyKeyboardMarkup.resizeKeyboard(true));
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (sendResponse.isOk()) {
            logger.error("Ошибка в отправке сообщения: ", sendResponse.description());
        }
    }
}
