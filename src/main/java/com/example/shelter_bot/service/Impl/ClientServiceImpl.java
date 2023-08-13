package com.example.shelter_bot.service.Impl;

import com.example.shelter_bot.entity.Client;
import com.example.shelter_bot.repository.ClientRepository;
import com.example.shelter_bot.service.ClientService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.jvnet.hk2.annotations.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClientServiceImpl implements ClientService {

    private final TelegramBot telegramBot;
    private final ClientRepository clientRepository;

    private final Pattern patternForClient = Pattern.compile("([А-я\\s]+)\\s+(\\d{11})\\s+([А-я\\d\\s.,]+)");

    public ClientServiceImpl(TelegramBot telegramBot, ClientRepository clientRepository) {
        this.telegramBot = telegramBot;
        this.clientRepository = clientRepository;
    }

    /**
     * В БД сохраняем клиента.
     *
     * @param client представитель класса {@link Client}.
     */
    @Override
    public Client saveClientToRepository(Client client) {
        clientRepository.save(client);
        return client;
    }

    /**
     * Парсинг данных клиента
     *
     * @param fromId ID клиента.
     * @param clientData контактные данные
     * @return   клиента  {@link Client}.
     */
    @Override
    public Client parseClientData(Long fromId, String clientData) {
        Client client = new Client();
        if (clientData != null) {
            Matcher matcher = patternForClient.matcher(clientData);
            if (matcher.find()) {
                String name = matcher.group(1);
                String phoneNumber = matcher.group(2);
                String address = matcher.group(3);
                client.setId(fromId);
                client.setName(name);
                client.setPhoneNumber(phoneNumber);
                client.setAddress(address);
                new SendMessage(fromId, "Спасибо! Ваши данные записаны");
            } else {
                new SendMessage(fromId, "Повторите ввод данных !!!  Формат данных некорректен!");
            }
        }
        return client;
    }

    /**
     * Информация о клиент по ID.
     * @param id его идентификатор.
     */
    @Override
    public Optional<Client> findClient(Long id) {
        return clientRepository.findById(id);
    }
}