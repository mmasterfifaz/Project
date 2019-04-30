/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.apache.log4j.Logger;

/**
 *
 * @author oat
 */
public class DateStringConverter implements Converter {
    private static Logger log = Logger.getLogger(DateStringConverter.class);
    private String pattern = "dd/MM/yyyy HH:mm";
                   
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
            throws ConverterException {
        //String pattern = (String) component.getAttributes().get("datePattern");
        //String locale = (String) component.getAttributes().get("locale");
 
        String result = "";   
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, new Locale("th","TH"));
        if(value!= null && value.length() > 0) {
            try {
                Date date = sdf.parse(value);
                result = sdf.format(date);
            } catch (Exception e) {
                Date date = new Date();
                //log.error(e.getMessage());
                FacesMessage facesMessage = new FacesMessage("Invalid Date", value + " is an invalid date. Example " + sdf.format(date));
                FacesContext.getCurrentInstance().addMessage("DATE PARSE ERROR", facesMessage);
            }
        }

        return result;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
            throws ConverterException {
        //String pattern = (String) component.getAttributes().get("datePattern");
        //String locale = (String) component.getAttributes().get("locale");
 
        String result = "";
        String valueStr = (String) value;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, new Locale("th","TH"));
        if (valueStr!= null && valueStr.length() > 0) {
            try {
                Date date = sdf.parse(valueStr);
                result = sdf.format(date);
            } catch (Exception e) {
                //log.error(e.getMessage());
                Date date = new Date();
                FacesMessage facesMessage = new FacesMessage("Invalid Date", value + " is an invalid date. Example " + sdf.format(date));
                FacesContext.getCurrentInstance().addMessage("DATE PARSE ERROR", facesMessage);
            }
        }
        return result;
    }

    }