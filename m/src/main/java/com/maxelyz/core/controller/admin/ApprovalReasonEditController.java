package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.ActivityTypeDAO;
import com.maxelyz.core.model.dao.ApprovalReasonDAO;
import com.maxelyz.core.model.entity.ActivityType;
import com.maxelyz.core.model.entity.ApprovalReason;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;

@ManagedBean
//@RequestScoped
@ViewScoped
public class ApprovalReasonEditController {
    private static Logger log = Logger.getLogger(ApprovalReasonEditController.class);
    private static String REDIRECT_PAGE = "approvalreason.jsf";
    private static String SUCCESS = "approvalreason.xhtml?faces-redirect=true";
    private static String FAILURE = "approvalreasonedit.xhtml";

    private ApprovalReason approvalReason;
    private String mode;
    private String message;
    private String messageDup;
    private String approveReasonType;

    @ManagedProperty(value="#{approvalReasonDAO}")
    private ApprovalReasonDAO approvalReasonDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:approvalreason:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
       messageDup = "";
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           approvalReason = new ApprovalReason();
           approvalReason.setEnable(Boolean.TRUE);
           approvalReason.setStatus(Boolean.TRUE);
       } else {
           mode = "edit";
           ApprovalReasonDAO dao = getApprovalReasonDAO();
           approvalReason = dao.findApprovalReason(new Integer(selectedID));
           if (approvalReason==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           }
       }
    }

    public String saveAction() {
        messageDup = "";
        if(checkCode(approvalReason)) {
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            ApprovalReasonDAO dao = getApprovalReasonDAO();
            try {
                if (getMode().equals("add")) {
                    approvalReason.setId(null);
                    approvalReason.setEnable(true);
                    approvalReason.setCreateBy(username);
                    approvalReason.setCreateDate(now);
                    if(approvalReason.getType().equals("tmr"))
                        approvalReason.setApprovalStatus("");
                    dao.create(approvalReason);
                } else {
                    if(approvalReason.getType().equals("tmr"))
                        approvalReason.setApprovalStatus("");
                    approvalReason.setUpdateBy(username);
                    approvalReason.setUpdateDate(now);
                    dao.edit(approvalReason);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Code is already taken";
            return null;  
        }
    }
    
    public Boolean checkCode(ApprovalReason approvalReason) {
        String code = approvalReason.getCode();
        Integer id=0; 
        if(approvalReason.getId() != null)
            id = approvalReason.getId();
        ApprovalReasonDAO dao = getApprovalReasonDAO();
        
        Integer cnt = dao.checkApprovalCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public boolean isSavePermitted() {
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
  	if (selectedID==null || selectedID.isEmpty()) {
           return SecurityUtil.isPermitted("admin:approvalreason:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:approvalreason:edit"); 
       }
    } 
    
    public Map<String, String> getActivityList() {
        Map<String, String> values = new LinkedHashMap<String, String>();
        values.put(JSFUtil.getBundleValue("approvalPending"), JSFUtil.getBundleValue("approvalPendingValue"));
        values.put(JSFUtil.getBundleValue("approvalRequest"), JSFUtil.getBundleValue("approvalRequestValue"));
        values.put(JSFUtil.getBundleValue("approvalReconfirm"), JSFUtil.getBundleValue("approvalReconfirmValue"));
        values.put(JSFUtil.getBundleValue("approvalResend"), JSFUtil.getBundleValue("approvalResendValue"));
        values.put(JSFUtil.getBundleValue("approvalRejected"), JSFUtil.getBundleValue("approvalRejectedValue"));
        values.put(JSFUtil.getBundleValue("approvalApproved"), JSFUtil.getBundleValue("approvalApprovedValue"));
        return values;
    }
        
    public void activityListener() {
        approveReasonType = approvalReason.getType();
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public ApprovalReason getApprovalReason() {
        return approvalReason;
    }

    public void setApprovalReason(ApprovalReason approvalReason) {
        this.approvalReason = approvalReason;
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

    public ApprovalReasonDAO getApprovalReasonDAO() {
        return approvalReasonDAO;
    }

    public void setApprovalReasonDAO(ApprovalReasonDAO approvalReasonDAO) {
        this.approvalReasonDAO = approvalReasonDAO;
    }

    public String getApproveReasonType() {
        return approveReasonType;
    }

    public void setApproveReasonType(String approveReasonType) {
        this.approveReasonType = approveReasonType;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

}
