package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Client;
import com.example.shelter_bot.service.ClientService;
import org.junit.jupiter.api.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        return ResponseEntity.ok(clientService.saveClientToRepository(client));
    }


    @GetMapping("/get")
    public ResponseEntity<Optional<Client>> getClient(@RequestParam (name = "id") Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Client> client = clientService.findClient(id);
        return ResponseEntity.ok(client);
    }

}