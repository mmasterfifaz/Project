/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.social.model.dao.LogoffTypeDAO;
import com.maxelyz.core.model.entity.LogoffType;
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
public class LogoffReasonEditController {
    private static Logger log = Logger.getLogger(LogoffReasonEditController.class);
    private static String REDIRECT_PAGE = "logoffreason.jsf";
    private static String SUCCESS = "logoffreason.xhtml?faces-redirect=true";
    private static String FAILURE = "logoffreasonedit.xhtml";

    private LogoffType logoffType;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{logoffTypeDAO}")
    private LogoffTypeDAO logoffTypeDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:logoffReason:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
       messageDup = "";
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           logoffType = new LogoffType();
           logoffType.setEnable(Boolean.TRUE);
           logoffType.setStatus(Boolean.TRUE);
       } else {
           mode = "edit";
           LogoffTypeDAO dao = getLogoffTypeDAO();
           logoffType = dao.findLogoffType(new Integer(selectedID));
           if (logoffType == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            } 
       }
    }

      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:logoffReason:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:logoffReason:edit"); 
       }
    }  
        
    public String saveAction() {
        messageDup = "";
        if(checkCode(logoffType)) {
            LogoffTypeDAO dao = getLogoffTypeDAO();
            try {                
                if (getMode().equals("add")) {
                    logoffType.setId(null);
                    logoffType.setCreateBy(JSFUtil.getUserSession().getUserName());
                    logoffType.setCreateDate(new Date());
                    dao.create(logoffType);
                } else {
                    logoffType.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    logoffType.setUpdateDate(new Date());
                    dao.edit(logoffType);
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

    public Boolean checkCode(LogoffType logoffType) {
        String code = logoffType.getCode();
        Integer id=0; 
        if(logoffType.getId() != null)
            id = logoffType.getId();
        LogoffTypeDAO dao = getLogoffTypeDAO();
        
        Integer cnt = dao.checkCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
       
    public String backAction() {
        return SUCCESS;
    }

    public LogoffType getLogoffType() {
        return logoffType;
    }

    public void setLogoffType(LogoffType logoffType) {
        this.logoffType = logoffType;
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

    public LogoffTypeDAO getLogoffTypeDAO() {
        return logoffTypeDAO;
    }

    public void setLogoffTypeDAO(LogoffTypeDAO logoffTypeDAO) {
        this.logoffTypeDAO = logoffTypeDAO;
    }
}
