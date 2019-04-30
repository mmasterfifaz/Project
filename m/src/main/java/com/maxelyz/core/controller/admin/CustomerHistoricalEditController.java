/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;
import com.maxelyz.core.model.dao.CustomerHistoricalGroupDAO;
import com.maxelyz.core.model.entity.CustomerHistoricalGroup;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.*;
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
//@RequestScoped
@ViewScoped
public class CustomerHistoricalEditController implements Serializable {
    private static Logger log = Logger.getLogger(CustomerHistoricalEditController.class);
    private static String REDIRECT_PAGE = "customerhistorical.jsf";
    private static String REFRESH = "customerhistoricaledit.xhtml?faces-redirect=true";
    private static String SUCCESS = "customerhistorical.xhtml?faces-redirect=true";
    private static String FAILURE = "customerhistoricaledit.xhtml";
    private static String BACK = "customerhistorical.xhtml";
    private CustomerHistoricalGroup customerHistoricalGroup;
    private String message;
    @ManagedProperty(value = "#{customerHistoricalGroupDAO}")
    private CustomerHistoricalGroupDAO customerHistoricalGroupDAO;
    
    @PostConstruct
    public void initialize() {
       if (!SecurityUtil.isPermitted("admin:customerhistorical:view")) {  
            SecurityUtil.redirectUnauthorize();  
        }
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            JSFUtil.redirect(REDIRECT_PAGE);
        } 
        else {
            customerHistoricalGroup = this.getCustomerHistoricalGroupDAO().findCustomerHistoricalGroup(new Integer(selectedID));
        }
    }
    
    public boolean isSavePermitted() {
        return SecurityUtil.isPermitted("admin:customerhistorical:edit");
    }
    
    public void initListener() {
        initialize();
        System.out.println("init initial");
    }
    
    public String saveAction() {
        try {
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            customerHistoricalGroup.setUpdateBy(username);
            customerHistoricalGroup.setUpdateDate(now);
            this.getCustomerHistoricalGroupDAO().edit(customerHistoricalGroup);
        } catch (Exception e) {
            log.error(e);
            message = e.getMessage();
            return FAILURE;
        }
        return SUCCESS;
    }
    
    public String backAction() {
        return BACK;
    }
    
    // Set properties
    public CustomerHistoricalGroup getCustomerHistoricalGroup() {
        return customerHistoricalGroup;
    }

    public void setCustomerHistoricalGroup(CustomerHistoricalGroup customerHistoricalGroup) {
        this.customerHistoricalGroup = customerHistoricalGroup;
    }
    
    public CustomerHistoricalGroupDAO getCustomerHistoricalGroupDAO() {
        return customerHistoricalGroupDAO;
    }

    public void setCustomerHistoricalGroupDAO(CustomerHistoricalGroupDAO customerHistoricalGroupDAO) {
        this.customerHistoricalGroupDAO = customerHistoricalGroupDAO;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
