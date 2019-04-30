package com.maxelyz.core.controller.login;

import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.entity.LogoffType;
//import com.maxelyz.core.service.SecurityService;
import com.maxelyz.core.service.UserService;
//import com.maxelyz.core.service.UserServiceImpl;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.Message;
import com.maxelyz.utils.SecurityUtil;
import com.maxelyz.utils.SessionBindingListener;
import com.maxelyz.core.model.value.admin.SessionMonitorValue;
import com.maxelyz.core.service.TelephonyService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
//import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.naming.AuthenticationException;
//import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.servlet.http.HttpSession;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.exolab.castor.types.DateTime;

@ManagedBean
@RequestScoped
public class LoginController {
    private static Logger log = Logger.getLogger(LoginController.class);    
    private static Logger ctiLog = Logger.getLogger("mxCtiLogger");
    
    private static String LOGIN_PAGE = "/login/login.xhtml";
    private String userName;//="oat";
    private String password;//="11111111";
    private String message;
    private String stationNo;
    
    private String ldapUrl="192.168.4.55";
    private String ldapManagerDN="administrator"; 
    private String ldapManagerPW="Passw0rd";
    private String logoffTypeName;
    private Map<String, Integer> logoffTypeList;
    private Boolean logoutStatus;
    
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{userService}")
    private UserService userService;
    @ManagedProperty(value = "#{telephonyService}")
    private TelephonyService telephonyService;

    @PostConstruct
    public void initialize() {
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//      if (!checkLicense()) {
//          JSFUtil.redirect("license.jsf"); 
//      }
        logoutStatus = false;
    }
    
    public boolean checkLicense() {
        String key = Message.getLocalKey()+JSFUtil.getApplication().getConcurrentUser();
        String licenseKey = JSFUtil.getApplication().getLicenseKey();
        return Message.getLicenKey(key).equals(licenseKey);
    }
    
    private void ldapAuthen(String username, String password,String baseDN) throws Exception {
        String url = JSFUtil.getApplication().getLdapUrl();
        if (baseDN.length()>0)
            url = url+"/"+baseDN;//+"/dc=terrabit,dc=co,dc=th";
        if (JSFUtil.getApplication().getLdapDomain()!=null && JSFUtil.getApplication().getLdapDomain().length()>0) {
            username = username+"@"+JSFUtil.getApplication().getLdapDomain();
        }
        DirContext context = SecurityUtil.getRootDirContext(url, username, password); 
    }
  
    public String loginAction() {
        UsersDAO dao = getUsersDAO();
        List<Users> users = dao.findUsersByLoginName(userName);
        
        if (users.size() > 0) {

            Users user = users.get(0);
            if ("ldap".equals(JSFUtil.getApplication().getAuthenMethod())) {
                try {
                    String ldapUser = user.getLdapLogin();
                    if (ldapUser==null)
                        ldapUser="";
                    String baseDN = user.getBaseDn();
                    if (baseDN==null)
                        baseDN = "";
                    ldapAuthen(ldapUser,password,baseDN);
                } catch(AuthenticationException e) {
                    String err = e.toString();
                    if(err.contains("data")) {
                        String code = err.substring(err.indexOf("data")+5, err.indexOf("data")+8);
                        if(code.equals("52e")) {
                            message = "Wrong Password (Invalid Credentials)";
                        } else if(code.equals("525")) {
                                message = "User Not Found";
                        } else if(code.equals("530")) {
                                message = "Not Permitted To Logon At This Time";
                        } else if(code.equals("531")) {
                                message = "Not Permitted To Logon At This Workstation";
                        } else if(code.equals("532")) {
                                message = "Password Expired";
                        } else if(code.equals("533")) {
                                message = "Account Disabled";
                        } else if(code.equals("701")) {
                                message = "Account Expired";
                        } else if(code.equals("773")) {
                                message = "User Must Reset Password";
                        } else if(code.equals("775")) {
                                message = "User Account Locked";
                        } else {
                                message = e.toString();
                        }
                    }
                    return null;
                } catch(Exception e) {
                    message = e.toString();
                    return null;
                }                
            } else  if (!(user.getLoginName().trim().toUpperCase().equals(userName.toUpperCase()) && user.getUserPassword().trim().equals(password))) {
                 message = "Invalid username or password";
                 return null;
            }
            
            if (user.getUserGroup().getConcurrentGroup() == null) { 
                message = "Concurrent License Group not set";
                return null;
            }
                        
            if (!addConcurrentUser(user, user.getUserGroup().getConcurrentGroup())) { 
                message = "Concurrent User Limit Exceeded";
                return null;
            }

            if (user.getUserExpire()!=null && user.getUserExpire()) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(new Date());
                    String nowdate = formatter.format(user.getUserExpireDate());
                    if (formatter2.parse(date).after(formatter2.parse(nowdate))){
                        message = "User Expired";
                        return null;
                    }
                }catch (Exception e) {
                    log.error(e);
                }
            }
            
