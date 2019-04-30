package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.value.admin.AssignmentTransferValue;
import com.maxelyz.core.model.dao.AssignmentDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.UnassignmentDAO;
import java.io.Serializable;
import com.maxelyz.core.model.dao.AssignmentTransferDAO;
import com.maxelyz.core.model.entity.Assignment;
import com.maxelyz.core.model.entity.Campaign;
import com.maxelyz.core.model.entity.Unassignment;
import com.maxelyz.core.model.entity.AssignmentTransfer;
//import com.maxelyz.utils.JSFUtil;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.JSFUtil;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name = "campaignSummaryController")
@ViewScoped
public class CampaignSummaryController implements Serializable {

    private static Logger log = Logger.getLogger(CampaignSummaryController.class);
    private Campaign campaign;
    private List<Assignment> assignmentList;
    private List<Unassignment> unassignmentList;
    private List<AssignmentTransfer> assignmentTransferList;
    private List<AssignmentTransferValue> transferList;
    private static String REDIRECT_PAGE = "campaign.jsf";
    private static String SUCCESS = "campaign.xhtml?faces-redirect=true";
    private static String EDIT = "campaignedit.xhtml";
    private static String FAILURE = "campaignsummary.xhtml";
    private static String ASSIGN = "campaignassignment.xhtml";
    private static String UNASSIGN = "campaignunassignment.xhtml";
    private static String TRANSFER = "campaigntransfer.xhtml";
    private static String PROSPECT = "campaignnewprospect.xhtml";
    private boolean enablePooling;
    private static String selectedTab;

    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{assignmentDAO}")
    private AssignmentDAO assignmentDAO;
    @ManagedProperty(value = "#{unassignmentDAO}")
    private UnassignmentDAO unassignmentDAO;
    @ManagedProperty(value = "#{assignmentTransferDAO}")
    private AssignmentTransferDAO assignmentTransferDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:campaign:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        enablePooling = false;
        String selectedID = "";

        if (JSFUtil.getRequestParameterMap("id") != null) {
            selectedID = JSFUtil.getRequestParameterMap("id");
        } else if (JSFUtil.getServletContext().getAttribute("id") != null) {
            selectedID = (String) JSFUtil.getServletContext().getAttribute("id").toString();
            JSFUtil.getServletContext().removeAttribute("id");
        }

