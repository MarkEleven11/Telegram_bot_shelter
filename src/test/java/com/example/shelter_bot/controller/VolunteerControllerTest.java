package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Volunteer;
import com.example.shelter_bot.repository.VolunteerRepository;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
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

@WebMvcTest
public class VolunteerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VolunteerRepository volunteerRepository;

    @Test
    void getAllVolunteersTest() throws Exception {

        Long firstId = 1L;
        Long secondId = 2L;
        Long thirdId = 3L;

        Volunteer testVolunteer1 = new Volunteer();
        testVolunteer1.setId(firstId);

        Volunteer testVolunteer2 = new Volunteer();
        testVolunteer2.setId(secondId);

        Volunteer testVolunteer3 = new Volunteer();
        testVolunteer3.setId(thirdId);

        when(volunteerRepository.findAll())
                .thenReturn(List.of(testVolunteer1, testVolunteer2, testVolunteer3));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(firstId))
                .andExpect(jsonPath("$[1].id").value(secondId))
                .andExpect(jsonPath("$[2].id").value(thirdId));
    }

    @Test
    void addVolunteerTest() throws Exception {

        Long id = 1L;
        String volunteerName = "Окская Оксана";
        Long chatId = 45965L;
        Boolean availability = true;

        Volunteer volunteer = new Volunteer();
        volunteer.setId(id);
        volunteer.setVolunteerName(volunteerName);
        volunteer.setChatId(chatId);
        volunteer.setAvailable(availability);

        JSONObject requestObject = new JSONObject();
        requestObject.put("id", id);
        requestObject.put("volunteerName", volunteerName);
        requestObject.put("chatId", chatId);
        requestObject.put("availability", availability);

        when(volunteerRepository.save(eq(volunteer)))
                .thenReturn(volunteer);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteers")
                        .content(requestObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.volunteerName").value(volunteerName))
                .andExpect(jsonPath("$.chatId").value(chatId))
                .andExpect(jsonPath("$.availability").value(availability));
    }

    @Test
    void removeVolunteerTest() throws Exception {

        Long correctId = 123L;
        Long wrongId = 321L;
        Volunteer volunteer = new Volunteer();
        volunteer.setId(correctId);

        when(volunteerRepository.findById(eq(correctId))).thenReturn(Optional.of(volunteer));
        when(volunteerRepository.findById(eq(wrongId))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/volunteers/{id}", correctId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(correctId));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/volunteers/{id}", wrongId))
                .andExpect(status().isNotFound());
    }
}
