/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller;

import com.maxelyz.social.model.dao.SoUserSignatureDAO;
import com.maxelyz.social.model.entity.SoUserSignature;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class UserSignatureEditController {
    private static Logger log = Logger.getLogger(UserSignatureEditController.class);
    private static String REDIRECT_PAGE = "usersignature.jsf";
    private static String SUCCESS = "usersignature.xhtml?faces-redirect=true";
    private static String FAILURE = "usersignatureedit.xhtml";

    private SoUserSignature soUserSignature;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{soUserSignatureDAO}")
    private SoUserSignatureDAO soUserSignatureDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("social:signature:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
       messageDup = "";
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           soUserSignature = new SoUserSignature();
           soUserSignature.setEnable(Boolean.TRUE);
           soUserSignature.setStatus(Boolean.TRUE);
       } else {
           mode = "edit";
           SoUserSignatureDAO dao = getSoUserSignatureDAO();
           soUserSignature = dao.findSoUserSignature(new Integer(selectedID));
           if (soUserSignature == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            } 
       }
    }

      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("social:signature:add");
       } else {
 	   return SecurityUtil.isPermitted("social:signature:edit"); 
       }
    }  
        
    public String saveAction() {
        messageDup = "";
        if(checkName(soUserSignature)) {
            SoUserSignatureDAO dao = getSoUserSignatureDAO();
            try {                
                if (getMode().equals("add")) {
                    soUserSignature.setId(null);
                    soUserSignature.setUsers(JSFUtil.getUserSession().getUsers());
                    soUserSignature.setCreateBy(JSFUtil.getUserSession().getUserName());
                    soUserSignature.setCreateDate(new Date());
                    dao.create(soUserSignature);
                } else {
                    soUserSignature.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    soUserSignature.setUpdateDate(new Date());
                    dao.edit(soUserSignature);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return null;
            }
            return SUCCESS;
        } else {
            messageDup = " Name is already taken";
            return null;  
        }
    }

    public Boolean checkName(SoUserSignature soUserSignature) {
        String name = soUserSignature.getName();
        Integer id=0; 
        if(soUserSignature.getId() != null)
            id = soUserSignature.getId();
        SoUserSignatureDAO dao = getSoUserSignatureDAO();
        
        Integer cnt = dao.checkEmailSignatureName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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

    public SoUserSignature getSoUserSignature() {
        return soUserSignature;
    }

    public void setSoUserSignature(SoUserSignature soUserSignature) {
        this.soUserSignature = soUserSignature;
    }

    public SoUserSignatureDAO getSoUserSignatureDAO() {
        return soUserSignatureDAO;
    }

    public void setSoUserSignatureDAO(SoUserSignatureDAO soUserSignatureDAO) {
        this.soUserSignatureDAO = soUserSignatureDAO;
    }

}
