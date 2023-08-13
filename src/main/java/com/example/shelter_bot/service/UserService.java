package com.example.shelter_bot.service;

import com.example.shelter_bot.repository.UserRepository;
import com.example.shelter_bot.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User writeContact(User user) {
        userRepository.save(user);
        return user;
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }
}
