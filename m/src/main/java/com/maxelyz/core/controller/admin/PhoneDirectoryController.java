/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.PhoneDirectoryCategoryDAO;
import com.maxelyz.core.model.dao.PhoneDirectoryDAO;
import com.maxelyz.core.model.entity.PhoneDirectoryCategory;
import com.maxelyz.core.model.entity.PhoneDirectory;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class PhoneDirectoryController implements Serializable{
    private static Logger log = Logger.getLogger(PhoneDirectoryController.class);
    private static String REFRESH = "phonedirectory.xhtml?faces-redirect=true";
    private static String EDIT = "phonedirectoryedit.xhtml";
    
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<PhoneDirectory> phoneDirectoryList;
    private PhoneDirectory phoneDirectory;
    private List<PhoneDirectoryCategory> phoneCategoryList;
    private int phoneDirectoryCategoryId = 0;
    private String name = "";
    private String surname = "";
    private String phoneNo = "";
    private String email = "";

    @ManagedProperty(value="#{phoneDirectoryCategoryDAO}")
    private PhoneDirectoryCategoryDAO phoneDirectoryCategoryDAO;
    @ManagedProperty(value="#{phoneDirectoryDAO}")
    private PhoneDirectoryDAO phoneDirectoryDAO;
    
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:phonedirectory:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        phoneDirectoryCategoryId = 0;
        name = "";
        surname = "";
        phoneNo = "";
        email = "";
        phoneCategoryList = phoneDirectoryCategoryDAO.findPhoneDirectoryCategoryEnable();
        this.search();
    }
    
        public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:phonedirectory:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:phonedirectory:delete");
    }
    
    private void search(){
        phoneDirectoryList = phoneDirectoryDAO.findPhoneDirectoryBySearch(phoneDirectoryCategoryId, name, surname, phoneNo, email);
    }
    
    public void searchActionListener(ActionEvent event){
        this.search();
    }
    
    public String editAction() {
        return EDIT;
    }
    
    public String deleteAction() throws Exception {
        PhoneDirectoryDAO dao = getPhoneDirectoryDAO();
         try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    phoneDirectory = dao.findPhoneDirectory(item.getKey());
                    phoneDirectory.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    phoneDirectory.setUpdateDate(new Date());
                    phoneDirectory.setEnable(false);
                    dao.edit(phoneDirectory);
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

    public PhoneDirectoryCategoryDAO getPhoneDirectoryCategoryDAO() {
        return phoneDirectoryCategoryDAO;
    }

    public void setPhoneDirectoryCategoryDAO(PhoneDirectoryCategoryDAO phoneDirectoryCategoryDAO) {
        this.phoneDirectoryCategoryDAO = phoneDirectoryCategoryDAO;
    }

    public PhoneDirectoryDAO getPhoneDirectoryDAO() {
        return phoneDirectoryDAO;
    }

    public void setPhoneDirectoryDAO(PhoneDirectoryDAO phoneDirectoryDAO) {
        this.phoneDirectoryDAO = phoneDirectoryDAO;
    }

    public List<PhoneDirectoryCategory> getPhoneCategoreList() {
        return phoneCategoryList;
    }

    public void setPhoneCategoreList(List<PhoneDirectoryCategory> phoneCategoryList) {
        this.phoneCategoryList = phoneCategoryList;
    }

    public List<PhoneDirectory> getPhoneDirectoryList() {
        return phoneDirectoryList;
    }

    public void setPhoneDirectoryList(List<PhoneDirectory> phoneDirectoryList) {
        this.phoneDirectoryList = phoneDirectoryList;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhoneDirectoryCategoryId() {
        return phoneDirectoryCategoryId;
    }

    public void setPhoneDirectoryCategoryId(int phoneDirectoryCategoryId) {
        this.phoneDirectoryCategoryId = phoneDirectoryCategoryId;
    }
    
}
