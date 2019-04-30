/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.model.dao.AuditLogDAO;
import com.maxelyz.core.model.dao.CustomerAuditLogDAO;
import com.maxelyz.core.model.entity.CustomerAuditLog;
import com.maxelyz.utils.JSFUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author ukritj
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class CustomerLogController {
    private static Logger log = Logger.getLogger(CustomerLogController.class); 
    
    private List<CustomerAuditLog> list;
    private Date fromDate;
    private Date toDate;
    
    @ManagedProperty(value="#{customerAuditLogDAO}")
    private CustomerAuditLogDAO customerAuditLogDAO;
    
    @PostConstruct
    public void initialize() {
        fromDate = new Date();
        toDate = new Date();

    }
    
    public void searchActionListener(){
        list = customerAuditLogDAO.findCustomerAuditLogList(JSFUtil.getUserSession().getCustomerDetail().getId(), fromDate, toDate);
    }

    public CustomerAuditLogDAO getCustomerAuditLogDAO() {
        return customerAuditLogDAO;
    }

    public void setCustomerAuditLogDAO(CustomerAuditLogDAO customerAuditLogDAO) {
        this.customerAuditLogDAO = customerAuditLogDAO;
    }

    public List<CustomerAuditLog> getList() {
        return list;
    }

    public void setList(List<CustomerAuditLog> list) {
        this.list = list;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
    
    
}
