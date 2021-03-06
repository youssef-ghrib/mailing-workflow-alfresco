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
import com.pfe.DAO.ServiceExterneDAO;
import com.pfe.entities.Etablissement;
import com.pfe.entities.ServiceExterne;

@ManagedBean( name = "serviceExterneMB" )
@ViewScoped
public class ServiceExterneMB {

    private ServiceExterne       serviceExterne    = new ServiceExterne();

    private List<ServiceExterne> servicesExternes  = new ArrayList<ServiceExterne>();
    private List<ServiceExterne> filteredServices  = new ArrayList<ServiceExterne>();
    private List<Etablissement>  etablissements    = new ArrayList<Etablissement>();
private String selectedName = null;
    private ServiceExterneDAO    serviceExterneDAO = new ServiceExterneDAO();
    private EtablissementDAO     etablissementDAO  = new EtablissementDAO();

    // Initialiser le "SelectOneMenu" avec la liste des services
    public void initSelectOneMenu() {
        etablissements = etablissementDAO.lister();
    }

    // Récupérer l'id du service à modifier et initialiser le formulaire avec
    // ses informations
    public void initForm() throws IOException {
        etablissements = etablissementDAO.lister();
        int id ;
        try {
			id = Integer.parseInt(FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap()
					.get("id"));

		} catch (java.lang.NumberFormatException ex) {
			// erreur form_id
			id = 0;
			FacesContext.getCurrentInstance().getExternalContext()
					.dispatch("/faces/presets/failServiceExterne.xhtml");

		}
        serviceExterne = serviceExterneDAO.consulter( id );
		if (    serviceExterne == null) {
			

			FacesContext.getCurrentInstance().getExternalContext()
					.dispatch("/faces/presets/failServiceExterne.xhtml");

		} 
		else{
			selectedName=serviceExterne.getNom();
		}
       
        
        
       
    }

    // Initialiser le "DataTable" avec la liste des services
    public void initDataTable() {
        servicesExternes = serviceExterneDAO.lister();
    }

    // Retourner la liste des services recherchés par l'utilisateur
    public List<ServiceExterne> completeService( String query ) {
        List<ServiceExterne> servicesExternes = serviceExterneDAO.lister();
        List<ServiceExterne> filteredServices = new ArrayList<ServiceExterne>();

        for ( int i = 0; i < servicesExternes.size(); i++ ) {
            ServiceExterne serviceExterne = servicesExternes.get( i );
            if ( serviceExterne.getNom().toLowerCase().startsWith( query ) ) {
                filteredServices.add( serviceExterne );
            }
        }
        return filteredServices;
    }

	public void validateServiceExterne(FacesContext ctx, UIComponent component,
			Object value) throws ValidatorException {
		
		String name = value.toString();
		boolean result=serviceExterneDAO.findService(name);
		if (result == true &&!name.equals(selectedName)) {
			FacesMessage msg = 
					new FacesMessage("erreur.", 
							"Service deja existe");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				
				
				throw new ValidatorException(msg);

		
		}
		
	}
	
    

    // Ajouter un service
    public String add() {
        serviceExterneDAO.ajouter( serviceExterne );
        return "listerServicesExternes.xhtml?faces-redirect=true";
    }

    // Modifier un service
    public String edit() {
        serviceExterneDAO.modifier( serviceExterne );
        return "listerServicesExternes.xhtml?faces-redirect=true";
    }

    // Supprimer un service
    public void delete( ActionEvent event ) {
        serviceExterne = (ServiceExterne) event.getComponent().getAttributes().get( "selectedItem" );
        servicesExternes.remove( serviceExterne );
        
        serviceExterneDAO.supprimer( serviceExterne );
    }

    // Rechercher des services
    public void search( ActionEvent actionEvent ) {
        if ( serviceExterne == null )
            servicesExternes = serviceExterneDAO.lister();
        else
            servicesExternes = serviceExterneDAO.select( serviceExterne.getNom() );
    }

    public ServiceExterne getServiceExterne() {
        return serviceExterne;
    }

    public void setServiceExterne( ServiceExterne serviceExterne ) {
        this.serviceExterne = serviceExterne;
    }

    public List<ServiceExterne> getServicesExternes() {
        return servicesExternes;
    }

    public void setServicesExternes( List<ServiceExterne> servicesExternes ) {
        this.servicesExternes = servicesExternes;
    }

    public List<ServiceExterne> getFilteredServices() {
        return filteredServices;
    }

    public void setFilteredServices( List<ServiceExterne> filteredServices ) {
        this.filteredServices = filteredServices;
    }

    public List<Etablissement> getEtablissements() {
        return etablissements;
    }

    public void setEtablissements( List<Etablissement> etablissements ) {
        this.etablissements = etablissements;
    }
}
