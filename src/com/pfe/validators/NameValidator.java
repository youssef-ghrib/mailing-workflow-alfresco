package com.pfe.validators;



import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;





@FacesValidator("NameValidator")
public class NameValidator implements Validator{
	
	
	
		
	 
		@Override
		public void validate(FacesContext context, UIComponent component,
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
	 
		}
 


