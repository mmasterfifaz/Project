/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.controller.front.customerHandling.PagedList;
import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.AssignmentDetailDAO;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.Campaign;
import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.UserGroup;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.controller.admin.PageList;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author Administrator
 */
@ManagedBean (name="appochingRecordController")
//@RequestScoped
@ViewScoped
public class AppochingRecordController implements Serializable{
    private static Logger log = Logger.getLogger(AppochingRecordController.class);
    private static String REFRESH = "approchingrecord.xhtml?faces-redirect=true";
    private static String EDIT = "approchingrecordedit.xhtml";
    
    private List<Campaign> campaignList;
    private List<UserGroup> userGroupList;
    private List<Users> userList;
    private List<AssignmentDetail> assignmentDetailList;
    private String refNo = "";
    private String customerName = "";
    private String telephoneNo = "";
    private Integer campaignId;
    private Integer userGroupId;
    private Integer userId;
    private boolean contactStatusAssigned;
    private boolean contactStatusViewed;
    private boolean contactStatusOpened;
    private boolean contactStatusFollowup;
    private boolean contactStatusClosed;
    private Integer scrollerPage;
    
    @ManagedProperty(value="#{campaignDAO}")
    private CampaignDAO campaignDAO;

    @ManagedProperty(value="#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;

    @ManagedProperty(value="#{usersDAO}")
    private UsersDAO usersDAO;
    
    @ManagedProperty(value="#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:approchingrecord:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        userGroupList = userGroupDAO.findUserGroupEntities();
        if(userGroupId != null && userGroupId != 0){
            userList = usersDAO.findUsersEntitiesByUserGroup(userGroupId.intValue());
        }
        campaignList = campaignDAO.findCampaignEntities();
        scrollerPage = 1;
    }
        
    public boolean isEditPermitted() {
        return SecurityUtil.isPermitted("admin:approchingrecord:edit");
    }
    
    //Listener
    public void userGroupChangeListener(ValueChangeEvent event) {
        userGroupId = (Integer) event.getNewValue();
        if(userGroupId != 0){
            userList = usersDAO.findUsersEntitiesByUserGroup(userGroupId.intValue());
        }else{
            userList = null;
        }
    }
    
    public void searchActionListener(ActionEvent event){
        scrollerPage = 1;
        search();
    }
    
    public void search(){
        assignmentDetailList = assignmentDetailDAO.findAssignmentDetailBySearch(refNo,customerName, telephoneNo,campaignId,userGroupId,userId,contactStatusAssigned,contactStatusViewed,contactStatusOpened,contactStatusFollowup,contactStatusClosed,0,1000);
    }
        
    //Action
    public String editAction(){
        return EDIT;
    }

    //Get Set DAO
    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    //Get Set Properties
    public List<AssignmentDetail> getAssignmentDetailList() {
        return assignmentDetailList;
    }

    public void setAssignmentDetailList(List<AssignmentDetail> assignmentDetailList) {
        this.assignmentDetailList = assignmentDetailList;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public List<Campaign> getCampaignList() {
        return campaignList;
    }

    public void setCampaignList(List<Campaign>  campaignList) {
        this.campaignList = campaignList;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public List<UserGroup> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(List<UserGroup> userGroupList) {
        this.userGroupList = userGroupList;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Users> getUserList() {
        return userList;
    }

    public void setUserList(List<Users> userList) {
        this.userList = userList;
    }

    public boolean isContactStatusAssigned() {
        return contactStatusAssigned;
    }

    public void setContactStatusAssigned(boolean contactStatusAssigned) {
        this.contactStatusAssigned = contactStatusAssigned;
    }

    public boolean isContactStatusClosed() {
        return contactStatusClosed;
    }

    public void setContactStatusClosed(boolean contactStatusClosed) {
        this.contactStatusClosed = contactStatusClosed;
    }

    public boolean isContactStatusFollowup() {
        return contactStatusFollowup;
    }

    public void setContactStatusFollowup(boolean contactStatusFollowup) {
        this.contactStatusFollowup = contactStatusFollowup;
    }

    public boolean isContactStatusOpened() {
        return contactStatusOpened;
    }

    public void setContactStatusOpened(boolean contactStatusOpened) {
        this.contactStatusOpened = contactStatusOpened;
    }

    public boolean isContactStatusViewed() {
        return contactStatusViewed;
    }

    public void setContactStatusViewed(boolean contactStatusViewed) {
        this.contactStatusViewed = contactStatusViewed;
    }

    public Integer getScrollerPage() {
        return scrollerPage;
    }

    public void setScrollerPage(Integer scrollerPage) {
        this.scrollerPage = scrollerPage;
    }
}
