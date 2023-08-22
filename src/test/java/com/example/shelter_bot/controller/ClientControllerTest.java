package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Client;
import com.example.shelter_bot.repository.ClientRepository;
import com.example.shelter_bot.service.ClientService;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientRepository clientRepository;


    @Test
    void CreateClientTest() throws Exception {

        Long id = 123L;
        String name = "Ivan Ivanov";
        String phoneNumber = "+89990001122";
        String address = "address";

        Client client = new Client();
        client.setId(id);
        client.setName(name);
        client.setPhoneNumber(phoneNumber);
        client.setAddress(address);

        JSONObject requestObject = new JSONObject();
        requestObject.put("id", id);
        requestObject.put("name", name);
        requestObject.put("phoneNumber", phoneNumber);
        requestObject.put("address", address);

        when(clientRepository.save(eq(client)))
                .thenReturn(client);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/client")
                        .content(requestObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.address").value(address));
    }


    @Test
    void getClientByIdTest() throws Exception {

        Long firstId = 1L;
        Long secondId = 2L;
        Long thirdId = 3L;

        Client firstClient = new Client();
        firstClient.setId(firstId);

        Client secondClient = new Client();
        secondClient.setId(secondId);

        Client thirdClient = new Client();
        thirdClient.setId(thirdId);

        when(clientRepository.findAll())
                .thenReturn(List.of(firstClient, secondClient, thirdClient));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(firstId))
                .andExpect(jsonPath("$[1].id").value(secondId))
                .andExpect(jsonPath("$[2].id").value(thirdId));
    }


    @Test
    void updateClientTest() throws Exception {

        Long id = 123L;
        Long wrongId = 231L;
        String oldName = "Ivan Ivanov";
        String newName = "Petr Petrov";

        Client oldClient = new Client();
        oldClient.setId(id);
        oldClient.setName(oldName);

        Client newClient = new Client();
        newClient.setId(id);
        newClient.setName(newName);

        JSONObject requestObject = new JSONObject();
        requestObject.put("id", id);
        requestObject.put("name", newName);

        when(clientRepository.findById(eq(id))).thenReturn(Optional.of(oldClient));
        when(clientRepository.findById(eq(wrongId))).thenReturn(Optional.empty());
        when(clientRepository.save(eq(newClient))).thenReturn(newClient);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/client")
                        .content(requestObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(newName));

        requestObject.put("id", wrongId);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/client")
                        .content(requestObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void deleteClient() {
        Long id = 1L;
        ClientRepository mockRepository = mock(ClientRepository.class);
        doNothing().when(mockRepository).deleteById(id);
        ClientService service = new ClientService(mockRepository);
        service.deleteClient(id);
        Mockito.verify(mockRepository, Mockito.times(1)).deleteById(id);
    }



}
