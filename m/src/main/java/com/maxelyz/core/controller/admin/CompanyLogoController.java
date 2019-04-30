package com.maxelyz.core.controller.admin;

import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class CompanyLogoController {
    private static Logger log = Logger.getLogger(CompanyLogoController.class);
    private static String EDIT = "companylogoedit.xhtml";
    
    @PostConstruct
    public void initialize() {      
        if (!SecurityUtil.isPermitted("admin:companylogo:view")) {    
            SecurityUtil.redirectUnauthorize();  
        }
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:companylogo:edit");
    }
    
    public String editAction() {
       return EDIT;
    }    
}
