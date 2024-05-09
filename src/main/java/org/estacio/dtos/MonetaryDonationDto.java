package org.estacio.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.estacio.enums.MonetaryDonationType;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonetaryDonationDto {
    private MonetaryDonationType type;
    private Double value;
    private Date date;
}
