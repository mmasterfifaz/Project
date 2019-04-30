// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UTF8Filter.java

package com.ntier.utils;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UTF8Filter
    implements Filter
{

    public UTF8Filter()
    {
    }

    public void init(FilterConfig filterconfig)
        throws ServletException
    {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        httpRequest.setCharacterEncoding("UTF-8");
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        httpResponse.setCharacterEncoding("UTF-8");
        if(chain != null)
            chain.doFilter(request, response);
    }

    public void destroy()
    {
    }
}
