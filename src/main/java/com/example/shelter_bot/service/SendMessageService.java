package com.example.shelter_bot.service;

import com.pengrad.telegrambot.request.SendMessage;
public interface SendMessageService {
    SendMessage shelterNotChoose(Long id);

    SendMessage commandIncorrect(Long id);

    SendMessage send(Long id, String text);
}
