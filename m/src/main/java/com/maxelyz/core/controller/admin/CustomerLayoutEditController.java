/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.CustomerLayoutDAO;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import com.maxelyz.core.model.entity.CustomerLayout;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class CustomerLayoutEditController implements Serializable {
    private static Logger log = Logger.getLogger(CustomerLayoutEditController.class);
    private static String SUCCESS = "customerlayout.xhtml?faces-redirect=true";
    private static String FAILURE = "customerlayoutedit.xhtml";
    private static String REDIRECT_PAGE = "customerlayout.jsf";
    
    private CustomerLayout customerLayout;
    private String mode;
    private String message;
    private String messageDup;
            
    @ManagedProperty(value="#{customerLayoutDAO}")
    private CustomerLayoutDAO customerLayoutDAO;
    
    @PostConstruct
    public void initialize() {
       if (!SecurityUtil.isPermitted("admin:customerlayout:edit")) {
            SecurityUtil.redirectUnauthorize();  
       }
       
       messageDup = "";                
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");
       if (selectedID == null || selectedID.isEmpty()) {
           mode = "add";
           customerLayout = new CustomerLayout();
       } else {
           mode = "edit";
           CustomerLayoutDAO dao = getCustomerLayoutDAO();
           customerLayout = dao.findCustomerLayout(new Integer(selectedID));
           if (customerLayout == null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           }
       }

    }

    public String backAction() {
        return SUCCESS;
    }
    
    public String saveAction() {
        messageDup = "";        
        String username = JSFUtil.getUserSession().getUserName();
        Date today = new Date();
        if(checkName(customerLayout)) {
            CustomerLayoutDAO dao = getCustomerLayoutDAO();
            try {                
                customerLayout.setUpdateBy(username);
                customerLayout.setUpdateDate(today);
                if (getMode().equals("add")) {                    
                    customerLayout.setId(null);
                    customerLayout.setCreateBy(username);
                    customerLayout.setCreateDate(today);
                    customerLayout.setEnable(Boolean.TRUE);                    
                    customerLayout.setIsDefault(Boolean.FALSE);
                    dao.create(customerLayout);
                } else {
                    dao.edit(customerLayout);
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
    
    public Boolean checkName(CustomerLayout customerLayout) {
        String name = customerLayout.getName();
        Integer id = 0; 
        if(customerLayout.getId() != null)
            id = customerLayout.getId();
        
        CustomerLayoutDAO dao = getCustomerLayoutDAO();        
        Integer cnt = dao.checkCustomerLayoutName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public boolean isSavePermitted() {
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
  	if (selectedID==null || selectedID.isEmpty()) {
           return SecurityUtil.isPermitted("admin:customerlayout:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:customerlayout:edit"); 
       }
    } 

    public CustomerLayout getCustomerLayout() {
        return customerLayout;
    }

    public void setCustomerLayout(CustomerLayout customerLayout) {
        this.customerLayout = customerLayout;
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

    // Get-Set DAO
    public CustomerLayoutDAO getCustomerLayoutDAO() {
        return customerLayoutDAO;
    }

    public void setCustomerLayoutDAO(CustomerLayoutDAO customerLayoutDAO) {
        this.customerLayoutDAO = customerLayoutDAO;
    }
    
}
