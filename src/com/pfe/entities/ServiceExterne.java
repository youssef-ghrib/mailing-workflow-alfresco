package com.pfe.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ServiceExterne {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int           id;
    private String        nom;

    @ManyToOne
    private Etablissement etablissement;

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom( String nom ) {
        this.nom = nom;
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public void setEtablissement( Etablissement etablissement ) {
        this.etablissement = etablissement;
    }
}
