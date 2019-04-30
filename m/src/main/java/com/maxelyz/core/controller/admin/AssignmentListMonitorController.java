package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.AssignmentDAO;
import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.value.admin.AssignmentListMonitorByStatusValue;
import com.maxelyz.core.model.value.admin.AssignmentListMonitorValue;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;


@ManagedBean
@ViewScoped
public class AssignmentListMonitorController implements Serializable{
    private static Logger log = Logger.getLogger(AssignmentListMonitorController.class);
    private static String REFRESH = "assignmentlistmonitor.xhtml?faces-redirect=true";
    private static String VIEW = "assignmentlistmonitordetail.xhtml";
    private int userGroupId;
    private int campaignId;

    private List<AssignmentListMonitorByStatusValue> assignmentByStatusList = new ArrayList<AssignmentListMonitorByStatusValue>();
    private String viewMode="today";
    private List<Integer> selectedCampaign = new ArrayList<Integer>();

    @ManagedProperty(value="#{assignmentDAO}")
    private AssignmentDAO assignmentDAO;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value="#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value="#{userDAO}")
    private UsersDAO usersDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:assignmentlistmonitoring:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
    }
    
    public void initUser() {
        assignmentByStatusList.clear();
        List<AssignmentListMonitorValue> assignmentList = null;

        if (viewMode.equals("today")) {
            assignmentList = this.getAssignmentDAO().findAssignmentListTodayByUser(campaignId, userGroupId);
        } else if (viewMode.equals("total")) {
            assignmentList = this.getAssignmentDAO().findAssignmentListAllByUser(campaignId, userGroupId);
        } else if (viewMode.equals("realtime")) {
            assignmentList = this.getAssignmentDAO().findAssignmentListRealTimeByUser(campaignId, userGroupId);
        }

        AssignmentListMonitorByStatusValue value=null;
        String name="", surname="";
        
        for (AssignmentListMonitorValue a : assignmentList) {
            if (!name.equals(a.getName()) || !surname.equals(a.getSurname())) {
                if (value!=null) {
                   assignmentByStatusList.add(value);
                }
                if(campaignId != 0) {
                    value = new AssignmentListMonitorByStatusValue(a.getCampaign(), a.getCampaignId(), a.getName(), a.getSurname(), a.getCreateBy());
                } else {
                    value = new AssignmentListMonitorByStatusValue("", 0, a.getName(), a.getSurname(), a.getCreateBy());
                }
                name = a.getName();
                surname = a.getSurname();
            }
            if (value!=null) {
                if ("assigned".equals(a.getStatus())) {
                   value.setAssigned(a.getNostatus().intValue());
                } else if ("viewed".equals(a.getStatus())) {
                   value.setViewed(a.getNostatus().intValue());
                 } else if ("opened".equals(a.getStatus())) {
                   value.setOpened(a.getNostatus().intValue());
                } else if ("followup".equals(a.getStatus())) {
                   value.setFollowup(a.getNostatus().intValue());
                } else if ("closed".equals(a.getStatus())) {
                   value.setClosed(a.getNostatus().intValue());
                }
                if(a.getGrossSale() != null) {
                    value.setGrossSale(value.getGrossSale() + a.getGrossSale().intValue());
                }
                if(a.getNetSale() != null) {
                    value.setNetSale(value.getNetSale() + a.getNetSale().intValue());
                }
            }
        }
        if (value!=null) {
            assignmentByStatusList.add(value);
        }
    }

    public String viewAction() {
       return VIEW;
    }
        
    public List<AssignmentListMonitorByStatusValue> getList() {
        return assignmentByStatusList;
    }
    
    public void pickListValueChange(ActionEvent event){
        List<Integer> list = selectedCampaign;
    }
    
    public Map<String, Integer> getUserGroupList() {    
        return getUserGroupDAO().getUserGroupListByAgent();
    }

    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }
    
    public Map<String, Integer> getCampaignList() {    
        return getCampaignDAO().getCampaignEnableList();
    }
    
    public int getCampaignId() {
        return campaignId;
    }
    
    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }
    
    public void searchListener(ActionEvent event) {
        initUser();
        //FacesContext.getCurrentInstance().renderResponse();
    }

    public String getViewMode() {
        return viewMode;
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
    }
    
    
    //Managed Propterties
    public AssignmentDAO getAssignmentDAO() {
        return assignmentDAO;
    }

    public void setAssignmentDAO(AssignmentDAO assignmentDAO) {
        this.assignmentDAO = assignmentDAO;
    }
    
    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
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
    
    public List<Integer> getSelectedCampaign() {
        return selectedCampaign;
    }
    
    public void setSelectedCampaign(List<Integer> selectedCampaign) {
        this.selectedCampaign = selectedCampaign;
}
}
