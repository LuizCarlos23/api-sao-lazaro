package org.estacio.repositories;

import jakarta.persistence.EntityManager;
import org.estacio.dtos.WarehouseCleaningMaterialDto;
import org.estacio.dtos.WarehouseMedicineDto;
import org.estacio.entities.WarehouseCleaningMaterial;
import org.estacio.entities.WarehouseMedicine;
import org.estacio.enums.MedicineType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MedicineRepository {
    @Autowired
    private EntityManager entityManager;

    public WarehouseMedicine getById(int id) {
        return  entityManager.find(WarehouseMedicine.class, id);
    }

    public WarehouseMedicine getByNameAndType(String name, MedicineType type) {
        return  entityManager.createQuery(
                        "SELECT wm FROM WarehouseMedicine wm " +
                                "WHERE wm.name = :name " +
                                "AND wm.type = :type", WarehouseMedicine.class)
                .setParameter("name", name)
                .setParameter("type", type)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Boolean addMedicine(WarehouseMedicineDto medicine) {
        try {
            WarehouseMedicine existingMedicine = this.getByNameAndType(medicine.getName(), medicine.getType());

            if (existingMedicine != null) {
                existingMedicine.setQuantity(existingMedicine.getQuantity() + medicine.getQuantity());
                entityManager.merge(existingMedicine);
            } else {
                entityManager.persist(new WarehouseMedicine(
                        medicine.getName(),
                        medicine.getType(),
                        medicine.getQuantity()
                ));
            }
            return true;
        } catch (Exception err) {
            System.out.println("Ocorreu um exeção ao adicionar medicamentos.");
            System.out.println(err);
            return false;
        }
    }
}