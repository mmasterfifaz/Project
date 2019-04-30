/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import com.maxelyz.social.model.dao.SoEmailSignatureDAO;
import com.maxelyz.social.model.dao.SoEmailAccountDAO;
import com.maxelyz.social.model.entity.SoAccount;
import com.maxelyz.social.model.entity.SoEmailSignature;
import javax.faces.bean.ViewScoped;
import com.maxelyz.social.model.entity.SoEmailAccount;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class EmailSignatureEditController {
    private static Logger log = Logger.getLogger(EmailSignatureEditController.class);
    private static String REDIRECT_PAGE = "emailsignature.jsf";
    private static String SUCCESS = "emailsignature.xhtml?faces-redirect=true";
    private static String FAILURE = "emailsignatureedit.xhtml";

    private SoEmailSignature soEmailSignature;
    private String mode;
    private String message;
    private String messageDup;
    private Integer accountId;

    @ManagedProperty(value="#{soEmailSignatureDAO}")
    private SoEmailSignatureDAO soEmailSignatureDAO;
    @ManagedProperty(value="#{soEmailAccountDAO}")
    private SoEmailAccountDAO soEmailAccountDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:emailSignature:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
       messageDup = "";
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");

       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           soEmailSignature = new SoEmailSignature();
           soEmailSignature.setEnable(Boolean.TRUE);
           soEmailSignature.setStatus(Boolean.TRUE);
       } else {
           mode = "edit";
           SoEmailSignatureDAO dao = getSoEmailSignatureDAO();
           soEmailSignature = dao.findSoEmailSignature(new Integer(selectedID));
           if (soEmailSignature == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
            } else {
                try {
                    accountId = soEmailSignature.getSoEmailAccount().getSoAccount().getId();
                } catch (NullPointerException e) {
                    accountId = 0;
                }
            }
       }
    }

      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:emailSignature:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:emailSignature:edit"); 
       }
    }  
        
    public String saveAction() {
        messageDup = "";
        if(checkName(soEmailSignature)) {
            SoEmailSignatureDAO dao = getSoEmailSignatureDAO();
            try {                
                if (getMode().equals("add")) {
                    soEmailSignature.setId(null);
                    SoEmailAccount soEmailAccount = getSoEmailAccountDAO().findSoEmailAccountBySoAccountId(accountId);
                    soEmailSignature.setSoEmailAccount(soEmailAccount);
                    soEmailSignature.setCreateBy(JSFUtil.getUserSession().getUserName());
                    soEmailSignature.setCreateDate(new Date());
                    dao.create(soEmailSignature);
                } else {
                    SoEmailAccount soEmailAccount = getSoEmailAccountDAO().findSoEmailAccountBySoAccountId(accountId);
                    soEmailSignature.setSoEmailAccount(soEmailAccount);
                    soEmailSignature.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    soEmailSignature.setUpdateDate(new Date());
                    dao.edit(soEmailSignature);
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

    public Boolean checkName(SoEmailSignature soEmailSignature) {
        String name = soEmailSignature.getName();
        Integer id=0; 
        if(soEmailSignature.getId() != null)
            id = soEmailSignature.getId();
        SoEmailSignatureDAO dao = getSoEmailSignatureDAO();
        
        Integer cnt = dao.checkEmailSignatureName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public Map<String, Integer> getAccountList() {
        SoEmailAccountDAO dao = getSoEmailAccountDAO();
        List<SoAccount> accountList = dao.findSoAccountList();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoAccount obj : accountList) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public SoEmailSignature getSoEmailSignature() {
        return soEmailSignature;
    }

    public void setSoEmailSignature(SoEmailSignature soEmailSignature) {
        this.soEmailSignature = soEmailSignature;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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

    public SoEmailSignatureDAO getSoEmailSignatureDAO() {
        return soEmailSignatureDAO;
    }

    public void setSoEmailSignatureDAO(SoEmailSignatureDAO soEmailSignatureDAO) {
        this.soEmailSignatureDAO = soEmailSignatureDAO;
    }

    public SoEmailAccountDAO getSoEmailAccountDAO() {
        return soEmailAccountDAO;
    }

    public void setSoEmailAccountDAO(SoEmailAccountDAO soEmailAccountDAO) {
        this.soEmailAccountDAO = soEmailAccountDAO;
    }

}
