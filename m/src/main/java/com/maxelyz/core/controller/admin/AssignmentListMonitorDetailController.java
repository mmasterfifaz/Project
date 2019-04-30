package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.AssignmentDetailDAO;
import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.ContactHistoryDAO;
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.ContactHistory;
import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.Users;
import org.apache.log4j.Logger;
import java.io.Serializable;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import com.maxelyz.utils.SecurityUtil;
import java.util.List;
import javax.faces.event.ActionEvent;

@ManagedBean (name="assignmentListMonitorDetailController")
@ViewScoped
public class AssignmentListMonitorDetailController implements Serializable {

    private static Logger log = Logger.getLogger(AssignmentListMonitorDetailController.class);

    private static String REDIRECT_PAGE = "assignmentlistmonitor.jsf";
    private static String SUCCESS = "assignmentlistmonitor.xhtml?faces-redirect=true";
    private static String FAILURE = "assignmentlistmonitordetail.xhtml";
    private static String selectedTab;  
    private String mode;
    private String agentID;
    private String campaignID;
    private Users agent;
    private List<AssignmentDetail> assignmentDetailList;
    private List<ContactHistory> contactHistoryList;
    private List<PurchaseOrder> saleList;
    
    @ManagedProperty(value="#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value="#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value="#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    @ManagedProperty(value = "#{contactHistoryDAO}")
    private ContactHistoryDAO contactHistoryDAO;
    @ManagedProperty(value="#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
        
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:assignmentlistmonitoring:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
           
        if (selectedTab == null) {
            selectedTab = "assignment";
        }
        
        agentID = (String) JSFUtil.getRequestParameterMap("agentId");
        if (agentID == null || agentID.isEmpty()) {
            JSFUtil.redirect(REDIRECT_PAGE);
            return;
        } else {
            agent = this.getUsersDAO().findUsers(new Integer(agentID));
            campaignID = (String) JSFUtil.getRequestParameterMap("campaignId");
            mode = (String) JSFUtil.getRequestParameterMap("mode");
            if (selectedTab.equals("assignment")) { 
                assignmentDetailList = assignmentDetailDAO.findAssignmentDetailByAgent(campaignID, agentID, mode);
            } else if (selectedTab.equals("sale")) { 
                saleList =  purchaseOrderDAO.findPurchaseOrderByCampaignAndUser(campaignID, agentID, mode);
            } 
        }
    }
    
    public void tabListener() {
        selectedTab = "assignment";
    }
    
    public void contactHistoryListener(ActionEvent event) {
        Integer adId = Integer.parseInt(JSFUtil.getRequestParameterMap("adId"));
        contactHistoryList = contactHistoryDAO.findContactHistoryByAssignmentDetail(adId);
    }
      
    public String backAction() {
        return SUCCESS;
    }

     public String getSelectedTab() {  
         return selectedTab;  
     }  
    
     public void setSelectedTab(String selectedTab) {  
         this.selectedTab = selectedTab;  
     }  

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<AssignmentDetail> getAssignmentDetailList() {
        return assignmentDetailList;
    }

    public void setAssignmentDetailList(List<AssignmentDetail> assignmentDetailList) {
        this.assignmentDetailList = assignmentDetailList;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }

    public String getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(String campaignID) {
        this.campaignID = campaignID;
    }

    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }
    
    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public Users getAgent() {
        return agent;
    }

    public void setAgent(Users agent) {
        this.agent = agent;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public List<ContactHistory> getContactHistoryList() {
        return contactHistoryList;
    }

    public void setContactHistoryList(List<ContactHistory> contactHistoryList) {
        this.contactHistoryList = contactHistoryList;
    }

    public ContactHistoryDAO getContactHistoryDAO() {
        return contactHistoryDAO;
    }

    public void setContactHistoryDAO(ContactHistoryDAO contactHistoryDAO) {
        this.contactHistoryDAO = contactHistoryDAO;
    }

    public List<PurchaseOrder> getSaleList() {
        return saleList;
    }

    public void setSaleList(List<PurchaseOrder> saleList) {
        this.saleList = saleList;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }
     
     
}
