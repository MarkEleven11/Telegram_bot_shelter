package com.example.shelter_bot.entity;

import com.example.shelter_bot.enums.PetType;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "pet")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private PetType petType;

    private String petName;

    private boolean availability;

    public Pet(long id, PetType petType, String petName, boolean availability) {
        this.id = id;
        this.petType = petType;
        this.petName = petName;
        this.availability = availability;
    }

    public Pet() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Pet test = (Pet) o;
        return id == test.id && Objects.equals(petType, test.petType) &&
                Objects.equals(petName, test.petName) &&
                Objects.equals(availability, test.availability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, petType, petName, availability);
    }
}
