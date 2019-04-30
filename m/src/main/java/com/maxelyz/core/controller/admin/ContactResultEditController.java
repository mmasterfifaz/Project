package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import java.util.Map;
import com.maxelyz.core.model.dao.ContactResultDAO;
import com.maxelyz.core.model.dao.ContactResultGroupDAO;
import com.maxelyz.core.model.entity.ContactResult;
import com.maxelyz.core.model.entity.ContactResultGroup;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
//@RequestScoped
@ViewScoped
public class ContactResultEditController {
    private static Logger log = Logger.getLogger(ContactResultEditController.class);
    private static String REDIRECT_PAGE = "contactresult.jsf";
    private static String SUCCESS = "contactresult.xhtml?faces-redirect=true";
    private static String FAILURE = "contactresultedit.xhtml";
    private ContactResult contactResult;
    private String mode;
    private String message = "";
    private String messageDup = "";
    private Integer contactResultGroupId;
    
    @ManagedProperty(value = "#{contactResultDAO}")
    private ContactResultDAO contactResultDAO;
    @ManagedProperty(value = "#{contactResultGroupDAO}")
    private ContactResultGroupDAO contactResultGroupDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:contactresult:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        messageDup = "";    
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            contactResult = new ContactResult();
            contactResult.setEnable(Boolean.TRUE);
            contactResult.setCloseAssignmentDetail(Boolean.FALSE);
            //contactResult.setContactResultGroup(new ContactResultGroup());
        } else {
            mode = "edit";
            ContactResultDAO dao = getContactResultDAO();
            contactResult = dao.findContactResult(new Integer(selectedID));
            if (contactResult == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
            if (contactResult.getContactResultGroup() != null)
                contactResultGroupId = contactResult.getContactResultGroup().getId();
        }
    }
    
      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:contactresult:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:contactresult:edit"); 
       }
    }  
    
    public String saveAction() {
        messageDup = "";
        if(checkCode(contactResult)) {
            contactResult.setContactResultGroup(new ContactResultGroup(this.contactResultGroupId));
            ContactResultDAO dao = getContactResultDAO();
            try {
                if (getMode().equals("add")) {
                    contactResult.setId(null);
                    dao.create(contactResult);
                } else {
                    dao.edit(contactResult);
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
    
    public Boolean checkCode(ContactResult contactResult) {
        String code = contactResult.getCode();
        String name = contactResult.getName();
        Integer id=0; 
        if(contactResult.getId() != null)
            id = contactResult.getId();
        ContactResultDAO dao = getContactResultDAO();
        
        Integer cnt = dao.checkDuplication(code, name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }

    public String backAction() {
        return SUCCESS;
    }

    public ContactResult getContactResult() {
        return contactResult;
        
    }

    public void setContactResult(ContactResult contactResult) {
        this.contactResult = contactResult;
    }
    
    public Integer getContactResultGroupId() {
        return contactResultGroupId;
    }

    public void setContactResultGroupId(Integer contactResultGroupId) {
        this.contactResultGroupId = contactResultGroupId;
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

    public ContactResultDAO getContactResultDAO() {
        return contactResultDAO;
    }

    public void setContactResultDAO(ContactResultDAO contactResultDAO) {
        this.contactResultDAO = contactResultDAO;
    }
    
    public ContactResultGroupDAO getContactResultGroupDAO() {
        return contactResultGroupDAO;
    }

    public void setContactResultGroupDAO(ContactResultGroupDAO contactResultGroupDAO) {
        this.contactResultGroupDAO = contactResultGroupDAO;
    }
    
    public Map<String, Integer> getContactResultGroupList() {
        return this.contactResultGroupDAO.getContactResultGroupList();
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }
}
