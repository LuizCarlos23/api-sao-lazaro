package org.estacio.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.estacio.enums.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingDto {
    private String name;
    private Double quantity;
    private Double value;
    private AnimalSize petfoodAnimalSize;
    private AgeRange petfoodAgeRange;
    private Specie petfoodSpecie;
    private MedicineType medicineType;
    private Date date;
}
