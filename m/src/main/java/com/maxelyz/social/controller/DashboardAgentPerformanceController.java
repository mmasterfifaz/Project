package com.maxelyz.social.controller;

import com.maxelyz.core.controller.admin.*;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

import com.maxelyz.utils.MxString;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import com.maxelyz.social.model.dao.SclContactStatsDAO;
import com.maxelyz.social.model.entity.SclContactStats;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import com.maxelyz.utils.MxzDateTime;
import org.apache.poi.util.StringUtil;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@ManagedBean
@ViewScoped
public class DashboardAgentPerformanceController implements Serializable {

    private static Logger log = Logger.getLogger(DashboardAgentPerformanceController.class);
    private static String REFRESH = "saleapproval.xhtml?faces-redirect=true";
    private List<SclContactStats> sclContactStatss;
    private Date fromDate, toDate;
    private String tmrName;
    private Integer dshChannel;
    private Integer dshAssign;
    private Integer dshRemain;
    private Integer dshRemainAll;
    private String dshAvgWorking;
    private String dshAvgResponse;

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{sclContactStatsDAO}")
    private SclContactStatsDAO sclContactStatsDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("social:dashboard:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        Date today = new Date();
//        fromDate = DateUtils.addDays(today,-1);
        fromDate = today;
        toDate = today;
        tmrName = "";
        this.searchActionListener();
    }

    public List<SclContactStats> getList() {
        return sclContactStatss;
    }

    public void searchActionListener() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59", Locale.US);
        String fromDateStr =  sdf1.format(fromDate);
        String toDateStr =  sdf2.format(toDate);

        String sp = "exec dbo.spDashboard " + MxString.quotedStrEmpty(fromDateStr) + "," + MxString.quotedStrEmpty(toDateStr);
        System.out.println(sp);
        if (execStoreProc(sp) == 1) {
            System.out.println("execStoreProc complete :"+sp);
        } else {
            System.out.println("execStoreProc error :"+sp);
        }

//        sclContactStatss = sclContactStatsDAO.findSclContactStatsEntities();
//        sclContactStatss = sclContactStatsDAO.findSclContactStatsToday();
        sclContactStatss = sclContactStatsDAO.findSclContactStatsByDate(fromDate, toDate);

        dshChannel = 0;
        dshAssign = 0;
        dshRemain = 0;
        dshRemainAll = 0;
        dshAvgWorking = "0:00";
        dshAvgResponse = "0:00";

//        dshChannel = String.valueOf(sclContactStatsDAO.sumTotalWorkingTimeByDate(fromDate, toDate));
//        dshAssign = String.valueOf(sclContactStatsDAO.sumReceivedPostByDate(fromDate, toDate));
//        dshRemain = String.valueOf(sclContactStatsDAO.sumTotalResponseTimeByDate(fromDate, toDate));
//
//        if (Integer.parseInt(dshAssign) > 0) {
//            dshAvgWorking = String.valueOf(com.maxelyz.utils.MxzDateTime.secToMmSs(sclContactStatsDAO.sumTotalWorkingTimeByDate(fromDate, toDate) / Integer.parseInt(dshAssign)));
//            dshAvgResponse = String.valueOf(com.maxelyz.utils.MxzDateTime.secToMmSs(sclContactStatsDAO.sumTotalResponseTimeByDate(fromDate, toDate) / Integer.parseInt(dshAssign)));
//        }
//        dshChannel = sclContactStatsDAO.sumTotalWorkingTimeByDate(fromDate, toDate);
//        dshAssign = sclContactStatsDAO.sumReceivedPostByDate(fromDate, toDate);
//        dshRemain = sclContactStatsDAO.sumTotalResponseTimeByDate(fromDate, toDate);

//        dshChannel = sclContactStatsDAO.sumTotalPostByDate(fromDate, toDate);
//        dshChannel = sclContactStatsDAO.sumTotalPostByDateInt(fromDate, toDate);
        dshAssign = sclContactStatsDAO.sumTotalAssignByDate2(fromDate, toDate).intValue();
//        dshRemain = sclContactStatsDAO.sumTotalRemainQByDate(fromDate, toDate);
        dshRemain = sclContactStatsDAO.sumTotalRemainQByDateInt(fromDate, toDate);
        dshRemainAll = sclContactStatsDAO.sumTotalRemainQAllInt();

        dshChannel =  dshAssign + dshRemain;

//        dshChannel = 9487;
//        dshAssign = 9175;
//        dshRemain = 312;

        if (dshAssign > 0) {
//            dshAvgWorking = String.valueOf(com.maxelyz.utils.MxzDateTime.secToMmSs(sclContactStatsDAO.sumTotalWorkingTimeByDate(fromDate, toDate) / dshAssign));
//            dshAvgResponse = String.valueOf(com.maxelyz.utils.MxzDateTime.secToMmSs(sclContactStatsDAO.sumTotalResponseTimeByDate(fromDate, toDate) / dshAssign));
            dshAvgWorking = sclContactStatsDAO.sumTotalWorkingTimeByDateStr();
            dshAvgResponse = sclContactStatsDAO.sumTotalResponseTimeByDateStr();

//            dshAvgWorking = "1:58";
//            dshAvgResponse = "1:13";
        }
    }

    private int execStoreProc(String stmtStr){

        Connection con = null;
        Statement stmt = null;

        try {
            con = dataSource.getConnection();
            String createString;
            createString = stmtStr;

            stmt = con.createStatement();
            stmt.executeQuery(createString);
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    System.err.println("Exception: " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    System.err.println("Exception: " + e.getMessage());
                }
            }
        }

        return 1;
    }

    //getter/setter
    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getTmrName() {
        return tmrName;
    }

    public void setTmrName(String tmrName) {
        this.tmrName = tmrName;
    }

    //Managed Propterties
    public SclContactStatsDAO getSclContactStatsDAO() {
        return sclContactStatsDAO;
    }

    public void setSclContactStatsDAO(SclContactStatsDAO sclContactStatsDAO) {
        this.sclContactStatsDAO = sclContactStatsDAO;
    }

    public List<SclContactStats> getSclContactStatss() {
        return sclContactStatss;
    }

    public void setSclContactStatss(List<SclContactStats> sclContactStatss) {
        this.sclContactStatss = sclContactStatss;
    }

    public Integer getDshChannel() {
        return dshChannel;
    }

    public void setDshChannel(Integer dshChannel) {
        this.dshChannel = dshChannel;
    }

    public Integer getDshAssign() {
        return dshAssign;
    }

    public void setDshAssign(Integer dshAssign) {
        this.dshAssign = dshAssign;
    }

    public Integer getDshRemain() {
        return dshRemain;
    }

    public void setDshRemain(Integer dshRemain) {
        this.dshRemain = dshRemain;
    }

    public Integer getDshRemainAll() {
        return dshRemainAll;
    }

    public void setDshRemainAll(Integer dshRemainAll) {
        this.dshRemainAll = dshRemainAll;
    }

    public String getDshAvgWorking() {
        return dshAvgWorking;
    }

    public void setDshAvgWorking(String dshAvgWorking) {
        this.dshAvgWorking = dshAvgWorking;
    }

    public String getDshAvgResponse() {
        return dshAvgResponse;
    }

    public void setDshAvgResponse(String dshAvgResponse) {
        this.dshAvgResponse = dshAvgResponse;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
