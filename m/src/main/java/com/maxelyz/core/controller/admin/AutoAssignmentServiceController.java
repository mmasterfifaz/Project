package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.AutoAssignmentDAO;
import com.maxelyz.core.model.entity.AutoAssignment;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class AutoAssignmentServiceController implements Serializable{
    private static Logger log = Logger.getLogger(AutoAssignmentServiceController.class);
    private static String REFRESH = "autoassignmentservice.xhtml?faces-redirect=true";
    private static String EDIT = "autoassignmentserviceedit.xhtml";
     
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<AutoAssignment> autoAssignmentServices;
    private AutoAssignment autoAssignmentService;
    
    @ManagedProperty(value="#{autoAssignmentDAO}")
    private AutoAssignmentDAO autoAssignmentDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:autoassignmentservice:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        AutoAssignmentDAO dao = getAutoAssignmentDAO();
        autoAssignmentServices = dao.findAutoAssignmentServiceEnable();
        
    }

    public String editAction() {
       return EDIT;
    }
    
    public String deleteAction() throws Exception {
        AutoAssignmentDAO dao = getAutoAssignmentDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    autoAssignmentService = dao.findAutoAssignment(item.getKey());
                    autoAssignmentService.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    autoAssignmentService.setUpdateDate(new Date());
                    autoAssignmentService.setEnable(false);
                    dao.edit(autoAssignmentService);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:autoassignmentservice:add");
    }
        
    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:autoassignmentservice:delete");  
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public AutoAssignmentDAO getAutoAssignmentDAO() {
        return autoAssignmentDAO;
    }

    public void setAutoAssignmentDAO(AutoAssignmentDAO autoAssignmentDAO) {
        this.autoAssignmentDAO = autoAssignmentDAO;
    }

    public List<AutoAssignment> getAutoAssignmentServices() {
        return autoAssignmentServices;
    }

    public void setAutoAssignmentServices(List<AutoAssignment> autoAssignmentServices) {
        this.autoAssignmentServices = autoAssignmentServices;
    }

    public AutoAssignment getAutoAssignmentService() {
        return autoAssignmentService;
    }

    public void setAutoAssignmentService(AutoAssignment autoAssignmentService) {
        this.autoAssignmentService = autoAssignmentService;
    }

}
