/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.NosaleReasonDAO;
import com.maxelyz.core.model.entity.NosaleReason;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author dev001
 */
@ManagedBean
@RequestScoped
public class NosaleReasonEditController {
    private static Logger log = Logger.getLogger(NosaleReasonEditController.class);
    private static String REDIRECT_PAGE = "nosalereason.jsf";
    private static String SUCCESS = "nosalereason.xhtml?faces-redirect=true";
    private static String FAILURE = "nosalereasonedit.xhtml";
    private NosaleReason nosaleReason;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{nosaleReasonDAO}")
    private NosaleReasonDAO nosaleReasonDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:nosalereason:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
       messageDup = "";
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           nosaleReason = new NosaleReason();
           nosaleReason.setEnable(Boolean.TRUE);
           nosaleReason.setStatus(Boolean.TRUE);

       } else {
           mode = "edit";
           NosaleReasonDAO dao = getNosaleReasonDAO();
           nosaleReason = dao.findNosaleReason(new Integer(selectedID));
           if (nosaleReason==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           }
       }
    }

      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:nosalereason:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:nosalereason:edit"); 
       }
    }  
        
    public String saveAction() {
         messageDup = "";
        if(checkName(nosaleReason)) {
            NosaleReasonDAO dao = getNosaleReasonDAO();
            try {
                if (getMode().equals("add")) {
                    nosaleReason.setId(null);
                    nosaleReason.setEnable(true);
                    nosaleReason.setCreateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    nosaleReason.setCreateDate(new Date());
                    /*nosaleReason.setStatus(true);*/
                    dao.create(nosaleReason);
                } else {
                    nosaleReason.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    nosaleReason.setUpdateDate(new Date());
                    dao.edit(nosaleReason);
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

    public Boolean checkName(NosaleReason nosaleReason) {
        String name = nosaleReason.getName();
        Integer id=0; 
        if(nosaleReason.getId() != null)
            id = nosaleReason.getId();
        NosaleReasonDAO dao = getNosaleReasonDAO();
        
        Integer cnt = dao.checkNosaleReasonName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public NosaleReason getNosaleReason() {
        return nosaleReason;
    }

    public void setNosaleReason(NosaleReason nosaleReason) {
        this.nosaleReason = nosaleReason;
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

    public NosaleReasonDAO getNosaleReasonDAO() {
        return nosaleReasonDAO;
    }

    public void setNosaleReasonDAO(NosaleReasonDAO nosaleReasonDAO) {
        this.nosaleReasonDAO = nosaleReasonDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

}
