package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.ReportData;
import com.example.shelter_bot.service.ReportDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ReportDataControllerTest {
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
            mockMvc.perform(MockMvcRequestBuilders.get("/photo-reports/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
            verify(service).findById(1L);
        }

        @Test
        void remove() throws Exception {
            mockMvc.perform(delete("/photo-reports/{id}", 1))
                    .andExpect(status().isOk());
            verify(service).remove(1L);
        }

        @Test
        void getAll() throws Exception {
            when(service.getAll()).thenReturn(List.of(new ReportData()));
            mockMvc.perform(MockMvcRequestBuilders.get("/photo-reports/getAll"))
                    .andExpect(status().isOk());
        }
}
