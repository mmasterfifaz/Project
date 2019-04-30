/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

/**
 *
 * @author admin
 */
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.ContactRecordValue;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.core.service.NextSequenceService;
import com.maxelyz.utils.JSFUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import org.richfaces.event.ItemChangeEvent;
import sun.security.provider.MD5;

@ManagedBean
@ViewScoped
public class SaleApproveEditController {

    private static Logger log = Logger.getLogger(SaleApproveEditController.class);
    private static String SUCCESS = "saleapprove.jsf";
    private PurchaseOrder po;
    private String fromPage = "";
    private int flowApproval;
    private boolean showUwStatus = false;
    private boolean showPaymentStatus = false;
    private boolean showQcStatus = false;
    private boolean showAgentStatus = false;
    private boolean showSupStatus = false;
    private boolean showConfirmStatus = false;
    private boolean btnSave = false;
    private boolean uwEnable = false;
    private boolean paymentEnable = false;
    private boolean qcEnable = false;
    private boolean supEnable = false;
    private boolean confirmEnable = false;
    private String status;
    private String uwDelegate = "";
    private Integer uwReasonId;
    private String uwMessage;
    private String uwMessage2;
    private String qcDelegate = "";
    private Integer qcReasonId;
    private String qcMessage;
    private String paymentDelegate = "";
    private Integer paymentReasonId;
    private String paymentMessage;
    private String agentDelegate;
    private Integer agentReasonId;
    private String agentMessage;
    private Integer supReasonId;
    private String supMessage;
    private String supDelegate;
    private String confirmMessage;
    private String confirmDelegate;
    private String mode;
    private String tabName;
    private String uwStatus;
    private String qcStatus;
    private String paymentStatus;
    private String agentStatus;
    private String supStatus;
    private String otherExclusionCode;
    private String otherExclusionName;
    private Integer poId;
    private String ckSpouse;
    private Integer mainPoId;

    private boolean disableUwSaveBtn = true;
    private boolean disableQcSaveBtn = true;
    private boolean disablePaymentSaveBtn = true;
    private boolean disableAgentSaveBtn = true;
    private boolean disableSupSaveBtn = true;

    private boolean disablePaymentApprove = false;

    private boolean uwEnableDelegate = false;
    private boolean paymentEnableDelegate = false;
    private boolean qcEnableDelegate = false;
    private boolean supEnableDelegate = true;

    private boolean enableReferenceNo = false;

    private List<ContactRecordValue> list;
    private List<PurchaseOrderApprovalLog> purchaseOrderApprovalLogList;

    private List<ApprovalReason> approvalReasonList;
    private List<ApprovalReason> approvalReasonUwList;
    private List<ApprovalReason> approvalReasonQcList;
    private List<ApprovalReason> approvalReasonPaymentList;
    private List<ApprovalReason> approvalReasonAgentList;
    private List<ApprovalReason> approvalReasonSupList;
    private Map<String, String> statusList;
    private Map<String, Integer> approvalExclusionList;
    private List<ApprovalExclusion> approvalExclusions;
    private List<Integer> selectedApprovalExclusionList = new ArrayList<Integer>();

    private List<ProductWorkflow> productWorkflowList;
    private ProductWorkflow productWorkflow;

    private PurchaseOrder poForMainInsure;
    private List<PurchaseOrderDetail> podForFamily;
    private boolean familyProduct;
    
