package org.estacio.repositories;

import jakarta.persistence.EntityManager;
import org.estacio.dtos.GeneralDonationDto;
import org.estacio.entities.AnimalInShelter;
import org.estacio.entities.WarehouseFood;
import org.estacio.entities.WarehousePetFood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FoodRepository {
    @Autowired
    private EntityManager entityManager;

    public WarehouseFood getById(int id) {
        return entityManager.find(WarehouseFood.class, id);
    }

    public WarehouseFood getByName(String name) {
        return entityManager.createQuery(
                        "SELECT wf FROM WarehouseFood wf " +
                                "WHERE wf.name = :name " , WarehouseFood.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

    }

}
