package com.example.shelter_bot.entity;

import com.example.shelter_bot.enums.PetType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
public class Context {
    @Id
    private Long chatId;
    @Enumerated
    private PetType petType;
    @OneToOne
    private Client client;

    public Context(Long chatId, PetType petType) {
        this.chatId = chatId;
        this.petType = petType;
    }

    public Context(Long chatId, PetType petType, Client client) {
        this.chatId = chatId;
        this.petType = petType;
        this.client = client;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
