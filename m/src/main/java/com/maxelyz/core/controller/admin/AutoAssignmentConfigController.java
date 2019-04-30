package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.AutoAssignmentConfigDAO;
import com.maxelyz.core.model.entity.AutoAssignmentConfig;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class AutoAssignmentConfigController implements Serializable{
    private static Logger log = Logger.getLogger(AutoAssignmentConfigController.class);
    private static String EDIT = "autoassignmentconfigedit.xhtml";
     
    private AutoAssignmentConfig autoAssignmentConfig;
    
    @ManagedProperty(value="#{autoAssignmentConfigDAO}")
    private AutoAssignmentConfigDAO autoAssignmentConfigDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:autoassignmentconfig:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        try{
            autoAssignmentConfig = this.getAutoAssignmentConfigDAO().findAutoAssignmentConfigEntities();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(autoAssignmentConfig == null) {
            autoAssignmentConfig = new AutoAssignmentConfig();
            autoAssignmentConfig.setUserQuota(0);
            autoAssignmentConfig.setDuplicatePeriod(0);
        }
    }

     public AutoAssignmentConfig getAutoAssignmentConfig() {
       return autoAssignmentConfig;
    }

    public void setAutoAssignmentConfig(AutoAssignmentConfig autoAssignmentConfig) {
        this.autoAssignmentConfig = autoAssignmentConfig;
    }

     public String editAction() {
       return EDIT;
    }
    
    public boolean isEditPermitted() {
        return SecurityUtil.isPermitted("admin:autoassignmentconfig:edit");
    }

    public AutoAssignmentConfigDAO getAutoAssignmentConfigDAO() {
        return autoAssignmentConfigDAO;
    }

    public void setAutoAssignmentConfigDAO(AutoAssignmentConfigDAO autoAssignmentConfigDAO) {
        this.autoAssignmentConfigDAO = autoAssignmentConfigDAO;
    }

}
