package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;

import com.maxelyz.core.model.dao.ApprovalReasonDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.ApprovalReason;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class ApprovalReasonController implements Serializable{
    private static Logger log = Logger.getLogger(ApprovalReasonController.class);
    private static String REFRESH = "approvalreason.xhtml";
    private static String EDIT = "approvalreasonedit.xhtml";
  
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<ApprovalReason> approvalReasons;
    private ApprovalReason approvalReason;
    
    @ManagedProperty(value="#{approvalReasonDAO}")
    private ApprovalReasonDAO approvalReasonDAO;
    @ManagedProperty(value="#{securityService}")
    private SecurityService securityService;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:approvalreason:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        approvalReasons = approvalReasonDAO.findApprovalReasonEntities();
        
    }

    public List<ApprovalReason> getList() {
        return approvalReasons;
    }

     public String editAction() {
       return EDIT;
    }

    public String deleteAction() throws Exception {

        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    approvalReason = approvalReasonDAO.findApprovalReason(item.getKey());
                    approvalReason.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    approvalReason.setUpdateDate(new Date());
                    approvalReason.setEnable(false);
                    approvalReasonDAO.edit(approvalReason);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:approvalreason:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:approvalreason:delete");
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<ApprovalReason> getApprovalReasons() {
        return approvalReasons;
    }

    public void setApprovalReasons(List<ApprovalReason> approvalReasons) {
        this.approvalReasons = approvalReasons;
    }


    public ApprovalReasonDAO getApprovalReasonDAO() {
        return approvalReasonDAO;
    }

    public void setApprovalReasonDAO(ApprovalReasonDAO approvalReasonDAO) {
        this.approvalReasonDAO = approvalReasonDAO;
    }


    public SecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
   
}
