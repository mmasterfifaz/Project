/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.AuditLogDAO;
import com.maxelyz.core.model.entity.AuditLog;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import com.maxelyz.core.service.SecurityServiceImpl;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
/**
 *
 * @author vee
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class AuditLogController implements Serializable{
    private static Logger log = Logger.getLogger(AuditLogController.class);
    private static String REFRESH = "auditlog.xhtml?faces-redirect=true";
    private static String SEARCHRESULT = "auditlog.xhtml"; 
 
    private String userName;
    private String customerName;
    private String category;
    private Date auditDateFrom;
    private Date auditDateTo;
    private List<AuditLog> auditLogs;
//    private AuditLog auditLog;  
      
    @ManagedProperty(value="#{auditLogDAO}")
    private AuditLogDAO auditLogDAO;

    @PostConstruct
    public void initialize() {
        AuditLogDAO dao = getAuditLogDAO();
        auditLogs = dao.findAccessLogEntities();
    }
        
    public void searchAudit(){
        auditLogs = this.getAuditLogDAO().findAccessLogEntities(auditDateFrom, auditDateTo, category, userName, customerName);
    }
        
    public List<AuditLog> getList() {
        return getAuditLogs();
    }
    
//    public String searchAction() {
//        AuditLogDAO dao = getAuditLogDAO();
//        auditLogs = dao.findUsersByName(userName);
//        return REFRESH;
//    }

//    public void popupSearchAction() {
//        AuditLogDAO dao = getAuditLogDAO();
//        auditLogs = dao.findUsersByName(userName);
//    }
 
    public Date getAuditDateFrom() {
        return auditDateFrom;
    }

    public void setAuditDateFrom(Date auditDateFrom) {
        this.auditDateFrom = auditDateFrom;
    }

    public Date getAuditDateTo() {
        return auditDateTo;
    }

    public void setAuditDateTo(Date auditDateTo) {
        this.auditDateTo = auditDateTo;
    }
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }

    public AuditLogDAO getAuditLogDAO() {
        return auditLogDAO;
    }

    public void setAuditLogDAO(AuditLogDAO auditLogDAO) {
        this.auditLogDAO = auditLogDAO;
    }
    
}
