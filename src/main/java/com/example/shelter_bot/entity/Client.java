package com.example.shelter_bot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_client")
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column()
    private String address;
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;
    @Column(name = "chat_id")
    private Long chatId;

    public Client(Long id, String name, String phoneNumber, String address, Long chatId) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.chatId = chatId;
    }

    public Client(String name, String phoneNumber, Long chatId) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.chatId = chatId;
    }
}