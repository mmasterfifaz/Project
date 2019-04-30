package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.CaseDetailDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CaseRequestDAO;
import com.maxelyz.core.model.dao.CaseTopicDAO;
import com.maxelyz.core.model.dao.CaseTypeDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.CaseDetail;
import com.maxelyz.core.model.entity.CaseRequest;
import com.maxelyz.core.model.entity.CaseTopic;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

@ManagedBean
@ViewScoped
//@RequestScoped
public class CaseRequestController implements Serializable {

    private static Logger log = Logger.getLogger(CaseRequestController.class);
    private static String REFRESH = "caserequest.xhtml?faces-redirect=true";
    private static String EDIT = "caserequestedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<CaseRequest> caseRequests;
    private CaseRequest caseRequest;
    
    //search
    private Map<String, Integer> caseTypeList;
    private Map<String, Integer> caseTopicList;
    private Map<String, Integer> caseDetailList;
    private Integer caseTypeId;
    private Integer caseTopicId;
    private Integer caseDetailId;
    
    @ManagedProperty(value="#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;
    @ManagedProperty(value="#{caseTopicDAO}")
    private CaseTopicDAO caseTopicDAO;
    @ManagedProperty(value="#{caseDetailDAO}")
    private CaseDetailDAO caseDetailDAO;
    @ManagedProperty(value = "#{caseRequestDAO}")
    private CaseRequestDAO caseRequestDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:caserequest:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        CaseRequestDAO dao = getCaseRequestDAO();
        caseRequests = dao.findCaseRequestEntities();
        caseTypeList = this.getCaseTypeDAO().getCaseTypeList();
    }
    
        public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:caserequest:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:caserequest:delete");
    }
    
    public List<CaseRequest> getList() {
        return getCaseRequests();
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() throws Exception {
        CaseRequestDAO dao = getCaseRequestDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    caseRequest = dao.findCaseRequest(item.getKey());
                    caseRequest.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    caseRequest.setUpdateDate(new Date());
                    caseRequest.setEnable(false);
                    dao.edit(caseRequest);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<CaseRequest> getCaseRequests() {
        return caseRequests;
    }

    public void setCaseRequests(List<CaseRequest> CaseRequests) {
        this.caseRequests = CaseRequests;
    }
    
    public Integer getCaseTopicId() {
        return caseTopicId;
    }

    public void setCaseTopicId(Integer caseTopicId) {
        this.caseTopicId = caseTopicId;
    }

    public Integer getCaseTypeId() {
        return caseTypeId;
    }

    public void setCaseTypeId(Integer caseTypeId) {
        this.caseTypeId = caseTypeId;
    }

    public Map<String, Integer> getCaseTypeList() {
        return caseTypeList;
    }

    public Map<String, Integer> getCaseTopicList() {
        return caseTopicList;
    }

    public void caseTypeListener(ValueChangeEvent event) {
        caseTypeId = (Integer) event.getNewValue();
        if(caseDetailList != null) {
            caseDetailId = 0;
            caseDetailList.clear();
        }
        setCaseTopics(caseTypeId);
        if (caseTypeId==null || caseTypeId==0) {
            caseRequests = this.getCaseRequestDAO().findCaseRequestEntities();
        } else {
            caseRequests = this.getCaseRequestDAO().findCaseRequestByCaseType(caseTypeId);
        }
    }

    public void caseTopicListener(ValueChangeEvent event) {
        caseTopicId = (Integer) event.getNewValue();
        setCaseDetails(caseTopicId);
         if (caseTopicId != null && caseTopicId != 0) {
             caseRequests = this.getCaseRequestDAO().findCaseRequestByCaseTopic(caseTopicId);
        } else if (caseTypeId != null && caseTypeId != 0) {
                    caseRequests = this.getCaseRequestDAO().findCaseRequestByCaseType(caseTypeId);
                }
    }

    public void caseDetailListener(ValueChangeEvent event) {
        caseDetailId = (Integer) event.getNewValue();
         if (caseDetailId != null && caseDetailId != 0) {
             caseRequests = this.getCaseRequestDAO().findCaseRequestByCaseDetailId(caseDetailId);
        } else if (caseTopicId != null && caseTopicId != 0) {
                    caseRequests = this.getCaseRequestDAO().findCaseRequestByCaseTopic(caseTopicId);
                }
    }
        
//    public void searchActionListener(ActionEvent event) {
//         if (caseTopicId!=null && caseTopicId!=0) {
////            caseDetails = this.getCaseDetailDAO().findCaseDetailByCaseTopic(caseTopicId);
//        } else {
////            caseDetails = this.getCaseDetailDAO().findCaseDetailEntities();
//        }
//    }

    public void setCaseTopics(Integer caseTypeId) {
        CaseTopicDAO dao = getCaseTopicDAO();
        List<CaseTopic> caseTopics = dao.findCaseTopicByCaseTypeStatus(caseTypeId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseTopic obj : caseTopics) {
            values.put(obj.getName(), obj.getId());
        }
        caseTopicList = values;
    }

    public void setCaseDetails(Integer caseTopicId) {
        CaseDetailDAO dao = getCaseDetailDAO();
        List<CaseDetail> caseDetails = dao.findCaseDetailByCaseTopicStatus(caseTopicId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseDetail obj : caseDetails) {
            values.put(obj.getName(), obj.getId());
        }
        caseDetailList = values;
    }
        
    public Map<String, Integer> getCaseDetailList() {
        return caseDetailList;
    }

//    public void setCaseDetailList(Map<String, Integer> caseDetailList) {
//        this.caseDetailList = caseDetailList;
//    }

    public Integer getCaseDetailId() {
        return caseDetailId;
    }

    public void setCaseDetailId(Integer caseDetailId) {
        this.caseDetailId = caseDetailId;
    }

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
}
