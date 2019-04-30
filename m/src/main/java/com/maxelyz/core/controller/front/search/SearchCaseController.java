/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.search;

import com.maxelyz.core.constant.CrmConstant;
import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.ActivityTypeDAO;
import com.maxelyz.core.model.dao.CaseDetailDAO;
import com.maxelyz.core.model.dao.CaseRequestDAO;
import com.maxelyz.core.model.dao.CaseTopicDAO;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import org.apache.log4j.Logger;

import com.maxelyz.core.model.dao.CaseTypeDAO;
import com.maxelyz.core.model.dao.CustomerTypeDAO;
import com.maxelyz.core.model.dao.ServiceTypeDAO;
import com.maxelyz.core.model.dao.front.search.SearchCaseDAO;
import com.maxelyz.core.model.entity.ActivityType;
import com.maxelyz.core.model.entity.CaseDetail;
import com.maxelyz.core.model.entity.CaseRequest;
import com.maxelyz.core.model.entity.CaseTopic;
import com.maxelyz.core.model.entity.CaseType;
import com.maxelyz.core.model.entity.CustomerType;
import com.maxelyz.core.model.entity.ServiceType;
import com.maxelyz.core.model.value.front.customerHandling.ContactCaseInfoValue;
import com.maxelyz.core.model.value.front.search.SearchCaseValue;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author Manop
 */
@ManagedBean(name="searchCaseController")
@SessionScoped
public class SearchCaseController implements Serializable {

