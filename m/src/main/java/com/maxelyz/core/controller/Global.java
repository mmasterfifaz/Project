package com.maxelyz.core.controller;

import com.maxelyz.core.model.dao.ConfigurationDAO;
import com.maxelyz.core.model.entity.Configuration;
import com.maxelyz.utils.JSFUtil;
//import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import org.apache.log4j.Logger;

@ApplicationScoped
@ManagedBean
public class Global {
    private static Logger log = Logger.getLogger(Global.class);
    private final static String PASSWORD_EXPIRED = "PasswordExpired";
    private final static String MAX_ROWS = "MaxRows";
    private final static String MAX_PAGES = "MaxPages";
    private final static String SLA_CLOSE1 = "SLAClose1";
    private final static String SLA_CLOSE2 = "SLAClose2";
    private final static String SLA_CLOSE3 = "SLAClose3";
    private final static String SLA_CLOSE4 = "SLAClose4";
    private final static String SLA_CLOSE5 = "SLAClose5";
    private final static String SLA_ACCEPT = "SLAAccept";
    private final static String SLA_ACTIVITY = "SLAActivity";
    private final static String SLA_START_WORKING_HOUR = "SLAStartWorkingHour";
    private final static String SLA_END_WORKING_HOUR = "SLAEndWorkingHour";
    private final static String SKIP_WEEKEND = "SkipWeekend";
    private final static String SKIP_HOLIDAY = "SkipHoliday";
    private final static String MAX_CALL1 = "MaxCall1";
    private final static String MAX_CALL2 = "MaxCall2";
    private final static String SALE_APPROVE_FLOW = "SaleApproveFlow";
    private final static String SETTLEMENT_PAYMENT = "SettlementPayment";
    private final static String LDAP_URL = "LDAPURL";
    private final static String AUTHEN_METHOD = "AuthenMethod";
    private final static String LDAP_DOMAIN = "LDAPDomain";
    private final static String VOICE_MMS_URL = "VoiceMMSURL";
    private final static String VOICE_FILE_URL = "VoiceFileURL";
    private final static String VOICE_FILE_PATH = "VoiceFilePath";
    private final static String CLOSE_ASSIGNMENT_LIST = "CloseCampaignList";
    private final static String SMTP_HOST = "SMTPHost";
    private final static String SMTP_PORT = "SMTPPort";
    private final static String ADMIN_EMAIL = "AdminEmail";
    private final static String SMTP_SSL = "SMTPSSL";
    private final static String SMTP_SSL_PORT = "SMTPSSLPort";
    private final static String SMTP_AUTH = "SMTPAuth";
    private final static String SMTP_AUTH_USERNAME = "SMTPAuthUsername";
    private final static String SMTP_AUTH_PASSWORD = "SMTPAuthPassword";
    private final static String ASSIGNMENT_LIST_MAX_ROWS = "AssignmentListMaxRows";
    private final static String ASSIGNMENT_LIST_MAX_PAGES = "AssignmentListMaxPages";
    private final static String PAYMENT_GATEWAY_BBL_URL = "PaymentGatewayBBLUrl";
    private final static String PAYMENT_GATEWAY_BBL_MERCHANTID = "PaymentGatewayBBLMerchantId";
    private final static String KB_TopView = "KBTopView";
    private final static String TRANSFER_COMPLETE_DISPOSITION = "TransferCompleteDisposition";
    private final static String TRANSFER_SERVICE = "TransferService";
    private final static String EMAIL_FILE_PATH = "EmailFilePath";
    private final static String SEPARATE_PATH = "SeparatePath";
    private final static String ASTERISK_WS_URL = "AsteriskWsURL";
    private final static String CONVERT_VOICE_URL = "ConvertVoiceURL";
    private final static String CONVERT_VOICE_WSDL = "ConvertVoiceWSDL";
    private final static String CONVERT_VOICE_RESTFUL = "ConvertVoiceRestful";
    private final static String CONVERT_VOICE_FOLDER = "ConvertVoiceFolder";
    private final static String VOICE_BASE_PATH = "VoiceBasePath";
    private final static String AUTO_CLOSE_ASSIGNMENTDETAIL = "AutoCloseAssignmentDetail";
    private final static String EXTERNAL_DNC = "ExternalDNC";
    private final static String IMPORT_DNC = "ImportDNC";
    private final static String EXTERNAL_REFNO = "ExternalRefNo";
    private final static String VOICE_SOURCE = "VoiceSource";
    private final static String SYSTEM_ID = "SystemID";
    
