package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Shelter;
import com.example.shelter_bot.entity.User;
import com.example.shelter_bot.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserService service;

    User testUser = new User(1L, "Иванов Иван Иванович", "8-800-555-5-35", new Shelter());

    @Test
    public void testWriteContact() {
        Mockito.when(repository.save(testUser)).thenReturn(testUser);
        User newUser = service.writeContact(testUser);
        Mockito.verify(repository, Mockito.times(1)).save(testUser);
        Assertions.assertEquals(testUser.getName(), newUser.getName());
        Assertions.assertEquals(testUser.getPhoneNumber(), newUser.getPhoneNumber());
        Assertions.assertEquals(testUser.getShelter(), newUser.getShelter());
    }

    @Test
    public void testGetUser() {
        Long testId = 1L;
        Mockito.when(repository.findById(testId)).thenReturn(Optional.of(testUser));
        Optional<User> result = service.getUser(testId);
        Assertions.assertEquals(testUser, result);
    }
}
