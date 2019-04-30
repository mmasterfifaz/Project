package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.AutoAssignmentConfigDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.AutoAssignmentConfig;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@ViewScoped
public class AutoAssignmentConfigEditController {
    private static Logger log = Logger.getLogger(AutoAssignmentConfigEditController.class);
    private static String SUCCESS = "autoassignmentconfig.xhtml?faces-redirect=true";
    private static String FAILURE = "autoassignmentconfigedit.xhtml";

    private String mode;
    private String message;
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
            e.getMessage();
        }
        if(autoAssignmentConfig == null) {  
            autoAssignmentConfig = new AutoAssignmentConfig();
            autoAssignmentConfig.setUserQuota(1);
        }
    }

    public String saveAction() {
        try {
            autoAssignmentConfig.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
            autoAssignmentConfig.setUpdateDate(new Date());
            if(!autoAssignmentConfig.getCheckDuplicate()) {
                autoAssignmentConfig.setDuplicatePeriod(0);
            }
            if(autoAssignmentConfig.getId() == null) {
                autoAssignmentConfig.setId(null);
                this.getAutoAssignmentConfigDAO().create(autoAssignmentConfig);
            } else {
                this.getAutoAssignmentConfigDAO().edit(autoAssignmentConfig);
            }
        } catch (Exception e) {
            log.error(e);
            message = e.getMessage();
            return FAILURE;
        }
        return SUCCESS;
    }
    
    public boolean isSavePermitted() {
       return SecurityUtil.isPermitted("admin:autoassignmentconfig:edit"); 
    } 

    public String backAction() {
        return SUCCESS;
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

    public AutoAssignmentConfig getAutoAssignmentConfig() {
        return autoAssignmentConfig;
    }

    public void setAutoAssignmentConfig(AutoAssignmentConfig autoAssignmentConfig) {
        this.autoAssignmentConfig = autoAssignmentConfig;
    }

    public AutoAssignmentConfigDAO getAutoAssignmentConfigDAO() {
        return autoAssignmentConfigDAO;
    }

    public void setAutoAssignmentConfigDAO(AutoAssignmentConfigDAO autoAssignmentConfigDAO) {
        this.autoAssignmentConfigDAO = autoAssignmentConfigDAO;
    }

}
