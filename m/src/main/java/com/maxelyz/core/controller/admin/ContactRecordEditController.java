/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.ContactRecordValue;
import com.maxelyz.core.model.value.front.customerHandling.ContactCaseInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.event.ActionEvent;


/**
 *
 * @author admin
 */
@ManagedBean
@ViewScoped
public class ContactRecordEditController implements Serializable{

    private static Logger log = Logger.getLogger(ContactRecordEditController.class);
    private static String BACK = "contactrecord.xhtml";
    private static String REFRESH = "contactrecord.xhtml";
    private static String EDIT = "contactrecordedit.xhtml";
    private Integer customerId;
    private Integer contactHistoryId;
    private String trackId;
    //private List<ContactRecordValue> contactHistoryList;
    //private List<ContactHistorySaleResult> contactHistorySaleResultList;
    private List<PurchaseOrderDetail> contactHistorySaleResultList;
    private Customer customer;
    private List<ContactCaseInfoValue> contactCaseList = new ArrayList<ContactCaseInfoValue>();
   // private List<ContactCaseInfoValue> contactCaseList = new ArrayList<ContactCaseInfoValue>();
    private String title;
    private ContactHistory contactHistory;

    private String fromDate;
    private String toDate;
    private Integer userGroupId;
    private Integer userId;
    private String textSearch;
    private String contactToSearch;

