package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.ReportData;
import com.example.shelter_bot.service.ReportDataService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
@RestController
@RequestMapping("photo-reports")
public class ReportDataController {
    private final String fileType = "image/jpeg";
    private final ReportDataService reportDataService;
    public ReportDataController(ReportDataService reportDataService) {
        this.reportDataService = reportDataService;
    }
    @GetMapping("/{id}/photo-from-db")
    public ResponseEntity<byte[]> downloadPhotoFromDB(@Parameter (description = "report id") @PathVariable Long id) {
        ReportData reportData = this.reportDataService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileType));
        headers.setContentLength(reportData.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(reportData.getData());
    }
    @GetMapping("/{id}")
    public ReportData downloadReport(@Parameter(description = "report id") @PathVariable Long id) {
        return this.reportDataService.findById(id);
    }
    @GetMapping("/getAll")
    public ResponseEntity<Collection<ReportData>> getAll() {
        return ResponseEntity.ok(this.reportDataService.getAll());
    }
    @DeleteMapping("/{id}")
    public void remove(@Parameter (description = "report id") @PathVariable Long id) {
        this.reportDataService.remove(id);
    }

}