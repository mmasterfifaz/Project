package com.maxelyz.social.controller;

import com.maxelyz.core.controller.admin.dto.StringTemplateDTO;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.social.model.entity.SclContactStats;
import com.maxelyz.core.model.value.admin.RptContactCaseTypeValue;
import com.maxelyz.social.model.dao.SclContactStatsDAO;
import com.maxelyz.utils.JSFUtil;
import com.ntier.utils.ParameterMap;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
@RequestScoped
public class DashboardSocialStaticController implements Serializable {

    private static Logger log = Logger.getLogger(DashboardSocialStaticController.class);
    private List<SclContactStats> sclContactStatss;

    private Date fromDate, toDate;

    private String chart1Label = "";
    private String chart1Data;
    private String chart2Label = "";
    private String chart2Data;
    private String chart3Label = "";
    private String chart3Data;

    private String chart1Data1;
    private String chart1Data2;
    private String chart1Data3;
    private String chart1Data4;

    private String chart2Data1;
    private String chart2Data2;
    private String chart2Data3;
    private String chart2Data4;
    private String chart2Data5;

    private String chart3Data1;
    private String chart3Data2;
    private String chart3Data3;

    private List<Object> objectList = new ArrayList<Object>();
    List<StringTemplateDTO> data1lists = new ArrayList<StringTemplateDTO>();
    List<StringTemplateDTO> data2lists = new ArrayList<StringTemplateDTO>();
    List<StringTemplateDTO> data3lists = new ArrayList<StringTemplateDTO>();

    @ManagedProperty(value = "#{sclContactStatsDAO}")
    private SclContactStatsDAO sclContactStatsDAO;

