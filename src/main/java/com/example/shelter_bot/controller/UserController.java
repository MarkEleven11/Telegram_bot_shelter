package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.Client;
import com.example.shelter_bot.entity.User;
import com.example.shelter_bot.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if (user.getName() == null || user.getPhoneNumber() == null ||
                user.getShelter() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.writeContact(user));
    }

    @Operation(summary = "Получить данные о пользователе по ID", tags = "User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о пользователе получена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))})
    })
    @GetMapping("/get")
    public ResponseEntity<Optional<User>> getUser(@RequestParam (name = "id") Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }
}