            JSFUtil.getUserSession().removeCustomerSession();
            UserSession u = JSFUtil.getUserSession();
            u.setLdapPassword(password);
            u.setUsers(user);
            u.setUserGroup(user.getUserGroup());
            u.setUserGroupAclList(usersDAO.findAclByUser(user));
            u.setUserGroupLocationList(userGroupDAO.findUserGroupLocationByUserGroupId(user.getUserGroup().getId()));
            
            //Get Station From Login
            if(stationNo.contains("Station") || stationNo.equals("")) {
                u.setStationNo(null);
                stationNo = null;
            } else {
                u.setStationNo(stationNo);
                //Call Login Telephony Rest API                
                if(stationNo != null && !stationNo.equals("")) {
                    try {                     
                        String loginResult = "";
                        if(user.getUserGroup() != null && user.getUserGroup().getCtiAdapter() != null && user.getUserGroup().getCtiAdapter().getCtiVendorCode().equals("aspect")) {
                            //GET CTI TOOLBAR
                            u.setCtiToolbar(user.getUserGroup().getCtiAdapter().getWebToolbarUrl());
                        } else {
                            if(user.getTelephonySso()) {
                                loginResult = this.telephonyService.loginTelephony(user.getLdapLogin(), password, stationNo);
                                user.setTelephonyLoginName(user.getLdapLogin());
                            } else {
                                loginResult = this.telephonyService.loginTelephony(user.getTelephonyLoginName(), user.getTelephonyPassword(), stationNo);
                            }                        

                            ctiLog.info(new Date().toString()+" LOGIN Extension:  "+stationNo+", Result: "+loginResult);
                            if(!loginResult.equals("SUCCESS")) {
                                message = loginResult;
                                return null;
                            }
                        } 
                        
                    } catch (Exception E) {
                        message = E.getMessage();
                        return null;
                    }
                }
            }
            
            userService.setLoginTime(user);            
            
            String pageNav="/front/todolist/incoming.xhtml";

            if (checkForceChangePassword(user)) {
                pageNav = "changePassword";
            } else {
                if (user.getDefaultPage().equals("todolist")) {
                    u.setMainPage("todolist");
                    pageNav = "/front/todolist/incoming.xhtml";
                    if (SecurityUtil.isPermitted("telephonybar:view") && stationNo != null && !stationNo.equals("")) {
                        JSFUtil.redirect("framecs.html");
                        return null;
                    }else{
                        JSFUtil.redirect("framecsaction.html");
                        return null;
                    }
                } else if (user.getDefaultPage().equals("campaign")) {
                    u.setMainPage("campaign");
                    pageNav = "/campaign/assignmentList.xhtml";
                    if (SecurityUtil.isPermitted("telephonybar:view") && stationNo != null && !stationNo.equals("")) {
                        JSFUtil.redirect("framets.html");
                        return null;
                    }else{
                        JSFUtil.redirect("frametsaction.html");
                        return null;
                    }
                } else if (user.getDefaultPage().equals("admin")) {
                    u.setMainPage("admin");
                    pageNav = "/admin/dashboard.xhtml";
                    if (SecurityUtil.isPermitted("telephonybar:view") && stationNo != null && !stationNo.equals("")) {
                        JSFUtil.redirect("frameadmin.html");
                        return null;
                    }else{
                        JSFUtil.redirect("frameadminaction.html");
                        return null;
                    }
                } else if (user.getDefaultPage().equals("report")) {
                    u.setMainPage("report");
                    pageNav = "/report/caselist.xhtml";
                    if (SecurityUtil.isPermitted("telephonybar:view") && stationNo != null && !stationNo.equals("")) {
                        JSFUtil.redirect("framereport.html");
                        return null;
                    }else{
                        JSFUtil.redirect("framereportaction.html");
                        return null;
                    }
                    
                } else if (user.getDefaultPage().equals("search")) {
                    u.setMainPage("search");
                    pageNav = "/front/search/searchCustomer.xhtml";
                    if (SecurityUtil.isPermitted("telephonybar:view") && stationNo != null && !stationNo.equals("")) {
                        JSFUtil.redirect("framecs.html");
                        return null;
                    }else{
                        JSFUtil.redirect("framecsaction.html");
                        return null;
                    }
                }else if (user.getDefaultPage().equals("social")) {
                    u.setMainPage("social");
                    pageNav = "/social/sclassignment.xhtml";
                    
                    
                } else {
                    message = "Invalid role of user group";
                    pageNav = LOGIN_PAGE;
                    return pageNav;
                }
               
            }

