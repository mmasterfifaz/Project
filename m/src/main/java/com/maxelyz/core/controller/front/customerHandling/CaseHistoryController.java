/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.value.front.customerHandling.ContactCaseInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import org.apache.log4j.Logger;

/**
 *
 * @author admin
 */
@ManagedBean
@RequestScoped
public class CaseHistoryController {
    private static Logger loger = Logger.getLogger(CustomerHandlingDetailController.class);
    private String SUCCESS = "/front/customerHandling/caseHistory.xhtml";
    
    private List<ContactCaseInfoValue> contactCaseList = new ArrayList<ContactCaseInfoValue>();
    
    @ManagedProperty(value="#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("casehistory:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        JSFUtil.getUserSession().setRefNo(null);
    }

    public List<ContactCaseInfoValue> getContactCaseList() {
        CustomerInfoValue c = JSFUtil.getUserSession().getCustomerDetail();
        if(c != null){
            try {
                contactCaseList = customerHandlingDAO.findContactCaseByNoRec(c.getId(), 0);
            } catch(NullPointerException e) {
                loger.error(e);
            }
        }
        return contactCaseList;
    }

    public void setContactCaseList(List<ContactCaseInfoValue> contactCaseList) {
        this.contactCaseList = contactCaseList;
    }

    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }

    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }
    
    
}
