// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CustomerHandlingDetailController.java

package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.common.front.dispatch.FrontDispatcher;
import com.maxelyz.core.common.front.dispatch.RegistrationPouch;
import com.maxelyz.core.model.dao.ContactCaseDAO;
import com.maxelyz.core.model.dao.ContactHistoryDAO;
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.entity.ContactCase;
import com.maxelyz.core.model.entity.ContactHistory;
import com.maxelyz.core.model.value.front.customerHandling.ActivityInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.ContactCaseInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.ContactHistoryInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

@ManagedBean
//@RequestScoped
@ViewScoped
public class CustomerHandlingDetailController {

    private static Logger loger = Logger.getLogger(CustomerHandlingDetailController.class);
    private String SUCCESS = "/front/customerHandling/customerDetail.xhtml";
    private String FAIL = "searchCustomer.jsf";
    private String EDIT = "/front/customerHandling/caseHandling.xhtml";

    private CustomerInfoValue customerInfoValue;
    private String selectedCaseId;
    private List<ContactCaseInfoValue> contactCaseList = new ArrayList<ContactCaseInfoValue>();
    private List<ActivityInfoValue> activityList = new ArrayList<ActivityInfoValue>();
    private List<Object> campaignProductList = new ArrayList<Object>();
    private List<Object> billingReminderList = new ArrayList<Object>();
    private List<ContactHistoryInfoValue> contactHistoryInfoValueList;
    private String emessage = null;

    
    @ManagedProperty(value="#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
    @ManagedProperty(value="#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value="#{contactHistoryDAO}")
    private ContactHistoryDAO contactHistoryDAO;
    @ManagedProperty(value="#{contactCaseDAO}")
    private ContactCaseDAO contactCaseDAO;

    private Integer customerId;

    private List saleHistoryList;

    @PostConstruct
    public void initialize()
    {   
        String selectedId = null;
        if (JSFUtil.getUserSession().getCustomerDetail() != null) {
            customerId = JSFUtil.getUserSession().getCustomerDetail().getId();
        } else {
            selectedId = JSFUtil.getRequestParameterMap("selectId");
            customerId = Integer.parseInt(selectedId);     
            customerInfoValue = customerHandlingDAO.findCustomerHandling(customerId);
            JSFUtil.getUserSession().setCustomerDetail(customerInfoValue);
        }

        initSaleHistory();
        JSFUtil.getUserSession().setRefNo(null);
//        initContactHistory();
    }

    public void initSaleHistory(){
        Integer userId = JSFUtil.getUserSession().getUsers().getId();
        saleHistoryList = purchaseOrderDAO.findByCustomerUser(customerId, userId);

    }

    public void initContactHistory(){
        contactCaseList = customerHandlingDAO.findContactCaseByContactHistory(customerId);
        
//        contactHistoryInfoValueList = new ArrayList<ContactHistoryInfoValue>();
//        List<ContactHistory> contactHistoryList = contactHistoryDAO.findContactHistoryByCustomer1(customerId);
//        for(ContactHistory contactHistory : contactHistoryList){
//            List<ContactCaseInfoValue> ccList = customerHandlingDAO.findContactCaseByContactHistory(customerId, contactHistory.getId());
//            ContactHistoryInfoValue c = new ContactHistoryInfoValue(contactHistory, ccList);
//            contactHistoryInfoValueList.add(c);
//        }

    }
    
     public String toRegistrationForm(Object purchaseOrderId) {
        this.emessage = null;
        try{  
            Integer iPurchaseOrderId = null;
            try{ iPurchaseOrderId = Integer.valueOf(purchaseOrderId.toString()); }catch(Exception e){ }
            RegistrationPouch pouch = new RegistrationPouch(); // create pouch to carry data for purchase order            
            pouch.registeredPouchType(RegistrationPouch.SENDER_TYPE.CUSTOMERDETAIL, RegistrationPouch.RECEIVER_MODE.EDIT);
            pouch.putEditModeParameter(iPurchaseOrderId);
            FrontDispatcher.keepPouch(pouch); // registered pouch on dispatcher
            return FrontDispatcher.getPouchReceiver(pouch); // get receiver
        } catch (Exception e) {
            System.out.println("Error when dispatch from customerDetail to registration form : "+e.getMessage());
            this.emessage = "Cannot forward to registration form. [Cause: "+e.getMessage()+"] ";
            return null;
        }        
    }
    

    public String getEmessage() {
        return emessage;
    }
     
     public String editCase() {
         return EDIT;
     }

    public String getCustomerInfo() {
        if(customerId == null || customerId == 0) {
            return FAIL;
        } else {
            return SUCCESS;
        }
    }

    public CustomerInfoValue getCustomerInfoValue() {
        return customerInfoValue;
    }

    public void setCustomerInfoValue(CustomerInfoValue customerInfoValue)
    {
        this.customerInfoValue = customerInfoValue;
    }

    public CustomerHandlingDAO getCustomerHandlingDAO()
    {
        return customerHandlingDAO;
    }

    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO)
    {
        this.customerHandlingDAO = customerHandlingDAO;
    }

    /**
     * @return the contactCaseList
     */
    public List<ContactCaseInfoValue> getContactCaseList() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpSession session = (HttpSession)context.getExternalContext().getSession(false);
        CustomerInfoValue c = JSFUtil.getUserSession().getCustomerDetail();
        if(c != null){
            try {
                contactCaseList = getCustomerHandlingDAO().findContactCaseByNoRec(c.getId(), 3);
            } catch(NullPointerException e) {
                loger.error(e);
            }
//            if(null != contactCaseList && contactCaseList.size() > 0 && selectedCaseId == null)
//                activityList = getCustomerHandlingDAO().findActivity(((ContactCaseInfoValue)contactCaseList.get(0)).getId());
        }
        return contactCaseList;
    }

