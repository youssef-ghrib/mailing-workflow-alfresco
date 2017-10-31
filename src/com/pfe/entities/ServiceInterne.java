package com.pfe.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.*;

@Entity
public class ServiceInterne {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int    id;
    private String nom;
  
    @OneToMany(mappedBy="serviceInt",cascade = CascadeType.ALL)
	private List<Employe>employes;

    
    public List<Employe> getEmployes() {
		return employes;
	}

	public void setEmployes(List<Employe> employes) {
		this.employes = employes;
	}

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
}
