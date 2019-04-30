/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.common.front.dispatch.FrontDispatcher;
import com.maxelyz.core.common.front.dispatch.RegistrationPouch;
import com.maxelyz.core.model.dao.CardIssuerDAO;
import com.maxelyz.core.model.dao.PaymentMethodDAO;
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.service.OrderService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.bean.ViewScoped;
import org.apache.poi.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

@ManagedBean
@RequestScoped
@ViewScoped
/**
 *
 * @author admin
 */
public class PaymentController {
    private static Logger log = Logger.getLogger(PaymentController.class);
    private static String SUCCESS = "/front/customerHandling/payment.xhtml";
    private static String REDIRECT_PAGE = JSFUtil.getRequestContextPath() + "/front/customerHandling/saleApproaching.jsf";
    private static String CONFIRM_PAGE = "/front/customerHandling/confirmationPo.xhtml";
    private String categoryType;
    private Boolean edit = false;
    private Boolean editPayment = false;
    private Integer noPaid = 0;
    private Double paidTotal = new Double(0);
    private Map<Integer, Integer> noInstallmentList;
    private boolean online = false;
    private Product product;
    private String pageFrom;
    private Integer poId;
    private Integer paymentMethodId;
    private String msg = "";
    private boolean havePaid;
    private String paymentStatus;
    private Boolean disableSaveButton = true;
        
    private PurchaseOrder purchaseOrder;
    private PurchaseOrderDetail purchaseOrderDetail;
    private PurchaseOrderInstallment poi;
    
