/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.PhoneDirectoryCategoryDAO;
import com.maxelyz.core.model.dao.PhoneDirectoryDAO;
import com.maxelyz.core.model.entity.PhoneDirectoryCategory;
import com.maxelyz.core.model.entity.PhoneDirectory;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
/**
 *
 * @author Administrator
 */
@ManagedBean (name="popupSelectAddress")
@ViewScoped
public class PopupSelectAddress {
    private static Logger log = Logger.getLogger(PopupSelectAddress.class);
    private List<PhoneDirectoryCategory> phoneCategoryList;
    private List<PhoneDirectory> emailList;
    private int phoneCategoryId = 0;
    private String keyword;
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    
    @ManagedProperty(value="#{phoneDirectoryCategoryDAO}")
    private PhoneDirectoryCategoryDAO phoneDirectoryCategoryDAO;
    @ManagedProperty(value = "#{phoneDirectoryDAO}")
    private PhoneDirectoryDAO phoneDirectoryDAO;

    @PostConstruct
    public void initialize() {
        phoneCategoryList = phoneDirectoryCategoryDAO.findPhoneDirectoryCategoryEntities();
        phoneCategoryId = 0;
        keyword = "";
        if(emailList != null) {
            emailList.clear();
        }
        popupSearchAction();
    }

    public void popupSearchAction() {
        PhoneDirectoryDAO dao = getPhoneDirectoryDAO();
        emailList = dao.findAddressDirectoryBySearch(phoneCategoryId,keyword);
        
    }

    public List<PhoneDirectoryCategory> getPhoneCategoryList() {
        return phoneCategoryList;
    }

    public void setPhoneCategoryList(List<PhoneDirectoryCategory> phoneCategoryList) {
        this.phoneCategoryList = phoneCategoryList;
    }
    
    public List<PhoneDirectory> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<PhoneDirectory> emailList) {
        this.emailList = emailList;
    }

    public int getPhoneCategoryId() {
        return phoneCategoryId;
    }

    public void setPhoneCategoryId(int phoneCategoryId) {
        this.phoneCategoryId = phoneCategoryId;
    }
    
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public PhoneDirectoryDAO getPhoneDirectoryDAO() {
        return phoneDirectoryDAO;
    }

    public void setPhoneDirectoryDAO(PhoneDirectoryDAO phoneDirectoryDAO) {
        this.phoneDirectoryDAO = phoneDirectoryDAO;
    }

    public PhoneDirectoryCategoryDAO getPhoneDirectoryCategoryDAO() {
        return phoneDirectoryCategoryDAO;
    }

    public void setPhoneDirectoryCategoryDAO(PhoneDirectoryCategoryDAO phoneDirectoryCategoryDAO) {
        this.phoneDirectoryCategoryDAO = phoneDirectoryCategoryDAO;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }
}
