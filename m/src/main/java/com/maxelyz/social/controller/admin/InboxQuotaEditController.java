
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class InboxQuotaEditController implements Serializable {

    private static Logger log = Logger.getLogger(InboxQuotaEditController.class);    
    private static String REDIRECT_PAGE = "inboxquota.jsf";
    private static String SUCCESS = "inboxquota.xhtml?faces-redirect=true";
    private static String FAILURE = "inboxquotaedit.xhtml";
    private String message = "";
    private List<Users> users;
    private String quotaType = "custom";
    private int defaultQuotaPerUser = 1;
    private int defaultQuotaPerDay = 1;
    
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:inboxQuota:edit")) {
            SecurityUtil.redirectUnauthorize();  
        }
        message = "";
        quotaType = "custom";
        defaultQuotaPerUser = 1;
        defaultQuotaPerDay = 1;
        users = usersDAO.findSoAgentList();
    }
    
     public void quotaTypePerUserListener() {  //ActionEvent event
        message = "";
        if(defaultQuotaPerUser != 0) {
            for (Users u : users) {
                u.setSoOnhandQuota(defaultQuotaPerUser);
            }
        } 
    }
     
     public void quotaTypePerDayListener() {  //ActionEvent event
        message = "";
        if(defaultQuotaPerDay != 0) {
            for (Users u : users) {
                u.setSoInboxQuota(defaultQuotaPerDay);
            }
        } 
    }
          
     public void changeTypeListener() { 
        message = "";
    }
     
    public synchronized String saveAction() {
        if(users != null) {
            try {
                UsersDAO dao = getUsersDAO();
                dao.updateInboxQuota(users);
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
        }
        return SUCCESS;
    }
     
    public String backAction() {
        return SUCCESS;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Users> getUsersList() {
        return users;
    }

    public void setUsersList(List<Users> users) {
        this.users = users;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public int getDefaultQuotaPerUser() {
        return defaultQuotaPerUser;
    }

    public void setDefaultQuotaPerUser(int defaultQuotaPerUser) {
        this.defaultQuotaPerUser = defaultQuotaPerUser;
    }

    public int getDefaultQuotaPerDay() {
        return defaultQuotaPerDay;
    }

    public void setDefaultQuotaPerDay(int defaultQuotaPerDay) {
        this.defaultQuotaPerDay = defaultQuotaPerDay;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

}