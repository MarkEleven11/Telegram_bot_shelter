package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Client;
import com.example.shelter_bot.repository.ClientRepository;
import org.jvnet.hk2.annotations.Service;

import java.util.List;

@Service
public class ClientServiceImpl extends ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        super(clientRepository);
        this.clientRepository = clientRepository;
    }


    //Добавить клиента
    @Override
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }


    //Получение клиена по Id
    public Client getClientById(Long Id) {
        return clientRepository.findClientById(Id);
    }


    //Внесение изменений
    public Client updateClient(Client client) {
        return clientRepository.findById(client.getId())
                .map(c -> clientRepository.save(client))
                .orElse(null);
    }


    //Список клиентов
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }


    //Удаление клиента
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}