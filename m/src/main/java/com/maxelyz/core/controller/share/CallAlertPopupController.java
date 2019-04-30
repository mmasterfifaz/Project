package com.maxelyz.core.controller.share;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.CustomerDAO;
import com.maxelyz.core.model.dao.PhoneDirectoryDAO;
import com.maxelyz.core.model.entity.Channel;
import com.maxelyz.core.model.entity.ContactHistory;
import com.maxelyz.core.model.entity.CtiAdapter;
import com.maxelyz.core.model.entity.Customer;
import com.maxelyz.core.model.entity.PhoneDirectory;
import com.maxelyz.core.service.TelephonyService;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.richfaces.json.JSONObject;

@ManagedBean
//@RequestScoped
@ViewScoped
public class CallAlertPopupController implements Serializable {

    private static Logger log = Logger.getLogger(CallAlertPopupController.class);
    private static String SEARCH_PAGE = "/front/search/searchCustomer.xhtml?faces-redirect=true";
    private static String CUSTOMER_HANDLING_PAGE = "/front/customerHandling/customerDetail.xhtml?faces-redirect=true";
    //private static String CUSTOMER_HANDLING_PAGE = "/front/customerHandling/saleApproaching.xhtml?faces-redirect=true";
    private static String REFRESH = "callalertpopup.xhtml";
    private String customerName;
    private String telephoneNo;
    private String manualCall;
    private String showTelephoneNo;
    private Integer customerId = 0;
    private String customerType;
    private String serviceName;
    private String callCount;
    private String uniqueId;
    private String dnis;
    private String callCategory;
    private String callParam1;
//    private String inboundType;
    private List<Customer> customers = new ArrayList<Customer>();
    private List<PhoneDirectory> operators = new ArrayList<PhoneDirectory>();
    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value="#{phoneDirectoryDAO}")
    private PhoneDirectoryDAO phoneDirectoryDAO;
    @ManagedProperty(value = "#{telephonyService}")
    private TelephonyService telephonyService;
    
    @PostConstruct
    public void initialize() {
//        inboundType = "manualAnswer";
        customerName = "";
    }

    public String refreshAction() {
        return null;
    }

    public String getCallInfo(String telephonyName, String stationNo) {
        String trackId = null;
        try {
            CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
            if(stationNo != null && !stationNo.equals("") && ctiAdapter != null && ctiAdapter.getCtiVendorCode() != null && ctiAdapter.getCtiVendorCode().equals("aspect")) { 
                // CALL ASPECT TO GET TALK TIME AND TRACK ID
                JSONObject trackInfo = telephonyService.getTrackid(telephonyName, stationNo);   
                if(trackInfo != null) {
                    String msgTrackInfo = trackInfo.getString("Message"); 
                    trackId = trackInfo.getString("TrackId");                    

                    if(msgTrackInfo.equals("null") || msgTrackInfo.equals("")) {
                        System.out.println("GETTRACKID Extension: "+stationNo+", Result: "+trackId);
                        JSFUtil.getUserSession().setTelephonyTrackId(trackId);
                    } else {                        
                        System.out.println("GETTRACKID ERROR: "+stationNo+", Result: "+msgTrackInfo);
                    }       
                }
            }        
            return trackId;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }        
        
    }
    
    private void createContactHistory(String channel) { 
        try {
            UserSession u = JSFUtil.getUserSession();
            ContactHistory contactHistory = new ContactHistory();
            Integer channelId = 0;
            String trackId = null;
            
            String telephonyName;
            if(u.getUsers().getTelephonySso()) {
                telephonyName = u.getUsers().getLdapLogin();
            } else {
                telephonyName = u.getUsers().getTelephonyLoginName();
            }
            
            if(manualCall.equals("false")) {    // EQUAL AUTO ANSWER
                trackId = getCallInfo(telephonyName, u.getStationNo());
            }             

            if (channel.equals("inbound")) {
                channelId = 2;
            } else if (channel.equals("outbound")) {
                channelId = 9;
            }

            Customer customer = null;
            if(customerId != null && customerId != 0) {
                customer = getCustomerDAO().findCustomer(customerId); 
            }

            if(trackId != null) {
                contactHistory.setTelephonyTrackId(trackId);
                JSFUtil.getUserSession().setTelephonyTrackId(trackId);
            }
            
            if (contactHistory.getId() == null) {
                contactHistory.setCustomer(customer);
                contactHistory.setContactDate(new Date());
                contactHistory.setContactTo(telephoneNo);
                contactHistory.setContactStatus("c");
                contactHistory.setDmccontact(true);
                contactHistory.setCallSuccess(true);
                contactHistory.setFollowupsale(false);
                contactHistory.setContactClose(false);
                contactHistory.setStationNo(JSFUtil.getUserSession().getStationNo());
                contactHistory.setCreateDate(new Date());
                contactHistory.setCreateBy(u.getUserName());
                contactHistory.setUsers(u.getUsers());
                if (channelId != 0) {
                    contactHistory.setChannel(new Channel(channelId));
                }
            }            
//            u.getContactHistoryDAO().create(contactHistory);
            u.setContactHistory(contactHistory);
            
        } catch (Exception e) {
//            log.error("userSession.createContactHistory 2 :" + e);
        }
    }
    
    public String unqinueCustomerAction() {
        if(manualCall.equals("true")) {
            manualAnswerCall();
        }             
        JSFUtil.getUserSession().setFirstPage("search");
        UserSession u = JSFUtil.getUserSession(); 
        u.setDialShow(false);
        u.setHangupShow(true);
        u.setNextcallShow(false);
        u.setDialClicked(true);
        u.setOutboundCall(false);
        u.setInboundCall(true);
        u.setClickNextCall(false);
        u.setChannel("inbound");
        createContactHistory("inbound");
        
        return CUSTOMER_HANDLING_PAGE;
    }

    public String mulipleCustomerAction() {
        if(manualCall.equals("true")) {
            manualAnswerCall();
        } 
        JSFUtil.getUserSession().setFirstPage("search");
        UserSession u = JSFUtil.getUserSession();
        u.setDialShow(false);
        u.setHangupShow(true);
        u.setNextcallShow(false);
        u.setDialClicked(true);
        u.setOutboundCall(false);
        u.setInboundCall(true);
        u.setClickNextCall(false);
        u.setChannel("inbound");
        createContactHistory("inbound");
        
        return SEARCH_PAGE;
    }
    
    public void manualAnswerCall() {
        try {
           String telephonyName;
            if(JSFUtil.getUserSession().getUsers().getTelephonySso()) {
                telephonyName = JSFUtil.getUserSession().getUsers().getLdapLogin();
            } else {
                telephonyName = JSFUtil.getUserSession().getUsers().getTelephonyLoginName();
            }             
            String answerResult = telephonyService.answer(telephonyName, JSFUtil.getUserSession().getStationNo());            
            System.out.println("ANSWER Extension: "+JSFUtil.getUserSession().getStationNo()+", Result: "+answerResult);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void rejectAnswerCall() {
        try {
            String telephonyName;
            if(JSFUtil.getUserSession().getUsers().getTelephonySso()) {
                telephonyName = JSFUtil.getUserSession().getUsers().getLdapLogin();
            } else {
                telephonyName = JSFUtil.getUserSession().getUsers().getTelephonyLoginName();
            } 
            String rejectResult = telephonyService.reject(telephonyName, JSFUtil.getUserSession().getStationNo());    
            System.out.println("REJECT Extension: "+JSFUtil.getUserSession().getStationNo()+", Result: "+rejectResult);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String getCustomerName() {
        if (telephoneNo != null) {
             //if (callCategory.equalsIgnoreCase("OUTBOUND")) {
                //customers = this.getCustomerDAO().findCustomerByRefNo(callParam1);
            //} else {
            customers = this.getCustomerDAO().findCustomerByTelephoneNo(telephoneNo);
            JSFUtil.getUserSession().setContactTo(telephoneNo);
            //}
            if (customers.size() == 1) {
                Customer c = customers.get(0);
                String custName="";
                if (c.getName()!=null) {
                    custName = c.getName();
                }
                if (c.getSurname()!=null) {
                    custName += " "+c.getSurname();
                }
                customerName = custName;
                customerId = customers.get(0).getId();
                customerType = customers.get(0).getCustomerType().getName();
            } else if (customers.isEmpty()) {
                customerName = "Unknown Customer";
                customerType = "-";
                customerId = 0;
            } else {
                customerName = "Multiple Customer";
                customerType = "-";
                customerId = 0;
            }
        }
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public String getShowTelephoneNo() {
        return showTelephoneNo;
    }

    public void setShowTelephoneNo(String showTelephoneNo) {
        this.showTelephoneNo = showTelephoneNo;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public boolean isFoundUniqueCustomer() {
        return customers.size() == 1;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public PhoneDirectoryDAO getPhoneDirectoryDAO() {
        return phoneDirectoryDAO;
    }

    public void setPhoneDirectoryDAO(PhoneDirectoryDAO phoneDirectoryDAO) {
        this.phoneDirectoryDAO = phoneDirectoryDAO;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDnis() {
        return dnis;
    }

    public void setDnis(String dnis) {
        this.dnis = dnis;
    }

    public String getCallCount() {
        return callCount;
    }

    public void setCallCount(String callCount) {
        this.callCount = callCount;
        JSFUtil.getUserSession().setCallCount(callCount);
    }
    
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
        JSFUtil.getUserSession().setUniqueId(uniqueId);
    }
    
    public String getCallCategory() {
        return callCategory;
    }

    public void setCallCategory(String callCategory) {
        this.callCategory = callCategory;
    }

    public String getCallParam1() {
        return callParam1;
    }

    public void setCallParam1(String callParam1) {
        this.callParam1 = callParam1;
    }

    public TelephonyService getTelephonyService() {
        return telephonyService;
    }

    public void setTelephonyService(TelephonyService telephonyService) {
        this.telephonyService = telephonyService;
    }

//    public String getInboundType() {
//        return inboundType;
//    }
//
//    public void setInboundType(String inboundType) {
//        this.inboundType = inboundType;
//    }

    public String getManualCall() {
        return manualCall;
    }

    public void setManualCall(String manualCall) {
        this.manualCall = manualCall;
    }
    
    
}
