/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.controller.front.customerHandling;

import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.CustomerTypeDAO;
import com.maxelyz.core.model.dao.CaseTypeDAO;
import com.maxelyz.core.model.dao.front.search.SearchCustomerDAO;
import com.maxelyz.core.model.entity.CaseType;
import com.maxelyz.core.model.entity.CustomerType;
import com.maxelyz.core.model.value.front.search.SearchCustomerValue;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Prawait
 */
@ManagedBean(name="popupSelectCustomerController")
@ViewScoped
public class PopupSelectCustomerController extends BaseController implements Serializable{

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<SearchCustomerValue> searchCustomerList;
    private List<CustomerType> customerTypeList = new ArrayList<CustomerType>();
    private List<CaseType> caseTypes = new ArrayList<CaseType>();
    private Boolean isSearch;

    @ManagedProperty(value="#{searchCustomerDAO}")
    private SearchCustomerDAO searchCustomerDAO;
    @ManagedProperty(value="#{customerTypeDAO}")
    private CustomerTypeDAO customerTypeDAO;
    @ManagedProperty(value="#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;

    private String customerName;
    private String identityNo;
    private String address;
    private String phoneNo;
    private String gender;
    private Integer customerTypeId;
    private String account;
    private String caseId;
    private Date contactDateFrom;
    private Date contactDateTo;
    private String serviceType;
    private Integer channelId; // 1=Inbound, 2=Outbound
    private Integer caseTypeId;
    private String priority;
    private String description;
    private String status;
    private String createdBy;

    // Paging properties
    private Integer currentPage;
    private Integer totalPages;
    private Integer totalRows;

    /** Creates a new instance of SearchCustomerController */
    public PopupSelectCustomerController() {
    }

    @PostConstruct
    public void initialize(){

    }

    public void searchCustomerListener(){
        searchCustomerList = getSearchCustomerDAO().searchCustomer(customerName, identityNo, address, phoneNo, gender, customerTypeId,
                    account, caseId, contactDateFrom, contactDateTo, channelId, caseTypeId, priority, description, status, createdBy, null, null, null, null, null, null, null, 0, 1000);
    }

    public List<SearchCustomerValue> getSelectCustomerList() {
        return searchCustomerList;
    }

    /**
     * @return the searchCustomerDAO
     */
    public SearchCustomerDAO getSearchCustomerDAO() {
        return searchCustomerDAO;
    }

    /**
     * @param searchCustomerDAO the searchCustomerDAO to set
     */
    public void setSearchCustomerDAO(SearchCustomerDAO searchCustomerDAO) {
        this.searchCustomerDAO = searchCustomerDAO;
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
     * @return the searchCustomerList
     */
    public List<SearchCustomerValue> getSearchCustomerList() {
        return searchCustomerList;
    }

    /**
     * @param searchCustomerList the searchCustomerList to set
     */
    public void setSearchCustomerList(List<SearchCustomerValue> searchCustomerList) {
        this.searchCustomerList = searchCustomerList;
    }

    /**
     * @return the isSearch
     */
    public Boolean getIsSearch() {
        return isSearch;
    }

    /**
     * @param isSearch the isSearch to set
     */
    public void setIsSearch(Boolean isSearch) {
        this.isSearch = isSearch;
    }

     /**
     * @return the customerTypeList
     */
    public Map<String, Integer> getCustomerTypeList() {
        return getCustomerTypeDAO().getCustomeTypeList();
    }

    /**
     * @param customerTypeList the customerTypeList to set
     */
    public void setCustomerTypeList(List<CustomerType> customerTypeList) {
        this.customerTypeList = customerTypeList;
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
     * @return the caseTypes
     */
    public List getCaseTypes() {
        caseTypes = getCaseTypeDAO().findCaseTypeEntities();
        return caseTypes;
    }

    /**
     * @param caseTypes the caseTypes to set
     */
    public void setCaseTypes(List<CaseType> caseTypes) {
        this.caseTypes = caseTypes;
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
     * @return the account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return the caseId
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     * @param caseId the caseId to set
     */
    public void setCaseId(String caseId) {
        this.caseId = caseId;
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
     * @return the serviceType
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType the serviceType to set
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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
     * @return the identityNo
     */
    public String getIdentityNo() {
        return identityNo;
    }

    /**
     * @param identityNo the identityNo to set
     */
    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
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
     * @return the priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(String priority) {
        this.priority = priority;
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
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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
     * @return the currentPage
     */
    public Integer getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * @return the totalPages
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages the totalPages to set
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * @return the totalRows
     */
    public Integer getTotalRows() {
        return totalRows;
    }

    /**
     * @param totalRows the totalRows to set
     */
    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }


}

