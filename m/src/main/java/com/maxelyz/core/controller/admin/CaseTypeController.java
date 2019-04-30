package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CaseTypeDAO;
import com.maxelyz.core.model.entity.CaseType;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class CaseTypeController implements Serializable {

    private static Logger log = Logger.getLogger(CaseTypeController.class);
    private static String REFRESH = "casetype.xhtml?faces-redirect=true";
    private static String EDIT = "casetypeedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<CaseType> caseTypes;
    private CaseType caseType;
    @ManagedProperty(value = "#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:casetype:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        CaseTypeDAO dao = getCaseTypeDAO();
        caseTypes = dao.findCaseTypeEntities();
        
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:casetype:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:casetype:delete");
    }

    public List<CaseType> getList() {
        return getCaseTypes();
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        CaseTypeDAO dao = getCaseTypeDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    caseType = dao.findCaseType(item.getKey());
                    caseType.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    caseType.setUpdateDate(new Date());
                    caseType.setEnable(false);
                    dao.edit(caseType);
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

    public List<CaseType> getCaseTypes() {
        return caseTypes;
    }

    public void setCaseTypes(List<CaseType> CaseTypes) {
        this.caseTypes = CaseTypes;
    }

    public CaseTypeDAO getCaseTypeDAO() {
        return caseTypeDAO;
    }

    public void setCaseTypeDAO(CaseTypeDAO caseTypeDAO) {
        this.caseTypeDAO = caseTypeDAO;
    }
}
