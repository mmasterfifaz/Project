package com.maxelyz.social.controller;

import com.maxelyz.social.model.dao.SoAccountDAO;
import com.maxelyz.social.model.dao.SoRptPostDAO;
import com.maxelyz.social.model.entity.SoAccount;
import com.maxelyz.social.model.entity.SclContactStats;
import com.maxelyz.social.model.entity.SoRptPostAccountByDateDTO;
import com.maxelyz.social.model.entity.SoRptPostDTO;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.MxzDateTime;
import com.maxelyz.utils.SecurityUtil;
import com.ntier.utils.ParameterMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.faces.bean.ViewScoped;

@ManagedBean
@RequestScoped
@ViewScoped
public class SocialAnalysisReport implements Serializable {

    private static Logger log = Logger.getLogger(SocialAnalysisReport.class);
    private static String REFRESH = "socialanalysisreport.xhtml?faces-redirect=true";
    private static String SELECT = "socialanalysisreport.xhtml";
    private List<SclContactStats> sclContactStatss;
    private Date fromDate, toDate;
    private String graphName;
    private String graphTitle;
    private String graphType;
    private String graphLabels;
    private String graphDatas1;
    private String graphDatas2;
    private String graphDatas3;
    private String graphDatas4;
    private String graphDatas5;

    private String graphSerie1;
    private String graphSerie2;
    private String graphSerie3;
    private String graphSerie4;
    private String graphSerie5;

    private List<SoAccount> soAccountList;
    private String[] soAccountString;

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{soAccountDAO}")
    private SoAccountDAO soAccountDAO;
    @ManagedProperty(value = "#{soRptPostDAO}")
    private SoRptPostDAO soRptPostDAO;

    private List<String> strResultDetail = new ArrayList<String>();

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("social:report:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        Date today = new Date();
//        fromDate = today;
        fromDate = DateUtils.addDays(today,-30);
        toDate = today;
        graphName = "";
        graphType = "ClusteredColumns";

        soAccountList = soAccountDAO.findSoAccountEntities();


        String str = "";
        for (SoAccount s : soAccountList) {
                str += s.getId() + ",";
        }
//        if (str.length()>0) {
//            str = str.substring(0, str.length() - 1);
//        }
        removeLastComma(str);
        soAccountString = str.split(",");

        this.graphActionListener(null);
    }

    public List<SclContactStats> getList() {
        return sclContactStatss;
    }

