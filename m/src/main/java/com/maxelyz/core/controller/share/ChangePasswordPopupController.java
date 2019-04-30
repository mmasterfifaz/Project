package com.maxelyz.core.controller.share;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;

@ManagedBean
@RequestScoped
public class ChangePasswordPopupController implements Serializable {
    private static Logger log = Logger.getLogger(ChangePasswordPopupController.class);
    private static String LOGIN_PAGE = "/login/login.xhtml";
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
    private String message;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;

    @PostConstruct
    public void initialize() {
    }

    public String saveAction() {
        currentPassword = currentPassword.trim();
        newPassword = newPassword.trim();
        confirmNewPassword = confirmNewPassword.trim();
        UserSession userSession = (UserSession) JSFUtil.getUserSession();
        Users user = userSession.getUsers();
        if (!user.getUserPassword().trim().equals(currentPassword)) {
            message = "Wrong Current Password";
            return null;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            message = "New Password does not match Confirm New Password";
            return null;
        }
        if (newPassword.equals(currentPassword)) {
            message = "New Password must not same as Current Password";
            return null;
        }
        if (!UsersDAO.isValidPassword(newPassword)) {
            message = "New Password must contain at least 8 characters";
            return null;
        }
        
        user.setUserPassword(newPassword);
        user.setLastChangedPassword(new Date());
        user.setForceChangePassword(0);
        getUsersDAO().edit(user);
        return "/login/login.xhtml?faces-redirect=true";
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }
}
