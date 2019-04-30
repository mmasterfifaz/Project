/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import com.maxelyz.social.model.dao.SoTransferReasonDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.social.model.entity.SoTransferReason;
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
public class TransferReasonController {
           
    private static Logger log = Logger.getLogger(TransferReasonController.class);
    private static String REFRESH = "transferreason.xhtml?faces-redirect=true";
    private static String EDIT = "transferreasonedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<SoTransferReason> transferReasonList;
    private SoTransferReason transferReason;
    @ManagedProperty(value = "#{soTransferReasonDAO}")
    private SoTransferReasonDAO soTransferReasonDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:transferReason:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        transferReasonList = soTransferReasonDAO.findSoTransferReasonListOrderByCode();        
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:transferReason:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:transferReason:delete");
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    transferReason =  soTransferReasonDAO.findSoTransferReason(item.getKey());
                    transferReason.setEnable(false);
                    soTransferReasonDAO.edit(transferReason);
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

    public List<SoTransferReason> getTransferReasonList() {
        return transferReasonList;
    }

    public void setTransferReasonList(List<SoTransferReason> transferReasonList) {
        this.transferReasonList = transferReasonList;
    }

    public SoTransferReason getTransferReason() {
        return transferReason;
    }

    public void setTransferReason(SoTransferReason transferReason) {
        this.transferReason = transferReason;
    }

    public SoTransferReasonDAO getSoTransferReasonDAO() {
        return soTransferReasonDAO;
    }

    public void setSoTransferReasonDAO(SoTransferReasonDAO soTransferReasonDAO) {
        this.soTransferReasonDAO = soTransferReasonDAO;
    } 
}
