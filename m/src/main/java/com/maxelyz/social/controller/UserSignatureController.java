/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller;

import com.maxelyz.social.model.dao.SoUserSignatureDAO;
import com.maxelyz.social.model.entity.SoUserSignature;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
//import javax.faces.event.ValueChangeEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class UserSignatureController  implements Serializable{
    
    private static Log log = LogFactory.getLog(UserSignatureController.class);
    private static String REFRESH = "usersignature.xhtml?faces-redirect=true";
    private static String EDIT = "usersignatureedit.xhtml";

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<SoUserSignature> userSigList;
    private SoUserSignature soUserSignature;
    
    @ManagedProperty(value="#{soUserSignatureDAO}")
    private SoUserSignatureDAO soUserSignatureDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("social:signature:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        SoUserSignatureDAO dao = getSoUserSignatureDAO();
        userSigList = dao.findSoUserSignatureEntities();
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("social:signature:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("social:signature:delete");
    }

    public List<SoUserSignature> getList() {
        return getUserSigList();
    }

    public String editAction() {
       return EDIT;
    }

    public String deleteAction() throws Exception {
        SoUserSignatureDAO dao = getSoUserSignatureDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    soUserSignature = dao.findSoUserSignature(item.getKey());
                    soUserSignature.setEnable(false);
                    dao.edit(soUserSignature);
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

    public List<SoUserSignature> getUserSigList() {
        return userSigList;
    }

    public void setUserSigList(List<SoUserSignature> userSigList) {
        this.userSigList = userSigList;
    }

    public SoUserSignature getSoUserSignature() {
        return soUserSignature;
    }

    public void setSoUserSignature(SoUserSignature soUserSignature) {
        this.soUserSignature = soUserSignature;
    }

    public SoUserSignatureDAO getSoUserSignatureDAO() {
        return soUserSignatureDAO;
    }

    public void setSoUserSignatureDAO(SoUserSignatureDAO soUserSignatureDAO) {
        this.soUserSignatureDAO = soUserSignatureDAO;
    }    
}
