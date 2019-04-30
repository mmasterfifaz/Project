/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.PhoneDirectoryCategoryDAO;
import com.maxelyz.core.model.entity.PhoneDirectoryCategory;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.bean.ViewScoped;
/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class PhoneCategoryController implements Serializable{
    private static Logger log = Logger.getLogger(PhoneCategoryController.class);
    private static String REFRESH = "phonecategory.xhtml?faces-redirect=true";
    private static String EDIT = "phonecategoryedit.xhtml";
        
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<PhoneDirectoryCategory> phoneCategoryList;
    private PhoneDirectoryCategory phoneDirectoryCategory;

    @ManagedProperty(value="#{phoneDirectoryCategoryDAO}")
    private PhoneDirectoryCategoryDAO phoneDirectoryCategoryDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:phonecategory:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        phoneCategoryList = phoneDirectoryCategoryDAO.findPhoneDirectoryCategoryEntities();
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:phonecategory:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:phonecategory:delete");
    }
    
    public String editAction() {
        return EDIT;
    }
    
    public String deleteAction() throws Exception {
        PhoneDirectoryCategoryDAO dao = getPhoneDirectoryCategoryDAO();
         try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    phoneDirectoryCategory = dao.findPhoneDirectoryCategory(item.getKey());
                    phoneDirectoryCategory.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    phoneDirectoryCategory.setUpdateDate(new Date());
                    phoneDirectoryCategory.setEnable(false);
                    dao.edit(phoneDirectoryCategory);
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

    public List<PhoneDirectoryCategory> getPhoneCategoryList() {
        return phoneCategoryList;
    }

    public void setPhoneCategoryList(List<PhoneDirectoryCategory> phoneCategoryList) {
        this.phoneCategoryList = phoneCategoryList;
    }

    public PhoneDirectoryCategory getPhoneDirectoryCategory() {
        return phoneDirectoryCategory;
    }

    public void setPhoneDirectoryCategory(PhoneDirectoryCategory phoneDirectoryCategory) {
        this.phoneDirectoryCategory = phoneDirectoryCategory;
    }
    
}
