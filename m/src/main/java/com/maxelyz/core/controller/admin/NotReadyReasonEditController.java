/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.social.model.dao.SoUserNotReadyReasonDAO;
import com.maxelyz.social.model.entity.SoUserNotReadyReason;
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
public class NotReadyReasonEditController {
    private static Logger log = Logger.getLogger(NotReadyReasonEditController.class);
    private static String REDIRECT_PAGE = "notreadyreason.jsf";
    private static String SUCCESS = "notreadyreason.xhtml?faces-redirect=true";
    private static String FAILURE = "notreadyreasonedit.xhtml";

    private SoUserNotReadyReason soUserNotReadyReason;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{soUserNotReadyReasonDAO}")
    private SoUserNotReadyReasonDAO soUserNotReadyReasonDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:notreadyReason:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
       messageDup = "";
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           soUserNotReadyReason = new SoUserNotReadyReason();
           soUserNotReadyReason.setEnable(Boolean.TRUE);
           soUserNotReadyReason.setStatus(Boolean.TRUE);
       } else {
           mode = "edit";
           SoUserNotReadyReasonDAO dao = getSoUserNotReadyReasonDAO();
           soUserNotReadyReason = dao.findSoUserNotReadyReason(new Integer(selectedID));
           if (soUserNotReadyReason == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            } 
       }
    }

      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:notreadyReason:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:notreadyReason:edit"); 
       }
    }  
        
    public String saveAction() {
        messageDup = "";
        if(checkCode(soUserNotReadyReason)) {
            SoUserNotReadyReasonDAO dao = getSoUserNotReadyReasonDAO();
            try {                
                if (getMode().equals("add")) {
                    soUserNotReadyReason.setId(null);
                    soUserNotReadyReason.setCreateBy(JSFUtil.getUserSession().getUserName());
                    soUserNotReadyReason.setCreateDate(new Date());
                    dao.create(soUserNotReadyReason);
                } else {
                    soUserNotReadyReason.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    soUserNotReadyReason.setUpdateDate(new Date());
                    dao.edit(soUserNotReadyReason);
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

    public Boolean checkCode(SoUserNotReadyReason soUserNotReadyReason) {
        String code = soUserNotReadyReason.getCode();
        Integer id=0; 
        if(soUserNotReadyReason.getId() != null)
            id = soUserNotReadyReason.getId();
        SoUserNotReadyReasonDAO dao = getSoUserNotReadyReasonDAO();
        
        Integer cnt = dao.checkCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
       
    public String backAction() {
        return SUCCESS;
    }

    public SoUserNotReadyReason getSoUserNotReadyReason() {
        return soUserNotReadyReason;
    }

    public void setSoUserNotReadyReason(SoUserNotReadyReason soUserNotReadyReason) {
        this.soUserNotReadyReason = soUserNotReadyReason;
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

    public SoUserNotReadyReasonDAO getSoUserNotReadyReasonDAO() {
        return soUserNotReadyReasonDAO;
    }

    public void setSoUserNotReadyReasonDAO(SoUserNotReadyReasonDAO soUserNotReadyReasonDAO) {
        this.soUserNotReadyReasonDAO = soUserNotReadyReasonDAO;
    }

}
