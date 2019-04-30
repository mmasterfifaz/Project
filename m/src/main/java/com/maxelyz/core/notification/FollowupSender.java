/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.notification;

import com.maxelyz.core.notification.dto.FollowupDTO;
import com.maxelyz.core.notification.dto.NotificationDTO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;

/**
 *
 * @author ukritj
 */
public class FollowupSender implements Runnable {

    private static Logger log = Logger.getLogger(NotificationSender.class);
    private static final CometEngine engine = CometContext.getInstance().getEngine();
    private DataSource dataSource;
    
    public FollowupSender(DataSource ds){
        this.dataSource = ds;
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(60000);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //this.sendCountMsg();
            this.sendAlertFollowup();

        }

    }

    private void sendAlertFollowup() {
        try {
            Map<String, String> map1 = AppStore.getInstance().getMap();
            Iterator<Map.Entry<String, String>> iter1 = map1.entrySet().iterator();

            List<FollowupDTO> list = null;
            if (iter1.hasNext()) {
                list = getListFollowup();

                for (FollowupDTO fl : list) {

                    Map<String, String> map = AppStore.getInstance().getMap();
                    Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
                    while (iter.hasNext()) {

                        Map.Entry<String, String> entry = iter.next();
                        String id = entry.getKey();
                        Integer userId = Integer.parseInt(entry.getValue().trim());

                        if (fl.getUserId().intValue() == userId.intValue()) {
                            engine.sendTo(Constant.APP_CHANNEL, engine.getConnection(id), fl);
                            break;
                        }
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private synchronized Connection getConnection() throws SQLException, NamingException {

//        Context initContext = new InitialContext();
//        Context envContext = (Context) initContext.lookup("java:/comp/env");
//        DataSource ds = (DataSource) envContext.lookup("jdbc/MaxarDB");

        return dataSource.getConnection();	// Allocate and use a connection from the pool
    }

    private List<FollowupDTO> getListFollowup() throws ServletException, IOException {
        
        List<FollowupDTO> list = new ArrayList<FollowupDTO>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "select noti.id as id"
                    + ", noti.to_user_id as to_user_id"
                    + ", noti.customer_name as customer_name"
                    + ", noti.followup_date as followup_date"
                    + ", noti.detail as detail"
                    + " from notification noti"
                    + " inner join assignment_detail ad on ad.id = noti.assignment_detail_id"
                    + " where noti.enable = 1"
                    + " and noti.type = 'followup'"
                    + " and (noti.followup_date between GETDATE() and DATEADD(MI, 6, GETDATE()))"
                    + " order by noti.to_user_id";

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                FollowupDTO fl = new FollowupDTO(Constant.FOLLOWUP, rs.getInt("id"), rs.getInt("to_user_id"), rs.getString("customer_name"), rs.getTimestamp("followup_date"), rs.getString("detail"));
                list.add(fl);
            }

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

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
}
