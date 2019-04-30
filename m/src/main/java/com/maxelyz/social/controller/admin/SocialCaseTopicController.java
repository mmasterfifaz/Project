/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import com.maxelyz.social.model.dao.SoCaseTypeDAO;
import com.maxelyz.social.model.entity.SoCaseType;
import com.maxelyz.utils.SecurityUtil;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.event.ValueChangeEvent;
/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class SocialCaseTopicController implements Serializable {

    private static Logger log = Logger.getLogger(SocialCaseTypeController.class);
    private static String REFRESH = "socialcasetopic.xhtml?faces-redirect=true";
    private static String EDIT = "socialcasetopicedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<SoCaseType> soCaseTopics;
    private SoCaseType soCaseTopic;
    private int caseTypeId;
    
    @ManagedProperty(value = "#{soCaseTypeDAO}")
    private SoCaseTypeDAO soCaseTypeDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:socialcasetopic:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        soCaseTopics = soCaseTypeDAO.findSoCaseTopicList();
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:socialcasetopic:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:socialcasetopic:delete");
    }

    public List<SoCaseType> getList() {
        return getSoCaseTopics();
    }

    public void caseTypeChangeListener(ValueChangeEvent event) {
        caseTypeId = (Integer) event.getNewValue();
        SoCaseTypeDAO dao = getSoCaseTypeDAO();
        if(caseTypeId == 0)
            soCaseTopics = dao.findSoCaseTopicList();
        else
            soCaseTopics = dao.findSoCaseTopicListById(caseTypeId);
        
    }
    
    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    soCaseTopic = soCaseTypeDAO.findSoCaseType(item.getKey());
                    soCaseTopic.setEnable(false);
                    soCaseTypeDAO.edit(soCaseTopic);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public Map<String, Integer> getCaseTypeList() {
        return this.getSoCaseTypeDAO().getSoCaseTypeList();// this.getMessageTemplateCategoryDAO().getMessageTemplateCategoryList();
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<SoCaseType> getSoCaseTopics() {
        return soCaseTopics;
    }

    public void setSoCaseTopics(List<SoCaseType> soCaseTopics) {
        this.soCaseTopics = soCaseTopics;
    }

    public SoCaseType getSoCaseTopic() {
        return soCaseTopic;
    }

    public void setSoCaseTopic(SoCaseType soCaseTopic) {
        this.soCaseTopic = soCaseTopic;
    }

    public SoCaseTypeDAO getSoCaseTypeDAO() {
        return soCaseTypeDAO;
    }

    public void setSoCaseTypeDAO(SoCaseTypeDAO soCaseTypeDAO) {
        this.soCaseTypeDAO = soCaseTypeDAO;
    }

    public int getCaseTypeId() {
        return caseTypeId;
    }

    public void setCaseTypeId(int caseTypeId) {
        this.caseTypeId = caseTypeId;
    }
}

