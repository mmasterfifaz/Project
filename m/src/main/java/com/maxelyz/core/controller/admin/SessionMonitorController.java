package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.ActivityType;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import com.maxelyz.core.model.value.admin.SessionMonitorValue;
import com.maxelyz.utils.SessionBindingListener;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.bean.ViewScoped;

@ManagedBean
@RequestScoped
@ViewScoped
public class SessionMonitorController implements Serializable{
    private static Logger log = Logger.getLogger(SessionMonitorController.class);
    private static String REFRESH = null;

    List<SessionMonitorValue> concurrentUser1 = new ArrayList<SessionMonitorValue>();
    List<SessionMonitorValue> concurrentUser2 = new ArrayList<SessionMonitorValue>();
    List<SessionMonitorValue> concurrentUser3 = new ArrayList<SessionMonitorValue>();
    List<SessionMonitorValue> concurrentUser4 = new ArrayList<SessionMonitorValue>();
    List<SessionMonitorValue> concurrentUser5 = new ArrayList<SessionMonitorValue>();
    int totalSession = 0;
    private String chkShow="";
    
    List<SessionMonitorValue> sessionValueList = new ArrayList<SessionMonitorValue>();  //For Admin Menu

    @PostConstruct
    public void initialize() {
        chkShow = (String) JSFUtil.getRequestParameterMap("admin");
        totalSession = 0;
        sessionValueList.clear();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        ServletContext application = session.getServletContext();
        
        if(application.getAttribute("concurrentusersgroup1") != null) {
            concurrentUser1.clear();
            Map<String, SessionMonitorValue> concurrentUserMap = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup1");
            for (Map.Entry<String, SessionMonitorValue> entry : concurrentUserMap.entrySet()) {
                SessionMonitorValue sessionUser = new SessionMonitorValue(entry.getValue().getUser(), entry.getValue().getLoginTime(), entry.getValue().getRemoteAddr(), entry.getValue().getSessionId(), entry.getValue().getAgentStatus(), entry.getKey());
                concurrentUser1.add(sessionUser); 
                totalSession++;
                sessionValueList.add(sessionUser);
            }
        } 
        
        if(application.getAttribute("concurrentusersgroup2") != null) {
            concurrentUser2.clear();
            Map<String, SessionMonitorValue> concurrentUserMap = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup2");
            for (Map.Entry<String, SessionMonitorValue> entry : concurrentUserMap.entrySet()) {
                SessionMonitorValue sessionUser = new SessionMonitorValue(entry.getValue().getUser(), entry.getValue().getLoginTime(), entry.getValue().getRemoteAddr(), entry.getValue().getSessionId(), entry.getValue().getAgentStatus(), entry.getKey());
                concurrentUser2.add(sessionUser); 
                totalSession++;
                sessionValueList.add(sessionUser);
            }
        } 
        
        if(application.getAttribute("concurrentusersgroup3") != null) {
            concurrentUser3.clear();
            Map<String, SessionMonitorValue> concurrentUserMap = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup3");
            for (Map.Entry<String, SessionMonitorValue> entry : concurrentUserMap.entrySet()) {
                SessionMonitorValue sessionUser = new SessionMonitorValue(entry.getValue().getUser(), entry.getValue().getLoginTime(), entry.getValue().getRemoteAddr(), entry.getValue().getSessionId(), entry.getValue().getAgentStatus(), entry.getKey());
                concurrentUser3.add(sessionUser); 
                totalSession++;
                sessionValueList.add(sessionUser);
            }
        } 
        
        if(application.getAttribute("concurrentusersgroup4") != null) {
            concurrentUser4.clear();
            Map<String, SessionMonitorValue> concurrentUserMap = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup4");
            for (Map.Entry<String, SessionMonitorValue> entry : concurrentUserMap.entrySet()) {
                SessionMonitorValue sessionUser = new SessionMonitorValue(entry.getValue().getUser(), entry.getValue().getLoginTime(), entry.getValue().getRemoteAddr(), entry.getValue().getSessionId(), entry.getValue().getAgentStatus(), entry.getKey());
                concurrentUser4.add(sessionUser); 
                totalSession++;
                sessionValueList.add(sessionUser);
            }
        } 
        
        if(application.getAttribute("concurrentusersgroup5") != null) {
            concurrentUser5.clear();
            Map<String, SessionMonitorValue> concurrentUserMap = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup5");
            for (Map.Entry<String, SessionMonitorValue> entry : concurrentUserMap.entrySet()) {
                SessionMonitorValue sessionUser = new SessionMonitorValue(entry.getValue().getUser(), entry.getValue().getLoginTime(), entry.getValue().getRemoteAddr(), entry.getValue().getSessionId(), entry.getValue().getAgentStatus(), entry.getKey());
                concurrentUser5.add(sessionUser); 
                totalSession++;
                sessionValueList.add(sessionUser);
            }
        } 
        
    }
    
    public void removeSessionGroup1() {
        String sessionId = (String) JSFUtil.getRequestParameterMap("sessionValue");
        ServletContext application = JSFUtil.getServletContext();
        if(application.getAttribute("concurrentusersgroup1") != null) {
            Map<String, SessionMonitorValue> concurrentUserMap1 = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup1");
            if(concurrentUserMap1.containsKey(sessionId)) {
                synchronized (concurrentUserMap1) {
                    concurrentUserMap1.remove(sessionId);       
                }
            }
        } 
        initialize();
    }
    
