
package com.maxelyz.core.controller.front.customerHandling;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.AuditLogDAO;
import com.maxelyz.core.model.entity.AuditLog;
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
public class CustomerAuditLogController implements Serializable{
    private static Logger log = Logger.getLogger(CustomerAuditLogController.class);
    private static String REFRESH = "customerauditlog.xhtml?faces-redirect=true";
    private static String SEARCHRESULT = "customerauditlog.xhtml";
 
    private String userName;
    private String category;
    private Integer customerId;
    private Date auditDateFrom;
    private Date auditDateTo;
    private List<AuditLog> auditLogs;
    //private AuditLog auditLog;  
      
    @ManagedProperty(value="#{auditLogDAO}")
    private AuditLogDAO customerAuditLogDAO;
    
    @PostConstruct
    public void initialize() {
//        AuditLogDAO dao = getCustomerAuditLogDAO();
//        auditLogs = dao.findAccessLogEntities();
        searchAudit();
    }

    public void searchAudit(){
        customerId = JSFUtil.getUserSession().getCustomerDetail().getId();
        auditLogs = this.getCustomerAuditLogDAO().findCustomerLogEntities(auditDateFrom, auditDateTo, category, userName, customerId);
    }
            
    public List<AuditLog> getList() {
        return getAuditLogs();
    }
    
    public String searchAction() {
        AuditLogDAO dao = getCustomerAuditLogDAO();
        auditLogs = dao.findUsersByName(userName);
        return REFRESH;
    }

    public void popupSearchAction() {
        AuditLogDAO dao = getCustomerAuditLogDAO();
        auditLogs = dao.findUsersByName(userName);
    }
 
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
    
    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }

    public AuditLogDAO getCustomerAuditLogDAO() {
        return customerAuditLogDAO;
    }

    public void setCustomerAuditLogDAO(AuditLogDAO customerAuditLogDAO) {
        this.customerAuditLogDAO = customerAuditLogDAO;
    }
}
