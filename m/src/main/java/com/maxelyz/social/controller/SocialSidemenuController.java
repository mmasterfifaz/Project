/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller;

import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class SocialSidemenuController {
    @PostConstruct
    public void initialize() {
        JSFUtil.getUserSession().setCustomerDetail(null);
    }

    public boolean isShowMenuLeft(){
//        return isCustomerServicesPermitted() || isCampaignSalesPermitted();
        return true;
    }

    public boolean isShowMenuRight(){
//        return isSLAPermitted() || isInformationProvidingPermitted();
        return true;
    }

    public boolean isSocialTodolistPermitted() {
        return SecurityUtil.isPermitted("social:todolist:view");
    }
    
    public boolean isSocialSignaturePermitted() {
        return SecurityUtil.isPermitted("social:signature:view");
    }

    public boolean isSocialPublisherPermitted() {
        return SecurityUtil.isPermitted("social:publisher:view");
    }

    public boolean isSocialMonitorPermitted() {
        return SecurityUtil.isPermitted("social:monitor:view");
    }

    public boolean isSocialDashboardPermitted() {
        return SecurityUtil.isPermitted("social:dashboard:view");
    }

    public boolean isSocialReportPermitted() {
        return SecurityUtil.isPermitted("social:report:view");
    }

}

