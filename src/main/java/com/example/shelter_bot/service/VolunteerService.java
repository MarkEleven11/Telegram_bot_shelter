package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Shelter;
import com.example.shelter_bot.entity.Volunteer;
import com.example.shelter_bot.exceptions.VolunteerNotFoundException;
import com.example.shelter_bot.repository.VolunteerRepository;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    public Volunteer getVolunteerById(long id) {
        return volunteerRepository.findById(id).orElse(null);
    }

    public Volunteer addVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    public Volunteer updateVolunteer(Volunteer volunteer) {
        Volunteer findVolunteer = getVolunteerById(volunteer.getId());
        if (findVolunteer == null) {
            throw new VolunteerNotFoundException();
        }
        return volunteerRepository.save(volunteer);
    }

    public void removeVolunteer(long id) {
        volunteerRepository.deleteById(id);
    }

    //Получить список свободных волонтеров
    public List<Volunteer> getAllAvailableVolunteers() {
        return volunteerRepository.findVolunteerByAvailableTrue();
    }

    //Метод вызова волонтера
    public SendMessage callVolunteer(Long chatId, Shelter shelter) {
        String nickname = volunteerRepository.findAllById(shelter.getId()).stream()
                .map(v -> v.getVolunteerName())
                .findAny()
                .orElseThrow(() -> new VolunteerNotFoundException());

        return new SendMessage(chatId, "Вы можете связаться с волонтёром в Telegram: " + nickname);
    }
}
