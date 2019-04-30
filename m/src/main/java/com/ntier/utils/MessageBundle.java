/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ntier.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author Manop
 */
public class MessageBundle {

    protected static ClassLoader getCurrentClassLoader(Object defaultObject){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        if (loader == null){
            loader = defaultObject.getClass().getClassLoader();
        }

        return loader;
    }

    public static String getMessageReourceBundle(
            String bundleName,
            String key,
            Object[] params,
            Locale locale){

        String text = null;

        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, getCurrentClassLoader(params));

        try{
            text = bundle.getString(key);
        }catch(MissingResourceException e){
            text = "?? key: " + key + " not found ??";
        }

        if (params != null){
            MessageFormat mf = new MessageFormat(text, locale);
            text = mf.format(params, new StringBuffer(), null).toString();
        }

        return text;
    }

}
