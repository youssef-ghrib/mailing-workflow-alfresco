package com.pfe.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.pfe.DAO.ServiceInterneDAO;
import com.pfe.entities.ServiceInterne;

@FacesConverter( "serviceConverter" )
public class ServiceConverter implements Converter {

    public Object getAsObject( FacesContext fc, UIComponent uic, String value ) {
        if ( value != null && value.trim().length() > 0 ) {
            try {
                ServiceInterneDAO serviceInterneDAO = new ServiceInterneDAO();
                return serviceInterneDAO.consulter( Integer.parseInt( value ) );
            } catch ( Exception e ) {
              
                return null;
            }
        }
        else {
            return null;
        }
    }

    public String getAsString( FacesContext fc, UIComponent uic, Object object ) {
        if ( !object.equals("") && object != null  )  {
            return String.valueOf( ( (ServiceInterne) object ).getId() );
        }
        else {
            return null;
        }
    }
}
