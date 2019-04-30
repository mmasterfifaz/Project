/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.ParameterMappingValue;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.core.service.TelephonyService;
import com.maxelyz.utils.JSFUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import org.richfaces.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author admin
 */
@ManagedBean
@ViewScoped
public class NewContactController {

    private static Logger log = Logger.getLogger(NewContactController.class);
    private static Logger ctiLog = Logger.getLogger("mxCtiLogger");

    private Customer customer;
    private List<ContactResult> contactResultList;
    private List<ContactResult> mainContactResultList;
    
    private String contactStatus;
    private Integer contactResultId;
    private Integer mainContactResultId;
    private String mainContactResultCode;
    private List<String[]> contactStatusList;
    private ContactHistory contactHistory;

    private String oldContact1;
    private String oldContact2;
    private String oldContact3;
    private String oldContact4;
    private String oldContact5;
    private String oldContactExt1;
    private String oldContactExt2;
    private String oldContactExt3;
    private String oldContactExt4;
    private String oldContactExt5;
    private String oldMobile;
    private String oldHome;
    private String oldHomeExt;
    private String oldOffice;
    private String oldOfficeExt;
    private String oldFax;
    private String oldFaxExt;
    private String oldEmail;

    private boolean doNotCall = false;
    private List<String> chkFullName = new ArrayList<String>();
    private List<String> chkLastName = new ArrayList<String>();
    private List<String> chkTelPhone = new ArrayList<String>();

    private ContactResultPlan contactResultPlan;
    List<Integer> purchaseOrderId = new ArrayList<Integer>();

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{dataSourceExternal}")
    private DataSource dataSourceExternal;

    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
    @ManagedProperty(value = "#{contactResultDAO}")
    private ContactResultDAO contactResultDAO;

    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    @ManagedProperty(value = "#{marketingCustomerDAO}")
    private MarketingCustomerDAO marketingCustomerDAO;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    @ManagedProperty(value = "#{contactHistoryDAO}")
    private ContactHistoryDAO contactHistoryDAO;
    @ManagedProperty(value = "#{customerAuditLogDAO}")
    private CustomerAuditLogDAO customerAuditLogDAO;
    @ManagedProperty(value = "#{configurationDAO}")
    private ConfigurationDAO configurationDAO;
    @ManagedProperty(value = "#{telephonyService}")
    private TelephonyService telephonyService;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;

    @PostConstruct
    public void initialize() {

        // CLEAR MSG FOR TELEPOHNY BAR
        JSFUtil.getUserSession().setMsgTelephony("");

        CustomerInfoValue c = JSFUtil.getUserSession().getCustomerDetail();
        if (c != null) {
            customer = customerDAO.findCustomer(c.getId());
            if (customer != null) {
                oldContact1 = customer.getContactPhone1() != null ? customer.getContactPhone1() : "";
                oldContact2 = customer.getContactPhone2() != null ? customer.getContactPhone2() : "";
                oldContact3 = customer.getContactPhone3() != null ? customer.getContactPhone3() : "";
                oldContact4 = customer.getContactPhone4() != null ? customer.getContactPhone4() : "";
                oldContact5 = customer.getContactPhone5() != null ? customer.getContactPhone5() : "";
                oldContactExt1 = customer.getContactExt1() != null ? customer.getContactExt1() : "";
                oldContactExt2 = customer.getContactExt2() != null ? customer.getContactExt2() : "";
                oldContactExt3 = customer.getContactExt3() != null ? customer.getContactExt3() : "";
                oldContactExt4 = customer.getContactExt4() != null ? customer.getContactExt4() : "";
                oldContactExt5 = customer.getContactExt5() != null ? customer.getContactExt5() : "";
                oldMobile = customer.getMobilePhone() != null ? customer.getMobilePhone() : "";
                oldHome = customer.getHomePhone() != null ? customer.getHomePhone() : "";
                oldHomeExt = customer.getHomeExt() != null ? customer.getHomeExt() : "";
                oldOffice = customer.getOfficePhone() != null ? customer.getOfficePhone() : "";
                oldOfficeExt = customer.getOfficeExt() != null ? customer.getOfficeExt() : "";
                oldFax = customer.getFax() != null ? customer.getFax() : "";
                oldFaxExt = customer.getFaxExt() != null ? customer.getFaxExt() : "";
                oldEmail = customer.getEmail() != null ? customer.getEmail() : "";

                Configuration conf = this.getConfigurationDAO().findConfiguration("ExternalDNC");
                if (conf != null) {
                    if (conf.getValue() != null && conf.getValue().equalsIgnoreCase("true")) {
                        this.initDoNotCall();
                    }
                }
            }
        }
        contactResultList = contactResultDAO.findByContactStatus("newcontact");
        contactStatusList = this.initContactStatusList();
        // CHECK CONTACT HISTORY
//        if (JSFUtil.getUserSession().getContactHistory() != null && JSFUtil.getUserSession().getContactHistory().getId() != null) {
//            contactHistory = JSFUtil.getUserSession().getContactHistory();
//        } else {            
//            System.out.println("create contacthistory");
//            if(JSFUtil.getUserSession().getChannel().equals("inbound")) {
//                createContactHistory();
//            }
//        }
    }

