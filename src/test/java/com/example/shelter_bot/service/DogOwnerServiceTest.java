package com.example.shelter_bot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

public class DogOwnerServiceTest {

    @ExtendWith(MockitoExtension.class)
    class DogOwnerServiceTest {
        @Mock
        private DogOwnerRepository repository;
        @InjectMocks
        private DogOwnerService service;
        @Test
        public void testGetById() {
            Long testId = 1L;
            DogOwner dogOwner = new DogOwner(1L, "Vasya", 321L);
            Mockito.when(repository.findById(testId)).thenReturn(Optional.of(dogOwner));
            DogOwner result = service.getById(testId);
            Assertions.assertEquals(dogOwner, result);
        }

        @Test
        public void testGetByChatId() {
            Long testChatId = 5456L;
            DogOwner dogOwner = new DogOwner(1L, "Vasya", testChatId);
            Set<DogOwner> dogOwnerSet = new HashSet<>();
            dogOwnerSet.add(dogOwner);
            Mockito.when(repository.getDogOwnerByChatId(testChatId)).thenReturn(dogOwnerSet);
            Set<DogOwner> result = service.getByChatId(testChatId);
            Assertions.assertEquals(dogOwnerSet, result);
        }

        @Test
        public void testGetByIdNotFound() throws DogOwnerNotFoundException {
            Long testId = 1L;
            Mockito.when(repository.findById(testId)).thenReturn(Optional.empty());
            Assertions.assertThrows(DogOwnerNotFoundException.class, () -> service.getById(testId));
        }

        @Test
        public void testUpdateDogOwners() {
            DogOwner dogOwner1 = new DogOwner();
            dogOwner1.setName("Vasya");
            dogOwner1.setId(1L);
            DogOwner dogOwner2 = new DogOwner();
            dogOwner2.setName("Volodya");
            dogOwner2.setId(2L);
            DogOwner dogOwner3 = new DogOwner();
            dogOwner3.setName("Bob");
            dogOwner3.setId(3L);
            DogOwner updatedDogOwner1 = service.save(dogOwner1);
            DogOwner updatedDogOwner2 = service.save(dogOwner2);
            DogOwner updatedDogOwner3 = service.save(dogOwner3);
            Assertions.assertEquals(updatedDogOwner1.getName(), "Vasya");
            Assertions.assertEquals(updatedDogOwner1.getId(), Long.valueOf(1L));
            Assertions.assertEquals(updatedDogOwner2.getName(), "Volodya");
            Assertions.assertEquals(updatedDogOwner2.getId(), Long.valueOf(2L));
            Assertions.assertEquals(updatedDogOwner3.getName(), "Bob");
            Assertions.assertEquals(updatedDogOwner3.getId(), Long.valueOf(3L));
        }
    }
}
