package com.maxelyz.core.controller.share;

import com.maxelyz.core.model.dao.AgentScriptDAO;
import com.maxelyz.core.model.entity.AgentScript;
import com.maxelyz.utils.JSFUtil;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;
import javax.faces.component.html.HtmlOutputText;

@ManagedBean
@RequestScoped
public class AgentScriptFrontController {

    private static Logger log = Logger.getLogger(AgentScriptFrontController.class);
    private HtmlOutputText script;
    @ManagedProperty(value = "#{agentScriptDAO}")
    private AgentScriptDAO agentScriptDAO;

    public HtmlOutputText getScript() {
        return script;
    }

    public void setScript(HtmlOutputText script) {
        this.script = script;
    }

    public String getScriptValue() {
        String msg = "";
        try {  
            Integer pageId = new Integer((String) script.getAttributes().get("pageId"));
            ////log.error("Page id = "+ pageId);
            msg = this.agentScriptDAO.findAgentScript(pageId) == null ? "" : this.agentScriptDAO.findAgentScript(pageId).getScript() ;
        } catch (Exception e) {
            log.error(e);
            return msg;
        }
        return msg;
    }

    public AgentScriptDAO getAgentScriptDAO() {
        return agentScriptDAO;
    }

    public void setAgentScriptDAO(AgentScriptDAO agentScriptDAO) {
        this.agentScriptDAO = agentScriptDAO;
    }
}
