/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ServiceTypeDAO;
import com.maxelyz.core.model.entity.ServiceType;
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
public class ServiceTypeController {
    
    private static Logger log = Logger.getLogger(ServiceTypeController.class);
    private static String REFRESH = "servicetype.xhtml?faces-redirect=true";
    private static String EDIT = "servicetypeedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<ServiceType> serviceTypeList;
    private ServiceType serviceType;
    
    @ManagedProperty(value = "#{serviceTypeDAO}")
    private ServiceTypeDAO serviceTypeDAO;
    
    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:servicetype:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        ServiceTypeDAO dao = getServiceTypeDAO();
        serviceTypeList = dao.findServiceTypeEntities();
        
    }

    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:servicetype:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:servicetype:delete");
    }

    public List<ServiceType> getList() {
        return getServiceTypeList();
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        ServiceTypeDAO dao = getServiceTypeDAO();
        String username = JSFUtil.getUserSession().getUserName();
        Date now = new Date();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    serviceType = dao.findServiceType(item.getKey());
                    serviceType.setEnable(false);
                    serviceType.setUpdateBy(username);
                    serviceType.setUpdateDate(now);
                    dao.edit(serviceType);
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

    public List<ServiceType> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(List<ServiceType> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }

    public ServiceTypeDAO getServiceTypeDAO() {
        return serviceTypeDAO;
    }

    public void setServiceTypeDAO(ServiceTypeDAO serviceTypeDAO) {
        this.serviceTypeDAO = serviceTypeDAO;
    }
}
