package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CallsuccessReasonDAO;
import com.maxelyz.core.model.entity.CallsuccessReason;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class CallSuccessEditController {
    private static Logger log = Logger.getLogger(CallSuccessEditController.class);
    private static String REDIRECT_PAGE = "callsuccess.jsf";
    private static String SUCCESS = "callsuccess.xhtml?faces-redirect=true";
    private static String FAILURE = "callsuccessedit.xhtml";
    private CallsuccessReason callsuccessReason;
    private String mode;
    private String message="";

    @ManagedProperty(value="#{callsuccessReasonDAO}")
    private CallsuccessReasonDAO callsuccessReasonDAO;

    @PostConstruct
    public void initialize() {
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           callsuccessReason = new CallsuccessReason();
           callsuccessReason.setEnable(Boolean.TRUE);
           callsuccessReason.setStatus(Boolean.TRUE);
       } else {
           mode = "edit";
           CallsuccessReasonDAO dao = getCallsuccessReasonDAO();
           callsuccessReason = dao.findCallsuccessReason(new Integer(selectedID));
           if (callsuccessReason==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           }
       }
    }

    public String saveAction() {
        CallsuccessReasonDAO dao = getCallsuccessReasonDAO();
        try {
            if (getMode().equals("add")) {
                callsuccessReason.setId(null);
                dao.create(callsuccessReason);
            } else {
                dao.edit(callsuccessReason);
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

    public CallsuccessReason getCallsuccessReason() {
        return callsuccessReason;
    }

    public void setCallsuccessReason(CallsuccessReason callsuccessReason) {
        this.callsuccessReason = callsuccessReason;
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

    public CallsuccessReasonDAO getCallsuccessReasonDAO() {
        return callsuccessReasonDAO;
    }

    public void setCallsuccessReasonDAO(CallsuccessReasonDAO callsuccessReasonDAO) {
        this.callsuccessReasonDAO = callsuccessReasonDAO;
    }
    
}
