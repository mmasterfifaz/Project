/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ChildRegFormDAO;
import com.maxelyz.core.model.dao.ChildRegTypeDAO;
import com.maxelyz.core.model.dao.ProductChildRegTypeDAO;
import com.maxelyz.core.model.entity.ChildRegField;
import com.maxelyz.core.model.entity.ChildRegForm;
import com.maxelyz.core.model.entity.ChildRegType;
import com.maxelyz.core.model.entity.ProductChildRegType;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Champ
 */
@ManagedBean
@RequestScoped
public class ChildRegFormController {
    private static String REFRESH = "childregform.xhtml?faces-redirect=true";
    private static String EDIT = "childregformedit.xhtml";
    private static String FIELD = "childregformfield.xhtml";
    private static String CUSTOMFIELD = "childregformcustomfield.xhtml";
    private static String MANAGE = "childregformmanage.xhtml";
    private static Log log = LogFactory.getLog(ChildRegFormController.class);
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<ChildRegForm> childRegForms;
    private List<ChildRegType> childRegTypes;
    private String keyword;
    private ChildRegForm reg;
    private List<String> childRegFormList = new ArrayList<>();
    
    @ManagedProperty(value = "#{childRegFormDAO}")
    private ChildRegFormDAO childRegFormDAO;    
   
    @ManagedProperty(value = "#{childRegTypeDAO}")
    private ChildRegTypeDAO childRegTypeDAO;
    
    @ManagedProperty(value = "#{productChildRegTypeDAO}")
    private ProductChildRegTypeDAO productChildRegTypeDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:childreg:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
     
        ChildRegTypeDAO dao1 = getChildRegTypeDAO();
        childRegTypes = dao1.findChildRegTypeEntities();
                
        ChildRegFormDAO dao = getChildRegFormDAO();
        childRegForms = dao.findChildRegFormEntities();
        
        List<ProductChildRegType> pcrt;
        for (int i = 0; i < childRegTypes.size(); i++) {
            pcrt = productChildRegTypeDAO.checkProductChildRegTypeIsUsed(childRegTypes.get(i).getId());
            for (int j = 0; j < pcrt.size(); j++) {
                String[] split = pcrt.get(j).getChildRegFormList().split(",");
                childRegFormList.addAll(Arrays.asList(split));
            }
        }
        Set<String> filterDup = new HashSet<>(childRegFormList);
        childRegFormList.clear();
        childRegFormList.addAll(filterDup);
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:childreg:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:childreg:delete");
    }

    public List<ChildRegForm> getList() {
        return getChildRegForms();
    }

    public String searchAction() {
        ChildRegFormDAO dao = getChildRegFormDAO();
        childRegForms = dao.findChildRegFormByName(keyword);
        return REFRESH;
    }

    public String editAction() {
        return EDIT;
    }

    public String childRegFieldAction() {
        return FIELD;
    }

    public String manageAction() {
        return MANAGE;
    }

    public String deleteAction() throws Exception {
        ChildRegFormDAO dao = getChildRegFormDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    reg = dao.findChildRegForm(item.getKey());
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
    
    public boolean isDelete(int id) {
        boolean check = false;
        for (int i = 0; i < childRegFormList.size(); i++) {
            if(childRegFormList.get(i).equals(Integer.toString(id))){
                check = true;
                break;
            } else {
                check = false;
            }
        }
        return check;
    }
    
    public String copyAction() {
        ChildRegFormDAO dao = getChildRegFormDAO();
        try {
            String selectedID = (String) JSFUtil.getRequestParameterMap("selectedID");
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            
            ChildRegForm childRegForm = this.getChildRegFormDAO().findChildRegForm(new Integer(selectedID));
            ChildRegForm childRegFormCopy = new ChildRegForm();
            childRegFormCopy.setDescription(childRegForm.getDescription());
            childRegFormCopy.setEnable(childRegForm.getEnable());
            childRegFormCopy.setName(childRegForm.getName()+" (2) ");
//            childRegFormCopy.setMerge_file(childRegForm.getMerge_file());
            childRegFormCopy.setCreateBy(username);
            childRegFormCopy.setCreateDate(now);
            
            ArrayList<ChildRegField> childRegFieldList = new ArrayList<ChildRegField>();
            for(ChildRegField field: childRegForm.getChildRegFieldCollection()) {
                ChildRegField fieldCopy = new ChildRegField();
                fieldCopy.setChildRegForm(childRegFormCopy);
                fieldCopy.setName(field.getName());
                fieldCopy.setRequired(field.getRequired());
                fieldCopy.setCustomName(field.getCustomName());
                fieldCopy.setSeqNo(field.getSeqNo());
                fieldCopy.setGroupNo(field.getGroupNo());
                fieldCopy.setControlType( field.getControlType());
                fieldCopy.setItems(field.getItems());
                childRegFieldList.add(fieldCopy);
            }
            childRegFormCopy.setChildRegFieldCollection(childRegFieldList);
            dao.create(childRegFormCopy);
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

    public List<ChildRegForm> getChildRegForms() {
        return childRegForms;
    }

    public void setChildRegForms(List<ChildRegForm> childRegForms) {
        this.childRegForms = childRegForms;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    /**
     * @return the childRegFormDAO
     */
    public ChildRegFormDAO getChildRegFormDAO() {
        return childRegFormDAO;
    }

    /**
     * @param childRegFormDAO the childRegFormDAO to set
     */
    public void setChildRegFormDAO(ChildRegFormDAO childRegFormDAO) {
        this.childRegFormDAO = childRegFormDAO;
    }
    
    /**
     * @return the childRegFormDAO
     */
    public ChildRegTypeDAO getChildRegTypeDAO() {
        return childRegTypeDAO;
    }

    /**
     * @param childRegTypeDAO the childRegTypeDAO to set
     */
    public void setChildRegTypeDAO(ChildRegTypeDAO childRegTypeDAO) {
        this.childRegTypeDAO = childRegTypeDAO;
    }

    public ProductChildRegTypeDAO getProductChildRegTypeDAO() {
        return productChildRegTypeDAO;
    }

    public void setProductChildRegTypeDAO(ProductChildRegTypeDAO productChildRegTypeDAO) {
        this.productChildRegTypeDAO = productChildRegTypeDAO;
    }
}
