package com.example.shelter_bot.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class SendMessageServiceImpl implements SendMessageService {
    /**
     * Вывод предупреждения при попытке совершить действие над приютом, не выбрав его.
     *
     * @param id идентификатор пользователя.
     */
    @Override
    public SendMessage shelterNotChoose(Long id) {
        return new SendMessage(id, "Приют не выбран.")
                .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton("Выбор приюта")
                        .callbackData("/start"))
        );
    }

    /**
     * Вывод сообщения при получении неизвестной команды.
     *
     * @param id идентификатор пользователя.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage commandIncorrect(Long id) {
        return new SendMessage(id, "Некорректная команда");
    }

    /**
     * Вывод сообщения.
     *
     * @param id   идентификатор пользователя.
     * @param text выводимое сообщение.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage send(Long id, String text) {
        return new SendMessage(id, text);
    }
}