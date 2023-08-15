package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Pet;
import com.example.shelter_bot.enums.PetType;
import com.example.shelter_bot.exceptions.PetNotFoundException;
import com.example.shelter_bot.repository.PetRepository;
import com.example.shelter_bot.service.PetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@WebMvcTest(PetController.class)
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PetRepository petRepository;

    @MockBean
    private PetService petService;


    @Test
    public void getPetByIdTest() {
        Long testId = 1L;
        Pet pet = new Pet(testId, PetType.DOG, "Бобик", true);
        Mockito.when(petRepository.findById(testId)).thenReturn(Optional.of(pet));
        Pet result = petService.getPetById(testId);
        Assertions.assertEquals(pet, result);
    }

    @Test
    void addPetTest() {
        Long testId = 1L;
        Pet petTest = new Pet(testId, "Кот", PetType.CAT);
        assertEquals(petTest, pet1);
    }


    @Test
    public void updatePetTest() {
        Long testId = 1L;
        Pet pet = new Pet(testId, "Собака", PetType.DOG);


        Pet updatedPet = petService.updatePet(pet);


        Assertions.assertEquals(updatedPet.getId(), Long.valueOf(1L));
        Assertions.assertEquals(updatedPet.getPetName(), "PetTest");
        Assertions.assertEquals(updatedPet.getPetType(), PetType.DOG);

        Pet pet2 = new Pet();
        Assertions.assertThrows(PetNotFoundException.class, () -> petService.updatePet(pet2));
    }
}
