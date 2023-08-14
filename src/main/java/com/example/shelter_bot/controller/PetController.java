package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Pet;
import com.example.shelter_bot.service.PetService;
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

    @ManagedOperation(description = "Получить полный список животных")
    @GetMapping("")
    public ResponseEntity<List<Pet>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @ManagedOperation(description = "Добавить питомца в базу данных приюта")
    @PostMapping("")
    public ResponseEntity<Pet> addNewPet(Pet pet) {
        return ResponseEntity.ok(petService.addPet(pet));
    }

    @ManagedOperation(description = "Изменить информацию о питомце")
    @PutMapping("")
    public Pet updatePetInfo(Pet pet) {
        return petService.updatePet(pet);
    }

    @ManagedOperation(description = "Удалить питомца из базы данных приюта")
    @DeleteMapping("/{id}")
    public void deletePetInfo(@PathVariable long id) {
        petService.removePet(id);
    }
}
