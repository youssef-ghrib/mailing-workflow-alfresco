package com.pfe.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import com.pfe.DAO.EtablissementDAO;
import com.pfe.entities.Etablissement;

@ManagedBean( name = "etablissementMB" )
@ViewScoped
public class EtablissementMB {

    private Etablissement       etablissement          = new Etablissement();

    private List<Etablissement> etablissements         = new ArrayList<Etablissement>();
    private List<Etablissement> filteredEtablissements = new ArrayList<Etablissement>();
private String selectedName=null;
    private EtablissementDAO    etablissementDAO       = new EtablissementDAO();

    // Initialiser le "DataTable" avec la liste des établissements
    public void initDataTable() {
        etablissements = etablissementDAO.lister();
    }

    // Récupérer l'id de l'établissement à modifier et initialiser le formulaire
    // avec ses informations
    public void initForm() throws IOException {
        int id ;
      
        
        try {
			id = Integer.parseInt(FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap()
					.get("id"));

		} catch (java.lang.NumberFormatException ex) {
			// erreur form_id
			id = 0;
			FacesContext.getCurrentInstance().getExternalContext()
					.dispatch("/faces/presets/failEtablissement.xhtml");

		}
        etablissement = etablissementDAO.consulter( id );
	
		if ( etablissement == null) {
			

			FacesContext.getCurrentInstance().getExternalContext()
					.dispatch("/faces/presets/failEtablissement.xhtml");

		} 
		else{
			 selectedName=etablissement.getNom();
	    }
		}
        
       

    // Retourner la liste des établissements recherchés par l'utilisateur
    public List<Etablissement> completeEtablissement( String query ) {
        List<Etablissement> etablissements = etablissementDAO.lister();
        List<Etablissement> filteredServices = new ArrayList<Etablissement>();

        for ( int i = 0; i < etablissements.size(); i++ ) {
            Etablissement etablissement = etablissements.get( i );
            if ( etablissement.getNom().toLowerCase().startsWith( query ) ) {
                filteredServices.add( etablissement );
            }
        }
        return filteredServices;
    }

    // Ajouter un etablissements
    public String add() {
        etablissementDAO.ajouter( etablissement );
        return "listerEtablissements.xhtml?faces-redirect=true";
    }

    
	public void validateEtablissement(FacesContext ctx, UIComponent component,
			Object value) throws ValidatorException {
		String name = value.toString();
		boolean result= etablissementDAO.findEtablissement(name);
		System.out.println("selectedName"+selectedName);
		if (result == true && !name.equals(selectedName)) {
			FacesMessage msg = 
					new FacesMessage("Erreur.", 
							"Etablissement deja existe");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				
				
				throw new ValidatorException(msg);

		
		}
		
	}
    // Modifier un établissement
    public String edit() {
        etablissementDAO.modifier( etablissement );
        return "listerEtablissements.xhtml?faces-redirect=true";
    }

    // Supprimer un établissement
    public void delete( ActionEvent event ) {
        etablissement = (Etablissement) event.getComponent().getAttributes().get( "selectedItem" );
        etablissements.remove( etablissement );
        etablissementDAO.supprimer( etablissement );
    }

    // Rechercher des établissements
    public void search( ActionEvent actionEvent ) {
        if ( etablissement == null )
            etablissements = etablissementDAO.lister();
        else
            etablissements = etablissementDAO.select( etablissement.getNom() );
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public void setEtablissement( Etablissement etablissement ) {
        this.etablissement = etablissement;
    }

    public List<Etablissement> getEtablissements() {
        return etablissements;
    }

    public void setEtablissements( List<Etablissement> etablissements ) {
        this.etablissements = etablissements;
    }

    public List<Etablissement> getFilteredEtablissements() {
        return filteredEtablissements;
    }

    public void setFilteredEtablissements( List<Etablissement> filteredEtablissements ) {
        this.filteredEtablissements = filteredEtablissements;
    }
}
