package com.example.shelter_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//фича-ветка пользователя
@SpringBootApplication
@EnableScheduling
public class ShelterBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShelterBotApplication.class, args);
    }

}
