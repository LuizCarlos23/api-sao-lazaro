package org.estacio.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.estacio.enums.AgeRange;
import org.estacio.enums.AnimalSize;
import org.estacio.enums.MedicineType;
import org.estacio.enums.Specie;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralDonationDto {
    private String name;
    private Double quantity;
    private AnimalSize petfoodAnimalSize;
    private AgeRange petfoodAgeRange;
    private Specie petfoodSpecie;
    private MedicineType medicineType;
    private LocalDate date;
}
