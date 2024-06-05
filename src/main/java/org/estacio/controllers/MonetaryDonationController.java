package org.estacio.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.estacio.dtos.MonetaryDonationDto;
import org.estacio.entities.MonetaryDonation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("monetary_donation")
public class MonetaryDonationController {
    @Autowired
    private EntityManager entityManager;
    @GetMapping("/")
    public ResponseEntity<?> list() { // TODO: adicionar filtro
        try {
            String jpql = "select A from MonetaryDonation A ORDER BY id DESC";
            TypedQuery<MonetaryDonation> query = entityManager.createQuery(jpql, MonetaryDonation.class);
            List<MonetaryDonation> monetaryDonations = query.getResultList();

            return new ResponseEntity<>(monetaryDonations, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao listar as doações");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping ("/")
    @Transactional
    public ResponseEntity<?> register(@RequestBody MonetaryDonationDto donation) {
        try {
            entityManager.persist(new MonetaryDonation(donation.getType(), donation.getValue(), donation.getDate()));

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception err) {
            System.out.println("Ocorreu um erro ao registrar a doação");
            System.out.println(err);
            return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
