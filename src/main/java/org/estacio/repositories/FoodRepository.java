package org.estacio.repositories;

import jakarta.persistence.EntityManager;
import org.estacio.dtos.GeneralDonationDto;
import org.estacio.dtos.WarehouseFoodDto;
import org.estacio.dtos.WarehouseMedicineDto;
import org.estacio.entities.AnimalInShelter;
import org.estacio.entities.WarehouseFood;
import org.estacio.entities.WarehouseMedicine;
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

    public Boolean addFood(WarehouseFoodDto food) {
        try {
            WarehouseFood existingFood = this.getByName(food.getName());

            if (existingFood != null) {
                existingFood.setQuantity(existingFood.getQuantity() + food.getQuantity());
                entityManager.merge(existingFood);
            } else {
                entityManager.persist(new WarehouseFood(
                        food.getName(),
                        food.getQuantity()
                ));
            }
            return true;
        } catch (Exception err) {
            System.out.println("Ocorreu um exeção ao adicionar alimento.");
            System.out.println(err);
            return false;
        }
    }

}
