package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.PetOwners;
import com.example.shelter_bot.service.PetOwnersService;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
public class PetOwnerController {

    private final PetOwnersService petOwnersService;

    public PetOwnerController(PetOwnersService petOwnersService) {
        this.petOwnersService = petOwnersService;
    }

    @ManagedOperation(description = "Получить полный список владельцев")
    @GetMapping("")
    public ResponseEntity<List<PetOwners>> getAllPetOwners() {
        return ResponseEntity.ok(petOwnersService.getAllPetOwners());
    }

    @ManagedOperation(description = "Добавить владельца в базу данных приюта")
    @PostMapping("")
    public ResponseEntity<PetOwners> addNewPetOwner(PetOwners petOwner) {
        return ResponseEntity.ok(petOwnersService.addPetOwner(petOwner));
    }

    @ManagedOperation(description = "Изменить информацию о владельце")
    @PutMapping("")
    public PetOwners updatePetOwnerInfo(PetOwners petOwner) {
        return petOwnersService.updatePetOwner(petOwner);
    }

    @ManagedOperation(description = "Удалить владельца из базы данных приюта")
    @DeleteMapping("/{id}")
    public void deletePetOwnerInfo(@PathVariable long id) {
        petOwnersService.removePetOwner(id);
    }
}
