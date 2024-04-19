package org.estacio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.estacio.enums.MedicineType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "warehouse_medicines")
public class WarehouseMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Enumerated
    @Column(nullable = false)
    private MedicineType type;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    public WarehouseMedicine(String name, MedicineType type, Double quantity) {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }
}
