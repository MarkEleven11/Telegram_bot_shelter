package com.example.shelter_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface SendMessageService {
    SendMessage shelterNotChoose(Long id);

    SendMessage commandIncorrect(Long id);

    SendMessage send(Long id, String text);
}
