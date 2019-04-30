/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.admin;

import com.maxelyz.social.model.dao.SoAccountDAO;
//import com.maxelyz.social.model.dao.SoEmailAccountDAO;
import com.maxelyz.social.model.entity.SoAccount;
//import com.maxelyz.social.model.entity.SoEmailAccount;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class EmailAccountController implements Serializable{
    
    private static Log log = LogFactory.getLog(EmailAccountController.class);
    private static String REFRESH = "emailaccount.xhtml?faces-redirect=true";
    private static String EDIT = "emailaccountedit.xhtml";

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<SoAccount> soAccountList;
    private SoAccount soAccount;
    
    @ManagedProperty(value="#{soAccountDAO}")
    private SoAccountDAO soAccountDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:emailAccount:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        SoAccountDAO dao = getSoAccountDAO();
        soAccountList = dao.findSoAccountByEmailAccount();
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:emailAccount:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:emailAccount:delete");
    }

    public List<SoAccount> getList() {
        return getSoAccountList();
    }

    public String editAction() {
       return EDIT;
    }

    public String deleteAction() throws Exception {
        SoAccountDAO dao = getSoAccountDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    soAccount = dao.findSoAccount(item.getKey());
                    soAccount.setEnable(false);
                    dao.edit(soAccount);
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

    public List<SoAccount> getSoAccountList() {
        return soAccountList;
    }

    public void setSoAccountList(List<SoAccount> soAccountList) {
        this.soAccountList = soAccountList;
    }

    public SoAccountDAO getSoAccountDAO() {
        return soAccountDAO;
    }

    public void setSoAccountDAO(SoAccountDAO soAccountDAO) {
        this.soAccountDAO = soAccountDAO;
    }

    public SoAccount getSoAccount() {
        return soAccount;
    }

    public void setSoAccount(SoAccount soAccount) {
        this.soAccount = soAccount;
    }

}
