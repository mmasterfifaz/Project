/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import com.maxelyz.social.model.dao.SoIgnoreReasonDAO;
import com.maxelyz.social.model.entity.SoIgnoreReason;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class IgnoreReasonEditController {
    private static Logger log = Logger.getLogger(IgnoreReasonEditController.class);
    private static String REDIRECT_PAGE = "ignorereason.jsf";
    private static String SUCCESS = "ignorereason.xhtml?faces-redirect=true";
    private static String FAILURE = "ignorereasonedit.xhtml";

    private SoIgnoreReason soIgnoreReason;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{soIgnoreReasonDAO}")
    private SoIgnoreReasonDAO soIgnoreReasonDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:ignoreReason:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
       messageDup = "";
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           soIgnoreReason = new SoIgnoreReason();
           soIgnoreReason.setEnable(Boolean.TRUE);
           soIgnoreReason.setStatus(Boolean.TRUE);
       } else {
           mode = "edit";
           SoIgnoreReasonDAO dao = getSoIgnoreReasonDAO();
           soIgnoreReason = dao.findSoIgnoreReason(new Integer(selectedID));
           if (soIgnoreReason == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            } 
       }
    }

      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:ignoreReason:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:ignoreReason:edit"); 
       }
    }  
        
    public String saveAction() {
        messageDup = "";
        if(checkCode(soIgnoreReason)) {
            SoIgnoreReasonDAO dao = getSoIgnoreReasonDAO();
            try {                
                if (getMode().equals("add")) {
                    soIgnoreReason.setId(null);
                    soIgnoreReason.setCreateBy(JSFUtil.getUserSession().getUserName());
                    soIgnoreReason.setCreateDate(new Date());
                    dao.create(soIgnoreReason);
                } else {
                    soIgnoreReason.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    soIgnoreReason.setUpdateDate(new Date());
                    dao.edit(soIgnoreReason);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return null;
            }
            return SUCCESS;
        } else {
            messageDup = " Code is already taken";
            return null;  
        }
    }

    public Boolean checkCode(SoIgnoreReason soIgnoreReason) {
        String code = soIgnoreReason.getCode();
        Integer id=0; 
        if(soIgnoreReason.getId() != null)
            id = soIgnoreReason.getId();
        SoIgnoreReasonDAO dao = getSoIgnoreReasonDAO();
        
        Integer cnt = dao.checkCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
       
    public String backAction() {
        return SUCCESS;
    }

    public SoIgnoreReason getSoIgnoreReason() {
        return soIgnoreReason;
    }

    public void setSoIgnoreReason(SoIgnoreReason soIgnoreReason) {
        this.soIgnoreReason = soIgnoreReason;
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

    public SoIgnoreReasonDAO getSoIgnoreReasonDAO() {
        return soIgnoreReasonDAO;
    }

    public void setSoIgnoreReasonDAO(SoIgnoreReasonDAO soIgnoreReasonDAO) {
        this.soIgnoreReasonDAO = soIgnoreReasonDAO;
    }    
}
