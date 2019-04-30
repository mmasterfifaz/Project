/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.BusinessUnitDAO;
import com.maxelyz.core.model.dao.ServiceTypeDAO;
import com.maxelyz.core.model.entity.BusinessUnit;
import com.maxelyz.core.model.entity.ServiceType;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
@ManagedBean (name="businessUnitEditController")
@RequestScoped
//@ViewScoped
public class BusinessUnitEditController {
    private static Logger log = Logger.getLogger(BusinessUnitEditController.class);
    private static String REDIRECT_PAGE = "businessunit.jsf";
    private static String SUCCESS = "businessunit.xhtml?faces-redirect=true";
    private static String FAILURE = "businessunitedit.xhtml";
 
    private Map<String, Integer> servicetypeList;  
    private List<Integer> selectedServiceType;
    private BusinessUnit businessUnit;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value = "#{serviceTypeDAO}")
    private ServiceTypeDAO serviceTypeDAO;
    @ManagedProperty(value = "#{businessUnitDAO}")
    private BusinessUnitDAO businessUnitDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:businessunit:view")) 
        {
            SecurityUtil.redirectUnauthorize();  
        }
        
        messageDup ="";
        servicetypeList = this.getServiceTypeDAO().getServiceTypeList();
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           businessUnit = new BusinessUnit();
           businessUnit.setEnable(Boolean.TRUE);
           businessUnit.setStatus(Boolean.TRUE);
        } else {
           mode = "edit";
           BusinessUnitDAO dao = getBusinessUnitDAO();
           businessUnit = dao.findBusinessUnit(new Integer(selectedID));
           if (businessUnit==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           } else {
               List<Integer> val = new ArrayList<Integer>();
               Collection<ServiceType> services =  businessUnit.getServiceTypeCollection();
               for(ServiceType s: services) {
                   val.add(s.getId());
               }
               selectedServiceType = val;
           }
       }
    }

    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:businessunit:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:businessunit:edit"); 
       }
    } 
        
    public String saveAction() {
        messageDup = "";
        if(checkName(businessUnit)) {
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            BusinessUnitDAO dao = getBusinessUnitDAO();
           
            try {
                if (getMode().equals("add")) {
                    businessUnit.setId(null);
                    businessUnit.setCreateBy(username);
                    businessUnit.setCreateDate(now);
                    businessUnit.setServiceTypeCollection(this.getSelectedServiceTypeCollection());
                    dao.create(businessUnit);
                } else {
                    businessUnit.setServiceTypeCollection(this.getSelectedServiceTypeCollection());
                    businessUnit.setUpdateBy(username);
                    businessUnit.setUpdateDate(now);
                    dao.edit(businessUnit);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Name is already taken";
            return FAILURE;      
        }
    }

    public List<ServiceType> getSelectedServiceTypeCollection() {
        List<ServiceType> serviceTypes = new ArrayList<ServiceType>();
        for (int sid : getSelectedServiceType()) {
            ServiceType s = new ServiceType();
            s.setId(sid);
            serviceTypes.add(s);
        }
        return serviceTypes;
    }

    public Boolean checkName(BusinessUnit businessUnit) {
        String name = businessUnit.getName();
        Integer id=0; 
        if(businessUnit.getId() != null)
            id = businessUnit.getId();
        BusinessUnitDAO dao = getBusinessUnitDAO();
        
        Integer cnt = dao.checkBusinessUnitName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }

    public String backAction() {
        return SUCCESS;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BusinessUnitDAO getBusinessUnitDAO() {
        return businessUnitDAO;
    }

    public void setBusinessUnitDAO(BusinessUnitDAO businessUnitDAO) {
        this.businessUnitDAO = businessUnitDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public ServiceTypeDAO getServiceTypeDAO() {
        return serviceTypeDAO;
    }

    public void setServiceTypeDAO(ServiceTypeDAO serviceTypeDAO) {
        this.serviceTypeDAO = serviceTypeDAO;
    }
    
    public Map<String, Integer> getServiceTypeList() {
        return servicetypeList;
    }
        
    public List<Integer> getSelectedServiceType() {
        return selectedServiceType;
    }

    public void setSelectedServiceType(List<Integer> selectedServiceType) {
        this.selectedServiceType = selectedServiceType;
    }
  
}
