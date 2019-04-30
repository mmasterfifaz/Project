package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.converter.PaymentModeConverter;
import com.maxelyz.core.common.front.dispatch.FrontDispatcher;
import com.maxelyz.core.common.front.dispatch.RegistrationPouch;
import com.maxelyz.core.model.dao.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.front.customerHandling.*;
import com.maxelyz.core.service.QuestionnaireService;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang3.StringUtils;

@ManagedBean (name="confirmationPoController")
//@RequestScoped
@ViewScoped
public class ConfirmationPoController implements Serializable {

    private static Logger log = Logger.getLogger(ConfirmationPoController.class);
    private static String REDIRECT_PAGE = JSFUtil.getRequestContextPath()+"/campaign/assignmentList.jsf";
    private static String SUCCESS = "/front/customerHandling/saleHistory.xhtml?faces-redirect=true";
    private static String FAILURE = "campaignsummary.xhtml";
    private Date defaultDob = null;
    private Product product;
    private RegistrationForm registrationForm;
    private List<ChildRegType> childRegType;
    private List<DeclarationForm> declarationForm;
    private PurchaseOrder purchaseOrder;
    private PurchaseOrderDetail purchaseOrderDetail;
    private Map<String, Integer> provinceList;
    private List<QuestionnaireInfoValue<PurchaseOrderQuestionaire>> questionnaires;
    private List<RegistrationInfoValue> regInfo;
    private List<ChildRegInfoValue<PurchaseOrderChildRegister>> childRegInfo;
    private List<DeclarationInfoValue<PurchaseOrderDeclaration>> declarInfo;
    private List<NosaleReason> nosaleReasonList;
    private List<FollowupsaleReason> followupsaleReasonList;
    private String message;
    private String returnPage;
    private Boolean disableSaveButton=false;
    private Integer nosaleReasonId;
    private Integer followupsaleReasonId;
    private QaTrans qaTrans;
    private QaTransDetail qaTransDetail;
    private List<QaTransDetail> qaTransDetailsList;
    private List<QaFormInfoValue> qaFormValueList;
    private QaForm qaForm;
    private List<QaSelectedCategory> qaSelectedCategoryList;
    private List<QaQuestion> qaQuestionList;
    private List<QaChoice> qaChoiceList;
    private String answer, remark;
    private String categoryType;
    private List<PurchaseOrderInstallment> poInstallmentList;
    private Integer poId;
    private Integer productId;
    private String pageFrom;

    private String confirmScript;

    private String cardNo1="";
    private String cardNo2="";
    private String cardNo3="";
    private String cardNo4="";
    
    private String pid1;
    private String pid2;
    private String pid3;
    private String pid4;
    private String pid5;
    private String payerInitialText;
    
    private String emessage;
    
    private PaymentMethod paymentMethod;
    private boolean isPaymentMethodV2;
    
