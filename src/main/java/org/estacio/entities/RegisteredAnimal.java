package org.estacio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "registered_animals")
public class RegisteredAnimal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "entry_date", nullable = false)
    private Date entryDate;

    @Column(nullable = false)
    private String race;
    private String anamnesis;

    public RegisteredAnimal(Date entryDate, String race, String anamnesis) {
        this.entryDate = entryDate;
        this.race = race;
        this.anamnesis = anamnesis;
    }
}
