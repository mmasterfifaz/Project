/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller;

import com.maxelyz.core.controller.front.customerHandling.ContactSummaryController;
import com.maxelyz.core.controller.front.search.SearchCustomerController;
import com.maxelyz.utils.JSFUtil;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

@ManagedBean
//@RequestScoped
@ViewScoped
public class HelperBean {
    private boolean changePasswordPopup=false;
    private boolean myScriptPopup=false;

    private boolean productPopup=false;
    private boolean objectionPopup=false;
    private boolean knowledgePopup=false;

    private boolean contactSummaryPopup=false;



    public boolean isPostback() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getRenderKit().getResponseStateManager().isPostback(context);
    }

    public boolean isChangePasswordPopup() {
        return changePasswordPopup;
    }

    public boolean isMyScriptPopup() {
        return myScriptPopup;
    }

    public boolean isProductPopup() {
        return productPopup;
    }

    public boolean isKnowledgePopup() {
        return knowledgePopup;
    }

    public boolean isObjectionPopup() {
        return objectionPopup;
    }

    public boolean isContactSummaryPopup() {
        return contactSummaryPopup;
    }

//    if (SecurityUtil.isPermitted("todolist:view") && SecurityUtil.isPermitted("campaign:view")) {
//            String page = "";
//            if (JSFUtil.getUserSession().getFirstPage().equals("todolist")) {
//                page = "/front/todolist/incoming.jsf?faces-redirect=true";
//            }else if (JSFUtil.getUserSession().getFirstPage().equals("campaign")) {
//                page = "/campaign/assignmentList.jsf?faces-redirect=true";
//            }else if (JSFUtil.getUserSession().getFirstPage().equals("search")) {
//                this.getSearchCustomerController().searchCustomer();
//                page = "/front/search/searchCustomer.jsf?faces-redirect=true";
//            }else{
//                page = "/front/todolist/incoming.jsf?faces-redirect=true";
//            }
//            JSFUtil.redirect(JSFUtil.getRequestContextPath() + page);
//        }else if (SecurityUtil.isPermitted("todolist:view")) {
//            JSFUtil.redirect(JSFUtil.getRequestContextPath() + SUCCESS1);
//        }else if (SecurityUtil.isPermitted("campaign:view")) {
//            JSFUtil.redirect(JSFUtil.getRequestContextPath() + SUCCESS_TS_ASSIGNMENTLIST1);
//        }else{
//            JSFUtil.redirect(JSFUtil.getRequestContextPath() + SUCCESS1);
//        }

    public String checkPreviousPage() {
        String page = null;
        if (JSFUtil.getUserSession().getFirstPage().equals("todolist")) {
            page = "/front/todolist/incoming.xhtml?faces-redirect=true";
        } else if (JSFUtil.getUserSession().getFirstPage().equals("campaign")) {
                if(JSFUtil.getUserSession().getFromTab().equals("assignmentList"))
            page = "/campaign/assignmentList.xhtml?faces-redirect=true";
                else if(JSFUtil.getUserSession().getFromTab().equals("openedList"))
                    page = "/campaign/openedList.xhtml?faces-redirect=true";
                else if(JSFUtil.getUserSession().getFromTab().equals("followupList"))
                    page = "/campaign/followupList.xhtml?faces-redirect=true";
                else if(JSFUtil.getUserSession().getFromTab().equals("pendingList"))
                    page = "/campaign/pendingList.xhtml?faces-redirect=true";
                else if(JSFUtil.getUserSession().getFromTab().equals("closedList"))
                    page = "/campaign/closedList.xhtml?faces-redirect=true";
        } else if (JSFUtil.getUserSession().getFirstPage().equals("search")) {
            this.getSearchCustomerController().searchCustomer();
            page = "/front/search/searchCustomer.xhtml?faces-redirect=true";
        } else if(JSFUtil.getUserSession().getFirstPage().equals("admin"))  {
            page = "/admin/contactrecord.xhtml?faces-redirect=true";
        } else {
            page = "/front/todolist/incoming.xhtml?faces-redirect=true";
        }
        return page;
    }
    
    public String endContactAction() {
        String page = null; //null mean... still on page
        if (!JSFUtil.getUserSession().isShowContactSummary()) {
            page = checkPreviousPage();
            JSFUtil.getUserSession().removeCustomerSession();
        }
        return page;
    }
    
    public SearchCustomerController getSearchCustomerController() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ValueExpression vex = app.getExpressionFactory().
                createValueExpression(context.getELContext(), "#{searchCustomerController}", SearchCustomerController.class);
        SearchCustomerController g = (SearchCustomerController) vex.getValue(context.getELContext());
        return g;
    }
    
    //Listener
    public void changePasswordPopupListener(ActionEvent event) {
        changePasswordPopup = true;
    }

    public void myScriptPopupListener(ActionEvent event) {
        myScriptPopup = true;
    }

    public void productPopupListener(ActionEvent event) {
        JSFUtil.getUserSession().showInboundContactSummary();
        productPopup = true;
    }

    public void objectionPopupListener(ActionEvent event) {
        objectionPopup = true;
    }

    public void knowledgePopupListener(ActionEvent event) {
        JSFUtil.getUserSession().showInboundContactSummary();
        knowledgePopup = true;
    }

    public ContactSummaryController getContactSummaryController() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ValueExpression vex = app.getExpressionFactory().
                createValueExpression(context.getELContext(), "#{contactSummaryController}", ContactSummaryController.class);
        ContactSummaryController g = (ContactSummaryController) vex.getValue(context.getELContext());
        return g;
    }

    public void contactSummaryPopupListener(ActionEvent event) {
        if(JSFUtil.getUserSession().isInboundCall()) { 
            if(JSFUtil.getUserSession().getUserGroup().getRole().contains("Agent")) {
                JSFUtil.getUserSession().setShowContactSummary(true);
                contactSummaryPopup = true;
                getContactSummaryController().initContactSummary();
            } 
            if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CSCounter")) {      
                if(JSFUtil.getUserSession().getHasContactCases() || JSFUtil.getUserSession().getHasKnowledgebases() || !JSFUtil.getUserSession().isHangupShow()) {
                    JSFUtil.getUserSession().setDialClicked(true);
                    JSFUtil.getUserSession().setClickNextCall(false);
                    JSFUtil.getUserSession().setShowContactSummary(true);
                    contactSummaryPopup = true;
                } else {
                    JSFUtil.getUserSession().setShowContactSummary(false);
                    contactSummaryPopup = false;
                }
            }
        } else {    //OUTBOUND
            getContactSummaryController().initContactSummary();
            contactSummaryPopup = true;
        }
    }
    
//    public void contactSummaryPopupListener(ActionEvent event) {
//        if(JSFUtil.getUserSession().isInboundCall()) {
//            if(JSFUtil.getUserSession().getUserGroup().getRole().contains("Agent")) {
//                JSFUtil.getUserSession().setShowContactSummary(true);
//                contactSummaryPopup = true;
//                getContactSummaryController().initContactSummary();
//            } else if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CSCounter")) {                
//                    JSFUtil.getUserSession().setShowContactSummary(false);
//                    if(JSFUtil.getUserSession().getHasContactCases() || JSFUtil.getUserSession().getHasKnowledgebases()){
//                        JSFUtil.getUserSession().setShowContactSummary(true);
//                        contactSummaryPopup = true;
//                    } 
//            }
//        } else {
//            getContactSummaryController().initContactSummary();
//            contactSummaryPopup = true;
//        }
//    }
    

}
