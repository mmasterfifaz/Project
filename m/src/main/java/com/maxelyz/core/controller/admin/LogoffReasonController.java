/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.social.model.dao.LogoffTypeDAO;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.LogoffType;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class LogoffReasonController {
        
    private static Logger log = Logger.getLogger(LogoffReasonController.class);
    private static String REFRESH = "logoffreason.xhtml?faces-redirect=true";
    private static String EDIT = "logoffreasonedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<LogoffType> logoffTypeList;
    private LogoffType logoffType;
    @ManagedProperty(value = "#{logoffTypeDAO}")
    private LogoffTypeDAO logoffTypeDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:logoffReason:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        logoffTypeList = logoffTypeDAO.findLogoffTypeEntities();        
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:logoffReason:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:logoffReason:delete");
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    logoffType =  logoffTypeDAO.findLogoffType(item.getKey());
                    logoffType.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    logoffType.setUpdateDate(new Date());
                    logoffType.setEnable(false);
                    logoffTypeDAO.edit(logoffType);
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

    public List<LogoffType> getLogoffTypeList() {
        return logoffTypeList;
    }

    public void setLogoffTypeList(List<LogoffType> logoffTypeList) {
        this.logoffTypeList = logoffTypeList;
    }

    public LogoffType getLogoffType() {
        return logoffType;
    }

    public void setLogoffType(LogoffType logoffType) {
        this.logoffType = logoffType;
    }

    public LogoffTypeDAO getLogoffTypeDAO() {
        return logoffTypeDAO;
    }

    public void setLogoffTypeDAO(LogoffTypeDAO logoffTypeDAO) {
        this.logoffTypeDAO = logoffTypeDAO;
    }    
}