    public void removeSessionGroup2() {
        String sessionId = (String) JSFUtil.getRequestParameterMap("sessionValue");
        ServletContext application = JSFUtil.getServletContext();
        if(application.getAttribute("concurrentusersgroup2") != null) {
            Map<String, SessionMonitorValue> concurrentUserMap2 = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup2");
            if(concurrentUserMap2.containsKey(sessionId)) {
                synchronized (concurrentUserMap2) {
                    concurrentUserMap2.remove(sessionId);       
                }
            }
        } 
        initialize();
    }
    
    public void removeSessionGroup3() {
        String sessionId = (String) JSFUtil.getRequestParameterMap("sessionValue");
        ServletContext application = JSFUtil.getServletContext();
        if(application.getAttribute("concurrentusersgroup3") != null) {
            Map<String, SessionMonitorValue> concurrentUserMap3 = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup3");
            if(concurrentUserMap3.containsKey(sessionId)) {
                synchronized (concurrentUserMap3) {
                    concurrentUserMap3.remove(sessionId);       
                }
            }
        } 
        initialize();
    }
        
    public void removeSessionGroup4() {
        String sessionId = (String) JSFUtil.getRequestParameterMap("sessionValue");
        ServletContext application = JSFUtil.getServletContext();
        if(application.getAttribute("concurrentusersgroup4") != null) {
            Map<String, SessionMonitorValue> concurrentUserMap4 = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup4");
            if(concurrentUserMap4.containsKey(sessionId)) {
                synchronized (concurrentUserMap4) {
                    concurrentUserMap4.remove(sessionId);       
                }
            }
        } 
        initialize();
    }
            
    public void removeSessionGroup5() {
        String sessionId = (String) JSFUtil.getRequestParameterMap("sessionValue");
        ServletContext application = JSFUtil.getServletContext();
        if(application.getAttribute("concurrentusersgroup5") != null) {
            Map<String, SessionMonitorValue> concurrentUserMap5 = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup5");
            if(concurrentUserMap5.containsKey(sessionId)) {
                synchronized (concurrentUserMap5) {
                    concurrentUserMap5.remove(sessionId);       
                }
            }
        } 
        initialize();
    }
    
//        if(application.getAttribute("concurrentusersgroup2") != null) {
//            HashMap<String, SessionMonitorValue> concurrentUserMap2 = (HashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup2");
//            if(concurrentUserMap2.containsKey(sessionId)) {
//                System.out.println(concurrentUserMap2+"-"+sessionId);
//                synchronized (concurrentUserMap2) {
//                    concurrentUserMap2.remove(sessionId);        
//                }
//            }
//        } 
//       
//        if(application.getAttribute("concurrentusersgroup3") != null) {
//            HashMap<String, SessionMonitorValue> concurrentUserMap3 = (HashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup3");
//            if(concurrentUserMap3.containsKey(sessionId)) {
//                System.out.println(concurrentUserMap3+"-"+sessionId);
//                synchronized (concurrentUserMap3) {
//                    concurrentUserMap3.remove(sessionId);        
//                }
//            }
//        } 
//        
//        if(application.getAttribute("concurrentusersgroup4") != null) {
//            HashMap<String, SessionMonitorValue> concurrentUserMap4 = (HashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup4");
//            if(concurrentUserMap4.containsKey(sessionId)) {
//                System.out.println(concurrentUserMap4+"-"+sessionId);
//                synchronized (concurrentUserMap4) {
//                    concurrentUserMap4.remove(sessionId);       
//                }
//            }
//        } 
//        
//        if(application.getAttribute("concurrentusersgroup5") != null) {
//            HashMap<String, SessionMonitorValue> concurrentUserMap5 = (HashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup5");
//            if(concurrentUserMap5.containsKey(sessionId)) {
//                System.out.println(concurrentUserMap5+"-"+sessionId);
//                synchronized (concurrentUserMap5) {
//                    concurrentUserMap5.remove(sessionId);        
//                }
//            }
//        }


    public List<SessionMonitorValue> getConcurrentUser1() {
        return concurrentUser1;
    }

    public List<SessionMonitorValue> getConcurrentUser2() {
        return concurrentUser2;
    }

    public List<SessionMonitorValue> getConcurrentUser3() {
        return concurrentUser3;
    }

    public List<SessionMonitorValue> getConcurrentUser4() {
        return concurrentUser4;
    }

    public List<SessionMonitorValue> getConcurrentUser5() {
        return concurrentUser5;
    }
    
    public int getTotalSession() {
        return totalSession;
    }

    public String getChkShow() {
        return chkShow;
    }

    public void setChkShow(String chkShow) {
        this.chkShow = chkShow;
    }

    public List<SessionMonitorValue> getSessionValueList() {
        return sessionValueList;
    }

    public void setSessionValueList(List<SessionMonitorValue> sessionValueList) {
        this.sessionValueList = sessionValueList;
    }
    
    
    
}
