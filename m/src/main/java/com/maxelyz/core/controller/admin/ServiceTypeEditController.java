/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ServiceTypeDAO;
import com.maxelyz.core.model.entity.ServiceType;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class ServiceTypeEditController {
    private static Logger log = Logger.getLogger(ServiceTypeEditController.class);
    private static String REDIRECT_PAGE = "servicetype.jsf";
    private static String SUCCESS = "servicetype.xhtml?faces-redirect=true";
    private static String FAILURE = "servicetypeedit.xhtml";

    private ServiceType serviceType;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value = "#{serviceTypeDAO}")
    private ServiceTypeDAO serviceTypeDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:servicetype:view")) 
        {
            SecurityUtil.redirectUnauthorize();  
        }
        
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           serviceType = new ServiceType();
           serviceType.setEnable(Boolean.TRUE);
           serviceType.setStatus(Boolean.TRUE);
       } else {
           mode = "edit";
           ServiceTypeDAO dao = getServiceTypeDAO();
           serviceType = dao.findServiceType(new Integer(selectedID));
           if (serviceType==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           }
       }
    }


      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:servicetype:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:servicetype:edit"); 
       }
    } 
        
    public String saveAction() {
        messageDup = "";
        if(checkName(serviceType)) {
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            ServiceTypeDAO dao = getServiceTypeDAO();
            try {
                if (getMode().equals("add")) {
                    serviceType.setId(null);
                    serviceType.setCreateBy(username);
                    serviceType.setCreateDate(now);
                    dao.create(serviceType);
                } else {
                    serviceType.setUpdateBy(username);
                    serviceType.setUpdateDate(now);
                    dao.edit(serviceType);
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

    public Boolean checkName(ServiceType serviceType) {
        String name = serviceType.getName();
        Integer id=0; 
        if(serviceType.getId() != null)
            id = serviceType.getId();
        ServiceTypeDAO dao = getServiceTypeDAO();
        
        Integer cnt = dao.checkServiceTypeName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
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

    public ServiceTypeDAO getServiceTypeDAO() {
        return serviceTypeDAO;
    }

    public void setServiceTypeDAO(ServiceTypeDAO serviceTypeDAO) {
        this.serviceTypeDAO = serviceTypeDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
    
}
