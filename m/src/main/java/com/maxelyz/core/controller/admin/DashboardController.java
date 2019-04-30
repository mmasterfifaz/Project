package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.RptSalePerformance1;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.model.value.admin.RptContactCaseTypeValue;
import com.maxelyz.utils.JSFUtil;
import com.ntier.utils.ParameterMap;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class DashboardController implements Serializable {

    private static Logger log = Logger.getLogger(DashboardController.class);
    private List<RptContactCaseTypeValue> rptContactCaseTypeValues;

    private String contactCaseTypeLabel;
    private String contactCaseTypePendingData;
    private String contactCaseTypeClosedData;
    private String contactCaseTypeFirstCallData;
    
    private String yesSaleByCampaignData;
    private String yesSaleByCampaignLabel;
    private String yesSaleByUserData;
    private String yesSaleByUserLabel;
    
    private int userGroupId;


    @ManagedProperty(value = "#{rptContactChannelDAO}")
    private RptContactChannelDAO rptContactChannelDAO;
    @ManagedProperty(value = "#{rptRptSalePerformanceDAO}")
    private RptSalePerformanceDAO rptSalePerformanceDAO;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    
    @PostConstruct
    public void initialize() {
        JSFUtil.getUserSession().setMainPage("admin");
        //Date today = new Date(2011-1900,1,11); //11 feb 2011
        Date today = new Date();
        rptContactCaseTypeValues = this.getRptContactChannelDAO().findContactCaseByCaseType(JSFUtil.toDateWithoutTime(today));
        
        int i = 0;
        contactCaseTypeLabel = "";
        contactCaseTypePendingData = "";
        contactCaseTypeClosedData = "";
        contactCaseTypeFirstCallData = "";
        for (RptContactCaseTypeValue obj : rptContactCaseTypeValues) {
            contactCaseTypeLabel += String.format(",{value: %d, text: \"%s\"}", ++i, obj.getCaseType());
            contactCaseTypePendingData +=  obj.getPending() + ",";
            contactCaseTypeClosedData += obj.getClosed() + ",";
            contactCaseTypeFirstCallData += obj.getFirstCallResolution() + ",";
        }
        contactCaseTypePendingData = removeLastComma(contactCaseTypePendingData);
        contactCaseTypeClosedData = removeLastComma(contactCaseTypeClosedData);
        contactCaseTypeFirstCallData = removeLastComma(contactCaseTypeFirstCallData);
       
        /*
        i=0;
        List<Users> users = usersDAO.getUsersListByUserGroup(userGroupId);
        //List<Users> users = usersDAO.findAgent(JSFUtil.getUserSession().getUsers());
        saleData="";
        saleLabel="";
        for (Users obj : users) {
            RptSalePerformance1 rptSalePerformance1 =  rptSalePerformanceDAO.findRptSalePerformance(obj);
            saleData += String.format(",{value: %d, text: \"%s\"}", ++i, obj.getName());
            saleLabel += rptSalePerformance1.getProductYesSale()+",";
           
        }
        * 
        */
        
        yesSaleByCampaignData="";
        yesSaleByCampaignLabel="";
        i=0;
        List<ParameterMap> p = purchaseOrderDAO.findYesSaleByCampaignToday();
        for (ParameterMap obj : p) {
            yesSaleByCampaignLabel += String.format(",{value: %d, text: \"%s\"}", ++i, obj.getName());
            yesSaleByCampaignData += obj.getValue()+",";
            
        }
        yesSaleByCampaignData = removeLastComma(yesSaleByCampaignData);
        
        yesSaleByUserData="";
        yesSaleByUserLabel="";
        i=0;
        List<ParameterMap> p2 = purchaseOrderDAO.findYesSaleByUserToday();
        for (ParameterMap obj : p2) {
            yesSaleByUserLabel += String.format(",{value: %d, text: \"%s\"}", ++i, obj.getName());
            yesSaleByUserData += obj.getValue()+",";
         
        }
        yesSaleByUserData = removeLastComma(yesSaleByUserData);
        
    }

    private String removeLastComma(String str) {
        if (str.length() > 0)
            str = str.substring(0, str.length() - 1);
        else
            str = "0";
        return str;
    }

    public String getContactCaseTypePendingData() {
        return contactCaseTypePendingData;
    }

    public String getContactCaseTypeClosedData() {
        return contactCaseTypeClosedData;
    }

    public String getContactCaseTypeLabel() {
        return contactCaseTypeLabel;
    }

    public String getContactCaseTypeFirstCallData() {
        return contactCaseTypeFirstCallData;
    }

    public String getYesSaleByCampaignData() {
        return yesSaleByCampaignData;
    }

    public void setYesSaleByCampaignData(String yesSaleByCampaignData) {
        this.yesSaleByCampaignData = yesSaleByCampaignData;
    }

    public String getYesSaleByCampaignLabel() {
        return yesSaleByCampaignLabel;
    }

    public void setYesSaleByCampaignLabel(String yesSaleByCampaignLabel) {
        this.yesSaleByCampaignLabel = yesSaleByCampaignLabel;
    }

    public String getYesSaleByUserData() {
        return yesSaleByUserData;
    }

    public void setYesSaleByUserData(String yesSaleByUserData) {
        this.yesSaleByUserData = yesSaleByUserData;
    }

    public String getYesSaleByUserLabel() {
        return yesSaleByUserLabel;
    }

    public void setYesSaleByUserLabel(String yesSaleByUserLabel) {
        this.yesSaleByUserLabel = yesSaleByUserLabel;
    }
    
    
    public Map<String, Integer> getUserGroupList() {    
        return getUserGroupDAO().getUserGroupListByAgent();
    }

    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }


    public RptContactChannelDAO getRptContactChannelDAO() {
        return rptContactChannelDAO;
    }

    public void setRptContactChannelDAO(RptContactChannelDAO rptContactChannelDAO) {
        this.rptContactChannelDAO = rptContactChannelDAO;
    }

    public RptSalePerformanceDAO getRptSalePerformanceDAO() {
        return rptSalePerformanceDAO;
    }

    public void setRptSalePerformanceDAO(RptSalePerformanceDAO rptSalePerformanceDAO) {
        this.rptSalePerformanceDAO = rptSalePerformanceDAO;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }
    
    
}
