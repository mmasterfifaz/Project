package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.AgentScriptDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.maxelyz.core.model.entity.AgentScript;
import javax.annotation.PostConstruct;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class AgentScriptController implements Serializable{
    private static Log log = LogFactory.getLog(AgentScriptController.class);

    private static String REFRESH = "agentscript.xhtml?faces-redirect=true";
    private static String EDIT = "agentscriptedit.xhtml";  
    private List<AgentScript> agentScripts;
    private AgentScript agentScript;
    @ManagedProperty(value="#{agentScriptDAO}")
    private AgentScriptDAO agentScriptDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:agentscript:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        agentScripts = getAgentScriptDAO().findAgentScriptEntities();    
    }

    public List<AgentScript> getList() {
        return getAgentScripts();
    }

    public String editAction() {
       return EDIT;
    }

    public List<AgentScript> getAgentScripts() {
        return agentScripts;
    }

    public void setAgentScripts(List<AgentScript> AgentScripts) {
        this.agentScripts = AgentScripts;
    }

    public AgentScriptDAO getAgentScriptDAO() {
        return agentScriptDAO;
    }

    public void setAgentScriptDAO(AgentScriptDAO agentScriptDAO) {
        this.agentScriptDAO = agentScriptDAO;
    }


 
}
