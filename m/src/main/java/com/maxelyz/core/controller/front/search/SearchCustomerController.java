/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.controller.front.search;

import com.maxelyz.core.constant.CrmConstant;
import com.maxelyz.core.controller.UserSession;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.CustomerTypeDAO;
import com.maxelyz.core.model.dao.CaseTypeDAO;
import com.maxelyz.core.model.dao.ServiceTypeDAO;
import com.maxelyz.core.model.dao.front.search.SearchCustomerDAO;
import com.maxelyz.core.model.entity.CaseType;
import com.maxelyz.core.model.entity.CustomerType;
import com.maxelyz.core.model.entity.ServiceType;
import com.maxelyz.core.model.value.front.search.SearchCustomerValue;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Prawait
 */
@ManagedBean(name="searchCustomerController")
@SessionScoped
public class SearchCustomerController extends BaseController implements Serializable{
    private static String SEARCHRESULT = "searchCustomer.xhtml";

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
    @ManagedProperty(value = "#{serviceTypeDAO}")
    private ServiceTypeDAO serviceTypeDAO;

    private Integer serviceTypeId;
    private List<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
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
    private Integer scrollerPage;
    
    // Car
    private String carBrand;
    private String carModel;
    private Integer carFromYear;
    private Integer carToYear;
    private String carGroup;
    private String carNo;

    /** Creates a new instance of SearchCustomerController */
    public SearchCustomerController() {
    }

    @PostConstruct
    public void initialize(){
        if (!SecurityUtil.isPermitted("searchcustomer:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        JSFUtil.getUserSession().setFirstPage("search");
        JSFUtil.getUserSession().setMainPage("search");
        removeCustomerSession();
        serviceTypeList = serviceTypeDAO.findServiceTypeEntities();
//        customerTypeList = getCustomerTypeDAO().findCustomerTypeEntities();
//        caseTypes = getCaseTypeDAO().findCaseTypeEntities();
    }
        
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("searchcustomer:add");
    }
    
    public String searchCustomer(){
        if (!SecurityUtil.isPermitted("searchcustomer:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        if (scrollerPage == null || scrollerPage == 0) {
            scrollerPage = 1;
        }
        int maxResult = JSFUtil.getApplication().getMaxrows();
        int firstResult = ((scrollerPage - 1) * maxResult);
        searchCustomerList = getSearchCustomerDAO().searchCustomer(customerName, identityNo, address, phoneNo, gender, customerTypeId,
                    account, caseId, contactDateFrom, contactDateTo, channelId, caseTypeId, priority, description, status, createdBy, carBrand, carModel, carFromYear, carToYear, carGroup, carNo, serviceTypeId, 0, 1000);
//        totalRows = getSearchCustomerDAO().searchCustomereRowCount(customerName, identityNo, address, phoneNo, gender, customerTypeId,
//                account, gender, contactDateFrom, contactDateTo, channelId, caseTypeId, priority, description, status, createdBy);

        return SEARCHRESULT;
    }

    public void searchPopupCustomer(){
        searchCustomerList = getSearchCustomerDAO().searchCustomer();
    }

    public List<SearchCustomerValue> getSelectCustomerList() {
//        if (searchCustomerList == null){
//            Integer rowPerPage = 100;
//            searchCustomerList = getSearchCustomerDAO().searchCustomer(customerName, identityNo, address, phoneNo, gender, customerTypeId, account, gender, contactDateFrom, contactDateTo, channelId, caseTypeId, priority, description, status, createdBy, 0, rowPerPage);
//            totalRows = getSearchCustomerDAO().searchCustomereRowCount(customerName, identityNo, address, phoneNo, gender, customerTypeId, account, gender, contactDateFrom, contactDateTo, channelId, caseTypeId, priority, description, status, createdBy);
//            searchCustomerList = null;
//            totalRows = 0;
//        }
        return searchCustomerList;
    }

    public void removeCustomerSession(){
        JSFUtil.getUserSession().setCustomerDetail(null);
    }

    public void clearValue() {
        customerName = "";
        identityNo = "";
        address = "";
        phoneNo = "";
        gender = "";
        customerTypeId = 0;
        account = "";
        caseId = "";
        contactDateFrom = null;
        contactDateTo = null;
        channelId = 0;
        caseTypeId = 0;
        priority = "";
        description = "";
        status = "";
        createdBy = "";
        carBrand = "";
        carModel = "";
        carFromYear = 0;
        carToYear = 0;
        carGroup = "";
        carNo = "";
    }

    //call from /share/callalertpopup.xhtml
    public void telephoneListener(ActionEvent event) {
        clearValue();
        String telno = JSFUtil.getRequestParameterMap("telephoneno");
        if (telno!=null && telno.length()>0) {
            this.phoneNo = telno;
            this.searchCustomer();
        }
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
        if (!SecurityUtil.isPermitted("searchcustomer:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
//        if (searchCustomerList == null){
//            Integer rowPerPage = 100;
//            searchCustomerList = getSearchCustomerDAO().searchCustomer(customerName, identityNo, address, phoneNo, gender, customerTypeId, account, gender, contactDateFrom, contactDateTo, channelId, caseTypeId, priority, description, status, createdBy, 0, rowPerPage);
//            totalRows = getSearchCustomerDAO().searchCustomereRowCount(customerName, identityNo, address, phoneNo, gender, customerTypeId, account, gender, contactDateFrom, contactDateTo, channelId, caseTypeId, priority, description, status, createdBy);
//            searchCustomerList = null;
//            totalRows = 0;
//        }
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
//        List caseTypeList = new ArrayList();
//        caseTypeList.add(new SelectItem(null,CrmConstant.ALL));
//        for(CaseType caseType : caseTypes){
//            if(caseType.getId() != null){
//                caseTypeList.add(new SelectItem(caseType.getId(),caseType.getName()));
//            }
//        }
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

    public Integer getScrollerPage() {
        return scrollerPage;
    }

    public void setScrollerPage(Integer scrollerPage) {
        this.scrollerPage = scrollerPage;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public Integer getCarFromYear() {
        return carFromYear;
    }

    public void setCarFromYear(Integer carFromYear) {
        this.carFromYear = carFromYear;
    }

    public Integer getCarToYear() {
        return carToYear;
    }

    public void setCarToYear(Integer carToYear) {
        this.carToYear = carToYear;
    }

    public String getCarGroup() {
        return carGroup;
    }

    public void setCarGroup(String carGroup) {
        this.carGroup = carGroup;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public ServiceTypeDAO getServiceTypeDAO() {
        return serviceTypeDAO;
    }

    public void setServiceTypeDAO(ServiceTypeDAO serviceTypeDAO) {
        this.serviceTypeDAO = serviceTypeDAO;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public List<ServiceType> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(List<ServiceType> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }

}
