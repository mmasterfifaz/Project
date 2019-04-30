package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.RptSalePerformanceDAO;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

@ManagedBean
@RequestScoped
@ViewScoped
public class RptUpdateController implements Serializable{
    private static Logger log = Logger.getLogger(RptUpdateController.class);
    private static String REFRESH = "rptupdate.xhtml";
    private Date fromSaleDate;
    private Date toSaleDate;
    private String updateResult;
    
    @ManagedProperty(value="#{rptSalePerformanceDAO}")
    private RptSalePerformanceDAO rptSalePerformanceDAO;

    @PostConstruct
    public void initialize() {
        fromSaleDate = new Date();
        toSaleDate = new Date();
    }

    public void clearResultActionListener(ActionEvent ev) {
        updateResult = "Ready";
    }

    public void updateSaleRptActionListener(ActionEvent ev) {
        updateResult = this.getRptSalePerformanceDAO().updateSalePerformanceReport(fromSaleDate,toSaleDate);
    }

    public void updateSaleRptNewActionListener(ActionEvent ev) {
        updateResult = this.getRptSalePerformanceDAO().updateSalePerformanceReportNew(fromSaleDate,toSaleDate);
    }

    public Date getFromSaleDate() {
        return fromSaleDate;
    }

    public void setFromSaleDate(Date fromSaleDate) {
        this.fromSaleDate = fromSaleDate;
    }

    public Date getToSaleDate() {
        return toSaleDate;
    }

    public void setToSaleDate(Date toSaleDate) {
        this.toSaleDate = toSaleDate;
    }

    public String getUpdateResult() {
        return updateResult;
    }


    //Managed Properties
    public RptSalePerformanceDAO getRptSalePerformanceDAO() {
        return rptSalePerformanceDAO;
    }

    public void setRptSalePerformanceDAO(RptSalePerformanceDAO rptSalePerformanceDAO) {
        this.rptSalePerformanceDAO = rptSalePerformanceDAO;
    }
}
