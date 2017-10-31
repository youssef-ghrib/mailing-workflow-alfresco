package com.pfe.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class CourrierDepart {

    @Id
    private String            ref;
  
    private String          statut;
    private String          objet;

    @Temporal( TemporalType.DATE )
    private Date            dateEnvoi;
    private String          tags;
    private String          priorite;

    @ManyToOne
    private Employe         empSource;

    @ManyToOne
    private ServiceInterne  serviceIntSource;

    @ManyToOne
    private Etablissement   etablissementDest;

    @ManyToOne
    private ServiceExterne  serviceExtDest;

    @ManyToOne
    private PersonneExterne personneExtDest;

  

    public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
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

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi( Date dateEnvoi ) {
        this.dateEnvoi = dateEnvoi;
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

    public Employe getEmpSource() {
        return empSource;
    }

    public void setEmpSource( Employe empSource ) {
        this.empSource = empSource;
    }

    public ServiceInterne getServiceIntSource() {
        return serviceIntSource;
    }

    public void setServiceIntSource( ServiceInterne serviceIntSource ) {
        this.serviceIntSource = serviceIntSource;
    }

    public Etablissement getEtablissementDest() {
        return etablissementDest;
    }

    public void setEtablissementDest( Etablissement etablissementDest ) {
        this.etablissementDest = etablissementDest;
    }

    public ServiceExterne getServiceExtDest() {
        return serviceExtDest;
    }

    public void setServiceExtDest( ServiceExterne serviceExtDest ) {
        this.serviceExtDest = serviceExtDest;
    }

    public PersonneExterne getPersonneExtDest() {
        return personneExtDest;
    }

    public void setPersonneExtDest( PersonneExterne personneExtDest ) {
        this.personneExtDest = personneExtDest;
    }
}
