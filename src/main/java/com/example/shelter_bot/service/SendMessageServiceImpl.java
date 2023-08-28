package com.example.shelter_bot.service;

import org.springframework.stereotype.Service;
import com.pengrad.telegrambot.request.SendMessage;

@Service
public class SendMessageServiceImpl implements SendMessageService {

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
     * @return message
     */
    @Override
    public SendMessage send(Long id, String text) {
        return new SendMessage(id, text);
    }
}