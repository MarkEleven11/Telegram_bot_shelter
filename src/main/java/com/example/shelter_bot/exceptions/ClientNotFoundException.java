package com.example.shelter_bot.exceptions;

import com.example.shelter_bot.entity.Client;

public class ClientNotFoundException extends RuntimeException{
    public ClientNotFoundException() {super("Клиент не найден");}
}
