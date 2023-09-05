package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Client;
import com.example.shelter_bot.exceptions.ClientNotFoundException;
import com.example.shelter_bot.exceptions.PetNotFoundException;
import com.example.shelter_bot.repository.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository repository;
    @InjectMocks
    private ClientService service;

    private final Long testId = 1L;
    Client testClient1 = new Client(1L, "Иванов Иван Иваночик", "8-800-000-00-11", "адрес1", 546515L);
    Client testClient2 = new Client(2L, "Петров Петр Петрович", "8-800-000-00-22", "адрес2", 6546845L);
    Client testClient3 = new Client(3L, "Васильев Василий Васильевич", "8-800-000-00-33", "адрес3", 5612646L);



    @Test
    public void testCreateClient() {
        Mockito.when(repository.save(testClient1)).thenReturn(testClient1);
        Client createClient = service.createClient(testClient1);
        Mockito.verify(repository, Mockito.times(1)).save(testClient1);
        Assertions.assertEquals(testClient1.getId(), createClient.getId());
        Assertions.assertEquals(testClient1.getName(), createClient.getName());
        Assertions.assertEquals(testClient1.getAddress(), createClient.getAddress());
        Assertions.assertEquals(testClient1.getPhoneNumber(), createClient.getPhoneNumber());
    }

    @Test
    public void testGetClientById() {
        Mockito.when(repository.findById(testId)).thenReturn(Optional.of(testClient1));
        Client result = service.getClientById(testId);
        Assertions.assertEquals(testClient1, result);
    }

    @Test
    public void GetClientByIdNotFound() throws ClientNotFoundException {
        Mockito.when(repository.findById(testId)).thenReturn(Optional.empty());
        Assertions.assertThrows(PetNotFoundException.class, () -> service.getClientById(testId));
    }

    @Test
    public void testUpdateClient() {
        Client updateClient1 = service.updateClient(testClient1);
        Client updateClient2 = service.updateClient(testClient2);
        Client updateClient3 = service.updateClient(testClient3);

        Assertions.assertEquals(updateClient1.getId(), Long.valueOf(1L));
        Assertions.assertEquals(updateClient1.getName(), "Иванов Иван Иваночик");
        Assertions.assertEquals(updateClient1.getPhoneNumber(), "8-800-000-00-11");
        Assertions.assertEquals(updateClient1.getAddress(), "адрес1");

        Assertions.assertEquals(updateClient2.getId(), Long.valueOf(2L));
        Assertions.assertEquals(updateClient2.getName(), "Петров Петр Петрович");
        Assertions.assertEquals(updateClient2.getPhoneNumber(), "8-800-000-00-22");
        Assertions.assertEquals(updateClient2.getAddress(), "адрес2");

        Assertions.assertEquals(updateClient3.getId(), Long.valueOf(3L));
        Assertions.assertEquals(updateClient3.getName(), "Васильев Василий Васильевич");
        Assertions.assertEquals(updateClient3.getPhoneNumber(), "8-800-000-00-33");
        Assertions.assertEquals(updateClient3.getAddress(), "адрес3");

        Client extraClient = new Client();
        Assertions.assertThrows(ClientNotFoundException.class, () -> service.updateClient(extraClient));

    }
}
