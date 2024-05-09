package org.estacio.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.estacio.dtos.AdopteAnimalDto;
import org.estacio.dtos.AnimalDto;
import org.estacio.dtos.DeceasedDto;
import org.estacio.entities.AdoptedAnimal;
import org.estacio.entities.AnimalInShelter;
import org.estacio.entities.DeceasedAnimal;
import org.estacio.entities.RegisteredAnimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("animal")
public class AnimalController {
    @Autowired
    private EntityManager entityManager;

    @GetMapping("/")
    public ResponseEntity<?> list() {
        try {
            String jpql = "select A from RegisteredAnimal A";
            TypedQuery<RegisteredAnimal> query = entityManager.createQuery(jpql, RegisteredAnimal.class);
            List<RegisteredAnimal> animals = query.getResultList();

            return new ResponseEntity<>(animals, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao listar os animais");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/shelter")
    public ResponseEntity<?> listAnimalsInShelter() {
        try {
            String jpql = "select A from AnimalInShelter A";
            TypedQuery<AnimalInShelter> query = entityManager.createQuery(jpql, AnimalInShelter.class);
            List<AnimalInShelter> animals = query.getResultList();

            return new ResponseEntity<>(animals, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao listar os animais");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/deceaseds")
        public ResponseEntity<?> listDeceasedAnimals() {
        try {
            String jpql = "select D from DeceasedAnimal D";
            TypedQuery<DeceasedAnimal> query = entityManager.createQuery(jpql, DeceasedAnimal.class);
            List<DeceasedAnimal> animals = query.getResultList();

            return new ResponseEntity<>(animals, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao listar os animais");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/adopteds")
    public ResponseEntity<?> listAdoptedAnimals() {
        try {
            String jpql = "select A from AdoptedAnimal A";
            TypedQuery<AdoptedAnimal> query = entityManager.createQuery(jpql, AdoptedAnimal.class);
            List<AdoptedAnimal> animals = query.getResultList();

            return new ResponseEntity<>(animals, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao listar os animais");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/adopte")
    @Transactional
    public ResponseEntity<?> adopte(@RequestBody AdopteAnimalDto adoptedData) {
        try {
            AnimalInShelter shelter = entityManager.createQuery("select A FROM AnimalInShelter A " +
                            "WHERE A.registeredAnimal.id = :id", AnimalInShelter.class)
                    .setParameter("id", adoptedData.getId())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (shelter == null) {
                return ResponseEntity.badRequest().body("Animal não encontrado");
            }

            entityManager.remove(shelter);
            entityManager.persist(new AdoptedAnimal(shelter.getRegisteredAnimal(), new Date(), adoptedData.getAdopterName(),
                    adoptedData.getAdopterNumber(), adoptedData.getAdopterCpf()));

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao registrar a adoção do animal");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/decease")
    @Transactional
    public ResponseEntity<?> deceased(@RequestBody DeceasedDto deceasedAnimal) {
        try {
            AnimalInShelter shelter = entityManager.createQuery("select A FROM AnimalInShelter A " +
                            "WHERE A.registeredAnimal.id = :id", AnimalInShelter.class)
                    .setParameter("id", deceasedAnimal.getId())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (shelter == null) {
                return ResponseEntity.badRequest().body("Animal não encontrado");
            }

            entityManager.remove(shelter);
            entityManager.persist(new DeceasedAnimal(shelter.getRegisteredAnimal(), deceasedAnimal.getReason(), new Date()));

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao registrar a adoção do animal");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity<?> register(@RequestBody AnimalDto animal) {
        try {
            RegisteredAnimal registeredAnimal = new RegisteredAnimal(animal.getEntranceDate(), animal.getRace(), animal.getAnamnesis());
            entityManager.persist(registeredAnimal);
            entityManager.persist(new AnimalInShelter(animal.getLocation(), registeredAnimal));
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
            RegisteredAnimal animalFound = entityManager.find(RegisteredAnimal.class, id);

            if (animalFound == null) {
                return ResponseEntity.badRequest().body("Animal não encontrado");
            }

            RegisteredAnimal dataEdited = new RegisteredAnimal(id, animalData.getEntranceDate(), animalData.getRace(), animalData.getAnamnesis());

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
            RegisteredAnimal animalFound = entityManager.find(RegisteredAnimal.class, id);

            if (animalFound == null) {
                return ResponseEntity.badRequest().body("Animal não encontrado");
            }

            entityManager.createQuery("select A FROM AnimalInShelter A WHERE A.registeredAnimal.id = :id", AnimalInShelter.class)
                    .setParameter("id", id)
                    .getResultList()
                    .stream()
                    .findFirst().ifPresent(animalInShelter -> entityManager.remove(animalInShelter));

            entityManager.remove(animalFound);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao deletar os dados do animal");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
