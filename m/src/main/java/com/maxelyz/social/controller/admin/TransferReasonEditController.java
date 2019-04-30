/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import com.maxelyz.social.model.dao.SoTransferReasonDAO;
import com.maxelyz.social.model.entity.SoTransferReason;
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
public class TransferReasonEditController {
    private static Logger log = Logger.getLogger(TransferReasonEditController.class);
    private static String REDIRECT_PAGE = "transferreason.jsf";
    private static String SUCCESS = "transferreason.xhtml?faces-redirect=true";
    private static String FAILURE = "transferreasonedit.xhtml";

    private SoTransferReason soTransferReason;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{soTransferReasonDAO}")
    private SoTransferReasonDAO soTransferReasonDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:transferReason:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
       messageDup = "";
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           soTransferReason = new SoTransferReason();
           soTransferReason.setEnable(Boolean.TRUE);
           soTransferReason.setStatus(Boolean.TRUE);
       } else {
           mode = "edit";
           SoTransferReasonDAO dao = getSoTransferReasonDAO();
           soTransferReason = dao.findSoTransferReason(new Integer(selectedID));
           if (soTransferReason == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            } 
       }
    }

      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:transferReason:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:transferReason:edit"); 
       }
    }  
        
    public String saveAction() {
        messageDup = "";
        if(checkCode(soTransferReason)) {
            SoTransferReasonDAO dao = getSoTransferReasonDAO();
            try {                
                if (getMode().equals("add")) {
                    soTransferReason.setId(null);
                    soTransferReason.setCreateBy(JSFUtil.getUserSession().getUserName());
                    soTransferReason.setCreateDate(new Date());
                    dao.create(soTransferReason);
                } else {
                    soTransferReason.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    soTransferReason.setUpdateDate(new Date());
                    dao.edit(soTransferReason);
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

    public Boolean checkCode(SoTransferReason soTransferReason) {
        String code = soTransferReason.getCode();
        Integer id=0; 
        if(soTransferReason.getId() != null)
            id = soTransferReason.getId();
        SoTransferReasonDAO dao = getSoTransferReasonDAO();
        
        Integer cnt = dao.checkCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
       
    public String backAction() {
        return SUCCESS;
    }

    public SoTransferReason getSoTransferReason() {
        return soTransferReason;
    }

    public void setSoTransferReason(SoTransferReason soTransferReason) {
        this.soTransferReason = soTransferReason;
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

    public SoTransferReasonDAO getSoTransferReasonDAO() {
        return soTransferReasonDAO;
    }

    public void setSoTransferReasonDAO(SoTransferReasonDAO soTransferReasonDAO) {
        this.soTransferReasonDAO = soTransferReasonDAO;
    }
}
