
package com.maxelyz.utils;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.entity.AuditLog;
import com.maxelyz.core.service.SecurityService;
import java.io.IOException;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author oat
 */
public class LoggingFilter implements Filter {

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    private static Logger log = Logger.getLogger(LoggingFilter.class);  
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            String url = httpServletRequest.getServletPath();
            HttpSession session = httpServletRequest.getSession(false);
            WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
            SecurityService securityService = (SecurityService) springContext.getBean("securityService");
            
            if (url.contains(".jsf") && !url.contains("/a4j/") && !url.contains("javax.faces.resource")) {
                UserSession obj = null;
                Integer userId = null;
                String userName = null;
                Integer customerId = null;
                String customerName = null;
                if (session != null) {
                    obj = (UserSession) session.getAttribute("userSession"); //com.maxelyz.core.controller
                    if (obj!=null && obj.getUsers()!=null) {
                        userId = obj.getUsers().getId();
                        userName = obj.getUserName();
                        if (obj.getCustomerDetail()!=null) {
                            try {
                                customerId = obj.getCustomerDetail().getId();
                                customerName = obj.getCustomerDetail().getName()+" "+obj.getCustomerDetail().getSurname();
                            } catch(Exception e) {  }
                        }
                    }
                }
                
                AuditLog auditLog = new AuditLog();
                auditLog.setCategory(AuditLog.ACCESS);
                auditLog.setUrl(url);
                auditLog.setRemoteAddr(request.getRemoteAddr());
                auditLog.setRemoteHost(request.getRemoteHost());
                auditLog.setHttpUserAgent(httpServletRequest.getHeader("user-agent"));
                auditLog.setHttpReferer(httpServletRequest.getHeader("referer"));
                auditLog.setCustomerId(customerId);
                auditLog.setCustomerName(customerName);
                auditLog.setCreateDate(new Date());               
                auditLog.setCreateBy(userName);
                auditLog.setCreateById(userId);
                SecurityUtil.createAuditLog(auditLog);
                
                ////log.error(new Date()+" "+url+" remote addr:"+request.getRemoteAddr()+ " host:"+ request.getRemoteHost());
                
            }
        } catch (Exception e) {
           log.error(e); 
        }
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {}
        
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
            }
 
}
