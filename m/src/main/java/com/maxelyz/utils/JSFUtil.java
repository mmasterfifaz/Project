/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.utils;

import com.maxelyz.core.controller.Global;
import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.value.admin.SessionMonitorValue;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class JSFUtil {

    private static Logger log = Logger.getLogger(JSFUtil.class);
//    private static String SEPERATE_DIR = "\\";
    private static String SEPERATE_DIR = "/";

    public static String getRequestParameterMap(String name) {
        String parameter = null;
        Map requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (requestParameterMap != null) {
            parameter = (String) requestParameterMap.get(name);
        }
        return parameter;
    }

    public static void redirect(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException e) {
            //log.error("Cannot Redirect to " + url + ":" + e.getMessage());
        }
    }

    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public static InputStream getResourceAsStream(String path) {
        return FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(path);
    }

    public static String getRequestContextPath() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }

    public static String getRealPath() {
        return getRealPath("/");
    }

    public static String getRealPath(String path) {
        return FacesContext.getCurrentInstance().getExternalContext().getRealPath(path);
    }

    public static String getuploadPath() {
        //System.out.println(getRealPath("/upload"));
        return getRealPath("/upload")  + SEPERATE_DIR;
    }

    public static UserSession getUserSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ValueExpression vex = app.getExpressionFactory().
                createValueExpression(context.getELContext(), "#{userSession}", UserSession.class);
        UserSession u = (UserSession) vex.getValue(context.getELContext());
        return u;
    }

    public static Global getApplication() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ValueExpression vex = app.getExpressionFactory().
                createValueExpression(context.getELContext(), "#{global}", Global.class);
        Global g = (Global) vex.getValue(context.getELContext());
        return g;
    }
    
//    public static HashMap<String, String> getConcurrentUser() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
//        ServletContext application = session.getServletContext();
//        HashMap<String, String> concurrentUser = (HashMap<String, String>) application.getAttribute("concurrentusers");
//        return concurrentUser;
//    }
    
    public static Map<String, SessionMonitorValue> getConcurrentUser(String concurrentGroup) {
        Map<String, SessionMonitorValue> concurrentUser = null;
        
        FacesContext context = FacesContext.getCurrentInstance();
        if(context != null) {
            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
            if(session != null) {
                ServletContext application = session.getServletContext();
                if(application != null) {

                    if(concurrentGroup.equals("1"))
                        concurrentUser = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup1");
                    else if(concurrentGroup.equals("2"))
                        concurrentUser = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup2");
                    else if(concurrentGroup.equals("3"))
                        concurrentUser = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup3");
                    else if(concurrentGroup.equals("4"))
                        concurrentUser = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup4");
                    else if(concurrentGroup.equals("5"))
                        concurrentUser = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup5");
                
                }
            }
        }
        return concurrentUser;
    }    
    
    public static int getCntConcurrentUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        ServletContext application = session.getServletContext();
         
        int cnt = 0;
        if(application.getAttribute("concurrentusersgroup1") != null) {
            cnt = cnt + ((ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup1")).size();
        }
        if(application.getAttribute("concurrentusersgroup2") != null) {
            cnt = cnt + ((ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup2")).size();
        }
        if(application.getAttribute("concurrentusersgroup3") != null) {
            cnt = cnt + ((ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup3")).size();
        }
        if(application.getAttribute("concurrentusersgroup4") != null) {
            cnt = cnt + ((ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup4")).size();
        }
        if(application.getAttribute("concurrentusersgroup5") != null) {
            cnt = cnt + ((ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup5")).size();
        }
        
        return cnt;
    }    
    
    public static HttpSession getSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        return (HttpSession) context.getExternalContext().getSession(false);
    }
    
        public static HttpSession getNewSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        return (HttpSession) context.getExternalContext().getSession(true);
    }
    
    public static void setConcurrentUser(Map<String, SessionMonitorValue> concurrentUser) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        ServletContext application = session.getServletContext();
        application.setAttribute("concurrentusers",concurrentUser);
    }
    
    public static void setConcurrentUserGroup(Map<String, SessionMonitorValue> concurrentUser, String concurrentGroup) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        ServletContext application = session.getServletContext();
        
        if(concurrentGroup.equals("1"))
            application.setAttribute("concurrentusersgroup1",concurrentUser);
        else  if(concurrentGroup.equals("2"))
            application.setAttribute("concurrentusersgroup2",concurrentUser);
        else  if(concurrentGroup.equals("3"))
            application.setAttribute("concurrentusersgroup3",concurrentUser);
        else  if(concurrentGroup.equals("4"))
            application.setAttribute("concurrentusersgroup4",concurrentUser);
        else  if(concurrentGroup.equals("5"))
            application.setAttribute("concurrentusersgroup5",concurrentUser);
    }

    public static String getBundleValue(String key) {
        ResourceBundle bundle;
        FacesContext context = FacesContext.getCurrentInstance();
        bundle = context.getApplication().getResourceBundle(context, "msg");
        String result = null;

        try {
            result = bundle.getString(key);
        } catch (MissingResourceException e) {
            return null;
        }
        return result;
    }

    public static Date toDateWithoutTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date d = c.getTime();   
        return d;
    }

    public static Date toDateWithMaxTime(Date toDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(toDate);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date d = cal.getTime();
        return d;
    }

    public static String getDateFormat1(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS",Locale.US);
        return dateFormat.format(date);
    }

    public static ServletContext getServletContext() {
        return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    }
    
    public static String convertSecondToHHMMSS(Integer secs){
        int hours = secs / 3600;
        int remainder = secs % 3600;
        int minutes = remainder / 60;
        int seconds = remainder % 60; 

        String disHour = (hours < 10 ? "0" : "") + hours;
        String disMinu = (minutes < 10 ? "0" : "") + minutes;
        String disSec = (seconds < 10 ? "0" : "") + seconds;

        return (disHour + ":" + disMinu + ":" + disSec);
    }
    
    public static String removeEnter(String text) {
        return text.replace("\r\n", " ").replace("\n", " ");
    }
}
