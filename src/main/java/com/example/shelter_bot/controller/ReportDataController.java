package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Pet;
import com.example.shelter_bot.entity.ReportData;
import com.example.shelter_bot.service.ReportDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Получить фотоотчет из базы данных", tags = "Report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчет получен",
                    content = {@Content(mediaType = fileType,
                            schema = @Schema(implementation = ReportData.class))})
    })
    @GetMapping("/{id}/photo-from-db")
    public ResponseEntity<byte[]> downloadPhotoFromDB(@Parameter (description = "report id") @PathVariable Long id) {
        ReportData reportData = this.reportDataService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileType));
        headers.setContentLength(reportData.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(reportData.getData());
    }

    @Operation(summary = "Получить полный отчет из базы данных", tags = "Report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчет получен",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportData.class))})
    })
    @GetMapping("/{id}")
    public ReportData downloadReport(@Parameter(description = "report id") @PathVariable Long chatId) {
        return this.reportDataService.findByChatId(chatId);
    }

    @Operation(summary = "Получить все отчеты из базы данных", tags = "Report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция выполнена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportData.class))})
    })
    @GetMapping("/getAll")
    public ResponseEntity<Collection<ReportData>> getAll() {
        return ResponseEntity.ok(this.reportDataService.getAll());
    }

    @Operation(summary = "Удалить отчет из базы данных", tags = "Report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчет удален",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportData.class))})
    })
    @DeleteMapping("/{id}")
    public void remove(@Parameter (description = "report id") @PathVariable Long id) {
        this.reportDataService.remove(id);
    }

}