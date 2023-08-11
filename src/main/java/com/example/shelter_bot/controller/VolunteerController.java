package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Volunteer;
import com.example.shelter_bot.service.VolunteerService;
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

    @ManagedOperation(description = "Получить полный список волонтеров")
    @GetMapping("")
    public ResponseEntity<List<Volunteer>> getAllVolunteers() {
        return ResponseEntity.ok(volunteerService.getAllVolunteers());
    }

    @ManagedOperation(description = "Получить полный список свободных волонтеров")
    @GetMapping("/free")
    public ResponseEntity<List<Volunteer>> getAllAvailableVolunteers() {
        return ResponseEntity.ok(volunteerService.getAllAvailableVolunteers());
    }

    @ManagedOperation(description = "Добавить волонтера в базу данных приюта")
    @PostMapping("")
    public ResponseEntity<Volunteer> addNewVolunteer(Volunteer volunteer) {
        return ResponseEntity.ok(volunteerService.addVolunteer(volunteer));
    }

    @ManagedOperation(description = "Изменить информацию о волонтере")
    @PutMapping("")
    public Volunteer updateVolunteerInfo(Volunteer volunteer) {
        return volunteerService.updateVolunteer(volunteer);
    }

    @ManagedOperation(description = "Удалить волонтера из базы данных приюта")
    @DeleteMapping("/{id}")
    public void deleteVolunteerInfo(@PathVariable long id) {
        volunteerService.removeVolunteer(id);
    }


}
