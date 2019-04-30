package com.maxelyz.core.controller.admin;

import com.maxelyz.converter.PaymentModeConverter;
import com.maxelyz.core.common.dto.PaymentMethodStringCastor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.maxelyz.core.model.dao.ProductPlanDAO;
import com.maxelyz.core.model.entity.ProductPlan;
import javax.faces.bean.*;

import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.ProductPlanCategoryDAO;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.ProductPlanCategory;
import com.maxelyz.core.model.entity.ProductPlanDetail;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.bean.ViewScoped;

@ManagedBean
//@RequestScoped
@ViewScoped
public class ProductPlanEditController {

    private static Log log = LogFactory.getLog(ProductPlanEditController.class);
    private static String SUCCESS = "productplan.xhtml";
    private static String FAILURE = "productplan.xhtml";
    private static String BACK = "productplan.xhtml";
    private static String REDIRECT_PAGE = "product.jsf";

    private ProductPlan productPlan;
    private String mode;

    private Product product;

    //ProductSponsor
    private SelectItem[] typeList;
    private int type = 1;

    private List<ProductPlanDetail> productPlanDetails;
    private Map<String, String> paymentModeList;
    private List<ProductPlanCategory> productPlanCategoryList;
    private List<PaymentMethodStringCastor> productPaymentMethod;
    private Map<String, String> productPaymentMode;

    private String selectedID = "";
    private String productID = "";
    private String message = "";
    private Integer planNoChk;

    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{productPlanDAO}")
    private ProductPlanDAO productPlanDAO;
    @ManagedProperty(value = "#{productPlanCategoryDAO}")
    private ProductPlanCategoryDAO productPlanCategoryDAO;
    
    //------------------------------------------------------------------------------
    private String code1="";
    private String code2="";
    private String code3="";
    private String code4="";
    private String code5="";
    private String code6="";
    private String code7="";
    private String code8="";
    private String code9="";
    private String code10="";
    
    private String descriptionTH1="";
    private String descriptionTH2="";
    private String descriptionTH3="";
    private String descriptionTH4="";
    private String descriptionTH5="";
    private String descriptionTH6="";
    private String descriptionTH7="";
    private String descriptionTH8="";
    private String descriptionTH9="";
    private String descriptionTH10="";
    
    private String descriptionENG1="";
    private String descriptionENG2="";
    private String descriptionENG3="";
    private String descriptionENG4="";
    private String descriptionENG5="";
    private String descriptionENG6="";
    private String descriptionENG7="";
    private String descriptionENG8="";
    private String descriptionENG9="";
    private String descriptionENG10="";
    
    private String sumInsured1="";
    private String sumInsured2="";
    private String sumInsured3="";
    private String sumInsured4="";
    private String sumInsured5="";
    private String sumInsured6="";
    private String sumInsured7="";
    private String sumInsured8="";
    private String sumInsured9="";
    private String sumInsured10="";
    
    private String deduct1="";
    private String deduct2="";
    private String deduct3="";
    private String deduct4="";
    private String deduct5="";
    private String deduct6="";
    private String deduct7="";
    private String deduct8="";
    private String deduct9="";
    private String deduct10="";
    
    private String popCode1="";
    private String popCode2="";
    private String popCode3="";
    private String popCode4="";
    private String popCode5="";
    private String popCode6="";
    private String popCode7="";
    private String popCode8="";
    private String popCode9="";
    private String popCode10="";
    
    private String popNetPremium1="";
    private String popNetPremium2="";
    private String popNetPremium3="";
    private String popNetPremium4="";
    private String popNetPremium5="";
    private String popNetPremium6="";
    private String popNetPremium7="";
    private String popNetPremium8="";
    private String popNetPremium9="";
    private String popNetPremium10="";
    
    private String popSumInsured1="";
    private String popSumInsured2="";
    private String popSumInsured3="";
    private String popSumInsured4="";
    private String popSumInsured5="";
    private String popSumInsured6="";
    private String popSumInsured7="";
    private String popSumInsured8="";
    private String popSumInsured9="";
    private String popSumInsured10="";
    
    private String popDeduct1="";
    private String popDeduct2="";
    private String popDeduct3="";
    private String popDeduct4="";
    private String popDeduct5="";
    private String popDeduct6="";
    private String popDeduct7="";
    private String popDeduct8="";
    private String popDeduct9="";
    private String popDeduct10="";
    
    private String popfx11="";
    private String popfx12="";
    private String popfx13="";
    private String popfx14="";
    private String popfx15="";
    private String popfx16="";
    private String popfx17="";
    private String popfx18="";
    private String popfx19="";
    private String popfx20="";
    
    private Integer seqNoFx;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:productinformation:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        selectedID = JSFUtil.getRequestParameterMap("selectedID");
        productID = JSFUtil.getRequestParameterMap("productID");

