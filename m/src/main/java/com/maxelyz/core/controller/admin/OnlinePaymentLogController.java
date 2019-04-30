package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.BrandDAO;
import com.maxelyz.core.model.dao.OnlinePaymentLogDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.Brand;
import com.maxelyz.core.model.entity.OnlinePaymentLog;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;

@ManagedBean
@RequestScoped
public class OnlinePaymentLogController {

    private static Logger log = Logger.getLogger(OnlinePaymentLogController.class);
    private static String REFRESH = "onlinepaymentlog.xhtml?faces-redirect=true";
    private List<OnlinePaymentLog> onlinePaymentLogs;
    private Date fromDate;
    private Date toDate;
    private String postParam;
    private String returnParam;
    private String createBy;
    
    @ManagedProperty(value = "#{onlinePaymentLogDAO}")
    private OnlinePaymentLogDAO onlinePaymentLogDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:onlinepaymentlog:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        fromDate = JSFUtil.toDateWithoutTime(new Date());
        toDate = JSFUtil.toDateWithMaxTime(new Date());
        postParam = "";
        returnParam = "";
        createBy = ""; 
        search();
    }

    
    public void search() {
        onlinePaymentLogs = onlinePaymentLogDAO.findOnlinePaymentLog(fromDate, toDate, postParam, returnParam, createBy);   
    }
    
    public void searchActionListener(ActionEvent event) {
        search();
    }

    public List<OnlinePaymentLog> getList() {
        return onlinePaymentLogs;
    } 
    

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getPostParam() {
        return postParam;
    }

    public void setPostParam(String postParam) {
        this.postParam = postParam;
    }

    public String getReturnParam() {
        return returnParam;
    }

    public void setReturnParam(String returnParam) {
        this.returnParam = returnParam;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    
    //-----OnlinePaymentLogDAO--------
    public OnlinePaymentLogDAO getOnlinePaymentLogDAO() {
        return onlinePaymentLogDAO;
    }

    public void setOnlinePaymentLogDAO(OnlinePaymentLogDAO onlinePaymentLogDAO) {
        this.onlinePaymentLogDAO = onlinePaymentLogDAO;
    }

  
}
