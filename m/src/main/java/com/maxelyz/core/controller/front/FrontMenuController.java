/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front;

import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

/**
 *
 * @author admin
 */
@ManagedBean
@RequestScoped
public class FrontMenuController {

    @PostConstruct
    public void initialize() {
    }

    public void checkSearchPermitted() {
        JSFUtil.getUserSession().setCustomerDetail(null);
        JSFUtil.getUserSession().setFirstPage("search");
        JSFUtil.getUserSession().setMainPage("search");
        if (SecurityUtil.isPermitted("searchcustomer:view")) {
            JSFUtil.redirect(JSFUtil.getServletContext().getContextPath() + "/front/search/searchCustomer.jsf");
        } else if (SecurityUtil.isPermitted("searchcase:view")) {
            JSFUtil.redirect(JSFUtil.getServletContext().getContextPath() + "/front/search/searchCase.jsf");
        } else if (SecurityUtil.isPermitted("searchaccount:view")) {
            JSFUtil.redirect(JSFUtil.getServletContext().getContextPath() + "/front/search/searchAccount.jsf");
        } else if (SecurityUtil.isPermitted("searchmessage:view")) {
            JSFUtil.redirect(JSFUtil.getServletContext().getContextPath() + "/front/search/searchMessage.jsf");
        }
    }

    //Main Menu
    //Campaign
    public boolean isCampaignViewPermitted() {
        return SecurityUtil.isPermitted("campaign:view");
    }
    //To Do List

    public boolean isToDoListViewPermitted() {
        return SecurityUtil.isPermitted("todolist:view");
    }
    //Search

    public boolean isSearchViewPermitted() {
        return SecurityUtil.isPermitted("search:view");
    }

    public boolean isSearchCustomerViewPermitted() {
        return SecurityUtil.isPermitted("searchcustomer:view");
    }

    public boolean isSearchCustomerAddPermitted() {
        return SecurityUtil.isPermitted("searchcustomer:add");
    }

    public boolean isSearchCaseViewPermitted() {
        return SecurityUtil.isPermitted("searchcase:view");
    }

    public boolean isSearchAccountViewPermitted() {
        return SecurityUtil.isPermitted("searchaccount:view");
    }

    public boolean isSearchAccountAddPermitted() {
        return SecurityUtil.isPermitted("searchaccount:add");
    }

    public boolean isSearchAccountEditPermitted() {
        return SecurityUtil.isPermitted("searchaccount:edit");
    }

    public boolean isSearchAccountDeletePermitted() {
        return SecurityUtil.isPermitted("searchaccount:delete");
    }

    public boolean isReportViewPermitted() {
        return SecurityUtil.isPermitted("report:view");
    }

    public boolean isAdminViewPermitted() {
        return SecurityUtil.isPermitted("admin:view");
    }

    public boolean isCustomerHandlingViewPermitted() {
        return SecurityUtil.isPermitted("customerhandling:view");
    }

    public boolean isCustomerProfileViewPermitted() {
        return SecurityUtil.isPermitted("customerprofile:view");
    }

    public boolean isCustomerProfileAddPermitted() {
        return SecurityUtil.isPermitted("customerprofile:add");
    }

    public boolean isCustomerProfileEditPermitted() {
        return SecurityUtil.isPermitted("customerprofile:edit");
    }

    public boolean isCaseViewPermitted() {
        return SecurityUtil.isPermitted("case:view");
    }

    public boolean isCaseAddPermitted() {
        return SecurityUtil.isPermitted("case:add");
    }

    public boolean isCaseEditPermitted() {
        return SecurityUtil.isPermitted("case:edit");
    }

    public boolean isActivityAddPermitted() {
        return SecurityUtil.isPermitted("activity:add");
    }

    public boolean isCaseEditAttachmentPermitted() {
        return SecurityUtil.isPermitted("case:edit:attachment");
    }

    public boolean isCaseEditCasetypePermitted() {
        return SecurityUtil.isPermitted("case:edit:casetype");
    }

    public boolean isCaseEditServicetypePermitted() {
        return SecurityUtil.isPermitted("case:edit:servicetype");
    }

    public boolean isCaseEditBusinessUnitPermitted() {
        return SecurityUtil.isPermitted("case:edit:businessunit");
    }

    public boolean isCaseEditContactpersonPermitted() {
        return SecurityUtil.isPermitted("case:edit:contactperson");
    }

    public boolean isCaseEditChannelPermitted() {
        return SecurityUtil.isPermitted("case:edit:channel");
    }

    public boolean isCaseEditRelatedinfoPermitted() {
        return SecurityUtil.isPermitted("case:edit:relatedinfo");
    }

    public boolean isCaseEditContactdatePermitted() {
        return SecurityUtil.isPermitted("case:edit:contactdate");
    }

    public boolean isCaseEditLocationPermitted() {
        return SecurityUtil.isPermitted("case:edit:location");
    }

    public boolean isCaseEditDescriptionPermitted() {
        return SecurityUtil.isPermitted("case:edit:description");
    }

    public boolean isCaseEditRemarkPermitted() {
        return SecurityUtil.isPermitted("case:edit:remark");
    }

    public boolean isCaseEditRelatedcasePermitted() {
        return SecurityUtil.isPermitted("case:edit:relatedcase");
    }

    public boolean isCaseEditPriorityPermitted() {
        return SecurityUtil.isPermitted("case:edit:priority");
    }

    public boolean isCaseEditStatusPermitted() {
        return SecurityUtil.isPermitted("case:edit:status");
    }

    public boolean isCasehistoryViewPermitted() {
        return SecurityUtil.isPermitted("casehistory:view");
    }

    public boolean isSaleapproachingViewPermitted() {
        return SecurityUtil.isPermitted("saleapproaching:view");
    }

    public boolean isSalehistoryViewPermitted() {
        return SecurityUtil.isPermitted("salehistory:view");
    }

    public boolean isShoppingcartViewPermitted() {
        return SecurityUtil.isPermitted("shoppingcart:view");
    }
     
    public boolean isAuditLogViewPermitted() {
        return SecurityUtil.isPermitted("auditlog:view");
    }

    public boolean isContactHistoryViewPermitted() {
        return SecurityUtil.isPermitted("contacthistory:view");
    }
    
    //Customer Historical View
    public boolean isCustomerHistoryViewPermitted() {
        return SecurityUtil.isPermitted("customerhistorical:view");
    }

    public boolean isCustomerHandlingReportViewPermitted() {
        return SecurityUtil.isPermitted("customerhandlingreport:view");
    }

    public boolean isCaseListByCustomerReportViewPermitted() {
        return SecurityUtil.isPermitted("caselistbycustomerreport:view");
    }

    public boolean isTelephoneLogViewPermitted() {
        return SecurityUtil.isPermitted("telephonelog:view");
    }

    public boolean isSocialToDoListViewPermitted() {
        return SecurityUtil.isPermitted("social:todolist:view");
    }
}
