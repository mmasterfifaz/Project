// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CaseAcceptanceAjax.java

package com.maxelyz.core.controller.front.todolist;

import com.maxelyz.core.model.dao.ActivityDAO;
import com.maxelyz.core.model.entity.Activity;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class CaseAcceptanceAjax extends HttpServlet
    implements Servlet
{

    public CaseAcceptanceAjax()
    {
    }

    protected void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        PrintWriter out = response.getWriter();
        String activityId = request.getParameter("activityId");
        String remark = request.getParameter("remark");
        String receiveStatus = request.getParameter("receiveStatus");
        try
        {
            ActivityDAO dao = (ActivityDAO)context.getBean("activityDAO");
            activity = dao.findActivity(new Integer(activityId));
            activity.setRemark(remark);
            activity.setReceiveStatus(receiveStatus);
            dao.edit(activity);
            out.print("Success");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            out.print("Fail");
        }
    }

    public Activity getActivity()
    {
        return activity;
    }

    public void setActivity(Activity activity)
    {
        this.activity = activity;
    }

    public ActivityDAO getActivityDAO()
    {
        return activityDAO;
    }

    public void setActivityDAO(ActivityDAO activityDAO)
    {
        this.activityDAO = activityDAO;
    }

    private Activity activity;
    private ActivityDAO activityDAO;
}
