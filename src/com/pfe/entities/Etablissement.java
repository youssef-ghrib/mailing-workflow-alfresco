package com.pfe.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Etablissement {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int    id;
    private String nom;
    private String adresse;

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom( String nom ) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse( String adresse ) {
        this.adresse = adresse;
    }

    public void setId( int id ) {
        this.id = id;
    }
}
