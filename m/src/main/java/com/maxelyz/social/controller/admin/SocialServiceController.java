/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.controller.admin;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import org.apache.log4j.Logger;

import com.maxelyz.social.model.entity.SoService;
import com.maxelyz.social.model.dao.SoServiceDAO;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Prawait
 */
@ManagedBean
@ViewScoped
public class SocialServiceController {
    
    private static Logger logger = Logger.getLogger(SocialServiceController.class);
    private String EDIT = "servicedetail.xhtml";
    private String keyword = "";
    private String status = "";
    private Integer priority = 0;
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<SoService> soServiceList;
    
    @ManagedProperty(value = "#{soServiceDAO}")
    private SoServiceDAO soServiceDAO;
    
    @PostConstruct
    public void initialize() {
        soServiceList = search();
    }
    
    private List<SoService> search(){
        List<SoService> list = null;
        try{
            list = soServiceDAO.findSoServiceList(priority, keyword, status);
        }catch(Exception e){
            logger.error(e);
        }
        return list;
    }
    
    public String editAction(){
        return EDIT;
    }
    
    public void searchActionListener(){
        soServiceList = search();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<SoService> getSoServiceList() {
        return soServiceList;
    }

    public void setSoServiceList(List<SoService> soServiceList) {
        this.soServiceList = soServiceList;
    }

    public SoServiceDAO getSoServiceDAO() {
        return soServiceDAO;
    }

    public void setSoServiceDAO(SoServiceDAO soServiceDAO) {
        this.soServiceDAO = soServiceDAO;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }
}