    private String msgUwSaveRemark = "";
    private String msgQcSaveRemark = "";
    private String msgPaymentSaveRemark = "";
    private PaymentMethod paymentMethod;
    private String paymentBody;
    private String parammeter;
    private String messageFromSmartBroker ="";
    private String meassageSendSMS = "";
    private boolean renderUpdatetoSmartBroker = false;
    private boolean renderSendSMS = false;

    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value = "#{contactHistoryDAO}")
    private ContactHistoryDAO contactHistoryDAO;
    @ManagedProperty(value = "#{approvalReasonDAO}")
    private ApprovalReasonDAO approvalReasonDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{approvalExclusionDAO}")
    private ApprovalExclusionDAO approvalExclusionDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{productWorkflowDAO}")
    private ProductWorkflowDAO productWorkflowDAO;
    @ManagedProperty(value = "#{nextSequenceService}")
    private NextSequenceService nextSequenceService;
    @ManagedProperty(value = "#{purchaseOrderDetailDAO}")
    private PurchaseOrderDetailDAO purchaseOrderDetailDAO;
    @ManagedProperty(value = "#{paymentMethodDAO}")
    private PaymentMethodDAO paymentMethodDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:salesapproval:view") && !SecurityUtil.isPermitted("saleapproval:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        flowApproval = JSFUtil.getApplication().getSaleApproveFlow();
        showPaymentStatus = false;
        showQcStatus = false;
        if (JSFUtil.getRequestParameterMap("poId") != null && !JSFUtil.getRequestParameterMap("poId").equals("")) {
            poId = Integer.parseInt(JSFUtil.getRequestParameterMap("poId"));
            if (poId != null && poId != 0) {
                updateStatus(poId);
                po = purchaseOrderDAO.findPurchaseOrder(poId);              
                if(po.getMainPoId()!=null && po.getMainPoId() != 0) {
                    podForFamily = purchaseOrderDetailDAO.findFamilyPurchaseOrderDetail(po.getMainPoId());
                    //poForMainInsure = purchaseOrderDAO.findPurchaseOrder(po.getMainPoId());
                    //podForFamily = purchaseOrderDetailDAO.findPurchaseOrderDetailByPoId(poForMainInsure.getId());
                }else{
                    poForMainInsure = null;
                    podForFamily = purchaseOrderDetailDAO.findFamilyPurchaseOrderDetail(po.getId());
                    //podForFamily = purchaseOrderDetailDAO.findPurchaseOrderDetailByMainPoId(po.getId());
                }
                if (po != null) {
                    List<PurchaseOrderDetail> pods = (List<PurchaseOrderDetail>) po.getPurchaseOrderDetailCollection();
                    productWorkflowList = productWorkflowDAO.findProductWorkflowByProductId(pods.get(0).getProduct().getId());
                }
            }
        }

        messageFromSmartBroker ="";
        meassageSendSMS = "";

        if(po.getSmsSubmitDate()!=null && po.getSmsResult()!=null && !po.getSmsResult().equals("") && !po.getSmsSubmitDate().equals("")){
            renderSendSMS = true;
        }

        if(po.getPaymentSubmitDate()!=null && !po.getPaymentSubmitDate().equals("")){
            renderUpdatetoSmartBroker = true;
        }

        if (productWorkflowList != null && !productWorkflowList.isEmpty()) {
            productWorkflow = this.getCurrentProductWorkflow();
        }

        if (JSFUtil.getRequestParameterMap("fromPage") != null && !JSFUtil.getRequestParameterMap("fromPage").equals("")) {
            fromPage = JSFUtil.getRequestParameterMap("fromPage");
        }
             
        if (po != null && po.getAssignmentDetail() != null) {
            list = contactHistoryDAO.findByAssignmentDetailId(po.getAssignmentDetail().getId());
        } else {
            list = null;
        }

        if (po != null && po.getPaymentMethod() != null && !po.getPaymentMethod().equals("")){
            try {
                paymentMethod = paymentMethodDAO.findPaymentMethod(new Integer(po.getPaymentMethod()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (JSFUtil.getRequestParameterMap("ckSpouse") != null && !JSFUtil.getRequestParameterMap("ckSpouse").equals("")) {
            ckSpouse = JSFUtil.getRequestParameterMap("ckSpouse");
        }

        if (JSFUtil.getRequestParameterMap("mainPoId") != null && !JSFUtil.getRequestParameterMap("mainPoId").equals("")) {
            mainPoId = Integer.parseInt(JSFUtil.getRequestParameterMap("mainPoId"));
        }
       
        if (JSFUtil.getRequestParameterMap("familyProduct") != null ) {
            familyProduct = Boolean.parseBoolean(JSFUtil.getRequestParameterMap("familyProduct"));
        }


        
        checkBtn();
        initList();
        statusList = this.getActivityList();
        uwStatus = po != null ? (po.getApprovalStatus() == null ? "" : po.getApprovalStatus()) : "";
        qcStatus = po != null ? (po.getQcStatus() == null ? "" : po.getQcStatus()) : "";
        paymentStatus = po != null ? (po.getPaymentStatus() == null ? "" : po.getPaymentStatus()) : "";
        approvalExclusions = approvalExclusionDAO.findApprovalExclusionEntities();
        if (po != null && po.getLatestDelegateToRole() != null && po.getLatestDelegateToRole().toUpperCase().equals("SUPERVISOR")) {
            supEnable = true;
            if (po.getLatestDelegateFromRole() != null && !po.getLatestDelegateFromRole().equals("")) {
                this.supDelegate = po.getLatestDelegateFromRole();
            }
        }

        this.enableReferenceNo = JSFUtil.getApplication().isExternalRefNo();
    }

    public String convertNullToString(String data){
        if(data == null) {
            return "";
        }else {
            return data;
        }
    }

    public void sendSMS(){
        messageFromSmartBroker ="";
        meassageSendSMS ="";
        String phonenumber ="";
        List<PurchaseOrderDetail> pods = (List<PurchaseOrderDetail>) po.getPurchaseOrderDetailCollection();
        for(PurchaseOrderRegister por : pods.get(0).getPurchaseOrderRegisterCollection()){
            phonenumber = por.getMobilePhone().substring(1,por.getMobilePhone().length());
            break;
        }
        SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        JsonObject json = new JsonObject();
        String transaction_id = dt.format(new Date());
        String project_id = "117";
        String sender = "BigCBroker";
        String msisdn = "66"+phonenumber;
        String msg = "";
        String pwd = "0D6tFr5H";

        try {
            msg = URLEncoder.encode("คลิกเพื่อชำระเบี้ยประกันรถยนต์ "+ po.getPaymentGatewayUrl(), "UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }
        String hash = getMd5("transaction_id="+transaction_id+"&project_id="+project_id+"&sender="+sender+"&msisdn="+msisdn+"&msg="+msg+"&pwd="+pwd);
        json.addProperty("transaction_id",transaction_id);
        json.addProperty("project_id",project_id);
        json.addProperty("sender", sender);
        json.addProperty("msisdn",msisdn);
        json.addProperty("msg", msg);
        json.addProperty("session", hash);

        String Url = "https://ppro-smsapi.eggdigital.com/sms-api/sms_single"; //TEST
        //String Url = "https://smsapi.eggdigital.com/sms-api/sms_single"; //Production
        String responseBody = "";
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream in = null;
        try {

            URL url = new URL(Url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            os = conn.getOutputStream();
            os.write(json.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            if (conn.getResponseCode() != 200) {
                msg = conn.getResponseMessage();
                if( conn.getResponseCode() == 500){
                    meassageSendSMS = "Cloud not get any response. please check service. path= "+Url;
                }
                if( conn.getResponseCode() == 404){
                    meassageSendSMS = "Criteria is not match. Code: "+conn.getResponseCode();
                }
                //throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());

            }else {
                in = new BufferedInputStream(conn.getInputStream());
                responseBody = IOUtils.toString(in, "UTF-8");
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(responseBody).getAsJsonObject();
                String success = rootObj.get("code").getAsString();
                if(success.equals("000")) {
                    meassageSendSMS = "";
                    //meassageSendSMS = rootObj.get("status").getAsString();
                    purchaseOrderDAO.updateSentSMS(po.getId());
                    renderSendSMS =true;
                } else {
                    meassageSendSMS = rootObj.get("status").getAsString();
                }
            }
            if(in!=null) {
                in.close();
            }
            if(conn!=null) {
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            msg = e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            msg = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        } finally {

        }
    }

    public void updatePaymentToSmartBroker(){
        messageFromSmartBroker ="";
        meassageSendSMS ="";
        if((po.getPaymentGatewaySubmitDate()!=null && !po.getPaymentGatewaySubmitDate().equals("") && po.getPaymentReferenceNo1() != null && !po.getPaymentReferenceNo1().trim().equals(""))
                || (paymentMethod.getCode().equals("CCA")) || paymentMethod.getCode().equals("CSH")) {
            Users u = new Users();
            u = JSFUtil.getUserSession().getUsers();
            SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat dt2 = new SimpleDateFormat("yyyyMMdd");
            JsonObject json = new JsonObject();
            JsonObject jsonInarray = new JsonObject();
            JsonArray array = new JsonArray();
            Customer c = new Customer();
            c = po.getCustomer();

            json.addProperty("LEADID", c.getFlexfield1());
            json.addProperty("USER", "TERRABIT");
            json.addProperty("SALECODE", convertNullToString(u.getFx1()));
            String sale_date = dt.format(new Date());
            json.addProperty("DATETIME", sale_date);

            jsonInarray.addProperty("AMOUNT", String.format("%.02f", po.getPrice()));
            jsonInarray.addProperty("TOTALPREM", String.format("%.02f", po.getNetPremium()));
            jsonInarray.addProperty("DISCOUNT", String.format("%.02f", po.getDiscount()));
            jsonInarray.addProperty("TOTALPREMAFDISC", String.format("%.02f", po.getTotalAmount()));
            //jsonInarray.addProperty("INVOICENO",convertNullToString(po.getPaymentRefNo()));
            jsonInarray.addProperty("INVOICENO", "1" + convertNullToString(c.getFlexfield1()) + "1");
            String payment_date = dt2.format(po.getPaymentGatewaySubmitDate());
            jsonInarray.addProperty("PAYDATE", convertNullToString(payment_date));
            if (paymentMethod.isCreditcard() == true && paymentMethod.getOnline() == true) {
                jsonInarray.addProperty("PAYTYPE", "2C2P");
            } else if (paymentMethod.isCreditcard() == false) {
                jsonInarray.addProperty("PAYTYPE", "QRCODE123");
            }
            if (paymentMethod.isCreditcard() == true && paymentMethod.getOnline() == true) {
                jsonInarray.addProperty("REF1", convertNullToString(po.getPaymentReferenceNo1()));
            } else if (paymentMethod.isCreditcard() == false) {
                jsonInarray.addProperty("REF1", convertNullToString(po.getCustomer().getFlexfield1()));
            }
            if (paymentMethod.isCreditcard() == true && paymentMethod.getOnline() == true) {
                jsonInarray.addProperty("REF2", convertNullToString(po.getCardNo()));
            } else if (paymentMethod.isCreditcard() == false) {
                jsonInarray.addProperty("REF2", convertNullToString(""));
            }
            jsonInarray.addProperty("REF3", convertNullToString(""));
            jsonInarray.addProperty("CREDITCARD", convertNullToString(po.getCardNo()));
            if (paymentMethod.isCreditcard() == true && paymentMethod.getOnline() == true) {
                jsonInarray.addProperty("CREDITCARDEXP", convertNullToString(po.getCardExpiryYear() + StringUtils.leftPad(po.getCardExpiryMonth().toString(), 2, "0")));
            } else {
                jsonInarray.addProperty("CREDITCARDEXP", "");
            }
            jsonInarray.addProperty("CREDITCARDBANKCODE", "");

            array.add(jsonInarray);
            json.add("PREMIUM", array);

            String Url = "https://bgcdisbweb.bigc.co.th/bcibservicer/api/policy/wsupdatepayment"; // Test
            //String Url = "https://bgcpisbweb.bigc.co.th/bcibservicer/api/policy/WSUPDATEPAYMENT"; // production
            String msg = "Success";
            String responseBody = "";
            HttpURLConnection conn = null;
            OutputStream os = null;
            InputStream in = null;
            try {

                URL url = new URL(Url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                os = conn.getOutputStream();
                os.write(json.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                if (conn.getResponseCode() != 200) {
                    msg = conn.getResponseMessage();
                    if (conn.getResponseCode() == 500) {
                        messageFromSmartBroker = "Cloud not get any response. please check service. path= " + Url;
                    }
                    if (conn.getResponseCode() == 404) {
                        messageFromSmartBroker = "Criteria is not match. Code: " + conn.getResponseCode();
                    }
                    //throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());

                } else {
                    in = new BufferedInputStream(conn.getInputStream());
                    responseBody = IOUtils.toString(in, "UTF-8");
                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(responseBody).getAsJsonObject();
                    String success = rootObj.get("RESPCODE").getAsString();
                    if (success.equals("0000")) {
                        messageFromSmartBroker = "";
                        //messageFromSmartBroker = rootObj.get("RESPMSG").getAsString();
                        purchaseOrderDAO.updatePaymentSubmitDate(po.getId(), po.getPaymentReferenceNo1(),po.getPaymentGatewaySubmitDate());
                        renderUpdatetoSmartBroker = true;
                    } else {
                        messageFromSmartBroker = rootObj.get("RESPMSG").getAsString();
                    }
                }
                if (in != null) {
                    in.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                msg = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                msg = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                msg = e.getMessage();
            } finally {

            }
        } else {
            if((po.getPaymentGatewaySubmitDate()==null || po.getPaymentGatewaySubmitDate().toString().trim().equals("")) && (po.getPaymentReferenceNo1() == null || po.getPaymentReferenceNo1().trim().equals(""))) {
                messageFromSmartBroker = "กรุณากรอก เลขที่อ้างอิง (Reference No 1) และ วันที่ชำระ (Payment Date)";
            } else if((po.getPaymentGatewaySubmitDate()==null || po.getPaymentGatewaySubmitDate().toString().trim().equals("")) && !po.getPaymentReferenceNo1().trim().equals("")) {
                messageFromSmartBroker = "กรุณากรอก วันที่ชำระ (Payment Date)";
            } else if ((!po.getPaymentGatewaySubmitDate().toString().trim().equals("")) && (po.getPaymentReferenceNo1() == null || po.getPaymentReferenceNo1().trim().equals(""))) {
                messageFromSmartBroker = "กรุณากรอก เลขที่อ้างอิง (Reference No 1)";

            }
        }
    }

    public static String getMd5(String input)
    {   String hashtext="";
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashtext;
    }


    public void prepareParameterPayment2C2P(){
        DecimalFormat df = new DecimalFormat(".##");
        parammeter = "";
        if(paymentMethod.isCreditcard() == true && paymentMethod.getOnline() == true){
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int toYear = cal.get(Calendar.YEAR);
            int yearCard = Integer.parseInt("20"+po.getCardExpiryYear());
            if(yearCard >= toYear) {
                purchaseOrderDAO.updatePaymentGatewaySubmitDate(po.getId());
            }
            String cardExpiryMonth = StringUtils.leftPad(po.getCardExpiryMonth().toString(), 2, "0");
            String cardExpiryYear = yearCard+"";
            Double totalAmout = po.getTotalAmount();
            String payNo = "";
            if(po.getPaymentCountNo() !=null && !po.getPaymentCountNo().equals("")){
                Integer num = Integer.parseInt(po.getPaymentCountNo());
                num = num +1;
                String number = StringUtils.leftPad(num.toString(), 2, "0");
                purchaseOrderDAO.updatePaymentCountNo(number,po.getId());
                po.setPaymentCountNo(number);
            } else {
                payNo = StringUtils.leftPad("1", 2, "0");
                purchaseOrderDAO.updatePaymentCountNo(payNo,po.getId());
                po.setPaymentCountNo(payNo);
            }
            String formattedString = String.format("%.02f", totalAmout);
            parammeter += "cardnumber=" + po.getCardNo() + "&month="
                        + cardExpiryMonth + "&year=" + cardExpiryYear
                        + "&cardholderName=" + po.getCardHolderName() + "&description=" + "&amount=" + formattedString.replace(".", "")
                        + "&leadId=" + po.getCustomer().getFlexfield1()+"&poId="+po.getId()+"&paymentCountNo="+po.getPaymentCountNo();

        } else if(paymentMethod.isCreditcard() == false){
            purchaseOrderDAO.updatePaymentGatewaySubmitDate(po.getId());
            List<PurchaseOrderDetail> pods = (List<PurchaseOrderDetail>) po.getPurchaseOrderDetailCollection();
            for(PurchaseOrderRegister por : pods.get(0).getPurchaseOrderRegisterCollection()){
                parammeter += "phonenumber="+por.getMobilePhone();
                parammeter += "&email="+por.getEmail();
                parammeter += "&paymentExpiryDate="+por.getFx47();
                break;
            }
            Double totalAmout = po.getTotalAmount();
            String payNo = "";
            if(po.getPaymentCountNo() !=null && !po.getPaymentCountNo().equals("")){
                Integer num = Integer.parseInt(po.getPaymentCountNo());
                num = num +1;
                String number = StringUtils.leftPad(num.toString(), 2, "0");
                purchaseOrderDAO.updatePaymentCountNo(number,po.getId());
                po.setPaymentCountNo(number);
            } else {
                payNo = StringUtils.leftPad("1", 2, "0");
                purchaseOrderDAO.updatePaymentCountNo(payNo,po.getId());
                po.setPaymentCountNo(payNo);
            }
            String formattedString = String.format("%.02f", totalAmout);
            parammeter += "&description="+"&amount="+formattedString.replace(".","")
                       //+"&paymentRefNo="+paymentRefNo;
                       + "&leadId=" + po.getCustomer().getFlexfield1()+"&poId="+po.getId()+"&agentCode="+po.getAgentCashCode()+"&channelCode="+po.getChannelCashCode()+"&paymentCountNo="+po.getPaymentCountNo();;
        }
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

    private ProductWorkflow getNextWorkflow() {
        ProductWorkflow nextProductWorkflow = null;
        int i = 0;
        if (productWorkflowList != null && !productWorkflowList.isEmpty() && productWorkflow != null) {
            for (ProductWorkflow pw : productWorkflowList) {
                if (i == 1) {
                    nextProductWorkflow = pw;
                    break;
                }
                if (pw.getApprovalRoleName().equals(productWorkflow.getApprovalRoleName())) {
                    i++;
                }
            }
        }
        return nextProductWorkflow;
    }

    private String getNextRole() {
        String nextRole = null;
        ProductWorkflow nextwf = getNextWorkflow();
        if (nextwf != null) {
            nextRole = nextwf.getApprovalRoleName();
        } else {
            if (productWorkflow != null && productWorkflowList != null && productWorkflowList.size() > 0) {
                ProductWorkflow lastwf = productWorkflowList.get(productWorkflowList.size() - 1);
                if (lastwf.getApprovalRoleName() != null && lastwf.getApprovalRoleName().equals(productWorkflow.getApprovalRoleName())) {
                    nextRole = null; // incase current flow = last flow
                }
            }
        }
        return nextRole;
    }

    private boolean checkApproveFlow(ProductWorkflow pw) {
        String lastRole = productWorkflowList.get(productWorkflowList.size() - 1).getApprovalRoleName().toUpperCase();
        boolean chk = false;
        uwEnable = false;
        paymentEnable = false;
        qcEnable = false;
        if (pw.getApprovalRoleName().toUpperCase().equals("UW")) {
            if (po.getApprovalStatus() != null && po.getApprovalStatus().toUpperCase().equals("APPROVED")) {
                chk = true;
            } else {
                chk = false;
            }
            if (!chk && JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("UW")) {
                uwEnable = true;
                disableUwSaveBtn = false;
            }
            if (lastRole.equals("UW")) {
                uwEnable = true;
                disableUwSaveBtn = false;
            }
            if (po.getLatestDelegateToRole() != null && po.getLatestDelegateToRole().toUpperCase().equals("UW")) {
                uwEnable = true;
                disableUwSaveBtn = false;
                uwDelegate = po.getLatestDelegateFromRole();
            }
        } else if (pw.getApprovalRoleName().toUpperCase().equals("PAYMENT")) {
            if (po.getPaymentStatus() != null && po.getPaymentStatus().toUpperCase().equals("APPROVED")) {
                chk = true;
            } else {
                chk = false;
            }
            if (!chk && JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("PAYMENT")) {
                paymentEnable = true;
                disablePaymentSaveBtn = false;
            }
            if (lastRole.equals("PAYMENT")) {
                paymentEnable = true;
                disablePaymentSaveBtn = false;
            }
            if (po.getLatestDelegateToRole() != null && po.getLatestDelegateToRole().toUpperCase().equals("PAYMENT")) {
                paymentEnable = true;
                disablePaymentSaveBtn = false;
                paymentDelegate = po.getLatestDelegateFromRole();
            }

            List<PurchaseOrderDetail> pods = (List<PurchaseOrderDetail>) po.getPurchaseOrderDetailCollection();
            if (po.getTraceNo() != null && !po.getTraceNo().isEmpty() && !po.getTraceNo().equals("")) {
                disablePaymentApprove = false;
            } else {
                if(pods.get(0).getProduct().getFx1().equals("CMI") || pods.get(0).getProduct().getFx1().equals("VMI")) {
                    disablePaymentApprove = false;
                }else {
                    disablePaymentApprove = true;
                }
            }
        } else if (pw.getApprovalRoleName().toUpperCase().equals("QC")) {
            if (po.getQcStatus() != null && po.getQcStatus().toUpperCase().equals("APPROVED")) {
                chk = true;
            } else {
                chk = false;
            }
            if (!chk && JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("QC")) {
                qcEnable = true;
                disableQcSaveBtn = false;
            }
            if (lastRole.equals("QC")) {
                qcEnable = true;
                disableQcSaveBtn = false;
            }
            if (po.getLatestDelegateToRole() != null && po.getLatestDelegateToRole().toUpperCase().equals("QC")) {
                qcEnable = true;
                disableQcSaveBtn = false;
                qcDelegate = po.getLatestDelegateFromRole();
            }
        }

        return chk;
    }

    private void checkDelegate() {
        if (JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("UW") && po.getLatestDelegateToRole() != null && po.getLatestDelegateToRole().toUpperCase().equals("UW")) {
            uwEnable = true;
            disableUwSaveBtn = false;
            uwDelegate = po.getLatestDelegateFromRole();
        } else if (JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("SUPERVISOR") && po.getLatestDelegateToRole() != null && po.getLatestDelegateToRole().toUpperCase().equals("SUPERVISOR")) {
            supEnable = true;
            disableSupSaveBtn = false;
            supDelegate = po.getLatestDelegateFromRole();
        } else if (JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("PAYMENT") && po.getLatestDelegateToRole() != null && po.getLatestDelegateToRole().toUpperCase().equals("PAYMENT")) {
            paymentEnable = true;
            disablePaymentSaveBtn = false;
            paymentDelegate = po.getLatestDelegateFromRole();
        } else if (JSFUtil.getUserSession().getUserGroup().getRole().toUpperCase().contains("QC") && po.getLatestDelegateToRole() != null && po.getLatestDelegateToRole().toUpperCase().equals("QC")) {
            qcEnable = true;
            disableQcSaveBtn = false;
            qcDelegate = po.getLatestDelegateFromRole();
        }
    }

    private ProductWorkflow getLastWorkflow() {
        ProductWorkflow lastWorkflow = null;
        try {
            lastWorkflow = productWorkflowList.get(productWorkflowList.size() - 1);
            if (lastWorkflow.getApprovalRoleName().toUpperCase().equals("UW")) {
                uwEnable = true;
            } else if (lastWorkflow.getApprovalRoleName().toUpperCase().equals("PAYMENT")) {
                paymentEnable = true;
            } else if (lastWorkflow.getApprovalRoleName().toUpperCase().equals("QC")) {
                qcEnable = true;
            }
        } catch (Exception e) {
            log.error(e);
        }
        return lastWorkflow;
    }

    public void updateStatus(Integer poId) {
        po = purchaseOrderDAO.findPurchaseOrder(poId);
        String role = JSFUtil.getUserSession().getUserGroup().getRole();
        if (role.contains("Qc") && po.getQcStatus().equals("waiting")) {
            po.setQcStatus("pending");
            updatePurchaseOrderApprovalLog(po, "Qc");
        }
        if (role.contains("Payment") && po.getPaymentStatus().equals("waiting")) {
            po.setPaymentStatus("pending");
            updatePurchaseOrderApprovalLog(po, "Payment");
        }
        if (role.contains("Uw") && po.getApprovalStatus().equals("waiting")) {
            po.setApprovalStatus("pending");
            updatePurchaseOrderApprovalLog(po, "Uw");
        }
        try {
            getPurchaseOrderDAO().edit(po);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePurchaseOrderApprovalLog(PurchaseOrder po, String byRole) {
        PurchaseOrderApprovalLog purchaseOrderApprovalLog = new PurchaseOrderApprovalLog();
        purchaseOrderApprovalLog.setPurchaseOrder(po);
        purchaseOrderApprovalLog.setApprovalStatus("pending");
        purchaseOrderApprovalLog.setApprovalReason(null);
        purchaseOrderApprovalLog.setToRole("");
        purchaseOrderApprovalLog.setToUser(null);
        purchaseOrderApprovalLog.setMessage(null);
        purchaseOrderApprovalLog.setCreateByRole(byRole);
        purchaseOrderApprovalLog.setCreateBy("System");
        purchaseOrderApprovalLog.setCreateDate(new Date());
        purchaseOrderApprovalLog.setUsers(JSFUtil.getUserSession().getUsers());
        try {
            purchaseOrderDAO.createApprovalLog(purchaseOrderApprovalLog);
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void checkBtn() {
        if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Agent")) {
            mode = "tmr";
            if (po.getQcStatus().equals("rejected")
                    || po.getApprovalStatus().equals("rejected")
                    || po.getPaymentStatus().equals("rejected")) {
                showAgentStatus = false;
            } else {
                showAgentStatus = true;
                if (tabName == null) {
                    tabName = "agent";
                }
            }
            disableAgentSaveBtn = true;
        }
        if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Supervisor")) {
            mode = "sup";
            showSupStatus = true;
            if (tabName == null) {
                tabName = "sup";
            }
        }
        if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Uw")) {
            mode = "uw";
            showUwStatus = true;
            if (tabName == null) {
                tabName = "uw";
            }
        }
        if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Payment")) {
            mode = "payment";
            showPaymentStatus = true;
            if (tabName == null) {
                tabName = "payment";
            }
        }
        if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Qc")) {
            mode = "qc";
            showQcStatus = true;
            if (tabName == null) {
                tabName = "qc";
            }
        }
        if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Confirm")) {
            mode = "confirm";
            if (po.getReasonCode() != null && po.getReasonCode().toUpperCase().equals("IF")) {
                showConfirmStatus = true;
                if (tabName == null) {
                    tabName = "confirm";
                }
                if (po.getConfirmFlag() != null && !po.getConfirmFlag()) {
                    confirmEnable = true;
                    confirmDelegate = "UW";
                }
            }
        }
    }

    private void checkFlowControll() {
        if (flowApproval == 1) {//payment --> qc
            if (uwStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                paymentEnable = true;
                if (paymentStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                    qcEnable = true;
                } else {
                    qcEnable = false;
                }
            } else {
                paymentEnable = false;
                qcEnable = false;
            }
        } else if (flowApproval == 2) {//qc --> payment
            if (uwStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                qcEnable = true;
                if (qcStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                    paymentEnable = true;
                } else {
                    paymentEnable = false;
                }
            } else {
                qcEnable = false;
                paymentEnable = false;
            }
        }
    }

    private void flowPayment() {
        if (SecurityUtil.isPermitted("admin:salesapproval:paymentapproval")) {
            if (paymentStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                qcEnable = true;
            } else {
                qcEnable = true;
            }
        }
    }

    private void flowQc() {
        if (SecurityUtil.isPermitted("admin:salesapproval:qcapproval")) {
            if (qcStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                paymentEnable = true;
            } else {
                paymentEnable = false;
            }
        }
    }

    private List<PurchaseOrderApprovalLog> getApprovalLog() {
        List<PurchaseOrderApprovalLog> list = null;
        try {
            list = purchaseOrderApprovalLogList = purchaseOrderDAO.findApprovalLog(po.getId());
        } catch (Exception e) {
            log.error(e);
        }
        return list;

    }

    public void changeModeListener(ItemChangeEvent event) {
        tabName = event.getNewItemName();
        if (poId != null && poId != 0) {
            po = purchaseOrderDAO.findPurchaseOrder(poId);
        }
        this.initialize();
    }

    private void initList() {
        if (mode != null) {
            if (mode.equals("uw")) {
                approvalReasonUwList = approvalReasonDAO.findByStatus("uw", uwStatus);
            } else if (mode.equals("qc")) {
                approvalReasonQcList = approvalReasonDAO.findByStatus("qc", qcStatus);
            } else if (mode.equals("payment")) {
                approvalReasonPaymentList = approvalReasonDAO.findByStatus("payment", paymentStatus);
            } else if (mode.equals("tmr")) {
                approvalReasonAgentList = approvalReasonDAO.findByStatus("tmr");
            } else if (mode.equals("sup")) {
                approvalReasonSupList = approvalReasonDAO.findByStatus("sup");
            }
        }
    }

    public void tabApprovalListener(ItemChangeEvent event) {
        if (poId != null && poId != 0) {
            po = purchaseOrderDAO.findPurchaseOrder(poId);
        }
        this.initialize();
    }

    public void tabPaymentRerender() {
        tabName = "payment";
        if (poId != null && poId != 0) {
            po = purchaseOrderDAO.findPurchaseOrder(poId);

            renderSendSMS =false;
            if(po.getSmsSubmitDate()!=null && po.getSmsResult()!=null && !po.getSmsResult().equals("") && !po.getSmsSubmitDate().equals("")){
                renderSendSMS = true;
            }

            renderUpdatetoSmartBroker =false;
            if(po.getPaymentSubmitDate()!=null && !po.getPaymentSubmitDate().equals("")){
                renderUpdatetoSmartBroker = true;
            }
        }
        messageFromSmartBroker = "";
        meassageSendSMS = "";
        this.initialize();
    }

    public void uwStatusChange(ValueChangeEvent event) {
        uwStatus = (String) event.getNewValue();
        if (uwStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalRequestValue"))
                || uwStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalReconfirmValue"))
                || uwStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalResendValue"))) {
            uwEnableDelegate = true;
        } else {
            uwEnableDelegate = false;
        }
        uwDelegate = null;
        approvalReasonUwList = approvalReasonDAO.findByStatus("uw", uwStatus);
        
        if (uwStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalRejectedValue"))) {
            this.msgUwSaveRemark = this.purchaseOrderDAO.validateApproveFamilyPurchaseOrder(po.getId(), JSFUtil.getBundleValue("approvalRejected"));
        }
    }

    public void qcStatusChange(ValueChangeEvent event) {
        qcStatus = (String) event.getNewValue();
        if (qcStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalRequestValue"))
                || qcStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalReconfirmValue"))
                || qcStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalResendValue"))) {
            qcEnableDelegate = true;
        } else {
            qcEnableDelegate = false;
        }
        qcDelegate = null;
        approvalReasonQcList = approvalReasonDAO.findByStatus("qc", qcStatus);
        
        if (qcStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalRejectedValue"))) {
            this.msgQcSaveRemark = this.purchaseOrderDAO.validateApproveFamilyPurchaseOrder(po.getId(), JSFUtil.getBundleValue("approvalRejected"));
        }
    }

    public void paymentStatusChange(ValueChangeEvent event) {
        paymentStatus = (String) event.getNewValue();
        if (paymentStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalRequestValue"))
                || paymentStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalReconfirmValue"))
                || paymentStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalResendValue"))) {
            paymentEnableDelegate = true;
        } else {
            paymentEnableDelegate = false;
        }
        paymentDelegate = null;
        approvalReasonPaymentList = approvalReasonDAO.findByStatus("payment", paymentStatus);
        
        if (paymentStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalRejectedValue"))) {
            this.msgPaymentSaveRemark = this.purchaseOrderDAO.validateApproveFamilyPurchaseOrder(po.getId(), JSFUtil.getBundleValue("approvalRejected"));
        }
    }

    public void supStatusChange(ValueChangeEvent event) {
        supStatus = (String) event.getNewValue();
        if (supStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalRequestValue"))
                || supStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalReconfirmValue"))
                || supStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalResendValue"))) {
            supEnableDelegate = true;
        } else {
            supEnableDelegate = false;
        }
        supDelegate = null;
        approvalReasonSupList = approvalReasonDAO.findByStatus("sup", supStatus);
    }

    public void confirmStatusChange(ValueChangeEvent event) {
        supStatus = (String) event.getNewValue();

        approvalReasonSupList = approvalReasonDAO.findByStatus("sup", supStatus);
    }

    public List<ApprovalExclusion> getSelectedApprovalExclusionCollection() {
        List<ApprovalExclusion> list = new ArrayList<ApprovalExclusion>();
        ApprovalExclusionDAO dao = getApprovalExclusionDAO();
        for (int excId : selectedApprovalExclusionList) {
            ApprovalExclusion exc = dao.findApprovalExclusion(excId);
            list.add(exc);
        }
        return list;
    }

    private void saveDelegateToRole(String toRole) {
        if (toRole.toUpperCase().equals("UW")) {
            po.setApprovalStatus(JSFUtil.getBundleValue("approvalPendingValue"));
        } else if (toRole.toUpperCase().equals("PAYMENT")) {
            po.setPaymentStatus(JSFUtil.getBundleValue("approvalPendingValue"));
        } else if (toRole.toUpperCase().equals("QC")) {
            po.setQcStatus(JSFUtil.getBundleValue("approvalPendingValue"));
        }
    }

    private void markPendingAtToRoleStatusOfPurchaseOrder(String toRole) {
        if (toRole.toUpperCase().equals("UW")) {
            po.setApprovalStatus(JSFUtil.getBundleValue("approvalPendingValue"));
        } else if (toRole.toUpperCase().equals("PAYMENT")) {
            po.setPaymentStatus(JSFUtil.getBundleValue("approvalPendingValue"));
        } else if (toRole.toUpperCase().equals("QC")) {
            po.setQcStatus(JSFUtil.getBundleValue("approvalPendingValue"));
        }
    }

    private void markLatestInfoAtPurchaseOrder(String fromActorRole, String toActorRole, String updStatus) {
        //Step1: Set common information of lateststatus
        po.setLatestStatus(updStatus);
        po.setLatestStatusDate(new Date());
        po.setLatestStatusBy(JSFUtil.getUserSession().getUserName());
        po.setLatestStatusByUser(JSFUtil.getUserSession().getUsers());
        po.setLatestDelegateToRole(toActorRole);
        po.setLatestDelegateFromRole(fromActorRole);
    }

    public void saveUwActionListener(ActionEvent event) {
        ApprovalReason approvalReason = uwReasonId != null ? approvalReasonDAO.findApprovalReason(uwReasonId) : null;
        po.setApprovalStatus(uwStatus);
        po.setApprovalReason(approvalReason);
        po.setLatestReason(approvalReason != null ? approvalReason.getName() : null);
        po.setOtherExclusionCode(otherExclusionCode);
        po.setOtherExclusionName(otherExclusionName);
        po.setRemark(this.uwMessage);
        po.setRemark2(this.uwMessage2);
        po.setApproveDate(new Date());
        po.setApproveBy(JSFUtil.getUserSession().getUserName());
        po.setApproveByUser(JSFUtil.getUserSession().getUsers());

        String myRole = "Uw";
        String newLdfr = myRole, newLdft = null;
        if (uwEnableDelegate) { //in case, there is delegateTo value (requestMoreInfo,Reconfirm,Resend)
            newLdft = uwDelegate;
            this.markPendingAtToRoleStatusOfPurchaseOrder(uwDelegate);
            this.uwEnable = false;
        } else { // in case, there is not delegateTo value
            if (uwStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                newLdft = getNextRole(); //delegate to next role , following workflow configuration

            } else if (uwStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalRejectedValue"))
                    || uwStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalPendingValue"))) {
                newLdft = myRole;
            }
        }
        this.markLatestInfoAtPurchaseOrder(newLdfr, newLdft, uwStatus);

        this.savePurchaseOrder();

        PurchaseOrderApprovalLog purchaseOrderApprovalLog = new PurchaseOrderApprovalLog();
        purchaseOrderApprovalLog.setApprovalStatus(po.getApprovalStatus());
        purchaseOrderApprovalLog.setApprovalReason(approvalReason);
        purchaseOrderApprovalLog.setCreateByRole(newLdfr);
        purchaseOrderApprovalLog.setToRole(newLdft);
        purchaseOrderApprovalLog.setMessage(uwMessage);
        purchaseOrderApprovalLog.setOtherExclusionCode(otherExclusionCode);
        purchaseOrderApprovalLog.setOtherExclusionName(otherExclusionName);
        purchaseOrderApprovalLog.setApprovalExclusionCollection(this.getSelectedApprovalExclusionCollection());

        this.savePurchaseOrderApprovalLog(purchaseOrderApprovalLog);

        this.uwMessage = null;
        this.uwReasonId = null;
        this.uwDelegate = null;
        this.otherExclusionCode = null;
        this.otherExclusionName = null;
        selectedApprovalExclusionList.clear();

        this.initialize();
    }

    public void savePaymentActionListener(ActionEvent event) {
        ApprovalReason approvalReason = paymentReasonId != null ? approvalReasonDAO.findApprovalReason(paymentReasonId) : null;
        po.setPaymentStatus(paymentStatus);
        po.setPaymentReason(approvalReason);
        po.setLatestReason(approvalReason != null ? approvalReason.getName() : null);
        po.setRemark(this.paymentMessage);
        po.setPaymentDate(new Date());
        po.setPaymentBy(JSFUtil.getUserSession().getUserName());
        po.setPaymentByUser(JSFUtil.getUserSession().getUsers());

        String myRole = "Payment";
        String newLdfr = myRole, newLdft = null;
        if (paymentEnableDelegate) { //in case, there is delegateTo value (requestMoreInfo,Reconfirm,Resend)
            newLdft = paymentDelegate;
            this.markPendingAtToRoleStatusOfPurchaseOrder(paymentDelegate);
            this.paymentEnable = false;
        } else { // in case, there is not delegateTo value
            if (paymentStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                newLdft = getNextRole(); //delegate to next role , following workflow configuration

            } else if (paymentStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalRejectedValue"))
                    || paymentStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalPendingValue"))) {
                newLdft = myRole;
            }
        }
        this.markLatestInfoAtPurchaseOrder(newLdfr, newLdft, paymentStatus);

        this.savePurchaseOrder();

        PurchaseOrderApprovalLog purchaseOrderApprovalLog = new PurchaseOrderApprovalLog();
        purchaseOrderApprovalLog.setApprovalStatus(po.getPaymentStatus());
        purchaseOrderApprovalLog.setApprovalReason(approvalReason);
        purchaseOrderApprovalLog.setCreateByRole(newLdfr);
        purchaseOrderApprovalLog.setToRole(newLdft);
        purchaseOrderApprovalLog.setMessage(paymentMessage);

        this.savePurchaseOrderApprovalLog(purchaseOrderApprovalLog);

        this.paymentMessage = null;
        this.paymentReasonId = null;
        this.paymentDelegate = null;

        this.initialize();
    }

    public void saveQcActionListener(ActionEvent event) {
        try {
            ApprovalReason approvalReason = qcReasonId != null ? approvalReasonDAO.findApprovalReason(qcReasonId) : null;
            po.setQcStatus(qcStatus);
            po.setQcReason(approvalReason);
            po.setLatestReason(approvalReason != null ? approvalReason.getName() : null);
            po.setRemark(this.qcMessage);
            po.setQcDate(new Date());
            po.setQcBy(JSFUtil.getUserSession().getUserName());
            po.setQcByUser(JSFUtil.getUserSession().getUsers());
            List<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrderDAO.findPurchaseOrderDetailByPurchaseOrderId(po.getId());
            PurchaseOrderDetail pod = purchaseOrderDetails.get(0);
            if (po.getSaleResult() == 'Y'
                    && (po.getRefNo() == null || po.getRefNo().equals(""))
                    && po.getQcStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                    && pod.getProduct().getRefNoGenerateEvent().toUpperCase().equals("QC")) {
                po.setRefNo(nextSequenceService.genRef(pod.getProduct().getSequenceNo().getId()));
            }

            String myRole = "Qc";
            String newLdfr = myRole, newLdft = null;
            if (qcEnableDelegate) { //in case, there is delegateTo value (requestMoreInfo,Reconfirm,Resend)
                newLdft = qcDelegate;
                this.markPendingAtToRoleStatusOfPurchaseOrder(qcDelegate);
                this.qcEnable = false;
            } else { // in case, there is not delegateTo value
                if (qcStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                    newLdft = getNextRole(); //delegate to next role , following workflow configuration

                } else if (qcStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalRejectedValue"))
                        || qcStatus.equalsIgnoreCase(JSFUtil.getBundleValue("approvalPendingValue"))) {
                    newLdft = myRole;
                }
            }
            this.markLatestInfoAtPurchaseOrder(newLdfr, newLdft, qcStatus);

            this.savePurchaseOrder();

            PurchaseOrderApprovalLog purchaseOrderApprovalLog = new PurchaseOrderApprovalLog();
            purchaseOrderApprovalLog.setApprovalStatus(po.getQcStatus());
            purchaseOrderApprovalLog.setApprovalReason(approvalReason);
            purchaseOrderApprovalLog.setCreateByRole(newLdfr);
            purchaseOrderApprovalLog.setToRole(newLdft);
            purchaseOrderApprovalLog.setMessage(qcMessage);

            this.savePurchaseOrderApprovalLog(purchaseOrderApprovalLog);

            approvalExclusions = approvalExclusionDAO.findApprovalExclusionEntities();
            this.selectedApprovalExclusionList = null;
            this.qcMessage = null;
            this.qcReasonId = null;
            this.qcDelegate = null;

            this.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveAgentActionListener(ActionEvent event) {
        ApprovalReason approvalReason = agentReasonId != null ? approvalReasonDAO.findApprovalReason(agentReasonId) : null;
        //po.setPaymentStatus(paymentStatus);
        po.setLatestReason(approvalReason != null ? approvalReason.getName() : null);
        po.setRemark(this.agentMessage);
        this.markLatestInfoAtPurchaseOrder("Agent", agentDelegate, po.getLatestStatus()); //update with old latest status
        this.savePurchaseOrder();

        PurchaseOrderApprovalLog purchaseOrderApprovalLog = new PurchaseOrderApprovalLog();
        //purchaseOrderApprovalLog.setApprovalStatus(po.getPaymentStatus());
        purchaseOrderApprovalLog.setApprovalReason(approvalReason);
        purchaseOrderApprovalLog.setCreateByRole("Agent");
        purchaseOrderApprovalLog.setToRole(agentDelegate);
        purchaseOrderApprovalLog.setMessage(agentMessage);

        this.savePurchaseOrderApprovalLog(purchaseOrderApprovalLog);
        this.agentMessage = null;
        this.agentReasonId = null;
        this.agentDelegate = null;
    }

    public void saveSupActionListener(ActionEvent event) {
        ApprovalReason approvalReason = supReasonId != null ? approvalReasonDAO.findApprovalReason(supReasonId) : null;
        po.setApprovalSupStatus(supStatus);
        po.setLatestReason(approvalReason != null ? approvalReason.getName() : null);
        po.setRemark(this.supMessage);

        po.setLatestStatus(supStatus);
        po.setLatestStatusDate(new Date());
        po.setLatestDelegateToRole(supDelegate);
        po.setLatestDelegateFromRole("Supervisor");
        po.setLatestStatusBy(JSFUtil.getUserSession().getUserName());
        po.setLatestStatusByUser(JSFUtil.getUserSession().getUsers());

        if (supDelegate != null && !supDelegate.equals("")) {
            this.saveDelegateToRole(supDelegate);
            this.supEnable = false;
        }

        this.savePurchaseOrder();

        PurchaseOrderApprovalLog purchaseOrderApprovalLog = new PurchaseOrderApprovalLog();
        purchaseOrderApprovalLog.setApprovalReason(approvalReason);
        purchaseOrderApprovalLog.setCreateByRole("Supervisor");
        purchaseOrderApprovalLog.setToRole(supDelegate);
        purchaseOrderApprovalLog.setMessage(supMessage);

        this.savePurchaseOrderApprovalLog(purchaseOrderApprovalLog);

        this.supMessage = null;
        this.supReasonId = null;
        this.supDelegate = null;
    }

    public void saveConfirmActionListener(ActionEvent event) {
        po.setRemark(this.confirmMessage);
        po.setConfirmDate(new Date());
        po.setConfirmBy(JSFUtil.getUserSession().getUserName());
        po.setConfirmByUser(JSFUtil.getUserSession().getUsers());
        this.confirmEnable = false;
        this.savePurchaseOrder();

        PurchaseOrderApprovalLog purchaseOrderApprovalLog = new PurchaseOrderApprovalLog();
        purchaseOrderApprovalLog.setApprovalStatus("Confirmed Call");
        purchaseOrderApprovalLog.setCreateByRole("Confirm");
        purchaseOrderApprovalLog.setMessage(confirmMessage);

        this.savePurchaseOrderApprovalLog(purchaseOrderApprovalLog);

        this.confirmMessage = null;
        this.confirmDelegate = null;
    }

    private String convertActivity(String sta) {
        String str = "";

        if (sta.equals(JSFUtil.getBundleValue("approvalPendingValue"))) {
            str = JSFUtil.getBundleValue("approvalPending");
        } else if (sta.equals(JSFUtil.getBundleValue("approvalRequestValue"))) {
            str = JSFUtil.getBundleValue("approvalRequest");
        } else if (sta.equals(JSFUtil.getBundleValue("approvalReconfirmValue"))) {
            str = JSFUtil.getBundleValue("approvalReconfirm");
        } else if (sta.equals(JSFUtil.getBundleValue("approvalRejectedValue"))) {
            str = JSFUtil.getBundleValue("approvalRejected");
        } else if (sta.equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
            str = JSFUtil.getBundleValue("approvalApproved");
        } else if (sta.equals(JSFUtil.getBundleValue("approvalResendValue"))) {
            str = JSFUtil.getBundleValue("approvalResend");
        }
        return str;

    }

    private boolean checkNextRoleApproved(String nextRole) {
        boolean check = false;
        if (nextRole.toUpperCase().equals("UW") && po.getApprovalStatus().equalsIgnoreCase(JSFUtil.getBundleValue("approvalApprovedValue"))) {
            check = true;
        } else if (nextRole.toUpperCase().equals("PAYMENT") && po.getPaymentStatus().equalsIgnoreCase(JSFUtil.getBundleValue("approvalApprovedValue"))) {
            check = true;
        } else if (nextRole.toUpperCase().equals("QC") && po.getQcStatus().equalsIgnoreCase(JSFUtil.getBundleValue("approvalApprovedValue"))) {
            check = true;
        }
        return check;
    }

    private void savePurchaseOrder() {
        try {
            if (po.getSaleResult() == 'Y'
                    && po.getApprovalStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                    && po.getQcStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                    && po.getPaymentStatus().equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                    && po.getSaleDate() == null) {
                po.setSaleDate(new Date());
            }
            purchaseOrderDAO.updateStatus(po);
            if (familyProduct) {
                purchaseOrderDAO.updateFamilyPurchaseOrder(po.getId());
            }
            checkBtn();
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void savePurchaseOrderApprovalLog(PurchaseOrderApprovalLog purchaseOrderApprovalLog) {
        purchaseOrderApprovalLog.setPurchaseOrder(po);
        purchaseOrderApprovalLog.setCreateBy(JSFUtil.getUserSession().getUserName());
        purchaseOrderApprovalLog.setUsers(JSFUtil.getUserSession().getUsers());
        purchaseOrderApprovalLog.setCreateDate(new Date());
        try {
            purchaseOrderDAO.createApprovalLog(purchaseOrderApprovalLog);
            if (familyProduct) {
                purchaseOrderDAO.updateFamilyPurchaseOrderApprovalLog(po.getId());
            }
        } catch (Exception e) {
            log.error(e);
        }
        purchaseOrderApprovalLogList = purchaseOrderDAO.findApprovalLog(po.getId());
    }

    public Map<String, String> getActivityList() {
        Map<String, String> values = new LinkedHashMap<String, String>();
        values.put(JSFUtil.getBundleValue("approvalPending"), JSFUtil.getBundleValue("approvalPendingValue"));
        values.put(JSFUtil.getBundleValue("approvalRequest"), JSFUtil.getBundleValue("approvalRequestValue"));
        values.put(JSFUtil.getBundleValue("approvalReconfirm"), JSFUtil.getBundleValue("approvalReconfirmValue"));
        values.put(JSFUtil.getBundleValue("approvalResend"), JSFUtil.getBundleValue("approvalResendValue"));
        values.put(JSFUtil.getBundleValue("approvalRejected"), JSFUtil.getBundleValue("approvalRejectedValue"));
        if (tabName != null && tabName.equals("payment")) {
            if (!disablePaymentApprove) {
                values.put(JSFUtil.getBundleValue("approvalApproved"), JSFUtil.getBundleValue("approvalApprovedValue"));
            }
        } else {
            values.put(JSFUtil.getBundleValue("approvalApproved"), JSFUtil.getBundleValue("approvalApprovedValue"));
        }
        return values;
    }

    public String backAction() {
        CustomerInfoValue c = null;
        JSFUtil.getUserSession().setCustomerDetail(c);
        this.getSaleApproveController().search();

        return SUCCESS;
    }

    public SaleApproveController getSaleApproveController() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ValueExpression vex = app.getExpressionFactory().
                createValueExpression(context.getELContext(), "#{saleApproveController}", SaleApproveController.class);
        SaleApproveController g = (SaleApproveController) vex.getValue(context.getELContext());
        return g;
    }

    public PurchaseOrder getPo() {
        return po;
    }

    public void setPo(PurchaseOrder po) {
        this.po = po;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public List<ContactRecordValue> getList() {
        return list;
    }

    public void setList(List<ContactRecordValue> list) {
        this.list = list;
    }

    public ContactHistoryDAO getContactHistoryDAO() {
        return contactHistoryDAO;
    }

    public void setContactHistoryDAO(ContactHistoryDAO contactHistoryDAO) {
        this.contactHistoryDAO = contactHistoryDAO;
    }

    public List<PurchaseOrderApprovalLog> getPurchaseOrderApprovalLogList() {
        if (po != null) {
            purchaseOrderApprovalLogList = purchaseOrderDAO.findApprovalLog(po.getId());
            if (purchaseOrderApprovalLogList != null && !purchaseOrderApprovalLogList.isEmpty()) {
                PurchaseOrderApprovalLog log = purchaseOrderApprovalLogList.get(0);
                if (log.getToRole() != null && log.getToRole().equals("Agent")) {
                    agentDelegate = log.getCreateByRole();
                }
            }
        }
        return purchaseOrderApprovalLogList;
    }

    public void setPurchaseOrderApprovalLogList(List<PurchaseOrderApprovalLog> purchaseOrderApprovalLogList) {
        this.purchaseOrderApprovalLogList = purchaseOrderApprovalLogList;
    }

    public String getFromPage() {
        return fromPage;
    }

    public void setFromPage(String fromPage) {
        this.fromPage = fromPage;
    }

    public int getFlowApproval() {
        return flowApproval;
    }

    public void setFlowApproval(int flowApproval) {
        this.flowApproval = flowApproval;
    }

    public boolean isShowPaymentStatus() {
        return showPaymentStatus;
    }

    public void setShowPaymentStatus(boolean showPaymentStatus) {
        this.showPaymentStatus = showPaymentStatus;
    }

    public boolean isShowQcStatus() {
        return showQcStatus;
    }

    public void setShowQcStatus(boolean showQcStatus) {
        this.showQcStatus = showQcStatus;
    }

    public boolean isBtnSave() {
        return btnSave;
    }

    public void setBtnSave(boolean btnSave) {
        this.btnSave = btnSave;
    }

    public boolean isShowUwStatus() {
        return showUwStatus;
    }

    public void setShowUwStatus(boolean showUwStatus) {
        this.showUwStatus = showUwStatus;
    }

    public ApprovalReasonDAO getApprovalReasonDAO() {
        return approvalReasonDAO;
    }

    public void setApprovalReasonDAO(ApprovalReasonDAO approvalReasonDAO) {
        this.approvalReasonDAO = approvalReasonDAO;
    }

    public List<ApprovalReason> getApprovalReasonList() {
        return approvalReasonList;
    }

    public void setApprovalReasonList(List<ApprovalReason> approvalReasonList) {
        this.approvalReasonList = approvalReasonList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getQcStatus() {
        return qcStatus;
    }

    public void setQcStatus(String qcStatus) {
        this.qcStatus = qcStatus;
    }

    public String getUwStatus() {
        return uwStatus;
    }

    public void setUwStatus(String uwStatus) {
        this.uwStatus = uwStatus;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public Map<String, String> getStatusList() {
        return statusList;
    }

    public void setStatusList(Map<String, String> statusList) {
        this.statusList = statusList;
    }

    public String getPaymentMessage() {
        return paymentMessage;
    }

    public void setPaymentMessage(String paymentMessage) {
        this.paymentMessage = paymentMessage;
    }

    public Integer getPaymentReasonId() {
        return paymentReasonId;
    }

    public void setPaymentReasonId(Integer paymentReasonId) {
        this.paymentReasonId = paymentReasonId;
    }

    public String getQcMessage() {
        return qcMessage;
    }

    public void setQcMessage(String qcMessage) {
        this.qcMessage = qcMessage;
    }

    public Integer getQcReasonId() {
        return qcReasonId;
    }

    public void setQcReasonId(Integer qcReasonId) {
        this.qcReasonId = qcReasonId;
    }

    public String getUwMessage() {
        return uwMessage;
    }

    public void setUwMessage(String uwMessage) {
        this.uwMessage = uwMessage;
    }

    public Integer getUwReasonId() {
        return uwReasonId;
    }

    public void setUwReasonId(Integer uwReasonId) {
        this.uwReasonId = uwReasonId;
    }

    public String getPaymentDelegate() {
        return paymentDelegate;
    }

    public void setPaymentDelegate(String paymentDelegate) {
        this.paymentDelegate = paymentDelegate;
    }

    public String getQcDelegate() {
        return qcDelegate;
    }

    public void setQcDelegate(String qcDelegate) {
        this.qcDelegate = qcDelegate;
    }

    public String getUwDelegate() {
        return uwDelegate;
    }

    public void setUwDelegate(String uwDelegate) {
        this.uwDelegate = uwDelegate;
    }

    public Map<String, Integer> getApprovalExclusionList() {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (ApprovalExclusion exc : approvalExclusions) {
            values.put(exc.getCode() + ": " + exc.getName(), exc.getId());
        }
        return values;
    }

    public void setApprovalExclusionList(Map<String, Integer> approvalExclusionList) {
        this.approvalExclusionList = approvalExclusionList;
    }

    public ApprovalExclusionDAO getApprovalExclusionDAO() {
        return approvalExclusionDAO;
    }

    public void setApprovalExclusionDAO(ApprovalExclusionDAO approvalExclusionDAO) {
        this.approvalExclusionDAO = approvalExclusionDAO;
    }

    public List<Integer> getSelectedApprovalExclusionList() {
        return selectedApprovalExclusionList;
    }

    public void setSelectedApprovalExclusionList(List<Integer> selectedApprovalExclusionList) {
        this.selectedApprovalExclusionList = selectedApprovalExclusionList;
    }

    public List<ApprovalReason> getApprovalReasonPaymentList() {
        return approvalReasonPaymentList;
    }

    public void setApprovalReasonPaymentList(List<ApprovalReason> approvalReasonPaymentList) {
        this.approvalReasonPaymentList = approvalReasonPaymentList;
    }

    public List<ApprovalReason> getApprovalReasonQcList() {
        return approvalReasonQcList;
    }

    public void setApprovalReasonQcList(List<ApprovalReason> approvalReasonQcList) {
        this.approvalReasonQcList = approvalReasonQcList;
    }

    public List<ApprovalReason> getApprovalReasonUwList() {
        return approvalReasonUwList;
    }

    public void setApprovalReasonUwList(List<ApprovalReason> approvalReasonUwList) {
        this.approvalReasonUwList = approvalReasonUwList;
    }

    public String getAgentDelegate() {
        return agentDelegate;
    }

    public void setAgentDelegate(String agentDelegate) {
        this.agentDelegate = agentDelegate;
    }

    public String getAgentMessage() {
        return agentMessage;
    }

    public void setAgentMessage(String agentMessage) {
        this.agentMessage = agentMessage;
    }

    public Integer getAgentReasonId() {
        return agentReasonId;
    }

    public void setAgentReasonId(Integer agentReasonId) {
        this.agentReasonId = agentReasonId;
    }

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    public List<ApprovalReason> getApprovalReasonAgentList() {
        return approvalReasonAgentList;
    }

    public void setApprovalReasonAgentList(List<ApprovalReason> approvalReasonAgentList) {
        this.approvalReasonAgentList = approvalReasonAgentList;
    }

    public boolean isQcEnable() {
        return qcEnable;
    }

    public void setQcEnable(boolean qcEnable) {
        this.qcEnable = qcEnable;
    }

    public boolean isPaymentEnable() {
        return paymentEnable;
    }

    public void setPaymentEnable(boolean paymentEnable) {
        this.paymentEnable = paymentEnable;
    }

    public boolean isDisablePaymentSaveBtn() {
        return disablePaymentSaveBtn;
    }

    public void setDisablePaymentSaveBtn(boolean disablePaymentSaveBtn) {
        this.disablePaymentSaveBtn = disablePaymentSaveBtn;
    }

    public boolean isDisableQcSaveBtn() {
        return disableQcSaveBtn;
    }

    public void setDisableQcSaveBtn(boolean disableQcSaveBtn) {
        this.disableQcSaveBtn = disableQcSaveBtn;
    }

    public boolean isDisableUwSaveBtn() {
        return disableUwSaveBtn;
    }

    public void setDisableUwSaveBtn(boolean disableUwSaveBtn) {
        this.disableUwSaveBtn = disableUwSaveBtn;
    }

    public boolean isShowAgentStatus() {
        return showAgentStatus;
    }

    public void setShowAgentStatus(boolean showAgentStatus) {
        this.showAgentStatus = showAgentStatus;
    }

    public boolean isDisableAgentSaveBtn() {
        return disableAgentSaveBtn;
    }

    public void setDisableAgentSaveBtn(boolean disableAgentSaveBtn) {
        this.disableAgentSaveBtn = disableAgentSaveBtn;
    }

    public String getOtherExclusionCode() {
        return otherExclusionCode;
    }

    public void setOtherExclusionCode(String otherExclusionCode) {
        this.otherExclusionCode = otherExclusionCode;
    }

    public String getOtherExclusionName() {
        return otherExclusionName;
    }

    public void setOtherExclusionName(String otherExclusionName) {
        this.otherExclusionName = otherExclusionName;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public List<ApprovalReason> getApprovalReasonSupList() {
        return approvalReasonSupList;
    }

    public void setApprovalReasonSupList(List<ApprovalReason> approvalReasonSupList) {
        this.approvalReasonSupList = approvalReasonSupList;
    }

    public boolean isShowSupStatus() {
        return showSupStatus;
    }

    public void setShowSupStatus(boolean showSupStatus) {
        this.showSupStatus = showSupStatus;
    }

    public Integer getSupReasonId() {
        return supReasonId;
    }

    public void setSupReasonId(Integer supReasonId) {
        this.supReasonId = supReasonId;
    }

    public String getSupMessage() {
        return supMessage;
    }

    public void setSupMessage(String supMessage) {
        this.supMessage = supMessage;
    }

    public String getSupDelegate() {
        return supDelegate;
    }

    public void setSupDelegate(String supDelegate) {
        this.supDelegate = supDelegate;
    }

    public String getSupStatus() {
        return supStatus;
    }

    public void setSupStatus(String supStatus) {
        this.supStatus = supStatus;
    }

    public boolean isSupEnable() {
        return supEnable;
    }

    public void setSupEnable(boolean supEnable) {
        this.supEnable = supEnable;
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

    public boolean isUwEnable() {
        return uwEnable;
    }

    public void setUwEnable(boolean uwEnable) {
        this.uwEnable = uwEnable;
    }

    public boolean isDisableSupSaveBtn() {
        return disableSupSaveBtn;
    }

    public void setDisableSupSaveBtn(boolean disableSupSaveBtn) {
        this.disableSupSaveBtn = disableSupSaveBtn;
    }

    public NextSequenceService getNextSequenceService() {
        return nextSequenceService;
    }

    public void setNextSequenceService(NextSequenceService nextSequenceService) {
        this.nextSequenceService = nextSequenceService;
    }

    public boolean isUwEnableDelegate() {
        return uwEnableDelegate;
    }

    public void setUwEnableDelegate(boolean uwEnableDelegate) {
        this.uwEnableDelegate = uwEnableDelegate;
    }

    public boolean isPaymentEnableDelegate() {
        return paymentEnableDelegate;
    }

    public void setPaymentEnableDelegate(boolean paymentEnableDelegate) {
        this.paymentEnableDelegate = paymentEnableDelegate;
    }

    public boolean isQcEnableDelegate() {
        return qcEnableDelegate;
    }

    public void setQcEnableDelegate(boolean qcEnableDelegate) {
        this.qcEnableDelegate = qcEnableDelegate;
    }

    public boolean isSupEnableDelegate() {
        return supEnableDelegate;
    }

    public void setSupEnableDelegate(boolean supEnableDelegate) {
        this.supEnableDelegate = supEnableDelegate;
    }

    public boolean isShowConfirmStatus() {
        return showConfirmStatus;
    }

    public void setShowConfirmStatus(boolean showConfirmStatus) {
        this.showConfirmStatus = showConfirmStatus;
    }

    public boolean isConfirmEnable() {
        return confirmEnable;
    }

    public void setConfirmEnable(boolean confirmEnable) {
        this.confirmEnable = confirmEnable;
    }

    public String getConfirmMessage() {
        return confirmMessage;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    public String getConfirmDelegate() {
        return confirmDelegate;
    }

    public void setConfirmDelegate(String confirmDelegate) {
        this.confirmDelegate = confirmDelegate;
    }

    public Integer getPoId() {
        return poId;
    }

    public void setPoId(Integer poId) {
        this.poId = poId;
    }

    public boolean isDisablePaymentApprove() {
        return disablePaymentApprove;
    }

    public void setDisablePaymentApprove(boolean disablePaymentApprove) {
        this.disablePaymentApprove = disablePaymentApprove;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getUwMessage2() {
        return uwMessage2;
    }

    public void setUwMessage2(String uwMessage2) {
        this.uwMessage2 = uwMessage2;
    }

    public boolean isEnableReferenceNo() {
        return enableReferenceNo;
    }

    public void setEnableReferenceNo(boolean enableReferenceNo) {
        this.enableReferenceNo = enableReferenceNo;
    }

    public PurchaseOrder getPoForMainInsure() {
        return poForMainInsure;
    }

    public void setPoForMainInsure(PurchaseOrder poForMainInsure) {
        this.poForMainInsure = poForMainInsure;
    }

    public List<PurchaseOrderDetail> getPodForFamily() {
        return podForFamily;
    }

    public void setPodForFamily(List<PurchaseOrderDetail> podForFamily) {
        this.podForFamily = podForFamily;
    }
    
    public String getFamilyProductType() {
        List<PurchaseOrderDetail> pod = (List<PurchaseOrderDetail>) po.getPurchaseOrderDetailCollection();
        if (pod.get(0).getProduct().getFamilyProduct() && pod.get(0).getProduct().getFamilyProductType() == 1) {
            return "Spouse Package";
        }else if (pod.get(0).getProduct().getFamilyProduct() && pod.get(0).getProduct().getFamilyProductType() == 2) {
            return "Family Pack Discount";
        }else{
            return "";
        }
    }
    
    public String getRelationMainInsured(PurchaseOrderDetail pod) {
        List<PurchaseOrderRegister> por = new ArrayList<>(pod.getPurchaseOrderRegisterCollection());
        if (por.size() > 0) {         
            String relationMainInsured = por.get(0).getRelationMainInsured();
            if (relationMainInsured == null) {
                return "Main Insure";
            } else if (relationMainInsured.equalsIgnoreCase("S")) {
                return "Spouse";
            } else if (relationMainInsured.equalsIgnoreCase("K")) {
                return "Child";
            } else if (relationMainInsured.equalsIgnoreCase("P")) {
                return "Parent";
            } else {
                return "Main Insure";
            }
        }else{
            return "";
        }
    }

    public String getCkSpouse() {
        return ckSpouse;
    }

    public void setCkSpouse(String ckSpouse) {
        this.ckSpouse = ckSpouse;
    }

    public Integer getMainPoId() {
        return mainPoId;
    }

    public void setMainPoId(Integer mainPoId) {
        this.mainPoId = mainPoId;
    }

    public PurchaseOrderDetailDAO getPurchaseOrderDetailDAO() {
        return purchaseOrderDetailDAO;
    }

    public void setPurchaseOrderDetailDAO(PurchaseOrderDetailDAO purchaseOrderDetailDAO) {
        this.purchaseOrderDetailDAO = purchaseOrderDetailDAO;
    }

    public boolean isFamilyProduct() {
        return familyProduct;
    }

    public void setFamilyProduct(boolean familyProduct) {
        this.familyProduct = familyProduct;
    }
        
    public Product getProduct() {
        PurchaseOrder po = purchaseOrderDAO.findPurchaseOrder(poId);
        List<PurchaseOrderDetail> pod = (List<PurchaseOrderDetail>) po.getPurchaseOrderDetailCollection();
        return pod.get(0).getProduct();
    }
    
    public String getApprovalStatus(String approvalValue) {
        try{
            if (approvalValue.equals(JSFUtil.getBundleValue("approvalWaitingValue"))) {
                return JSFUtil.getBundleValue("approvalWaiting");
            } else if (approvalValue.equals(JSFUtil.getBundleValue("approvalPendingValue"))) {
                return JSFUtil.getBundleValue("approvalPending");
            } else if (approvalValue.equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                return JSFUtil.getBundleValue("approvalApproved");
            } else if (approvalValue.equals(JSFUtil.getBundleValue("approvalRejectedValue"))) {
                return JSFUtil.getBundleValue("approvalRejected");
            } else if (approvalValue.equals(JSFUtil.getBundleValue("approvalReconfirmValue"))) {
                return JSFUtil.getBundleValue("approvalReconfirm");
            } else if (approvalValue.equals(JSFUtil.getBundleValue("approvalRequestValue"))) {
                return JSFUtil.getBundleValue("approvalRequest");
            } else if (approvalValue.equals(JSFUtil.getBundleValue("approvalReopenValue"))) {
                return JSFUtil.getBundleValue("approvalReopen");
            } else if (approvalValue.equals(JSFUtil.getBundleValue("approvalResendValue"))) {
                return JSFUtil.getBundleValue("approvalResend");
            } else {
                return "";
            }
        }catch(Exception e){
            return "";
        }
    }
    
    public String getMsgUwSaveRemark() {
        return msgUwSaveRemark;
    }

    public void setMsgUwSaveRemark(String msgUwSaveRemark) {
        this.msgUwSaveRemark = msgUwSaveRemark;
    }
    
    public String getMsgQcSaveRemark() {
        return msgQcSaveRemark;
    }

    public void setMsgQcSaveRemark(String msgQcSaveRemark) {
        this.msgQcSaveRemark = msgQcSaveRemark;
    }
    
    public String getMsgPaymentSaveRemark() {
        return msgPaymentSaveRemark;
    }

    public void setMsgPaymentSaveRemark(String msgPaymentSaveRemark) {
        this.msgPaymentSaveRemark = msgPaymentSaveRemark;
    }
    
    public Double getTotalPremium() {
        Double totalPremium = 0.0;
        for(PurchaseOrderDetail pod: this.podForFamily) {
            if(String.valueOf(pod.getPurchaseOrder().getSaleResult()).equals("Y") && !pod.getPurchaseOrder().getApprovalStatus().equals("rejected") && !pod.getPurchaseOrder().getQcStatus().equals("rejected") && !pod.getPurchaseOrder().getPaymentStatus().equals("rejected")) {
                totalPremium += pod.getPurchaseOrder().getNetPremium();
            }
        }
        return totalPremium;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethodDAO getPaymentMethodDAO() {
        return paymentMethodDAO;
    }

    public void setPaymentMethodDAO(PaymentMethodDAO paymentMethodDAO) {
        this.paymentMethodDAO = paymentMethodDAO;
    }

    public String getPaymentBody() {
        return paymentBody;
    }

    public void setPaymentBody(String paymentBody) {
        this.paymentBody = paymentBody;
    }

    public String getParammeter() {
        return parammeter;
    }

    public void setParammeter(String parammeter) {
        this.parammeter = parammeter;
    }

    public String getMessageFromSmartBroker() {
        return messageFromSmartBroker;
    }

    public void setMessageFromSmartBroker(String messageFromSmartBroker) {
        this.messageFromSmartBroker = messageFromSmartBroker;
    }

    public String getMeassageSendSMS() {
        return meassageSendSMS;
    }

    public void setMeassageSendSMS(String meassageSendSMS) {
        this.meassageSendSMS = meassageSendSMS;
    }

    public boolean isRenderUpdatetoSmartBroker() {
        return renderUpdatetoSmartBroker;
    }

    public void setRenderUpdatetoSmartBroker(boolean renderUpdatetoSmartBroker) {
        this.renderUpdatetoSmartBroker = renderUpdatetoSmartBroker;
    }

    public boolean isRenderSendSMS() {
        return renderSendSMS;
    }

    public void setRenderSendSMS(boolean renderSendSMS) {
        this.renderSendSMS = renderSendSMS;
    }
}