    public void graphActionListener(ActionEvent event) {
        try {
            fetchAction();
        } catch (ServletException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String graphNameLink = (String) JSFUtil.getRequestParameterMap("graphNameLink");
        if (StringUtils.isNotBlank(graphNameLink) ) {
            graphName = graphNameLink;
        }
        graphName = StringUtils.isBlank(graphName)?"totalpostbydate":graphName;
//        graphName = StringUtils.isBlank(graphName)?"graphtest1":graphName;
//        graphName = "sentimentalbydate";
//        graphName = "accountbydate";

//        graphType = "Lines";
//        graphType = "ClusteredColumns";
//        graphType = "Pie";

        Integer i = soRptPostDAO.countByDate(fromDate,toDate);
        System.out.println(i);

        graphLabels="";
        graphDatas1="";
        graphDatas2="";
        graphDatas3="";
        graphDatas4="";
        graphDatas5="";

        graphSerie1="";
        graphSerie2="";
        graphSerie3="";
        graphSerie4="";
        graphSerie5="";

        i=0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.US);

        List<SoRptPostDTO> soRptPostDTOs = new ArrayList<SoRptPostDTO>();
        List<SoRptPostAccountByDateDTO> soRptPostAccountByDateDTOs = new ArrayList<SoRptPostAccountByDateDTO>();

        if (graphName.equals("totalpostbydate")) {
            graphTitle = "Total Post By Date";
            soRptPostDTOs = soRptPostDAO.findTotalByDate(fromDate, toDate, getSoAccountStringX());
            for (SoRptPostDTO obj : soRptPostDTOs) {
                graphLabels += String.format(",{value: %d, text: \"%s\"}", ++i, obj.getTitle());
                graphDatas1 += obj.getTotalPost()+",";
                graphDatas2 += obj.getReply()+",";
                graphDatas3 += obj.getIgnore()+",";
                graphDatas4 += obj.getNoReply()+",";
            }
//            graphSerie1="Total Post";
//            graphSerie2="Reply";
//            graphSerie3="Ignore";
//            graphSerie4="Pending Post";
            graphSerie1=".addSeries(\"Total Post\",["+removeLastComma(graphDatas1)+"], {stroke: {color:\"#ff6600\"}, fill: \"#ff6600\"})\n";
            graphSerie1+=".addSeries(\"Reply\",["+removeLastComma(graphDatas2)+"], {stroke: {color:\"#fe9900\"}, fill: \"#fe9900\"})\n";
            graphSerie1+=".addSeries(\"Ignore\",["+removeLastComma(graphDatas3)+"], {stroke: {color:\"#f06eaa\"}, fill: \"#f06eaa\"})\n";
            graphSerie1+=".addSeries(\"Pending Post\",["+removeLastComma(graphDatas4)+"], {stroke: {color:\"#0272be\"}, fill: \"#0272be\"})\n";

        } else if (graphName.equals("sentimentalbydate")) {
            graphTitle = "Sentimental By Date";
            soRptPostDTOs = soRptPostDAO.findTotalByDate(fromDate, toDate, getSoAccountStringX());
            for (SoRptPostDTO obj : soRptPostDTOs) {
                graphLabels += String.format(",{value: %d, text: \"%s\"}", ++i, obj.getTitle());
                graphDatas1 += obj.getNegative()+",";
                graphDatas2 += obj.getNeutral()+",";
                graphDatas3 += obj.getPositive()+",";
                graphDatas4 += obj.getNoSentimental()+",";
            }
//            graphSerie1="Negative";
//            graphSerie2="Neutral";
//            graphSerie3="Positive";
//            graphSerie4="None";
            graphSerie1=".addSeries(\"Negative\",["+removeLastComma(graphDatas1)+"], {stroke: {color:\"#ff6600\"}, fill: \"#ff6600\"})\n";
            graphSerie1+=".addSeries(\"Neutral\",["+removeLastComma(graphDatas2)+"], {stroke: {color:\"#fe9900\"}, fill: \"#fe9900\"})\n";
            graphSerie1+=".addSeries(\"Positive\",["+removeLastComma(graphDatas3)+"], {stroke: {color:\"#f06eaa\"}, fill: \"#f06eaa\"})\n";
            graphSerie1+=".addSeries(\"None\",["+removeLastComma(graphDatas4)+"], {stroke: {color:\"#0272be\"}, fill: \"#0272be\"})\n";

        } else if (graphName.equals("accountbydate")) {
            graphTitle = "Account By Date";
            soRptPostAccountByDateDTOs = soRptPostDAO.findAccountByDate(fromDate, toDate, getSoAccountStringX());
            for (SoRptPostAccountByDateDTO obj : soRptPostAccountByDateDTOs) {
                graphLabels += String.format(",{value: %d, text: \"%s\"}", ++i, obj.getTitle());
                graphDatas1 += obj.getAccount1()+",";
                graphDatas2 += obj.getAccount2()+",";
                graphDatas3 += obj.getAccount3()+",";
                graphDatas4 += obj.getAccount4()+",";
                graphDatas5 += obj.getAccount5()+",";
            }
//            graphSerie1="AspectDemo_FanPage";
//            graphSerie2="pantip_test1";
//            graphSerie3="pantip_test2";
//            graphSerie4="twitter_test1";
//            graphSerie5="twitter_test2";
            graphSerie1=".addSeries(\"AspectDemo_FanPage\",["+removeLastComma(graphDatas1)+"], {stroke: {color:\"#ff6600\"}, fill: \"#ff6600\"})\n";
            graphSerie1+=".addSeries(\"pantip_test1\",["+removeLastComma(graphDatas2)+"], {stroke: {color:\"#fe9900\"}, fill: \"#fe9900\"})\n";
            graphSerie1+=".addSeries(\"pantip_test2\",["+removeLastComma(graphDatas3)+"], {stroke: {color:\"#f06eaa\"}, fill: \"#f06eaa\"})\n";
            graphSerie1+=".addSeries(\"twitter_test1\",["+removeLastComma(graphDatas4)+"], {stroke: {color:\"#0272be\"}, fill: \"#0272be\"})\n";
            graphSerie1+=".addSeries(\"twitter_test2\",["+removeLastComma(graphDatas5)+"], {stroke: {color:\"#90D158\"}, fill: \"#90D158\"})\n";

        } else if (graphName.equals("totalpostbyaccount")) {
            graphTitle = "Total Post By Account";
            soRptPostDTOs = soRptPostDAO.findTotalByAccount(fromDate, toDate, getSoAccountStringX());
            for (SoRptPostDTO obj : soRptPostDTOs) {
                graphLabels += String.format(",{value: %d, text: \"%s\"}", ++i, obj.getTitle());
                graphDatas1 += obj.getTotalPost()+",";
                graphDatas2 += obj.getReply()+",";
                graphDatas3 += obj.getIgnore()+",";
                graphDatas4 += obj.getNoReply()+",";
            }
//            graphSerie1="Total Post";
//            graphSerie2="Reply";
//            graphSerie3="Ignore";
//            graphSerie4="Pending Post";
            graphSerie1=".addSeries(\"Total Post\",["+removeLastComma(graphDatas1)+"], {stroke: {color:\"#ff6600\"}, fill: \"#ff6600\"})\n";
            graphSerie1+=".addSeries(\"Reply\",["+removeLastComma(graphDatas2)+"], {stroke: {color:\"#fe9900\"}, fill: \"#fe9900\"})\n";
            graphSerie1+=".addSeries(\"Ignore\",["+removeLastComma(graphDatas3)+"], {stroke: {color:\"#f06eaa\"}, fill: \"#f06eaa\"})\n";
            graphSerie1+=".addSeries(\"Pending Post\",["+removeLastComma(graphDatas4)+"], {stroke: {color:\"#0272be\"}, fill: \"#0272be\"})\n";
        }

        System.out.println(soRptPostDTOs.size());
//        graphDatas1 = removeLastComma(graphDatas1);
//        graphDatas2 = removeLastComma(graphDatas2);
//        graphDatas3 = removeLastComma(graphDatas3);
//        graphDatas4 = removeLastComma(graphDatas4);

    }

