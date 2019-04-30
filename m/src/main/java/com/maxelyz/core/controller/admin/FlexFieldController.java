/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author vee
 */
@ManagedBean
@RequestScoped
public class FlexFieldController {
    private static String EDIT = "flexfieldedit.xhtml";
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:flexfield:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
    }

    public boolean isEditPermitted() {
 	   return SecurityUtil.isPermitted("admin:flexfield:edit"); 
    }  
        
    public String editAction() {
        return EDIT;
    }
}
