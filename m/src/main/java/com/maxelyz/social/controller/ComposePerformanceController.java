/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.controller;

import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.social.model.dao.SoMessageDAO;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import javax.annotation.PostConstruct;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
/**
 *
 * @author ukritj
 */
@ManagedBean
@ViewScoped
public class ComposePerformanceController {

    private static Logger log = Logger.getLogger(ComposePerformanceController.class);
    private Date fromDate, toDate;
    private List<String[]> dataList;
    
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{soMessageDAO}")
    private SoMessageDAO soMessageDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("social:dashboard:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        Date today = new Date();
        fromDate = today;
        toDate = today;
        dataList = new ArrayList<String[]>();
        this.searchActionListener();
    }

    public void searchActionListener() {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59", Locale.US);
        String fromDateStr =  sdf1.format(fromDate);
        String toDateStr =  sdf2.format(toDate);
        
        List<Users> userList = usersDAO.findSoAgentListOrderByName();
        
        dataList = soMessageDAO.findComposePerformance(userList, fromDateStr, toDateStr);
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

    public List<String[]> getDataList() {
        return dataList;
    }

    public void setDataList(List<String[]> dataList) {
        this.dataList = dataList;
    }

    public SoMessageDAO getSoMessageDAO() {
        return soMessageDAO;
    }

    public void setSoMessageDAO(SoMessageDAO soMessageDAO) {
        this.soMessageDAO = soMessageDAO;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }
}
