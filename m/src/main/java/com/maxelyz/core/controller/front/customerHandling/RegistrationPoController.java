package com.maxelyz.core.controller.front.customerHandling;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.maxelyz.converter.PaymentModeConverter;
import com.maxelyz.core.common.dto.MxLocalServiceResponse;
import com.maxelyz.core.common.dto.PaymentMethodStringCastor;
import com.maxelyz.core.common.front.dispatch.FrontDispatcher;
import com.maxelyz.core.common.front.dispatch.RegistrationPouch;
import com.maxelyz.core.common.log.RegistrationLogging;
import com.maxelyz.core.common.log.TimeCollector;
import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.ndao.RegistrationTxDAO;
import com.maxelyz.core.model.value.admin.CreditCardUsageValue;
import com.maxelyz.core.model.value.front.customerHandling.*;
import com.maxelyz.core.service.NextSequenceService;
import com.maxelyz.core.service.nift.RegistrationTrxService;
import com.maxelyz.utils.DataTransferUtil;
import com.maxelyz.utils.FormUtil;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import com.maxelyz.validator.CreditCardValidator;
import com.maxelyz.validator.ThaiIDValidator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionSystemException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

@ManagedBean(name = "registrationPoController")
@ViewScoped
public class RegistrationPoController extends BaseController implements Serializable {

    /**
     * ************************* FORWARD PAGE - PROPERTY
     * *********************************
     */
    private static final String SUCCESS = "/front/customerHandling/registrationpo.xhtml";
    private static final String CONFIRMATION_PAGE = "/front/customerHandling/confirmationPo.xhtml";
    private static final String PAYMENT_PAGE = "/front/customerHandling/payment.xhtml";
    private static final String SALEHISTORY_PAGE = "/front/customerHandling/saleHistory.xhtml?faces-redirect=true";
    private static String REDIRECT_PAGE = JSFUtil.getRequestContextPath() + "/campaign/assignmentList.jsf";

    private static final String SELF = "registrationpo.xhtml";
    private static String EDIT = "";

    /**
     * ************************* SUB CLASS
     * *****************************************
     */
    public enum FieldName {
        AGE, BMI, QUESTION1, QUESTION2, QUESTION3, QUESTION4, QUESTION5, QUESTION6, QUESTION7, QUESTION8, QUESTION9, QUESTION10, QUESTION11, QUESTION12, QUESTION13, QUESTION14, QUESTION15, QUESTION16, QUESTION17, QUESTION18, QUESTION19, QUESTION20, QUESTION21, QUESTION22, QUESTION23, QUESTION24, QUESTION25, QUESTION26, QUESTION27, QUESTION28, QUESTION29, QUESTION30;
    }

    /**
     * ************************* FORM - PROPERTY
     * *********************************
     */
    private TimeZone defaultTimeZone;
    private Date defaultDob = null;
    private Date defaultCardExpiryDate = null;
    private Product product;
    private RegistrationForm registrationForm;
    private List<ChildRegForm> childRegForm;
    private List<DeclarationForm> declarationForm;
    private PurchaseOrder purchaseOrder;
    private PurchaseOrderDetail purchaseOrderDetail;
    //private List<PurchaseOrderChildRegister> poChildRegisterList;
    //private List<PurchaseOrderDeclaration> poDeclarationList;
    private List provinceList;
    private List<Province> fprovinceList;
    private List<String[]> paymentModeList;
    private List<PaymentMethod> paymentMethodList;
    private List cardIssuerList;
    private boolean online = false;
    private List<RegistrationInfoValue<PurchaseOrderRegister>> regInfo = new ArrayList<RegistrationInfoValue<PurchaseOrderRegister>>();
    private List<ChildRegType> childRegType = new ArrayList<ChildRegType>();
    private List<ChildRegInfoValue<PurchaseOrderChildRegister>> childRegInfo = new ArrayList<ChildRegInfoValue<PurchaseOrderChildRegister>>();
    public List<List<ChildRegInfoValue<PurchaseOrderChildRegister>>> childRegInfoList = new ArrayList<List<ChildRegInfoValue<PurchaseOrderChildRegister>>>();
    private List<DeclarationInfoValue<PurchaseOrderDeclaration>> declarInfo = new ArrayList<DeclarationInfoValue<PurchaseOrderDeclaration>>();
    private List<NosaleReason> nosaleReasonList;
    private List<FollowupsaleReason> followupsaleReasonList;
    private String message;
    private Boolean disableSaveButton = false;
    private Integer nosaleReasonId;
    private Integer followupsaleReasonId;
    private List<Object> productPlanList;
    private Integer paymentMethodId;
    private Integer productPlanId;
    private String paymentModeId;
    private Integer poId;
    private boolean fromSaleApprovalPage = false;
    private boolean showPrevious = true;
    private boolean showNext = true;
    private String gender;

    private ProductPlan productPlan;
    private ProductPlanDetail productPlanDetail;
    private Double price;
    private Double stampDuty;
    private Double vat;
    private Double netPremium;
    private Double annualNetPremium;
    private Double annualPrice;
    private Double sumInsured;
    private Double totalPrice;
    private String categoryType;
    private Boolean checkAct = false;
    private Boolean checkDiscount = false;
    private Double sum1 = new Double(0);
    private Double sumNoVat = new Double(0);
    private Double sumVat = new Double(0);
    private String productPlanDetailInfoJson;

    private List<OccupationCategory> occupationCategoryList;
    private List<PurchaseOrderInstallment> poInstallmentList;
    private PurchaseOrderInstallment poi;

    private Integer noPaid = 0;
    private Double paidTotal = new Double(0);
    private Map<Integer, Integer> noInstallmentList;
    private Boolean edit = false;
    private Boolean editPayment = true;
    private PurchaseOrderApprovalLog purchaseOrderApprovalLog;
    private QaTrans qaTrans;
    public List<QaTransDetail> qaTransDetailList;
    private QaTrans qcQaTrans;
    public List<QaTransDetail> qcQaTransDetailList;

    private String pageFrom;
    private Integer ageCal = 0;

    //Delivery Method
    private List<DeliveryMethod> deliveryMethodList;
    private DeliveryMethod deliveryMethod;
    private Integer deliveryId;
    private Date deliveryDate;
    private String deliveryDescription;

    private ArrayList<PurchaseOrderRegister> poRegListCopy = null;
    private ArrayList<PurchaseOrderChildRegister> poChildRegisterListCopy = null;
    private ArrayList<PurchaseOrderDeclaration> poDeclarationListCopy = null;

    private boolean immediateCheck = false;
    private String messageCardno = "";
    private String cardNo1 = "";
    private String cardNo2 = "";
    private String cardNo3 = "";
    private String cardNo4 = "";

    private List<ProductWorkflow> productWorkflowList;
    private ProductWorkflow productWorkflow;

    private boolean enableTraceNo = false;
    private boolean enableReferenceNo = false;

    private Integer bankId;

    private String payerInitial;
    private String payerInitialType;
    private List<SelectItem> payerInitialList;
    private String payerName;
    private String payerSurname;
    private String payerGender;
    private Integer payerProvinceId;
    private Integer payerDistrictId;
    private Integer payerSubDistrictId;
    private List payerDistrictList;
    private List payerSubDistrictList;
    private String payerPostalCode;

    private List<Bank> bankList;

    private String pid1;
    private String pid2;
    private String pid3;
    private String pid4;
    private String pid5;
    private String msgIdnoPayer;

    private Integer remainAddress1 = 0;
    private Integer remainAddress2 = 0;
    private String msgAddress = "";

    private Double percentage1 = 0.00;
    private Double percentage2 = 0.00;

    private List payerOccupationList;
    private Integer payerOccupationCategoryId;
    private Integer payerOccupationId;

    private List<PaymentMethod> allPaymentMethodList;
    private List<PaymentMethod> productPaymentMethodList;
    private List<PaymentMethodStringCastor> productPaymentMethodCastorList;
    private Map<String, String> productPaymentMode;

    private QaTrans qaTrans4Save = null;

    private PaymentMethod paymentMethod;
    private List<CreditCardUsageValue> usageList = new ArrayList<CreditCardUsageValue>();
    private String cardNo = "";

    private List<ListDetail> listOfMaritalStatus = new ArrayList<ListDetail>();
    private SelectItem[] maritalStatusList;
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
    private List<CoverageInfo> coverageInfoList = null;

    private String currentCopyFrom;
    private String homeCopyFrom;
    private String shippingCopyFrom;
    private String billingCopyFrom;

    private String spouse = "";
    private Integer productPlanIdForMain;
    private PurchaseOrder poCopyForMain;
    private List<PurchaseOrderDetail> podFamilyList;
    
    private boolean showSaveRemark = false;
    private String msgSaveRemark = "";
    private String productFlexField1 = "";
    private String motorBody = "";
    private String discountType = "P";
    private Double discountByPercent =0.00;
    private Double discountByMoney =0.00;
    private boolean disableDiscountByPercent = false;
    private boolean disableDiscountByMoney = true;
    private String discountMessageDup ="";
    private PurchaseOrder po2;
    private String channelCashType;
    private String channelCashCode;
    private String agentCashCode;
    public  HashMap<String,String> channelCashCodeMap = new LinkedHashMap<>();
    public  HashMap<String,String> agentCashCodeMap = new LinkedHashMap<>();


