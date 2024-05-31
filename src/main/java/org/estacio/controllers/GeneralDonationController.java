package org.estacio.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.estacio.dtos.*;
import org.estacio.entities.*;
import org.estacio.enums.GeneralDonationType;
import org.estacio.enums.ShoppingType;
import org.estacio.repositories.CleaningMaterialRepository;
import org.estacio.repositories.FoodRepository;
import org.estacio.repositories.MedicineRepository;
import org.estacio.repositories.PetFoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("general_donation")
public class GeneralDonationController {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private PetFoodRepository petFoodRepository;
    @Autowired
    private CleaningMaterialRepository cleaningMaterialRepository;

    @Autowired
    private MedicineRepository medicineRepository;


    @GetMapping("/")
    public ResponseEntity<?> list() { // TODO: adicionar filtro
        try {
            String jpql = "select A from GeneralDonation A";
            TypedQuery<GeneralDonation> query = entityManager.createQuery(jpql, GeneralDonation.class);
            List<GeneralDonation> shopping = query.getResultList();

            return new ResponseEntity<>(shopping, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao listar as doações");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/pet_food")
    @Transactional
    public ResponseEntity<?> petFoodRegister(@RequestBody WarehousePetFoodDto donatedPetFood) {
        try {
            GeneralDonation donation = new GeneralDonation(donatedPetFood);

            entityManager.persist(donation);

            if (!petFoodRepository.addPetFood(donatedPetFood)) {
                return ResponseEntity.badRequest().body("Não foi possível adicionar a ração, por favor verifique os campos");
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
    public ResponseEntity<?> FoodRegister(@RequestBody WarehouseFoodDto donatedFood) {
        try {
            GeneralDonation donation = new GeneralDonation(donatedFood);

            entityManager.persist(donation);

            if (!foodRepository.addFood(donatedFood)) {
                return ResponseEntity.badRequest().body("Não foi possível adicionar o alimento, por favor verifique os campos");
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
    public ResponseEntity<?> medicineRegister(@RequestBody WarehouseMedicineDto donatedMedicine) {
        try {
            GeneralDonation donation = new GeneralDonation(donatedMedicine);

            entityManager.persist(donation);

            if (!medicineRepository.addMedicine(donatedMedicine)) {
                return ResponseEntity.badRequest().body("Não foi possível adicionar o medicamento, por favor verifique os campos");
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
    public ResponseEntity<?> cleaningMaterialRegister(@RequestBody WarehouseCleaningMaterialDto donatedCleaningMaterial) {
        try {
            GeneralDonation donation = new GeneralDonation(donatedCleaningMaterial);

            entityManager.persist(donation);

            if (!cleaningMaterialRepository.addMaterial(donatedCleaningMaterial)) {
                return ResponseEntity.badRequest().body("Não foi possível adicionar o material de limpeza, por favor verifique os campos");
            }

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao registrar o material de limpeza");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
