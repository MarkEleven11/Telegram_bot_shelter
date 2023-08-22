package com.example.shelter_bot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class CatServiceTest {
    @ExtendWith(MockitoExtension.class)
    public class CatServiceTest {

        private final Long testId = 1L;
        Cat testCat1 = new Cat(testId, "CatTest1", "CatBreedTest1", 2015, "descriptionTest1");
        Cat testCat2 = new Cat(2L, "CatTest2", "CatBreedTest2", 2016, "descriptionTest2");
        Cat testCat3 = new Cat(3L, "CatTest3", "CatBreedTest3", 2017, "descriptionTest3");
        @Mock
        private CatRepository repository;
        @InjectMocks
        private CatService service;

        @Test
        public void testGetById() {
            Mockito.when(repository.findById(testId)).thenReturn(Optional.of(testCat1));
            Cat result = service.getById(testId);
            Assertions.assertEquals(testCat1, result);
        }

        @Test
        public void testGetByIdNotFound() throws CatNotFoundException {
            Mockito.when(repository.findById(testId)).thenReturn(Optional.empty());
            Assertions.assertThrows(CatNotFoundException.class, () -> service.getById(testId));
        }

        @Test
        public void testCreateCat() {
            Mockito.when(repository.save(testCat1)).thenReturn(testCat1);
            Cat createdCat = service.addCat(testCat1);
            Mockito.verify(repository, Mockito.times(1)).save(testCat1);
            Assertions.assertEquals(testCat1.getId(), createdCat.getId());
            Assertions.assertEquals(testCat1.getName(), createdCat.getName());
            Assertions.assertEquals(testCat1.getBreed(), createdCat.getBreed());
            Assertions.assertEquals(testCat1.getYearOfBirth(), createdCat.getYearOfBirth());
        }

        @Test
        public void testUpdateCat() {

            //       Mockito.when(repository.getById(1L)).thenReturn(testCat1);
            Cat updatedCat1 = service.update(testCat1);
            Cat updatedCat2 = service.update(testCat2);
            Cat updatedCat3 = service.update(testCat3);

            Assertions.assertEquals(updatedCat1.getId(), Long.valueOf(1L));
            Assertions.assertEquals(updatedCat1.getName(), "CatTest1");
            Assertions.assertEquals(updatedCat1.getBreed(), "CatBreedTest1");
            Assertions.assertEquals(updatedCat1.getYearOfBirth(), 2015);
            Assertions.assertEquals(updatedCat1.getDescription(), "descriptionTest1");

            Assertions.assertEquals(updatedCat2.getId(), Long.valueOf(2L));
            Assertions.assertEquals(updatedCat2.getName(), "CatTest2");
            Assertions.assertEquals(updatedCat2.getBreed(), "CatBreedTest2");
            Assertions.assertEquals(updatedCat2.getYearOfBirth(), 2016);
            Assertions.assertEquals(updatedCat2.getDescription(), "descriptionTest2");

            Assertions.assertEquals(updatedCat3.getId(), Long.valueOf(3L));
            Assertions.assertEquals(updatedCat3.getName(), "CatTest3");
            Assertions.assertEquals(updatedCat3.getBreed(), "CatBreedTest3");
            Assertions.assertEquals(updatedCat3.getYearOfBirth(), 2017);
            Assertions.assertEquals(updatedCat3.getDescription(), "descriptionTest3");
            Cat cat4 = new Cat();
            Assertions.assertThrows(CatNotFoundException.class, () -> service.update(cat4));
        }
    }
}
