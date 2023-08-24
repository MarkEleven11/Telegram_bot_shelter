package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Shelter;
import com.example.shelter_bot.enums.PetType;
import com.example.shelter_bot.repository.ShelterRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ShelterServiceTest {

    @Mock
    private ShelterRepository repository;
    @InjectMocks
    private ShelterServiceImpl service;

    private final Long testId = 1L;
    Shelter testShelter = new Shelter(1L, "Название приюта", "адрес",
            "10:00-22:00", "инфо", "на посту охраны 5 человек",
            "location", PetType.DOG);
    @Test
    public void testAboutShelter() {
        PetType testType = PetType.DOG;
        Assertions.assertThat(testType).isNotNull();
        Shelter result = repository.findShelterByPetTypeIs(testType);
        Assertions.assertThat(result.getAbout()).isNotNull();
    }

    @Test
    public void testGiveMenu() {
        Assertions.assertThat(service.start(testShelter, testId)).isNotNull();
    }

    @Test
    public void testStart() {
        Assertions.assertThat(service.start(testShelter, testId)).isNotNull();
    }

    @Test
    public void testChooseShelter() {
        PetType testTypeExists = PetType.DOG;
        service.saveShelterToRepository(testShelter);
        Assertions.assertThat(service.chooseShelter(testTypeExists)).isNotNull();
        //Assertions.assertThat(service.chooseShelter(PetType.CAT)).isNull();
    }

    @Test
    public void testSaveShelterToRepository() {
        Mockito.when(repository.save(testShelter)).thenReturn(testShelter);
        Shelter createShelter = service.saveShelterToRepository(testShelter);
        Mockito.verify(repository, Mockito.times(1)).save(testShelter);
        org.junit.jupiter.api.Assertions.assertEquals(testShelter.getId(), createShelter.getId());
        org.junit.jupiter.api.Assertions.assertEquals(testShelter.getName(), createShelter.getName());
        org.junit.jupiter.api.Assertions.assertEquals(testShelter.getAddress(), createShelter.getAddress());
        org.junit.jupiter.api.Assertions.assertEquals(testShelter.getAbout(), createShelter.getAbout());
    }

    @Test
    public void testFindShelter() {
        Mockito.when(repository.findById(testId)).thenReturn(Optional.of(testShelter));
        Optional<Shelter> resultShelter = service.findShelter(testId);
        Assertions.assertThat(resultShelter).isNotNull();
    }
}
