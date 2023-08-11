package com.example.shelter_bot.repository;

import com.example.shelter_bot.entity.PetOwners;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetOwnersRepository extends JpaRepository<PetOwners, Long> {

    PetOwners findPetOwnersByOwnerChatId(Long chatId);
}
