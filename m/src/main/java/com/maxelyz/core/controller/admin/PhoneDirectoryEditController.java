/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.PhoneDirectoryCategoryDAO;
import com.maxelyz.core.model.dao.PhoneDirectoryDAO;
import com.maxelyz.core.model.dao.PhoneDirectoryTelephoneDAO;
import com.maxelyz.core.model.entity.PhoneDirectoryCategory;
import com.maxelyz.core.model.entity.PhoneDirectory;
import com.maxelyz.core.model.entity.PhoneDirectoryTelephone;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.*;
/**
 *
 * @author Administrator
 */
@ManagedBean (name="phoneDirectoryEditController")
@RequestScoped
@ViewScoped
public class PhoneDirectoryEditController {
    
    private static Logger log = Logger.getLogger(PhoneDirectoryEditController.class);
    private static String REDIRECT_PAGE = "phonedirectory.jsf";
    private static String SUCCESS = "phonedirectory.xhtml?faces-redirect=true";
    private static String FAILURE = "phonedirectoryedit.xhtml";
    private PhoneDirectory phoneDirectory;
    private Integer phoneDirectoryCategoryId;
    private List<PhoneDirectoryCategory> phoneCategoreList;
    private List<PhoneDirectoryTelephone> phoneDirectoryTelephoneList;
    private String mode;
    private String message;
    private String messageDup;
    
    @ManagedProperty(value = "#{phoneDirectoryCategoryDAO}")
    private PhoneDirectoryCategoryDAO phoneDirectoryCategoryDAO;
    @ManagedProperty(value = "#{phoneDirectoryDAO}")
    private PhoneDirectoryDAO phoneDirectoryDAO;
    @ManagedProperty(value = "#{phoneDirectoryTelephoneDAO}")
    private PhoneDirectoryTelephoneDAO phoneDirectoryTelephoneDAO;
    
    @PostConstruct
    public void initialize() {
        
        if (!SecurityUtil.isPermitted("admin:phonedirectory:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        messageDup = "";
        phoneCategoreList = phoneDirectoryCategoryDAO.findPhoneDirectoryCategoryEnable();
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            phoneDirectory = new PhoneDirectory();
            phoneDirectory.setEnable(Boolean.TRUE);
            phoneDirectory.setStatus(Boolean.TRUE);
            phoneDirectoryTelephoneList = new ArrayList<PhoneDirectoryTelephone>();
            for (int i=0;i<10;i++) {
                PhoneDirectoryTelephone p = new PhoneDirectoryTelephone();
                phoneDirectoryTelephoneList.add(p);
            }
        } else {
            mode = "edit";
            PhoneDirectoryDAO dao = this.getPhoneDirectoryDAO();
            phoneDirectory = dao.findPhoneDirectory(new Integer(selectedID));
            if (phoneDirectory == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            } else {
                try {
                    phoneDirectoryTelephoneList = new ArrayList<PhoneDirectoryTelephone>();
                    phoneDirectoryCategoryId = phoneDirectory.getPhoneDirectoryCategory().getId();
                    List<PhoneDirectoryTelephone> TelephoneList = (List<PhoneDirectoryTelephone>)phoneDirectory.getPhoneDirectoryTelephoneCollection();
                    if(TelephoneList != null) {
                        for(PhoneDirectoryTelephone obj: TelephoneList) {
                            phoneDirectoryTelephoneList.add(obj);
                        }
                    }
                    if(phoneDirectoryTelephoneList.size() <= 10) {
                        for (int i=phoneDirectoryTelephoneList.size();i<10;i++) {
                            PhoneDirectoryTelephone p = new PhoneDirectoryTelephone();
                            phoneDirectoryTelephoneList.add(p);
                        }
                    }
                } catch (NullPointerException e) {
                    phoneDirectoryCategoryId = 0;
                }
            }
        }
    }
    
    public boolean isSavePermitted() {
        if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:phonedirectory:add");
        } else {
           return SecurityUtil.isPermitted("admin:phonedirectory:edit"); 
        }
    }  
    
