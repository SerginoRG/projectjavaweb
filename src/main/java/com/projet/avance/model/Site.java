package com.projet.avance.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_site")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long siteId;

    private String nameSite;

    private String lieuSite;

    private Double tarifJournalier;

    public Site() {
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getNameSite() {
        return nameSite;
    }

    public void setNameSite(String nameSite) {
        this.nameSite = nameSite;
    }

    public String getLieuSite() {
        return lieuSite;
    }

    public void setLieuSite(String lieuSite) {
        this.lieuSite = lieuSite;
    }

    public Double getTarifJournalier() {
        return tarifJournalier;
    }

    public void setTarifJournalier(Double tarifJournalier) {
        this.tarifJournalier = tarifJournalier;
    }
}