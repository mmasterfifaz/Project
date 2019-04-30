package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CaseTopicDAO;
import com.maxelyz.core.model.entity.CaseTopic;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class CaseTopicController implements Serializable{
    private static Logger log = Logger.getLogger(CaseTopicController.class);
    private static String REFRESH = "casetopic.xhtml?faces-redirect=true";
    private static String EDIT = "casetopicedit.xhtml";
  
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<CaseTopic> caseTopics;
    private CaseTopic caseTopic;
    
    @ManagedProperty(value="#{caseTopicDAO}")
    private CaseTopicDAO caseTopicDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:casetopic:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        CaseTopicDAO dao = getCaseTopicDAO();
        caseTopics = dao.findCaseTopicEntities();
        
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:casetopic:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:casetopic:delete");
    }
    
    public List<CaseTopic> getList() {
        return getCaseTopics();
    }

     public String editAction() {
       return EDIT;
    }

    public String deleteAction() throws Exception {
        CaseTopicDAO dao = getCaseTopicDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    caseTopic = dao.findCaseTopic(item.getKey());
                    caseTopic.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    caseTopic.setUpdateDate(new Date());
                    caseTopic.setEnable(false);
                    dao.edit(caseTopic);
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

    public List<CaseTopic> getCaseTopics() {
        return caseTopics;
    }

    public void setCaseTopics(List<CaseTopic> CaseTopics) {
        this.caseTopics = CaseTopics;
    }

    public CaseTopicDAO getCaseTopicDAO() {
        return caseTopicDAO;
    }

    public void setCaseTopicDAO(CaseTopicDAO caseTopicDAO) {
        this.caseTopicDAO = caseTopicDAO;
    }
}
