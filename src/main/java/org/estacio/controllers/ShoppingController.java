package org.estacio.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.estacio.dtos.ShoppingDto;
import org.estacio.entities.*;
import org.estacio.enums.ShoppingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("shopping")
public class ShoppingController {

    @Autowired
    private EntityManager entityManager;
    @GetMapping("/")
    public ResponseEntity<?> list() { // TODO: adicionar filtro
        try {
            String jpql = "select A from Shopping A";
            TypedQuery<Shopping> query = entityManager.createQuery(jpql, Shopping.class);
            List<Shopping> shopping = query.getResultList();

            return new ResponseEntity<>(shopping, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao listar as compras");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/pet_food")
    @Transactional
    public ResponseEntity<?> petFoodRegister(@RequestBody ShoppingDto shoppingPetFood) {
        try {
            Shopping shopping = new Shopping(
                    ShoppingType.PET_FOOD,
                    shoppingPetFood.getPetfoodSpecie(),
                    shoppingPetFood.getName(),
                    shoppingPetFood.getQuantity(),
                    shoppingPetFood.getValue(),
                    new Date(), // TODO: ajustar formato
                    shoppingPetFood.getPetfoodAnimalSize(),
                    shoppingPetFood.getPetfoodAgeRange(),
                    null
            );

            entityManager.persist(shopping);

            WarehousePetFood existingPetFood = entityManager.createQuery(
                    "SELECT wpf FROM WarehousePetFood wpf " +
                    "WHERE wpf.specie = :specie " +
                    "AND wpf.name = :name " +
                    "AND wpf.ageRange = :ageRange " +
                    "AND wpf.animalSize = :animalSize", WarehousePetFood.class)
                .setParameter("specie", shoppingPetFood.getPetfoodSpecie())
                .setParameter("name", shoppingPetFood.getName())
                .setParameter("ageRange", shoppingPetFood.getPetfoodAgeRange())
                .setParameter("animalSize", shoppingPetFood.getPetfoodAnimalSize())
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

            if (existingPetFood != null) {
                existingPetFood.setQuantityKg(existingPetFood.getQuantityKg() + shoppingPetFood.getQuantity());
                entityManager.merge(existingPetFood);
            } else {
                entityManager.persist(new WarehousePetFood(
                        shoppingPetFood.getPetfoodSpecie(),
                        shoppingPetFood.getName(),
                        shoppingPetFood.getQuantity(),
                        shoppingPetFood.getPetfoodAgeRange(),
                        shoppingPetFood.getPetfoodAnimalSize()
                ));
            }

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao registrar a ração");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/food")
    @Transactional
    public ResponseEntity<?> foodRegister(@RequestBody ShoppingDto shoppingFood) {
        try {
            Shopping shopping = new Shopping(
                    ShoppingType.FOOD,
                    null,
                    shoppingFood.getName(),
                    shoppingFood.getQuantity(),
                    shoppingFood.getValue(),
                    new Date(),
                    null,
                    null,
                    null
            );

            entityManager.persist(shopping);

            WarehouseFood existingFood = entityManager.createQuery(
                            "SELECT wf FROM WarehouseFood wf " +
                                    "WHERE wf.name = :name " , WarehouseFood.class)
                    .setParameter("name", shoppingFood.getName())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (existingFood != null) {
                existingFood.setQuantity(existingFood.getQuantity() + shoppingFood.getQuantity());
                entityManager.merge(existingFood);
            } else {
                entityManager.persist(new WarehouseFood(
                        shoppingFood.getName(),
                        shoppingFood.getQuantity()
                ));
            }

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao registrar o alimento");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/medicine")
    @Transactional
    public ResponseEntity<?> medicineRegister(@RequestBody ShoppingDto shoppingMedicine) {
        try {
            Shopping shopping = new Shopping(
                    ShoppingType.MEDICINE,
                    null,
                    shoppingMedicine.getName(),
                    shoppingMedicine.getQuantity(),
                    shoppingMedicine.getValue(),
                    new Date(),
                    null,
                    null,
                    shoppingMedicine.getMedicineType()
            );

            entityManager.persist(shopping);

            WarehouseMedicine existingMedicine = entityManager.createQuery(
                            "SELECT wm FROM WarehouseMedicine wm " +
                                    "WHERE wm.name = :name " +
                                    "AND wm.type = :type", WarehouseMedicine.class)
                    .setParameter("name", shoppingMedicine.getName())
                    .setParameter("type", shoppingMedicine.getMedicineType())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (existingMedicine != null) {
                existingMedicine.setQuantity(existingMedicine.getQuantity() + shoppingMedicine.getQuantity());
                entityManager.merge(existingMedicine);
            } else {
                entityManager.persist(new WarehouseMedicine(
                        shoppingMedicine.getName(),
                        shoppingMedicine.getMedicineType(),
                        shoppingMedicine.getQuantity()
                ));
            }

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao registrar o medicamento");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cleaning_material")
    @Transactional
    public ResponseEntity<?> cleaningMaterialRegister(@RequestBody ShoppingDto shoppingCleaningMaterial) {
        try {
            Shopping shopping = new Shopping(
                    ShoppingType.CLEANING_MATERIAL,
                    null,
                    shoppingCleaningMaterial.getName(),
                    shoppingCleaningMaterial.getQuantity(),
                    shoppingCleaningMaterial.getValue(),
                    new Date(),
                    null,
                    null,
                    null
            );

            entityManager.persist(shopping);

            WarehouseCleaningMaterial existingCleaningMaterial = entityManager.createQuery(
                            "SELECT wcm FROM WarehouseCleaningMaterial wcm " +
                                    "WHERE wcm.name = :name", WarehouseCleaningMaterial.class)
                    .setParameter("name", shoppingCleaningMaterial.getName())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (existingCleaningMaterial != null) {
                existingCleaningMaterial.setQuantity(existingCleaningMaterial.getQuantity() + shoppingCleaningMaterial.getQuantity());
                entityManager.merge(existingCleaningMaterial);
            } else {
                entityManager.persist(new WarehouseCleaningMaterial(
                        shoppingCleaningMaterial.getName(),
                        shoppingCleaningMaterial.getQuantity()
                ));
            }

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao registrar o material de limpeza");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
