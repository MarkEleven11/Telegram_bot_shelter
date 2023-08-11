package com.example.shelter_bot.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "owners")
public class PetOwners {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerChatId;

    @Column(name = "name")
    private String petOwnerName;

    @Column(name = "pet_name")
    private String petName;

    public PetOwners(Long id, Long ownerChatId, String petOwnerName, String petName) {
        this.id = id;
        this.ownerChatId = ownerChatId;
        this.petOwnerName = petOwnerName;
        this.petName = petName;
    }

    public PetOwners() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    public Long getOwnerChatId() {
        return ownerChatId;
    }

    public void setOwnerChatId(Long ownerChatId) {
        this.ownerChatId = ownerChatId;
    }

    public String getPetOwnerName() {
        return petOwnerName;
    }

    public void setPetOwnerName(String petOwnerName) {
        this.petOwnerName = petOwnerName;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o) return  false;
        PetOwners test = (PetOwners) o;
        return id.equals(id) &&
                Objects.equals(ownerChatId, test.getOwnerChatId()) &&
                Objects.equals(petOwnerName, test.getPetOwnerName()) &&
                Objects.equals(petName, test.getPetName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerChatId, petOwnerName, petName);
    }
}
