package com.maxelyz.core.controller.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.maxelyz.core.model.dao.MessageBroadcastDAO;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.MessageBroadcast;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

@ManagedBean (name="messageBroadcastEditController")
//@RequestScoped
@ViewScoped
public class MessageBroadcastEditController implements Serializable {
    private static Log log = LogFactory.getLog(MessageBroadcastEditController.class);
    private static String REDIRECT_PAGE = "messagebroadcast.jsf";
    private static String SUCCESS = "messagebroadcast.xhtml?faces-redirect=true";
    private static String FAILURE = "messagebroadcastedit.xhtml";
    
    private MessageBroadcast messageBroadcast;
    private String mode;
    private Map<String, Integer> userList = new LinkedHashMap<String, Integer>();
    private List<Integer> selectedUser;
    private int userGroupId;
    private String selectingUser;
    
    @ManagedProperty(value = "#{messageBroadcastDAO}")
    private MessageBroadcastDAO messageBroadcastDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:messagebroadcast:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            messageBroadcast = new MessageBroadcast();
            messageBroadcast.setPriority(3);
            messageBroadcast.setEnable(true);
            messageBroadcast.setStartDate(JSFUtil.toDateWithoutTime(new Date()));
            messageBroadcast.setEndDate(JSFUtil.toDateWithoutTime(new Date()));
        } else {
            mode = "edit";
            messageBroadcast = getMessageBroadcastDAO().findMessageBroadcast(new Integer(selectedID));
            if (messageBroadcast == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            } else {
                if(messageBroadcast.getAllUser())
                    selectingUser = "all";
                else if(messageBroadcast.getUserGroupId() != null) {
                        selectingUser = "group";
                        userGroupId = messageBroadcast.getUserGroupId();
                } else if(messageBroadcast.getUsersCollection() != null) {
                        selectingUser = "tmr";
                        
                        List<Users> users = null;
                        if(SecurityUtil.isPermitted("admin:messagebroadcast:manager")) {
                            users = this.getUsersDAO().findUsersEntities();
                        } else {
                            users = this.getUsersDAO().findUserUnderParentId(JSFUtil.getUserSession().getUsers());
                        }
//                        List<Users> users = this.getUsersDAO().findUserUnderParentId(JSFUtil.getUserSession().getUsers());
                        setUserList(users);
                        
                        Collection<Users> userCollection = messageBroadcast.getUsersCollection();
                        List<Integer> values= new ArrayList<Integer>();
                        if(userCollection != null) {
                            for (Users u: userCollection) {
                                values.add(u.getId());
                            }
                            selectedUser = values;
                        }
                }
            }
        }
    }

    public void userSelectingChangeListener() {
        if (selectingUser.equals("tmr")) {
            List<Users> users = null;
            if(SecurityUtil.isPermitted("admin:messagebroadcast:manager")) {
                users = this.getUsersDAO().findUsersEntities();
            } else {
                users = this.getUsersDAO().findUserUnderParentId(JSFUtil.getUserSession().getUsers());
            }
            setUserList(users);
        } else if(selectingUser.equals("group")){
                if(userList != null)
                    userList.clear();
                getUserGroupList();
        }
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public Map<String, Integer> getUserGroupList() {
        if(SecurityUtil.isPermitted("admin:messagebroadcast:manager")) {
            return getUserGroupDAO().getUserGroupList();
        } else {
            return getUserGroupDAO().getUserGroupListUnderParent(JSFUtil.getUserSession().getUsers());
        }
    }
   
    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:messagebroadcast:add");
        } else {
            return SecurityUtil.isPermitted("admin:messagebroadcast:edit"); 
        }
    }

    public String saveAction() {
        Date today = new Date();
        String userName = JSFUtil.getUserSession().getUsers().getName();
                
        if(selectingUser.equals("all")) {
            messageBroadcast.setAllUser(true);
            messageBroadcast.setUserGroupId(null);
            
            List<Users> allUsers = null;
            if(SecurityUtil.isPermitted("admin:messagebroadcast:manager")) {
                allUsers = this.getUsersDAO().findUsersEntities();
            } else {
                allUsers = this.getUsersDAO().findUserUnderParentId(JSFUtil.getUserSession().getUsers());
            }
//            List<Users> allUsers = this.getUsersDAO().findUserUnderParentId(JSFUtil.getUserSession().getUsers());
            messageBroadcast.setUsersCollection(allUsers);
        } else if(selectingUser.equals("group")) {
            messageBroadcast.setAllUser(false);
            messageBroadcast.setUserGroupId(userGroupId);
        } if(selectingUser.equals("tmr")) {
            messageBroadcast.setAllUser(false);
            messageBroadcast.setUserGroupId(null);
            messageBroadcast.setUsersCollection(this.getSelectedUserCollection());
        }
        try {
            if (getMode().equals("add")) {
                messageBroadcast.setCreateDate(today);
                messageBroadcast.setCreateBy(userName);
                messageBroadcast.setCreateByUser(JSFUtil.getUserSession().getUsers());
                messageBroadcast.setId(null);
                getMessageBroadcastDAO().create(messageBroadcast);
            } else {
                messageBroadcast.setUpdateDate(today);
                messageBroadcast.setUpdateBy(userName);
                getMessageBroadcastDAO().edit(messageBroadcast);
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

    public MessageBroadcast getMessageBroadcast() {
        return messageBroadcast;
    }

    public void setMessageBroadcast(MessageBroadcast messageBroadcast) {
        this.messageBroadcast = messageBroadcast;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
    
    public void setUserList(List<Users> users) {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Users u: users) {
            values.put(u.getName(), u.getId());
        }
        userList = values;
    }
    
    public Map<String, Integer> getUserList() {
        return userList;
    }

    public List<Integer> getSelectedUser() {
        return selectedUser;
    }
    
    public void setSelectedUser(List<Integer> selectedUser) {
        this.selectedUser = selectedUser;
    }
    
    public List<Users> getSelectedUserCollection() {
        List<Users> users = new ArrayList<Users>();
        for (int uid : getSelectedUser()) {
            Users u = new Users();
            u.setId(uid);
            users.add(u);
        }
        return users;
    }

    public int getuserGroupId() {
        return userGroupId;
    }

    public void setuserGroupId(int gid) {
        this.userGroupId = gid;
    }

    public MessageBroadcastDAO getMessageBroadcastDAO() {
        return messageBroadcastDAO;
    }

    public void setMessageBroadcastDAO(MessageBroadcastDAO messageBroadcastDAO) {
        this.messageBroadcastDAO = messageBroadcastDAO;
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

    public String getSelectingUser() {
        return selectingUser;
    }

    public void setSelectingUser(String selectingUser) {
        this.selectingUser = selectingUser;
    }
}
