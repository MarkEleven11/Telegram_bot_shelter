package com.example.shelter_bot.exceptions;

public class VolunteerNotFoundException extends RuntimeException{

    public VolunteerNotFoundException() {super ("Не удалось найти волонтера по запросу");}
}
