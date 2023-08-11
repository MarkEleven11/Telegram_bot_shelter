package com.example.shelter_bot.service;

import com.example.shelter_bot.entity.Volunteer;
import com.example.shelter_bot.exceptions.VolunteerNotFoundException;
import com.example.shelter_bot.repository.VolunteerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

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
    public Volunteer callVolunteer(long chatId) {
        Long volunteerId = getVolunteerById(chatId).getChatId();
        if (volunteerId != null) {
            return getVolunteerById(volunteerId);
        } else {
            List<Volunteer> list = getAllAvailableVolunteers();
            if(list.size() > 0) {
                Random random = new Random();
                return list.get(random.nextInt(list.size()));
            } else {
                throw new VolunteerNotFoundException();
            }
        }
    }
}
