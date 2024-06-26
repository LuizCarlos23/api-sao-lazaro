package org.estacio.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.estacio.dtos.WarehouseFoodDto;
import org.estacio.dtos.WarehouseFoodWriteoffDto;
import org.estacio.entities.WarehouseFood;
import org.estacio.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("food")
public class FoodController {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping ("/")
    public ResponseEntity<?> list () {
        try {
            String jpql = "select F from WarehouseFood F ORDER BY id DESC";
            TypedQuery<WarehouseFood> query = entityManager.createQuery(jpql, WarehouseFood.class);
            List<WarehouseFood> foods = query.getResultList();

            return new ResponseEntity<> (foods, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao listar os alimentos");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping ("/")
//    @Transactional
//    public ResponseEntity<?> register (@RequestBody WarehouseFoodDto food) {
//        try {
//            WarehouseFood existingFood = entityManager.createQuery(
//                            "SELECT wf FROM WarehouseFood wf " +
//                                    "WHERE wf.name = :name ", WarehouseFood.class)
//                    .setParameter("name", food.getName())
//                    .getResultList()
//                    .stream()
//                    .findFirst()
//                    .orElse(null);
//
//            if (existingFood != null) {
//                existingFood.setQuantity(existingFood.getQuantity() + food.getQuantity());
//                entityManager.merge(existingFood);
//            } else {
//                entityManager.persist(new WarehouseFood(
//                        food.getName(),
//                        food.getQuantity()
//                ));
//            }
//
//            return new ResponseEntity<>(HttpStatus.CREATED);
//        } catch (Exception err) {
//            System.out.println("Ocorreu um erro ao registrar o alimento");
//            System.out.println(err);
//            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> edit(@PathVariable int id, @RequestBody WarehouseFoodDto foodData) {
        try {
            WarehouseFood foodFound = foodRepository.getById(id);

            if (foodFound == null) {
                return ResponseEntity.badRequest().body("Alimento não encontrado");
            }

            WarehouseFood dataEdited = new WarehouseFood(id, foodData.getName(), foodData.getQuantity());

            entityManager.merge(dataEdited);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao editar os dados do alimento");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/writeoff")
    @Transactional
    public ResponseEntity<?> writeoff(@RequestBody WarehouseFoodWriteoffDto responseData) {
        try {
            if (responseData.getQuantity() <= 0) return ResponseEntity.badRequest().body("A quantidade tem que ser maior que zero");

            WarehouseFood foodFound = entityManager.find(WarehouseFood.class, responseData.getId());

            if (foodFound == null) {
                return ResponseEntity.badRequest().body("Id não encontrado");
            } else if (foodFound.getQuantity() - responseData.getQuantity() < 0) {
                return ResponseEntity.badRequest().body("A quantiade inserida excede a quantidade em estoque");
            }

            foodFound.setQuantity(foodFound.getQuantity() - responseData.getQuantity());

            entityManager.merge(foodFound);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao remover o alimento do estoque");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping ("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            WarehouseFood foodFound = foodRepository.getById(id);

            if (foodFound == null) {
                return ResponseEntity.badRequest().body("Alimento não encontrado");
            }

            entityManager.remove(foodFound);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao deletar os dados do alimento");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
