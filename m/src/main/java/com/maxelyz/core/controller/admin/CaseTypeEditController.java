package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CaseTypeDAO;
import com.maxelyz.core.model.entity.CaseType;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class CaseTypeEditController {
    private static Logger log = Logger.getLogger(CaseTypeEditController.class);
    private static String REDIRECT_PAGE = "casetype.jsf";
    private static String SUCCESS = "casetype.xhtml?faces-redirect=true";
    private static String FAILURE = "casetypeedit.xhtml";

    private CaseType caseType;
    private String mode;
    private String message;
    private String messageDup;

    @ManagedProperty(value="#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:casetype:view")) 
        {
            SecurityUtil.redirectUnauthorize();  
        }
        
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           caseType = new CaseType();
           caseType.setEnable(Boolean.TRUE);
           caseType.setStatus(Boolean.TRUE);
        } else {
           mode = "edit";
           CaseTypeDAO dao = getCaseTypeDAO();
           caseType = dao.findCaseType(new Integer(selectedID));
           if (caseType==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           }
       }
    }


      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:casetype:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:casetype:edit"); 
       }
    } 
        
    public String saveAction() {
        messageDup = "";
        if(checkCode(caseType)) {
            CaseTypeDAO dao = getCaseTypeDAO();
            try {
                if (getMode().equals("add")) {
                    caseType.setId(null);
                    caseType.setEnable(true);
                    dao.create(caseType);
                } else {
                    dao.edit(caseType);
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

    public Boolean checkCode(CaseType caseType) {
        String code = caseType.getCode();
        Integer id=0; 
        if(caseType.getId() != null)
            id = caseType.getId();
        CaseTypeDAO dao = getCaseTypeDAO();
        
        Integer cnt = dao.checkCaseTypeCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
   
    public String backAction() {
        return SUCCESS;
    }

    public CaseType getCaseType() {
        return caseType;
    }

    public void setCaseType(CaseType caseType) {
        this.caseType = caseType;
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

    public CaseTypeDAO getCaseTypeDAO() {
        return caseTypeDAO;
    }

    public void setCaseTypeDAO(CaseTypeDAO caseTypeDAO) {
        this.caseTypeDAO = caseTypeDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
    

}
