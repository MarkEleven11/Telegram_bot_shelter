package com.example.shelter_bot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_user", nullable = false)
    private String name;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;
    @Column(name = "user_chat_id")
    private Long userChatId;

    public User(String name, String phoneNumber, Shelter shelter, Long userChatId) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.shelter = shelter;
        this.userChatId = userChatId;
    }

    public User(Long id, String name, String phoneNumber, Shelter shelter) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.shelter = shelter;
    }

    public User(String name, String phoneNumber, Shelter shelter) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.shelter = shelter;
    }
}
