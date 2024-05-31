package org.estacio.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.estacio.enums.MonetaryDonationType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonetaryDonationDto {
    private MonetaryDonationType type;
    private Double value;
    private LocalDate date;
}
