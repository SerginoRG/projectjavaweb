package com.projet.avance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projet.avance.model.Visiteur;

@Repository
public interface VisiteurRepository extends JpaRepository<Visiteur, Long> {

}