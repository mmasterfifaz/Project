/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import com.maxelyz.social.model.dao.SoEmailSignatureDAO;
import com.maxelyz.social.model.dao.SoEmailAccountDAO;
import com.maxelyz.social.model.entity.SoEmailSignature;
import com.maxelyz.social.model.entity.SoAccount;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
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
public class EmailSignatureController implements Serializable{
    
    private static Log log = LogFactory.getLog(EmailSignatureController.class);
    private static String REFRESH = "emailsignature.xhtml?faces-redirect=true";
    private static String EDIT = "emailsignatureedit.xhtml";

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<SoEmailSignature> emailSigList;
    private int emailAccountId;
    private SoEmailSignature soEmailSignature;
    
    @ManagedProperty(value="#{soEmailSignatureDAO}")
    private SoEmailSignatureDAO soEmailSignatureDAO;
    @ManagedProperty(value="#{soEmailAccountDAO}")
    private SoEmailAccountDAO soEmailAccountDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:emailSignature:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        SoEmailSignatureDAO dao = getSoEmailSignatureDAO();
        emailSigList = dao.findSoEmailAccountSignatureList();
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:emailSignature:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:emailSignature:delete");
    }

    public List<SoEmailSignature> getList() {
        return getEmailSigList();
    }

    public void emailAccountChangeListener(ValueChangeEvent event) {
        emailAccountId = (Integer) event.getNewValue();
        SoEmailSignatureDAO dao = getSoEmailSignatureDAO();
        if (emailAccountId==0)
            emailSigList = dao.findSoEmailSignatureEntities();
        else
            emailSigList = dao.findSoEmailSignatureByAccountId(emailAccountId);
    }

     public String editAction() {
       return EDIT;
    }

    public String deleteAction() throws Exception {
        SoEmailSignatureDAO dao = getSoEmailSignatureDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    soEmailSignature = dao.findSoEmailSignature(item.getKey());
                    soEmailSignature.setEnable(false);
                    dao.edit(soEmailSignature);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
        
    public Map<String, Integer> getEmailAccountList() {
        return this.getSoEmailAccountDAO().getAccountList();
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<SoEmailSignature> getEmailSigList() {
        return emailSigList;
    }

    public void setEmailSigList(List<SoEmailSignature> emailSigList) {
        this.emailSigList = emailSigList;
    }

    public int getEmailAccountId() {
        return emailAccountId;
    }

    public void setEmailAccountId(int emailAccountId) {
        this.emailAccountId = emailAccountId;
    }

    public SoEmailSignature getSoEmailSignature() {
        return soEmailSignature;
    }

    public void setSoEmailSignature(SoEmailSignature soEmailSignature) {
        this.soEmailSignature = soEmailSignature;
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
