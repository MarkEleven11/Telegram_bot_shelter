package com.example.shelter_bot.exceptions;

public class OwnerNotFoundException extends RuntimeException{

    public OwnerNotFoundException() {super ("Пользователь не найден");}
}
