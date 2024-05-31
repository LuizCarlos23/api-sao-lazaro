package org.estacio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    private LocalDate date;

    public DeceasedAnimal(RegisteredAnimal registeredAnimal, String reason, LocalDate date) {
        this.registeredAnimal = registeredAnimal;
        this.reason = reason;
        this.date = date;
    }
}
