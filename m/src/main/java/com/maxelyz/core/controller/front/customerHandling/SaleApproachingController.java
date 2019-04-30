/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

/**
 *
 * @author admin
 */
import com.google.gson.Gson;
import com.maxelyz.converter.PaymentModeConverter;
import com.maxelyz.core.common.front.dispatch.FrontDispatcher;
import com.maxelyz.core.common.front.dispatch.RegistrationPouch;
import com.maxelyz.core.common.dto.PaymentMethodStringCastor;
import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.front.customerHandling.CoverageInfo;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.ProductPlanDetailInfo;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.bean.ViewScoped;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;

@ManagedBean
@ViewScoped
public class SaleApproachingController implements Serializable {

    private static Logger log = Logger.getLogger(SaleApproachingController.class);
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private Map<Integer, Boolean> selectedIds1 = new ConcurrentHashMap<Integer, Boolean>();
    private Boolean checkProductCategoryAll;
    private Boolean checkProductPlanCategoryAll;
    private String SUCCESS = "/front/customerHandling/saleApproaching.xhtml";
    private String FAIL = "assignmentList.xhtml";
    private String VIEWED = "viewed";
    private String ASSIGNED = "assigned";
    private String FOLLOWUP = "followup";
    private String REGISTRATION_RETAIL_FORM = "registrationRetail.xhtml";
    private String SHOPPING_CART = "shoppingCart.xhmtl?faces-redirect=true";
//    private boolean enableBtnRegister;
//    private boolean enableBtnAddToCart;
    private Integer productId;
    private Integer productCategoryId = 0;
    private Integer quantity = new Integer(0);
    private Double price = new Double(0);
    private Double stampDuty = new Double(0);
    private Double vat = new Double(0);
    private Double netPremium = new Double(0);
    private Double totalPrice = new Double(0);
    private Double sumInsured = new Double(0);
    private Integer age;
    private Integer productPlanId;
    private String paymentModeId;
    private String tempCategoryType;
    private UserSession userSession;
    private TempPurchaseOrder tempPurchaseOrder;
    private List<TempPurchaseOrder> tempPurchaseOrderList;
    private TempPurchaseOrderDetail tempPurchaseOrderDetail;
    private AssignmentDetail assignmentDetail;
    private Integer adId;
    private Integer adIdInbound;
    private Product product;
    private ProductPlan productPlan;
    private ProductPlanDetail productPlanDetail;
    private List<Object> productList;
    private List<Object> productPlanList;
    private List<String[]> paymentModeList;
    private List<Object> productCategoryList;
    private List<ProductPlanCategory> productPlanCategoryList;
    private List<ProductPlan> dataList;
    
    private Campaign campaign;
    private List<Campaign> campaignList;
    private Integer campaignId = 0;
    private Integer productPlanCategoryId;

    private Boolean showTabLife = false;
    private Boolean showTabNonLife = false;
    private Boolean showTabMotor = false;
    private Boolean showTabMotorAdvance = false;
    private Boolean showTabRetail = false;
    private String defaultCategoryType = "";

    private Brand brand;
    private Integer brandId;
    private Model model;
    private Integer modelId;
    private ModelType modelType;
    private Integer modelTypeId;
    private Integer modelYear;
    private List<Brand> brandList;
    private List<Model> modelList = new ArrayList<Model>();
    private List<ModelType> modelTypeList = new ArrayList<ModelType>();
    private SelectItem[] modelYearList;
    private List<Integer> productCategoryIdList = new ArrayList<Integer>();
    private List<Integer> productPlanCategoryIdList = new ArrayList<Integer>();

    private int page = 1;
    private Integer rows = 0;

    private Boolean campaignInbound = false;
    private Integer sumInsuredMin = 0;
    private Integer sumInsuredMax = 0;
    private String claimType = "";

    private Date dateStart;
    private Integer noYear;

    private String description;

    private String gender;
    private Integer contactResultPlanId;

    private CoverageInfo coverageInfo1 = new CoverageInfo();
    private CoverageInfo coverageInfo2 = new CoverageInfo();
    private CoverageInfo coverageInfo3 = new CoverageInfo();
    private CoverageInfo coverageInfo4 = new CoverageInfo();
    private CoverageInfo coverageInfo5 = new CoverageInfo();
    private CoverageInfo coverageInfo6 = new CoverageInfo();
    private CoverageInfo coverageInfo7 = new CoverageInfo();
    private CoverageInfo coverageInfo8 = new CoverageInfo();
    private CoverageInfo coverageInfo9 = new CoverageInfo();
    private CoverageInfo coverageInfo10 = new CoverageInfo();
    private ProductPlanDetailInfo productPlanDetailInfo;
    private String productPlanDetailInfoJson;
    private List<CoverageInfo> coverageInfoList = null;

    private String productFlexField1 = "";

    @ManagedProperty(value = "#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
    @ManagedProperty(value = "#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{productPlanDAO}")
    private ProductPlanDAO productPlanDAO;
    @ManagedProperty(value = "#{productPlanCategoryDAO}")
    private ProductPlanCategoryDAO productPlanCategoryDAO;
    @ManagedProperty(value = "#{productPlanDetailDAO}")
    private ProductPlanDetailDAO productPlanDetailDAO;
    @ManagedProperty(value = "#{productCategoryDAO}")
    private ProductCategoryDAO productCategoryDAO;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value = "#{purchaseOrderDetailDAO}")
    private PurchaseOrderDetailDAO purchaseOrderDetailDAO;
    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{districtDAO}")
    private DistrictDAO districtDAO;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{assignmentDAO}")
    private AssignmentDAO assignmentDAO;

    @ManagedProperty(value = "#{brandDAO}")
    private BrandDAO brandDAO;

    @ManagedProperty(value = "#{modelDAO}")
    private ModelDAO modelDAO;

    @ManagedProperty(value = "#{modelTypeDAO}")
    private ModelTypeDAO modelTypeDAO;

    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    @ManagedProperty(value = "#{marketingCustomerDAO}")
    private MarketingCustomerDAO marketingCustomerDAO;
    @ManagedProperty(value = "#{contactHistoryDAO}")
    private ContactHistoryDAO contactHistoryDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("saleapproaching:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        if (SecurityUtil.isPermitted("campaign:inbound")) {
            campaignInbound = true;
        }
        try {
            if (JSFUtil.getRequestParameterMap("selectedId") != null) {
                Integer assignmentDetailId = 0;
                if (JSFUtil.getRequestParameterMap("selectedId").equalsIgnoreCase("-1")) { // for PDS
                    assignmentDetailId = JSFUtil.getUserSession().getNotificationRefId();
                } else {
                    assignmentDetailId = Integer.parseInt(JSFUtil.getRequestParameterMap("selectedId"));
                }

                if (assignmentDetailId != null && assignmentDetailId != 0) {
                    //set AssignmentDetail
                    assignmentDetail = assignmentDetailDAO.findAssignmentDetail(assignmentDetailId);
                    if (StringUtils.equals(assignmentDetail.getStatus(), ASSIGNED)) {
                        assignmentDetailDAO.updateStatus(assignmentDetail, VIEWED);
                        assignmentDetail = assignmentDetailDAO.findAssignmentDetail(assignmentDetailId);
                    }
                    JSFUtil.getUserSession().setAssignmentDetail(assignmentDetail);//oat add

                    //set CustomerInfoValue
                    Integer customerId = assignmentDetail.getCustomerId().getId();
                    CustomerInfoValue customerInfoValue = customerHandlingDAO.findCustomerHandling(customerId);
                    JSFUtil.getUserSession().setCustomerDetail(customerInfoValue);

                    //this.initCampaign();
                    //this.initTab();
                }
            }
            //return SUCCESS;
        } catch (Exception e) {
            log.error(e);
            //return FAIL;
        }
        userSession = JSFUtil.getUserSession();
        JSFUtil.getUserSession().setRefNo(null);
        //JSFUtil.getUserSession().setMode("hide"); //Dial Panel Hide
        //JSFUtil.getUserSession().setTelephonyEdit("view"); //Dial Panel View
        initAssignmentDetail();
        if (userSession.getCustomerDetail() != null) {
            this.initCampaign();
            this.initTab();
        }

