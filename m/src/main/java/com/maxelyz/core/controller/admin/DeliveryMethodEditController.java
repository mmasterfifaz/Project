/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.DeliveryMethodDAO;
import com.maxelyz.core.model.entity.DeliveryMethod;
import org.apache.log4j.Logger;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import javax.faces.bean.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class DeliveryMethodEditController {
    private static Logger log = Logger.getLogger(DeliveryMethodEditController.class);
    private static String REDIRECT_PAGE = "deliverymethod.jsf";
    private static String SUCCESS = "deliverymethod.xhtml?faces-redirect=true";
    private static String FAILURE = "deliverymethodedit.xhtml";

    private DeliveryMethod deliveryMethod;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{deliveryMethodDAO}")
    private DeliveryMethodDAO deliveryMethodDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:deliverymethod:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            deliveryMethod = new DeliveryMethod();
            deliveryMethod.setEnable(true);
            deliveryMethod.setStatus(true);
        } else {
            mode = "edit";
            DeliveryMethodDAO dao = getDeliveryMethodDAO();
            deliveryMethod = dao.findDeliveryMethod(new Integer(selectedID));
            if (deliveryMethod == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }
    }


    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:deliverymethod:add");
        } else {
            return SecurityUtil.isPermitted("admin:deliverymethod:edit");
        }
    }
        
    public String saveAction() {
         messageDup = "";
        if(checkCode(deliveryMethod)) {
            Date today = new Date();
            String userName = JSFUtil.getUserSession().getUserName();
            DeliveryMethodDAO dao = getDeliveryMethodDAO();
            try {
                if (getMode().equals("add")) {
                    deliveryMethod.setId(null);
                    deliveryMethod.setCreateDate(today);
                    deliveryMethod.setCreateBy(userName);
                    dao.create(deliveryMethod);
                } else {
                    deliveryMethod.setUpdateDate(today);
                    deliveryMethod.setUpdateBy(userName);
                    dao.edit(deliveryMethod);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Code is already taken";
            return FAILURE;  
        }
    }

    public Boolean checkCode(DeliveryMethod deliveryMethod) {
        String code = deliveryMethod.getCode();
        Integer id=0; 
        if(deliveryMethod.getId() != null)
            id = deliveryMethod.getId();
        DeliveryMethodDAO dao = getDeliveryMethodDAO();
        
        Integer cnt = dao.checkDeliveryMethodCode(code, id);
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

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public DeliveryMethodDAO getDeliveryMethodDAO() {
        return deliveryMethodDAO;
    }

    public void setDeliveryMethodDAO(DeliveryMethodDAO deliveryMethodDAO) {
        this.deliveryMethodDAO = deliveryMethodDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

}
