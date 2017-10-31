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

import com.pfe.DAO.PersonneExterneDAO;
import com.pfe.DAO.ServiceExterneDAO;
import com.pfe.entities.PersonneExterne;
import com.pfe.entities.ServiceExterne;

@ManagedBean( name = "personneExterneMB" )
@ViewScoped
public class PersonneExterneMB {

    private PersonneExterne       personneExterne           = new PersonneExterne();

    private List<ServiceExterne>  servicesExternes          = new ArrayList<ServiceExterne>();
    private List<PersonneExterne> personnesExternes         = new ArrayList<PersonneExterne>();
    private List<PersonneExterne> filteredPersonnesExternes = new ArrayList<PersonneExterne>();
    private String selectedName ;
    private ServiceExterneDAO     serviceExterneDAO         = new ServiceExterneDAO();
    private PersonneExterneDAO    personneExterneDAO        = new PersonneExterneDAO();
    
    // Initialiser le "SelectOneMenu" avec la liste des personnes externes
    public void initSelectOneMenu() {
        servicesExternes = serviceExterneDAO.lister();
    }

    // Récupérer l'id de la personne externe à modifier et initialiser le
    // formulaire avec ses informations
    public void initForm() throws IOException {
        servicesExternes = serviceExterneDAO.lister();
        int id ;
        try {
			id = Integer.parseInt(FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap()
					.get("id"));

		} catch (java.lang.NumberFormatException ex) {
			// erreur form_id
			id = 0;
			FacesContext.getCurrentInstance().getExternalContext()
					.dispatch("/faces/presets/failPersonne.xhtml");

		}
        personneExterne = personneExterneDAO.consulter( id );
	
		if (   personneExterne == null) {
			

			FacesContext.getCurrentInstance().getExternalContext()
					.dispatch("/faces/presets/failPersonneExterne.xhtml");

		} 
        
       
    }

	public void validateName(FacesContext ctx, UIComponent component,
			Object value) throws ValidatorException {
		
		String name = value.toString();
		if (!name.matches("[a-zA-z]+([ '-][a-zA-Z]+)*")) {
			FacesMessage msg = 
					new FacesMessage("erreur", 
							"Entrer un nom valide");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				
				
				throw new ValidatorException(msg);
		}
		
	}
    // Initialiser le "DataTable" avec la liste des personnes externes
    public void initDataTable() {
        personnesExternes = personneExterneDAO.lister();
    }

    // Retourner la liste des personnes externes recherchés par l'utilisateur
    public List<PersonneExterne> completePersonne( String query ) {
        List<PersonneExterne> servicesExternes = personneExterneDAO.lister();
        List<PersonneExterne> filteredServices = new ArrayList<PersonneExterne>();

        for ( int i = 0; i < servicesExternes.size(); i++ ) {
            PersonneExterne personneExterne = servicesExternes.get( i );
            if ( personneExterne.getNom().toLowerCase().startsWith( query ) ) {
                filteredServices.add( personneExterne );
            }
        }
        return filteredServices;
    }
    
    
	public void validatePersonneExterne(FacesContext ctx, UIComponent component,
			Object value) throws ValidatorException {
		String name = value.toString();
		boolean result= personneExterneDAO.findPersonne(personneExterne);
		if (result == true && !selectedName.equals(name)) {
			FacesMessage msg = 
					new FacesMessage("Erreur.", 
							"Etablissement deja existe");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				
				
				throw new ValidatorException(msg);

		
		}
		
	}
   

    // Ajouter une personne externe
    public String add() {
        personneExterneDAO.ajouter( personneExterne );
        return "listerPersonnesExternes.xhtml?faces-redirect=true";
    }

    // Modifier une personne externe
    public String edit() {
        personneExterneDAO.modifier( personneExterne );
        return "listerPersonnesExternes.xhtml?faces-redirect=true";
    }

    // Supprimer une personne externe
    public void delete( ActionEvent event ) {
        personneExterne = (PersonneExterne) event.getComponent().getAttributes().get( "selectedItem" );
        personneExterneDAO.supprimer( personneExterne );
        personnesExternes.remove( personneExterne );
    }

    // Rechercher des personnes externes
    public void search( ActionEvent actionEvent ) {
        if ( personneExterne == null )
            personnesExternes = personneExterneDAO.lister();
        else
            personnesExternes = personneExterneDAO.select( personneExterne.getNom() );
    }

    public PersonneExterne getPersonneExterne() {
        return personneExterne;
    }

    public void setPersonneExterne( PersonneExterne personneExterne ) {
        this.personneExterne = personneExterne;
    }

    public List<ServiceExterne> getServicesExternes() {
        return servicesExternes;
    }

    public void setServicesExternes( List<ServiceExterne> servicesExternes ) {
        this.servicesExternes = servicesExternes;
    }

    public List<PersonneExterne> getPersonnesExternes() {
        return personnesExternes;
    }

    public void setPersonnesExternes( List<PersonneExterne> personnesExternes ) {
        this.personnesExternes = personnesExternes;
    }

    public List<PersonneExterne> getFilteredPersonnesExternes() {
        return filteredPersonnesExternes;
    }

    public void setFilteredPersonnesExternes( List<PersonneExterne> filteredPersonnesExternes ) {
        this.filteredPersonnesExternes = filteredPersonnesExternes;
    }
}
