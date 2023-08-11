package com.example.shelter_bot.repository;

import com.example.shelter_bot.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    List<Volunteer> findVolunteerByAvailableTrue();

}
