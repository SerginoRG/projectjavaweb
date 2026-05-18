package com.projet.avance.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "app_visiter")
public class Visiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visiterId;

    @ManyToOne
    @JoinColumn(name = "visiteur_id")
    private Visiteur visiteur;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;

    private Long nbrJours;

    private LocalDate dateVisite;

    public Visiter() {
    }

    public Long getVisiterId() {
        return visiterId;
    }

    public void setVisiterId(Long visiterId) {
        this.visiterId = visiterId;
    }

    public Visiteur getVisiteur() {
        return visiteur;
    }

    public void setVisiteur(Visiteur visiteur) {
        this.visiteur = visiteur;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Long getNbrJours() {
        return nbrJours;
    }

    public void setNbrJours(Long nbrJours) {
        this.nbrJours = nbrJours;
    }

    public LocalDate getDateVisite() {
        return dateVisite;
    }

    public void setDateVisite(LocalDate dateVisite) {
        this.dateVisite = dateVisite;
    }
}