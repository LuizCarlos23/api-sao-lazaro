package org.estacio.repositories;

import jakarta.persistence.EntityManager;
import org.estacio.dtos.WarehousePetFoodDto;
import org.estacio.entities.WarehouseFood;
import org.estacio.entities.WarehousePetFood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PetFoodRepository {
    @Autowired
    private EntityManager entityManager;

    public WarehousePetFood getById(int id) {
        return entityManager.find(WarehousePetFood.class, id);
    }

    public WarehousePetFood getByPetFood(WarehousePetFoodDto petFood) {
        return entityManager.createQuery(
                        "SELECT wpf FROM WarehousePetFood wpf " +
                                "WHERE wpf.specie = :specie " +
                                "AND wpf.name = :name " +
                                "AND wpf.ageRange = :ageRange " +
                                "AND wpf.animalSize = :animalSize", WarehousePetFood.class)
                .setParameter("specie", petFood.getSpecie())
                .setParameter("name", petFood.getName())
                .setParameter("ageRange", petFood.getAgeRange())
                .setParameter("animalSize", petFood.getAnimalSize())
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

}
