package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.entity.Campaign;
import com.maxelyz.core.model.value.admin.CampaignValue;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

@ManagedBean
//@RequestScoped
@ViewScoped
public class CampaignController implements Serializable{
    private static Logger log = Logger.getLogger(CampaignController.class);
    private static String REFRESH = "campaign.xhtml?faces-redirect=true";
    private static String EDIT = "campaignedit.xhtml";
    private static String SUMMARY = "campaignsummary.xhtml";

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<CampaignValue> campaignList;
    private String keyword;
    private Campaign campaign;
    private String name = "";
    private Date dateFrom;
    private Date dateTo;
    private Date today;
    private String status = "";

    @ManagedProperty(value="#{campaignDAO}")
    private CampaignDAO campaignDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:campaign:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
//        try{
//            Date now = new Date();
//            dateFrom = JSFUtil.toDateWithoutTime(now);
//            dateTo = JSFUtil.toDateWithMaxTime(now);
//        }catch(Exception e){
//        
//        }
        today = new Date();
        this.search();
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:campaign:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:campaign:delete");
    }
    
    public boolean isManagePermitted() {
        return SecurityUtil.isPermitted("admin:campaign:manage");
    }
    
    public void searchActionListener(ActionEvent event){
        this.search();
    }
    
    private void search(){
        campaignList = campaignDAO.searchCampaignBy(name, dateFrom, dateTo, status);
    }

    public String editAction() {
        return EDIT;
    }

    public String summaryAction() {
        return SUMMARY;
    }
    
    public String deleteAction() throws Exception {
        CampaignDAO dao = getCampaignDAO();
         try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    campaign = dao.findCampaign(item.getKey());
                    
                    if(campaign.getMarketingInbound() != null) {
                        System.out.println("Delete Campaign Inbound Set Marketing inbound to NULL");
                        campaign.setMarketingInbound(null);
                    }
                    if(campaign.getAssignmentInbound() != null) {
                        System.out.println("Delete Campaign Set Assignment inbound to NULL");
                        campaign.setAssignmentInbound(null);
                    }
                    
                    campaign.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    campaign.setUpdateDate(new Date());
                    campaign.setEnable(false);
                    dao.edit(campaign);
                }
            }    
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;

    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<CampaignValue> getCampaignList() {
        return campaignList;
    }

    public void setCampaignList(List<CampaignValue> campaignList) {
        this.campaignList = campaignList;
    }

    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }
    
    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }
}
