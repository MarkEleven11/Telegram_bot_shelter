package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Pet;
import com.example.shelter_bot.enums.PetType;
import com.example.shelter_bot.exceptions.PetNotFoundException;
import com.example.shelter_bot.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository repository;
    @InjectMocks
    private PetService service;

    private final Long testId = 1L;
    Pet testPet1 = new Pet(testId, PetType.CAT, "Имя кота", true);
    Pet testPet2 = new Pet(2L, PetType.DOG, "Имя собаки", true);
    Pet testPet3 = new Pet(3L, PetType.CAT, "Имя кота 2", true);

    @Test
    public void testGetPetById() {
        Mockito.when(repository.findById(testId)).thenReturn(Optional.of(testPet1));
        Pet result = service.getPetById(testId);
        Assertions.assertEquals(testPet1, result);
    }

    @Test
    public void testGetPetByIdNotFound() throws PetNotFoundException {
        Mockito.when(repository.findById(testId)).thenReturn(Optional.empty());
        Assertions.assertThrows(PetNotFoundException.class, () -> service.getPetById(testId));
    }

    @Test
    public void testAddPet() {
        Mockito.when(repository.save(testPet1)).thenReturn(testPet1);
        Pet addPet = service.addPet(testPet1);
        Mockito.verify(repository, Mockito.times(1)).save(testPet1);
        Assertions.assertEquals(testPet1.getId(), addPet.getId());
        Assertions.assertEquals(testPet1.getPetType(), addPet.getPetType());
        Assertions.assertEquals(testPet1.getPetName(), addPet.getPetName());
        Assertions.assertEquals(testPet1.isAvailability(), addPet.isAvailability());
    }

    @Test
    public void testUpdateCat() {
        Pet updatePet1 = service.updatePet(testPet1);
        Pet updatePet2 = service.updatePet(testPet2);
        Pet updatePet3 = service.updatePet(testPet3);

        Assertions.assertEquals(updatePet1.getId(), Long.valueOf(1L));
        Assertions.assertEquals(updatePet1.getPetType(), PetType.CAT);
        Assertions.assertEquals(updatePet1.getPetName(), "Имя кота");
        Assertions.assertTrue(updatePet1.isAvailability());

        Assertions.assertEquals(updatePet2.getId(), Long.valueOf(2L));
        Assertions.assertEquals(updatePet2.getPetType(), PetType.DOG);
        Assertions.assertEquals(updatePet2.getPetName(), "Имя собаки");
        Assertions.assertTrue(updatePet2.isAvailability());

        Assertions.assertEquals(updatePet3.getId(), Long.valueOf(3L));
        Assertions.assertEquals(updatePet3.getPetType(), PetType.CAT);
        Assertions.assertEquals(updatePet3.getPetName(), "Имя кота 2");
        Assertions.assertTrue(updatePet3.isAvailability());

        Pet extraPet = new Pet();
        Assertions.assertThrows(PetNotFoundException.class, () -> service.updatePet(extraPet));
    }
}
