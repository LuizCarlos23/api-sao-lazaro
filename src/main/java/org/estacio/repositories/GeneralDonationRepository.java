package org.estacio.repositories;

import jakarta.persistence.EntityManager;
import org.estacio.entities.AnimalInShelter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GeneralDonationRepository {
    @Autowired
    private EntityManager entityManager;

}
