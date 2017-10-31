package com.pfe.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Fichier {

	@Id
	 @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id ;
    private String         chemin;

    @OneToOne(cascade=CascadeType.ALL)
    private CourrierArrive courrierArrive;
    
    @OneToOne
    private CourrierDepart courrierDepart;



    public CourrierDepart getCourrierDepart() {
		return courrierDepart;
	}

	public void setCourrierDepart(CourrierDepart courrierDepart) {
		this.courrierDepart = courrierDepart;
	}

	
	
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getChemin() {
        return chemin;
    }

    public void setChemin( String chemin ) {
        this.chemin = chemin;
    }

	public CourrierArrive getCourrierArrive() {
		return courrierArrive;
	}

	public void setCourrierArrive(CourrierArrive courrierArrive) {
		this.courrierArrive = courrierArrive;
	}

    
}
