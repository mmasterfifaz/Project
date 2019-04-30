/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.service;

import com.maxelyz.core.controller.admin.ActivityTypeController;
import com.maxelyz.core.model.dao.ContactCaseDAO;
import com.maxelyz.core.model.dao.OnlinePaymentLogDAO;
import com.maxelyz.core.model.entity.OnlinePaymentLog;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.bean.ManagedProperty;
import javax.net.ssl.SSLSocketFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author oat
 */
public class OrderServiceImpl implements OrderService {
    private static Logger log = Logger.getLogger(OrderServiceImpl.class);
    
    @ManagedProperty(value = "#{onlinePaymentLogDAO}")
    private OnlinePaymentLogDAO onlinePaymentLogDAO;
    
    @Override
    public Map<String, String> bblPost(String userName, String ip_postData, String ip_pageUrl) throws Exception {
        String strResult = "";
        URL url = new URL(ip_pageUrl);

        URLConnection con = url.openConnection();
        if (con instanceof com.sun.net.ssl.HttpsURLConnection) {
            ((com.sun.net.ssl.HttpsURLConnection)con).setSSLSocketFactory(((SSLSocketFactory)SSLSocketFactory.getDefault()));
        }
        con.setDoOutput(true);
        con.setDoInput(true);

        con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-length", String.valueOf(ip_postData.length()));
        OutputStream outStream = con.getOutputStream();
        outStream.write(ip_postData.getBytes());
        outStream.flush();

        InputStream inStream = con.getInputStream();
        while (true) {
            int c= inStream.read();
            if (c==-1) {
                break;
            }
            strResult += String.valueOf((char)c);
        }

        inStream.close();
        outStream.close();
        
        try {
            writePaymentLog(userName, ip_postData, strResult);
        } catch(Exception e) {
            log.error(e);
        }

        //put result into Map
        StringTokenizer st = new StringTokenizer(strResult,"&");
        if (!st.hasMoreTokens()) {
            throw new Exception("No Data Return");
        }

        Map<String, String> resultMap = new LinkedHashMap<String, String>();

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            String element[] = token.split("=");
            if (element.length==2) {
                resultMap.put(element[0], element[1].trim());                    
            }
        }

        return resultMap;
    } 
    
    private void writePaymentLog(String userName, String postParam, String returnParam) throws Exception {
        OnlinePaymentLog o = new OnlinePaymentLog();
        o.setCreateBy(userName);
        o.setCreateDate(new java.util.Date());
        o.setPostParam(postParam);
        o.setReturnParam(returnParam);
        onlinePaymentLogDAO.create(o);
        
    }

    public OnlinePaymentLogDAO getOnlinePaymentLogDAO() {
        return onlinePaymentLogDAO;
    }

    public void setOnlinePaymentLogDAO(OnlinePaymentLogDAO onlinePaymentLogDAO) {
        this.onlinePaymentLogDAO = onlinePaymentLogDAO;
    }
    
    
}
