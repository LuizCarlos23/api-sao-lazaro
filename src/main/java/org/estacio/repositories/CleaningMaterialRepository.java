package org.estacio.repositories;

import jakarta.persistence.EntityManager;
import org.estacio.dtos.WarehouseCleaningMaterialDto;
import org.estacio.entities.WarehouseCleaningMaterial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CleaningMaterialRepository {
    @Autowired
    private EntityManager entityManager;

    public WarehouseCleaningMaterial getById(int id) {
        return  entityManager.find(WarehouseCleaningMaterial.class, id);
    }

    public WarehouseCleaningMaterial getByName(String name) {
        return  entityManager.createQuery(
                        "SELECT wcm FROM WarehouseCleaningMaterial wcm " +
                            "WHERE wcm.name = :name ", WarehouseCleaningMaterial.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Boolean addMaterial(WarehouseCleaningMaterialDto cleaningMaterial) {
        try {
            WarehouseCleaningMaterial existingCleaningMaterial = this.getByName(cleaningMaterial.getName());

            if (existingCleaningMaterial != null) {
                existingCleaningMaterial.setQuantity(existingCleaningMaterial.getQuantity() + cleaningMaterial.getQuantity());
                entityManager.merge(existingCleaningMaterial);
            } else {
                entityManager.persist(new WarehouseCleaningMaterial(
                        cleaningMaterial.getName(),
                        cleaningMaterial.getQuantity()
                ));
            }
            return true;
        } catch (Exception err) {
            System.out.println("Ocorreu um error ao adicionar o material de limpeza");
            return false;
        }
    }
}