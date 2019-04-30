package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ProductCategoryDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.ProductPlanDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.ProductPlan;
import com.maxelyz.core.model.entity.ProductPlanDetail;
import com.maxelyz.core.model.entity.ProductWorkflow;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.*;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.transaction.UserTransaction;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

@ManagedBean
@ViewScoped
public class ProductController implements Serializable{

    private static String REFRESH = "product.xhtml?faces-redirect=true";
    private static String EDIT = "productedit.xhtml";
    private static String SELECT = "productplan.xhtml";
    private static String PRODUCTDOC = "productdocument.xhtml";
    private static String PRODUCTCHILDREG = "productchildreg.xhtml";
    
    private static Log log = LogFactory.getLog(ProductController.class);

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<Product> products;
    private int productCategoryId;
    private Product prod;

    private FileUploadBean fileUploadBean = new FileUploadBean();
    private String rootPath = JSFUtil.getRealPath()+"upload\\product\\";//"D:\\crm\\MaxarCRM\\web\\upload\\";

    @ManagedProperty(value="#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value="#{productCategoryDAO}")
    private ProductCategoryDAO productCategoryDAO;
    @ManagedProperty(value="#{productPlanDAO}")
    private ProductPlanDAO productPlanDAO;
   /* @ManagedProperty(value="#{productPlanDetailDAO}")
    private ProductPlanDAO productPlanDetailDAO;*/

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:productinformation:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        ProductDAO dao = getProductDAO();
        products = dao.findProductEntities();
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:productinformation:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:productinformation:delete");
    }

    public List<Product> getList() {
        return getProducts();
    }

    public void productCategoryChangeListener(ValueChangeEvent event) {
        productCategoryId = (Integer) event.getNewValue();
        if (productCategoryId==0)
            products = this.getProductDAO().findProductEntities();
        else
            products = this.getProductDAO().findProductByCategoryId(productCategoryId);
    }

     public String editAction() {
       return EDIT;
    }

     public String selectAction() {
       return SELECT;
    }
     
     public String selectProductdocument() {
       return PRODUCTDOC;
    }

     public String selectProductchildreg() {
       return PRODUCTCHILDREG;
    }

