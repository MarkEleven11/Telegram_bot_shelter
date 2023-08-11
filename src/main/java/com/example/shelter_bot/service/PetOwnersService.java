package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Pet;
import com.example.shelter_bot.entity.PetOwners;
import com.example.shelter_bot.exceptions.OwnerNotFoundException;
import com.example.shelter_bot.exceptions.PetNotFoundException;
import com.example.shelter_bot.repository.PetOwnersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetOwnersService {

    private final PetOwnersRepository petOwnersRepository;

    public PetOwnersService(PetOwnersRepository petOwnersRepository) {
        this.petOwnersRepository = petOwnersRepository;
    }

    public List<PetOwners> getAllPetOwners() {
        return petOwnersRepository.findAll();
    }

    public PetOwners getPetOwnersById(long id) {
        return petOwnersRepository.findById(id).orElse(null);
    }

    public PetOwners addPetOwner(PetOwners petOwner) {
        return petOwnersRepository.save(petOwner);
    }

    public PetOwners updatePetOwner(PetOwners petOwner) {
        PetOwners findOwner = getPetOwnersById(petOwner.getId());
        if (findOwner == null) {
            throw new OwnerNotFoundException();
        }
        return petOwnersRepository.save(petOwner);
    }

    public void removePetOwner(long id) {
        petOwnersRepository.deleteById(id);
    }
}
