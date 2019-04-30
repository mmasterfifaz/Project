/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import com.maxelyz.core.model.dao.UserGroupDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.Users;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class InboxQuotaController implements Serializable {

    private static Logger log = Logger.getLogger(InboxQuotaController.class);
    private static String REFRESH = "inboxquota.xhtml?faces-redirect=true";
    private static String EDIT = "inboxquotaedit.xhtml";
    private String message = "";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<Users> users;
    private String keyword;
    private Users user;
    private Map<String, Integer> userGroupList;
    private Integer userGroupId;
    private String status;
    
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:inboxQuota:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        keyword = "";
        users = usersDAO.findSoAgentBySearch(userGroupId, keyword);
        userGroupList = this.getUserGroupDAO().getSoAgentGroupList();
    }

    public boolean isEditPermitted() {
        return SecurityUtil.isPermitted("admin:inboxQuota:edit");
    }

    public void searchActionListener(ActionEvent event) {
        users = usersDAO.findSoAgentBySearch(userGroupId, keyword);
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() throws Exception {
        UsersDAO dao = getUsersDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    user = dao.findUsers(item.getKey());
                    user.setEnable(false);
                    dao.edit(user);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<Users> getUsersList() {
        return users;
    }

    public void setUsersList(List<Users> users) {
        this.users = users;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Map<String, Integer> getUserGroupList() {
        return userGroupList;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }
    
}