    @ManagedProperty(value = "#{subdistrictDAO}")
    private SubdistrictDAO subdistrictDAO;
    @ManagedProperty(value = "#{districtDAO}")
    private DistrictDAO districtDAO;
    @ManagedProperty(value = "#{provinceDAO}")
    private ProvinceDAO provinceDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{occupationDAO}")
    private OccupationDAO occupationDAO;
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
    @ManagedProperty(value = "#{nosaleReasonDAO}")
    private NosaleReasonDAO nosaleReasonDAO;
    @ManagedProperty(value = "#{followupsaleReasonDAO}")
    private FollowupsaleReasonDAO followupsaleReasonDAO;
    //vee
    @ManagedProperty(value = "#{qaTransDAO}")
    private QaTransDAO qaTransDAO;
    @ManagedProperty(value = "#{qaTransDetailDAO}")
    private QaTransDetailDAO qaTransDetailDAO;
    @ManagedProperty(value = "#{questionnaireService}")
    private QuestionnaireService questionnaireService;
    @ManagedProperty(value = "#{productPlanDetailDAO}")
    private ProductPlanDetailDAO productPlanDetailDAO;
    @ManagedProperty(value = "#{purchaseOrderRegisterDAO}")
    private PurchaseOrderRegisterDAO purchaseOrderRegisterDAO;
    @ManagedProperty(value = "#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
    @ManagedProperty(value = "#{paymentMethodDAO}")
    private PaymentMethodDAO paymentMethodDAO;
    
    private String totalAmount;

    //@PostConstruct
    public void initialize() {
        regInfo = new ArrayList<RegistrationInfoValue>();
        try {
            if(JSFUtil.getRequestParameterMap("mainPage") != null && JSFUtil.getRequestParameterMap("mainPage").equals("history")){
                poId=null;
                productId=null;
            }
            if(poId == null){
                if(JSFUtil.getRequestParameterMap("poId") != null) {
                    poId = Integer.parseInt(JSFUtil.getRequestParameterMap("poId"));
                } else {
                    poId = (Integer) JSFUtil.getRequest().getAttribute("purchaseOrderId");
                }
            }
            if(productId == null){
                if(JSFUtil.getRequestParameterMap("productId") != null) {
                    productId = Integer.parseInt(JSFUtil.getRequestParameterMap("productId"));
                } else {
                    productId = (Integer) JSFUtil.getRequest().getAttribute("productId");
                }
            }     
            if(JSFUtil.getRequestParameterMap("mainPage") != null && JSFUtil.getRequestParameterMap("mainPage").equals("history")) {
                purchaseOrder = purchaseOrderDAO.findPurchaseOrder(poId);
                if(JSFUtil.getUserSession().getCustomerDetail() == null){
                    CustomerInfoValue c = customerHandlingDAO.findCustomerHandling(purchaseOrder.getCustomer().getId());
                    JSFUtil.getUserSession().setCustomerDetail(c);
                }
            } else {
                List<PurchaseOrder> purchaseOrderList = JSFUtil.getUserSession().getPurchaseOrders();
                for(PurchaseOrder p: purchaseOrderList) {
                    if(p.getId().equals(poId)) {
                        //purchaseOrder = purchaseOrderDAO.findPurchaseOrder(p.getId());
                        purchaseOrder = p;
                    }
                }
            }
            //purchaseOrder = this.getPurchaseOrderDAO().findPurchaseOrder(poId);
            qaTrans = purchaseOrder.getQaTransApproval();
            purchaseOrderDetail = this.getPurchaseOrderDAO().findPurchaseOrderDetailByProduct(poId, productId);
            product = this.getProductDAO().findProduct(productId);
            categoryType = product.getProductCategory().getCategoryType();
            poInstallmentList = purchaseOrderDAO.findInstallment(purchaseOrder.getId());
            
            if (purchaseOrder == null || purchaseOrderDetail == null || product == null) {
                throw new Exception("No Data Found, purchaseOrder or purchaseOrderDetail or product is null");
            }
            Integer iPaymentMethodId = 0;
            try{ iPaymentMethodId = Integer.parseInt(purchaseOrder.getPaymentMethod());}catch(Exception e){}
            paymentMethod = paymentMethodDAO.findPaymentMethod(iPaymentMethodId);
            isPaymentMethodV2 = true;
            try { 
                PaymentModeConverter.convertToPaymentModeList(this.product.getPaymentMethod());
            } catch(Exception e) { isPaymentMethodV2 = false; e.printStackTrace();}

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            JSFUtil.redirect(SUCCESS);
            return;
 //           return REDIRECT_PAGE;
        } finally {
//            JSFUtil.getRequest().removeAttribute("urchaseOrderId");
//            JSFUtil.getRequest().removeAttribute("productId");
        }

        Calendar c = Calendar.getInstance();
        c.set(1970, 0, 1);  //year, month, day
        defaultDob = c.getTime();

        int productId = purchaseOrderDetail.getProduct().getId();
        registrationForm = this.getRegistrationFormDAO().findRegistrationFormByProductId(productId);
        childRegType = this.getChildRegTypeDAO().findChildRegTypeByProduct(product.getId());
        declarationForm = this.getDeclarationFormDAO().findDeclarationFormByProductId(productId);
        nosaleReasonList = this.getNosaleReasonDAO().findNosaleReasonEntities();
        followupsaleReasonList = followupsaleReasonDAO.findFollowupsaleReasonEntities();

        this.setProviceList();
        this.setRegistrationForm();
        this.setChildRegister();
        this.setDeclaration();
        if(qaTrans != null)
            this.setQaForm();
        createConfirmScript();
    }
    
    public void previewConfirmScript() {
        try {
            if(JSFUtil.getRequestParameterMap("mainPage") != null && JSFUtil.getRequestParameterMap("mainPage").equals("history")){
                poId=null;
                productId=null;
            }
            if(poId == null){
                if(JSFUtil.getRequestParameterMap("poId") != null) {
                    poId = Integer.parseInt(JSFUtil.getRequestParameterMap("poId"));
                } else {
                    poId = (Integer) JSFUtil.getRequest().getAttribute("purchaseOrderId");
                }
            }
            if(productId == null){
                if(JSFUtil.getRequestParameterMap("productId") != null) {
                    productId = Integer.parseInt(JSFUtil.getRequestParameterMap("productId"));
                } else {
                    productId = (Integer) JSFUtil.getRequest().getAttribute("productId");
                }
            }     
            if(JSFUtil.getRequestParameterMap("mainPage") != null && JSFUtil.getRequestParameterMap("mainPage").equals("history")) {
                purchaseOrder = purchaseOrderDAO.findPurchaseOrder(poId);
                if(JSFUtil.getUserSession().getCustomerDetail() == null){
                    CustomerInfoValue c = customerHandlingDAO.findCustomerHandling(purchaseOrder.getCustomer().getId());
                    JSFUtil.getUserSession().setCustomerDetail(c);
                }
            } else {
                List<PurchaseOrder> purchaseOrderList = JSFUtil.getUserSession().getPurchaseOrders();
                for(PurchaseOrder p: purchaseOrderList) {
                    if(p.getId().equals(poId)) {
                        purchaseOrder = p;
                    }
                }
            }     
            product = this.getProductDAO().findProduct(productId);
        } catch (Exception e) {
            e.printStackTrace();
            JSFUtil.redirect(SUCCESS);
            return;
        }
        createConfirmScript();
    }

    public void createConfirmScript() {  
        String tempScript = product.getConfirmationScript();
        if(tempScript != null) {
            confirmScript = replaceTagField(tempScript);  
        }
    }
    
    public String replaceTagField(String text) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th","TH"));
        DecimalFormat dec = new DecimalFormat("#,##0.00");
        
        String custName = JSFUtil.getUserSession().getCustomerDetail().getInitial() != null ? JSFUtil.getUserSession().getCustomerDetail().getInitial()+" " : "";
        custName += JSFUtil.getUserSession().getCustomerDetail().getName() != null ? JSFUtil.getUserSession().getCustomerDetail().getName()+" " : "";
        custName += JSFUtil.getUserSession().getCustomerDetail().getSurname()!= null ? JSFUtil.getUserSession().getCustomerDetail().getSurname() : "";
        String userName = JSFUtil.getUserSession().getUsers().getName() != null ? JSFUtil.getUserSession().getUsers().getName() : ""+" "+JSFUtil.getUserSession().getUsers().getSurname() != null ? JSFUtil.getUserSession().getUsers().getSurname() : "";
        text = StringUtils.replace(text, "#customername", (custName != null) && (!custName.equals(" ")) ? custName : "#customername");
        text = StringUtils.replace(text, "#username", (userName != null) && (!userName.equals(" ")) ? userName : "#username");
        text = StringUtils.replace(text, "#referenceno", purchaseOrder.getRefNo() != null ? purchaseOrder.getRefNo() : "#referenceno");
        text = StringUtils.replace(text, "#netpremium", purchaseOrder.getNetPremium()!= null ? dec.format(purchaseOrder.getNetPremium()) : "#netpremium");
        text = StringUtils.replace(text, "#annualnetpremium", purchaseOrder.getAnnualNetPremium() != null ? dec.format(purchaseOrder.getAnnualNetPremium()) : "#annualnetpremium");
        text = StringUtils.replace(text, "#creditcard", purchaseOrder.getCardNo() != null ? purchaseOrder.getCardNo() : "#creditcard");
        text = StringUtils.replace(text, "#bank", purchaseOrder.getBank() != null ? purchaseOrder.getBank().getName() : "#bank");
        text = StringUtils.replace(text, "#cardexpirymonth", purchaseOrder.getCardExpiryMonth()!= null ? purchaseOrder.getCardExpiryMonth().toString() : "#cardexpirymonth");
        text = StringUtils.replace(text, "#cardexpiryyear", purchaseOrder.getCardExpiryYear()!= null ? purchaseOrder.getCardExpiryYear().toString() : "#cardexpiryyear");
        if(purchaseOrder.getTotalAmount() != null) {
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            this.totalAmount = formatter.format(purchaseOrder.getTotalAmount());
        }
        text = StringUtils.replace(text, "#total_amount", purchaseOrder.getTotalAmount()!= null ? this.totalAmount : "#total_amount");
        
        text = StringUtils.replace(text, "#accountno", purchaseOrder.getPaymentAccountNo() != null ? purchaseOrder.getPaymentAccountNo().toString() : "#accountno");
        text = StringUtils.replace(text, "#expecteddate", purchaseOrder.getPaymentExpectedDate() != null ? sdf.format(purchaseOrder.getPaymentExpectedDate()) : "#expecteddate");
        text = StringUtils.replace(text, "#remarks", purchaseOrder.getPaymentRemarks() != null ? purchaseOrder.getPaymentRemarks().toString() : "#remarks");
        
        List<PurchaseOrderDetail> pDetails = (List<PurchaseOrderDetail>) purchaseOrder.getPurchaseOrderDetailCollection();
        text = StringUtils.replace(text, "#productcode", pDetails.get(0).getProduct().getCode() != null ?  pDetails.get(0).getProduct().getCode() : "#productcode");
        text = StringUtils.replace(text, "#productname", pDetails.get(0).getProduct().getName() != null ?  pDetails.get(0).getProduct().getName() : "#productname");
        text = StringUtils.replace(text, "#planname", pDetails.get(0).getProductPlan().getName() != null ?  pDetails.get(0).getProductPlan().getName() : "#planname");
        text = StringUtils.replace(text, "#suminsured", pDetails.get(0).getProductPlan().getSumInsured() != null ? pDetails.get(0).getProductPlan().getSumInsured().toString() : "#suminsured");
        text = StringUtils.replace(text, "#payment_mode", pDetails.get(0).getProductPlan().getPaymentModeLabel()!= null ?  pDetails.get(0).getProductPlan().getPaymentModeLabel(): "#payment_mode");
        List<PurchaseOrderRegister> pOrderRegister = purchaseOrderRegisterDAO.findPurchaseOrderRegisterByPurchaseDetailId(pDetails.get(0).getId());
        int i=1;
        String reg;
        if(pOrderRegister != null) {
            if(pDetails.get(0).getProductPlan() != null) {
                ProductPlanDetail pPlanDetail = productPlanDetailDAO.findByAge(pDetails.get(0).getProductPlan(),pOrderRegister.get(0).getAge());
                if(pPlanDetail != null) {
                    //text = StringUtils.replace(text, "#netpremium", pPlanDetail.getNetPremium() != null ? pPlanDetail.getNetPremium().toString() : "#netpremium");
                    //text = StringUtils.replace(text, "#annualnetpremium", pPlanDetail.getAnnualNetPremium() != null ?  pPlanDetail.getAnnualNetPremium().toString() : "#annualnetpremium");
                }
            }
            for(PurchaseOrderRegister poReg : pOrderRegister) {
                reg = "#reg"+i;
                text = StringUtils.replace(text, reg+"_initial", (poReg.getInitial() != null && !poReg.getInitial().equals("")) ? poReg.getInitialLabel() : reg+"_initial");
                text = StringUtils.replace(text, reg+"_name", (poReg.getName() != null && !poReg.getName().equals("")) ? poReg.getName() : reg+"_name");
                text = StringUtils.replace(text, reg+"_surname", (poReg.getSurname() != null && !poReg.getSurname().equals(""))  ? poReg.getSurname() : reg+"_surname");
                text = StringUtils.replace(text, reg+"_idcardno", (poReg.getIdno() != null && !poReg.getIdno().equals("")) ? poReg.getIdno() : reg+"_idcardno");
                text = StringUtils.replace(text, reg+"_idcardexpirydate", poReg.getIdcardExpiryDate() != null ? sdf.format(poReg.getIdcardExpiryDate()) : reg+"_idcardexpirydate");
                text = StringUtils.replace(text, reg+"_gender", (poReg.getGender() != null && !poReg.getGender().equals("")) ? poReg.getGender() : reg+"_gender");
                text = StringUtils.replace(text, reg+"_dob", poReg.getDob() != null ? sdf.format(poReg.getDob()) : reg+"_dob");
                text = StringUtils.replace(text, reg+"_age", poReg.getAge() != null ? poReg.getAge().toString() : reg+"_age");
                text = StringUtils.replace(text, reg+"_weight", poReg.getWeight() != null ? poReg.getWeight().toString() : reg+"_weight");
                text = StringUtils.replace(text, reg+"_height", poReg.getHeight() != null ? poReg.getHeight().toString() : reg+"_height");
                text = StringUtils.replace(text, reg+"_maritalstatus", (poReg.getMaritalStatus() != null && !poReg.getMaritalStatus().equals("")) ? poReg.getMaritalStatus() : reg+"_maritalstatus");
                text = StringUtils.replace(text, reg+"_nationality", (poReg.getNationality() != null && !poReg.getNationality().equals("")) ? poReg.getNationality() : reg+"_nationality");
                text = StringUtils.replace(text, reg+"_race", (poReg.getRace() != null  && !poReg.getRace().equals("")) ? poReg.getRace() : reg+"_race");
                text = StringUtils.replace(text, reg+"_religion", (poReg.getReligion() != null && !poReg.getReligion().equals("")) ? poReg.getReligion() : reg+"_religion");
                text = StringUtils.replace(text, reg+"_jobdescription", (poReg.getJobDescription() != null && !poReg.getJobDescription().equals(" ")) ? poReg.getJobDescription() : reg+"_jobdescription");
                text = StringUtils.replace(text, reg+"_position", (poReg.getPosition() != null && !poReg.getPosition().equals("")) ? poReg.getPosition() : reg+"_position");
                text = StringUtils.replace(text, reg+"_occupation", (poReg.getOccupationText() != null && !poReg.getOccupationText().equals("")) ? poReg.getOccupationText() : reg+"_occupation");
                text = StringUtils.replace(text, reg+"_businesstype", (poReg.getBusinessType() != null && !poReg.getBusinessType().equals("")) ? poReg.getBusinessType() : reg+"_businesstype");
                text = StringUtils.replace(text, reg+"_income", (poReg.getIncome() != null && !poReg.getIncome().equals("")) ? poReg.getIncome() : reg+"_income");
                text = StringUtils.replace(text, reg+"_homephone", (poReg.getHomePhone() != null && !poReg.getHomePhone().equals("")) ? poReg.getHomePhone() : reg+"_homephone");
                text = StringUtils.replace(text, reg+"_officephone", (poReg.getOfficePhone() != null && !poReg.getOfficePhone().equals("")) ? poReg.getOfficePhone() : reg+"_officephone");
                text = StringUtils.replace(text, reg+"_mobilephone", (poReg.getMobilePhone() != null && !poReg.getMobilePhone().equals("")) ? poReg.getMobilePhone() : reg+"_mobilephone");
                text = StringUtils.replace(text, reg+"_email", (poReg.getEmail() != null && !poReg.getEmail().equals("")) ? poReg.getEmail() : reg+"_email");

                text = StringUtils.replace(text, reg+"_currentfullname", (poReg.getCurrentFullname() != null && !poReg.getCurrentFullname().equals("")) ? poReg.getCurrentFullname() : reg+"_currentfullname");
                text = StringUtils.replace(text, reg+"_currentaddressline1", (poReg.getCurrentAddressLine1() != null && !poReg.getCurrentAddressLine1().equals("")) ? poReg.getCurrentAddressLine1() : reg+"_currentaddressline1");
                text = StringUtils.replace(text, reg+"_currentaddressline2", (poReg.getCurrentAddressLine2() != null && !poReg.getCurrentAddressLine2().equals("")) ? poReg.getCurrentAddressLine2() : reg+"_currentaddressline2");
                text = StringUtils.replace(text, reg+"_currentaddressline3", (poReg.getCurrentAddressLine3() != null && !poReg.getCurrentAddressLine3().equals("")) ? poReg.getCurrentAddressLine3() : reg+"_currentaddressline3");
                text = StringUtils.replace(text, reg+"_currentaddressline4", (poReg.getCurrentAddressLine4() != null && !poReg.getCurrentAddressLine4().equals("")) ? poReg.getCurrentAddressLine4() : reg+"_currentaddressline4");
                text = StringUtils.replace(text, reg+"_currentaddressline5", (poReg.getCurrentAddressLine5() != null && !poReg.getCurrentAddressLine5().equals("")) ? poReg.getCurrentAddressLine5() : reg+"_currentaddressline5");
                text = StringUtils.replace(text, reg+"_currentaddressline6", (poReg.getCurrentAddressLine6() != null && !poReg.getCurrentAddressLine6().equals("")) ? poReg.getCurrentAddressLine6() : reg+"_currentaddressline6");
                text = StringUtils.replace(text, reg+"_currentaddressline7", (poReg.getCurrentAddressLine7() != null && !poReg.getCurrentAddressLine7().equals("")) ? poReg.getCurrentAddressLine7() : reg+"_currentaddressline7");
                text = StringUtils.replace(text, reg+"_currentaddressline8", (poReg.getCurrentAddressLine8() != null && !poReg.getCurrentAddressLine8().equals("")) ? poReg.getCurrentAddressLine8() : reg+"_currentaddressline8");
                text = StringUtils.replace(text, reg+"_currentsubdistrictname", poReg.getCurrentSubDistrict() != null ? poReg.getCurrentSubDistrict().getName() : reg+"_currentsubdistrictname");
                text = StringUtils.replace(text, reg+"_currentdistrictname", poReg.getCurrentDistrict() != null ? poReg.getCurrentDistrict().getName() : reg+"_currentdistrictname");
                text = StringUtils.replace(text, reg+"_currentprovincename", poReg.getCurrentDistrict() != null ? poReg.getCurrentDistrict().getProvinceId().getName() :  reg+"_currentprovincename");
                text = StringUtils.replace(text, reg+"_currentpostalcode", (poReg.getCurrentPostalCode() != null && !poReg.getCurrentPostalCode().equals("")) ? poReg.getCurrentPostalCode() : reg+"_currentpostalcode");
                text = StringUtils.replace(text, reg+"_currenttelephone1", (poReg.getCurrentTelephone1() != null && !poReg.getCurrentTelephone1().equals("")) ? poReg.getCurrentTelephone1() : reg+"_currenttelephone1");
                text = StringUtils.replace(text, reg+"_currenttelephone2", (poReg.getCurrentTelephone2() != null && !poReg.getCurrentTelephone2().equals("")) ? poReg.getCurrentTelephone2() : reg+"_currenttelephone2");

                text = StringUtils.replace(text, reg+"_homefullname", (poReg.getHomeFullname() != null && !poReg.getHomeFullname().equals("")) ? poReg.getHomeFullname() : reg+"_homefullname");
                text = StringUtils.replace(text, reg+"_homeaddressline1", (poReg.getHomeAddressLine1() != null && !poReg.getHomeAddressLine1().equals("")) ? poReg.getHomeAddressLine1() : reg+"_homeaddressline1");
                text = StringUtils.replace(text, reg+"_homeaddressline2", (poReg.getHomeAddressLine2() != null && !poReg.getHomeAddressLine2().equals("")) ? poReg.getHomeAddressLine2() : reg+"_homeaddressline2");
                text = StringUtils.replace(text, reg+"_homeaddressline3", (poReg.getHomeAddressLine3() != null && !poReg.getHomeAddressLine3().equals("")) ? poReg.getHomeAddressLine3() : reg+"_homeaddressline3");
                text = StringUtils.replace(text, reg+"_homeaddressline4", (poReg.getHomeAddressLine4() != null && !poReg.getHomeAddressLine4().equals("")) ? poReg.getHomeAddressLine4() : reg+"_homeaddressline4");
                text = StringUtils.replace(text, reg+"_homeaddressline5", (poReg.getHomeAddressLine5() != null && !poReg.getHomeAddressLine5().equals("")) ? poReg.getHomeAddressLine5() : reg+"_homeaddressline5");
                text = StringUtils.replace(text, reg+"_homeaddressline6", (poReg.getHomeAddressLine6() != null && !poReg.getHomeAddressLine6().equals("")) ? poReg.getHomeAddressLine6() : reg+"_homeaddressline6");
                text = StringUtils.replace(text, reg+"_homeaddressline7", (poReg.getHomeAddressLine7() != null && !poReg.getHomeAddressLine7().equals("")) ? poReg.getHomeAddressLine7() : reg+"_homeaddressline7");
                text = StringUtils.replace(text, reg+"_homeaddressline8", (poReg.getHomeAddressLine8() != null && !poReg.getHomeAddressLine8().equals("")) ? poReg.getHomeAddressLine8() : reg+"_homeaddressline8");
                text = StringUtils.replace(text, reg+"_homesubdistrictname", poReg.getHomeSubDistrict() != null ? poReg.getHomeSubDistrict().getName() : reg+"_homesubdistrictname");
                text = StringUtils.replace(text, reg+"_homedistrictname", poReg.getHomeDistrict() != null ? poReg.getHomeDistrict().getName() : reg+"_homedistrictname");
                text = StringUtils.replace(text, reg+"_homeprovincename", poReg.getHomeDistrict() != null ? poReg.getHomeDistrict().getProvinceId().getName() : reg+"_homeprovincename");
                text = StringUtils.replace(text, reg+"_homepostalcode", (poReg.getHomePostalCode() != null && !poReg.getHomePostalCode().equals("")) ? poReg.getHomeAddressLine8() : reg+"_homeaddressline8");
                text = StringUtils.replace(text, reg+"_hometelephone1", (poReg.getHomeTelephone1() != null && !poReg.getHomeTelephone1().equals("")) ? poReg.getHomeAddressLine8() : reg+"_homeaddressline8");
                text = StringUtils.replace(text, reg+"_hometelephone2", (poReg.getHomeTelephone2() != null && !poReg.getHomeTelephone2().equals("")) ? poReg.getHomeAddressLine8() : reg+"_homeaddressline8");

                text = StringUtils.replace(text, reg+"_billingfullname", (poReg.getBillingFullname() != null && !poReg.getBillingFullname().equals("")) ? poReg.getBillingFullname() : reg+"_billingfullname");
                text = StringUtils.replace(text, reg+"_billingaddressline1", (poReg.getBillingAddressLine1() != null && !poReg.getBillingAddressLine1().equals("")) ? poReg.getBillingAddressLine1() : reg+"_billingaddressline1");
                text = StringUtils.replace(text, reg+"_billingaddressline2", (poReg.getBillingAddressLine2() != null && !poReg.getBillingAddressLine2().equals("")) ? poReg.getBillingAddressLine2() : reg+"_billingaddressline2");
                text = StringUtils.replace(text, reg+"_billingaddressline3", (poReg.getBillingAddressLine3() != null && !poReg.getBillingAddressLine3().equals("")) ? poReg.getBillingAddressLine3() : reg+"_billingaddressline3");
                text = StringUtils.replace(text, reg+"_billingaddressline4", (poReg.getBillingAddressLine4() != null && !poReg.getBillingAddressLine4().equals("")) ? poReg.getBillingAddressLine4() : reg+"_billingaddressline4");
                text = StringUtils.replace(text, reg+"_billingaddressline5", (poReg.getBillingAddressLine5() != null && !poReg.getBillingAddressLine5().equals("")) ? poReg.getBillingAddressLine5() : reg+"_billingaddressline5");
                text = StringUtils.replace(text, reg+"_billingaddressline6", (poReg.getBillingAddressLine6() != null && !poReg.getBillingAddressLine6().equals("")) ? poReg.getBillingAddressLine6() : reg+"_billingaddressline6");
                text = StringUtils.replace(text, reg+"_billingaddressline7", (poReg.getBillingAddressLine7() != null && !poReg.getBillingAddressLine7().equals("")) ? poReg.getBillingAddressLine7() : reg+"_billingaddressline7");
                text = StringUtils.replace(text, reg+"_billingaddressline8", (poReg.getBillingAddressLine8() != null && !poReg.getBillingAddressLine8().equals("")) ? poReg.getBillingAddressLine8() : reg+"_billingaddressline8");
                text = StringUtils.replace(text, reg+"_billingsubdistrictname", poReg.getBillingSubDistrict() != null ? poReg.getBillingSubDistrict().getName() : reg+"_billingsubdistrictname");
                text = StringUtils.replace(text, reg+"_billingdistrictname", poReg.getBillingDistrict() != null ? poReg.getBillingDistrict().getName() : reg+"_billingdistrictname");
                text = StringUtils.replace(text, reg+"_billingprovincename", poReg.getBillingDistrict() != null ? poReg.getBillingDistrict().getProvinceId().getName() : reg+"_billingprovincename");
                text = StringUtils.replace(text, reg+"_billingpostalcode", (poReg.getBillingPostalCode() != null && !poReg.getBillingPostalCode().equals("")) ? poReg.getBillingPostalCode() : reg+"_billingpostalcode");
                text = StringUtils.replace(text, reg+"_billingtelephone1", (poReg.getBillingTelephone1() != null && !poReg.getBillingTelephone1().equals("")) ? poReg.getBillingTelephone1() : reg+"_billingtelephone1");
                text = StringUtils.replace(text, reg+"_billingtelephone2", (poReg.getBillingTelephone2() != null && !poReg.getBillingTelephone2().equals("")) ? poReg.getBillingTelephone2() : reg+"_billingtelephone2");

                text = StringUtils.replace(text, reg+"_shippingfullname", (poReg.getShippingFullname() != null && !poReg.getShippingFullname().equals("")) ? poReg.getHomeAddressLine8() : reg+"_shippingfullname");
                text = StringUtils.replace(text, reg+"_shippingaddressline1", (poReg.getShippingAddressLine1() != null && !poReg.getShippingAddressLine1().equals("")) ? poReg.getShippingAddressLine1() : reg+"_shippingaddressline1");
                text = StringUtils.replace(text, reg+"_shippingaddressline2", (poReg.getShippingAddressLine2() != null && !poReg.getShippingAddressLine2().equals("")) ? poReg.getShippingAddressLine2() : reg+"_shippingaddressline2");
                text = StringUtils.replace(text, reg+"_shippingaddressline3", (poReg.getShippingAddressLine3() != null && !poReg.getShippingAddressLine3().equals("")) ? poReg.getShippingAddressLine3() : reg+"_shippingaddressline3");
                text = StringUtils.replace(text, reg+"_shippingaddressline4", (poReg.getShippingAddressLine4() != null && !poReg.getShippingAddressLine4().equals("")) ? poReg.getShippingAddressLine4() : reg+"_shippingaddressline4");
                text = StringUtils.replace(text, reg+"_shippingaddressline5", (poReg.getShippingAddressLine5() != null && !poReg.getShippingAddressLine5().equals("")) ? poReg.getShippingAddressLine5() : reg+"_shippingaddressline5");
                text = StringUtils.replace(text, reg+"_shippingaddressline6", (poReg.getShippingAddressLine6() != null && !poReg.getShippingAddressLine6().equals("")) ? poReg.getShippingAddressLine6() : reg+"_shippingaddressline6");
                text = StringUtils.replace(text, reg+"_shippingaddressline7", (poReg.getShippingAddressLine7() != null && !poReg.getShippingAddressLine7().equals("")) ? poReg.getShippingAddressLine7() : reg+"_shippingaddressline7");
                text = StringUtils.replace(text, reg+"_shippingaddressline8", (poReg.getShippingAddressLine8() != null && !poReg.getShippingAddressLine8().equals("")) ? poReg.getShippingAddressLine8() : reg+"_shippingaddressline8");
                text = StringUtils.replace(text, reg+"_shippingsubdistrictname", poReg.getShippingSubDistrict() != null ? poReg.getShippingSubDistrict().getName() : reg+"_shippingsubdistrictname");
                text = StringUtils.replace(text, reg+"_shippingdistrictname", poReg.getShippingDistrict() != null ? poReg.getShippingDistrict().getName() : reg+"_shippingdistrictname");
                text = StringUtils.replace(text, reg+"_shippingprovincename", poReg.getShippingDistrict() != null ? poReg.getShippingDistrict().getProvinceId().getName() :  reg+"_shippingprovincename");
                text = StringUtils.replace(text, reg+"_shippingpostalcode", (poReg.getShippingPostalCode() != null && !poReg.getShippingPostalCode().equals("")) ? poReg.getShippingPostalCode() : reg+"_shippingpostalcode");
                text = StringUtils.replace(text, reg+"_shippingtelephone1", (poReg.getShippingTelephone1() != null && !poReg.getShippingTelephone1().equals("")) ? poReg.getShippingTelephone1() : reg+"_shippingtelephone1");
                text = StringUtils.replace(text, reg+"_shippingtelephone2", (poReg.getShippingTelephone2() != null && !poReg.getShippingTelephone2().equals("")) ? poReg.getShippingTelephone2() : reg+"_shippingtelephone2");

                text = StringUtils.replace(text, reg+"_fx1", (poReg.getFx1() != null && !poReg.getFx1().equals("")) ? poReg.getFx1() : reg+"_fx1");
                text = StringUtils.replace(text, reg+"_fx2", (poReg.getFx2() != null && !poReg.getFx2().equals("")) ? poReg.getFx2() : reg+"_fx2");
                text = StringUtils.replace(text, reg+"_fx3", (poReg.getFx3() != null && !poReg.getFx3().equals("")) ? poReg.getFx3() : reg+"_fx3");
                text = StringUtils.replace(text, reg+"_fx4", (poReg.getFx4() != null && !poReg.getFx4().equals("")) ? poReg.getFx4() : reg+"_fx4");
                text = StringUtils.replace(text, reg+"_fx5", (poReg.getFx5() != null && !poReg.getFx5().equals("")) ? poReg.getFx5() : reg+"_fx5");
                text = StringUtils.replace(text, reg+"_fx6", (poReg.getFx6() != null && !poReg.getFx6().equals("")) ? poReg.getFx6() : reg+"_fx6");
                text = StringUtils.replace(text, reg+"_fx7", (poReg.getFx7() != null && !poReg.getFx7().equals("")) ? poReg.getFx7() : reg+"_fx7");
                text = StringUtils.replace(text, reg+"_fx8", (poReg.getFx8() != null && !poReg.getFx8().equals("")) ? poReg.getFx8() : reg+"_fx8");
                text = StringUtils.replace(text, reg+"_fx9", (poReg.getFx9() != null && !poReg.getFx9().equals("")) ? poReg.getFx9() : reg+"_fx9");
                text = StringUtils.replace(text, reg+"_fx10", (poReg.getFx10() != null && !poReg.getFx10().equals("")) ? poReg.getFx10() : reg+"_fx10");
                text = StringUtils.replace(text, reg+"_fx11", (poReg.getFx11() != null && !poReg.getFx11().equals("")) ? poReg.getFx11() : reg+"_fx11");
                text = StringUtils.replace(text, reg+"_fx12", (poReg.getFx12() != null && !poReg.getFx12().equals("")) ? poReg.getFx12() : reg+"_fx12");
                text = StringUtils.replace(text, reg+"_fx13", (poReg.getFx13() != null && !poReg.getFx13().equals(""))? poReg.getFx13() : reg+"_fx13");
                text = StringUtils.replace(text, reg+"_fx14", (poReg.getFx14() != null && !poReg.getFx14().equals("")) ? poReg.getFx14() : reg+"_fx14");
                text = StringUtils.replace(text, reg+"_fx15", (poReg.getFx15() != null && !poReg.getFx15().equals("")) ? poReg.getFx15() : reg+"_fx15");
                text = StringUtils.replace(text, reg+"_fx16", (poReg.getFx16() != null && !poReg.getFx16().equals("")) ? poReg.getFx16() : reg+"_fx16");
                text = StringUtils.replace(text, reg+"_fx17", (poReg.getFx17() != null && !poReg.getFx17().equals("")) ? poReg.getFx17() : reg+"_fx17");
                text = StringUtils.replace(text, reg+"_fx18", (poReg.getFx18() != null && !poReg.getFx18().equals("")) ? poReg.getFx18() : reg+"_fx18");
                text = StringUtils.replace(text, reg+"_fx19", (poReg.getFx19() != null && !poReg.getFx19().equals("")) ? poReg.getFx19() : reg+"_fx19");
                text = StringUtils.replace(text, reg+"_fx20", (poReg.getFx20() != null && !poReg.getFx20().equals("")) ? poReg.getFx20() : reg+"_fx20");

                i++;
            }
            
            // REPLACE TAG FAMILY PLAN        
            Integer poMainId = null;
            if(purchaseOrder.getMainPoId() != null) {  // FIND FAMILY PLAN
                poMainId = purchaseOrder.getMainPoId();
            } else {                        // FIND MAIN INSURE
                poMainId = purchaseOrder.getId();            
            }
            
            Integer age = pOrderRegister.get(0).getAge();
            String gender = pOrderRegister.get(0).getGender();

            ProductPlanDetail ppdMainInsure = productPlanDetailDAO.findMainInsurePlan(poMainId, age, gender);
            List<ProductPlanDetail> ppdChildFamilyList = productPlanDetailDAO.findRelationChlidPlan(poMainId, age, gender);

            if(ppdMainInsure != null) {
                ProductPlan ppMainInsure = ppdMainInsure.getProductPlan();
                text = StringUtils.replace(text, "#familyplanname[0]", ppMainInsure.getName() != null ? ppMainInsure.getName() : "#familyplanname[0]");
                text = StringUtils.replace(text, "#familysuminsured[0]", ppMainInsure.getSumInsured() != null ? dec.format(ppMainInsure.getSumInsured()) : "#familysuminsured[0]");
                text = StringUtils.replace(text, "#familypaymentmode[0]", ppMainInsure.getPaymentModeLabel() != null ? ppMainInsure.getPaymentModeLabel(): "#familypaymentmode[0]");
                text = StringUtils.replace(text, "#familynetpremium[0]", ppdMainInsure.getNetPremium() != null ? dec.format(ppdMainInsure.getNetPremium()) : "#familynetpremium[0]");
                text = StringUtils.replace(text, "#familyannualnetpremium[0]", ppdMainInsure.getAnnualNetPremium() != null ? dec.format(ppdMainInsure.getAnnualNetPremium()) : "#familyannualnetpremium[0]");
            }

            if(ppdChildFamilyList != null && ppdChildFamilyList.size() > 0) {
                int iChild = 1;
                for (ProductPlanDetail ppdChild : ppdChildFamilyList) {
                    ProductPlan ppChild = ppdChild.getProductPlan();
                    text = StringUtils.replace(text, "#familyplanname["+iChild+"]", ppChild.getName() != null ? ppChild.getName() : "#familyplanname["+iChild+"]");
                    text = StringUtils.replace(text, "#familysuminsured["+iChild+"]", ppChild.getSumInsured() != null ? dec.format(ppChild.getSumInsured()) : "#familysuminsured["+iChild+"]");
                    text = StringUtils.replace(text, "#familypaymentmode["+iChild+"]", ppChild.getPaymentModeLabel() != null ? ppChild.getPaymentModeLabel(): "#familypaymentmode["+iChild+"]");
                    text = StringUtils.replace(text, "#familynetpremium["+iChild+"]", ppdChild.getNetPremium() != null ? dec.format(ppdChild.getNetPremium()) : "#familynetpremium["+iChild+"]");
                    text = StringUtils.replace(text, "#familyannualnetpremium["+iChild+"]", ppdChild.getAnnualNetPremium() != null ? dec.format(ppdChild.getAnnualNetPremium()) : "#familyannualnetpremium["+iChild+"]");
                    iChild++;
                }
            }
        }
        
        return text;
    }
    
