package org.estacio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "warehouse_cleaning_materials")
public class WarehouseCleaningMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double quantity;

    public WarehouseCleaningMaterial (String name, Double quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}
