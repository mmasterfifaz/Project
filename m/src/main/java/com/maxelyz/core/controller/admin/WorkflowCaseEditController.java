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
public class WorkflowCaseEditController {
    private static Logger log = Logger.getLogger(WorkflowCaseEditController.class);
    private static String REDIRECT_PAGE = "workflowcase.jsf";
    private static String SUCCESS = "workflowcase.xhtml?faces-redirect=true";
    private static String FAILURE = "workflowcaseedit.xhtml";
    
    private CaseRequest caseRequest;
    private Map<String, Integer> userGroupList;
    private List<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
    private List<BusinessUnit> businessUnitList = new ArrayList<BusinessUnit>();
    private List<Location> locationList = new ArrayList<Location>();
    private Map<String, Integer> workflowRuleList;
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<CaseRequest> caseRequests;
    private List<WorkflowCase> workflowCases;
    private Integer userGroupId;
    private Integer serviceTypeId;
    private Integer businessUnitId;
    private Integer locationId;
    private Integer lowRuleId;
    private Integer mediumRuleId;
    private Integer highRuleId;
    private Integer immediateRuleId;
    private String message;
    private String mode;
    
    @ManagedProperty(value="#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{caseRequestDAO}")
    private CaseRequestDAO caseRequestDAO;
    @ManagedProperty(value="#{workflowRuleDAO}")
    private WorkflowRuleDAO workflowRuleDAO;
    @ManagedProperty(value="#{workflowCaseDAO}")
    private WorkflowCaseDAO workflowCaseDAO;
    @ManagedProperty(value = "#{serviceTypeDAO}")
    private ServiceTypeDAO serviceTypeDAO;
    @ManagedProperty(value = "#{businessUnitDAO}")
    private BusinessUnitDAO businessUnitDAO;
    @ManagedProperty(value = "#{locationDAO}")
    private LocationDAO locationDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:caseworkflow:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        String userGroupID = (String) JSFUtil.getRequestParameterMap("userGroupId");
        String serviceTypeID = (String) JSFUtil.getRequestParameterMap("serviceTypeId");
        String businessUnitID = (String) JSFUtil.getRequestParameterMap("businessUnitId");
        String locationID = (String) JSFUtil.getRequestParameterMap("locationId");
        String lowRuleID = (String) JSFUtil.getRequestParameterMap("lowRuleId");
        String mediumRuleID = (String) JSFUtil.getRequestParameterMap("mediumRuleId");
        String highRuleID = (String) JSFUtil.getRequestParameterMap("highRuleId");
        String immediateRuleID = (String) JSFUtil.getRequestParameterMap("immediateRuleId");

