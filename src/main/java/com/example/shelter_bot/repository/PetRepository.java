package com.example.shelter_bot.repository;

import com.example.shelter_bot.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Pet findPetByPetNameAndAndAvailability(String petName, boolean availability);
}
