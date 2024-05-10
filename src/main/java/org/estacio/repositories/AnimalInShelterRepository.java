package org.estacio.repositories;

import jakarta.persistence.EntityManager;
import org.estacio.entities.AnimalInShelter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AnimalInShelterRepository {
    @Autowired
    private EntityManager entityManager;

    public AnimalInShelter getByAnimalId(int id) {
        return entityManager.createQuery("select A FROM AnimalInShelter A " +
                        "WHERE A.registeredAnimal.id = :id", AnimalInShelter.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

}
