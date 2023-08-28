package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Shelter;
import com.example.shelter_bot.enums.PetType;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ShelterService {

    Shelter chooseShelter(PetType petType);
    SendMessage giveMenu(Long fromId);

    SendMessage start(Shelter shelter, Long fromId);

    Shelter saveShelterToRepository(Shelter shelter);

    Optional<Shelter> findShelter(Long id);
}
