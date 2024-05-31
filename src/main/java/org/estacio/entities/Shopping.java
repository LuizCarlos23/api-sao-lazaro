package org.estacio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.estacio.enums.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shopping")
public class Shopping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private ShoppingType type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private Double value;

    @Column(name = "petfood_animal_size")
    private AnimalSize petfoodAnimalSize;

    @Column(name = "petfood_age_range")
    private AgeRange petfoodAgeRange;

    @Column(name = "petfood_specie")
    private Specie petfoodSpecie;

    @Column(name = "medicine_type")
    private MedicineType medicineType;

    @Column(nullable = false)
    private LocalDate date;

    public Shopping(ShoppingType type, Specie petfoodSpecie, String name, Double quantity, Double value, LocalDate date, AnimalSize petfoodAnimalSize, AgeRange petfoodAgeRange, MedicineType medicineType) {
        this.type = type;
        this.petfoodSpecie = petfoodSpecie;
        this.name = name;
        this.quantity = quantity;
        this.value = value;
        this.date = date;
        this.petfoodAnimalSize = petfoodAnimalSize;
        this.petfoodAgeRange = petfoodAgeRange;
        this.medicineType = medicineType;
    }
}