    public String deleteAction() throws Exception {
        ProductDAO dao = getProductDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    prod = dao.findProduct(item.getKey());
                    prod.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    prod.setUpdateDate(new Date());
                    prod.setEnable(false);
                    dao.edit(prod);
                    this.fileUploadBean.deleteDir(new java.io.File(this.rootPath+prod.getId()));
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public String copyAction() {
        try {
            String selectedID = (String) JSFUtil.getRequestParameterMap("productID");
            String username = JSFUtil.getUserSession().getUserName();
            Date now = new Date();
            
            Product product = this.getProductDAO().findProduct(new Integer(selectedID));
            Product productCopy = new Product();
            
            productCopy.setCode(product.getCode()+" (2) ");
            productCopy.setName(product.getName());
            productCopy.setHighlight(product.getHighlight());
            productCopy.setDescription(product.getDescription());
            productCopy.setRemark(product.getRemark());
            productCopy.setProductSponsor(product.getProductSponsor());
            productCopy.setProductThumbnail(product.getProductThumbnail());
            productCopy.setProductCategory(product.getProductCategory());
            productCopy.setRegistrationForm(product.getRegistrationForm());
            productCopy.setPaymentMode(product.getPaymentMode());
            productCopy.setDeliveryMethod(product.getDeliveryMethod());
            productCopy.setEnable(product.getEnable());
            productCopy.setCreateBy(username);
            productCopy.setCreateDate(now);
            productCopy.setUpdateBy(null);
            productCopy.setUpdateDate(null);
            productCopy.setPrice(product.getPrice());
            productCopy.setPromotionPrice(product.getPromotionPrice());
            productCopy.setConfirmationScript(product.getConfirmationScript());
            productCopy.setModel(product.getModel());
            productCopy.setModelType(product.getModelType());
            productCopy.setModelYear(product.getModelYear());
            productCopy.setFx1(product.getFx1());
            productCopy.setFx2(product.getFx2());
            productCopy.setFx3(product.getFx3());
            productCopy.setFx4(product.getFx4());
            productCopy.setFx5(product.getFx5());
            productCopy.setFx6(product.getFx6());
            productCopy.setFx7(product.getFx7());
            productCopy.setFx8(product.getFx8());
            productCopy.setFx9(product.getFx9());
            productCopy.setFx10(product.getFx10());
            productCopy.setFx11(product.getFx11());
            productCopy.setFx12(product.getFx12());
            productCopy.setFx13(product.getFx13());
            productCopy.setFx14(product.getFx14());
            productCopy.setFx15(product.getFx15());
            productCopy.setFx16(product.getFx16());
            productCopy.setFx17(product.getFx17());
            productCopy.setFx18(product.getFx18());
            productCopy.setFx19(product.getFx19());
            productCopy.setFx20(product.getFx20());
            productCopy.setApprovalRule(product.getApprovalRule());
            productCopy.setMotorProtectionClass(product.getMotorProtectionClass());
            productCopy.setModelCc(product.getModelCc());
            productCopy.setModelFromYear(product.getModelFromYear());
            productCopy.setModelSeat(product.getModelSeat());
            productCopy.setModelToYear(product.getModelToYear());
            productCopy.setModelWeight(product.getModelWeight());
            productCopy.setSequenceNo(product.getSequenceNo());
            productCopy.setAllowCopy(product.getAllowCopy());
            productCopy.setGender(product.getGender());
            productCopy.setContactResultPlan(product.getContactResultPlan());
            productCopy.setFamilyProduct(product.getFamilyProduct());
            productCopy.setFamilyProductType(product.getFamilyProductType());
            productCopy.setFamilyProductLogic(product.getFamilyProductLogic());
            productCopy.setCardType(product.getCardType());
            productCopy.setCardTypeDebit(product.getCardTypeDebit());
            productCopy.setCardBank(product.getCardBank());
            productCopy.setCardBankDebit(product.getCardBankDebit());
            productCopy.setBeneficiaryNo(product.getBeneficiaryNo());
            productCopy.setPercentBeneficiaryFxFieldNo(product.getPercentBeneficiaryFxFieldNo());
            productCopy.setAgeCalMethod(product.getAgeCalMethod());
            productCopy.setAgeCalDay(product.getAgeCalDay());
            productCopy.setAgeCalMonth(product.getAgeCalMonth());
            productCopy.setRefNoGenerateEvent(product.getRefNoGenerateEvent());
            this.getProductDAO().create(productCopy);

            HashMap<Integer, Integer> mappingProductPlanCopy = new HashMap<Integer, Integer>();
            List<ProductPlan> productPlanList = new ArrayList<ProductPlan>();
            for(ProductPlan p: product.getProductPlanCollection()) {
                ProductPlan productPlan = new ProductPlan();
                productPlan.setProduct(productCopy);
                productPlan.setNo(p.getNo());
                productPlan.setCode(p.getCode());
                productPlan.setName(p.getName());
                productPlan.setPaymentMode(p.getPaymentMode());
                productPlan.setDescription(p.getDescription());
                productPlan.setSumInsured(p.getSumInsured());
                productPlan.setEnable(p.getEnable());
                productPlan.setProductPlanCategory(p.getProductPlanCategory());
                productPlan.setClaimType(p.getClaimType());
                productPlan.setLifeLossPerson(p.getLifeLossPerson());
                productPlan.setLifeLossTime(p.getLifeLossTime());
                productPlan.setAssetLossTime(p.getAssetLossTime());
                productPlan.setAssetDeduct(p.getAssetDeduct());
                productPlan.setMotorLoss(p.getMotorLoss());
                productPlan.setMotorDeduct(p.getMotorDeduct());
                productPlan.setMotorDamage(p.getMotorDamage());
                productPlan.setNoLostDriver(p.getNoLostDriver());
                productPlan.setCostLostDriver(p.getCostLostDriver());
                productPlan.setNoLostPassenger(p.getNoLostPassenger());
                productPlan.setCostLostPassenger(p.getCostLostPassenger());
                productPlan.setNoDisableDriver(p.getNoDisableDriver());
                productPlan.setCostDisableDriver(p.getCostDisableDriver());
                productPlan.setNoDisablePassenger(p.getNoDisablePassenger());
                productPlan.setCostDisablePassenger(p.getCostDisablePassenger());
                productPlan.setMedicalExpense(p.getMedicalExpense());
                productPlan.setBail(p.getBail());
                productPlan.setAct(p.getAct());
                productPlan.setNetPremium(p.getNetPremium());
                productPlan.setFx1(p.getFx1());
                productPlan.setFx2(p.getFx2());
                productPlan.setFx3(p.getFx3());
                productPlan.setFx4(p.getFx4());
                productPlan.setFx5(p.getFx5());
                productPlan.setFx6(p.getFx6());
                productPlan.setFx7(p.getFx7());
                productPlan.setFx8(p.getFx8());
                productPlan.setFx9(p.getFx9());
                productPlan.setFx10(p.getFx10());
                productPlan.setFx11(p.getFx11());
                productPlan.setFx12(p.getFx12());
                productPlan.setFx13(p.getFx13());
                productPlan.setFx14(p.getFx14());
                productPlan.setFx15(p.getFx15());
                productPlan.setFx16(p.getFx16());
                productPlan.setFx17(p.getFx17());
                productPlan.setFx18(p.getFx18());
                productPlan.setFx19(p.getFx19());
                productPlan.setFx20(p.getFx20());
                productPlan.setMasterPlan(p.getMasterPlan());
                this.getProductPlanDAO().create(productPlan);
                mappingProductPlanCopy.put(p.getId(),productPlan.getId());
                
                List<ProductPlanDetail> productPlanDetails = new ArrayList<ProductPlanDetail>();
                for(ProductPlanDetail pp: p.getProductPlanDetailCollection()) {
                    ProductPlanDetail productPlanDetail = new ProductPlanDetail();
                    productPlanDetail.setProductPlan(productPlan);
                    productPlanDetail.setSeqNo(pp.getSeqNo());
                    productPlanDetail.setCode(pp.getCode());
                    productPlanDetail.setFromVal(pp.getFromVal());
                    productPlanDetail.setToVal(pp.getToVal());
                    productPlanDetail.setGender(pp.getGender());
                    productPlanDetail.setPrice(pp.getPrice());
                    productPlanDetail.setStampDuty(pp.getStampDuty());
                    productPlanDetail.setVat(pp.getVat());
                    productPlanDetail.setNetPremium(pp.getNetPremium());
                    productPlanDetail.setAnnualNetPremium(pp.getAnnualNetPremium());
                    productPlanDetail.setAnnualPrice(pp.getAnnualPrice());
                    productPlanDetail.setFx1(pp.getFx1());
                    productPlanDetail.setFx2(pp.getFx2());
                    productPlanDetail.setFx3(pp.getFx3());
                    productPlanDetail.setFx4(pp.getFx4());
                    productPlanDetail.setFx5(pp.getFx5());
                    productPlanDetail.setFx6(pp.getFx6());
                    productPlanDetail.setFx7(pp.getFx7());
                    productPlanDetail.setFx8(pp.getFx8());
                    productPlanDetail.setFx9(pp.getFx9());
                    productPlanDetail.setFx10(pp.getFx10());
                    productPlanDetail.setFx11(pp.getFx11());
                    productPlanDetail.setFx12(pp.getFx12());
                    productPlanDetail.setFx13(pp.getFx13());
                    productPlanDetail.setFx14(pp.getFx14());
                    productPlanDetail.setFx15(pp.getFx15());
                    productPlanDetail.setFx16(pp.getFx16());
                    productPlanDetail.setFx17(pp.getFx17());
                    productPlanDetail.setFx18(pp.getFx18());
                    productPlanDetail.setFx19(pp.getFx19());
                    productPlanDetail.setFx20(pp.getFx20());

                    productPlanDetails.add(productPlanDetail);
                }
                productPlan.setProductPlanDetailCollection(productPlanDetails);
                this.getProductPlanDAO().edit(productPlan);
                productPlanList.add(productPlan);
            }
            productCopy.setProductPlanCollection(productPlanList);

            Set set = mappingProductPlanCopy.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry)iterator.next();
                Integer productPlanBeforeCopy = Integer.parseInt(mentry.getKey().toString());
                ProductPlan productPlanForRelation = productPlanDAO.findProductPlan(new Integer(productPlanBeforeCopy));
                Integer productPlanAfterCopy = mappingProductPlanCopy.get(productPlanBeforeCopy);
                for (ProductPlan pp :productPlanForRelation.getProductPlanMasterCollection()){
                    Integer productPlanAfterCopyRelation = mappingProductPlanCopy.get(pp.getId());
                    productPlanDAO.insertCopyProductPlanRelation(productPlanAfterCopy,productPlanAfterCopyRelation);
                }
            }

            if(product.getProductWorkflowCollection() != null){
                List<ProductWorkflow> pwList = new ArrayList<ProductWorkflow>();
                for(ProductWorkflow pw : product.getProductWorkflowCollection()){
                    ProductWorkflow pwCopy = new ProductWorkflow();
                    pwCopy.setId(null);
                    pwCopy.setProduct(productCopy);
                    pwCopy.setStepNo(pw.getStepNo());
                    pwCopy.setApprovalRoleName(pw.getApprovalRoleName());
                    pwCopy.setRequire(pw.getRequire());
                    pwCopy.setNotify(pw.getNotify());
                    pwCopy.setSentEmail(pw.getSentEmail());
                    pwCopy.setEmail(pw.getEmail());
                    pwCopy.setAutoApprove(pw.getAutoApprove());
                    pwCopy.setCreateDate(new Date());
                    pwCopy.setCreateBy(JSFUtil.getUserSession().getUserName());
                    pwCopy.setUpdateDate(new Date());
                    pwCopy.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    pwList.add(pwCopy);
                }
                productCopy.setProductWorkflowCollection(pwList);
            }
            this.getProductDAO().edit(productCopy);
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
        
    public Map<String, Integer> getProductCateogryList() {
        return this.getProductCategoryDAO().getProductCateogryList();
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }



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
   
    public ProductPlanDAO getProductPlanDAO() {
        return productPlanDAO;
    }

    public void setProductPlanDAO(ProductPlanDAO productPlanDAO) {
        this.productPlanDAO = productPlanDAO;
    }

}
