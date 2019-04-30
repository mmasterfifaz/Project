/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.BusinessUnitDAO;
import com.maxelyz.core.model.entity.BusinessUnit;
import com.maxelyz.core.model.dao.LocationDAO;
import com.maxelyz.core.model.entity.Location;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import java.util.Map;
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
@ManagedBean
@RequestScoped
//@ViewScoped
public class LocationEditController {
    
    private static Logger log = Logger.getLogger(LocationEditController.class);
    private static String REDIRECT_PAGE = "location.jsf";
    private static String SUCCESS = "location.xhtml?faces-redirect=true";
    private static String FAILURE = "locationedit.xhtml";
    private Map<String, Integer> businessUnitList;  
    private Integer businessUnitId;
    private Location location;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value = "#{businessUnitDAO}")
    private BusinessUnitDAO businessUnitDAO;
    @ManagedProperty(value = "#{locationDAO}")
    private LocationDAO locationDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:location:view")) 
        {
            SecurityUtil.redirectUnauthorize();  
        }
        
        messageDup="";
        businessUnitList = this.getBusinessUnitDAO().getBusinessUnitList();
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
       
        if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           location = new Location();
           location.setEnable(Boolean.TRUE);
           location.setStatus(Boolean.TRUE);
        } else {
           mode = "edit";
           LocationDAO dao = getLocationDAO();
           location = dao.findLocation(new Integer(selectedID));
           businessUnitId = location.getBusinessUnit().getId();
           if (location==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           }
       }
    }


      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:location:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:location:edit"); 
       }
    } 
        
    public String saveAction() {
        messageDup = "";
        if(checkName(location)) {
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            LocationDAO dao = getLocationDAO();
            try {
                if (getMode().equals("add")) {
                    location.setId(null);
                    location.setBusinessUnit(new BusinessUnit(businessUnitId));
                    location.setCreateBy(username);
                    location.setCreateDate(now);
                    dao.create(location);
                } else {
                    location.setBusinessUnit(new BusinessUnit(businessUnitId));
                    location.setUpdateBy(username);
                    location.setUpdateDate(now);
                    dao.edit(location);
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

    public Boolean checkName(Location location) {
        String name = location.getName();
        Integer id=0; 
        if(location.getId() != null)
            id = location.getId();
        LocationDAO dao = getLocationDAO();
        
        Integer cnt = dao.checkLocationName(name, businessUnitId, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }

    public String backAction() {
        return SUCCESS;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public Map<String, Integer> getBusinessUnitList() {
        return businessUnitList;
    }
    
    public Integer getBusinessUnitId() {
        return businessUnitId;
    }
    
    public void setBusinessUnitId(Integer businessUnitId) {
        this.businessUnitId = businessUnitId;
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

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
    
}
