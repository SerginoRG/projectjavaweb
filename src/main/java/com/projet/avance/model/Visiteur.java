package com.projet.avance.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_visiteur")
public class Visiteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visiteurId;

    private String nameVisiteur;

    private String adresseVisiteur;

    public Visiteur() {
    }

    public Long getVisiteurId() {
        return visiteurId;
    }

    public void setVisiteurId(Long visiteurId) {
        this.visiteurId = visiteurId;
    }

    public String getNameVisiteur() {
        return nameVisiteur;
    }

    public void setNameVisiteur(String nameVisiteur) {
        this.nameVisiteur = nameVisiteur;
    }

    public String getAdresseVisiteur() {
        return adresseVisiteur;
    }

    public void setAdresseVisiteur(String adresseVisiteur) {
        this.adresseVisiteur = adresseVisiteur;
    }
}