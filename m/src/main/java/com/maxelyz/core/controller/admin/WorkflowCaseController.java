/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.bean.ViewScoped;
/**
 *
 * @author Administrator
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class WorkflowCaseController {
    private static Logger log = Logger.getLogger(CaseDetailController.class);
    private static String REFRESH = "workflowcase.xhtml?faces-redirect=true";
    private static String EDIT = "workflowcaseedit.xhtml";
  
    private Map<WorkflowCasePK, Boolean> selectedIds = new ConcurrentHashMap<WorkflowCasePK, Boolean>();
//    private WorkflowCase workflowCase;
    private List<WorkflowCase> workflowCases;
    private Map<String, Integer> caseTypeList;
    private Map<String, Integer> caseTopicList;
    private Map<String, Integer> caseDetailList;
    private Map<String, Integer> caseRequestList;
    private Map<String, Integer> serviceTypeList;
    private Map<String, Integer> businessUnitList;
    private Map<String, Integer> locationList;
    private Map<String, Integer> userGroupList;
    private Integer caseTypeId=0;
    private Integer caseTopicId=0;
    private Integer caseDetailId=0;
    private Integer caseRequestId=0;
    private Integer serviceTypeId=0;
    private Integer businessUnitId=0;
    private Integer locationId=0;
    private Integer userGroupId=0;
    
    @ManagedProperty(value="#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;
    @ManagedProperty(value="#{caseTopicDAO}")
    private CaseTopicDAO caseTopicDAO;
    @ManagedProperty(value="#{caseDetailDAO}")
    private CaseDetailDAO caseDetailDAO;
    @ManagedProperty(value="#{caseRequestDAO}")
    private CaseRequestDAO caseRequestDAO;
    @ManagedProperty(value="#{serviceTypeDAO}")
    private ServiceTypeDAO serviceTypeDAO;
    @ManagedProperty(value = "#{businessUnitDAO}")
    private BusinessUnitDAO businessUnitDAO;
    @ManagedProperty(value = "#{locationDAO}")
    private LocationDAO locationDAO;
    @ManagedProperty(value="#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value="#{workflowCaseDAO}")
    private WorkflowCaseDAO workflowCaseDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:caseworkflow:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
 
        caseTypeList = this.getCaseTypeDAO().getCaseTypeList();
        serviceTypeList = this.getServiceTypeDAO().getServiceTypeList();
        userGroupList = this.getUserGroupDAO().getUserGroupList();
        workflowCases = this.getWorkflowCaseDAO().findWorkflowCaseEntities();
    }
    
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:caseworkflow:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:caseworkflow:delete");
    }
    
     public String editAction() {
       return EDIT;
    }

    public String deleteAction() throws Exception {
        WorkflowCaseDAO dao = getWorkflowCaseDAO();
        try {
            for (Map.Entry<WorkflowCasePK, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    dao.deleteWorkflowCase(item.getKey());
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public Map<WorkflowCasePK, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<WorkflowCasePK, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<WorkflowCase> getWorkflowCases() {
        return workflowCases;
    }

    public void setWorkflowCases(List<WorkflowCase> workflowCases) {
        this.workflowCases = workflowCases;
    }

    public Integer getCaseTopicId() {
        return caseTopicId;
    }

    public void setCaseTopicId(Integer caseTopicId) {
        this.caseTopicId = caseTopicId;
    }

    public Integer getCaseTypeId() {
        return caseTypeId;
    }

    public void setCaseTypeId(Integer caseTypeId) {
        this.caseTypeId = caseTypeId;
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

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Integer getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Integer businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Map<String, Integer> getCaseTypeList() {
        return caseTypeList;
    }

    public Map<String, Integer> getCaseTopicList() {
        return caseTopicList;
    }
    
    public Map<String, Integer> getCaseDetailList() {
        return caseDetailList;
    }
    
    public Map<String, Integer> getCaseRequestList() {
        return caseRequestList;
    }
    
    public Map<String, Integer> getUserGroupList() {
        return userGroupList;
    }
     
    public Map<String, Integer> getServiceTypeList() {
        return serviceTypeList;
    }

    public Map<String, Integer> getBusinessUnitList() {
        return businessUnitList;
    }

    public Map<String, Integer> getLocationList() {
        return locationList;
    }
    
    // Listener
    public void caseTypeListener(ValueChangeEvent event) {
        caseTypeId = (Integer) event.getNewValue();
        setCaseTopics(caseTypeId);
        caseTopicId = 0;
        caseDetailId = 0;
        caseRequestId = 0;
        if(caseDetailList != null)
            caseDetailList.clear();
        if(caseRequestList != null)
            caseRequestList.clear();
        searchAction();
    }

    public void caseTopicListener(ValueChangeEvent event) {
        caseTopicId = (Integer) event.getNewValue();
        setCaseDetails(caseTopicId);
        caseDetailId = 0;
        caseRequestId = 0;
        if(caseRequestList != null)
            caseRequestList.clear();
        searchAction();
    }
    
    public void caseDetailListener(ValueChangeEvent event) {
        caseDetailId = (Integer) event.getNewValue();
        setCaseRequest(caseDetailId);
        caseRequestId = 0;
        searchAction();
    }
    
    public void serviceTypeListener(ValueChangeEvent event) {
        serviceTypeId = (Integer) event.getNewValue();
        setBusinessUnitList(serviceTypeId);
        businessUnitId = 0;
        locationId = 0;
        if(locationList != null)
            locationList.clear();
        searchAction();
    }
    
    public void businessUnitListener(ValueChangeEvent event) {
        businessUnitId = (Integer) event.getNewValue();
        setLocationList(businessUnitId);
        locationId = 0;
        searchAction();
    }
        
    public void searchAction() {
        workflowCases = this.getWorkflowCaseDAO().getWorkflowCaseBySearch(caseTypeId, caseTopicId, caseDetailId, caseRequestId, 
                                                                          serviceTypeId, businessUnitId, locationId, userGroupId);
    }

    public void setCaseTopics(Integer caseTypeId) {
        CaseTopicDAO dao = getCaseTopicDAO();
        List<CaseTopic> caseTopics = dao.findCaseTopicByCaseTypeStatus(caseTypeId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseTopic obj : caseTopics) {
            values.put(obj.getName(), obj.getId());
        }
        caseTopicList = values;
    }
    
    public void setCaseDetails(Integer caseTopicId) {
        CaseDetailDAO dao = getCaseDetailDAO();
        List<CaseDetail> caseDetails = dao.findCaseDetailByCaseTopicStatus(caseTopicId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseDetail obj : caseDetails) {
            values.put(obj.getName(), obj.getId());
        }
        caseDetailList = values;
    }
    
    public void setCaseRequest(Integer caseDetailId) {
        CaseRequestDAO dao = getCaseRequestDAO();
        List<CaseRequest> caseRequests = dao.findCaseRequestByCaseDetailStatus(caseDetailId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseRequest obj : caseRequests) {
            values.put(obj.getName(), obj.getId());
        }
        caseRequestList = values;
    }
    
    public void setBusinessUnitList(Integer serviceTypeId) {
        BusinessUnitDAO dao = getBusinessUnitDAO();
        List<BusinessUnit> businessUnits = dao.findBusinessUnitByServiceTypeId(serviceTypeId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (BusinessUnit obj : businessUnits) {
            values.put(obj.getName(), obj.getId());
        }
        businessUnitList = values;
    }
    
    public void setLocationList(Integer businessUnitId) {
        LocationDAO dao = getLocationDAO();
        List<Location> locations = dao.findLocationByBusinessUnit(businessUnitId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Location obj : locations) {
            values.put(obj.getName(), obj.getId());
        }
        locationList = values;
    }
    
    // Get set DAO
    public CaseTopicDAO getCaseTopicDAO() {
        return caseTopicDAO;
    }

    public void setCaseTopicDAO(CaseTopicDAO caseTopicDAO) {
        this.caseTopicDAO = caseTopicDAO;
    }

    public CaseTypeDAO getCaseTypeDAO() {
        return caseTypeDAO;
    }

    public void setCaseTypeDAO(CaseTypeDAO caseTypeDAO) {
        this.caseTypeDAO = caseTypeDAO;
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

    public ServiceTypeDAO getServiceTypeDAO() {
        return serviceTypeDAO;
    }

    public void setServiceTypeDAO(ServiceTypeDAO serviceTypeDAO) {
        this.serviceTypeDAO = serviceTypeDAO;
    }

    public WorkflowCaseDAO getWorkflowCaseDAO() {
        return workflowCaseDAO;
    }

    public void setWorkflowCaseDAO(WorkflowCaseDAO workflowCaseDAO) {
        this.workflowCaseDAO = workflowCaseDAO;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public BusinessUnitDAO getBusinessUnitDAO() {
        return businessUnitDAO;
    }

    public void setBusinessUnitDAO(BusinessUnitDAO businessUnitDAO) {
        this.businessUnitDAO = businessUnitDAO;
    }

    public LocationDAO getLocationDAO() {
        return locationDAO;
    }

    public void setLocationDAO(LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }
    
}