    @PostConstruct
    public void initialize() {
        //Date today = new Date(2011-1900,1,11); //11 feb 2011
        Date today = new Date();
        fromDate = DateUtils.addDays(today, -100);
        toDate = today;
        StringTemplateDTO stringTemplateDTO;
        int i = 0;
        Integer cnt;
        Integer cntTotal;
        String percentString;

//        sclContactStatss = sclContactStatsDAO.findSclContactStatsEntities();

//        i = 0;
//        chart1Label = "";
//        for (SclContactStats obj : sclContactStatss) {
//            chart1Label += String.format(",{value: %d, text: \"%s\"}", ++i, obj.getChannelId().toString());
//            //chart1Label += String.format(",{value: %d, text: \"%s\"}", ++i, Integer.toString(2500+i));
//            chart1Data +=  obj.getReplyPost() + ",";
//        }
//        chart1Data = removeLastComma(chart1Data);

//        int j = sclContactStatsDAO.sumReceivedPostByDate(fromDate, toDate);
//        objectList = sclContactStatsDAO.findStaticChannel(fromDate, toDate, 1);
//        Object obj = objectList.get(0);


//================ Chart1
        cntTotal = sclContactStatsDAO.sumReplyPostByChannel(fromDate, toDate, null);
        cntTotal = 1270;
        i = 0;
        
        cnt = sclContactStatsDAO.sumReplyPostByChannel(fromDate, toDate, 1);
        if (cnt==null) {cnt=0;}
        cnt = 110;
        chart1Data1 = calcPercent(cntTotal,cnt);
        percentString = calcPercentPlus(cntTotal,cnt);
        chart1Label += String.format(",{value: %d, text: \"%s\"}", ++i, "");
//        chart1Label += String.format(",{value: %d, text: \"%s\"}", ++i, "Facebook");
        stringTemplateDTO = new StringTemplateDTO("Facebook",Integer.toString(cnt),percentString);
        data1lists.add(stringTemplateDTO);

        cnt = sclContactStatsDAO.sumReplyPostByChannel(fromDate, toDate, 2);
        if (cnt==null) {cnt=0;}
        cnt = 1125;
        chart1Data2 = calcPercent(cntTotal,cnt);
        percentString = calcPercentPlus(cntTotal,cnt);
        chart1Label += String.format(",{value: %d, text: \"%s\"}", ++i, "");
//        chart1Label += String.format(",{value: %d, text: \"%s\"}", ++i, "Facebook");
        stringTemplateDTO = new StringTemplateDTO("Twitter",Integer.toString(cnt),percentString);
        data1lists.add(stringTemplateDTO);

        cnt = sclContactStatsDAO.sumReplyPostByChannel(fromDate, toDate, 3);
        if (cnt==null) {cnt=0;}
        cnt = 25;
        chart1Data3 = calcPercent(cntTotal,cnt);
        percentString = calcPercentPlus(cntTotal,cnt);
        chart1Label += String.format(",{value: %d, text: \"%s\"}", ++i, "");
//        chart1Label += String.format(",{value: %d, text: \"%s\"}", ++i, "Facebook");
        stringTemplateDTO = new StringTemplateDTO("Pantip",Integer.toString(cnt),percentString);
        data1lists.add(stringTemplateDTO);

        cnt = sclContactStatsDAO.sumReplyPostByChannel(fromDate, toDate, 4);
        if (cnt==null) {cnt=0;}
        cnt = 10;
        chart1Data4 = calcPercent(cntTotal,cnt);
        percentString = calcPercentPlus(cntTotal,cnt);
        chart1Label += String.format(",{value: %d, text: \"%s\"}", ++i, "");
//        chart1Label += String.format(",{value: %d, text: \"%s\"}", ++i, "Facebook");
        stringTemplateDTO = new StringTemplateDTO("Instagram",Integer.toString(cnt),percentString);
        data1lists.add(stringTemplateDTO);

        stringTemplateDTO = new StringTemplateDTO("All",cntTotal.toString(),"100%");
        data1lists.add(stringTemplateDTO);

//================ Chart2
        cntTotal = sclContactStatsDAO.sumReplyPostByPostType(fromDate, toDate, null);
        cntTotal = 1270;
        i = 0;
        
        cnt = sclContactStatsDAO.sumReplyPostByPostType(fromDate, toDate, 1);
        if (cnt==null) {cnt=0;}
        cnt = 360;
        chart2Data1 = calcPercent(cntTotal,cnt);
        percentString = calcPercentPlus(cntTotal,cnt);
        chart2Label += String.format(",{value: %d, text: \"%s\"}", ++i, "");
//        chart2Label += String.format(",{value: %d, text: \"%s\"}", ++i, "ปัญหา");
        stringTemplateDTO = new StringTemplateDTO("ปัญหา",Integer.toString(cnt),percentString);
        data2lists.add(stringTemplateDTO);

        cnt = sclContactStatsDAO.sumReplyPostByPostType(fromDate, toDate, 2);
        if (cnt==null) {cnt=0;}
        cnt = 120;
        chart2Data2 = calcPercent(cntTotal,cnt);
        percentString = calcPercentPlus(cntTotal,cnt);
        chart2Label += String.format(",{value: %d, text: \"%s\"}", ++i, "");
//        chart2Label += String.format(",{value: %d, text: \"%s\"}", ++i, "ข้อเสนอแนะ/ความคิดเห็น");
        stringTemplateDTO = new StringTemplateDTO("ข้อเสนอแนะ/ความคิดเห็น",Integer.toString(cnt),percentString);
        data2lists.add(stringTemplateDTO);

        cnt = sclContactStatsDAO.sumReplyPostByPostType(fromDate, toDate, 3);
        if (cnt==null) {cnt=0;}
        cnt = 300;
        chart2Data3 = calcPercent(cntTotal,cnt);
        percentString = calcPercentPlus(cntTotal,cnt);
        chart2Label += String.format(",{value: %d, text: \"%s\"}", ++i, "");
//        chart2Label += String.format(",{value: %d, text: \"%s\"}", ++i, "ตำหนิ/ร้องเรียน");
        stringTemplateDTO = new StringTemplateDTO("ตำหนิ/ร้องเรียน",Integer.toString(cnt),percentString);
        data2lists.add(stringTemplateDTO);

        cnt = sclContactStatsDAO.sumReplyPostByPostType(fromDate, toDate, 4);
        if (cnt==null) {cnt=0;}
        cnt = 10;
        chart2Data4 = calcPercent(cntTotal,cnt);
        percentString = calcPercentPlus(cntTotal,cnt);
        chart2Label += String.format(",{value: %d, text: \"%s\"}", ++i, "");
//        chart2Label += String.format(",{value: %d, text: \"%s\"}", ++i, "คำชม");
        stringTemplateDTO = new StringTemplateDTO("คำชม",Integer.toString(cnt),percentString);
        data2lists.add(stringTemplateDTO);

        cnt = sclContactStatsDAO.sumReplyPostByPostType(fromDate, toDate, 5);
        if (cnt==null) {cnt=0;}
        cnt = 10;
        chart2Data5 = calcPercent(cntTotal,cnt);
        percentString = calcPercentPlus(cntTotal,cnt);
        chart2Label += String.format(",{value: %d, text: \"%s\"}", ++i, "");
//        chart2Label += String.format(",{value: %d, text: \"%s\"}", ++i, "อื่นๆ");
        stringTemplateDTO = new StringTemplateDTO("อื่นๆ",Integer.toString(cnt),percentString);
        data2lists.add(stringTemplateDTO);

        stringTemplateDTO = new StringTemplateDTO("All",cntTotal.toString(),"100%");
        data2lists.add(stringTemplateDTO);

//================ Chart3
        cntTotal = sclContactStatsDAO.sumReplyPostByCustFeeling(fromDate, toDate, null);
        cntTotal = 1270;
        i = 0;

        cnt = sclContactStatsDAO.sumReplyPostByCustFeeling(fromDate, toDate, 1);
        if (cnt==null) {cnt=0;}
        cnt = 360;
        chart3Data1 = calcPercent(cntTotal,cnt);
        percentString = calcPercentPlus(cntTotal,cnt);
        chart3Label += String.format(",{value: %d, text: \"%s\"}", ++i, "");
//        chart3Label += String.format(",{value: %d, text: \"%s\"}", ++i, "Positive");
        stringTemplateDTO = new StringTemplateDTO("Positive",chart3Data1,percentString);
        data3lists.add(stringTemplateDTO);

        cnt = sclContactStatsDAO.sumReplyPostByCustFeeling(fromDate, toDate, 2);
        if (cnt==null) {cnt=0;}
        cnt = 120;
        chart3Data2 = calcPercent(cntTotal,cnt);
        percentString = calcPercentPlus(cntTotal,cnt);
        chart3Label += String.format(",{value: %d, text: \"%s\"}", ++i, "");
//        chart3Label += String.format(",{value: %d, text: \"%s\"}", ++i, "Positive");
        stringTemplateDTO = new StringTemplateDTO("Neutral",chart3Data2,percentString);
        data3lists.add(stringTemplateDTO);

        cnt = sclContactStatsDAO.sumReplyPostByCustFeeling(fromDate, toDate, 3);
        if (cnt==null) {cnt=0;}
        cnt = 300;
        chart3Data3 = calcPercent(cntTotal,cnt);
        percentString = calcPercentPlus(cntTotal,cnt);
        chart3Label += String.format(",{value: %d, text: \"%s\"}", ++i, "");
//        chart3Label += String.format(",{value: %d, text: \"%s\"}", ++i, "Positive");
        stringTemplateDTO = new StringTemplateDTO("Negative",chart3Data3,percentString);
        data3lists.add(stringTemplateDTO);

        stringTemplateDTO = new StringTemplateDTO("All",cntTotal.toString(),"100%");
        data3lists.add(stringTemplateDTO);
    }

