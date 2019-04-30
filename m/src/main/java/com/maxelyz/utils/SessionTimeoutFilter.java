/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.utils;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.value.admin.SessionMonitorValue;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Oat
 */
public class SessionTimeoutFilter implements Filter {

    private String timeoutPage = "/login/login.jsf";

    private String loginPath = "/login/";
    //private String timeoutPage = "/index.html";

    public void init(FilterConfig filterconfig) throws ServletException {
    }
    
    public boolean isDuplicateLogin(ServletRequest request,String loginName) {
         HttpServletRequest httpServletRequest = (HttpServletRequest) request;
         HttpSession session = httpServletRequest.getSession(false);
         ServletContext application = session.getServletContext();
         String groups[] = {"concurrentusersgroup1","concurrentusersgroup2","concurrentusersgroup3","concurrentusersgroup4","concurrentusersgroup5"};
         Boolean chkDup = false;
         
         for (String group : groups) {
             if(application.getAttribute(group) != null) {
//                chkDup = true;
                Map<String, SessionMonitorValue> concurrentUserMap1 = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute(group);
                if(concurrentUserMap1 != null && !concurrentUserMap1.isEmpty() && concurrentUserMap1.containsKey(loginName)) {
                    SessionMonitorValue s = concurrentUserMap1.get(loginName);
                    if(s != null && s.getRemoteAddr() != null){
                        chkDup = !s.getRemoteAddr().equals(request.getRemoteAddr());
                    }else{
                        chkDup = false;
                    }
                    try{
                        if(chkDup) {
                            System.out.println("[" + new Date() +"] loginName : " + loginName + ", Current getRemoteAddr : " + request.getRemoteAddr() + ", SessionMonitorValue : " + s.getRemoteAddr() + ", SessionMonitorValue loginName : " + s.getUser().getLoginName());
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
             } 
         } 
         return chkDup;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain) throws IOException, ServletException {
        if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            String url = httpServletRequest.getServletPath();
            HttpSession session = httpServletRequest.getSession(false);

            UserSession obj = null;
            if (session != null) {
                obj = (UserSession) session.getAttribute("userSession"); //com.maxelyz.core.controller
            }
            if (!(url.contains("/a4j/") || url.contains("/img/") || url.contains("/resources/") || url.contains("/css/") || url.contains("/api") ||  url.contains("/MaxarWSService") || url.contains("/primepush")) && !(url.contains(loginPath) || url.contains("/index.html"))) {
                //if dup login then kick
                if (obj!=null && obj.getUsers()!=null && isDuplicateLogin(request,obj.getUsers().getLoginName())) {
//                    session.invalidate();
                    String dupurl = httpServletRequest.getContextPath() + "/login/duplicatelogin.jsf";
                    //System.out.println(dupurl);
                    httpServletResponse.sendRedirect(dupurl);
                    return; 
                }
                //session timeout
                if (obj == null || obj.getUsers() == null) {
                    String timeoutUrl = httpServletRequest.getContextPath() + "/index.html";//getTimeoutPage();
                    System.out.println(timeoutUrl);
                    httpServletResponse.sendRedirect(timeoutUrl);
                    return;
                }

            }
            /*if (obj != null) {
                if (url.contains("incoming")) {
                    obj.setSubPage("IN");
                    obj.setMainPage("todolist");
                } else if (url.contains("pending.jsf")) {
                    obj.setSubPage("PD");
                    obj.setMainPage("todolist");
                } else if (url.contains("assignmentList")) {
                    obj.setSubPage("AS");
                    obj.setMainPage("campaign");
                } else if (url.contains("openedList")) {
                    obj.setSubPage("OP");
                    obj.setMainPage("campaign");
                } else if (url.contains("followupList")) {
                    obj.setSubPage("FL");
                    obj.setMainPage("campaign");
                } else if (url.contains("pendingList")) {
                    obj.setSubPage("PDC");
                    obj.setMainPage("campaign");
                } else if (url.contains("closedList")) {
                    obj.setSubPage("CL");
                    obj.setMainPage("campaign");
                } else if (url.contains("searchCustomer")) {
                    obj.setSubPage("searchCustomer");
                    obj.setMainPage("search");
                } else if (url.contains("searchCase")) {
                    obj.setSubPage("searchCase");
                    obj.setMainPage("search");
                } else if (url.contains("searchAccount")) {
                    obj.setSubPage("searchAccount");
                    obj.setMainPage("search");
                } else if (url.contains("/report/")) {
                    obj.setSubPage("");
                    obj.setMainPage("report");
                } else if (url.contains("/admin/")) {
                    obj.setSubPage("");
                    obj.setMainPage("admin");
                } else if (url.contains("/customerHandling/")) {
                    obj.setSubPage("");
                    obj.setMainPage("customerHandling");
                } else if (url.contains("/social/")) {
                    //obj.setSubPage("");
                    obj.setMainPage("social");
                } else {
                    obj.setSubPage("");
                    obj.setMainPage("");
                }

            }*/
        }
        filterchain.doFilter(request, response);
    }

    private boolean isSessionControlRequiredForThisResource(HttpServletRequest httpServletRequest) {
        String requestPath = httpServletRequest.getRequestURI();
        boolean controlRequired = !requestPath.contains(getTimeoutPage());
        return controlRequired;
    }

    private boolean isSessionInvalid(HttpServletRequest httpServletRequest) {
        boolean sessionInvalid = (httpServletRequest.getRequestedSessionId() != null) && !httpServletRequest.isRequestedSessionIdValid();
        return sessionInvalid;
    }

    public String getTimeoutPage() {
        return timeoutPage;
    }

    public void setTimeoutPage(String timeoutPage) {
        this.timeoutPage = timeoutPage;
    }

    public void destroy() {
    }
}