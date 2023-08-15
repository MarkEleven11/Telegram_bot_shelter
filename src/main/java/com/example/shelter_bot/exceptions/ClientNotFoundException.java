package com.example.shelter_bot.exceptions;

public class ClientNotFoundException extends RuntimeException{

    public ClientNotFoundException() {
        super("Клиент не найден");
    }
}