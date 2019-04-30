package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.OpOutDAO;
import com.maxelyz.core.model.entity.OpOut;
import com.maxelyz.utils.JSFUtil;
import java.io.IOException;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.bean.*;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.component.UIViewRoot;

@ManagedBean
//@RequestScoped
@ViewScoped

public class OpOutEditController {
    private static Logger log = Logger.getLogger(OpOutEditController.class);
    private static String REDIRECT_PAGE = "opout.jsf";
    private static String SUCCESS = "opout.xhtml?faces-redirect=true";
    private static String FAILURE = "opoutedit.xhtml";

    private OpOut opOut;
    private String mode;
    private String message;

    @ManagedProperty(value="#{opOutDAO}")
    private OpOutDAO opOutDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:listexclusion:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           opOut = new OpOut();
           opOut.setCheckName(true);
           opOut.setName("");
           opOut.setCheckSurname(true);
           opOut.setSurname("");
           opOut.setCheckTelephone(true);
           opOut.setTelephone1("");
           opOut.setTelephone2("");
           opOut.setTelephone3("");
           opOut.setTelephone3("");
           opOut.setIdcard("");
           opOut.setRemark("");
           opOut.setSource("");
           opOut.setEffect("");
           opOut.setRemark("");
           opOut.setAccessLevel("");
           opOut.setLastActivityDate(new Date());
       } else {
           mode = "edit";
           OpOutDAO dao = getOpOutDAO();
           opOut = dao.findOpOut(new Integer(selectedID));
           if (opOut==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           }
       }
    }
    
    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:listexclusion:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:listexclusion:edit"); 
       }
    } 
      
    public String saveAction() {
        String user  = JSFUtil.getUserSession().getUserName();
        Date now = new Date();
        OpOutDAO dao = getOpOutDAO();
        try {
            if (getMode().equals("add")) {
                opOut.setId(null);
                opOut.setCreateDate(now);
                opOut.setCreateBy(user);
                opOut.setUpdateDate(now);
                opOut.setUpdateBy(user);
                dao.create(opOut);
            } else {
                opOut.setUpdateDate(now);
                opOut.setUpdateBy(user);
                dao.edit(opOut);
            }
        } catch (Exception e) {
            log.error(e);
            message = e.getMessage();
            return FAILURE;
        }
        return SUCCESS;

    }

    public String backAction() {
        return SUCCESS;
    }

    public OpOut getOpOut() {
        return opOut;
    }

    public void setOpOut(OpOut opOut) {
        this.opOut = opOut;
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
    
    public void checkNameListener(ValueChangeEvent event) {
        this.opOut.setCheckName((Boolean) event.getNewValue());
        this.opOut.setName("");
    }
    
    public void checkSurnameListener(ValueChangeEvent event) {
        this.opOut.setCheckSurname((Boolean) event.getNewValue());
        this.opOut.setSurname("");
    }
    
    public void checkTelephoneListener(ValueChangeEvent event) {
        this.opOut.setCheckTelephone((Boolean) event.getNewValue());
        this.opOut.setTelephone1("");  
    }

    public OpOutDAO getOpOutDAO() {
        return opOutDAO;
    }

    public void setOpOutDAO(OpOutDAO opOutDAO) {
        this.opOutDAO = opOutDAO;
    }

}
