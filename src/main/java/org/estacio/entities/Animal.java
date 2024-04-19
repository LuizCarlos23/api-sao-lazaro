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
@Table(name = "animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "entrance_date", nullable = false)
    private Date entranceDate;
    @Column(nullable = false)
    private String race;
    private String local;
    private String anamnesis;

    public Animal(Date entranceDate, String race, String local, String anamnesis) {
        this.entranceDate = entranceDate;
        this.race = race;
        this.local = local;
        this.anamnesis = anamnesis;
    }
}
