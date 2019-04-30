/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.common;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.entity.Users;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Prawait
 */
public class BaseController {

    protected final Logger log = Logger.getLogger(getClass());

    /**
     * Get user information from session.
     * @return user information
     */
    protected static Users getLoginUser() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Application app = context.getApplication();
            ValueExpression vex = app.getExpressionFactory().
                    createValueExpression(context.getELContext(), "#{userSession}", UserSession.class);
            UserSession userSession = (UserSession) vex.getValue(context.getELContext());

            return userSession.getUsers();
        } catch (NullPointerException e) {
            throw e;
        }
    }

    protected static String getLoginUserName() {
        Users user = getLoginUser();
        return user.getName() + " " + user.getSurname();
    }

    /**
     * get request by key
     * @param key
     * @return
     */
    protected String getRequest(String key) {
        String value = "";
        try {
            value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
        } catch (Exception e) {
            value = null;
        }
        return value;
    }

    public boolean isPostback() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getRenderKit().getResponseStateManager().isPostback(context);
    }
}