    public void toConfirm(ActionEvent event){
        initialize();
    }

    public void initListener(ActionEvent event) {
        initialize();
    }

    public String backAction() {
        this.emessage = null;
        try{  
            RegistrationPouch pouch = new RegistrationPouch(); // create pouch to carry data for purchase order            
            pouch.registeredPouchType(RegistrationPouch.SENDER_TYPE.CONFIRMPURCHASEORDER, RegistrationPouch.RECEIVER_MODE.EDIT);
            pouch.putEditModeParameter(this.purchaseOrder.getId());
            FrontDispatcher.keepPouch(pouch); // registered pouch on dispatcher
            return FrontDispatcher.getPouchReceiver(pouch); // get receiver
        } catch (Exception e) {
            log.error("Error when dispatch from saleHistory to registration form : "+e.getMessage());
            this.emessage = "Cannot go back to registration form. [Cause: "+e.getMessage()+"] ";
            return null;
        }          
    }

    public String fromPopup2RegistrationForm() {
        this.emessage = null;
        try{  
            RegistrationPouch pouch = new RegistrationPouch(); // create pouch to carry data for purchase order            
            pouch.registeredPouchType(RegistrationPouch.SENDER_TYPE.CONTACTRECORD, RegistrationPouch.RECEIVER_MODE.EDIT);
            pouch.putEditModeParameter(this.purchaseOrder.getId());
            FrontDispatcher.keepPouch(pouch); // registered pouch on dispatcher
            return FrontDispatcher.getPouchReceiver(pouch); // get receiver
        } catch (Exception e) {
            log.error("Error when dispatch from saleHistory to registration form : "+e.getMessage());
            this.emessage = "Cannot forward to registration form. [Cause: "+e.getMessage()+"] ";
            return null;
        }        
    }    
    
    
    private void setPoFlexField(PurchaseOrderRegister poReg, List<FlexFieldInfoValue> fxInfo) {
        for (FlexFieldInfoValue fx : fxInfo) {
            int no = fx.getNo();
            switch (no) {
                case 1:
                    poReg.setFx1(fx.getPoFlexField());
                    break;
                case 2:
                    poReg.setFx2(fx.getPoFlexField());
                    break;
                case 3:
                    poReg.setFx3(fx.getPoFlexField());
                    break;
                case 4:
                    poReg.setFx4(fx.getPoFlexField());
                    break;
                case 5:
                    poReg.setFx5(fx.getPoFlexField());
                    break;
                case 6:
                    poReg.setFx6(fx.getPoFlexField());
                    break;
                case 7:
                    poReg.setFx7(fx.getPoFlexField());
                    break;
                case 8:
                    poReg.setFx8(fx.getPoFlexField());
                    break;
                case 9:
                    poReg.setFx9(fx.getPoFlexField());
                    break;
                case 10:
                    poReg.setFx10(fx.getPoFlexField());
                    break;
                case 11:
                    poReg.setFx11(fx.getPoFlexField());
                    break;
                case 12:
                    poReg.setFx12(fx.getPoFlexField());
                    break;
                case 13:
                    poReg.setFx13(fx.getPoFlexField());
                    break;
                case 14:
                    poReg.setFx14(fx.getPoFlexField());
                    break;
                case 15:
                    poReg.setFx15(fx.getPoFlexField());
                    break;
                case 16:
                    poReg.setFx16(fx.getPoFlexField());
                    break;
                case 17:
                    poReg.setFx17(fx.getPoFlexField());
                    break;
                case 18:
                    poReg.setFx18(fx.getPoFlexField());
                    break;
                case 19:
                    poReg.setFx19(fx.getPoFlexField());
                    break;
                case 20:
                    poReg.setFx20(fx.getPoFlexField());
                    break;
            }
        }
    }

