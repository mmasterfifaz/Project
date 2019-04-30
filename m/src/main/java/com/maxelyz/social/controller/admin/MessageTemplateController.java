/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;


import com.maxelyz.core.model.dao.MessageTemplateCategoryDAO;
import com.maxelyz.core.model.dao.MessageTemplateDAO;
import com.maxelyz.core.model.entity.MessageTemplate;
//import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ValueChangeEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class MessageTemplateController implements Serializable{
    
    private static Log log = LogFactory.getLog(MessageTemplateController.class);
    private static String REFRESH = "messagetemplate.xhtml?faces-redirect=true";
    private static String EDIT = "messagetemplateedit.xhtml";

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<MessageTemplate> msgTemplateList;
    private int msgTemplateCategoryId;
    private MessageTemplate msgTemplate;
    
    @ManagedProperty(value="#{messageTemplateDAO}")
    private MessageTemplateDAO messageTemplateDAO;
    @ManagedProperty(value="#{messageTemplateCategoryDAO}")
    private MessageTemplateCategoryDAO messageTemplateCategoryDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:messageTemplate:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        MessageTemplateDAO dao = getMessageTemplateDAO();
        msgTemplateList = dao.findMessageTemplateEntities();
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:messageTemplate:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:messageTemplate:delete");
    }

    public List<MessageTemplate> getList() {
        return getMsgTemplateList();
    }

    public void templateCategoryChangeListener(ValueChangeEvent event) {
        msgTemplateCategoryId = (Integer) event.getNewValue();
        MessageTemplateDAO dao = getMessageTemplateDAO();
        if (msgTemplateCategoryId==0)
            msgTemplateList = dao.findMessageTemplateEntities();
        else
            msgTemplateList = dao.findMessageTemplateByCategoryId(msgTemplateCategoryId);
    }

     public String editAction() {
       return EDIT;
    }

    public String deleteAction() throws Exception {
        MessageTemplateDAO dao = getMessageTemplateDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    msgTemplate = dao.findMessageTemplate(item.getKey());
                    msgTemplate.setEnable(false);
                    dao.edit(msgTemplate);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
        
    public Map<String, Integer> getTemplateCateogryList() {
        return this.getMessageTemplateCategoryDAO().getMessageTemplateCategoryList();
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<MessageTemplate> getMsgTemplateList() {
        return msgTemplateList;
    }

    public void setMsgTemplateList(List<MessageTemplate> msgTemplateList) {
        this.msgTemplateList = msgTemplateList;
    }

    public int getMsgTemplateCategoryId() {
        return msgTemplateCategoryId;
    }

    public void setMsgTemplateCategoryId(int msgTemplateCategoryId) {
        this.msgTemplateCategoryId = msgTemplateCategoryId;
    }

    public MessageTemplate getMsgTemplate() {
        return msgTemplate;
    }

    public void setMsgTemplate(MessageTemplate msgTemplate) {
        this.msgTemplate = msgTemplate;
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

}
