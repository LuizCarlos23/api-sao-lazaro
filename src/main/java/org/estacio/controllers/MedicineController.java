package org.estacio.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.estacio.dtos.WarehouseMedicineDto;
import org.estacio.dtos.WarehouseMedicineWriteoffDto;
import org.estacio.entities.WarehouseMedicine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("medicine")
public class MedicineController {
    @Autowired
    private EntityManager entityManager;
    @GetMapping("/")
    public ResponseEntity<?> list() {
        try {
            String jpql = "select A from WarehouseMedicine A";
            TypedQuery<WarehouseMedicine> query = entityManager.createQuery(jpql, WarehouseMedicine.class);
            List<WarehouseMedicine> animals = query.getResultList();

            return new ResponseEntity<>(animals, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao listar os remedios do estoque");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity<?> register(@RequestBody WarehouseMedicineDto medicine) {
        try {
            WarehouseMedicine existingMedicine = entityManager.createQuery(
                    "SELECT wm FROM WarehouseMedicine wm " +
                    "WHERE wm.name = :name " +
                    "AND wm.type = :type ", WarehouseMedicine.class)
                .setParameter("name", medicine.getName())
                .setParameter("type", medicine.getType())
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

            if (existingMedicine != null) {
                existingMedicine.setQuantity(existingMedicine.getQuantity() + medicine.getQuantity());
                entityManager.merge(existingMedicine);
            } else {
                entityManager.persist(new WarehouseMedicine(
                        medicine.getName(),
                        medicine.getType(),
                        medicine.getQuantity()
                ));
            }

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao registrar o remedio");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> edit(@PathVariable int id, @RequestBody WarehouseMedicineDto medicine) {
        try {
            WarehouseMedicine medicineFound = entityManager.find(WarehouseMedicine.class, id);

            if (medicineFound == null) {
                return ResponseEntity.badRequest().body("Remédio não encontrado");
            }

            WarehouseMedicine dataEdited = new WarehouseMedicine(id,
                    medicine.getName(), medicine.getType(),
                    medicine.getQuantity());

            entityManager.merge(dataEdited);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao editar os dados do remedio");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/writeoff")
    @Transactional
    public ResponseEntity<?> writeoff(@RequestBody WarehouseMedicineWriteoffDto responseData) {
        try {
            if (responseData.getQuantity() <= 0) return ResponseEntity.badRequest().body("A quantidade tem que ser maior que zero");

            WarehouseMedicine medicineFound = entityManager.find(WarehouseMedicine.class, responseData.getId());

            if (medicineFound == null) {
                return ResponseEntity.badRequest().body("Id não encontrado");
            } else if (medicineFound.getQuantity() - responseData.getQuantity() < 0) {
                return ResponseEntity.badRequest().body("A quantiade inserida excede a quantidade em estoque");
            }

            medicineFound.setQuantity(medicineFound.getQuantity() - responseData.getQuantity());

            entityManager.merge(medicineFound);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao remover o remedio do estoque");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            WarehouseMedicine medicineFound = entityManager.find(WarehouseMedicine.class, id);

            if (medicineFound == null) {
                return ResponseEntity.badRequest().body("Remédio não encontrado");
            }

            entityManager.remove(medicineFound);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao deletar os dados do remedio do estoque");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
