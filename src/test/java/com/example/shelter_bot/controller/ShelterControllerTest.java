package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Shelter;
import com.example.shelter_bot.repository.ShelterRepository;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ShelterControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ShelterRepository shelterRepository;


    @Test
    void addShelterTest() throws Exception {

        Long id = 123L;
        String name = "Добрые руки";
        String address = "address";
        String schedule = "10:00-22:00";


        Shelter shelter = new Shelter();
        shelter.setId(id);
        shelter.setName(name);
        shelter.setAddress(address);
        shelter.setSchedule(schedule);

        JSONObject requestObject = new JSONObject();
        requestObject.put("id", id);
        requestObject.put("name", name);
        requestObject.put("address", address);
        requestObject.put("schedule", schedule);

        when(shelterRepository.save(eq(shelter)))
                .thenReturn(shelter);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shelter")
                        .content(requestObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.schedule").value(schedule));
    }


    @Test
    void getShelterByIdTest() throws Exception {

        Long firstId = 1L;
        Long secondId = 2L;
        Long thirdId = 3L;

        Shelter testShelter1 = new Shelter();
        testShelter1.setId(firstId);

        Shelter testShelter2 = new Shelter();
        testShelter2.setId(secondId);

        Shelter testShelter3 = new Shelter();
        testShelter3.setId(thirdId);

        when(shelterRepository.findAll())
                .thenReturn(List.of(testShelter1, testShelter2, testShelter3));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(firstId))
                .andExpect(jsonPath("$[1].id").value(secondId))
                .andExpect(jsonPath("$[2].id").value(thirdId));
    }
}
