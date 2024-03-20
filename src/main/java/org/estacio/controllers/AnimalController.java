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
            System.out.println("Ocorreu ao listar os animais!");
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

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um error ao registrar o animal!");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
