/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.AssignmentDetailDAO;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.ContactHistory;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
@ManagedBean (name="approchingRecordEditController")
//@RequestScoped
@ViewScoped
public class ApprochingRecordEditController {
    
    private static Logger log = Logger.getLogger(ApprochingRecordEditController.class);
    private static String SUCCESS = "approchingrecord.xhtml";
    private static String FAILURE = "approchingrecordedit.xhtml";
    private AssignmentDetail assignmentDetail;
    private Collection <ContactHistory> contactHistoryList;
    private CustomerInfoValue customerInfoValue;
    
    @ManagedProperty(value="#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    @ManagedProperty(value="#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
        
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:approchingrecord:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        String selectedID = null;
        selectedID = (String) JSFUtil.getRequestParameterMap("selectedID");
        if(selectedID != null) {
            assignmentDetail = assignmentDetailDAO.findAssignmentDetail(new Integer(selectedID));
            contactHistoryList = assignmentDetail.getContactHistoryCollection();
        }
        String customerID = null;
        customerID = (String) JSFUtil.getRequestParameterMap("customerID"); 
        if(customerID != null) {
            customerInfoValue = customerHandlingDAO.findCustomerHandling(new Integer(customerID));
        }
    }
    
    //Action
    public String saveAction() {        
        try {
            assignmentDetailDAO.editMaxcall(assignmentDetail);
//            this.getAppochingRecordController().searchActionListener(null);
            return SUCCESS;
        } catch (Exception e) {
            log.error(e);
            return FAILURE;
        } 
    }
    
    public AppochingRecordController getAppochingRecordController() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ValueExpression vex = app.getExpressionFactory().
                createValueExpression(context.getELContext(), "#{appochingRecordController}", AppochingRecordController.class);
        AppochingRecordController g = (AppochingRecordController) vex.getValue(context.getELContext());
        return g;
    }

    public String backAction() {
        return SUCCESS;
    }

    //Get Set DAO
    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }
    
    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }

    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }
   
    // Get Set Properties
     public AssignmentDetail getAssignmentDetail() {
        return assignmentDetail;
    }

    public void setAssignmentDetail(AssignmentDetail assignmentDetail) {
        this.assignmentDetail = assignmentDetail;
    }
    
    public Map<Integer, Integer> getMaxCallList() {
        Map<Integer, Integer> values = new ConcurrentSkipListMap<Integer, Integer>();
        for (int i=1;i<=100;i++)
            values.put(i, i);
        return values;
    }
    
    public Collection <ContactHistory>  getContactHistoryList() {
        return contactHistoryList;
    }

    public void setContactHistoryList(Collection <ContactHistory>  contactHistoryList) {
        this.contactHistoryList = contactHistoryList;
    }

    public CustomerInfoValue getCustomerInfoValue() {
        return customerInfoValue;
    }

    public void setCustomerInfoValue(CustomerInfoValue customerInfoValue) {
        this.customerInfoValue = customerInfoValue;
    }

}
