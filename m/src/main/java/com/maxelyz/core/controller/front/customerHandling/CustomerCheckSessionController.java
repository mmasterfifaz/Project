// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CustomerCheckSessionController.java

package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.utils.JSFUtil;
import java.io.PrintStream;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
@RequestScoped
public class CustomerCheckSessionController extends BaseController
{
    private String FAIL = "../search/searchCustomer.jsf";
    private String customer;

    @PostConstruct
    public void initialize()
    {
        try
        {
//            FacesContext context = FacesContext.getCurrentInstance();
//            HttpSession session = (HttpSession)context.getExternalContext().getSession(false);
            if(JSFUtil.getUserSession().getCustomerDetail() == null)
                JSFUtil.redirect(FAIL);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("###################").append(e).toString());
        }
    }

    public String getCustomer()
    {
        return customer;
    }
}
