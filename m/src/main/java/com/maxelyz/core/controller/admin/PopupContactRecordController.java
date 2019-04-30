/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ContactCaseDAO;
import com.maxelyz.core.model.entity.ContactCase;
import com.maxelyz.core.model.entity.Customer;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Administrator
 */

@ManagedBean
@RequestScoped
@ViewScoped
public class PopupContactRecordController {
    
    private ContactCase contactCase;
    
    @ManagedProperty(value = "#{contactCaseDAO}")
    private ContactCaseDAO contactCaseDAO;
    
    @PostConstruct
    public void initialize() {
        if(JSFUtil.getRequestParameterMap("from") != null) {
            if(JSFUtil.getRequestParameterMap("historyCode") != null) {
                String code = JSFUtil.getRequestParameterMap("historyCode");
                contactCase = contactCaseDAO.findContactCaseByCode(code);
            }
        }
    }
    
    public void popupListener() {
        if(JSFUtil.getRequestParameterMap("from") != null) {
            if(JSFUtil.getRequestParameterMap("historyCode") != null) {
                String code = JSFUtil.getRequestParameterMap("historyCode");
                contactCase = contactCaseDAO.findContactCaseByCode(code);
            }
        }
    }
    
    public void clearCustomer() {
        if(JSFUtil.getUserSession().getCustomerDetail() != null) {
            CustomerInfoValue customer = null;
            JSFUtil.getUserSession().setCustomerDetail(customer);
        }
    }
   
    public ContactCaseDAO getContactCaseDAO() {
        return contactCaseDAO;
    }

    public void setContactCaseDAO(ContactCaseDAO contactCaseDAO) {
        this.contactCaseDAO = contactCaseDAO;
    }
    
    public ContactCase getContactCase() {
        return contactCase;
    }

    public void setContactCase(ContactCase contactCase) {
        this.contactCase = contactCase;
    }
}
