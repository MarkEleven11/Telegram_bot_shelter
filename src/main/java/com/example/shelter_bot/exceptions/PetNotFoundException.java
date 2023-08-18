package com.example.shelter_bot.exceptions;

public class PetNotFoundException extends RuntimeException {

    public PetNotFoundException() {
        super("Питомец не найден");
    }
}