        if (userGroupID == null || userGroupID.isEmpty()) {
            mode = "add";
        } else {
           mode = "edit";
           userGroupId = userGroupID==null || userGroupID.equals("")?null:Integer.parseInt(userGroupID);
           serviceTypeList = userGroupDAO.getServiceTypeList(userGroupId);
           serviceTypeId = serviceTypeID==null || serviceTypeID.equals("")?null:Integer.parseInt(serviceTypeID);
           businessUnitList = userGroupDAO.getBusinessUnitList(userGroupId, serviceTypeId);
           businessUnitId = businessUnitID==null || businessUnitID.equals("")?null:Integer.parseInt(businessUnitID);
           locationList = userGroupDAO.getLocationList(userGroupId, serviceTypeId, businessUnitId);
           locationId = locationID==null || locationID.equals("")?null:Integer.parseInt(locationID);
           lowRuleId = lowRuleID==null || lowRuleID.equals("")?null:Integer.parseInt(lowRuleID);
           mediumRuleId = mediumRuleID==null || mediumRuleID.equals("")?null:Integer.parseInt(mediumRuleID);
           highRuleId = highRuleID==null || highRuleID.equals("")?null:Integer.parseInt(highRuleID);
           immediateRuleId = immediateRuleID==null || immediateRuleID.equals("")?null:Integer.parseInt(immediateRuleID);
           
           workflowCases = this.getWorkflowCaseDAO().findWorkflowCaseSameRule(userGroupId, serviceTypeId, businessUnitId, locationId, 
                                                                              lowRuleId, mediumRuleId, highRuleId, immediateRuleId);
           caseRequests = this.getCaseRequestDAO().findCaseRequestEditByWorkflowCase(workflowCases);
        }
        userGroupList = this.getUserGroupDAO().getUserGroupList();
        workflowRuleList = this.getWorkflowRuleDAO().getWorkflowRuleList();
    }
    
     public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:caseworkflow:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:caseworkflow:edit"); 
       }
    }
     
    public void userGroupListener(ValueChangeEvent event) {
        userGroupId = (Integer) event.getNewValue();
        serviceTypeId = null;
        businessUnitId = null;
        locationId = null;
        if(serviceTypeList != null || !serviceTypeList.isEmpty())
            serviceTypeList.clear();
        if(businessUnitList != null || !businessUnitList.isEmpty())
            businessUnitList.clear();
        if(locationList != null || !locationList.isEmpty())
            locationList.clear();
        if(caseRequests != null)
            caseRequests.clear();
        
        if(userGroupId != null && userGroupId != 0) {
            serviceTypeList = userGroupDAO.getServiceTypeList(userGroupId);            
        }
    }
    
    public void serviceTypeListener(ValueChangeEvent event){
        if(caseRequests != null)
            caseRequests.clear();
        try {
            serviceTypeId = (Integer) event.getNewValue();
            businessUnitId = null;
            locationId = null;
            if(businessUnitList != null || !businessUnitList.isEmpty())
                businessUnitList.clear();
            if(locationList != null || !locationList.isEmpty())
                locationList.clear();

            if(serviceTypeId != null && serviceTypeId != 0){
                businessUnitList = userGroupDAO.getBusinessUnitList(userGroupId, serviceTypeId);  
                
                if(businessUnitList != null && businessUnitList.size() == 1) {
                    businessUnitId =  businessUnitList.get(0).getId();
                    locationList = userGroupDAO.getLocationList(JSFUtil.getUserSession().getUserGroup().getId(), serviceTypeId, businessUnitId);  
                    if(locationList != null && locationList.size() == 1) {
                        locationId = locationList.get(0).getId();
                        setLocationId(locationId);
                        autoCheckcaseRequest();
                    } else {
                        setLocationId(0);
                    }
                } else {
                    setBusinessUnitId(0);
                    if(locationList != null && !locationList.isEmpty())
                        locationList.clear();
                    locationId = null;
                }   
            }
        } catch(Exception e) {
            log.error(e);
        }
    }

    public void businessUnitListener(ValueChangeEvent event){
        if(caseRequests != null)
            caseRequests.clear();
        try {
            businessUnitId = (Integer) event.getNewValue();
            if(businessUnitId != null && businessUnitId != 0) {
                locationList = userGroupDAO.getLocationList(userGroupId, serviceTypeId, businessUnitId);  
                if(locationList != null && locationList.size() == 1) {
                    locationId = locationList.get(0).getId();
                    setLocationId(locationId);
                    autoCheckcaseRequest();
                } else {
                    setLocationId(0);
                }
            }
        } catch(Exception e) {
            log.error(e);
        }
    }
    
    public void autoCheckcaseRequest() {
        if(serviceTypeId != null && serviceTypeId != 0 && businessUnitId != null && businessUnitId != 0 && locationId != null && locationId != 0) {
            caseRequests = this.getCaseRequestDAO().findWorkflowCaseAvailable(userGroupId, serviceTypeId, businessUnitId, locationId);
        }
    }
    
    public void caseRequestListener(ValueChangeEvent event) {
        locationId  = (Integer) event.getNewValue();
        if(caseRequests != null)
            caseRequests.clear();
        if(serviceTypeId != null && serviceTypeId != 0 && businessUnitId != null && businessUnitId != 0 && locationId != null && locationId != 0) {
            caseRequests = this.getCaseRequestDAO().findWorkflowCaseAvailable(userGroupId, serviceTypeId, businessUnitId, locationId);//findCaseRequestByWorkflowCase(wfcList);
        }
    }
    
    public String saveAction() {
        try {
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            WorkflowRule lowRule = null;
            WorkflowRule mediumRule = null;
            WorkflowRule highRule = null;
            WorkflowRule immediateRule = null;
            if(lowRuleId != null && lowRuleId != 0)
                lowRule = new WorkflowRule(lowRuleId);
            if(mediumRuleId != null && mediumRuleId != 0)
                mediumRule = new WorkflowRule(mediumRuleId);
            if(highRuleId !=null && highRuleId != 0)
                highRule = new WorkflowRule(highRuleId);
            if(immediateRuleId != null && immediateRuleId != 0)
                immediateRule = new WorkflowRule(immediateRuleId);
            if(mode.equals("add")) {
                for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                    if (item.getValue()) {
                        caseRequest = this.getCaseRequestDAO().findCaseRequest(item.getKey());
                        WorkflowCasePK wkPK = new WorkflowCasePK();
                        WorkflowCase workflowCase = new WorkflowCase();
                        wkPK.setUserGroupId(userGroupId);
                        wkPK.setServiceTypeId(serviceTypeId);
                        wkPK.setBusinessUnitId(businessUnitId);
                        wkPK.setLocationId(locationId);
                        wkPK.setCaseDetailId(caseRequest.getCaseDetailId().getId());
                        wkPK.setCaseRequestId(caseRequest.getId());
                        workflowCase.setCreateBy(username);
                        workflowCase.setCreateDate(now);
                        workflowCase.setLowPriorityWorkflowRuleId(lowRule);
                        workflowCase.setMediumPriorityWorkflowRuleId(mediumRule);
                        workflowCase.setHighPriorityWorkflowRuleId(highRule);
                        workflowCase.setImmediatelyPriorityWorkflowRuleId(immediateRule);
                        workflowCase.setWorkflowCasePK(wkPK);
                        this.getWorkflowCaseDAO().create(workflowCase);
                    }
                }
            } else {
                for(WorkflowCase wkfc: workflowCases) {
                    wkfc.setUpdateBy(username);
                    wkfc.setUpdateDate(now);
                    wkfc.setLowPriorityWorkflowRuleId(lowRule);
                    wkfc.setMediumPriorityWorkflowRuleId(mediumRule);
                    wkfc.setHighPriorityWorkflowRuleId(highRule);
                    wkfc.setImmediatelyPriorityWorkflowRuleId(immediateRule);
                    this.getWorkflowCaseDAO().edit(wkfc);
                }
                
            }
        } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
    }
    
    public String backAction() {
        return SUCCESS;
    }
    
    //Get Set Method   
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
    
    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Map<String, Integer> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(Map<String, Integer> userGroupList) {
        this.userGroupList = userGroupList;
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

    public List<BusinessUnit> getBusinessUnitList() {
        return businessUnitList;
    }

    public void setBusinessUnitList(List<BusinessUnit> businessUnitList) {
        this.businessUnitList = businessUnitList;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    public List<ServiceType> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(List<ServiceType> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }

    
    public Map<String, Integer> getWorkflowRuleList() {
        return workflowRuleList;
    }
    
    public List<CaseRequest> getCaseRequestList() {
        return caseRequests;
    }
    
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public Integer getHighRuleId() {
        return highRuleId;
    }

    public void setHighRuleId(Integer highRuleId) {
        this.highRuleId = highRuleId;
    }

    public Integer getImmediateRuleId() {
        return immediateRuleId;
    }

    public void setImmediateRuleId(Integer immediateRuleId) {
        this.immediateRuleId = immediateRuleId;
    }

    public Integer getLowRuleId() {
        return lowRuleId;
    }

    public void setLowRuleId(Integer lowRuleId) {
        this.lowRuleId = lowRuleId;
    }

    public Integer getMediumRuleId() {
        return mediumRuleId;
    }

    public void setMediumRuleId(Integer mediumRuleId) {
        this.mediumRuleId = mediumRuleId;
    }
    
    // DAO
    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public WorkflowRuleDAO getWorkflowRuleDAO() {
        return workflowRuleDAO;
    }

    public void setWorkflowRuleDAO(WorkflowRuleDAO workflowRuleDAO) {
        this.workflowRuleDAO = workflowRuleDAO;
    }

    public CaseRequestDAO getCaseRequestDAO() {
        return caseRequestDAO;
    }

    public void setCaseRequestDAO(CaseRequestDAO caseRequestDAO) {
        this.caseRequestDAO = caseRequestDAO;
    }

    public WorkflowCaseDAO getWorkflowCaseDAO() {
        return workflowCaseDAO;
    }

    public void setWorkflowCaseDAO(WorkflowCaseDAO workflowCaseDAO) {
        this.workflowCaseDAO = workflowCaseDAO;
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

    public ServiceTypeDAO getServiceTypeDAO() {
        return serviceTypeDAO;
    }

    public void setServiceTypeDAO(ServiceTypeDAO serviceTypeDAO) {
        this.serviceTypeDAO = serviceTypeDAO;
    }
}
