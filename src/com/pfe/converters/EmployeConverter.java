package com.pfe.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.pfe.DAO.EmployeDAO;
import com.pfe.entities.Employe;

@FacesConverter( "employeConverter" )
public class EmployeConverter implements Converter {

    public Object getAsObject( FacesContext fc, UIComponent uic, String value ) {
        if ( value != null && value.trim().length() > 0 ) {
            try {
                EmployeDAO employeDAO = new EmployeDAO();
                return employeDAO.consulter( Integer.parseInt( value ) );
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
            return String.valueOf( ( (Employe) object ).getId() );
        }
        else {
            return null;
        }
    }
}
