package com.projet.avance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projet.avance.model.Visiteur;
import java.util.List;

@Repository
public interface VisiteurRepository extends JpaRepository<Visiteur, Long> {
    // Rechercher par nom
    List<Visiteur> findByNameVisiteurContainingIgnoreCase(String name);
}