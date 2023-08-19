package com.example.shelter_bot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.persistence.Lob;
import java.util.Date;

/**
 * Отчеты
 **/

@AllArgsConstructor
@Data
@NoArgsConstructor

public class ReportData {
    @GeneratedValue
    @Id
//    @Entity

    private long id;
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
}