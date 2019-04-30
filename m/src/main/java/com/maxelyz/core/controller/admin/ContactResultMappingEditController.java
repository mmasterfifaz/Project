package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ContactResultDAO;
import com.maxelyz.core.model.dao.ContactResultMappingDAO;
import com.maxelyz.core.model.entity.ContactResult;
import com.maxelyz.core.model.entity.ContactResultMapping;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.Date;
import java.util.List;

/**
 * Created by support on 12/21/2017.
 */
@ManagedBean
@ViewScoped
public class ContactResultMappingEditController {
    private static Logger log = Logger.getLogger(FollowupEditController.class);
    private static String REDIRECT_PAGE = "contactresultmapping.jsf";
    private static String SUCCESS = "contactresultmapping.xhtml?faces-redirect=true";
    private static String FAILURE = "contactresultmappingedit.xhtml";
    private ContactResultMapping contactResultMapping;
    private String mode;
    private String message = "";
    private String messageDup = "";
    private Integer contactResultId;
    private List<ContactResult> contactResultList;

    @ManagedProperty(value = "#{contactResultMappingDAO}")
    private ContactResultMappingDAO contactResultMappingDAO;

    @ManagedProperty(value = "#{contactResultDAO}")
    private ContactResultDAO contactResultDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:contactresultmapping:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            contactResultMapping = new ContactResultMapping();
            contactResultMapping.setEnable(Boolean.TRUE);
            contactResultMapping.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            ContactResultMappingDAO dao = getContactResultMappingDAO();
            contactResultMapping = dao.findContactResultMapping(new Integer(selectedID));

            if(contactResultMapping.getContactResultId() != null && contactResultMapping.getContactResultId().getId() !=null  ){
                contactResultId =  contactResultMapping.getContactResultId().getId();
            }

            if (contactResultMapping==null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }

        contactResultList = getContactResultDAO().findContactResultEntities();
    }

    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:contactresultmapping:add");
        } else {
            return SecurityUtil.isPermitted("admin:contactresultmapping:delete");
        }
    }

    public String saveAction() {
        messageDup = "";
        if(checkCode(contactResultMapping)) {
            ContactResultMappingDAO dao = getContactResultMappingDAO();
            try {
                if (getMode().equals("add")) {
                    contactResultMapping.setId(null);
                    contactResultMapping.setContactResultId(new ContactResult(contactResultId));
                    contactResultMapping.setCode(contactResultMapping.getCode().trim());
                    contactResultMapping.setCreateBy(JSFUtil.getUserSession().getUserName());
                    contactResultMapping.setCreateDate(new Date());

                    dao.create(contactResultMapping);
                } else {
                    contactResultMapping.setContactResultId(new ContactResult(contactResultId));
                    contactResultMapping.setCode(contactResultMapping.getCode().trim());
                    contactResultMapping.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    contactResultMapping.setUpdateDate(new Date());
                    dao.edit(contactResultMapping);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
        } else {
            messageDup = " Code is already taken";
            return null;
        }
    }

    public Boolean checkCode(ContactResultMapping contactResultMapping) {
        String code = contactResultMapping.getCode();
        Integer id=0;
        if(contactResultMapping.getId() != null)
            id = contactResultMapping.getId();
        ContactResultMappingDAO dao = getContactResultMappingDAO();

        Integer cnt = dao.checkContactResultMappingCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }

    public String backAction() {
        return SUCCESS;
    }

    public ContactResultMappingDAO getContactResultMappingDAO() {
        return contactResultMappingDAO;
    }

    public void setContactResultMappingDAO(ContactResultMappingDAO contactResultMappingDAO) {
        this.contactResultMappingDAO = contactResultMappingDAO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public ContactResultMapping getContactResultMapping() {
        return contactResultMapping;
    }

    public void setContactResultMapping(ContactResultMapping contactResultMapping) {
        this.contactResultMapping = contactResultMapping;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public Integer getContactResultId() {
        return contactResultId;
    }

    public void setContactResultId(Integer contactResultId) {
        this.contactResultId = contactResultId;
    }

    public List<ContactResult> getContactResultList() {
        return contactResultList;
    }

    public void setContactResultList(List<ContactResult> contactResultList) {
        this.contactResultList = contactResultList;
    }

    public ContactResultDAO getContactResultDAO() {
        return contactResultDAO;
    }

    public void setContactResultDAO(ContactResultDAO contactResultDAO) {
        this.contactResultDAO = contactResultDAO;
    }
}
