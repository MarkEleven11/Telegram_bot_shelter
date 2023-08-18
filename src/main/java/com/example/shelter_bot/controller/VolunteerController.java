package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Pet;
import com.example.shelter_bot.entity.Volunteer;
import com.example.shelter_bot.service.VolunteerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/volunteers")
public class VolunteerController {

    private VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @Operation(summary = "Получить полный список волонтеров", tags = "Volunteer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список всех волонтеров",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Volunteer.class))})
    })
    @GetMapping("")
    public ResponseEntity<List<Volunteer>> getAllVolunteers() {
        return ResponseEntity.ok(volunteerService.getAllVolunteers());
    }

    @Operation(summary = "Получить полный список свободных волонтеров", tags = "Volunteer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список всех свободных волонтеров",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Volunteer.class))})
    })
    @GetMapping("/free")
    public ResponseEntity<List<Volunteer>> getAllAvailableVolunteers() {
        return ResponseEntity.ok(volunteerService.getAllAvailableVolunteers());
    }

    @Operation(summary = "Добавить волонтера в базу данных приюта", tags = "Volunteer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные о волонтере добавлены",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Volunteer.class))})
    })
    @PostMapping("")
    public ResponseEntity<Volunteer> addNewVolunteer(Volunteer volunteer) {
        return ResponseEntity.ok(volunteerService.addVolunteer(volunteer));
    }
    @Operation(summary = "Изменить информацию о волонтере", tags = "Volunteer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные о волонтере изменены",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Volunteer.class))})
    })
    @PutMapping("")
    public Volunteer updateVolunteerInfo(Volunteer volunteer) {
        return volunteerService.updateVolunteer(volunteer);
    }

    @Operation(summary = "Удалить волонтера из базы данных приюта", tags = "Volunteer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Волонтер удален из базы",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Volunteer.class))})
    })
    @DeleteMapping("/{id}")
    public void deleteVolunteerInfo(@PathVariable long id) {
        volunteerService.removeVolunteer(id);
    }


}
