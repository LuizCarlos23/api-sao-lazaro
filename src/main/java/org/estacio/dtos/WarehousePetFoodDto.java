package org.estacio.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.estacio.enums.AgeRange;
import org.estacio.enums.AnimalSize;
import org.estacio.enums.Specie;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehousePetFoodDto {
    private Specie specie;
    private String name;
    private Double quantityKg;
    private AgeRange ageRange;
    private AnimalSize animalSize;
}
