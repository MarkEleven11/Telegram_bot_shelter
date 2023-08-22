package com.example.shelter_bot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "report")
@Data
@NoArgsConstructor
public class ReportData {
    @javax.persistence.Id
    @Id
    private Long id;
    private Long chatId;
    private String name;
    private String health;
    private String ration;
    private String behaviour;
    private Date lastMessage;
    @Lob
    private byte[] data;

    public ReportData(Long chatId, String name, String health, String ration, String behaviour, Date lastMessage, byte[] data) {
        this.chatId = chatId;
        this.name = name;
        this.health = health;
        this.ration = ration;
        this.behaviour = behaviour;
        this.lastMessage = lastMessage;
        this.data = data;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}