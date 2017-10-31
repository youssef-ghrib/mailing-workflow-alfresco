package com.pfe.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PersonneExterne {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int            id;
    private String         nom;
    private String         prenom;

    @ManyToOne
    private ServiceExterne serviceExt;

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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom( String prenom ) {
        this.prenom = prenom;
    }

    public ServiceExterne getServiceExt() {
        return serviceExt;
    }

    public void setServiceExt( ServiceExterne serviceExt ) {
        this.serviceExt = serviceExt;
    }
}
