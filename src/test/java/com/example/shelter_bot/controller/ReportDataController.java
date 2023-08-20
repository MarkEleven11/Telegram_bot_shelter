package com.example.shelter_bot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ReportDataController {

    @WebMvcTest(ReportDataController.class)
    class ReportDataControllerTest {
        @Autowired
        private MockMvc mockMvc;
        @MockBean
        private ReportDataService service;

        @Test
        void downloadReport() throws Exception {
            ReportData reportData = new ReportData();
            reportData.setId(1L);
            service.save(reportData);
            when(service.findById(anyLong())).thenReturn(reportData);
            mockMvc.perform(
                            get("/photo-reports/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
            verify(service).findById(1L);
        }

        @Test
        void remove() throws Exception {
            mockMvc.perform(
                            delete("/photo-reports/{id}", 1))
                    .andExpect(status().isOk());
            verify(service).remove(1L);
        }

        @Test
        void getAll() throws Exception {
            when(service.getAll()).thenReturn(List.of(new ReportData()));
            mockMvc.perform(
                            get("/photo-reports/getAll"))
                    .andExpect(status().isOk());
        }

        @Test
        void downloadPhotoFromDB() throws Exception {
        }
    }
}
