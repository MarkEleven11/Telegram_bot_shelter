package com.example.shelter_bot.entity;
import com.example.shelter_bot.enums.PetType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "pet")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@Column(name = "pet_type", nullable = false)
//    @ManyToOne
//    @JoinColumn(name = "pet_type_id")
    @Enumerated (EnumType.STRING)
    private PetType petType;

    @Column(name = "chat_id", nullable = false)
    private long chatId;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Pet(long id, PetType petType, long chatId) {
        this.id = id;
        this.petType = petType;
        this.chatId = chatId;
    }
    public Pet() {

    }
}
