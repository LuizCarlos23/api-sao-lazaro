package org.estacio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.estacio.enums.MonetaryDonationType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "monetary_donations")
public class MonetaryDonation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private MonetaryDonationType type;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private LocalDate date;
    
    public MonetaryDonation(MonetaryDonationType type, Double value, LocalDate date) {
        this.type = type;
        this.value = value;
        this.date = date;
    }
}
