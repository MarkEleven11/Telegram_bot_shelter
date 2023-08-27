package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Client;
import com.example.shelter_bot.entity.Context;
import com.example.shelter_bot.enums.PetType;
import com.example.shelter_bot.exceptions.ClientNotFoundException;
import com.example.shelter_bot.exceptions.PetNotFoundException;
import com.example.shelter_bot.repository.ContextRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

public class ContextServiceTest {

    @Mock
    ContextRepository repository;

    @InjectMocks
    ContextService service;


    Context contextMock;

    private final Long testId = 1L;
    Client testClient = new Client(1L, "Иванов Иван Иваночик", "8-800-000-00-11", "адрес1");
    Context testContext = new Context(1L, PetType.DOG, testClient);

    @Test
    public void testSaveContext() {
        Mockito.when(repository.save(testContext)).thenReturn(testContext);
        Context createContext = service.saveContext(testContext);
        Mockito.verify(repository, Mockito.times(1)).save(testContext);
        Assertions.assertEquals(testContext.getChatId(), createContext.getChatId());
        Assertions.assertEquals(testContext.getPetType(), createContext.getPetType());
        Assertions.assertEquals(testContext.getClient(), createContext.getClient());
    }

    @Test
    public void testGetContextById() {
        Mockito.when(repository.findById(testId)).thenReturn(Optional.of(testContext));
        Optional<Context> result = service.getByChatId(testId);
        Assertions.assertEquals(testContext, result);
    }

    @Test
    public void GetClientByIdNotFound() throws ClientNotFoundException {
        Mockito.when(repository.findById(testId)).thenReturn(Optional.empty());
        Assertions.assertThrows(PetNotFoundException.class, () -> service.getByChatId(testId));
    }
}
