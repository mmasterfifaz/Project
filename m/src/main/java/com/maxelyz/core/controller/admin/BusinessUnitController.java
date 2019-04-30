/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.BusinessUnitDAO;
import com.maxelyz.core.model.entity.BusinessUnit;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class BusinessUnitController {
        
    private static Logger log = Logger.getLogger(BusinessUnitController.class);
    private static String REFRESH = "businessunit.xhtml?faces-redirect=true";
    private static String EDIT = "businessunitedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<BusinessUnit> businessUnitList;
    private BusinessUnit businessUnit;
    
    @ManagedProperty(value = "#{businessUnitDAO}")
    private BusinessUnitDAO businessUnitDAO;
    
    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:businessunit:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        BusinessUnitDAO dao = getBusinessUnitDAO();
        businessUnitList = dao.findBusinessUnitEntities();
        
    }

    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:businessunit:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:businessunit:delete");
    }

    public List<BusinessUnit> getList() {
        return getBusinessUnitList();
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        BusinessUnitDAO dao = getBusinessUnitDAO();
        String username = JSFUtil.getUserSession().getUserName();
        Date now = new Date();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    businessUnit = dao.findBusinessUnit(item.getKey());
                    businessUnit.setEnable(false);
                    businessUnit.setUpdateBy(username);
                    businessUnit.setUpdateDate(now);
                    dao.edit(businessUnit);
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

    public List<BusinessUnit> getBusinessUnitList() {
        return businessUnitList;
    }

    public void setBusinessUnitList(List<BusinessUnit> businessUnitList) {
        this.businessUnitList = businessUnitList;
    }

    public BusinessUnitDAO getBusinessUnitDAO() {
        return businessUnitDAO;
    }

    public void setBusinessUnitDAO(BusinessUnitDAO businessUnitDAO) {
        this.businessUnitDAO = businessUnitDAO;
    }
}
