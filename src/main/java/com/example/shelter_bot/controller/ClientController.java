package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Client;
import com.example.shelter_bot.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/add")
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        if (client.getName() == null || client.getPhoneNumber() == null ||
                client.getAddress() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(clientService.createClient(client));
    }


    @GetMapping("/get")
    public ResponseEntity<List<Client>> getClientById() {
        List<Client> findClientById = clientService.findAllClients();
        if (findClientById == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(findClientById);
    }
}