    private static Logger log = Logger.getLogger(SearchCaseController.class);
    private static String SEARCHRESULT = "searchCase.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    //private List<SearchCaseValue> searchCaseValues;
    private List<ContactCaseInfoValue> searchCaseValues;
    private List<ContactCaseInfoValue> searchCasePopupValues = new ArrayList<ContactCaseInfoValue>();
    @ManagedProperty(value = "#{searchCaseDAO}")
    private SearchCaseDAO searchCaseDAO;
    @ManagedProperty(value = "#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;
    @ManagedProperty(value = "#{caseTopicDAO}")
    private CaseTopicDAO caseTopicDAO;
    @ManagedProperty(value = "#{caseDetailDAO}")
    private CaseDetailDAO caseDetailDAO;
    @ManagedProperty(value = "#{caseRequestDAO}")
    private CaseRequestDAO caseRequestDAO;
    @ManagedProperty(value="#{customerTypeDAO}")
    private CustomerTypeDAO customerTypeDAO;
    @ManagedProperty(value = "#{serviceTypeDAO}")
    private ServiceTypeDAO serviceTypeDAO;
    @ManagedProperty(value = "#{activityTypeDAO}")
    private ActivityTypeDAO activityTypeDAO;

    private String caseID;
    private Date contactDateFrom;
    private Date contactDateTo;
//    private Integer caseTypeId;
    private String description;
    private String caseStatus;
    private String createdBy;
    private String customerName;
    private String idNo;
    private String address;
    private String phoneNo;
    private String gender;
    private Integer customerTypeId;
    private List<CustomerType> customerTypeList = new ArrayList<CustomerType>();
    
//    private List<CaseType> caseTypeList = new ArrayList<CaseType>();
    private List<CaseType> caseTypes = new ArrayList<CaseType>();
    private List<CaseTopic> caseTopics = new ArrayList<CaseTopic>();
    private List<CaseDetail> caseDetails = new ArrayList<CaseDetail>();
    private List<CaseRequest> caseRequests = new ArrayList<CaseRequest>();
    private Integer caseTypeId;
    private Integer caseTopicId;
    private Integer caseDetailId;
    private Integer caseRequestId;
    
    private Integer serviceTypeId;
    private List<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
    private Integer scrollerPage;
    private List activityTypeList = new ArrayList();
    private Integer activityTypeId;
    private String delegateStatus;

    @PostConstruct
    public void initialize() {
       	if (!SecurityUtil.isPermitted("searchcase:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        JSFUtil.getUserSession().setFirstPage("search");
        caseTypes = getCaseTypeDAO().findCaseTypeEntities();
        customerTypeList = getCustomerTypeDAO().findCustomerTypeEntities();
        serviceTypeList = serviceTypeDAO.findServiceTypeEntities();
        activityTypeList = setActivityTypeList1(activityTypeDAO.findActivityTypeNonsystem());
//        searchCaseValues = getSearchCaseDAO().searchCase();
    }
        
    public String searchCaseAction() {   
        try {
            searchCaseValues = searchCaseDAO.searchCase(caseID, contactDateFrom, contactDateTo, null, caseTypeId, caseTopicId, caseDetailId, caseRequestId, description, caseStatus, createdBy, customerName, idNo, address, phoneNo, gender, customerTypeId, null, null, serviceTypeId, activityTypeId, delegateStatus, 0, 1000);
        } catch (Exception e) {
//            log.error(e);
        }

        return SEARCHRESULT;
    }

    public void searchRelatedCaseAction(ActionEvent event) {
        try {
            Integer customerId = null;
            if (JSFUtil.getUserSession().getCustomerDetail() != null) {
                customerId = JSFUtil.getUserSession().getCustomerDetail().getId();
                searchCasePopupValues = searchCaseDAO.searchCase(caseID, contactDateFrom, contactDateTo, null, caseTypeId, caseTopicId, caseDetailId, caseRequestId, description, caseStatus, createdBy, customerName, idNo, address, phoneNo, gender, customerTypeId, null, customerId, serviceTypeId, activityTypeId, delegateStatus, 0, 1000);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
    
    private void setCaseTopicList(Integer caseTypeId) {
        caseTopics = caseTopicDAO.findCaseTopicByCaseTypeStatus(caseTypeId);
        if(caseTopics != null && caseTopics.size() == 1) {
            caseTopicId = caseTopics.get(0).getId();
            setCaseDetailList(caseTopicId);
        } else {            
            setCaseTopicId(Integer.valueOf(0));
        }
    }

    private void setCaseDetailList(Integer caseTopicId) {
        caseDetails = caseDetailDAO.findAvailableCaseDetailByCaseTopic(caseTopicId);
        if(caseDetails != null && caseDetails.size() == 1) {
            caseDetailId = caseDetails.get(0).getId();
//            caseDetail = caseDetailDAO.findCaseDetail(caseDetailId);
            setCaseRequestList(caseDetailId);
        } else {
            setCaseRequestList(Integer.valueOf(0));
        }
    }

    private void setCaseRequestList(Integer caseDetailId) {
        caseRequests = caseRequestDAO.findCaseRequestByCaseDetailStatus(caseDetailId);
        if(caseRequests != null && caseRequests.size() == 1) {
            caseRequestId = caseRequests.get(0).getId();
            setCaseRequestId(caseRequestId);
        }
    }

    public void caseTypeListener() {//ActionEvent event) {
        if(caseTopics != null) {
            caseTopics.clear();
            setCaseTopicId(0);
        }
        if(caseDetails != null) {
            caseDetails.clear();
            setCaseDetailId(0);
        }
        if(caseRequests != null) {
            caseRequests.clear();
            setCaseRequestId(0);
        }
//        Integer id = (Integer) event.getNewValue();
//        setCaseTypeId(caseTypeId);
        setCaseTopicList(caseTypeId);
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void caseTopicListener() {   //ActionEvent event) {
        if(caseDetails != null) {
            caseDetails.clear();
            setCaseDetailId(0);
        }
        if(caseRequests != null) {
            caseRequests.clear();
            setCaseRequestId(0);
        }
        setCaseDetailList(caseTopicId);
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void caseDetailListener() {  //ActionEvent event) {
        if(caseRequests != null) {
            caseRequests.clear();
            setCaseRequestId(0);
        }
        setCaseRequestList(caseDetailId);
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public List setActivityTypeList1(List<ActivityType> list) {
        List activityTypeValueList = new ArrayList();
        //activityTypeValueList.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        for (ActivityType activityType : list) {
            if (activityType.getId() != null) {
                activityTypeValueList.add(new SelectItem(activityType.getId(), activityType.getName()));
            }
        }
        return activityTypeValueList;
    }

    public void activityTypeListener() {//ActionEvent event) {
        if(activityTypeId != null && activityTypeId != 0){
            if(activityTypeId != 1) {
                delegateStatus = "";
            }
        }
    }

    public ServiceTypeDAO getServiceTypeDAO() {
        return serviceTypeDAO;
    }

    public void setServiceTypeDAO(ServiceTypeDAO serviceTypeDAO) {
        this.serviceTypeDAO = serviceTypeDAO;
    }

    public List<ServiceType> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(List<ServiceType> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }

    /**
     * @return the selectedIds
     */
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    /**
     * @param selectedIds the selectedIds to set
     */
    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    /**
     * @return the searchCaseDAO
     */
    public SearchCaseDAO getSearchCaseDAO() {
        return searchCaseDAO;
    }

    /**
     * @param searchCaseDAO the searchCaseDAO to set
     */
    public void setSearchCaseDAO(SearchCaseDAO searchCaseDAO) {
        this.searchCaseDAO = searchCaseDAO;
    }

    /**
     * @return the searchCaseValue
     */
    public List<ContactCaseInfoValue> getSearchCaseValues() {
        if (!SecurityUtil.isPermitted("searchcase:view")) {
            SecurityUtil.redirectUnauthorize();   
        }
//        if (searchCaseValues == null){
//            searchCaseValues = getSearchCaseDAO().searchCase();
//        }
        return searchCaseValues;
    }

    /**
     * @param searchCaseValue the searchCaseValue to set
     */
    public void setSearchCaseValues(List<ContactCaseInfoValue> searchCaseValues) {
        this.searchCaseValues = searchCaseValues;
    }

    /**
     * @return the caseTypeDAO
     */
    public CaseTypeDAO getCaseTypeDAO() {
        return caseTypeDAO;
    }

    /**
     * @param caseTypeDAO the caseTypeDAO to set
     */
    public void setCaseTypeDAO(CaseTypeDAO caseTypeDAO) {
        this.caseTypeDAO = caseTypeDAO;
    }

    /**
     * @return the customerTypeDAO
     */
    public CustomerTypeDAO getCustomerTypeDAO() {
        return customerTypeDAO;
    }

    /**
     * @param customerTypeDAO the customerTypeDAO to set
     */
    public void setCustomerTypeDAO(CustomerTypeDAO customerTypeDAO) {
        this.customerTypeDAO = customerTypeDAO;
    }

    /**
     * @return the caseID
     */
    public String getCaseID() {
        return caseID;
    }

    /**
     * @param caseID the caseID to set
     */
    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    /**
     * @return the contactDateFrom
     */
    public Date getContactDateFrom() {
        return contactDateFrom;
    }

    /**
     * @param contactDateFrom the contactDateFrom to set
     */
    public void setContactDateFrom(Date contactDateFrom) {
        this.contactDateFrom = contactDateFrom;
    }

    /**
     * @return the contactDateTo
     */
    public Date getContactDateTo() {
        return contactDateTo;
    }

    /**
     * @param contactDateTo the contactDateTo to set
     */
    public void setContactDateTo(Date contactDateTo) {
        this.contactDateTo = contactDateTo;
    }

    /**
     * @return the caseTypeId
     */
    public Integer getCaseTypeId() {
        return caseTypeId;
    }

    /**
     * @param caseTypeId the caseTypeId to set
     */
    public void setCaseTypeId(Integer caseTypeId) {
        this.caseTypeId = caseTypeId;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the caseStatus
     */
    public String getCaseStatus() {
        return caseStatus;
    }

    /**
     * @param caseStatus the caseStatus to set
     */
    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the idNo
     */
    public String getIdNo() {
        return idNo;
    }

    /**
     * @param idNo the idNo to set
     */
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the phoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * @param phoneNo the phoneNo to set
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the customerTypeId
     */
    public Integer getCustomerTypeId() {
        return customerTypeId;
    }

    /**
     * @param customerTypeId the customerTypeId to set
     */
    public void setCustomerTypeId(Integer customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    /**
     * @return the caseTypeList
     */
    public List getCaseTypeList() {
        List caseTypeValueList = new ArrayList();
        caseTypeValueList.add(new SelectItem(null, CrmConstant.ALL));
        for (CaseType caseType : caseTypes){
            if (caseType.getId() != null){
                caseTypeValueList.add(new SelectItem(caseType.getId(), caseType.getName()));
            }
        }
        return caseTypeValueList;
    }

    /**
     * @param caseTypeList the caseTypeList to set
     */
    public void setCaseTypeList(List<CaseType> caseTypes) {
        this.caseTypes = caseTypes;
    }

    /**
     * @return the customerTypeList
     */
    public List getCustomerTypeList() {
        List customerTypeValueList = new ArrayList();
        customerTypeValueList.add(new SelectItem(null, CrmConstant.ALL));
        for (CustomerType customerType: customerTypeList){
            if (customerType.getId() != null){
                customerTypeValueList.add(new SelectItem(customerType.getId(), customerType.getName()));
            }
        }
        return customerTypeValueList;
    }

    /**
     * @param customerTypeList the customerTypeList to set
     */
    public void setCustomerTypeList(List<CustomerType> customerTypeList) {
        this.customerTypeList = customerTypeList;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Integer getScrollerPage() {
        return scrollerPage;
    }

    public void setScrollerPage(Integer scrollerPage) {
        this.scrollerPage = scrollerPage;
    }

    public List<ContactCaseInfoValue> getSearchCasePopupValues() {
        return searchCasePopupValues;
    }

    public void setSearchCasePopupValues(List<ContactCaseInfoValue> searchCasePopupValues) {
        this.searchCasePopupValues = searchCasePopupValues;
    }

    public ActivityTypeDAO getActivityTypeDAO() {
        return activityTypeDAO;
    }

    public void setActivityTypeDAO(ActivityTypeDAO activityTypeDAO) {
        this.activityTypeDAO = activityTypeDAO;
    }

    public List getActivityTypeList() {
        return activityTypeList;
    }

    public void setActivityTypeList(List activityTypeList) {
        this.activityTypeList = activityTypeList;
    }

    public Integer getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(Integer activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public String getDelegateStatus() {
        return delegateStatus;
    }

    public void setDelegateStatus(String delegateStatus) {
        this.delegateStatus = delegateStatus;
    }

    public CaseTopicDAO getCaseTopicDAO() {
        return caseTopicDAO;
    }

    public void setCaseTopicDAO(CaseTopicDAO caseTopicDAO) {
        this.caseTopicDAO = caseTopicDAO;
    }

    public CaseDetailDAO getCaseDetailDAO() {
        return caseDetailDAO;
    }

    public void setCaseDetailDAO(CaseDetailDAO caseDetailDAO) {
        this.caseDetailDAO = caseDetailDAO;
    }

    public CaseRequestDAO getCaseRequestDAO() {
        return caseRequestDAO;
    }

    public void setCaseRequestDAO(CaseRequestDAO caseRequestDAO) {
        this.caseRequestDAO = caseRequestDAO;
    }
    
    public Integer getCaseTopicId() {
        return caseTopicId;
    }

    public void setCaseTopicId(Integer caseTopicId) {
        this.caseTopicId = caseTopicId;
    }
    
    public Integer getCaseDetailId() {
        return caseDetailId;
    }

    public void setCaseDetailId(Integer caseDetailId) {
        this.caseDetailId = caseDetailId;
    }
    
    public Integer getCaseRequestId() {
        return caseRequestId;
    }

    public void setCaseRequestId(Integer caseRequestId) {
        this.caseRequestId = caseRequestId;
    }
    
    public List<CaseTopic> getCaseTopics() {
        return caseTopics;
    }

    public void setCaseTopics(List<CaseTopic> caseTopics) {
        this.caseTopics = caseTopics;
    }
    
    public List<CaseDetail> getCaseDetails() {
        return caseDetails;
    }

    public void setCaseDetails(List<CaseDetail> caseDetails) {
        this.caseDetails = caseDetails;
    }

    public List<CaseRequest> getCaseRequests() {
        return caseRequests;
    }

    public void setCaseRequests(List<CaseRequest> caseRequests) {
        this.caseRequests = caseRequests;
    }
}
