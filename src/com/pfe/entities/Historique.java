package com.pfe.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Historique {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int            id;
    private String         statut;

    @Temporal( TemporalType.TIMESTAMP )
    private Date           dateReception;

    @Temporal( TemporalType.TIMESTAMP )
    private Date           dateFinTraitement;
    private String         commentaires;

    @ManyToOne
    private CourrierArrive courrierArr;

    @ManyToOne
    private Employe        employe;

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut( String statut ) {
        this.statut = statut;
    }

    public Date getDateReception() {
        return dateReception;
    }

    public void setDateReception( Date dateReception ) {
        this.dateReception = dateReception;
    }

    public Date getDateFinTraitement() {
        return dateFinTraitement;
    }

    public void setDateFinTraitement( Date dateFinTraitement ) {
        this.dateFinTraitement = dateFinTraitement;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires( String commentaires ) {
        this.commentaires = commentaires;
    }

    public CourrierArrive getCourrierArr() {
        return courrierArr;
    }

    public void setCourrierArr( CourrierArrive courrierArr ) {
        this.courrierArr = courrierArr;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye( Employe employe ) {
        this.employe = employe;
    }
}
