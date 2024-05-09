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
@Table(name = "deceased_animals")
public class DeceasedAnimal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "registered_animal_id", referencedColumnName = "id")
    private RegisteredAnimal registeredAnimal;

    private String reason;

    private Date date;

    public DeceasedAnimal(RegisteredAnimal registeredAnimal, String reason, Date date) {
        this.registeredAnimal = registeredAnimal;
        this.reason = reason;
        this.date = date;
    }
}
