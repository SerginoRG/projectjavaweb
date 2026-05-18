package com.projet.avance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projet.avance.model.Visiter;

@Repository
public interface VisiterRepository extends JpaRepository<Visiter, Long> {

}