    private void setNoUseDistrict(PurchaseOrderRegister poReg) {
        if (poReg.getCurrentDistrict() != null) {
            if (poReg.getCurrentDistrict().getId() == null || poReg.getCurrentDistrict().getId() == 0) {
                poReg.setCurrentDistrict(null);
            }
        }
        if (poReg.getHomeDistrict() != null) {
            if (poReg.getHomeDistrict().getId() == null || poReg.getHomeDistrict().getId() == 0) {
                poReg.setHomeDistrict(null);
            }
        }
        if (poReg.getBillingDistrict() != null) {
            if (poReg.getBillingDistrict().getId() == null || poReg.getBillingDistrict().getId() == 0) {
                poReg.setBillingDistrict(null);
            }
        }
        if (poReg.getShippingDistrict() != null) {
            if (poReg.getShippingDistrict().getId() == null || poReg.getShippingDistrict().getId() == 0) {
                poReg.setShippingDistrict(null);
            }
        }
    }
    
    private void recoverDistrict(PurchaseOrderRegister poReg) {
        if (poReg.getCurrentDistrict()== null) {
            poReg.setCurrentDistrict(new District());
        }
        if (poReg.getHomeDistrict()== null) {
            poReg.setHomeDistrict(new District());
        }
        if (poReg.getBillingDistrict() == null) {
            poReg.setBillingDistrict(new District());
        }
        if (poReg.getShippingDistrict() == null) {
            poReg.setShippingDistrict(new District());
        }
    }

