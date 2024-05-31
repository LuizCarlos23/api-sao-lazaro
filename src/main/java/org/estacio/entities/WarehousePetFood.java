package org.estacio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.estacio.enums.AgeRange;
import org.estacio.enums.AnimalSize;
import org.estacio.enums.Specie;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "warehouse_pet_foods")
public class WarehousePetFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated
    @Column(nullable = false)
    private Specie specie;

    @Column(nullable = false)
    private String name;

    @Column(name = "quantity_kg", nullable = false)
    private Double quantityKg;

    @Enumerated
    @Column(name = "age_range", nullable = false)
    private AgeRange ageRange;

    @Enumerated
    @Column(name = "animal_size", nullable = false)
    private AnimalSize animalSize;

    public WarehousePetFood(Specie specie, String name, Double quantityKg, AgeRange ageRange, AnimalSize animalSize) {
        this.specie = specie;
        this.name = name;
        this.quantityKg = quantityKg;
        this.ageRange = ageRange;
        this.animalSize = animalSize;
    }
}
