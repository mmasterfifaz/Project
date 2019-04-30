package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.AssignmentDetailDAO;
import com.maxelyz.core.model.dao.CampaignDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.MarketingDAO;
import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.Marketing;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class MarketingController implements Serializable {

    private static Logger log = Logger.getLogger(MarketingController.class);
    private static String REFRESH = "marketing.xhtml?faces-redirect=true";
    private static String EDIT = "marketingedit.xhtml";
    private static String SEGMENTATION = "marketingsegmentation.xhtml";
    private static String HISTORY = "marketinghistory.xhtml";
    private String message = "";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<Marketing> marketings;
    private String keyword;
    private Marketing marketing;
    private List<AssignmentDetail> assignmentDetailList;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;

    @PostConstruct
    public void initialize() {
       if (!SecurityUtil.isPermitted("admin:marketinglistupload:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        MarketingDAO dao = getMarketingDAO();
        marketings = dao.findMarketingEntities();
    }

    public boolean checkMarketingForPurge(Integer id) {
        Integer cntMIB = campaignDAO.checkMarketingInbound(id);
        if(cntMIB == 0)
            return true;
        else
            return false;
      
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:marketinglistupload:add");
    }

    public boolean isPurgePermitted() {
        return SecurityUtil.isPermitted("admin:marketinglistupload:purge");
    }
    
    public boolean isEOCPermitted() {
        return SecurityUtil.isPermitted("admin:marketinglistupload:eoc");
    }

    public List<Marketing> getList() {
        return getMarketings();
    }
    
    public void searchActionListener(ActionEvent event){
        this.search();
    }
    
    private void search(){
        marketings = marketingDAO.findBy(keyword);
    }

    public String editAction() {
        return EDIT;
    }
    
    public String segmentationAction() {
        return SEGMENTATION;
    }
    
    public String historyAction() {
        return HISTORY;
    }

    public String purgeAction() {
        Integer marketingId = null;
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    marketingId = item.getKey();
                    marketingDAO.purge(marketingId);
                    
                    marketing = marketingDAO.findMarketing(marketingId);
                    marketing.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    marketing.setUpdateDate(new Date());
                    marketing.setEnable(false);
                    marketingDAO.edit(marketing);
                    //writeLog(marketing);
                }
            }
        } catch (Exception e) {
            message = e.getMessage();
            log.error(e);
        }
        return REFRESH;
    }

    public String EOCAction(){
        Integer marketingId = null;
        try {
            for (Map.Entry<Integer, Boolean> entry : selectedIds.entrySet()) {
                if(entry.getValue()){
                    marketingId  = entry.getKey();
                    assignmentDetailList = assignmentDetailDAO.findAssignmentDetailByMarketingId(marketingId);

                    for (int i = 0; i < assignmentDetailList.size(); i++) {
                        assignmentDetailList.get(i).setUpdateDate(new Date());
                        assignmentDetailList.get(i).setStatus("closed");
                        assignmentDetailDAO.edit(assignmentDetailList.get(i));
                    }
                }
            }
        } catch (Exception e) {
            message = e.getMessage();
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

    public List<Marketing> getMarketings() {
        return marketings;
    }

    public void setMarketings(List<Marketing> marketings) {
        this.marketings = marketings;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }

    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
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

}
