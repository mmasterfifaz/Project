/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import com.maxelyz.core.model.dao.MessageTemplateCategoryDAO;
import com.maxelyz.core.model.dao.MessageTemplateDAO;
import com.maxelyz.core.model.entity.MessageTemplate;
import com.maxelyz.core.model.entity.MessageTemplateCategory;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class MessageTemplateEditController {
    private static Logger log = Logger.getLogger(MessageTemplateEditController.class);
    private static String REDIRECT_PAGE = "messagetemplate.jsf";
    private static String SUCCESS = "messagetemplate.xhtml?faces-redirect=true";
    private static String FAILURE = "messagetemplateedit.xhtml";

    private MessageTemplate messageTemplate;
    private String mode;
    private String message;
    private String messageDup;
    private Integer categoryId;

    @ManagedProperty(value="#{messageTemplateDAO}")
    private MessageTemplateDAO messageTemplateDAO;
    @ManagedProperty(value="#{messageTemplateCategoryDAO}")
    private MessageTemplateCategoryDAO messageTemplateCategoryDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:messageTemplate:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
       messageDup = "";
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           messageTemplate = new MessageTemplate();
           messageTemplate.setEnable(Boolean.TRUE);
           messageTemplate.setStatus(Boolean.TRUE);
       } else {
           mode = "edit";
           MessageTemplateDAO dao = getMessageTemplateDAO();
           messageTemplate = dao.findMessageTemplate(new Integer(selectedID));
           if (messageTemplate == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            } else {
                try {
                    categoryId = messageTemplate.getMessageTemplateCategory().getId();
                } catch (NullPointerException e) {
                    categoryId = 0;
                }
            }
       }
    }

      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:messageTemplate:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:messageTemplate:edit"); 
       }
    }  
        
    public String saveAction() {
        messageDup = "";
        if(checkName(messageTemplate)) {
            MessageTemplateDAO dao = getMessageTemplateDAO();
            try {                
                if (getMode().equals("add")) {
                    messageTemplate.setId(null);
                    messageTemplate.setMessageTemplateCategory(new MessageTemplateCategory(categoryId));
                    messageTemplate.setType("a");
                    messageTemplate.setCreateBy(JSFUtil.getUserSession().getUserName());
                    messageTemplate.setCreateDate(new Date());
                    dao.create(messageTemplate);
                } else {
                    messageTemplate.setType("a");
                    messageTemplate.setMessageTemplateCategory(new MessageTemplateCategory(categoryId));
                    messageTemplate.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    messageTemplate.setUpdateDate(new Date());
                    dao.edit(messageTemplate);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return null;
            }
            return SUCCESS;
        } else {
            messageDup = " Name is already taken";
            return null;  
        }
    }

    public Boolean checkName(MessageTemplate messageTemplate) {
        String name = messageTemplate.getName();
        Integer id=0; 
        if(messageTemplate.getId() != null)
            id = messageTemplate.getId();
        MessageTemplateDAO dao = getMessageTemplateDAO();
        
        Integer cnt = dao.checkMessageTemplateName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public Map<String, Integer> getCategoryList() {
        MessageTemplateCategoryDAO dao = getMessageTemplateCategoryDAO();
        List<MessageTemplateCategory> categoryList = dao.findMessageTemplateCategoryStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (MessageTemplateCategory obj : categoryList) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public MessageTemplate getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(MessageTemplate messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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
