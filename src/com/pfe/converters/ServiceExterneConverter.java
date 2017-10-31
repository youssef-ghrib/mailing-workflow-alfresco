package com.pfe.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.pfe.DAO.ServiceExterneDAO;
import com.pfe.entities.ServiceExterne;

@FacesConverter( "serviceExterneConverter" )
public class ServiceExterneConverter implements Converter {

    public Object getAsObject( FacesContext fc, UIComponent uic, String value ) {
        if ( value != null && value.trim().length() > 0 ) {
            try {
                ServiceExterneDAO serviceExterneDAO = new ServiceExterneDAO();
                return serviceExterneDAO.consulter( Integer.parseInt( value ) );
            } catch ( Exception e ) {
            	
                return null;
            }
        }
        else {
            return null;
        }
    }

    public String getAsString( FacesContext fc, UIComponent uic, Object object ) {
        if ( object != null && !object.equals("")) {
            return String.valueOf( ( (ServiceExterne) object ).getId() );
        }
        else {
            return null;
        }
    }
}
