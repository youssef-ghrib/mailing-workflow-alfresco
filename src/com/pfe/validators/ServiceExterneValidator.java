package com.pfe.validators;



import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.pfe.DAO.ServiceExterneDAO;





@FacesValidator("ServiceExterneValidator")
public class ServiceExterneValidator implements Validator{
	
	
	ServiceExterneDAO serviceExterneDAO = new ServiceExterneDAO();
		
	 
		@Override
		public void validate(FacesContext context, UIComponent component,
				Object value) throws ValidatorException {
	 
		
				String name = value.toString();
				boolean result=serviceExterneDAO.findService(name);
				if (result == true) {
					FacesMessage msg = 
							new FacesMessage("erreur.", 
									"Service deja existe");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						
						
						throw new ValidatorException(msg);

				
				}
				
	 
			}
	 
		}
 


