package com.maxelyz.core.controller;

import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.SessionMonitorValue;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.core.service.TelephonyService;
import com.maxelyz.core.service.UserService;
import com.maxelyz.utils.JSFUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PreDestroy;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.richfaces.json.JSONObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

@ManagedBean
@SessionScoped
public final class UserSession implements Serializable {

    private static Logger log = Logger.getLogger(UserSession.class);
    private static Logger ctiLog = Logger.getLogger("mxCtiLogger");
      
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US);
    private Users user;
    private UserGroup userGroup;
    private CustomerInfoValue customerDetail;
    private List<Product> products;
    private List<Knowledgebase> knowledgebases;
    private List<ContactCase> contactCases;
    private List<PurchaseOrder> purchaseOrders;
//  private List<Notification> notificationList;
    private List<NotificationMessage> notificationMessages;
    private AssignmentDetail assignmentDetail;
    private boolean dialClicked;
    private boolean hasClick;
    private String telephonyTrackId;
    private ContactHistory contactHistory;
    private boolean logInfo;
    private boolean showContactSummary;
    private boolean inboundCall;
    private boolean outboundCall;
    private Date loginTime;
    private Calendar messageBroadcastAccesstime;
    private String messageBroadcastString = "";
    private String mainPage;
    private String subPage;
    private String dialNo = "";
    private String dialExt = "";
    private String dialToString = "";
    private boolean dialShow = true;
    private boolean hangupShow = false;
    private boolean nextcallShow = false;
    private String dialStatus;
    private boolean dialStatusActive = false;
    private String contactTo;
    private String channel;
    private String callCategory;
    private List<Acl> userGroupAclList;
    private List<UserGroupLocation> userGroupLocationList;
    private String firstPage;
    private String ldapPassword;
    private String refNo;
    private String mode = "hide"; //show, hide
    private String telephonyEdit = "view"; //view, edit
    private boolean clickNextCall;
    private String transferNo = "";
    private String callCount = "";
    private String uniqueId = "";
    private String contactToName = "";
    private boolean hasKnowledgeBase;
    private String stationNo;
    private Integer contactResultId;
    private String contactResultCode;    
    private String fromTab;    
    private String param1;
    private String param2;    
    private Integer talkTime;
    private Integer contactCaseId;
    private Integer workflowlogId;
    private String from;    
    private String agentStatus = "Not Ready";    
    private String convertVoiceURL;
    private String refYes;
    private String dialCall;
    private String msgTelephony;
    private String ctiToolbar;
    private Integer contactResultPlanId;
    private Integer purchaseOrderId;
    
    private String notificationUuid;
    private Integer notificationRefId;
    private String notificationPhoneNumber;

    @ManagedProperty(value = "#{messageBroadcastDAO}")
    private MessageBroadcastDAO messageBroadcastDAO;
    @ManagedProperty(value = "#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    @ManagedProperty(value = "#{contactHistoryDAO}")
    private ContactHistoryDAO contactHistoryDAO;
    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{marketingCustomerDAO}")
    private MarketingCustomerDAO marketingCustomerDAO;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{userService}")
    private UserService userService;
    @ManagedProperty(value = "#{telephonyService}")
    private TelephonyService telephonyService;
    @ManagedProperty(value = "#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
    @ManagedProperty(value = "#{notificationMessageDAO}")
    private NotificationMessageDAO notificationMessageDAO;

    public UserSession() {
        initialize();
    }

    public void initialize() {
        customerDetail = new CustomerInfoValue();
        knowledgebases = new ArrayList<Knowledgebase>();
        products = new ArrayList<Product>();
        contactCases = new ArrayList<ContactCase>();
        purchaseOrders = new ArrayList<PurchaseOrder>();
        notificationMessages = new ArrayList<NotificationMessage>();
        dialClicked = false;
        logInfo = false;
        loginTime = new Date();
        showContactSummary = false;
        user = null;
        userGroup = null;
        dialStatus = null;
        clickNextCall = false;
        convertVoiceURL = JSFUtil.getApplication().getConvertVoiceURL();
        contactResultPlanId = null;
        purchaseOrderId = null;
        removeCustomerSession();
    }

    @PreDestroy
    public void shutdown() {
        knowledgebases = null;
        products = null;
        contactCases = null;
        purchaseOrders = null;
        notificationMessages = null;
        assignmentDetail = null;
        logInfo = false;
        loginTime = null;
        showContactSummary = false;
        user = null;
        //removeCustomerSession();
    }
            
    public UserSession(Users user) {
        super();
        this.user = user;
    }

    public Users getUsers() {
        return user;
    }

    public String getUserName() {
        return user.getName() + " " + user.getSurname();
    }

    public void setUsers(Users user) {
        this.user = user;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public String getLoginTimeString() {
        return sdf.format(loginTime);
    }

    public Boolean getRoleAdmin() {
        return getRoleUser("roleAdmin");
    }

    public Boolean getRoleCs() {
        return getRoleUser("roleCs");
    }

    public Boolean getRoleTs() {
        return getRoleUser("roleTs");
    }

    public Boolean getRoleQc() {
        return getRoleUser("roleQc");
    }

    public Boolean getRoleSaleApprove() {
        return getRoleUser("roleSaleApprove");
    }

    public Boolean getRoleReport() {
        return getRoleUser("roleReport");
    }

    private boolean getRoleUser(String roleName) {
        return user.getUserGroup().getRole().matches(".*" + JSFUtil.getBundleValue(roleName) + ".*");
    }
    
    private boolean getRoleUser2(String roleName) {
        return user.getUserGroup().getRole().matches(".*" + roleName + ".*");
    }
    
    public Boolean getRoleCSCounter() {
        return getRoleUser("CSCounter");
    }

    public Boolean getRoleKBManager() {
        return getRoleUser2("KBManager");
    }
    
    public Boolean getRoleKBApproval() {
        return getRoleUser2("KBApproval");
    }
    
    public Boolean getRoleKBDataEntry() {
        return getRoleUser2("KBDataEntry");
    }
    
//    public Boolean getDisableEndContact() {
//        System.out.println(JSFUtil.getRequest().getRequestURI());
//        if(JSFUtil.getRequest().getRequestURI().contains("admin/")) {
//            return false;
//        }  else {
//            return true;
//        }
//    }
    
    public Boolean getHandlingCustomer() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Object obj = customerDetail;
        if (obj != null) {
            this.setLogInfo(true);
            if(JSFUtil.getRequest().getRequestURI().contains("customerHandling/")){
                 return true;
            } else {
                return false;
            }
           
        } else {
            this.setLogInfo(false);
            this.clearInfo();
            return false;
        }
//        if(JSFUtil.getRequest().getRequestURI().contains("customerHandling/")){
//            return true;
//        } else {
//            return false;
//        }
    }
    
    public CustomerInfoValue getCustomerDetail() {
        return customerDetail;
    }

    public void setCustomerDetail(CustomerInfoValue customerDetail) {
        this.customerDetail = customerDetail;
    }

    //Save Knowledge Base when User Click Knowledge Popup
    public void setKnowledgebase(Knowledgebase kbParam) {
        if (logInfo) {
            for (Knowledgebase kb : knowledgebases) {
                if (kbParam.getId().equals(kb.getId())) {
                    return; //do not add to arraylist if duplicate
                }
            }
            knowledgebases.add(kbParam);
        }
    }

    public List<Knowledgebase> getKnowledgebases() {
        return knowledgebases;
    }

    public boolean getHasKnowledgebases() {
        return knowledgebases.size() > 0;
    }

    public List<ContactCase> getContactCases() {
        return contactCases;
    }

    public void setContactCases(ContactCase contactCaseParam) {
        if (logInfo) {
            for (ContactCase contactCase : contactCases) {
                if (contactCaseParam.getId().equals(contactCase.getId())) {
                    return; //do not add to arraylist if duplicate
                }
            }
            contactCases.add(contactCaseParam);
        }
    }

    public boolean getHasContactCases() {
        return contactCases.size() > 0;
    }

    public void clearInfo() {
        knowledgebases.clear();
        products.clear();
        contactCases.clear();
        purchaseOrders.clear();
        notificationMessages.clear();
        assignmentDetail = null;
        dialClicked = false;
        showContactSummary = false;
        customerDetail = null;
        //inboundCall = false;
        outboundCall = false;
        telephonyTrackId = null;
        contactResultPlanId = null;
        purchaseOrderId = null;   
    }

    public AssignmentDetail getAssignmentDetail() {
        return assignmentDetail;
    }

    public void setAssignmentDetail(AssignmentDetail assignmentDetail) {
        this.assignmentDetail = assignmentDetail;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(Product productParam) {
        if (logInfo) {
            for (Product product : products) {
                if (productParam.getId().equals(product.getId())) {
                    return; //do not add to arraylist if duplicate
                }
            }
            products.add(productParam);
        }

    }

    public void resetProducts() {
        products = new ArrayList<Product>();
    }

    public boolean getHasProducts() {
        return products.size() > 0;
    }

    public void clearProducts() {
        products.clear();
    }

    public List<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(PurchaseOrder purchaseOrderParam) {
        try {

//            if (purchaseOrders.size() > 0) {
            for (PurchaseOrder purchaseOrder : purchaseOrders) {
                if (purchaseOrderParam.getId().equals(purchaseOrder.getId())) {
                    //return; //do not add to arraylist if duplicate
                    purchaseOrders.remove(purchaseOrder);
                    break;
                }
            }
            purchaseOrders.add(purchaseOrderParam);
            List<PurchaseOrderDetail> pDetails = (List<PurchaseOrderDetail>) purchaseOrderParam.getPurchaseOrderDetailCollection();
            products.remove(pDetails.get(0).getProduct());
//            }

        } catch (Exception e) {
            log.error(e);
        }
    }

    public void resetPurchaseOrders() {
        purchaseOrders = new ArrayList<PurchaseOrder>();
    }

    public boolean getHasPurchaseOrders() {
        return purchaseOrders.size() > 0;
    }

    public void clearPurchaseOrders() {
        purchaseOrders.clear();
    }

    private String getPurchaseOrderId(int recordNo) {
        String id = "";
        if (purchaseOrders.size() >= recordNo) {
            PurchaseOrder po = purchaseOrders.get(recordNo - 1);
            if (po.getId() != null) {
                id = po.getId().toString();
            }
        }
        return id;
    }

    public String getPurchaseOrderId1() {
        return getPurchaseOrderId(1);
    }

    public String getPurchaseOrderId2() {
        return getPurchaseOrderId(2);
    }

    public String getPurchaseOrderId3() {
        return getPurchaseOrderId(3);
    }

    public String getPurchaseOrderId4() {
        return getPurchaseOrderId(4);
    }

    public String getPurchaseOrderId5() {
        String id = "";
        if (purchaseOrders.size() >= 5) {
            for (int recordNo = 5; recordNo <= purchaseOrders.size(); recordNo++) {
                PurchaseOrder po = purchaseOrders.get(recordNo - 1);
                id += po.getId().toString() + ",";
            }
        }
        return id;
    }
    
    private String getPurchaseOrderRefNo1(int recordNo) {
        String refNo = "";
        if (purchaseOrders.size() >= recordNo) {
            PurchaseOrder po = purchaseOrders.get(recordNo - 1);
            if (po.getRefNo() != null) {
                refNo = po.getRefNo();
            }
        }
        return refNo;
    }
    
    public String getPurchaseOrder1RefNo1() {
        return getPurchaseOrderRefNo1(1);
    }

    public String getPurchaseOrder2RefNo1() {
        return getPurchaseOrderRefNo1(2);
    }

    public String getPurchaseOrder3RefNo1() {
        return getPurchaseOrderRefNo1(3);
    }

    public String getPurchaseOrder4RefNo1() {
        return getPurchaseOrderRefNo1(4);
    }
    
    public String getPurchaseOrder5RefNo1() {
        String refNo = "";
        if (purchaseOrders.size() >= 5) {
            for (int recordNo = 5; recordNo <= purchaseOrders.size(); recordNo++) {
                PurchaseOrder po = purchaseOrders.get(recordNo - 1);
                refNo += po.getRefNo() + ",";
            }
        }
        return refNo;
    }
        
    private String getPurchaseOrderRefNo2(int recordNo) {
        String refNo2 = "";
        if (purchaseOrders.size() >= recordNo) {
            PurchaseOrder po = purchaseOrders.get(recordNo - 1);
            if (po.getRefNo2() != null) {
                refNo2 = po.getRefNo2();
            }
        }
        return refNo2;
    }
    
    public String getPurchaseOrder1RefNo2() {
        return getPurchaseOrderRefNo2(1);
    }

    public String getPurchaseOrder2RefNo2() {
        return getPurchaseOrderRefNo2(2);
    }

    public String getPurchaseOrder3RefNo2() {
        return getPurchaseOrderRefNo2(3);
    }

    public String getPurchaseOrder4RefNo2() {
        return getPurchaseOrderRefNo2(4);
    }
    
    public String getPurchaseOrder5RefNo2() {
        String refNo2 = "";
        if (purchaseOrders.size() >= 5) {
            for (int recordNo = 5; recordNo <= purchaseOrders.size(); recordNo++) {
                PurchaseOrder po = purchaseOrders.get(recordNo - 1);
                refNo2 += po.getRefNo2() + ",";
            }
        }
        return refNo2;
    }

    private String getCaseCode(int recordNo) {
        String id = "";
        if (contactCases.size() >= recordNo) {
            ContactCase c = contactCases.get(recordNo - 1);
            if (c.getCode() != null) {
                id = c.getCode().toString();
            }
        }
        return id;
    }

    public String getCaseCode1() {
        return getCaseCode(1);
    }

    public String getCaseCode2() {
        return getCaseCode(2);
    }

    public String getCaseCode3() {
        return getCaseCode(3);
    }

    public String getCaseCode4() {
        return getCaseCode(4);
    }

    public String getCaseCode5() {
        String id = "";
        try {
            if (contactCases.size() >= 5) {
                for (int recordNo = 5; recordNo <= contactCases.size(); recordNo++) {
                    ContactCase c = contactCases.get(recordNo - 1);
                    id += c.getCode().toString() + ",";
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return id;

    }

    private String getPurchaseOrderSaleResult(int recordNo) {
        String saleResult = "";
        if (purchaseOrders.size() >= recordNo) {
            PurchaseOrder po = purchaseOrders.get(recordNo - 1);
            saleResult = po.getSaleResult() + "";
        }
        return saleResult;
    }

    public String getPurchaseOrderSaleResult1() {
        return getPurchaseOrderSaleResult(1);
    }

    public String getPurchaseOrderSaleResult2() {
        return getPurchaseOrderSaleResult(2);
    }

    public String getPurchaseOrderSaleResult3() {
        return getPurchaseOrderSaleResult(3);
    }

    public String getPurchaseOrderSaleResult4() {
        return getPurchaseOrderSaleResult(4);
    }

    public String getPurchaseOrderSaleResult5() {
        String saleResult = "";
        if (purchaseOrders.size() >= 5) {
            for (int recordNo = 5; recordNo <= purchaseOrders.size(); recordNo++) {
                PurchaseOrder po = purchaseOrders.get(recordNo - 1);
                saleResult += po.getSaleResult() + ",";
            }
        }
        return saleResult;
    }

    public boolean isLogInfo() {
        return logInfo;
    }

    public void setLogInfo(boolean logInfo) {
        this.logInfo = logInfo;
    }

    private boolean diffTime(long minute) {
        if (messageBroadcastAccesstime == null) {
            messageBroadcastAccesstime = Calendar.getInstance();
            return true;
        }
        Calendar now = Calendar.getInstance();
        long diffMinute = (long) ((now.getTimeInMillis() - messageBroadcastAccesstime.getTimeInMillis()) / 1000.0 / 60);
        if (diffMinute >= minute) {
            messageBroadcastAccesstime = Calendar.getInstance();
            return true;
        }
        return false;
    }

    public String getMessageBroadcast() {
        if (diffTime(5)) { //fetch new data every 5 min.
            List<MessageBroadcast> messageBroadcasts = (List<MessageBroadcast>) getMessageBroadcastDAO().findMessageBroadcastByUser(user);
            StringBuilder sb = new StringBuilder();
            for (MessageBroadcast m : messageBroadcasts) {
                String image;
                if (m.getAllUser()) {
                    image = "agent_script_03"; //all
                } else if (m.getUserGroupId() != null) {
                    image = "agent_script_02"; //group
                } else {
                    image = "agent_script_01"; //individual
                }
                String imagePath = JSFUtil.getRequestContextPath() + "/img/" + image + ".png";
                sb.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td width=\"550\"><span style=\"font-size: 8.5pt;\">");
                sb.append(m.getMessage());
                sb.append("</span></td><td align=\"right\"><img src=\"");
                sb.append(imagePath);
                sb.append("\" />");
                sb.append("</td></tr></table>");
                sb.append("||");
            }
            String out = sb.toString();
            if (out.length() > 1) {
                out = out.substring(0, out.length() - 2);
            }
            messageBroadcastString = out;
        }
        return messageBroadcastString;
    }

    public boolean isShowContactSummary() {
        return showContactSummary;
    }

    public void setShowContactSummary(boolean showContactSummary) {
        this.showContactSummary = showContactSummary;
    }

    //Managed Properties
    public MessageBroadcastDAO getMessageBroadcastDAO() {
        return messageBroadcastDAO;
    }

    public void setMessageBroadcastDAO(MessageBroadcastDAO messageBroadcastDAO) {
        this.messageBroadcastDAO = messageBroadcastDAO;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }

    public void removeCustomerSession() {
        try {
            customerDetail = null;
            clearEndContact();
//            removeDial();
        } catch (Exception e) {
            log.error(e);
        }
    }
    
    public void clearEndContact() {
        knowledgebases.clear();
        products.clear();
        contactCases.clear();
        purchaseOrders.clear();
        assignmentDetail = null;
        customerDetail = null;
    }

    public void removeDial() {
        dialNo = "";
        dialExt = "";
        dialToString = "";
        dialStatus = null;
        dialClicked = false;
//        dialShow = true;
        hangupShow = false;
        nextcallShow = false;
        contactTo = null;
        telephonyTrackId = null;
        inboundCall = false;
        outboundCall = false;
        param1 = null;
        callCategory = "";
        dialCall = "";
        if (stationNo != null && !stationNo.isEmpty()) {
            dialShow = true;
        } else {
            dialShow = false;
        }
    }

    public void removeContactHistory() {
        contactHistory = null;
    }
    
    public void clearRefYes() {
        refYes = null;
    }

    public void removeCustomerSessionListener(ActionEvent event) {
        removeCustomerSession();
    }

    public void showInboundContactSummary() {
//        inboundCall = true;
        this.setShowContactSummary(true);
    }

    public void showContactSummaryListener(ActionEvent event) {
        outboundCall = true;
        this.setShowContactSummary(true);
    }
    
    private String getXML(String urlStr) {
        try {
            
            URL apiEndPoint = new URL(urlStr);
            URLConnection connection = apiEndPoint.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));

            StringBuilder builder = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
                builder.append("\n");
            }
            in.close();

            return builder.toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Url of the API is not correct.");
        } catch (IOException e) {
            throw new RuntimeException("Cannot communicate with the server.");
        }
    }

    public void dialTelephonyListener(ActionEvent event) {
        msgTelephony = "";
        String telephonyName;
        if(JSFUtil.getUserSession().getUsers().getTelephonySso()) {
            telephonyName = JSFUtil.getUserSession().getUsers().getLdapLogin();
        } else {
            telephonyName = JSFUtil.getUserSession().getUsers().getTelephonyLoginName();
        }
        if(!dialClicked) {           
            try {
                CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
                 // IF ADAPTER IS ASPECT WILL CHECK STATE BEFORE DIAL
                if(stationNo != null && !stationNo.equals("") && ctiAdapter != null && ctiAdapter.getCtiVendorCode() != null && ctiAdapter.getCtiVendorCode().equals("aspect")) {
                    JSONObject stateInfo = telephonyService.getCurrentstate(telephonyName, stationNo);   
                    if(stateInfo != null) {
                        String msg = stateInfo.getString("Message"); 
                        String state = stateInfo.getString("CurrentState"); 
                        if(msg.equals("null") || msg.equals("")) {
                            if(state.toUpperCase().equals("PARKED") || state.toUpperCase().equals("IDLE")) {
                                //CALL TELEPHONY SERVICE FOR DIAL 
                                String result = telephonyService.dial(telephonyName, stationNo, user.getUserGroup().getTelephonyServiceId(), dialNo);
                                ctiLog.info(new Date().toString()+" DIAL Extension: "+stationNo+", DIAL No: "+dialNo+", DIAL Result: "+result);
                                                       
                                if(result.equals("SUCCESS")) {
                                    contactHistory = new ContactHistory();
                                    clickNextCall = false;
                                    dialClicked = true;
                                    outboundCall = true;
                                    channel = "outbound";
                                    callCategory = "manualOutbound";
                                    dialShow = false;
                                    hangupShow = true;
                                    nextcallShow = false;
                                    dialNo = (dialNo != null ? dialNo.trim() : "");
                                    contactTo = dialNo;
                                    dialCall = dialNo;
                                    this.setShowContactSummary(true);

                                    FacesContext ctx = FacesContext.getCurrentInstance();                                   
                                    com.maxelyz.core.controller.HelperBean helperBean = (com.maxelyz.core.controller.HelperBean) ctx.getELContext().getELResolver().getValue(ctx.getELContext(), null, "helperBean");
                                    helperBean.contactSummaryPopupListener(event); //force load contactsummary in view
                                
                                    this.createContactHistory();
                                } else {
                                    msgTelephony = result;
                                }                                
                                
                            } else {
                                // RETURN INVALID STATE
                                ctiLog.info(new Date().toString()+" Extension: "+stationNo+" INVALID STATE: "+state+" CANNOT DIAL");
                                msgTelephony = "INVALID STATE: "+state+" CANNOT DIAL";
                            }
                        } else {
                            ctiLog.info(new Date().toString()+" Extension: "+stationNo+" ERROR getCurrentstate: "+msg);
                        }
                    }
                } else {
                    // CALL TELEPHONY SERVICE FOR DIAL
                    contactHistory = new ContactHistory();
                    clickNextCall = false;
                    dialClicked = true;
                    outboundCall = true;
                    channel = "outbound";
                    callCategory = "manualOutbound";
                    dialShow = false;
                    hangupShow = true;
                    nextcallShow = false;
                    dialNo = (dialNo != null ? dialNo.trim() : "");
                    contactTo = dialNo;
                    dialCall = dialNo;
                    this.setShowContactSummary(true);
                                
                    if(stationNo != null && !stationNo.equals("")) {   
                        String result = telephonyService.dial(telephonyName, stationNo, user.getUserGroup().getTelephonyServiceId(), dialNo);
                        ctiLog.info(new Date().toString()+" DIAL Extension: "+stationNo+", DIAL No: "+dialNo+", DIAL Result: "+result);

                        // CALL NICE TO UPDATE DATA REFERENCE FOR RECORDER TRACK
                        if(ctiAdapter != null && ctiAdapter.getRecorderVendorCode() != null && ctiAdapter.getRecorderVendorCode().equals("nice")) {
                            telephonyService.callNiceUpdateOpenCall(stationNo, refYes, dialCall, customerDetail.getName(), customerDetail.getSurname(), customerDetail.getFlexfield1(), JSFUtil.getUserSession().getUsers());
                        }
                    }
                    this.createContactHistory();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        }
    }
    
    public void extTelephonyListener(ActionEvent event) {
        //CALL WEB SERVICE FOR Extension Number 
        System.out.println("Call Extension");
    } 
        
    public void notifyContactHistory() {
        dialClicked = false;
        if(!dialClicked) {           
            try {
                contactHistory = new ContactHistory();
                clickNextCall = false;
                dialClicked = true;
                inboundCall = true;
                outboundCall = false;
                channel = "inbound";
                //callCategory = "";
                dialShow = false;
                hangupShow = true;
                nextcallShow = false;
                dialNo = (dialNo != null ? dialNo.trim() : "");
                contactTo = dialNo;
                dialCall = dialNo;
                showContactSummary = true;
                
                this.createContactHistory();   
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        }
    }
        
    public void synStateWrap() {
        dialShow = false;
        hangupShow = false;
        nextcallShow = true;
        clickNextCall = false;
        dialClicked = true;
        refYes = null;
        dialCall = null;
        this.setShowContactSummary(true);
    }
    
    public void synStatePark() {
        dialShow = true;
        hangupShow = false;
        nextcallShow = false;        
        clickNextCall = true;
        dialClicked = false;
    }
    
    public void saveTelephonyTrackID() {
        String trackId = null;
        try {
            String telephonyName;
            if(JSFUtil.getUserSession().getUsers().getTelephonySso()) {
                telephonyName = JSFUtil.getUserSession().getUsers().getLdapLogin();
            } else {
                telephonyName = JSFUtil.getUserSession().getUsers().getTelephonyLoginName();
            }
            CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
            if(stationNo != null && !stationNo.equals("") && ctiAdapter != null && ctiAdapter.getCtiVendorCode() != null && ctiAdapter.getCtiVendorCode().equals("aspect")) { 
                // CALL ASPECT TO GET TALK TIME AND TRACK ID
                JSONObject trackInfo = telephonyService.getTrackid(telephonyName, stationNo);   
                if(trackInfo != null) {
                    String msgTrackInfo = trackInfo.getString("Message"); 
                    trackId = trackInfo.getString("TrackId");                    

                    if(msgTrackInfo.equals("null") || msgTrackInfo.equals("")) {
                        ctiLog.info(new Date().toString()+" GETTRACKID on ACTIVE Extension: "+stationNo+", Result: "+trackId);
                        JSFUtil.getUserSession().setTelephonyTrackId(trackId);
                    } else {                        
                        ctiLog.info(new Date().toString()+" GETTRACKID on ACTIVE ERROR: "+stationNo+", Result: "+msgTrackInfo);
                    }       
                }
            }        
            if(JSFUtil.getUserSession().getContactHistory().getId() != null) {
                contactHistoryDAO.updateTrackId(contactHistory, telephonyTrackId);
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }    
    }
    
    public void hangupTelephonyListener(ActionEvent event) {  
        msgTelephony = "";
        try {
            String telephonyName;
            if(JSFUtil.getUserSession().getUsers().getTelephonySso()) {
                telephonyName = JSFUtil.getUserSession().getUsers().getLdapLogin();
            } else {
                telephonyName = JSFUtil.getUserSession().getUsers().getTelephonyLoginName();
            }
            if(stationNo != null && !stationNo.equals("")) {      
                String result = "";
                
                // IF ADAPTER IS ASPECT WILL CHECK STATE BEFORE HANGUP
                CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
                if(ctiAdapter != null && ctiAdapter.getCtiVendorCode() != null && ctiAdapter.getCtiVendorCode().equals("aspect")) {
                    JSONObject stateInfo = telephonyService.getCurrentstate(telephonyName, stationNo);   
                    if(stateInfo != null) {
                        String msg = stateInfo.getString("Message"); 
                        String state = stateInfo.getString("CurrentState"); 
                        if(msg.equals("null") || msg.equals("")) {
                            if(state.toUpperCase().equals("DIALING") || state.toUpperCase().equals("ACTIVE")) {
                                // CALL TELEPHONY SERVICE FOR HANG UP 
                                dialShow = false;
                                hangupShow = false;
                                nextcallShow = true;
                                clickNextCall = false;
                                dialClicked = true;
        
                                result = telephonyService.hangup(telephonyName, stationNo);
                                ctiLog.info(new Date().toString()+" HANGUP Extension: "+stationNo+", Result: "+result);
                            } else if(state.toUpperCase().equals("WRAP") || state.toUpperCase().equals("IDLE") || state.toUpperCase().equals("PARKED")) {
                                dialShow = false;
                                hangupShow = false;
                                nextcallShow = true;
                                clickNextCall = false;
                                dialClicked = true;
                                
                                result = "No Need to Hangup with State: " + state;
                                ctiLog.info(new Date().toString()+" HANGUP Extension: "+stationNo+", Result: "+result);
                            } else if(state.toUpperCase().equals("HELD")) {
                                // RETURN INVALID STATE
                                ctiLog.info(new Date().toString()+" Extension: "+stationNo+" INVALID STATE: "+state+" CANNOT HANGUP");
                                msgTelephony = "INVALID STATE: Please UNHOLD Before HANGUP";
                            } else {
                                ctiLog.info(new Date().toString()+" Extension: "+stationNo+" INVALID STATE: "+state+" CANNOT HANGUP");
                                msgTelephony = "INVALID STATE: "+state+" CANNOT HANGUP";
                            }
                        } else {
                            ctiLog.info(new Date().toString()+" Extension: "+stationNo+" ERROR getCurrentstate: "+msg);
                        }
                    }
                } else { 
                    // CALL TELEPHONY SERVICE FOR HANG UP 
                    dialShow = false;
                    hangupShow = false;
                    nextcallShow = true;
                    clickNextCall = false;
                    dialClicked = true;
                                
                    result = telephonyService.hangup(telephonyName, stationNo);
                    ctiLog.info(new Date().toString()+" HANGUP Extension: "+stationNo+", Result: "+result);
                }
                                
                // CALL NICE TO UPDATE DATA REFERENCE FOR RECORDER TRACK
                if(ctiAdapter != null && ctiAdapter.getRecorderVendorCode() != null && ctiAdapter.getRecorderVendorCode().equals("nice")) {
                    telephonyService.callNiceUpdateAnyOpenCall(stationNo, refYes, dialCall, customerDetail.getName(), customerDetail.getSurname(), customerDetail.getFlexfield1(), JSFUtil.getUserSession().getUsers());                    
                }                 
            }
            refYes = null;
            dialCall = null;
            this.setShowContactSummary(true);
        } catch (Exception e) {
            log.error(e);
        }
    }
        
    private void createContactHistory() {  
        try {
            Integer channelId = 0;

            if (channel.equals("inbound")) {// || inboundCall) {
//                inboundCall = true;
                channelId = 2;
            } else if (channel.equals("outbound")) {// || outboundCall) {
//                outboundCall = true;
                channelId = 9;
            }

            if (contactHistory == null) {
                contactHistory = new ContactHistory();
            }
            
            if (contactHistory.getCustomer() == null) {
                if (customerDetail != null) {
                    contactHistory.setCustomer(customerDAO.findCustomer(customerDetail.getId()));
                }
            }
            if (contactHistory.getId() == null) {
                contactHistory.setUniqueId(uniqueId);
                contactHistory.setContactDate(new Date());
                contactHistory.setContactStatus("c");//close
                contactHistory.setDmccontact(true);
                contactHistory.setCallSuccess(true);
                contactHistory.setFollowupsale(false);
                contactHistory.setContactClose(false);
                contactHistory.setCreateDate(new Date());
                contactHistory.setCreateBy(this.getUserName());
                contactHistory.setUsers(user);
                contactHistory.setStationNo(stationNo);
            }
            if (channelId != null && channelId != 0) {
                contactHistory.setChannel(new Channel(channelId));
            }
            if (contactHistory.getContactTo() == null || contactHistory.getContactTo().equals("")) {
                contactHistory.setContactTo(contactTo);
            }
            //if (contactHistory.getTelephonyTrackId() == null || contactHistory.getTelephonyTrackId().equals("") || contactHistory.getTelephonyTrackId().equals("0")) {
            //    contactHistory.setTelephonyTrackId(trackId);
            //}

            try {
                if (contactHistory.getId() == null) {
                    if (assignmentDetail != null && assignmentDetail.getId() != null) {
                        try {
                            if (assignmentDetail.getNewList() != null && assignmentDetail.getNewList()) {
                                assignmentDetail.setNewList(Boolean.FALSE);
                            }
                            
                            if(assignmentDetail.getStatus().equals("followup")) {
                                assignmentDetailDAO.setNewToOldList(assignmentDetail);
                            } else {
                                assignmentDetailDAO.setOpenStatus(assignmentDetail);
                            }
                            
                            this.statusChangeAction();
                            AssignmentDetail ad = assignmentDetailDAO.findAssignmentDetail(this.assignmentDetail.getId());
                            this.setAssignmentDetail(ad);

                        } catch (Exception e) {
                            log.error(e);
                        }

                        //update Marketing Customer status to old where press dial
                        if (assignmentDetail.getAssignmentId() != null && customerDetail.getId() != null) {
                            try {
                                MarketingCustomerPK mck = new MarketingCustomerPK(assignmentDetail.getAssignmentId().getMarketing().getId(), customerDetail.getId());
                                MarketingCustomer mc = marketingCustomerDAO.findMarketingCustomer(mck);
                                if(mc != null) {
                                    //if (mc.getFirstContactDate() == null) {
                                    //    mc.setFirstContactDate(new Date());
                                    //}
                                    mc.setNewList(Boolean.FALSE);
                                    marketingCustomerDAO.edit(mc);
                                }
                            } catch (Exception e) {
                                log.error(e);
                            }

                        }

                        List<AssignmentDetail> assignmentDetails = new ArrayList<AssignmentDetail>();
                        assignmentDetails.add(assignmentDetail);
                        contactHistory.setAssignmentDetailCollection(assignmentDetails);
                    }
                   
                    if(contactHistory.getStationNo() == null || contactHistory.getStationNo().equals("")){
                        contactHistory.setStationNo(stationNo);
                    }
                    contactHistoryDAO.create(contactHistory);
                    this.countCall();
                }
            } catch (Exception e) {
                //log.error("userSession.createContactHistory 1 :" + e);
            }
            //userSession.setContactHistory(contactHistory);
        } catch (Exception e) {
            //log.error("userSession.createContactHistory 2 :" + e);
        }
    }

    private void countCall() {
        if (assignmentDetail != null) { //Don't have Telephony
            if(contactTo != null && !contactTo.equals("")){
                this.setDialNo(contactTo);
            }
            
            Integer max = assignmentDetail.getMaxCall() != null ? assignmentDetail.getMaxCall() : 0;
            Integer count = assignmentDetail.getCallAttempt() != null ? assignmentDetail.getCallAttempt() : 0;
            Integer max2 = assignmentDetail.getMaxCall2() != null ? assignmentDetail.getMaxCall2() : 0;
            Integer count2 = assignmentDetail.getCallAttempt2() != null ? assignmentDetail.getCallAttempt2() : 0;
            boolean dmc = assignmentDetail.isDmc();
            if (!dmc) {
                if ((count < max)) {
                    count++;
                    assignmentDetail.setCallAttempt(count);
                    try {
                        this.getAssignmentDetailDAO().saveCall(assignmentDetail.getId(), count);
                    } catch (Exception e) {
                    }
                } else if (count >= max) {
                    this.setDialShow(false);
                }
            } else if (dmc) {
                if ((count2 < max2)) {
                    count2++;
                    assignmentDetail.setCallAttempt2(count2);
                    try {
                        this.getAssignmentDetailDAO().saveCall2(assignmentDetail.getId(), count2);
                    } catch (Exception e) {
                    }
                } else if (count2 >= max2) {
                    this.setDialShow(false);
                }

            }
        }
    }
//  
//    public void dialListener(ActionEvent event) {
//        //dial button - called from customerInfo.xhtml
//        if(!dialClicked){
//            contactHistory = new ContactHistory();
//            clickNextCall = false;
//            dialClicked = true;
//            outboundCall = true;
//            channel = "outbound";
//            callCategory = "manualOutbound";
//            //this.setShowContactSummary(true);
//            
//            FacesContext ctx = FacesContext.getCurrentInstance();
//            try {
//                com.maxelyz.core.controller.HelperBean helperBean = (com.maxelyz.core.controller.HelperBean) ctx.getELContext().getELResolver().getValue(ctx.getELContext(), null, "helperBean");
//                helperBean.contactSummaryPopupListener(event); //force load contactsummary in view
//            } catch (Exception e) {
//                log.error(e);
//            }
//
//            //AssignmentDetail ad = JSFUtil.getUserSession().getAssignmentDetail();
//            //if (assignmentDetail == null){
//            //    initAssignmentDetail();
//            //}
//            /*
//            if (assignmentDetail != null) {
//                if(assignmentDetail.getId() != null){
//                    try {     
//                        this.assignmentDetailDAO.setOpenStatus(assignmentDetail);
//                        statusChangeAction();
//                    } catch (Exception e) {
//                        log.error(e);
//                    }
//                } else {
//                    assignmentDetail.setStatus("opened");
//                }
//                //update Marketing Customer status to old where press dial
//                if(assignmentDetail.getAssignmentId() != null && customerDetail.getId() != null){
//                    try{
//                        MarketingCustomerPK mck = new MarketingCustomerPK(assignmentDetail.getAssignmentId().getMarketing().getId(), this.customerDetail.getId());
//                        MarketingCustomer mc = marketingCustomerDAO.findMarketingCustomer(mck);
//                        mc.setNewList(Boolean.FALSE);
//                        marketingCustomerDAO.edit(mc);                
//                    }catch(Exception e){
//                        log.error(e);
//                    }
//
//                }
//            }
//            assignmentDetail = assignmentDetailDAO.findAssignmentDetail(assignmentDetail.getId());
//            JSFUtil.getUserSession().setAssignmentDetail(assignmentDetail);
//            */
//        }
//    }
//    
//    private void countCall(UserSession userSession) {
//        //String telephonyTrackId = userSession.getTelephonyTrackId();
//        AssignmentDetail assignmentDetail = userSession.getAssignmentDetail();
//        //String contactTo = userSession.getContactTo();
////        String dialNo = userSession.getDialNo();
//        
////        if (trackId != null && !trackId.equals("") && assignmentDetail != null) {//Have Telephony
//        if (assignmentDetail != null) { //Don't have Telephony
//            if(contactTo != null && !contactTo.equals("")){
//                userSession.setDialNo(contactTo);
//            }
//            
//            Integer max = assignmentDetail.getMaxCall() != null ? assignmentDetail.getMaxCall() : 0;
//            Integer count = assignmentDetail.getCallAttempt() != null ? assignmentDetail.getCallAttempt() : 0;
//            Integer max2 = assignmentDetail.getMaxCall2() != null ? assignmentDetail.getMaxCall2() : 0;
//            Integer count2 = assignmentDetail.getCallAttempt2() != null ? assignmentDetail.getCallAttempt2() : 0;
//            boolean dmc = assignmentDetail.isDmc();
//            if (!dmc) {
//                if ((count < max)) {
//                    count++;
//                    assignmentDetail.setCallAttempt(count);
//                    try {
//                        userSession.getAssignmentDetailDAO().saveCall(assignmentDetail.getId(), count);
//                    } catch (Exception e) {
//                    }
//                } else if (count >= max) {
//                    userSession.setDialShow(false);
//                }
//            } else if (dmc) {
//                if ((count2 < max2)) {
//                    count2++;
//                    assignmentDetail.setCallAttempt2(count2);
//                    try {
//                        userSession.getAssignmentDetailDAO().saveCall2(assignmentDetail.getId(), count2);
//                    } catch (Exception e) {
//                    }
//                } else if (count2 >= max2) {
//                    userSession.setDialShow(false);
//                }
//
//            }
//        }
//    }

//    public void dialNoTelListener(ActionEvent event) {
//        //dial button - called from customerInfo.xhtml
//        if(!dialClicked){
//            contactHistory = new ContactHistory();
//            clickNextCall = false;
//            dialClicked = true;
//            outboundCall = true;
//            channel = "outbound";
//            callCategory = "manualOutbound";
//            this.setShowContactSummary(true);
//
//            FacesContext ctx = FacesContext.getCurrentInstance();
//            try {
//                com.maxelyz.core.controller.HelperBean helperBean = (com.maxelyz.core.controller.HelperBean) ctx.getELContext().getELResolver().getValue(ctx.getELContext(), null, "helperBean");
//                helperBean.contactSummaryPopupListener(event); //force load contactsummary in view
//            } catch (Exception e) {
//                log.error(e);
//            }
//
//            //AssignmentDetail ad = JSFUtil.getUserSession().getAssignmentDetail();
//            /*
//            if (assignmentDetail == null){
//                initAssignmentDetail();
//            }
//            if (assignmentDetail != null) {
//                if(assignmentDetail.getId() != null){
//                    try {     
//                        this.assignmentDetailDAO.setOpenStatus(assignmentDetail);
//                        statusChangeAction();
//                    } catch (Exception e) {
//                        log.error(e);
//                    }
//                } else {
//                    assignmentDetail.setStatus("opened");
//                }
//                //update Marketing Customer status to old where press dial
//                if(assignmentDetail.getAssignmentId() != null && customerDetail.getId() != null){
//                    try{
//                        MarketingCustomerPK mck = new MarketingCustomerPK(assignmentDetail.getAssignmentId().getMarketing().getId(), this.customerDetail.getId());
//                        MarketingCustomer mc = marketingCustomerDAO.findMarketingCustomer(mck);
//                        mc.setNewList(Boolean.FALSE);
//                        marketingCustomerDAO.edit(mc);                
//                    }catch(Exception e){
//                        log.error(e);
//                    }
//
//                }
//            }
//            if(assignmentDetail != null && assignmentDetail.getId() != null){
//                assignmentDetail = assignmentDetailDAO.findAssignmentDetail(assignmentDetail.getId());
//            }
//            JSFUtil.getUserSession().setAssignmentDetail(assignmentDetail);
//            */
//        }
//    }
    
//    private void initAssignmentDetail(){
//            assignmentDetail = new AssignmentDetail();
//            assignmentDetail.setUsers(this.user);
//            assignmentDetail.setAssignmentId(null);
//            assignmentDetail.setCustomerId(new Customer(this.customerDetail.getId()));
//            assignmentDetail.setCustomerName(this.customerDetail.getName() + ' ' + this.customerDetail.getSurname());
//            assignmentDetail.setStatus("followup");
//            assignmentDetail.setAssignDate(new Date());
//            try{
//                //assignmentDetailDAO.create(assignmentDetail);
//            }catch(Exception e){
//                log.error(e);
//            }
//    }

    public boolean isDialClicked() {
        return dialClicked;
    }

    public int getSessionTimeout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) context.getExternalContext().getSession(false);
        return httpSession.getMaxInactiveInterval(); //min * 60 (from web.xml)
    }
    
    public void statusChangeAction(){
        if(assignmentDetail != null && assignmentDetail.getId() != null && assignmentDetail.getAssignmentId() != null
                && assignmentDetail.getAssignmentId().getCampaign() != null && assignmentDetail.getAssignmentId().getCampaign().getId() != null
                && customerDetail != null && customerDetail.getId() != null){
            CampaignCustomerPK ccPK = new CampaignCustomerPK(assignmentDetail.getAssignmentId().getCampaign().getId(), customerDetail.getId());
            CampaignCustomer cc = campaignDAO.findCampaignCustomer(ccPK);
            if(cc != null) {
                if(cc.getListUsed() != null && cc.getListUsed()){
                    if(cc.getAssignmentDetail() != null && cc.getAssignmentDetail().getId().intValue() != assignmentDetail.getId().intValue()){
                        cc.setListReused(Boolean.TRUE);
                    }
                }else{                    
                    cc.setListUsed(Boolean.TRUE);
                    cc.setAssignmentDetail(assignmentDetail);
                    cc.setListUsedDate(new Date());
                }
                try{
                    campaignDAO.editCampaignCustomer(cc);
                }catch(Exception e){
                    log.error(e);
                }
            }
        }
    }
    
    public void disableInboundCall() {
        inboundCall = false;
    }

    public void changeModeListener(ActionEvent event) {
        mode = JSFUtil.getRequestParameterMap("mode");        
    }

    public void changeTelephonyEditListener(ActionEvent event) {
        telephonyEdit = JSFUtil.getRequestParameterMap("telephonyEdit");        
    }

    public void logoutActionListener(ActionEvent event) {
        //this.removeSessionTomcat(this.user.getLoginName());
        JSFUtil.getSession().invalidate();
        JSFUtil.redirect(JSFUtil.getRequestContextPath()+"/?faces-redirect=true");
    }
    
    private void removeSessionTomcat(String loginName){
        String groups[] = {"concurrentusersgroup1","concurrentusersgroup2","concurrentusersgroup3","concurrentusersgroup4","concurrentusersgroup5"};
        ServletContext application = JSFUtil.getServletContext();
        for (String group : groups) {
            if (application.getAttribute(group) != null) {
                Map<String, SessionMonitorValue> concurrentUserMap = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute(group);
                if (concurrentUserMap.containsKey(loginName)) {
                    try {
                        synchronized (concurrentUserMap) {
                            concurrentUserMap.remove(loginName);       
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    public String getMainPage() {
        return mainPage;
    }

    public void setMainPage(String mainPage) {
        this.mainPage = mainPage;
    }

    public String getSubPage() {
        return subPage;
    }

    public void setSubPage(String subPage) {
        this.subPage = subPage;
    }

    public boolean isInboundCall() {
        return inboundCall;
    }

    public void setInboundCall(boolean inboundCall) {
        this.inboundCall = inboundCall;
    }

    public boolean isOutboundCall() {
        return outboundCall;
    }

    public void setOutboundCall(boolean outboundCall) {
        this.outboundCall = outboundCall;
    }

    public String getDialExt() {
        return dialExt;
    }

    public void setDialExt(String dialExt) {
        this.dialExt = dialExt;
    }

    public String getDialNo() {
        
        if(this.dialNo == null || this.dialNo.equals("")){
            if(customerDetail != null){
                if(customerDetail.getTelephoneDefault() != null && !customerDetail.getTelephoneDefault().equals("")){
                    if(customerDetail.getTelephoneDefault().equals("mobile")){
                        this.dialNo = customerDetail.getMobilePhone();
                    }else if(customerDetail.getTelephoneDefault().equals("office")){
                        this.dialNo = customerDetail.getOfficePhone();
                        this.dialExt = customerDetail.getOfficeExt();
                    }else if(customerDetail.getTelephoneDefault().equals("home")){
                        this.dialNo = customerDetail.getHomePhone();
                        this.dialExt = customerDetail.getHomeExt();
                    }else if(customerDetail.getTelephoneDefault().equals("contact1")){
                        this.dialNo = customerDetail.getContactPhone1();
                        this.dialExt = customerDetail.getContactExt1();
                    }else if(customerDetail.getTelephoneDefault().equals("contact2")){
                        this.dialNo = customerDetail.getContactPhone2();
                        this.dialExt = customerDetail.getContactExt2();
                    }else if(customerDetail.getTelephoneDefault().equals("contact3")){
                        this.dialNo = customerDetail.getContactPhone3();
                        this.dialExt = customerDetail.getContactExt3();
                    }else if(customerDetail.getTelephoneDefault().equals("contact4")){
                        this.dialNo = customerDetail.getContactPhone4();
                        this.dialExt = customerDetail.getContactExt4();
                    }else if(customerDetail.getTelephoneDefault().equals("contact5")){
                        this.dialNo = customerDetail.getContactPhone5();
                        this.dialExt = customerDetail.getContactExt5();
                    }
                }else{
                if(customerDetail.getMobilePhone() != null && !customerDetail.getMobilePhone().equals("")){
                    this.dialNo = customerDetail.getMobilePhone();
                }else if(customerDetail.getOfficePhone() != null && !customerDetail.getOfficePhone().equals("")){
                    this.dialNo = customerDetail.getOfficePhone();
                    this.dialExt = customerDetail.getOfficeExt();
                }else if(customerDetail.getHomePhone() != null && !customerDetail.getHomePhone().equals("")){
                    this.dialNo = customerDetail.getHomePhone();
                    this.dialExt = customerDetail.getHomeExt();
                }
            }
        }
        }
        
        return dialNo;
    }

    public void setDialNo(String dialNo) {
        this.dialNo = dialNo;
    }

    public String getTransferNo() {
        return transferNo;
    }

    public void setTransferNo(String transferNo) {
        this.transferNo = transferNo;
    }

    public String getCallCount() {
        return callCount;
    }

    public void setCallCount(String callCount) {
        this.callCount = callCount;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getContactToName() {
        return contactToName;
    }

    public void setContactToName(String contactToName) {
        this.contactToName = contactToName;
    }

    public String getDialToString() {
        String str = "";
        if (dialNo != null && !dialNo.equals("")) {
            str += dialNo;
        }
        if (dialExt != null && !dialExt.equals("")) {
            str += "," + dialExt;
        }
        dialToString = str;
        return dialToString;
    }

    public void setDialToString(String dialToString) {
        this.dialToString = dialToString;
    }

    public boolean isDialShow() {
//        if (SecurityUtil.isPermitted("telephonybar:view")) {
//            dialShow = true;
//        } else {
//            dialShow = false;
//        }
        
        if(inboundCall) {
            return dialShow;
        } else {
            if (stationNo != null && !stationNo.isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
//        return dialShow;
    }

//    public boolean isDialShow() {
//        return dialShow;
//    }
     
    public void setDialShow(boolean dialShow) {
        this.dialShow = dialShow;
    }

    public String getTelephonyTrackId() {
        return telephonyTrackId;
    }

    public void setTelephonyTrackId(String telephonyTrackId) {
        this.telephonyTrackId = telephonyTrackId;
            }
                
    public String getDialStatus() {
        return dialStatus != null ? dialStatus.toLowerCase() : null;
    }

    public void setDialStatus(String dialStatus) {
        this.dialStatus = ((dialStatus != null) ? dialStatus.toLowerCase() : null);
        if(this.dialStatus != null && this.dialStatus.equals("active")){
            dialStatusActive = true;
        }
    }

    public ContactHistory getContactHistory() {
        return contactHistory;
    }

    public void setContactHistory(ContactHistory contactHistory) {
        this.contactHistory = contactHistory;
    }

    public String getContactTo() {
        return contactTo;
    }

    public void setContactTo(String contactTo) {
        this.contactTo = contactTo;
    }

    public ContactHistoryDAO getContactHistoryDAO() {
        return contactHistoryDAO;
    }

    public void setContactHistoryDAO(ContactHistoryDAO contactHistoryDAO) {
        this.contactHistoryDAO = contactHistoryDAO;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Acl> getUserGroupAclList() {
        return userGroupAclList;
    }

    public void setUserGroupAclList(List<Acl> userGroupAclList) {
        this.userGroupAclList = userGroupAclList;
    }

    public List<UserGroupLocation> getUserGroupLocationList() {
        return userGroupLocationList;
    }

    public void setUserGroupLocationList(List<UserGroupLocation> userGroupLocationList) {
        this.userGroupLocationList = userGroupLocationList;
    }

    public String getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(String firstPage) {
        this.firstPage = firstPage;
    }

    public String getLdapPassword() {
        return ldapPassword;
    }

    public void setLdapPassword(String ldapPassword) {
        this.ldapPassword = ldapPassword;
    }

    public MarketingCustomerDAO getMarketingCustomerDAO() {
        return marketingCustomerDAO;
    }

    public void setMarketingCustomerDAO(MarketingCustomerDAO marketingCustomerDAO) {
        this.marketingCustomerDAO = marketingCustomerDAO;
    }

    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTelephonyEdit() {
        return telephonyEdit;
    }

    public void setTelephonyEdit(String telephonyEdit) {
        this.telephonyEdit = telephonyEdit;
    }

    public boolean isDialStatusActive() {
        return dialStatusActive;
    }

    public void setDialStatusActive(boolean dialStatusActive) {
        this.dialStatusActive = dialStatusActive;
    }

    public boolean isClickNextCall() {
        return clickNextCall;
    }

    public void setClickNextCall(boolean clickNextCall) {
        this.clickNextCall = clickNextCall;
    }

    public void setDialClicked(boolean dialClicked) {
        this.dialClicked = dialClicked;
    }

    public boolean isHasClick() {
        return hasClick;
}

    public void setHasClick(boolean hasClick) {
        this.hasClick = hasClick;
    }

    public String getFromTab() {
        return fromTab;
    }

    public void setFromTab(String fromTab) {
        this.fromTab = fromTab;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public Integer getTalkTime() {
        return talkTime;
    }

    public void setTalkTime(Integer talkTime) {
        this.talkTime = talkTime;
//        if(this.contactHistory != null && this.contactHistory.getId() != null && this.contactHistory.getTelephonyTrackId() != null && !this.contactHistory.getTelephonyTrackId().equals("") && !this.contactHistory.getTelephonyTrackId().equals("0")){
//            //this.contactHistory.setTalkTime(this.talkTime);
//            try{
//                this.contactHistoryDAO.updateTalkTime(contactHistory, talkTime);
//            }catch(Exception e){
//            }
//        }
    }

    public Integer getContactCaseId() {
        return contactCaseId;
    }

    public void setContactCaseId(Integer contactCaseId) {
        this.contactCaseId = contactCaseId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Integer getWorkflowlogId() {
        return workflowlogId;
    }

    public void setWorkflowlogId(Integer workflowlogId) {
        this.workflowlogId = workflowlogId;
    }

    public boolean isHasKnowledgeBase() {
        return knowledgebases.size() > 0;
    }

    public void setHasKnowledgeBase(boolean hasKnowledgeBase) {
        this.hasKnowledgeBase = hasKnowledgeBase;
    }

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    public boolean isHangupShow() {
        return hangupShow;
    }

    public void setHangupShow(boolean hangupShow) {
        this.hangupShow = hangupShow;
    }

    public boolean isNextcallShow() {
        return nextcallShow;
    }

    public void setNextcallShow(boolean nextcallShow) {
        this.nextcallShow = nextcallShow;
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public Integer getContactResultId() {
        return contactResultId;
    }

    public void setContactResultId(Integer contactResultId) {
        this.contactResultId = contactResultId;
    }

    public String getContactResultCode() {
        return contactResultCode;
    }

    public void setContactResultCode(String contactResultCode) {
        this.contactResultCode = contactResultCode;
    }

    public String getConvertVoiceURL() {
        return convertVoiceURL;
    }

    public void setConvertVoiceURL(String convertVoiceURL) {
        this.convertVoiceURL = convertVoiceURL;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getCallCategory() {
        return callCategory;
    }

    public void setCallCategory(String callCategory) {
        this.callCategory = callCategory;
    }

    public TelephonyService getTelephonyService() {
        return telephonyService;
    }

    public void setTelephonyService(TelephonyService telephonyService) {
        this.telephonyService = telephonyService;
    }

    public String getRefYes() {
        return refYes;
    }

    public void setRefYes(String refYes) {
        this.refYes = refYes;
    }

    public String getDialCall() {
        return dialCall;
    }

    public void setDialCall(String dialCall) {
        this.dialCall = dialCall;
    }

    public String getMsgTelephony() {
        return msgTelephony;
    }

    public void setMsgTelephony(String msgTelephony) {
        this.msgTelephony = msgTelephony;
    }

    public String getCtiToolbar() {
        return ctiToolbar;
    }

    public void setCtiToolbar(String ctiToolbar) {
        this.ctiToolbar = ctiToolbar;
    }

    public Integer getContactResultPlanId() {
        return contactResultPlanId;
    }

    public void setContactResultPlanId(Integer contactResultPlanId) {
        this.contactResultPlanId = contactResultPlanId;
    }

    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }
   
    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }
    
    public String getNotificationUuid() {
        return notificationUuid;
    }
    
    public void setNotificationUuid(String uuid) {
        this.notificationUuid = uuid;
    }

    public Integer getNotificationRefId() {
        return notificationRefId;
    }
    
    public void setNotificationRefId(Integer refId) {
        this.notificationRefId = refId;
    }
    
    public String getNotificationPhoneNumber() {
        return notificationPhoneNumber;
    }

    public void setNotificationPhoneNumber(String phoneNumber) {
        this.notificationPhoneNumber = phoneNumber;
    }
    
    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }
    
    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }
    
    public NotificationMessageDAO getNotificationMessageDAO() {
        return notificationMessageDAO;
    }
    
    public void setNotificationMessageDAO(NotificationMessageDAO notificationMessageDAO) {
        this.notificationMessageDAO = notificationMessageDAO;
    }
    
    public List<NotificationMessage> getNotificationMessages() {  
        //return getNotificationMessageDAO().findNotificationMessageByLoginName(user.getLoginName());
        return getNotificationMessageDAO().findNotificationMessageByLoginName(user.getTelephonyLoginName());
    }

    public void setNotificationMessages(NotificationMessage notification) {
        for (NotificationMessage notificationMessage : notificationMessages) {
            if (notificationMessage.getId().equals(notification.getId())) {
                return;
            }
        }
        notificationMessages.add(notification);    
    }
    
    public void notificationMessageListener(ActionEvent event) {       
        try {
            String telephonyName;
            if(JSFUtil.getUserSession().getUsers().getTelephonySso()) {
                telephonyName = JSFUtil.getUserSession().getUsers().getLdapLogin();
            } else {
                telephonyName = JSFUtil.getUserSession().getUsers().getTelephonyLoginName();
            }
            //String uuid = (String) event.getComponent().getAttributes().get("uuid");
            //Integer refId = (Integer) event.getComponent().getAttributes().get("refId");
            //String phoneNumber = (String) event.getComponent().getAttributes().get("phoneNumber");
            
            //notificationMessageDAO.updateStatusByUUID(uuid);
            notificationMessageDAO.updateStatusByRefId(notificationRefId);
            this.assignmentDetail = assignmentDetailDAO.findAssignmentDetail(notificationRefId);
            this.assignmentDetail.setStatus("opened");
            assignmentDetailDAO.edit(this.assignmentDetail);
            this.customerDetail = customerHandlingDAO.findCustomerInfo(this.assignmentDetail.getCustomerId().getId());
            this.dialNo = notificationPhoneNumber;
            
            CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
            if(stationNo != null && !stationNo.equals("") && ctiAdapter != null && ctiAdapter.getCtiVendorCode() != null && ctiAdapter.getCtiVendorCode().equals("aspect")) {
                //CTI Adapter is 'Aspect'
                JSONObject stateInfo = telephonyService.getCurrentstate(telephonyName, stationNo);   
                if(stateInfo != null) {
                    String msg = stateInfo.getString("Message"); 
                    String state = stateInfo.getString("CurrentState"); 
                    if(msg.equals("null") || msg.equals("")) {
                        if(state.toUpperCase().equals("ACTIVE")) {
                            contactHistory = new ContactHistory();
                            clickNextCall = false;
                            dialClicked = true;
                            outboundCall = true;
                            inboundCall = false;
                            channel = "outbound";
                            callCategory = "predictiveOutbound";
                            dialShow = false;
                            hangupShow = true;
                            nextcallShow = false;
                            
                            dialNo = (dialNo != null ? dialNo.trim() : "");
                            contactTo = dialNo;
                            dialCall = dialNo;
                            
                            this.setShowContactSummary(true);
                            this.createContactHistory();
                        } else {
                            contactHistory = new ContactHistory();
                            clickNextCall = false;
                            dialClicked = true;
                            outboundCall = true;
                            inboundCall = false;
                            channel = "outbound";
                            callCategory = "predictiveOutbound";
                            dialShow = false;
                            hangupShow = false;
                            nextcallShow = true;
                            
                            dialNo = (dialNo != null ? dialNo.trim() : "");
                            contactTo = dialNo;
                            dialCall = dialNo;
 
                            this.setShowContactSummary(true);
                            this.createContactHistory();
                        }
                    } else {
                        ctiLog.info(new Date().toString()+" Extension: "+stationNo+" ERROR getCurrentstate: " + msg);
                    }
                }
            } else { //CTI Adapter is not 'Aspect'
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                System.out.println(JSFUtil.getUserSession().getUserName() + "(not aspect) : Start notificationMessageListener, " + dateFormat.format(new Date()));
                
                contactHistory = new ContactHistory();
                clickNextCall = false;
                dialClicked = true;
                outboundCall = true;
                inboundCall = false;
                channel = "outbound";
                callCategory = "predictiveOutbound";
                dialShow = false;
                hangupShow = true;
                nextcallShow = false;
                
                dialNo = (dialNo != null ? dialNo.trim() : "");
                contactTo = dialNo;
                dialCall = dialNo;
                
                this.setShowContactSummary(true);
                this.createContactHistory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getNotificationMessageCount() {  
        if(JSFUtil.getUserSession().getUsers().getTelephonySso()) {
            return getNotificationMessageDAO().getNotificationMessageCount(user.getLdapLogin());
        } else {
            return getNotificationMessageDAO().getNotificationMessageCount(user.getTelephonyLoginName());
        } 
    }
    
    public String getNotificationMessageCustomerName(NotificationMessage notification) {   
        JsonParser jsonParser = new JsonParser();
        Object object = jsonParser.parse(notification.getDisplayValue());
        JsonArray jsonArray = (JsonArray) object;
        //System.out.println("Json object :: " + jsonArray.get(1).getAsJsonObject().get("value").getAsString());

        return jsonArray.get(1).getAsJsonObject().get("value").getAsString() + " " + jsonArray.get(2).getAsJsonObject().get("value").getAsString();
    }
    
    public String getJsonNotificationMessages(NotificationMessage notification) {
        return "{" +
        "\"uuid\":\"" + notification.getUuid() + "\"" +
        ",\"refid\":\"" + notification.getRefId() + "\"" +
        ",\"token\":\"" + notification.getToken() + "\"" +
        ",\"title\":\"" + notification.getTitle() + "\"" +
        ",\"phonenumber\":\"" + notification.getPhoneNumber() + "\"" +
        ",\"displayvalue\":\"" + notification.getDisplayValue().replace("\"", "\\\"") + "\"" + "}";
    }
    
}