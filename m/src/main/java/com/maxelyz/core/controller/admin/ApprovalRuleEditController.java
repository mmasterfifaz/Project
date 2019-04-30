package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ApprovalRuleDAO;
import com.maxelyz.core.model.entity.ApprovalRule;
import org.apache.log4j.Logger;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class ApprovalRuleEditController {
    private static Logger log = Logger.getLogger(ApprovalRuleEditController.class);
    private static String REDIRECT_PAGE = "approvalrule.jsf";
    private static String SUCCESS = "approvalrule.xhtml?faces-redirect=true";
    private static String FAILURE = "approvalruleedit.xhtml";

    private ApprovalRule approvalRule;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{approvalRuleDAO}")
    private ApprovalRuleDAO approvalRuleDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:approvalrule:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            approvalRule = new ApprovalRule();
            approvalRule.setEnable(Boolean.TRUE);
            approvalRule.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            try{
                approvalRule = approvalRuleDAO.findApprovalRule(new Integer(selectedID));
            }catch(Exception e){
                e.printStackTrace();
            }
            if (approvalRule == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }
    }


    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:approvalrule:add");
        } else {
            return SecurityUtil.isPermitted("admin:approvalrule:edit");
        }
    }
        
    public String saveAction() {
        messageDup = "";
        if(checkName(approvalRule)) {
            try {
                if (mode.equals("add")) {
                    approvalRule.setId(null);
                    approvalRule.setEnable(true);
                    approvalRuleDAO.create(approvalRule);
                } else {
                    approvalRuleDAO.edit(approvalRule);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Name is already taken";
            return null;  
//            return FAILURE;  
        }
    }

    public Boolean checkName(ApprovalRule approvalRule) {
        String name = approvalRule.getName();
        Integer id=0; 
        if(approvalRule.getId() != null)
            id = approvalRule.getId();
        ApprovalRuleDAO dao = getApprovalRuleDAO();
        
        Integer cnt = dao.checkApprovalRuleName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
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

    public ApprovalRule getApprovalRule() {
        return approvalRule;
    }

    public void setApprovalRule(ApprovalRule approvalRule) {
        this.approvalRule = approvalRule;
    }

    public ApprovalRuleDAO getApprovalRuleDAO() {
        return approvalRuleDAO;
    }

    public void setApprovalRuleDAO(ApprovalRuleDAO approvalRuleDAO) {
        this.approvalRuleDAO = approvalRuleDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

}
