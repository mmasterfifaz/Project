/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.WorkflowRuleDAO;
import com.maxelyz.core.model.entity.WorkflowRule;
import com.maxelyz.utils.JSFUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Administrator
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class WorkflowRuleController {
    
    private static Log log = LogFactory.getLog(WorkflowRuleController.class);
    private static String EDIT = "/admin/workflowruleedit.xhtml";
    private static String REFRESH = "/admin/workflowrule.xhtml?faces-redirect=true";

    private List<WorkflowRule> workflowRuleList;
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private String keyword;
    @ManagedProperty(value = "#{workflowRuleDAO}")
    private WorkflowRuleDAO workflowRuleDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:workflowrule:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        workflowRuleList = workflowRuleDAO.findWorkflowRuleEntities();
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:workflowrule:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:workflowrule:delete");
    }
    
    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        WorkflowRuleDAO dao = getWorkflowRuleDAO();
        WorkflowRule workflowRule = null;
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    workflowRule = dao.findWorkflowRule(item.getKey());
                    workflowRule.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    workflowRule.setUpdateDate(new Date());
                    workflowRule.setEnable(false);
                    dao.edit(workflowRule);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public void searchAction() {
        workflowRuleList = workflowRuleDAO.findWorkflowRuleByName(keyword);
    }
    
    public WorkflowRuleDAO getWorkflowRuleDAO() {
        return workflowRuleDAO;
    }

    public void setWorkflowRuleDAO(WorkflowRuleDAO workflowRuleDAO) {
        this.workflowRuleDAO = workflowRuleDAO;
    }

    public List<WorkflowRule> getWorkflowRuleList() {
        return workflowRuleList;
    }

    public void setWorkflowRuleList(List<WorkflowRule> workflowRuleList) {
        this.workflowRuleList = workflowRuleList;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
