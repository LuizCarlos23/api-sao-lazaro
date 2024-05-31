package org.estacio.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalDto {
    private LocalDate entranceDate;
    private String race;
    private String location;
    private String anamnesis;
}