    public String saveAction() {
//        messageDup = "";
//        if(checkCode(caseTopic)) {
            PhoneDirectoryDAO dao = getPhoneDirectoryDAO();
            try {
                List<PhoneDirectoryTelephone> telephoneList = new ArrayList<PhoneDirectoryTelephone>();
                String username = JSFUtil.getUserSession().getUserName();
                Date now = new Date();
                if(mode.equals("add")) {
                    phoneDirectory.setId(null);
                    phoneDirectory.setPhoneDirectoryCategory(new PhoneDirectoryCategory(phoneDirectoryCategoryId));
                    phoneDirectory.setCreateBy(username);
                    phoneDirectory.setCreateDate(now);
                    this.getPhoneDirectoryDAO().create(phoneDirectory);
                    PhoneDirectoryTelephoneDAO phoneDAO = getPhoneDirectoryTelephoneDAO();
                    for(PhoneDirectoryTelephone p: phoneDirectoryTelephoneList) {
                        if(!p.getPhoneNo().equals("")) {
                            p.setPhoneDirectory(phoneDirectory);
                            phoneDAO.create(p);
                            telephoneList.add(p);
                        }
                    }
                    phoneDirectory.setPhoneDirectoryTelephoneCollection(telephoneList);
                    dao.edit(phoneDirectory);
                } else {
                    phoneDirectory.setPhoneDirectoryCategory(new PhoneDirectoryCategory(phoneDirectoryCategoryId));
                    phoneDirectory.setUpdateBy(username);
                    phoneDirectory.setUpdateDate(now);
                    for(PhoneDirectoryTelephone p: phoneDirectoryTelephoneList) {
                        if(!p.getPhoneNo().equals("")) {
                            p.setPhoneDirectory(phoneDirectory);
                            telephoneList.add(p);
                        }
                    }
                    phoneDirectory.setPhoneDirectoryTelephoneCollection(telephoneList);
                    dao.editPhoneDirectory(phoneDirectory);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
//        } else {
//            messageDup = " Code is already taken";
//            return FAILURE;  
//        }
    }
    
    public Boolean checkCode(PhoneDirectory phoneDirectory) {
//        String code = caseTopic.getCode();
//        Integer id=0; 
//        if(caseTopic.getId() != null)
//            id = caseTopic.getId();
//        CaseTopicDAO dao = getCaseTopicDAO();
//        
//        Integer cnt = dao.checkCaseTopicCode(code, id);
//        if(cnt == 0)
            return true;
//        else
//            return false;
    }

    public String backAction() {
        return SUCCESS;
    }
    
    // Get set Properties
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

    public PhoneDirectoryTelephoneDAO getPhoneDirectoryTelephoneDAO() {
        return phoneDirectoryTelephoneDAO;
    }

    public void setPhoneDirectoryTelephoneDAO(PhoneDirectoryTelephoneDAO phoneDirectoryTelephoneDAO) {
        this.phoneDirectoryTelephoneDAO = phoneDirectoryTelephoneDAO;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public PhoneDirectory getPhoneDirectory() {
        return phoneDirectory;
    }

    public void setPhoneDirectory(PhoneDirectory phoneDirectory) {
        this.phoneDirectory = phoneDirectory;
    }

    public List<PhoneDirectoryCategory> getPhoneCategoreList() {
        return phoneCategoreList;
    }

    public void setPhoneCategoreList(List<PhoneDirectoryCategory> phoneCategoreList) {
        this.phoneCategoreList = phoneCategoreList;
    }

    public Integer getPhoneDirectoryCategoryId() {
        return phoneDirectoryCategoryId;
    }

    public void setPhoneDirectoryCategoryId(Integer phoneDirectoryCategoryId) {
        this.phoneDirectoryCategoryId = phoneDirectoryCategoryId;
    }

    public List<PhoneDirectoryTelephone> getPhoneDirectoryTelephoneList() {
        return phoneDirectoryTelephoneList;
    }

    public void setPhoneDirectoryTelephoneList(List<PhoneDirectoryTelephone> phoneDirectoryTelephoneList) {
        this.phoneDirectoryTelephoneList = phoneDirectoryTelephoneList;
    }

    
}
