package com.example.shelter_bot.repository;

import com.example.shelter_bot.entity.Shelter;
import com.example.shelter_bot.enums.PetType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {

    Shelter findShelterByPetTypeIs(PetType petType);}
