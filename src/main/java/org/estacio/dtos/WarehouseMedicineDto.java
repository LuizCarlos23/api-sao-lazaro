package org.estacio.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.estacio.enums.MedicineType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseMedicineDto {
    private String name;
    private MedicineType type;
    private Double quantity;

}