    public String saveAction() {
        return SUCCESS;
    }

    public void setRegistrationForm() {
        List<RegistrationField> registrationFields = (List<RegistrationField>) registrationForm.getRegistrationFieldCollection();
        for (int i = 0; i < 6; i++) {
            try {
                PurchaseOrderRegister tpor = null;
                List<PurchaseOrderRegister> poReg = this.getPurchaseOrderDAO().findPurchaseOrderRegisterByPoDetail(purchaseOrderDetail.getId());
                if(poReg != null){
                    for(PurchaseOrderRegister tt : poReg){
                        if((i+1) == tt.getNo()){
                            tpor = tt;
                        }
                    }
                }
                RegistrationInfoValue<PurchaseOrderRegister> item = new RegistrationInfoValue<PurchaseOrderRegister> (registrationForm, registrationFields, i + 1, districtDAO, subdistrictDAO, occupationDAO, tpor, "confirm");
                item.setAgeCalMethod(product.getAgeCalMethod());
                item.setAgeCalMonth(product.getAgeCalMonth());
                if(item.getPo().getDob() == null){
                    item.getPo().setDob(defaultDob);
                    item.setAge(defaultDob);
                }else{
                    item.setAge(item.getPo().getDob());
                }
                if(item.getPo().getCurrentDistrict() != null && item.getPo().getCurrentDistrict().getId() != null)
                    item.getPo().setCurrentDistrict(districtDAO.findDistrict(item.getPo().getCurrentDistrict().getId()));

                if(item.getPo().getHomeDistrict() != null && item.getPo().getHomeDistrict().getId() != null)
                    item.getPo().setHomeDistrict(districtDAO.findDistrict(item.getPo().getHomeDistrict().getId()));

                if(item.getPo().getBillingDistrict() != null && item.getPo().getBillingDistrict().getId() != null)
                    item.getPo().setBillingDistrict(districtDAO.findDistrict(item.getPo().getBillingDistrict().getId()));

                if(item.getPo().getShippingDistrict() != null && item.getPo().getShippingDistrict().getId() != null)
                    item.getPo().setShippingDistrict(districtDAO.findDistrict(item.getPo().getShippingDistrict().getId()));
                
                //Subdistrict
                if(item.getPo().getCurrentSubDistrict() != null && item.getPo().getCurrentSubDistrict().getId() != null)
                    item.getPo().setCurrentSubDistrict(subdistrictDAO.findSubDistrict(item.getPo().getCurrentSubDistrict().getId()));

                if(item.getPo().getHomeSubDistrict() != null && item.getPo().getHomeSubDistrict().getId() != null)
                    item.getPo().setHomeSubDistrict(subdistrictDAO.findSubDistrict(item.getPo().getHomeSubDistrict().getId()));

                if(item.getPo().getBillingSubDistrict() != null && item.getPo().getBillingSubDistrict().getId() != null)
                    item.getPo().setBillingSubDistrict(subdistrictDAO.findSubDistrict(item.getPo().getBillingSubDistrict().getId()));

                if(item.getPo().getShippingSubDistrict() != null && item.getPo().getShippingSubDistrict().getId() != null)
                    item.getPo().setShippingSubDistrict(subdistrictDAO.findSubDistrict(item.getPo().getShippingSubDistrict().getId()));
                
                 //set Citizen ID
                if(item.getPo().getIdno() != null && item.getPo().getIdno().length() == 13) {   //!item.getPo().getIdno().equals("") && 
                    String idno = item.getPo().getIdno();
                    item.setId1(idno.substring(0, 1));
                    item.setId2(idno.substring(1, 5));
                    item.setId3(idno.substring(5, 10));
                    item.setId4(idno.substring(10, 12));
                    item.setId5(idno.substring(12, 13));
                    item.setCitizenId(idno);
                }
                regInfo.add(item);

            } catch (NonexistentEntityException e) {
                //no need recovery process
            } catch (Exception e) {
                log.error(e);
            }
        }
        
        if(purchaseOrder.getPayerInitial() != null && !purchaseOrder.getPayerInitial().isEmpty()){
            payerInitialText = changeCodeToText(purchaseOrder.getPayerInitial(),"initial", registrationFields);
        }
        //set card no
        if(purchaseOrder.getCardNo() != null && purchaseOrder.getCardNo().length() == 16) { //!purchaseOrder.getCardNo().equals("") && 
            String cardNo = purchaseOrder.getCardNo();
            cardNo1 = cardNo.substring(0, 4);
            cardNo2 = cardNo.substring(4, 8);
            cardNo3 = cardNo.substring(8, 12);
            cardNo4 = cardNo.substring(12, 16);
        }
        
        if(purchaseOrder.getPayerIdcard() != null && purchaseOrder.getPayerIdcard().length() == 13){
            String pidno = purchaseOrder.getPayerIdcard();
            pid1 = pidno.substring(0, 1);
            pid2 = pidno.substring(1, 5);
            pid3 = pidno.substring(5, 10);
            pid4 = pidno.substring(10, 12);
            pid5 = pidno.substring(12, 13);
        }
    }
    
