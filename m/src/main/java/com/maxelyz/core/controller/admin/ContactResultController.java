package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;

import com.maxelyz.core.model.dao.ContactResultPlanDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.ContactResultDAO;
import com.maxelyz.core.model.dao.ContactResultGroupDAO;
import com.maxelyz.core.model.entity.ContactResult;
import com.maxelyz.core.model.entity.ContactResultGroup;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;

@ManagedBean
//@RequestScoped
@ViewScoped
public class ContactResultController implements Serializable {

    private static Logger log = Logger.getLogger(ContactResultController.class);
    private static String REFRESH = "contactresult.xhtml?faces-redirect=true";
    private static String EDIT = "contactresultedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<ContactResult> contactResults;
    private ContactResult contactResult;
    private String code = "";
    private String name = "";
    private String statusID = "";
    private String contactStatus = "";
    private int groupId = 0;
    private List<ContactResultGroup> groupsList;
    
    @ManagedProperty(value = "#{contactResultDAO}")
    private ContactResultDAO contactResultDAO;
    @ManagedProperty(value = "#{contactResultGroupDAO}")
    private ContactResultGroupDAO contactResultGroupDAO;
    @ManagedProperty(value = "#{contactResultPlanDAO}")
    private ContactResultPlanDAO contactResultPlanDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:contactresult:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        this.search();
        groupsList = contactResultGroupDAO.findContactResultGroupEntities();
    }
    
    public void searchActionListener(ActionEvent event){
        
        this.search();
    }
    
    private void search(){
        try{
            contactResults = contactResultDAO.findByFilter(code, statusID, name, contactStatus, groupId);
        }catch(Exception e){
            log.error(e);
        }
    
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:contactresult:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:contactresult:delete");
    }
    
    public List<ContactResult> getList() {
        return contactResults;
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() throws Exception {
        ContactResultDAO dao = getContactResultDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    contactResult = dao.findContactResult(item.getKey());
                    contactResult.setEnable(false);
                    contactResultPlanDAO.deleteContactResultPlanWithContactResultId(item.getKey());
                    dao.edit(contactResult);
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

    public List<ContactResult> getContactResults() {
        return contactResults;
    }


    public ContactResultDAO getContactResultDAO() {
        return contactResultDAO;
    }

    public void setContactResultDAO(ContactResultDAO contactResultDAO) {
        this.contactResultDAO = contactResultDAO;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    
    public ContactResult getContactResult() {
        return contactResult;
    }

    public void setContactResult(ContactResult contactResult) {
        this.contactResult = contactResult;
    }

    public ContactResultGroupDAO getContactResultGroupDAO() {
        return contactResultGroupDAO;
    }

    public void setContactResultGroupDAO(ContactResultGroupDAO contactResultGroupDAO) {
        this.contactResultGroupDAO = contactResultGroupDAO;
    }

    public List<ContactResultGroup> getGroupsList() {
        return groupsList;
    }

    public void setGroupsList(List<ContactResultGroup> groupsList) {
        this.groupsList = groupsList;
    }
    
    public Map<String, Integer> getContactResultGroupList() {
        return this.contactResultGroupDAO.getContactResultGroupList();
    }

    public ContactResultPlanDAO getContactResultPlanDAO() {
        return contactResultPlanDAO;
    }

    public void setContactResultPlanDAO(ContactResultPlanDAO contactResultPlanDAO) {
        this.contactResultPlanDAO = contactResultPlanDAO;
    }
}
