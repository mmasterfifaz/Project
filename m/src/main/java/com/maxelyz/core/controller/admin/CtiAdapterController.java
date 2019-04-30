package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CtiAdapterDAO;
import com.maxelyz.core.model.entity.CtiAdapter;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class CtiAdapterController implements Serializable{
    private static Logger log = Logger.getLogger(CtiAdapterController.class);
    private static String REFRESH = "ctiadapter.xhtml?faces-redirect=true";
    private static String EDIT = "ctiadapteredit.xhtml";
     
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<CtiAdapter> ctiAdapters;
    private CtiAdapter ctiAdapter;
    
    @ManagedProperty(value="#{ctiAdapterDAO}")
    private CtiAdapterDAO ctiAdapterDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:ctiadapter:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        CtiAdapterDAO dao = getCtiAdapterDAO();
        ctiAdapters = dao.findCtiAdapterEntities();
        
    }

    public List<CtiAdapter> getList() {
        return getCtiAdapters();
    }

     public String editAction() {
       return EDIT;
    }

    public String deleteAction() throws Exception {
        CtiAdapterDAO dao = getCtiAdapterDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    ctiAdapter = dao.findCtiAdapter(item.getKey());
                    ctiAdapter.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    ctiAdapter.setUpdateDate(new Date());
                    ctiAdapter.setEnable(false);
                    dao.edit(ctiAdapter);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:ctiadapter:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:ctiadapter:delete");
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<CtiAdapter> getCtiAdapters() {
        return ctiAdapters;
    }

    public void setCtiAdapters(List<CtiAdapter> ctiAdapters) {
        this.ctiAdapters = ctiAdapters;
    }

    public CtiAdapter getCtiAdapter() {
        return ctiAdapter;
    }

    public void setCtiAdapter(CtiAdapter ctiAdapter) {
        this.ctiAdapter = ctiAdapter;
    }

    public CtiAdapterDAO getCtiAdapterDAO() {
        return ctiAdapterDAO;
    }

    public void setCtiAdapterDAO(CtiAdapterDAO ctiAdapterDAO) {
        this.ctiAdapterDAO = ctiAdapterDAO;
    }

}
