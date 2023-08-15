package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Client;
import com.example.shelter_bot.exceptions.ClientNotFoundException;
import com.example.shelter_bot.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    //Добавить клиента
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }


    //Получение клиена по Id
    public Client getClientById(Long id) {
        return this.clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
    }


    //Внесение изменений
    Client updateClient(Client client) {
        Client findClient = getClientById(client.getId());
        if (findClient == null) {
            throw new ClientNotFoundException();
        }
        return clientRepository.save(client);
    }


    //Список клиентов
    public List<Client> findAllClients() {
         return (List<Client>) clientRepository.findAll();
    }


    //Удаление клиента
    public void deleteClient(Long Id){
        clientRepository.deleteById(Id);
    }

}