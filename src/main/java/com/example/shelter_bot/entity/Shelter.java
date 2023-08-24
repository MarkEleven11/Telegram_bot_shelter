package com.example.shelter_bot.entity;

import com.example.shelter_bot.enums.PetType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "shelter")
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_shelter", nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String schedule;
    @Column(nullable = false)
    private String about;
    private String guard;
    @Column(name = "location_map")
    private String locationMap;
    private PetType petType;
    @OneToMany(mappedBy = "shelter")
    private Set<Volunteer> volunteerSet = new HashSet<>();
    @OneToMany(mappedBy = "shelter")
    private Set<User> ownerSet = new HashSet<>();

    public Shelter(Long id, String name, String address, String schedule, String about, String guard, String locationMap, PetType petType) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.schedule = schedule;
        this.about = about;
        this.guard = guard;
        this.locationMap = locationMap;
        this.petType = petType;
    }

    public Shelter(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getGuard() {
        return guard;
    }

    public void setGuard(String guard) {
        this.guard = guard;
    }

    public String getLocationMap() {
        return locationMap;
    }

    public void setLocationMap(String locationMap) {
        this.locationMap = locationMap;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public Set<Volunteer> getVolunteerSet() {
        return volunteerSet;
    }

    public void setVolunteerSet(Set<Volunteer> volunteerSet) {
        this.volunteerSet = volunteerSet;
    }

    public Set<User> getOwnerSet() {
        return ownerSet;
    }

    public void setOwnerSet(Set<User> ownerSet) {
        this.ownerSet = ownerSet;
    }
}