    public void saveListener(ActionEvent event) {
        try {
            if (customer != null) {
                JSFUtil.getUserSession().setCustomerDetail(null);

                if (customer.getHomePhoneReasonId() != null && customer.getHomePhoneReasonId() != 0) {
                    customer.setHomePhoneReason(contactResultDAO.findContactResult(customer.getHomePhoneReasonId()).getName());
                } else {
                    customer.setHomePhoneReason(null);
                }

                if (customer.getOfficePhoneReasonId() != null && customer.getOfficePhoneReasonId() != 0) {
                    customer.setOfficePhoneReason(contactResultDAO.findContactResult(customer.getOfficePhoneReasonId()).getName());
                } else {
                    customer.setOfficePhoneReason(null);
                }

                if (customer.getMobilePhoneReasonId() != null && customer.getMobilePhoneReasonId() != 0) {
                    customer.setMobilePhoneReason(contactResultDAO.findContactResult(customer.getMobilePhoneReasonId()).getName());
                } else {
                    customer.setMobilePhoneReason(null);
                }

                if (customer.getContactPhone1ReasonId() != null && customer.getContactPhone1ReasonId() != 0) {
                    customer.setContactPhone1Reason(contactResultDAO.findContactResult(customer.getContactPhone1ReasonId()).getName());
                } else {
                    customer.setContactPhone1Reason(null);
                }

                if (customer.getContactPhone2ReasonId() != null && customer.getContactPhone2ReasonId() != 0) {
                    customer.setContactPhone2Reason(contactResultDAO.findContactResult(customer.getContactPhone2ReasonId()).getName());
                } else {
                    customer.setContactPhone2Reason(null);
                }

                if (customer.getContactPhone3ReasonId() != null && customer.getContactPhone3ReasonId() != 0) {
                    customer.setContactPhone3Reason(contactResultDAO.findContactResult(customer.getContactPhone3ReasonId()).getName());
                } else {
                    customer.setContactPhone3Reason(null);
                }

                if (customer.getContactPhone4ReasonId() != null && customer.getContactPhone4ReasonId() != 0) {
                    customer.setContactPhone4Reason(contactResultDAO.findContactResult(customer.getContactPhone4ReasonId()).getName());
                } else {
                    customer.setContactPhone4Reason(null);
                }

                if (customer.getContactPhone5ReasonId() != null && customer.getContactPhone5ReasonId() != 0) {
                    customer.setContactPhone5Reason(contactResultDAO.findContactResult(customer.getContactPhone5ReasonId()).getName());
                } else {
                    customer.setContactPhone5Reason(null);
                }

                customerDAO.edit(customer);

                if (!oldContact1.equals(customer.getContactPhone1())) {
                    saveCustomerAuditLog("contact_phone1", oldContact1, customer.getContactPhone1());
                    oldContact1 = customer.getContactPhone1();
                }
                if (!oldContact2.equals(customer.getContactPhone2())) {
                    saveCustomerAuditLog("contact_phone2", oldContact2, customer.getContactPhone2());
                    oldContact2 = customer.getContactPhone2();
                }
                if (!oldContact3.equals(customer.getContactPhone3())) {
                    saveCustomerAuditLog("contact_phone3", oldContact3, customer.getContactPhone3());
                    oldContact3 = customer.getContactPhone3();
                }
                if (!oldContact4.equals(customer.getContactPhone4())) {
                    saveCustomerAuditLog("contact_phone4", oldContact4, customer.getContactPhone4());
                    oldContact4 = customer.getContactPhone4();
                }
                if (!oldContact5.equals(customer.getContactPhone5())) {
                    saveCustomerAuditLog("contact_phone5", oldContact5, customer.getContactPhone5());
                    oldContact5 = customer.getContactPhone5();
                }
                if (!oldContactExt1.equals(customer.getContactExt1())) {
                    saveCustomerAuditLog("contact_ext1", oldContactExt1, customer.getContactExt1());
                    oldContactExt1 = customer.getContactExt1();
                }
                if (!oldContactExt2.equals(customer.getContactExt2())) {
                    saveCustomerAuditLog("contact_ext2", oldContactExt2, customer.getContactExt2());
                    oldContactExt2 = customer.getContactExt2();
                }
                if (!oldContactExt3.equals(customer.getContactExt3())) {
                    saveCustomerAuditLog("contact_ext3", oldContactExt3, customer.getContactExt3());
                    oldContactExt3 = customer.getContactExt3();
                }
                if (!oldContactExt4.equals(customer.getContactExt4())) {
                    saveCustomerAuditLog("contact_ext4", oldContactExt4, customer.getContactExt4());
                    oldContactExt4 = customer.getContactExt4();
                }
                if (!oldContactExt5.equals(customer.getContactExt5())) {
                    saveCustomerAuditLog("contact_ext5", oldContactExt5, customer.getContactExt5());
                    oldContactExt5 = customer.getContactExt5();
                }
                oldMobile = (oldMobile == null ? "" : oldMobile);
                if (!oldMobile.equals(customer.getMobilePhone() == null ? "" : customer.getMobilePhone())) {
                    saveCustomerAuditLog("mobile_phone", oldMobile, customer.getMobilePhone());
                    oldMobile = customer.getMobilePhone();
                }
                oldHome = (oldHome == null ? "" : oldHome);
                if (!oldHome.equals(customer.getHomePhone() == null ? "" : customer.getHomePhone())) {
                    saveCustomerAuditLog("home_phone", oldHome, customer.getHomePhone());
                    oldHome = customer.getHomePhone();
                }
                oldHomeExt = (oldHomeExt == null ? "" : oldHomeExt);
                if (!oldHomeExt.equals(customer.getHomeExt() == null ? "" : customer.getHomeExt())) {
                    saveCustomerAuditLog("home_ext", oldHomeExt, customer.getHomeExt());
                    oldHomeExt = customer.getHomeExt();
                }
                oldOffice = (oldOffice == null ? "" : oldOffice);
                if (!oldOffice.equals(customer.getOfficePhone() == null ? "" : customer.getOfficePhone())) {
                    saveCustomerAuditLog("office_phone", oldOffice, customer.getOfficePhone());
                    oldOffice = customer.getOfficePhone();
                }
                oldOfficeExt = (oldOfficeExt == null ? "" : oldOfficeExt);
                if (!oldOfficeExt.equals(customer.getOfficeExt() == null ? "" : customer.getOfficeExt())) {
                    saveCustomerAuditLog("office_ext", oldOfficeExt, customer.getOfficeExt());
                    oldOfficeExt = customer.getOfficeExt();
                }
                oldFax = (oldFax == null ? "" : oldFax);
                if (!oldFax.equals(customer.getFax() == null ? "" : customer.getFax())) {
                    saveCustomerAuditLog("fax", oldFax, customer.getFax());
                    oldFax = customer.getFax();
                }
                oldFaxExt = (oldFaxExt == null ? "" : oldFaxExt);
                if (!oldFaxExt.equals(customer.getFaxExt() == null ? "" : customer.getFaxExt())) {
                    saveCustomerAuditLog("fax_ext", oldFaxExt, customer.getFaxExt());
                    oldFaxExt = customer.getFaxExt();
                }
                oldEmail = (oldEmail == null ? "" : oldEmail);
                if (!oldEmail.equals(customer.getEmail() == null ? "" : customer.getEmail())) {
                    saveCustomerAuditLog("email", oldEmail, customer.getEmail());
                    oldEmail = customer.getEmail();
                }

                CustomerInfoValue customerInfoValue = customerHandlingDAO.findCustomerHandling(customer.getId());
                JSFUtil.getUserSession().setCustomerDetail(customerInfoValue);
                JSFUtil.getUserSession().setTelephonyEdit("view");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDoNotCall() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if (chkFullName != null) {
            chkFullName.clear();
        }
        if (chkLastName != null) {
            chkLastName.clear();
        }
        if (chkTelPhone != null) {
            chkTelPhone.clear();
        }

        try {
            //conn = dataSourceExternal.getConnection();
            //pstmt = conn.prepareStatement("SELECT FullName, FirstName, LastName, PHONE1, PHONE2, PHONE3, FullNameCheck, LastNameCheck, TelePhoneCheck FROM VIEW_DO_NOT_CALL");
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("SELECT FullName, '' AS FirstName, LastName, PHONE1, PHONE2, PHONE3, [check_fullname] AS FullNameCheck, [check_lastname] AS LastNameCheck, [check_telephone] AS TelePhoneCheck FROM mtl_dnc");
            rs = pstmt.executeQuery();
            //ResultSetMetaData metadata = rs.getMetaData();
            //int columnCount = metadata.getColumnCount();

            while (rs.next()) {
                //Check fullname
                if (rs.getString(7) != null && rs.getString(7).equalsIgnoreCase("01")) {
                    if (rs.getString(1) != null && !rs.getString(1).isEmpty()) {
                        chkFullName.add(rs.getString(1));
                    }
                }
                //Check lastname
                if (rs.getString(8) != null && rs.getString(8).equalsIgnoreCase("01")) {
                    if (rs.getString(3) != null && !rs.getString(3).isEmpty()) {
                        chkLastName.add(rs.getString(3));
                    }
                }
                //Check phone
                if (rs.getString(9) != null && rs.getString(9).equalsIgnoreCase("01")) {
                    if (rs.getString(4) != null && !rs.getString(4).isEmpty()) {
                        chkTelPhone.add(rs.getString(4));
                    }
                    if (rs.getString(5) != null && !rs.getString(5).isEmpty()) {
                        chkTelPhone.add(rs.getString(5));
                    }
                    if (rs.getString(6) != null && !rs.getString(6).isEmpty()) {
                        chkTelPhone.add(rs.getString(6));
                    }
                }
            }

            //from view mib
//            pstmt = conn.prepareStatement("SELECT * FROM vw_mib");
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//                //Check fullname
//                if (rs.getString(4) != null && rs.getString(4).equalsIgnoreCase("01")) {
//                    if (rs.getString(1) != null && !rs.getString(1).isEmpty()) {
//                        chkFullName.add(rs.getString(1));
//                    }
//                }
//                //Check lastname
//                if (rs.getString(5) != null && rs.getString(5).equalsIgnoreCase("01")) {
//                    if (rs.getString(3) != null && !rs.getString(3).isEmpty()) {
//                        chkLastName.add(rs.getString(3));
//                    }
//                }
//            }
            if (customer != null) {
                //chk fullname
                for (String fn : chkFullName) {
                    if (!doNotCall) {
                        if (fn.equals(customer.getName() + " " + customer.getSurname())) {
                            doNotCall = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }

                //chk lastname
                for (String ln : chkLastName) {
                    if (!doNotCall) {
                        if (ln.equalsIgnoreCase(customer.getSurname())) {
                            doNotCall = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }

                //chk phone
                for (String p : chkTelPhone) {
                    if (!doNotCall) {
                        if ((customer.getHomePhone() != null && !customer.getHomePhone().isEmpty() && customer.getHomePhone().equalsIgnoreCase(p))
                                || (customer.getOfficePhone() != null && !customer.getOfficePhone().isEmpty() && customer.getOfficePhone().equalsIgnoreCase(p))
                                || (customer.getMobilePhone() != null && !customer.getMobilePhone().isEmpty() && customer.getMobilePhone().equalsIgnoreCase(p))) {
                            doNotCall = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (doNotCall) {
                    //Stamp op-out = 1 to customer
                    customer.setOpOut(Boolean.TRUE);
                    customer.setOpOutDate(new Date());
                    customerDAO.edit(customer);
                }
            }

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SaleApproachingController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(SaleApproachingController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void saveCustomerAuditLog(String fieldName, String oldNo, String newNo) {
        try {
            CustomerAuditLog aLog = new CustomerAuditLog();
            aLog.setCustomerId(customer.getId());
            aLog.setFieldName(fieldName);
            aLog.setOldValue(oldNo);
            aLog.setNewValue(newNo);
            aLog.setCreateDate(new Date());
            aLog.setCreateBy(JSFUtil.getUserSession().getUserName());
            customerAuditLogDAO.create(aLog);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public List<String[]> initContactStatusList() {
        List<String[]> list = new ArrayList<String[]>();
        String[] a = new String[2];
        a[0] = "DMC";
        a[1] = "dmc";
        list.add(a);
        String[] b = new String[2];
        b[0] = "Not DMC";
        b[1] = "contactable";
        list.add(b);
        String[] c = new String[2];
        c[0] = "Unreachable";
        c[1] = "uncontactable";
        list.add(c);
        String[] d = new String[2];
        d[0] = "CS";
        d[1] = "cs";
        list.add(d);
        //Map<String, String> values = new ConcurrentSkipListMap<String, String>();

        //values.put("DMC", "dmc");
        //values.put("Not DMC", "contactable");
        //values.put("Unreachable", "uncontactable");
        //values.put("CS", "cs");
        /*
         if(JSFUtil.getUserSession().isDialShow()){
         if(JSFUtil.getUserSession().getChannel() == null){
         values.put("DMC", "dmc");
         values.put("Not DMC", "contactable");
         values.put("Unreachable", "uncontactable");
         values.put("CS", "cs");
         }else{
         if (JSFUtil.getUserSession().getChannel().equals("outbound")
         && JSFUtil.getUserSession().isDialStatusActive()) {
         values.put("DMC", "dmc");
         values.put("Not DMC", "contactable");
         }
         if ((JSFUtil.getUserSession().getChannel().equals("outbound")
         || JSFUtil.getUserSession().getChannel().equals("inbound"))
         && !JSFUtil.getUserSession().isDialStatusActive()) {
         values.put("Unreachable", "uncontactable");
         }
         if(JSFUtil.getUserSession().getChannel().equals("inbound")
         && JSFUtil.getUserSession().isDialStatusActive()) {
         values.put("CS", "cs");
         values.put("Telesale", "ts");
         }

         }
         }else{
         values.put("DMC", "dmc");
         values.put("Not DMC", "contactable");
         values.put("Unreachable", "uncontactable");
         values.put("CS", "cs");
         }
         */
        //if (!SecurityUtil.isPermitted("telephonybar:view")) {
        //    values.put("Telesale", "ts");
        //}
        return list;
    }

    public void resetSelect(){
        contactStatus = "";
        contactResultId = null;
        mainContactResultId = null;
        mainContactResultCode = null;
        mainContactResultList = null;
    }

    public void contactStatusListener() {

        contactResultId = null;
        mainContactResultId = null;
        mainContactResultCode = null;
        mainContactResultList =null;

        contactStatusList = this.initContactStatusList();
        UserSession u = JSFUtil.getUserSession();

        String status = null;
        if (contactStatus.equals("dmc")) {
            status = "dmc";
        } else if (contactStatus.equals("contactable")) {
            status = "contactable";
        } else if (contactStatus.equals("cs")) {
            status = "inbound";
        } else if (contactStatus.equals("ts")) {
            status = "inboundtelesale";
        } else if (contactStatus.equals("uncontactable")) {
            status = "uncontactable";
        }

//        String stationNo = JSFUtil.getUserSession().getStationNo();
//        String telephonyName;
//        if(JSFUtil.getUserSession().getUsers().getTelephonySso()) {
//            telephonyName = JSFUtil.getUserSession().getUsers().getLdapLogin();
//        } else {
//            telephonyName = JSFUtil.getUserSession().getUsers().getTelephonyLoginName();
//        }
//
//        String state = "";
//        if(stationNo != null && !stationNo.equals("")) {
//            try {
//                JSONObject stateInfo = telephonyService.getCurrentstate(telephonyName, stationNo);
//                if (stateInfo != null) {
//                    state = stateInfo.getString("CurrentState");
//                }
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//        }

        if (status != null) {
            String checkCall = "old";
//            if (state.equalsIgnoreCase("active")) {
                if(!contactStatus.equals("cs")) {
                    if (u.getPurchaseOrders().size() != 0 && u.getContactResultPlanId() != null) {
                        // click product and new order
                        for (int i = 0; i < u.getPurchaseOrders().size(); i++) {
                            if (u.getPurchaseOrders().get(i).getId().equals(u.getPurchaseOrderId())) {
                                checkCall = "new";
                            }
                        }
                        if (checkCall.equals("new")) {
                            //click product in calling and there is a new order (new product)
                            if (u.getContactResultPlanId() != null && u.getPurchaseOrders().size() != 0) {
                                if (checkYesSaleInCall()) {
                                    Integer contactResultPlanId = purchaseOrderDAO.findContactResultPlanByPurchaseOrderIdAndCheckYesSale(purchaseOrderId, true);
                                    mainContactResultList = contactResultDAO.findByContactStatusAndYesSale(status, contactResultPlanId, true);
                                } else {
                                    Integer contactResultPlanId = purchaseOrderDAO.findContactResultPlanByPurchaseOrderIdAndCheckYesSale(purchaseOrderId, false);
                                    mainContactResultList = contactResultDAO.findByContactStatusAndYesSale(status, contactResultPlanId, false);
                                }
                            } else {
                                mainContactResultList = contactResultDAO.findByContactStatus(status);
                            }
                        } else { // checkcall = old
                            //click (old product) in calling and there is a new order
                            if (checkYesSaleByPurchaseOrderId()) {
                                mainContactResultList = contactResultDAO.findByContactStatusAndYesSale(status, u.getContactResultPlanId(), true);
                            } else {
                                mainContactResultList = contactResultDAO.findByContactStatusLinkProduct(status, u.getContactResultPlanId(), false);
                            }
                        }
                    } else if (u.getPurchaseOrders().size() != 0) {
                        //no click product but there is a new order.
                        if (checkYesSaleInCall()) {
                            //Yes sale and No click product
                            Integer contactResultPlanId = purchaseOrderDAO.findContactResultPlanByPurchaseOrderIdAndCheckYesSale(purchaseOrderId, true);
                            mainContactResultList = contactResultDAO.findByContactStatusAndYesSale(status, contactResultPlanId, true);
                        } else {
                            //No sale and No click product
                            Integer contactResultPlanId = purchaseOrderDAO.findContactResultPlanByPurchaseOrderIdAndCheckYesSale(purchaseOrderId, false);
                            mainContactResultList = contactResultDAO.findByContactStatusAndYesSale(status, contactResultPlanId, false);
                        }
                    } else if (u.getPurchaseOrders().size() == 0 && u.getContactResultPlanId() != null) {
                        //click product and there is not a new order
                        if (checkYesSaleInCallByCustomer()) {
                            mainContactResultList = contactResultDAO.findByContactStatusAndYesSale(status, u.getContactResultPlanId(), true);
                        } else {
                            mainContactResultList = contactResultDAO.findByContactStatusLinkProduct(status, u.getContactResultPlanId(), false);
                        }

                    } else {
                        //no click product and there is not new order (show defult)
                        if (checkYesSaleNoClickProductAndNoNewOrderInCallByCustomer() != 0) {
                            int contactResultPlanId = checkYesSaleNoClickProductAndNoNewOrderInCallByCustomer();
                            mainContactResultList = contactResultDAO.findByContactStatusAndYesSale(status, contactResultPlanId, true);
                        } else {
                            mainContactResultList = contactResultDAO.findByContactStatus(status);
                        }
                    }
                }else {
                    mainContactResultList = contactResultDAO.findByContactStatus(status);
                }
//            } else {
//                //old product
//                if (u.getContactResultPlanId() != null && !contactStatus.equals("cs")) {
//                    int contactResultPlan = u.getContactResultPlanId();
//                    mainContactResultList = contactResultDAO.findByContactStatusLinkProduct(status, contactResultPlan, true);
//                } else {
//                    mainContactResultList = contactResultDAO.findByContactStatus(status);
//                }
//            }
        }
    }

    public boolean checkYesSaleByPurchaseOrderId() {
        UserSession u = JSFUtil.getUserSession();
        List<Integer> poID = new ArrayList<Integer>();
        poID.add(u.getPurchaseOrderId());
        Integer count = purchaseOrderDAO.findPurchaseOrderIsYesSale(poID);
        //System.out.println("Count " + count);
        if (count != 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean checkYesSaleInCall() {
        for (int i = 0; i < JSFUtil.getUserSession().getPurchaseOrders().size(); i++) {
            purchaseOrderId.add(JSFUtil.getUserSession().getPurchaseOrders().get(i).getId());
        }
        Integer count = purchaseOrderDAO.findPurchaseOrderIsYesSale(purchaseOrderId);
        //System.out.println("Count " + count);
        if (count != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkYesSaleInCallByCustomer() {
        int customerId = JSFUtil.getUserSession().getCustomerDetail().getId();
        Integer count = purchaseOrderDAO.findPurchaseOrderIsYesSaleByCustomerId(customerId);
        if (count != 0) {
            return true;
        } else {
            return false;
        }
    }

    public int checkYesSaleNoClickProductAndNoNewOrderInCallByCustomer() {
        int customerId = JSFUtil.getUserSession().getCustomerDetail().getId();
        Integer id = purchaseOrderDAO.findTopContactResultPlanInPurchaseOrderIsYesSaleByCustomerIdDESC(customerId);
        return id;
    }

    public void changeContactResultListener(ValueChangeEvent event) {
        if (event != null && event.getNewValue() != null) {
            contactResultId = (Integer) event.getNewValue();
            mainContactResultId = contactResultId;
            ContactResult contactResult = contactResultDAO.findContactResult(contactResultId);
            mainContactResultCode = contactResult.getCode();
            JSFUtil.getUserSession().setContactCaseId(mainContactResultId);
            JSFUtil.getUserSession().setContactResultCode(mainContactResultCode);
        }

//        if(event != null && event.getNewValue() != null){
//            String str = (String) event.getNewValue();
//            String[] stra = str.split(":");
//            mainContactResultId = Integer.parseInt(stra[0]);
//            mainContactResultCode = stra[1];
//        }else{
//            mainContactResultId = null;
//            mainContactResultCode = null;
//        }
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

    private Integer getTalkTime() {
        Integer tTime = 0;
        try {
            String urlDial = JSFUtil.getApplication().getAsteriskWsUrl() + "/PreviewDialServlet?ACTION=getduration&ACCOUNTID=" + contactHistory.getTelephonyTrackId();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(this.getXML(urlDial)));
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("RESPONSE");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println(eElement.getElementsByTagName("DURATION").item(0).getTextContent());
                    if (eElement.getElementsByTagName("DURATION").item(0).getTextContent() != null
                            && !eElement.getElementsByTagName("DURATION").item(0).getTextContent().isEmpty()) {
                        tTime = Integer.parseInt(StringUtils.trim(eElement.getElementsByTagName("DURATION").item(0).getTextContent()));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tTime;
    }

    public void getCallInfo(String telephonyName, String stationNo) {
        try {
//            CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
//            if(stationNo != null && !stationNo.equals("") && ctiAdapter != null && ctiAdapter.getCtiVendorCode()!= null && ctiAdapter.getCtiVendorCode().equals("aspect")) { 
            // CALL ASPECT TO GET TALK TIME AND TRACK ID
                JSONObject callInfo = telephonyService.getCallInformationData(telephonyName, stationNo);
                if(callInfo != null) {
                    String msgCallInfo = callInfo.getString("Message");
                    JSONObject resultCallInfo = new JSONObject(callInfo.getString("Result"));

                    String startCallTime = resultCallInfo.getString("StartCallTime");
                    String endCallTime = resultCallInfo.getString("EndCallTime");
                    String totalCallTime = resultCallInfo.getString("TotalCallTime");
                    String trackId = resultCallInfo.getString("TrackId");

                    if(msgCallInfo.equals("null") || msgCallInfo.equals("")) {
                        ctiLog.info(new Date().toString()+" GETCALLINFORMATION Extension: "+stationNo+" startCallTime: "+startCallTime+" - endCallTime: "+endCallTime+" - totalCallTime: "+totalCallTime+" - trackId: "+trackId);
                    JSFUtil.getUserSession().setTelephonyTrackId(trackId);
                    Double convTalkTime = Double.parseDouble(totalCallTime);
                    JSFUtil.getUserSession().setTalkTime(convTalkTime.intValue());
                } else {
                        ctiLog.info(new Date().toString()+" Extension: "+stationNo+" ERROR GETCALLINFORMATION: "+msgCallInfo);
                }
            }
//            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void nextCallListener(ActionEvent event) {
        String stationNo = JSFUtil.getUserSession().getStationNo();
        String telephonyName;
        if(JSFUtil.getUserSession().getUsers().getTelephonySso()) {
            telephonyName = JSFUtil.getUserSession().getUsers().getLdapLogin();
        } else {
            telephonyName = JSFUtil.getUserSession().getUsers().getTelephonyLoginName();
        }

        JSFUtil.getUserSession().setShowContactSummary(true);
        try {
            CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();
            String state = "";
            if(stationNo != null && !stationNo.equals("") && ctiAdapter != null && ctiAdapter.getCtiVendorCode()!= null && ctiAdapter.getCtiVendorCode().equals("aspect")) { 
                JSONObject stateInfo = telephonyService.getCurrentstate(telephonyName, stationNo);
                if(stateInfo != null) {
                    String msg = stateInfo.getString("Message");
                    state = stateInfo.getString("CurrentState");
                    if(msg.equals("null") || msg.equals("")) {                       
                        ctiLog.info(new Date().toString()+" Extension: "+stationNo+" GET Currentstate BEFORE NEXT CALL Result: "+state);
                    } else {
                        ctiLog.info(new Date().toString()+" Extension: "+stationNo+" ERROR getCurrentstate BEFORE NEXT CALL: "+msg);
                    }
                }

                // 1. CALL CTI SERVER GET INFORMATION CALL
                if(state.toUpperCase().equals("WRAP")) {
                    this.getCallInfo(telephonyName, stationNo);
                }
            }

            // 2. SAVE TARCK ID AND TALK TIME IN CONTACT HISTORY
            this.saveContactHistory();

            // 3. SAVE ASSIGNMENT DETAIL
            this.saveAssignmentDetail();

            // CALL CTI ASPECT FUNCTION
            if(stationNo != null && !stationNo.equals("") && ctiAdapter != null && ctiAdapter.getCtiVendorCode() != null && ctiAdapter.getCtiVendorCode().equals("aspect")) { 
                try {
                    if(!state.toUpperCase().equals("IDLE") && !state.toUpperCase().equals("PARKED")) {
                        // 4. CALL PARK FOR ASPECT CTI
                        String resultPark  = telephonyService.notreadypark(telephonyName, stationNo);
                        ctiLog.info(new Date().toString()+" Extension: "+stationNo+" NOTREADYPARK Extension: "+stationNo+", Result: "+resultPark);

                        // 5. CALL CTI SERVER SAVE 20 PARAMETER
                        if(ctiAdapter.getRecorderVendorCode() != null && ctiAdapter.getRecorderVendorCode().equals("aspect")) {
                            UserSession userSession = JSFUtil.getUserSession();
                            StringBuilder paramData = new StringBuilder();
                            String jsonParam = ctiAdapter.getRecorderParam();
                            Gson gson = new Gson();

                            if(jsonParam != null && !jsonParam.equals("")) {
                                String convertToList = jsonParam.replace("{\"params-mapping\":", "");
                                convertToList = convertToList.substring(0, convertToList.length() - 1);
                                List<ParameterMappingValue> parameterMapList = gson.fromJson(convertToList, new TypeToken<List<ParameterMappingValue>>(){}.getType());
                                if(parameterMapList != null && parameterMapList.size() > 0) {

                                    for(ParameterMappingValue param : parameterMapList) {
                                        String value = "";
                                        if(!param.getValue().startsWith("#")) {
                                            value = param.getValue();
                                        } else if(param.getValue().equals("#tmrname")) {
                                            value = userSession.getUserName();
                                        } else if(param.getValue().equals("#customerid")) {
                                            value = userSession.getCustomerDetail().getId().toString();
                                        } else if(param.getValue().equals("#customername")) {
                                            value = userSession.getCustomerDetail().getName() + " " + userSession.getCustomerDetail().getSurname();
                                        } else if(param.getValue().equals("#customerfirstname")) {
                                            value = userSession.getCustomerDetail().getName();
                                        } else if(param.getValue().equals("#customerlastname")) {
                                            value = userSession.getCustomerDetail().getSurname();
                                        } else if(param.getValue().equals("#poid1")) {
                                            value = userSession.getPurchaseOrderId1();
                                        } else if(param.getValue().equals("#poid2")) {
                                            value = userSession.getPurchaseOrderId2();
                                        } else if(param.getValue().equals("#poid3")) {
                                            value = userSession.getPurchaseOrderId3();
                                        } else if(param.getValue().equals("#poid4")) {
                                            value = userSession.getPurchaseOrderId4();
                                        } else if(param.getValue().equals("#poid5")) {
                                            value = userSession.getPurchaseOrderId5();
                                        } else if(param.getValue().equals("#refno1_po1")) {
                                            value = userSession.getPurchaseOrder1RefNo1();
                                        } else if(param.getValue().equals("#refno1_po2")) {
                                            value = userSession.getPurchaseOrder2RefNo1();
                                        } else if(param.getValue().equals("#refno1_po3")) {
                                            value = userSession.getPurchaseOrder3RefNo1();
                                        } else if(param.getValue().equals("#refno1_po4")) {
                                            value = userSession.getPurchaseOrder4RefNo1();
                                        } else if(param.getValue().equals("#refno1_po5")) {
                                            value = userSession.getPurchaseOrder5RefNo1();
                                        } else if(param.getValue().equals("#refno2_po1")) {
                                            value = userSession.getPurchaseOrder1RefNo2();
                                        } else if(param.getValue().equals("#refno2_po2")) {
                                            value = userSession.getPurchaseOrder2RefNo2();
                                        } else if(param.getValue().equals("#refno2_po3")) {
                                            value = userSession.getPurchaseOrder3RefNo2();
                                        } else if(param.getValue().equals("#refno2_po4")) {
                                            value = userSession.getPurchaseOrder4RefNo2();
                                        } else if(param.getValue().equals("#refno2_po5")) {
                                            value = userSession.getPurchaseOrder5RefNo2();
                                        } else if(param.getValue().equals("#saleresult1")) {
                                            value = userSession.getPurchaseOrderSaleResult1();
                                        } else if(param.getValue().equals("#saleresult2")) {
                                            value = userSession.getPurchaseOrderSaleResult2();
                                        } else if(param.getValue().equals("#saleresult3")) {
                                            value = userSession.getPurchaseOrderSaleResult3();
                                        } else if(param.getValue().equals("#saleresult4")) {
                                            value = userSession.getPurchaseOrderSaleResult4();
                                        } else if(param.getValue().equals("#saleresult5")) {
                                            value = userSession.getPurchaseOrderSaleResult5();
                                        } else if(param.getValue().equals("#casecode1")) {
                                            value = userSession.getCaseCode1();
                                        } else if(param.getValue().equals("#casecode2")) {
                                            value = userSession.getCaseCode2();
                                        } else if(param.getValue().equals("#casecode3")) {
                                            value = userSession.getCaseCode3();
                                        } else if(param.getValue().equals("#casecode4")) {
                                            value = userSession.getCaseCode4();
                                        } else if(param.getValue().equals("#casecode5")) {
                                            value = userSession.getCaseCode5();
                                        }
                                        paramData.append("\"");
                                        paramData.append(param.getName());
                                        paramData.append("\":\"");
                                        paramData.append(value);
                                        paramData.append("\",");
                                    }
                                }
                            }
                            String paramInfo = paramData.toString();
                            paramInfo = paramInfo.substring(0, paramInfo.length() - 1);
                            String resultSaveParam = telephonyService.saveAspectParameter(telephonyName, stationNo, paramInfo);
                            ctiLog.info(new Date().toString()+" SAVEASPECTPARAMETER Extension: "+stationNo+", Param: "+paramInfo+", Result: "+resultSaveParam);
                        }

                        // 6. CALL CTI SERVER NEXT CALL 
                        String resultNextCall = "";
                        if(contactHistory != null && contactHistory.getContactResult() != null) {
                            resultNextCall = telephonyService.nextcall(telephonyName, stationNo, contactHistory.getContactResult().getCode());
                        } else {
                            resultNextCall = telephonyService.nextcall(telephonyName, stationNo, "JKC");
                        }
                        ctiLog.info(new Date().toString()+" NEXTCALL Extension: "+stationNo+", Code: "+contactHistory.getContactResult().getCode()+", Result: "+resultNextCall);

                    }
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        clearValue();

    }

//    private void showCurrentState(String telephonyName, String stationNo) {
//        try {
//            JSONObject stateInfo = telephonyService.getCurrentstate(telephonyName, stationNo);   
//            if(stateInfo != null) {
//                String msg = stateInfo.getString("Message"); 
//                String state = stateInfo.getString("CurrentState"); 
//
//                if(msg.equals("null") || msg.equals("")) {
//                    System.out.println("CurrentState: "+state);
//                } else {
//                    System.out.println("msg showCurrentState "+msg);
//                }       
//            }
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }        
//    }
    
    private void clearValue() {
        JSFUtil.getUserSession().setDialNo("");
        JSFUtil.getUserSession().setDialExt("");
        JSFUtil.getUserSession().setDialToString("");
        JSFUtil.getUserSession().setDialShow(false);
        JSFUtil.getUserSession().setContactTo(null);
        JSFUtil.getUserSession().setTelephonyTrackId(null);
        JSFUtil.getUserSession().setTalkTime(null);
        if (JSFUtil.getUserSession().getContactHistory() != null) {
            JSFUtil.getUserSession().getContactHistory().setTelephonyTrackId(null);
            JSFUtil.getUserSession().getContactHistory().setTalkTime(null);
            JSFUtil.getUserSession().getContactHistory().setContactTo(null);
        }
        JSFUtil.getUserSession().setDialStatus(null);
        JSFUtil.getUserSession().setDialStatusActive(false);
        JSFUtil.getUserSession().setMode("hide");
        JSFUtil.getUserSession().setTelephonyEdit("view");
        JSFUtil.getUserSession().setChannel(null);
        JSFUtil.getUserSession().setClickNextCall(Boolean.TRUE);
        JSFUtil.getUserSession().setDialClicked(Boolean.FALSE);
        JSFUtil.getUserSession().setDialShow(true);
        JSFUtil.getUserSession().setHangupShow(false);
        JSFUtil.getUserSession().setNextcallShow(false);
        JSFUtil.getUserSession().setContactResultId(null);
        JSFUtil.getUserSession().setContactResultCode(null);
        contactResultId = null;
        contactStatus = null;
        mainContactResultId = null;
        mainContactResultList = null;
    }

    private void saveAssignmentDetail() {

        if (contactStatus != null && !contactStatus.equalsIgnoreCase("cs")) {
            AssignmentDetail a = JSFUtil.getUserSession().getAssignmentDetail();
            if (a != null) {
                String b = JSFUtil.getUserSession().getParam1();
                boolean c = JSFUtil.getUserSession().isInboundCall();
                boolean d = JSFUtil.getUserSession().isOutboundCall();
                if (JSFUtil.getUserSession().isInboundCall()
                        && JSFUtil.getUserSession().getUserGroup().getRole().contains("Agent")
                        && JSFUtil.getUserSession().getAssignmentDetail() == null) {
                    initAssignmentDetail();
                } else if (JSFUtil.getUserSession().isOutboundCall()
                        && (JSFUtil.getUserSession().getParam1() != null && !JSFUtil.getUserSession().getParam1().isEmpty())
                        && JSFUtil.getUserSession().getUserGroup().getRole().contains("Agent")
                        && JSFUtil.getUserSession().getAssignmentDetail() == null) {
                    initAssignmentDetail();
                }

                if (JSFUtil.getUserSession().getAssignmentDetail() != null && JSFUtil.getUserSession().getAssignmentDetail().getId() != null) {
                    AssignmentDetail ad = JSFUtil.getUserSession().getAssignmentDetail();
                    if (ad.getContactStatus() == null) {
                        ad.setContactStatus(contactHistory.getContactResult().getContactStatus());
                    } else {
                        if (!ad.getContactStatus().equalsIgnoreCase("dmc")) {
                            if (ad.getContactStatus().equalsIgnoreCase("uncontactable")) {
                                if (contactHistory.getContactResult().getContactStatus().equalsIgnoreCase("contactable")
                                        || contactHistory.getContactResult().getContactStatus().equalsIgnoreCase("dmcnotoffer")
                                        || contactHistory.getContactResult().getContactStatus().equalsIgnoreCase("dmc")) {
                                    ad.setContactStatus(contactHistory.getContactResult().getContactStatus());
                                }
                            } else if (ad.getContactStatus().equalsIgnoreCase("contactable")) {
                                if (contactHistory.getContactResult().getContactStatus().equalsIgnoreCase("dmcnotoffer")
                                        || contactHistory.getContactResult().getContactStatus().equalsIgnoreCase("dmc")) {
                                    ad.setContactStatus(contactHistory.getContactResult().getContactStatus());
                                }
                            } else if (ad.getContactStatus().equalsIgnoreCase("dmcnotoffer")) {
                                if (contactHistory.getContactResult().getContactStatus().equalsIgnoreCase("dmc")) {
                                    ad.setContactStatus(contactHistory.getContactResult().getContactStatus());
                                }
                            }
                        }
                    }

                    if (contactHistory != null || contactHistory.getContactResult() != null) {
                        ad.setLatestContactStatus(contactHistory.getContactResult().getContactStatus());
                        ad.setContactResult(contactHistory.getContactResult().getName());
                        ad.setContactResultId(contactHistory.getContactResult());
                    }

                    if ((contactHistory.getContactResult().getContactStatus().equalsIgnoreCase(JSFUtil.getBundleValue("dmcValue"))
                            || contactHistory.getContactResult().getContactStatus().equalsIgnoreCase(JSFUtil.getBundleValue("dmcNotOfferValue")))) {
                        ad.setStatus("followup");
                        ad.setContactResult(contactHistory.getContactResult().getName());
                        ad.setContactDate(new Date());
                        ad.setUpdateDate(new Date());
                    }
                    try {
                        assignmentDetailDAO.saveContactStatus(ad);
                    } catch (Exception e) {
                        log.error(e);
                    }

                    try {
                        //STEP 1 CHECK CONFIG AUTOCLOSE
                        Boolean autoClose = JSFUtil.getApplication().isAutoCloseAssignmentDetail();
                        boolean doAutoClose = false;
                        if (contactHistory.getContactResult() != null) {
                            ContactResult contactResult = contactHistory.getContactResult();
                            if (contactResult.getContactStatus().equalsIgnoreCase("dmc") || contactResult.getContactStatus().equalsIgnoreCase("dmcnotoffer")) {
                                if (!ad.isDmc()) {
                                    ad.setDmc(true);
                                    assignmentDetailDAO.editDmc(ad, true);
                                }
                            }

                            //STEP 2 CHECK CONTACT RESULT IS AUTOCLOSE
                            if (autoClose && contactResult.getCloseAssignmentDetail() != null && contactResult.getCloseAssignmentDetail()) {
                                this.assignmentDetailDAO.editStatus(ad, "closed");
                                doAutoClose = true;
                            } else if (autoClose && !contactResult.getCloseAssignmentDetail() && a.getStatus().equals("closed")) {
                                this.assignmentDetailDAO.editStatus(ad, "followup");
                            }
                        }

                        //STEP 3 IF NOT DO CONFIG AUTOCLOSE CHECK MAXCALL AUTOCLOSE                        
                        if (!doAutoClose) {
                            Campaign campaign = ad.getAssignmentId().getCampaign();
                            ContactResult maxCallContactResult = null;
                            if (ad.isDmc()) {
                                if (ad.getCallAttempt2() != null && ad.getMaxCall2() != null && ad.getCallAttempt2().intValue() == ad.getMaxCall2().intValue()) {
                                    this.assignmentDetailDAO.editStatus(ad, "closed");
                                    maxCallContactResult = campaign.getMaxCall2ContactResult();
                                }
                            } else {
                                if (ad.getCallAttempt() != null && ad.getMaxCall() != null && ad.getCallAttempt().intValue() == ad.getMaxCall().intValue()) {
                                    this.assignmentDetailDAO.editStatus(ad, "closed");
                                    maxCallContactResult = campaign.getMaxCallContactResult();
                                }
                            }

                            if (maxCallContactResult != null) {
                                ad.setContactResultId(maxCallContactResult);
                                ad.setContactResult(maxCallContactResult.getName());
                                contactHistory.setContactResult(maxCallContactResult);

                                // SAVE ASSIGNMENT DEATIL AND CONTACT HISTORY
                                this.assignmentDetailDAO.saveMaxCallAutoClose(ad);
                                contactHistoryDAO.updateMaxCallContact(contactHistory);

                                JSFUtil.getUserSession().setContactHistory(contactHistory);
                            }
                        }

                        JSFUtil.getUserSession().statusChangeAction();

                    } catch (Exception e) {
                        log.error(e);
                    }

                    // KEEP UPDATED ASSIGNMENT DETAIL INTO SESSION
                    if (ad != null && ad.getId() != null) {
                        ad = assignmentDetailDAO.findAssignmentDetail(ad.getId());
                    }
                    JSFUtil.getUserSession().setAssignmentDetail(ad);

                }

            }
        }

    }

    public void saveContactHistory() throws Exception {
        //this.initAssignmentDetail();
        this.initContactHistory();
    }

    private void initContactHistory() {
        if (JSFUtil.getUserSession().getContactHistory() == null) {
            if (JSFUtil.getUserSession().getTelephonyTrackId() != null
                    && !JSFUtil.getUserSession().getTelephonyTrackId().equals("")
                    && JSFUtil.getUserSession().getCustomerDetail() != null
                    && JSFUtil.getUserSession().getCustomerDetail().getId() != null) {
                contactHistory = contactHistoryDAO.findByCustomerTrackId(JSFUtil.getUserSession().getCustomerDetail().getId(), JSFUtil.getUserSession().getTelephonyTrackId());
            } else {
                this.createContactHistory();
            }
        } else {
            contactHistory = JSFUtil.getUserSession().getContactHistory();
            if(contactHistory.getId() == null) {
                this.createContactHistory();
            }
        }

        try {
            if (contactHistory.getCustomer() == null && JSFUtil.getUserSession().getCustomerDetail() != null) {
                contactHistory.setCustomer(customerDAO.findCustomer(JSFUtil.getUserSession().getCustomerDetail().getId()));
            }
            if (contactHistory.getContactTo() == null || contactHistory.getContactTo().equals("")) {
                if (JSFUtil.getUserSession().getContactTo() != null && !JSFUtil.getUserSession().getContactTo().equals("")) {
                    contactHistory.setContactTo(JSFUtil.getUserSession().getContactTo());
                }
            }

            if(contactHistory.getStationNo() == null || contactHistory.getStationNo().equals("")){
                contactHistory.setStationNo(JSFUtil.getUserSession().getStationNo());
            }

            //CHECK TRACK ID
            if (contactHistory.getTelephonyTrackId() == null || contactHistory.getTelephonyTrackId().equals("")) {
                if (JSFUtil.getUserSession().getTelephonyTrackId() != null && !JSFUtil.getUserSession().getTelephonyTrackId().equals("")) {
                    contactHistory.setTelephonyTrackId(JSFUtil.getUserSession().getTelephonyTrackId());
                }
            }

            // CHECK TALK TIME
            if(JSFUtil.getUserSession().getTalkTime() != null && JSFUtil.getUserSession().getTalkTime() != 0){
                contactHistory.setTalkTime(JSFUtil.getUserSession().getTalkTime());
            }

            // CHECK CONTACT RESULT
            if (mainContactResultId == null || mainContactResultId == 0) {
                if (JSFUtil.getUserSession() != null && JSFUtil.getUserSession().getContactResultId() != null && JSFUtil.getUserSession().getContactResultId() != 0) {
                    mainContactResultId = JSFUtil.getUserSession().getContactResultId();
                }
            }
            if (mainContactResultId != null && mainContactResultId != 0) {
                contactHistory.setContactResult(contactResultDAO.findContactResult(mainContactResultId));
            }
            contactHistoryDAO.edit(contactHistory);

            JSFUtil.getUserSession().setContactHistory(contactHistory);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createContactHistory() {
        contactHistory = new ContactHistory();
        contactHistory.setContactDate(new Date());
        if (JSFUtil.getUserSession().isInboundCall()) {
            if (JSFUtil.getUserSession().getUserGroup().getRole().contains("CSCounter")) {
                contactHistory.setChannel(new Channel(2));//inbound:phonecall
            } else {
                contactHistory.setChannel(new Channel(6)); //inbound:Walk-in
            }
        } else {
            contactHistory.setChannel(new Channel(9)); //outbound:phonecall
        }
        contactHistory.setContactStatus("c");//close
        contactHistory.setCallSuccess(true);
        contactHistory.setDmccontact(true);
        contactHistory.setFollowupsale(false);
        contactHistory.setContactClose(false);
        contactHistory.setCreateDate(new Date());
        contactHistory.setUsers(JSFUtil.getUserSession().getUsers());
        contactHistory.setCreateBy(JSFUtil.getUserSession().getUserName());
        if (JSFUtil.getUserSession().getCustomerDetail() != null) {
            contactHistory.setCustomer(customerDAO.findCustomer(JSFUtil.getUserSession().getCustomerDetail().getId()));
        }
        contactHistory.setContactTo(JSFUtil.getUserSession().getContactTo());
        contactHistory.setTelephonyTrackId(JSFUtil.getUserSession().getTelephonyTrackId());
        contactHistory.setTalkTime(JSFUtil.getUserSession().getTalkTime());
        if(contactHistory.getStationNo() == null || contactHistory.getStationNo().equals("")){
            contactHistory.setStationNo(JSFUtil.getUserSession().getStationNo());
        }
        contactHistoryDAO.create(contactHistory);

        JSFUtil.getUserSession().setContactHistory(contactHistory);

    }

    private void initAssignmentDetail() {
        try {

            AssignmentDetail ad = null;
            if (JSFUtil.getUserSession().getAssignmentDetail() != null) {
                ad = JSFUtil.getUserSession().getAssignmentDetail();
            } else {
                ad = new AssignmentDetail();
                ad.setUsers(JSFUtil.getUserSession().getUsers());
                ad.setAssignmentId(null);
                ad.setCustomerId(new Customer(JSFUtil.getUserSession().getCustomerDetail().getId()));
                ad.setCustomerName(JSFUtil.getUserSession().getCustomerDetail().getName() + ' ' + JSFUtil.getUserSession().getCustomerDetail().getSurname());
                ad.setStatus("followup");
                ad.setAssignDate(new Date());
                JSFUtil.getUserSession().setAssignmentDetail(ad);
            }

            Campaign cp = null;
            if (ad.getId() == null) {
                if (ad.getAssignmentId() == null) {
                    List<Campaign> campaignList = campaignDAO.findCampaignInbound();
                    if (campaignList != null && !campaignList.isEmpty()) {
                        cp = campaignList.get(0);
                        if (cp != null && cp.getInbound()) {
                            ad.setMarketingCode(cp.getMarketingInbound().getCode());
                            ad.setCampaignName(cp.getName());
                            ad.setAssignmentId(cp.getAssignmentInbound());
                            ad.setMaxCall(cp.getMaxCall());
                            ad.setMaxCall2(cp.getMaxCall2());
                            ad.setCallAttempt(0);
                            ad.setCallAttempt2(0);
                        }
                    }
                }
                //assignmentDetailDAO.create(ad);
                JSFUtil.getUserSession().setAssignmentDetail(ad);

                if (ad.getAssignmentId() != null) {
                    if (ad.getAssignmentId().getMarketing() == null) {
                        ad.getAssignmentId().setMarketing(cp.getMarketingInbound());
                    }
                    Marketing m = ad.getAssignmentId().getMarketing();
                    Integer c = (m.getTotalAssigned() == null ? 0 : m.getTotalAssigned()) + 1;
                    m.setTotalAssigned(c);
                    marketingDAO.edit(m);

                    MarketingCustomerPK marketingCustomerPK = new MarketingCustomerPK(m.getId(), JSFUtil.getUserSession().getCustomerDetail().getId());
                    MarketingCustomer mc = marketingCustomerDAO.findMarketingCustomer(marketingCustomerPK);
                    if (mc == null) {
                        mc = new MarketingCustomer();
                        mc.setAssign(true);
                        mc.setCustomer(new Customer(JSFUtil.getUserSession().getCustomerDetail().getId()));
                        mc.setMarketing(m);
                        mc.setMarketingCustomerPK(marketingCustomerPK);
                        mc.setCreateDate(new Date());
                        marketingCustomerDAO.create(mc);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toCloseNoListener(ActionEvent event) {
        String no = JSFUtil.getRequestParameterMap("frm:contactNo");
        if (no != null && !no.equals("")) {
            if (customer.getContactPhone1() != null && customer.getContactPhone1().equals(no)) {
                customer.setContactPhone1Close(Boolean.TRUE);
            }
            if (customer.getContactPhone2() != null && customer.getContactPhone2().equals(no)) {
                customer.setContactPhone2Close(Boolean.TRUE);
            }
            if (customer.getContactPhone3() != null && customer.getContactPhone3().equals(no)) {
                customer.setContactPhone3Close(Boolean.TRUE);
            }
            if (customer.getContactPhone4() != null && customer.getContactPhone4().equals(no)) {
                customer.setContactPhone4Close(Boolean.TRUE);
            }
            if (customer.getContactPhone5() != null && customer.getContactPhone5().equals(no)) {
                customer.setContactPhone5Close(Boolean.TRUE);
            }
            if (customer.getHomePhone() != null && customer.getHomePhone().equals(no)) {
                customer.setHomePhoneClose(Boolean.TRUE);
            }
            if (customer.getOfficePhone() != null && customer.getOfficePhone().equals(no)) {
                customer.setOfficePhoneClose(Boolean.TRUE);
            }
            if (customer.getMobilePhone() != null && customer.getMobilePhone().equals(no)) {
                customer.setMobilePhoneClose(Boolean.TRUE);
            }
            try {
                customerDAO.edit(customer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String subPhoneNo(String str) {
        str = str.substring(0, str.length() - 4) + "****";
        return str;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }

    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }

    public ContactResultDAO getContactResultDAO() {
        return contactResultDAO;
    }

    public void setContactResultDAO(ContactResultDAO contactResultDAO) {
        this.contactResultDAO = contactResultDAO;
    }

    public List<ContactResult> getContactResultList() {
        return contactResultList;
    }

    public void setContactResultList(List<ContactResult> contactResultList) {
        this.contactResultList = contactResultList;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }

    public Integer getContactResultId() {
        return contactResultId;
    }

    public void setContactResultId(Integer contactResultId) {
        this.contactResultId = contactResultId;
    }

    public List<String[]> getContactStatusList() {
        return this.initContactStatusList();
    }

    public void setContactStatusList(List<String[]> contactStatusList) {
        this.contactStatusList = contactStatusList;
    }

    public List<ContactResult> getMainContactResultList() {
        return mainContactResultList;
    }

    public void setMainContactResultList(List<ContactResult> mainContactResultList) {
        this.mainContactResultList = mainContactResultList;
    }

    public Integer getMainContactResultId() {
        return mainContactResultId;
    }

    public void setMainContactResultId(Integer mainContactResultId) {
        this.mainContactResultId = mainContactResultId;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }

    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public MarketingCustomerDAO getMarketingCustomerDAO() {
        return marketingCustomerDAO;
    }

    public void setMarketingCustomerDAO(MarketingCustomerDAO marketingCustomerDAO) {
        this.marketingCustomerDAO = marketingCustomerDAO;
    }

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }

    public ContactHistory getContactHistory() {
        return contactHistory;
    }

    public void setContactHistory(ContactHistory contactHistory) {
        this.contactHistory = contactHistory;
    }

    public ContactHistoryDAO getContactHistoryDAO() {
        return contactHistoryDAO;
    }

    public void setContactHistoryDAO(ContactHistoryDAO contactHistoryDAO) {
        this.contactHistoryDAO = contactHistoryDAO;
    }

    public String getMainContactResultCode() {
        return mainContactResultCode;
    }

    public void setMainContactResultCode(String mainContactResultCode) {
        this.mainContactResultCode = mainContactResultCode;
    }

    public String getOldContact1() {
        return oldContact1;
    }

    public void setOldContact1(String oldContact1) {
        this.oldContact1 = oldContact1;
    }

    public String getOldContact2() {
        return oldContact2;
    }

    public void setOldContact2(String oldContact2) {
        this.oldContact2 = oldContact2;
    }

    public String getOldContact3() {
        return oldContact3;
    }

    public void setOldContact3(String oldContact3) {
        this.oldContact3 = oldContact3;
    }

    public String getOldContact4() {
        return oldContact4;
    }

    public void setOldContact4(String oldContact4) {
        this.oldContact4 = oldContact4;
    }

    public String getOldContact5() {
        return oldContact5;
    }

    public void setOldContact5(String oldContact5) {
        this.oldContact5 = oldContact5;
    }

    public String getOldContactExt1() {
        return oldContactExt1;
    }

    public void setOldContactExt1(String oldContactExt1) {
        this.oldContactExt1 = oldContactExt1;
    }

    public String getOldContactExt2() {
        return oldContactExt2;
    }

    public void setOldContactExt2(String oldContactExt2) {
        this.oldContactExt2 = oldContactExt2;
    }

    public String getOldContactExt3() {
        return oldContactExt3;
    }

    public void setOldContactExt3(String oldContactExt3) {
        this.oldContactExt3 = oldContactExt3;
    }

    public String getOldContactExt4() {
        return oldContactExt4;
    }

    public void setOldContactExt4(String oldContactExt4) {
        this.oldContactExt4 = oldContactExt4;
    }

    public String getOldContactExt5() {
        return oldContactExt5;
    }

    public void setOldContactExt5(String oldContactExt5) {
        this.oldContactExt5 = oldContactExt5;
    }

    public String getOldMobile() {
        return oldMobile;
    }

    public void setOldMobile(String oldMobile) {
        this.oldMobile = oldMobile;
    }

    public String getOldHome() {
        return oldHome;
    }

    public void setOldHome(String oldHome) {
        this.oldHome = oldHome;
    }

    public String getOldHomeExt() {
        return oldHomeExt;
    }

    public void setOldHomeExt(String oldHomeExt) {
        this.oldHomeExt = oldHomeExt;
    }

    public String getOldOffice() {
        return oldOffice;
    }

    public void setOldOffice(String oldOffice) {
        this.oldOffice = oldOffice;
    }

    public String getOldOfficeExt() {
        return oldOfficeExt;
    }

    public void setOldOfficeExt(String oldOfficeExt) {
        this.oldOfficeExt = oldOfficeExt;
    }

    public String getOldFax() {
        return oldFax;
    }

    public void setOldFax(String oldFax) {
        this.oldFax = oldFax;
    }

    public String getOldFaxExt() {
        return oldFaxExt;
    }

    public void setOldFaxExt(String oldFaxExt) {
        this.oldFaxExt = oldFaxExt;
    }

    public String getOldEmail() {
        return oldEmail;
    }

    public void setOldEmail(String oldEmail) {
        this.oldEmail = oldEmail;
    }

    public CustomerAuditLogDAO getCustomerAuditLogDAO() {
        return customerAuditLogDAO;
    }

    public void setCustomerAuditLogDAO(CustomerAuditLogDAO customerAuditLogDAO) {
        this.customerAuditLogDAO = customerAuditLogDAO;
    }

    public boolean isDoNotCall() {
        return doNotCall;
    }

    public void setDoNotCall(boolean doNotCall) {
        this.doNotCall = doNotCall;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSourceExternal() {
        return dataSourceExternal;
    }

    public void setDataSourceExternal(DataSource dataSourceExternal) {
        this.dataSourceExternal = dataSourceExternal;
    }

    public ConfigurationDAO getConfigurationDAO() {
        return configurationDAO;
    }

    public void setConfigurationDAO(ConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
    }

    public TelephonyService getTelephonyService() {
        return telephonyService;
    }

    public void setTelephonyService(TelephonyService telephonyService) {
        this.telephonyService = telephonyService;
    }

    public ContactResultPlan getContactResultPlan() {
        return contactResultPlan;
    }

    public void setContactResultPlan(ContactResultPlan contactResultPlan) {
        this.contactResultPlan = contactResultPlan;
    }

    public List<Integer> getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(List<Integer> purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }
    
    

}
