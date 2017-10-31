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
import com.pfe.DAO.ServiceInterneDAO;





@FacesValidator("ServiceInterneValidator")
public class ServiceInterneValidator implements Validator{
	
	
	ServiceInterneDAO serviceInterneDAO = new ServiceInterneDAO();
		
	 
		@Override
		public void validate(FacesContext context, UIComponent component,
				Object value) throws ValidatorException {
	 
		
				String name = value.toString();
				boolean result= serviceInterneDAO.findService(name);
				if (result == true) {
					FacesMessage msg = 
							new FacesMessage("erreur.", 
									"service deja existe");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						
						
						throw new ValidatorException(msg);

				
				}
				
	 
			}
	 
		}
 


