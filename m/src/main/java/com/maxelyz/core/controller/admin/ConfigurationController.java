package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ConfigurationDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.maxelyz.core.model.entity.Configuration;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;


@ManagedBean
@RequestScoped
public class ConfigurationController implements Serializable{
    private static Log log = LogFactory.getLog(ConfigurationController.class);

    private static String REFRESH = "configuration.xhtml?faces-redirect=true";
    private static String EDIT = "configurationedit.xhtml";
    private List<Configuration> configurations;
    private Configuration configuration;
    @ManagedProperty(value="#{configurationDAO}")
    private ConfigurationDAO configurationDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:systemconfiguration:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        configurations = getConfigurationDAO().findConfigurationEntities();
    }

    public List<Configuration> getList() {
        return getConfigurations();
    }

    public String editAction() {
       return EDIT;
    }

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> Configurations) {
        this.configurations = Configurations;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public ConfigurationDAO getConfigurationDAO() {
        return configurationDAO;
    }

    public void setConfigurationDAO(ConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
    }


 
}
