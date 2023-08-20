package com.example.shelter_bot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DogServiceTest {
    @ExtendWith(MockitoExtension.class)
    class DogServiceTest {
        @Mock
        private DogRepository repository;
        @InjectMocks
        private DogService service;

        @Test
        public void testGetById() {
            Long testId = 1L;
            Dog dog = new Dog(testId, "Собака", "Порода", 2020, "Описание");
            Mockito.when(repository.findById(testId)).thenReturn(Optional.of(dog));
            Dog result = service.getById(testId);
            Assertions.assertEquals(dog, result);
        }

        @Test
        public void testCreateDog() {
            Long testId = 1L;
            Dog dog = new Dog(testId, "Собака", "Порода", 2020, "Описание");
            Mockito.when(repository.save(dog)).thenReturn(dog);
            Dog createdDog = service.addDog(dog);
            Mockito.verify(repository, Mockito.times(1)).save(dog);
            Assertions.assertEquals(dog.getName(), createdDog.getName());
            Assertions.assertEquals(dog.getId(), createdDog.getId());
        }

        @Test
        public void testUpdateDog() {
            Dog dog1 = new Dog();
            dog1.setName("Собака1");
            dog1.setBreed("Порода1");
            dog1.setYearOfBirth(2021);
            dog1.setDescription("Описание1");
            dog1.setId(1L);
            Dog dog2 = new Dog();
            dog2.setName("Собака2");
            dog2.setBreed("Порода2");
            dog2.setYearOfBirth(2022);
            dog2.setDescription("Описание2");
            dog2.setId(2L);
            Dog dog3 = new Dog();
            dog3.setName("Собака3");
            dog3.setBreed("Порода3");
            dog3.setYearOfBirth(2020);
            dog3.setDescription("Описание3");
            dog3.setId(3L);
            Mockito.when(repository.getById(1L)).thenReturn(dog1);
            Dog updatedDog1 = service.update(dog1);
            Dog updatedDog2 = service.update(dog2);
            Dog updatedDog3 = service.update(dog3);
            Assertions.assertEquals(updatedDog1.getName(), "Собака1");
            Assertions.assertEquals(updatedDog1.getBreed(), "Порода1");
            Assertions.assertEquals(updatedDog1.getYearOfBirth(), 2021);
            Assertions.assertEquals(updatedDog1.getDescription(), "Описание1");
            Assertions.assertEquals(updatedDog1.getId(), Long.valueOf(1L));
            Assertions.assertEquals(updatedDog2.getName(), "Собака2");
            Assertions.assertEquals(updatedDog2.getBreed(), "Порода2");
            Assertions.assertEquals(updatedDog2.getYearOfBirth(), 2022);
            Assertions.assertEquals(updatedDog2.getDescription(), "Описание2");
            Assertions.assertEquals(updatedDog2.getId(), Long.valueOf(2L));
            Assertions.assertEquals(updatedDog3.getName(), "Собака3");
            Assertions.assertEquals(updatedDog3.getBreed(), "Порода3");
            Assertions.assertEquals(updatedDog3.getYearOfBirth(), 2020);
            Assertions.assertEquals(updatedDog3.getDescription(), "Описание3");
            Assertions.assertEquals(updatedDog3.getId(), Long.valueOf(3L));
            Dog dog4 = new Dog();
            Assertions.assertThrows(DogNotFoundException.class, () -> {
                service.update(dog4);
            });
        }
    }
}