    /**
     * ************************* CONTROLLER - PROPERTY
     * *********************************
     */
    @ManagedProperty(value = "#{subdistrictDAO}")
    private SubdistrictDAO subdistrictDAO;
    @ManagedProperty(value = "#{districtDAO}")
    private DistrictDAO districtDAO;
    @ManagedProperty(value = "#{provinceDAO}")
    private ProvinceDAO provinceDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{registrationFormDAO}")
    private RegistrationFormDAO registrationFormDAO;
    @ManagedProperty(value = "#{childRegTypeDAO}")
    private ChildRegTypeDAO childRegTypeDAO;
    @ManagedProperty(value = "#{childRegFormDAO}")
    private ChildRegFormDAO childRegFormDAO;
    @ManagedProperty(value = "#{declarationFormDAO}")
    private DeclarationFormDAO declarationFormDAO;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value = "#{purchaseOrderDetailDAO}")
    private PurchaseOrderDetailDAO purchaseOrderDetailDAO;
    @ManagedProperty(value = "#{nosaleReasonDAO}")
    private NosaleReasonDAO nosaleReasonDAO;
    @ManagedProperty(value = "#{followupsaleReasonDAO}")
    private FollowupsaleReasonDAO followupsaleReasonDAO;
    @ManagedProperty(value = "#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
    @ManagedProperty(value = "#{productPlanDAO}")
    private ProductPlanDAO productPlanDAO;
    @ManagedProperty(value = "#{productPlanDetailDAO}")
    private ProductPlanDetailDAO productPlanDetailDAO;
    @ManagedProperty(value = "#{occupationCategoryDAO}")
    private OccupationCategoryDAO occupationCategoryDAO;
    @ManagedProperty(value = "#{occupationDAO}")
    private OccupationDAO occupationDAO;
    @ManagedProperty(value = "#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    @ManagedProperty(value = "#{approvalRuleDAO}")
    private ApprovalRuleDAO approvalRuleDAO;
    @ManagedProperty(value = "#{qaTransDAO}")
    private QaTransDAO qaTransDAO;
    @ManagedProperty(value = "#{qaTransDetailDAO}")
    private QaTransDetailDAO qaTransDetailDAO;
    @ManagedProperty(value = "#{paymentMethodDAO}")
    private PaymentMethodDAO paymentMethodDAO;
    @ManagedProperty(value = "#{deliveryMethodDAO}")
    private DeliveryMethodDAO deliveryMethodDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{cardIssuerDAO}")
    private CardIssuerDAO cardIssuerDAO;
    @ManagedProperty(value = "#{nextSequenceService}")
    private NextSequenceService nextSequenceService;
    @ManagedProperty(value = "#{productWorkflowDAO}")
    private ProductWorkflowDAO productWorkflowDAO;
    @ManagedProperty(value = "#{bankDAO}")
    private BankDAO bankDAO;
    @ManagedProperty(value = "#{listDetailDAO}")
    private ListDetailDAO listDetailDAO;
    @ManagedProperty(value = "#{purchaseOrderRegisterDAO}")
    private PurchaseOrderRegisterDAO purchaseOrderRegisterDAO;

    @ManagedProperty(value = "#{mxService}")
    private RegistrationTrxService registrationTxService;

    @PostConstruct
    public void initialize() {
        TimeCollector tc = new TimeCollector(true);
        try {
            this.setupIndependencyLookupProperty();
            this.setupControllerPropertyFromPouch();
            this.setupQaFormPouch();
            this.setupFormProperty();
            this.setupDependencyLookupProperty();
            this.setMaritalStatusList();
        } catch (Exception e) {
            this.message = "Incomplete to setup registration form. Please go back to the previous page and try again or contact the System administrator. [Cause: " + e.getMessage() + "]";
            RegistrationLogging.logError("Error has occured when initialized page : " + e.getMessage(), e);
        }
        RegistrationLogging.debugElapseTime(tc, "Duration of initial Registration form");

        if(!productFlexField1.equals("CMI") && !productFlexField1.equals("VMI")) {
            calculateLifeNonLife();
        }
    }

    /**
     * ******************* INIT - METHOD *****************************
     */
    public void renderDataFromBCIBUpdate(ActionEvent event){
        if(productFlexField1.equals("CMI") || productFlexField1.equals("VMI")) {
            if (purchaseOrder != null && purchaseOrder.getId() != null) {

                po2 = purchaseOrderDAO.findPurchaseOrder(purchaseOrder.getId());

                List<PurchaseOrderDetail> pod2 = (List<PurchaseOrderDetail>) po2.getPurchaseOrderDetailCollection();
                List<PurchaseOrderRegister> por2 = (List<PurchaseOrderRegister>) pod2.get(0).getPurchaseOrderRegisterCollection();

                for(FlexFieldInfoValue fx :regInfo.get(0).getFlexFields()){
                    if(fx.getNo()==44){
                        if((por2.get(0).getFx44() != null && !por2.get(0).getFx44().equals(""))) {
                            fx.setPoFlexField(por2.get(0).getFx44());
                        }
                    }else if(fx.getNo()==50){
                        if((por2.get(0).getFx50() != null && !por2.get(0).getFx50().equals(""))) {
                            fx.setPoFlexField(por2.get(0).getFx50());
                        }
                    }
                }

//                if(netPremium.doubleValue() != po2.getNetPremium().doubleValue()){
//                    discountByMoney = 0.00;
//                    discountByPercent = 0.00;
//                    discountMessageDup ="";
//                    totalPrice = 0.00;
//                }

                if(po2.getPrice() != 0.00) {
                    price = po2.getPrice();
                }
                if(po2.getAnnualNetPremium() != 0.00) {
                    annualNetPremium = po2.getAnnualNetPremium();
                }
                if(po2.getNetPremium() != 0.00) {
                    netPremium = po2.getNetPremium();
                    DecimalFormat df = new DecimalFormat(".##");
                    totalPrice = netPremium - discountByMoney;
                    totalPrice = Double.parseDouble(df.format(totalPrice));
                }

            }
        }
    }

    private void setupQaFormPouch() {
        if (purchaseOrder != null) {
            if (product.getRegistrationForm().getQaForm() != null || this.purchaseOrder.getQaTransApproval() != null) {
                this.qaTrans4Save = new QaTrans(); //create QA Trans link
                if (this.purchaseOrder.getQaTransApproval() != null) {
                    try {
                        DataTransferUtil.transferQaTransDataWoCl(qaTrans4Save, this.purchaseOrder.getQaTransApproval());
                    } catch (Exception e) {
                    }
                }
                this.qaTrans4Save.setQaTransDetailCollection(new ArrayList<QaTransDetail>(0));
                RegistrationPouch qaPouch = FrontDispatcher.retreiveRegistrationPouch4Child();
                if (qaPouch != null) {
                    qaPouch.putPurchaseOrderForChild(this.purchaseOrder, this.qaTrans4Save); // put purchaseOrder to child
                }
                RegistrationLogging.logInfo("Setup pouch for qa form=" + qaPouch + ", qaTrans4Save=" + this.qaTrans4Save);
            } else {
                FrontDispatcher.pickupRegistrationPouch4Child(); // remove pouch from child by pickup it
            }
        }
    }

    private RegistrationPouch getPouchFromDispatcher() {
        RegistrationPouch pouch = FrontDispatcher.pickupRegistrationPouch();
        if (pouch == null) { // old fashion
            Integer purchaseOrderIdParam = null;
            try {
                purchaseOrderIdParam = Integer.valueOf(JSFUtil.getRequestParameterMap("poId"));
            } catch (Exception e) {
            }
            String mainPage = JSFUtil.getRequestParameterMap("mainPage");
            String page = JSFUtil.getRequestParameterMap("mainPage");
            if ((mainPage != null && "saleapproval".equals(mainPage)) || (page != null && "saleapproval".equals(page))) {
                pouch = new RegistrationPouch(); // create pouch to carry data for purchase order            
                pouch.registeredPouchType(RegistrationPouch.SENDER_TYPE.SALEAPPROVAL, RegistrationPouch.RECEIVER_MODE.EDIT);
                pouch.putEditModeParameter(purchaseOrderIdParam);
            }
        }
        return pouch;
    }

    private void setupControllerPropertyFromPouch() throws Exception {
        RegistrationPouch pouch = this.getPouchFromDispatcher();
        RegistrationLogging.logInfo("Show Registration by pouch=" + pouch);

        if (pouch != null) {
            switch (pouch.getSenderType()) {
                case SALEAPPROVAL:
                    this.fromSaleApprovalPage = true;
                    this.showPrevious = false;
                    this.showNext = false;
                    break;
                case SALEAPPROACHING:
                    JSFUtil.getRequest().removeAttribute("page");
                    break;
            }

            switch (pouch.getReceiverMode()) {
                case NEW:
                    this.ageCal = pouch.getAge();
                    this.gender = pouch.getGender();
                    this.paymentModeId = pouch.getPaymentModeId();
                    this.purchaseOrder = pouch.getNewPurchaseOrderObject();
                    paymentMethod = new PaymentMethod();
                    break;
                case EDIT:
                    this.poId = pouch.getEditPurchaseOrderId();
                    this.purchaseOrder = this.getPurchaseOrderDAO().findPurchaseOrder(poId);
                    if (this.purchaseOrder != null) {
                        JSFUtil.getUserSession().setAssignmentDetail(this.purchaseOrder.getAssignmentDetail());

                        //RESET REFERENCE YES SALE AND GET NEW YES REF
                        if (purchaseOrder.getSaleResult() == 'Y') {
                            JSFUtil.getUserSession().setRefYes(purchaseOrder.getRefNo());
                        }
                    }
                    if (purchaseOrder.getPaymentMethod() != null && !purchaseOrder.getPaymentMethod().isEmpty()) {
                        paymentMethod = this.getPaymentMethodDAO().findPaymentMethod(new Integer(purchaseOrder.getPaymentMethod()));
                    }
                    break;
                case COPY:
                    int copyFromPoId = pouch.getCopyFromPurchaseOrderId();
                    copyPo(copyFromPoId);
                    paymentMethod = new PaymentMethod();
                    break;
            }

            if (this.purchaseOrder == null) {
                throw new Exception("Cannot initial page. [Cause : Not found purchase order. " + RegistrationLogging.getObjectValueToString("Registration Pouch", pouch) + "]");
            }

            if (pouch.getReceiverMode() != RegistrationPouch.RECEIVER_MODE.COPY) {
                if (this.purchaseOrder.getRefNo() != null && !purchaseOrder.getRefNo().isEmpty()) {
                    JSFUtil.getUserSession().setRefNo(purchaseOrder.getRefNo());
                }
                this.toGetProductInfo();

                if (product != null && product.getBeneficiaryNo() != null && product.getBeneficiaryNo() != 0) {
                    this.setPercentValue();
                }

                if (JSFUtil.getUserSession().getCustomerDetail() == null) {
                    CustomerInfoValue c = customerHandlingDAO.findCustomerHandling(purchaseOrder.getCustomer().getId());
                    JSFUtil.getUserSession().setCustomerDetail(c);
                }
                if (purchaseOrder.getSaleResult() == 'Y') {
                    this.editPayment = SecurityUtil.isPermitted("admin:salesapproval:paymentapproval");
                    this.immediateCheck = true;
                } else {
                    this.editPayment = true;
                    this.immediateCheck = false;
                }

                this.toRegistrationFormPoDetail();
                //checkBtn();
            }

        } else {
//          throw new Exception("Cannot initial page. [Cause : Not found registration pouch. ");
        }
    }

    private void setupFormProperty() {
        this.defaultTimeZone = TimeZone.getDefault();
        String roleList = JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase();
        this.enableTraceNo = roleList.contains("QC") || roleList.contains("PAYMENT") || roleList.contains("UW");

        // Check Save for Owner Agent of PO, Campaign and Marketing Active
        if (purchaseOrder != null) {
            Assignment chkActive = purchaseOrder.getAssignmentDetail().getAssignmentId();
            this.disableSaveButton = !(this.purchaseOrder.getCreateByUser().getId().intValue() == JSFUtil.getUserSession().getUsers().getId().intValue()
                    && chkActive.getCampaign().getStatus() && chkActive.getCampaign().getEnable()
                    && chkActive.getMarketing().getStatus() && chkActive.getMarketing().getEnable());

            if (purchaseOrder.getId() != null && purchaseOrder.getId() != 0 && this.purchaseOrder.getSaleResult() == 'Y') {

                this.productWorkflowList = productWorkflowDAO.findProductWorkflowByProductId(product.getId());
                if (productWorkflowList != null && !productWorkflowList.isEmpty()) {
                    productWorkflow = this.getCurrentProductWorkflow();
                }
                String ldtr = FormUtil.emptyWhenNull(this.purchaseOrder.getLatestDelegateToRole()).toUpperCase();
                if (roleList.contains("SUPERVISOR")) {
                    this.disableSaveButton = !ldtr.equals("SUPERVISOR");
                } else if (roleList.contains("AGENT")) {
                    if (ldtr.equals("AGENT")) {
                        this.disableSaveButton = (this.purchaseOrder.getCreateByUser().getId().intValue() != JSFUtil.getUserSession().getUsers().getId().intValue());
                    }
                }

                if (JSFUtil.getApplication().isExternalRefNo()) {
                    this.enableReferenceNo = roleList.contains("QC") || roleList.contains("PAYMENT") || roleList.contains("UW");
                }
            }
        }
    }

    private void setupIndependencyLookupProperty() {
        this.allPaymentMethodList = paymentMethodDAO.findPaymentMethodEntities();
        this.bankList = bankDAO.findBankByIds("");
    }

    private void setupDependencyLookupProperty() {
        if (this.product != null && this.product.getCardBank() != null) {
            this.bankList = bankDAO.findBankByIds(this.product.getCardBank());
        }
//      this.cardIssuerList = cardIssuerDAO.findCardIssuerName(product.getCardBank());
    }

    private void copyPo(Integer poIdCopy) {
        poId = null;
        if (poIdCopy != null) {
            PurchaseOrder poForCopy = this.getPurchaseOrderDAO().findPurchaseOrder(poIdCopy);
            PurchaseOrderDetail podForCopy = ((List<PurchaseOrderDetail>) poForCopy.getPurchaseOrderDetailCollection()).get(0);

            this.purchaseOrder = new PurchaseOrder();
            if (podForCopy.getProduct().getFamilyProduct()) {
                //purchaseOrder.setParentPurchaseOrder(poForCopy);
                poCopyForMain = purchaseOrderDAO.findPurchaseOrder(poIdCopy);
                purchaseOrder.setMainPoId(poIdCopy);
            }
            ArrayList<PurchaseOrderDetail> podList = new ArrayList<PurchaseOrderDetail>(1);
            PurchaseOrderDetail pod = new PurchaseOrderDetail();
            poRegListCopy = new ArrayList<PurchaseOrderRegister>(6);
            poDeclarationListCopy = new ArrayList<PurchaseOrderDeclaration>(podForCopy.getPurchaseOrderRegisterCollection().size());
            pod.setProductPlan(podForCopy.getProductPlan());
            pod.setProductPlanDetail(podForCopy.getProductPlanDetail());
            pod.setQuantity(1);
            pod.setUnitPrice(podForCopy.getUnitPrice() != null ? podForCopy.getUnitPrice() : null);
            pod.setPurchaseOrder(purchaseOrder);
            if (JSFUtil.getUserSession().getAssignmentDetail() != null && JSFUtil.getUserSession().getAssignmentDetail().getId() != null) {
                if (JSFUtil.getUserSession().getAssignmentDetail().getId().intValue() != poForCopy.getAssignmentDetail().getId().intValue()) {
                    pod.setAssignmentDetail(JSFUtil.getUserSession().getAssignmentDetail());
                } else if (podForCopy.getAssignmentDetail() != null) {
                    pod.setAssignmentDetail(podForCopy.getAssignmentDetail());
                }
            } else if (podForCopy.getAssignmentDetail() != null) {
                pod.setAssignmentDetail(podForCopy.getAssignmentDetail());
            }
            pod.setProduct(podForCopy.getProduct());
            pod.setAmount(podForCopy.getAmount());
            pod.setCreateDate(new Date());

            for (PurchaseOrderRegister reg : podForCopy.getPurchaseOrderRegisterCollection()) {
                PurchaseOrderRegister poReg = new PurchaseOrderRegister();
                
                //poReg.setAge(reg.getAge());
                poReg.setBillingAddressLine1(reg.getBillingAddressLine1());
                poReg.setBillingAddressLine2(reg.getBillingAddressLine2());
                poReg.setBillingAddressLine3(reg.getBillingAddressLine3());
                poReg.setBillingAddressLine4(reg.getBillingAddressLine4());
                poReg.setBillingAddressLine5(reg.getBillingAddressLine5());
                poReg.setBillingAddressLine6(reg.getBillingAddressLine6());
                poReg.setBillingAddressLine7(reg.getBillingAddressLine7());
                poReg.setBillingAddressLine8(reg.getBillingAddressLine8());
                poReg.setBillingDistrict(reg.getBillingDistrict());
                poReg.setBillingFullname(reg.getBillingFullname());
                poReg.setBillingPostalCode(reg.getBillingPostalCode());
                poReg.setBillingSubDistrict(reg.getBillingSubDistrict());
                poReg.setBillingTelephone1(reg.getBillingTelephone1());
                poReg.setBillingTelephone2(reg.getBillingTelephone2());
                poReg.setBusinessType(reg.getBusinessType());
                poReg.setCurrentAddressLine1(reg.getCurrentAddressLine1());
                poReg.setCurrentAddressLine2(reg.getCurrentAddressLine2());
                poReg.setCurrentAddressLine3(reg.getCurrentAddressLine3());
                poReg.setCurrentAddressLine4(reg.getCurrentAddressLine4());
                poReg.setCurrentAddressLine5(reg.getCurrentAddressLine5());
                poReg.setCurrentAddressLine6(reg.getCurrentAddressLine6());
                poReg.setCurrentAddressLine7(reg.getCurrentAddressLine7());
                poReg.setCurrentAddressLine8(reg.getCurrentAddressLine8());
                poReg.setCurrentDistrict(reg.getCurrentDistrict());
                poReg.setCurrentFullname(reg.getCurrentFullname());
                poReg.setCurrentPostalCode(reg.getCurrentPostalCode());
                poReg.setCurrentSubDistrict(reg.getCurrentSubDistrict());
                poReg.setCurrentTelephone1(reg.getCurrentTelephone1());
                poReg.setCurrentTelephone2(reg.getCurrentTelephone2());
                //poReg.setDob(reg.getDob());
                poReg.setEmail(reg.getEmail());
                //poReg.setIdCardIssueDistrict(reg.getIdCardIssueDistrict());
                //poReg.setIdCardIssueProvince(reg.getIdCardIssueProvince());
                /*
                poReg.setFx1(reg.getFx1());
                poReg.setFx2(reg.getFx2());
                poReg.setFx3(reg.getFx3());
                poReg.setFx4(reg.getFx4());
                poReg.setFx5(reg.getFx5());
                poReg.setFx6(reg.getFx6());
                poReg.setFx7(reg.getFx7());
                poReg.setFx8(reg.getFx8());
                poReg.setFx9(reg.getFx9());
                poReg.setFx10(reg.getFx10());
                poReg.setFx11(reg.getFx11());
                poReg.setFx12(reg.getFx12());
                poReg.setFx13(reg.getFx13());
                poReg.setFx14(reg.getFx14());
                poReg.setFx15(reg.getFx15());
                poReg.setFx16(reg.getFx16());
                poReg.setFx17(reg.getFx17());
                poReg.setFx18(reg.getFx18());
                poReg.setFx19(reg.getFx19());
                poReg.setFx20(reg.getFx20());
                poReg.setFx21(reg.getFx21());
                poReg.setFx22(reg.getFx22());
                poReg.setFx23(reg.getFx23());
                poReg.setFx24(reg.getFx24());
                poReg.setFx25(reg.getFx25());
                poReg.setFx26(reg.getFx26());
                poReg.setFx27(reg.getFx27());
                poReg.setFx28(reg.getFx28());
                poReg.setFx29(reg.getFx29());
                poReg.setFx30(reg.getFx30());
                poReg.setFx31(reg.getFx31());
                poReg.setFx32(reg.getFx32());
                poReg.setFx33(reg.getFx33());
                poReg.setFx34(reg.getFx34());
                poReg.setFx35(reg.getFx35());
                poReg.setFx36(reg.getFx36());
                poReg.setFx37(reg.getFx37());
                poReg.setFx38(reg.getFx38());
                poReg.setFx39(reg.getFx39());
                poReg.setFx40(reg.getFx40());
                poReg.setFx41(reg.getFx41());
                poReg.setFx42(reg.getFx42());
                poReg.setFx43(reg.getFx43());
                poReg.setFx44(reg.getFx44());
                poReg.setFx45(reg.getFx45());
                poReg.setFx46(reg.getFx46());
                poReg.setFx47(reg.getFx47());
                poReg.setFx48(reg.getFx48());
                poReg.setFx49(reg.getFx49());
                poReg.setFx50(reg.getFx50());
                */
                //poReg.setGender(reg.getGender());
                //poReg.setWeight(reg.getWeight());
                //poReg.setHeight(reg.getHeight());
                poReg.setHomeAddressLine1(reg.getHomeAddressLine1());
                poReg.setHomeAddressLine2(reg.getHomeAddressLine2());
                poReg.setHomeAddressLine3(reg.getHomeAddressLine3());
                poReg.setHomeAddressLine4(reg.getHomeAddressLine4());
                poReg.setHomeAddressLine5(reg.getHomeAddressLine5());
                poReg.setHomeAddressLine6(reg.getHomeAddressLine6());
                poReg.setHomeAddressLine7(reg.getHomeAddressLine7());
                poReg.setHomeAddressLine8(reg.getHomeAddressLine8());
                poReg.setHomeDistrict(reg.getHomeDistrict());
                poReg.setHomeFullname(reg.getHomeFullname());
                poReg.setHomePhone(reg.getHomePhone());
                poReg.setHomePostalCode(reg.getHomePostalCode());
                poReg.setHomeSubDistrict(reg.getHomeSubDistrict());
                poReg.setHomeTelephone1(reg.getHomeTelephone1());
                poReg.setHomeTelephone2(reg.getHomeTelephone2());
                //poReg.setIdcardExpiryDate(reg.getIdcardExpiryDate());
                //poReg.setIdcardTypeId(reg.getIdcardTypeId());
                //poReg.setIdno(reg.getIdno());
                //poReg.setIncome(reg.getIncome());
                //poReg.setInitial(reg.getInitial());
                //poReg.setInitialLabel(reg.getInitialLabel());
                //poReg.setJobDescription(reg.getJobDescription());
                //poReg.setMaritalStatus(reg.getMaritalStatus());
                poReg.setMobilePhone(reg.getMobilePhone());
                //poReg.setName(reg.getName());
                //poReg.setSurname(reg.getSurname());
                poReg.setNationality(reg.getNationality());
                poReg.setNationalityLabel(reg.getNationalityLabel());
                poReg.setNo(reg.getNo());
                //poReg.setOccupation(reg.getOccupation());
                //poReg.setOccupationLabel(reg.getOccupationLabel());
                //poReg.setOccupationText(reg.getOccupationText());
                poReg.setOfficePhone(reg.getOfficePhone());
                //poReg.setPosition(reg.getPosition());
                poReg.setPurchaseOrderDetail(pod);
                poReg.setRace(reg.getRace());
                poReg.setRaceLabel(reg.getRaceLabel());
                poReg.setReligion(reg.getReligion());
                poReg.setReligionLabel(reg.getReligionLabel());
                poReg.setShippingAddressLine1(reg.getShippingAddressLine1());
                poReg.setShippingAddressLine2(reg.getShippingAddressLine2());
                poReg.setShippingAddressLine3(reg.getShippingAddressLine3());
                poReg.setShippingAddressLine4(reg.getShippingAddressLine4());
                poReg.setShippingAddressLine5(reg.getShippingAddressLine5());
                poReg.setShippingAddressLine6(reg.getShippingAddressLine6());
                poReg.setShippingAddressLine7(reg.getShippingAddressLine7());
                poReg.setShippingAddressLine8(reg.getShippingAddressLine8());
                poReg.setShippingDistrict(reg.getShippingDistrict());
                poReg.setShippingFullname(reg.getShippingFullname());
                poReg.setShippingPostalCode(reg.getShippingPostalCode());
                poReg.setShippingSubDistrict(reg.getShippingSubDistrict());
                poReg.setShippingTelephone1(reg.getShippingTelephone1());
                poReg.setShippingTelephone2(reg.getShippingTelephone2());

                poRegListCopy.add(poReg);
            }
            for (PurchaseOrderDeclaration declaration : podForCopy.getPurchaseOrderDeclarationCollection()) {
                PurchaseOrderDeclaration poDeclaration = new PurchaseOrderDeclaration();
                poDeclaration.setPurchaseOrderDetail(pod);
                poDeclaration.setDeclarationForm(declaration.getDeclarationForm());
                poDeclaration.setEnable(declaration.getEnable());
                poDeclaration.setFx1(declaration.getFx1());
                poDeclaration.setFx2(declaration.getFx2());
                poDeclaration.setFx3(declaration.getFx3());
                poDeclaration.setFx4(declaration.getFx4());
                poDeclaration.setFx5(declaration.getFx5());
                poDeclaration.setFx6(declaration.getFx6());
                poDeclaration.setFx7(declaration.getFx7());
                poDeclaration.setFx8(declaration.getFx8());
                poDeclaration.setFx9(declaration.getFx9());
                poDeclaration.setFx10(declaration.getFx10());
                poDeclaration.setFx11(declaration.getFx11());
                poDeclaration.setFx12(declaration.getFx12());
                poDeclaration.setFx13(declaration.getFx13());
                poDeclaration.setFx14(declaration.getFx14());
                poDeclaration.setFx15(declaration.getFx15());
                poDeclaration.setFx16(declaration.getFx16());
                poDeclaration.setFx17(declaration.getFx17());
                poDeclaration.setFx18(declaration.getFx18());
                poDeclaration.setFx19(declaration.getFx19());
                poDeclaration.setFx20(declaration.getFx20());
                poDeclarationListCopy.add(poDeclaration);
            }
//          pod.setPurchaseOrderRegisterCollection(poRegListCopy);
            podList.add(pod);

            purchaseOrder.setRefNo(null);
            purchaseOrder.setCustomer(poForCopy.getCustomer());
            if (JSFUtil.getUserSession().getAssignmentDetail() != null && JSFUtil.getUserSession().getAssignmentDetail().getId() != null) {
                if (JSFUtil.getUserSession().getAssignmentDetail().getId().intValue() != poForCopy.getAssignmentDetail().getId().intValue()) {
                    purchaseOrder.setAssignmentDetail(JSFUtil.getUserSession().getAssignmentDetail());
                } else if (podForCopy.getAssignmentDetail() != null) {
                    purchaseOrder.setAssignmentDetail(podForCopy.getAssignmentDetail());
                }
            } else if (podForCopy.getAssignmentDetail() != null) {
                purchaseOrder.setAssignmentDetail(podForCopy.getAssignmentDetail());
            }
            //purchaseOrder.setAssignmentDetail(poForCopy.getAssignmentDetail());
            purchaseOrder.setBillingFullname(poForCopy.getBillingFullname());
            purchaseOrder.setBillingAddressLine1(poForCopy.getBillingAddressLine1());
            purchaseOrder.setBillingAddressLine2(poForCopy.getBillingAddressLine2());
            if (poForCopy.getBillingDistrict() != null) {
                purchaseOrder.setBillingDistrict(poForCopy.getBillingDistrict());
            } else {
                purchaseOrder.setBillingDistrict(null);
            }
            purchaseOrder.setBillingPostalCode(poForCopy.getBillingPostalCode());

            purchaseOrder.setShippingFullname(poForCopy.getShippingFullname());
            purchaseOrder.setShippingAddressLine1(poForCopy.getShippingAddressLine1());
            purchaseOrder.setShippingAddressLine2(poForCopy.getShippingAddressLine2());
            if (poForCopy.getShippingDistrict() != null) {
                purchaseOrder.setShippingDistrict(poForCopy.getShippingDistrict());
            } else {
                purchaseOrder.setShippingDistrict(null);
            }
            purchaseOrder.setShippingPostalCode(poForCopy.getShippingPostalCode());
            purchaseOrder.setPurchaseDate(null);
            purchaseOrder.setPurchaseOrderDetailCollection(podList);
            if(!productFlexField1.equals("CMI") && !productFlexField1.equals("VMI")) {
                purchaseOrder.setSaleResult("N".charAt(0));
            }else {
                purchaseOrder.setSaleResult("F".charAt(0));
            }
            purchaseOrder.setCreateBy(JSFUtil.getUserSession().getUserName());
            purchaseOrder.setCreateByUser(JSFUtil.getUserSession().getUsers());
            purchaseOrder.setCreateDate(new Date());
            purchaseOrder.setApprovalStatus(JSFUtil.getBundleValue("approvalWaitingValue"));
            purchaseOrder.setPaymentStatus(JSFUtil.getBundleValue("approvalWaitingValue"));
            purchaseOrder.setQcStatus(JSFUtil.getBundleValue("approvalWaitingValue"));

            purchaseOrder.setCardNo(poForCopy.getCardNo());
            purchaseOrder.setCardHolderName(poForCopy.getCardHolderName());
            purchaseOrder.setCardIssuer(poForCopy.getCardIssuer());
            if (poForCopy.getBank() != null) {
                purchaseOrder.setBank(new Bank(poForCopy.getBank().getId()));
            }
            purchaseOrder.setCardType(poForCopy.getCardType());
            purchaseOrder.setCardExpiryMonth(poForCopy.getCardExpiryMonth());
            purchaseOrder.setCardExpiryYear(poForCopy.getCardExpiryYear());
            purchaseOrder.setPaymentMethod(poForCopy.getPaymentMethod());
            purchaseOrder.setPaymentMethodName(poForCopy.getPaymentMethodName());
            purchaseOrder.setPaymentAccountNo(poForCopy.getPaymentAccountNo());
            purchaseOrder.setPaymentExpectedDate(poForCopy.getPaymentExpectedDate());
            purchaseOrder.setPaymentRemarks(poForCopy.getPaymentRemarks());

            purchaseOrder.setPrice(poForCopy.getPrice());
            purchaseOrder.setStampDuty(poForCopy.getStampDuty());
            purchaseOrder.setVat(poForCopy.getVat());
            purchaseOrder.setNetPremium(poForCopy.getNetPremium());
            purchaseOrder.setAnnualNetPremium(poForCopy.getAnnualNetPremium());
            purchaseOrder.setAnnualPrice(poForCopy.getAnnualPrice());
            purchaseOrder.setSumInsured(poForCopy.getSumInsured());
            purchaseOrder.setTotalAmount(poForCopy.getTotalAmount());

            purchaseOrder.setProductPlanDetailInfo(poForCopy.getProductPlanDetailInfo());

            if (poForCopy.getModelYear() != null) {
                purchaseOrder.setModelYear(poForCopy.getModelYear());
            }

            this.toGetProductInfo();
            this.toRegistrationFormPoDetail();
            clearPrice();
            clearProductPlan();

            if (JSFUtil.getUserSession().getCustomerDetail() == null) {
                CustomerInfoValue c = customerHandlingDAO.findCustomerHandling(purchaseOrder.getCustomer().getId());
                JSFUtil.getUserSession().setCustomerDetail(c);
            }
            //JSFUtil.getRequest().setAttribute("poAttribute", purchaseOrder);
            //checkBtn();
        }
    }

    private void toRegistrationFormPoDetail() {
        try {
            //purchaseOrderDetail = purchaseOrderDAO.findPurchaseOrderDetailByPurchaseOrderId(purchaseOrder.getId());

            registrationForm = this.getRegistrationFormDAO().findRegistrationFormByProductId(product.getId());
            childRegType = this.getChildRegTypeDAO().findChildRegTypeByProduct(product.getId());
            declarationForm = this.declarationFormDAO.findDeclarationFormByProductId(this.product.getId());
            productPlanList = productPlanDAO.findProductPlanEntitiesByProductPaymentMode(product.getId(), paymentModeId, gender);
            productFlexField1 = product.getFx1();

            if(productFlexField1.equals("CMI") || productFlexField1.equals("VMI")) {

                price = purchaseOrder.getPrice();
                annualNetPremium = purchaseOrder.getAnnualNetPremium();
                netPremium = purchaseOrder.getNetPremium();

                if(purchaseOrder!=null && purchaseOrder.getDiscountType() !=null && !purchaseOrder.getDiscountType().equals("")) {
                    discountType = purchaseOrder.getDiscountType();
                }

                if(purchaseOrder!=null && purchaseOrder.getDiscount() !=null && purchaseOrder.getDiscount() != 0.0) {
                    discountByMoney = purchaseOrder.getDiscount();
//                    if(discountByMoney != null && discountByMoney != 0.0) {
//                        if(discountType.equals("V"))
//                            calculateDiscountByMoney();
//                    }
                }

                if(purchaseOrder!=null && purchaseOrder.getDiscountPercent() !=null && purchaseOrder.getDiscountPercent() != 0.0) {
                    discountByPercent = purchaseOrder.getDiscountPercent();
//                    if(discountByPercent != null && discountByPercent != 0.0) {
//                        if(discountType.equals("P"))
//                            calculateDiscountByPercent();
//                    }
                }

                channelCashType = purchaseOrder.getChannelCashType();
                channelCashCode = purchaseOrder.getChannelCashCode();
                agentCashCode = purchaseOrder.getAgentCashCode();

                if(channelCashType !=null) {
                    if (channelCashType.equals("1")) {
                        channelCashCode = "OVERTHECOUNTER";
                        agentCashCodeMap.clear();
                        agentCashCodeMap.put("Tesco Lotus Counter Service", "TESCO");
                        agentCashCodeMap.put("Big C Supercenter", "BIGC");
                        agentCashCodeMap.put("Pay@Post by Thailand post", "PAYATPOST");
                        agentCashCodeMap.put("mPay Station by AIS", "MPAY");
                        agentCashCodeMap.put("True Money Shop", "TRUEMONEY");
                        agentCashCodeMap.put("CenPay by Central", "CENPAY");
                        agentCashCodeMap.put("Boonterm Kiosk Payment", "BOONTERM");
                    } else if (channelCashType.equals("2")) {
                        channelCashCodeMap.clear();
                        channelCashCodeMap.put("ATM Machine", "ATM");
                        channelCashCodeMap.put("Bank Counter", "BANKCOUNTER");
                        channelCashCodeMap.put("Internet Banking", "IBANKING");
                        channelCashCodeMap.put("Web Payment", "WEBPAY");
                        channelCashCodeMap.put("Over the counter", "OVERTHECOUNTER");
                        //channelCashCodeMap.put("Kiosk Machines", "KIOSK");
                        agentCashCodeMap.clear();
                        agentCashCodeMap.put("Siam Commercial Bank", "SCB");
                        agentCashCodeMap.put("Krung Thai Bank", "KTB");
                        agentCashCodeMap.put("TMB Bank Public Company Limited", "TMB");
                        agentCashCodeMap.put("United Overseas Bank", "UOB");
                        agentCashCodeMap.put("Bank of Ayutthaya", "BAY");
                        agentCashCodeMap.put("Bangkok Bank", "BBL");
                        agentCashCodeMap.put("Kasikorn Bank", "KBANK");
                        agentCashCodeMap.put("Thanachart Bank Public Company Ltd", "TBANK");
                    }
                }

                Object[] row = (Object[]) productPlanList.get(0);
                this.productPlanId = Integer.parseInt(row[0].toString());
                //calculate();

                // motor build json body
                Users u = new  Users();
                u = JSFUtil.getUserSession().getUsers();
                Customer c = new Customer();
                c = purchaseOrder.getCustomer();
                int countPurchase = purchaseOrderDAO.purchaseOrderCountByCustomerId(c.getId());
                String app ="EDIT";
                if(countPurchase == 0){
                    app = "NEW";
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", new Locale("en", "en"));

                try {

                    JsonObject body = new JsonObject();
                    body.addProperty("EXT_USERID", convertNullToString(u.getFx1()));
                    JsonObject data = new JsonObject();
                    data.addProperty("LEAD_ID", convertNullToString(c.getFlexfield1()));
                    data.addProperty("USER", "TERRABIT");
                    data.addProperty("SALECODE", convertNullToString(u.getFx1()));
                    data.addProperty("VEFFECTIVEDATE", ""); // check again
                    data.addProperty("VEXPIRYDATE", ""); // check again
                    data.addProperty("CEFFECTIVEDATE", ""); // check again
                    data.addProperty("CEXPIRYDATE", ""); // check again
                    data.addProperty("TITLENAME", convertNullToString(c.getInitial()));
                    data.addProperty("FIRSTNAME", convertNullToString(c.getName()));
                    data.addProperty("LASTNAME", convertNullToString(c.getSurname()));
                    data.addProperty("IDCARD", convertNullToString(c.getIdno()));
                    data.addProperty("BIRTHDATE", c.getDob() !=null && !c.getDob().equals("") ? dateFormat.format(c.getDob()):"");
                    data.addProperty("MOBILE_PHONE",convertNullToString(c.getMobilePhone()));
                    data.addProperty("HOME_PHONE", convertNullToString(c.getHomePhone()));
                    data.addProperty("ADDRESTEXT", convertNullToString(c.getFlexfield11()));
                    data.addProperty("HOUSENO", convertNullToString(c.getFlexfield12()));
                    data.addProperty("MOO", convertNullToString(c.getFlexfield13()));
                    data.addProperty("SOI", convertNullToString(c.getFlexfield14()));
                    data.addProperty("ROAD", convertNullToString(c.getFlexfield15()));
                    data.addProperty("SUBDISTRICT", convertNullToString(c.getFlexfield16()));
                    data.addProperty("DISTRICT", convertNullToString(c.getFlexfield17()));
                    data.addProperty("PROVINCE", convertNullToString(c.getFlexfield18()));
                    data.addProperty("REGION", convertNullToString(c.getFlexfield19()));
                    data.addProperty("POSTCODE", convertNullToString(c.getFlexfield20()));
                    data.addProperty("CLASS", "MO");  // check again
                    data.addProperty("SUBCLASS", product.getFx1().equals("CMI")? "C" : "V"); // check again
                    data.addProperty("PREV_POLICYNO", convertNullToString(c.getFlexfield37()));
                    data.addProperty("PREV_POLICYYEAR", ""); // check again
                    data.addProperty("BRAND", convertNullToString(c.getFlexfield21()));
                    data.addProperty("MODEL", convertNullToString(c.getFlexfield22()));
                    data.addProperty("SUBMODEL", convertNullToString(c.getFlexfield23()));
                    data.addProperty("MODELYEAR", convertNullToString(c.getFlexfield24()));
                    data.addProperty("REGISTERYEAR", convertNullToString(c.getFlexfield25()));
                    data.addProperty("COMPULSORYCODE", convertNullToString(c.getFlexfield34()));
                    data.addProperty("VOLUNTARYCODE", convertNullToString(c.getFlexfield33()));
                    data.addProperty("CC", convertNullToString(c.getFlexfield28()));
                    data.addProperty("SEAT", convertNullToString(c.getFlexfield29()));
                    data.addProperty("GVW", convertNullToString(c.getFlexfield30()));
                    data.addProperty("CCTVFLAG", ""); // check again
                    data.addProperty("EXTRA_ACC", ""); // check again
                    data.addProperty("EXTRA_ACCSI", ""); // check again
                    data.addProperty("CHASSISNO", convertNullToString(c.getFlexfield32()));
                    data.addProperty("ENGINENO", convertNullToString(c.getFlexfield31()));
                    data.addProperty("LICENSE", convertNullToString(c.getFlexfield26()));
                    data.addProperty("LICESEPVN", convertNullToString(c.getFlexfield27()));
                    data.addProperty("COMPANYCODE", convertNullToString(c.getFlexfield36()));
                    data.addProperty("STATUS", product.getFx1().equals("CMI")? "AI" : "QI" );
                    data.addProperty("GROSSPREM", convertNullToString(c.getFlexfield89()));
                    data.addProperty("NCB", convertNullToString(c.getFlexfield90()));
                    data.addProperty("OTHERDISCOUNT", "" ); //check again
                    data.addProperty("DEDUCT", ""); //check again
                    data.addProperty("PERIL", ""); //check again
                    data.addProperty("NETGROSSPREM", convertNullToString(c.getFlexfield92()));
                    data.addProperty("DUTY", convertNullToString(c.getFlexfield93()));
                    data.addProperty("TAX", convertNullToString(c.getFlexfield94()));
                    data.addProperty("TOTALPREM", convertNullToString(c.getFlexfield95()));
                    body.add("DATA_INPUT", data);

                    motorBody = body.toString();
                    System.out.println(motorBody);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (JSFUtil.getRequestParameterMap("ckSpouse") != null && !JSFUtil.getRequestParameterMap("ckSpouse").equals("")) {
                spouse = JSFUtil.getRequestParameterMap("ckSpouse");
            } else {
                spouse = "";
            }

            if (JSFUtil.getRequestParameterMap("productPlanId") != null && !JSFUtil.getRequestParameterMap("productPlanId").equals("")) {
                productPlanIdForMain = Integer.parseInt(JSFUtil.getRequestParameterMap("productPlanId"));
            }

            if (JSFUtil.getRequestParameterMap("mainPoId") != null && !JSFUtil.getRequestParameterMap("mainPoId").equals("")) {
                int mainPoId = Integer.parseInt(JSFUtil.getRequestParameterMap("mainPoId"));
                if (purchaseOrderDAO.findProductPlanFromMainPoId(mainPoId) != null) {
                    productPlanIdForMain = purchaseOrderDAO.findProductPlanFromMainPoId(mainPoId);
                }
                poCopyForMain = purchaseOrderDAO.findPurchaseOrder(mainPoId);
            }
            
            setupProductPlanList();

            Calendar c = Calendar.getInstance(Locale.US);
            c.set(ageCal != 0 ? c.get(Calendar.YEAR) - ageCal : 1970, 0, 1);  //year, month, day
            defaultDob = c.getTime();
            Calendar c2 = Calendar.getInstance(Locale.US);
            c2.set(c2.get(Calendar.YEAR) + 1, 0, 1);
            defaultCardExpiryDate = c2.getTime();

            nosaleReasonList = this.getNosaleReasonDAO().findNosaleReasonEntities();
            followupsaleReasonList = followupsaleReasonDAO.findFollowupsaleReasonEntities();

            this.setProviceList();
            this.initPaymentModeList(); // call new initPayment Mode
            this.initOccupationCategoryList();
            this.setRegistrationForm();
            this.setChildRegister();
            this.setDeclaration();
            this.initPayment();
        } catch (Exception e) {
            RegistrationLogging.logError(e.getMessage(), e);
            JSFUtil.redirect(REDIRECT_PAGE);
        } finally {

        }
    }

    public void typeDiscountListener(AjaxBehaviorEvent event){
        if(discountType.equals("P")){
            disableDiscountByPercent = false;
            disableDiscountByMoney = true;
            discountByMoney = 0.00;
            discountByPercent = 0.00;
            discountMessageDup ="";
            totalPrice = 0.00;
        }else {
            disableDiscountByMoney = false;
            disableDiscountByPercent = true;
            discountByMoney = 0.00;
            discountByPercent = 0.00;
            discountMessageDup ="";
            totalPrice = 0.00;
        }
    }

    public void calculateDiscountByPercent(){
        DecimalFormat df = new DecimalFormat(".##");
        discountByMoney = 0.00;
        totalPrice = 0.00;
        //System.out.println(discountByPercent);
        if(discountByPercent>20){
            discountMessageDup ="ส่วนลดมูลค่าไม่เกิน 20% ของเบี้ยประกัน";
            discountByMoney = netPremium * discountByPercent /100;
            totalPrice = netPremium - discountByMoney;
            totalPrice = Double.parseDouble(df.format(totalPrice));
        }else {
            discountMessageDup ="";
            discountByMoney = netPremium * discountByPercent /100;
            totalPrice = netPremium - discountByMoney;
            totalPrice = Double.parseDouble(df.format(totalPrice));
        }

        discountByMoney = Double.parseDouble(df.format(discountByMoney));

    }

    public void calculateDiscountByMoney(){
        DecimalFormat df = new DecimalFormat(".##");
        discountByPercent =0.00;
        totalPrice = 0.00;
        //System.out.println("calculateDiscountByMoney");
        discountByPercent = discountByMoney * 100 / netPremium;
        if(discountByPercent>20){
            discountMessageDup ="ส่วนลดมูลค่าไม่เกิน 20% ของเบี้ยประกัน";
            totalPrice = netPremium - discountByMoney;
            totalPrice = Double.parseDouble(df.format(totalPrice));
        }else {
            discountMessageDup ="";
            totalPrice = netPremium - discountByMoney;
            totalPrice = Double.parseDouble(df.format(totalPrice));
        }

        discountByPercent = Double.parseDouble(df.format(discountByPercent));
    }

    public String convertNullToString(String data){
        if(data == null) {
            return "";
        }else {
            return data;
        }
    }

    public void changeChannelCashType(){
        channelCashCode = "";
        agentCashCode ="";
        //System.out.println(channelCashType);
        if(channelCashType.equals("1")){
            channelCashCode = "OVERTHECOUNTER";
            agentCashCodeMap.clear();
            agentCashCodeMap.put("Tesco Lotus Counter Service","TESCO");
            agentCashCodeMap.put("Big C Supercenter","BIGC");
            agentCashCodeMap.put("Pay@Post by Thailand post","PAYATPOST");
            agentCashCodeMap.put("mPay Station by AIS","MPAY");
            agentCashCodeMap.put("True Money Shop","TRUEMONEY");
            agentCashCodeMap.put("CenPay by Central","CENPAY");
            agentCashCodeMap.put("Boonterm Kiosk Payment","BOONTERM");
        } else if(channelCashType.equals("2")){
            channelCashCodeMap.clear();
            channelCashCodeMap.put("ATM Machine","ATM");
            channelCashCodeMap.put("Bank Counter","BANKCOUNTER");
            channelCashCodeMap.put("Internet Banking","IBANKING");
            channelCashCodeMap.put("Web Payment","WEBPAY");
            channelCashCodeMap.put("Over the counter","OVERTHECOUNTER");
            channelCashCodeMap.put("Kiosk Machines","KIOSK");
            agentCashCodeMap.clear();
            agentCashCodeMap.put("Siam Commercial Bank","SCB");
            agentCashCodeMap.put("Krung Thai Bank","KTB");
            agentCashCodeMap.put("TMB Bank Public Company Limited","TMB");
            agentCashCodeMap.put("United Overseas Bank","UOB");
            agentCashCodeMap.put("Bank of Ayutthaya","BAY");
            agentCashCodeMap.put("Bangkok Bank","BBL");
            agentCashCodeMap.put("Kasikorn Bank","KBANK");
            agentCashCodeMap.put("Thanachart Bank Public Company Ltd","TBANK");
        }

    }

    /**
     * ******************* ACTION HANDLING - METHOD
     * *****************************
     */
    public void searchZipCode(RegistrationInfoValue registration) {
        String selectedAddress = (String) JSFUtil.getRequestParameterMap("selectedAddress");

        if (registration.getPo() instanceof PurchaseOrderRegister && registration.getPo() != null && selectedAddress != null) {

            // 1. KEEP SEARCH POSTAL CODE BEFORE CLEAR
            String searchPostal = "";
            if (selectedAddress.equals("current")) {
                searchPostal = registration.getCurrentPostal();
            } else if (selectedAddress.equals("home")) {
                searchPostal = registration.getHomePostal();
            } else if (selectedAddress.equals("billing")) {
                searchPostal = registration.getBillingPostal();
            } else if (selectedAddress.equals("shipping")) {
                searchPostal = registration.getShippingPostal();
            }

            // 2. CLEAR OLD VALUE -> PROVINCE, DISTRICT, SUB DISTRICT, POSTAL CODE
            clearZipCode(registration);

            // 3. SEARCH POSTAL CODE 
            List<District> districts = districtDAO.findDistrictByPostalCode(searchPostal);
            Province province = null;
            if (districts != null && districts.size() > 0) {
                province = districts.get(0).getProvinceId();
            }

            // 4. SET NEW VALUE -> PROVINCE, DISTRICT, SUB DISTRICT, POSTAL CODE 
            if (selectedAddress.equals("current")) {
                registration.setCurrentPostal(searchPostal);
                if (districts != null && districts.size() > 1) {
                    // 4.1 IF POSTAL CODE HAVE MORE ONE DISTRICT                   
                    if (province != null) {
                        registration.setCurrentProvinceId(province.getId());
                    }
                    registration.setCurrentDistrictList(districtDAO.getListDistrictByPostalCode(searchPostal));
                    registration.setCurrentDistrictId(null);
                    registration.setCurrentSubDistrictList(null);
                    countPreviewCurrentAdd3(registration);
                    countPreviewCurrentAdd2(registration);
                } else if (districts != null && districts.size() == 1) {
                    // 4.2 IF POSTAL CODE HAVE ONLY ONE DISTRICT             
                    if (province != null) {
                        registration.setCurrentProvinceId(province.getId());
                    }
                    registration.setCurrentDistrictList(districtDAO.getListDistrictByPostalCode(searchPostal));
                    registration.setCurrentDistrictId(districts.get(0).getId());
                    List subdistricts = subdistrictDAO.findSubDistrictByPostalCode(searchPostal);
                    if (subdistricts != null) {
                        registration.setCurrentSubDistrictList(subdistricts);
                        if (subdistricts.size() == 1) {
                            Object[] obj = (Object[]) subdistricts.get(0);
                            Integer subdistrictId = Integer.parseInt(obj[0].toString());
                            registration.setCurrentSubDistrictId(subdistrictId);
                        }
                    }
                    countPreviewCurrentAdd3(registration);
                    countPreviewCurrentAdd2(registration);
                } else {
                    // 4.3 IF POSTAL CODE NOT EXISTING
                    registration.setMsgPostalCode("ไม่พบข้อมูลในระบบ กรุณาตรวจสอบ");
                }
            } else if (selectedAddress.equals("home")) {
                registration.setHomePostal(searchPostal);
                if (districts != null && districts.size() > 1) {
                    // 4.1 IF POSTAL CODE HAVE MORE ONE DISTRICT                   
                    if (province != null) {
                        registration.setHomeProvinceId(province.getId());
                        registration.setHomeDistrictList(province.getId(), searchPostal);
                    }
                    registration.setHomeDistrictId(null);
                    registration.setHomeSubDistrictList(null);
                    countPreviewHomeAdd3(registration);
                    countPreviewHomeAdd2(registration);
                } else if (districts != null && districts.size() == 1) {
                    // 4.2 IF POSTAL CODE HAVE ONLY ONE DISTRICT             
                    if (province != null) {
                        registration.setHomeProvinceId(province.getId());
                        registration.setHomeDistrictList(province.getId(), searchPostal);
                    }
                    registration.setHomeDistrictId(districts.get(0).getId());
                    List subdistricts = subdistrictDAO.findSubDistrictByPostalCode(searchPostal);
                    if (subdistricts != null) {
                        registration.setHomeSubDistrictList(subdistricts);
                        if (subdistricts.size() == 1) {
                            Object[] obj = (Object[]) subdistricts.get(0);
                            Integer subdistrictId = Integer.parseInt(obj[0].toString());
                            registration.setHomeSubDistrictId(subdistrictId);
                        }
                    }
                    countPreviewHomeAdd3(registration);
                    countPreviewHomeAdd2(registration);
                } else {
                    // 4.3 IF POSTAL CODE NOT EXISTING
                    registration.setMsgPostalCode("ไม่พบข้อมูลในระบบ กรุณาตรวจสอบ");
                }
            } else if (selectedAddress.equals("billing")) {
                registration.setBillingPostal(searchPostal);
                if (districts != null && districts.size() > 1) {
                    // 4.1 IF POSTAL CODE HAVE MORE ONE DISTRICT                   
                    if (province != null) {
                        registration.setBillingProvinceId(province.getId());
                        registration.setBillingDistrictList(province.getId(), searchPostal);
                    }
                    registration.setBillingDistrictId(null);
                    registration.setBillingSubDistrictList(null);
                    countPreviewBillingAdd3(registration);
                    countPreviewBillingAdd2(registration);
                } else if (districts != null && districts.size() == 1) {
                    // 4.2 IF POSTAL CODE HAVE ONLY ONE DISTRICT             
                    if (province != null) {
                        registration.setBillingProvinceId(province.getId());
                        registration.setBillingDistrictList(province.getId(), searchPostal);
                    }
                    registration.setBillingDistrictId(districts.get(0).getId());
                    List subdistricts = subdistrictDAO.findSubDistrictByPostalCode(searchPostal);
                    if (subdistricts != null) {
                        registration.setBillingSubDistrictList(subdistricts);
                        if (subdistricts.size() == 1) {
                            Object[] obj = (Object[]) subdistricts.get(0);
                            Integer subdistrictId = Integer.parseInt(obj[0].toString());
                            registration.setBillingSubDistrictId(subdistrictId);
                        }
                    }
                    countPreviewBillingAdd3(registration);
                    countPreviewBillingAdd2(registration);
                } else {
                    // 4.3 IF POSTAL CODE NOT EXISTING
                    registration.setMsgPostalCode("ไม่พบข้อมูลในระบบ กรุณาตรวจสอบ");
                }
            } else if (selectedAddress.equals("shipping")) {
                registration.setShippingPostal(searchPostal);
                if (districts != null && districts.size() > 1) {
                    // 4.1 IF POSTAL CODE HAVE MORE ONE DISTRICT                   
                    if (province != null) {
                        registration.setShippingProvinceId(province.getId());
                        registration.setShippingDistrictList(province.getId(), searchPostal);
                    }
                    registration.setShippingDistrictId(null);
                    registration.setShippingSubDistrictList(null);
                    countPreviewShippingAdd3(registration);
                    countPreviewShippingAdd2(registration);
                } else if (districts != null && districts.size() == 1) {
                    // 4.2 IF POSTAL CODE HAVE ONLY ONE DISTRICT             
                    if (province != null) {
                        registration.setShippingProvinceId(province.getId());
                        registration.setShippingDistrictList(province.getId(), searchPostal);
                    }
                    registration.setShippingDistrictId(districts.get(0).getId());
                    List subdistricts = subdistrictDAO.findSubDistrictByPostalCode(searchPostal);
                    if (subdistricts != null) {
                        registration.setShippingSubDistrictList(subdistricts);
                        if (subdistricts.size() == 1) {
                            Object[] obj = (Object[]) subdistricts.get(0);
                            Integer subdistrictId = Integer.parseInt(obj[0].toString());
                            registration.setShippingSubDistrictId(subdistrictId);
                        }
                    }
                    countPreviewShippingAdd3(registration);
                    countPreviewShippingAdd2(registration);
                } else {
                    // 4.3 IF POSTAL CODE NOT EXISTING
                    registration.setMsgPostalCode("ไม่พบข้อมูลในระบบ กรุณาตรวจสอบ");
                }
            }
        }

    }

    public void clearZipCode(RegistrationInfoValue registration) {
        String selectedAddress = (String) JSFUtil.getRequestParameterMap("selectedAddress");
        if (registration.getPo() instanceof PurchaseOrderRegister && registration.getPo() != null && selectedAddress != null) {
            if (selectedAddress.equals("current")) {
                registration.setCurrentProvinceId(null);
                registration.setCurrentDistrictList(null);
                registration.setCurrentDistrictId(null);
                registration.setCurrentSubDistrictList(null);
                registration.setCurrentSubDistrictId(null);
                registration.setCurrentPostal(null);
                registration.setMsgPostalCode(null);

                countPreviewCurrentAdd3(registration);
                countPreviewCurrentAdd2(registration);
            } else if (selectedAddress.equals("home")) {
                registration.setHomeProvinceId(null);
                registration.setHomeDistrictList(null, null);
                registration.setHomeDistrictId(null);
                registration.setHomeSubDistrictList(null);
                registration.setHomeSubDistrictId(null);
                registration.setHomePostal(null);
                registration.setMsgPostalCode(null);

                countPreviewHomeAdd3(registration);
                countPreviewHomeAdd2(registration);
            } else if (selectedAddress.equals("billing")) {
                registration.setBillingProvinceId(null);
                registration.setBillingDistrictList(null, null);
                registration.setBillingDistrictId(null);
                registration.setBillingSubDistrictList(null);
                registration.setBillingSubDistrictId(null);
                registration.setBillingPostal(null);
                registration.setMsgPostalCode(null);

                countPreviewBillingAdd3(registration);
                countPreviewBillingAdd2(registration);
            } else if (selectedAddress.equals("shipping")) {
                registration.setShippingProvinceId(null);
                registration.setShippingDistrictList(null, null);
                registration.setShippingDistrictId(null);
                registration.setShippingSubDistrictList(null);
                registration.setShippingSubDistrictId(null);
                registration.setShippingPostal(null);
                registration.setMsgPostalCode(null);

                countPreviewShippingAdd3(registration);
                countPreviewShippingAdd2(registration);
            }
        }
    }

    public void copyAddressToCurrent(RegistrationInfoValue registration) {
        if (registration.getPo() instanceof PurchaseOrderRegister && registration.getPo() != null) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();

            // 1. CLEAR OLD VALUE
            por.setCurrentFullname(null);
            por.setCurrentAddressLine1(null);
            por.setCurrentAddressLine2(null);
            por.setCurrentAddressLine3(null);
            por.setCurrentAddressLine4(null);
            por.setCurrentAddressLine5(null);
            por.setCurrentAddressLine6(null);
            por.setCurrentAddressLine7(null);
            por.setCurrentAddressLine8(null);
            registration.setCurrentProvinceId(null);
            registration.setCurrentDistrictId(null);
            registration.setCurrentSubDistrictId(null);
            registration.setCurrentPostal(null);

            // 2. GET VALUE FROM SELECTED ADDRESS
            String fullName = null, addressLine1 = null, addressLine2 = null, addressLine3 = null, addressLine4 = null, addressLine5 = null;
            String addressLine6 = null, addressLine7 = null, addressLine8 = null, postalCode = null;
            Integer provinceId = null, districtId = null, subDistrictId = null;
            if (currentCopyFrom.equals("homeAddress")) {
                fullName = por.getHomeFullname();
                addressLine1 = por.getHomeAddressLine1();
                addressLine2 = por.getHomeAddressLine2();
                addressLine3 = por.getHomeAddressLine3();
                addressLine4 = por.getHomeAddressLine4();
                addressLine5 = por.getHomeAddressLine5();
                addressLine6 = por.getHomeAddressLine6();
                addressLine7 = por.getHomeAddressLine7();
                addressLine8 = por.getHomeAddressLine8();

                if (registration.getHomeProvinceId() != null) {
                    provinceId = registration.getHomeProvinceId();
                }

                if (registration.getHomeDistrictId() != null) {
                    districtId = registration.getHomeDistrictId();
                }

                if (registration.getHomeSubDistrictId() != null) {
                    subDistrictId = registration.getHomeSubDistrictId();
                }

                postalCode = registration.getHomePostal();
            } else if (currentCopyFrom.equals("shippingAddress")) {
                fullName = por.getShippingFullname();
                addressLine1 = por.getShippingAddressLine1();
                addressLine2 = por.getShippingAddressLine2();
                addressLine3 = por.getShippingAddressLine3();
                addressLine4 = por.getShippingAddressLine4();
                addressLine5 = por.getShippingAddressLine5();
                addressLine6 = por.getShippingAddressLine6();
                addressLine7 = por.getShippingAddressLine7();
                addressLine8 = por.getShippingAddressLine8();

                if (registration.getShippingProvinceId() != null) {
                    provinceId = registration.getShippingProvinceId();
                }

                if (registration.getShippingDistrictId() != null) {
                    districtId = registration.getShippingDistrictId();
                }

                if (registration.getShippingSubDistrictId() != null) {
                    subDistrictId = registration.getShippingSubDistrictId();
                }

                postalCode = registration.getShippingPostal();
            } else if (currentCopyFrom.equals("billingAddress")) {
                fullName = por.getBillingFullname();
                addressLine1 = por.getBillingAddressLine1();
                addressLine2 = por.getBillingAddressLine2();
                addressLine3 = por.getBillingAddressLine3();
                addressLine4 = por.getBillingAddressLine4();
                addressLine5 = por.getBillingAddressLine5();
                addressLine6 = por.getBillingAddressLine6();
                addressLine7 = por.getBillingAddressLine7();
                addressLine8 = por.getBillingAddressLine8();

                if (registration.getBillingProvinceId() != null) {
                    provinceId = registration.getBillingProvinceId();
                }

                if (registration.getBillingDistrictId() != null) {
                    districtId = registration.getBillingDistrictId();
                }

                if (registration.getBillingSubDistrictId() != null) {
                    subDistrictId = registration.getBillingSubDistrictId();
                }

                postalCode = registration.getBillingPostal();
            }

            // 3. SET VALUE
            if (registration.getCurrentAddressFullName() != null) {
                por.setCurrentFullname(fullName);
            }
            if (registration.getCurrentAddress1() != null) {
                por.setCurrentAddressLine1(addressLine1);
            }
            if (registration.getCurrentAddress2() != null) {
                por.setCurrentAddressLine2(addressLine2);
            }
            if (registration.getCurrentAddress3() != null) {
                por.setCurrentAddressLine3(addressLine3);
            }
            if (registration.getCurrentAddress4() != null) {
                por.setCurrentAddressLine4(addressLine4);
            }
            if (registration.getCurrentAddress5() != null) {
                por.setCurrentAddressLine5(addressLine5);
            }
            if (registration.getCurrentAddress6() != null) {
                por.setCurrentAddressLine6(addressLine6);
            }
            if (registration.getCurrentAddress7() != null) {
                por.setCurrentAddressLine7(addressLine7);
            }
            if (registration.getCurrentAddress8() != null) {
                por.setCurrentAddressLine8(addressLine8);
            }
            if (registration.getCurrentAddressProvince() != null) {
                if (provinceId != null && provinceId != 0) {
                    registration.setCurrentProvinceId(provinceId);
                    registration.setCurrentDistrictList(districtDAO.findDistrictByProvinceId1(provinceId, postalCode));
                }
                if (districtId != null && districtId != 0) {
                    registration.setCurrentDistrictId(districtId);
                    registration.setCurrentSubDistrictList(subdistrictDAO.findSubDistrictByDistrictId1(districtId, postalCode));
                }
                registration.setCurrentSubDistrictId(subDistrictId);
                registration.setCurrentPostal(postalCode);
                registration.setMsgPostalCode("");
            }

            // 4. CREATE PREVIEW   
            countPreviewCurrentAdd1(registration);
            countPreviewCurrentAdd3(registration);
            countPreviewCurrentAdd2(registration);
        }
    }

    public void copyAddressToHome(RegistrationInfoValue registration) {
        if (registration.getPo() instanceof PurchaseOrderRegister && registration.getPo() != null) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();

            // 1. CLEAR OLD VALUE
            por.setHomeFullname(null);
            por.setHomeAddressLine1(null);
            por.setHomeAddressLine2(null);
            por.setHomeAddressLine3(null);
            por.setHomeAddressLine4(null);
            por.setHomeAddressLine5(null);
            por.setHomeAddressLine6(null);
            por.setHomeAddressLine7(null);
            por.setHomeAddressLine8(null);
            registration.setHomeProvinceId(null);
            registration.setHomeDistrictId(null);
            registration.setHomeSubDistrictId(null);
            registration.setHomePostal(null);

            // 2. GET VALUE FROM SELECTED ADDRESS
            String fullName = null, addressLine1 = null, addressLine2 = null, addressLine3 = null, addressLine4 = null, addressLine5 = null;
            String addressLine6 = null, addressLine7 = null, addressLine8 = null, postalCode = null;
            Integer provinceId = null, districtId = null, subDistrictId = null;
            if (homeCopyFrom.equals("currentAddress")) {
                fullName = por.getCurrentFullname();
                addressLine1 = por.getCurrentAddressLine1();
                addressLine2 = por.getCurrentAddressLine2();
                addressLine3 = por.getCurrentAddressLine3();
                addressLine4 = por.getCurrentAddressLine4();
                addressLine5 = por.getCurrentAddressLine5();
                addressLine6 = por.getCurrentAddressLine6();
                addressLine7 = por.getCurrentAddressLine7();
                addressLine8 = por.getCurrentAddressLine8();

                if (registration.getCurrentProvinceId() != null) {
                    provinceId = registration.getCurrentProvinceId();
                }

                if (registration.getCurrentDistrictId() != null) {
                    districtId = registration.getCurrentDistrictId();
                }

                if (registration.getCurrentSubDistrictId() != null) {
                    subDistrictId = registration.getCurrentSubDistrictId();
                }

                postalCode = registration.getCurrentPostal();
            } else if (homeCopyFrom.equals("shippingAddress")) {
                fullName = por.getShippingFullname();
                addressLine1 = por.getShippingAddressLine1();
                addressLine2 = por.getShippingAddressLine2();
                addressLine3 = por.getShippingAddressLine3();
                addressLine4 = por.getShippingAddressLine4();
                addressLine5 = por.getShippingAddressLine5();
                addressLine6 = por.getShippingAddressLine6();
                addressLine7 = por.getShippingAddressLine7();
                addressLine8 = por.getShippingAddressLine8();

                if (registration.getShippingProvinceId() != null) {
                    provinceId = registration.getShippingProvinceId();
                }

                if (registration.getShippingDistrictId() != null) {
                    districtId = registration.getShippingDistrictId();
                }

                if (registration.getShippingSubDistrictId() != null) {
                    subDistrictId = registration.getShippingSubDistrictId();
                }

                postalCode = registration.getShippingPostal();
            } else if (homeCopyFrom.equals("billingAddress")) {
                fullName = por.getBillingFullname();
                addressLine1 = por.getBillingAddressLine1();
                addressLine2 = por.getBillingAddressLine2();
                addressLine3 = por.getBillingAddressLine3();
                addressLine4 = por.getBillingAddressLine4();
                addressLine5 = por.getBillingAddressLine5();
                addressLine6 = por.getBillingAddressLine6();
                addressLine7 = por.getBillingAddressLine7();
                addressLine8 = por.getBillingAddressLine8();

                if (registration.getBillingProvinceId() != null) {
                    provinceId = registration.getBillingProvinceId();
                }

                if (registration.getBillingDistrictId() != null) {
                    districtId = registration.getBillingDistrictId();
                }

                if (registration.getBillingSubDistrictId() != null) {
                    subDistrictId = registration.getBillingSubDistrictId();
                }

                postalCode = registration.getBillingPostal();
            }

            // 3. SET VALUE
            if (registration.getHomeAddressFullName() != null) {
                por.setHomeFullname(fullName);
            }
            if (registration.getHomeAddress1() != null) {
                por.setHomeAddressLine1(addressLine1);
            }
            if (registration.getHomeAddress2() != null) {
                por.setHomeAddressLine2(addressLine2);
            }
            if (registration.getHomeAddress3() != null) {
                por.setHomeAddressLine3(addressLine3);
            }
            if (registration.getHomeAddress4() != null) {
                por.setHomeAddressLine4(addressLine4);
            }
            if (registration.getHomeAddress5() != null) {
                por.setHomeAddressLine5(addressLine5);
            }
            if (registration.getHomeAddress6() != null) {
                por.setHomeAddressLine6(addressLine6);
            }
            if (registration.getHomeAddress7() != null) {
                por.setHomeAddressLine7(addressLine7);
            }
            if (registration.getHomeAddress8() != null) {
                por.setHomeAddressLine8(addressLine8);
            }
            if (registration.getHomeAddressProvince() != null) {
                if (provinceId != null && provinceId != 0) {
                    registration.setHomeProvinceId(provinceId);
                    registration.setHomeDistrictList(provinceId, postalCode);
                }
                if (districtId != null && districtId != 0) {
                    registration.setHomeDistrictId(districtId);
                    registration.setHomeSubDistrictList(subdistrictDAO.findSubDistrictByDistrictId1(districtId, postalCode));
                }
                registration.setHomeSubDistrictId(subDistrictId);
                registration.setHomePostal(postalCode);
                registration.setMsgPostalCode("");
            }

            // 4. CREATE PREVIEW
            countPreviewHomeAdd1(registration);
            countPreviewHomeAdd3(registration);
            countPreviewHomeAdd2(registration);
        }

    }

    public void copyAddressToShipping(RegistrationInfoValue registration) {
        if (registration.getPo() instanceof PurchaseOrderRegister && registration.getPo() != null) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();

            // 1. CLEAR OLD VALUE
            por.setShippingFullname(null);
            por.setShippingAddressLine1(null);
            por.setShippingAddressLine2(null);
            por.setShippingAddressLine3(null);
            por.setShippingAddressLine4(null);
            por.setShippingAddressLine5(null);
            por.setShippingAddressLine6(null);
            por.setShippingAddressLine7(null);
            por.setShippingAddressLine8(null);
            registration.setShippingProvinceId(null);
            registration.setShippingDistrictId(null);
            registration.setShippingSubDistrictId(null);
            registration.setShippingPostal(null);

            // 2. GET VALUE FROM SELECTED ADDRESS
            String fullName = null, addressLine1 = null, addressLine2 = null, addressLine3 = null, addressLine4 = null, addressLine5 = null;
            String addressLine6 = null, addressLine7 = null, addressLine8 = null, postalCode = null;
            Integer provinceId = null, districtId = null, subDistrictId = null;
            if (shippingCopyFrom.equals("currentAddress")) {
                fullName = por.getCurrentFullname();
                addressLine1 = por.getCurrentAddressLine1();
                addressLine2 = por.getCurrentAddressLine2();
                addressLine3 = por.getCurrentAddressLine3();
                addressLine4 = por.getCurrentAddressLine4();
                addressLine5 = por.getCurrentAddressLine5();
                addressLine6 = por.getCurrentAddressLine6();
                addressLine7 = por.getCurrentAddressLine7();
                addressLine8 = por.getCurrentAddressLine8();

                if (registration.getCurrentProvinceId() != null) {
                    provinceId = registration.getCurrentProvinceId();
                }

                if (registration.getCurrentDistrictId() != null) {
                    districtId = registration.getCurrentDistrictId();
                }

                if (registration.getCurrentSubDistrictId() != null) {
                    subDistrictId = registration.getCurrentSubDistrictId();
                }

                postalCode = registration.getCurrentPostal();
            } else if (shippingCopyFrom.equals("homeAddress")) {
                fullName = por.getHomeFullname();
                addressLine1 = por.getHomeAddressLine1();
                addressLine2 = por.getHomeAddressLine2();
                addressLine3 = por.getHomeAddressLine3();
                addressLine4 = por.getHomeAddressLine4();
                addressLine5 = por.getHomeAddressLine5();
                addressLine6 = por.getHomeAddressLine6();
                addressLine7 = por.getHomeAddressLine7();
                addressLine8 = por.getHomeAddressLine8();

                if (registration.getHomeProvinceId() != null) {
                    provinceId = registration.getHomeProvinceId();
                }

                if (registration.getHomeDistrictId() != null) {
                    districtId = registration.getHomeDistrictId();
                }

                if (registration.getHomeSubDistrictId() != null) {
                    subDistrictId = registration.getHomeSubDistrictId();
                }

                postalCode = registration.getHomePostal();
            } else if (shippingCopyFrom.equals("billingAddress")) {
                fullName = por.getBillingFullname();
                addressLine1 = por.getBillingAddressLine1();
                addressLine2 = por.getBillingAddressLine2();
                addressLine3 = por.getBillingAddressLine3();
                addressLine4 = por.getBillingAddressLine4();
                addressLine5 = por.getBillingAddressLine5();
                addressLine6 = por.getBillingAddressLine6();
                addressLine7 = por.getBillingAddressLine7();
                addressLine8 = por.getBillingAddressLine8();

                if (registration.getBillingProvinceId() != null) {
                    provinceId = registration.getBillingProvinceId();
                }

                if (registration.getBillingDistrictId() != null) {
                    districtId = registration.getBillingDistrictId();
                }

                if (registration.getBillingSubDistrictId() != null) {
                    subDistrictId = registration.getBillingSubDistrictId();
                }

                postalCode = registration.getBillingPostal();
            }

            // 3. SET VALUE
            if (registration.getShippingAddressFullName() != null) {
                por.setShippingFullname(fullName);
            }
            if (registration.getShippingAddress1() != null) {
                por.setShippingAddressLine1(addressLine1);
            }
            if (registration.getShippingAddress2() != null) {
                por.setShippingAddressLine2(addressLine2);
            }
            if (registration.getShippingAddress3() != null) {
                por.setShippingAddressLine3(addressLine3);
            }
            if (registration.getShippingAddress4() != null) {
                por.setShippingAddressLine4(addressLine4);
            }
            if (registration.getShippingAddress5() != null) {
                por.setShippingAddressLine5(addressLine5);
            }
            if (registration.getShippingAddress6() != null) {
                por.setShippingAddressLine6(addressLine6);
            }
            if (registration.getShippingAddress7() != null) {
                por.setShippingAddressLine7(addressLine7);
            }
            if (registration.getShippingAddress8() != null) {
                por.setShippingAddressLine8(addressLine8);
            }
            if (registration.getShippingAddressProvince() != null) {
                if (provinceId != null && provinceId != 0) {
                    registration.setShippingProvinceId(provinceId);
                    registration.setShippingDistrictList(provinceId, postalCode);
                }
                if (districtId != null && districtId != 0) {
                    registration.setShippingDistrictId(districtId);
                    registration.setShippingSubDistrictList(subdistrictDAO.findSubDistrictByDistrictId1(districtId, postalCode));
                }
                registration.setShippingSubDistrictId(subDistrictId);
                registration.setShippingPostal(postalCode);
                registration.setMsgPostalCode("");
            }

            // 4. CREATE PREVIEW
            countPreviewShippingAdd1(registration);
            countPreviewShippingAdd3(registration);
            countPreviewShippingAdd2(registration);
        }
    }

    public void copyAddressToBilling(RegistrationInfoValue registration) {
        if (registration.getPo() instanceof PurchaseOrderRegister && registration.getPo() != null) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();

            // 1. CLEAR OLD VALUE
            por.setBillingFullname(null);
            por.setBillingAddressLine1(null);
            por.setBillingAddressLine2(null);
            por.setBillingAddressLine3(null);
            por.setBillingAddressLine4(null);
            por.setBillingAddressLine5(null);
            por.setBillingAddressLine6(null);
            por.setBillingAddressLine7(null);
            por.setBillingAddressLine8(null);
            registration.setBillingProvinceId(null);
            registration.setBillingDistrictId(null);
            registration.setBillingSubDistrictId(null);
            registration.setBillingPostal(null);

            // 2. GET VALUE FROM SELECTED ADDRESS
            String fullName = null, addressLine1 = null, addressLine2 = null, addressLine3 = null, addressLine4 = null, addressLine5 = null;
            String addressLine6 = null, addressLine7 = null, addressLine8 = null, postalCode = null;
            Integer provinceId = null, districtId = null, subDistrictId = null;
            if (billingCopyFrom.equals("currentAddress")) {
                fullName = por.getCurrentFullname();
                addressLine1 = por.getCurrentAddressLine1();
                addressLine2 = por.getCurrentAddressLine2();
                addressLine3 = por.getCurrentAddressLine3();
                addressLine4 = por.getCurrentAddressLine4();
                addressLine5 = por.getCurrentAddressLine5();
                addressLine6 = por.getCurrentAddressLine6();
                addressLine7 = por.getCurrentAddressLine7();
                addressLine8 = por.getCurrentAddressLine8();

                if (registration.getCurrentProvinceId() != null) {
                    provinceId = registration.getCurrentProvinceId();
                }

                if (registration.getCurrentDistrictId() != null) {
                    districtId = registration.getCurrentDistrictId();
                }

                if (registration.getCurrentSubDistrictId() != null) {
                    subDistrictId = registration.getCurrentSubDistrictId();
                }

                postalCode = registration.getCurrentPostal();
            } else if (billingCopyFrom.equals("homeAddress")) {
                fullName = por.getHomeFullname();
                addressLine1 = por.getHomeAddressLine1();
                addressLine2 = por.getHomeAddressLine2();
                addressLine3 = por.getHomeAddressLine3();
                addressLine4 = por.getHomeAddressLine4();
                addressLine5 = por.getHomeAddressLine5();
                addressLine6 = por.getHomeAddressLine6();
                addressLine7 = por.getHomeAddressLine7();
                addressLine8 = por.getHomeAddressLine8();

                if (registration.getHomeProvinceId() != null) {
                    provinceId = registration.getHomeProvinceId();
                }

                if (registration.getHomeDistrictId() != null) {
                    districtId = registration.getHomeDistrictId();
                }

                if (registration.getHomeSubDistrictId() != null) {
                    subDistrictId = registration.getHomeSubDistrictId();
                }

                postalCode = registration.getHomePostal();
            } else if (billingCopyFrom.equals("shippingAddress")) {
                fullName = por.getShippingFullname();
                addressLine1 = por.getShippingAddressLine1();
                addressLine2 = por.getShippingAddressLine2();
                addressLine3 = por.getShippingAddressLine3();
                addressLine4 = por.getShippingAddressLine4();
                addressLine5 = por.getShippingAddressLine5();
                addressLine6 = por.getShippingAddressLine6();
                addressLine7 = por.getShippingAddressLine7();
                addressLine8 = por.getShippingAddressLine8();

                if (registration.getShippingProvinceId() != null) {
                    provinceId = registration.getShippingProvinceId();
                }

                if (registration.getShippingDistrictId() != null) {
                    districtId = registration.getShippingDistrictId();
                }

                if (registration.getShippingSubDistrictId() != null) {
                    subDistrictId = registration.getShippingSubDistrictId();
                }

                postalCode = registration.getShippingPostal();
            }

            // 3. SET VALUE
            if (registration.getBillingAddressFullName() != null) {
                por.setBillingFullname(fullName);
            }
            if (registration.getBillingAddress1() != null) {
                por.setBillingAddressLine1(addressLine1);
            }
            if (registration.getBillingAddress2() != null) {
                por.setBillingAddressLine2(addressLine2);
            }
            if (registration.getBillingAddress3() != null) {
                por.setBillingAddressLine3(addressLine3);
            }
            if (registration.getBillingAddress4() != null) {
                por.setBillingAddressLine4(addressLine4);
            }
            if (registration.getBillingAddress5() != null) {
                por.setBillingAddressLine5(addressLine5);
            }
            if (registration.getBillingAddress6() != null) {
                por.setBillingAddressLine6(addressLine6);
            }
            if (registration.getBillingAddress7() != null) {
                por.setBillingAddressLine7(addressLine7);
            }
            if (registration.getBillingAddress8() != null) {
                por.setBillingAddressLine8(addressLine8);
            }
            if (registration.getBillingAddressProvince() != null) {
                if (provinceId != null && provinceId != 0) {
                    registration.setBillingProvinceId(provinceId);
                    registration.setBillingDistrictList(provinceId, postalCode);
                }
                if (districtId != null && districtId != 0) {
                    registration.setBillingDistrictId(districtId);
                    registration.setBillingSubDistrictList(subdistrictDAO.findSubDistrictByDistrictId1(districtId, postalCode));
                }
                registration.setBillingSubDistrictId(subDistrictId);
                registration.setBillingPostal(postalCode);
                registration.setMsgPostalCode("");
            }

            // 4. CREATE PREVIEW      
            countPreviewBillingAdd1(registration);
            countPreviewBillingAdd3(registration);
            countPreviewBillingAdd2(registration);
        }
    }

    public void checkIdNo(RegistrationInfoValue registration) {
        validateThaiIdNo(registration);
    }

    public void checkAddress1(RegistrationInfoValue registration) {
        Integer remain = 0;
        Integer max = 30;
        if (registration.getPo() instanceof PurchaseOrderRegister) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();
            Integer a1 = por.getCurrentAddressLine1() != null ? por.getCurrentAddressLine1().length() : 0;
            Integer a2 = por.getCurrentAddressLine2() != null ? por.getCurrentAddressLine2().length() : 0;
            Integer a3 = por.getCurrentAddressLine3() != null ? por.getCurrentAddressLine3().length() : 0;

            if (a1 != 0 && a2 == 0 && a3 == 0) {
                remain = (max - a1);
            } else if (a1 != 0 && a2 != 0 && a3 == 0) {
                remain = (((max - 1) - a1) - a2);
            } else if (a1 != 0 && a2 == 0 && a3 != 0) {
                remain = (((max - 1) - a1) - a3);
            } else if (a1 != 0 && a2 != 0 && a3 != 0) {
                remain = (((max - 2) - a1) - a2) - a3;
            } else {
                remain = max;
            }

            String str = "";

            if (por.getCurrentAddressLine1() != null) {
                str += por.getCurrentAddressLine1();
            }
            if (por.getCurrentAddressLine2() != null) {
                if (!str.equals("")) {
                    str += " ";
                }
                str += por.getCurrentAddressLine2();
            }
            if (por.getCurrentAddressLine3() != null) {
                if (!str.equals("")) {
                    str += " ";
                }
                str += por.getCurrentAddressLine3();
            }

            //Integer len = a1 + a2 + a3;
            remainAddress1 = remain;
        }
    }

    public void checkAddress2(RegistrationInfoValue registration) {
        Integer remain = 0;
        Integer max = 30;
        if (registration.getPo() instanceof PurchaseOrderRegister) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();
            Integer a4 = por.getCurrentAddressLine4() != null ? por.getCurrentAddressLine4().length() : 0;
            Integer a5 = por.getCurrentAddressLine5() != null ? por.getCurrentAddressLine5().length() : 0;

            if (a4 != 0 && a5 == 0) {
                remain = (max - a4) - 2;
            } else if (a4 != 0 && a5 != 0) {
                remain = (((max - 5) - a4) - a5);
            } else if (a4 == 0 && a5 != 0) {
                remain = (max - a5) - 2;
            } else {
                remain = max;
            }

            //Integer len = a1 + a2 + a3;
            remainAddress2 = remain;
        }

    }

    public void checkIdNoPayer() {
        String idno = "";
        idno = pid1 != null ? pid1 : "";
        idno += pid2 != null ? pid2 : "";
        idno += pid3 != null ? pid3 : "";
        idno += pid4 != null ? pid4 : "";
        idno += pid5 != null ? pid5 : "";
        msgIdnoPayer = "";
        if (!idno.equals("")) {
            if (!ThaiIDValidator.checkPID(idno)) {
                msgIdnoPayer = " Invalid Citizen ID";
            } else {
                purchaseOrder.setPayerIdcard(idno);
            }
        } else if (idno.equals("") || idno.isEmpty()) {
            purchaseOrder.setPayerIdcard("");
        }
    }

    public void checkIssuerAndCardType(String cardNo) {
        String cardType = "VISA";
        if (cardNo != null && !cardNo.isEmpty()) {
            CardIssuer cardIssuer = null;           //new CardIssuer();
            if (cardNo.length() > 8) {               // CHECK 8 DIGIT
                cardIssuer = cardIssuerDAO.findByCardList(cardNo.substring(0, 8));
                if (cardIssuer == null) {            // CHECK 7 DIGIT
                    cardIssuer = cardIssuerDAO.findByCardList(cardNo.substring(0, 7));
                    if (cardIssuer == null) {        // CHECK 6 DIGIT
                        cardIssuer = cardIssuerDAO.findByCardList(cardNo.substring(0, 6));
                    }
                }
            }

            if (cardIssuer != null) {
                cardType = cardIssuer.getCardType();
                purchaseOrder.setCardIssuer(cardIssuer.getName());
                purchaseOrder.setBank(cardIssuer.getBank());
                bankId = cardIssuer.getBank().getId();
            } else {
                purchaseOrder.setCardIssuer(null);
                purchaseOrder.setBank(null);
                bankId = null;
                if (cardNo.substring(0, 1).equals("5")) {
                    cardType = "MASTER";
                } else {
                    cardType = "VISA";
                }
            }
        } else {
            cardType = "VISA";
        }
        this.purchaseOrder.setCardType(cardType);

    }

    public void checkIssuer() {
        try {
            PaymentMethod paymentMethod = paymentMethodDAO.findPaymentMethod(paymentMethodId);
            Boolean debitCard = paymentMethod.isDebitcard();
            Boolean creditCard = paymentMethod.isCreditcard();
            String paymentCard = "";
            if (creditCard) {
                paymentCard = "C";
            } else if (debitCard) {
                paymentCard = "D";
            }
            CardIssuer cardIssuer = cardIssuerDAO.findByCardList(purchaseOrder.getCardNo(), paymentCard);
            if (cardIssuer != null) {
                purchaseOrder.setCardType(cardIssuer.getCardType());
                //purchaseOrder.setCardIssuer(cardIssuer.getBank().getName());
                purchaseOrder.setBank(cardIssuer.getBank());
                bankId = cardIssuer.getBank().getId();
            } else {
                purchaseOrder.setCardType(null);
                purchaseOrder.setCardIssuer(null);
                purchaseOrder.setBank(null);
                bankId = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkCardNo() {
        messageCardno = "";
        String cardNo = FormUtil.buildCreditCardNo(cardNo1, cardNo2, cardNo3, cardNo4);
        if (cardNo.equals("")) {
            purchaseOrder.setCardNo(null);
            purchaseOrder.setCardType(null);
            purchaseOrder.setCardIssuer(null);
            purchaseOrder.setBank(null);
            bankId = null;
        } else if (cardNo.equals("0000000000000000") || cardNo.length() != 16) {
            messageCardno = " Invalid Card Number";
            purchaseOrder.setCardIssuer(null);
        } else {
            if (CreditCardValidator.luhnCheck(cardNo)) {
                purchaseOrder.setCardNo(cardNo);
//                checkIssuer();
                checkIssuerAndCardType(cardNo);
            } else {
                messageCardno = " Invalid Card Number";
                purchaseOrder.setCardType(null);
                purchaseOrder.setCardIssuer(null);
                purchaseOrder.setBank(null);
                bankId = null;
            }
        }
    }
    
    public void checkFamilyDiscount() {
        
    }

    public void checkImmediate() {
        if (purchaseOrder != null && String.valueOf(purchaseOrder.getSaleResult()).equals("Y")) {
            immediateCheck = true;
        } else {
            immediateCheck = false;
        }
        msgSaveRemark = "";
        if (this.product.getFamilyProductType() != null && this.product.getFamilyProductType() == 2) { // Check Family Product Discount 
            if (this.purchaseOrder != null) {
                int poId;
                if (this.purchaseOrder.getId() == null) {
                    poId = 0;
                }else{
                    poId = this.purchaseOrder.getId();
                }
                
                int mainPoId;
                if (this.purchaseOrder.getMainPoId() == null) {
                    mainPoId = poId;
                }else{
                    mainPoId = this.purchaseOrder.getMainPoId();
                }
                
                msgSaveRemark = this.purchaseOrderDAO.validateFamilyPurchaseOrder(poId, mainPoId, this.productPlanId, this.paymentModeId, String.valueOf(purchaseOrder.getSaleResult()));
            }
        /*
            if (this.purchaseOrder != null && this.purchaseOrder.getId() != null) { // Not New Purchase Order
                PurchaseOrder po = this.getPurchaseOrderDAO().findPurchaseOrder(this.purchaseOrder.getId());
                List<PurchaseOrderDetail> poDetail = (List<PurchaseOrderDetail>) po.getPurchaseOrderDetailCollection();
                ProductPlanDetail planDetail = poDetail.get(0).getProductPlanDetail();
                Integer productPlan = planDetail.getProductPlan().getId();
                String paymentMode = planDetail.getPaymentMode();
            
                if (!this.paymentMethodId.equals(paymentMode) || this.productPlanId != productPlan) {
                    this.showSaveRemark = true;
                }
                
                if (String.valueOf(this.purchaseOrder.getSaleResult()).equals("Y") && String.valueOf(this.purchaseOrder.getSaleResult()).equals("Y")) {
                    
                }
                
                List<PurchaseOrderDetail> poDetailFamilyList;
                if(this.purchaseOrder.getMainPoId() != null && this.purchaseOrder.getMainPoId() != 0) {
                    poDetailFamilyList = purchaseOrderDetailDAO.findFamilyPurchaseOrderDetail(this.purchaseOrder.getMainPoId());
                }else{
                    poDetailFamilyList = purchaseOrderDetailDAO.findFamilyPurchaseOrderDetail(this.purchaseOrder.getId());
                }
                Integer countMatchDiscountCondition = 0;
                msgSaveRemark = "Ref No : ";
                for (PurchaseOrderDetail podFamily : poDetailFamilyList) {
                    PurchaseOrder poFamily;
                    
                    if (podFamily.getPurchaseOrder().getId() == this.purchaseOrder.getId()) { // if same purchase order
                        poFamily = this.purchaseOrder;
                    }else{
                        poFamily = podFamily.getPurchaseOrder();
                    }
                    
                    if (msgSaveRemark.isEmpty()) {
                        msgSaveRemark = poFamily.getRefNo();
                    }else{
                        msgSaveRemark = ", " + poFamily.getRefNo();
                    }
                    
                    if (String.valueOf(poFamily.getSaleResult()).equals("Y") && !poFamily.getApprovalStatus().equalsIgnoreCase("rejected") && !poFamily.getPaymentStatus().equalsIgnoreCase("rejected") && !poFamily.getQcStatus().equalsIgnoreCase("rejected")) {
                        countMatchDiscountCondition++;
                    }

                            
                }
                
                if (this.productPlan.getMasterPlan() && countMatchDiscountCondition >= Integer.valueOf(product.getFamilyProductLogic())){
                    this.showSaveRemark = true;
                    msgSaveRemark = "Ref No. : " + msgSaveRemark + " is will be update Product Plan and Premium";
                }else if(!this.productPlan.getMasterPlan() && countMatchDiscountCondition < Integer.valueOf(product.getFamilyProductLogic())){
                    this.showSaveRemark = true;
                    msgSaveRemark = "Ref No. : " + msgSaveRemark + " is will be update Product Plan and Premium";
                }
            
        */
        }
        
        
        //msgSaveRemark = this.validateFamilyPurchaseOrder((this.purchaseOrder.getId() == null) ? 0 : this.purchaseOrder.getId(), (this.purchaseOrder.getMainPoId() == null) ? 0 : this.purchaseOrder.getMainPoId(), this.productPlanId, this.paymentModeId, String.valueOf(purchaseOrder.getSaleResult()));
        // (msgSaveRemark.isEmpty()) {
        //    msgSaveRemark = "No Save Remark";
        //}
        //if (this.paymentModeId != 0 || this.productPlanId != 0) {
         //   
        //}
        
        /*
        List<PurchaseOrderDetail> pod = (List<PurchaseOrderDetail>) this.purchaseOrder.getPurchaseOrderDetailCollection();
        if (pod.size() > 0 && pod.get(0).getProductPlan().getMasterPlan()) {
            if (this.productPlan.getProduct().getFamilyProductType()) {
                
            }
            
            boolean noSpouse = false;
            List<PurchaseOrderRegister> por = new ArrayList<>(pod.get(0).getPurchaseOrderRegisterCollection());
            if (por.size() > 0) {         
                String relationMainInsured = por.get(0).getRelationMainInsured();
                if (relationMainInsured.equalsIgnoreCase("S")) {
                    noSpouse = true;
                }
            }
        }
        */
        
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void checkImmediateAgain(ActionEvent event) {
        if (purchaseOrder.getSaleResult() == 'Y') {
            immediateCheck = true;
        } else {
            immediateCheck = false;
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void checkOnlineListener(ActionEvent event) {
        try {
            PaymentMethod payment = this.getPaymentMethodDAO().findPaymentMethod(poi.getPaymentMethod().getId());
            if (payment.getOnline()) {
                online = true;
            } else {
                online = false;
            }
        } catch (Exception e) {
            RegistrationLogging.logError(e.getMessage(), e);
        }
    }

    public void paymentMethodValueChangeListener(ValueChangeEvent event) throws Exception {
        paymentMethodId = -1;
        paymentMethod = new PaymentMethod();
        if (event.getNewValue() instanceof Integer) {
            paymentMethodId = (Integer) event.getNewValue();
        } else {
            try {
                paymentMethodId = Integer.parseInt((String) event.getNewValue());
            } catch (Exception e) {
            }
        }
        paymentMethod = this.getPaymentMethodDAO().findPaymentMethod(paymentMethodId);

        // CHECK CREDIT OR DEBIT CARD FOR FILTER NEW BANKLIST
        if (paymentMethod != null && (paymentMethod.isCreditcard() || paymentMethod.isDebitcard())) {
            if (paymentMethod.isCreditcard() && this.product != null && this.product.getCardBank() != null) {
                this.bankList = bankDAO.findBankByIds(this.product.getCardBank());
            } else if (paymentMethod.isDebitcard() && this.product != null && this.product.getCardBankDebit() != null) {
                this.bankList = bankDAO.findBankByIds(this.product.getCardBankDebit());
            }
        }
        if(!productFlexField1.equals("CMI") && !productFlexField1.equals("VMI")) {
            calculate();
        }
    }

    public void paymentModeValueChangeListener(ValueChangeEvent event) {
        paymentModeId = (String) event.getNewValue();
        productPlanDetail = null;
        productPlanId = 0;

        setupProductPlanList();

        setupPaymentMethodList();
        int ipaymentMethod = -1;
        try {
            ipaymentMethod = Integer.parseInt(this.purchaseOrder.getPaymentMethod());
        } catch (Exception e) {
        }

        if (this.purchaseOrder != null && ipaymentMethod != -1) {
            // check for exists payment method when change payment mode
            boolean isExists = false;
            for (PaymentMethod o : this.productPaymentMethodList) {
                isExists = o.getId().intValue() == ipaymentMethod;
                if (isExists) {
                    break;
                }
            }
            if (!isExists) {
                this.purchaseOrder.setPaymentMethod(null);
                this.purchaseOrder.setPaymentMethodName(null);
            } // reset data
        }
        calculate();
    }

    public void productPlanValueChangeListener(ValueChangeEvent event) {
        productPlanId = (Integer) event.getNewValue();
        calculate();
    }

    public void genderValueChangeListener(ValueChangeEvent event) {
        if (product.getGender() != null && product.getGender()) {
            gender = (String) event.getNewValue();

            productPlanDetail = null;
            productPlanId = 0;

            setupProductPlanList();

            calculateLifeNonLife();
        }
    }

    public boolean checkFamily(Product product) {
        int productId = product.getId();
        int chkFamily = productDAO.checkFamily(productId);
        if (chkFamily == 0) {
            return false;
        } else {
            return true;
        }
    }
    
    private void setupProductPlanList() {
        if (checkFamily(product)) {
            //if (spouse.equals("spouse") && spouse != null && !spouse.equals("")) {
            if (poCopyForMain != null) {
                //ProductPlan productPlanForMain = productPlanDAO.findProductPlan(productPlanIdForMain);
                if (product.getFamilyProductType() == 1) {
                    productPlanList = productPlanDAO.findProductPlanEntitiesProductPaymentModeByFamilyIsSpouse(productPlanIdForMain, paymentModeId);
                }else{
                    productPlanList = productPlanDAO.findProductPlanEntitiesProductPaymentModeByFamilyDiscount(productPlanIdForMain, paymentModeId, purchaseOrderDetail.getProductPlan().getMasterPlan());
                }
            } else {
                productPlanList = productPlanDAO.findProductPlanEntitiesProductPaymentModeByFamily(product.getId(), paymentModeId, purchaseOrderDetail.getProductPlan().getMasterPlan(), gender);
            }
        } else {
            productPlanList = productPlanDAO.findProductPlanEntitiesByProductPaymentMode(product.getId(), paymentModeId, gender);
        }
    }

    public void calculateMotorListener(ActionEvent event) {
        calculateMotor();
    }

    public void childRegistrationFormListener(ValueChangeEvent event) {
        UIInput component = (UIInput) event.getComponent();
        int formIndex = Integer.parseInt(component.getAttributes().get("formNo").toString()) - 1;
        childRegInfo.get(formIndex).setEnable((Boolean) event.getNewValue());
    }

    public void declarationFormListener(ValueChangeEvent event) {
        UIInput component = (UIInput) event.getComponent();
        int formIndex = Integer.parseInt(component.getAttributes().get("formNo").toString()) - 1;
        declarInfo.get(formIndex).setDeclarationEnable((Boolean) event.getNewValue());
    }

    public void childRegFieldListener(ValueChangeEvent event) {
        UIInput component = (UIInput) event.getComponent();
        int formIndex = Integer.parseInt(component.getAttributes().get("formNo").toString()) - 1;
        int fieldIndex = Integer.parseInt(component.getAttributes().get("fieldNo").toString()) - 1;
        int itemIndex = Integer.parseInt(component.getAttributes().get("itemNo").toString()) - 1;
        if (childRegInfo.get(formIndex).getFlexFields().get(fieldIndex).getControlType().equals("radio")) {
            for (SelectItem item : childRegInfo.get(formIndex).getFlexFields().get(fieldIndex).getItemList()) {
                item.setDisabled(false);
                item.setDescription("");
            }
        }
        childRegInfo.get(formIndex).getFlexFields().get(fieldIndex).getItemList().get(itemIndex).setDisabled(((Boolean) event.getNewValue()).booleanValue());
        childRegInfo.get(formIndex).getFlexFields().get(fieldIndex).getItemList().get(itemIndex).setDescription("");
    }

    public void declarationFieldListener(ValueChangeEvent event) {
        UIInput component = (UIInput) event.getComponent();
        int formIndex = Integer.parseInt(component.getAttributes().get("formNo").toString()) - 1;
        int fieldIndex = Integer.parseInt(component.getAttributes().get("fieldNo").toString()) - 1;
        int itemIndex = Integer.parseInt(component.getAttributes().get("itemNo").toString()) - 1;
        //declarInfo.get(formIndex).getFlexFields().get(fieldIndex).getItemList().get(itemIndex).setValue((Boolean) event.getNewValue());
        if (declarInfo.get(formIndex).getFlexFields().get(fieldIndex).getControlType().equals("radio")) {
            for (SelectItem item : declarInfo.get(formIndex).getFlexFields().get(fieldIndex).getItemList()) {
                item.setDisabled(false);
                item.setDescription("");
            }
        }
        declarInfo.get(formIndex).getFlexFields().get(fieldIndex).getItemList().get(itemIndex).setDisabled(((Boolean) event.getNewValue()).booleanValue());
        declarInfo.get(formIndex).getFlexFields().get(fieldIndex).getItemList().get(itemIndex).setDescription("");
    }

    public void flexFieldDateListener(ActionEvent e) {
        int fieldIndex = Integer.parseInt(JSFUtil.getRequestParameterMap("flexFieldNo").toString());
        String strDate = JSFUtil.getRequestParameterMap("flexFieldDate");
        int registrationNo = Integer.parseInt(JSFUtil.getRequestParameterMap("registrationNo").toString());

        Date fieldDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
            fieldDate = sdf.parse(strDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd 16:00:00");
            for(FlexFieldInfoValue  f :this.regInfo.get(registrationNo).getFlexFields()){
                if(f.getNo()==fieldIndex && f.getControlType().equals("calendar")){
                    if(!productFlexField1.equals("CMI") && !productFlexField1.equals("VMI")){
                        f.setPoFlexField(dateFormat.format(fieldDate));
                        break;
                    }else {
                        if(fieldIndex == 47) {
                            f.setPoFlexField(dateFormat2.format(fieldDate));
                            break;
                        } else {
                            f.setPoFlexField(dateFormat.format(fieldDate));
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void childRegDateListener(ActionEvent e) {
        int formIndex = Integer.parseInt(JSFUtil.getRequestParameterMap("formNo").toString()) - 1;
        int fieldIndex = Integer.parseInt(JSFUtil.getRequestParameterMap("fieldNo").toString()) - 1;
        String strDate = JSFUtil.getRequestParameterMap("date");

        Date fieldDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
            fieldDate = sdf.parse(strDate);
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            childRegInfo.get(formIndex).getFlexFields().get(fieldIndex).setPoFlexField(sdf.format(fieldDate));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void declarationDateListener(ActionEvent e) {
        int formIndex = Integer.parseInt(JSFUtil.getRequestParameterMap("formNo").toString()) - 1;
        int fieldIndex = Integer.parseInt(JSFUtil.getRequestParameterMap("fieldNo").toString()) - 1;
        String strDate = JSFUtil.getRequestParameterMap("date");

        Date fieldDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
            fieldDate = sdf.parse(strDate);
            declarInfo.get(formIndex).getFlexFields().get(fieldIndex).setPoFlexField(sdf.format(fieldDate));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clickSameAsPolicyHolder(ActionEvent event) {
        if (purchaseOrder.getPayerSameAsInsured()) {
            for (RegistrationInfoValue<PurchaseOrderRegister> reg : regInfo) {
                PurchaseOrderRegister poReg = reg.getPo();
                if (poReg.getNo() == 1) {
                    payerInitial = poReg.getInitial();
                    payerName = poReg.getName();
                    payerSurname = poReg.getSurname();
                    payerGender = poReg.getGender();
                    break;
                }
            }
            purchaseOrder.setPayerInitial(payerInitial);
            purchaseOrder.setPayerName(payerName);
            purchaseOrder.setPayerSurname(payerSurname);
            purchaseOrder.setPayerGender(payerGender);
        } else {
            purchaseOrder.setPayerInitial(null);
            purchaseOrder.setPayerName(null);
            purchaseOrder.setPayerSurname(null);
            purchaseOrder.setPayerGender(null);
        }
    }

    public void setQuestionnares() {
        /*
        Questionnaire questionnaire = registrationForm.getQuestionnaire();
        if (questionnaire == null) {
            return;
        }
        List<QuestionnaireDetail> questionnaireDetails = (List<QuestionnaireDetail>) questionnaire.getQuestionnaireDetailCollection();
        questionnaires = new ArrayList<QuestionnaireInfoValue<PurchaseOrderQuestionaire>>();

        for (QuestionnaireDetail q : questionnaireDetails) {
            PurchaseOrderQuestionaire tpoq = null;
            if (purchaseOrderDetail.getPurchaseOrderQuestionaireCollection() != null) {
                for(PurchaseOrderQuestionaire tq : purchaseOrderDetail.getPurchaseOrderQuestionaireCollection()){
                    if(q.getNo() == tq.getSeqNo()){
                        tpoq = tq;
                        break;
                    }else{
                        tpoq =  null;
                    }
                }
            }
            if(tpoq == null) tpoq = new PurchaseOrderQuestionaire();
            QuestionnaireInfoValue<PurchaseOrderQuestionaire> item = new QuestionnaireInfoValue<PurchaseOrderQuestionaire>(questionnaire, q, tpoq);
            questionnaires.add(item);
        }*/
    }

    public void selectProductListener(ActionEvent event) {
        productPlanId = Integer.parseInt(JSFUtil.getRequestParameterMap("productPlanId"));
        productPlan = productPlanDAO.findProductPlan(productPlanId);
        if (productPlan != null) {
            purchaseOrder.setAmount1(productPlan.getNetPremium() == null ? new Double(0) : productPlan.getNetPremium());
            purchaseOrder.setAmount2(productPlan.getAct() == null ? new Double(0) : productPlan.getAct());
            purchaseOrder.setDiscount(new Double(0));
        }
        //purchaseOrder.setNoInstallment(0);
        calculate();
    }

    public void noInstallmentValueChangeListener(ValueChangeEvent event) {
        Integer newNo = (Integer) event.getNewValue();
        purchaseOrder.setNoInstallment(newNo);
        calculatePayment2();
    }

    public void savePaymentListener(ActionEvent event) {
        //initNoPaid();
        edit = true;
        poi.setUpdateBy(JSFUtil.getUserSession().getUserName());
        poi.setUpdateById(JSFUtil.getUserSession().getUsers().getId());
        poi.setUpdateDate(new Date());
        calculatePayment2();
    }

    public void installmentNoListener(ActionEvent event) {
        paymentMethodList = this.getPaymentMethodDAO().findPaymentMethodEntities();
        Integer installmentNo = Integer.parseInt(JSFUtil.getRequestParameterMap("installmentNo"));
        for (PurchaseOrderInstallment o : poInstallmentList) {
            if (o.getInstallmentNo() == installmentNo.intValue()) {
                poi = o;
                break;
            }
        }
        if (poi.getId() == null) {
            poi.setPaymentMethod(new PaymentMethod());
        }
    }

    public void dueDateListener(ValueChangeEvent event) {
        Date dd = (Date) event.getNewValue();
        if (poInstallmentList != null && !poInstallmentList.isEmpty()) {
            for (PurchaseOrderInstallment poi : poInstallmentList) {
                if (purchaseOrder.getNoInstallment().intValue() == poi.getInstallmentNo()) {
                    if (poi.getPaymentStatus() == null || !poi.getPaymentStatus().equals("paid")) {
                        if (poi.getDueDate() != null) {
                            if (dd.before(poi.getDueDate())) {
                                poi.setDueDate(dd);
                            }
                        } else {
                            poi.setDueDate(dd);
                        }
                    }
                }
            }
        }
    }

    public void changeDobListener() {
        this.calculate();
    }

    public void payerIdCardExpiryDateListener(ActionEvent e) {
        String strDate = JSFUtil.getRequestParameterMap("expirydate");
        Date expiryDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
            expiryDate = sdf.parse(strDate);
            this.purchaseOrder.setPayerIdcardExpiryDate(expiryDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void payerDobListener(ActionEvent e) {
        String strDate = JSFUtil.getRequestParameterMap("payerdob");
        Date payerDob;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
            payerDob = sdf.parse(strDate);
            this.purchaseOrder.setPayerDob(payerDob);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void paidDateListener(ActionEvent e) {
        String strDate = JSFUtil.getRequestParameterMap("paiddate");
        Date paidDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
            paidDate = sdf.parse(strDate);
            this.purchaseOrder.setPaidDate(paidDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void paymentExpectedDateListener(ActionEvent e) {
        String strDate = JSFUtil.getRequestParameterMap("expecteddate");
        Date expectedDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
            expectedDate = sdf.parse(strDate);
            this.purchaseOrder.setPaymentExpectedDate(expectedDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getPageFrom() {
        if (JSFUtil.getRequest().getAttribute("page") != null && ((String) JSFUtil.getRequest().getAttribute("page")).equals("confirmation")) {
            this.initialize();
            pageFrom = (String) JSFUtil.getRequest().getAttribute("page");
        }
        JSFUtil.getRequest().removeAttribute("page");
        return pageFrom;
    }

    public void setProviceList() {
        provinceList = getProvinceDAO().getProvinceList1();
    }

    public List<SelectItem> getDistrictList(Integer provinceId) {
        List<District> districtList = getDistrictDAO().findDistrictByProvinceId(provinceId);
        List<SelectItem> values = new ArrayList<SelectItem>();
        values.add(new SelectItem(null, JSFUtil.getBundleValue("please select")));
        for (District obj : districtList) {
            values.add(new SelectItem(obj.getId(), obj.getName()));
        }
        return values;
    }

    public void payerListener(ValueChangeEvent event) {
        Integer payerType = (Integer) event.getNewValue();
        purchaseOrder.setPayerType(payerType);
        for (RegistrationInfoValue<PurchaseOrderRegister> reg : regInfo) {
            PurchaseOrderRegister poReg = reg.getPo();
            if (purchaseOrder.getPayerType() != null && purchaseOrder.getPayerType() == 2 && poReg.getNo() == 1) {
                purchaseOrder.setPayerInitial(poReg.getInitial()); //1
                purchaseOrder.setPayerName(poReg.getName()); //2
                purchaseOrder.setPayerSurname(poReg.getSurname()); //3
                purchaseOrder.setPayerIdtype(poReg.getIdcardTypeId() != null ? poReg.getIdcardTypeId().toString() : null); //4
                purchaseOrder.setPayerIdcard(poReg.getIdno()); //5
                purchaseOrder.setPayerGender(poReg.getGender()); //6
                purchaseOrder.setPayerDob(poReg.getDob()); //7
                purchaseOrder.setPayerAddress1(poReg.getCurrentAddressLine1()); //8
                purchaseOrder.setPayerAddress2(poReg.getCurrentAddressLine2()); //9
                purchaseOrder.setPayerAddress3(poReg.getCurrentAddressLine3()); //10
                purchaseOrder.setPayerSubDistrict(poReg.getCurrentSubDistrict()); //11
                purchaseOrder.setPayerDistrict(poReg.getCurrentDistrict()); //12
                purchaseOrder.setPayerPostalCode(poReg.getCurrentPostalCode()); //13
                purchaseOrder.setPayerMaritalStatus(poReg.getMaritalStatus()); //14
                purchaseOrder.setPayerHomePhone(poReg.getHomePhone()); //15
                purchaseOrder.setPayerMobilePhone(poReg.getMobilePhone()); //16
                purchaseOrder.setPayerOfficePhone(poReg.getOfficePhone()); //17
                purchaseOrder.setPayerOccupation(poReg.getOccupation() != null ? poReg.getOccupation() : null); //18
                purchaseOrder.setPayerOccupationOther(poReg.getPosition()); //19
                purchaseOrder.setPayerEmail(poReg.getEmail()); //20
                //purchaseOrder.setPayerRelation(JSFUtil.getBundleValue("me"));
                break;
            }
        }

        if (product.getFamilyProduct() && purchaseOrder.getParentPurchaseOrder() != null && purchaseOrder.getPayerType() == 1) {
            purchaseOrder.setPayerInitial(purchaseOrder.getParentPurchaseOrder().getPayerInitial()); //1
            purchaseOrder.setPayerName(purchaseOrder.getParentPurchaseOrder().getPayerName()); //2
            purchaseOrder.setPayerSurname(purchaseOrder.getParentPurchaseOrder().getPayerSurname()); //3
            purchaseOrder.setPayerIdtype(purchaseOrder.getParentPurchaseOrder().getPayerIdtype()); //4
            purchaseOrder.setPayerIdcard(purchaseOrder.getParentPurchaseOrder().getPayerIdcard()); //5
            purchaseOrder.setPayerGender(purchaseOrder.getParentPurchaseOrder().getPayerGender()); //6
            purchaseOrder.setPayerDob(purchaseOrder.getParentPurchaseOrder().getPayerDob()); //7
            purchaseOrder.setPayerAddress1(purchaseOrder.getParentPurchaseOrder().getPayerAddress1()); //8
            purchaseOrder.setPayerAddress2(purchaseOrder.getParentPurchaseOrder().getPayerAddress2()); //9
            purchaseOrder.setPayerAddress3(purchaseOrder.getParentPurchaseOrder().getPayerAddress3()); //10
            purchaseOrder.setPayerSubDistrict(purchaseOrder.getParentPurchaseOrder().getPayerSubDistrict() != null ? purchaseOrder.getParentPurchaseOrder().getPayerSubDistrict() : null); //11
            purchaseOrder.setPayerDistrict(purchaseOrder.getParentPurchaseOrder().getPayerDistrict() != null ? purchaseOrder.getParentPurchaseOrder().getPayerDistrict() : null); //12
            purchaseOrder.setPayerPostalCode(purchaseOrder.getParentPurchaseOrder().getPayerPostalCode()); //13
            purchaseOrder.setPayerMaritalStatus(purchaseOrder.getParentPurchaseOrder().getPayerMaritalStatus()); //14
            purchaseOrder.setPayerHomePhone(purchaseOrder.getParentPurchaseOrder().getPayerHomePhone()); //15
            purchaseOrder.setPayerMobilePhone(purchaseOrder.getParentPurchaseOrder().getPayerMobilePhone()); //16
            purchaseOrder.setPayerOfficePhone(purchaseOrder.getParentPurchaseOrder().getPayerOfficePhone()); //17
            purchaseOrder.setPayerOccupation(purchaseOrder.getParentPurchaseOrder().getPayerOccupation()); //18
            purchaseOrder.setPayerOccupationOther(purchaseOrder.getParentPurchaseOrder().getPayerOccupationOther()); //19
            purchaseOrder.setPayerOccupationCode(purchaseOrder.getParentPurchaseOrder().getPayerOccupationCode()); //19
            purchaseOrder.setPayerEmail(purchaseOrder.getParentPurchaseOrder().getPayerEmail()); //20
        }

        if (purchaseOrder.getPayerType() != null && purchaseOrder.getPayerType() == 3) {
            purchaseOrder.setPayerInitial(null); //1
            purchaseOrder.setPayerName(null); //2
            purchaseOrder.setPayerSurname(null); //3
            purchaseOrder.setPayerIdtype(null); //4
            purchaseOrder.setPayerIdcard(null); //5
            purchaseOrder.setPayerGender(null); //6
            purchaseOrder.setPayerDob(null); //7
            purchaseOrder.setPayerAddress1(null); //8
            purchaseOrder.setPayerAddress2(null); //9
            purchaseOrder.setPayerAddress3(null); //10
            purchaseOrder.setPayerSubDistrict(null); //11
            purchaseOrder.setPayerDistrict(null); //12
            purchaseOrder.setPayerPostalCode(null); //13
            purchaseOrder.setPayerMaritalStatus(null); //14
            purchaseOrder.setPayerHomePhone(null); //15
            purchaseOrder.setPayerMobilePhone(null); //16
            purchaseOrder.setPayerOfficePhone(null); //17
            purchaseOrder.setPayerOccupation(null); //18
            purchaseOrder.setPayerOccupationOther(null); //19
            purchaseOrder.setPayerOccupationCode(null); //19
            purchaseOrder.setPayerEmail(null); //20
            payerOccupationCategoryId = null;
            payerOccupationId = null;
            payerProvinceId = null;
            payerDistrictId = null;
            payerSubDistrictId = null;

        }
    }

    public void payerOccupationCategoryListener(ValueChangeEvent event) {
        payerOccupationCategoryId = (Integer) event.getNewValue();
        this.setPayerOccupationList1(payerOccupationCategoryId);
    }

    public void payerProvinceListener(ValueChangeEvent event) {
        Integer provinceId = (Integer) event.getNewValue();
        if (provinceId != null && provinceId != 0) {
            payerDistrictList = getDistrictList1(provinceId, "");
        } else if (payerDistrictList != null) {
            payerDistrictList.clear();
        }
        payerDistrictId = 0;
        if (payerSubDistrictList != null) {
            payerSubDistrictList.clear();
            payerSubDistrictId = 0;
        }
    }

    public void payerDistrictListener(ValueChangeEvent event) {
        Integer districtId = (Integer) event.getNewValue();
        if (districtId != null && districtId != 0) {
            payerSubDistrictList = getSubDistrictList1(districtId, "");
        }
        payerSubDistrictId = 0;
    }

    public void payerSubDistrictListener(ValueChangeEvent event) {
        Integer subdistrictId = (Integer) event.getNewValue();
        if (subdistrictId != null && subdistrictId != 0) {
            Subdistrict subdistrict = subdistrictDAO.findSubDistrict(subdistrictId);
            payerPostalCode = subdistrict.getPostalCode();
        }
    }

    public boolean isShowCreditCardInfo() {
        if (this.productPaymentMethodCastorList == null) {//version 1 not use paymentMethodCaster
            return (this.purchaseOrder != null && this.purchaseOrder.getPaymentMethod() != null && "1".equals(this.purchaseOrder.getPaymentMethod()))
                    || (paymentMethodId != null && 1 == paymentMethodId.intValue());
        } else {
            boolean result = false;
            int ipaymentMethodId = -1;
            try {
                ipaymentMethodId = Integer.parseInt(this.purchaseOrder.getPaymentMethod());
            } catch (Exception e) {
            }
            if (ipaymentMethodId != -1) {
                for (PaymentMethod o : this.productPaymentMethodList) {
                    if (o.getId().intValue() == ipaymentMethodId && (o.isCreditcard() || o.isDebitcard())) {
                        result = true;
                        break;
                    }
                }
            }
            return result;
        }
    }

    public boolean isShowBankTransferInfo() {
        if (this.productPaymentMethodCastorList == null) {//version 1 not use paymentMethodCaster
            return (this.purchaseOrder != null && this.purchaseOrder.getPaymentMethod() != null && "3".equals(this.purchaseOrder.getPaymentMethod()))
                    || (paymentMethodId != null && 3 == paymentMethodId.intValue());
        } else {
            return false; // no config bank transfer flag at db
        }
    }

    public String editAction() {
        return EDIT;
    }

    public String backAction() {
        return SUCCESS;
    }

    @Deprecated
    public String selfAction() {
        initialize();
        return SELF;
    }

    public String previous() {
        return fromSaleApprovalPage ? "saleApproaching.xhtml" : SALEHISTORY_PAGE;
    }

    public String nextAction() {
        JSFUtil.getUserSession().setPurchaseOrders(purchaseOrder); //set po to usersession
        JSFUtil.getRequest().setAttribute("purchaseOrderId", purchaseOrder.getId());
        JSFUtil.getRequest().setAttribute("productId", product.getId());
        JSFUtil.getRequest().setAttribute("page", "registration");
        if (this.fromSaleApprovalPage) {
            JSFUtil.getRequest().setAttribute("mainPage", "saleapproval");
            return "paymentforsaleapproval.xhtml";
        }
        return CONFIRMATION_PAGE;
    }
    
    public String saveQcQaTransAction() {
        //return "/front/customerHandling/qcsaleapproval.xhtml";
        showSaveRemark = true;
        msgSaveRemark = "Save Complete";
        
        return EDIT;
    }

    public String saveAction() {
        RegistrationLogging.debugData("Begin save purchase order : id=" + this.purchaseOrder.getId() + ", From ADid=" + ((this.purchaseOrder.getAssignmentDetail() == null) ? null : this.purchaseOrder.getAssignmentDetail().getId()) + ", sale result=" + this.purchaseOrder.getSaleResult());
        String returnPage = null;
        TimeCollector tc = new TimeCollector(true);
        PurchaseOrder po4Save = null;
        if (productPlanDetailInfoJson != null) {
            this.purchaseOrder.setProductPlanDetailInfo(productPlanDetailInfoJson);
        }
        try {
            po4Save = new PurchaseOrder();
            DataTransferUtil.transferPurchaseOrderData(po4Save, this.purchaseOrder);
            PurchaseOrderDetail pod4Save = null;
            if (po4Save.getPurchaseOrderDetailCollection() != null && po4Save.getPurchaseOrderDetailCollection().size() > 0) {
                pod4Save = (PurchaseOrderDetail) po4Save.getPurchaseOrderDetailCollection().toArray()[0];
            } else {
                pod4Save = new PurchaseOrderDetail();
                DataTransferUtil.transferPurchaseOrderDetailData(pod4Save, this.purchaseOrderDetail);
                pod4Save.setPurchaseOrder(po4Save);
                po4Save.setPurchaseOrderDetailCollection(new ArrayList<PurchaseOrderDetail>(1));
                po4Save.getPurchaseOrderDetailCollection().add(pod4Save);
            }

            //Step1 : validate data
            boolean isValid = validateBeforeSave(po4Save);
            if (!isValid) {
                return returnPage; //back to register form with error message
            }
            //Step2 : prepared  purchaseOrderRegister object and put it into purchase_order_register
            preparePurchaseOrderRegisterList4Save(pod4Save, this.regInfo);

            //Step2.1 : prepared  purchaseOrderChildRegister object and put it into purchase_order_child_register
            preparePurchaseOrderChildRegisterList4Save(pod4Save, this.childRegInfo);

            //Step2.2 : prepared  purchaseOrderDeclaration object and put it into purchas_order_declaration
            preparePurchaseOrderDeclarationList4Save(pod4Save, this.declarInfo);

            //Step3 : prepared purchaseOrder object at entry form
            preparePurchaseOrderDataSection4Save(po4Save, pod4Save);

            //CREATE QA TRANS
            po4Save.setQaTransApproval(this.qaTrans4Save);
            if (po4Save.getId() == null) {  // ADD NEW QA TRANS
                if (po4Save.getQaTransApproval() != null && po4Save.getQaTransApproval().getQaFormId() != null) {
                    getQaTransDAO().create(po4Save.getQaTransApproval());
                }
            } else {    // EDIT QA TRANS
                if (po4Save.getQaTransApproval() != null && po4Save.getQaTransApproval().getQaFormId() != null) {
                    if (po4Save.getQaTransApproval().getId() == null) { //A1: In case, not have qa trans approval before
                        getQaTransDAO().create(po4Save.getQaTransApproval());
                        RegistrationTxDAO registrationDAO = new RegistrationTxDAO();
                        registrationDAO.updateNewApprovalQaFormOnPurchaseOrder(po4Save);
                    } else { //A2: In case, qa trans approval is exists in po, so delete all qa trans detail and insert new qa trans detail.
                        getQaTransDetailDAO().deleteQaTransDetails(po4Save.getQaTransApproval().getId());
                        for (QaTransDetail qatd : po4Save.getQaTransApproval().getQaTransDetailCollection()) {
                            getQaTransDetailDAO().create(qatd);
                        }
                    }
                }

            }

            //Step4 : prepared purchaseOrder object at approval section
            preparePurchaseOrderApprovalSection4Save(po4Save);

            //Step5 : prepared assignement detail and setupQATrans from QAFormController
            po4Save.setAssignmentDetail(new AssignmentDetail());

            AssignmentDetail ad = null;
            if (JSFUtil.getUserSession().getAssignmentDetail() != null) {
                ad = JSFUtil.getUserSession().getAssignmentDetail();
            } else {
                this.purchaseOrder.getAssignmentDetail();
            }

            DataTransferUtil.transferAssignmentDetailDataWoCl(po4Save.getAssignmentDetail(), ad);
            FormUtil.setupAssignmentDetailStatusFollowingPOStatus(po4Save);
//          po4Save.setQaTransApproval(this.qaTrans4Save);

            //Check Payment Name
            if (po4Save.getPaymentMethod() != null && !po4Save.getPaymentMethod().equals("")) {
                PaymentMethod payment4Save = this.getPaymentMethodDAO().findPaymentMethod(Integer.parseInt(po4Save.getPaymentMethod()));
                po4Save.setPaymentMethodName(payment4Save.getName());
            }

            //Step6 : save
            MxLocalServiceResponse serviceResult = new MxLocalServiceResponse();
            if (po4Save.getId() == null) {
                try {
                    registrationTxService.insertNewPO(po4Save, serviceResult);
                } catch (TransactionSystemException re) {
                    RegistrationLogging.logError("Rollback transaction after error has occured.", null);
                }
            } else {
                try {
                    registrationTxService.updatePO(po4Save, serviceResult);
                } catch (TransactionSystemException re) {
                    RegistrationLogging.logError("Rollback transaction after error has occured.", null);
                }
            }

            //Step7 : Get result 
            boolean sendNotiFlag = false;
            if (MxLocalServiceResponse.MX_LOCAL_SERVICE_STATUS.OK.equals(serviceResult.getServiceStatus())) {
                //Step7.1 : Manage Success result 
                //PurchaseOrder successPO = (PurchaseOrder)serviceResult.getResult();
                JSFUtil.getUserSession().setAssignmentDetail(po4Save.getAssignmentDetail());
                JSFUtil.getUserSession().setPurchaseOrders(po4Save); //set po to usersession
                JSFUtil.getRequest().setAttribute("purchaseOrder", po4Save);
                JSFUtil.getRequest().setAttribute("purchaseOrderId", po4Save.getId());
                JSFUtil.getRequest().setAttribute("productId", this.product.getId());
                JSFUtil.getRequest().setAttribute("page", "registration");

                //SAVE REFERENCE YES SALE TO USER SESSION
                if (po4Save.getSaleResult() == 'Y') {
                    JSFUtil.getUserSession().setRefYes(po4Save.getRefNo());
                }

                if (fromSaleApprovalPage) {
                    returnPage = "registrationforsaleapprovalthankyou.xhtml";
                } else {
                    JSFUtil.getUserSession().setShowContactSummary(true);
                    returnPage = ('Y' == po4Save.getSaleResult()) ? CONFIRMATION_PAGE : SALEHISTORY_PAGE;
                }
                sendNotiFlag = ('Y' == po4Save.getSaleResult());
            } else {
                //Step7.2 : Manage error reuslt
                returnPage = null;
                message = "Could not save purchase order. [Cause: " + serviceResult.getServiceErrorMessage() + "] ";
            }

            //Step7  : save notification
            try {
                //if ( sendNotiFlag ) this.saveNotification();
            } catch (Exception ne) {
                RegistrationLogging.logError("Cannot save notification after insert new po " + ne.getMessage(), ne);
            }

        } catch (Exception e) {
            e.printStackTrace();
            StringBuilder datt = new StringBuilder(RegistrationLogging.getObjectValueToString("Purchase Order", po4Save));
            try {
                if (po4Save != null) {
                    datt.append(RegistrationLogging.getObjectValueToString("Purchase Detail", po4Save.getPurchaseOrderDetailCollection()));
                    PurchaseOrderDetail podTrack = po4Save.getPurchaseOrderDetailCollection() == null ? null : (PurchaseOrderDetail) po4Save.getPurchaseOrderDetailCollection().toArray()[0];
                    datt.append(RegistrationLogging.getObjectValueToString("Purchase Registration Form", podTrack.getPurchaseOrderRegisterCollection()));
                    datt.append(RegistrationLogging.getObjectValueToString("Approval Log", po4Save.getPurchaseOrderApprovalLogCollection()));
                }
            } catch (Exception ein) {
                ein.printStackTrace();
            }
            RegistrationLogging.logError(e.getMessage() + " Save Data [" + datt.toString() + "] ", e);
            returnPage = null;
            message = "Could not save purchase order. [Cause: " + e.getMessage() + "] ";
        } finally {
            RegistrationLogging.debugElapseTime(tc, "Total save purchase order time.");
            RegistrationLogging.debugData("Finished save purchase order , returnPage=" + returnPage + ", message=" + message);

        }
        return returnPage;
    }

    public String saveApply() {
        if(purchaseOrder == null || purchaseOrder.getId()==null) {
            RegistrationLogging.debugData("Begin save purchase order : id=" + this.purchaseOrder.getId() + ", From ADid=" + ((this.purchaseOrder.getAssignmentDetail() == null) ? null : this.purchaseOrder.getAssignmentDetail().getId()) + ", sale result=" + this.purchaseOrder.getSaleResult());
            String returnPage = null;
            TimeCollector tc = new TimeCollector(true);
            PurchaseOrder po4Save = null;
            if (productPlanDetailInfoJson != null) {
                this.purchaseOrder.setProductPlanDetailInfo(productPlanDetailInfoJson);
            }
            try {
                po4Save = new PurchaseOrder();
                DataTransferUtil.transferPurchaseOrderData(po4Save, this.purchaseOrder);
                PurchaseOrderDetail pod4Save = null;
                if (po4Save.getPurchaseOrderDetailCollection() != null && po4Save.getPurchaseOrderDetailCollection().size() > 0) {
                    pod4Save = (PurchaseOrderDetail) po4Save.getPurchaseOrderDetailCollection().toArray()[0];
                } else {
                    pod4Save = new PurchaseOrderDetail();
                    DataTransferUtil.transferPurchaseOrderDetailData(pod4Save, this.purchaseOrderDetail);
                    pod4Save.setPurchaseOrder(po4Save);
                    po4Save.setPurchaseOrderDetailCollection(new ArrayList<PurchaseOrderDetail>(1));
                    po4Save.getPurchaseOrderDetailCollection().add(pod4Save);
                }

                po4Save.setSaleResult('F');
                //Step1 : validate data
//            boolean isValid = validateBeforeSave(po4Save);
//            if (!isValid) {
//                return returnPage; //back to register form with error message
//            }
                //Step2 : prepared  purchaseOrderRegister object and put it into purchase_order_register
                preparePurchaseOrderRegisterList4Save(pod4Save, this.regInfo);

                //Step2.1 : prepared  purchaseOrderChildRegister object and put it into purchase_order_child_register
                preparePurchaseOrderChildRegisterList4Save(pod4Save, this.childRegInfo);

                //Step2.2 : prepared  purchaseOrderDeclaration object and put it into purchas_order_declaration
                preparePurchaseOrderDeclarationList4Save(pod4Save, this.declarInfo);

                //Step3 : prepared purchaseOrder object at entry form
                preparePurchaseOrderDataSection4Save(po4Save, pod4Save);

                //CREATE QA TRANS
                po4Save.setQaTransApproval(this.qaTrans4Save);
                if (po4Save.getId() == null) {  // ADD NEW QA TRANS
                    if (po4Save.getQaTransApproval() != null && po4Save.getQaTransApproval().getQaFormId() != null) {
                        getQaTransDAO().create(po4Save.getQaTransApproval());
                    }
                } else {    // EDIT QA TRANS
                    if (po4Save.getQaTransApproval() != null && po4Save.getQaTransApproval().getQaFormId() != null) {
                        if (po4Save.getQaTransApproval().getId() == null) { //A1: In case, not have qa trans approval before
                            getQaTransDAO().create(po4Save.getQaTransApproval());
                            RegistrationTxDAO registrationDAO = new RegistrationTxDAO();
                            registrationDAO.updateNewApprovalQaFormOnPurchaseOrder(po4Save);
                        } else { //A2: In case, qa trans approval is exists in po, so delete all qa trans detail and insert new qa trans detail.
                            getQaTransDetailDAO().deleteQaTransDetails(po4Save.getQaTransApproval().getId());
                            for (QaTransDetail qatd : po4Save.getQaTransApproval().getQaTransDetailCollection()) {
                                getQaTransDetailDAO().create(qatd);
                            }
                        }
                    }

                }

                //Step4 : prepared purchaseOrder object at approval section
                preparePurchaseOrderApprovalSection4Save(po4Save);

                //Step5 : prepared assignement detail and setupQATrans from QAFormController
                po4Save.setAssignmentDetail(new AssignmentDetail());

                AssignmentDetail ad = null;
                if (JSFUtil.getUserSession().getAssignmentDetail() != null) {
                    ad = JSFUtil.getUserSession().getAssignmentDetail();
                } else {
                    this.purchaseOrder.getAssignmentDetail();
                }

                DataTransferUtil.transferAssignmentDetailDataWoCl(po4Save.getAssignmentDetail(), ad);
                FormUtil.setupAssignmentDetailStatusFollowingPOStatus(po4Save);
//          po4Save.setQaTransApproval(this.qaTrans4Save);

                //Check Payment Name
                if (po4Save.getPaymentMethod() != null && !po4Save.getPaymentMethod().equals("")) {
                    PaymentMethod payment4Save = this.getPaymentMethodDAO().findPaymentMethod(Integer.parseInt(po4Save.getPaymentMethod()));
                    po4Save.setPaymentMethodName(payment4Save.getName());
                }

                //Step6 : save
                MxLocalServiceResponse serviceResult = new MxLocalServiceResponse();
                if (po4Save.getId() == null) {
                    try {
                        registrationTxService.insertNewPO(po4Save, serviceResult);
                        purchaseOrder = po4Save;
                        List<PurchaseOrderDetail> pDetails = (List<PurchaseOrderDetail>) purchaseOrder.getPurchaseOrderDetailCollection();
                        purchaseOrderDetail = pDetails.get(0);
                        purchaseOrder.setSaleResult("F".charAt(0));
                        setRegistrationForm();
                    } catch (TransactionSystemException re) {
                        RegistrationLogging.logError("Rollback transaction after error has occured.", null);
                    }
                } else {
                    try {
                        registrationTxService.updatePO(po4Save, serviceResult);
                    } catch (TransactionSystemException re) {
                        RegistrationLogging.logError("Rollback transaction after error has occured.", null);
                    }
                }

                //Step7 : Get result
                boolean sendNotiFlag = false;
                if (MxLocalServiceResponse.MX_LOCAL_SERVICE_STATUS.OK.equals(serviceResult.getServiceStatus())) {
                    //Step7.1 : Manage Success result
                    //PurchaseOrder successPO = (PurchaseOrder)serviceResult.getResult();
                    JSFUtil.getUserSession().setAssignmentDetail(po4Save.getAssignmentDetail());
                    JSFUtil.getUserSession().setPurchaseOrders(po4Save); //set po to usersession
                    JSFUtil.getRequest().setAttribute("purchaseOrder", po4Save);
                    JSFUtil.getRequest().setAttribute("purchaseOrderId", po4Save.getId());
                    JSFUtil.getRequest().setAttribute("productId", this.product.getId());
                    JSFUtil.getRequest().setAttribute("page", "registration");

                    //SAVE REFERENCE YES SALE TO USER SESSION
                    if (po4Save.getSaleResult() == 'Y') {
                        JSFUtil.getUserSession().setRefYes(po4Save.getRefNo());
                    }

                    if (fromSaleApprovalPage) {
                        returnPage = null;
                    } else {
                        JSFUtil.getUserSession().setShowContactSummary(true);
                        returnPage = null;
                    }
                    sendNotiFlag = ('Y' == po4Save.getSaleResult());
                } else {
                    //Step7.2 : Manage error reuslt
                    returnPage = null;
                    message = "Could not save purchase order. [Cause: " + serviceResult.getServiceErrorMessage() + "] ";
                }

                //Step7  : save notification
                try {
                    //if ( sendNotiFlag ) this.saveNotification();
                } catch (Exception ne) {
                    RegistrationLogging.logError("Cannot save notification after insert new po " + ne.getMessage(), ne);
                }

            } catch (Exception e) {
                e.printStackTrace();
                StringBuilder datt = new StringBuilder(RegistrationLogging.getObjectValueToString("Purchase Order", po4Save));
                try {
                    if (po4Save != null) {
                        datt.append(RegistrationLogging.getObjectValueToString("Purchase Detail", po4Save.getPurchaseOrderDetailCollection()));
                        PurchaseOrderDetail podTrack = po4Save.getPurchaseOrderDetailCollection() == null ? null : (PurchaseOrderDetail) po4Save.getPurchaseOrderDetailCollection().toArray()[0];
                        datt.append(RegistrationLogging.getObjectValueToString("Purchase Registration Form", podTrack.getPurchaseOrderRegisterCollection()));
                        datt.append(RegistrationLogging.getObjectValueToString("Approval Log", po4Save.getPurchaseOrderApprovalLogCollection()));
                    }
                } catch (Exception ein) {
                    ein.printStackTrace();
                }
                RegistrationLogging.logError(e.getMessage() + " Save Data [" + datt.toString() + "] ", e);
                returnPage = null;
                message = "Could not save purchase order. [Cause: " + e.getMessage() + "] ";
            } finally {
                RegistrationLogging.debugElapseTime(tc, "Total save purchase order time.");
                RegistrationLogging.debugData("Finished save purchase order , returnPage=" + returnPage + ", message=" + message);

            }
        }
        return null;
    }

    /**
     * ************************************* PRIVATE METHOD
     * **********************************
     */
    /* =============================  4SAVE-METHOD ============================== */
    private boolean validateBeforeSave(PurchaseOrder po) {
        boolean isValid = true;
        boolean incrementalValidate = false; //return back when found an invalid part and do not validate the remain part.
        message = "";
        try {
            boolean isYesSale = po != null && po.getSaleResult() == 'Y';

            //Step1 : check > Is payment method selected?
            if (isYesSale && (po.getPaymentMethod() == null || "".equals(po.getPaymentMethod()))) {
                message += "Payment Method is required. ";
                isValid = false;
                if (incrementalValidate) {
                    return isValid;
                }
            }

            if(isYesSale && discountByPercent > 20) {
                discountMessageDup = "ส่วนลดมูลค่าไม่เกิน 20% ของเบี้ยประกัน";
                isValid = false;
                if (incrementalValidate) {
                    return isValid;
                }
            }

            //Step2 : check > product plan
            if (this.productPlan == null) {
                if (this.productPlanId == null || this.productPlanId == 0) {
                    message += "Please select product plan. ";
                } else {
                    message += "Cannot apply the product. [Cause: Age/Gender of insured person is not in the product's condition.]";
                }
                isValid = false;
                if (incrementalValidate) {
                    return isValid;
                }
            }

//            if(this.regInfo != null){
//                int formNo = 1;
//                boolean isProcess = true;
//                for (RegistrationInfoValue<PurchaseOrderRegister> por : this.regInfo) {
//                    for(FlexFieldInfoValue fx : por.getFlexFields()){
//                        if(fx.getNo() == 50){
//                            if(fx.getPoFlexField().length()>2000){
//                                isProcess = false;
//                                message += "แบบฟอร์มที่ " + formNo + " ฟิลด์ " + fx.getFlexField() + " สามารถรถบันทึกข้อความได้สูงสุด 2000 ตัวอักษร\n";
//                                message += "test";
//                            }
//                        }
//                    }
//                    formNo++;
//                }
//                if(isProcess) {
//                    return isValid = false;
//                }
//            }

            //Step3 : check > year of paiddate
            if (po.getPaidDate() != null) {
                // set baseDate
                Calendar baseDate = Calendar.getInstance(Locale.US); // using now
                if (po.getCreateDate() != null) {
                    baseDate.setTime(po.getCreateDate());  // using create purchase order date
                }                // set paidDate
                Calendar paidCal = Calendar.getInstance(Locale.US);
                paidCal.setTime(po.getPaidDate());
                // cal range
                int elapseYear = paidCal.get(Calendar.YEAR) - baseDate.get(Calendar.YEAR);
                if (Math.abs(elapseYear) > 100) { // assume that paid year is not invalid
                    message += "Paid date is out of range. ";
                    isValid = false;
                    if (incrementalValidate) {
                        return isValid;
                    }
                }
            }

            //Step4: check > thai id no
            for (RegistrationInfoValue<PurchaseOrderRegister> reg : regInfo) {
                if (reg.getPo().getIdcardTypeId() != null && reg.getPo().getIdcardTypeId() == 1) {
                    if (reg.getIdno() != null && reg.isRequireIdno()) {
                        boolean validThaiIdNo = validateThaiIdNo(reg);
                        if (validThaiIdNo) {
                            reg.getPo().setIdno(reg.getCitizenId());
                        } else {
                            isValid = false;
                            reg.setMsgIdno(" Invalid Citizen ID");
                        }
                    }
                } else {
                    reg.getPo().setIdno(reg.getCitizenId());
                }
            }
            if (incrementalValidate && !isValid) {
                return isValid;
            }

            //Step5: check > credit card/bank transfer
            if (isShowCreditCardInfo()) { // in case show credit card field , data must be verified
                boolean validCreditCardNo = validateCardNoBeforeSave();
                if (!validCreditCardNo) {
                    isValid = false;
                }
            } else {
                po.setCardNo(null);
                po.setCardExpiryMonth(null);
                po.setCardExpiryYear(null);
                po.setCardHolderName(null);
                po.setCardType(null);
                po.setCardIssuer(null);

                if (!isShowBankTransferInfo()) {  // in case payment method is not bank transfer, then clear bankAccount.
                    po.setBankAccountNo(null);
                    bankId = null;
                }
            }
            if (incrementalValidate && !isValid) {
                return isValid;
            }
            //Step6:
            ArrayList invalidRemarkList = new ArrayList();
            if (this.qaTrans4Save != null) {
                if (this.qaTrans4Save.getQaTransDetailCollection() != null) {
                    for (QaTransDetail d : this.qaTrans4Save.getQaTransDetailCollection()) {
                        if (d.getRemark() != null && d.getRemark().length() > 500) {
                            isValid = false;
                            invalidRemarkList.add(d.getSeqNo());
                        }
                    }
                    if (invalidRemarkList.size() > 0) {
                        message += "Remark of question " + StringUtils.join(invalidRemarkList, ",") + " must less than 500 characters.";
                    }
                }
            }
        } catch (Exception e) {
            isValid = false;
            message = e.getMessage();
        } finally {
            RegistrationLogging.debugData("Validate purchase order before save : validate result=" + isValid + ", validate message=" + this.message);
        }
        return isValid;
    }

    private void preparePurchaseOrderRegisterList4Save(PurchaseOrderDetail podParent, List<RegistrationInfoValue<PurchaseOrderRegister>> porWrappedList) throws Exception {
        List<PurchaseOrderRegister> porList = new ArrayList<PurchaseOrderRegister>(6);
        int porNumber = 0;
        String textFieldCtlValue = JSFUtil.getBundleValue("textFieldControl");
        try {
            //Take info from each por wrapper to purchaseorderregister object 
            for (RegistrationInfoValue<PurchaseOrderRegister> porw : porWrappedList) {
                PurchaseOrderRegister por = new PurchaseOrderRegister();
                DataTransferUtil.transferPurchaseOrderRegisterData(por, porw.getPo());
                por.setPurchaseOrderDetail(podParent); // set parent of registration
                // set value to por object from porw
                if (porw.getInitialControlType() != null && textFieldCtlValue.equalsIgnoreCase(porw.getInitialControlType())) {
                    por.setInitialLabel(por.getInitial());
                }
                por.setOccupation(FormUtil.isNotExistsIntegerId(porw.getOccupationId()) ? null : new Occupation(porw.getOccupationId()));
                // Current Address Setting
                por.setCurrentDistrict(FormUtil.isNotExistsIntegerId(porw.getCurrentDistrictId()) ? null : new District(porw.getCurrentDistrictId()));
                por.setCurrentSubDistrict(FormUtil.isNotExistsIntegerId(porw.getCurrentSubDistrictId()) ? null : new Subdistrict(porw.getCurrentSubDistrictId()));
                por.setCurrentPostalCode(FormUtil.nullWhenEmpty(porw.getCurrentPostal()));
                // Home Address Setting
                por.setHomeDistrict(FormUtil.isNotExistsIntegerId(porw.getHomeDistrictId()) ? null : new District(porw.getHomeDistrictId()));
                por.setHomeSubDistrict(FormUtil.isNotExistsIntegerId(porw.getHomeSubDistrictId()) ? null : new Subdistrict(porw.getHomeSubDistrictId()));
                por.setHomePostalCode(FormUtil.nullWhenEmpty(porw.getHomePostal()));
                // Billing Address Setting
                por.setBillingDistrict(FormUtil.isNotExistsIntegerId(porw.getBillingDistrictId()) ? null : new District(porw.getBillingDistrictId()));
                por.setBillingSubDistrict(FormUtil.isNotExistsIntegerId(porw.getBillingSubDistrictId()) ? null : new Subdistrict(porw.getBillingSubDistrictId()));
                por.setBillingPostalCode(FormUtil.nullWhenEmpty(porw.getBillingPostal()));
                // Shipping Address Setting
                por.setShippingDistrict(FormUtil.isNotExistsIntegerId(porw.getShippingDistrictId()) ? null : new District(porw.getShippingDistrictId()));
                por.setShippingSubDistrict(FormUtil.isNotExistsIntegerId(porw.getShippingSubDistrictId()) ? null : new Subdistrict(porw.getShippingSubDistrictId()));
                por.setShippingPostalCode(FormUtil.nullWhenEmpty(porw.getShippingPostal()));
                // Flex field Setting
                FormUtil.copyFlexFieldValue2Por(por, porw.getFlexFields());
                // Set register Number
                por.setNo(++porNumber);
                // Add por to List
                porList.add(por);
            }
            podParent.setPurchaseOrderRegisterCollection(porList);
        } finally {
            RegistrationLogging.debugData("Prepared purchase order register list", "purchase order register list", porList);
        }
    }

    private void preparePurchaseOrderChildRegisterList4Save(PurchaseOrderDetail podParent, List<ChildRegInfoValue<PurchaseOrderChildRegister>> childRegList) throws Exception {
        List<PurchaseOrderChildRegister> poChildRegisterList = new ArrayList<PurchaseOrderChildRegister>(childRegInfo.size());
        try {
            //Take info from each por wrapper to PurchaseOrderChildRegister object 
            for (ChildRegInfoValue<PurchaseOrderChildRegister> childReg : childRegList) {
                PurchaseOrderChildRegister poChildRegister = new PurchaseOrderChildRegister();
                DataTransferUtil.transferPurchaseOrderChildRegisterData(poChildRegister, childReg.getPoChildRegister());
                poChildRegister.setPurchaseOrderDetail(podParent);
                poChildRegister.setChildRegForm(childReg.getChildRegForm());
                poChildRegister.setEnable(childReg.getEnable());

                // Flex field Setting
                FormUtil.copyFlexFieldValue2PoChildRegister(poChildRegister, childReg.getFlexFields());

                // Add poChildRegister to List
                poChildRegisterList.add(poChildRegister);
            }
            podParent.setPurchaseOrderChildRegisterCollection(poChildRegisterList);
        } finally {
            RegistrationLogging.debugData("Prepared purchase order child register list", "purchase order child register list", poChildRegisterList);
        }
    }

    private void preparePurchaseOrderDeclarationList4Save(PurchaseOrderDetail podParent, List<DeclarationInfoValue<PurchaseOrderDeclaration>> declarationList) throws Exception {
        List<PurchaseOrderDeclaration> podList = new ArrayList<PurchaseOrderDeclaration>(declarInfo.size());
        try {
            //Take info from each por wrapper to PurchaseOrderDeclaration object 
            for (DeclarationInfoValue<PurchaseOrderDeclaration> declaration : declarationList) {
                PurchaseOrderDeclaration pod = new PurchaseOrderDeclaration();
                DataTransferUtil.transferPurchaseOrderDeclarationData(pod, declaration.getPoDeclaration());
                pod.setPurchaseOrderDetail(podParent);
                pod.setDeclarationForm(declaration.getDeclarationForm());
                pod.setEnable(declaration.getDeclarationEnable());

                // Flex field Setting
                FormUtil.copyFlexFieldValue2Pod(pod, declaration.getFlexFields());

                // Add por to List
                podList.add(pod);
            }
            podParent.setPurchaseOrderDeclarationCollection(podList);
        } finally {
            RegistrationLogging.debugData("Prepared purchase order declaration list", "purchase order declaration list", podList);
        }
    }

    private void preparePurchaseOrderDataSection4Save(PurchaseOrder po, PurchaseOrderDetail pod) throws Exception {
        try {
            //Step1 : Setup data for new po
            if (po.getId() == null) {
                if (!po.getPurchaseOrderDetailCollection().contains(pod)) {
                    po.setPurchaseOrderDetailCollection(new ArrayList<PurchaseOrderDetail>(1));
                    po.getPurchaseOrderDetailCollection().add(pod);
                }
                po.setYesFlag(Boolean.FALSE);
                po.setCreateDate(new Date());
            } else {
                po.setUpdateDate(new Date());
                po.setUpdateBy(getLoginUserName());
                po.setUpdateByUser(getLoginUser());
            }

            if (po.getRefNo() == null || po.getRefNo().isEmpty()) {
                boolean isGenRefNo = FormUtil.isRegProductRefNoGen(this.product);
                Integer seqNo = this.product.getSequenceNo().getId();
                if (isGenRefNo && seqNo != null) {
                    if (poCopyForMain != null && poCopyForMain.getRefNo() != "") {
                        String newRefNo;
                        if (this.product.getSequenceNo().getGenerateType().equalsIgnoreCase("Internal")) {
                            String oldRefNo = poCopyForMain.getRefNo();
                            newRefNo = oldRefNo.substring(0, oldRefNo.length() - 1) + "2";
                        }else{
                            newRefNo = this.nextSequenceService.genRef(seqNo);
                        }
                        po.setRefNo(newRefNo);
                        po.setMainPoId(poCopyForMain.getId());
                        po.setRefNo2("");
                    } else {
                        String newRefNo = this.nextSequenceService.genRef(seqNo);
                        po.setRefNo(newRefNo);
                        po.setRefNo2("");
                    }
                }
            }

            if(productFlexField1.equals("VMI") || productFlexField1.equals("CMI")){
                po.setDiscount(discountByMoney);
                po.setDiscountPercent(discountByPercent);
                po.setDiscountType(discountType);
                po.setChannelCashType(channelCashType);
                po.setChannelCashCode(channelCashCode);
                po.setAgentCashCode(agentCashCode);
            }

            if ('Y' == po.getSaleResult() && po.getPurchaseDate() == null) {
                po.setPurchaseDate(new Date());
            }

            //Step2 : Setup payer information
            List<PurchaseOrderRegister> porList = (ArrayList<PurchaseOrderRegister>) pod.getPurchaseOrderRegisterCollection();
            int payerType = (po.getPayerType() == null) ? 0 : po.getPayerType().intValue();
            switch (payerType) {
                case 1: // main insured
                    FormUtil.setupPayerIsMainInsured(po, this.product);
                    break;
                case 2: // applicant    
                    FormUtil.setupPayerIsApplicant(po, porList);
                    break;
                case 3: // other person
                    FormUtil.setupPayerIsOtherPerson(po, this.payerOccupationId, this.payerSubDistrictId);
                    break;
                default:
                    FormUtil.setupEmptyPayer(po);
            }

            //Step3 : Setup other info
            if (!porList.isEmpty()) {
                int porListSize = porList.size();
                String[] insurePersonFullNameList = new String[porListSize];
                String[] insurePersonIdNoList = new String[porListSize];
                for (int i = 0; i < porListSize; i++) {
                    insurePersonFullNameList[i] = FormUtil.getFullName(porList.get(i).getName(), porList.get(i).getSurname());
                    insurePersonIdNoList[i] = FormUtil.emptyWhenNull(porList.get(i).getIdno());
                }
                po.setInsurePerson(StringUtils.join(insurePersonFullNameList, ','));
                po.setIdno(StringUtils.join(insurePersonIdNoList, ','));
            }

            if (FormUtil.isNotExistsIntegerId(bankId)) {
                po.setBank(null);
            } else {
                Bank bank = bankDAO.findBank(bankId);
                po.setBank((bank == null) ? null : bank);
            }
            if (this.productPlan.getId() != null && this.productPlan.getId() != 0) {
                pod.setProductPlan(productPlan);
                po.setProductPlanCode(productPlan.getCode());
                po.setProductPlanName(productPlan.getName());
            }
            if (porList.size() > 0) {
                po.setPrice(price);
                po.setStampDuty(stampDuty);
                po.setVat(vat);
                po.setNetPremium(netPremium);
                po.setAnnualNetPremium(annualNetPremium);
                po.setAnnualPrice(annualPrice);
                po.setSumInsured(sumInsured);
                po.setTotalAmount(totalPrice);
            }
            try {
                pod.setPaymentMode(paymentModeId);
                pod.setAmount(productPlanDetail.getPrice() * purchaseOrderDetail.getQuantity());
                pod.setUnitPrice(productPlanDetail.getPrice());
            } catch (Exception e) {
                pod.setAmount(new Double(0));
                pod.setUnitPrice(new Double(0));
            }

        } finally {
            RegistrationLogging.debugData("Prepared purchase order at data section ", "purchaseOrder", po);
        }

    }

    private void preparePurchaseOrderApprovalSection4Save(PurchaseOrder po) throws Exception {
        try {
            //QaTrans qaTx = (registrationForm.getQaForm() == null) ? null : (QaTrans) JSFUtil.getRequest().getAttribute("qaTrans"); // mark check , set before ?
            //po.setQaTransApproval(qaTx);

            //Step1 : Setup delivery info
            if (this.deliveryId != null) {
                DeliveryMethod dmObj = deliveryMethodDAO.findDeliveryMethod(new Integer(deliveryId));
                if (dmObj != null) {
                    po.setDeliveryMethod(dmObj);
                }
                po.setDeliveryDate(deliveryDate == null ? null : deliveryDate);
                po.setDeliveryDescription(FormUtil.nullWhenEmpty(this.deliveryDescription));
            }

            //Step2 : setup paid date when it null
            if (po.getTraceNo() != null && !po.getTraceNo().isEmpty() && po.getPaidDate() == null) {
                po.setPaidDate(new Date());
            }

            //Step3 : set  po status 
            stampPurchaseOrderStatus4Save(po);

            //Step4 : run uw approval rule and program will stamp new approvalStatus
            boolean isRunAutomaticUwApprovalRules = false;
            String currentUwApprovalStatus = po.getApprovalStatus();
            if (String.valueOf(po.getSaleResult()).equals("Y")) {
                if (currentUwApprovalStatus != null && !currentUwApprovalStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                    doAutomaticUwApprove(po);
                    isRunAutomaticUwApprovalRules = true;
                }
            }

            //Step5 : setup po latest status and stamp approval log
            this.stampPurchaseOrderLatestStatus4Save(po, isRunAutomaticUwApprovalRules);

        } finally {
            RegistrationLogging.debugData("Prepared purchase order at approval section ", "purchaseOrder", po);
        }

    }

    private void stampPurchaseOrderStatus4Save(PurchaseOrder po) throws Exception {
        try {
            //Set Reason 
            po.setNosaleReason(FormUtil.isNotExistsIntegerId(nosaleReasonId) ? null : new NosaleReason(nosaleReasonId));
            po.setFollowupsaleReason(FormUtil.isNotExistsIntegerId(followupsaleReasonId) ? null : new FollowupsaleReason(followupsaleReasonId));
            //Set role status
            if (po.getApprovalStatus() == null) {
                po.setApprovalStatus(JSFUtil.getBundleValue("approvalWaitingValue"));
            }
            if (po.getPaymentStatus() == null) {
                po.setPaymentStatus(JSFUtil.getBundleValue("approvalWaitingValue"));
            }
            if (po.getQcStatus() == null) {
                po.setQcStatus(JSFUtil.getBundleValue("approvalWaitingValue"));
            }
        } finally {
            RegistrationLogging.debugData("Stamp purchase order status for purchase order id =" + po.getId() + ", New => noSaleReason=" + nosaleReasonId + ", followupsaleReasonId=" + followupsaleReasonId + ", approvalStatus=" + po.getApprovalStatus() + ", paymentStatus=" + po.getPaymentStatus() + ", qcStatus" + po.getQcStatus() + ", saleResult=" + po.getSaleResult() + ", latest dfr=" + po.getLatestDelegateFromRole() + ", latest dtr=" + po.getLatestDelegateToRole() + ", latest status=" + po.getLatestStatus());
        }

    }

    private void stampPurchaseOrderLatestStatus4Save(PurchaseOrder po, boolean isRunAutomaticUwApprovalRules) throws Exception {
        try {
            String userRole = JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase();
            String ldtr = (po.getLatestDelegateToRole() == null) ? "" : po.getLatestDelegateToRole().toUpperCase();
            String ldfr = (po.getLatestDelegateFromRole() == null) ? "" : po.getLatestDelegateFromRole().toUpperCase();

            boolean actorIsAgent = userRole.contains("AGENT");
            boolean sendToAgentRole = "AGENT".equals(ldtr);
            String newApprovalStatus = po.getApprovalStatus();
            // setup latest status incase new
            if (actorIsAgent) { // actor = agent
                if (isRunAutomaticUwApprovalRules) { // there is run automatic uw rules
                    if ("approved".equals(newApprovalStatus)) {
                        po.setLatestDelegateFromRole("Uw");
                        po.setLatestDelegateToRole(nextRoleAfterUwApproved());
                        po.setLatestStatus(newApprovalStatus);
                        po.setLatestStatusBy("System");
                        po.setLatestStatusDate(new Date());
                    } else if ("rejected".equals(newApprovalStatus)) {
                        po.setLatestDelegateFromRole("Uw");
                        po.setLatestDelegateToRole("Uw");
                        po.setLatestStatus(newApprovalStatus);
                        po.setLatestStatusBy("System");
                        po.setLatestStatusDate(new Date());
                    } else if ("waiting".equals(newApprovalStatus)) {
                        po.setLatestDelegateFromRole("Uw");
                        po.setLatestDelegateToRole("Uw");
                        po.setLatestStatus(newApprovalStatus);
                        po.setLatestStatusBy("System");
                        po.setLatestStatusDate(new Date());
                    }
                } else if (sendToAgentRole) {  // po is send to agent for doing something and agent already did something then, po will sent back to sender
                    if ("UW".equals(ldfr)) {
                        po.setApprovalStatus(JSFUtil.getBundleValue("approvalPendingValue"));
                    } else if ("QC".equals(ldfr)) {
                        po.setQcStatus(JSFUtil.getBundleValue("approvalPendingValue"));
                    } else if ("PAYMENT".equals(ldfr)) {
                        po.setPaymentStatus(JSFUtil.getBundleValue("approvalPendingValue"));
                    }
                    String latestDelegateToRole = po.getLatestDelegateFromRole();
                    po.setLatestStatusDate(new Date());
                    po.setLatestDelegateToRole(latestDelegateToRole);
                    po.setLatestDelegateFromRole("Agent");
                    po.setLatestStatusBy(JSFUtil.getUserSession().getUserName());
                    po.setLatestStatusByUser(JSFUtil.getUserSession().getUsers());
                    // create approval log 
                    PurchaseOrderApprovalLog poApprovalLog = new PurchaseOrderApprovalLog();
                    poApprovalLog.setApprovalStatus("Saved Registration");
                    poApprovalLog.setCreateByRole("Agent");
                    poApprovalLog.setToRole(latestDelegateToRole);
                    poApprovalLog.setCreateBy(JSFUtil.getUserSession().getUserName());
                    poApprovalLog.setUsers(JSFUtil.getUserSession().getUsers());
                    poApprovalLog.setCreateDate(new Date());
                    poApprovalLog.setPurchaseOrder(po);
                    if (po.getPurchaseOrderApprovalLogCollection() == null) {
                        po.setPurchaseOrderApprovalLogCollection(new ArrayList<PurchaseOrderApprovalLog>());
                    }
                    po.getPurchaseOrderApprovalLogCollection().add(poApprovalLog);
                }
            }
        } finally {
            RegistrationLogging.debugData("Stamp latest status for purchase order id =" + po.getId() + ", latest status=" + po.getLatestStatus() + ", reason=" + po.getLatestReason() + ", by=" + po.getLatestStatusBy() + ", from role=" + po.getLatestDelegateFromRole() + ", to role=" + po.getLatestDelegateToRole());
        }
    }

    private void doAutomaticUwApprove(PurchaseOrder po) throws Exception {
        try {
            String executeResult = null;
            if (this.product.getApprovalRule() == null) {
                // IF NO APPROVAL RULE CHECK WORKFLOW RULE IN PRODUCT
                // 1. IF WF RULE STEP 1 IS UW
                //      1.1 IF UW RULE IS AUTO APPROVE SET executeResult = approved
                //      1.2 IF UW RULE IS NOT AUTO APPROVE SET executeResult = waiting
                // 2. IF WF RULE STEP 1 IS NOT UW SET executeResult = waiting
                // 3. IF NO WORKFLOW RULE IN PRODUCT SET executeResult = waiting
                List<ProductWorkflow> wfList = productWorkflowDAO.findProductWorkflowByProductId(product.getId());
                if (wfList != null && wfList.size() > 0) {
                    ProductWorkflow wf = wfList.get(0);
                    if (wf.getApprovalRoleName().toUpperCase().equals("UW") && wf.getAutoApprove()) {
                        executeResult = "approved";
                    } else {
                        executeResult = "waiting";
                    }
                } else {
                    executeResult = "waiting";
                }
            } else {
                // IF THERE ARE APPROVAL RULE 
                // 1. GET LIST OF QUESTIONAIR
                Integer productApprovalRuleId = this.product.getApprovalRule().getId();
                List<QaTransDetail> approvalQuestionList = null;
                if (po.getQaTransApproval() != null && registrationForm.getQaForm() != null) {
                    approvalQuestionList = qaTransDetailDAO.findQaTransDetailByQaTransId(po.getQaTransApproval()); //using at setPurchaseOrderStatus
                }

                // 2. CHECK REJECT RULE
                executeResult = FormUtil.autoExecuteRejectApprovalRules(productApprovalRuleId, po, approvalQuestionList);

                // 3. IF NOT REJECT CHECK UW RULE       
                if (executeResult == null || !"rejected".equalsIgnoreCase(executeResult)) {
                    executeResult = FormUtil.autoExecuteUwApprovalRules(productApprovalRuleId, po, approvalQuestionList);
                }

                // 4. IF NO RESULT FROM APPROVAL RULE THEN CHECK WORKFLOW RULE IN PRODUCT
                // 5. IF WF RULE STEP 1 IS UW
                //      5.1 IF UW RULE IS AUTO APPROVE SET executeResult = approved
                //      5.2 IF UW RULE IS NOT AUTO APPROVE SET executeResult = waiting
                // 6. IF WF RULE STEP 1 IS NOT UW SET executeResult = waiting
                // 7. IF NO WORKFLOW RULE IN PRODUCT SET executeResult = waiting
                if (executeResult == null) {
                    List<ProductWorkflow> wfList = productWorkflowDAO.findProductWorkflowByProductId(product.getId());
                    if (wfList != null && wfList.size() > 0) {
                        ProductWorkflow wf = wfList.get(0);
                        if (wf.getApprovalRoleName().toUpperCase().equals("UW") && wf.getAutoApprove()) {
                            executeResult = "approved";
                        } else {
                            executeResult = "waiting";
                        }
                    } else {
                        executeResult = "waiting";
                    }
                }

            }

            // Add New approval log for automatic uw approval  (approved , rejected , waiting)
            PurchaseOrderApprovalLog approvalLog = new PurchaseOrderApprovalLog();
            approvalLog.setApprovalStatus(executeResult);
            approvalLog.setToRole("Uw");
            approvalLog.setCreateByRole("Uw");
            approvalLog.setCreateBy("System");
            approvalLog.setCreateDate(new Date());
            approvalLog.setUsers(JSFUtil.getUserSession().getUsers());
            approvalLog.setPurchaseOrder(po);
            if (po.getPurchaseOrderApprovalLogCollection() == null) {
                po.setPurchaseOrderApprovalLogCollection(new ArrayList<PurchaseOrderApprovalLog>());
            }
            po.getPurchaseOrderApprovalLogCollection().add(approvalLog);

            //stamp approved status
            po.setApprovalStatus(executeResult);
//            if ("approved".equals(executeResult)) {
            po.setApproveBy("System");
            po.setApproveDate(new Date());
//            }
        } finally {
            RegistrationLogging.debugData("Automatic uw approve for purchase order id =" + po.getId() + ", approval status=" + po.getApprovalStatus());
        }
    }


    /* ============================= 4BUILDER/TRANSFORM/CALCULATE/CHECK-METHOD ==================================*/
    private void autoApprovalRule(List<PurchaseOrderRegister> regDetail) {
        if (purchaseOrder != null && String.valueOf(purchaseOrder.getSaleResult()).equals("Y")) {
            if (product.getApprovalRule() != null) {
                List<ApprovalRuleDetail> listUw = approvalRuleDAO.findApprovalRuleDetail(product.getApprovalRule().getId(), "uw");
                List<ApprovalRuleDetail> listReject = approvalRuleDAO.findApprovalRuleDetail(product.getApprovalRule().getId(), "reject");
                if (!listReject.isEmpty()) {
                    this.saveApprovalRule(listReject, regDetail);
                }
                if (!listUw.isEmpty()) {
                    this.saveApprovalRule(listUw, regDetail);
                }
            }
        }
    }

    private String nextRoleAfterUwApproved() throws Exception {
        String nextRole = null;
        List<ProductWorkflow> pdwfList = productWorkflowDAO.findProductWorkflowByProductId(this.product.getId());
        if (pdwfList != null) {
            int i = 0;
            for (; i < pdwfList.size(); i++) {
                ProductWorkflow pdwf = pdwfList.get(i);
                if (pdwf != null && pdwf.getApprovalRoleName() != null && "Uw".equalsIgnoreCase(pdwf.getApprovalRoleName())) {
                    break;
                }
            }
            if (pdwfList.size() >= i + 1) {
                nextRole = pdwfList.get(i + 1).getApprovalRoleName();
            }
        }
        return nextRole;
    }

    private ProductWorkflow getCurrentProductWorkflow() {
        ProductWorkflow currentProductWorkflow = null;
        if (productWorkflowList != null && !productWorkflowList.isEmpty()) {
            for (ProductWorkflow pw : productWorkflowList) {
                if (!this.checkApproveFlow(pw)) {
                    currentProductWorkflow = pw;
                    break;
                }
            }
        }
        return currentProductWorkflow;
    }

    private boolean checkApproveFlow(ProductWorkflow pw) {
        String lastRole = productWorkflowList.get(productWorkflowList.size() - 1).getApprovalRoleName().toUpperCase();
        boolean chk = false;
        if (pw.getApprovalRoleName().toUpperCase().equals("UW")) {
            if (purchaseOrder.getApprovalStatus() != null && purchaseOrder.getApprovalStatus().toUpperCase().equals("APPROVED")) {
                chk = true;
            } else {
                chk = false;
            }
            if (!chk && JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("UW")) {
                disableSaveButton = false;
            } else {
                disableSaveButton = true;
            }
            if (JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("UW") && lastRole.equals("UW")) {
                disableSaveButton = false;
            }
        } else if (pw.getApprovalRoleName().toUpperCase().equals("SUPERVISOR")) {
            if (purchaseOrder.getApprovalSupStatus() != null && purchaseOrder.getApprovalSupStatus().toUpperCase().equals("APPROVED")) {
                chk = true;
            } else {
                chk = false;
            }
            if (!chk && JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("SUPERVISOR")) {
                disableSaveButton = false;
            } else {
                disableSaveButton = true;
            }
            if (JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("SUPERVISOR") && lastRole.equals("SUPERVISOR")) {
                disableSaveButton = false;
            }
        } else if (pw.getApprovalRoleName().toUpperCase().equals("PAYMENT")) {
            if (purchaseOrder.getPaymentStatus() != null && purchaseOrder.getPaymentStatus().toUpperCase().equals("APPROVED")) {
                chk = true;
            } else {
                chk = false;
            }
            if (!chk && JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("PAYMENT")) {
                disableSaveButton = false;
            } else {
                disableSaveButton = true;
            }
            if (JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("PAYMENT") && lastRole.equals("PAYMENT")) {
                disableSaveButton = false;
            }
        } else if (pw.getApprovalRoleName().toUpperCase().equals("QC")) {
            if (purchaseOrder.getQcStatus() != null && purchaseOrder.getQcStatus().toUpperCase().equals("APPROVED")) {
                chk = true;
            } else {
                chk = false;
            }
            if (!chk && JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("QC")) {
                disableSaveButton = false;
            } else {
                disableSaveButton = true;
            }
            if (JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("QC") && lastRole.equals("QC")) {
                disableSaveButton = false;
            }
        }
        return chk;
    }

    private void toWorkflow(ProductWorkflow pw, List<PurchaseOrderRegister> regDetail) {
        /*
        if(pw.getApprovalRoleName().toUpperCase().equals("UW")){
            if(pw.getAutoApprove()){
                autoApprovalRule(regDetail);
            }
        } else if(pw.getApprovalRoleName().toUpperCase().equals("SUPERVISOR")){
            
        } else if(pw.getApprovalRoleName().toUpperCase().equals("PAYMENT")){
            
        } else if(pw.getApprovalRoleName().toUpperCase().equals("QC")){
            
        }
        
        if(pw.getNotify()){
        
        }
        
        if(pw.getSentEmail()){
        
        }*/
    }

    private void checkDelegate() {
        if (JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("UW") && purchaseOrder.getLatestDelegateToRole() != null && purchaseOrder.getLatestDelegateToRole().toUpperCase().equals("UW")) {
            disableSaveButton = false;
        } else if (JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("PAYMENT") && purchaseOrder.getLatestDelegateToRole() != null && purchaseOrder.getLatestDelegateToRole().toUpperCase().equals("PAYMENT")) {
            disableSaveButton = false;
        } else if (JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("QC") && purchaseOrder.getLatestDelegateToRole() != null && purchaseOrder.getLatestDelegateToRole().toUpperCase().equals("QC")) {
            disableSaveButton = false;
        }
    }

    private void checkBtn() {
        String role = JSFUtil.getUserSession().getUsers().getUserGroup().getRole();
        if (role.contains("Agent")) {

            if (purchaseOrder.getApprovalStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                    && purchaseOrder.getQcStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                    && purchaseOrder.getPaymentStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                disableSaveButton = false;
            } else {
                disableSaveButton = true;
            }


            /*
            if (this.purchaseOrder.getId()!=null && this.purchaseOrder.getSaleResult()=='Y') {
                disableSaveButton = false;
            } else {
                disableSaveButton = true;
            }
             */
        } else if (role.contains("Uw")) {
            if (purchaseOrder.getApprovalStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                    && purchaseOrder.getPaymentStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                    && purchaseOrder.getQcStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                disableSaveButton = false;
            } else {
                disableSaveButton = true;
            }
        } else {
            if (role.contains("Payment") && role.contains("Qc")) {
                if (purchaseOrder.getApprovalStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                        && purchaseOrder.getPaymentStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                        && purchaseOrder.getQcStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                    disableSaveButton = false;
                } else {
                    disableSaveButton = true;
                }
            } else if (role.contains("Payment")) {
                if (JSFUtil.getApplication().getSaleApproveFlow() == 1) {  //Payment --> Qc
                    if (purchaseOrder.getApprovalStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                            && !purchaseOrder.getPaymentStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                        disableSaveButton = true;
                    } else {
                        disableSaveButton = false;
                    }
                } else if (JSFUtil.getApplication().getSaleApproveFlow() == 2) { //Qc --> Payment
                    if (purchaseOrder.getApprovalStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                            && purchaseOrder.getQcStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                            && !purchaseOrder.getPaymentStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                        disableSaveButton = true;
                    } else {
                        disableSaveButton = false;
                    }
                }
            } else if (role.contains("Qc")) {
                if (JSFUtil.getApplication().getSaleApproveFlow() == 1) {  //Payment --> Qc
                    if (purchaseOrder.getApprovalStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                            && purchaseOrder.getPaymentStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                            && !purchaseOrder.getQcStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                        disableSaveButton = true;
                    } else {
                        disableSaveButton = false;
                    }
                } else if (JSFUtil.getApplication().getSaleApproveFlow() == 2) { //Qc --> Payment
                    if (purchaseOrder.getApprovalStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                            && !purchaseOrder.getQcStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                        disableSaveButton = true;
                    } else {
                        disableSaveButton = false;
                    }
                }
            }
        }
    }

    private void setPercentValue() {
        int beneficiaryNo = product.getBeneficiaryNo();

        if (beneficiaryNo == 1) {
            percentage1 = 100.00;
            percentage2 = 0.00;
        } else if (beneficiaryNo > 1 && (100 % beneficiaryNo != 0)) {
            percentage1 = Math.ceil(100.00 / beneficiaryNo);
            percentage2 = new Double((100.00 - percentage1) / (beneficiaryNo - 1));
        } else if (beneficiaryNo > 1 && (100 % beneficiaryNo == 0)) {
            percentage1 = 0.00;
            percentage2 = new Double((100.00 - percentage1) / beneficiaryNo);
        }

    }

    private void setFxFieldPercent(PurchaseOrderRegister por) {

        int no = 0;
        Double p = 0.00;
        if (product != null && product.getPercentBeneficiaryFxFieldNo() != null && product.getPercentBeneficiaryFxFieldNo() != 0) {
            no = product.getPercentBeneficiaryFxFieldNo().intValue();
            int beneficiaryNo = product.getBeneficiaryNo();

            if (beneficiaryNo == 1) {
                p = 100.00;
            } else if (beneficiaryNo > 1 && (100 % beneficiaryNo != 0) && por.getNo() == 2) {
                p = percentage1;
            } else if (beneficiaryNo > 1 && (100 % beneficiaryNo == 0) && por.getNo() == 2) {
                p = percentage2;
            } else {
                p = percentage2;
            }
            switch (no) {
                case 1:
                    if (por.getFx1() == null) {
                        por.setFx1(p.toString());
                    }
                    break;
                case 2:
                    if (por.getFx2() == null) {
                        por.setFx2(p.toString());
                    }
                    break;
                case 3:
                    if (por.getFx3() == null) {
                        por.setFx3(p.toString());
                    }
                    break;
                case 4:
                    if (por.getFx4() == null) {
                        por.setFx4(p.toString());
                    }
                    break;
                case 5:
                    if (por.getFx5() == null) {
                        por.setFx5(p.toString());
                    }
                    break;
                case 6:
                    if (por.getFx6() == null) {
                        por.setFx6(p.toString());
                    }
                    break;
                case 7:
                    if (por.getFx7() == null) {
                        por.setFx7(p.toString());
                    }
                    break;
                case 8:
                    if (por.getFx8() == null) {
                        por.setFx8(p.toString());
                    }
                    break;
                case 9:
                    if (por.getFx9() == null) {
                        por.setFx9(p.toString());
                    }
                    break;
                case 10:
                    if (por.getFx10() == null) {
                        por.setFx10(p.toString());
                    }
                    break;
                case 11:
                    if (por.getFx11() == null) {
                        por.setFx11(p.toString());
                    }
                    break;
                case 12:
                    if (por.getFx12() == null) {
                        por.setFx12(p.toString());
                    }
                    break;
                case 13:
                    if (por.getFx13() == null) {
                        por.setFx13(p.toString());
                    }
                    break;
                case 14:
                    if (por.getFx14() == null) {
                        por.setFx14(p.toString());
                    }
                    break;
                case 15:
                    if (por.getFx15() == null) {
                        por.setFx15(p.toString());
                    }
                    break;
                case 16:
                    if (por.getFx16() == null) {
                        por.setFx16(p.toString());
                    }
                    break;
                case 17:
                    if (por.getFx17() == null) {
                        por.setFx17(p.toString());
                    }
                    break;
                case 18:
                    if (por.getFx18() == null) {
                        por.setFx18(p.toString());
                    }
                    break;
                case 19:
                    if (por.getFx19() == null) {
                        por.setFx19(p.toString());
                    }
                    break;
                case 20:
                    if (por.getFx20() == null) {
                        por.setFx20(p.toString());
                    }
                    break;
                case 21:
                    if (por.getFx21() == null) {
                        por.setFx21(p.toString());
                    }
                    break;
                case 22:
                    if (por.getFx22() == null) {
                        por.setFx22(p.toString());
                    }
                    break;
                case 23:
                    if (por.getFx23() == null) {
                        por.setFx23(p.toString());
                    }
                    break;
                case 24:
                    if (por.getFx24() == null) {
                        por.setFx24(p.toString());
                    }
                    break;
                case 25:
                    if (por.getFx25() == null) {
                        por.setFx25(p.toString());
                    }
                    break;
                case 26:
                    if (por.getFx26() == null) {
                        por.setFx26(p.toString());
                    }
                    break;
                case 27:
                    if (por.getFx27() == null) {
                        por.setFx27(p.toString());
                    }
                    break;
                case 28:
                    if (por.getFx28() == null) {
                        por.setFx28(p.toString());
                    }
                    break;
                case 29:
                    if (por.getFx29() == null) {
                        por.setFx29(p.toString());
                    }
                    break;
                case 30:
                    if (por.getFx30() == null) {
                        por.setFx30(p.toString());
                    }
                    break;
                case 31:
                    if (por.getFx31() == null) {
                        por.setFx31(p.toString());
                    }
                    break;
                case 32:
                    if (por.getFx32() == null) {
                        por.setFx32(p.toString());
                    }
                    break;
                case 33:
                    if (por.getFx33() == null) {
                        por.setFx33(p.toString());
                    }
                    break;
                case 34:
                    if (por.getFx34() == null) {
                        por.setFx34(p.toString());
                    }
                    break;
                case 35:
                    if (por.getFx35() == null) {
                        por.setFx35(p.toString());
                    }
                    break;
                case 36:
                    if (por.getFx36() == null) {
                        por.setFx36(p.toString());
                    }
                    break;
                case 37:
                    if (por.getFx37() == null) {
                        por.setFx37(p.toString());
                    }
                    break;
                case 38:
                    if (por.getFx38() == null) {
                        por.setFx38(p.toString());
                    }
                    break;
                case 39:
                    if (por.getFx39() == null) {
                        por.setFx39(p.toString());
                    }
                    break;
                case 40:
                    if (por.getFx40() == null) {
                        por.setFx40(p.toString());
                    }
                    break;
                case 41:
                    if (por.getFx41() == null) {
                        por.setFx41(p.toString());
                    }
                    break;
                case 42:
                    if (por.getFx42() == null) {
                        por.setFx42(p.toString());
                    }
                    break;
                case 43:
                    if (por.getFx43() == null) {
                        por.setFx43(p.toString());
                    }
                    break;
                case 44:
                    if (por.getFx44() == null) {
                        por.setFx44(p.toString());
                    }
                    break;
                case 45:
                    if (por.getFx45() == null) {
                        por.setFx45(p.toString());
                    }
                    break;
                case 46:
                    if (por.getFx46() == null) {
                        por.setFx46(p.toString());
                    }
                    break;
                case 47:
                    if (por.getFx47() == null) {
                        por.setFx47(p.toString());
                    }
                    break;
                case 48:
                    if (por.getFx48() == null) {
                        por.setFx48(p.toString());
                    }
                    break;
                case 49:
                    if (por.getFx49() == null) {
                        por.setFx49(p.toString());
                    }
                    break;
                case 50:
                    if (por.getFx50() == null) {
                        por.setFx50(p.toString());
                    }
                    break;
            }
        }
    }

    private Integer getAge() {
        int age = 0;
        Date dob = null;
        if (!productFlexField1.equals("CMI") && !productFlexField1.equals("VMI")){
            for (RegistrationInfoValue<PurchaseOrderRegister> reg : regInfo) {
                if (reg.getPo().getNo() == 1 && reg.getAge() != 0) {
                    age = reg.getAge();
                }
//            PurchaseOrderRegister por = reg.getPo();
//            if(por.getNo() == 1){
//                dob = por.getDob();
//                break;
//            }
            }
        }
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String dobStr = dateFormat.format(dob);
//
//        age = calculateAge(dobStr);

        return Integer.valueOf(age);
    }

    public String convertThaiDate(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
            return sdf.format(date);
        }

        return null;
    }

    public String convertThaiStrDate(String strDate) {
        if (strDate != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
                return sdf.format(new SimpleDateFormat("yyyy-MM-dd").parse(strDate));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private int calculateAge(String dob) {

        int yearDOB = Integer.parseInt(dob.substring(0, 4));
        int monthDOB = Integer.parseInt(dob.substring(5, 7));
        int dayDOB = Integer.parseInt(dob.substring(8, 10));

        DateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.US);
        java.util.Date date = new java.util.Date();
        int thisYear = Integer.parseInt(dateFormat.format(date));

        dateFormat = new SimpleDateFormat("MM", Locale.US);
        int thisMonth = Integer.parseInt(dateFormat.format(date));

        dateFormat = new SimpleDateFormat("dd", Locale.US);
        int thisDay = Integer.parseInt(dateFormat.format(date));

        int age = thisYear - yearDOB;

        if (thisMonth < monthDOB) {
            age = age - 1;
        }

        //if(thisMonth == monthDOB && thisDay < dayDOB){
        //    age = age - 1;
        //}
        return age;
    }
    
    private void clearProductPlan() {
        if(!productFlexField1.equals("CMI") && !productFlexField1.equals("VMI")) {
            productPlanId = 0;
            productPlan = null;
            productPlanDetail = null;
        }
    }
    
    private void clearPrice() {
        if(!productFlexField1.equals("CMI") && !productFlexField1.equals("VMI")) {
            price = null;
            stampDuty = null;
            vat = null;
            netPremium = null;
            totalPrice = null;
        }
    }

    private String changeCodeToText(String poFlexField, String field, List<RegistrationField> registrationFields) {
        String value = "";
        for (RegistrationField t : registrationFields) {
            if (t.getName().equals(field) && t.getControlType().equals("combobox")) {
                String[] tmp = t.getItems().split(",");
                for (String item : tmp) {
                    String[] text = item.split(":");
                    if (text[0].equals(poFlexField)) {
                        value = text[1];
                        break;
                    }
                }
            }
            if (!value.equals("")) {
                break;
            }
        }
        return value;
    }

    private void saveApprovalRule(List<ApprovalRuleDetail> list, List<PurchaseOrderRegister> regDetail) {
        //List<ApprovalRuleDetail> list = approvalRuleDAO.findApprovalRuleDetail(ar.getId(), "uw");
        Integer age = 0;
        Double w = 0.00;
        Double h = 0.00;
        for (PurchaseOrderRegister por : regDetail) {
            if (por.getNo() == 1) {
                age = por.getAge();
                w = por.getWeight();
                h = por.getHeight();
                break;
            }
        }
        Boolean bUw = false;
        if (list != null && !list.isEmpty()) {
            for (ApprovalRuleDetail ard : list) {
                bUw = false;
                switch (FieldName.valueOf(ard.getFieldName().toUpperCase())) {
                    case AGE:
                        bUw = approvalRuleDAO.checkParam(age, ard.getOperation(), ard.getValue());
                        break;
                    case BMI:
                        Double bmi = FormUtil.computeBmi(w, h);
                        bUw = (bmi != null ? approvalRuleDAO.checkBmi(bmi, ard.getOperation(), ard.getValue()) : false);
                        break;
                    default:
                        Integer no = Integer.parseInt(ard.getFieldName().substring(8));
                        // bUw = checkQuestion(purchaseOrderDetail.getPurchaseOrderQuestionaireCollection(), no, ard.getOperation(), ard.getValue());
                        bUw = registrationForm.getQaForm() != null ? checkQuestion(qaTransDetailList, no, ard.getOperation(), ard.getValue()) : false;
                }
                if (bUw) {
                    if (ard.getApprovalRuleDetailPK().getType().equals("reject")) {
                        purchaseOrder.setApprovalStatus("reject");

                        purchaseOrderApprovalLog = new PurchaseOrderApprovalLog();
                        purchaseOrderApprovalLog.setApprovalStatus("reject");
                        purchaseOrderApprovalLog.setToRole("Uw");
                        //purchaseOrderApprovalLog.setComment("Reject");
                    }

                    if (ard.getApprovalRuleDetailPK().getType().equals("uw") && !purchaseOrder.getApprovalStatus().equals("reject")) {
                        purchaseOrder.setApprovalStatus("waiting");

                        purchaseOrderApprovalLog = new PurchaseOrderApprovalLog();
                        purchaseOrderApprovalLog.setApprovalStatus("waiting");
                        purchaseOrderApprovalLog.setToRole("Uw");
                        //purchaseOrderApprovalLog.setComment("In Underwriting");
                    }
                    break;
                }
            }
        }
        if (!bUw) {
            purchaseOrder.setApprovalStatus("approved");
            purchaseOrder.setApproveBy("System");
            purchaseOrder.setApproveDate(new Date());

            purchaseOrderApprovalLog = new PurchaseOrderApprovalLog();
            purchaseOrderApprovalLog.setApprovalStatus("approved");
            purchaseOrderApprovalLog.setToRole("Uw");
            //purchaseOrderApprovalLog.setComment("Approved");
        }
    }

    private boolean checkQuestion(List<QaTransDetail> qaTransDetailList, Integer no, String operator, String value) {
        Boolean b = false;
        for (QaTransDetail q : qaTransDetailList) {
            if (q.getSeqNo() == no.intValue()) {
                String[] qaAnswer;
                qaAnswer = q.getAnswer().split(",");
                for (int i = 0; i < qaAnswer.length; i++) {
                    b = approvalRuleDAO.checkParam(Integer.parseInt(qaAnswer[i]), operator, value);
                }
                break;
            }
        }
        return b;
    }

    /* ================================ 4VALIDATE-METHOD ==================================*/
    private boolean validateThaiIdNo(RegistrationInfoValue registration) {
        registration.setMsgIdno("");
        String idno = FormUtil.buildThaiIdNo(registration);
        boolean valid = ThaiIDValidator.checkPID(idno);
        if (valid) {
            registration.setCitizenId(idno);
        } else {
            registration.setMsgIdno(" Invalid Citizen ID");
        }
        return valid;
    }

    private boolean validateCardNoBeforeSave() {
        boolean isValid = false;
        messageCardno = "";
        String cardNo = FormUtil.buildCreditCardNo(cardNo1, cardNo2, cardNo3, cardNo4);
        if (cardNo.length() != 16 || cardNo.equals("0000000000000000")) {
            messageCardno = " Invalid Card Number";
            purchaseOrder.setCardIssuer(null);
        } else {
            isValid = CreditCardValidator.luhnCheck(cardNo);
            if (isValid) {
                purchaseOrder.setCardNo(cardNo);
            } else {
                messageCardno = " Invalid Card Number";
                purchaseOrder.setCardType(null);
                purchaseOrder.setCardIssuer(null);
                purchaseOrder.setBank(null);
                bankId = null;
            }
        }
        return isValid;
    }

    private boolean checkQuestion(Collection<PurchaseOrderQuestionaire> list, Integer no, String operator, String value) throws Exception {
        throw new Exception("Not do yet");
        /*Boolean b = false;
        for(PurchaseOrderQuestionaire q : list){
            if(q.getSeqNo() == no.intValue()){
                b = approvalRuleDAO.checkParam(q.getAnswerChoice(), operator, value);
                break;
            }
        }
        return b;*/
    }

    /* ================================ 4MASTER DATA -METHOD =========================== */
    private List getDistrictList1(Integer provinceId, String postalCode) {
        List districtList = null;
        if (provinceId != 0 && provinceId != null) {
            districtList = districtDAO.findDistrictByProvinceId1(provinceId, postalCode);
        }
        return districtList;
    }

    private List getSubDistrictList1(Integer districtId, String postalCode) {
        List subDistrictList = null;
        if (districtId != 0 && districtId != null) {
            subDistrictList = subdistrictDAO.findSubDistrictByDistrictId1(districtId, postalCode);
        }
        return subDistrictList;
    }

    private void setupPaymentMethodList() { // depend on payment mode, product plan
        // initial paymentMethod List
        this.productPaymentMethodList = new ArrayList<PaymentMethod>();
        if (this.paymentModeId != null) {
            if (this.productPaymentMethodCastorList != null) {
                PaymentMethod[] dbpmArray = new PaymentMethod[this.allPaymentMethodList.size()];
                List<PaymentMethodStringCastor> activePmcsList = PaymentModeConverter.findPaymentMethodByDbPaymentModeId(this.productPaymentMethodCastorList, this.paymentModeId);
                for (PaymentMethodStringCastor o : activePmcsList) {
                    for (int i = 0; i < this.allPaymentMethodList.size(); i++) {
                        if (o.getPaymentMethodId().intValue() == this.allPaymentMethodList.get(i).getId().intValue()) {
                            dbpmArray[i] = this.allPaymentMethodList.get(i);
                            break;
                        }
                    }
                }
                int iPoPaymentMethodId = 0;
                boolean clearPaymentMethod = false;
                try {
                    iPoPaymentMethodId = Integer.parseInt(this.purchaseOrder.getPaymentMethod());
                } catch (Exception e) {
                }
                for (PaymentMethod o : dbpmArray) {
                    if (o != null) {
                        this.productPaymentMethodList.add(o);
                        if (iPoPaymentMethodId == o.getId().intValue()) {
                            clearPaymentMethod = true;
                        }
                    }
                }
                if (clearPaymentMethod == false) {
                    this.purchaseOrder.setPaymentMethod(null);
                    this.purchaseOrder.setPaymentMethodName(null);
                    this.paymentMethodId = null;
                }
            }
        }

    }
    
    /*
    @Deprecated
    private void initPaymentModeList() {
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
//                        System.out.println("Invalid Entry!");
                }
                paymentModeList.add(stra);
            }
        }
    }
    */

    private void initPaymentModeList() {
        paymentModeList = new ArrayList<String[]>();
        if (!product.getPaymentMode().isEmpty()) {
            try {
                List<PaymentMethodStringCastor> pmlist = PaymentModeConverter.convertToPaymentModeList(this.product.getPaymentMode());
                LinkedHashMap<String, String> pmmap = PaymentModeConverter.findProductPaymentMode(pmlist);
                for (String pmlabel : pmmap.keySet()) {
                    if (this.purchaseOrder.getMainPoId() != null && product.getFamilyProductType() != null && product.getFamilyProductType() == 2) {
                        if (!pmmap.get(pmlabel).equals(this.paymentModeId)) {
                            continue;
                        }
                    }
                    paymentModeList.add(new String[]{pmmap.get(pmlabel), pmlabel});
                }
            } catch (Exception e) {
                RegistrationLogging.logError("Exception has occured when convert payment mode => " + e.getMessage(), e);
            }
        }
    }

    private void toGetProductInfo() {
        try {
            List<PurchaseOrderDetail> pDetails = (List<PurchaseOrderDetail>) purchaseOrder.getPurchaseOrderDetailCollection();
            purchaseOrderDetail = pDetails.get(0);
            productPlanDetail = purchaseOrderDetail.getProductPlanDetail();
            productPlanId = purchaseOrderDetail.getProductPlan().getId();

            paymentModeId = productPlanDetail.getPaymentMode();
            if (paymentModeId == null) {
                paymentModeId = productPlanDetail.getProductPlan().getPaymentMode();
            }

            if (productPlanDetail != null) {
                if (productPlanDetail.getGender() != null && !productPlanDetail.getGender().equals("")) {
                    gender = productPlanDetail.getGender();
                }
            }
            productPlan = purchaseOrderDetail.getProductPlan();
            product = purchaseOrderDetail.getProduct();
            categoryType = product.getProductCategory().getCategoryType();

            if (purchaseOrder != null) {
                price = purchaseOrder.getPrice();
                stampDuty = purchaseOrder.getStampDuty();
                vat = purchaseOrder.getVat();
                netPremium = purchaseOrder.getNetPremium();
                annualNetPremium = purchaseOrder.getAnnualNetPremium();
                annualPrice = purchaseOrder.getAnnualPrice();
                sumInsured = purchaseOrder.getSumInsured();
                if (purchaseOrder.getPaymentMethod() == null || purchaseOrder.getPaymentMethod().isEmpty()) {
                    totalPrice = 0.0d; // totalPrice will be shown when payment method is exists
                    purchaseOrder.setTotalAmount(0.0d);
                } else {
                    totalPrice = purchaseOrder.getTotalAmount();
                }
            }

            //find delivery method
            if (purchaseOrder != null) {
                try {
                    this.paymentMethodId = new Integer(purchaseOrder.getPaymentMethod());
                } catch (Exception e) {
                }
                deliveryMethod = purchaseOrder.getDeliveryMethod();
                deliveryDate = purchaseOrder.getDeliveryDate();
                deliveryDescription = purchaseOrder.getDeliveryDescription();
                if (deliveryMethod != null) {
                    deliveryId = deliveryMethod.getId();
                }
            }
            String deliveryMethods = product.getDeliveryMethod();
            String[] deliveryMethodString;
            deliveryMethodString = deliveryMethods.split(",");
            String code = "";
            if (deliveryMethods == null) {
                deliveryMethods = "0";
            } else {
                for (int i = 0; i < deliveryMethodString.length; i++) {
                    code += "'" + deliveryMethodString[i] + "',";
                }
                code = code.substring(0, code.length() - 1);
                deliveryMethodList = getDeliveryMethodDAO().findDeliveryMethodByCode(code);
            }
            // initial productMethod
            this.productPaymentMethodCastorList = null;
            try {
                this.productPaymentMethodCastorList = PaymentModeConverter.convertToPaymentModeList(this.product.getPaymentMode());
            } catch (Exception e) {
                RegistrationLogging.logInfo("Exception has occured when convert payment mode (payment mode=" + ((this.product == null) ? null : this.product.getPaymentMode()) + ") => " + e.getMessage());
            }
            this.setupPaymentMethodList();
        } catch (Exception e) {
            RegistrationLogging.logError(e.getMessage(), e);
        }
    }

    private void initPayment() {
        if (purchaseOrder != null && purchaseOrder.getId() != null && purchaseOrder.getNoInstallment() != null && purchaseOrder.getNoInstallment() != 0) {
            poInstallmentList = purchaseOrderDAO.findInstallment(purchaseOrder.getId());
            edit = true;
        }
        initNoPaid();
    }

    private void initNoPaid() {

        if (poInstallmentList != null && !poInstallmentList.isEmpty()) {
            noPaid = 0;
            paidTotal = 0.00;
            for (PurchaseOrderInstallment poi : poInstallmentList) {
                if (poi.getPaymentStatus() != null && poi.getPaymentStatus().equals("paid")) {
                    noPaid++;
                    paidTotal += poi.getInstallmentAmount() != null ? poi.getInstallmentAmount().doubleValue() : 0.00;
                }
            }
        }
        Map<Integer, Integer> list = new ConcurrentSkipListMap<Integer, Integer>();
        for (Integer i = (noPaid + 1); i <= 3; i++) {
            if (i != 0) {
                list.put(i, i);
            }
        }
        noInstallmentList = list;
    }

    private void initOccupationCategoryList() {
        this.occupationCategoryList = this.getOccupationCategoryDAO().findOccupationCategoryEntities();
    }

    private void setPayerOccupationList1(Integer occupationCategoryId) {
        List occupations = occupationDAO.findOccupationByOccupationCategoryId1(occupationCategoryId);
        payerOccupationList = occupations;
    }

    private void setRegistrationForm() {
        regInfo = new ArrayList<RegistrationInfoValue<PurchaseOrderRegister>>();
        List<RegistrationField> registrationFields = (List<RegistrationField>) registrationForm.getRegistrationFieldCollection();
        for (int i = 0; i < 6; i++) {
            try {
                PurchaseOrderRegister tpor = null;
                if (purchaseOrderDetail.getPurchaseOrderRegisterCollection() != null) {
                    for (PurchaseOrderRegister tt : purchaseOrderDetail.getPurchaseOrderRegisterCollection()) {
                        if ((i + 1) == tt.getNo()) {
                            tpor = tt;
                        }
                    }
                } else if (poRegListCopy != null) {
                    for (PurchaseOrderRegister tt : poRegListCopy) {
                        if ((i + 1) == tt.getNo()) {
                            tpor = tt;
                        }
                    }
                }

                if (tpor == null) {
                    tpor = new PurchaseOrderRegister();
                    tpor.setNo(i + 1);

                    //default value from customer profile
                    if (i == 0) {
                        try {
                            CustomerInfoValue cus = JSFUtil.getUserSession().getCustomerDetail();
                            tpor.setInitial(cus.getInitial());
                            tpor.setName(cus.getName());
                            tpor.setSurname(cus.getSurname());
                            /*
                            //set name for card holde name
                            if (purchaseOrder.getCardHolderName() == null || purchaseOrder.getCardHolderName().isEmpty()) {
                                purchaseOrder.setCardHolderName(cus.getName() + " " + cus.getSurname());
                            }
                             */
                            if (this.purchaseOrder.getMainPoId() == null) {
                                tpor.setDob(cus.getDob());
                                tpor.setIdno(cus.getIdno());
                            }
                            /*tpor.setFx11(cus.getFlexfield11());
                            tpor.setFx12(cus.getFlexfield12());
                            tpor.setFx13(cus.getFlexfield13());
                            tpor.setFx14(cus.getFlexfield14());
                            tpor.setFx15(cus.getFlexfield15());
                            tpor.setFx16(cus.getFlexfield16());
                            tpor.setFx17(cus.getFlexfield17());
                            tpor.setFx18(cus.getFlexfield18());
                            tpor.setFx19(cus.getFlexfield19());
                            tpor.setFx20(cus.getFlexfield20());*/
                            //JSFUtil.getUserSession().getCustomerDAO().
                            if (cus.getHomePhoneClose() == null || (cus.getHomePhoneClose() != null && !cus.getHomePhoneClose())) {
                                tpor.setHomePhone(cus.getHomePhone());
                            }
                            if (cus.getOfficePhoneClose() == null || (cus.getOfficePhoneClose() != null && !cus.getOfficePhoneClose())) {
                                tpor.setOfficePhone(cus.getOfficePhone());
                            }
                            if (cus.getMobilePhoneClose() == null || (cus.getMobilePhoneClose() != null && !cus.getMobilePhoneClose())) {
                                tpor.setMobilePhone(cus.getMobilePhone());
                            }
                        } catch (Exception e) {
                            RegistrationLogging.logError("##### PurchaseOrderRegister : " + JSFUtil.getUserSession().getCustomerDetail().getId() + "#####" + e.getMessage(), e);
                        }
                    }
                }

                if (i > 0) {
                    this.setFxFieldPercent(tpor);
                }
                //if(tpor.getCurrentDistrict() == null) tpor.setCurrentDistrict(new District());
                //if(tpor.getHomeDistrict() == null) tpor.setHomeDistrict(new District());
                //if(tpor.getShippingDistrict() == null) tpor.setShippingDistrict(new District());
                //if(tpor.getBillingDistrict() == null) tpor.setBillingDistrict(new District());
                // RegistrationLogging.debugData(" data registrationForm="+registrationForm+", registrationFields="+registrationFields+", group="+(i + 1));

                RegistrationInfoValue<PurchaseOrderRegister> item = new RegistrationInfoValue<PurchaseOrderRegister>(registrationForm, registrationFields, i + 1, districtDAO, subdistrictDAO, occupationDAO, tpor, "registration");
                item.setAgeCalMethod(product.getAgeCalMethod());
                item.setAgeCalMonth(product.getAgeCalMonth());
                item.setAgeCalDay(product.getAgeCalDay());

                if (item.getPo().getDob() == null) {
                    item.getPo().setDob(defaultDob);
                    item.setAge(defaultDob);
                } else {
                    item.setAge(item.getPo().getDob());
                }

                if (item.getPo().getIdcardExpiryDate() == null) {
                    item.getPo().setIdcardExpiryDate(defaultCardExpiryDate);
                }

                //set Citizen ID
                if (item.getPo().getIdcardTypeId() == null || item.getPo().getIdcardTypeId() == 1) {
                    if (item.getPo().getIdno() != null && item.getPo().getIdno().length() == 13) {   //!item.getPo().getIdno().equals("") &&
                        String idno = item.getPo().getIdno();
                        item.setId1(idno.substring(0, 1));
                        item.setId2(idno.substring(1, 5));
                        item.setId3(idno.substring(5, 10));
                        item.setId4(idno.substring(10, 12));
                        item.setId5(idno.substring(12, 13));
                        item.setCitizenId(idno);

                        item.getPo().setIdcardTypeId(1);
                        if (!validateThaiIdNo(item)) {
                            item.setMsgIdno(" Invalid Citizen ID");
                        }
                    }
                } else {
                    item.setCitizenId(item.getPo().getIdno());
                }

                if (i == 0 && item.getPo().getId() == null && item.getPo().getGender() == null) {
                    if (gender != null && !gender.isEmpty() && !gender.equals("A")) {
                        item.getPo().setGender(gender);
                    }
                }

                Map<String, String> addrCopyForCurrent = new LinkedHashMap<String, String>();
                Map<String, String> addrCopyForHome = new LinkedHashMap<String, String>();
                Map<String, String> addrCopyForShipping = new LinkedHashMap<String, String>();
                Map<String, String> addrCopyForBilling = new LinkedHashMap<String, String>();

                addrCopyForCurrent.put("Please Select", "");
                addrCopyForHome.put("Please Select", "");
                addrCopyForShipping.put("Please Select", "");
                addrCopyForBilling.put("Please Select", "");

                if (item.getCurrentAddress() != null) {
                    addrCopyForHome.put(item.getCurrentAddress().equals("") ? "Current Address" : item.getCurrentAddress(), "currentAddress");
                    addrCopyForBilling.put(item.getCurrentAddress().equals("") ? "Current Address" : item.getCurrentAddress(), "currentAddress");
                    addrCopyForShipping.put(item.getCurrentAddress().equals("") ? "Current Address" : item.getCurrentAddress(), "currentAddress");
                }
                if (item.getHomeAddress() != null) {
                    addrCopyForCurrent.put(item.getHomeAddress().equals("") ? "Home Address" : item.getHomeAddress(), "homeAddress");
                    addrCopyForBilling.put(item.getHomeAddress().equals("") ? "Home Address" : item.getHomeAddress(), "homeAddress");
                    addrCopyForShipping.put(item.getHomeAddress().equals("") ? "Home Address" : item.getHomeAddress(), "homeAddress");
                }
                if (item.getBillingAddress() != null) {
                    addrCopyForCurrent.put(item.getBillingAddress().equals("") ? "Billing Address" : item.getBillingAddress(), "billingAddress");
                    addrCopyForHome.put(item.getBillingAddress().equals("") ? "Billing Address" : item.getBillingAddress(), "billingAddress");
                    addrCopyForShipping.put(item.getBillingAddress().equals("") ? "Billing Address" : item.getBillingAddress(), "billingAddress");
                }
                if (item.getShippingAddress() != null) {
                    addrCopyForCurrent.put(item.getShippingAddress().equals("") ? "Shipping Address" : item.getShippingAddress(), "shippingAddress");
                    addrCopyForHome.put(item.getShippingAddress().equals("") ? "Shipping Address" : item.getShippingAddress(), "shippingAddress");
                    addrCopyForBilling.put(item.getShippingAddress().equals("") ? "Shipping Address" : item.getShippingAddress(), "shippingAddress");
                }

                item.setAddrCopyForCurrent(addrCopyForCurrent);
                item.setAddrCopyForHome(addrCopyForHome);
                item.setAddrCopyForShipping(addrCopyForShipping);
                item.setAddrCopyForBilling(addrCopyForBilling);

                // initial address preview from default
                if (item.getBillingAddress() != null) {
                    this.countPreviewBillingAdd3(item);
                    this.countPreviewBillingAdd2(item);
                    this.countPreviewBillingAdd1(item);
                }
                if (item.getShippingAddress() != null) {
                    this.countPreviewShippingAdd3(item);
                    this.countPreviewShippingAdd2(item);
                    this.countPreviewShippingAdd1(item);
                }
                if (item.getHomeAddress() != null) {
                    this.countPreviewHomeAdd3(item);
                    this.countPreviewHomeAdd2(item);
                    this.countPreviewHomeAdd1(item);
                }
                if (item.getCurrentAddress() != null) {
                    this.countPreviewCurrentAdd3(item);
                    this.countPreviewCurrentAdd2(item);
                    this.countPreviewCurrentAdd1(item);
                }

                regInfo.add(item);

                if (i == 0) {
                    payerInitialType = item.getInitialControlType();
                    payerInitialList = item.getInitialList();
                    payerInitial = item.getPo().getInitial();
                }
            } catch (NonexistentEntityException e) {
                //no need recovery this process
            } catch (Exception e) {
                RegistrationLogging.logError(e.getMessage(), e);
            }
        }

        //set card no
        if (purchaseOrder.getCardNo() != null && purchaseOrder.getCardNo().length() == 16) { //!purchaseOrder.getCardNo().equals("") &&
            String cardNo = purchaseOrder.getCardNo();
            cardNo1 = cardNo.substring(0, 4);
            cardNo2 = cardNo.substring(4, 8);
            cardNo3 = cardNo.substring(8, 12);
            cardNo4 = cardNo.substring(12, 16);
        }
        bankId = purchaseOrder.getBank() != null ? purchaseOrder.getBank().getId() : null;
        if (purchaseOrder.getPayerDob() == null) {
            purchaseOrder.setPayerDob(defaultDob);
        }
        if (purchaseOrder.getPayerIdcard() != null && purchaseOrder.getPayerIdcard().length() == 13) {
            String pidno = purchaseOrder.getPayerIdcard();
            pid1 = pidno.substring(0, 1);
            pid2 = pidno.substring(1, 5);
            pid3 = pidno.substring(5, 10);
            pid4 = pidno.substring(10, 12);
            pid5 = pidno.substring(12, 13);
            purchaseOrder.setPayerIdcard(pidno);
        }
        if (purchaseOrder.getPayerSubDistrict() != null) {
            payerSubDistrictId = purchaseOrder.getPayerSubDistrict().getId();
            payerDistrictId = purchaseOrder.getPayerSubDistrict().getDistrict().getId();
            payerProvinceId = purchaseOrder.getPayerSubDistrict().getDistrict().getProvinceId().getId();
            payerPostalCode = purchaseOrder.getPayerSubDistrict().getPostalCode();
            payerDistrictList = getDistrictList1(payerProvinceId, "");
            payerSubDistrictList = getSubDistrictList1(payerDistrictId, "");
        }
        if (purchaseOrder.getPayerOccupation() != null) {
            payerOccupationId = purchaseOrder.getPayerOccupation().getId();
            payerOccupationCategoryId = purchaseOrder.getPayerOccupation().getOccupationCategory().getId();
            this.setPayerOccupationList1(payerOccupationCategoryId);
        }

        if (purchaseOrder.getPayerInitial() != null && !purchaseOrder.getPayerInitial().isEmpty()) {
            purchaseOrder.setPayerInitialText(changeCodeToText(purchaseOrder.getPayerInitial(), "initial", registrationFields));
        }

    }

    private void setChildRegister() {
        childRegInfo = new ArrayList<>();
        int childRegIndex = 0;
        for (int i = 0; i < this.childRegType.size(); i++) {
            String childRegTypeFormList[] = null;
            for(ProductChildRegType productChildRegType : childRegType.get(i).getProductChildRegTypeCollection()){
                if(productChildRegType.getProduct().getId().equals(product.getId())){
                    childRegTypeFormList = productChildRegType.getChildRegFormList().split(",");
                    break;
                }
            }

            List<ChildRegForm> childRegForms = new ArrayList<>();
            for(ChildRegForm crf : (List<ChildRegForm>) this.childRegType.get(i).getChildRegFormCollection()){
                for (String numFrom : childRegTypeFormList) {
                    if (crf.getId().equals(Integer.parseInt(numFrom))) {
                        childRegForms.add(crf);
                        break;
                    }
                }
            }

            for (int j = 0; j < childRegForms.size(); j++) {
                List<ChildRegField> childRegFields = (List<ChildRegField>) childRegForms.get(j).getChildRegFieldCollection();
                try {
                    PurchaseOrderChildRegister poChildRegister = null;
                    if (purchaseOrderDetail.getPurchaseOrderChildRegisterCollection() != null) {
                        for (PurchaseOrderChildRegister p : purchaseOrderDetail.getPurchaseOrderChildRegisterCollection()) {
                            if (childRegForms.get(j).getId().equals(p.getChildRegForm().getId())) {
                                poChildRegister = p;
                                break;
                            }
                        }
                    } else if (poChildRegisterListCopy != null) {
                        for (PurchaseOrderChildRegister p : poChildRegisterListCopy) {
                            if (childRegForms.get(j).getId().equals(p.getChildRegForm().getId())) {
                                poChildRegister = p;
                                break;
                            }
                        }
                    }

                    if (poChildRegister == null) {
                        poChildRegister = new PurchaseOrderChildRegister();
                        poChildRegister.setPurchaseOrderDetail(purchaseOrderDetail);
                        poChildRegister.setChildRegForm(childRegForms.get(j));
                        if (childRegForms.get(j).getRequired()) {
                            poChildRegister.setEnable(true);
                        }else{
                            poChildRegister.setEnable(false);
                        }
                    }
                    childRegIndex++;
                    ChildRegInfoValue<PurchaseOrderChildRegister> item = new ChildRegInfoValue<PurchaseOrderChildRegister>(childRegForms.get(j), childRegFields, childRegIndex, poChildRegister, "child_registration");

                    childRegInfo.add(item);

                } catch (NonexistentEntityException e) {

                } catch (Exception e) {
                    RegistrationLogging.logError(e.getMessage(), e);
                }
            }
        }
    }

    private void setDeclaration() {
        declarInfo = new ArrayList<DeclarationInfoValue<PurchaseOrderDeclaration>>();

        for (int i = 0; i < this.declarationForm.size(); i++) {
            List<DeclarationField> declarationFields = (List<DeclarationField>) this.declarationForm.get(i).getDeclarationFieldCollection();
            try {
                PurchaseOrderDeclaration poDeclaration = null;
                if (purchaseOrderDetail.getPurchaseOrderDeclarationCollection() != null) {
                    for (PurchaseOrderDeclaration p : purchaseOrderDetail.getPurchaseOrderDeclarationCollection()) {
                        if (declarationForm.get(i).getId().equals(p.getDeclarationForm().getId())) {
                            poDeclaration = p;
                            break;
                        }
                    }
                } else if (poDeclarationListCopy != null) {
                    for (PurchaseOrderDeclaration p : poDeclarationListCopy) {
                        if (declarationForm.get(i).getId().equals(p.getDeclarationForm().getId())) {
                            poDeclaration = p;
                            break;
                        }
                    }
                }

                if (poDeclaration == null) {
                    poDeclaration = new PurchaseOrderDeclaration();
                    poDeclaration.setPurchaseOrderDetail(purchaseOrderDetail);
                    poDeclaration.setDeclarationForm(this.declarationForm.get(i));
                    poDeclaration.setEnable(false);
                }

                DeclarationInfoValue<PurchaseOrderDeclaration> item = new DeclarationInfoValue<PurchaseOrderDeclaration>(declarationForm.get(i), declarationFields, i + 1, poDeclaration, "declaration");

                declarInfo.add(item);

            } catch (NonexistentEntityException e) {

            } catch (Exception e) {
                RegistrationLogging.logError(e.getMessage(), e);
            }
        }
    }

    private Map<String, Integer> toProductPlanList(Collection<ProductPlan> param) {
        Map<String, Integer> list = new ConcurrentHashMap<String, Integer>();
        for (ProductPlan pp : param) {
            list.put(pp.getName(), pp.getId());
        }
        return list;
    }

    /* ================================= 4PO's PRICE CALCULATION - METHOD ========================= */
    private void calculate() {
        if (categoryType.equals("life")) {
            calculateNonLife();
        } else if (categoryType.equals("nonlife")) {
            calculateNonLife();
        } else if (categoryType.equals("motor")) {
            calculateMotor();
        } else if (categoryType.equals("motoradvance")) {
            calculateMotorAdvance();
        } else if (categoryType.equals("retail")) {
            calculateRetail();
        }

    }

    private void calculateLife() {
        try {
//            if (product != null) {
            productPlan = productPlanDAO.findProductPlanByIdPaymentMode(productPlanId, paymentModeId);
            productPlanDetail = productPlanDetailDAO.findByAge(productPlan, getAge());
            if (productPlanDetail != null) {
                price = productPlanDetail.getPrice();
                stampDuty = productPlanDetail.getStampDuty();
                vat = productPlanDetail.getVat();
                netPremium = productPlanDetail.getNetPremium();
                annualNetPremium = productPlanDetail.getAnnualNetPremium();
                annualPrice = productPlanDetail.getAnnualPrice();
                sumInsured = productPlan.getSumInsured();
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
//            }
        } catch (Exception e) {
            clearPrice();
        }
    }

    private void calculateLifeNonLife() {
        try {
            productPlanDetail = productPlanDetailDAO.findProductPlanDetail(product, productPlanId, getAge(), gender, paymentModeId);
            productPlan = null;
            if (productPlanDetail != null) {

                paymentModeId = productPlanDetail.getPaymentMode();
                if (paymentModeId == null) {
                    paymentModeId = productPlanDetail.getProductPlan().getPaymentMode();
                }

                purchaseOrderDetail.setProductPlanDetail(productPlanDetail);
                productPlan = productPlanDetail.getProductPlan();
                purchaseOrderDetail.setProductPlan(productPlan);
                this.productPlanId = this.productPlan.getId(); // reset product plan id

                price = productPlanDetail.getPrice();
                stampDuty = productPlanDetail.getStampDuty();
                vat = productPlanDetail.getVat();
                netPremium = productPlanDetail.getNetPremium();
                annualNetPremium = productPlanDetail.getAnnualNetPremium();
                annualPrice = productPlanDetail.getAnnualPrice();
                sumInsured = productPlan.getSumInsured();
                totalPrice = 0.0;

                coverageInfoList = new ArrayList<CoverageInfo>();

                Integer countNumberOfCoverage = 0;
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
                if (productPlanDetail.getCode() != null) {
                    productPlanDetailInfo.setPlan_detail_code(productPlanDetail.getCode());
                } else {
                    productPlanDetailInfo.setPlan_detail_code("");
                }
                productPlanDetailInfo.setNumber_of_coverage(countNumberOfCoverage.toString());
                productPlanDetailInfo.setCoverage_info(coverageInfoList);
                productPlanDetailInfoList.add(productPlanDetailInfo);

                productPlanDetailInfoJson = gson.toJson(productPlanDetailInfoList);
                productPlanDetailInfoJson = productPlanDetailInfoJson.substring(1, productPlanDetailInfoJson.length() - 1);

                calculateTotalPriceOfLifeNonLife();
            } else {
                clearPrice();
                clearProductPlan(); // clear product plan id for showing at screen
                //if(String.valueOf(purchaseOrder.getSaleResult()).equals("Y")){
                //    purchaseOrder.setSaleResult("N".charAt(0));
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
            clearPrice();      
            clearProductPlan(); // clear product plan id for showing at screen
            //if(String.valueOf(purchaseOrder.getSaleResult()).equals("Y")){
            //    purchaseOrder.setSaleResult("N".charAt(0));
            //}
        }
    }
    
    public Integer convertStringToInt(String data){
        int number = 0;
        if(!data.isEmpty()){
            number = Integer.parseInt(data);
        }
        return number;
    }

    private void calculateTotalPriceOfLifeNonLife() {
        totalPrice = 0.0d;

        if (this.paymentModeId != null && !this.paymentModeId.isEmpty() && paymentMethodId != null) {
            // find firstInstallmentNo
            int noOfFirstInstallment = 0;
            try {
                if (this.productPaymentMethodCastorList == null) {
                    throw new Exception("Old Payment Method Version => change calculation to old version ");
                }
                for (PaymentMethodStringCastor o : this.productPaymentMethodCastorList) {
                    if (o.getPaymentMethodId().intValue() == paymentMethodId) {
                        noOfFirstInstallment = o.getNoPayPeriod(this.paymentModeId);
                        purchaseOrderDetail.setMonthlyFirstPayment(new Integer(noOfFirstInstallment));
                        break;
                    }
                }
            } catch (Exception e) {
                RegistrationLogging.logError("Exception has occured when convert payment mode => " + e.getMessage(), e);

                // using old fashion of applicaiton
                if (paymentModeId.equals("1")) {
                    noOfFirstInstallment = product.getMonthlyFirstPayment();
                } else if (paymentModeId.equals("2")) {
                    noOfFirstInstallment = product.getMonthlyFirstPayment();
                } else {
                    noOfFirstInstallment = 1;
                }
            }
            if (noOfFirstInstallment > 0) { // do not calculate price when no of firstinstallment = 0
                this.totalPrice = this.netPremium * noOfFirstInstallment;
            }
        }
    }

    private void calculateNonLife() {
        calculateLifeNonLife();
    }

    private void calculateMotor() {
        sum1 = 0.00;
        purchaseOrderDetail.setPaymentMode("4");
        purchaseOrder.setAmountBfVat(new Double(0.00));
        purchaseOrder.setTotalAmount(new Double(0.00));
        if (purchaseOrder.getAmount2() != 0 && purchaseOrder.getAmount2() != null) {
            checkAct = true;
        }
        if (purchaseOrder.getDiscount() != 0 && purchaseOrder.getDiscount() != null) {
            checkDiscount = true;
        }
        sum1 += purchaseOrder.getAmount1();
        if (checkAct) {
            sum1 += purchaseOrder.getAmount2();
        }

        if (purchaseOrder.getFirstDamage() != null && purchaseOrder.getFirstDamage() != 0) {
            sum1 = sum1 - purchaseOrder.getFirstDamage();
        }

        if (purchaseOrder.getDiscountGroup() != null && purchaseOrder.getDiscountGroup() != 0) {
            sum1 = sum1 - purchaseOrder.getDiscountGroup();
        }

        if (purchaseOrder.getDiscountGoodRecord() != null && purchaseOrder.getDiscountGoodRecord() != 0) {
            sum1 = sum1 - purchaseOrder.getDiscountGoodRecord();
        }

        if (checkDiscount) {
            purchaseOrder.setTotalAmount(sum1 - purchaseOrder.getDiscount());
        } else {
            purchaseOrder.setTotalAmount(sum1);
        }

        purchaseOrder.setAmountBfVat((purchaseOrder.getTotalAmount() / (new Double(1.07428))));
        totalPrice = purchaseOrder.getTotalAmount();
    }

    private void calculateMotorAdvance() {
        try {
//            if (product != null) {
            productPlan = productPlanDAO.findProductPlanByIdPaymentMode(productPlanId, paymentModeId);
            productPlanDetail = productPlanDetailDAO.findByAge(productPlan, getAge());
            if (productPlanDetail != null) {
                price = productPlanDetail.getPrice();
                stampDuty = productPlanDetail.getStampDuty();
                vat = productPlanDetail.getVat();
                netPremium = productPlanDetail.getNetPremium();
                totalPrice = (netPremium);
            } else {
                clearPrice();
            }
//            }
        } catch (Exception e) {
            clearPrice();
        }
    }

    @Deprecated
    private void calculatePayment() {
        Double a = new Double(0);
        Double s1 = new Double(0);
        if (purchaseOrder.getTotalAmount() != 0 && purchaseOrder.getTotalAmount() != null) {
            a = Double.parseDouble(Math.round(purchaseOrder.getTotalAmount() / purchaseOrder.getNoInstallment()) + "");
        }
        for (PurchaseOrderInstallment poi : poInstallmentList) {
            s1 += a;
            if (poInstallmentList.size() == poi.getInstallmentNo()) {
                poi.setInstallmentAmount(BigDecimal.valueOf(purchaseOrder.getTotalAmount() - s1));
            } else {
                poi.setInstallmentAmount(BigDecimal.valueOf(a));
            }
        }
    }

    private void calculateRetail() {

    }

    private void calculatePayment2() {

        List<PurchaseOrderInstallment> tmpList = new ArrayList<PurchaseOrderInstallment>();
        initNoPaid();
        Integer newNo = purchaseOrder.getNoInstallment();
        if (newNo != 0) {
            Double a = new Double(0);
            Double s1 = new Double(0);
            //initNoPaid();
            if (!edit) {
                if (purchaseOrder.getTotalAmount() != 0 && purchaseOrder.getTotalAmount() != null) {
                    a = Double.parseDouble(Math.round(purchaseOrder.getTotalAmount() / newNo) + "");
                }
                poInstallmentList = new ArrayList<PurchaseOrderInstallment>();
                for (int i = 1; i <= newNo.intValue(); i++) {
                    PurchaseOrderInstallment poi = new PurchaseOrderInstallment();
                    poi.setInstallmentNo(i);
                    poi.setPaymentStatus("notpaid");
                    if (i < newNo.intValue()) {
                        s1 += a;
                        poi.setInstallmentAmount(BigDecimal.valueOf(a));
                    } else {
                        poi.setInstallmentAmount(BigDecimal.valueOf(purchaseOrder.getTotalAmount() - s1));
                        if (poi.getDueDate() == null) {
                            poi.setDueDate(purchaseOrder.getDueDate());
                        }
                    }
                    poInstallmentList.add(poi);
                }
            } else {
                if (poInstallmentList.size() < newNo.intValue()) {
                    for (PurchaseOrderInstallment poi : poInstallmentList) {
                        //if(poi.getPaymentStatus().equals("paid")){
                        s1 += poi.getInstallmentAmount() != null ? poi.getInstallmentAmount().doubleValue() : 0.00;
                        //} else {
                        //    s1 += a;
                        //    poi.setInstallmentAmount(BigDecimal.valueOf(a));
                        //}
                        tmpList.add(poi);
                    }
                    for (int i = (poInstallmentList.size() + 1); i <= newNo.intValue(); i++) {
                        PurchaseOrderInstallment poi = new PurchaseOrderInstallment();
                        poi.setInstallmentNo(i);
                        poi.setPaymentStatus("notpaid");
                        if (i < newNo.intValue()) {
                            //    s1 += a;
                            poi.setInstallmentAmount(BigDecimal.valueOf(0));
                            tmpList.add(poi);
                        } else if (i == newNo.intValue()) {
                            poi.setInstallmentAmount(BigDecimal.valueOf(purchaseOrder.getTotalAmount() - s1));
                            tmpList.add(poi);
                            break;
                        }
                    }
                } else {
                    for (PurchaseOrderInstallment poi : poInstallmentList) {
                        if (poi.getInstallmentNo() < newNo.intValue()) {
                            //if(poi.getPaymentStatus().equals("paid")){
                            s1 += poi.getInstallmentAmount() != null ? poi.getInstallmentAmount().doubleValue() : 0.00;
                            //} else {
                            //    s1 += a;
                            //    poi.setInstallmentAmount(BigDecimal.valueOf(a));
                            //}
                            tmpList.add(poi);
                        } else if (poi.getInstallmentNo() == newNo.intValue()) {
                            //if(!poi.getPaymentStatus().equals("paid")){
                            poi.setInstallmentAmount(BigDecimal.valueOf(purchaseOrder.getTotalAmount() - s1));
                            //}                            
                            tmpList.add(poi);
                            break;
                        }
                    }
                }
                poInstallmentList = tmpList;
            }
        }
    }

    /**
     * ******************** PERMISSION METHOD ***********************
     */
    public boolean isRegistrationFormEditPermitted() {
        return SecurityUtil.isPermitted("registration:form:edit");
    }

    public boolean isEditRegisFormPermitted() {
        if (JSFUtil.getUserSession().getUsers().getUserGroup().getRole().equals("Agent")) {
            return true;
        } else {
            return SecurityUtil.isPermitted("admin:salesapproval:edit");
        }
    }

    /**
     * ******************** ADDRESS METHOD ***************************
     */
    public void countPreviewHomeAdd1(RegistrationInfoValue registration) {
        registration.setPreviewHomeAdd1("");
        if (registration.getPo() instanceof PurchaseOrderRegister && registration.getPo() != null) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();
            String data = this.buildAddress1(por.getHomeAddressLine1(), por.getHomeAddressLine2(), por.getHomeAddressLine3(), por.getHomeAddressLine4());
            registration.setPreviewHomeAdd1(data);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void countPreviewHomeAdd2(RegistrationInfoValue registration) {
        registration.setPreviewHomeAdd2("");
        if (registration.getPo() instanceof PurchaseOrderRegister) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();
            String data = this.buildAddress2(por.getHomeAddressLine5(), registration.getHomeSubDistrictId(), registration.getHomeDistrictId());
            registration.setPreviewHomeAdd2(data);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void countPreviewHomeAdd3(RegistrationInfoValue registration) {
        registration.setPreviewHomeAdd3("");
        registration.setPreviewHomeAdd2("");
        if (registration.getPo() instanceof PurchaseOrderRegister) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();
            String data2 = this.buildAddress2(por.getHomeAddressLine5(), null, null);
            String data = this.buildAddress3(registration.getHomeProvinceId());
            registration.setPreviewHomeAdd3(data);
            registration.setPreviewHomeAdd2(data2);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void countPreviewCurrentAdd1(RegistrationInfoValue registration) {
        registration.setPreviewCurrentAdd1("");
        if (registration.getPo() instanceof PurchaseOrderRegister && registration.getPo() != null) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();
            String data = this.buildAddress1(por.getCurrentAddressLine1(), por.getCurrentAddressLine2(), por.getCurrentAddressLine3(), por.getCurrentAddressLine4());
            registration.setPreviewCurrentAdd1(data);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void countPreviewCurrentAdd2(RegistrationInfoValue registration) {
        registration.setPreviewCurrentAdd2("");
        if (registration.getPo() instanceof PurchaseOrderRegister) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();
            String data = this.buildAddress2(por.getCurrentAddressLine5(), registration.getCurrentSubDistrictId(), registration.getCurrentDistrictId());
            registration.setPreviewCurrentAdd2(data);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void countPreviewCurrentAdd3(RegistrationInfoValue registration) {
        registration.setPreviewCurrentAdd3("");
        registration.setPreviewCurrentAdd2(""); //reset address2
        if (registration.getPo() instanceof PurchaseOrderRegister) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();
            String data2 = this.buildAddress2(por.getCurrentAddressLine5(), null, null);
            String data = this.buildAddress3(registration.getCurrentProvinceId());
            registration.setPreviewCurrentAdd3(data);
            registration.setPreviewCurrentAdd2(data2);

        }

        FacesContext.getCurrentInstance().renderResponse();
    }

    public void countPreviewShippingAdd1(RegistrationInfoValue registration) {
        registration.setPreviewShippingAdd1("");
        if (registration.getPo() instanceof PurchaseOrderRegister && registration.getPo() != null) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();
            String data = this.buildAddress1(por.getShippingAddressLine1(), por.getShippingAddressLine2(), por.getShippingAddressLine3(), por.getShippingAddressLine4());
            registration.setPreviewShippingAdd1(data);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void countPreviewShippingAdd2(RegistrationInfoValue registration) {
        registration.setPreviewShippingAdd2("");
        if (registration.getPo() instanceof PurchaseOrderRegister) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();
            String data = this.buildAddress2(por.getShippingAddressLine5(), registration.getShippingSubDistrictId(), registration.getShippingDistrictId());
            registration.setPreviewShippingAdd2(data);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void countPreviewShippingAdd3(RegistrationInfoValue registration) {
        registration.setPreviewShippingAdd3("");
        registration.setPreviewShippingAdd2(""); //reset address2
        if (registration.getPo() instanceof PurchaseOrderRegister) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();
            String data2 = this.buildAddress2(por.getShippingAddressLine5(), null, null);
            String data = this.buildAddress3(registration.getShippingProvinceId());
            registration.setPreviewShippingAdd3(data);
            registration.setPreviewShippingAdd2(data2);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void countPreviewBillingAdd1(RegistrationInfoValue registration) {
        registration.setPreviewBillingAdd1("");
        if (registration.getPo() instanceof PurchaseOrderRegister && registration.getPo() != null) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();
            String data = this.buildAddress1(por.getBillingAddressLine1(), por.getBillingAddressLine2(), por.getBillingAddressLine3(), por.getBillingAddressLine4());
            registration.setPreviewBillingAdd1(data);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void countPreviewBillingAdd2(RegistrationInfoValue registration) {
        registration.setPreviewBillingAdd2("");
        if (registration.getPo() instanceof PurchaseOrderRegister) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();
            String data = this.buildAddress2(por.getBillingAddressLine5(), registration.getBillingSubDistrictId(), registration.getBillingDistrictId());
            registration.setPreviewBillingAdd2(data);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void countPreviewBillingAdd3(RegistrationInfoValue registration) {
        registration.setPreviewBillingAdd3("");
        registration.setPreviewBillingAdd2(""); //reset address2
        if (registration.getPo() instanceof PurchaseOrderRegister) {
            PurchaseOrderRegister por = (PurchaseOrderRegister) registration.getPo();
            String data2 = this.buildAddress2(por.getBillingAddressLine5(), null, null);
            String data = this.buildAddress3(registration.getBillingProvinceId());
            registration.setPreviewBillingAdd3(data);
            registration.setPreviewBillingAdd2(data2);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    private String buildAddress1(String line1, String line2, String line3, String line4) {
        StringBuilder result = new StringBuilder("");
        if (line1 != null && !line1.isEmpty()) {
            result.append(line1);
        }
        if (line2 != null && !line2.isEmpty()) {
            result.append(" ").append(line2);
        }
        if (line3 != null && !line3.isEmpty()) {
            result.append(" หมู่").append(line3);
        }
        if (line4 != null && !line4.isEmpty()) {
            result.append(" ซ.").append(line4);
        }
        return result.toString();
    }

    private String buildAddress2(String line5, Integer subDistrictId, Integer districtId) {
        StringBuilder result = new StringBuilder("");
        if (line5 != null && !line5.isEmpty()) {
            result.append(" ถ.").append(line5);
        }
        if (subDistrictId != null) {
            Subdistrict o = subdistrictDAO.findSubDistrict(subDistrictId);
            if (o != null && o.getName() != null) {
                result.append(" ").append(o.getName().replace("ตำบล", "ต."));
            }
        }
        if (districtId != null) {
            District o = districtDAO.findDistrict(districtId);
            if (o != null && o.getName() != null) {
                result.append(" ").append(o.getName().replace("อำเภอ", "อ."));
            }
        }
        return result.toString();
    }

    private String buildAddress3(Integer provinceId) {
        StringBuilder result = new StringBuilder("");
        if (provinceId != null) {
            Province o = provinceDAO.findProvince(provinceId);
            if (o != null && o.getName() != null) {
                if ("กรุงเทพมหานคร".equalsIgnoreCase(o.getName())) {
                    result.append("กรุงเทพฯ");
                } else {
                    result.append("จ.").append(o.getName());
                }
            }
        }
        return result.toString();
    }

    /**
     * ******************** GETTER-SETTER METHOD ********************
     */
    public void retrieveCreditCardUsage() {
        this.cardNo = "";
        String cardNo = FormUtil.buildCreditCardNo(cardNo1, cardNo2, cardNo3, cardNo4);
        this.cardNo = cardNo;
        if (cardNo != null && cardNo.length() == 16) {
            List<Object[]> list = purchaseOrderDAO.findCreditCardUsageByCardNo(cardNo);
            usageList = new ArrayList<CreditCardUsageValue>();
            String custInt = "", custName = "", custSur = "", insInt = "", insName = "", insSur = "";
            String Cust = "", Ins = "";
            if (list != null && list.size() > 0) {
                for (Object[] obj : list) {
                    Cust = "";
                    Ins = "";
                    custInt = (String.valueOf(obj[0]) == null || (String.valueOf(obj[0]) != null && String.valueOf(obj[0]).isEmpty()) || (String.valueOf(obj[0]) != null && String.valueOf(obj[0]).equalsIgnoreCase("null"))) ? "" : String.valueOf(obj[0]);
                    custName = (String.valueOf(obj[1]) == null || (String.valueOf(obj[1]) != null && String.valueOf(obj[1]).isEmpty())) ? "" : String.valueOf(obj[1]).replaceAll("\\s+", "");
                    custSur = (String.valueOf(obj[2]) == null || (String.valueOf(obj[2]) != null && String.valueOf(obj[2]).isEmpty())) ? "" : String.valueOf(obj[2]).replaceAll("\\s+", "");
                    insInt = (String.valueOf(obj[3]) == null || (String.valueOf(obj[3]) != null && String.valueOf(obj[3]).isEmpty()) || (String.valueOf(obj[3]) != null && String.valueOf(obj[3]).equalsIgnoreCase("null"))) ? "" : String.valueOf(obj[3]);
                    insName = (String.valueOf(obj[4]) == null || (String.valueOf(obj[4]) != null && String.valueOf(obj[4]).isEmpty())) ? "" : String.valueOf(obj[4]).replaceAll("\\s+", "");
                    insSur = (String.valueOf(obj[5]) == null || (String.valueOf(obj[5]) != null && String.valueOf(obj[5]).isEmpty())) ? "" : String.valueOf(obj[5]).replaceAll("\\s+", "");
                    Cust += (custInt == null || (custInt != null && custInt.isEmpty())) ? "" : custInt;
                    Cust += (custName == null || (custName != null && custName.isEmpty())) ? "" : custName + " ";
                    Cust += (custSur == null || (custSur != null && custSur.isEmpty())) ? "" : custSur;
                    Ins += (insInt == null || (insInt != null && insInt.isEmpty())) ? "" : insInt;
                    Ins += (insName == null || (insName != null && insName.isEmpty())) ? "" : insName + " ";
                    Ins += (insSur == null || (insSur != null && insSur.isEmpty())) ? "" : insSur;
                    CreditCardUsageValue value = null;
                    if (String.valueOf(obj[6]) != null && !String.valueOf(obj[6]).isEmpty() && !(String.valueOf(obj[6]) != null && String.valueOf(obj[6]).equalsIgnoreCase("null"))) {
                        String a = obj[6].toString();
                        Date d = (Date) obj[6];
                        value = new CreditCardUsageValue(Cust, Ins, (Date) obj[6], String.valueOf(obj[7]));
                    } else {
                        value = new CreditCardUsageValue(Cust, Ins, null, String.valueOf(obj[7]));
                    }

                    usageList.add(value);
                }
            }
        }
    }

    /**
     * ********************* GETTER-SETTER METHOD ********************
     */
    public RegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    public void setRegistrationForm(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }

    public List<ChildRegForm> getChildRegForm() {
        return childRegForm;
    }

    public void setChildRegForm(List<ChildRegForm> childRegForm) {
        this.childRegForm = childRegForm;
    }

    public List<DeclarationForm> getDeclarationForm() {
        return declarationForm;
    }

    public void setDeclarationForm(List<DeclarationForm> declarationForm) {
        this.declarationForm = declarationForm;
    }

    public List<RegistrationInfoValue<PurchaseOrderRegister>> getRegInfo() {
        return regInfo;
    }

    public List<ChildRegInfoValue<PurchaseOrderChildRegister>> getChildRegInfo() {
        return childRegInfo;
    }

    public List<DeclarationInfoValue<PurchaseOrderDeclaration>> getDeclarInfo() {
        return declarInfo;
    }

    public List<Object> getProductPlanList() {
        return productPlanList;
    }

    public void setProductPlanList(List<Object> productPlanList) {
        this.productPlanList = productPlanList;
    }

    public List<NosaleReason> getNosaleReasonList() {
        return nosaleReasonList;
    }

    public List<FollowupsaleReason> getFollowupsaleReasonList() {
        return followupsaleReasonList;
    }

    public List<String[]> getPaymentModeList() {
        return paymentModeList;
    }

    public List getProvinceList() {
        return provinceList;
    }

    public Boolean getDisableSaveButton() {
        return disableSaveButton;
    }

    public void setDisableSaveButton(Boolean disableSaveButton) {
        this.disableSaveButton = disableSaveButton;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Integer getNosaleReasonId() {
        return nosaleReasonId;
    }

    public void setNosaleReasonId(Integer nosaleReasonId) {
        this.nosaleReasonId = nosaleReasonId;
    }

    public Integer getFollowupsaleReasonId() {
        return followupsaleReasonId;
    }

    public void setFollowupsaleReasonId(Integer followupsaleReasonId) {
        this.followupsaleReasonId = followupsaleReasonId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getProductPlanId() {
        return productPlanId;
    }

    public void setProductPlanId(Integer productPlanId) {
        this.productPlanId = productPlanId;
    }

    //Managed Properties
    public RegistrationFormDAO getRegistrationFormDAO() {
        return registrationFormDAO;
    }

    public void setRegistrationFormDAO(RegistrationFormDAO registrationFormDAO) {
        this.registrationFormDAO = registrationFormDAO;
    }

    public ChildRegTypeDAO getChildRegTypeDAO() {
        return childRegTypeDAO;
    }

    public void setChildRegTypeDAO(ChildRegTypeDAO childRegTypeDAO) {
        this.childRegTypeDAO = childRegTypeDAO;
    }

    public ChildRegFormDAO getChildRegFormDAO() {
        return childRegFormDAO;
    }

    public void setChildRegFormDAO(ChildRegFormDAO childRegFormDAO) {
        this.childRegFormDAO = childRegFormDAO;
    }

    public DeclarationFormDAO getDeclarationFormDAO() {
        return declarationFormDAO;
    }

    public void setDeclarationFormDAO(DeclarationFormDAO declarationFormDAO) {
        this.declarationFormDAO = declarationFormDAO;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public DistrictDAO getDistrictDAO() {
        return districtDAO;
    }

    public void setDistrictDAO(DistrictDAO districtDAO) {
        this.districtDAO = districtDAO;
    }

    public ProvinceDAO getProvinceDAO() {
        return provinceDAO;
    }

    public void setProvinceDAO(ProvinceDAO provinceDAO) {
        this.provinceDAO = provinceDAO;
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

    public FollowupsaleReasonDAO getFollowupsaleReasonDAO() {
        return followupsaleReasonDAO;
    }

    public void setFollowupsaleReasonDAO(FollowupsaleReasonDAO followupsaleReasonDAO) {
        this.followupsaleReasonDAO = followupsaleReasonDAO;
    }

    public NosaleReasonDAO getNosaleReasonDAO() {
        return nosaleReasonDAO;
    }

    public void setNosaleReasonDAO(NosaleReasonDAO nosaleReasonDAO) {
        this.nosaleReasonDAO = nosaleReasonDAO;
    }

    public Integer getPoId() {
        return poId;
    }

    public void setPoId(Integer poId) {
        this.poId = poId;
    }

    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }

    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }

    public PurchaseOrderDetail getPurchaseOrderDetail() {
        return purchaseOrderDetail;
    }

    public void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
        this.purchaseOrderDetail = purchaseOrderDetail;
    }

    public Double getNetPremium() {
        return netPremium;
    }

    public void setNetPremium(Double netPremium) {
        this.netPremium = netPremium;
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public ProductPlanDAO getProductPlanDAO() {
        return productPlanDAO;
    }

    public void setProductPlanDAO(ProductPlanDAO productPlanDAO) {
        this.productPlanDAO = productPlanDAO;
    }

    public ProductPlanDetailDAO getProductPlanDetailDAO() {
        return productPlanDetailDAO;
    }

    public void setProductPlanDetailDAO(ProductPlanDetailDAO productPlanDetailDAO) {
        this.productPlanDetailDAO = productPlanDetailDAO;
    }

    public OccupationDAO getOccupationDAO() {
        return occupationDAO;
    }

    public void setOccupationDAO(OccupationDAO occupationDAO) {
        this.occupationDAO = occupationDAO;
    }

    public ProductPlanDetail getProductPlanDetail() {
        return productPlanDetail;
    }

    public void setProductPlanDetail(ProductPlanDetail productPlanDetail) {
        this.productPlanDetail = productPlanDetail;
    }

    public ProductPlan getProductPlan() {
        return productPlan;
    }

    public void setProductPlan(ProductPlan productPlan) {
        this.productPlan = productPlan;
    }

    public List<OccupationCategory> getOccupationCategoryList() {
        return occupationCategoryList;
    }

    public void setOccupationCategoryList(List<OccupationCategory> occupationCategoryList) {
        this.occupationCategoryList = occupationCategoryList;
    }

    public OccupationCategoryDAO getOccupationCategoryDAO() {
        return occupationCategoryDAO;
    }

    public void setOccupationCategoryDAO(OccupationCategoryDAO occupationCategoryDAO) {
        this.occupationCategoryDAO = occupationCategoryDAO;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(String paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public Boolean getCheckAct() {
        return checkAct;
    }

    public void setCheckAct(Boolean checkAct) {
        this.checkAct = checkAct;
    }

    public Double getSum1() {
        return sum1;
    }

    public void setSum1(Double sum1) {
        this.sum1 = sum1;
    }

    public Boolean getCheckDiscount() {
        return checkDiscount;
    }

    public void setCheckDiscount(Boolean checkDiscount) {
        this.checkDiscount = checkDiscount;
    }

    public Double getSumNoVat() {
        return sumNoVat;
    }

    public void setSumNoVat(Double sumNoVat) {
        this.sumNoVat = sumNoVat;
    }

    public Double getSumVat() {
        return sumVat;
    }

    public void setSumVat(Double sumVat) {
        this.sumVat = sumVat;
    }

    public List<PurchaseOrderInstallment> getPoInstallmentList() {
        return poInstallmentList;
    }

    public void setPoInstallmentList(List<PurchaseOrderInstallment> poInstallmentList) {
        this.poInstallmentList = poInstallmentList;
    }

    public PurchaseOrderInstallment getPoi() {
        return poi;
    }

    public void setPoi(PurchaseOrderInstallment poi) {
        this.poi = poi;
    }

    public boolean isShowPrevious() {
        return showPrevious;
    }

    public void setShowPrevious(boolean showPrevious) {
        this.showPrevious = showPrevious;
    }

    public Integer getNoPaid() {
        return noPaid;
    }

    public void setNoPaid(Integer noPaid) {
        this.noPaid = noPaid;
    }

    public Map<Integer, Integer> getNoInstallmentList() {
        return noInstallmentList;
    }

    public void setNoInstallmentList(Map<Integer, Integer> noInstallmentList) {
        this.noInstallmentList = noInstallmentList;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public Boolean getEditPayment() {
        return editPayment;
    }

    public void setEditPayment(Boolean editPayment) {
        this.editPayment = editPayment;
    }

    public ApprovalRuleDAO getApprovalRuleDAO() {
        return approvalRuleDAO;
    }

    public void setApprovalRuleDAO(ApprovalRuleDAO approvalRuleDAO) {
        this.approvalRuleDAO = approvalRuleDAO;
    }

    public QaTransDAO getQaTransDAO() {
        return qaTransDAO;
    }

    public void setQaTransDAO(QaTransDAO qaTransDAO) {
        this.qaTransDAO = qaTransDAO;
    }

    public QaTransDetailDAO getQaTransDetailDAO() {
        return qaTransDetailDAO;
    }

    public void setQaTransDetailDAO(QaTransDetailDAO qaTransDetailDAO) {
        this.qaTransDetailDAO = qaTransDetailDAO;
    }

    public PurchaseOrderApprovalLog getPurchaseOrderApprovalLog() {
        return purchaseOrderApprovalLog;
    }

    public void setPurchaseOrderApprovalLog(PurchaseOrderApprovalLog purchaseOrderApprovalLog) {
        this.purchaseOrderApprovalLog = purchaseOrderApprovalLog;
    }

    public PaymentMethodDAO getPaymentMethodDAO() {
        return paymentMethodDAO;
    }

    public void setPaymentMethodDAO(PaymentMethodDAO paymentMethodDAO) {
        this.paymentMethodDAO = paymentMethodDAO;
    }

    public List<PaymentMethod> getPaymentMethodList() {
        return paymentMethodList;
    }

    public boolean getOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setPageFrom(String pageFrom) {
        this.pageFrom = pageFrom;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }

    public DeliveryMethodDAO getDeliveryMethodDAO() {
        return deliveryMethodDAO;
    }

    public void setDeliveryMethodDAO(DeliveryMethodDAO deliveryMethodDAO) {
        this.deliveryMethodDAO = deliveryMethodDAO;
    }

    public List<DeliveryMethod> getDeliveryMethodList() {
        return deliveryMethodList;
    }

    public void setDeliveryMethodList(List<DeliveryMethod> deliveryMethodList) {
        this.deliveryMethodList = deliveryMethodList;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryDescription() {
        return deliveryDescription;
    }

    public void setDeliveryDescription(String deliveryDescription) {
        this.deliveryDescription = deliveryDescription;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public SubdistrictDAO getSubdistrictDAO() {
        return subdistrictDAO;
    }

    public void setSubdistrictDAO(SubdistrictDAO subdistrictDAO) {
        this.subdistrictDAO = subdistrictDAO;
    }

    public boolean getImmediateCheck() {
        return immediateCheck;
    }

    public void setImmediateCheck(boolean immediateCheck) {
        this.immediateCheck = immediateCheck;
    }

    public TimeZone getDefaultTimeZone() {
        return defaultTimeZone;
    }

    public void setDefaultTimeZone(TimeZone defaultTimeZone) {
        this.defaultTimeZone = defaultTimeZone;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public CardIssuerDAO getCardIssuerDAO() {
        return cardIssuerDAO;
    }

    public void setCardIssuerDAO(CardIssuerDAO cardIssuerDAO) {
        this.cardIssuerDAO = cardIssuerDAO;
    }

    public List getCardIssuerList() {
        return cardIssuerList;
    }

    public void setCardIssuerList(List cardIssuerList) {
        this.cardIssuerList = cardIssuerList;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMessageCardno() {
        return messageCardno;
    }

    public void setMessageCardno(String messageCardno) {
        this.messageCardno = messageCardno;
    }

    public String getCardNo1() {
        return cardNo1;
    }

    public void setCardNo1(String cardNo1) {
        this.cardNo1 = cardNo1;
    }

    public String getCardNo2() {
        return cardNo2;
    }

    public void setCardNo2(String cardNo2) {
        this.cardNo2 = cardNo2;
    }

    public String getCardNo3() {
        return cardNo3;
    }

    public void setCardNo3(String cardNo3) {
        this.cardNo3 = cardNo3;
    }

    public String getCardNo4() {
        return cardNo4;
    }

    public void setCardNo4(String cardNo4) {
        this.cardNo4 = cardNo4;
    }

    public NextSequenceService getNextSequenceService() {
        return nextSequenceService;
    }

    public void setNextSequenceService(NextSequenceService nextSequenceService) {
        this.nextSequenceService = nextSequenceService;
    }

    public ProductWorkflowDAO getProductWorkflowDAO() {
        return productWorkflowDAO;
    }

    public void setProductWorkflowDAO(ProductWorkflowDAO productWorkflowDAO) {
        this.productWorkflowDAO = productWorkflowDAO;
    }

    public List<ProductWorkflow> getProductWorkflowList() {
        return productWorkflowList;
    }

    public void setProductWorkflowList(List<ProductWorkflow> productWorkflowList) {
        this.productWorkflowList = productWorkflowList;
    }

    public ProductWorkflow getProductWorkflow() {
        return productWorkflow;
    }

    public void setProductWorkflow(ProductWorkflow productWorkflow) {
        this.productWorkflow = productWorkflow;
    }

    public boolean isEnableTraceNo() {
        return enableTraceNo;
    }

    public void setEnableTraceNo(boolean enableTraceNo) {
        this.enableTraceNo = enableTraceNo;
    }

    public boolean isEnableReferenceNo() {
        return enableReferenceNo;
    }

    public void setEnableReferenceNo(boolean enableReferenceNo) {
        this.enableReferenceNo = enableReferenceNo;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getPayerInitial() {
        return payerInitial;
    }

    public void setPayerInitial(String payerInitial) {
        this.payerInitial = payerInitial;
    }

    public String getPayerInitialType() {
        return payerInitialType;
    }

    public void setPayerInitialType(String payerInitialType) {
        this.payerInitialType = payerInitialType;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerSurname() {
        return payerSurname;
    }

    public void setPayerSurname(String payerSurname) {
        this.payerSurname = payerSurname;
    }

    public List<SelectItem> getPayerInitialList() {
        return payerInitialList;
    }

    public void setPayerInitialList(List<SelectItem> payerInitialList) {
        this.payerInitialList = payerInitialList;
    }

    public String getPayerGender() {
        return payerGender;
    }

    public void setPayerGender(String payerGender) {
        this.payerGender = payerGender;
    }

    public List<Bank> getBankList() {
        return bankList;
    }

    public void setBankList(List<Bank> bankList) {
        this.bankList = bankList;
    }

    public BankDAO getBankDAO() {
        return bankDAO;
    }

    public void setBankDAO(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }

    public String getPid1() {
        return pid1;
    }

    public void setPid1(String pid1) {
        this.pid1 = pid1;
    }

    public String getPid2() {
        return pid2;
    }

    public void setPid2(String pid2) {
        this.pid2 = pid2;
    }

    public String getPid3() {
        return pid3;
    }

    public void setPid3(String pid3) {
        this.pid3 = pid3;
    }

    public String getPid4() {
        return pid4;
    }

    public void setPid4(String pid4) {
        this.pid4 = pid4;
    }

    public String getPid5() {
        return pid5;
    }

    public void setPid5(String pid5) {
        this.pid5 = pid5;
    }

    public String getMsgIdnoPayer() {
        return msgIdnoPayer;
    }

    public void setMsgIdnoPayer(String msgIdnoPayer) {
        this.msgIdnoPayer = msgIdnoPayer;
    }

    public Integer getRemainAddress1() {
        return remainAddress1;
    }

    public void setRemainAddress1(Integer remainAddress1) {
        this.remainAddress1 = remainAddress1;
    }

    public Integer getRemainAddress2() {
        return remainAddress2;
    }

    public void setRemainAddress2(Integer remainAddress2) {
        this.remainAddress2 = remainAddress2;
    }

    public String getMsgAddress() {
        return msgAddress;
    }

    public void setMsgAddress(String msgAddress) {
        this.msgAddress = msgAddress;
    }

    public Double getPercentage1() {
        return percentage1;
    }

    public void setPercentage1(Double percentage1) {
        this.percentage1 = percentage1;
    }

    public Double getPercentage2() {
        return percentage2;
    }

    public void setPercentage2(Double percentage2) {
        this.percentage2 = percentage2;
    }

    public Integer getPayerProvinceId() {
        return payerProvinceId;
    }

    public void setPayerProvinceId(Integer payerProvinceId) {
        this.payerProvinceId = payerProvinceId;
    }

    public Integer getPayerDistrictId() {
        return payerDistrictId;
    }

    public void setPayerDistrictId(Integer payerDistrictId) {
        this.payerDistrictId = payerDistrictId;
    }

    public Integer getPayerSubDistrictId() {
        return payerSubDistrictId;
    }

    public void setPayerSubDistrictId(Integer payerSubDistrictId) {
        this.payerSubDistrictId = payerSubDistrictId;
    }

    public List getPayerDistrictList() {
        return payerDistrictList;
    }

    public void setPayerDistrictList(List payerDistrictList) {
        this.payerDistrictList = payerDistrictList;
    }

    public List getPayerSubDistrictList() {
        return payerSubDistrictList;
    }

    public void setPayerSubDistrictList(List payerSubDistrictList) {
        this.payerSubDistrictList = payerSubDistrictList;
    }

    public String getPayerPostalCode() {
        return payerPostalCode;
    }

    public void setPayerPostalCode(String payerPostalCode) {
        this.payerPostalCode = payerPostalCode;
    }

    public List getPayerOccupationList() {
        return payerOccupationList;
    }

    public void setPayerOccupationList(List payerOccupationList) {
        this.payerOccupationList = payerOccupationList;
    }

    public Integer getPayerOccupationId() {
        return payerOccupationId;
    }

    public void setPayerOccupationId(Integer payerOccupationId) {
        this.payerOccupationId = payerOccupationId;
    }

    public Integer getPayerOccupationCategoryId() {
        return payerOccupationCategoryId;
    }

    public void setPayerOccupationCategoryId(Integer payerOccupationCategoryId) {
        this.payerOccupationCategoryId = payerOccupationCategoryId;
    }

    public List<PaymentMethod> getProductPaymentMethodList() {
        return productPaymentMethodList;
    }

    public Map<String, String> getProductPaymentMode() {
        return productPaymentMode;
    }

    public void setPaymentMethodList(List<PaymentMethod> paymentMethodList) {
        this.paymentMethodList = paymentMethodList;
    }

    public void setPaymentModeList(List<String[]> paymentModeList) {
        this.paymentModeList = paymentModeList;
    }

    public List<PaymentMethod> getAllPaymentMethodList() {
        return allPaymentMethodList;
    }

    public List<PaymentMethodStringCastor> getProductPaymentMethodCastorList() {
        return productPaymentMethodCastorList;
    }

    public List<Province> getFprovinceList() {
        return fprovinceList;
    }

    public RegistrationTrxService getRegistrationTxService() {
        return registrationTxService;
    }

    public void setRegistrationTxService(RegistrationTrxService registrationTxService) {
        this.registrationTxService = registrationTxService;
    }

    public QaTrans getQaTrans4Save() {
        return qaTrans4Save;
    }
    
    public QaTrans getQcQaTrans() {
        return qcQaTrans;
    }
    
    public void setQcQaTrans(QaTrans qcQaTrans) {
        this.qcQaTrans = qcQaTrans;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<CreditCardUsageValue> getUsageList() {
        return usageList;
    }

    public void setUsageList(List<CreditCardUsageValue> usageList) {
        this.usageList = usageList;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setMaritalStatusList() {
        ListDetailDAO dao = getListDetailDAO();
//      MaritalStatusDAO dao = getMaritalStatusDAO();
        listOfMaritalStatus = dao.findListMaritalStatus();
        maritalStatusList = new SelectItem[listOfMaritalStatus.size() + 1];
        int pos = 0;
        maritalStatusList[pos++] = new SelectItem(null, "Please Select");
        for (ListDetail ft : listOfMaritalStatus) {
            maritalStatusList[pos++] = new SelectItem(ft.getCode(), ft.getName());
        }
    }

    public SelectItem[] getMaritalStatusList() {
        return maritalStatusList;
    }

    public void setMaritalStatusList(SelectItem[] maritalStatusList) {
        this.maritalStatusList = maritalStatusList;
    }

    public List<ListDetail> getListOfMaritalStatus() {
        return listOfMaritalStatus;
    }

    public void setListOfMaritalStatus(List<ListDetail> listOfMaritalStatus) {
        this.listOfMaritalStatus = listOfMaritalStatus;
    }

    public ListDetailDAO getListDetailDAO() {
        return listDetailDAO;
    }

    public void setListDetailDAO(ListDetailDAO listDetailDAO) {
        this.listDetailDAO = listDetailDAO;
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

    public String getProductPlanDetailInfoJson() {
        return productPlanDetailInfoJson;
    }

    public void setProductPlanDetailInfoJson(String productPlanDetailInfoJson) {
        this.productPlanDetailInfoJson = productPlanDetailInfoJson;
    }

    public ProductPlanDetailInfo getProductPlanDetailInfo() {
        return productPlanDetailInfo;
    }

    public void setProductPlanDetailInfo(ProductPlanDetailInfo productPlanDetailInfo) {
        this.productPlanDetailInfo = productPlanDetailInfo;
    }

    public List<ChildRegType> getChildRegType() {
        return childRegType;
    }

    public double sumPercentBeneficiary(Integer childRegTypeId) {
        double totalPercentBeneficiary = 0.00;
        for (ChildRegInfoValue<PurchaseOrderChildRegister> childReg : this.childRegInfo) {
            if (childReg.getEnable()) {
                if(childReg.getChildRegForm().getChildRegType().getId() == childRegTypeId) {
                    for (FlexFieldInfoValue flexField : childReg.getFlexFields()) {
                        if (flexField.getFlexField().equalsIgnoreCase("%ผู้รับผลประโยชน์")) {
                            totalPercentBeneficiary = totalPercentBeneficiary + Double.parseDouble((flexField.getPoFlexField() == null || flexField.getPoFlexField().trim().isEmpty() ? "0.00" : flexField.getPoFlexField()));
                        }
                    }
                }
            }
        }
        return totalPercentBeneficiary;
    }

    public String getCurrentCopyFrom() {
        return currentCopyFrom;
    }

    public void setCurrentCopyFrom(String currentCopyFrom) {
        this.currentCopyFrom = currentCopyFrom;
    }

    public String getHomeCopyFrom() {
        return homeCopyFrom;
    }

    public void setHomeCopyFrom(String homeCopyFrom) {
        this.homeCopyFrom = homeCopyFrom;
    }

    public String getShippingCopyFrom() {
        return shippingCopyFrom;
    }

    public void setShippingCopyFrom(String shippingCopyFrom) {
        this.shippingCopyFrom = shippingCopyFrom;
    }

    public String getBillingCopyFrom() {
        return billingCopyFrom;
    }

    public void setBillingCopyFrom(String billingCopyFrom) {
        this.billingCopyFrom = billingCopyFrom;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public Integer getProductPlanIdForMain() {
        return productPlanIdForMain;
    }

    public void setProductPlanIdForMain(Integer productPlanIdForMain) {
        this.productPlanIdForMain= productPlanIdForMain;
    }

    public PurchaseOrderRegisterDAO getPurchaseOrderRegisterDAO() {
        return purchaseOrderRegisterDAO;
    }

    public void setPurchaseOrderRegisterDAO(PurchaseOrderRegisterDAO purchaseOrderRegisterDAO) {
        this.purchaseOrderRegisterDAO = purchaseOrderRegisterDAO;
    }

    public PurchaseOrder getPoCopyForMain() {
        return poCopyForMain;
    }

    public void setPoCopyForMain(PurchaseOrder poCopyForMain) {
        this.poCopyForMain = poCopyForMain;
    }
    
    public boolean isShowSaveRemark() {
        return showSaveRemark;
    }

    public void setShowSaveRemark(boolean showSaveRemark) {
        this.showSaveRemark = showSaveRemark;
    }
    
    public void setMsgSaveRemark(String msgSaveRemark) {
        this.msgSaveRemark = msgSaveRemark;
    }

    public String getMsgSaveRemark() {
        return msgSaveRemark;
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

    public String getMotorBody() {
        return motorBody;
    }

    public void setMotorBody(String motorBody) {
        this.motorBody = motorBody;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountByPercent() {
        return discountByPercent;
    }

    public void setDiscountByPercent(Double discountByPercent) {
        this.discountByPercent = discountByPercent;
    }

    public Double getDiscountByMoney() {
        return discountByMoney;
    }

    public void setDiscountByMoney(Double discountByMoney) {
        this.discountByMoney = discountByMoney;
    }

    public boolean isDisableDiscountByPercent() {
        return disableDiscountByPercent;
    }

    public void setDisableDiscountByPercent(boolean disableDiscountByPercent) {
        this.disableDiscountByPercent = disableDiscountByPercent;
    }

    public boolean isDisableDiscountByMoney() {
        return disableDiscountByMoney;
    }

    public void setDisableDiscountByMoney(boolean disableDiscountByMoney) {
        this.disableDiscountByMoney = disableDiscountByMoney;
    }

    public String getDiscountMessageDup() {
        return discountMessageDup;
    }

    public void setDiscountMessageDup(String discountMessageDup) {
        this.discountMessageDup = discountMessageDup;
    }

    public PurchaseOrder getPo2() {
        return po2;
    }

    public void setPo2(PurchaseOrder po2) {
        this.po2 = po2;
    }

    public String getChannelCashType() {
        return channelCashType;
    }

    public void setChannelCashType(String channelCashType) {
        this.channelCashType = channelCashType;
    }

    public String getChannelCashCode() {
        return channelCashCode;
    }

    public void setChannelCashCode(String channelCashCode) {
        this.channelCashCode = channelCashCode;
    }

    public String getAgentCashCode() {
        return agentCashCode;
    }

    public void setAgentCashCode(String agentCashCode) {
        this.agentCashCode = agentCashCode;
    }

    public HashMap<String, String> getChannelCashCodeMap() {
        return channelCashCodeMap;
    }

    public void setChannelCashCodeMap(HashMap<String, String> channelCashCodeMap) {
        this.channelCashCodeMap = channelCashCodeMap;
    }

    public HashMap<String, String> getAgentCashCodeMap() {
        return agentCashCodeMap;
    }

    public void setAgentCashCodeMap(HashMap<String, String> agentCashCodeMap) {
        this.agentCashCodeMap = agentCashCodeMap;
    }
}
