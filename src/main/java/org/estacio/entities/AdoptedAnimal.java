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
@Table(name = "adopted_animals")
public class AdoptedAnimal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "registered_animal_id", referencedColumnName = "id")
    private RegisteredAnimal registeredAnimal;

    @Column(name = "adoption_date")
    private LocalDate adoptionDate;

    @Column(name = "adopter_name")
    private String adopterName;

    @Column(name = "adopter_number")
    private String adopterNumber;

    @Column(name = "adopter_cpf")
    private String adopterCpf;

    public AdoptedAnimal(RegisteredAnimal registeredAnimal, LocalDate adoptionDate, String adopterName, String adopterNumber, String adopterCpf) {
        this.registeredAnimal = registeredAnimal;
        this.adoptionDate = adoptionDate;
        this.adopterName = adopterName;
        this.adopterNumber = adopterNumber;
        this.adopterCpf = adopterCpf;
    }
}
