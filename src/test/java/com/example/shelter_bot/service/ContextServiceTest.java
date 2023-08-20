package com.example.shelter_bot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

public class ContextServiceTest {

    @ExtendWith(MockitoExtension.class)
    public class ContextServiceTest {
        @Mock
        private ContextRepository repository;
        @InjectMocks
        private ContextService service;

        @Test
        public void testSaveContext() {
            Long testId = 1L;
            Context context = new Context(testId, ShelterType.DOG);
            when(repository.save(context)).thenReturn(context);
            Context saveContext = service.saveContext(context);
            Mockito.verify(repository, Mockito.times(1)).save(context);
            Assertions.assertEquals(context.getChatId(), saveContext.getChatId());
            Assertions.assertEquals(context.getShelterType(), saveContext.getShelterType());
            Assertions.assertEquals(context.getDogOwner(), saveContext.getDogOwner());
            Assertions.assertEquals(context.getCatOwner(), saveContext.getCatOwner());
        }

        @Test
        public void testGetByChatId() {
            Optional<Context> context = Optional.of(new Context(1L, ShelterType.DOG));
            when(repository.findByChatId(anyLong())).thenReturn(context);
            repository.findByChatId(context.get().getChatId());
            assertNotNull(context);
            Assertions.assertEquals(context.get(), context.get());
        }

        @Test
        public void testGetAll() {
            List<Context> contextList = new ArrayList<>();
            Context context22 = new Context(1L, ShelterType.DOG);
            contextList.add(context22);
            Context context23 = new Context(2L, ShelterType.CAT);
            contextList.add(context23);
            Mockito.when(repository.findAll()).thenReturn(contextList);
            Collection<Context> result = service.getAll();
            Assertions.assertEquals(contextList, result);
            Mockito.verify(repository, Mockito.times(1)).findAll();
        }
    }
}
