package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Client;
import com.example.shelter_bot.entity.Pet;
import com.example.shelter_bot.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Добавить клиента в базу даных", tags = "Client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент добавлен в базу данных",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Client.class))})
    })
    @PostMapping("/add")
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        if (client.getName() == null || client.getPhoneNumber() == null ||
                client.getAddress() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(clientService.createClient(client));
    }

    @Operation(summary = "Получить данные о клиенте по ID", tags = "Client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о клиенте получена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Client.class))})
    })
    @GetMapping("/get")
    public ResponseEntity<List<Client>> getClientById() {
        List<Client> findClientById = clientService.findAllClients();
        if (findClientById == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(findClientById);
    }
}