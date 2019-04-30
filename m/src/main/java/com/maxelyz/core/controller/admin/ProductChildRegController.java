/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ChildRegFormDAO;
import com.maxelyz.core.model.dao.ChildRegTypeDAO;
import com.maxelyz.core.model.dao.ProductChildRegTypeDAO;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.entity.ProductChildRegType;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Champ
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class ProductChildRegController {
    private static String REFRESH = "productchildreg.xhtml";
    private static String EDIT = "productchildregedit.xhtml";
    private static String FAILURE = "product.xhtml";
    private static String REDIRECT_PAGE = "product.jsf";
    private static Log log = LogFactory.getLog(ChildRegFormController.class);
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
//    private List<ChildRegForm> childRegForms;
    private List<ProductChildRegType> productChildRegTypes;
    private String keyword;
    private ProductChildRegType productChildReg;
    @ManagedProperty(value = "#{productChildRegTypeDAO}")
    private ProductChildRegTypeDAO productChildRegTypeDAO;
    @ManagedProperty(value = "#{childRegFormDAO}")
    private ChildRegFormDAO childRegFormDAO;
    
    String productID;

    @PostConstruct
    public void initialize() {
//        if (!SecurityUtil.isPermitted("admin:childreg:view")) {
//            SecurityUtil.redirectUnauthorize();  
//        }
        
        productID = JSFUtil.getRequestParameterMap("productID");
        
        if (productID == null || productID.isEmpty()) {
            JSFUtil.redirect(REDIRECT_PAGE);
            return;
        } else {
//            ProductDocumentDAO daoProductdocument = getProductDocumentDAO();
//            productdocument = daoProductdocument.findProductDocument(new Integer(productID));
              ProductChildRegTypeDAO dao1 = getProductChildRegTypeDAO();
              productChildRegTypes = dao1.findProductChildRegTypeByProductId(new Integer(productID));
//            ProductDAO daoProduct = getProductDAO();
//            product = daoProduct.findProduct(new Integer(productID));
        }
//        ProductChildRegTypeDAO dao1 = getProductChildRegTypeDAO();
//        productChildRegTypes = dao1.findProductChildRegTypeEntities();
                
//        ChildRegFormDAO dao = getChildRegFormDAO();
//        childRegForms = dao.findChildRegFormEntities();
    }

    public ProductChildRegType getProductChildReg() {
        return productChildReg;
    }

    public void setProductChildReg(ProductChildRegType productChildReg) {
        this.productChildReg = productChildReg;
    }

    public ProductChildRegTypeDAO getProductChildRegTypeDAO() {
        return productChildRegTypeDAO;
    }

    public void setProductChildRegTypeDAO(ProductChildRegTypeDAO productChildRegTypeDAO) {
        this.productChildRegTypeDAO = productChildRegTypeDAO;
    }
    
    public String editAction() {
        return EDIT;
    }
    
    public String deleteAction() throws Exception {
        ProductChildRegTypeDAO dao = getProductChildRegTypeDAO();
        productChildReg = dao.findProductChildRegType(Integer.SIZE);
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    productChildReg = dao.findProductChildRegType(item.getKey());
                    productChildReg.setEnable(false);
                    dao.edit(productChildReg);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public String showChildRegFormListName(String list)
    {
        List<String> listName;
        ChildRegFormDAO dao = getChildRegFormDAO();
        listName  = dao.findChildRegFormListById(list);
        
        String name="";
        for (String n : listName) {
             name += n+",";   
        }
        
        if(!name.isEmpty())
        {
            name = name.substring(0, name.length()-1);
        }
        
        return name;
    }
    
    public String backAction() {
        return FAILURE;
    }

    public List<ProductChildRegType> getProductChildRegTypes() {
        return productChildRegTypes;
    }

    public void setProductChildRegTypes(List<ProductChildRegType> productChildRegTypes) {
        this.productChildRegTypes = productChildRegTypes;
    }
    
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public ChildRegFormDAO getChildRegFormDAO() {
        return childRegFormDAO;
    }

    public void setChildRegFormDAO(ChildRegFormDAO childRegFormDAO) {
        this.childRegFormDAO = childRegFormDAO;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
    
    
   
}
