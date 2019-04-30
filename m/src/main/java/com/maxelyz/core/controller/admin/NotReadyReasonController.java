/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.social.model.dao.SoUserNotReadyReasonDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.social.model.entity.SoUserNotReadyReason;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class NotReadyReasonController {
        
    private static Logger log = Logger.getLogger(NotReadyReasonController.class);
    private static String REFRESH = "notreadyreason.xhtml?faces-redirect=true";
    private static String EDIT = "notreadyreasonedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<SoUserNotReadyReason> notreadyReasonList;
    private SoUserNotReadyReason notreadyReason;
    @ManagedProperty(value = "#{soUserNotReadyReasonDAO}")
    private SoUserNotReadyReasonDAO soUserNotReadyReasonDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:notreadyReason:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        notreadyReasonList = soUserNotReadyReasonDAO.findSoUserNotReadyReasonEntities();        
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:notreadyReason:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:notreadyReason:delete");
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    notreadyReason =  soUserNotReadyReasonDAO.findSoUserNotReadyReason(item.getKey());
                    notreadyReason.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    notreadyReason.setUpdateDate(new Date());
                    notreadyReason.setEnable(false);
                    soUserNotReadyReasonDAO.edit(notreadyReason);
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

    public List<SoUserNotReadyReason> getNotreadyReasonList() {
        return notreadyReasonList;
    }

    public void setNotreadyReasonList(List<SoUserNotReadyReason> notreadyReasonList) {
        this.notreadyReasonList = notreadyReasonList;
    }

    public SoUserNotReadyReason getNotreadyReason() {
        return notreadyReason;
    }

    public void setNotreadyReason(SoUserNotReadyReason notreadyReason) {
        this.notreadyReason = notreadyReason;
    }

    public SoUserNotReadyReasonDAO getSoUserNotReadyReasonDAO() {
        return soUserNotReadyReasonDAO;
    }

    public void setSoUserNotReadyReasonDAO(SoUserNotReadyReasonDAO soUserNotReadyReasonDAO) {
        this.soUserNotReadyReasonDAO = soUserNotReadyReasonDAO;
    }

}
