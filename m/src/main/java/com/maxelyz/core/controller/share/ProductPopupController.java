package com.maxelyz.core.controller.share;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.ProductCategoryDAO;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;

@ManagedBean
@RequestScoped
public class ProductPopupController implements Serializable {

    private static Logger log = Logger.getLogger(ProductPopupController.class);
    private static String REFRESH = "productpopup.xhtml";
    private Map<Long, Boolean> selectedIds = new ConcurrentHashMap<Long, Boolean>(); //use for checkbox
    private List<Product> productList;
    private Product product;
    private String keyword = "";
    private String selectedID;
    private int productCategoryId;

    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{productCategoryDAO}")
    private ProductCategoryDAO productCategoryDAO;

    @PostConstruct
    public void initialize() {
        selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            productList = this.getProductDAO().findProductEntities();
        } else {
            productList = this.getProductDAO().findProductEntities();
            product = this.getProductDAO().findProduct(new Integer(selectedID));

            //Save product to session
            if (product != null) {
             UserSession userSession = JSFUtil.getUserSession();
             userSession.setProducts(product);
        }
        }

    }

    public List<Product> getList() {
        return productList;
    }

    public void searchListener() {
        productList = this.getProductDAO().findProductByName(keyword);
        productCategoryId = 0; //reset product category
        if (!productList.isEmpty()) {
            selectedID = "0";
        }
    }

    public void changeProductCategoryListener() {
        if (productCategoryId == 0) {
            productList = this.getProductDAO().findProductEntities();
        } else {
            productList = this.getProductDAO().findProductByCategoryId(productCategoryId);
        }

        if (!productList.isEmpty()) {
            selectedID = "0";
        }
    }

    public Map<String, Integer> getProductCategoryList() {
        return this.getProductCategoryDAO().getProductCateogryList();
    }

    public String showAction() {
        return REFRESH;
    }

    public void setProduct(Product products) {
        this.product = products;
    }

    public Product getProduct() {
        return product;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }
   
    //Managed Properties
    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public ProductCategoryDAO getProductCategoryDAO() {
        return productCategoryDAO;
    }

    public void setProductCategoryDAO(ProductCategoryDAO productCategoryDAO) {
        this.productCategoryDAO = productCategoryDAO;
    }
}
