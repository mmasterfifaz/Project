/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import com.maxelyz.core.model.dao.MessageTemplateCategoryDAO;
import com.maxelyz.core.model.entity.MessageTemplateCategory;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class TemplateCategoryEditController {
    private static Logger log = Logger.getLogger(TemplateCategoryEditController.class);
    private static String REDIRECT_PAGE = "templatecategory.jsf";
    private static String SUCCESS = "templatecategory.xhtml?faces-redirect=true";
    private static String FAILURE = "templatecategoryedit.xhtml";

    private MessageTemplateCategory messageTemplateCategory;
    private String mode;
    private String message;
    private String messageDup;

   @ManagedProperty(value = "#{messageTemplateCategoryDAO}")
    private MessageTemplateCategoryDAO messageTemplateCategoryDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:messageTemplateCategory:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            messageTemplateCategory = new MessageTemplateCategory();
            messageTemplateCategory.setEnable(Boolean.TRUE);
            messageTemplateCategory.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            try{
                messageTemplateCategory = messageTemplateCategoryDAO.findMessageTemplateCategory(new Integer(selectedID));
            }catch(Exception e){
                e.printStackTrace();
            }
            if (messageTemplateCategory == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }
    }


    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:messageTemplateCategory:add");
        } else {
            return SecurityUtil.isPermitted("admin:messageTemplateCategory:edit");
        }
    }
        
    public String saveAction() {
        messageDup = "";
        if(checkName(messageTemplateCategory)) {
            try {
                if (getMode().equals("add")) {
                    messageTemplateCategory.setId(null);
                    messageTemplateCategory.setCreateBy(JSFUtil.getUserSession().getUserName());
                    messageTemplateCategory.setCreateDate(new Date());
                    messageTemplateCategoryDAO.create(messageTemplateCategory);
                } else {
                    messageTemplateCategory.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    messageTemplateCategory.setUpdateDate(new Date());
                    messageTemplateCategoryDAO.edit(messageTemplateCategory);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Name is already taken";
            return FAILURE;  
        }
    }

    public Boolean checkName(MessageTemplateCategory messageTemplateCategory) {
        String name = messageTemplateCategory.getName();
        Integer id=0; 
        if(messageTemplateCategory.getId() != null)
            id = messageTemplateCategory.getId();
        MessageTemplateCategoryDAO dao = getMessageTemplateCategoryDAO();
        
        Integer cnt = dao.checkMessageTemplateCategoryName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
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

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
}