        UserSession u = JSFUtil.getUserSession();
        u.setContactResultPlanId(null);
    }

    private void initTempPurchaseOrder() {
        if (userSession.getCustomerDetail() != null) {
            if (userSession.getCustomerDetail().getId() != null && userSession.getCustomerDetail().getId() != 0) {
                Customer customer = customerDAO.findCustomer(userSession.getCustomerDetail().getId());
                tempPurchaseOrderList = purchaseOrderDAO.findTempByCustomer(customer);
                for (TempPurchaseOrder tpo : tempPurchaseOrderList) {
                    if (tpo.getTempPurchaseOrderDetailCollection() != null) {
                        for (TempPurchaseOrderDetail tpod : tpo.getTempPurchaseOrderDetailCollection()) {
                            tempCategoryType = tpod.getProduct().getProductCategory().getCategoryType();
                            break;
                        }
                    }
                }
                this.initCampaign();
                this.initTab();
            }
        }
    }

    private void initCampaign() {

        if (userSession.getCustomerDetail() != null) {
            if (userSession.getCustomerDetail().getId() != null && userSession.getCustomerDetail().getId() != 0) {

                // FOR PREDICTIVE TELESALE Param1 = CUSTOMER ID and Param2 = MARKETING ID
                if (userSession.getParam1() != null && !userSession.getParam1().isEmpty() && userSession.getParam2() != null && !userSession.getParam2().isEmpty()) {
                    MarketingCustomer mc = marketingDAO.findMarketingCustomerByPK(Integer.parseInt(userSession.getParam1()), Integer.parseInt(userSession.getParam2()));
                    Marketing m = mc.getMarketing();
                    campaignList = campaignDAO.findByMarketingInbound(m.getId());
                } else {    // FOR NORMAL TELESALE
                    campaignList = campaignDAO.findCampaignBy(userSession.getUsers().getId(), userSession.getCustomerDetail().getId());

                    if (campaignInbound) {
                        List<Campaign> campaignInboundList = campaignDAO.findCampaignInbound();
                        if (campaignList == null || campaignList.isEmpty()) {
                            campaignList = new ArrayList<Campaign>();
                        }
                        for (Campaign c : campaignInboundList) {
                            campaignList.add(c);
                        }
                    }

                }

                if (assignmentDetail != null
                        && assignmentDetail.getId() != null
                        && assignmentDetail.getAssignmentId() != null) {
                    campaignId = assignmentDetail.getAssignmentId().getCampaign().getId();
                    campaign = assignmentDetail.getAssignmentId().getCampaign();
                } else {
                    if (campaignList != null && !campaignList.isEmpty()) {
                        campaign = campaignList.get(0);
                        campaignId = campaign.getId();
                    }
                }
                if (campaign != null && campaign.getInbound() != null && campaign.getInbound() == true && assignmentDetail.getAssignmentId() == null) {
                    Assignment a = null;
                    if (a == null) {
                        a = new Assignment();
                        a.setCampaign(campaign);
                        a.setMarketing(campaign.getMarketingInbound());
                        a.setAssignDate(new Date());
                        a.setAssignmentMode("supervisor");
                        a.setAssignmentType("custom");
                        a.setCreateByUser(userSession.getUsers());
                        a.setCreateBy(userSession.getUserName());
                        a.setCreateDate(new Date());
                        a.setNewList(true);
                        a.setNoCustomer(1);
                        a.setNoUser(1);
                        assignmentDAO.create(a);
                        Collection<Product> products = campaign.getProductCollection();
                        List<AssignmentProduct> aps = new ArrayList<AssignmentProduct>();
                        for (Product obj : products) {
                            AssignmentProductPK pk = new AssignmentProductPK(a.getId(), obj.getId());
                            AssignmentProduct ap = new AssignmentProduct(pk);
                            ap.setProduct(obj);
                            ap.setProductCode(obj.getCode());
                            ap.setProductName(obj.getName());
                            aps.add(ap);
                        }
                        a.setAssignmentProductCollection(aps);
                        assignmentDAO.edit(a);
                        //----
                        //campaign.setAssignmentInbound(a);
                        //campaignDAO.edit(campaign);
                    }
                    if (a.getMarketing() == null) {
                        if (campaign.getMarketingInbound() != null) {
                            Marketing marketing = campaign.getMarketingInbound();
                            a.setMarketing(marketing);
                        }
                    }
                    assignmentDetail.setCampaignName(campaign.getName());
                    assignmentDetail.setMarketingCode(campaign.getMarketingInbound() != null ? campaign.getMarketingInbound().getCode() : "");
                    assignmentDetail.setAssignmentId(a);
                    assignmentDetail.setMaxCall(campaign.getMaxCall());
                    assignmentDetail.setMaxCall2(campaign.getMaxCall2());
                    assignmentDetail.setCallAttempt(0);
                    assignmentDetail.setCallAttempt2(0);
                    try {
                        assignmentDetailDAO.create(assignmentDetail);
                    } catch (Exception e) {
                        //log.error("Create AssignmentDetail : " + e);
                    }
                    if (JSFUtil.getUserSession().getContactHistory() != null && JSFUtil.getUserSession().getContactHistory().getId() != null
                            && assignmentDetail != null && assignmentDetail.getId() != null) {
                        ContactHistory contactHistory = JSFUtil.getUserSession().getContactHistory();
                        try {
                            contactHistoryDAO.createContactHistoryAssignmentDetail(contactHistory.getId(), assignmentDetail.getId());
                        } catch (Exception e) {
                            //log.error("Create ContactHistory AssignmentDetail : " + e);
                        }
                    }
                    userSession.setAssignmentDetail(assignmentDetail);
                }
            }
        }
    }

    private void initAssignmentDetail() {
        Integer assignmentDetailId = 0;
        if (StringUtils.isNotEmpty(JSFUtil.getRequestParameterMap("selectedId")) && !StringUtils.equals(JSFUtil.getRequestParameterMap("selectedId"), "0")) {
            //assignmentDetailId = Integer.parseInt(JSFUtil.getRequestParameterMap("selectedId"));
            if (JSFUtil.getRequestParameterMap("selectedId").equalsIgnoreCase("-1")) { // for PDS
                assignmentDetailId = JSFUtil.getUserSession().getNotificationRefId();
            } else {
                assignmentDetailId = Integer.parseInt(JSFUtil.getRequestParameterMap("selectedId"));
            }
        } else if (userSession.getAssignmentDetail() != null && userSession.getAssignmentDetail().getId() != null) {
            assignmentDetailId = userSession.getAssignmentDetail().getId();
        } else {
            assignmentDetailId = 0;
        }
        adId = assignmentDetailId;

        if (assignmentDetailId != 0 && assignmentDetailId != null) {
            assignmentDetail = assignmentDetailDAO.findAssignmentDetail(assignmentDetailId);
            userSession.setAssignmentDetail(assignmentDetail);
        } else if (JSFUtil.getUserSession().getAssignmentDetail() != null) {
            assignmentDetail = JSFUtil.getUserSession().getAssignmentDetail();
        } else {
            assignmentDetail = new AssignmentDetail();
            assignmentDetail.setUsers(userSession.getUsers());
            assignmentDetail.setAssignmentId(null);
            assignmentDetail.setCustomerId(userSession.getCustomerDetail() != null ? new Customer(userSession.getCustomerDetail().getId()) : null);
            assignmentDetail.setCustomerName(userSession.getCustomerDetail().getName() + ' ' + userSession.getCustomerDetail().getSurname());

            // IF INBOUND CALL IS PREDICTIVE, LIST STATUS BEGIN WITH OPEN
            if (userSession.getParam1() != null && !userSession.getParam1().isEmpty() && userSession.getParam2() != null && !userSession.getParam2().isEmpty()) {
                assignmentDetail.setStatus("opened");
            } else {    // IF INBOUND CALL IS NOT PREDICTIVE, LIST STATUS BEGIN WITH FOLLOW 
                assignmentDetail.setStatus(FOLLOWUP);
            }

            assignmentDetail.setAssignDate(new Date());
            userSession.setAssignmentDetail(assignmentDetail);
        }
        if (assignmentDetail != null && assignmentDetail.getCustomerId() != null && userSession.getCustomerDetail() == null) {
            Integer customerId = assignmentDetail.getCustomerId().getId();
            CustomerInfoValue customerInfoValue = customerHandlingDAO.findCustomerHandling(customerId);
            JSFUtil.getUserSession().setCustomerDetail(customerInfoValue);
        }
    }

    public String toSaleApproaching() {
        try {
            //RESET REFERENCE YES SALE
            JSFUtil.getUserSession().setRefYes(null);

//            Integer assignmentDetailId = Integer.parseInt(JSFUtil.getRequestParameterMap("selectedId"));
//            if (assignmentDetailId != null && assignmentDetailId != 0) {
//                //set AssignmentDetail
//                assignmentDetail = assignmentDetailDAO.findAssignmentDetail(assignmentDetailId);
//                if (StringUtils.equals(assignmentDetail.getStatus(), ASSIGNED)) {
//                    assignmentDetailDAO.updateStatus(assignmentDetail, VIEWED);
//                    assignmentDetail = assignmentDetailDAO.findAssignmentDetail(assignmentDetailId);
//                }
//                JSFUtil.getUserSession().setAssignmentDetail(assignmentDetail);//oat add
//
//                //set CustomerInfoValue
//                Integer customerId = assignmentDetail.getCustomerId().getId();
//                CustomerInfoValue customerInfoValue = customerHandlingDAO.findCustomerHandling(customerId);
//                JSFUtil.getUserSession().setCustomerDetail(customerInfoValue);
//
//                this.initCampaign();
//                this.initTab();
//            }
            return SUCCESS;
        } catch (Exception e) {
            log.error(e);
            return FAIL;
        }
    }

    private void setTpo() {
        if (userSession.getCustomerDetail().getId() != null && userSession.getCustomerDetail().getId() != 0) {
            Customer customer = customerDAO.findCustomer(userSession.getCustomerDetail().getId());
            tempPurchaseOrderList = purchaseOrderDAO.findTempByCustomer(customer);
            for (TempPurchaseOrder tpo : tempPurchaseOrderList) {
                if (tpo.getTempPurchaseOrderDetailCollection() != null) {
                    for (TempPurchaseOrderDetail tpod : tpo.getTempPurchaseOrderDetailCollection()) {
                        if (tpod.getProduct().getId() == product.getId()) {
                            tempPurchaseOrder = tpo;
                            break;
                        }
                    }
                }
            }
        }
        if (tempPurchaseOrder == null) {
            tempPurchaseOrder = new TempPurchaseOrder();
        }
    }

    private void toGetProductPlan(PurchaseOrder po) {
        if (defaultCategoryType.equals("life")) {
            //productPlan = productPlanDAO.findProductPlan(productPlanId);
        } else if (defaultCategoryType.equals("nonlife")) {
            //productPlan = productPlanDAO.findProductPlan(productPlanId);
        } else if (defaultCategoryType.equals("motor")) {

        } else if (defaultCategoryType.equals("retail")) {

        }
    }

    private PurchaseOrder preparedPO4Register() throws Exception {
        //try {  //oat
        PurchaseOrder po = new PurchaseOrder();
        PurchaseOrderDetail pod = new PurchaseOrderDetail();
        ArrayList<PurchaseOrderDetail> podList = new ArrayList<PurchaseOrderDetail>();

        this.toGetProductPlan(po);

//            productPlan = productPlanDAO.findProductPlanByIdPaymentMode(productPlanId, paymentModeId);
        if (productPlan != null) {
            product = productPlan.getProduct();
        }
        //productPlan = productPlanDAO.findProductPlanByIdPaymentMode(productPlanId);
        pod.setProductPlan(productPlan);
        pod.setPaymentMode(paymentModeId);
        pod.setProductPlanDetail(productPlanDetail);
        pod.setQuantity(1);
        pod.setUnitPrice(netPremium != null ? netPremium : null);
//            if(productPlan.getPaymentMode() != null && !productPlan.getPaymentMode().equals("")){
//                if (productPlan.getPaymentMode().equals("1")) {
//                    totalPrice = (netPremium * 2);
//                } else {
//                    totalPrice = (netPremium);
//                }
//            }else{
//                totalPrice = netPremium;
//            }

        pod.setPurchaseOrder(po);
        if (assignmentDetail != null) {
            pod.setAssignmentDetail(assignmentDetail);
        }
        pod.setProduct(product);
        pod.setAmount(totalPrice);
        pod.setCreateDate(new Date());

        podList.add(pod);

        po.setRefNo(null);
        if (userSession.getCustomerDetail().getId() != null && userSession.getCustomerDetail().getId() != 0) {
            Customer customer = customerDAO.findCustomer(userSession.getCustomerDetail().getId());
            po.setCustomer(customer);
        }
        if (assignmentDetail != null) {
            po.setAssignmentDetail(assignmentDetail);
            po.setBillingFullname(assignmentDetail.getCustomerId().getBillingFullname());
            po.setBillingAddressLine1(assignmentDetail.getCustomerId().getBillingAddressLine1());
            po.setBillingAddressLine2(assignmentDetail.getCustomerId().getBillingAddressLine2());
            if (assignmentDetail.getCustomerId().getBillingDistrictId() != null) {
                po.setBillingDistrict(assignmentDetail.getCustomerId().getBillingDistrictId());
            } else {
                po.setBillingDistrict(null);
            }
            po.setBillingPostalCode(assignmentDetail.getCustomerId().getBillingPostalCode());

            po.setShippingFullname(assignmentDetail.getCustomerId().getShippingFullname());
            po.setShippingAddressLine1(assignmentDetail.getCustomerId().getShippingAddressLine1());
            po.setShippingAddressLine2(assignmentDetail.getCustomerId().getShippingAddressLine2());
            if (assignmentDetail.getCustomerId().getShippingDistrictId() != null) {
                po.setShippingDistrict(assignmentDetail.getCustomerId().getShippingDistrictId());
            } else {
                po.setShippingDistrict(null);
            }
            po.setShippingPostalCode(assignmentDetail.getCustomerId().getShippingPostalCode());
        }
        //po.setPurchaseDate(new Date());
        po.setPrice(price);
        po.setStampDuty(stampDuty);
        po.setVat(vat);
        po.setNetPremium(netPremium);
        po.setAnnualNetPremium(productPlanDetail.getAnnualNetPremium() != null ? productPlanDetail.getAnnualNetPremium() : null);
        po.setTotalAmount(totalPrice);
        po.setSumInsured(sumInsured);
        po.setPurchaseOrderDetailCollection(podList);
        if(!product.getFx1().equals("CMI") && !product.getFx1().equals("VMI")) {
            po.setSaleResult("N".charAt(0));
        }else {
            po.setSaleResult("F".charAt(0));
        }
        po.setCreateBy(userSession.getUserName());
        po.setCreateByUser(userSession.getUsers());
        po.setCreateDate(new Date());
        po.setApprovalStatus(JSFUtil.getBundleValue("approvalWaitingValue"));
        po.setPaymentStatus(JSFUtil.getBundleValue("approvalWaitingValue"));
        po.setQcStatus(JSFUtil.getBundleValue("approvalWaitingValue"));
        po.setProductPlanDetailInfo(productPlanDetailInfoJson);
        if (po.getConfirmFlag() == null) {
            po.setConfirmFlag(false);
        }
        if (po.getYesFlag() == null) {
            po.setYesFlag(false);
        }

        if (defaultCategoryType.equals("motor")) {
            po.setModelYear(modelYear);
        }

        //JSFUtil.getRequest().setAttribute("poAttribute", po);
        //} catch (Exception e) {
        //    log.error(e);
        //}  //oat
        return po;
    }

    private void toSave() {
        try {
            //Begin TempPurchaseOrder Version
            Collection<TempPurchaseOrderDetail> listDetail = null;

            if (userSession.getCustomerDetail().getId() != null && userSession.getCustomerDetail().getId() != 0) {
                Customer customer = customerDAO.findCustomer(userSession.getCustomerDetail().getId());
                tempPurchaseOrderList = purchaseOrderDAO.findTempByCustomer(customer);
                for (TempPurchaseOrder tpo : tempPurchaseOrderList) {
                    tempPurchaseOrder = tpo;
                    break;
                }
            }
            if (tempPurchaseOrder == null) {
                //new tpo
                tempPurchaseOrder = new TempPurchaseOrder();
            }
            if (tempPurchaseOrder.getTempPurchaseOrderDetailCollection() == null) {
                listDetail = new ArrayList<TempPurchaseOrderDetail>();
            } else {
                listDetail = tempPurchaseOrder.getTempPurchaseOrderDetailCollection();
            }

            //new tpod
            tempPurchaseOrderDetail = new TempPurchaseOrderDetail();

            if (defaultCategoryType.equals("life") || defaultCategoryType.equals("nonlife")) {
                productPlan = productPlanDAO.findProductPlanByIdPaymentMode(productPlanId, paymentModeId);
                //productPlan = productPlanDAO.findProductPlanByIdPaymentMode(productPlanId);
                tempPurchaseOrderDetail.setProductPlan(productPlan);
                tempPurchaseOrderDetail.setPaymentMode(productPlan.getPaymentMode());
                tempPurchaseOrderDetail.setQuantity(1);
                tempPurchaseOrderDetail.setUnitPrice(netPremium != null ? netPremium : null);
                if (productPlan.getPaymentMode().equals("1")) {
                    try {
                        totalPrice = (netPremium * product.getMonthlyFirstPayment());
                    } catch (Exception e) {
                        totalPrice = netPremium;
                    }
                } else if (productPlan.getPaymentMode().equals("2")) {
                    try {
                        totalPrice = (netPremium * product.getQuarterlyFirstPayment());
                    } catch (Exception e) {
                        totalPrice = netPremium;
                    }
                } else {
                    totalPrice = (netPremium);
                }
            } else if (defaultCategoryType.equals("retail")) {
                tempPurchaseOrderDetail.setQuantity(quantity);
                tempPurchaseOrderDetail.setUnitPrice(product.getPrice() != null ? product.getPrice() : null);
                totalPrice = quantity * product.getPrice();
            }
            tempPurchaseOrderDetail.setTempPurchaseOrder(tempPurchaseOrder);
            if (assignmentDetail != null) {
                tempPurchaseOrderDetail.setAssignmentDetailId(assignmentDetail.getId());
            }
            tempPurchaseOrderDetail.setProduct(product);
            tempPurchaseOrderDetail.setAmount(totalPrice);
            tempPurchaseOrderDetail.setCreateDate(new Date());

            listDetail.add(tempPurchaseOrderDetail);

            tempPurchaseOrder.setRefNo(null);
            if (userSession.getCustomerDetail().getId() != null && userSession.getCustomerDetail().getId() != 0) {
                Customer customer = customerDAO.findCustomer(userSession.getCustomerDetail().getId());
                tempPurchaseOrder.setCustomer(customer);
            }
            if (assignmentDetail != null) {
                tempPurchaseOrder.setAssignmentDetail(assignmentDetail);
                tempPurchaseOrder.setBillingFullname(assignmentDetail.getCustomerId().getBillingFullname());
                tempPurchaseOrder.setBillingAddressLine1(assignmentDetail.getCustomerId().getBillingAddressLine1());
                tempPurchaseOrder.setBillingAddressLine2(assignmentDetail.getCustomerId().getBillingAddressLine2());
                if (assignmentDetail.getCustomerId().getBillingDistrictId() != null) {
                    tempPurchaseOrder.setBillingDistrict(assignmentDetail.getCustomerId().getBillingDistrictId());
                } else {
                    tempPurchaseOrder.setBillingDistrict(null);
                }
                tempPurchaseOrder.setBillingPostalCode(assignmentDetail.getCustomerId().getBillingPostalCode());

                tempPurchaseOrder.setShippingFullname(assignmentDetail.getCustomerId().getShippingFullname());
                tempPurchaseOrder.setShippingAddressLine1(assignmentDetail.getCustomerId().getShippingAddressLine1());
                tempPurchaseOrder.setShippingAddressLine2(assignmentDetail.getCustomerId().getShippingAddressLine2());
                if (assignmentDetail.getCustomerId().getShippingDistrictId() != null) {
                    tempPurchaseOrder.setShippingDistrict(assignmentDetail.getCustomerId().getShippingDistrictId());
                } else {
                    tempPurchaseOrder.setShippingDistrict(null);
                }
                tempPurchaseOrder.setShippingPostalCode(assignmentDetail.getCustomerId().getShippingPostalCode());
            }
            tempPurchaseOrder.setPurchaseDate(new Date());
            tempPurchaseOrder.setTotalAmount(totalPrice);
            tempPurchaseOrder.setTempPurchaseOrderDetailCollection(listDetail);
            tempPurchaseOrder.setSaleResult("N".charAt(0));
            tempPurchaseOrder.setCreateByUser(userSession.getUsers());
            tempPurchaseOrder.setCreateDate(new Date());

            if (tempPurchaseOrder.getId() != null && tempPurchaseOrder.getId() != 0) {
                purchaseOrderDAO.editTemp(tempPurchaseOrder);
            } else {
                purchaseOrderDAO.createTemp(tempPurchaseOrder);
            }

            JSFUtil.getRequest().setAttribute("tempPurchaseOrderId", tempPurchaseOrder.getId());
            JSFUtil.getRequest().setAttribute("productId", product.getId());

            //End TempPurchaseOrder Version
        } catch (Exception e) {
            log.error(e);
        }

    }

    private void checkAssignmentDetail() {
        if (assignmentDetail != null && assignmentDetail.getId() == null) {

            try {

                if (assignmentDetail.getAssignmentId() == null) {
                    if (campaign.getAssignmentInbound() != null) {
                        assignmentDetail.setAssignmentId(campaign.getAssignmentInbound());
                    }
                }

                assignmentDetailDAO.create(assignmentDetail);

                Marketing m = assignmentDetail.getAssignmentId().getMarketing();

                MarketingCustomerPK marketingCustomerPK = new MarketingCustomerPK(m.getId(), userSession.getCustomerDetail().getId());
                MarketingCustomer mc = marketingCustomerDAO.findMarketingCustomer(marketingCustomerPK);
                if (mc == null) {

                    mc = new MarketingCustomer();
                    mc.setAssign(true);
                    mc.setCustomer(new Customer(userSession.getCustomerDetail().getId()));
                    mc.setMarketing(m);
                    mc.setMarketingCustomerPK(marketingCustomerPK);
                    mc.setCreateDate(new Date());
                    marketingCustomerDAO.create(mc);

                    Integer c = m.getTotalAssigned() + 1;
                    m.setTotalAssigned(c);
                    marketingDAO.edit(m);
                }

                CampaignCustomerPK ccPK = new CampaignCustomerPK(assignmentDetail.getAssignmentId().getCampaign().getId(), userSession.getCustomerDetail().getId());
                CampaignCustomer cc = campaignDAO.findCampaignCustomer(ccPK);
                if (cc == null) {
                    cc = new CampaignCustomer();
                    cc.setAssignmentDetail(assignmentDetail);
                    cc.setCampaign(assignmentDetail.getAssignmentId().getCampaign());
                    cc.setCampaignCustomerPK(ccPK);
                    cc.setListUsed(Boolean.TRUE);
                    cc.setListUsedDate(new Date());
                    cc.setListReused(Boolean.FALSE);
                    campaignDAO.createCampaignCustomer(cc);
                }
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    public String toRegister() {
        String toPage = null;
        this.checkAssignmentDetail();
        if (JSFUtil.getRequestParameterMap("productPlanId") != null && !JSFUtil.getRequestParameterMap("productPlanId").equals("")) {
            productPlanId = Integer.parseInt(JSFUtil.getRequestParameterMap("productPlanId"));
        }
        try {
            PurchaseOrder newPO = this.preparedPO4Register();
            RegistrationPouch pouch = new RegistrationPouch(); // create pouch to carry data for purchase order
            pouch.registeredPouchType(RegistrationPouch.SENDER_TYPE.SALEAPPROACHING, RegistrationPouch.RECEIVER_MODE.NEW);
            pouch.putNewModeParameter(age, gender, paymentModeId, newPO);
            FrontDispatcher.keepPouch(pouch); // hold pouch 
            toPage = FrontDispatcher.getPouchReceiver(pouch); // get reciver
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        return toPage;
    }

    public String addToCart() {
        toSave();
        return SHOPPING_CART;
    }

    public void selectedProductListener() {
        clearPrice();
        productId = Integer.parseInt(JSFUtil.getRequestParameterMap("productId"));
        contactResultPlanId = Integer.parseInt(JSFUtil.getRequestParameterMap("contactResultPlanId"));
        product = productDAO.findProduct(productId);
        productFlexField1 = product.getFx1();
        if((productFlexField1!="" && productFlexField1 != null && product != null) || productFlexField1 == "CMI" || productFlexField1 =="VMI"){
            age = 0; //DEFAULT
            if(product.getGender()) {
                gender = "m"; //DEFAULT
            }else {
                gender = "";
            }
            paymentModeId = "4"; //DEFAULT
            if(checkFamily(product)){
                productPlanList = productPlanDAO.findProductPlanEntitiesProductPaymentModeByFamily(product.getId(), paymentModeId, gender);
                Object[] row = (Object[]) productPlanList.get(0);
                productPlanId = Integer.parseInt(row[0].toString());
                productPlanDetail = null;
                calculateLife();
            }else {
                productPlanList = productPlanDAO.findProductPlanEntitiesByProductPaymentMode(product.getId(), paymentModeId, gender);
                Object[] row = (Object[]) productPlanList.get(0);
                productPlanId = Integer.parseInt(row[0].toString());
                productPlanDetail = null;
                calculateLife();
            }

        }
        description = replaceTagField(product.getDescription());
        //When click product name then add product into session
        if (JSFUtil.getUserSession().getProducts() != null) {
            JSFUtil.getUserSession().setProducts(product);
            UserSession u =  JSFUtil.getUserSession();
            u.setContactResultPlanId(contactResultPlanId);
        }
        // initial payment mode
        paymentModeList = new ArrayList<String[]>();
        if (!product.getPaymentMode().isEmpty()) {
            try {
                List<PaymentMethodStringCastor> pmlist = PaymentModeConverter.convertToPaymentModeList(this.product.getPaymentMode());
                LinkedHashMap<String, String> pmmap = PaymentModeConverter.findProductPaymentMode(pmlist);
                for (String pmlabel : pmmap.keySet()) {
                    paymentModeList.add(new String[]{pmmap.get(pmlabel), pmlabel});
                }
            } catch (Exception e) {
                log.error(this.getClass().getName() + ".selectedProductListener : Exception has occured when convert payment mode => " + e.getMessage());
                //e.printStackTrace();
                initPaymentModeList(); // set to old pattern when error has occured
            }
        }
         
        if(checkFamily(product)){ 
            productPlanList = productPlanDAO.findProductPlanEntitiesProductPaymentModeByFamily(product.getId(), paymentModeId, gender);
        }else {
            productPlanList = productPlanDAO.findProductPlanEntitiesByProductPaymentMode(product.getId(), paymentModeId, gender);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    @Deprecated
    public void initPaymentModeList() {
        paymentModeList = new ArrayList<String[]>();
        String[] stra = null;
        if (!product.getPaymentMode().isEmpty()) {
            StringTokenizer st = new StringTokenizer(product.getPaymentMode(), ",");
            while (st.hasMoreTokens()) {
                Integer token = Integer.parseInt((String) st.nextToken());
                stra = new String[2];
                switch (token) {
                    case 1:
                        stra[0] = "1";
                        stra[1] = "Month";
                        break;
                    case 2:
                        stra[0] = "2";
                        stra[1] = "Quarter";
                        break;
                    case 3:
                        stra[0] = "3";
                        stra[1] = "Half Year";
                        break;
                    case 4:
                        stra[0] = "4";
                        stra[1] = "Year";
                        break;
                    default:
                        System.out.println("Invalid Entry!");
                }
                paymentModeList.add(stra);
            }
        }
    }

    private void changeProductCategory() {
        
        Integer assignmentId = 0;
        if(assignmentDetail != null) {
            assignmentId = assignmentDetail.getAssignmentId().getId();
        }
        
        product = null;
        paymentModeList = null;
        productPlanList = null;
//        enableBtnAddToCart = false;
//        enableBtnRegister = false;
        if (productCategoryId == null) {
            productCategoryId = 0;
        }
        clearPrice();
        if (campaignList != null && !campaignList.isEmpty()) {
            if (campaign.getInbound()) {
                if (productCategoryId == 0) {
                    productList = productDAO.findProductBy(defaultCategoryType, campaignId, 0, assignmentId);
                } else {
                    productList = productDAO.findProductBy(defaultCategoryType, campaignId, productCategoryId, assignmentId);
                }
            } else if (userSession.getUsers() != null && userSession.getCustomerDetail() != null && defaultCategoryType != null && campaignId != null) {
                if (productCategoryId == 0) {
                    productList = productDAO.findProductBy(userSession.getUsers().getId(), userSession.getCustomerDetail().getId(), defaultCategoryType, campaignId, 0, assignmentId);
                } else {
                    productList = productDAO.findProductBy(userSession.getUsers().getId(), userSession.getCustomerDetail().getId(), defaultCategoryType, campaignId, productCategoryId, assignmentId);
                }
            }

            if (productList != null && !productList.isEmpty()) {
                productId = 0;
            }
        }
    }

    public void changeProductCategoryListener() {
        changeProductCategory();
    }

    private void calculateLife() {
        //if (productPlanId != null && productPlanId != 0 && paymentModeId != null && !paymentModeId.equals("")) {
        if (productPlanId != null && productPlanId != 0) {
            quantity = 1;
            productPlan = productPlanDAO.findProductPlanByIdPaymentMode(productPlanId, paymentModeId);
            if (productPlan != null) {
                productPlanDetail = productPlanDetailDAO.findByAge(productPlan, age);
                if (productPlanDetail != null) {
                    price = productPlanDetail.getPrice();
                    stampDuty = productPlanDetail.getStampDuty();
                    vat = productPlanDetail.getVat();
                    netPremium = productPlanDetail.getNetPremium();
                    if (productPlan.getPaymentMode().equals("1")) {
                        totalPrice = netPremium * product.getMonthlyFirstPayment();
                    } else if (productPlan.getPaymentMode().equals("2")) {
                        totalPrice = netPremium * product.getQuarterlyFirstPayment();
                    } else {
                        totalPrice = (netPremium);
                    }
                } else {
                    clearPrice();
                }
            } else {
                clearPrice();
            }
        } else {
            clearPrice();
        }
    }

    private void calculateNonLife() {
        if (productPlanId != null && productPlanId != 0) {
            quantity = 1;
            productPlan = productPlanDAO.findProductPlanByIdPaymentMode(productPlanId, paymentModeId);
            if (productPlan != null) {
                productPlanDetail = productPlanDetailDAO.findByAge(productPlan, age);
                if (productPlanDetail != null) {
                    price = productPlanDetail.getPrice();
                    stampDuty = productPlanDetail.getStampDuty();
                    vat = productPlanDetail.getVat();
                    netPremium = productPlanDetail.getNetPremium();
                    if (productPlan.getPaymentMode().equals("1")) {
                        totalPrice = netPremium * product.getMonthlyFirstPayment();
                    } else if (productPlan.getPaymentMode().equals("2")) {
                        totalPrice = netPremium * product.getQuarterlyFirstPayment();
                    } else {
                        totalPrice = (netPremium);
                    }
                } else {
                    clearPrice();
                }
            } else {
                clearPrice();
            }
        } else {
            clearPrice();
        }
    }

    private void calculateMotor() {

    }

    private void calculateMotorAdvance() {
        if (productPlanId != null && productPlanId != 0) {
            quantity = 1;
            productPlan = productPlanDAO.findProductPlanByIdPaymentMode(productPlanId, paymentModeId);
            if (productPlan != null) {
                productPlanDetail = productPlanDetailDAO.findByAge(productPlan, age);
                if (productPlanDetail != null) {
                    price = productPlanDetail.getPrice();
                    stampDuty = productPlanDetail.getStampDuty();
                    vat = productPlanDetail.getVat();
                    netPremium = productPlanDetail.getNetPremium();
                    totalPrice = (netPremium);
                } else {
                    clearPrice();
                }
            } else {
                clearPrice();
            }
        } else {
            clearPrice();
        }
    }

    private void calculateRetail() {
        if (product != null) {
            price = product.getPrice();
            totalPrice = Double.valueOf(quantity * product.getPrice());
        } else {
            price = new Double(0);
            totalPrice = new Double(0);
        }
    }

    private void calculateLifeNonLife() {
        if (productPlanId != null && productPlanId != 0) {
            quantity = 1;
            productPlanDetail = productPlanDetailDAO.findProductPlanDetail(product, productPlanId, age, gender, paymentModeId);
            if (productPlanDetail != null) {
                productPlan = productPlanDetail.getProductPlan();
                price = productPlanDetail.getPrice();
                stampDuty = productPlanDetail.getStampDuty();
                vat = productPlanDetail.getVat();
                netPremium = productPlanDetail.getNetPremium();
                sumInsured = productPlanDetail.getProductPlan().getSumInsured();
                
                coverageInfoList = new ArrayList<CoverageInfo>();
                
                Integer countNumberOfCoverage=0;
                //1
                if(productPlan.getFx1() != null)
                {   if(!(productPlan.getFx1().isEmpty()))
                    {
                        countNumberOfCoverage++;
                        try
                        {
                            coverageInfo1.setId("1");
                        }
                        catch (Exception ex)
                        {
                            coverageInfo1.setId("");
                        }
                
                        try
                        {
                            coverageInfo1.setCode(productPlan.getFx1().split("\\|")[0]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo1.setCode("");
                        }
                
                        try
                        {
                            coverageInfo1.setDescription_TH(productPlan.getFx1().split("\\|")[1]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo1.setDescription_TH("");
                        }
                    
                        try
                        {
                            coverageInfo1.setDescription_EN(productPlan.getFx1().split("\\|")[2]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo1.setDescription_EN("");
                        }
                
                        try
                        {
                            coverageInfo1.setSum_Insured(productPlan.getFx1().split("\\|")[3]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo1.setSum_Insured("");
                        }
                
                        try
                        {
                            coverageInfo1.setDeduct(productPlan.getFx1().split("\\|")[4]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo1.setDeduct("");
                        }
                        
                        if(productPlanDetail.getFx1()!=null)
                        {   
                            if(productPlanDetail.getFx1().equals("|"))
                            {
                                coverageInfo1.setPremium_Amt("");
                                coverageInfo1.setSum_Insured_PlanDetail("");
                                coverageInfo1.setDeduct_PlanDetail("");
                            }
                            else if(productPlanDetail.getFx1().equals("|||"))
                            {
                                coverageInfo1.setPremium_Amt("");
                                coverageInfo1.setSum_Insured_PlanDetail("");
                                coverageInfo1.setDeduct_PlanDetail("");
                            }
                            else
                            {
                                if(productPlan.getFx1().split("\\|")[0].equals(productPlanDetail.getFx1().split("\\|")[0]))
                                {
                                    try
                                    {
                                        coverageInfo1.setPremium_Amt(productPlanDetail.getFx1().split("\\|")[1]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo1.setPremium_Amt("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo1.setSum_Insured_PlanDetail(productPlanDetail.getFx1().split("\\|")[2]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo1.setSum_Insured_PlanDetail("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo1.setDeduct_PlanDetail(productPlanDetail.getFx1().split("\\|")[3]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo1.setDeduct_PlanDetail("");
                                    }
                                }
                            }
                        }
                        else
                        {
                            coverageInfo1.setPremium_Amt("");
                            coverageInfo1.setSum_Insured_PlanDetail("");
                            coverageInfo1.setDeduct_PlanDetail("");
                        }
                        coverageInfoList.add(coverageInfo1);
                    }
                }
                //2
                if(productPlan.getFx2() != null)
                {   if(!(productPlan.getFx2().isEmpty()))
                    {
                        countNumberOfCoverage++;
                        try
                        {
                            coverageInfo2.setId("2");
                        }
                        catch (Exception ex)
                        {
                            coverageInfo2.setId("");
                        }
                
                        try
                        {
                            coverageInfo2.setCode(productPlan.getFx2().split("\\|")[0]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo2.setCode("");
                        }
                
                        try
                        {
                            coverageInfo2.setDescription_TH(productPlan.getFx2().split("\\|")[1]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo2.setDescription_TH("");
                        }
                    
                        try
                        {
                            coverageInfo2.setDescription_EN(productPlan.getFx2().split("\\|")[2]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo2.setDescription_EN("");
                        }
                
                        try
                        {
                            coverageInfo2.setSum_Insured(productPlan.getFx2().split("\\|")[3]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo2.setSum_Insured("");
                        }
                
                        try
                        {
                            coverageInfo2.setDeduct(productPlan.getFx2().split("\\|")[4]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo2.setDeduct("");
                        }
                        
                        if(productPlanDetail.getFx2()!=null)
                        {   
                            if(productPlanDetail.getFx2().equals("|"))
                            {
                                coverageInfo2.setPremium_Amt("");
                                coverageInfo2.setSum_Insured_PlanDetail("");
                                coverageInfo2.setDeduct_PlanDetail("");
                            }
                            else if(productPlanDetail.getFx2().equals("|||"))
                            {
                                coverageInfo2.setPremium_Amt("");
                                coverageInfo2.setSum_Insured_PlanDetail("");
                                coverageInfo2.setDeduct_PlanDetail("");
                            }
                            else
                            {
                                if(productPlan.getFx2().split("\\|")[0].equals(productPlanDetail.getFx2().split("\\|")[0]))
                                {
                                    try
                                    {
                                        coverageInfo2.setPremium_Amt(productPlanDetail.getFx2().split("\\|")[1]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo2.setPremium_Amt("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo2.setSum_Insured_PlanDetail(productPlanDetail.getFx2().split("\\|")[2]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo2.setSum_Insured_PlanDetail("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo2.setDeduct_PlanDetail(productPlanDetail.getFx2().split("\\|")[3]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo2.setDeduct_PlanDetail("");
                                    }
                                }
                            }
                        }
                        else
                        {
                            coverageInfo2.setPremium_Amt("");
                            coverageInfo2.setSum_Insured_PlanDetail("");
                            coverageInfo2.setDeduct_PlanDetail("");
                        }
                        coverageInfoList.add(coverageInfo2);
                    }
                }
                //3
                if(productPlan.getFx3() != null)
                {   if(!(productPlan.getFx3().isEmpty()))
                    {
                        countNumberOfCoverage++;
                        try
                        {
                            coverageInfo3.setId("3");
                        }
                        catch (Exception ex)
                        {
                            coverageInfo3.setId("");
                        }

                        try
                        {
                            coverageInfo3.setCode(productPlan.getFx3().split("\\|")[0]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo3.setCode("");
                        }

                        try
                        {
                            coverageInfo3.setDescription_TH(productPlan.getFx3().split("\\|")[1]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo3.setDescription_TH("");
                        }

                        try
                        {
                            coverageInfo3.setDescription_EN(productPlan.getFx3().split("\\|")[2]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo3.setDescription_EN("");
                        }

                        try
                        {
                            coverageInfo3.setSum_Insured(productPlan.getFx3().split("\\|")[3]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo3.setSum_Insured("");
                        }

                        try
                        {
                            coverageInfo3.setDeduct(productPlan.getFx3().split("\\|")[4]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo3.setDeduct("");
                        }
                        
                        if(productPlanDetail.getFx3()!=null)
                        {   
                            if(productPlanDetail.getFx3().equals("|"))
                            {
                                coverageInfo3.setPremium_Amt("");
                                coverageInfo3.setSum_Insured_PlanDetail("");
                                coverageInfo3.setDeduct_PlanDetail("");
                            }
                            else if(productPlanDetail.getFx3().equals("|||"))
                            {
                                coverageInfo3.setPremium_Amt("");
                                coverageInfo3.setSum_Insured_PlanDetail("");
                                coverageInfo3.setDeduct_PlanDetail("");
                            }
                            else
                            {
                                if(productPlan.getFx3().split("\\|")[0].equals(productPlanDetail.getFx3().split("\\|")[0]))
                                {
                                    try
                                    {
                                        coverageInfo3.setPremium_Amt(productPlanDetail.getFx3().split("\\|")[1]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo3.setPremium_Amt("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo3.setSum_Insured_PlanDetail(productPlanDetail.getFx3().split("\\|")[2]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo3.setSum_Insured_PlanDetail("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo3.setDeduct_PlanDetail(productPlanDetail.getFx3().split("\\|")[3]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo3.setDeduct_PlanDetail("");
                                    }
                                }
                            }
                        }
                        else
                        {
                            coverageInfo3.setPremium_Amt("");
                            coverageInfo3.setSum_Insured_PlanDetail("");
                            coverageInfo3.setDeduct_PlanDetail("");
                        }
                        coverageInfoList.add(coverageInfo3);
                    }
                }
                //4
                if(productPlan.getFx4() != null)
                {   if(!(productPlan.getFx4().isEmpty()))
                    {
                        countNumberOfCoverage++;
                        try
                        {
                            coverageInfo4.setId("4");
                        }
                        catch (Exception ex)
                        {
                            coverageInfo4.setId("");
                        }

                        try
                        {
                            coverageInfo4.setCode(productPlan.getFx4().split("\\|")[0]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo4.setCode("");
                        }

                        try
                        {
                            coverageInfo4.setDescription_TH(productPlan.getFx4().split("\\|")[1]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo4.setDescription_TH("");
                        }

                        try
                        {
                            coverageInfo4.setDescription_EN(productPlan.getFx4().split("\\|")[2]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo4.setDescription_EN("");
                        }

                        try
                        {
                            coverageInfo4.setSum_Insured(productPlan.getFx4().split("\\|")[3]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo4.setSum_Insured("");
                        }

                        try
                        {
                            coverageInfo4.setDeduct(productPlan.getFx4().split("\\|")[4]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo4.setDeduct("");
                        }
                        
                        if(productPlanDetail.getFx4()!=null)
                        {   
                            if(productPlanDetail.getFx4().equals("|"))
                            {
                                coverageInfo4.setPremium_Amt("");
                                coverageInfo4.setSum_Insured_PlanDetail("");
                                coverageInfo4.setDeduct_PlanDetail("");
                            }
                            else if(productPlanDetail.getFx4().equals("|||"))
                            {
                                coverageInfo4.setPremium_Amt("");
                                coverageInfo4.setSum_Insured_PlanDetail("");
                                coverageInfo4.setDeduct_PlanDetail("");
                            }
                            else
                            {
                                if(productPlan.getFx4().split("\\|")[0].equals(productPlanDetail.getFx4().split("\\|")[0]))
                                {
                                    try
                                    {
                                        coverageInfo4.setPremium_Amt(productPlanDetail.getFx4().split("\\|")[1]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo4.setPremium_Amt("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo4.setSum_Insured_PlanDetail(productPlanDetail.getFx4().split("\\|")[2]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo4.setSum_Insured_PlanDetail("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo4.setDeduct_PlanDetail(productPlanDetail.getFx4().split("\\|")[3]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo4.setDeduct_PlanDetail("");
                                    }
                                }
                            }
                        }
                        else
                        {
                            coverageInfo4.setPremium_Amt("");
                            coverageInfo4.setSum_Insured_PlanDetail("");
                            coverageInfo4.setDeduct_PlanDetail("");
                        }
                        coverageInfoList.add(coverageInfo4);
                    }
                }
                //5
                if(productPlan.getFx5() != null)
                {   if(!(productPlan.getFx5().isEmpty()))
                    {
                        countNumberOfCoverage++;
                        try
                        {
                            coverageInfo5.setId("5");
                        }
                        catch (Exception ex)
                        {
                            coverageInfo5.setId("");
                        }

                        try
                        {
                            coverageInfo5.setCode(productPlan.getFx5().split("\\|")[0]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo5.setCode("");
                        }

                        try
                        {
                            coverageInfo5.setDescription_TH(productPlan.getFx5().split("\\|")[1]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo5.setDescription_TH("");
                        }

                        try
                        {
                            coverageInfo5.setDescription_EN(productPlan.getFx5().split("\\|")[2]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo5.setDescription_EN("");
                        }

                        try
                        {
                            coverageInfo5.setSum_Insured(productPlan.getFx5().split("\\|")[3]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo5.setSum_Insured("");
                        }

                        try
                        {
                            coverageInfo5.setDeduct(productPlan.getFx5().split("\\|")[4]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo5.setDeduct("");
                        }
                        
                        if(productPlanDetail.getFx5()!=null)
                        {   
                            if(productPlanDetail.getFx5().equals("|"))
                            {
                                coverageInfo5.setPremium_Amt("");
                                coverageInfo5.setSum_Insured_PlanDetail("");
                                coverageInfo5.setDeduct_PlanDetail("");
                            }
                            else if(productPlanDetail.getFx1().equals("|||"))
                            {
                                coverageInfo5.setPremium_Amt("");
                                coverageInfo5.setSum_Insured_PlanDetail("");
                                coverageInfo5.setDeduct_PlanDetail("");
                            }
                            else
                            {
                                if(productPlan.getFx5().split("\\|")[0].equals(productPlanDetail.getFx5().split("\\|")[0]))
                                {
                                    try
                                    {
                                        coverageInfo5.setPremium_Amt(productPlanDetail.getFx5().split("\\|")[1]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo5.setPremium_Amt("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo5.setSum_Insured_PlanDetail(productPlanDetail.getFx5().split("\\|")[2]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo5.setSum_Insured_PlanDetail("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo5.setDeduct_PlanDetail(productPlanDetail.getFx5().split("\\|")[3]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo5.setDeduct_PlanDetail("");
                                    }
                                }
                            }
                        }
                        else
                        {
                            coverageInfo5.setPremium_Amt("");
                            coverageInfo5.setSum_Insured_PlanDetail("");
                            coverageInfo5.setDeduct_PlanDetail("");
                        }
                        coverageInfoList.add(coverageInfo5);
                    }
                }
                //6
                if(productPlan.getFx6() != null)
                {   if(!(productPlan.getFx6().isEmpty()))
                    {
                        countNumberOfCoverage++;
                        try
                        {
                            coverageInfo6.setId("6");
                        }
                        catch (Exception ex)
                        {
                            coverageInfo6.setId("");
                        }

                        try
                        {
                            coverageInfo6.setCode(productPlan.getFx6().split("\\|")[0]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo6.setCode("");
                        }

                        try
                        {
                            coverageInfo6.setDescription_TH(productPlan.getFx6().split("\\|")[1]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo6.setDescription_TH("");
                        }

                        try
                        {
                            coverageInfo6.setDescription_EN(productPlan.getFx6().split("\\|")[2]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo6.setDescription_EN("");
                        }

                        try
                        {
                            coverageInfo6.setSum_Insured(productPlan.getFx6().split("\\|")[3]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo6.setSum_Insured("");
                        }

                        try
                        {
                            coverageInfo6.setDeduct(productPlan.getFx6().split("\\|")[4]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo6.setDeduct("");
                        }
                        
                        if(productPlanDetail.getFx6()!=null)
                        {   
                            if(productPlanDetail.getFx6().equals("|"))
                            {
                                coverageInfo6.setPremium_Amt("");
                                coverageInfo6.setSum_Insured_PlanDetail("");
                                coverageInfo6.setDeduct_PlanDetail("");
                            }
                            else if(productPlanDetail.getFx6().equals("|||"))
                            {
                                coverageInfo6.setPremium_Amt("");
                                coverageInfo6.setSum_Insured_PlanDetail("");
                                coverageInfo6.setDeduct_PlanDetail("");
                            }
                            else
                            {
                                if(productPlan.getFx6().split("\\|")[0].equals(productPlanDetail.getFx6().split("\\|")[0]))
                                {
                                    try
                                    {
                                        coverageInfo6.setPremium_Amt(productPlanDetail.getFx6().split("\\|")[1]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo6.setPremium_Amt("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo6.setSum_Insured_PlanDetail(productPlanDetail.getFx6().split("\\|")[2]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo6.setSum_Insured_PlanDetail("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo6.setDeduct_PlanDetail(productPlanDetail.getFx6().split("\\|")[3]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo6.setDeduct_PlanDetail("");
                                    }
                                }
                            }
                        }
                        else
                        {
                            coverageInfo6.setPremium_Amt("");
                            coverageInfo6.setSum_Insured_PlanDetail("");
                            coverageInfo6.setDeduct_PlanDetail("");
                        }
                        coverageInfoList.add(coverageInfo6);
                    }
                }
                //7
                if(productPlan.getFx7() != null)
                {   if(!(productPlan.getFx7().isEmpty()))
                    {
                        countNumberOfCoverage++;
                        try
                        {
                            coverageInfo7.setId("7");
                        }
                        catch (Exception ex)
                        {
                            coverageInfo7.setId("");
                        }

                        try
                        {
                            coverageInfo7.setCode(productPlan.getFx7().split("\\|")[0]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo7.setCode("");
                        }

                        try
                        {
                            coverageInfo7.setDescription_TH(productPlan.getFx7().split("\\|")[1]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo7.setDescription_TH("");
                        }

                        try
                        {
                            coverageInfo7.setDescription_EN(productPlan.getFx7().split("\\|")[2]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo7.setDescription_EN("");
                        }

                        try
                        {
                            coverageInfo7.setSum_Insured(productPlan.getFx7().split("\\|")[3]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo7.setSum_Insured("");
                        }

                        try
                        {
                            coverageInfo7.setDeduct(productPlanDetail.getFx7().split("\\|")[4]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo7.setDeduct("");
                        }
                        
                        if(productPlanDetail.getFx7()!=null)
                        {   
                            if(productPlanDetail.getFx7().equals("|"))
                            {
                                coverageInfo7.setPremium_Amt("");
                                coverageInfo7.setSum_Insured_PlanDetail("");
                                coverageInfo7.setDeduct_PlanDetail("");
                            }
                            else if(productPlanDetail.getFx7().equals("|||"))
                            {
                                coverageInfo7.setPremium_Amt("");
                                coverageInfo7.setSum_Insured_PlanDetail("");
                                coverageInfo7.setDeduct_PlanDetail("");
                            }
                            else
                            {
                                if(productPlan.getFx7().split("\\|")[0].equals(productPlanDetail.getFx7().split("\\|")[0]))
                                {
                                    try
                                    {
                                        coverageInfo7.setPremium_Amt(productPlanDetail.getFx7().split("\\|")[1]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo7.setPremium_Amt("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo7.setSum_Insured_PlanDetail(productPlanDetail.getFx7().split("\\|")[2]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo7.setSum_Insured_PlanDetail("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo7.setDeduct_PlanDetail(productPlanDetail.getFx7().split("\\|")[3]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo7.setDeduct_PlanDetail("");
                                    }
                                }
                            }
                        }
                        else
                        {
                            coverageInfo7.setPremium_Amt("");
                            coverageInfo7.setSum_Insured_PlanDetail("");
                            coverageInfo7.setDeduct_PlanDetail("");
                        }
                        coverageInfoList.add(coverageInfo7);
                    }
                }
                //8
                if(productPlan.getFx8() != null)
                {   if(!(productPlan.getFx8().isEmpty()))
                    {
                        countNumberOfCoverage++;
                        try
                        {
                            coverageInfo8.setId("8");
                        }
                        catch (Exception ex)
                        {
                            coverageInfo8.setId("");
                        }

                        try
                        {
                            coverageInfo8.setCode(productPlan.getFx8().split("\\|")[0]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo8.setCode("");
                        }

                        try
                        {
                            coverageInfo8.setDescription_TH(productPlan.getFx8().split("\\|")[1]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo8.setDescription_TH("");
                        }

                        try
                        {
                            coverageInfo8.setDescription_EN(productPlan.getFx8().split("\\|")[2]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo8.setDescription_EN("");
                        }

                        try
                        {
                            coverageInfo8.setSum_Insured(productPlan.getFx8().split("\\|")[3]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo8.setSum_Insured("");
                        }

                        try
                        {
                            coverageInfo8.setDeduct(productPlan.getFx8().split("\\|")[4]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo8.setDeduct("");
                        }
                        
                        if(productPlanDetail.getFx8()!=null)
                        {   
                            if(productPlanDetail.getFx8().equals("|"))
                            {
                                coverageInfo8.setPremium_Amt("");
                                coverageInfo8.setSum_Insured_PlanDetail("");
                                coverageInfo8.setDeduct_PlanDetail("");
                            }
                            else if(productPlanDetail.getFx8().equals("|||"))
                            {
                                coverageInfo8.setPremium_Amt("");
                                coverageInfo8.setSum_Insured_PlanDetail("");
                                coverageInfo8.setDeduct_PlanDetail("");
                            }
                            else
                            {
                                if(productPlan.getFx8().split("\\|")[0].equals(productPlanDetail.getFx8().split("\\|")[0]))
                                {
                                    try
                                    {
                                        coverageInfo8.setPremium_Amt(productPlanDetail.getFx8().split("\\|")[1]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo8.setPremium_Amt("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo8.setSum_Insured_PlanDetail(productPlanDetail.getFx8().split("\\|")[2]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo8.setSum_Insured_PlanDetail("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo8.setDeduct_PlanDetail(productPlanDetail.getFx8().split("\\|")[3]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo8.setDeduct_PlanDetail("");
                                    }
                                }
                            }
                        }
                        else
                        {
                            coverageInfo8.setPremium_Amt("");
                            coverageInfo8.setSum_Insured_PlanDetail("");
                            coverageInfo8.setDeduct_PlanDetail("");
                        }
                        coverageInfoList.add(coverageInfo8);
                    }
                }
                //9
                if(productPlan.getFx9() != null)
                {   if(!(productPlan.getFx9().isEmpty()))
                    {
                        countNumberOfCoverage++;
                        try
                        {
                            coverageInfo9.setId("9");
                        }
                        catch (Exception ex)
                        {
                            coverageInfo9.setId("");
                        }

                        try
                        {
                            coverageInfo9.setCode(productPlan.getFx9().split("\\|")[0]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo9.setCode("");
                        }

                        try
                        {
                            coverageInfo9.setDescription_TH(productPlan.getFx9().split("\\|")[1]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo9.setDescription_TH("");
                        }

                        try
                        {
                            coverageInfo9.setDescription_EN(productPlan.getFx9().split("\\|")[2]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo9.setDescription_EN("");
                        }

                        try
                        {
                            coverageInfo9.setSum_Insured(productPlan.getFx9().split("\\|")[3]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo9.setSum_Insured("");
                        }

                        try
                        {
                            coverageInfo9.setDeduct(productPlan.getFx9().split("\\|")[4]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo9.setDeduct("");
                        }
                        
                        if(productPlanDetail.getFx9()!=null)
                        {   
                            if(productPlanDetail.getFx9().equals("|"))
                            {
                                coverageInfo9.setPremium_Amt("");
                                coverageInfo9.setSum_Insured_PlanDetail("");
                                coverageInfo9.setDeduct_PlanDetail("");
                            }
                            else if(productPlanDetail.getFx9().equals("|||"))
                            {
                                coverageInfo9.setPremium_Amt("");
                                coverageInfo9.setSum_Insured_PlanDetail("");
                                coverageInfo9.setDeduct_PlanDetail("");
                            }
                            else
                            {
                                if(productPlan.getFx9().split("\\|")[0].equals(productPlanDetail.getFx9().split("\\|")[0]))
                                {
                                    try
                                    {
                                        coverageInfo9.setPremium_Amt(productPlanDetail.getFx9().split("\\|")[1]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo9.setPremium_Amt("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo9.setSum_Insured_PlanDetail(productPlanDetail.getFx9().split("\\|")[2]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo9.setSum_Insured_PlanDetail("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo9.setDeduct_PlanDetail(productPlanDetail.getFx9().split("\\|")[3]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo9.setDeduct_PlanDetail("");
                                    }
                                }
                            }
                        }
                        else
                        {
                            coverageInfo9.setPremium_Amt("");
                            coverageInfo9.setSum_Insured_PlanDetail("");
                            coverageInfo9.setDeduct_PlanDetail("");
                        }
                        coverageInfoList.add(coverageInfo9);
                    }
                }
                //10
                if(productPlan.getFx10() != null)
                {   if(!(productPlan.getFx10().isEmpty()))
                    {
                        countNumberOfCoverage++;
                        try
                        {
                            coverageInfo10.setId("10");
                        }
                        catch (Exception ex)
                        {
                            coverageInfo10.setId("");
                        }

                        try
                        {
                            coverageInfo10.setCode(productPlan.getFx10().split("\\|")[0]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo10.setCode("");
                        }

                        try
                        {
                            coverageInfo10.setDescription_TH(productPlan.getFx10().split("\\|")[1]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo10.setDescription_TH("");
                        }

                        try
                        {
                            coverageInfo10.setDescription_EN(productPlan.getFx10().split("\\|")[2]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo10.setDescription_EN("");
                        }

                        try
                        {
                            coverageInfo10.setSum_Insured(productPlan.getFx10().split("\\|")[3]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo10.setSum_Insured("");
                        }

                        try
                        {
                            coverageInfo10.setDeduct(productPlan.getFx10().split("\\|")[4]);
                        }
                        catch (Exception ex)
                        {
                            coverageInfo10.setDeduct("");
                        }
                        
                        if(productPlanDetail.getFx10()!=null)
                        {   
                            if(productPlanDetail.getFx10().equals("|"))
                            {
                                coverageInfo10.setPremium_Amt("");
                                coverageInfo10.setSum_Insured_PlanDetail("");
                                coverageInfo10.setDeduct_PlanDetail("");
                            }
                            else if(productPlanDetail.getFx10().equals("|||"))
                            {
                                coverageInfo10.setPremium_Amt("");
                                coverageInfo10.setSum_Insured_PlanDetail("");
                                coverageInfo10.setDeduct_PlanDetail("");
                            }
                            else
                            {
                                if(productPlan.getFx10().split("\\|")[0].equals(productPlanDetail.getFx10().split("\\|")[0]))
                                {
                                    try
                                    {
                                        coverageInfo10.setPremium_Amt(productPlanDetail.getFx10().split("\\|")[1]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo10.setPremium_Amt("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo10.setSum_Insured_PlanDetail(productPlanDetail.getFx10().split("\\|")[2]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo10.setSum_Insured_PlanDetail("");
                                    }
                                    
                                    try
                                    {
                                        coverageInfo10.setDeduct_PlanDetail(productPlanDetail.getFx10().split("\\|")[3]);
                                    }
                                    catch (Exception ex)
                                    {
                                        coverageInfo10.setDeduct_PlanDetail("");
                                    }
                                }
                            }
                        }
                        else
                        {
                            coverageInfo10.setPremium_Amt("");
                            coverageInfo10.setSum_Insured_PlanDetail("");
                            coverageInfo10.setDeduct_PlanDetail("");
                        }
                        coverageInfoList.add(coverageInfo10);
                    }
                }
                Gson gson = new Gson();
//                String convertToJson = gson.toJson(coverageInfoList);
                
                List<ProductPlanDetailInfo> productPlanDetailInfoList = new ArrayList<ProductPlanDetailInfo>();
                productPlanDetailInfo = new ProductPlanDetailInfo();
                productPlanDetailInfo.setPlan_detail_code(productPlanDetail.getCode());
                productPlanDetailInfo.setNumber_of_coverage(countNumberOfCoverage.toString());
                productPlanDetailInfo.setCoverage_info(coverageInfoList);
                productPlanDetailInfoList.add(productPlanDetailInfo);
                
                productPlanDetailInfoJson = gson.toJson(productPlanDetailInfoList);
                productPlanDetailInfoJson = productPlanDetailInfoJson.substring(1, productPlanDetailInfoJson.length()-1);

                paymentModeId = productPlanDetail.getPaymentMode();
                if (paymentModeId == null) {
                    paymentModeId = productPlanDetail.getProductPlan().getPaymentMode();
                }
                //
                if (StringUtils.isNotBlank(paymentModeId)) {
                    // find firstInstallmentNo
                    int noOfFirstInstallment = 0;
                    try {
                        List<PaymentMethodStringCastor> pmsc = PaymentModeConverter.convertToPaymentModeList(product.getPaymentMode());
                        // always setup to first payment method of particular paymenmode
                        for (PaymentMethodStringCastor o : pmsc) {
                            int inoOfFirstInstallment = o.getNoPayPeriod(this.paymentModeId);
                            if (inoOfFirstInstallment > 0) {
                                noOfFirstInstallment = inoOfFirstInstallment;
                                break;
                            }
                        }
                    } catch (Exception e) {
                        log.error(this.getClass().getName() + ".calculateLifeNonLife : Exception has occured when convert payment mode => " + e.getMessage());
                        //e.printStackTrace();
                        // using old fashion of applicaiton
                        if (paymentModeId.equals("1")) {
                            noOfFirstInstallment = product.getMonthlyFirstPayment();
                        } else if (paymentModeId.equals("2")) {
                            noOfFirstInstallment = product.getMonthlyFirstPayment();
                        } else {
                            noOfFirstInstallment = 1;
                        }
                    }
                    this.totalPrice = this.netPremium * noOfFirstInstallment;
                }

            } else {
                clearPrice();
            }
        } else {
            clearPrice();
        }
    }
    
    public Integer convertStringToInt(String data){
        int number = 0;
        if(!data.isEmpty()){
            number = Integer.parseInt(data);
        }
        return number;
    }
    
    public void ageValueChangeListener(ValueChangeEvent event) {
        age = (Integer) event.getNewValue();

        calculate();
    }

    public void paymentModeValueChangeListener(ValueChangeEvent event) {
        paymentModeId = (String) event.getNewValue();

        productPlanDetail = null;
        productPlanId = 0;

        if(checkFamily(product)){ 
        productPlanList = productPlanDAO.findProductPlanEntitiesProductPaymentModeByFamily(product.getId(), paymentModeId, gender);
        }else {
        productPlanList = productPlanDAO.findProductPlanEntitiesByProductPaymentMode(product.getId(), paymentModeId, gender);
        }
        calculate();
    }

    public void genderActionListener(ValueChangeEvent event) {
        gender = (String) event.getNewValue();

        productPlanDetail = null;
        productPlanId = 0;
        
        if(checkFamily(product)){
        productPlanList = productPlanDAO.findProductPlanEntitiesProductPaymentModeByFamily(product.getId(), paymentModeId, gender);
        }else {
        productPlanList = productPlanDAO.findProductPlanEntitiesByProductPaymentMode(product.getId(), paymentModeId, gender);
        }
        calculate();
    }

    public void productPlanValueChangeListener(ValueChangeEvent event) {
        productPlanId = (Integer) event.getNewValue();

        productPlanDetail = null;
        calculate();
    }
    
     public boolean checkFamily(Product product) {  
        int product2 = product.getId();
        int chkFamily = productDAO.checkFamily(product2);
        if(chkFamily == 0){
            return false;
        }else {
            return true;
        }
    }

    public void calculate() {
        if (defaultCategoryType.equals("life")) {
            calculateLifeNonLife();
        } else if (defaultCategoryType.equals("nonlife")) {
            calculateLifeNonLife();
        } else if (defaultCategoryType.equals("motor")) {
            calculateMotor();
        } else if (defaultCategoryType.equals("motoradvance")) {
            calculateMotorAdvance();
        } else if (defaultCategoryType.equals("retail")) {
            calculateRetail();
        }
    }

    private void clearPrice() {
        quantity = null;
        //age = null;
        price = null;
        stampDuty = null;
        vat = null;
        netPremium = null;
        productPlanId = null;
        totalPrice = null;
    }

    public void campaignChangeListener() {
        this.initTab();
    }

    private void initTab() {
        showTabLife = false;
        showTabNonLife = false;
        showTabMotor = false;
        showTabRetail = false;
        defaultCategoryType = "";
        productCategoryId = 0;
        productList = null;
        product = null;
        List<String> list = null;
        if (campaignId != 0) {
            campaign = campaignDAO.findCampaign(campaignId);
            if (campaign.getInbound() != null && campaign.getInbound() == true && assignmentDetail.getAssignmentId() == null) {
                Assignment a = campaign.getAssignmentInbound();
                if (a == null) {
                    a = new Assignment();
                    a.setCampaign(campaign);
                    a.setMarketing(campaign.getMarketingInbound());
                    a.setAssignDate(new Date());
                    a.setAssignmentMode("supervisor");
                    a.setAssignmentType("custom");
                    a.setCreateByUser(userSession.getUsers());
                    a.setCreateBy(userSession.getUserName());
                    a.setCreateDate(new Date());
                    assignmentDAO.create(a);
                    ///
                    Collection<Product> products = campaign.getProductCollection();
                    List<AssignmentProduct> aps = new ArrayList<AssignmentProduct>();
                    for (Product obj : products) {
                        AssignmentProductPK pk = new AssignmentProductPK(a.getId(), obj.getId());
                        AssignmentProduct ap = new AssignmentProduct(pk);
                        ap.setProduct(obj);
                        ap.setProductCode(obj.getCode());
                        ap.setProductName(obj.getName());
                        aps.add(ap);
                    }
                    a.setAssignmentProductCollection(aps);
                    assignmentDAO.edit(a);
                    campaign.setAssignmentInbound(a);
                    campaignDAO.edit(campaign);

                }

                assignmentDetail.setCampaignName(campaign.getName());
                assignmentDetail.setMarketingCode(campaign.getMarketingInbound().getCode());
                assignmentDetail.setAssignmentId(a);
                assignmentDetail.setMaxCall(campaign.getMaxCall());
                assignmentDetail.setMaxCall2(campaign.getMaxCall2());
                if (assignmentDetail.getId() == null && JSFUtil.getUserSession().isDialClicked()) {
                    assignmentDetail.setCallAttempt(1);
                } else if (assignmentDetail.getId() == null && !JSFUtil.getUserSession().isDialClicked()) {
                    assignmentDetail.setCallAttempt(0);
                }
                assignmentDetail.setCallAttempt2(0);
                userSession.setAssignmentDetail(assignmentDetail);

                list = productCategoryDAO.findProductCategoryTypeByCampaign(campaignId);
            } else {
                //assignmentDetail = assignmentDetailDAO.findAssignmentDetail(adId);
                //list = productCategoryDAO.findProductCategoryType(campaignId, userSession.getUsers().getId(), userSession.getCustomerDetail().getId());
                list = productCategoryDAO.findProductCategoryTypeByCampaign(campaignId);
            }
            JSFUtil.getUserSession().setAssignmentDetail(assignmentDetail);

//        defaultCategoryType = "";
            if (list != null && !list.isEmpty()) {
                for (String categoryType : list) {
                    if (categoryType.equals("life")) {
                        showTabLife = true;
                        defaultCategoryType = defaultCategoryType.equals("") ? "life" : defaultCategoryType;
                    } else if (categoryType.equals("nonlife")) {
                        showTabNonLife = true;
                        defaultCategoryType = defaultCategoryType.equals("") ? "nonlife" : defaultCategoryType;
                    } else if (categoryType.equals("motor")) {
                        showTabMotor = true;
                        defaultCategoryType = defaultCategoryType.equals("") ? "motor" : defaultCategoryType;
                    } else if (categoryType.equals("retail")) {
                        showTabRetail = true;
                        defaultCategoryType = defaultCategoryType.equals("") ? "retail" : defaultCategoryType;
                    } else if (categoryType.equals("motoradvance")) {
                        showTabMotorAdvance = true;
                        defaultCategoryType = defaultCategoryType.equals("") ? "motoradvance" : defaultCategoryType;
                    }
                }
                this.findProductCategory();
                tabAction(defaultCategoryType);
            }
        }
    }

    private void findProductCategory() {
        if (campaignList != null && !campaignList.isEmpty()) {
            if (campaign.getInbound() != null && campaign.getInbound()) {
                productCategoryList = productCategoryDAO.getProductCateogryListBy1(campaignId, defaultCategoryType);
            } else {
                if (userSession.getUsers() != null && userSession.getUsers().getId() != null
                        && userSession.getCustomerDetail() != null && userSession.getCustomerDetail().getId() != null
                        && campaignId != null
                        && defaultCategoryType != null) {
                    productCategoryList = productCategoryDAO.getProductCateogryListBy(userSession.getUsers().getId(), userSession.getCustomerDetail().getId(), campaignId, defaultCategoryType);
                }
            }
        }

    }

    public void tabListener(ActionEvent event) {
        product = null;
        paymentModeList = null;
        productPlanList = null;
        productPlanDetail = null;

        String param = JSFUtil.getRequestParameterMap("categoryType");
        defaultCategoryType = param;
        this.findProductCategory();
        tabAction(defaultCategoryType);
    }

    public void modelTypeChangeListener(ValueChangeEvent event) {
        brandId = 0;
        modelId = 0;
        modelYear = 0;
        modelTypeId = (Integer) event.getNewValue();
        findBrandList(modelTypeId);
    }

    private void findBrandList(Integer id) {
        brandList = brandDAO.findByModelType(id);
        modelList = null;
    }

    public void brandChangeListener(ValueChangeEvent event) {
        modelId = 0;
        modelYear = 0;
        brandId = (Integer) event.getNewValue();
        findModelList(modelTypeId, brandId);
    }

    private void findModelList(Integer modelTypeId, Integer brandId) {
        modelList = modelDAO.findModelByModelTypeBrand(modelTypeId, brandId);
    }

    private void tabAction(String type) {

        if (type.equals("life")) {
            this.toLife();
        } else if (type.equals("nonlife")) {
            this.toNonLife();
        } else if (type.equals("motor")) {
            this.toMotor();
        } else if (type.equals("motoradvance")) {
            this.toMotorAdvance();
        } else if (type.equals("retail")) {
            this.toRetail();
        }
    }

    private void toLife() {
        //productList = productDAO.findProductBy(userSession.getUsers().getId(), userSession.getCustomerDetail().getId(), defaultCategoryType, campaignId, productCategoryId);
        changeProductCategory();
    }

    private void toNonLife() {
        //productList = productDAO.findProductBy(userSession.getUsers().getId(), userSession.getCustomerDetail().getId(), defaultCategoryType, campaignId, productCategoryId);
        changeProductCategory();
    }

    private void toMotor() {
        productPlanCategoryList = productPlanCategoryDAO.findProductPlanCategoryEntities();
        modelTypeList = modelTypeDAO.findModelTypeEntities();
    }

    private void toMotorAdvance() {
        //productList = productDAO.findProductBy(userSession.getUsers().getId(), userSession.getCustomerDetail().getId(), defaultCategoryType, campaignId, productCategoryId);
    }

    private void toRetail() {

    }

    public void dateStartListener(ValueChangeEvent e) {
        dateStart = (Date) e.getNewValue();
        age = this.calculateYear(dateStart);
        this.calculateMotorAdvance();
    }

    private Integer calculateYear(Date d) {
        Calendar dob = Calendar.getInstance();
        dob.setTime(d);
        Calendar today = Calendar.getInstance();
        int y1 = dob.get(Calendar.YEAR);
        int y2 = today.get(Calendar.YEAR);
        int m1 = dob.get(Calendar.MONTH);
        int m2 = today.get(Calendar.MONTH);
        int d1 = dob.get(Calendar.DATE);
        int d2 = today.get(Calendar.DATE);
        Integer y = y2 - y1;
        if ((m1 > m2) || (m1 == m2 && d1 > d2)) //ถ้าเดือนเกิดมากกว่าเดือนปัจจุบัน หรือวันเกิดมากกว่าวันปัจจุบัน -> ยังไม่ครบปี
        {
            y--;
        }
        return y;
    }

    public void checkProductCategoryAllListener(ActionEvent event) {
        checkProductCategoryAll = !Boolean.parseBoolean(JSFUtil.getRequestParameterMap("checkProductCategoryAll"));
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                item.setValue(checkProductCategoryAll);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void checkProductPlanCategoryAllListener(ActionEvent event) {
        checkProductPlanCategoryAll = !Boolean.parseBoolean(JSFUtil.getRequestParameterMap("checkProductPlanCategoryAll"));
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds1.entrySet()) {
                item.setValue(checkProductPlanCategoryAll);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void searchActionListener(ActionEvent event) {
        page = 1;
        getProductCategoryIdList();
        getProductPlanCategoryIdList();

        search();
    }

    private void search() {
        int itemPerPage = JSFUtil.getApplication().getMaxrows();
        int firstResult = ((page - 1) * itemPerPage);
        List<ProductPlan> list = null;
        if (campaign.getInbound()) {
            list = productDAO.findProductByInbound(defaultCategoryType, campaignId, productCategoryIdList, productPlanCategoryIdList, brandId, modelId, modelTypeId, modelYear, sumInsuredMin, sumInsuredMax, claimType, firstResult, itemPerPage);
            rows = productDAO.countProductByInbound(defaultCategoryType, campaignId, productCategoryIdList, productPlanCategoryIdList, brandId, modelId, modelTypeId, modelYear, sumInsuredMin, sumInsuredMax, claimType);
        } else {
            list = productDAO.findProductBy(userSession.getUsers().getId(), userSession.getCustomerDetail().getId(), defaultCategoryType, campaignId, productCategoryIdList, productPlanCategoryIdList, brandId, modelId, modelTypeId, modelYear, sumInsuredMin, sumInsuredMax, claimType, firstResult, itemPerPage);
            rows = productDAO.countProductBy(userSession.getUsers().getId(), userSession.getCustomerDetail().getId(), defaultCategoryType, campaignId, productCategoryIdList, productPlanCategoryIdList, brandId, modelId, modelTypeId, modelYear, sumInsuredMin, sumInsuredMax, claimType);
        }
        dataList = list != null ? new PagedList(list, rows, itemPerPage) : null;
    }

    public String replaceTagField(String text) {
        String custName = JSFUtil.getUserSession().getCustomerDetail().getInitial() != null ? JSFUtil.getUserSession().getCustomerDetail().getInitial() + " " : "";
        custName += JSFUtil.getUserSession().getCustomerDetail().getName() != null ? JSFUtil.getUserSession().getCustomerDetail().getName() + " " : "";
        custName += JSFUtil.getUserSession().getCustomerDetail().getSurname() != null ? JSFUtil.getUserSession().getCustomerDetail().getSurname() : "";
        String userName = JSFUtil.getUserSession().getUsers().getName() != null ? JSFUtil.getUserSession().getUsers().getName() : "" + " " + JSFUtil.getUserSession().getUsers().getSurname() != null ? JSFUtil.getUserSession().getUsers().getSurname() : "";
        text = StringUtils.replace(text, "#customername", (custName != null) && (!custName.equals(" ")) ? custName : "#customername");
        text = StringUtils.replace(text, "#username", (userName != null) && (!userName.equals(" ")) ? userName : "#username");
        text = StringUtils.replace(text, "#tmrlicenseno", (JSFUtil.getUserSession().getUsers().getLicenseNo() != null) && (!JSFUtil.getUserSession().getUsers().getLicenseNo().equals(" ")) ? JSFUtil.getUserSession().getUsers().getLicenseNo() : "#tmrlicenseno");
        text = StringUtils.replace(text, "#tmrdescription", (JSFUtil.getUserSession().getUsers().getDescription() != null) && (!JSFUtil.getUserSession().getUsers().getDescription().equals(" ")) ? JSFUtil.getUserSession().getUsers().getDescription() : "#tmrdescription");

        return text;
    }

    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }

    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }

    public AssignmentDetail getAssignmentDetail() {
        return assignmentDetail;
    }

    public void setAssignmentDetail(AssignmentDetail assignmentDetail) {
        this.assignmentDetail = assignmentDetail;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Object> getProductList() {
        return productList;
    }

    public void setProductList(List<Object> productList) {
        this.productList = productList;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Integer productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public ProductCategoryDAO getProductCategoryDAO() {
        return productCategoryDAO;
    }

    public void setProductCategoryDAO(ProductCategoryDAO productCategoryDAO) {
        this.productCategoryDAO = productCategoryDAO;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public List<TempPurchaseOrder> getTempPurchaseOrderList() {
        return tempPurchaseOrderList;
    }

    public void setTempPurchaseOrderList(List<TempPurchaseOrder> tempPurchaseOrderList) {
        this.tempPurchaseOrderList = tempPurchaseOrderList;
    }

    public TempPurchaseOrderDetail getTempPurchaseOrderDetail() {
        return tempPurchaseOrderDetail;
    }

    public void setTempPurchaseOrderDetail(TempPurchaseOrderDetail tempPurchaseOrderDetail) {
        this.tempPurchaseOrderDetail = tempPurchaseOrderDetail;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public PurchaseOrderDetailDAO getPurchaseOrderDetailDAO() {
        return purchaseOrderDetailDAO;
    }

    public void setPurchaseOrderDetailDAO(PurchaseOrderDetailDAO purchaseOrderDetailDAO) {
        this.purchaseOrderDetailDAO = purchaseOrderDetailDAO;
    }

    public List<Object> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<Object> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }

    public Double getNetPremium() {
        return netPremium;
    }

    public void setNetPremium(Double netPremium) {
        this.netPremium = netPremium;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Object> getProductPlanList() {
        return productPlanList;
    }

    public void setProductPlanList(List<Object> productPlanList) {
        this.productPlanList = productPlanList;
    }

    public Integer getProductPlanId() {
        return productPlanId;
    }

    public void setProductPlanId(Integer productPlanId) {
        this.productPlanId = productPlanId;
    }

    public List<String[]> getPaymentModeList() {
        return paymentModeList;
    }

    public void setPaymentModeList(List<String[]> paymentModeList) {
        this.paymentModeList = paymentModeList;
    }

    public ProductPlanDAO getProductPlanDAO() {
        return productPlanDAO;
    }

    public void setProductPlanDAO(ProductPlanDAO productPlanDAO) {
        this.productPlanDAO = productPlanDAO;
    }

    public ProductPlan getProductPlan() {
        return productPlan;
    }

    public void setProductPlan(ProductPlan productPlan) {
        this.productPlan = productPlan;
    }

    public ProductPlanDetailDAO getProductPlanDetailDAO() {
        return productPlanDetailDAO;
    }

    public void setProductPlanDetailDAO(ProductPlanDetailDAO productPlanDetailDAO) {
        this.productPlanDetailDAO = productPlanDetailDAO;
    }

    public ProductPlanDetail getProductPlanDetail() {
        return productPlanDetail;
    }

    public void setProductPlanDetail(ProductPlanDetail productPlanDetail) {
        this.productPlanDetail = productPlanDetail;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getStampDuty() {
        return stampDuty;
    }

    public void setStampDuty(Double stampDuty) {
        this.stampDuty = stampDuty;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public DistrictDAO getDistrictDAO() {
        return districtDAO;
    }

    public void setDistrictDAO(DistrictDAO districtDAO) {
        this.districtDAO = districtDAO;
    }

    public String getTempCategoryType() {
        return tempCategoryType;
    }

    public void setTempCategoryType(String tempCategoryType) {
        this.tempCategoryType = tempCategoryType;
    }

    public TempPurchaseOrder getTempPurchaseOrder() {
        return tempPurchaseOrder;
    }

    public void setTempPurchaseOrder(TempPurchaseOrder tempPurchaseOrder) {
        this.tempPurchaseOrder = tempPurchaseOrder;
    }

//    public boolean isEnableBtnAddToCart() {
//        return enableBtnAddToCart;
//    }
//
//    public void setEnableBtnAddToCart(boolean enableBtnAddToCart) {
//        this.enableBtnAddToCart = enableBtnAddToCart;
//    }
//    public boolean isEnableBtnRegister() {
//        return enableBtnRegister;
//    }
//
//    public void setEnableBtnRegister(boolean enableBtnRegister) {
//        this.enableBtnRegister = enableBtnRegister;
//    }
    public List<Campaign> getCampaignList() {
        return campaignList;
    }

    public void setCampaignList(List<Campaign> campaignList) {
        this.campaignList = campaignList;
    }

    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Boolean getShowTabLife() {
        return showTabLife;
    }

    public void setShowTabLife(Boolean showTabLife) {
        this.showTabLife = showTabLife;
    }

    public Boolean getShowTabMotor() {
        return showTabMotor;
    }

    public void setShowTabMotor(Boolean showTabMotor) {
        this.showTabMotor = showTabMotor;
    }

    public Boolean getShowTabNonLife() {
        return showTabNonLife;
    }

    public void setShowTabNonLife(Boolean showTabNonLife) {
        this.showTabNonLife = showTabNonLife;
    }

    public Boolean getShowTabRetail() {
        return showTabRetail;
    }

    public void setShowTabRetail(Boolean showTabRetail) {
        this.showTabRetail = showTabRetail;
    }

    public String getDefaultCategoryType() {
        return defaultCategoryType;
    }

    public void setDefaultCategoryType(String defaultCategoryType) {
        this.defaultCategoryType = defaultCategoryType;
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

    public Integer getProductPlanCategoryId() {
        return productPlanCategoryId;
    }

    public void setProductPlanCategoryId(Integer productPlanCategoryId) {
        this.productPlanCategoryId = productPlanCategoryId;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public List<Model> getModelList() {
        return modelList;
    }

    public void setModelList(List<Model> modelList) {
        this.modelList = modelList;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    public Integer getModelTypeId() {
        return modelTypeId;
    }

    public void setModelTypeId(Integer modelTypeId) {
        this.modelTypeId = modelTypeId;
    }

    public List<ModelType> getModelTypeList() {
        return modelTypeList;
    }

    public void setModelTypeList(List<ModelType> modelTypeList) {
        this.modelTypeList = modelTypeList;
    }

    public SelectItem[] getModelYearList() {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy", Locale.US);
        int year = Integer.parseInt(simpleDateformat.format(new Date()));
        modelYearList = new SelectItem[20];
        for (int i = 0; i < 20; i++) {
            Integer y = year - i;
            modelYearList[i] = new SelectItem(y, y.toString());
        }
        return modelYearList;
    }

    public void setModelYearList(SelectItem[] modelYearList) {
        this.modelYearList = modelYearList;
    }

    public BrandDAO getBrandDAO() {
        return brandDAO;
    }

    public void setBrandDAO(BrandDAO brandDAO) {
        this.brandDAO = brandDAO;
    }

    public ModelDAO getModelDAO() {
        return modelDAO;
    }

    public void setModelDAO(ModelDAO modelDAO) {
        this.modelDAO = modelDAO;
    }

    public ModelTypeDAO getModelTypeDAO() {
        return modelTypeDAO;
    }

    public void setModelTypeDAO(ModelTypeDAO modelTypeDAO) {
        this.modelTypeDAO = modelTypeDAO;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public Boolean getCheckProductCategoryAll() {
        return checkProductCategoryAll;
    }

    public void setCheckProductCategoryAll(Boolean checkProductCategoryAll) {
        this.checkProductCategoryAll = checkProductCategoryAll;
    }

    public Boolean getCheckProductPlanCategoryAll() {
        return checkProductPlanCategoryAll;
    }

    public void setCheckProductPlanCategoryAll(Boolean checkProductPlanCategoryAll) {
        this.checkProductPlanCategoryAll = checkProductPlanCategoryAll;
    }

    public Map<Integer, Boolean> getSelectedIds1() {
        return selectedIds1;
    }

    public void setSelectedIds1(Map<Integer, Boolean> selectedIds1) {
        this.selectedIds1 = selectedIds1;
    }

    public List<Integer> getProductCategoryIdList() {
        List<Integer> list = new ArrayList<Integer>();
        for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
            if (item.getValue()) {
                list.add(item.getKey());
            }
        }
        productCategoryIdList = list;
        return productCategoryIdList;
    }

    public void setProductCategoryIdList(List<Integer> productCategoryIdList) {
        this.productCategoryIdList = productCategoryIdList;
    }

    public List<Integer> getProductPlanCategoryIdList() {
        List<Integer> list = new ArrayList<Integer>();
        for (Map.Entry<Integer, Boolean> item : selectedIds1.entrySet()) {
            if (item.getValue()) {
                list.add(item.getKey());
            }
        }
        productPlanCategoryIdList = list;
        return productPlanCategoryIdList;
    }

    public void setProductPlanCategoryIdList(List<Integer> productPlanCategoryIdList) {
        this.productPlanCategoryIdList = productPlanCategoryIdList;
    }

    public List<ProductPlan> getDataList() {
        return dataList;
    }

    public void setDataList(List<ProductPlan> dataList) {
        this.dataList = dataList;
    }

    public String getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(String paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        this.search();
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public AssignmentDAO getAssignmentDAO() {
        return assignmentDAO;
    }

    public void setAssignmentDAO(AssignmentDAO assignmentDAO) {
        this.assignmentDAO = assignmentDAO;
    }

    public Boolean getCampaignInbound() {
        return campaignInbound;
    }

    public void setCampaignInbound(Boolean campaignInbound) {
        this.campaignInbound = campaignInbound;
    }

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }

    public MarketingCustomerDAO getMarketingCustomerDAO() {
        return marketingCustomerDAO;
    }

    public void setMarketingCustomerDAO(MarketingCustomerDAO marketingCustomerDAO) {
        this.marketingCustomerDAO = marketingCustomerDAO;
    }

    public Integer getSumInsuredMax() {
        return sumInsuredMax;
    }

    public void setSumInsuredMax(Integer sumInsuredMax) {
        this.sumInsuredMax = sumInsuredMax;
    }

    public Integer getSumInsuredMin() {
        return sumInsuredMin;
    }

    public void setSumInsuredMin(Integer sumInsuredMin) {
        this.sumInsuredMin = sumInsuredMin;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public Boolean getShowTabMotorAdvance() {
        return showTabMotorAdvance;
    }

    public void setShowTabMotorAdvance(Boolean showTabMotorAdvance) {
        this.showTabMotorAdvance = showTabMotorAdvance;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Integer getNoYear() {
        return noYear;
    }

    public void setNoYear(Integer noYear) {
        this.noYear = noYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public Integer getAdIdInbound() {
        return adIdInbound;
    }

    public void setAdIdInbound(Integer adIdInbound) {
        this.adIdInbound = adIdInbound;
    }

    public ContactHistoryDAO getContactHistoryDAO() {
        return contactHistoryDAO;
    }

    public void setContactHistoryDAO(ContactHistoryDAO contactHistoryDAO) {
        this.contactHistoryDAO = contactHistoryDAO;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(Double sumInsured) {
        this.sumInsured = sumInsured;
    }

    public CoverageInfo getCoverageInfo1() {
        return coverageInfo1;
    }

    public void setCoverageInfo1(CoverageInfo coverageInfo1) {
        this.coverageInfo1 = coverageInfo1;
    }

    public CoverageInfo getCoverageInfo2() {
        return coverageInfo2;
    }

    public void setCoverageInfo2(CoverageInfo coverageInfo2) {
        this.coverageInfo2 = coverageInfo2;
    }

    public CoverageInfo getCoverageInfo3() {
        return coverageInfo3;
    }

    public void setCoverageInfo3(CoverageInfo coverageInfo3) {
        this.coverageInfo3 = coverageInfo3;
    }

    public CoverageInfo getCoverageInfo4() {
        return coverageInfo4;
    }

    public void setCoverageInfo4(CoverageInfo coverageInfo4) {
        this.coverageInfo4 = coverageInfo4;
    }

    public CoverageInfo getCoverageInfo5() {
        return coverageInfo5;
    }

    public void setCoverageInfo5(CoverageInfo coverageInfo5) {
        this.coverageInfo5 = coverageInfo5;
    }

    public CoverageInfo getCoverageInfo6() {
        return coverageInfo6;
    }

    public void setCoverageInfo6(CoverageInfo coverageInfo6) {
        this.coverageInfo6 = coverageInfo6;
    }

    public CoverageInfo getCoverageInfo7() {
        return coverageInfo7;
    }

    public void setCoverageInfo7(CoverageInfo coverageInfo7) {
        this.coverageInfo7 = coverageInfo7;
    }

    public CoverageInfo getCoverageInfo8() {
        return coverageInfo8;
    }

    public void setCoverageInfo8(CoverageInfo coverageInfo8) {
        this.coverageInfo8 = coverageInfo8;
    }

    public CoverageInfo getCoverageInfo9() {
        return coverageInfo9;
    }

    public void setCoverageInfo9(CoverageInfo coverageInfo9) {
        this.coverageInfo9 = coverageInfo9;
    }

    public CoverageInfo getCoverageInfo10() {
        return coverageInfo10;
    }

    public void setCoverageInfo10(CoverageInfo coverageInfo10) {
        this.coverageInfo10 = coverageInfo10;
    }

    public ProductPlanDetailInfo getProductPlanDetailInfo() {
        return productPlanDetailInfo;
    }

    public void setProductPlanDetailInfo(ProductPlanDetailInfo productPlanDetailInfo) {
        this.productPlanDetailInfo = productPlanDetailInfo;
    }

    public String getProductPlanDetailInfoJson() {
        return productPlanDetailInfoJson;
    }

    public void setProductPlanDetailInfoJson(String productPlanDetailInfoJson) {
        this.productPlanDetailInfoJson = productPlanDetailInfoJson;
    }

    public boolean isShowEMsgAgeOutOfRange() {
        boolean result = false;
        if (this.age != null && this.productPlanId != null && this.paymentModeId != null && !this.paymentModeId.isEmpty()) {
            result = this.productPlanDetail == null;
            if (result == false && this.productPlanDetail.getFromVal() != null && this.productPlanDetail.getToVal() != null) {
                result = !(this.productPlanDetail.getFromVal().intValue() <= this.age.intValue() && this.productPlanDetail.getToVal().intValue() >= this.age.intValue());
            }
        }
        return result;
    }

    public Integer getContactResultPlanId() {
        return contactResultPlanId;
    }

    public void setContactResultPlanId(Integer contactResultPlanId) {
        this.contactResultPlanId = contactResultPlanId;
    }

    public List<CoverageInfo> getCoverageInfoList() {
        return coverageInfoList;
    }

    public void setCoverageInfoList(List<CoverageInfo> coverageInfoList) {
        this.coverageInfoList = coverageInfoList;
    }

    public String getProductFlexField1() {
        return productFlexField1;
    }

    public void setProductFlexField1(String productFlexField1) {
        this.productFlexField1 = productFlexField1;
    }
}