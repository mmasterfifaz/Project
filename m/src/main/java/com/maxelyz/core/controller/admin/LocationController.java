/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;


import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.BusinessUnitDAO;
import com.maxelyz.core.model.dao.LocationDAO;
import com.maxelyz.core.model.entity.BusinessUnit;
import com.maxelyz.core.model.entity.Location;
import com.maxelyz.core.service.SecurityService;
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
public class LocationController {
    private static Logger log = Logger.getLogger(LocationController.class);
    private static String REFRESH = "location.xhtml?faces-redirect=true";
    private static String EDIT = "locationedit.xhtml";
  
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<Location> locationList;
    private Map<String, Integer> businessUnitList;
    private Integer businessUnitId;
    private Location location;
     
    @ManagedProperty(value="#{businessUnitDAO}")
    private BusinessUnitDAO businessUnitDAO;
    @ManagedProperty(value="#{locationDAO}")
    private LocationDAO locationDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:location:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        LocationDAO dao = getLocationDAO();
        locationList = dao.findLocationEntities();
        businessUnitList = this.getBusinessUnitDAO().getBusinessUnitList();
        
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:location:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:location:delete");
    }
    
    public List<Location> getList() {
        return getLocationList();
    }

     public String editAction() {
       return EDIT;
    }

    public String deleteAction() throws Exception {
        LocationDAO dao = getLocationDAO();
        String username = JSFUtil.getUserSession().getUserName();
        Date now = new Date();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    location = dao.findLocation(item.getKey());
                    location.setEnable(false);
                    location.setUpdateBy(username);
                    location.setUpdateDate(now);
                    dao.edit(location);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    public Integer getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Integer businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public Map<String, Integer> getBusinessUnitList() {
        return businessUnitList;
    }

    public void businessListener(ValueChangeEvent event) {
        businessUnitId = (Integer) event.getNewValue();
        if (businessUnitId==null || businessUnitId==0) {
            locationList = this.getLocationDAO().findLocationEntities();
        } else {
            locationList = this.getLocationDAO().findLocationByBusinessUnit(businessUnitId);
        }
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
