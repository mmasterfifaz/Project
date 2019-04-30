/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.validator;

import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author admin
 */
public class DateValidator implements Validator {

    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {

        Date now = new Date();        
        Date date = (Date) object;
        
        if (date.after(now)) {
            FacesMessage message = new FacesMessage();
            message.setDetail("Only past & current date are available.");
            message.setSummary("Only past & current date are available.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
//            FacesMessage message = new FacesMessage("Only past & current date are available.");
//            message.setSeverity(FacesMessage.SEVERITY_FATAL);
            throw new ValidatorException(message);
        }
    }
}
