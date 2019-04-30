/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author admin
 */
public class DateBeforeValidator implements Validator {

    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00.0", Locale.US);
        if(object != null){
            try{
                Date now = (Date) formatter.parse(formatter.format(new Date()));
                Date date = (Date) formatter.parse(formatter.format((Date) object));

                if (date.before(now)) {
                    FacesMessage message = new FacesMessage();
                    message.setDetail("Only future & current date are available.");
                    message.setSummary("Only future & current date are available.");
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
        //            FacesMessage message = new FacesMessage("Only past & current date are available.");
        //            message.setSeverity(FacesMessage.SEVERITY_FATAL);
                    throw new ValidatorException(message);
                }
            }catch(ParseException e){
            }
        }
    }
}
