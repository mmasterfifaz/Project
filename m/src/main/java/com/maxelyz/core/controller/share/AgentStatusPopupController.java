package com.maxelyz.core.controller.share;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.model.value.admin.SessionMonitorValue;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;
import com.maxelyz.core.service.UserService;
import com.maxelyz.social.model.dao.SoUserNotReadyLogDAO;
import com.maxelyz.social.model.entity.SoUserNotReadyLog;
import com.maxelyz.social.model.entity.SoUserNotReadyReason;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class AgentStatusPopupController implements Serializable {
    private static Logger log = Logger.getLogger(AgentStatusPopupController.class);
    private String agentStatus;
    private String message;
    private String messageReason;
    private String notreadyReason;
//    private Map<String, Integer> notreadyList;
    //@ManagedProperty(value = "#{usersDAO}")
    //private UsersDAO usersDAO;
    @ManagedProperty(value = "#{userService}")
    private UserService userService;
    @ManagedProperty(value = "#{soUserNotReadyLogDAO}")
    private SoUserNotReadyLogDAO soUserNotReadyLogDAO;

    @PostConstruct
    public void initialize() {
        message = "";
        messageReason = "";
        notreadyReason = "";
        String loginName = "";
        SessionMonitorValue value = null;
        
        if(JSFUtil.getUserSession() != null && JSFUtil.getUserSession().getUsers() != null) {
            loginName = JSFUtil.getUserSession().getUsers().getLoginName();
            value = this.getSessionMonitorValueByLoginName(loginName);
        }
        
        if (value != null) {
            agentStatus = value.getAgentStatus();//ready, notready      
        } else {
            agentStatus = "Invalid";
        }
    }
    
    public boolean isShowPermitted() {
        return SecurityUtil.isPermitted("agent:status");
    }
    
    private SessionMonitorValue getSessionMonitorValueByLoginName(String loginName) {
        Map<String, SessionMonitorValue> concurrentUserMap;
        SessionMonitorValue value=null;
        
        for (Integer i=1;i<=5;i++) {
            concurrentUserMap = JSFUtil.getConcurrentUser(i.toString());
            if(concurrentUserMap != null){
                if (concurrentUserMap.containsKey(loginName)) {
                    value = concurrentUserMap.get(loginName);
                    break;
                }
            }
        }
        return value;
    }

    public String saveAction() {
        if(agentStatus.equals("Not Ready")) {
            if(notreadyReason != null && !notreadyReason.equals("")) {
                SoUserNotReadyLog nrl = new SoUserNotReadyLog();
                nrl.setId(null);
                nrl.setUsers(JSFUtil.getUserSession().getUsers());
                nrl.setNotReadyTime(new Date());
                nrl.setReason(notreadyReason);
                try {  
                    this.soUserNotReadyLogDAO.create(nrl);
                } catch (Exception e) {
                    log.error(e);
                }
                
                JSFUtil.getUserSession().setAgentStatus(agentStatus);
                String loginName = JSFUtil.getUserSession().getUsers().getLoginName();
                SessionMonitorValue value=this.getSessionMonitorValueByLoginName(loginName);
                value.setAgentStatus(agentStatus);
            } else {
                messageReason = "Not Ready Reason is required.";
            }
        } else {
            JSFUtil.getUserSession().setAgentStatus(agentStatus);
            String loginName = JSFUtil.getUserSession().getUsers().getLoginName();
            SessionMonitorValue value=this.getSessionMonitorValueByLoginName(loginName);
            value.setAgentStatus(agentStatus);
        }
        return null;
    }

   
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
/*
    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }
    * */

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    public String getNotreadyReason() {
        return notreadyReason;
    }

    public void setNotreadyReason(String notreadyReason) {
        this.notreadyReason = notreadyReason;
    }

    public String getMessageReason() {
        return messageReason;
    }

    public void setMessageReason(String messageReason) {
        this.messageReason = messageReason;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    public List<SoUserNotReadyReason> getNotReadyList() {
        return userService.findNotReadyList();
    }

    public SoUserNotReadyLogDAO getSoUserNotReadyLogDAO() {
        return soUserNotReadyLogDAO;
    }

    public void setSoUserNotReadyLogDAO(SoUserNotReadyLogDAO soUserNotReadyLogDAO) {
        this.soUserNotReadyLogDAO = soUserNotReadyLogDAO;
    }
    
}
