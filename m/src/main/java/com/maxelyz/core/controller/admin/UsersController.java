package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.UserGroupDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.Users;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class UsersController implements Serializable {

    private static Logger log = Logger.getLogger(UsersController.class);
    private static String REFRESH = "users.xhtml?faces-redirect=true";
    private static String EDIT = "usersedit.xhtml";
    private String message = "";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<Users> users;
    private String keyword;
    private Users user;
    private Map<String, Integer> userGroupList;
    private Integer userGroupId;
    private String status;
    private String license;
    private String login;
    private String telephony;
    private String ldap;
    private Date dateFrom;
    private Date dateTo;
    
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:user:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        status = "enable";
        keyword = "";
        license = "";
        login = "";
        telephony = "";
        ldap = "";
        Integer parentId = JSFUtil.getUserSession().getUsers().getUserGroup().getId();
        users = this.getUsersDAO().findUsersUnderParentBySearch(userGroupId, keyword, status, license, dateFrom, dateTo, login, telephony, ldap, parentId);
        if(JSFUtil.getUserSession().getUsers().getIsAdministrator()) {
            userGroupList = this.getUserGroupDAO().getUserGroupListByParentId(0);
        } else {
            userGroupList = this.getUserGroupDAO().getUserGroupListByParentId(parentId);
        }
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:user:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:user:delete");
    }

    public void searchActionListener(ActionEvent event) {
        Integer parentId = JSFUtil.getUserSession().getUsers().getUserGroup().getId();
        users = usersDAO.findUsersUnderParentBySearch(userGroupId, keyword, status, license, dateFrom, dateTo, login, telephony, ldap, parentId);
    }
/*
    public List<Users> getList() {
        return getUsers();
    }

    public String searchAction() {
        UsersDAO dao = getUsersDAO();
        users = dao.findUsersByName(keyword);
        return REFRESH;
    }

    public void popupSearchAction() {
        UsersDAO dao = getUsersDAO();
        users = dao.findUsersByName(keyword);
//        return REFRESH;
    }
*/
    public String editAction() {
        return EDIT;
    }

    public String deleteAction() throws Exception {
        UsersDAO dao = getUsersDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    user = dao.findUsers(item.getKey());
                    user.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    user.setUpdateDate(new Date());
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
    
    //search
    
    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getLdap() {
        return ldap;
    }

    public void setLdap(String ldap) {
        this.ldap = ldap;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getTelephony() {
        return telephony;
    }

    public void setTelephony(String telephony) {
        this.telephony = telephony;
    }

}
