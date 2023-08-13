package com.example.shelter_bot.exceptions;

public class ClientNotFoundException extends RuntimeException{

    public ClientNotFoundException(String message) {
        super("Клиент не найден");
    }
}