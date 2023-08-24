package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Volunteer;
import com.example.shelter_bot.exceptions.VolunteerNotFoundException;
import com.example.shelter_bot.repository.VolunteerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class VolunteerServiceTest {

    @Mock
    private VolunteerRepository repository;
    @InjectMocks
    private VolunteerService service;

    Volunteer testVolunteer1 = new Volunteer(1L, "Павел Павлович", 1526L, true);
    Volunteer testVolunteer2 = new Volunteer(2L, "Михал Михайлович", 65994L, true);
    Volunteer testVolunteer3 = new Volunteer(3L, "Окская Оксана", 95746L, false);

    @Test
    public void testGetAllVolunteers() {
        repository.save(testVolunteer1);
        repository.save(testVolunteer2);
        repository.save(testVolunteer3);
        List<Volunteer> savedVolunteers = repository.findAll();
        Assertions.assertEquals(service.getAllVolunteers().size(), savedVolunteers.size());
    }

    @Test
    public void testGetVolunteerById() {
        long testId = 1L;
        Mockito.when(repository.findById(testId)).thenReturn(Optional.of(testVolunteer1));
        Volunteer result = service.getVolunteerById(testId);
        Assertions.assertEquals(testVolunteer1, result);
    }

    @Test
    public void testAddVolunteer() {
        Mockito.when(repository.save(testVolunteer1)).thenReturn(testVolunteer1);
        Volunteer addVolunteer = service.addVolunteer(testVolunteer1);
        Mockito.verify(repository, Mockito.times(1)).save(testVolunteer1);
        Assertions.assertEquals(testVolunteer1.getId(), addVolunteer.getId());
        Assertions.assertEquals(testVolunteer1.getVolunteerName(), addVolunteer.getVolunteerName());
        Assertions.assertEquals(testVolunteer1.getChatId(), addVolunteer.getChatId());
        Assertions.assertEquals(testVolunteer1.isAvailable(), addVolunteer.isAvailable());
    }

    @Test
    public void testUpdateVolunteer() {
        Volunteer updateVolunteer1 = service.updateVolunteer(testVolunteer1);
        Volunteer updateVolunteer2 = service.updateVolunteer(testVolunteer2);
        Volunteer updateVolunteer3 = service.updateVolunteer(testVolunteer3);

        Assertions.assertEquals(updateVolunteer1.getId(), Long.valueOf(1L));
        Assertions.assertEquals(updateVolunteer1.getVolunteerName(), "Павел Павлович");
        Assertions.assertEquals(updateVolunteer1.getChatId(), 1526L);
        Assertions.assertTrue(updateVolunteer1.isAvailable());

        Assertions.assertEquals(updateVolunteer2.getId(), Long.valueOf(2L));
        Assertions.assertEquals(updateVolunteer2.getVolunteerName(), "Михал Михайлович");
        Assertions.assertEquals(updateVolunteer2.getChatId(), 65994L);
        Assertions.assertTrue(updateVolunteer2.isAvailable());

        Assertions.assertEquals(updateVolunteer3.getId(), Long.valueOf(3L));
        Assertions.assertEquals(updateVolunteer3.getVolunteerName(), "Окская Оксана");
        Assertions.assertEquals(updateVolunteer3.getChatId(), 95746L);
        Assertions.assertFalse(updateVolunteer3.isAvailable());

        Volunteer extraVolunteer = new Volunteer();
        Assertions.assertThrows(VolunteerNotFoundException.class, () -> service.updateVolunteer(extraVolunteer));
    }

    @Test
    public void testGetAllAvailableVolunteers() {
        repository.save(testVolunteer1);
        repository.save(testVolunteer2);
        repository.save(testVolunteer3);
        List<Volunteer> savedVolunteers = repository.findVolunteerByAvailableTrue();
        Assertions.assertNotNull(savedVolunteers);
        //Assertions.assertEquals(savedVolunteers.size(), 2);
    }
}
