package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.ActivityTypeDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.ActivityType;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import org.omg.CORBA.ACTIVITY_COMPLETED;

@ManagedBean
@RequestScoped
public class ActivityTypeController implements Serializable{
    private static Logger log = Logger.getLogger(ActivityTypeController.class);
    private static String REFRESH = "activitytype.xhtml?faces-redirect=true";
    private static String EDIT = "activitytypeedit.xhtml";
     
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<ActivityType> activityTypes;
    private ActivityType activityType;
    
    @ManagedProperty(value="#{activityTypeDAO}")
    private ActivityTypeDAO activityTypeDAO;
    @ManagedProperty(value="#{securityService}")
    private SecurityService securityService;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:activitytype:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        ActivityTypeDAO dao = getActivityTypeDAO();
        activityTypes = dao.findActivityTypeNonsystem();
        
    }

    public List<ActivityType> getList() {
        return getActivityTypes();
    }

     public String editAction() {
       return EDIT;
    }

    public String deleteAction() throws Exception {
        ActivityTypeDAO dao = getActivityTypeDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    activityType = dao.findActivityType(item.getKey());
                    activityType.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    activityType.setUpdateDate(new Date());
                    activityType.setEnable(false);
                    dao.edit(activityType);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:activitytype:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:activitytype:delete");
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<ActivityType> getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(List<ActivityType> activityTypes) {
        this.activityTypes = activityTypes;
    }

    public ActivityTypeDAO getActivityTypeDAO() {
        return activityTypeDAO;
    }

    public void setActivityTypeDAO(ActivityTypeDAO activityTypeDAO) {
        this.activityTypeDAO = activityTypeDAO;
    }

    public SecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
   
}