    private void setChildRegister() {
        childRegInfo = new ArrayList<ChildRegInfoValue<PurchaseOrderChildRegister>>();
        
        for (int i = 0; i < this.childRegType.size(); i++) {
            List<ChildRegForm> childRegForms = (List<ChildRegForm>) this.childRegType.get(i).getChildRegFormCollection();
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
                    }

                    if (poChildRegister == null) {
                        poChildRegister = new PurchaseOrderChildRegister();
                        poChildRegister.setPurchaseOrderDetail(purchaseOrderDetail);
                        poChildRegister.setChildRegForm(childRegForms.get(j));
                        poChildRegister.setEnable(false);               
                    }

                    ChildRegInfoValue<PurchaseOrderChildRegister> item = new ChildRegInfoValue<PurchaseOrderChildRegister>(childRegForms.get(j), childRegFields, j + 1, poChildRegister, "child_registration");

                    childRegInfo.add(item);

                } catch (Exception e) {
                    log.error(e);
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
                }  
                
                if (poDeclaration == null) {
                    poDeclaration = new PurchaseOrderDeclaration();
                    poDeclaration.setPurchaseOrderDetail(purchaseOrderDetail);
                    poDeclaration.setDeclarationForm(this.declarationForm.get(i));
                    poDeclaration.setEnable(false);               
                }

