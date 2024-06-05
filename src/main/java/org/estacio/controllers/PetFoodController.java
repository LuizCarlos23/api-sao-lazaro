package org.estacio.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.estacio.dtos.WarehousePetFoodDto;
import org.estacio.dtos.WarehousePetFoodWriteoffDto;
import org.estacio.entities.WarehousePetFood;
import org.estacio.repositories.PetFoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pet_food")
public class PetFoodController {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PetFoodRepository petFoodRepository;

    @GetMapping("/")
    public ResponseEntity<?> list() {
        try {
            String jpql = "select A from WarehousePetFood A ORDER BY id DESC";
            TypedQuery<WarehousePetFood> query = entityManager.createQuery(jpql, WarehousePetFood.class);
            List<WarehousePetFood> animals = query.getResultList();

            return new ResponseEntity<>(animals, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao listar as rações do estoque");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping("/")
//    @Transactional
//    public ResponseEntity<?> register(@RequestBody WarehousePetFoodDto petFood) {
//        try {
//            WarehousePetFood existingPetFood = petFoodRepository.getByPetFood(petFood);
//
//            if (existingPetFood != null) {
//                existingPetFood.setQuantityKg(existingPetFood.getQuantityKg() + petFood.getQuantityKg());
//                entityManager.merge(existingPetFood);
//            } else {
//                entityManager.persist(new WarehousePetFood(
//                        petFood.getSpecie(),
//                        petFood.getName(),
//                        petFood.getQuantityKg(),
//                        petFood.getAgeRange(),
//                        petFood.getAnimalSize()
//                ));
//            }
//
//            return new ResponseEntity<>(HttpStatus.CREATED);
//        } catch (Exception err) {
//            System.out.println("Ocorreu um erro ao registrar a ração");
//            System.out.println(err);
//            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> edit(@PathVariable int id, @RequestBody WarehousePetFoodDto petFood) {
        try {
            WarehousePetFood petFoodFound = petFoodRepository.getById(id);

            if (petFoodFound == null) {
                return ResponseEntity.badRequest().body("Ração não encontrado");
            }

            WarehousePetFood dataEdited = new WarehousePetFood(id,
                    petFood.getSpecie(), petFood.getName(),
                    petFood.getQuantityKg(), petFood.getAgeRange(),
                    petFood.getAnimalSize());

            entityManager.merge(dataEdited);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao editar os dados da ração");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/writeoff")
    @Transactional
    public ResponseEntity<?> removeQuantity(@RequestBody WarehousePetFoodWriteoffDto responseData) {
        try {
            if (responseData.getQuantity() <= 0) return ResponseEntity.badRequest().body("A quantidade tem que ser maior que zero");

            WarehousePetFood petFoodFound = entityManager.find(WarehousePetFood.class, responseData.getId());

            if (petFoodFound == null) {
                return ResponseEntity.badRequest().body("Id não encontrado");
            } else if (petFoodFound.getQuantityKg() - responseData.getQuantity() < 0) {
                return ResponseEntity.badRequest().body("A quantiade inserida excede a quantidade em estoque");
            }

            petFoodFound.setQuantityKg(petFoodFound.getQuantityKg() - responseData.getQuantity());

            entityManager.merge(petFoodFound);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao remover a ração do estoque");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            WarehousePetFood petFoodFound = entityManager.find(WarehousePetFood.class, id);

            if (petFoodFound == null) {
                return ResponseEntity.badRequest().body("Ração não encontrada");
            }

            entityManager.remove(petFoodFound);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao deletar os dados da ração do estoque");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
