package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CaseTopicDAO;
import com.maxelyz.core.model.dao.CaseTypeDAO;
import com.maxelyz.core.model.entity.CaseTopic;
import com.maxelyz.core.model.entity.CaseType;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class CaseTopicEditController {

    private static Logger log = Logger.getLogger(CaseTopicEditController.class);
    private static String REDIRECT_PAGE = "casetopic.jsf";
    private static String SUCCESS = "casetopic.xhtml?faces-redirect=true";
    private static String FAILURE = "casetopicedit.xhtml";
    private CaseTopic caseTopic;
    private String mode;
    private String message;
    private String messageDup;
    private Integer caseTypeId;
    @ManagedProperty(value = "#{caseTopicDAO}")
    private CaseTopicDAO caseTopicDAO;
    @ManagedProperty(value = "#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;

    @PostConstruct
    public void initialize() {
        
        if (!SecurityUtil.isPermitted("admin:casetopic:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            caseTopic = new CaseTopic();
            caseTopic.setEnable(Boolean.TRUE);
            caseTopic.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            CaseTopicDAO dao = this.getCaseTopicDAO();
            caseTopic = dao.findCaseTopic(new Integer(selectedID));
            if (caseTopic==null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            } else {
                try {
                    caseTypeId = caseTopic.getCaseTypeId().getId();
                } catch (NullPointerException e) {
                    caseTypeId = 0;
                }
            }
        }
    }

      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:casetopic:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:casetopic:edit"); 
       }
    }  
        
    public String saveAction() {
        messageDup = "";
        if(checkCode(caseTopic)) {
            CaseTopicDAO dao = getCaseTopicDAO();
            try {
                CaseType caseType = new CaseType();
                caseType.setId(caseTypeId);
                caseTopic.setCaseTypeId(caseType);

                if (getMode().equals("add")) {
                    caseTopic.setId(null);
                    caseTopic.setEnable(true);
                    dao.create(caseTopic);
                } else {
                    dao.edit(caseTopic);
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
    
    public Boolean checkCode(CaseTopic caseTopic) {
        String code = caseTopic.getCode();
        Integer id=0; 
        if(caseTopic.getId() != null)
            id = caseTopic.getId();
        CaseTopicDAO dao = getCaseTopicDAO();
        
        Integer cnt = dao.checkCaseTopicCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }

    public String backAction() {
        return SUCCESS;
    }

    public CaseTopic getCaseTopic() {
        return caseTopic;
    }

    public void setCaseTopic(CaseTopic caseTopic) {
        this.caseTopic = caseTopic;
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

    public Integer getCaseTypeId() {
        return caseTypeId;
    }

    public void setCaseTypeId(Integer caseTypeId) {
        this.caseTypeId = caseTypeId;
    }

    public Map<String, Integer> getCaseTypes() {
        CaseTypeDAO dao = getCaseTypeDAO();
        List<CaseType> caseTypes = dao.findCaseTypeStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseType obj : caseTypes) {
            values.put(obj.getName(), obj.getId());
        }
        return values;

    }
    //Managed Properties

    public CaseTopicDAO getCaseTopicDAO() {
        return caseTopicDAO;
    }

    public void setCaseTopicDAO(CaseTopicDAO caseTopicDAO) {
        this.caseTopicDAO = caseTopicDAO;
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
