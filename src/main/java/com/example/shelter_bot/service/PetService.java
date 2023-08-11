package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Pet;
import com.example.shelter_bot.exceptions.PetNotFoundException;
import com.example.shelter_bot.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet getPetById(long id) {
        return petRepository.findById(id).orElse(null);
    }

    public Pet addPet(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet updatePet(Pet pet) {
        Pet findPet = getPetById(pet.getId());
        if (findPet == null) {
            throw new PetNotFoundException();
        }
        return petRepository.save(pet);
    }

    public void removePet(long id) {
        petRepository.deleteById(id);
    }
}