    /**
     * @param contactCaseList the contactCaseList to set
     */
    public void setContactCaseList(List<ContactCaseInfoValue> contactCaseList) {
        this.contactCaseList = contactCaseList;
    }

    /**
     * @return the activityList
     */
    public List<ActivityInfoValue> getActivityList() {
        return activityList;
    }

    /**
     * @param activityList the activityList to set
     */
    public void setActivityList(List<ActivityInfoValue> activityList) {
        this.activityList = activityList;
    }

    public void selectCaseAction(){
        String caseId = JSFUtil.getRequestParameterMap("selectId");
        selectedCaseId = caseId;
        setActivityList(getCustomerHandlingDAO().findActivity(Integer.parseInt(caseId)));
    }

    /**
     * @return the selectedCaseId
     */
    public String getSelectedCaseId() {
        return selectedCaseId;
    }

    /**
     * @param selectedCaseId the selectCaseId to set
     */
    public void setSelectedCaseId(String selectedCaseId) {
        this.selectedCaseId = selectedCaseId;
    }

    public List<Object> getCampaignProductList() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpSession session = (HttpSession)context.getExternalContext().getSession(false);
        if(JSFUtil.getUserSession().getCustomerDetail() != null)
        {
            customerInfoValue = JSFUtil.getUserSession().getCustomerDetail();
            campaignProductList = customerHandlingDAO.findCampaignProductList(customerInfoValue.getId());
        }
        
        return campaignProductList;
    }

    public void setCampaignProductList(List<Object> campaignProductList) {
        this.campaignProductList = campaignProductList;
    }

    public List<Object> getBillingReminderList() {
        return billingReminderList;
    }

    public void setBillingReminderList(List<Object> billingReminderList) {
        this.billingReminderList = billingReminderList;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List getSaleHistoryList() {
        return saleHistoryList;
    }

    public void setSaleHistoryList(List saleHistoryList) {
        this.saleHistoryList = saleHistoryList;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public List<ContactHistoryInfoValue> getContactHistoryInfoValueList() {
        return contactHistoryInfoValueList;
    }

    public void setContactHistoryInfoValueList(List<ContactHistoryInfoValue> contactHistoryInfoValueList) {
        this.contactHistoryInfoValueList = contactHistoryInfoValueList;
    }

    public ContactHistoryDAO getContactHistoryDAO() {
        return contactHistoryDAO;
    }

    public void setContactHistoryDAO(ContactHistoryDAO contactHistoryDAO) {
        this.contactHistoryDAO = contactHistoryDAO;
    }

    public ContactCaseDAO getContactCaseDAO() {
        return contactCaseDAO;
    }

    public void setContactCaseDAO(ContactCaseDAO contactCaseDAO) {
        this.contactCaseDAO = contactCaseDAO;
    }

}
