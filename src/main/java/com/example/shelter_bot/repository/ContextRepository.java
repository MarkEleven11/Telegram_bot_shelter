package com.example.shelter_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.shelter_bot.entity.Context;

import java.util.Optional;

@Repository
public interface ContextRepository extends JpaRepository<Context, Long> {
    Optional<Context> findByChatId(Long chatId);
}
