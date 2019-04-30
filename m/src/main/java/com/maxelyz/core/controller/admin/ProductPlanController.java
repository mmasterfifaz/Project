package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.maxelyz.core.model.dao.ProductPlanDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.ProductPlan;
import javax.annotation.Resource;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.faces.bean.*;
import javax.transaction.UserTransaction;
import javax.faces.context.FacesContext;

import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.ProductPlanDetail;
import com.maxelyz.utils.JSFUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.omg.CORBA.INTERNAL;

@ManagedBean
//@RequestScoped
@ViewScoped
public class ProductPlanController implements Serializable {

    private static String REFRESH = "productplan.xhtml";
    private static String EDIT = "productplanedit.xhtml";
    private static String IMPORT = "productplanimport.xhtml";
    private static String BACK = "product.xhtml?faces-redirect=true";
    private static String REDIRECT_PAGE = "product.jsf";
    private static String RELATION = "productplanrelation.xhtml";

    private static Log log = LogFactory.getLog(ProductPlanController.class);
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); 
    private List<ProductPlan> productPlans;
    private String keyword;
    private ProductPlan reg;
    private Product product;
    private int productId;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{productPlanDAO}")
    private ProductPlanDAO productPlanDAO;

    @PostConstruct
    public void initialize() {
       if (!SecurityUtil.isPermitted("admin:productinformation:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        String productID = JSFUtil.getRequestParameterMap("productID");

        if (productID == null || productID.isEmpty()) {
            JSFUtil.redirect(REDIRECT_PAGE);
            return;
        } else {
            ProductDAO daoProduct = getProductDAO();
            product = daoProduct.findProduct(new Integer(productID));
            productId = product.getId();
        }
        ProductPlanDAO dao = getProductPlanDAO();
        productPlans = dao.findProductPlanEntitiesByProduct(productId);

    }
    
    public void initinitialize() {
        System.out.println(JSFUtil.getRequestParameterMap("productID"));
        initialize();    
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:productinformation:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:productinformation:delete");
    }
    
    public void copyAction(ActionEvent event) {
        ProductPlanDAO dao = getProductPlanDAO();
        try {
            String selectedID = JSFUtil.getRequestParameterMap("selectedID");
            
            ProductPlan productPlan = dao.findProductPlan(new Integer(selectedID));
            ProductPlan productPlanCopy = new ProductPlan();
            
            productPlanCopy.setNo(productPlan.getNo());
            productPlanCopy.setCode(productPlan.getCode() + " (2) ");
            productPlanCopy.setName(productPlan.getName());
            productPlanCopy.setPaymentMode(productPlan.getPaymentMode());
            productPlanCopy.setDescription(productPlan.getDescription());
            productPlanCopy.setProduct(productPlan.getProduct());
            productPlanCopy.setSumInsured(productPlan.getSumInsured());
            productPlanCopy.setEnable(productPlan.getEnable());
            productPlanCopy.setProductPlanCategory(productPlan.getProductPlanCategory());
            productPlanCopy.setClaimType(productPlan.getClaimType());
            productPlanCopy.setLifeLossPerson(productPlan.getLifeLossPerson());
            productPlanCopy.setLifeLossTime(productPlan.getLifeLossTime());
            productPlanCopy.setAssetLossTime(productPlan.getAssetLossTime());
            productPlanCopy.setAssetDeduct(productPlan.getAssetDeduct());
            productPlanCopy.setMotorLoss(productPlan.getMotorLoss());
            productPlanCopy.setMotorDeduct(productPlan.getMotorDeduct());
            productPlanCopy.setMotorDamage(productPlan.getMotorDamage());
            productPlanCopy.setNoLostDriver(productPlan.getNoLostDriver());
            productPlanCopy.setCostLostDriver(productPlan.getCostLostDriver());
            productPlanCopy.setNoLostPassenger(productPlan.getNoLostPassenger());
            productPlanCopy.setCostLostPassenger(productPlan.getCostLostPassenger());
            productPlanCopy.setNoDisableDriver(productPlan.getNoDisableDriver());
            productPlanCopy.setCostDisableDriver(productPlan.getCostDisableDriver());
            productPlanCopy.setNoDisablePassenger(productPlan.getNoDisablePassenger());
            productPlanCopy.setCostDisablePassenger(productPlan.getCostDisablePassenger());
            productPlanCopy.setMedicalExpense(productPlan.getMedicalExpense());
            productPlanCopy.setBail(productPlan.getBail());
            productPlanCopy.setAct(productPlan.getAct());
            productPlanCopy.setNetPremium(productPlan.getNetPremium());
            productPlanCopy.setFx1(productPlan.getFx1());
            productPlanCopy.setFx2(productPlan.getFx2());
            productPlanCopy.setFx3(productPlan.getFx3());
            productPlanCopy.setFx4(productPlan.getFx4());
            productPlanCopy.setFx5(productPlan.getFx5());
            productPlanCopy.setFx6(productPlan.getFx6());
            productPlanCopy.setFx7(productPlan.getFx7());
            productPlanCopy.setFx8(productPlan.getFx8());
            productPlanCopy.setFx9(productPlan.getFx9());
            productPlanCopy.setFx10(productPlan.getFx10());
            productPlanCopy.setFx11(productPlan.getFx11());
            productPlanCopy.setFx12(productPlan.getFx12());
            productPlanCopy.setFx13(productPlan.getFx13());
            productPlanCopy.setFx14(productPlan.getFx14());
            productPlanCopy.setFx15(productPlan.getFx15());
            productPlanCopy.setFx16(productPlan.getFx16());
            productPlanCopy.setFx17(productPlan.getFx17());
            productPlanCopy.setFx18(productPlan.getFx18());
            productPlanCopy.setFx19(productPlan.getFx19());
            productPlanCopy.setFx20(productPlan.getFx20());
            productPlanCopy.setMasterPlan(productPlan.getMasterPlan());

            List<ProductPlan> copyProductPlanCollection = new ArrayList<ProductPlan>();
            for (ProductPlan pp :productPlan.getProductPlanMasterCollection()){
                copyProductPlanCollection.add(pp);
            }
            productPlanCopy.setProductPlanMasterCollection(copyProductPlanCollection);

            dao.create(productPlanCopy);
            
            List<ProductPlanDetail> productPlanDetails = new ArrayList<ProductPlanDetail>();
            for(ProductPlanDetail p: productPlan.getProductPlanDetailCollection()) {
                ProductPlanDetail PlanDetailCopy = new ProductPlanDetail();
                PlanDetailCopy.setProductPlan(productPlanCopy);
                PlanDetailCopy.setCode(p.getCode());
                PlanDetailCopy.setSeqNo(p.getSeqNo());
                PlanDetailCopy.setFromVal(p.getFromVal());
                PlanDetailCopy.setToVal(p.getToVal());
                PlanDetailCopy.setGender(p.getGender());
                PlanDetailCopy.setPrice(p.getPrice());
                PlanDetailCopy.setStampDuty(p.getStampDuty());
                PlanDetailCopy.setVat(p.getVat());
                PlanDetailCopy.setNetPremium(p.getNetPremium());
                PlanDetailCopy.setAnnualNetPremium(p.getAnnualNetPremium());
                PlanDetailCopy.setAnnualPrice(p.getAnnualPrice());
                PlanDetailCopy.setFx1(p.getFx1());
                PlanDetailCopy.setFx2(p.getFx2());
                PlanDetailCopy.setFx3(p.getFx3());
                PlanDetailCopy.setFx4(p.getFx4());
                PlanDetailCopy.setFx5(p.getFx5());
                PlanDetailCopy.setFx6(p.getFx6());
                PlanDetailCopy.setFx7(p.getFx7());
                PlanDetailCopy.setFx8(p.getFx8());
                PlanDetailCopy.setFx9(p.getFx9());
                PlanDetailCopy.setFx10(p.getFx10());
                PlanDetailCopy.setFx11(p.getFx11());
                PlanDetailCopy.setFx12(p.getFx12());
                PlanDetailCopy.setFx13(p.getFx13());
                PlanDetailCopy.setFx14(p.getFx14());
                PlanDetailCopy.setFx15(p.getFx15());
                PlanDetailCopy.setFx16(p.getFx16());
                PlanDetailCopy.setFx17(p.getFx17());
                PlanDetailCopy.setFx18(p.getFx18());
                PlanDetailCopy.setFx19(p.getFx19());
                PlanDetailCopy.setFx20(p.getFx20());
                
                productPlanDetails.add(PlanDetailCopy);
            }
            productPlanCopy.setProductPlanDetailCollection(productPlanDetails);
            dao.edit(productPlanCopy);
        } catch (Exception e) {
            log.error(e);
        }
        String productID = JSFUtil.getRequestParameterMap("productID");
        productPlans = dao.findProductPlanEntitiesByProduct(new Integer(productID));
        //return REFRESH;
    }
    
     public boolean getCheckFamily() {  
        int product2 = product.getId();
        int chkFamily = productDAO.checkFamily(product2);
        if(chkFamily == 0){
            return false;
        }else {
            return true;
        }
    }
        
    
    public List<ProductPlan> getList() {
        productPlans = productPlans = productPlanDAO.findProductPlanEntitiesByProduct(productId);
        return productPlans;
    }

    public String searchAction() {
        ProductPlanDAO dao = getProductPlanDAO();
        productPlans = dao.findProductPlanByName(keyword);
        return REFRESH;
    }

    public String editAction() {
        return EDIT;
    }

    public String backAction() {
        return BACK;
    }
    
    public String relationAction() {
        return RELATION;
    }

    public String deleteAction() throws Exception { 
        ProductPlanDAO dao = getProductPlanDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    reg = dao.findProductPlan(item.getKey());
                    reg.setEnable(false);
                    dao.edit(reg);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        initialize();
        return REFRESH;
    }
    
    public String importAction() {
        return IMPORT;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<ProductPlan> getProductPlans() {
        return productPlans;
    }

    public void setProductPlans(List<ProductPlan> productPlans) {
        this.productPlans = productPlans;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return the productPlanDAO
     */
    public ProductPlanDAO getProductPlanDAO() {
        return productPlanDAO;
    }

    /**
     * @param productPlanDAO the productPlanDAO to set
     */
    public void setProductPlanDAO(ProductPlanDAO productPlanDAO) {
        this.productPlanDAO = productPlanDAO;
    }

    /**
     * @return the productDAO
     */
    public ProductDAO getProductDAO() {
        return productDAO;
    }

    /**
     * @param productDAO the productDAO to set
     */
    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }
}
