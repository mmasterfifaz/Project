/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.utils;

import com.maxelyz.core.model.entity.AuditLog;
import com.maxelyz.core.model.entity.UserGroupLocation;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.service.SecurityService;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    private static Logger log = Logger.getLogger(SecurityUtil.class);
    private static SecurityService securityService;
        
    public static boolean isPermitted(String permission) {
        boolean result=false;
        try {
            Users u = JSFUtil.getUserSession().getUsers();
            result = securityService.isPermitted(u,permission);  
        } catch(Exception e) {
            log.error(e);
        }
        return result;
    }
    
    public static void createAuditLog(AuditLog auditLog ) {
        try {
            securityService.createAuditLog(auditLog);
        } catch(Exception e) {
            log.error(e);
        }
    }

    public static void redirectUnauthorize() {
        JSFUtil.redirect(JSFUtil.getServletContext().getContextPath()+"/unauthorization.jsf");
    }
    
    @Autowired(required = true)
    public void setSecurityService(SecurityService securityService) {
        SecurityUtil.securityService = securityService;
    }    
        
    public static boolean isViewLocation(Integer serviceTypeId, Integer businessUnitId, Integer locationId) {
        boolean result=false;
        try {
            List<UserGroupLocation> uglList = JSFUtil.getUserSession().getUserGroupLocationList();
            result = securityService.isViewLocation(uglList, serviceTypeId, businessUnitId, locationId);  
        } catch(Exception e) {
            log.error(e);
        }
        return result;
    }
    
    
    
    public static DirContext getRootDirContext(String url, String managerDN, String managerPassword) throws NamingException {
        Hashtable env = new Hashtable();
        
        env.put(Context.SECURITY_PRINCIPAL, managerDN); 
        env.put(Context.SECURITY_CREDENTIALS, managerPassword); 
        DirContext initial = new InitialDirContext(env);
        Object obj = initial.lookup(url); 
        if (!(obj instanceof DirContext)) 
            throw new NamingException("No directory context"); 
        return (DirContext) obj;
    }
}
