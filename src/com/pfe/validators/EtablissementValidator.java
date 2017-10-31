package com.pfe.validators;



import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.pfe.DAO.EtablissementDAO;
import com.pfe.DAO.ServiceExterneDAO;
import com.pfe.controllers.CourrierArriveMB;
import com.pfe.controllers.EtablissementMB;







@FacesValidator("EtablissementValidator")
public class EtablissementValidator implements Validator{
	

	 @ManagedProperty(value="#{etablissementMB}")
		private EtablissementMB mb;
	 
	EtablissementDAO etablissementDAO = new EtablissementDAO();
		
	 
		public EtablissementMB getMb() {
		return mb;
	}


	public void setMb(EtablissementMB mb) {
		this.mb = mb;
	}


		@Override
		public void validate(FacesContext context, UIComponent component,
				Object value) throws ValidatorException {
	 
		
		
				String name = value.toString();
				boolean result= etablissementDAO.findEtablissement(name);
				if (result == true) {
					FacesMessage msg = 
							new FacesMessage("Erreur.", 
									"Etablissement deja existe");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						
						
						throw new ValidatorException(msg);

				
				}
				
	 
			}
	 
		}
 


