package com.pfe.validators;



import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.pfe.DAO.EmployeDAO;






@FacesValidator("UserNameValidator")
public class UserNameValidator implements Validator{
	
EmployeDAO employeDAO = new EmployeDAO();

	
		
	 
		@Override
		public void validate(FacesContext context, UIComponent component,
				Object value) throws ValidatorException {
	 
		
			
			String username = value.toString();
			boolean result = employeDAO.findUserName(username);
			if (result == true) {
				FacesMessage msg = 
						new FacesMessage("erreur", 
							"nom d'utilisateur deja existe");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					
					
					throw new ValidatorException(msg);

			
			}
			
				
				
	 
			}
	 
		}
 