    private List<PurchaseOrderInstallment> poInstallmentList;
    private List<PaymentMethod> paymentMethodList;
    private boolean fromSaleApprovalPage = false;
    private boolean showPrevious = true;
    private boolean showNext = true;
    
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value = "#{paymentMethodDAO}")
    private PaymentMethodDAO paymentMethodDAO;
    @ManagedProperty(value = "#{orderService}")
    private OrderService orderService;
    @ManagedProperty(value = "#{cardIssuerDAO}")
    private CardIssuerDAO cardIssuerDAO;
    
    @PostConstruct
    public void initialize() {
        
        //request from saleapproval page
        String mainPage = "";
        if(JSFUtil.getRequestParameterMap("mainPage") != null){
            mainPage = JSFUtil.getRequestParameterMap("mainPage");
        }else if(JSFUtil.getRequest().getAttribute("mainPage") != null){
            mainPage = (String)JSFUtil.getRequest().getAttribute("mainPage");
            JSFUtil.getRequest().removeAttribute("mainPage");
        }
        if (mainPage.equals("saleapproval")) {
            fromSaleApprovalPage = true;
            showPrevious = false;
            showNext = false;
        }
        
        msg = "";
        if(JSFUtil.getRequestParameterMap("purchaseOrderId") != null) {
            poId = Integer.parseInt(StringUtils.defaultString(JSFUtil.getRequestParameterMap("purchaseOrderId"), "0"));
        } else if(JSFUtil.getRequest().getAttribute("purchaseOrderId") != null) {
            poId = Integer.parseInt(StringUtils.defaultString((JSFUtil.getRequest().getAttribute("purchaseOrderId")).toString(), "0"));
            JSFUtil.getRequest().removeAttribute("purchaseOrderId");
        }
        if(JSFUtil.getRequest().getAttribute("purchaseOrder") != null) {
            purchaseOrder = (PurchaseOrder) JSFUtil.getRequest().getAttribute("purchaseOrder");
            if(purchaseOrder.getId() != null){
                poId = purchaseOrder.getId();
            }
            JSFUtil.getRequest().removeAttribute("purchaseOrder");
        }
        paymentMethodList = this.getPaymentMethodDAO().findPaymentMethodEntities();
        
        if(poId != 0){
            if(purchaseOrder == null){
                purchaseOrder = purchaseOrderDAO.findPurchaseOrder(poId);
            }
            if(purchaseOrder != null){
                List<PurchaseOrderDetail> podList = (List<PurchaseOrderDetail>) purchaseOrder.getPurchaseOrderDetailCollection();
                if(!podList.isEmpty()){
                    purchaseOrderDetail = podList.get(0);
                    if(purchaseOrderDetail != null){
                        product = purchaseOrderDetail.getProduct();
                        categoryType = purchaseOrderDetail.getProduct().getProductCategory().getCategoryType();
                    }
                }
                /*
                if(String.valueOf(purchaseOrder.getSaleResult()).equals("Y")){
                    if (SecurityUtil.isPermitted("admin:salesapproval:paymentapproval")) {
                        editPayment = true;
                    }else{
                        editPayment = false;
                    }
                }else{
                    editPayment = true;
                }
                */
                initPayment();
                checkBtn();
            }
        }else{
            JSFUtil.redirect(REDIRECT_PAGE);
        }
    } 
    
    public String toPayment(){        
        if(JSFUtil.getRequestParameterMap("page") != null && JSFUtil.getRequestParameterMap("page").equals("registration")){
            initialize();
        }
        return SUCCESS;
    }
    
    private void initPayment(){
        if(purchaseOrder != null && purchaseOrder.getId() != null){
            poInstallmentList = purchaseOrderDAO.findInstallment(purchaseOrder.getId());
            if(!poInstallmentList.isEmpty()){
                edit = true;
            } else {
                purchaseOrder.setNoInstallment(1);
                calculatePayment2();
            }
        }
        initNoPaid();
    }
    
    private void initNoPaid(){
    
            if(poInstallmentList != null &&!poInstallmentList.isEmpty()){
                noPaid = 0;
                paidTotal = 0.00;
                for(PurchaseOrderInstallment poi : poInstallmentList){
                    if(poi.getPaymentStatus() != null && poi.getPaymentStatus().equals("paid")){
                        noPaid++;
                        paidTotal += poi.getInstallmentAmount() != null ? poi.getInstallmentAmount().doubleValue() : 0.00;
                    }                
                }
            }
            Map<Integer, Integer> list = new LinkedHashMap<Integer, Integer>();
            for (Integer i = 1; i <= 3; i++) {
                list.put(i, i);
            }
            noInstallmentList = list;
    }
    
    public void noInstallmentValueChangeListener(ActionEvent event){
        Integer newNo = purchaseOrder.getNoInstallment();
        purchaseOrder.setNoInstallment(newNo);
        calculatePayment2();
    }
    
    private void calculatePayment2(){

        List<PurchaseOrderInstallment> tmpList = new ArrayList<PurchaseOrderInstallment>();
        initNoPaid();
        Integer newNo = purchaseOrder.getNoInstallment();
        if(newNo != 0){
            Double a = new Double(0);
            Double s1 = new Double(0);
            //initNoPaid();
            if(!edit){
                if(purchaseOrder.getTotalAmount() != null && purchaseOrder.getTotalAmount() != 0){
                    a = Double.parseDouble(Math.round(purchaseOrder.getTotalAmount()/newNo) + "");
                }
                poInstallmentList = new ArrayList<PurchaseOrderInstallment>();
                for (int i = 1; i <= newNo.intValue(); i++) {
                    PurchaseOrderInstallment poi = new PurchaseOrderInstallment();
                    poi.setDueDate(new Date());
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
            }else{
                if(poInstallmentList.size() < newNo.intValue()){
                    for(PurchaseOrderInstallment poi : poInstallmentList){
                        //if(poi.getPaymentStatus().equals("paid")){
                            s1 += poi.getInstallmentAmount() != null ? poi.getInstallmentAmount().doubleValue() : 0.00;
                        //} else {
                        //    s1 += a;
                        //    poi.setInstallmentAmount(BigDecimal.valueOf(a));
                        //}
                        tmpList.add(poi);
                    }
                    for(int i = (poInstallmentList.size() + 1); i <= newNo.intValue(); i++){
                        PurchaseOrderInstallment poi = new PurchaseOrderInstallment();
                        poi.setDueDate(new Date());
                        poi.setInstallmentNo(i);
                        poi.setPaymentStatus("notpaid");
                        if(i < newNo.intValue()){
                        //    s1 += a;
                            poi.setInstallmentAmount(BigDecimal.valueOf(0));
                            tmpList.add(poi);
                        }else if(i == newNo.intValue()){
                            poi.setInstallmentAmount(BigDecimal.valueOf(purchaseOrder.getTotalAmount() - s1));
                            tmpList.add(poi);
                            break;
                        }
                    }
                }else{
                    for(PurchaseOrderInstallment poi : poInstallmentList){
                        if(poi.getInstallmentNo() < newNo.intValue()){
                            //if(poi.getPaymentStatus().equals("paid")){
                                s1 += poi.getInstallmentAmount() != null ? poi.getInstallmentAmount().doubleValue() : 0.00;
                            //} else {
                            //    s1 += a;
                            //    poi.setInstallmentAmount(BigDecimal.valueOf(a));
                            //}
                            tmpList.add(poi);
                        }else if(poi.getInstallmentNo() == newNo.intValue()){
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
        initNoPaid();
    }
    
    public void dueDateListener(ValueChangeEvent event){
        Date dd = (Date) event.getNewValue();
        if(poInstallmentList != null && !poInstallmentList.isEmpty()){
            for(PurchaseOrderInstallment poi : poInstallmentList){
                if(purchaseOrder.getNoInstallment().intValue() == poi.getInstallmentNo()){
                    if(poi.getPaymentStatus() == null || !poi.getPaymentStatus().equals("paid")){
                        if(poi.getDueDate() != null){
                            if(dd.before(poi.getDueDate())){
                                poi.setDueDate(dd);
                            }
                        }else{
                            poi.setDueDate(dd);
                        }
                    }
                }
            }
        }
    }
    
    public void installmentNoListener(ActionEvent event){
        Integer installmentNo = Integer.parseInt(JSFUtil.getRequestParameterMap("installmentNo"));
        for (PurchaseOrderInstallment o : poInstallmentList) {
            if (o.getInstallmentNo() == installmentNo.intValue()) {
                poi = o;
                break;
            }
        }
        if (poi.getPaymentMethod() == null) {
            poi.setPaymentMethod(new PaymentMethod());
        }
        paymentStatus = poi.getPaymentStatus();
        if(poi.getCardExpiryMonth() != null && poi.getCardExpiryMonth() == 0) poi.setCardExpiryMonth(null);
        if(poi.getCardExpiryYear() != null && poi.getCardExpiryYear() == 0) poi.setCardExpiryYear(null);
        msg = "";
        this.toCheckStatusPaid();
    }
    
    private void toCheckStatusPaid(){
        havePaid = false;
        for (PurchaseOrderInstallment o : poInstallmentList) {
            if(poi.getInstallmentNo() == 1){
                havePaid = true;
            }else{
                if (poi.getInstallmentNo() > o.getInstallmentNo()) {
                    if(o.getPaymentStatus().equals("paid")){
                        havePaid = true;
                    }else{
                        havePaid = false;
                    }
                }
            }
        }
    }
    
    public void checkOnlineListener(ValueChangeEvent event){
        Object obj = event.getNewValue();
        Integer id = (Integer) obj; 
        try {
            PaymentMethod paymentMethod = this.getPaymentMethodDAO().findPaymentMethod(id);
            poi.setPaymentMethod(paymentMethod);
            if(paymentMethod.getOnline()) {
                online = true;
            } else {
                online = false;
            }
        } catch(Exception e){
            log.error(e);
        }
    }
    
    public void paymentStatusListener(ValueChangeEvent event){
        Object obj = event.getNewValue();
        String str = (String) obj; 
        try {
            if(str.equals("paid")){
                paymentStatus = "paid";
            }else{
                paymentStatus = "notpaid";
            }
        } catch(Exception e){
            log.error(e);
        }
    }
    
    public String saveAction() {        
        
        try{
            
            this.savePurchaseOrderInstallmentList();
            
        }catch(Exception e){
            e.printStackTrace();
            return SUCCESS;
        }
        //JSFUtil.getUserSession().setShowContactSummary(true);
        JSFUtil.getUserSession().setPurchaseOrders(purchaseOrder); //set po to usersession
        JSFUtil.getRequest().setAttribute("purchaseOrderId", purchaseOrder.getId());
        JSFUtil.getRequest().setAttribute("productId", product.getId());
        JSFUtil.getRequest().setAttribute("page", "payment");
        if (fromSaleApprovalPage) {
            return "paymentforsaleapprovalthankyou.xhtml";
        }
        return CONFIRM_PAGE;
    }
    
    private void savePurchaseOrderInstallmentList(){
        try{
            //begin check for approval
            if(purchaseOrder.getPaymentStatus() != null && !purchaseOrder.getPaymentStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
                Double paidTotalAmount = new Double(0);
                for(PurchaseOrderInstallment poi : poInstallmentList){
                    if(poi.getPaymentStatus() != null && poi.getPaymentStatus().equals("paid")){
                        paidTotalAmount += poi.getInstallmentAmount() != null ? poi.getInstallmentAmount().doubleValue() : 0.00;
                    }                
                }
                if(paidTotalAmount.doubleValue() == purchaseOrder.getTotalAmount().doubleValue()){
                    purchaseOrder.setPaymentStatus(JSFUtil.getBundleValue("approvalApprovedValue"));
                    purchaseOrder.setPaymentBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    purchaseOrder.setPaymentByUser(JSFUtil.getUserSession().getUsers());
                    purchaseOrder.setPaymentDate(new Date());

                    //save PurchaseOrderApprovalLog
                    try{
                        PurchaseOrderApprovalLog purchaseOrderApprovalLog = new PurchaseOrderApprovalLog();
                        purchaseOrderApprovalLog.setApprovalStatus(JSFUtil.getBundleValue("approvalApprovedValue"));
                        purchaseOrderApprovalLog.setCreateByRole("Payment");
                        purchaseOrderApprovalLog.setPurchaseOrder(purchaseOrder);
                        purchaseOrderApprovalLog.setUsers(JSFUtil.getUserSession().getUsers());
                        purchaseOrderApprovalLog.setCreateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                        purchaseOrderApprovalLog.setCreateDate(new Date());
                        if(purchaseOrder.getApprovalStatus() != null && purchaseOrder.getApprovalStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
                            purchaseOrderApprovalLog.setToRole("Qc");
                        }else{
                            purchaseOrderApprovalLog.setToRole("Uw");
                        }
                        
                        purchaseOrderDAO.createApprovalLog(purchaseOrderApprovalLog);
                    }catch(Exception e){
                        log.error(e);
                    }
                }
            }
            //end check for approval
            purchaseOrderDAO.saveNoInstallment(purchaseOrder);
            
            purchaseOrderDAO.deletePurchaseOrderInstallment(purchaseOrder.getId());
            if(purchaseOrder.getNoInstallment() != null && purchaseOrder.getNoInstallment() != 0){
                for(PurchaseOrderInstallment poi : poInstallmentList){
                    poi.setId(null);
                    poi.setPurchaseOrder(purchaseOrder);
                    poi.setInstallmentRefNo(purchaseOrder.getRefNo() + poi.getInstallmentNo());
                    poi.setUpdateDate(new Date());
                    poi.setUpdateBy(JSFUtil.getUserSession().getUserName());
                    poi.setUpdateById(JSFUtil.getUserSession().getUsers().getId());
                    purchaseOrderDAO.createInstallment(poi);
                }
            }        
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void savePaymentListener(ActionEvent event){
        //initNoPaid();
        edit = true;
        poi.setPaymentStatus(paymentStatus);
        poi.setUpdateBy(JSFUtil.getUserSession().getUserName());
        poi.setUpdateById(JSFUtil.getUserSession().getUsers().getId());
        poi.setUpdateDate(new Date());
        calculatePayment2();
        this.savePurchaseOrderInstallmentList();
    }

    public String backAction() {
        JSFUtil.getRequest().setAttribute("page", "payment");
        if (fromSaleApprovalPage) {
            JSFUtil.getRequest().setAttribute("poId", purchaseOrder.getId());
            JSFUtil.getRequest().setAttribute("mainPage", "saleapproval");
            return "registrationforsaleapproval.xhtml";
        }else{
            try{
                RegistrationPouch pouch = new RegistrationPouch(); // create pouch to carry data for purchase order        
                pouch.registeredPouchType(RegistrationPouch.SENDER_TYPE.SALEHISTORY, RegistrationPouch.RECEIVER_MODE.EDIT);
                pouch.putEditModeParameter(purchaseOrder.getId());
                FrontDispatcher.keepPouch(pouch); // registered pouch on dispatcher
                return FrontDispatcher.getPouchReceiver(pouch); // get receiver
            }catch(Exception e){
                log.error(e);
                return null; 
            }   
        }  
    }

    public String nextAction() {
        
        JSFUtil.getUserSession().setPurchaseOrders(purchaseOrder); //set po to usersession
        JSFUtil.getRequest().setAttribute("purchaseOrderId", purchaseOrder.getId());
        JSFUtil.getRequest().setAttribute("productId", product.getId());
        JSFUtil.getRequest().setAttribute("page", "payment");

        return CONFIRM_PAGE;
    }
    
    public void paymentOnlineActionListener(ActionEvent event) {
        String bblResult = "";
        boolean paymentOnlineStatus = false;
        String paidDate = "";
        Double totalPaidAmount = Double.valueOf(0.00);
        String authId = "";
        for (PurchaseOrderInstallment obj : poInstallmentList) {
            if (obj.getPaymentStatus().equals("paid")) {
                totalPaidAmount += (obj.getInstallmentAmount() != null ? obj.getInstallmentAmount().doubleValue() : new Double(0));
            }
        }
        Double amount = poi.getInstallmentAmount() != null ? poi.getInstallmentAmount().doubleValue() : new Double(0);
        boolean canPay = false;
        if (purchaseOrder.getNoInstallment().intValue() == poi.getInstallmentNo() && (purchaseOrder.getTotalAmount() - totalPaidAmount) == amount) {
            canPay = true;
        } else if(poi.getInstallmentNo() < purchaseOrder.getNoInstallment().intValue()){
            canPay = true;            
        } else {
            canPay = false;
            msg = "Amount is incorrect.";
        }
        if(canPay){
            boolean chk = this.checkPostData();
            if (chk) {
                String postData = "merchantId="+JSFUtil.getApplication().getPaymentGatewayBBLMerchantId()
                        + "&orderRef=" + StringUtils.trim(purchaseOrder.getRefNo()) + poi.getInstallmentNo()
                        + "&amount=" + poi.getInstallmentAmount()
                        + "&currCode=764"
                        + "&lang=E"
                        + "&pMethod=" + StringUtils.trim(poi.getCardType())
                        + "&epMonth=" + (poi.getCardExpiryMonth() < 10 ? "0" : "") + poi.getCardExpiryMonth().toString()
                        + "&epYear=" + "20" + (poi.getCardExpiryYear() < 10 ? "0" : "") + poi.getCardExpiryYear().toString()
                        + "&cardNo=" + poi.getCardNo()
                        + "&cardHolder=" + poi.getCardHolderName()
                        + "&payType=N"
                        + "&remark=tvi";
                //String postData = "merchantId=1089&orderRef=120400011&amount=0.5&currCode=764&lang=E&pMethod=VISA&epMonth=03&epYear=14&cardNo=4391370003291263&cardHolder=UKRIT JUICHAROEN&payType=N&remark=test_tvi2";
                
                try {
                    Map<String, String> resultMap = orderService.bblPost(JSFUtil.getUserSession().getUserName(), postData, JSFUtil.getApplication().getPaymentGatewayBBLUrl());
                    for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                        bblResult += "key:" + StringUtils.trim(entry.getKey()) + "value:" + StringUtils.trim(entry.getValue());
                        if (StringUtils.trim(entry.getKey()).equals("successcode")) {
                            if (StringUtils.trim(entry.getValue()).equalsIgnoreCase("0")) {
                                paymentOnlineStatus = true;
                            } else {
                                paymentOnlineStatus = false;
                            }
                        }
                        if (StringUtils.trim(entry.getKey()).equalsIgnoreCase("TxTime")) {
                            paidDate = StringUtils.trim(entry.getValue());
                        }
                        if (StringUtils.trim(entry.getKey()).equalsIgnoreCase("AuthId")) {
                            authId = StringUtils.trim(entry.getValue());
                        }
                    }
                } catch (Exception ex) {
                    bblResult = ex.getMessage();
                }
                
                
                //Test
                //paymentOnlineStatus = true; //Test
                //paidDate = "2012-04-27 17:55:59.0"; //Test
                
                if (paymentOnlineStatus) {
                    Date date = null;
                    poi.setPaymentStatus("paid");
                    poi.setAuthId(authId);
                    paymentStatus = poi.getPaymentStatus();
                    if (!paidDate.equals("")) {
                        try {
                            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.0",Locale.US);
                            date = (Date) formatter.parse(paidDate);
                            poi.setPaidDate(date);
                        } catch (ParseException e) {
                            System.out.println("Exception :" + e);
                        }
                    }
                    purchaseOrder.setSettlement(true);
                    purchaseOrder.setSettlementBy(JSFUtil.getUserSession().getUserName());
                    purchaseOrder.setSettlementDate(date);
                    edit = true;
                    calculatePayment2();
                    //this.savePurchaseOrderInstallmentList();
                    msg = "Transaction Completed.";
                    
                    //begin check for approval
                    Double paidTotalAmount = new Double(0);
                    for(PurchaseOrderInstallment poi : poInstallmentList){
                        if(poi.getPaymentStatus() != null && poi.getPaymentStatus().equals("paid")){
                            paidTotalAmount += poi.getInstallmentAmount() != null ? poi.getInstallmentAmount().doubleValue() : 0.00;
                        }                
                    }
                    if(paidTotalAmount.doubleValue() == purchaseOrder.getTotalAmount().doubleValue()){
                        purchaseOrder.setPaymentStatus(JSFUtil.getBundleValue("approvalApprovedValue"));
                        purchaseOrder.setPaymentBy("System");
                        purchaseOrder.setPaymentByUser(JSFUtil.getUserSession().getUsers());
                        purchaseOrder.setPaymentDate(new Date());
                        
                        //save PurchaseOrderApprovalLog
                        try{
                            PurchaseOrderApprovalLog purchaseOrderApprovalLog = new PurchaseOrderApprovalLog();
                            purchaseOrderApprovalLog.setApprovalStatus(JSFUtil.getBundleValue("approvalApprovedValue"));
                            purchaseOrderApprovalLog.setCreateByRole("Payment");
                            purchaseOrderApprovalLog.setPurchaseOrder(purchaseOrder);
                            purchaseOrderApprovalLog.setUsers(JSFUtil.getUserSession().getUsers());
                            purchaseOrderApprovalLog.setCreateBy("System");
                            purchaseOrderApprovalLog.setCreateDate(new Date());
                            if(purchaseOrder.getApprovalStatus() != null && purchaseOrder.getApprovalStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
                                purchaseOrderApprovalLog.setToRole("Qc");
                            }else{
                                purchaseOrderApprovalLog.setToRole("Uw");
                            }
                            purchaseOrderDAO.createApprovalLog(purchaseOrderApprovalLog);
                        }catch(Exception e){
                            log.error(e);
                        }
                    }
                    //end check for approval
                    purchaseOrderDAO.saveNoInstallment(purchaseOrder);

                    purchaseOrderDAO.deletePurchaseOrderInstallment(purchaseOrder.getId());
                    if(purchaseOrder.getNoInstallment() != null && purchaseOrder.getNoInstallment() != 0){
                        for(PurchaseOrderInstallment poi : poInstallmentList){
                            poi.setId(null);
                            poi.setPurchaseOrder(purchaseOrder);
                            poi.setInstallmentRefNo(purchaseOrder.getRefNo() + poi.getInstallmentNo());
                            poi.setUpdateDate(new Date());
                            poi.setUpdateBy(JSFUtil.getUserSession().getUserName());
                            poi.setUpdateById(JSFUtil.getUserSession().getUsers().getId());
                            try{
                                purchaseOrderDAO.createInstallment(poi);
                            }catch(Exception e){
                                log.error(e);
                            }
                        }
                    }        
                }else{
                    msg = "Transaction Not Completed. "+bblResult;
                }
            }
        }
    }
    
    private boolean checkPostData(){
        boolean chk = false;
        if(purchaseOrder.getRefNo() == null 
                || poi.getInstallmentAmount() == null 
                || poi.getCardType() == null 
                || poi.getCardExpiryMonth() == null
                || poi.getCardExpiryYear() == null
                || poi.getCardNo() == null
                || poi.getCardHolderName() == null
                || purchaseOrder.getRefNo().equals("") 
                || poi.getInstallmentNo() == 0 
                || poi.getInstallmentAmount() == BigDecimal.valueOf(0.00) 
                || poi.getCardType().equals("") 
                || poi.getCardExpiryMonth() == 0
                || poi.getCardExpiryYear() == 0
                || poi.getCardNo().equals("")
                || poi.getCardHolderName().equals("")) {
            chk = false;
        
        } else {
            chk = true;
        }
        return chk;
    }
    
    public void cardNoActionListener(ActionEvent event) {
        if(poi.getCardNo() != null && poi.getCardNo().length() == 16){
            String str = poi.getCardNo();
            CardIssuer obj = cardIssuerDAO.findByCardNo(StringUtils.substring(str, 0, 6));
            if(obj != null){
                poi.setCardType(obj.getCardType());
                poi.setCardIssuerCountry(obj.getIssuerCountry());
                poi.setCardIssuerName(obj.getIssuerName());
            }else{
                poi.setCardType("");
                poi.setCardIssuerCountry("");
                poi.setCardIssuerName("");
            }
        }else{
            poi.setCardType("");
            poi.setCardIssuerCountry("");
            poi.setCardIssuerName("");        
        }
    }
    
    public boolean isEditPermitted() {
        if(JSFUtil.getUserSession().getUsers().getUserGroup().getRole().equals("Agent"))
            return true;
        else
            return SecurityUtil.isPermitted("admin:salesapproval:paymentapproval");
    }
               
    private void checkBtn(){

        String role = JSFUtil.getUserSession().getUsers().getUserGroup().getRole();
        if(role.contains("Agent")){
            if(purchaseOrder.getQcStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                    || purchaseOrder.getPaymentStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
                disableSaveButton = false;
            }else{
                disableSaveButton = true;
            }
        } else if(role.contains("Uw") && role.contains("Payment") && role.contains("Qc")) {
             if(purchaseOrder.getApprovalStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue")) &&
                purchaseOrder.getPaymentStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue")) &&
                purchaseOrder.getQcStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
                disableSaveButton = false;
            } else {
                disableSaveButton = true;
            }
        } else if(role.contains("Uw") && role.contains("Payment")) {
             if(purchaseOrder.getApprovalStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue")) &&
                purchaseOrder.getPaymentStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
                disableSaveButton = false;
            } else {
                disableSaveButton = true;
            }
        } else if(role.contains("Uw") && role.contains("Qc")) {
             if(purchaseOrder.getApprovalStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue")) &&
                purchaseOrder.getQcStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
                disableSaveButton = false;
            } else {
                disableSaveButton = true;
            }
        } else if(role.contains("Payment") && role.contains("Qc")) {
             if(purchaseOrder.getPaymentStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue")) &&
                purchaseOrder.getQcStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
                disableSaveButton = false;
            } else {
                disableSaveButton = true;
            }
        } else if(role.contains("Uw")) {
            disableSaveButton = false;
            //if(purchaseOrder.getApprovalStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
            //    disableSaveButton = false;
            //} else {
            //    disableSaveButton = true;
            //}
        } else if(role.contains("Payment")) {
            disableSaveButton = true;
            //if(purchaseOrder.getPaymentStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
            //    disableSaveButton = false;
            //} else {
            //    disableSaveButton = true;
            //}
        } else if(role.contains("Qc")) {
            disableSaveButton = false;
            //if(purchaseOrder.getQcStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
            //    disableSaveButton = false;
            //} else {
            //    disableSaveButton = true;
            //}
        } else if(role.contains("CampaignManager")){
            disableSaveButton = false;
        } else if(role.contains("Supervisor")){
            disableSaveButton = false;
        }   
    }


    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public List<PurchaseOrderInstallment> getPoInstallmentList() {
        return poInstallmentList;
    }

    public void setPoInstallmentList(List<PurchaseOrderInstallment> poInstallmentList) {
        this.poInstallmentList = poInstallmentList;
    }

    public Boolean getEditPayment() {
        return editPayment;
    }

    public void setEditPayment(Boolean editPayment) {
        this.editPayment = editPayment;
    }

    public Map<Integer, Integer> getNoInstallmentList() {
        return noInstallmentList;
    }

    public void setNoInstallmentList(Map<Integer, Integer> noInstallmentList) {
        this.noInstallmentList = noInstallmentList;
    }

    public Integer getNoPaid() {
        return noPaid;
    }

    public void setNoPaid(Integer noPaid) {
        this.noPaid = noPaid;
    }

    public Double getPaidTotal() {
        return paidTotal;
    }

    public void setPaidTotal(Double paidTotal) {
        this.paidTotal = paidTotal;
    }

    public List<PaymentMethod> getPaymentMethodList() {
        return paymentMethodList;
    }

    public void setPaymentMethodList(List<PaymentMethod> paymentMethodList) {
        this.paymentMethodList = paymentMethodList;
    }

    public PurchaseOrderInstallment getPoi() {
        return poi;
    }

    public void setPoi(PurchaseOrderInstallment poi) {
        this.poi = poi;
    }

    public PaymentMethodDAO getPaymentMethodDAO() {
        return paymentMethodDAO;
    }

    public void setPaymentMethodDAO(PaymentMethodDAO paymentMethodDAO) {
        this.paymentMethodDAO = paymentMethodDAO;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getPageFrom() {        
        if(JSFUtil.getRequest().getAttribute("page") != null && ((String)JSFUtil.getRequest().getAttribute("page")).equals("registration")){
            this.initialize();
            pageFrom = (String)JSFUtil.getRequest().getAttribute("page");
        } else if(JSFUtil.getRequest().getAttribute("page") != null && ((String)JSFUtil.getRequest().getAttribute("page")).equals("confirmation")){
            this.initialize();
            pageFrom = (String)JSFUtil.getRequest().getAttribute("page");
        }
        
        JSFUtil.getRequest().removeAttribute("page");
        
        return pageFrom;
    }

    public void setPageFrom(String pageFrom) {
        this.pageFrom = pageFrom;
    }

    public Integer getPoId() {
        return poId;
    }

    public void setPoId(Integer poId) {
        this.poId = poId;
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public CardIssuerDAO getCardIssuerDAO() {
        return cardIssuerDAO;
    }

    public void setCardIssuerDAO(CardIssuerDAO cardIssuerDAO) {
        this.cardIssuerDAO = cardIssuerDAO;
    }

    public boolean isHavePaid() {
        return havePaid;
    }

    public void setHavePaid(boolean havePaid) {
        this.havePaid = havePaid;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isFromSaleApprovalPage() {
        return fromSaleApprovalPage;
    }

    public void setFromSaleApprovalPage(boolean fromSaleApprovalPage) {
        this.fromSaleApprovalPage = fromSaleApprovalPage;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }

    public boolean isShowPrevious() {
        return showPrevious;
    }

    public void setShowPrevious(boolean showPrevious) {
        this.showPrevious = showPrevious;
    }

    public PurchaseOrderDetail getPurchaseOrderDetail() {
        return purchaseOrderDetail;
    }

    public void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
        this.purchaseOrderDetail = purchaseOrderDetail;
    }
    
    public static void main(String[] arg){
        String str = "xxx Juicharoen";
        StringUtils.replace(str, "xxx", "Ukrit");
        System.out.println(str);
        StringUtils.replace(str, "Juicharoen", "AAA");
        System.out.println(str);
        
        try {
            String str_date = "2012-01-01 09:10:11:0";
            DateFormat formatter;
            Date date;
            formatter = new SimpleDateFormat("dd-MMM-yy", Locale.US);
            date = (Date) formatter.parse(str_date);
            System.out.println("Today is " + date);
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
        }

    }
    
    public Boolean getDisableSaveButton() {
        return disableSaveButton;
    }

    public void setDisableSaveButton(Boolean disableSaveButton) {
        this.disableSaveButton = disableSaveButton;
    }
}
