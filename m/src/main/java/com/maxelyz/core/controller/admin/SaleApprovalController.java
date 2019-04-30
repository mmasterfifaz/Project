package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.value.admin.SaleApprovalValue;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;

@ManagedBean
//@RequestScoped
@ViewScoped
public class SaleApprovalController implements Serializable{
    private static Logger log = Logger.getLogger(SaleApprovalController.class);
    private static String REFRESH = "saleapproval.xhtml?faces-redirect=true";
    private static String EDIT = "saleapprovaledit.xhtml";
 
    private List<SaleApprovalValue> saleApprovalValues;
    private Date fromDate, toDate;
    private String tmrName;
    private String approvalStatus;
    private String salesResult;
   
    @ManagedProperty(value="#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;

    @PostConstruct
    public void initialize() {
        System.out.println("Sale Approval Initial ");
        if (!SecurityUtil.isPermitted("admin:salesapproval:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        Date today = new Date();
        fromDate = today;
        toDate = today;
        tmrName="";
        approvalStatus="all";
        salesResult = "Y";
        this.searchActionListener(null);
    }

    public List<SaleApprovalValue> getList() {
        return saleApprovalValues;
    }

    public String editAction() {
       return EDIT;
    }

    public void searchActionListener(ActionEvent event) {       
        saleApprovalValues = this.purchaseOrderDAO.findPurchaseOrderBySaleApproval(fromDate, toDate, tmrName, approvalStatus, salesResult);
    }

    //getter/setter
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

    public String getTmrName() {
        return tmrName;
    }

    public void setTmrName(String tmrName) {
        this.tmrName = tmrName;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getSalesResult() {
        return salesResult;
    }

    public void setSalesResult(String salesResult) {
        this.salesResult = salesResult;
    }
    
    //Managed Propterties
    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

  
}
