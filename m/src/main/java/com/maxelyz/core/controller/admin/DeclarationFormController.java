/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.DeclarationFormDAO;
import com.maxelyz.core.model.entity.DeclarationField;
import com.maxelyz.core.model.entity.DeclarationForm;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
 * @author User
 */
@ManagedBean
@RequestScoped
public class DeclarationFormController implements Serializable  {
    
    private static String REFRESH = "declarationform.xhtml?faces-redirect=true";
    private static String EDIT = "declarationformedit.xhtml";
    private static String FIELD = "declarationformfield.xhtml";
    private static String CUSTOMFIELD = "declarationformcustomfield.xhtml";
    private static String MANAGE = "declarationformmanage.xhtml";
    private static Log log = LogFactory.getLog(DeclarationFormController.class);
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<DeclarationForm> declarationForms;
    private String keyword;
    private DeclarationForm reg;
    @ManagedProperty(value = "#{declarationFormDAO}")
    private DeclarationFormDAO declarationFormDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:declaration:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        DeclarationFormDAO dao = getDeclarationFormDAO();
        declarationForms = dao.findDeclarationFormEntities();
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:declaration:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:declaration:delete");
    }

    public List<DeclarationForm> getList() {
        return getDeclarationForms();
    }

    public String searchAction() {
        DeclarationFormDAO dao = getDeclarationFormDAO();
        declarationForms = dao.findDeclarationFormByName(keyword);
        return REFRESH;
    }

    public String editAction() {
        return EDIT;
    }

    public String declarationFieldAction() {
        return FIELD;
    }

    public String manageAction() {
        return MANAGE;
    }

    public String deleteAction() throws Exception {
        DeclarationFormDAO dao = getDeclarationFormDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    reg = dao.findDeclarationForm(item.getKey());
                    reg.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    reg.setUpdateDate(new Date());
                    reg.setEnable(false);
                    dao.edit(reg);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public String copyAction() {
        DeclarationFormDAO dao = getDeclarationFormDAO();
        try {
            String selectedID = (String) JSFUtil.getRequestParameterMap("selectedID");
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            
            DeclarationForm declarationForm = this.getDeclarationFormDAO().findDeclarationForm(new Integer(selectedID));
            DeclarationForm declarationFormCopy = new DeclarationForm();
            declarationFormCopy.setDescription(declarationForm.getDescription());
            declarationFormCopy.setEnable(declarationForm.getEnable());
            declarationFormCopy.setName(declarationForm.getName()+" (2) ");
            declarationFormCopy.setMerge_file(declarationForm.getMerge_file());
            declarationFormCopy.setCreateBy(username);
            declarationFormCopy.setCreateDate(now);
            
            ArrayList<DeclarationField> declarationFieldList = new ArrayList<DeclarationField>();
            for(DeclarationField field: declarationForm.getDeclarationFieldCollection()) {
                DeclarationField fieldCopy = new DeclarationField();
                fieldCopy.setDeclarationForm(declarationFormCopy);
                fieldCopy.setName(field.getName());
                fieldCopy.setRequired(field.getRequired());
                fieldCopy.setCustomName(field.getCustomName());
                fieldCopy.setSeqNo(field.getSeqNo());
                fieldCopy.setGroupNo(field.getGroupNo());
                fieldCopy.setControlType( field.getControlType());
                fieldCopy.setItems(field.getItems());
                declarationFieldList.add(fieldCopy);
            }
            declarationFormCopy.setDeclarationFieldCollection(declarationFieldList);
            dao.create(declarationFormCopy);
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

    public List<DeclarationForm> getDeclarationForms() {
        return declarationForms;
    }

    public void setDeclarationForms(List<DeclarationForm> declarationForms) {
        this.declarationForms = declarationForms;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return the declarationFormDAO
     */
    public DeclarationFormDAO getDeclarationFormDAO() {
        return declarationFormDAO;
    }

    /**
     * @param declarationFormDAO the declarationFormDAO to set
     */
    public void setDeclarationFormDAO(DeclarationFormDAO declarationFormDAO) {
        this.declarationFormDAO = declarationFormDAO;
    }
}