            return pageNav;
        
        }
        message = "Invalid username or password";
        return "login.xhtml";
    }

    public void logoutActionListener(ActionEvent event) {
        try {
            Users user = JSFUtil.getUserSession().getUsers();
            userService.setLogoutTime(user, logoffTypeName);
            logoutStatus = true;
            
            //Call Logout Telephony Rest API
            if(JSFUtil.getUserSession().getStationNo() != null && !JSFUtil.getUserSession().getStationNo().equals("")) {
                String result = "";
                if(user.getTelephonySso()) {
                    result = this.telephonyService.logoutTelephony(user.getLdapLogin(), stationNo);
                } else {
                    result = this.telephonyService.logoutTelephony(user.getTelephonyLoginName(), stationNo);
                }  
                ctiLog.info(new Date().toString()+" LOGOUT Extension: "+JSFUtil.getUserSession().getStationNo()+", Result: "+result);
            }
        } catch(Exception e) {
            log.error(e);
        }
    }
    
    public void logoutListener(ActionEvent event) {
        JSFUtil.getSession().invalidate();
    }
    
    public List<LogoffType> getLogoffType() {
        return userService.findLogoffTypes();
    }
    
    private synchronized boolean addConcurrentUser(Users user, String concurrentGroup) {
        HttpSession session = JSFUtil.getSession();
        Map<String, SessionMonitorValue> concurrentUserMap = JSFUtil.getConcurrentUser(concurrentGroup);//get from application
        if (concurrentUserMap==null) {
            concurrentUserMap = new ConcurrentHashMap<String, SessionMonitorValue>();
        }
        
        int concurrentAll = JSFUtil.getApplication().getConcurrentUser();
        int sumConcurrent = 0;
        int concurrentSize = 0;
        if(concurrentGroup.equals("1"))
            concurrentSize = JSFUtil.getApplication().getConcurrentUserGroup1();
        else  if(concurrentGroup.equals("2"))
            concurrentSize = JSFUtil.getApplication().getConcurrentUserGroup2();
        else  if(concurrentGroup.equals("3"))
            concurrentSize = JSFUtil.getApplication().getConcurrentUserGroup3();
        else  if(concurrentGroup.equals("4"))
            concurrentSize = JSFUtil.getApplication().getConcurrentUserGroup4();
        else  if(concurrentGroup.equals("5"))
            concurrentSize = JSFUtil.getApplication().getConcurrentUserGroup5();
        
        sumConcurrent = JSFUtil.getCntConcurrentUser();
       
        // CHECK DUPLICATE CONCURRENT
        if(concurrentUserMap.containsKey(user.getLoginName())) {
            if (!session.isNew()) {
                session.invalidate();
                session = JSFUtil.getNewSession();
            }
            Date currentDate = new Date();
            SessionMonitorValue value = new SessionMonitorValue(user, currentDate, JSFUtil.getRequest().getRemoteAddr(), session.getId(), "Not Ready");
            concurrentUserMap.put(user.getLoginName(), value);

            session.setAttribute("sessionBindingListener", new SessionBindingListener(user.getLoginName()));
            return true;
        } else {
            if (concurrentUserMap.size() < concurrentSize || concurrentSize == 0) {
                if(sumConcurrent < concurrentAll) {
                    if (!session.isNew()) {
                        session.invalidate();
                        session = JSFUtil.getNewSession();
                    }
                    Date currentDate = new Date();
                    SessionMonitorValue value = new SessionMonitorValue(user, currentDate, JSFUtil.getRequest().getRemoteAddr(), session.getId(), "Not Ready");
                    concurrentUserMap.put(user.getLoginName(), value);

                    session.setAttribute("sessionBindingListener", new SessionBindingListener(user.getLoginName()));
                    JSFUtil.setConcurrentUserGroup(concurrentUserMap, concurrentGroup);
                    return true;
                }
            }
        } 
        return false;
    }
    
    public boolean checkForceChangePassword(Users user) {
        if (getUsersDAO().forceChangePassword(user.getId())) {
            return true;
        }
        Calendar lastChangePasswordDate = Calendar.getInstance();
        lastChangePasswordDate.setTime(user.getLastChangedPassword());
        Calendar now = Calendar.getInstance();
        int day = (int) ((now.getTimeInMillis() - lastChangePasswordDate.getTimeInMillis()) / 1000.0 / 60 / 60 / 24);
        int passwordExpiredDay = JSFUtil.getApplication().getPasswordExpired();
        if (passwordExpiredDay != 0 && day >= passwordExpiredDay) {
                user.setForceChangePassword(1);
                this.usersDAO.edit(user);
                return true;    
        }
        return false;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogoffTypeName() {
        return logoffTypeName;
    }

    public void setLogoffTypeName(String logoffTypeName) {
        this.logoffTypeName = logoffTypeName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Integer> getLogoffTypeList() {
        return logoffTypeList;
    }

    public void setLogoffTypeList(Map<String, Integer> logoffTypeList) {
        this.logoffTypeList = logoffTypeList;
    } 

    //Mananaged Properties
    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public Boolean getLogoutStatus() {
        return logoutStatus;
    }

    public void setLogoutStatus(Boolean logoutStatus) {
        this.logoutStatus = logoutStatus;
    }

    public TelephonyService getTelephonyService() {
        return telephonyService;
    }

    public void setTelephonyService(TelephonyService telephonyService) {
        this.telephonyService = telephonyService;
    }
    
    
}
