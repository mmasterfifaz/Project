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
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
/**
 *
 * @author support
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class CampaignChannelEditController {
    private static Logger log = Logger.getLogger(CampaignChannelEditController.class);
    private static String REDIRECT_PAGE = "campaignchannel.jsf";
    private static String SUCCESS = "campaignchannel.xhtml?faces-redirect=true";
    //private static String FAILURE = "campaignchanneledit.xhtml";
    private CampaignChannel campaignChannel;
    private String mode;
    private String message;
    private String messageDup;
    
    @ManagedProperty(value = "#{campaignChannelDAO}")
    private CampaignChannelDAO campaignChannelDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:campaignchannel:view")) 
        {
            SecurityUtil.redirectUnauthorize();  
        }
        
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           campaignChannel = new CampaignChannel();
           campaignChannel.setEnable(Boolean.TRUE);
           campaignChannel.setStatus(Boolean.TRUE);
        } else {
           mode = "edit";
           CampaignChannelDAO dao = getCampaignChannelDAO();
           campaignChannel = dao.findCampaignChannel(new Integer(selectedID));
           if (campaignChannel==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           }
       }
    }
    
    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:campaignchannel:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:campaignchannel:edit"); 
       }
    }
    
    public String saveAction() {
        messageDup = "";
        if(checkCode(campaignChannel)) {
            CampaignChannelDAO dao = getCampaignChannelDAO();
            try {
                if (getMode().equals("add")) {
                    campaignChannel.setId(null);
                    campaignChannel.setCreateDate(new Date());
                    campaignChannel.setCreateBy(JSFUtil.getUserSession().getUserName());
                    campaignChannel.setEnable(true);
                    dao.create(campaignChannel);
                } else {
                    campaignChannel.setUpdateDate(new Date());
                    campaignChannel.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    dao.edit(campaignChannel);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return null;
            }
            return SUCCESS;
        } else {
            messageDup = " Code is already taken";
            return null;  
        }
    }
    
    public Boolean checkCode(CampaignChannel campaignChannel) {
        String topic = campaignChannel.getCode();
        Integer id = 0;
        if (campaignChannel.getId() != null) {
            id = campaignChannel.getId(); }
        
            Integer cnt = campaignChannelDAO.checkCampaignChannelCode(topic, id);
            if (cnt == 0) {
                return true;
            } else {
                return false;
            }
    }
    
    public String backAction() {
        return SUCCESS;
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

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
    
    
}