    @ManagedProperty(value = "#{configurationDAO}")
    private ConfigurationDAO configurationDAO;
    private Map<String, String> configurationMap = new ConcurrentHashMap<String, String>();
    private int passwordExpired;
    private int maxrows;
    private int maxpages;
    private int slaClose1;
    private int slaClose2;
    private int slaClose3;
    private int slaClose4;
    private int slaClose5;
    private int slaAccept;
    private int slaActivity;
    private double slaStartWorkingHour;
    private double slaEndWorkingHour;
    private boolean skipWeekEnd;
    private boolean skipHoliday;
    private int concurrentUser;
    private int concurrentUserGroup1;
    private int concurrentUserGroup2;
    private int concurrentUserGroup3;
    private int concurrentUserGroup4;
    private int concurrentUserGroup5;
    private String licenseKey;
    private int maxcall1;
    private int maxcall2;
    private int saleApproveFlow;
    private int settlementPayment;
    private String ldapUrl;
    private String authenMethod;
    private String ldapDomain;
    private String voiceMMSUrl;
    private String voiceFileUrl;
    private String voiceFilePath;
    private int closeAssignmentList;
    private String smtpHost;
    private int smtpPort;
    private boolean smtpSsl;
    private String adminEmail;
    private int smtpSslPort;
    private boolean smtpAuth;
    private String smtpAuthUsername;
    private String smtpAuthPassword;
    private int assignmentListMaxRows;
    private int assignmentListMaxPages;
    private String paymentGatewayBBLUrl;
    private String paymentGatewayBBLMerchantId;
    private int kbTopView;
    private String transferCompleteDisposition;
    private String transferToService;
    private String emailFilePath;
    private String separatePath;
    private String asteriskWsUrl;
    private String asteriskRemoteHost;
    private String convertVoiceURL;
    private String convertVoiceWSDL;
    private String convertVoiceRestful;
    private String convertVoiceFolder;
    private String voiceBasePath;
    private boolean autoCloseAssignmentDetail;
    private boolean externalDnc;
    private boolean importDnc;
    private boolean externalRefNo;
    private String voiceSource;
    private String systemID;

