/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.PhoneDirectoryCategoryDAO;
import com.maxelyz.core.model.entity.PhoneDirectoryCategory;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;
/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class PhoneCategoryEditController {
        
    private static Logger log = Logger.getLogger(PhoneDirectoryEditController.class);
    private static String REDIRECT_PAGE = "phonecategory.jsf";
    private static String SUCCESS = "phonecategory.xhtml?faces-redirect=true";
    private static String FAILURE = "phonecategoryedit.xhtml";
    private PhoneDirectoryCategory phoneDirectoryCategory;
    private String mode;
    private String message;
    private String messageDup;
    
    @ManagedProperty(value = "#{phoneDirectoryCategoryDAO}")
    private PhoneDirectoryCategoryDAO phoneDirectoryCategoryDAO;
    
    @PostConstruct
    public void initialize() {
        
        if (!SecurityUtil.isPermitted("admin:phonecategory:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        message = "";
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            phoneDirectoryCategory = new PhoneDirectoryCategory();
            phoneDirectoryCategory.setEnable(Boolean.TRUE);
            phoneDirectoryCategory.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            PhoneDirectoryCategoryDAO dao = this.getPhoneDirectoryCategoryDAO();
            phoneDirectoryCategory = dao.findPhoneDirectoryCategory(new Integer(selectedID));
            if (phoneDirectoryCategory == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            } 
        }
    }
    
    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:phonecategory:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:phonecategory:edit"); 
       }
    }  
    
    public String saveAction() {
        messageDup = "";
        if(checkName(phoneDirectoryCategory)) {
            try {
                String username = JSFUtil.getUserSession().getUserName();
                Date now = new Date();
                if(mode.equals("add")) {
                    phoneDirectoryCategory.setId(null);
                    phoneDirectoryCategory.setCreateBy(username);
                    phoneDirectoryCategory.setCreateDate(now);
                    this.getPhoneDirectoryCategoryDAO().create(phoneDirectoryCategory);
                } else {
                    phoneDirectoryCategory.setUpdateBy(username);
                    phoneDirectoryCategory.setUpdateDate(now);
                    this.getPhoneDirectoryCategoryDAO().edit(phoneDirectoryCategory);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
         } else {
            messageDup = " Name is already taken";
            return FAILURE;            
        }           
    }
    
    public String backAction() {
        return SUCCESS;
    }
    
    public Boolean checkName(PhoneDirectoryCategory phoneDirectoryCategory) {
        String name = phoneDirectoryCategory.getName();
        Integer id=0; 
        if(phoneDirectoryCategory.getId() != null)
            id = phoneDirectoryCategory.getId();
        PhoneDirectoryCategoryDAO dao = getPhoneDirectoryCategoryDAO();
        
        Integer cnt = dao.checkPhoneDirectoryCategoryName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public PhoneDirectoryCategory getPhoneDirectoryCategory() {
        return phoneDirectoryCategory;
    }

    public void setPhoneDirectoryCategory(PhoneDirectoryCategory phoneDirectoryCategory) {
        this.phoneDirectoryCategory = phoneDirectoryCategory;
    }

    public PhoneDirectoryCategoryDAO getPhoneDirectoryCategoryDAO() {
        return phoneDirectoryCategoryDAO;
    }

    public void setPhoneDirectoryCategoryDAO(PhoneDirectoryCategoryDAO phoneDirectoryCategoryDAO) {
        this.phoneDirectoryCategoryDAO = phoneDirectoryCategoryDAO;
    }

}
