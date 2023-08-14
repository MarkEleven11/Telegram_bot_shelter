package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Pet;
import com.example.shelter_bot.entity.Shelter;
import com.example.shelter_bot.enums.PetType;
import com.example.shelter_bot.exceptions.PetNotFoundException;
import com.example.shelter_bot.repository.PetRepository;
import com.example.shelter_bot.service.PetService;
import javassist.NotFoundException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.telegram.telegrambots.meta.api.objects.EntityType.URL;


@WebMvcTest(PetController.class)
public class PetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PetRepository petRepository;

    private Pet pet1;


    @MockBean
    private PetService petService;


    @Test
    public void getPetByIdTest() {
        Long testId = 1L;
        Pet pet = new Pet(testId, "Собака", PetType.DOG);
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