    @PostConstruct
    public void initialize() {
        List<Configuration> configurations =  this.getConfigurationDAO().findConfigurationEntities();
        configurationMap.clear();
        for (Configuration obj: configurations) {
            configurationMap.put(obj.getProperty(), obj.getValue());
        }
        passwordExpired = getProperties(PASSWORD_EXPIRED, 0);
        maxrows = getProperties(MAX_ROWS, 10);
        maxpages = getProperties(MAX_PAGES, 10);
        slaClose1 = getProperties(SLA_CLOSE1, 24);
        slaClose2 = getProperties(SLA_CLOSE2, 0);
        slaClose3 = getProperties(SLA_CLOSE3, 0);
        slaClose4 = getProperties(SLA_CLOSE4, 0);
        slaClose5 = getProperties(SLA_CLOSE5, 0);
        slaAccept = getProperties(SLA_ACCEPT, 24);
        slaActivity = getProperties(SLA_ACTIVITY, 24);
        slaStartWorkingHour = getProperties(SLA_START_WORKING_HOUR, 8.30);
        slaEndWorkingHour = getProperties(SLA_END_WORKING_HOUR, 22.0);
        skipWeekEnd = getProperties(SKIP_WEEKEND, true);
        skipHoliday = getProperties(SKIP_HOLIDAY, true);
        maxcall1 = getProperties(MAX_CALL1, 5);
        maxcall2 = getProperties(MAX_CALL2, 4);
        saleApproveFlow = getProperties(SALE_APPROVE_FLOW, 1); //1:Payment->QC, 2:QC->Payment
        settlementPayment = getProperties(SETTLEMENT_PAYMENT, 0); //0:Disable->Auto set settlement in po, 1:Enable -> need process to settlement
        ldapUrl = getProperties(LDAP_URL, "");
        authenMethod = getProperties(AUTHEN_METHOD, "db");
        ldapDomain = getProperties(LDAP_DOMAIN,"");
        voiceMMSUrl = getProperties(VOICE_MMS_URL,"mms://192.168.4.13/");
        voiceFileUrl = getProperties(VOICE_FILE_URL,"http://192.168.4.13:81/");
        voiceFilePath = getProperties(VOICE_FILE_PATH, "C:\\");
        closeAssignmentList = getProperties(CLOSE_ASSIGNMENT_LIST, 3);
        smtpHost = getProperties(SMTP_HOST, "smtp.gmail.com");
        smtpPort = getProperties(SMTP_PORT, 25);
        smtpSsl = getProperties(SMTP_SSL, false);
        smtpSslPort = getProperties(SMTP_SSL_PORT, 465);
        adminEmail = getProperties(ADMIN_EMAIL, "admin@email.com");
        smtpAuth = getProperties(SMTP_AUTH, false);
        smtpAuthUsername = getProperties(SMTP_AUTH_USERNAME, "yourname");
        smtpAuthPassword = getProperties(SMTP_AUTH_PASSWORD, "yourpassword");
        assignmentListMaxRows = getProperties(ASSIGNMENT_LIST_MAX_ROWS, 5);
        assignmentListMaxPages = getProperties(ASSIGNMENT_LIST_MAX_PAGES, 0);
        paymentGatewayBBLUrl = getProperties(PAYMENT_GATEWAY_BBL_URL, "https://psipay.bangkokbank.com/b2c/eng/directPay/payComp.jsp");
        paymentGatewayBBLMerchantId = getProperties(PAYMENT_GATEWAY_BBL_MERCHANTID, "9999");
        kbTopView = getProperties(KB_TopView, 20);
        transferCompleteDisposition = getProperties(TRANSFER_COMPLETE_DISPOSITION, "MX_MOVE");
        transferToService = getProperties(TRANSFER_SERVICE, "53");
        emailFilePath = getProperties(EMAIL_FILE_PATH, "D:\\emailgatewaystore\\");
        separatePath = getProperties(SEPARATE_PATH, "\\");
        asteriskWsUrl = getProperties(ASTERISK_WS_URL, "http://192.168.4.124:8081/MaxarAsteriskAPI/");
        convertVoiceURL = getProperties(CONVERT_VOICE_URL, "http://192.168.51.124:99");
        convertVoiceWSDL = getProperties(CONVERT_VOICE_WSDL, "http://192.168.51.124:99/ExportVoiceAspect.svc?wsdl");
        convertVoiceRestful = getProperties(CONVERT_VOICE_RESTFUL, "");     //eg. "http://192.168.51.102:10001/api/ConvertVoice/convert"
        convertVoiceFolder = getProperties(CONVERT_VOICE_FOLDER, "");       //eg. "fwd_central\FWD_QC\"
        voiceBasePath = getProperties(VOICE_BASE_PATH, "V:/");
        autoCloseAssignmentDetail = getProperties(AUTO_CLOSE_ASSIGNMENTDETAIL, false);
        externalDnc = getProperties(EXTERNAL_DNC, false);
        importDnc = getProperties(IMPORT_DNC, false);
        externalRefNo = getProperties(EXTERNAL_REFNO, false);
        voiceSource = getProperties(VOICE_SOURCE, "inet-tn01");
        systemID = getProperties(SYSTEM_ID, "01");
        
        //--------get from web.xml------
        try {
            String conUser =  JSFUtil.getServletContext().getInitParameter("com.maxelyz.CONCURRENT_USER"); 
            concurrentUser = Integer.parseInt(conUser);
        } catch (Exception e) {
            concurrentUser = 1;   
        }
        
        try {
            String conUser1 =  JSFUtil.getServletContext().getInitParameter("com.maxelyz.CONCURRENT_USER_GROUP1"); 
            concurrentUserGroup1 = Integer.parseInt(conUser1);
        } catch (Exception e) {
            concurrentUserGroup1 = 1;   
        }       
        
        try {
            String conUser2 =  JSFUtil.getServletContext().getInitParameter("com.maxelyz.CONCURRENT_USER_GROUP2"); 
            concurrentUserGroup2 = Integer.parseInt(conUser2);
        } catch (Exception e) {
            concurrentUserGroup2 = 1;   
        } 
        
        try {
            String conUser3 =  JSFUtil.getServletContext().getInitParameter("com.maxelyz.CONCURRENT_USER_GROUP3"); 
            concurrentUserGroup3 = Integer.parseInt(conUser3);
        } catch (Exception e) {
            concurrentUserGroup3 = 1;   
        } 
                
        try {
            String conUser4 =  JSFUtil.getServletContext().getInitParameter("com.maxelyz.CONCURRENT_USER_GROUP4"); 
            concurrentUserGroup4 = Integer.parseInt(conUser4);
        } catch (Exception e) {
            concurrentUserGroup4 = 1;   
        } 
                
        try {
            String conUser5 =  JSFUtil.getServletContext().getInitParameter("com.maxelyz.CONCURRENT_USER_GROUP5"); 
            concurrentUserGroup5 = Integer.parseInt(conUser5);
        } catch (Exception e) {
            concurrentUserGroup5 = 1;   
        } 
        
        try {
            licenseKey =  JSFUtil.getServletContext().getInitParameter("com.maxelyz.LICENSE_KEY"); 
        } catch (Exception e) {
            licenseKey = "";   
        }
   
    }