    @ManagedProperty(value="#{contactHistoryDAO}")
    private ContactHistoryDAO contactHistoryDAO;
    @ManagedProperty(value="#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value="#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value="#{contactCaseDAO}")
    private ContactCaseDAO contactCaseDAO;
    @ManagedProperty(value="#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;

    private ContactCase contactCase;
    private Relationship relationShip;
    private String relationShipText;
    private Collection<CaseAttachment> attachFiles = new ArrayList<CaseAttachment>();
    
    @ManagedProperty(value="#{relationshipDAO}")
    private RelationshipDAO relationshipDAO;
    
    @PostConstruct
    public void initialize() {
        JSFUtil.getUserSession().setFirstPage("admin");
        initGetParam();
        initCustomerId();
        initContactHistoryId();
        editAction();
    }

    public boolean isCaseEditPermitted() {
        return SecurityUtil.isPermitted("case:edit");
    }
    
    public void clearCustomer() {
        if(JSFUtil.getUserSession().getCustomerDetail() != null) {
            CustomerInfoValue customer = null;
            JSFUtil.getUserSession().setCustomerDetail(customer);
        }
    }
    
    private void initGetParam(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
        try{
            if(JSFUtil.getRequestParameterMap("fromDate") != null){
                fromDate = String.valueOf(JSFUtil.getRequestParameterMap("fromDate"));
            }
            if(JSFUtil.getRequestParameterMap("toDate") != null){
                toDate = String.valueOf(JSFUtil.getRequestParameterMap("toDate"));
            }
            if(JSFUtil.getRequestParameterMap("userGroupId") != null && !JSFUtil.getRequestParameterMap("userGroupId").equals("")){
                userGroupId = Integer.parseInt(JSFUtil.getRequestParameterMap("userGroupId"));
            }
            if(JSFUtil.getRequestParameterMap("userId") != null){
                userId = Integer.parseInt(JSFUtil.getRequestParameterMap("userId"));
            }
            if(JSFUtil.getRequestParameterMap("textSearch") != null){
                textSearch = String.valueOf(JSFUtil.getRequestParameterMap("textSearch"));
            }
            if(JSFUtil.getRequestParameterMap("contactToSearch") != null){
                contactToSearch = String.valueOf(JSFUtil.getRequestParameterMap("contactToSearch"));
            }
        }catch (Exception pe) {
            //log.error(pe.toString());
        }
    }

    public void editAction(){
        if(customerId != null && customerId != 0){
            customer = customerDAO.findCustomer(customerId);
            title = "By Customer";
        }else if(contactHistoryId != null && contactHistoryId != 0){
            contactHistory = contactHistoryDAO.findContactHistory(contactHistoryId);
            customer = contactHistory.getCustomer();
            customerId = customer.getId();
            title = "";
        }
        contactHistorySaleResultList  = contactHistoryDAO.findContactHistorySaleResult(contactHistoryId);
        contactCaseList = customerHandlingDAO.findContactCaseByContactHistoryId(contactHistoryId);
    }

    private void initCustomerId() {
        if (JSFUtil.getRequestParameterMap("customerId") != null) {
            customerId = Integer.parseInt(JSFUtil.getRequestParameterMap("customerId"));
        } else if (JSFUtil.getRequest().getAttribute("customerId") != null) {
            customerId = (Integer) JSFUtil.getRequest().getAttribute("customerId");
        }
    }
    
    private void initContactHistoryId() {
        if (JSFUtil.getRequestParameterMap("contactHistoryId") != null) {
            contactHistoryId = Integer.parseInt(JSFUtil.getRequestParameterMap("contactHistoryId"));
            contactHistory = contactHistoryDAO.findContactHistory(contactHistoryId);
        } else if (JSFUtil.getRequest().getAttribute("contactHistoryId") != null) {
            contactHistoryId = (Integer) JSFUtil.getRequest().getAttribute("contactHistoryId");
            contactHistory = contactHistoryDAO.findContactHistory(contactHistoryId);
        }
    }

    public String backAction(){
        return BACK;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }
/*
    public List<ContactRecordValue> getContactHistoryList() {
        return contactHistoryList;
    }

    public void setContactHistoryList(List<ContactRecordValue> contactHistoryList) {
        this.contactHistoryList = contactHistoryList;
    }
   */ 
    public ContactHistoryDAO getContactHistoryDAO() {
        return contactHistoryDAO;
    }

    public void setContactHistoryDAO(ContactHistoryDAO contactHistoryDAO) {
        this.contactHistoryDAO = contactHistoryDAO;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public List<PurchaseOrderDetail> getContactHistorySaleResultList() {
        return contactHistorySaleResultList;
    }

    public void setContactHistorySaleResultList(List<PurchaseOrderDetail> contactHistorySaleResultList) {
        this.contactHistorySaleResultList = contactHistorySaleResultList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ContactHistory getContactHistory() {
        return contactHistory;
    }

    public void setContactHistory(ContactHistory contactHistory) {
        this.contactHistory = contactHistory;
    }
    
    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public ContactCaseDAO getContactCaseDAO() {
        return contactCaseDAO;
    }

    public void setContactCaseDAO(ContactCaseDAO contactCaseDAO) {
        this.contactCaseDAO = contactCaseDAO;
    }

    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }

    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }

    public List<ContactCaseInfoValue> getContactCaseList() {
        return contactCaseList;
    }

    public void setContactCaseList(List<ContactCaseInfoValue> contactCaseList) {
        this.contactCaseList = contactCaseList;
    }

    public Integer getContactHistoryId() {
        return contactHistoryId;
    }

    public void setContactHistoryId(Integer contactHistoryId) {
        this.contactHistoryId = contactHistoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContactToSearch() {
        return contactToSearch;
    }

    public void setContactToSearch(String contactToSearch) {
        this.contactToSearch = contactToSearch;
    }

    public String getTextSearch() {
        return textSearch;
    }

    public void setTextSearch(String textSearch) {
        this.textSearch = textSearch;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }    
    
    public void popupListener() {
        if(JSFUtil.getRequestParameterMap("from") != null) {
            if(JSFUtil.getRequestParameterMap("historyCode") != null) {
                String code = JSFUtil.getRequestParameterMap("historyCode");
                contactCase = contactCaseDAO.findContactCaseByCode(code);
                if(contactCase.getRelationshipId() != null){
                    relationShip = relationshipDAO.findRelationship(contactCase.getRelationshipId());
                    relationShipText = relationShip.getName();
                }
                contactCase.getCaseAttachmentCollection().size();
                attachFiles = contactCase.getCaseAttachmentCollection();
            }
        }
    }
    
    public ContactCase getContactCase() {
        return contactCase;
    }

    public void setContactCase(ContactCase contactCase) {
        this.contactCase = contactCase;
    }
    
    public RelationshipDAO getRelationshipDAO() {
        return relationshipDAO;
    }

    public void setRelationshipDAO(RelationshipDAO relationshipDAO) {
        this.relationshipDAO = relationshipDAO;
    }
    
    public String getRelationShipText() {
        return relationShipText;
    }

    public void setRelationShipText(String relationShipText) {
         this.relationShipText = relationShipText;
    }
    
    public Collection<CaseAttachment> getAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(Collection<CaseAttachment> attachFiles) {
        this.attachFiles = attachFiles;
    }
}
