/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.model.dao.ContactHistoryDAO;
import com.maxelyz.core.model.value.admin.ContactRecordValue;
import com.maxelyz.utils.JSFUtil;

import java.util.*;
import org.apache.log4j.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
/**
 *
 * @author admin
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class ContactHistoryFrontController {
    
    private static Logger log = Logger.getLogger(ContactHistoryFrontController.class);
   
    private Integer customerId;
    private List<ContactRecordValue> contactHistoryList;

    @ManagedProperty(value="#{contactHistoryDAO}")
    private ContactHistoryDAO contactHistoryDAO;
    
    @PostConstruct
    public void initialize() {
//        SecurityUtil.isPermitted("casehistory:view");
        contactHistoryList = null;
        customerId = JSFUtil.getUserSession().getCustomerDetail().getId();
        if(customerId != null) {
            contactHistoryList = contactHistoryDAO.findContactHistoryByCustomerId(customerId);
        }
    }

    public List<ContactRecordValue> getContactHistoryList() {
        return contactHistoryList;
    }

    public void setContactHistoryList(List<ContactRecordValue> contactHistoryList) {
        this.contactHistoryList = contactHistoryList;
    }

    public ContactHistoryDAO getContactHistoryDAO() {
        return contactHistoryDAO;
    }

    public void setContactHistoryDAO(ContactHistoryDAO contactHistoryDAO) {
        this.contactHistoryDAO = contactHistoryDAO;
    }
    

}
