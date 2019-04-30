/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.notification;

import com.maxelyz.core.notification.dto.FollowupDTO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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
public class ApprovalSender implements Runnable {

    private static Logger log = Logger.getLogger(ApprovalSender.class);
    private static final CometEngine engine = CometContext.getInstance().getEngine();
    private DataSource dataSource;
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public ApprovalSender(DataSource ds){
        this.dataSource = ds;
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(30000);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            this.sendAlertApproval();

        }

    }
    
    private Date getStartTime(){
        Date now = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        Date time = cal.getTime();
        
        return time;
    }
    
    private Date getEndTime(){
        Date now = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        Date time = cal.getTime();
        
        return time;
    }
    
    private Date getTime1(){
        Date now = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        Date time1 = cal.getTime();
        
        return time1;
    }
    
    private Date getTime2(){
        Date now = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        Date time2 = cal.getTime();
        
        return time2;
    }
    
    private Date getTime3(){
        Date now = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        Date time3 = cal.getTime();
        
        return time3;
    }
    
    private void sendAlertApproval(){
        Date now = new Date();
        Date time1 = this.getTime1();
        Date time2 = this.getTime2();
        Date time3 = this.getTime3();
        String startTime = "";
        String endTime = "";
        
        if(now.after(this.getStartTime()) && now.before(this.getEndTime())) {
            if(now.after(this.getStartTime()) && now.before(time1)) {
                startTime = sdf.format(this.getStartTime());
                endTime = sdf.format(time1);
            } else if(now.after(time1) && now.before(time2)) {
                startTime = sdf.format(time1);
                endTime = sdf.format(time2);
            } else if(now.after(time3) && now.before(this.getEndTime())) {
                startTime = sdf.format(time2);
                endTime = sdf.format(time3);
            }
            if(!startTime.equals("") && !endTime.equals("")){
                this.sendAlert(startTime, endTime);
            }
        }
        
    }

    private void sendAlert(String startTime, String endTime) {
        try {
            Map<String, String> map1 = AppStore.getInstance().getMap();
            Iterator<Map.Entry<String, String>> iter1 = map1.entrySet().iterator();

            List<FollowupDTO> list = null;
            if (iter1.hasNext()) {
                list = getListApproval(startTime, endTime);

                for (FollowupDTO fl : list) {

                    Map<String, String> map = AppStore.getInstance().getMap();
                    Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
                    while (iter.hasNext()) {

                        Map.Entry<String, String> entry = iter.next();
                        String id = entry.getKey();
                        Integer userId = Integer.parseInt(entry.getValue().trim());

                        if (fl.getUserId() == userId) {
                            engine.sendTo(Constant.APP_CHANNEL, engine.getConnection(id), fl);
                            this.updateNoAlert(fl.getId());
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
    
    private void updateNoAlert(Integer id) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "update notification set no_alert = 1 where id = " + id;

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();

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
    }

    private List<FollowupDTO> getListApproval(String startTime, String endTime) throws ServletException, IOException {
        
        List<FollowupDTO> list = new ArrayList<FollowupDTO>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "select noti.id as id"
                    + ", noti.ref_no as ref_no"
                    + ", noti.to_user_id as to_user_id"
                    + ", noti.customer_name as customer_name"
                    + ", noti.followup_date as followup_date"
                    + ", noti.detail as detail"
                    + " from notification noti"
                    + " inner join assignment_detail ad on ad.id = noti.assignment_detail_id"
                    + " where noti.enable = 1 and noti.status = 1"
                    + " and noti.type = 'reconfirm' and (no_alert is null or no_alert = 0)"
                    + " and (noti.create_date between '" + startTime + "' and  '" + endTime + "')"
                    + " order by noti.to_user_id";

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                FollowupDTO fl = new FollowupDTO(Constant.RECONFIRM, rs.getInt("id"), rs.getInt("to_user_id"), rs.getString("customer_name"), rs.getTimestamp("followup_date"), rs.getString("detail"));
                fl.setRefNo(rs.getString("ref_no"));
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
    
    public static void main(String[] str){
//        Date now = new Date();
//        Calendar cal = new GregorianCalendar();
//        cal.setTime(now);
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        
//        Date time = cal.getTime();
//        
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(sdf.format(time));
        

        
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
}
