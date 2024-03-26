package org.estacio.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.estacio.dtos.AnimalDto;
import org.estacio.entities.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("animal")
public class AnimalController {
    @Autowired
    private EntityManager entityManager;

    @GetMapping("/")
    public ResponseEntity<?> list() {
        try {
            String jpql = "select A from Animal A";
            TypedQuery<Animal> query = entityManager.createQuery(jpql, Animal.class);
            List<Animal> animals = query.getResultList();

            return new ResponseEntity<>(animals, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao listar os animais");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity<?> register(@RequestBody AnimalDto animal) {
        try {
            entityManager.persist(new Animal(animal.getEntranceDate(), animal.getRace(),
                    animal.getLocal(), animal.getAnamnesis()));

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao registrar o animal");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> edit(@PathVariable int id, @RequestBody AnimalDto animalData) {
        try {
            Animal animalFound = entityManager.find(Animal.class, id);

            if (animalFound == null) {
                return ResponseEntity.badRequest().body("Animal não encontrado");
            }

            Animal dataEdited = new Animal(id, animalData.getEntranceDate(), animalData.getRace(),
                    animalData.getLocal(), animalData.getAnamnesis());

            entityManager.merge(dataEdited);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao editar os dados do animal");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // TODO: Metodo de teste. Os dados do animal não devem ser deletados, ele é
    //  apenas movido para outra tabela (de obito ou doação)
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            Animal animalFound = entityManager.find(Animal.class, id);

            if (animalFound == null) {
                return ResponseEntity.badRequest().body("Animal não encontrado");
            }

            entityManager.remove(animalFound);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao deletar os dados do animal");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
