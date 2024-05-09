package org.estacio.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdopteAnimalDto {
    private int id;
    private String adopterName;
    private String adopterNumber;
    private String adopterCpf;
}
