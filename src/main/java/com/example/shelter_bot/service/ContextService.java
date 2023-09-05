package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Context;
import com.example.shelter_bot.repository.ContextRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ContextService {
    private final ContextRepository contextRepository;

    public ContextService(ContextRepository contextRepository) {
        this.contextRepository = contextRepository;
    }
    public Context saveContext(Context context) {
        return contextRepository.save(context);
    }
    public Optional<Context> getByChatId(Long chatId) {
        return contextRepository.findByChatId(chatId);
    }

    public Collection<Context> getAll() {
        return contextRepository.findAll();
    }
}