                DeclarationInfoValue<PurchaseOrderDeclaration> item = new DeclarationInfoValue<PurchaseOrderDeclaration>(declarationForm.get(i), declarationFields, i + 1, poDeclaration, "declaration");

                declarInfo.add(item);
                
            } catch (Exception e) {
                log.error(e);
            }
        }
    }
    
    private String changeCodeToText(String poFlexField, String field, List<RegistrationField> registrationFields) {
        String value = "";
        for(RegistrationField t: registrationFields){
            if(t.getName().equals(field) && t.getControlType().equals("combobox")) {
                String[] tmp = t.getItems().split(",");
                for(String item : tmp) {
                    String[] text = item.split(":");
                    if(text[0].equals(poFlexField)) {
                        value = text[1];
                        break;
                    }
                }
            }
            if(!value.equals("")) {
                break;
            }
        }
        return value;
    }
    
    public void setQaForm() {
        qaTransDetailsList = qaTransDetailDAO.findQaTransDetailByQaTransId(qaTrans);
        qaForm = questionnaireService.findQaForm(qaTrans.getQaFormId());
        if(qaForm != null) {
            qaSelectedCategoryList = questionnaireService.findQaSelectedCategoryByFormId(qaForm);
            qaFormValueList = new ArrayList<QaFormInfoValue>();
            int i = 0;
            for(QaSelectedCategory o: qaSelectedCategoryList){
                qaQuestionList = questionnaireService.findQaQuestionByCategoryId(o.getQaCategory());
                for(QaQuestion q: qaQuestionList){
                    qaChoiceList = questionnaireService.findQaChoiceByQuestionId(q);
                    QaTransDetail d = qaTransDetailsList.get(i); 
                    QaFormInfoValue qaFormInfo = new QaFormInfoValue(q, qaChoiceList, answer, remark, d, d.getSeqNo());
                    qaFormValueList.add(qaFormInfo);
                    i++;
                }
            }
        }
    }

    public RegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    public void setRegistrationForm(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }
    
    public List<ChildRegType> getChildRegType() {
        return childRegType;
    }
    
    public void setChildRegType(List<ChildRegType> childRegType) {
        this.childRegType = childRegType;
    }
    
    public List<DeclarationForm> getDeclarationForm() {
        return declarationForm;
    }

    public void setDeclarationForm(List<DeclarationForm> declarationForm) {
        this.declarationForm = declarationForm;
    }

    public List<RegistrationInfoValue> getRegInfo() {
        return regInfo;
    }
    
    public List<ChildRegInfoValue<PurchaseOrderChildRegister>> getChildRegInfo() {
        return childRegInfo;
    }
    
    public boolean showDeclarationInfo() {
        if (getDeclarInfo() != null) {
            for(DeclarationInfoValue<PurchaseOrderDeclaration> declar: getDeclarInfo()) {
                if (declar.getDeclarationEnable()) return true;
            }
        }
        return false;
    }
    
    public List<DeclarationInfoValue<PurchaseOrderDeclaration>> getDeclarInfo() {
        return declarInfo;
    }

    public void setProviceList() {
        provinceList = getProvinceDAO().getProvinceList();
    }

    public Map<String, Integer> getProvinceList() {
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

    //List
    public List<SelectItem> getDistrictList(Integer provinceId) {
        List<District> districtList = getDistrictDAO().findDistrictByProvinceId(provinceId);
        List<SelectItem> values = new ArrayList<SelectItem>();
        values.add(new SelectItem(null, JSFUtil.getBundleValue("pleaseselect")));
        for (District obj : districtList) {
            values.add(new SelectItem(obj.getId(), obj.getName()));
        }
        return values;
    }

    public List<QuestionnaireInfoValue<PurchaseOrderQuestionaire>> getQuestionnaires() {
        return questionnaires;
    }

    public List<NosaleReason> getNosaleReasonList() {
        return nosaleReasonList;
    }

    public List<FollowupsaleReason> getFollowupsaleReasonList() {
        return followupsaleReasonList;
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

    public OccupationDAO getOccupationDAO() {
        return occupationDAO;
    }

    public void setOccupationDAO(OccupationDAO occupationDAO) {
        this.occupationDAO = occupationDAO;
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
    
    public QuestionnaireService getQuestionnaireService() {
        return questionnaireService;
    }

    public void setQuestionnaireService(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }
   
    public List<QaFormInfoValue> getQaFormValueList() {
        return qaFormValueList;
    }

    public void setQaFormValueList(List<QaFormInfoValue> qaFormValueList) {
        this.qaFormValueList = qaFormValueList;
    }
    
    public ProductPlanDetailDAO getProductPlanDetailDAO() {
        return productPlanDetailDAO;
    }

    public void setProductPlanDetailDAO(ProductPlanDetailDAO productPlanDetailDAO) {
        this.productPlanDetailDAO = productPlanDetailDAO;
    }
    
    public PurchaseOrderRegisterDAO getPurchaseOrderRegisterDAO() {
        return purchaseOrderRegisterDAO;
    }

    public void setPurchaseOrderRegisterDAO(PurchaseOrderRegisterDAO purchaseOrderRegisterDAO) {
        this.purchaseOrderRegisterDAO = purchaseOrderRegisterDAO;
    }
    
    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
    
    public List<PurchaseOrderInstallment> getPoInstallmentList() {
        return poInstallmentList;
    }

    public void setPoInstallmentList(List<PurchaseOrderInstallment> poInstallmentList) {
        this.poInstallmentList = poInstallmentList;
    }

    public String getConfirmationScript() {
        return confirmScript;
    }

    public void setConfirmationScript(String confirmScript) {
        this.confirmScript = confirmScript;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getPageFrom() {
        
        if(JSFUtil.getRequest().getAttribute("page") != null && ((String)JSFUtil.getRequest().getAttribute("page")).equals("registration")){
            this.initialize();
            pageFrom = (String)JSFUtil.getRequest().getAttribute("page");
            JSFUtil.getRequest().removeAttribute("page");
        }
        return pageFrom;
    }

    public void setPageFrom(String pageFrom) {
        this.pageFrom = pageFrom;
    }
    
    //History
    /**
     * @return the customerHandlingDAO
     */
    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }

    /**
     * @param customerHandlingDAO the customerHandlingDAO to set
     */
    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }

    public SubdistrictDAO getSubdistrictDAO() {
        return subdistrictDAO;
}

    public void setSubdistrictDAO(SubdistrictDAO subdistrictDAO) {
        this.subdistrictDAO = subdistrictDAO;
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
    
    public PurchaseOrderDetail getPurchaseOrderDetail() {
        return purchaseOrderDetail;
    }

    public void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
        this.purchaseOrderDetail = purchaseOrderDetail;
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

    public String getPayerInitialText() {
        return payerInitialText;
    }

    public void setPayerInitialText(String payerInitialText) {
        this.payerInitialText = payerInitialText;
    }

    public PaymentMethodDAO getPaymentMethodDAO() {
        return paymentMethodDAO;
    }

    public void setPaymentMethodDAO(PaymentMethodDAO paymentMethodDAO) {
        this.paymentMethodDAO = paymentMethodDAO;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getPaymentMethodName(){
        if ( isPaymentMethodV2 ){
            return (this.paymentMethod==null)?"":this.paymentMethod.getName();
        }else{
            if ( this.purchaseOrder==null || this.purchaseOrder.getPaymentMethod()==null ) return "";
            else if ( "1".equals( this.purchaseOrder.getPaymentMethod()) ) return "Credit Card" ;
            else if ( "2".equals( this.purchaseOrder.getPaymentMethod()) ) return "Cash" ;
            else if ( "3".equals( this.purchaseOrder.getPaymentMethod()) ) return "Bank Transfer" ;
            else return "";
        }
    }
    public boolean isShowCreditCardInfo(){
        if ( isPaymentMethodV2 ){
            return this.paymentMethod!=null && (this.paymentMethod.isCreditcard() || this.paymentMethod.isDebitcard());
        }else{
            return this.purchaseOrder!=null && this.purchaseOrder.getPaymentMethod()!=null && "1".equals( this.purchaseOrder.getPaymentMethod() );
        }
    }

    public String getEmessage() {
        return emessage;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
   
}