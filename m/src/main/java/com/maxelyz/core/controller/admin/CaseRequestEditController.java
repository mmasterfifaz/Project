package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CaseRequestDAO;
import com.maxelyz.core.model.dao.CaseDetailDAO;
import com.maxelyz.core.model.dao.CaseTopicDAO;
import com.maxelyz.core.model.dao.CaseTypeDAO;
import com.maxelyz.core.model.entity.CaseRequest;
import com.maxelyz.core.model.entity.CaseDetail;
import com.maxelyz.core.model.entity.CaseTopic;
import com.maxelyz.core.model.entity.CaseType;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.bean.*;
import javax.faces.event.ValueChangeEvent;
import javax.faces.bean.ViewScoped;

@ManagedBean (name="caseRequestEditController")
//@RequestScoped
@ViewScoped
public class CaseRequestEditController implements Serializable {

    private static Logger log = Logger.getLogger(CaseRequestEditController.class);
    private static String REDIRECT_PAGE = "caserequest.jsf";
    private static String SUCCESS = "caserequest.xhtml?faces-redirect=true";
    private static String FAILURE = "caserequestedit.xhtml";
    private CaseRequest caseRequest;
    private String mode;
    private String message;
    private String messageDup;
    private Integer caseTypeId;
    private Integer caseTopicId;
    private Integer caseDetailId;
    private Map<String, Integer> caseTopicsList;
    private Map<String, Integer> caseDetailsList;
    @ManagedProperty(value = "#{caseRequestDAO}")
    private CaseRequestDAO caseRequestDAO;
    @ManagedProperty(value = "#{caseDetailDAO}")
    private CaseDetailDAO caseDetailDAO;
    @ManagedProperty(value = "#{caseTopicDAO}")
    private CaseTopicDAO caseTopicDAO;
    @ManagedProperty(value = "#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;

    @PostConstruct
    public void initialize() {
         if (!SecurityUtil.isPermitted("admin:caserequest:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
         
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            caseRequest = new CaseRequest();
            caseRequest.setEnable(Boolean.TRUE);
            caseRequest.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            CaseRequestDAO dao = getCaseRequestDAO();
            caseRequest = dao.findCaseRequest(new Integer(selectedID));
            if (caseRequest==null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            } else {
                try {
                    caseDetailId = caseRequest.getCaseDetailId().getId();
                    caseTopicId = caseRequest.getCaseDetailId().getCaseTopicId().getId();
                    caseTypeId = caseRequest.getCaseDetailId().getCaseTopicId().getCaseTypeId().getId();
                    this.setCaseTopics(caseTypeId);
                    this.setCaseDetails(caseTopicId);
                } catch (NullPointerException e) {
                    caseDetailId = 0;
                    caseTopicId = 0;
                    caseTypeId = 0;
                }
            }
        }
    }

      public boolean isSavePermitted() {
   	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:caserequest:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:caserequest:edit"); 
       }
    }  
        
    public String saveAction() {
        messageDup = "";
        if(checkCode(caseRequest)) {
            CaseRequestDAO dao = getCaseRequestDAO();
            try {
                CaseDetail caseDetail = new CaseDetail();
                caseDetail.setId(caseDetailId);
                caseRequest.setCaseDetailId(caseDetail);
                if (getMode().equals("add")) {
                    caseRequest.setId(null);
                    caseRequest.setEnable(true);
                    dao.create(caseRequest);
                } else {
                    dao.edit(caseRequest);
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

    public Boolean checkCode(CaseRequest caseRequest) {
        String code = caseRequest.getCode();
        Integer id=0; 
        if(caseRequest.getId() != null)
            id = caseRequest.getId();
       CaseRequestDAO dao = getCaseRequestDAO();
        
        Integer cnt = dao.checkCaseRequestCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
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

    public void setCaseTopics(Integer caseTypeId) {
        CaseTopicDAO dao = getCaseTopicDAO();
        List<CaseTopic> caseTopics = dao.findCaseTopicByCaseTypeStatus(caseTypeId);
        // List<CaseTopic> caseTopics = dao.findCaseTopicEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseTopic obj : caseTopics) {
            values.put(obj.getName(), obj.getId());
        }
        caseTopicsList = values;
    }

    public Map<String, Integer> getCaseTopics() {
        return caseTopicsList;
    }

    public void setCaseDetails(Integer caseTopicId) {
        CaseDetailDAO dao = getCaseDetailDAO();
        List<CaseDetail> caseDetails = dao.findAvailableCaseDetailByCaseTopic(caseTopicId);
        //List<CaseDetail> caseDetails = dao.findCaseDetailEntities();

        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseDetail obj : caseDetails) {
            values.put(obj.getName(), obj.getId());
        }
        caseDetailsList = values;
    }

    public Map<String, Integer> getCaseDetails() {
        return caseDetailsList;
    }

    //Listener
    public void caseTypeListener(ValueChangeEvent event) {
        caseTypeId = (Integer) event.getNewValue();
        caseTopicId = null;
        caseDetailId = null;
        if(caseDetailsList != null && !caseDetailsList.isEmpty()) {
            caseDetailsList.clear();
        }
        setCaseTopics(caseTypeId);
        FacesContext.getCurrentInstance().renderResponse();

    }

    public void caseTopicListener(ValueChangeEvent event) {
        caseTopicId = (Integer) event.getNewValue();
        setCaseDetails(caseTopicId);
        FacesContext.getCurrentInstance().renderResponse();

    }

    //Backing
    public CaseRequest getCaseRequest() {
        return caseRequest;
    }

    public void setCaseRequest(CaseRequest caseRequest) {
        this.caseRequest = caseRequest;
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

    public Integer getCaseTopicId() {
        return caseTopicId;
    }

    public void setCaseTopicId(Integer caseTopicId) {
        this.caseTopicId = caseTopicId;
    }

    public Integer getCaseDetailId() {
        return caseDetailId;
    }

    public void setCaseDetailId(Integer caseDetailId) {
        this.caseDetailId = caseDetailId;
    }

    //Managed Properties
    public CaseRequestDAO getCaseRequestDAO() {
        return caseRequestDAO;
    }

    public void setCaseRequestDAO(CaseRequestDAO caseRequestDAO) {
        this.caseRequestDAO = caseRequestDAO;
    }

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

    public CaseDetailDAO getCaseDetailDAO() {
        return caseDetailDAO;
    }

    public void setCaseDetailDAO(CaseDetailDAO caseDetailDAO) {
        this.caseDetailDAO = caseDetailDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
    
}
