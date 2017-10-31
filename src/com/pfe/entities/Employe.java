package com.pfe.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Employe {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int            id;
    private String         nom;
    private String         prenom;
    private String         email;
    private String         username;
    private String         password;
    private String         role;
    @Column(name="LANGUAGE")
    private String         language="fr";
    private boolean        firstLogin;
    @ManyToOne
    private ServiceInterne serviceInt;

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    
    public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole( String role ) {
        this.role = role;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin( boolean firstLogin ) {
        this.firstLogin = firstLogin;
    }

    public ServiceInterne getServiceInt() {
        return serviceInt;
    }

    public void setServiceInt( ServiceInterne serviceInt ) {
        this.serviceInt = serviceInt;
    }
}
