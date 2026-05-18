package com.projet.avance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projet.avance.model.Site;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

}