        if (selectedTab == null) {
            if (this.isAddAssPermitted()) {
                selectedTab = "assignment";
            } else if (this.isAddUnAssPermitted()) {
                selectedTab = "unassignment";
            } else if (this.isAddTransferPermitted()) {
                selectedTab = "transfer";
            } else {
                selectedTab = "";
            }
        }
        if (selectedID == null || selectedID.isEmpty()) {
            JSFUtil.redirect(REDIRECT_PAGE);
            return;
        } else {
            CampaignDAO dao = getCampaignDAO();
            campaign = dao.findCampaign(new Integer(selectedID));
            if (selectedTab.equals("assignment")) {
                if (JSFUtil.getUserSession().getUsers().getIsAdministrator()) {
                    assignmentList = this.getAssignmentDAO().findAssignmentEntitiesByAdministrator(campaign);
                }else if (JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager")) {
                    assignmentList = this.getAssignmentDAO().findAssignmentEntitiesByManager(campaign, JSFUtil.getUserSession().getUsers());
                }else{
                    assignmentList = this.getAssignmentDAO().findAssignmentEntitiesByUser(campaign, JSFUtil.getUserSession().getUsers());
                }            
            } else if (selectedTab.equals("unassignment")) {
                        unassignmentList = this.getUnassignmentDAO().findUnassignmentEntitiesByCampaign(campaign.getId());
            } else if (selectedTab.equals("transfer")) {
                        transferList = this.getAssignmentTransferDAO().findAssignmentTransferEntitiesByCampaign(campaign.getId());
            }
        }
    }

    public void clearTab() {
        selectedTab = null;
    }
        
    public boolean isAddAssPermitted() {
        return SecurityUtil.isPermitted("admin:campaignassignment:add");
    }

    public boolean isAddUnAssPermitted() {
        return SecurityUtil.isPermitted("admin:campaignunassignment:add");
    }

    public boolean isEditPermitted() {
        return SecurityUtil.isPermitted("admin:campaign:edit");
    }

    public boolean isAddTransferPermitted() {
        return SecurityUtil.isPermitted("admin:campaignassignmenttransfer:add");
    }

    public boolean isAddNewProspectPermitted() {
        return SecurityUtil.isPermitted("admin:addnewprospect:add");
    }

    public void showPoolingListener(ValueChangeEvent event) {
        enablePooling = (Boolean) event.getNewValue();
        if (!enablePooling) {
            if (JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager")) {
//                if (JSFUtil.getUserSession().getUsers().getIsAdministrator()) {
                    assignmentList = this.getAssignmentDAO().findAssignmentEntitiesByCampaign(campaign.getId());
//                } else {
//                    assignmentList = this.getAssignmentDAO().findAssignmentEntitiesByCampaignAndMarketing(campaign.getId(), JSFUtil.getUserSession().getUsers().getUserGroup().getId());
//                }
            } else {
                assignmentList = this.getAssignmentDAO().findAssignmentEntitiesByCampaign(campaign.getId());
            }
        } else {
            if (JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager")) {
//                if (JSFUtil.getUserSession().getUsers().getIsAdministrator()) {
                    assignmentList = this.getAssignmentDAO().findEnableAssignmentPooling(campaign.getId());
//                } else {
//                    assignmentList = this.getAssignmentDAO().findEnableAssignmentPoolingByMarketing(campaign.getId(), JSFUtil.getUserSession().getUsers().getUserGroup().getId());
//                }
            } else {
                assignmentList = this.getAssignmentDAO().findEnableAssignmentPooling(campaign.getId());
            }
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public String editAction() {
        return EDIT;
    }

    public String assignAction() {
        return ASSIGN;
    }

    public String unAssignAction() {
        return UNASSIGN;
    }

    public String transferAction() {
        return TRANSFER;
    }

    public String backAction() {
        return SUCCESS;
    }

    public String prospectAction() {
        return PROSPECT;
    }

    //List
    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public List<Unassignment> getUnassignmentList() {
        return unassignmentList;
    }

    public List<AssignmentTransfer> getAssignmentTransfer() {
        return assignmentTransferList;
    }

    public List<AssignmentTransferValue> getTransferList() {
        return transferList;
    }

    //Properties
    public Campaign getCampaign() {
        return campaign;
    }

    //MAnaged Properties
    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public AssignmentDAO getAssignmentDAO() {
        return assignmentDAO;
    }

    public void setAssignmentDAO(AssignmentDAO assignmentDAO) {
        this.assignmentDAO = assignmentDAO;
    }

    public UnassignmentDAO getUnassignmentDAO() {
        return unassignmentDAO;
    }

    public void setUnassignmentDAO(UnassignmentDAO unassignmentDAO) {
        this.unassignmentDAO = unassignmentDAO;
    }

    public AssignmentTransferDAO getAssignmentTransferDAO() {
        return assignmentTransferDAO;
    }

    public void setAssignmentTransferDAO(AssignmentTransferDAO assignmentTransferDAO) {
        this.assignmentTransferDAO = assignmentTransferDAO;
    }

    public String changeAssignmentType(String assignmentType) {
        if (assignmentType.equals("average")) {
            return "Average";
        } else if (assignmentType.equals("custom")) {
            return "Custom";
        } else if (assignmentType.equals("pooling")) {
            return "Pooling";
        } else if (assignmentType.equals("poolingcustom")) {
            return "Pooling (Custom)";
        } else if (assignmentType.equals("poolingdaily")) { 
            return "Pooling (Daily)";
        } else {
            return "Auto-Dialer";
        }
    }

    public boolean isEnablePooling() {
        return enablePooling;
    }

    public void setEnablePooling(boolean enablePooling) {
        this.enablePooling = enablePooling;
    }

    public String getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }
}
