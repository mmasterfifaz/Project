package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.AutoAssignmentDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.MarketingDAO;
import com.maxelyz.core.model.dao.ProspectlistSponsorDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.AutoAssignment;
import com.maxelyz.core.model.entity.Campaign;
import com.maxelyz.core.model.entity.Marketing;
import com.maxelyz.core.model.entity.ProspectlistSponsor;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean (name="autoAssignmentServiceEditController")
@ViewScoped
public class AutoAssignmentServiceEditController {
    private static Logger log = Logger.getLogger(AutoAssignmentServiceEditController.class);
    private static String REDIRECT_PAGE = "autoassignmentservice.jsf";
    private static String SUCCESS = "autoassignmentservice.xhtml?faces-redirect=true";
    private static String FAILURE = "autoassignmentserviceedit.xhtml";

    private String mode;
    private String message;
    private String messageDup;
    private AutoAssignment autoAssignmentService;
    private int campaignId = 0;
    private int marketingId = 0;
    private int sponsorId = 0;
    private List<Integer> selectedAgent;
    private Map<String, Integer> agentList = new LinkedHashMap<String, Integer>();

    @ManagedProperty(value="#{autoAssignmentDAO}")
    private AutoAssignmentDAO autoAssignmentDAO;
    @ManagedProperty(value="#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    @ManagedProperty(value = "#{prospectlistSponsorDAO}")
    private ProspectlistSponsorDAO prospectlistSponsorDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:autoassignmentservice:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        messageDup = "";                
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        
        if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           autoAssignmentService = new AutoAssignment();
           autoAssignmentService.setEnable(Boolean.TRUE);
           autoAssignmentService.setStatus(Boolean.TRUE);
           autoAssignmentService.setPriority(3);
           autoAssignmentService.setAutoCreateType("day");
           autoAssignmentService.setAutoCreate(Boolean.FALSE);
       } else {
           mode = "edit";
           AutoAssignmentDAO dao = getAutoAssignmentDAO();
           autoAssignmentService = dao.findAutoAssignment(new Integer(selectedID));
           
           campaignId = autoAssignmentService.getCampaign().getId();
           
            if(autoAssignmentService.getAgentsCollection()!= null) {
                Collection<Users> userCollection = autoAssignmentService.getAgentsCollection();
                List<Integer> values= new ArrayList<Integer>();
                if(userCollection != null) {
                    for (Users u: userCollection) {
                        values.add(u.getId());
                    }
                    selectedAgent = values;
                }
            }
            if(autoAssignmentService.getAutoCreate()) {
                if(autoAssignmentService.getAutoProspectlistSponsor() != null) {
                    sponsorId = autoAssignmentService.getAutoProspectlistSponsor().getId();
                }
            } else {
                if(autoAssignmentService.getPredefinedMarketing() != null) {
                    marketingId = autoAssignmentService.getPredefinedMarketing().getId();
                    if(autoAssignmentService.getAutoCreateType() == null) {
                        autoAssignmentService.setAutoCreateType("day");
                    }
                }
            }
            
            if (autoAssignmentService == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
       }
        
        agentList = usersDAO.getAgentList();
    }

    public List<Users> getSelectedAgentCollection() {
        List<Users> users = new ArrayList<Users>();
        for (int uid : selectedAgent) {
            Users u = new Users();
            u.setId(uid);
            users.add(u);
        }
        return users;
    }
    
    public boolean isSavePermitted() {
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
  	if (selectedID==null || selectedID.isEmpty()) {
           return SecurityUtil.isPermitted("admin:autoassignmentservice:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:autoassignmentservice:edit"); 
       }
    } 
        
    public Boolean checkName(AutoAssignment autoAssignmentService) {
        String name = autoAssignmentService.getName();
        Integer id=0; 
        if(autoAssignmentService.getId() != null)
            id = autoAssignmentService.getId();
        
        AutoAssignmentDAO dao = getAutoAssignmentDAO();
        Integer cnt = dao.checkAutoAssignmentServiceName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String saveAction() {
        messageDup = "";
        if(checkName(autoAssignmentService)) {
            AutoAssignmentDAO dao = getAutoAssignmentDAO();
            
            if(autoAssignmentService.getAutoCreate()) {
                autoAssignmentService.setPredefinedMarketing(null);
                autoAssignmentService.setAutoProspectlistSponsor(new ProspectlistSponsor(sponsorId));
            } else {    //predefined
                autoAssignmentService.setPredefinedMarketing(new Marketing(marketingId));
                autoAssignmentService.setAutoCreateType(null);
                autoAssignmentService.setAutoEffectivePeriod(null);
                autoAssignmentService.setAutoMarketing(null);
                autoAssignmentService.setAutoMarketingName(null);
                autoAssignmentService.setAutoMarketingPrefix(null);
                autoAssignmentService.setAutoProspectlistSponsor(null);
            }
            autoAssignmentService.setCampaign(new Campaign(campaignId));
            autoAssignmentService.setAgentsCollection(this.getSelectedAgentCollection());
            
            try {
                if (getMode().equals("add")) {
                    autoAssignmentService.setId(null);
                    autoAssignmentService.setCreateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    autoAssignmentService.setCreateDate(new Date());
                    dao.create(autoAssignmentService);
                } else {
                    autoAssignmentService.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    autoAssignmentService.setUpdateDate(new Date());
                    dao.edit(autoAssignmentService);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Name is already taken";
            return null;  
        }
    }
    
    //List to UI----------------------------------------------------------------
    public Map<String, Integer> getCampaignList() {
        return this.getCampaignDAO().getCampaignEnableList();
    }
    
    public Map<String, Integer> getMarketingList() {
        return this.getMarketingDAO().getAvaialableAllMarketList();
    } 
    
    public Map<String, Integer> getProspectSponsorList() {
        return this.getProspectlistSponsorDAO().getProspectlistSponsorList();
    } 

    public String backAction() {
        return SUCCESS;
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

    public int getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(int marketingId) {
        this.marketingId = marketingId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public int getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(int sponsorId) {
        this.sponsorId = sponsorId;
    }

    public List<Integer> getSelectedAgent() {
        return selectedAgent;
    }

    public void setSelectedAgent(List<Integer> selectedAgent) {
        this.selectedAgent = selectedAgent;
    }

    public Map<String, Integer> getAgentList() {
        return agentList;
    }

    public void setAgentList(Map<String, Integer> agentList) {
        this.agentList = agentList;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public AutoAssignment getAutoAssignmentService() {
        return autoAssignmentService;
    }

    public void setAutoAssignmentService(AutoAssignment autoAssignmentService) {
        this.autoAssignmentService = autoAssignmentService;
    }
    
    public AutoAssignmentDAO getAutoAssignmentDAO() {
        return autoAssignmentDAO;
    }

    public void setAutoAssignmentDAO(AutoAssignmentDAO autoAssignmentDAO) {
        this.autoAssignmentDAO = autoAssignmentDAO;
    }

    public ProspectlistSponsorDAO getProspectlistSponsorDAO() {
        return prospectlistSponsorDAO;
    }

    public void setProspectlistSponsorDAO(ProspectlistSponsorDAO prospectlistSponsorDAO) {
        this.prospectlistSponsorDAO = prospectlistSponsorDAO;
    }

}
