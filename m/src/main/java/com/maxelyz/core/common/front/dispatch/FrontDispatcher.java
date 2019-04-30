/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.common.front.dispatch;

import com.maxelyz.core.controller.share.QaFormController;
import com.maxelyz.utils.JSFUtil;
import javax.faces.context.FacesContext;

/**
 *
 * @author DevTeam
 */ 

public class FrontDispatcher {         
    public static void dispatch(FrontPouch pouch) throws Exception{
        keepPouch(pouch);
        FacesContext.getCurrentInstance().getExternalContext().dispatch(pouch.getReceiverPage());
    }
    public static void keepPouch(FrontPouch pouch) throws Exception{
        pouch.validatePouchBeforeSending();
        JSFUtil.getRequest().setAttribute(pouch.getClass().getName(), pouch);
        // Addtional
        if (pouch instanceof RegistrationPouch ){
            keepRegistrationPouch4QaFormCtl(pouch);
        }
        
    }
    
    public static String getPouchReceiver(FrontPouch pouch)throws Exception{
        return pouch.getReceiverPage();
    }
    
    public static FrontPouch pickupPouch(Class pouchClass){
        return pickupPouch(pouchClass.getName());
    } 
    
    public static FrontPouch pickupPouch(String pouchClassName){
        FrontPouch pouch = (FrontPouch)JSFUtil.getRequest().getAttribute(pouchClassName);
        JSFUtil.getRequest().removeAttribute(pouchClassName);
        return pouch;
    }
    
    public static RegistrationPouch pickupRegistrationPouch(){
        return (RegistrationPouch)pickupPouch(RegistrationPouch.class);
    }
    
    public static RegistrationPouch pickupRegistrationPouch4Child(){
        return (RegistrationPouch)pickupPouch(RegistrationPouch.class.getName()+"$"+QaFormController.class.getName()); 
    }   
    
    public static RegistrationPouch retreiveRegistrationPouch4Child(){
        return (RegistrationPouch)JSFUtil.getRequest().getAttribute(RegistrationPouch.class.getName()+"$"+QaFormController.class.getName()); 
    }   
    
    /***********************************************/
    protected static void keepRegistrationPouch4QaFormCtl(FrontPouch pouch){
        JSFUtil.getRequest().setAttribute(RegistrationPouch.class.getName()+"$"+QaFormController.class.getName(), pouch);
    }
}
