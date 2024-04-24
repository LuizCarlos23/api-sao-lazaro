package org.estacio.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.estacio.dtos.ShoppingDto;
import org.estacio.entities.Shopping;
import org.estacio.entities.WarehousePetFood;
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
}
