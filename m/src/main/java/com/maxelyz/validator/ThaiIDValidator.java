/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author oat
 */
public class ThaiIDValidator implements Validator {
   public void validate(FacesContext context, UIComponent component, Object value) {
       if (value==null) {
           return;
       }
       String id = getDigitsOnly(value.toString());
       if (!checkPID(id)) {
           FacesMessage message = new FacesMessage("Invalid Citizen ID");
           message.setSeverity(FacesMessage.SEVERITY_ERROR);
           throw new ValidatorException(message);
       }
         
   } 
   
   public static boolean checkPID(String id) {
       if (id==null || id.length() != 13) {
           return false;
       }
       int sum = 0;
       for (int i = 0; i < 12; i++) {
           sum += Integer.parseInt(String.valueOf(id.charAt(i))) * (13 - i);
       }
       /* complements(12, sum mod 11) */
       return id.charAt(12) - '0' == ((11 - (sum % 11)) % 10);     
   }
   
   private static String getDigitsOnly(String s) { 
       StringBuilder digitsOnly = new StringBuilder (); 
       char c;
       for(int i = 0; i < s.length (); i++) {
           c = s.charAt (i);
           if (Character.isDigit(c)) {
               digitsOnly.append(c); 
           }
       }
       return digitsOnly.toString (); 
   }
}
