package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CaseDetailDAO;
import com.maxelyz.core.model.dao.CaseTopicDAO;
import com.maxelyz.core.model.dao.CaseTypeDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.CaseDetail;
import com.maxelyz.core.model.entity.CaseTopic;
import com.maxelyz.core.model.entity.CaseType;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
//import javax.faces.context.FacesContext;
import javax.faces.bean.*;
//import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.bean.ViewScoped;

@ManagedBean (name="caseDetailEditController")
@RequestScoped
@ViewScoped
public class CaseDetailEditController implements Serializable {
    private static Logger log = Logger.getLogger(CaseDetailEditController.class);
    private static String REDIRECT_PAGE = "casedetail.jsf";
    private static String SUCCESS = "casedetail.xhtml?faces-redirect=true";
    private static String FAILURE = "casedetailedit.xhtml";
    private CaseDetail caseDetail;
    private String mode;
    private String message;
    private String messageDup;
    private Integer caseTypeId;
    private Integer caseTopicId;
    private Integer toUserId;

    private Map<String, Integer> caseTopicsList;
    @ManagedProperty(value = "#{caseDetailDAO}")
    private CaseDetailDAO caseDetailDAO;
    @ManagedProperty(value = "#{caseTopicDAO}")
    private CaseTopicDAO caseTopicDAO;
    @ManagedProperty(value = "#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:casedetail:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
            
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            caseDetail = new CaseDetail();
            caseDetail.setEnable(Boolean.TRUE);
            caseDetail.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            CaseDetailDAO dao = getCaseDetailDAO();
            caseDetail = dao.findCaseDetail(new Integer(selectedID));
            if (caseDetail==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
            } else {
                try {
                    caseTopicId = caseDetail.getCaseTopicId().getId();
                    caseTypeId = caseDetail.getCaseTopicId().getCaseTypeId().getId();
                    this.setCaseTopics(caseTypeId);
                    if (caseDetail.getToUsers()!=null) {
                        toUserId = caseDetail.getToUsers().getId();
                    }
                } catch (NullPointerException e) {
                    caseTopicId = 0;
                    caseTypeId = 0;
                }
            }
        }
    }
    

      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:casedetail:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:casedetail:edit"); 
       }
    }
    
    public String saveAction() {
        messageDup = "";
        if(checkCode(caseDetail)) {
            CaseDetailDAO dao = getCaseDetailDAO();
            try {
                CaseTopic caseTopic = new CaseTopic();
                caseTopic.setId(caseTopicId);
                caseDetail.setCaseTopicId(caseTopic);
                if (toUserId!=null && toUserId!=0) {
                    caseDetail.setToUsers(new Users(toUserId));
                } else {
                    caseDetail.setToUsers(null);
                }
                if (getMode().equals("add")) {
                    caseDetail.setId(null);
                    caseDetail.setEnable(true);
                    dao.create(caseDetail);
                } else {
                    dao.edit(caseDetail);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return null;
            }
            return SUCCESS;
        } else {
            messageDup = " Code is already taken";
            return null;  
        }
    }
    
    public Boolean checkCode(CaseDetail caseDetail) {
        String code = caseDetail.getCode();
        Integer id=0; 
        if(caseDetail.getId() != null)
            id = caseDetail.getId();
       CaseDetailDAO dao = getCaseDetailDAO();
        
        Integer cnt = dao.checkCaseDetailCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public CaseDetail getCaseDetail() {
        return caseDetail;
    }

    public void setCaseDetail(CaseDetail caseDetail) {
        this.caseDetail = caseDetail;
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

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Map<String, Integer> getUsersList() {
        return this.getUsersDAO().getUsersList();
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
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseTopic obj : caseTopics) {
            values.put(obj.getName(), obj.getId());
        }
        caseTopicsList = values;

    }

    public Map<String, Integer> getCaseTopics() {
        return caseTopicsList;       
    }

    public void toUserListener(ValueChangeEvent event) {
        toUserId = (Integer) event.getNewValue();
        if (toUserId != null && toUserId !=0 ) {
            Users u = this.getUsersDAO().findUsers(toUserId);
            caseDetail.setToUserName(u.getName());
            caseDetail.setToUserEmail(u.getEmail());
            caseDetail.setToUserTelephone(u.getMobile());
        }
    }

    public void caseTypeListener(ValueChangeEvent event) {
        caseTypeId = (Integer) event.getNewValue();
        setCaseTopics(caseTypeId);
    }

    //Managed Properties
    public CaseDetailDAO getCaseDetailDAO() {
        return caseDetailDAO;
    }

    public void setCaseDetailDAO(CaseDetailDAO caseDetailDAO) {
        this.caseDetailDAO = caseDetailDAO;
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

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
    
}
