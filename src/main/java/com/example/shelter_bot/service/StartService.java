package com.example.shelter_bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface StartService {
    SendMessage start(Long id);
}
