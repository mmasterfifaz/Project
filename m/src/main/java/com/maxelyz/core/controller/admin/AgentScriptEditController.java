package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.AgentScriptDAO;
import com.maxelyz.core.model.entity.AgentScript;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.faces.context.FacesContext;
import javax.faces.bean.*;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.bean.ViewScoped;

@ManagedBean
//@RequestScoped
@ViewScoped
public class AgentScriptEditController {

    private static Log log = LogFactory.getLog(AgentScriptEditController.class);
    private static String REDIRECT_PAGE = "agentscript.jsf";
    private static String SUCCESS = "agentscript.xhtml?faces-redirect=true";
    private static String FAILURE = "agentscriptedit.xhtml";
    private AgentScript agentScript;
    private String mode;
    @ManagedProperty(value = "#{agentScriptDAO}")
    private AgentScriptDAO agentScriptDAO;

    @PostConstruct
    public void initialize() {
         if (!SecurityUtil.isPermitted("admin:agentscript:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
         
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            //mode = "add";
            //agentScript = new AgentScript();
            JSFUtil.redirect(REDIRECT_PAGE);
        } else {
            mode = "edit";
            agentScript = agentScriptDAO.findAgentScript(new Integer(selectedID));
            if (agentScript==null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }
    }

   public boolean isSavePermitted() {

 	   return SecurityUtil.isPermitted("admin:agentscript:edit"); 
       
    }  

    public String saveAction() {
        try {
            if (getMode().equals("edit")) {
                getAgentScriptDAO().edit(agentScript);
            }
        } catch (Exception e) {
            log.error(e);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String backAction() {
        return SUCCESS;
    }

    public AgentScript getAgentScript() {
        return agentScript;
    }

    public void setAgentScript(AgentScript agentScript) {
        this.agentScript = agentScript;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public AgentScriptDAO getAgentScriptDAO() {
        return agentScriptDAO;
    }

    public void setAgentScriptDAO(AgentScriptDAO agentScriptDAO) {
        this.agentScriptDAO = agentScriptDAO;
    }
}
