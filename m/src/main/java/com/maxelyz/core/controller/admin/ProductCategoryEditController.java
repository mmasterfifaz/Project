package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.ProductCategoryDAO;
import com.maxelyz.core.model.entity.ProductCategory;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import com.maxelyz.utils.SecurityUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class ProductCategoryEditController {
    private static Logger log = Logger.getLogger(ProductCategoryEditController.class);
    private ProductCategory productCategory;
    private String mode;
    private static String REDIRECT_PAGE = "productcategory.jsf";
    private static String SUCCESS = "productcategory.xhtml?faces-redirect=true";
    private static String FAILURE = "productcategoryedit.xhtml";
    private Map<String, String> productCategoryTypeList = new LinkedHashMap<String, String>();
    private String message = "";

    @ManagedProperty(value="#{productCategoryDAO}")
    private ProductCategoryDAO productCategoryDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:productcategory:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
       message = "";
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");
       if (selectedID==null || selectedID.isEmpty()) {
           mode = "add";
           productCategory = new ProductCategory();
       } else {
           mode = "edit";
           ProductCategoryDAO dao = getProductCategoryDAO();
           productCategory = dao.findProductCategory(new Integer(selectedID));
           if (productCategory==null) {
               JSFUtil.redirect(REDIRECT_PAGE);
           }
       }
       productCategoryTypeList = productCategoryDAO.getProductCateogryTypeList();
    }
    

      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:productcategory:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:productcategory:edit"); 
       }
    }  
        
    public String saveAction() {
        message = "";
        if(checkName(productCategory)) {
            ProductCategoryDAO dao = getProductCategoryDAO();
            try {
                productCategory.setEnable(true);
                if (getMode().equals("add")) {
                    productCategory.setId(null);//fix some bug
                    dao.create(productCategory);
                } else {
                    dao.edit(productCategory);
                }
            } catch (Exception e) {
                log.error(e);
                return FAILURE;
            }
            return SUCCESS;
        } else {
            message = " Name is already taken";
            return FAILURE;      
        }
    }
    
    public Boolean checkName(ProductCategory productCategory) {
        String name = productCategory.getName();
        Integer id=0; 
        if(productCategory.getId() != null)
            id = productCategory.getId();
        ProductCategoryDAO dao = getProductCategoryDAO();
        
        Integer cnt = dao.checkProductCategorytName(name, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    

    public String backAction() {
        return SUCCESS;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductSponsor(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public ProductCategoryDAO getProductCategoryDAO() {
        return productCategoryDAO;
    }

    public void setProductCategoryDAO(ProductCategoryDAO productCategoryDAO) {
        this.productCategoryDAO = productCategoryDAO;
    }

    public Map<String, String> getProductCategoryTypeList() {
        return productCategoryTypeList;
    }

    public void setProductCategoryTypeList(Map<String, String> productCategoryTypeList) {
        this.productCategoryTypeList = productCategoryTypeList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
