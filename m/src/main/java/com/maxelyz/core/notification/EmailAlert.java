/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.notification;

import com.maxelyz.core.notification.dto.EmailDTO;
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
public class EmailAlert implements Runnable {

    private static Logger log = Logger.getLogger(EmailAlert.class);
    private static final CometEngine engine = CometContext.getInstance().getEngine();
    private DataSource dataSource;
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public EmailAlert(DataSource ds){
        this.dataSource = ds;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
                
                this.sendAlert();
            
                Thread.sleep(5000);
                
                if(Thread.currentThread().getName().equals("EmailAlert")){
                    Thread.currentThread().stop();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendAlert() {
        try {

            List<Integer> list = getListUser();
            for (Integer uId : list) {

                Map<String, String> map = AppStore.getInstance().getMap();
                Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String id = entry.getKey();
                    Integer userId = Integer.parseInt(entry.getValue().trim());
                    EmailDTO object = new EmailDTO(Constant.EMAIL, id, userId);
                    if (uId.intValue() == userId.intValue()) {
                        engine.sendTo(Constant.APP_CHANNEL, engine.getConnection(id), object);
                        this.updateNoAlert(uId);
                        break;
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
            String sql = "update users set income_message = 0 where id = " + id;

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

    private List<Integer> getListUser() throws ServletException, IOException {
        
        List<Integer> list = new ArrayList<Integer>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "select id from users where status = 1 and enable = 1 and income_message > 0";

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                list.add(rs.getInt("id"));
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
