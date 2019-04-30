package com.maxelyz.core.controller.share;

import com.maxelyz.core.model.entity.UsersScript;
import com.maxelyz.core.model.dao.UsersScriptDAO;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
//import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;

@ManagedBean
//@RequestScoped
//@KeepAlive
@ViewScoped
public class MyScriptPopupController implements Serializable {

    private static Logger log = Logger.getLogger(MyScriptPopupController.class);
    private static String REFRESH = "myscriptpopup.xhtml";
    private Map<Long, Boolean> selectedIds = new HashMap<Long, Boolean>(); //use for checkbox
    private List<UsersScript> usersScriptList;
    private UsersScript usersScript;
    private String mode = "view";

    private String keyword = "";
    private String selectedID;
    private String message;
    @ManagedProperty(value = "#{usersScriptDAO}")
    private UsersScriptDAO usersScriptDAO;

    @PostConstruct
    public void initialize() {
        
        int userId = JSFUtil.getUserSession().getUsers().getId();
        if(JSFUtil.getRequestParameterMap("mode") != null) {
            mode = JSFUtil.getRequestParameterMap("mode");
        } else {
            mode = "view";
        }
        
        if ("view".equals(mode)) {
            usersScriptList = this.getUsersScriptDAO().findUsersScriptByUserId(userId);
            if (usersScriptList.size()>0) {
                usersScript = usersScriptList.get(0);
            }
        } else if ("add".equals(mode)) {
            usersScript = new UsersScript();
        } else if ("edit".equals(mode)) {
            String id = JSFUtil.getRequestParameterMap("id");
            usersScript = this.getUsersScriptDAO().findUsersScript(new Long(id));
        } 
    }

    public List<UsersScript> getList() {
        return usersScriptList;
    }

    public void searchListener() { 
        int userId = JSFUtil.getUserSession().getUsers().getId();
        usersScriptList = this.getUsersScriptDAO().findUsersScriptByKeyword(userId, keyword);
        if (!usersScriptList.isEmpty()) {
            //       selectedID = "0";
        }
    }

    public void showListener(ActionEvent event) {
        mode = "view";
        String id = JSFUtil.getRequestParameterMap("id");
        usersScript = this.getUsersScriptDAO().findUsersScript(new Long(id));
    }

    public String addAction() {
        return "myscripteditpopup.xhtml";
    }

    public String editAction() {
        return "myscripteditpopup.xhtml";
    }
    
    public String backAction() {
        return "myscriptpopup.xhtml?faces-redirect=true";
    }

    public String saveAction() {
        try {
            if ("add".equals(mode)) {
                usersScript.setId(null);
                usersScript.setUsers(JSFUtil.getUserSession().getUsers());
                this.usersScriptDAO.create(usersScript);
            } else {
                this.usersScriptDAO.edit(usersScript);
            }
            initialize();
        } catch (Exception e) {
            log.error(e);
            message = e.getMessage();
            return null;
        }
        mode="view";
        return "myscriptpopup.xhtml?faces-redirect=true";
    }

    public void deleteListener(ActionEvent event) {
        try {
            for (Map.Entry<Long, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    this.getUsersScriptDAO().destroy(item.getKey());
                }
            }

        } catch (Exception e) {
            log.error(e);
        }
        initialize();
    }

    public String showAction() {
        return REFRESH;
    }

    public void setUsersScript(UsersScript usersScripts) {
        this.usersScript = usersScripts;
    }

    public UsersScript getUsersScript() {
        return usersScript;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSelectedID() {
        return selectedID;
    }

    public void setSelectedID(String selectedID) {
        this.selectedID = selectedID;
    }

    public Map<Long, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Long, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //Managed Properties
    public UsersScriptDAO getUsersScriptDAO() {
        return usersScriptDAO;
    }

    public void setUsersScriptDAO(UsersScriptDAO usersScriptDAO) {
        this.usersScriptDAO = usersScriptDAO;
    }
}
