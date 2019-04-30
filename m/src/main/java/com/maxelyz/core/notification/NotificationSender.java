/*
 * Comet4J Copyright(c) 2011, http://code.google.com/p/comet4j/ This code is
 * licensed under BSD license. Use it as you wish, but keep this copyright
 * intact.
 */
package com.maxelyz.core.notification;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.entity.Notification;
import com.maxelyz.core.notification.dto.FollowupDTO;
import com.maxelyz.core.notification.dto.NotificationDTO;
import com.maxelyz.core.notification.dto.UserDTO;
import com.maxelyz.utils.JSFUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.comet4j.core.CometConnection;
import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;

public class NotificationSender implements Runnable {

    private static Logger log = Logger.getLogger(NotificationSender.class);
    private static final CometEngine engine = CometContext.getInstance().getEngine();
    private DataSource datasource;
    
    public NotificationSender(DataSource ds){
        this.datasource = ds;
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(5000);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            this.sendCountMsg();

        }

    }
    
    private void sendCountMsg(){
        try {
            Map<String, String> map1 = AppStore.getInstance().getMap();
            Iterator<Map.Entry<String, String>> iter1 = map1.entrySet().iterator();

            Map<Integer, Integer> list = null;
            if (iter1.hasNext()) {
//                Iterator<Map.Entry<String, String>> iter2 = map1.entrySet().iterator();
//                while (iter2.hasNext()) {
//
//                    Map.Entry<String, String> entry2 = iter2.next();
//                    String id2 = entry2.getKey();
//                    Integer userId2 = Integer.parseInt(entry2.getValue().trim());
//
//                    NotificationDTO noti2 = new NotificationDTO();
//                    noti2.setId(id2);
//                    noti2.setUserId(userId2);
//                    noti2.setCount(0);
//                    engine.sendTo(Constant.APP_CHANNEL, engine.getConnection(id2), noti2);
//                }
                
                list = getListCount();
                //if(list != null && !list.isEmpty()){
                    //Map<String, String> map = AppStore.getInstance().getMap();
                    Iterator<Map.Entry<String, String>> iter = map1.entrySet().iterator();
                    while (iter.hasNext()) {
                        Boolean exist = false;
                        Map.Entry<String, String> entry = iter.next();
                        String id = entry.getKey();
                        Integer userId = Integer.parseInt(entry.getValue().trim());

                        for (Map.Entry<Integer, Integer> item : list.entrySet()) {
                            if (item.getKey().intValue() == userId.intValue()) {
                                exist = true;
                                NotificationDTO noti = new NotificationDTO();
                                noti.setId(id);
                                noti.setUserId(userId);
                                noti.setCount(item.getValue());
                                engine.sendTo(Constant.APP_CHANNEL, engine.getConnection(id), noti);

                                break;
                            }
                        }
                        if(!exist){
                            NotificationDTO noti = new NotificationDTO();
                            noti.setId(id);
                            noti.setUserId(userId);
                            noti.setCount(0);
                            engine.sendTo(Constant.APP_CHANNEL, engine.getConnection(id), noti);
                        }

                    }
                //}
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private synchronized Connection getConnection() throws SQLException, NamingException {

//        Context initContext = new InitialContext();
//        Context envContext = (Context) initContext.lookup("java:/comp/env");
//        DataSource ds = (DataSource) envContext.lookup("jdbc/MaxarDB");

        return datasource.getConnection();	// Allocate and use a connection from the pool
    }

    private Map<Integer, Integer> getListCount() throws ServletException, IOException {
        //this.init();
        Map<Integer, Integer> values = new ConcurrentSkipListMap<Integer, Integer>();
        Map<Integer, Integer> values1 = new ConcurrentSkipListMap<Integer, Integer>();
        
        Map<Integer, Integer> val1 = null;
        Map<Integer, Integer> val2 = null;
        
        Map<Integer, Integer> list = new ConcurrentSkipListMap<Integer, Integer>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "select noti.to_user_id as userId, COUNT(noti.id) as c from notification noti"
                    + " left join assignment_detail ad on ad.id = noti.assignment_detail_id "
                    + " where noti.enable = 1 and noti.status = 1"
                    + " and noti.type <> 'followup'"
                    + " group by noti.to_user_id"
                    + " having COUNT(noti.id) > 0"
                    + " order by noti.to_user_id";

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                values.put(rs.getInt("userId"), rs.getInt("c"));
            }
            
            sql = "select noti.to_user_id as userId, COUNT(noti.id) as c from notification noti"
                    + " left join assignment_detail ad on ad.id = noti.assignment_detail_id "
                    + " where noti.enable = 1 and noti.status = 1"
                    + " and noti.type = 'followup'"
                    + " and CONVERT(VARCHAR(10), getdate(), 120) >= CONVERT(VARCHAR(10), noti.followup_date, 120)"
                    + " group by noti.to_user_id"
                    + " having COUNT(noti.id) > 0"
                    + " order by noti.to_user_id";

            //conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                values1.put(rs.getInt("userId"), rs.getInt("c"));
            }
            
            if(values.size() >= values1.size()){
                val1 = values;
                val2 = values1;
            }else{
                val1 = values1;
                val2 = values;
            }
            
            
            int c = 0;
            int c1 = 0;
            for (Map.Entry<Integer, Integer> item1 : val1.entrySet()) {
                c = item1.getValue();
                c1 = 0;
                for (Map.Entry<Integer, Integer> item2 : val2.entrySet()) {
                    if (item1.getKey() == item2.getKey()) {
                        c1 = item2.getValue();
                        break;
                    }
                }
                list.put(item1.getKey(), c+c1);
            }
            
            boolean exist = false;
            for (Map.Entry<Integer, Integer> item2 : val2.entrySet()) {
                exist = false;
                for (Map.Entry<Integer, Integer> item1 : val1.entrySet()) {
                    if (item1.getKey() == item2.getKey()) {
                        exist = true;
                        break;
                    }
                }
                if(!exist){
                    list.put(item2.getKey(), item2.getValue());
                }
            }
            //for (Map.Entry<Integer, Integer> item3 : val3.entrySet()) {
            //    list.put(item3.getKey(), item3.getValue());
            //    System.out.println(item3.getKey() + ":" + item3.getValue());
            //}
            
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }

        }
        return list;
    }

    
}
