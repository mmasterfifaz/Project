package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.FollowupsaleReasonDAO;
import com.maxelyz.core.model.entity.FollowupsaleReason;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class FollowupEditController {
    private static Logger log = Logger.getLogger(FollowupEditController.class);
    private static String REDIRECT_PAGE = "followup.jsf";
    private static String SUCCESS = "followup.xhtml?faces-redirect=true";
    private static String FAILURE = "followupedit.xhtml";
    private FollowupsaleReason followup;
    private String mode;
    private String message = "";
    private String messageDup = "";
    
    @ManagedProperty(value = "#{followupsaleReasonDAO}")
    private FollowupsaleReasonDAO followupsaleReasonDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:followup:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            followup = new FollowupsaleReason();
            followup.setEnable(Boolean.TRUE);
            followup.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            FollowupsaleReasonDAO dao = getFollowupsaleReasonDAO();
            followup = dao.findFollowupsaleReason(new Integer(selectedID));
            if (followup==null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }
    }
    
      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:followup:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:followup:edit"); 
       }
    } 
    
    public String saveAction() {
        messageDup = "";
        if(checkCode(followup)) {
            FollowupsaleReasonDAO dao = getFollowupsaleReasonDAO();
            try {
                if (getMode().equals("add")) {
                    followup.setId(null);
                    dao.create(followup);
                } else {
                    dao.edit(followup);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Code is already taken";
            return FAILURE;  
        }
    }
   
    public Boolean checkCode(FollowupsaleReason followup) {
        String code = followup.getCode();
        Integer id=0; 
        if(followup.getId() != null)
            id = followup.getId();
       FollowupsaleReasonDAO dao = getFollowupsaleReasonDAO();
        
        Integer cnt = dao.checkFollowupsaleReasonCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public FollowupsaleReason getFollowupsaleReason() {
        return followup;
    }

    public void setFollowupsaleReason(FollowupsaleReason followup) {
        this.followup = followup;
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

    public FollowupsaleReasonDAO getFollowupsaleReasonDAO() {
        return followupsaleReasonDAO;
    }

    public void setFollowupsaleReasonDAO(FollowupsaleReasonDAO followupsaleReasonDAO) {
        this.followupsaleReasonDAO = followupsaleReasonDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
    
}
