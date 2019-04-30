/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.utils;

import com.maxelyz.core.model.value.admin.SessionMonitorValue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class SessionBindingListener implements HttpSessionBindingListener {

    private String loginName;


    public SessionBindingListener(String loginName) {
        this.loginName = loginName;
    }
    
    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        //System.out.println("The value bound is " + event.getName() + "value =" + loginName);
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        ServletContext application = event.getSession().getServletContext();

        if(application.getAttribute("concurrentusersgroup1") != null) {
            Map<String, SessionMonitorValue> concurrentUserMap1 = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup1");
            if(concurrentUserMap1.containsKey(loginName)) {
                synchronized (concurrentUserMap1) {
                    concurrentUserMap1.remove(loginName);       
                }
            }
        } 
        
        if(application.getAttribute("concurrentusersgroup2") != null) {
            Map<String, SessionMonitorValue> concurrentUserMap2 = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup2");
            if(concurrentUserMap2.containsKey(loginName)) {
                synchronized (concurrentUserMap2) {
                    concurrentUserMap2.remove(loginName);        
                }
            }
        } 
       
        if(application.getAttribute("concurrentusersgroup3") != null) {
            Map<String, SessionMonitorValue> concurrentUserMap3 = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup3");
            if(concurrentUserMap3.containsKey(loginName)) {
                synchronized (concurrentUserMap3) {
                    concurrentUserMap3.remove(loginName);        
                }
            }
        } 
        
        if(application.getAttribute("concurrentusersgroup4") != null) {
            Map<String, SessionMonitorValue> concurrentUserMap4 = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup4");
            if(concurrentUserMap4.containsKey(loginName)) {
                synchronized (concurrentUserMap4) {
                    concurrentUserMap4.remove(loginName);       
                }
            }
        } 
        
        if(application.getAttribute("concurrentusersgroup5") != null) {
            Map<String, SessionMonitorValue> concurrentUserMap5 = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute("concurrentusersgroup5");
            if(concurrentUserMap5.containsKey(loginName)) {
                synchronized (concurrentUserMap5) {
                    concurrentUserMap5.remove(loginName);        
                }
            }
        }
    }
}
