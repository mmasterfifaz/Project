/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.controller.front.search;

import com.maxelyz.core.constant.CrmConstant;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.model.value.front.search.SearchAccountValue;
import com.maxelyz.core.model.dao.front.search.SearchAccountDAO;
import com.maxelyz.core.model.dao.CustomerTypeDAO;
import com.maxelyz.core.model.entity.CustomerType;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.model.SelectItem;

/**
 *
 * @author Manop
 */
@ManagedBean(name="searchAccountController")
@SessionScoped
public class SearchAccountController {
    private static Logger log = Logger.getLogger(SearchCaseController.class);
    private static String REFRESH = "searchAccount.xhtml";

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<SearchAccountValue> searchAccountValues;

    @ManagedProperty(value="#{searchAccountDAO}")
    private SearchAccountDAO searchAccountDAO;
    @ManagedProperty(value="#{customerTypeDAO}")
    private CustomerTypeDAO customerTypeDAO;

    private String accountName;
    private String accountId;

    private String customerName;
    private String phoneNo;
    private Integer customerTypeId;
    private List<CustomerType > customerTypeList = new ArrayList<CustomerType>();
    
    // Paging properties
    private Integer currentPage;
    private Integer totalPages;
    private Integer totalRows;
    private Integer scrollerPage;
    
    @PostConstruct
    public void initialize() {
      	if (!SecurityUtil.isPermitted("searchaccount:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        JSFUtil.getUserSession().setFirstPage("search");
        customerTypeList = getCustomerTypeDAO().findCustomerTypeEntities();
    }
        
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("searchaccount:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("searchaccount:delete");
    }

    public String searchAccountAction(){
       	if (!SecurityUtil.isPermitted("searchaccount:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
       /* try{
            searchAccountValues = getSearchAccountDAO().searchAccount(accountId.trim().length() > 0?Integer.valueOf(accountId):0, accountName.trim(), customerName.trim(), phoneNo.trim(), customerTypeId, 0, 10);
        }catch(Exception e){
            log.error(e);
        }
        return REFRESH;*/
    
      if (scrollerPage == null || scrollerPage == 0) {
            scrollerPage = 1;
        }
        int maxResult = JSFUtil.getApplication().getMaxrows();
        int firstResult = ((scrollerPage - 1) * maxResult);
        searchAccountValues = getSearchAccountDAO().searchAccount(accountId.trim().length() > 0?Integer.valueOf(accountId):0, accountName.trim(), customerName.trim(), phoneNo.trim(), customerTypeId, 0, 1000);

        return REFRESH;
    }

    public void searchPopupAccountAction(){

            searchAccountValues = getSearchAccountDAO().searchAccount(null, accountName.trim(), null, null, null, 0, 10);

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
     * @return the searchAccountValue
     */
    public List<SearchAccountValue> getSearchAccountValues() {
      	if (!SecurityUtil.isPermitted("searchaccount:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
//        if (searchAccountValues == null){
//            searchAccountValues = getSearchAccountDAO().searchAccount();
//        }
        return searchAccountValues;
    }

    /**
     * @param searchAccountValue the searchAccountValue to set
     */
    public void setSearchAccountValues(List<SearchAccountValue> searchAccountValues) {
        this.searchAccountValues = searchAccountValues;
    }

    /**
     * @return the searchAccountDAO
     */
    public SearchAccountDAO getSearchAccountDAO() {
        return searchAccountDAO;
    }

    /**
     * @param searchAccountDAO the searchAccountDAO to set
     */
    public void setSearchAccountDAO(SearchAccountDAO searchAccountDAO) {
        this.searchAccountDAO = searchAccountDAO;
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
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName the accountName to set
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * @return the accountId
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
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
     * @return the customerTypeList
     */
    public List getCustomerTypeList() {
        List customerTypeValueList = new ArrayList();
        customerTypeValueList.add(new SelectItem(null, CrmConstant.ALL));
        for (CustomerType customerType : customerTypeList){
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

    public Integer getScrollerPage() {
        return scrollerPage;
    }

    public void setScrollerPage(Integer scrollerPage) {
        this.scrollerPage = scrollerPage;
    }
    
}
