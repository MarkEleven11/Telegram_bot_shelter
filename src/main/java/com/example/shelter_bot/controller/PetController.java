package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Pet;
import com.example.shelter_bot.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @Operation(summary = "Получить полный список животных", tags = "Pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список всех питомцев",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pet.class))})
    })
    @GetMapping("")
    public ResponseEntity<List<Pet>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @Operation(summary = "Добавить питомца в базу данных приюта", tags = "Pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Питомец добавлен в базу данных",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pet.class))})
    })
    @PostMapping("")
    public ResponseEntity<Pet> addNewPet(Pet pet) {
        return ResponseEntity.ok(petService.addPet(pet));
    }

    @Operation(summary = "Изменить информацию о питомце", tags = "Pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о питомце изменена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pet.class))})
    })
    @PutMapping("")
    public Pet updatePetInfo(Pet pet) {
        return petService.updatePet(pet);
    }

    @Operation(summary = "Удалить питомца из базы данных приюта", tags = "Pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Питомец удален из базы",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pet.class))})
    })
    @DeleteMapping("/{id}")
    public void deletePetInfo(@PathVariable long id) {
        petService.removePet(id);
    }
}