    private int execStoreProc(String stmtStr) throws ServletException, IOException {
//        if (pCode == null) {
//            return 0;
//        }

        Connection con = null;
        Statement stmt = null;

        try {
            con = dataSource.getConnection();
            String createString;
            createString = stmtStr;

            stmt = con.createStatement();
            stmt.executeQuery(createString);
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            throw new ServletException(ex.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                    throw new ServletException(e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                    throw new ServletException(e.getMessage());
                }
            }
        }

        return 1;
    }

    public String fetchAction() throws ServletException, IOException {

        String sp = "exec dbo.refreshMessageCube";
        strResultDetail.clear();
//        if (product == null) {
//            return SELECT;
//        }
        if (execStoreProc(sp) == 1) {
            System.out.println("execStoreProc complete :"+sp);
        } else {
            System.out.println("execStoreProc error :"+sp);
        }

        return SELECT;

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

    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public List<SclContactStats> getSclContactStatss() {
        return sclContactStatss;
    }

    public void setSclContactStatss(List<SclContactStats> sclContactStatss) {
        this.sclContactStatss = sclContactStatss;
    }

    public List<SoAccount> getSoAccountList() {
        return soAccountList;
    }

    public void setSoAccountList(List<SoAccount> soAccountList) {
        this.soAccountList = soAccountList;
    }

    public String[] getSoAccountString() {
        return soAccountString;
    }

    public void setSoAccountString(String[] soAccountString) {
        this.soAccountString = soAccountString;
    }

    public SoAccountDAO getSoAccountDAO() {
        return soAccountDAO;
    }

    public void setSoAccountDAO(SoAccountDAO soAccountDAO) {
        this.soAccountDAO = soAccountDAO;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<String> getStrResultDetail() {
        return strResultDetail;
    }

    public void setStrResultDetail(List<String> strResultDetail) {
        this.strResultDetail = strResultDetail;
    }

    public String getGraphType() {
        return graphType;
    }

    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    public SoRptPostDAO getSoRptPostDAO() {
        return soRptPostDAO;
    }

    public void setSoRptPostDAO(SoRptPostDAO soRptPostDAO) {
        this.soRptPostDAO = soRptPostDAO;
    }

    public String getGraphLabels() {
        return graphLabels;
    }

    public void setGraphLabels(String graphLabels) {
        this.graphLabels = graphLabels;
    }

    public String getGraphDatas1() {
        return graphDatas1;
    }

    public void setGraphDatas1(String graphDatas1) {
        this.graphDatas1 = graphDatas1;
    }

    public String getGraphDatas2() {
        return graphDatas2;
    }

    public void setGraphDatas2(String graphDatas2) {
        this.graphDatas2 = graphDatas2;
    }

    public String getGraphDatas3() {
        return graphDatas3;
    }

    public void setGraphDatas3(String graphDatas3) {
        this.graphDatas3 = graphDatas3;
    }

    public String getGraphDatas4() {
        return graphDatas4;
    }

    public void setGraphDatas4(String graphDatas4) {
        this.graphDatas4 = graphDatas4;
    }

    public String getGraphSerie1() {
        return graphSerie1;
    }

    public void setGraphSerie1(String graphSerie1) {
        this.graphSerie1 = graphSerie1;
    }

    public String getGraphSerie2() {
        return graphSerie2;
    }

    public void setGraphSerie2(String graphSerie2) {
        this.graphSerie2 = graphSerie2;
    }

    public String getGraphSerie3() {
        return graphSerie3;
    }

    public void setGraphSerie3(String graphSerie3) {
        this.graphSerie3 = graphSerie3;
    }

    public String getGraphSerie4() {
        return graphSerie4;
    }

    public void setGraphSerie4(String graphSerie4) {
        this.graphSerie4 = graphSerie4;
    }

    public String getGraphDatas5() {
        return graphDatas5;
    }

    public void setGraphDatas5(String graphDatas5) {
        this.graphDatas5 = graphDatas5;
    }

    public String getGraphSerie5() {
        return graphSerie5;
    }

    public void setGraphSerie5(String graphSerie5) {
        this.graphSerie5 = graphSerie5;
    }

    public String getGraphTitle() {
        return graphTitle;
    }

    public void setGraphTitle(String graphTitle) {
        this.graphTitle = graphTitle;
    }

    private String removeLastComma(String str) {
        if (str.length() > 0)
            str = str.substring(0, str.length() - 1);
        else
            str = "0";
        return str;
    }

    public void setSoAccountStringX() {

    }

    public String getSoAccountStringX() {
//        String s = "";
//        for(int i=0; i<soAccountString.length; i++) {
//            s += soAccountString[i];
//        }
//        return s;

        String ret = "";
        for(String s : soAccountString) {
            ret += s+",";
        }

        return removeLastComma(ret);
    }

}