    private String removeLastComma(String str) {
        if (str.length() > 0)
            str = str.substring(0, str.length() - 1);
        else
            str = "0";
        return str;
    }

    private String calcPercent(Integer max, Integer value){
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(100.0 / max * value);
    }

    private String calcPercentPlus(Integer max, Integer value){
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(100.0 / max * value)+"%";
    }

    public List<SclContactStats> getSclContactStatss() {
        return sclContactStatss;
    }

    public void setSclContactStatss(List<SclContactStats> sclContactStatss) {
        this.sclContactStatss = sclContactStatss;
    }

    public String getChart1Label() {
        return chart1Label;
    }

    public void setChart1Label(String chart1Label) {
        this.chart1Label = chart1Label;
    }

    public String getChart1Data() {
        return chart1Data;
    }

    public void setChart1Data(String chart1Data) {
        this.chart1Data = chart1Data;
    }

    public SclContactStatsDAO getSclContactStatsDAO() {
        return sclContactStatsDAO;
    }

    public void setSclContactStatsDAO(SclContactStatsDAO sclContactStatsDAO) {
        this.sclContactStatsDAO = sclContactStatsDAO;
    }

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

    public String getChart2Label() {
        return chart2Label;
    }

    public void setChart2Label(String chart2Label) {
        this.chart2Label = chart2Label;
    }

