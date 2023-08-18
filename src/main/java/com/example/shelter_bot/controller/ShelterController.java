package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Client;
import com.example.shelter_bot.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.shelter_bot.entity.Shelter;

import java.util.Optional;

@RestController
@RequestMapping("/shelter")
public class ShelterController {

    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @Operation(summary = "Добавить приют в базу даных", tags = "Shelter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Приют добавлен в базу данных",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Shelter.class))})
    })
    @PostMapping("/add")
    public ResponseEntity<Shelter> addShelter(@RequestBody Shelter shelter) {
        if (shelter.getName() == null || shelter.getAddress() == null ||
                shelter.getSchedule() == null || shelter.getAbout() == null ||
                shelter.getLocationMap() == null || shelter.getPetType() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(shelterService.saveShelterToRepository(shelter));
    }

    @Operation(summary = "Получить данные о приюте по ID", tags = "Shelter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о приюте получена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Shelter.class))})
    })
    @GetMapping("/get")
    public ResponseEntity<Optional<Shelter>> getShelter(@RequestParam (name = "id") Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Shelter> shelter = shelterService.findShelter(id);
        return ResponseEntity.ok(shelter);
    }
}
