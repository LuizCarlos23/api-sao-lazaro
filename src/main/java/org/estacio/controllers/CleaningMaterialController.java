package org.estacio.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.estacio.dtos.WarehouseCleaningMaterialDto;
import org.estacio.dtos.WarehouseCleaningMaterialWriteoffDto;
import org.estacio.entities.WarehouseCleaningMaterial;
import org.estacio.repositories.CleaningMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cleaningmaterial")
public class CleaningMaterialController {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CleaningMaterialRepository cleaningMaterialRepository;

    @GetMapping("/")
    public ResponseEntity<?> list () {
        try {
            String jpql = "select CM from WarehouseCleaningMaterial CM";
            TypedQuery<WarehouseCleaningMaterial> query = entityManager.createQuery(jpql, WarehouseCleaningMaterial.class);
            List<WarehouseCleaningMaterial> cleaningMaterials = query.getResultList();

            return new ResponseEntity<> (cleaningMaterials, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao listar os materiais de limpeza");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity<?> register (@RequestBody WarehouseCleaningMaterialDto cleaningMaterial) {
        try {
            if (!cleaningMaterialRepository.addMaterial(cleaningMaterial)) {
                return ResponseEntity.badRequest().body("Ocorreu um erro ao adicionar o material");
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao registrar o material de limpeza");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> edit(@PathVariable int id, @RequestBody WarehouseCleaningMaterialDto cleaningMaterialData) {
        try {
            WarehouseCleaningMaterial cleaningMaterialFound = cleaningMaterialRepository.getById(id);

            if (cleaningMaterialFound == null) {
                return ResponseEntity.badRequest().body("Material de limpeza não encontrado");
            }

            WarehouseCleaningMaterial dataEdited = new WarehouseCleaningMaterial(id, cleaningMaterialData.getName(), cleaningMaterialData.getQuantity());

            entityManager.merge(dataEdited);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao editar os dados do material de limpeza");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/writeoff")
    @Transactional
    public ResponseEntity<?> writeoff(@RequestBody WarehouseCleaningMaterialWriteoffDto responseData) {
        try {
            if (responseData.getQuantity() <= 0) return ResponseEntity.badRequest().body("A quantidade tem que ser maior que zero");

            WarehouseCleaningMaterial cleaningMaterialFound = cleaningMaterialRepository.getById(responseData.getId());

            if (cleaningMaterialFound == null) {
                return ResponseEntity.badRequest().body("Id não encontrado");
            } else if (cleaningMaterialFound.getQuantity() - responseData.getQuantity() < 0) {
                return ResponseEntity.badRequest().body("A quantiade inserida excede a quantidade em estoque");
            }

            cleaningMaterialFound.setQuantity(cleaningMaterialFound.getQuantity() - responseData.getQuantity());

            entityManager.merge(cleaningMaterialFound);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao remover o material de limpeza do estoque");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping ("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            WarehouseCleaningMaterial cleaningMaterialFound = cleaningMaterialRepository.getById(id);

            if (cleaningMaterialFound == null) {
                return ResponseEntity.badRequest().body("Material de limpeza não encontrado");
            }

            entityManager.remove(cleaningMaterialFound);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao deletar os dados do material de limpeza");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}