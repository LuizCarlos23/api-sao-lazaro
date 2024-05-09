package org.estacio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.estacio.enums.MonetaryDonationType;

import java.util.Date;

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
    private Date date;
    
    public MonetaryDonation(MonetaryDonationType type, Double value, Date date) {
        this.type = type;
        this.value = value;
        this.date = date;
    }
}
