package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.ActivityTypeDAO;
import com.maxelyz.core.model.entity.ActivityType;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class ActivityTypeEditController {
    private static Logger log = Logger.getLogger(ActivityTypeEditController.class);
    private static String REDIRECT_PAGE = "activitytype.jsf";
    private static String SUCCESS = "activitytype.xhtml?faces-redirect=true";
    private static String FAILURE = "activitytypeedit.xhtml";

    private ActivityType activityType;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{activityTypeDAO}")
    private ActivityTypeDAO activityTypeDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:activitytype:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        messageDup = "";                
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           activityType = new ActivityType();
           activityType.setEnable(Boolean.TRUE);
           activityType.setStatus(Boolean.TRUE);
       } else {
           mode = "edit";
           ActivityTypeDAO dao = getActivityTypeDAO();
           activityType = dao.findActivityType(new Integer(selectedID));
           if (activityType==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           }
       }
    }

    public String saveAction() {
        messageDup = "";
        if(checkCode(activityType)) {
            ActivityTypeDAO dao = getActivityTypeDAO();
            try {
                if (getMode().equals("add")) {
                    activityType.setId(null);
                    activityType.setEnable(true);
                    dao.create(activityType);
                } else {
                    dao.edit(activityType);
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
    
    public Boolean checkCode(ActivityType activityType) {
        String code = activityType.getCode();
        Integer id=0; 
        if(activityType.getId() != null)
            id = activityType.getId();
       ActivityTypeDAO dao = getActivityTypeDAO();
        
        Integer cnt = dao.checkActivityTypeCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public boolean isSavePermitted() {
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
  	if (selectedID==null || selectedID.isEmpty()) {
           return SecurityUtil.isPermitted("admin:activitytype:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:activitytype:edit"); 
       }
    } 

    public String backAction() {
        return SUCCESS;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
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

    public ActivityTypeDAO getActivityTypeDAO() {
        return activityTypeDAO;
    }

    public void setActivityTypeDAO(ActivityTypeDAO activityTypeDAO) {
        this.activityTypeDAO = activityTypeDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

}
