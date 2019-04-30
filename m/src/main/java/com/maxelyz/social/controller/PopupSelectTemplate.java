/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller;

import com.maxelyz.core.model.dao.MessageTemplateCategoryDAO;
import com.maxelyz.core.model.dao.MessageTemplateDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.MessageTemplate;
import com.maxelyz.core.model.entity.MessageTemplateCategory;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.JSFUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.format.CellFormatPart;
import org.apache.poi.util.StringUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author admin
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class PopupSelectTemplate {
    private static Logger log = Logger.getLogger(PopupSelectTemplate.class);
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<MessageTemplate> lists;
    private List<MessageTemplateCategory> groups;
    private String keyword;
    private int groupId;
    private MessageTemplate messageTemplate = null;

    @ManagedProperty(value = "#{messageTemplateDAO}")
    private MessageTemplateDAO messageTemplateDAO;
    @ManagedProperty(value = "#{messageTemplateCategoryDAO}")
    private MessageTemplateCategoryDAO messageTemplateCategoryDAO;

    @PostConstruct
    public void initialize() {
        keyword = "";
        groupId = 0;
        messageTemplate = null;
        search();
    }

    public void selectIdListener() {
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if(selectedID != null) {
            messageTemplate = messageTemplateDAO.findMessageTemplate(new Integer(selectedID));
        }
    }
    
    public Map<String, Integer> getGroups() {
        return messageTemplateCategoryDAO.getMessageTemplateCategoryList();
    }

    public List<MessageTemplate> getLists() {
        return lists;
    }

    public void setLists(List<MessageTemplate> lists) {
        this.lists = lists;
    }

    public void search() {
        messageTemplate = null;
//        lists = messageTemplateDAO.findMessageTemplateModal(keyword, StringUtils.isBlank(groupId)?null:messageTemplateCategoryDAO.findMessageTemplateCategory(Integer.parseInt(groupId)));
        lists = messageTemplateDAO.findMessageTemplateModal(groupId,keyword);
    }

    public void popupSearchAction(ActionEvent event) {
        search();
//        return REFRESH;
    }

    public void popupGroupChangeListener() {
        search();
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public MessageTemplateDAO getMessageTemplateDAO() {
        return messageTemplateDAO;
    }

    public void setMessageTemplateDAO(MessageTemplateDAO messageTemplateDAO) {
        this.messageTemplateDAO = messageTemplateDAO;
    }

    public MessageTemplateCategoryDAO getMessageTemplateCategoryDAO() {
        return messageTemplateCategoryDAO;
    }

    public void setMessageTemplateCategoryDAO(MessageTemplateCategoryDAO messageTemplateCategoryDAO) {
        this.messageTemplateCategoryDAO = messageTemplateCategoryDAO;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public MessageTemplate getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(MessageTemplate messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

}
