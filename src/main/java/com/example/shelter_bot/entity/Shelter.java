package com.example.shelter_bot.entity;

import com.example.shelter_bot.enums.PetType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "shelter")
@Getter
@Setter
public class Shelter {
    @Id
    private Long id;
    @Column(nullable = false)
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
    @ManyToOne
    @JoinColumn(name = "pet_type_id")
    private PetType petType;
    @OneToMany(mappedBy = "shelter")
    private Set<Volunteer> volunteerSet = new HashSet<>();
    @OneToMany(mappedBy = "shelter")
    private Set<PetOwners> ownerSet = new HashSet<>();
}