    private int getProperties(String propertyName, int defaultValue) {
        int returnValue;
        try {
            returnValue = Integer.parseInt((configurationMap.get(propertyName)));
            if (configurationMap.get(propertyName)==null)
                throw new Exception(); 
        } catch (Exception e) {
            try {
                this.getConfigurationDAO().create(new Configuration(propertyName,String.valueOf(defaultValue),"int"));
            } catch (Exception ex) {
                //log.error(ex+":"+propertyName);
            }
            returnValue = defaultValue;
        }
        return returnValue;
    }

    private Double getProperties(String propertyName, double defaultValue) {
        double returnValue;
        try {
            returnValue = Double.parseDouble((configurationMap.get(propertyName)));
            if (configurationMap.get(propertyName)==null)
                throw new Exception(); 
        } catch (Exception e) {
            try {
                this.getConfigurationDAO().create(new Configuration(propertyName,String.valueOf(defaultValue),"Double"));
            } catch (Exception ex) {
                //log.error(ex+":"+propertyName);
            }
            returnValue = defaultValue;
        }
        return returnValue;
    }

     private boolean getProperties(String propertyName, boolean defaultValue) {
        boolean returnValue;
        try {
            returnValue = Boolean.parseBoolean(configurationMap.get(propertyName));
            if (configurationMap.get(propertyName)==null)
                throw new Exception(); 
        } catch (Exception e) {
            try {
                this.getConfigurationDAO().create(new Configuration(propertyName,String.valueOf(defaultValue),"boolean"));
            } catch (Exception ex) {
                //log.error(ex+":"+propertyName);
            }
            returnValue = defaultValue;
        }
        return returnValue;
    }

     private String getProperties(String propertyName, String defaultValue) {
        String returnValue;
        try {
            returnValue = configurationMap.get(propertyName);
            if (returnValue==null)
                throw new Exception();
        } catch (Exception e) {
            try {
                this.getConfigurationDAO().create(new Configuration(propertyName,String.valueOf(defaultValue),"String"));
            } catch (Exception ex) {
                //log.error(ex+":"+propertyName);
            }
            returnValue = defaultValue;
        }
        return returnValue;
    }
    public Map<String, String> getConfigurationMap() {
        return configurationMap;
    }

    public int getPasswordExpired() {
        return passwordExpired;
    }

    public int getMaxpages() {
        return maxpages;
    }

    public int getMaxrows() {
        return maxrows;
    }

    public int getSlaClose1() {
        return slaClose1;
    }

    public int getSlaClose2() {
        return slaClose2;
    }

    public int getSlaClose3() {
        return slaClose3;
    }

    public int getSlaClose4() {
        return slaClose4;
    }

