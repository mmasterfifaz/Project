package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.BusinessUnitDAO;
import com.maxelyz.core.model.dao.CaseDetailDAO;
import com.maxelyz.core.model.dao.CaseRequestDAO;
import com.maxelyz.core.model.dao.CaseTopicDAO;
import com.maxelyz.core.model.dao.CaseTypeDAO;
import com.maxelyz.core.model.dao.ConfigurationAutocaseDAO;
import com.maxelyz.core.model.dao.ConfigurationDAO;
import com.maxelyz.core.model.dao.LocationDAO;
import com.maxelyz.core.model.dao.ServiceTypeDAO;
import com.maxelyz.core.model.entity.BusinessUnit;
import com.maxelyz.core.model.entity.CaseDetail;
import com.maxelyz.core.model.entity.CaseRequest;
import com.maxelyz.core.model.entity.CaseTopic;
import com.maxelyz.core.model.entity.CaseType;
import com.maxelyz.core.model.entity.Configuration;
import com.maxelyz.core.model.entity.ConfigurationAutocase;
import com.maxelyz.core.model.entity.Location;
import com.maxelyz.core.model.entity.ServiceType;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

@ManagedBean
@ViewScoped
public class ConfigurationAutoCaseController {

    private static Log log = LogFactory.getLog(ConfigurationAutoCaseController.class);
    private static String REDIRECT_PAGE = "configuration.jsf";
    private static String SUCCESS = "configuration.xhtml?faces-redirect=true";
    private static String FAILURE = "configurationautocase.xhtml";
    private String message;
    private Integer caseTypeId;
    private Integer caseTopicId;
    private Integer caseDetailId;
    private Integer caseRequestId;
    
    private Integer serviceTypeId;
    private Integer businessUnitId;
    private Integer locationId;
    
    private Map<String, Integer> caseTopicsList;
    private Map<String, Integer> caseDetailsList;
    private Map<String, Integer> caseRequestsList;
    
    private Map<String, Integer> serviceTypesList;
    private Map<String, Integer> businessUnitsList;
    private Map<String, Integer> locationsList;
    
    
    private ConfigurationAutocase configurationAutocase;
   
    @ManagedProperty(value = "#{caseRequestDAO}")
    private CaseRequestDAO caseRequestDAO;
    @ManagedProperty(value = "#{caseDetailDAO}")
    private CaseDetailDAO caseDetailDAO;
    @ManagedProperty(value = "#{caseTopicDAO}")
    private CaseTopicDAO caseTopicDAO;
    @ManagedProperty(value = "#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;
    @ManagedProperty(value = "#{serviceTypeDAO}")
    private ServiceTypeDAO serviceTypeDAO;
    @ManagedProperty(value = "#{businessUnitDAO}")
    private BusinessUnitDAO businessUnitDAO;
    @ManagedProperty(value = "#{locationDAO}")
    private LocationDAO locationDAO;
    
    
    @ManagedProperty(value = "#{configurationAutocaseDAO}")
    private ConfigurationAutocaseDAO configurationAutocaseDAO;
 
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:systemconfiguration:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        configurationAutocase = configurationAutocaseDAO.findConfigurationAutocase(1);  
        if (configurationAutocase!=null) {    
            caseRequestId = configurationAutocase.getCaseRequestId();
            CaseRequest caseRequest = caseRequestDAO.findCaseRequest(caseRequestId);
            if(caseRequest != null) {
                caseDetailId = caseRequest.getCaseDetailId().getId();
                caseTopicId = caseRequest.getCaseDetailId().getCaseTopicId().getId();
                caseTypeId = caseRequest.getCaseDetailId().getCaseTopicId().getCaseTypeId().getId();
                this.setCaseTopics(caseTypeId);
                this.setCaseDetails(caseTopicId);
                this.setCaseRequests(caseDetailId);
            }
            
            
            serviceTypeId = configurationAutocase.getServicetypeId();
            locationId = configurationAutocase.getLocationId();
            Location location = locationDAO.findLocation(locationId);
            if(location != null) {
                businessUnitId = location.getBusinessUnit().getId();
            }
            this.setBusinessUnits(serviceTypeId);
            this.setLocations(businessUnitId);
            
        } else {
            configurationAutocase = new ConfigurationAutocase();
        }
    }
    
    
    public boolean isSavePermitted() {
 	   return SecurityUtil.isPermitted("admin:systemconfiguration:edit"); 
    }
      
