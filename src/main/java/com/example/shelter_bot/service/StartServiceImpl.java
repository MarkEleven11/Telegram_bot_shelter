package com.example.shelter_bot.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.jvnet.hk2.annotations.Service;
import com.pengrad.telegrambot.request.SendMessage;
@Service
public class StartServiceImpl implements StartService {

    @Override
    public SendMessage start(Long id) {
        InlineKeyboardMarkup shelterKeyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Приют для кошек").callbackData("/catShelter"),
                new InlineKeyboardButton("Приют для собак").callbackData("/dogShelter")
        );
        return new SendMessage(id, "Привет! Это бот приюта для животных из Астаны. " +
                "Я отвечу на твои вопросы и расскажу, как забрать животное к себе домой." +
                "\n " +
                "Для начала выбери приют. Выбрать несколько нельзя.").replyMarkup(shelterKeyboard);
    }

}