    public int getSlaClose5() {
        return slaClose5;
    }

    public int getSlaAccept() {
        return slaAccept;
    }

    public int getSlaActivity() {
        return slaActivity;
    }

    public Double getSlaEndWorkingHour() {
        return slaEndWorkingHour;
    }


    public Double getSlaStartWorkingHour() {
        return slaStartWorkingHour;
    }

    public boolean isSkipHoliday() {
        return skipHoliday;
    }

    public boolean isSkipWeekEnd() {
        return skipWeekEnd;
    }

    public int getConcurrentUser() {
        return concurrentUser;
    }

    public int getConcurrentUserGroup1() {
        return concurrentUserGroup1;
    }

    public int getConcurrentUserGroup2() {
        return concurrentUserGroup2;
    }

    public int getConcurrentUserGroup3() {
        return concurrentUserGroup3;
    }

    public int getConcurrentUserGroup4() {
        return concurrentUserGroup4;
    }

    public int getConcurrentUserGroup5() {
        return concurrentUserGroup5;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public int getMaxcall1() {
        return maxcall1;
    }

    public int getMaxcall2() {
        return maxcall2;
    }

    public int getSaleApproveFlow() {
        return saleApproveFlow;
    }

    public int getSettlementPayment() {
        return settlementPayment;
    }

    public String getAuthenMethod() {
        return authenMethod;
    }

    public void setAuthenMethod(String authenMethod) {
        this.authenMethod = authenMethod;
    }

    public String getLdapUrl() {
        return ldapUrl;
    }

    public void setLdapUrl(String ldapUrl) {
        this.ldapUrl = ldapUrl;
    }

    public String getLdapDomain() {
        return ldapDomain;
    }

    public void setLdapDomain(String ldapDomain) {
        this.ldapDomain = ldapDomain;
    }

    public String getVoiceFileUrl() {
        return voiceFileUrl;
    }

    public void setVoiceFileUrl(String voiceFileUrl) {
        this.voiceFileUrl = voiceFileUrl;
    }

    public String getVoiceFilePath() {
        return voiceFilePath;
    }

    public void setVoiceFilePath(String voiceFilePath) {
        this.voiceFilePath = voiceFilePath;
    }

    public String getVoiceMMSUrl() {
        return voiceMMSUrl;
    }

    public void setVoiceMMSUrl(String voiceMMSUrl) {
        this.voiceMMSUrl = voiceMMSUrl;
    }

    public int getCloseAssignmentList() {
        return closeAssignmentList;
    }

    public void setCloseAssignmentList(int closeAssignmentList) {
        this.closeAssignmentList = closeAssignmentList;
    }

    public void setSlaEndWorkingHour(double slaEndWorkingHour) {
        this.slaEndWorkingHour = slaEndWorkingHour;
    }

    public void setSlaStartWorkingHour(double slaStartWorkingHour) {
        this.slaStartWorkingHour = slaStartWorkingHour;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public boolean isSmtpSsl() {
        return smtpSsl;
    }

    public void setSmtpSsl(boolean smtpSsl) {
        this.smtpSsl = smtpSsl;
    }

    public int getSmtpSslPort() {
        return smtpSslPort;
    }

    public void setSmtpSslPort(int smtpSslPort) {
        this.smtpSslPort = smtpSslPort;
    }

    public boolean isSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(boolean smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public String getSmtpAuthPassword() {
        return smtpAuthPassword;
    }

    public void setSmtpAuthPassword(String smtpAuthPassword) {
        this.smtpAuthPassword = smtpAuthPassword;
    }

    public String getSmtpAuthUsername() {
        return smtpAuthUsername;
    }

    public void setSmtpAuthUsername(String smtpAuthUsername) {
        this.smtpAuthUsername = smtpAuthUsername;
    }

    public int getAssignmentListMaxPages() {
        return assignmentListMaxPages;
    }

    public void setAssignmentListMaxPages(int assignmentListMaxPages) {
        this.assignmentListMaxPages = assignmentListMaxPages;
    }

    public int getAssignmentListMaxRows() {
        return assignmentListMaxRows;
    }

    public void setAssignmentListMaxRows(int assignmentListMaxRows) {
        this.assignmentListMaxRows = assignmentListMaxRows;
    }

    public String getPaymentGatewayBBLUrl() {
        return paymentGatewayBBLUrl;
    }

    public void setPaymentGatewayBBLUrl(String paymentGatewayBBLUrl) {
        this.paymentGatewayBBLUrl = paymentGatewayBBLUrl;
    }

    public String getPaymentGatewayBBLMerchantId() {
        return paymentGatewayBBLMerchantId;
    }

    public void setPaymentGatewayBBLMerchantId(String paymentGatewayBBLMerchantId) {
        this.paymentGatewayBBLMerchantId = paymentGatewayBBLMerchantId;
    }
    
    public int getKbTopView() {
        return kbTopView;
    }

    public void setKbTopView(int kbTopView) {
        this.kbTopView = kbTopView;
    }

    public String getTransferCompleteDisposition() {
        return transferCompleteDisposition;
    }

    public void setTransferCompleteDisposition(String transferCompleteDisposition) {
        this.transferCompleteDisposition = transferCompleteDisposition;
    }

    public String getTransferToService() {
        return transferToService;
    }

    public void setTransferToService(String transferToService) {
        this.transferToService = transferToService;
    }
    
    //Managed Properties
    public ConfigurationDAO getConfigurationDAO() {
        return configurationDAO;
    }

    public void setConfigurationDAO(ConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
    }

    public String getEmailFilePath() {
        return emailFilePath;
    }

    public void setEmailFilePath(String emailFilePath) {
        this.emailFilePath = emailFilePath;
    }

    public String getSeparatePath() {
        return separatePath;
    }

    public void setSeparatePath(String separatePath) {
        this.separatePath = separatePath;
    }

    public String getAsteriskWsUrl() {
        return asteriskWsUrl;
    }

    public void setAsteriskWsUrl(String asteriskWsUrl) {
        this.asteriskWsUrl = asteriskWsUrl;
    }

    public String getConvertVoiceURL() {
        return convertVoiceURL;
    }

    public void setConvertVoiceURL(String convertVoiceURL) {
        this.convertVoiceURL = convertVoiceURL;
    }

    public String getConvertVoiceWSDL() {
        return convertVoiceWSDL;
    }

    public void setConvertVoiceWSDL(String convertVoiceWSDL) {
        this.convertVoiceWSDL = convertVoiceWSDL;
    }

    public String getConvertVoiceRestful() {
        return convertVoiceRestful;
    }

    public void setConvertVoiceRestful(String convertVoiceRestful) {
        this.convertVoiceRestful = convertVoiceRestful;
    }

    public String getConvertVoiceFolder() {
        return convertVoiceFolder;
    }

    public void setConvertVoiceFolder(String convertVoiceFolder) {
        this.convertVoiceFolder = convertVoiceFolder;
    }

    public String getVoiceBasePath() {
        return voiceBasePath;
    }

    public void setVoiceBasePath(String voiceBasePath) {
        this.voiceBasePath = voiceBasePath;
    }

    public boolean isAutoCloseAssignmentDetail() {
        return autoCloseAssignmentDetail;
    }

    public void setAutoCloseAssignmentDetail(boolean autoCloseAssignmentDetail) {
        this.autoCloseAssignmentDetail = autoCloseAssignmentDetail;
    }
    
    public boolean isExternalDnc() {
        return externalDnc;
    }

    public void setExternalDnc(boolean externalDnc) {
        this.externalDnc = externalDnc;
    }
    
    public boolean isImportDnc() {
        return importDnc;
    }

    public void setImportDnc(boolean importDnc) {
        this.importDnc = importDnc;
    }
    
     public boolean isExternalRefNo() {
        return externalRefNo;
    }

    public void setExternalRefNo(boolean externalRefNo) {
        this.externalRefNo = externalRefNo;
    }
    
    public String getVoiceSource() {
        return voiceSource;
    }

    public void setVoiceSource(String voiceSource) {
        this.voiceSource = voiceSource;
    }

    public String getSystemID() {
        return systemID;
    }

    public void setSystemID(String systemID) {
        this.systemID = systemID;
    }
    
    
}
