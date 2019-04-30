package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.ProductCategoryDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.ProductCategory;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class ProductCategoryController implements Serializable {

    private static Logger log = Logger.getLogger(ProductCategoryController.class);
    private static String REFRESH = "productcategory.xhtml?faces-redirect=true";
    private static String EDIT = "productcategoryedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<ProductCategory> productCategorys;
    private String keyword;
    private ProductCategory prod;
    @ManagedProperty(value = "#{productCategoryDAO}")
    private ProductCategoryDAO productCategoryDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:productcategory:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        ProductCategoryDAO dao = getProductCategoryDAO();
        productCategorys = dao.findProductCategoryEntities();
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:productcategory:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:productcategory:delete");
    }
    
    public List<ProductCategory> getList() {
        return getProductCategorys();
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() throws Exception {
        ProductCategoryDAO dao = getProductCategoryDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    prod = dao.findProductCategory(item.getKey());
                    prod.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    prod.setUpdateDate(new Date());
                    prod.setEnable(false);
                    dao.edit(prod);
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

    public List<ProductCategory> getProductCategorys() {
        return productCategorys;
    }

    public void setProductCategory(List<ProductCategory> productCategorys) {
        this.productCategorys = productCategorys;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ProductCategoryDAO getProductCategoryDAO() {
        return productCategoryDAO;
    }

    public void setProductCategoryDAO(ProductCategoryDAO productCategoryDAO) {
        this.productCategoryDAO = productCategoryDAO;
    }
}
