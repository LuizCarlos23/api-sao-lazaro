package org.estacio.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalDto {
    private Date entranceDate;
    private String race;
    private String location;
    private String anamnesis;
}
