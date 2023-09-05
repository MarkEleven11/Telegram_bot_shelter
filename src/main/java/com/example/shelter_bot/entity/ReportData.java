package com.example.shelter_bot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "report")
@Data
@NoArgsConstructor
public class ReportData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "chat_id_report")
    private Long chatId;
    @Column(name = "pet_name")
    private String name;
    private String health;
    private String ration;
    private String behaviour;
    @Column(name = "message_date")
    private Date lastMessage;
    @Column(name = "data_report")
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