package org.estacio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.estacio.enums.*;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "general_donations")
public class GeneralDonation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private GeneralDonationType type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double quantity;

    @Column(name = "petfood_animal_size")
    private AnimalSize petfoodAnimalSize;

    @Column(name = "petfood_age_range")
    private AgeRange petfoodAgeRange;

    @Column(name = "petfood_specie")
    private Specie petfoodSpecie;

    @Column(name = "medicine_type")
    private MedicineType medicineType;

    @Column(nullable = false)
    private Date date;

    public GeneralDonation(GeneralDonationType type, Specie petfoodSpecie,
                           String name, Double quantity, Date date,
                           AnimalSize petfoodAnimalSize, AgeRange petfoodAgeRange,
                           MedicineType medicineType) {
        this.type = type;
        this.petfoodSpecie = petfoodSpecie;
        this.name = name;
        this.quantity = quantity;
        this.date = date;
        this.petfoodAnimalSize = petfoodAnimalSize;
        this.petfoodAgeRange = petfoodAgeRange;
        this.medicineType = medicineType;
    }
}
