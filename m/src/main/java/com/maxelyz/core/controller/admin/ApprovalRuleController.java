package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ApprovalRuleDAO;
import com.maxelyz.core.model.entity.ApprovalRule;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class ApprovalRuleController implements Serializable {

    private static Logger log = Logger.getLogger(ApprovalRuleController.class);
    private static String REFRESH = "approvalrule.xhtml?faces-redirect=true";
    private static String EDIT = "approvalruleedit.xhtml";
    private static String DETAIL = "approvalruledetail.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<ApprovalRule> approvalRuleList;
    private ApprovalRule approvalRule;
    
    @ManagedProperty(value = "#{approvalRuleDAO}")
    private ApprovalRuleDAO approvalRuleDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:approvalrule:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        approvalRuleList = approvalRuleDAO.findApprovalRuleEntities();
        
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:approvalrule:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:approvalrule:delete");
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    approvalRule = approvalRuleDAO.findApprovalRule(item.getKey());
                    approvalRule.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    approvalRule.setUpdateDate(new Date());
                    approvalRule.setEnable(false);
                    approvalRuleDAO.edit(approvalRule);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public String detailAction(){
        return DETAIL;
    }

    public ApprovalRuleDAO getApprovalRuleDAO() {
        return approvalRuleDAO;
    }

    public void setApprovalRuleDAO(ApprovalRuleDAO approvalRuleDAO) {
        this.approvalRuleDAO = approvalRuleDAO;
    }

    public List<ApprovalRule> getApprovalRuleList() {
        return approvalRuleList;
    }

    public void setApprovalRuleList(List<ApprovalRule> approvalRuleList) {
        this.approvalRuleList = approvalRuleList;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public ApprovalRule getApprovalRule() {
        return approvalRule;
    }

    public void setApprovalRule(ApprovalRule approvalRule) {
        this.approvalRule = approvalRule;
    }
    
}
