package org.estacio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.estacio.dtos.WarehouseCleaningMaterialDto;
import org.estacio.dtos.WarehouseFoodDto;
import org.estacio.dtos.WarehouseMedicineDto;
import org.estacio.dtos.WarehousePetFoodDto;
import org.estacio.enums.*;

import java.time.LocalDate;


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
    private LocalDate date;

    public GeneralDonation(GeneralDonationType type, Specie petfoodSpecie,
                           String name, Double quantity, LocalDate date,
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

    public GeneralDonation(WarehousePetFoodDto petFood) {
        this.type = GeneralDonationType.PET_FOOD;
        this.petfoodSpecie = petFood.getSpecie();
        this.name = petFood.getName();
        this.quantity = petFood.getQuantityKg();
        this.date = LocalDate.now();
        this.petfoodAnimalSize = petFood.getAnimalSize();
        this.petfoodAgeRange = petFood.getAgeRange();
    }

    public GeneralDonation(WarehouseFoodDto food) {
        this.type = GeneralDonationType.FOOD;
        this.name = food.getName();
        this.quantity = food.getQuantity();
        this.date = LocalDate.now();
    }

    public GeneralDonation(WarehouseMedicineDto medicine) {
        this.type = GeneralDonationType.MEDICINE;
        this.name = medicine.getName();
        this.quantity = medicine.getQuantity();
        this.date = LocalDate.now();
        this.medicineType = medicine.getType();
    }

    public GeneralDonation(WarehouseCleaningMaterialDto cleaningMaterial) {
        this.type = GeneralDonationType.CLEANING_MATERIAL;
        this.name = cleaningMaterial.getName();
        this.quantity = cleaningMaterial.getQuantity();
        this.date = LocalDate.now();
    }
}
