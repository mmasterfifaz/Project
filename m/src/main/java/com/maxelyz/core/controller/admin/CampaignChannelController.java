/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.CampaignChannelDAO;
import com.maxelyz.core.model.entity.CampaignChannel;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;
/**
 *
 * @author support
 */
@ManagedBean
@RequestScoped
public class CampaignChannelController implements Serializable {
     private static Logger log = Logger.getLogger(CampaignChannelController.class);
     private static String REFRESH = "campaignchannel.xhtml?faces-redirect=true";
     private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
     private static String EDIT = "campaignchanneledit.xhtml";
     private List<CampaignChannel> campaignChannels;
     private CampaignChannel campaignChannel;
     
     @ManagedProperty(value = "#{campaignChannelDAO}")
     private CampaignChannelDAO campaignChannelDAO;
     
     @PostConstruct
     public void initialize() {      
        
         if (!SecurityUtil.isPermitted("admin:campaignchannel:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        CampaignChannelDAO dao = getCampaignChannelDAO();
        campaignChannels = dao.findCampaignChannelEntities();
        
    }
    
    public String deleteAction() throws Exception {
        CampaignChannelDAO dao = getCampaignChannelDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    campaignChannel = dao.findCampaignChannel(item.getKey());
                    campaignChannel.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    campaignChannel.setUpdateDate(new Date());
                    campaignChannel.setEnable(false);
                    dao.edit(campaignChannel);
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
    
    public String editAction() {
        return EDIT;
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:campaignchannel:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:campaignchannel:delete");
    }
    
    public List<CampaignChannel> getList() {
        return getCampaignChannels();
    }
    
    public List<CampaignChannel> getCampaignChannels() {
        return campaignChannels;
    }

    public void setCampaignChannels(List<CampaignChannel> campaignChannels) {
        this.campaignChannels = campaignChannels;
    }

    public CampaignChannel getCampaignChannel() {
        return campaignChannel;
    }

    public void setCampaignChannel(CampaignChannel campaignChannel) {
        this.campaignChannel = campaignChannel;
    }

    public CampaignChannelDAO getCampaignChannelDAO() {
        return campaignChannelDAO;
    }

    public void setCampaignChannelDAO(CampaignChannelDAO campaignChannelDAO) {
        this.campaignChannelDAO = campaignChannelDAO;
    }
        
}
