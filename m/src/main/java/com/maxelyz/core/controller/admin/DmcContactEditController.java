package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.DmccontactReasonDAO;
import com.maxelyz.core.model.entity.DmccontactReason;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class DmcContactEditController {
    private static Logger log = Logger.getLogger(DmcContactEditController.class);
    private static String REDIRECT_PAGE = "dmccontact.jsf";
    private static String SUCCESS = "dmccontact.xhtml?faces-redirect=true";
    private static String FAILURE = "dmccontactedit.xhtml";
    private DmccontactReason dmccontactReason;
    private String mode;
    private String message="";

    @ManagedProperty(value="#{dmccontactReasonDAO}")
    private DmccontactReasonDAO dmccontactReasonDAO;

    @PostConstruct
    public void initialize() {
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");
       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           dmccontactReason = new DmccontactReason();
           dmccontactReason.setEnable(Boolean.TRUE);
           dmccontactReason.setStatus(Boolean.TRUE);
       } else {
           mode = "edit";
           DmccontactReasonDAO dao = getDmccontactReasonDAO();
           dmccontactReason = dao.findDmccontactReason(new Integer(selectedID));
           if (dmccontactReason==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           }
       }
    }

    public String saveAction() {
        DmccontactReasonDAO dao = getDmccontactReasonDAO();
        try {
            if (getMode().equals("add")) {
                dmccontactReason.setId(null);
                dao.create(dmccontactReason);
            } else {
                dao.edit(dmccontactReason);
            }
        } catch (Exception e) {
            log.error(e);
            message = e.getMessage();
            return FAILURE;
        }
        return SUCCESS;
    }

    public String backAction() {
        return SUCCESS;
    }

    public DmccontactReason getDmccontactReason() {
        return dmccontactReason;
    }

    public void setDmccontactReason(DmccontactReason dmccontactReason) {
        this.dmccontactReason = dmccontactReason;
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

    public DmccontactReasonDAO getDmccontactReasonDAO() {
        return dmccontactReasonDAO;
    }

    public void setDmccontactReasonDAO(DmccontactReasonDAO dmccontactReasonDAO) {
        this.dmccontactReasonDAO = dmccontactReasonDAO;
    }
    
}