        if (productID == null || productID.isEmpty()) {
            JSFUtil.redirect(REDIRECT_PAGE);
            return;
        } else {
            ProductDAO daoProduct = getProductDAO();
            product = daoProduct.findProduct(new Integer(productID));

            if (product.getProductCategory().getCategoryType().equals("motor")) {
                productPlanCategoryList = productPlanCategoryDAO.findProductPlanCategoryEntities();
            }
        }

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            productPlan = new ProductPlan();
            productPlan.setProduct(product);
            productPlan.setMasterPlan(Boolean.TRUE);
            productPlan.setProductPlanCategory(new ProductPlanCategory());
            if (product.getProductCategory().getCategoryType().equals("life")
                    || product.getProductCategory().getCategoryType().equals("nonlife")
                    || product.getProductCategory().getCategoryType().equals("motoradvance")) {
                productPlanDetails = new ArrayList<ProductPlanDetail>();
                for (int i = 0; i < 1; i++) {
                    ProductPlanDetail p = new ProductPlanDetail();
                    p.setSeqNo(i + 1);
                    p.setProductPlan(productPlan);
                    productPlanDetails.add(p);
                }
            }
        } else {
            mode = "edit";
            productPlan = this.getProductPlanDAO().findProductPlan(new Integer(selectedID));
            if (product.getProductCategory().getCategoryType().equals("life")
                    || product.getProductCategory().getCategoryType().equals("nonlife")
                    || product.getProductCategory().getCategoryType().equals("motoradvance")) {
                productPlanDetails = (List<ProductPlanDetail>) productPlan.getProductPlanDetailCollection();     
            }
            planNoChk = productPlan.getNo();
            
//            if(!(productPlan.getFx1().isEmpty() || productPlan.getFx1().equals(null)))
//            {
//                String[] temp_fx = productPlan.getFx1().split("\\|");
//            }
              
                try
                {
                    code1 = productPlan.getFx1().split("\\|")[0];
                }
                catch(Exception ex)
                {
                    code1 = "";
                }
                try
                {
                    descriptionTH1 = productPlan.getFx1().split("\\|")[1];
                }
                catch(Exception ex)
                {
                    descriptionTH1 = "";
                }
                try
                {
                    descriptionENG1 = productPlan.getFx1().split("\\|")[2];
                }
                catch(Exception ex)
                {
                    descriptionENG1 = "";
                }
                try
                {
                    sumInsured1 = productPlan.getFx1().split("\\|")[3];
                }
                catch(Exception ex)
                {
                    sumInsured1 = "";
                }
                try
                {
                    deduct1 = productPlan.getFx1().split("\\|")[4];
                }
                catch(Exception ex)
                {
                    deduct1 = "";
                }
            

                try
                {
                    code2 = productPlan.getFx2().split("\\|")[0];
                }
                catch(Exception ex)
                {
                    code2 = "";
                }
                try
                {
                    descriptionTH2 = productPlan.getFx2().split("\\|")[1];
                }
                catch(Exception ex)
                {
                    descriptionTH2 = "";
                }
                try
                {
                    descriptionENG2 = productPlan.getFx2().split("\\|")[2];
                }
                catch(Exception ex)
                {
                    descriptionENG2 = "";
                }
                try
                {
                    sumInsured2 = productPlan.getFx2().split("\\|")[3];
                }
                catch(Exception ex)
                {
                    sumInsured2 = "";
                }
                try
                {
                    deduct2 = productPlan.getFx2().split("\\|")[4];
                }
                catch(Exception ex)
                {
                    deduct2 = "";
                }
            
            

                try
                {
                    code3 = productPlan.getFx3().split("\\|")[0];
                }
                catch(Exception ex)
                {
                    code3 = "";
                }
                try
                {
                    descriptionTH3 = productPlan.getFx3().split("\\|")[1];
                }
                catch(Exception ex)
                {
                    descriptionTH3 = "";
                }
                try
                {
                    descriptionENG3 = productPlan.getFx3().split("\\|")[2];
                }
                catch(Exception ex)
                {
                    descriptionENG3 = "";
                }
                try
                {
                    sumInsured3 = productPlan.getFx3().split("\\|")[3];
                }
                catch(Exception ex)
                {
                    sumInsured3 = "";
                }
                try
                {
                    deduct3 = productPlan.getFx3().split("\\|")[4];
                }
                catch(Exception ex)
                {
                    deduct3 = "";
                }
            
            
            
                try
                {
                    code4 = productPlan.getFx4().split("\\|")[0];
                }
                catch(Exception ex)
                {
                    code4 = "";
                }
                try
                {
                    descriptionTH4 = productPlan.getFx4().split("\\|")[1];
                }
                catch(Exception ex)
                {
                    descriptionTH4 = "";
                }
                try
                {
                    descriptionENG4 = productPlan.getFx4().split("\\|")[2];
                }
                catch(Exception ex)
                {
                    descriptionENG4 = "";
                }
                try
                {
                    sumInsured4 = productPlan.getFx4().split("\\|")[3];
                }
                catch(Exception ex)
                {
                    sumInsured4 = "";
                }
                try
                {
                    deduct4 = productPlan.getFx4().split("\\|")[4];
                }
                catch(Exception ex)
                {
                    deduct4 = "";
                }
            
            

                try
                {
                    code5 = productPlan.getFx5().split("\\|")[0];
                }
                catch(Exception ex)
                {
                    code5 = "";
                }
                try
                {
                    descriptionTH5 = productPlan.getFx5().split("\\|")[1];
                }
                catch(Exception ex)
                {
                    descriptionTH5 = "";
                }
                try
                {
                    descriptionENG5 = productPlan.getFx5().split("\\|")[2];
                }
                catch(Exception ex)
                {
                    descriptionENG5 = "";
                }
                try
                {
                    sumInsured5 = productPlan.getFx5().split("\\|")[3];
                }
                catch(Exception ex)
                {
                    sumInsured5 = "";
                }
                try
                {
                    deduct5 = productPlan.getFx5().split("\\|")[4];
                }
                catch(Exception ex)
                {
                    deduct5 = "";
                }
            
            

                try
                {
                    code6 = productPlan.getFx6().split("\\|")[0];
                }
                catch(Exception ex)
                {
                    code6 = "";
                }
                try
                {
                    descriptionTH6 = productPlan.getFx6().split("\\|")[1];
                }
                catch(Exception ex)
                {
                    descriptionTH6 = "";
                }
                try
                {
                    descriptionENG6 = productPlan.getFx6().split("\\|")[2];
                }
                catch(Exception ex)
                {
                    descriptionENG6 = "";
                }
                try
                {
                    sumInsured6 = productPlan.getFx6().split("\\|")[3];
                }
                catch(Exception ex)
                {
                    sumInsured6 = "";
                }
                try
                {
                    deduct6 = productPlan.getFx6().split("\\|")[4];
                }
                catch(Exception ex)
                {
                    deduct6 = "";
                }
            
            

                try
                {
                    code7 = productPlan.getFx7().split("\\|")[0];
                }
                catch(Exception ex)
                {
                    code7 = "";
                }
                try
                {
                    descriptionTH7 = productPlan.getFx7().split("\\|")[1];
                }
                catch(Exception ex)
                {
                    descriptionTH7 = "";
                }
                try
                {
                    descriptionENG7 = productPlan.getFx7().split("\\|")[2];
                }
                catch(Exception ex)
                {
                    descriptionENG7 = "";
                }
                try
                {
                    sumInsured7 = productPlan.getFx7().split("\\|")[3];
                }
                catch(Exception ex)
                {
                    sumInsured7 = "";
                }
                try
                {
                    deduct7 = productPlan.getFx7().split("\\|")[4];
                }
                catch(Exception ex)
                {
                    deduct7 = "";
                }
            
            

                try
                {
                    code8 = productPlan.getFx8().split("\\|")[0];
                }
                catch(Exception ex)
                {
                    code8 = "";
                }
                try
                {
                    descriptionTH8 = productPlan.getFx8().split("\\|")[1];
                }
                catch(Exception ex)
                {
                    descriptionTH8 = "";
                }
                try
                {
                    descriptionENG8 = productPlan.getFx8().split("\\|")[2];
                }
                catch(Exception ex)
                {
                    descriptionENG8 = "";
                }
                try
                {
                    sumInsured8 = productPlan.getFx8().split("\\|")[3];
                }
                catch(Exception ex)
                {
                    sumInsured8 = "";
                }
                try
                {
                    deduct8 = productPlan.getFx8().split("\\|")[4];
                }
                catch(Exception ex)
                {
                    deduct8 = "";
                }
            
            

                try
                {
                    code9 = productPlan.getFx9().split("\\|")[0];
                }
                catch(Exception ex)
                {
                    code9 = "";
                }
                try
                {
                    descriptionTH9 = productPlan.getFx9().split("\\|")[1];
                }
                catch(Exception ex)
                {
                    descriptionTH9 = "";
                }
                try
                {
                    descriptionENG9 = productPlan.getFx9().split("\\|")[2];
                }
                catch(Exception ex)
                {
                    descriptionENG9 = "";
                }
                try
                {
                    sumInsured9 = productPlan.getFx9().split("\\|")[3];
                }
                catch(Exception ex)
                {
                    sumInsured9 = "";
                }
                try
                {
                    deduct9 = productPlan.getFx9().split("\\|")[4];
                }
                catch(Exception ex)
                {
                    deduct9 = "";
                }
               
            

                try
                {
                    code10 = productPlan.getFx10().split("\\|")[0];
                }
                catch(Exception ex)
                {
                    code10 = "";
                }
                try
                {
                    descriptionTH10 = productPlan.getFx10().split("\\|")[1];
                }
                catch(Exception ex)
                {
                    descriptionTH10 = "";
                }
                try
                {
                    descriptionENG10 = productPlan.getFx10().split("\\|")[2];
                }
                catch(Exception ex)
                {
                    descriptionENG10 = "";
                }
                try
                {
                    sumInsured10 = productPlan.getFx10().split("\\|")[3];
                }
                catch(Exception ex)
                {
                    sumInsured10 = "";
                }
                try
                {
                    deduct10 = productPlan.getFx10().split("\\|")[4];
                }
                catch(Exception ex)
                {
                    deduct10 = "";
                }
                
            
        }
        try {
            loadForm();
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
        //this.setPaymentModeList();   
    }

    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:productinformation:add");
        } else {
            return SecurityUtil.isPermitted("admin:productinformation:edit");
        }
    }

    public String saveAction() {
        try {
            message = "";
            productPlan.setProductPlanCategory(null);
            productPlan.setEnable(true);
            productPlan.setProductPlanDetailCollection(productPlanDetails);
            
            if(!(code1.trim().isEmpty()&&descriptionTH1.trim().isEmpty()&&descriptionENG1.trim().isEmpty()&&sumInsured1.trim().isEmpty()&&deduct1.trim().isEmpty()))
            {
                productPlan.setFx1(code1+"|"+descriptionTH1+"|"+descriptionENG1+"|"+sumInsured1+"|"+deduct1);
            }
            else
            {
                productPlan.setFx1("");
            }
            
            if(!(code2.trim().isEmpty()&&descriptionTH2.trim().isEmpty()&&descriptionENG2.trim().isEmpty()&&sumInsured2.trim().isEmpty()&&deduct2.trim().isEmpty()))
            {
                productPlan.setFx2(code2+"|"+descriptionTH2+"|"+descriptionENG2+"|"+sumInsured2+"|"+deduct2);
            }
            else
            {
                productPlan.setFx2("");
            }
            
            if(!(code3.trim().isEmpty()&&descriptionTH3.trim().isEmpty()&&descriptionENG3.trim().isEmpty()&&sumInsured3.trim().isEmpty()&&deduct3.trim().isEmpty()))
            {
                productPlan.setFx3(code3+"|"+descriptionTH3+"|"+descriptionENG3+"|"+sumInsured3+"|"+deduct3);
            }
            else
            {
                productPlan.setFx3("");
            }
            
            if(!(code4.trim().isEmpty()&&descriptionTH4.trim().isEmpty()&&descriptionENG4.trim().isEmpty()&&sumInsured4.trim().isEmpty()&&deduct4.trim().isEmpty()))
            {
                productPlan.setFx4(code4+"|"+descriptionTH4+"|"+descriptionENG4+"|"+sumInsured4+"|"+deduct4);
            }
            else
            {
                productPlan.setFx4("");
            }
            
            if(!(code5.trim().isEmpty()&&descriptionTH5.trim().isEmpty()&&descriptionENG5.trim().isEmpty()&&sumInsured5.trim().isEmpty()&&deduct5.trim().isEmpty()))
            {
                productPlan.setFx5(code5+"|"+descriptionTH5+"|"+descriptionENG5+"|"+sumInsured5+"|"+deduct5);
            }
            else
            {
                productPlan.setFx5("");
            }
            
            if(!(code6.trim().isEmpty()&&descriptionTH6.trim().isEmpty()&&descriptionENG6.trim().isEmpty()&&sumInsured6.trim().isEmpty()&&deduct6.trim().isEmpty()))
            {
                productPlan.setFx6(code6+"|"+descriptionTH6+"|"+descriptionENG6+"|"+sumInsured6+"|"+deduct6);
            }
            else
            {
                productPlan.setFx6("");
            }
            
            if(!(code7.trim().isEmpty()&&descriptionTH7.trim().isEmpty()&&descriptionENG7.trim().isEmpty()&&sumInsured7.trim().isEmpty()&&deduct7.trim().isEmpty()))
            {
                productPlan.setFx7(code7+"|"+descriptionTH7+"|"+descriptionENG7+"|"+sumInsured7+"|"+deduct7);
            }
            else
            {
                productPlan.setFx7("");
            }
            
            if(!(code8.trim().isEmpty()&&descriptionTH8.trim().isEmpty()&&descriptionENG8.trim().isEmpty()&&sumInsured8.trim().isEmpty()&&deduct8.trim().isEmpty()))
            {
                productPlan.setFx8(code8+"|"+descriptionTH8+"|"+descriptionENG8+"|"+sumInsured8+"|"+deduct8);
            }
            else
            {
                productPlan.setFx8("");
            }
            
            if(!(code9.trim().isEmpty()&&descriptionTH9.trim().isEmpty()&&descriptionENG9.trim().isEmpty()&&sumInsured9.trim().isEmpty()&&deduct9.trim().isEmpty()))
            {
                productPlan.setFx9(code9+"|"+descriptionTH9+"|"+descriptionENG9+"|"+sumInsured9+"|"+deduct9);
            }
            else
            {
                productPlan.setFx9("");
            }
            
            if(!(code10.trim().isEmpty()&&descriptionTH10.trim().isEmpty()&&descriptionENG10.trim().isEmpty()&&sumInsured10.trim().isEmpty()&&deduct10.trim().isEmpty()))
            {
                productPlan.setFx10(code10+"|"+descriptionTH10+"|"+descriptionENG10+"|"+sumInsured10+"|"+deduct10);
            }
            else
            {
                productPlan.setFx10("");
            }
            
            //log.error("mode=" + product.getPaymentMode());
            if (getMode().equals("add")) {
                List<ProductPlan> pl = this.getProductPlanDAO().findProductPlanByNoAndProductId(productPlan.getNo(), product.getId());
                if (pl != null && pl.size() > 0) {
                    message = "Plan no can not duplicate";
                    return null;
                }
                productPlan.setId(null);
                productPlanDAO.create(productPlan);
            } else {
                if (planNoChk.intValue() != productPlan.getNo().intValue()) {
                    List<ProductPlan> pl = this.getProductPlanDAO().findProductPlanByNoAndProductId(productPlan.getNo(), product.getId());
                    if (pl != null && pl.size() > 0) {
                        message = "Plan no can not duplicate";
                        return null;
                    }
                }
                productPlanDAO.edit(productPlan);
            }
        } catch (Exception e) {
            log.error(e);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String saveMotorAction() {
        try {
            productPlan.setEnable(true);

            if (getMode().equals("add")) {
                productPlan.setId(null);
                this.getProductPlanDAO().create(productPlan);
            } else {
                this.getProductPlanDAO().edit(productPlan);
            }
        } catch (Exception e) {
            log.error(e);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String backAction() {
        return BACK;
    }

    //Listener
    public void initListener(ActionEvent e) {
        initialize();
    }

    public void addProductPlanDetailListener(ActionEvent e) {
        int size = productPlanDetails.size();
        ProductPlanDetail p = new ProductPlanDetail();
        p.setSeqNo(size + 1);
        p.setProductPlan(productPlan);
        productPlanDetails.add(p);
    }

    private void reCalculateSeqNo() {
        int seqNo = 0;
        for (ProductPlanDetail p : productPlanDetails) {
            p.setSeqNo(++seqNo);
        }
    }

    public void insertProductPlanDetailListener(ActionEvent e) {
        int seqNo = Integer.parseInt(JSFUtil.getRequestParameterMap("seqNo"));
        ProductPlanDetail p = new ProductPlanDetail();
        p.setProductPlan(productPlan);
        productPlanDetails.add(seqNo - 1, p);
        //Regenerate Seq No
        reCalculateSeqNo();
    }

    public void deleteProductPlanDetailListener(ActionEvent e) {
        int seqNo = Integer.parseInt(JSFUtil.getRequestParameterMap("seqNo"));
        productPlanDetails.remove(seqNo - 1);
        //Regenerate Seq No
        reCalculateSeqNo();
    }

    @Deprecated
    public void setPaymentModeList() {
        paymentModeList = new LinkedHashMap<String, String>();
        String[] stra = null;
        if (!product.getPaymentMode().isEmpty()) {
            StringTokenizer st = new StringTokenizer(product.getPaymentMode(), ",");
            while (st.hasMoreTokens()) {
                Integer token = Integer.parseInt((String) st.nextToken());
                stra = new String[2];
                switch (token) {
                    case 1:
                        paymentModeList.put("Month", "1");
                        break;
                    case 2:
                        paymentModeList.put("Quarter", "2");
                        break;
                    case 3:
                        paymentModeList.put("Half Year", "3");
                        break;
                    case 4:
                        paymentModeList.put("Year", "4");
                        break;
                    default:
                    //log.error("Payment Mode-Invalid Entry");

                }

            }
        }
    }

    @Deprecated
    public Map<String, String> getPaymentModeList() {
        return paymentModeList;
    }

    public ProductPlan getProductPlan() {
        return productPlan;
    }

    public void setProductPlan(ProductPlan productPlan) {
        this.productPlan = productPlan;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
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
     * @return the typeList
     */
    public SelectItem[] getTypeList() {
        typeList = new SelectItem[1];
        typeList[0] = new SelectItem(1, "Age");
        return typeList;
    }

    /**
     * @param typeList the typeList to set
     */
    public void setTypeList(SelectItem[] typeList) {
        this.typeList = typeList;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param mode the mode to set
     */
    public void setType(int type) {
        this.type = type;
    }

    public List<ProductPlanDetail> getProductPlanDetails() {
        return productPlanDetails;
    }

    public void setProductPlanDetails(List<ProductPlanDetail> productPlanDetails) {
        this.productPlanDetails = productPlanDetails;
    }

    //Managed Propertiess
    public ProductPlanDAO getProductPlanDAO() {
        return productPlanDAO;
    }

    public void setProductPlanDAO(ProductPlanDAO productPlanDAO) {
        this.productPlanDAO = productPlanDAO;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<ProductPlanCategory> getProductPlanCategoryList() {
        return productPlanCategoryList;
    }

    public void setProductPlanCategoryList(List<ProductPlanCategory> productPlanCategoryList) {
        this.productPlanCategoryList = productPlanCategoryList;
    }

    public ProductPlanCategoryDAO getProductPlanCategoryDAO() {
        return productPlanCategoryDAO;
    }

    public void setProductPlanCategoryDAO(ProductPlanCategoryDAO productPlanCategoryDAO) {
        this.productPlanCategoryDAO = productPlanCategoryDAO;
    }

    public Map<String, String> getProductPaymentMode() {
        return productPaymentMode;
    }

    public List<PaymentMethodStringCastor> getProductPaymentMethod() {
        return productPaymentMethod;
    }

    /* *********** Utility Method ****************** */
    protected void loadForm() throws Exception {
        // retreive lookup data
        this.productPaymentMode = new LinkedHashMap<String, String>();
        if (product != null && product.getPaymentMode() != null) {
            this.productPaymentMethod = new ArrayList<PaymentMethodStringCastor>();
            try {
                this.productPaymentMethod = PaymentModeConverter.convertToPaymentModeList(product.getPaymentMode());
            } catch (Exception e) {
                log.error(e);
            }
            this.productPaymentMode = PaymentModeConverter.findProductPaymentMode(this.productPaymentMethod);
            System.out.println("loadForm = paymentMode > " + this.product.getPaymentMode());
            System.out.println("loadForm = paymentMethod > " + this.productPaymentMethod);
            System.out.println("loadForm = paymentMode list > " + this.productPaymentMode);

        }
    }
    
    public void onClickPopupFlexFieldListener()
    {
        seqNoFx = Integer.parseInt(JSFUtil.getRequestParameterMap("seqNo1"))-1;
        
        try{
            popCode1 = productPlanDetails.get(seqNoFx).getFx1().split("\\|")[0];
        }
        catch (Exception ex)
        {
            popCode1 = "";
//            System.out.println("Click Popcode1 : "+popCode1);
        }
        try{
            popNetPremium1 = productPlanDetails.get(seqNoFx).getFx1().split("\\|")[1];
        }
        catch (Exception ex)
        {
            popNetPremium1 = "";
        }
        
        try{
            popSumInsured1 = productPlanDetails.get(seqNoFx).getFx1().split("\\|")[2];
        }
        catch (Exception ex)
        {
            popSumInsured1 = "";
        }
        try{
            popDeduct1 = productPlanDetails.get(seqNoFx).getFx1().split("\\|")[3];
        }
        catch (Exception ex)
        {
            popDeduct1 = "";
        }
        
        try{
            popCode2 = productPlanDetails.get(seqNoFx).getFx2().split("\\|")[0];
        }
        catch (Exception ex)
        {
            popCode2 = "";
        }
        try{
            popNetPremium2 = productPlanDetails.get(seqNoFx).getFx2().split("\\|")[1];
        }
        catch (Exception ex)
        {
            popNetPremium2 = "";
        }
       
        try{
            popSumInsured2 = productPlanDetails.get(seqNoFx).getFx2().split("\\|")[2];
        }
        catch (Exception ex)
        {
            popSumInsured2 = "";
        }
        try{
            popDeduct2 = productPlanDetails.get(seqNoFx).getFx2().split("\\|")[3];
        }
        catch (Exception ex)
        {
            popDeduct2 = "";
        }
        
        try{
            popCode3 = productPlanDetails.get(seqNoFx).getFx3().split("\\|")[0];
        }
        catch (Exception ex)
        {
            popCode3 = "";
        }
        try{
            popNetPremium3 = productPlanDetails.get(seqNoFx).getFx3().split("\\|")[1];
        }
        catch (Exception ex)
        {
            popNetPremium3 = "";
        }
                
        try{
            popSumInsured3 = productPlanDetails.get(seqNoFx).getFx3().split("\\|")[2];
        }
        catch (Exception ex)
        {
            popSumInsured3 = "";
        }
        try{
            popDeduct3 = productPlanDetails.get(seqNoFx).getFx3().split("\\|")[3];
        }
        catch (Exception ex)
        {
            popDeduct3 = "";
        }
        
        try{
            popCode4 = productPlanDetails.get(seqNoFx).getFx4().split("\\|")[0];
        }
        catch (Exception ex)
        {
            popCode4 = "";
        }
        try{
            popNetPremium4 = productPlanDetails.get(seqNoFx).getFx4().split("\\|")[1];
        }
        catch (Exception ex)
        {
            popNetPremium4 = "";
        }
        
        try{
            popSumInsured4 = productPlanDetails.get(seqNoFx).getFx4().split("\\|")[2];
        }
        catch (Exception ex)
        {
            popSumInsured4 = "";
        }
        try{
            popDeduct4 = productPlanDetails.get(seqNoFx).getFx4().split("\\|")[3];
        }
        catch (Exception ex)
        {
            popDeduct4 = "";
        }
        
        try{
            popCode5 = productPlanDetails.get(seqNoFx).getFx5().split("\\|")[0];
        }
        catch (Exception ex)
        {
            popCode5 = "";
        }
        try{
            popNetPremium5 = productPlanDetails.get(seqNoFx).getFx5().split("\\|")[1];
        }
        catch (Exception ex)
        {
            popNetPremium5 = "";
        }
                
        try{
            popSumInsured5 = productPlanDetails.get(seqNoFx).getFx5().split("\\|")[2];
        }
        catch (Exception ex)
        {
            popSumInsured5 = "";
        }
        try{
            popDeduct5 = productPlanDetails.get(seqNoFx).getFx5().split("\\|")[3];
        }
        catch (Exception ex)
        {
            popDeduct5 = "";
        }
        
        try{
            popCode6 = productPlanDetails.get(seqNoFx).getFx6().split("\\|")[0];
        }
        catch (Exception ex)
        {
            popCode6 = "";
        }
        try{
            popNetPremium6 = productPlanDetails.get(seqNoFx).getFx6().split("\\|")[1];
        }
        catch (Exception ex)
        {
            popNetPremium6 = "";
        }
                
        try{
            popSumInsured6 = productPlanDetails.get(seqNoFx).getFx6().split("\\|")[2];
        }
        catch (Exception ex)
        {
            popSumInsured6 = "";
        }
        try{
            popDeduct6 = productPlanDetails.get(seqNoFx).getFx6().split("\\|")[3];
        }
        catch (Exception ex)
        {
            popDeduct6 = "";
        }
        
        try{
            popCode7 = productPlanDetails.get(seqNoFx).getFx7().split("\\|")[0];
        }
        catch (Exception ex)
        {
            popCode7 = "";
        }
        try{
            popNetPremium7 = productPlanDetails.get(seqNoFx).getFx7().split("\\|")[1];
        }
        catch (Exception ex)
        {
            popNetPremium7 = "";
        }
   
        try{
            popSumInsured7 = productPlanDetails.get(seqNoFx).getFx7().split("\\|")[2];
        }
        catch (Exception ex)
        {
            popSumInsured7 = "";
        }
        try{
            popDeduct7 = productPlanDetails.get(seqNoFx).getFx7().split("\\|")[3];
        }
        catch (Exception ex)
        {
            popDeduct7 = "";
        }
        
        try{
            popCode8= productPlanDetails.get(seqNoFx).getFx8().split("\\|")[0];
        }
        catch (Exception ex)
        {
            popCode8 = "";
        }
        try{
            popNetPremium8 = productPlanDetails.get(seqNoFx).getFx8().split("\\|")[1];
        }
        catch (Exception ex)
        {
            popNetPremium8 = "";
        }
                
        try{
            popSumInsured8 = productPlanDetails.get(seqNoFx).getFx8().split("\\|")[2];
        }
        catch (Exception ex)
        {
            popSumInsured8 = "";
        }
        try{
            popDeduct8 = productPlanDetails.get(seqNoFx).getFx8().split("\\|")[3];
        }
        catch (Exception ex)
        {
            popDeduct8 = "";
        }
        
        try{
            popCode9 = productPlanDetails.get(seqNoFx).getFx9().split("\\|")[0];
        }
        catch (Exception ex)
        {
            popCode9 = "";
        }
        try{
            popNetPremium9 = productPlanDetails.get(seqNoFx).getFx9().split("\\|")[1];
        }
        catch (Exception ex)
        {
            popNetPremium9 = "";
        }
                
        try{
            popSumInsured9 = productPlanDetails.get(seqNoFx).getFx9().split("\\|")[2];
        }
        catch (Exception ex)
        {
            popSumInsured9 = "";
        }
        try{
            popDeduct9 = productPlanDetails.get(seqNoFx).getFx9().split("\\|")[3];
        }
        catch (Exception ex)
        {
            popDeduct9 = "";
        }
        
        try{
            popCode10 = productPlanDetails.get(seqNoFx).getFx10().split("\\|")[0];
        }
        catch (Exception ex)
        {
            popCode10 = "";
        }
        try{
            popNetPremium10 = productPlanDetails.get(seqNoFx).getFx10().split("\\|")[1];
        }
        catch (Exception ex)
        {
            popNetPremium10 = "";
        }
                
        try{
            popSumInsured10 = productPlanDetails.get(seqNoFx).getFx10().split("\\|")[2];
        }
        catch (Exception ex)
        {
            popSumInsured10 = "";
        }
        try{
            popDeduct10 = productPlanDetails.get(seqNoFx).getFx10().split("\\|")[3];
        }
        catch (Exception ex)
        {
            popDeduct10 = "";
        }
        
        popfx11 = productPlanDetails.get(seqNoFx).getFx11();
        popfx12 = productPlanDetails.get(seqNoFx).getFx12();
        popfx13 = productPlanDetails.get(seqNoFx).getFx13();
        popfx14 = productPlanDetails.get(seqNoFx).getFx14();
        popfx15 = productPlanDetails.get(seqNoFx).getFx15();
        popfx16 = productPlanDetails.get(seqNoFx).getFx16();
        popfx17 = productPlanDetails.get(seqNoFx).getFx17();
        popfx18 = productPlanDetails.get(seqNoFx).getFx18();
        popfx19 = productPlanDetails.get(seqNoFx).getFx19();
        popfx20 = productPlanDetails.get(seqNoFx).getFx20();
        
    }
    
    public void onClickPopupFlexFieldListener1(ActionEvent event)
    {

            System.out.println("Success 123456 ...");
        
    }
    
    public void onClosePopupListener()
    {
//        System.out.println("Close Popup");
//        System.out.println("popCode1 : "+popCode1);
//        System.out.println("popNetPremium : "+popNetPremium1);
        if(!(popCode1.trim().isEmpty()&&popNetPremium1.trim().isEmpty()&&popSumInsured1.trim().isEmpty()&&popDeduct1.trim().isEmpty()))
        {
            productPlanDetails.get(seqNoFx).setFx1(popCode1+"|"+popNetPremium1+"|"+popSumInsured1+"|"+popDeduct1);
        }
        else
        {
            productPlanDetails.get(seqNoFx).setFx1("");
        }
        if(!(popCode2.trim().isEmpty()&&popNetPremium2.trim().isEmpty()&&popSumInsured2.trim().isEmpty()&&popDeduct2.trim().isEmpty()))
        {
            productPlanDetails.get(seqNoFx).setFx2(popCode2+"|"+popNetPremium2+"|"+popSumInsured2+"|"+popDeduct2);
        }
        else
        {
            productPlanDetails.get(seqNoFx).setFx2("");
        }
        if(!(popCode3.trim().isEmpty()&&popNetPremium3.trim().isEmpty()&&popSumInsured3.trim().isEmpty()&&popDeduct3.trim().isEmpty()))
        {
            productPlanDetails.get(seqNoFx).setFx3(popCode3+"|"+popNetPremium3+"|"+popSumInsured3+"|"+popDeduct3);
        }
        else
        {
            productPlanDetails.get(seqNoFx).setFx3("");
        }
        if(!(popCode4.trim().isEmpty()&&popNetPremium4.trim().isEmpty()&&popSumInsured4.trim().isEmpty()&&popDeduct4.trim().isEmpty()))
        {
            productPlanDetails.get(seqNoFx).setFx4(popCode4+"|"+popNetPremium4+"|"+popSumInsured4+"|"+popDeduct4);
        }
        else
        {
            productPlanDetails.get(seqNoFx).setFx4("");
        }
        if(!(popCode5.trim().isEmpty()&&popNetPremium5.trim().isEmpty()&&popSumInsured5.trim().isEmpty()&&popDeduct5.trim().isEmpty()))
        {
            productPlanDetails.get(seqNoFx).setFx5(popCode5+"|"+popNetPremium5+"|"+popSumInsured5+"|"+popDeduct5);
        }
        else
        {
            productPlanDetails.get(seqNoFx).setFx5("");
        }
        if(!(popCode6.trim().isEmpty()&&popNetPremium6.trim().isEmpty()&&popSumInsured6.trim().isEmpty()&&popDeduct6.trim().isEmpty()))
        {
            productPlanDetails.get(seqNoFx).setFx6(popCode6+"|"+popNetPremium6+"|"+popSumInsured6+"|"+popDeduct6);
        }
        else
        {
            productPlanDetails.get(seqNoFx).setFx6("");
        }
        if(!(popCode7.trim().isEmpty()&&popNetPremium7.trim().isEmpty()&&popSumInsured7.trim().isEmpty()&&popDeduct7.trim().isEmpty()))
        {
            productPlanDetails.get(seqNoFx).setFx7(popCode7+"|"+popNetPremium7+"|"+popSumInsured7+"|"+popDeduct7);
        }
        else
        {
            productPlanDetails.get(seqNoFx).setFx7("");
        }
        if(!(popCode8.trim().isEmpty()&&popNetPremium8.trim().isEmpty()&&popSumInsured8.trim().isEmpty()&&popDeduct8.trim().isEmpty()))
        {
            productPlanDetails.get(seqNoFx).setFx8(popCode8+"|"+popNetPremium8+"|"+popSumInsured8+"|"+popDeduct8);
        }
        else
        {
            productPlanDetails.get(seqNoFx).setFx8("");
        }
        if(!(popCode9.trim().isEmpty()&&popNetPremium9.trim().isEmpty()&&popSumInsured9.trim().isEmpty()&&popDeduct9.trim().isEmpty()))
        {
            productPlanDetails.get(seqNoFx).setFx9(popCode9+"|"+popNetPremium9+"|"+popSumInsured9+"|"+popDeduct9);
        }
        else
        {
            productPlanDetails.get(seqNoFx).setFx9("");
        }
        if(!(popCode10.trim().isEmpty()&&popNetPremium10.trim().isEmpty()&&popSumInsured10.trim().isEmpty()&&popDeduct10.trim().isEmpty()))
        {
            productPlanDetails.get(seqNoFx).setFx10(popCode10+"|"+popNetPremium10+"|"+popSumInsured10+"|"+popDeduct10);
        }
        else
        {
            productPlanDetails.get(seqNoFx).setFx10("");
        }
        productPlanDetails.get(seqNoFx).setFx11(popfx11.trim());
        productPlanDetails.get(seqNoFx).setFx12(popfx12.trim());
        productPlanDetails.get(seqNoFx).setFx13(popfx13.trim());
        productPlanDetails.get(seqNoFx).setFx14(popfx14.trim());
        productPlanDetails.get(seqNoFx).setFx15(popfx15.trim());
        productPlanDetails.get(seqNoFx).setFx16(popfx16.trim());
        productPlanDetails.get(seqNoFx).setFx17(popfx17.trim());
        productPlanDetails.get(seqNoFx).setFx18(popfx18.trim());
        productPlanDetails.get(seqNoFx).setFx19(popfx19.trim());
        productPlanDetails.get(seqNoFx).setFx20(popfx20.trim());
        
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getCode3() {
        return code3;
    }

    public void setCode3(String code3) {
        this.code3 = code3;
    }

    public String getCode4() {
        return code4;
    }

    public void setCode4(String code4) {
        this.code4 = code4;
    }

    public String getCode5() {
        return code5;
    }

    public void setCode5(String code5) {
        this.code5 = code5;
    }

    public String getCode6() {
        return code6;
    }

    public void setCode6(String code6) {
        this.code6 = code6;
    }

    public String getCode7() {
        return code7;
    }

    public void setCode7(String code7) {
        this.code7 = code7;
    }

    public String getCode8() {
        return code8;
    }

    public void setCode8(String code8) {
        this.code8 = code8;
    }

    public String getCode9() {
        return code9;
    }

    public void setCode9(String code9) {
        this.code9 = code9;
    }

    public String getCode10() {
        return code10;
    }

    public void setCode10(String code10) {
        this.code10 = code10;
    }

    public String getDescriptionTH1() {
        return descriptionTH1;
    }

    public void setDescriptionTH1(String descriptionTH1) {
        this.descriptionTH1 = descriptionTH1;
    }

    public String getDescriptionTH2() {
        return descriptionTH2;
    }

    public void setDescriptionTH2(String descriptionTH2) {
        this.descriptionTH2 = descriptionTH2;
    }

    public String getDescriptionTH3() {
        return descriptionTH3;
    }

    public void setDescriptionTH3(String descriptionTH3) {
        this.descriptionTH3 = descriptionTH3;
    }

    public String getDescriptionTH4() {
        return descriptionTH4;
    }

    public void setDescriptionTH4(String descriptionTH4) {
        this.descriptionTH4 = descriptionTH4;
    }

    public String getDescriptionTH5() {
        return descriptionTH5;
    }

    public void setDescriptionTH5(String descriptionTH5) {
        this.descriptionTH5 = descriptionTH5;
    }

    public String getDescriptionTH6() {
        return descriptionTH6;
    }

    public void setDescriptionTH6(String descriptionTH6) {
        this.descriptionTH6 = descriptionTH6;
    }

    public String getDescriptionTH7() {
        return descriptionTH7;
    }

    public void setDescriptionTH7(String descriptionTH7) {
        this.descriptionTH7 = descriptionTH7;
    }

    public String getDescriptionTH8() {
        return descriptionTH8;
    }

    public void setDescriptionTH8(String descriptionTH8) {
        this.descriptionTH8 = descriptionTH8;
    }

    public String getDescriptionTH9() {
        return descriptionTH9;
    }

    public void setDescriptionTH9(String descriptionTH9) {
        this.descriptionTH9 = descriptionTH9;
    }

    public String getDescriptionTH10() {
        return descriptionTH10;
    }

    public void setDescriptionTH10(String descriptionTH10) {
        this.descriptionTH10 = descriptionTH10;
    }

    public String getDescriptionENG1() {
        return descriptionENG1;
    }

    public void setDescriptionENG1(String descriptionENG1) {
        this.descriptionENG1 = descriptionENG1;
    }

    public String getDescriptionENG2() {
        return descriptionENG2;
    }

    public void setDescriptionENG2(String descriptionENG2) {
        this.descriptionENG2 = descriptionENG2;
    }

    public String getDescriptionENG3() {
        return descriptionENG3;
    }

    public void setDescriptionENG3(String descriptionENG3) {
        this.descriptionENG3 = descriptionENG3;
    }

    public String getDescriptionENG4() {
        return descriptionENG4;
    }

    public void setDescriptionENG4(String descriptionENG4) {
        this.descriptionENG4 = descriptionENG4;
    }

    public String getDescriptionENG5() {
        return descriptionENG5;
    }

    public void setDescriptionENG5(String descriptionENG5) {
        this.descriptionENG5 = descriptionENG5;
    }

    public String getDescriptionENG6() {
        return descriptionENG6;
    }

    public void setDescriptionENG6(String descriptionENG6) {
        this.descriptionENG6 = descriptionENG6;
    }

    public String getDescriptionENG7() {
        return descriptionENG7;
    }

    public void setDescriptionENG7(String descriptionENG7) {
        this.descriptionENG7 = descriptionENG7;
    }

    public String getDescriptionENG8() {
        return descriptionENG8;
    }

    public void setDescriptionENG8(String descriptionENG8) {
        this.descriptionENG8 = descriptionENG8;
    }

    public String getDescriptionENG9() {
        return descriptionENG9;
    }

    public void setDescriptionENG9(String descriptionENG9) {
        this.descriptionENG9 = descriptionENG9;
    }

    public String getDescriptionENG10() {
        return descriptionENG10;
    }

    public void setDescriptionENG10(String descriptionENG10) {
        this.descriptionENG10 = descriptionENG10;
    }

    public String getSumInsured1() {
        return sumInsured1;
    }

    public void setSumInsured1(String sumInsured1) {
        this.sumInsured1 = sumInsured1;
    }

    public String getSumInsured2() {
        return sumInsured2;
    }

    public void setSumInsured2(String sumInsured2) {
        this.sumInsured2 = sumInsured2;
    }

    public String getSumInsured3() {
        return sumInsured3;
    }

    public void setSumInsured3(String sumInsured3) {
        this.sumInsured3 = sumInsured3;
    }

    public String getSumInsured4() {
        return sumInsured4;
    }

    public void setSumInsured4(String sumInsured4) {
        this.sumInsured4 = sumInsured4;
    }

    public String getSumInsured5() {
        return sumInsured5;
    }

    public void setSumInsured5(String sumInsured5) {
        this.sumInsured5 = sumInsured5;
    }

    public String getSumInsured6() {
        return sumInsured6;
    }

    public void setSumInsured6(String sumInsured6) {
        this.sumInsured6 = sumInsured6;
    }

    public String getSumInsured7() {
        return sumInsured7;
    }

    public void setSumInsured7(String sumInsured7) {
        this.sumInsured7 = sumInsured7;
    }

    public String getSumInsured8() {
        return sumInsured8;
    }

    public void setSumInsured8(String sumInsured8) {
        this.sumInsured8 = sumInsured8;
    }

    public String getSumInsured9() {
        return sumInsured9;
    }

    public void setSumInsured9(String sumInsured9) {
        this.sumInsured9 = sumInsured9;
    }

    public String getSumInsured10() {
        return sumInsured10;
    }

    public void setSumInsured10(String sumInsured10) {
        this.sumInsured10 = sumInsured10;
    }

    public String getDeduct1() {
        return deduct1;
    }

    public void setDeduct1(String deduct1) {
        this.deduct1 = deduct1;
    }

    public String getDeduct2() {
        return deduct2;
    }

    public void setDeduct2(String deduct2) {
        this.deduct2 = deduct2;
    }

    public String getDeduct3() {
        return deduct3;
    }

    public void setDeduct3(String deduct3) {
        this.deduct3 = deduct3;
    }

    public String getDeduct4() {
        return deduct4;
    }

    public void setDeduct4(String deduct4) {
        this.deduct4 = deduct4;
    }

    public String getDeduct5() {
        return deduct5;
    }

    public void setDeduct5(String deduct5) {
        this.deduct5 = deduct5;
    }

    public String getDeduct6() {
        return deduct6;
    }

    public void setDeduct6(String deduct6) {
        this.deduct6 = deduct6;
    }

    public String getDeduct7() {
        return deduct7;
    }

    public void setDeduct7(String deduct7) {
        this.deduct7 = deduct7;
    }

    public String getDeduct8() {
        return deduct8;
    }

    public void setDeduct8(String deduct8) {
        this.deduct8 = deduct8;
    }

    public String getDeduct9() {
        return deduct9;
    }

    public void setDeduct9(String deduct9) {
        this.deduct9 = deduct9;
    }

    public String getDeduct10() {
        return deduct10;
    }

    public void setDeduct10(String deduct10) {
        this.deduct10 = deduct10;
    }

    public String getPopCode1() {
        return popCode1;
    }

    public void setPopCode1(String popCode1) {
        this.popCode1 = popCode1;
    }

    public String getPopCode2() {
        return popCode2;
    }

    public void setPopCode2(String popCode2) {
        this.popCode2 = popCode2;
    }

    public String getPopCode3() {
        return popCode3;
    }

    public void setPopCode3(String popCode3) {
        this.popCode3 = popCode3;
    }

    public String getPopCode4() {
        return popCode4;
    }

    public void setPopCode4(String popCode4) {
        this.popCode4 = popCode4;
    }

    public String getPopCode5() {
        return popCode5;
    }

    public void setPopCode5(String popCode5) {
        this.popCode5 = popCode5;
    }

    public String getPopCode6() {
        return popCode6;
    }

    public void setPopCode6(String popCode6) {
        this.popCode6 = popCode6;
    }

    public String getPopCode7() {
        return popCode7;
    }

    public void setPopCode7(String popCode7) {
        this.popCode7 = popCode7;
    }

    public String getPopCode8() {
        return popCode8;
    }

    public void setPopCode8(String popCode8) {
        this.popCode8 = popCode8;
    }

    public String getPopCode9() {
        return popCode9;
    }

    public void setPopCode9(String popCode9) {
        this.popCode9 = popCode9;
    }

    public String getPopCode10() {
        return popCode10;
    }

    public void setPopCode10(String popCode10) {
        this.popCode10 = popCode10;
    }

    public String getPopNetPremium1() {
        return popNetPremium1;
    }

    public void setPopNetPremium1(String popNetPremium1) {
        this.popNetPremium1 = popNetPremium1;
    }

    public String getPopNetPremium2() {
        return popNetPremium2;
    }

    public void setPopNetPremium2(String popNetPremium2) {
        this.popNetPremium2 = popNetPremium2;
    }

    public String getPopNetPremium3() {
        return popNetPremium3;
    }

    public void setPopNetPremium3(String popNetPremium3) {
        this.popNetPremium3 = popNetPremium3;
    }

    public String getPopNetPremium4() {
        return popNetPremium4;
    }

    public void setPopNetPremium4(String popNetPremium4) {
        this.popNetPremium4 = popNetPremium4;
    }

    public String getPopNetPremium5() {
        return popNetPremium5;
    }

    public void setPopNetPremium5(String popNetPremium5) {
        this.popNetPremium5 = popNetPremium5;
    }

    public String getPopNetPremium6() {
        return popNetPremium6;
    }

    public void setPopNetPremium6(String popNetPremium6) {
        this.popNetPremium6 = popNetPremium6;
    }

    public String getPopNetPremium7() {
        return popNetPremium7;
    }

    public void setPopNetPremium7(String popNetPremium7) {
        this.popNetPremium7 = popNetPremium7;
    }

    public String getPopNetPremium8() {
        return popNetPremium8;
    }

    public void setPopNetPremium8(String popNetPremium8) {
        this.popNetPremium8 = popNetPremium8;
    }

    public String getPopNetPremium9() {
        return popNetPremium9;
    }

    public void setPopNetPremium9(String popNetPremium9) {
        this.popNetPremium9 = popNetPremium9;
    }

    public String getPopNetPremium10() {
        return popNetPremium10;
    }

    public void setPopNetPremium10(String popNetPremium10) {
        this.popNetPremium10 = popNetPremium10;
    }

    public String getPopfx11() {
        return popfx11;
    }

    public void setPopfx11(String popfx11) {
        this.popfx11 = popfx11;
    }

    public String getPopfx12() {
        return popfx12;
    }

    public void setPopfx12(String popfx12) {
        this.popfx12 = popfx12;
    }

    public String getPopfx13() {
        return popfx13;
    }

    public void setPopfx13(String popfx13) {
        this.popfx13 = popfx13;
    }

    public String getPopfx14() {
        return popfx14;
    }

    public void setPopfx14(String popfx14) {
        this.popfx14 = popfx14;
    }

    public String getPopfx15() {
        return popfx15;
    }

    public void setPopfx15(String popfx15) {
        this.popfx15 = popfx15;
    }

    public String getPopfx16() {
        return popfx16;
    }

    public void setPopfx16(String popfx16) {
        this.popfx16 = popfx16;
    }

    public String getPopfx17() {
        return popfx17;
    }

    public void setPopfx17(String popfx17) {
        this.popfx17 = popfx17;
    }

    public String getPopfx18() {
        return popfx18;
    }

    public void setPopfx18(String popfx18) {
        this.popfx18 = popfx18;
    }

    public String getPopfx19() {
        return popfx19;
    }

    public void setPopfx19(String popfx19) {
        this.popfx19 = popfx19;
    }

    public String getPopfx20() {
        return popfx20;
    }

    public void setPopfx20(String popfx20) {
        this.popfx20 = popfx20;
    }

    public Integer getSeqNoFx() {
        return seqNoFx;
    }

    public void setSeqNoFx(Integer seqNoFx) {
        this.seqNoFx = seqNoFx;
    }

    public String getPopSumInsured1() {
        return popSumInsured1;
    }

    public void setPopSumInsured1(String popSumInsured1) {
        this.popSumInsured1 = popSumInsured1;
    }

    public String getPopSumInsured2() {
        return popSumInsured2;
    }

    public void setPopSumInsured2(String popSumInsured2) {
        this.popSumInsured2 = popSumInsured2;
    }

    public String getPopSumInsured3() {
        return popSumInsured3;
    }

    public void setPopSumInsured3(String popSumInsured3) {
        this.popSumInsured3 = popSumInsured3;
    }

    public String getPopSumInsured4() {
        return popSumInsured4;
    }

    public void setPopSumInsured4(String popSumInsured4) {
        this.popSumInsured4 = popSumInsured4;
    }

    public String getPopSumInsured5() {
        return popSumInsured5;
    }

    public void setPopSumInsured5(String popSumInsured5) {
        this.popSumInsured5 = popSumInsured5;
    }

    public String getPopSumInsured6() {
        return popSumInsured6;
    }

    public void setPopSumInsured6(String popSumInsured6) {
        this.popSumInsured6 = popSumInsured6;
    }

    public String getPopSumInsured7() {
        return popSumInsured7;
    }

    public void setPopSumInsured7(String popSumInsured7) {
        this.popSumInsured7 = popSumInsured7;
    }

    public String getPopSumInsured8() {
        return popSumInsured8;
    }

    public void setPopSumInsured8(String popSumInsured8) {
        this.popSumInsured8 = popSumInsured8;
    }

    public String getPopSumInsured9() {
        return popSumInsured9;
    }

    public void setPopSumInsured9(String popSumInsured9) {
        this.popSumInsured9 = popSumInsured9;
    }

    public String getPopSumInsured10() {
        return popSumInsured10;
    }

    public void setPopSumInsured10(String popSumInsured10) {
        this.popSumInsured10 = popSumInsured10;
    }

    public String getPopDeduct1() {
        return popDeduct1;
    }

    public void setPopDeduct1(String popDeduct1) {
        this.popDeduct1 = popDeduct1;
    }

    public String getPopDeduct2() {
        return popDeduct2;
    }

    public void setPopDeduct2(String popDeduct2) {
        this.popDeduct2 = popDeduct2;
    }

    public String getPopDeduct3() {
        return popDeduct3;
    }

    public void setPopDeduct3(String popDeduct3) {
        this.popDeduct3 = popDeduct3;
    }

    public String getPopDeduct4() {
        return popDeduct4;
    }

    public void setPopDeduct4(String popDeduct4) {
        this.popDeduct4 = popDeduct4;
    }

    public String getPopDeduct5() {
        return popDeduct5;
    }

    public void setPopDeduct5(String popDeduct5) {
        this.popDeduct5 = popDeduct5;
    }

    public String getPopDeduct6() {
        return popDeduct6;
    }

    public void setPopDeduct6(String popDeduct6) {
        this.popDeduct6 = popDeduct6;
    }

    public String getPopDeduct7() {
        return popDeduct7;
    }

    public void setPopDeduct7(String popDeduct7) {
        this.popDeduct7 = popDeduct7;
    }

    public String getPopDeduct8() {
        return popDeduct8;
    }

    public void setPopDeduct8(String popDeduct8) {
        this.popDeduct8 = popDeduct8;
    }

    public String getPopDeduct9() {
        return popDeduct9;
    }

    public void setPopDeduct9(String popDeduct9) {
        this.popDeduct9 = popDeduct9;
    }

    public String getPopDeduct10() {
        return popDeduct10;
    }

    public void setPopDeduct10(String popDeduct10) {
        this.popDeduct10 = popDeduct10;
    }
    
    
    
    
}
