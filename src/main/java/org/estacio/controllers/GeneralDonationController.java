package org.estacio.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.estacio.dtos.GeneralDonationDto;
import org.estacio.dtos.ShoppingDto;
import org.estacio.entities.*;
import org.estacio.enums.GeneralDonationType;
import org.estacio.enums.ShoppingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("general_donation")
public class GeneralDonationController {

    @Autowired
    private EntityManager entityManager;
    @GetMapping("/")
    public ResponseEntity<?> list() { // TODO: adicionar filtro
        try {
            String jpql = "select A from GeneralDonation A";
            TypedQuery<GeneralDonation> query = entityManager.createQuery(jpql, GeneralDonation.class);
            List<GeneralDonation> shopping = query.getResultList();

            return new ResponseEntity<>(shopping, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao listar as compras");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/pet_food")
    @Transactional
    public ResponseEntity<?> petFoodRegister(@RequestBody GeneralDonationDto donatedPetFood) {
        try {
            GeneralDonation donation = new GeneralDonation(
                    GeneralDonationType.PET_FOOD,
                    donatedPetFood.getPetfoodSpecie(),
                    donatedPetFood.getName(),
                    donatedPetFood.getQuantity(),
                    new Date(), // TODO: ajustar formato
                    donatedPetFood.getPetfoodAnimalSize(),
                    donatedPetFood.getPetfoodAgeRange(),
                    null
            );

            entityManager.persist(donation);

            WarehousePetFood existingPetFood = entityManager.createQuery(
                    "SELECT wpf FROM WarehousePetFood wpf " +
                    "WHERE wpf.specie = :specie " +
                    "AND wpf.name = :name " +
                    "AND wpf.ageRange = :ageRange " +
                    "AND wpf.animalSize = :animalSize", WarehousePetFood.class)
                .setParameter("specie", donatedPetFood.getPetfoodSpecie())
                .setParameter("name", donatedPetFood.getName())
                .setParameter("ageRange", donatedPetFood.getPetfoodAgeRange())
                .setParameter("animalSize", donatedPetFood.getPetfoodAnimalSize())
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

            if (existingPetFood != null) {
                existingPetFood.setQuantityKg(existingPetFood.getQuantityKg() + donatedPetFood.getQuantity());
                entityManager.merge(existingPetFood);
            } else {
                entityManager.persist(new WarehousePetFood(
                        donatedPetFood.getPetfoodSpecie(),
                        donatedPetFood.getName(),
                        donatedPetFood.getQuantity(),
                        donatedPetFood.getPetfoodAgeRange(),
                        donatedPetFood.getPetfoodAnimalSize()
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
    public ResponseEntity<?> FoodRegister(@RequestBody GeneralDonationDto donatedFood) {
        try {
            GeneralDonation shopping = new GeneralDonation(
                    GeneralDonationType.FOOD,
                    null,
                    donatedFood.getName(),
                    donatedFood.getQuantity(),
                    new Date(),
                    null,
                    null,
                    null
            );

            entityManager.persist(shopping);

            WarehouseFood existingFood = entityManager.createQuery(
                            "SELECT wf FROM WarehouseFood wf " +
                                    "WHERE wf.name = :name " , WarehouseFood.class)
                    .setParameter("name", donatedFood.getName())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (existingFood != null) {
                existingFood.setQuantity(existingFood.getQuantity() + donatedFood.getQuantity());
                entityManager.merge(existingFood);
            } else {
                entityManager.persist(new WarehouseFood(
                        donatedFood.getName(),
                        donatedFood.getQuantity()
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
    public ResponseEntity<?> medicineRegister(@RequestBody GeneralDonationDto donatedMedicine) {
        try {
            GeneralDonation donation = new GeneralDonation(
                    GeneralDonationType.MEDICINE,
                    null,
                    donatedMedicine.getName(),
                    donatedMedicine.getQuantity(),
                    new Date(),
                    null,
                    null,
                    donatedMedicine.getMedicineType()
            );

            entityManager.persist(donation);

            WarehouseMedicine existingMedicine = entityManager.createQuery(
                            "SELECT wm FROM WarehouseMedicine wm " +
                                    "WHERE wm.name = :name " +
                                    "AND wm.type = :type", WarehouseMedicine.class)
                    .setParameter("name", donatedMedicine.getName())
                    .setParameter("type", donatedMedicine.getMedicineType())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (existingMedicine != null) {
                existingMedicine.setQuantity(existingMedicine.getQuantity() + donatedMedicine.getQuantity());
                entityManager.merge(existingMedicine);
            } else {
                entityManager.persist(new WarehouseMedicine(
                        donatedMedicine.getName(),
                        donatedMedicine.getMedicineType(),
                        donatedMedicine.getQuantity()
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
    public ResponseEntity<?> cleaningMaterialRegister(@RequestBody GeneralDonationDto donatedCleaningMaterial) {
        try {
            GeneralDonation donation = new GeneralDonation(
                    GeneralDonationType.CLEANING_MATERIAL,
                    null,
                    donatedCleaningMaterial.getName(),
                    donatedCleaningMaterial.getQuantity(),
                    new Date(),
                    null,
                    null,
                    null
            );

            entityManager.persist(donation);

            WarehouseCleaningMaterial existingCleaningMaterial = entityManager.createQuery(
                            "SELECT wcm FROM WarehouseCleaningMaterial wcm " +
                                    "WHERE wcm.name = :name", WarehouseCleaningMaterial.class)
                    .setParameter("name", donatedCleaningMaterial.getName())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (existingCleaningMaterial != null) {
                existingCleaningMaterial.setQuantity(existingCleaningMaterial.getQuantity() + donatedCleaningMaterial.getQuantity());
                entityManager.merge(existingCleaningMaterial);
            } else {
                entityManager.persist(new WarehouseCleaningMaterial(
                        donatedCleaningMaterial.getName(),
                        donatedCleaningMaterial.getQuantity()
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
