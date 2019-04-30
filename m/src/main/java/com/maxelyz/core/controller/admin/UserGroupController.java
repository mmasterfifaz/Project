package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.entity.UserGroup;
import com.maxelyz.core.model.entity.UserGroupAcl;
import com.maxelyz.core.model.entity.UserGroupAclPK;
import com.maxelyz.core.model.entity.UserGroupLocation;
import com.maxelyz.core.model.entity.UserGroupLocationPK;
import javax.annotation.PostConstruct;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import org.apache.commons.beanutils.BeanUtils;

@ManagedBean
@ViewScoped
public class UserGroupController implements Serializable {

    private static Logger log = Logger.getLogger(UserGroupController.class);
    private static String REFRESH = "usergroup.xhtml?faces-redirect=true";
    private static String EDIT = "usergroupedit.xhtml";
    private String message = "";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<UserGroup> userGroups;
    private List<UserGroup> userGroupList;
    private String keyword;
    private UserGroup usrg;
    private String name = "";
    private Integer userGroupId = 0;
    private String role = "";
    private String status = "";
    
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:usergroup:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        userGroupList = userGroupDAO.findUserGroupEntities();
        userGroups = userGroupDAO.findUserGroupEntities();
    }

    public void searchActionListener(ActionEvent event) {
        userGroups = userGroupDAO.findUserGroupBy(name, userGroupId, role, status);
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:usergroup:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:usergroup:delete");
    }

    public List<UserGroup> getList() {
        return getUserGroups();
    }

    public String searchAction() {
        UserGroupDAO dao = getUserGroupDAO();
        userGroups = dao.findUserGroupByName(keyword);
        return REFRESH;
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() throws Exception {
        UserGroupDAO dao = getUserGroupDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    usrg = dao.findUserGroup(item.getKey());
                    usrg.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    usrg.setUpdateDate(new Date());
                    usrg.setEnable(false);
                    dao.edit(usrg);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public String copyAction() {
        try {
            String selectedID = (String) JSFUtil.getRequestParameterMap("userGroupID");
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            
            UserGroup copyFromId = this.getUserGroupDAO().findUserGroup(new Integer(selectedID));
            UserGroup newUserGroup = new UserGroup();
            
            BeanUtils.copyProperties(newUserGroup, copyFromId);            
            newUserGroup.setId(null);
            newUserGroup.setName(copyFromId.getName() + " (2) ");
            newUserGroup.setCreateDate(now);
            newUserGroup.setCreateBy(username);
            newUserGroup.setEnable(Boolean.TRUE);
            newUserGroup.setStatus(Boolean.TRUE);
            newUserGroup.setUpdateBy(null);
            newUserGroup.setUpdateDate(null);
            newUserGroup.setUserGroupAclCollection(null);
            newUserGroup.setUserGroupLocationCollection(null);
            newUserGroup.setUsersCollection(null);
            this.getUserGroupDAO().create(newUserGroup);
            
            //SET ACL COLLECTION
            Integer newGroupId = newUserGroup.getId();
            List<UserGroupAcl> newUserGroupAclCollection = new ArrayList<UserGroupAcl>();
            for(UserGroupAcl acl: copyFromId.getUserGroupAclCollection()) {
                UserGroupAcl newAcl = new UserGroupAcl();
                UserGroupAclPK aclPK = new UserGroupAclPK(acl.getAcl().getCode(), newGroupId);
                newAcl.setUserGroupAclPK(aclPK);
                newAcl.setRemark(acl.getRemark());
                newUserGroupAclCollection.add(newAcl);
            }            
            newUserGroup.setUserGroupAclCollection(newUserGroupAclCollection);
            
            //SET LOCATION COLLECTION
            List<UserGroupLocation> newUserGroupLocationCollection = new ArrayList<UserGroupLocation>();
            for(UserGroupLocation location: copyFromId.getUserGroupLocationCollection()) {
                UserGroupLocation newLocation = new UserGroupLocation();
                UserGroupLocationPK  locationPK = new UserGroupLocationPK(newGroupId, location.getServiceType().getId(), location.getBusinessUnit().getId(), location.getLocation().getId());
                newLocation.setUserGroupLocationPK(locationPK);
                newLocation.setRemark(location.getRemark());
                newUserGroupLocationCollection.add(newLocation);
            }      
            newUserGroup.setUserGroupLocationCollection(newUserGroupLocationCollection);
            
            this.getUserGroupDAO().edit(newUserGroup);
            
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

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public List<UserGroup> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(List<UserGroup> userGroupList) {
        this.userGroupList = userGroupList;
    }
}