    public String saveAction() {
        try {       
            configurationAutocase.setId(new Integer(1));
            configurationAutocase.setCaseRequestId(caseRequestId);
            configurationAutocase.setServicetypeId(serviceTypeId);
            configurationAutocase.setLocationId(locationId);
   
            if (configurationAutocaseDAO.findConfigurationAutocase(new Integer(1))==null) {
                
                configurationAutocaseDAO.create(configurationAutocase);
            } else {
                configurationAutocaseDAO.edit(configurationAutocase); 
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

    public ConfigurationAutocase getConfigurationAutocase() {
        return configurationAutocase;
    }

    public void setConfigurationAutocase(ConfigurationAutocase configurationAutocase) {
        this.configurationAutocase = configurationAutocase;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Map<String, Integer> getCaseTypes() {
        CaseTypeDAO dao = getCaseTypeDAO();
        List<CaseType> caseTypes = dao.findCaseTypeEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseType obj : caseTypes) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    public void setCaseTopics(Integer caseTypeId) {
        CaseTopicDAO dao = getCaseTopicDAO();
        List<CaseTopic> caseTopics = dao.findCaseTopicByCaseType(caseTypeId);
        // List<CaseTopic> caseTopics = dao.findCaseTopicEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseTopic obj : caseTopics) {
            values.put(obj.getName(), obj.getId());
        }
        caseTopicsList = values;
    }

    public Map<String, Integer> getCaseTopics() {
        return caseTopicsList;
    }

    public void setCaseDetails(Integer caseTopicId) {
        CaseDetailDAO dao = getCaseDetailDAO();
        List<CaseDetail> caseDetails = dao.findCaseDetailByCaseTopic(caseTopicId);
        //List<CaseDetail> caseDetails = dao.findCaseDetailEntities();

        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseDetail obj : caseDetails) {
            values.put(obj.getName(), obj.getId());
        }
        caseDetailsList = values;
    }  

    public Map<String, Integer> getCaseDetails() {
        return caseDetailsList;
    }
    
    public void setCaseRequests(Integer caseDetailId) {
  
        List<CaseRequest> caseRequests = caseRequestDAO.findCaseRequestByCaseDetail(caseDetailId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseRequest obj : caseRequests) {
            values.put(obj.getName(), obj.getId());
        }
        caseRequestsList = values;
    }

    public Map<String, Integer> getCaseRequests() {
        return caseRequestsList;
    }

    
     public Map<String, Integer> getServiceTypes() {
        List<ServiceType> serviceTypes = serviceTypeDAO.findServiceTypeEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (ServiceType obj : serviceTypes) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    public void setBusinessUnits(Integer serviceTypeId) {    
        List<BusinessUnit> businessUnits = businessUnitDAO.findBusinessUnitByServiceTypeId(serviceTypeId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (BusinessUnit obj : businessUnits) {
            values.put(obj.getName(), obj.getId());
        }
        businessUnitsList = values;
    }

    public Map<String, Integer> getBusinessUnits() {
        return businessUnitsList;
    }
    
    public void setLocations(Integer locationId) {    
        List<Location> locations = locationDAO.findLocationByBusinessUnit(businessUnitId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Location obj : locations) {
            values.put(obj.getName(), obj.getId());
        }
        locationsList = values;
    }

    public Map<String, Integer> getLocations() {
        return locationsList;
    }
    
    //Listener
    public void caseTypeListener(ValueChangeEvent event) {
        caseTypeId = (Integer) event.getNewValue();
        caseTopicId = null;
        caseDetailId = null;
        caseRequestId = null;
        setCaseTopics(caseTypeId);
        caseDetailsList=null;
        caseRequestsList=null;
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void caseTopicListener(ValueChangeEvent event) {
        caseTopicId = (Integer) event.getNewValue();
        caseDetailId = null;
        caseRequestId = null;
        setCaseDetails(caseTopicId);
        caseRequestsList=null;
        FacesContext.getCurrentInstance().renderResponse();
    }
    
     public void caseDetailListener(ValueChangeEvent event) {
        caseRequestId = (Integer) event.getNewValue();
        setCaseRequests(caseRequestId);
        FacesContext.getCurrentInstance().renderResponse();
    }
     
     public void serviceTypeListener(ValueChangeEvent event) {
        serviceTypeId = (Integer) event.getNewValue();
        businessUnitId = null;
        locationId = null;       
        setBusinessUnits(serviceTypeId);
        locationsList=null;
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void businessUnitListener(ValueChangeEvent event) {
        businessUnitId = (Integer) event.getNewValue();
        setLocations(businessUnitId);
        FacesContext.getCurrentInstance().renderResponse();
    }

    //Backing
    public Integer getCaseTypeId() {
        return caseTypeId;
    }

    public void setCaseTypeId(Integer caseTypeId) {
        this.caseTypeId = caseTypeId;
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

    
    
    //Managed Properties
    public CaseRequestDAO getCaseRequestDAO() {
        return caseRequestDAO;
    }

    public void setCaseRequestDAO(CaseRequestDAO caseRequestDAO) {
        this.caseRequestDAO = caseRequestDAO;
    }

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

    public void setConfigurationAutocaseDAO(ConfigurationAutocaseDAO configurationAutocaseDAO) {
        this.configurationAutocaseDAO = configurationAutocaseDAO;
    }

    public ServiceTypeDAO getServiceTypeDAO() {
        return serviceTypeDAO;
    }

    public void setServiceTypeDAO(ServiceTypeDAO serviceTypeDAO) {
        this.serviceTypeDAO = serviceTypeDAO;
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
