/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.ProductPlanDAO;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.ProductPlan;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author support
 */
@ManagedBean
@ViewScoped
public class ProductPlanRelationController {

    private static Log log = LogFactory.getLog(ProductPlanRelationController.class);
    private static String SUCCESS = "productplanrelation.xhtml?faces-redirect=true";
    private static String REDIRECT_PAGE = "productplan.xhtml?faces-redirect=true";
    private Product product;
    private ProductPlan productPlan;
    private static String BACK = "productplan.xhtml?faces-redirect=true";

    private int productId;
    private int selectPlanId;
    private List<ProductPlan> productPlans;
    private List<ProductPlan> productPlanList;
    private Boolean checkSpouseAll = false;

    private List<ProductPlan> spouseList;
    private List<String> selectSpouseId = new ArrayList<String>();
    private Integer selectOneSpouseId;
    private boolean showTableRelation = false;
    private String name;
    private String messageDup;
    private String productID;
    private Integer familyType;

    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{productPlanDAO}")
    private ProductPlanDAO productPlanDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:productinformation:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        productID = JSFUtil.getRequestParameterMap("productID");

        if (productID == null || productID.isEmpty()) {
            JSFUtil.redirect(REDIRECT_PAGE);
            return;
        } else {
            ProductDAO daoProduct = getProductDAO();
            product = daoProduct.findProduct(new Integer(productID));
            productId = product.getId();
            familyType = product.getFamilyProductType();
        }
        productPlans = productPlanDAO.findProductPlanIsFamilyEntitiesByProduct(productId);
    }

    public void initinitialize() {
        initialize();
    }

    public void changeProductPlanListener(ValueChangeEvent event) {
        messageDup = "";
        selectPlanId = (Integer) event.getNewValue();
        if(selectPlanId !=0){ 
            showTableRelation = true;
        }else {
            showTableRelation = false;
        }
        this.selectSpouseId.clear();
               
        productPlan = productPlanDAO.findProductPlanIsFamilyAndSpouseByProductAndProductPlanId(selectPlanId);
        
        if (productPlan != null) { 
            // SET SPOUSE LIST
            if(familyType.equals(1)) {
                // IF TYPE = 1 MEAN 'Spouse Package'                 
                spouseList = productPlanDAO.findProductPlanIsFamilyAndSpouseByProduct(productId);
                
                // SET SELECTED SPOUSE                
                for (ProductPlan spousePlan : productPlan.getProductPlanMasterCollection()) {
                    this.selectSpouseId.add(spousePlan.getId().toString());
                }
            } else {
                // IF TYPE = 2 MEAN 'Family Pack Discount' WILL ALLOW TO SELECT ONLY 1 PRODUCT PLAN THAT IS THE SAME PAYMENT MODE                
                spouseList = productPlanDAO.findProductPlanSamePaymentModeWithMaster(productId, productPlan.getPaymentMode());
                
                // SET SELECTED SPOUSE
                if(productPlan.getProductPlanMasterCollection() != null && productPlan.getProductPlanMasterCollection().size() > 0) {
                    selectOneSpouseId = productPlan.getProductPlanMasterCollection().iterator().next().getId();
                }
            }            
        }
        
        if (selectSpouseId != null && spouseList != null && selectSpouseId.size() == spouseList.size()) { //&& spouseString.length == spoustList.size()
            checkSpouseAll = true;
        } else {
            checkSpouseAll = false;
        }
    }

    public boolean isSavePermitted() {
        return SecurityUtil.isPermitted("admin:productinformation:edit");
    }

    public String saveAction() {
        try {
            productPlanList = new ArrayList<ProductPlan>();
            if(familyType.equals(1)) {
                for (int i = 0; i < selectSpouseId.size(); i++) {
//                    ProductPlan pp = new ProductPlan();
//                    pp.setId(Integer.parseInt(selectSpouseId.get(i)));
                    productPlanList.add(new ProductPlan(Integer.parseInt(selectSpouseId.get(i))));
                }
            } else {                
                productPlanList.add(new ProductPlan(selectOneSpouseId));
            }
             
            if(selectPlanId != 0 ) {
                productPlan = productPlanDAO.findProductPlanIsFamilyAndSpouseByProductAndProductPlanId(selectPlanId);               
                productPlan.setProductPlanMasterCollection(productPlanList);
                productPlanDAO.edit(productPlan);
            } else {
                this.messageDup = "Please Select Plan";
            }
            if(spouseList.size() == 0){
                this.messageDup = "No spouse plan. Can't save data.";
            }
        } catch (Exception e) {
            log.error(e);
            //e.printStackTrace();
            return null;
        }
        showTableRelation = false;
        selectPlanId = 0;
        return SUCCESS + "&productID=" + productID;
        
    }

    public String backAction() {
        return BACK + "&productID=" + productID;
    }

    public List<ProductPlan> getList() {
        return productPlans;
    }
    
