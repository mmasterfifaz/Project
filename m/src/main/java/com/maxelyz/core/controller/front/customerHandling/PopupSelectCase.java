/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import org.apache.log4j.Logger;

import com.maxelyz.core.model.dao.front.search.SearchCaseDAO;
import com.maxelyz.core.model.value.front.customerHandling.ContactCaseInfoValue;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
/**
 *
 * @author admin
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class PopupSelectCase implements Serializable {
    private static Logger log = Logger.getLogger(PopupSelectCase.class);
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<ContactCaseInfoValue> searchCasePopupValues = new ArrayList<ContactCaseInfoValue>();
    @ManagedProperty(value = "#{searchCaseDAO}")
    private SearchCaseDAO searchCaseDAO;

    private String caseId;
    private Date contactDateFrom;
    private Date contactDateTo;

    @PostConstruct
    public void initialize() {

    }

    public void searchRelatedCaseAction(ActionEvent event) {
        try {
            Integer customerId = null;
            if (JSFUtil.getUserSession().getCustomerDetail() != null) {
                customerId = JSFUtil.getUserSession().getCustomerDetail().getId();
                searchCasePopupValues = searchCaseDAO.searchCase(caseId, contactDateFrom, contactDateTo, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, customerId, null, null, null, 0, 1000);
            }
        } catch (Exception e) {
            log.error(e);
        }
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
     * @return the caseID
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     * @param caseID the caseID to set
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

    public List<ContactCaseInfoValue> getSearchCasePopupValues() {
        return searchCasePopupValues;
    }

    public void setSearchCasePopupValues(List<ContactCaseInfoValue> searchCasePopupValues) {
        this.searchCasePopupValues = searchCasePopupValues;
    }
}
