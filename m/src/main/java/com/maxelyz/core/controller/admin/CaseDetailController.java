package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CaseDetailDAO;
import com.maxelyz.core.model.dao.CaseTopicDAO;
import com.maxelyz.core.model.dao.CaseTypeDAO;
import com.maxelyz.core.model.entity.CaseDetail;
import com.maxelyz.core.model.entity.CaseTopic;
import com.maxelyz.utils.JSFUtil;
//import com.maxelyz.core.service.SecurityService;
//import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.bean.ViewScoped;

@ManagedBean
//@RequestScoped
@ViewScoped
public class CaseDetailController implements Serializable{
    private static Logger log = Logger.getLogger(CaseDetailController.class);
    private static String REFRESH = "casedetail.xhtml?faces-redirect=true";
    private static String EDIT = "casedetailedit.xhtml";
  
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<CaseDetail> caseDetails;
    private Map<String, Integer> caseTypeList;
    private Map<String, Integer> caseTopicList;
    private CaseDetail caseDetail;
    private Integer caseTypeId;
    private Integer caseTopicId;
    
    @ManagedProperty(value="#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;
    @ManagedProperty(value="#{caseTopicDAO}")
    private CaseTopicDAO caseTopicDAO;
    @ManagedProperty(value="#{caseDetailDAO}")
    private CaseDetailDAO caseDetailDAO;


    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:casedetail:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        CaseDetailDAO dao = getCaseDetailDAO();
        caseDetails = dao.findCaseDetailEntities();
        caseTypeList = this.getCaseTypeDAO().getCaseTypeList();
        
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:casedetail:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:casedetail:delete");
    }
    
    public List<CaseDetail> getList() {
        return getCaseDetails();
    }

     public String editAction() {
       return EDIT;
    }

    public String deleteAction() throws Exception {
        CaseDetailDAO dao = getCaseDetailDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    caseDetail = dao.findCaseDetail(item.getKey());
                    caseDetail.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    caseDetail.setUpdateDate(new Date());
                    caseDetail.setEnable(false);
                    dao.edit(caseDetail);
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

    public List<CaseDetail> getCaseDetails() {
        return caseDetails;
    }

    public void setCaseDetails(List<CaseDetail> CaseDetails) {
        this.caseDetails = CaseDetails;
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
        setCaseTopics(caseTypeId);
        if (caseTypeId==null || caseTypeId==0) {
            caseDetails = this.getCaseDetailDAO().findCaseDetailEntities();
        } else {
            caseDetails = this.getCaseDetailDAO().findCaseDetailByCaseType(caseTypeId);
        }
    }

    public void caseTopicListener(ValueChangeEvent event) {
        caseTopicId = (Integer) event.getNewValue();
         if (caseTopicId!=null && caseTopicId!=0) {
            caseDetails = this.getCaseDetailDAO().findCaseDetailByCaseTopicStatus(caseTopicId);
        } if (caseTypeId==null || caseTypeId==0) {
            caseDetails = this.getCaseDetailDAO().findCaseDetailEntitiesByStatus();
        }
    }

    public void searchActionListener(ActionEvent event) {
         if (caseTopicId!=null && caseTopicId!=0) {
            caseDetails = this.getCaseDetailDAO().findCaseDetailByCaseTopic(caseTopicId);
        } else {
            caseDetails = this.getCaseDetailDAO().findCaseDetailEntities();
        }
    }

    public void setCaseTopics(Integer caseTypeId) {
        CaseTopicDAO dao = getCaseTopicDAO();
        List<CaseTopic> caseTopics = dao.findCaseTopicByCaseTypeStatus(caseTypeId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseTopic obj : caseTopics) {
            values.put(obj.getName(), obj.getId());
        }
        caseTopicList = values;
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
