package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ConfigurationDAO;
import com.maxelyz.core.model.entity.Configuration;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;

@ManagedBean
@ViewScoped
public class ConfigurationEditController {

    private static Log log = LogFactory.getLog(ConfigurationEditController.class);
    private static String REDIRECT_PAGE = "configuration.jsf";
    private static String SUCCESS = "configuration.xhtml?faces-redirect=true";
    private static String FAILURE = "configurationedit.xhtml";
    private Configuration configuration;
    private String message;
    @ManagedProperty(value = "#{configurationDAO}")
    private ConfigurationDAO configurationDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:systemconfiguration:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        String selectedID = (String) JSFUtil.getRequestParameterMap("property");

        if (selectedID == null || selectedID.length()==0) {
            JSFUtil.redirect(REDIRECT_PAGE);
        } else {
            configuration = configurationDAO.findConfiguration(selectedID);
            if (configuration==null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }
    }
    
      public boolean isSavePermitted() {
 	   return SecurityUtil.isPermitted("admin:systemconfiguration:edit"); 
    }
      
    public String saveAction() {
        try {
            getConfigurationDAO().edit(configuration);
            JSFUtil.getApplication().initialize(); //reload global configuration to application scope
        } catch (Exception e) {
            log.error(e);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String backAction() {
        return SUCCESS;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ConfigurationDAO getConfigurationDAO() {
        return configurationDAO;
    }

    public void setConfigurationDAO(ConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
    }
}
