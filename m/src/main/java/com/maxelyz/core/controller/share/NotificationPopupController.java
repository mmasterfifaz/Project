package com.maxelyz.core.controller.share;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.AssignmentDetailDAO;
import com.maxelyz.core.model.dao.CustomerDAO;
import com.maxelyz.core.model.dao.PhoneDirectoryDAO;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.dao.NotificationMessageDAO;
import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.Channel;
import com.maxelyz.core.model.entity.ContactHistory;
import com.maxelyz.core.model.entity.CtiAdapter;
import com.maxelyz.core.model.entity.Customer;
import com.maxelyz.core.model.entity.PhoneDirectory;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.core.service.TelephonyService;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.richfaces.json.JSONObject;

@ManagedBean
@SessionScoped
//@ViewScoped
public class NotificationPopupController implements Serializable {

    private static Logger log = Logger.getLogger(NotificationPopupController.class);
    //private static String SALEAPPROACHING_PAGE = "/front/customerHandling/saleApproaching.xhtml?faces-redirect=true";
    private static String SALEAPPROACHING_PAGE = "/front/customerHandling/saleApproaching.xhtml";
    private static String REFRESH = "notificationpopup.xhtml";
    
    private String uuid;
    private Integer refId;
    private String phoneNumber;
    
    @ManagedProperty(value = "#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    @ManagedProperty(value = "#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
    @ManagedProperty(value = "#{notificationMessageDAO}")
    private NotificationMessageDAO notificationMessageDAO;
    
    @PostConstruct
    public void initialize() {
        //customerName = "";
    }
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public void updateNotificationMessage() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println(JSFUtil.getUserSession().getUserName() + " : Start Update NotificationMessage, " + dateFormat.format(new Date()));
        
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        //System.out.println("UUID : " + params.get("uuid"));
        //System.out.println("RefId : " + params.get("refId"));     
        this.uuid = params.get("uuid");
        this.refId = Integer.valueOf(params.get("refId"));
        this.phoneNumber = params.get("phoneNumber");
        
        //JSFUtil.getUserSession().setAssignmentDetail(assignmentDetailDAO.findAssignmentDetail(this.refId));
        /*
        if(JSFUtil.getUserSession().getCustomerDetail() == null){
            CustomerInfoValue customerInfo = customerHandlingDAO.findCustomerHandling(JSFUtil.getUserSession().getAssignmentDetail().getCustomerId().getId());
            JSFUtil.getUserSession().setCustomerDetail(customerInfo);
        }
        */
        //JSFUtil.getUserSession().getAssignmentDetail().setStatus("opened");
        //JSFUtil.getUserSession().notifyContactHistory();
        //notificationMessageDAO.updateStatus(this.uuid);
        System.out.println(JSFUtil.getUserSession().getUserName() + " : End Update NotificationMessage : " + dateFormat.format(new Date()));
    }
    
    public void actionNotificationMessage() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println(JSFUtil.getUserSession().getUserName() + " : Update NotificationMessage, " + dateFormat.format(new Date()));
    }
    
    /*
    public String toSaleApproaching() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println(JSFUtil.getUserSession().getUserName() + ": Start Notification to SaleApproaching, " + dateFormat.format(new Date()));

        //JSFUtil.getUserSession().notifyContactHistory();
        notificationMessageDAO.updateStatus(uuid);
        System.out.println("End Notification to SaleApproaching :" + dateFormat.format(new Date()));
        
        return SALEAPPROACHING_PAGE;
    }
    */

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
    
    public NotificationMessageDAO getNotificationMessageDAO() {
        return notificationMessageDAO;
    }

    public void setNotificationMessageDAO(NotificationMessageDAO notificationMessageDAO) {
        this.notificationMessageDAO = notificationMessageDAO;
    }
}