//    public List<ProductPlan> getSelectedProductPlanCollection() {
//        List<ProductPlan> productPlanss = new ArrayList<ProductPlan>();
//        for (int i=0;i < selectSpouseId.size();i++) {
//            ProductPlan pp = new ProductPlan();
//            pp.setId(Integer.parseInt(selectSpouseId.get(i)));
//            productPlanss.add(pp);
//        }
//        return productPlanss;
//    }


    public void checkSpouseAllListener(ValueChangeEvent event) {
        checkSpouseAll = !Boolean.parseBoolean(JSFUtil.getRequestParameterMap("checkSpouseAll"));
        try {
            if (checkSpouseAll) {
                for (ProductPlan spouse : spouseList) {
                    selectSpouseId.add(spouse.getId().toString());
                }
            } else {
                selectSpouseId.clear();
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void checkSpouseListener() {
        if (selectSpouseId != null && spouseList != null && selectSpouseId.size() == spouseList.size()) { 
            checkSpouseAll = true;
        } else {
            checkSpouseAll = false;
        }
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public ProductPlanDAO getProductPlanDAO() {
        return productPlanDAO;
    }

    public void setProductPlanDAO(ProductPlanDAO productPlanDAO) {
        this.productPlanDAO = productPlanDAO;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public List<ProductPlan> getProductPlans() {
        return productPlans;
    }

    public void setProductPlans(List<ProductPlan> productPlans) {
        this.productPlans = productPlans;
    }

    public Boolean getCheckSpouseAll() {
        return checkSpouseAll;
    }

    public void setCheckSpouseAll(Boolean checkSpouseAll) {
        this.checkSpouseAll = checkSpouseAll;
    }

    public List<ProductPlan> getSpouseList() {
        return spouseList;
    }

    public void setSpouseList(List<ProductPlan> spouseList) {
        this.spouseList = spouseList;
    }

    public ProductPlan getProductPlan() {
        return productPlan;
    }

    public void setProductPlan(ProductPlan productPlan) {
        this.productPlan = productPlan;
    }

    public List<String> getSelectSpouseId() {
        return selectSpouseId;
    }

    public void setSelectSpouseId(List<String> selectSpouseId) {
        this.selectSpouseId = selectSpouseId;
    }

    public boolean isShowTableRelation() {
        return showTableRelation;
    }

    public void setShowTableRelation(boolean showTableRelation) {
        this.showTableRelation = showTableRelation;
    }

    public int getSelectPlanId() {
        return selectPlanId;
    }

    public void setSelectPlanId(int selectPlanId) {
        this.selectPlanId = selectPlanId;
    }

    public List<ProductPlan> getProductPlanList() {
        return productPlanList;
    }

    public void setProductPlanList(List<ProductPlan> productPlanList) {
        this.productPlanList = productPlanList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Integer getFamilyType() {
        return familyType;
    }

    public void setFamilyType(Integer familyType) {
        this.familyType = familyType;
    }

    public Integer getSelectOneSpouseId() {
        return selectOneSpouseId;
    }

    public void setSelectOneSpouseId(Integer selectOneSpouseId) {
        this.selectOneSpouseId = selectOneSpouseId;
    }
    
    
}
