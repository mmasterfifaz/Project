/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;


import com.maxelyz.core.model.dao.ChildRegTypeDAO;
import com.maxelyz.core.model.dao.ProductChildRegTypeDAO;
import com.maxelyz.core.model.entity.ChildRegType;
import com.maxelyz.core.model.entity.ProductChildRegType;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
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
 * @author TBN
 */
@ManagedBean
@RequestScoped
public class ChildRegTypeController {
    private static String REFRESH = "childregtype.xhtml?faces-redirect=true";
    private static String EDIT = "childregtypeedit.xhtml";
    private static Log log = LogFactory.getLog(ChildRegTypeController.class);
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<ChildRegType> childRegTypes;
    private String keyword;
    private ChildRegType reg;  
   
    @ManagedProperty(value = "#{childRegTypeDAO}")
    private ChildRegTypeDAO childRegTypeDAO;
    @ManagedProperty(value = "#{productChildRegTypeDAO}")
    private ProductChildRegTypeDAO productChildRegTypeDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:childregtype:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
     
        ChildRegTypeDAO dao = getChildRegTypeDAO();
        childRegTypes = dao.findChildRegTypeEntities();
                
    }
    
    public boolean isDelete(int id) {
        List<ProductChildRegType> pcrt = productChildRegTypeDAO.checkProductChildRegTypeIsUsed(id);
        return pcrt.size() > 0;
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:childregtype:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:childregtype:delete");
    }

    public List<ChildRegType> getList() {
        return getChildRegTypes();
    }

    public String editAction() {
        return EDIT;
    }
    
    public String deleteAction() throws Exception {
        ChildRegTypeDAO dao = getChildRegTypeDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    reg = dao.findChildRegType(item.getKey());
                    reg.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    reg.setUpdateDate(new Date());
                    reg.setEnable(false);
                    dao.updateSetEnableFalseChildRegFormByChildRegTypeId(item.getKey());
                    dao.edit(reg);
                    
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
//    public String copyAction() {
//        ChildRegFormDAO dao = getChildRegFormDAO();
//        try {
//            String selectedID = (String) JSFUtil.getRequestParameterMap("selectedID");
//            String username = JSFUtil.getUserSession().getUserName();
//            Date now = new Date();
//            
//            ChildRegForm childRegForm = this.getChildRegFormDAO().findChildRegForm(new Integer(selectedID));
//            ChildRegForm childRegFormCopy = new ChildRegForm();
//            childRegFormCopy.setDescription(childRegForm.getDescription());
//            childRegFormCopy.setEnable(childRegForm.getEnable());
//            childRegFormCopy.setName(childRegForm.getName()+" (2) ");
////            childRegFormCopy.setMerge_file(childRegForm.getMerge_file());
//            childRegFormCopy.setCreateBy(username);
//            childRegFormCopy.setCreateDate(now);
//            
//            ArrayList<ChildRegField> childRegFieldList = new ArrayList<ChildRegField>();
//            for(ChildRegField field: childRegForm.getChildRegFieldCollection()) {
//                ChildRegField fieldCopy = new ChildRegField();
//                fieldCopy.setChildRegForm(childRegFormCopy);
//                fieldCopy.setName(field.getName());
//                fieldCopy.setRequired(field.getRequired());
//                fieldCopy.setCustomName(field.getCustomName());
//                fieldCopy.setSeqNo(field.getSeqNo());
//                fieldCopy.setGroupNo(field.getGroupNo());
//                fieldCopy.setControlType( field.getControlType());
//                fieldCopy.setItems(field.getItems());
//                childRegFieldList.add(fieldCopy);
//            }
//            childRegFormCopy.setChildRegFieldCollection(childRegFieldList);
//            dao.create(childRegFormCopy);
//        } catch (Exception e) {
//            log.error(e);
//        }
//        return REFRESH;
//    }
 
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<ChildRegType> getChildRegTypes() {
        return childRegTypes;
    }

    public void setChildRegTypes(List<ChildRegType> childRegTypes) {
        this.childRegTypes = childRegTypes;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return the childRegTypeDAO
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
