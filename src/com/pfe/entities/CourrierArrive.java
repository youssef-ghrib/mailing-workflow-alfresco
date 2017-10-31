package com.pfe.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class CourrierArrive {

    @Id
    private String ref ;
    private String     refExp;
  
    private String          statut;
    private String          objet;
   
   
    @Temporal( TemporalType.DATE )
    @Column(name="DATERECEPTION")
    private Date            dateReception;
    private String          tags;
    private String          priorite;

    @ManyToOne
    private CourrierDepart  courrierDep;

    @ManyToOne
    private Employe         empDest;

    @ManyToOne
    private ServiceInterne  serviceIntDest;

    @ManyToOne
    private Etablissement   etablissementSource;

    @ManyToOne
    private ServiceExterne  serviceExtSource;

    @ManyToOne
    private PersonneExterne personneExtSource;

  

   

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getRefExp() {
		return refExp;
	}

	public void setRefExp(String refExp) {
		this.refExp = refExp;
	}


    public String getStatut() {
        return statut;
    }

    public void setStatut( String statut ) {
        this.statut = statut;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet( String objet ) {
        this.objet = objet;
    }

    public Date getDateReception() {
        return dateReception;
    }

    public void setDateReception( Date dateReception ) {
        this.dateReception = dateReception;
    }

    public String getTags() {
        return tags;
    }

    public void setTags( String tags ) {
        this.tags = tags;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite( String priorite ) {
        this.priorite = priorite;
    }

    public CourrierDepart getCourrierDep() {
        return courrierDep;
    }

    public void setCourrierDep( CourrierDepart courrierDep ) {
        this.courrierDep = courrierDep;
    }

    public Employe getEmpDest() {
        return empDest;
    }

    public void setEmpDest( Employe empDest ) {
        this.empDest = empDest;
    }

    public ServiceInterne getServiceIntDest() {
        return serviceIntDest;
    }

    public void setServiceIntDest( ServiceInterne serviceIntDest ) {
        this.serviceIntDest = serviceIntDest;
    }

    public Etablissement getEtablissementSource() {
        return etablissementSource;
    }

    public void setEtablissementSource( Etablissement etablissementSource ) {
        this.etablissementSource = etablissementSource;
    }

    public ServiceExterne getServiceExtSource() {
        return serviceExtSource;
    }

    public void setServiceExtSource( ServiceExterne serviceExtSource ) {
        this.serviceExtSource = serviceExtSource;
    }

    public PersonneExterne getPersonneExtSource() {
        return personneExtSource;
    }

    public void setPersonneExtSource( PersonneExterne personneExtSource ) {
        this.personneExtSource = personneExtSource;
    }
}
