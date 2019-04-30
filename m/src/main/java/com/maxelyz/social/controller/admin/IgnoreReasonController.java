/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import com.maxelyz.social.model.dao.SoIgnoreReasonDAO;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.social.model.entity.SoIgnoreReason;
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
public class IgnoreReasonController {
        
    private static Logger log = Logger.getLogger(IgnoreReasonController.class);
    private static String REFRESH = "ignorereason.xhtml?faces-redirect=true";
    private static String EDIT = "ignorereasonedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<SoIgnoreReason> soIgnoreReasonList;
    private SoIgnoreReason soIgnoreReason;
    @ManagedProperty(value = "#{soIgnoreReasonDAO}")
    private SoIgnoreReasonDAO soIgnoreReasonDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:ignoreReason:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        soIgnoreReasonList = soIgnoreReasonDAO.findSoIgnoreReasonEntities();        
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:ignoreReason:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:ignoreReason:delete");
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    soIgnoreReason =  soIgnoreReasonDAO.findSoIgnoreReason(item.getKey());
                    soIgnoreReason.setEnable(false);
                    soIgnoreReasonDAO.edit(soIgnoreReason);
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

    public List<SoIgnoreReason> getSoIgnoreReasonList() {
        return soIgnoreReasonList;
    }

    public void setSoIgnoreReasonList(List<SoIgnoreReason> soIgnoreReasonList) {
        this.soIgnoreReasonList = soIgnoreReasonList;
    }

    public SoIgnoreReason getSoIgnoreReason() {
        return soIgnoreReason;
    }

    public void setSoIgnoreReason(SoIgnoreReason soIgnoreReason) {
        this.soIgnoreReason = soIgnoreReason;
    }

    public SoIgnoreReasonDAO getSoIgnoreReasonDAO() {
        return soIgnoreReasonDAO;
    }

    public void setSoIgnoreReasonDAO(SoIgnoreReasonDAO soIgnoreReasonDAO) {
        this.soIgnoreReasonDAO = soIgnoreReasonDAO;
    }    
}
