package com.example.shelter_bot.controller;

import com.example.shelter_bot.entity.User;
import com.example.shelter_bot.service.UserService;
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

    @GetMapping("/get")
    public ResponseEntity<Optional<User>> getUser(@RequestParam (name = "id") Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }
}
