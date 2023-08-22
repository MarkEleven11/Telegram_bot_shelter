package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.ReportData;
import com.example.shelter_bot.repository.ReportDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ReportDataServiceTest {
    @Mock
        private ReportDataRepository repository;

        @InjectMocks
        private ReportDataService service;

        @Test
        void findById() {
            long testId = 1;
            ReportData reportData = new ReportData();
            reportData.setId(testId);
            Mockito.when(repository.findById(testId)).thenReturn(Optional.of(reportData));
            ReportData result = service.findById(testId);
            Assertions.assertEquals(reportData, result);
        }

        @Test
        void findByChatId() {
            long testId = 1;
            long testChatId = 3243242;
            ReportData reportData = new ReportData();
            reportData.setId(testId);
            reportData.setChatId(testChatId);
            Mockito.when(repository.findByChatId(testChatId)).thenReturn(reportData);
            ReportData result = service.findByChatId(testChatId);
            Assertions.assertEquals(reportData, result);
        }

        @Test
        void save() {
            long testId = 1;
            ReportData reportData = new ReportData();
            reportData.setId(testId);
            Mockito.when(repository.save(reportData)).thenReturn(reportData);
            ReportData createdReportData = service.save(reportData);
            Mockito.verify(repository, Mockito.times(1)).save(reportData);
            Assertions.assertEquals(reportData.getId(), createdReportData.getId());
        }

        @Test
        void removeById() {
            long testId = 1;
            ReportData reportData = new ReportData();
            reportData.setId(testId);

            ReportDataRepository mockRepository = mock(ReportDataRepository.class);
            doNothing().when(mockRepository).deleteById(testId);
            ReportDataService service = new ReportDataService(mockRepository);
            service.remove(testId);
            Mockito.verify(mockRepository, Mockito.times(1)).deleteById(testId);
        }

        @Test
        void getAll() {
            List<ReportData> reportDataList =  new ArrayList<>();
            ReportData reportData1 = new ReportData();
            reportData1.setId(1L);
            ReportData reportData2 = new ReportData();
            reportData1.setId(2L);
            Mockito.when(repository.findAll()).thenReturn(reportDataList);
            ReportDataService service = new ReportDataService(repository);
            Collection<ReportData> result = service.getAll();
            Assertions.assertEquals(reportDataList, result);
            Mockito.verify(repository, Mockito.times(1)).findAll();
        }

}
