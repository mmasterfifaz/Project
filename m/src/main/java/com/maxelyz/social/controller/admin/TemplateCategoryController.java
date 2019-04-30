/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import com.maxelyz.core.model.dao.MessageTemplateCategoryDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.MessageTemplateCategory;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class TemplateCategoryController {
    
    private static Logger log = Logger.getLogger(TemplateCategoryController.class);
    private static String REFRESH = "templatecategory.xhtml?faces-redirect=true";
    private static String EDIT = "templatecategoryedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<MessageTemplateCategory> mssageTemplateCategoryList;
    private MessageTemplateCategory messageTemplateCategory;
    @ManagedProperty(value = "#{messageTemplateCategoryDAO}")
    private MessageTemplateCategoryDAO messageTemplateCategoryDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:messageTemplateCategory:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        mssageTemplateCategoryList = messageTemplateCategoryDAO.findMessageTemplateCategoryEntities();
        
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:messageTemplateCategory:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:messageTemplateCategory:delete");
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    messageTemplateCategory =  messageTemplateCategoryDAO.findMessageTemplateCategory(item.getKey());
                    messageTemplateCategory.setEnable(false);
                    messageTemplateCategoryDAO.edit(messageTemplateCategory);
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

    public List<MessageTemplateCategory> getMssageTemplateCategoryList() {
        return mssageTemplateCategoryList;
    }

    public void setMssageTemplateCategoryList(List<MessageTemplateCategory> mssageTemplateCategoryList) {
        this.mssageTemplateCategoryList = mssageTemplateCategoryList;
    }

    public MessageTemplateCategory getMessageTemplateCategory() {
        return messageTemplateCategory;
    }

    public void setMessageTemplateCategory(MessageTemplateCategory messageTemplateCategory) {
        this.messageTemplateCategory = messageTemplateCategory;
    }

    public MessageTemplateCategoryDAO getMessageTemplateCategoryDAO() {
        return messageTemplateCategoryDAO;
    }

    public void setMessageTemplateCategoryDAO(MessageTemplateCategoryDAO messageTemplateCategoryDAO) {
        this.messageTemplateCategoryDAO = messageTemplateCategoryDAO;
    }

}
