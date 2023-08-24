package com.example.shelter_bot.controller;


import com.example.shelter_bot.entity.Shelter;
import com.example.shelter_bot.entity.User;
import com.example.shelter_bot.repository.UserRepository;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;


    @Test
    void addUserTest() throws Exception {

        Long id = 123L;
        String name = "Павел Павлович";
        String phoneNumber = "8-800-900-00-11";
        Shelter shelter = new Shelter();
        Long chatId = 64548L;

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setShelter(shelter);
        user.setUserChatId(chatId);

        JSONObject requestObject = new JSONObject();
        requestObject.put("id", id);
        requestObject.put("name", name);
        requestObject.put("phoneNumber", phoneNumber);
        requestObject.put("shelter", shelter);
        requestObject.put("chatId", chatId);

        when(userRepository.save(eq(user)))
                .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .content(requestObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.shelter").value(shelter))
                .andExpect(jsonPath("$.chatId").value(chatId));

    }


    @Test
    void getShelterByIdTest() throws Exception {

        Long firstId = 1L;
        Long secondId = 2L;
        Long thirdId = 3L;

        User testUser1 = new User();
        testUser1.setId(firstId);

        User testUser2 = new User();
        testUser2.setId(secondId);

        User testUser3 = new User();
        testUser3.setId(thirdId);

        when(userRepository.findAll())
                .thenReturn(List.of(testUser1, testUser2, testUser3));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(firstId))
                .andExpect(jsonPath("$[1].id").value(secondId))
                .andExpect(jsonPath("$[2].id").value(thirdId));
    }
}
