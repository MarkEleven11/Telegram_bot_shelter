package com.example.shelter_bot.service;

import com.pengrad.telegrambot.request.SendMessage;

public interface StartService {
    SendMessage start(Long id);
}
