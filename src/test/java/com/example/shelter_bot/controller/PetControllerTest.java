package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Pet;
import com.example.shelter_bot.enums.PetType;
import com.example.shelter_bot.repository.PetRepository;
import com.example.shelter_bot.service.PetService;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    void getAllPetsTest() throws Exception {

        Long firstId = 1L;
        Long secondId = 2L;
        Long thirdId = 3L;

        Pet firstPet = new Pet();
        firstPet.setId(firstId);

        Pet secondPet = new Pet();
        secondPet.setId(secondId);

        Pet thirdPet = new Pet();
        thirdPet.setId(thirdId);

        when(petRepository.findAll())
                .thenReturn(List.of(firstPet, secondPet, thirdPet));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(firstId))
                .andExpect(jsonPath("$[1].id").value(secondId))
                .andExpect(jsonPath("$[2].id").value(thirdId));
    }

    @Test
    void addPetTest() throws Exception {

        Long id = 1L;
        PetType petType = PetType.CAT;
        String petName = "Cat";
        Boolean availability = true;

        Pet pet = new Pet();
        pet.setId(id);
        pet.setPetType(petType);
        pet.setPetName(petName);
        pet.setAvailability(availability);

        JSONObject requestObject = new JSONObject();
        requestObject.put("id", id);
        requestObject.put("petType", petType.name());
        requestObject.put("petName", petName);
        requestObject.put("availability", availability);

        when(petRepository.save(eq(pet)))
                .thenReturn(pet);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/pets")
                        .content(requestObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.petType").value(petType.name()))
                .andExpect(jsonPath("$.petName").value(petName))
                .andExpect(jsonPath("$.availability").value(availability));
    }

    @Test
    void removePetTest() throws Exception {

        Long correctId = 123L;
        Long wrongId = 321L;
        Pet pet = new Pet();
        pet.setId(correctId);

        when(petRepository.findById(eq(correctId))).thenReturn(Optional.of(pet));
        when(petRepository.findById(eq(wrongId))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/pets/{id}", correctId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(correctId));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/pets/{id}", wrongId))
                .andExpect(status().isNotFound());
    }
}