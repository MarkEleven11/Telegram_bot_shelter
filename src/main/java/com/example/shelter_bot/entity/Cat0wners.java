package com.example.shelter_bot.entity;

import liquibase.pro.packaged.S;

import javax.persistence.*;

@Entity
@Table(name = "owners")
public class Cat0wners {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerChatId;

    @Column(name = "name")
    private String catOwnerName;

    @Column(name = "pet_name")
    private String petName;

    public Cat0wners(Long id, Long ownerChatId, String catOwnerName, String petName) {
        this.id = id;
        this.ownerChatId = ownerChatId;
        this.catOwnerName = catOwnerName;
        this.petName = petName;
    }

    public Cat0wners() {
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

    public String getCatOwnerName() {
        return catOwnerName;
    }

    public void setCatOwnerName(String catOwnerName) {
        this.catOwnerName = catOwnerName;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }
}
