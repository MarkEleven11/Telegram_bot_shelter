import Services.ShelterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.entity.Shelter;

import java.util.Optional;

@RestController
@RequestMapping("/shelter")
@Tag(name = "Приюты", description = "Операции создания записей о новых приютах и получения данных о них")
public class ShelterController {

    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @PostMapping("/add")
    public ResponseEntity<pro.sky.entity.Shelter> addShelter(@RequestBody Shelter shelter) {
        if (shelter.getName() == null || shelter.getAddress() == null ||
                shelter.getSchedule() == null || shelter.getAbout() == null ||
                shelter.getLocationMap() == null || shelter.getPetType() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(shelterService.saveShelterToRepository(shelter));
    }

    @GetMapping("/get")
    public ResponseEntity<Optional<Shelter>> geShelter(@RequestParam (name = "id") Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Shelter> shelter = shelterService.findShelter(id);
        return ResponseEntity.ok(shelter);
    }
}
