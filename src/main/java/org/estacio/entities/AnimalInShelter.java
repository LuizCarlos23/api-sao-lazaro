package org.estacio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "animals_in_shelters")
public class AnimalInShelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "registered_animal_id", referencedColumnName = "id")
    private RegisteredAnimal registeredAnimal;

    private String location;

    public AnimalInShelter(String location, RegisteredAnimal animal) {
        this.location = location;
        this.registeredAnimal = animal;
    }
}