    public String getChart2Data() {
        return chart2Data;
    }

    public void setChart2Data(String chart2Data) {
        this.chart2Data = chart2Data;
    }

    public String getChart3Label() {
        return chart3Label;
    }

    public void setChart3Label(String chart3Label) {
        this.chart3Label = chart3Label;
    }

    public String getChart3Data() {
        return chart3Data;
    }

    public void setChart3Data(String chart3Data) {
        this.chart3Data = chart3Data;
    }

    public String getChart1Data1() {
        return chart1Data1;
    }

    public void setChart1Data1(String chart1Data1) {
        this.chart1Data1 = chart1Data1;
    }

    public String getChart1Data2() {
        return chart1Data2;
    }

    public void setChart1Data2(String chart1Data2) {
        this.chart1Data2 = chart1Data2;
    }

    public String getChart1Data3() {
        return chart1Data3;
    }

    public void setChart1Data3(String chart1Data3) {
        this.chart1Data3 = chart1Data3;
    }

    public String getChart1Data4() {
        return chart1Data4;
    }

    public void setChart1Data4(String chart1Data4) {
        this.chart1Data4 = chart1Data4;
    }

    public String getChart2Data1() {
        return chart2Data1;
    }

    public void setChart2Data1(String chart2Data1) {
        this.chart2Data1 = chart2Data1;
    }

    public String getChart2Data2() {
        return chart2Data2;
    }

    public void setChart2Data2(String chart2Data2) {
        this.chart2Data2 = chart2Data2;
    }

    public String getChart2Data3() {
        return chart2Data3;
    }

    public void setChart2Data3(String chart2Data3) {
        this.chart2Data3 = chart2Data3;
    }

    public String getChart2Data4() {
        return chart2Data4;
    }

    public void setChart2Data4(String chart2Data4) {
        this.chart2Data4 = chart2Data4;
    }

    public String getChart2Data5() {
        return chart2Data5;
    }

    public void setChart2Data5(String chart2Data5) {
        this.chart2Data5 = chart2Data5;
    }

    public String getChart3Data1() {
        return chart3Data1;
    }

    public void setChart3Data1(String chart3Data1) {
        this.chart3Data1 = chart3Data1;
    }

    public String getChart3Data2() {
        return chart3Data2;
    }

    public void setChart3Data2(String chart3Data2) {
        this.chart3Data2 = chart3Data2;
    }

    public String getChart3Data3() {
        return chart3Data3;
    }

    public void setChart3Data3(String chart3Data3) {
        this.chart3Data3 = chart3Data3;
    }

    public List<StringTemplateDTO> getData1lists() {
        return data1lists;
    }

    public void setData1lists(List<StringTemplateDTO> data1lists) {
        this.data1lists = data1lists;
    }

    public List<StringTemplateDTO> getData2lists() {
        return data2lists;
    }

    public void setData2lists(List<StringTemplateDTO> data2lists) {
        this.data2lists = data2lists;
    }

    public List<StringTemplateDTO> getData3lists() {
        return data3lists;
    }

    public void setData3lists(List<StringTemplateDTO> data3lists) {
        this.data3lists = data3lists;
    }

}
