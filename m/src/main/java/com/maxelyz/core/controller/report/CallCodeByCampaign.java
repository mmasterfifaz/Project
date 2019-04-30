/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.report;

import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;


@ManagedBean
@RequestScoped
public class CallCodeByCampaign extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(CallCodeByCampaign.class);
    
    private SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
    private String fileName = "";
    DecimalFormat df = new DecimalFormat("#,##0.00");
    DecimalFormat df1 = new DecimalFormat("#,###");
    

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:callcodebycampaignid:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/callcodebycampaignid.jasper";
    }

    @Override
    public Map getParameterMap() { 
        Map parameterMap = new LinkedHashMap();
        parameterMap.put("from", fromDate);
        parameterMap.put("to", toDate);
        parameterMap.put("user_group_id", userGroupId);
        parameterMap.put("user_group_name", groupName);
        parameterMap.put("user_id", userId);
        parameterMap.put("user_name", userName);
        parameterMap.put("campaign_id", campaignId);
        parameterMap.put("campaign_name", campaignName);
        parameterMap.put("marketing_id", marketingId);
        parameterMap.put("marketing_name", marketingName);
        parameterMap.put("product_id", productId);
        parameterMap.put("product_name", productName);

        return parameterMap;
    }

    @Override
    public String getQuery() {
        String select = "select nosale.name, count(sr.id) as total, count(sr.id)*100.0/sum(count(sr.id)) over () as ratio "
                + "from contact_history ch "
                + "inner join contact_history_sale_result sr on sr.contact_history_id = ch.id "
                + "inner join nosale_reason nosale on nosale.id = sr.nosale_reason_id "
                + "inner join contact_history_assignment cha on cha.contact_history_id = ch.id "
                + "inner join assignment_detail ad on ad.id = cha.assignment_detail_id "
                + "inner join assignment a on a.id = ad.assignment_id "
                + "inner join users u on u.id = ch.create_by_id "
                + "inner join user_group g on g.id = u.user_group_id ";

        String where = "WHERE ch.create_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        if (userGroupId!=0)
            where += "AND g.id = " + userGroupId + " ";
        if (userId!=0)
            where += "AND u.id = " + userId + " ";
        if (campaignId!=0)
            where += "AND a.campaign_id = " + campaignId + " ";
        if (marketingId!=0)
            where += "AND a.marketing_id = " + marketingId + " ";

        String groupBy = "group by nosale.name ";
        String orderBy = "order by nosale.name ";
        String query = select+where+groupBy+orderBy;
        
        return query;
    }
    
    public String getHeaderFxQuery() {
//String query =  "select distinct ISNULL(cus.flexfield15, 'N/A' )  \n" +
//                "				from marketing_customer as mc,marketing as mar,assignment as asg ,contact_result as con,assignment_detail as asd,users as u,customer as cus  \n" +
//                "				where mc.marketing_id = mar.id and mc.customer_id = cus.id and mar.id = asg.marketing_id and con.id = asd.contact_result_id and asd.assignment_id = asg.id and asd.user_id = u.id and asd.customer_id = cus.id and asd.contact_date BETWEEN '2016-08-01 00:00:00' AND '2017-08-17 23:59:59' "; 
//            
//            if (marketingId!=0)
//            {query += "  and mar.id = " + marketingId + " ";}
//
//            query += "	group by con.status_id,con.name,mar.id,cus.flexfield15,cus.id,mc.assign  ";

            //String query = "select distinct ISNULL(cus.flexfield15, 'N/A' )  from marketing as mar,assignment as asg ,contact_result as con,assignment_detail as asd,users as u,customer as cus  where mar.id = asg.marketing_id and con.id = asd.contact_result_id and asd.assignment_id = asg.id and asd.user_id = u.id and asd.customer_id = cus.id  and asd.contact_date BETWEEN '"+ sdf.format(fromDate) +"' and '"+ sdf.format(toDate) +"' ";
            //String query = "select distinct ISNULL(cus.flexfield15, 'N/A' )  from marketing as mar,assignment as asg ,contact_result as con,assignment_detail as asd,users as u,customer as cus,contact_history as ch,contact_history_assignment as cha  where mar.id = asg.marketing_id and con.id = asd.contact_result_id and asd.assignment_id = asg.id and asd.user_id = u.id and asd.customer_id = cus.id and asd.id =  cha.assignment_detail_id and cha.contact_history_id = ch.id and ch.contact_date BETWEEN '"+ sdf.format(fromDate) +"' and '"+ sdf.format(toDate) +"' ";
//            if (marketingId!=0)
//            {query += "and mar.id = "+marketingId ;}

            String query = "SELECT distinct ISNULL(t.flexfield15, 'N/A' ) " +
        "FROM " +
        "(" +
        "select row_number() OVER(PARTITION BY asd.id ORDER BY ch.contact_date DESC) AS rn,con.name,asd.id,con.status_id,NULLIF(cus.flexfield15,'') as flexfield15,mc.assign,ch.contact_date from contact_history as ch,assignment_detail as asd,contact_history_assignment as cha , contact_result as con ,customer as cus,marketing_customer as mc where ch.id = cha.contact_history_id and asd.id = cha.assignment_detail_id\n" +
        "and ch.contact_date BETWEEN '"+ sdf.format(fromDate) +"' and '"+ sdf.format(toDate) +"' and con.id = ch.contact_result_id and asd.customer_id = cus.id and mc.customer_id = cus.id " ;
        
        if (marketingId!=0)
        {query += " and mc.marketing_id = " + marketingId + " ";}
        
        query += ") AS t where t.rn = 1 order by ISNULL(t.flexfield15, 'N/A' )";
            
            //group by con.status_id,con.name,mar.id,ISNULL(cus.flexfield15, 'N/A' ),cus.id";

        return query;
    }
    
     public String getCallCodeValueQuery() {
//        String query1 = "SELECT distinct [Status Id], [Call Code] " +
//        "FROM " +
//        "(" +
//        "select distinct con.status_id as [Status Id],con.name as [Call Code],count(*) as value,mar.id as marketID,cus.flexfield15 ,cus.id  " +
//        "from marketing as mar,assignment as asg ,contact_result as con,assignment_detail as asd,users as u,customer as cus  " +
//        "where mar.id = asg.marketing_id and con.id = asd.contact_result_id and asd.assignment_id = asg.id and asd.user_id = u.id and asd.customer_id = cus.id  and asd.contact_date BETWEEN '"+ sdf.format(fromDate) +"' and '"+ sdf.format(toDate) +"' " +
//        "group by con.status_id,con.name,mar.id,cus.flexfield15,cus.id " +
//        ") AS src ";
        
//        String query1 = "SELECT distinct [Status Id], [Call Code] " +
//        "FROM " +
//        "(" +
//        "select distinct con.status_id as [Status Id],con.name as [Call Code],count(*) as value,mar.id as marketID,cus.flexfield15 ,cus.id  " +
//        "from marketing as mar,assignment as asg ,contact_result as con,assignment_detail as asd,users as u,customer as cus,contact_history as ch,contact_history_assignment as cha  " +
//        "where mar.id = asg.marketing_id and con.id = asd.contact_result_id and asd.assignment_id = asg.id and asd.user_id = u.id and asd.customer_id = cus.id and asd.id =  cha.assignment_detail_id and cha.contact_history_id = ch.id\n" +
//        "and ch.contact_date BETWEEN '"+ sdf.format(fromDate) +"' and '"+ sdf.format(toDate) +"' " +
//        "group by con.status_id,con.name,mar.id,cus.flexfield15,cus.id " +
//        ") AS src ";
        
//        String query1 = "SELECT distinct [Status Id], [Call Code] " +
//        "FROM " +
//        "(" +
//        "select distinct con.status_id as [Status Id],con.name as [Call Code],count(*) as value,mar.id as marketID,cus.flexfield15 ,cus.id ,mc.assign,ch.contact_date,row_number() OVER(PARTITION BY cus.id ORDER BY ch.contact_date DESC) AS rn\n" +
//        "		from marketing_customer as mc,marketing as mar,assignment as asg ,contact_result as con,assignment_detail as asd,users as u,customer as cus,contact_history as ch,contact_history_assignment as cha   \n" +
//        "		where mc.marketing_id = mar.id and mar.id = asg.marketing_id and con.id = asd.contact_result_id and asd.assignment_id = asg.id and asd.user_id = u.id and asd.customer_id = cus.id and asd.id =  cha.assignment_detail_id and cha.contact_history_id = ch.id \n" +
//        "               and ch.contact_date BETWEEN '"+ sdf.format(fromDate) +"' and '"+ sdf.format(toDate) +"' and mc.assign = 1 " +
//        "group by con.status_id,con.name,mar.id,cus.flexfield15,cus.id,mc.assign,ch.contact_date " +
//        ") AS src ";
        
        String query1 = "SELECT distinct t.status_id,t.name " +
        "FROM " +
        "(" +
        "select row_number() OVER(PARTITION BY asd.id ORDER BY ch.contact_date DESC) AS rn,con.name,asd.id,con.status_id,NULLIF(cus.flexfield15,'') as flexfield15,mc.assign,ch.contact_date from contact_history as ch,assignment_detail as asd,contact_history_assignment as cha , contact_result as con ,customer as cus,marketing_customer as mc where ch.id = cha.contact_history_id and asd.id = cha.assignment_detail_id\n" +
        "and ch.contact_date BETWEEN '"+ sdf.format(fromDate) +"' and '"+ sdf.format(toDate) +"' and con.id = ch.contact_result_id and asd.customer_id = cus.id and mc.customer_id = cus.id " ;
        

        
//        if (marketingId!=0)
//        {query1 += " where src.marketID = " + marketingId + " ";}
        if (marketingId!=0)
        {query1 += " and mc.marketing_id = " + marketingId + " ";}
        
        query1 += ") AS t  where t.rn = 1  order by t.status_id";
         
        
        return query1;
    }
     
    public String getFxValueQuery() {
//String query1 = "select distinct t.status_id,t.status_name,ISNULL(t.flexfield15,'N/A'),count(*) as amount \n" +
//                "from(  select  c.status_id,c.status_name,c.flexfield15,c.marketID  \n" +
//                "		from  (select distinct con.status_id,con.name as status_name,count(*) as value,mar.id as marketID,cus.flexfield15 ,cus.id ,mc.assign\n" +
//                "				from marketing_customer as mc,marketing as mar,assignment as asg ,contact_result as con,assignment_detail as asd,users as u,customer as cus  \n" +
//                "				where mc.marketing_id = mar.id and mc.customer_id = cus.id and mar.id = asg.marketing_id and con.id = asd.contact_result_id and asd.assignment_id = asg.id and asd.user_id = u.id and asd.customer_id = cus.id and asd.contact_date BETWEEN '"+ sdf.format(fromDate) +"' and '"+ sdf.format(toDate) +"' \n" +
//                "				group by con.status_id,con.name,mar.id,cus.flexfield15,cus.id,mc.assign  ) as c \n" +
//                "		group by c.status_id,c.status_name,c.flexfield15,c.value,c.id,c.marketID   ) as t ";
//        

//        String query1 = "select distinct t.status_id,t.status_name,ISNULL(t.flexfield15,'N/A'),count(*) as amount \n" +
//                        "from (select distinct con.status_id,con.name as status_name,count(*) as value,mar.id as marketID,cus.flexfield15 ,cus.id ,mc.assign,ch.contact_date,row_number() OVER(PARTITION BY cus.id ORDER BY ch.contact_date DESC) AS rn\n" +
//                        "				from marketing_customer as mc,marketing as mar,assignment as asg ,contact_result as con,assignment_detail as asd,users as u,customer as cus,contact_history as ch,contact_history_assignment as cha   \n" +
//                        "				where mc.marketing_id = mar.id and mar.id = asg.marketing_id and con.id = asd.contact_result_id and asd.assignment_id = asg.id and asd.user_id = u.id and asd.customer_id = cus.id and asd.id =  cha.assignment_detail_id and cha.contact_history_id = ch.id \n" +
//                        "                               and ch.contact_date BETWEEN '"+ sdf.format(fromDate) +"' and '"+ sdf.format(toDate) +"'  and mc.assign = 1\n" +
//                        "				group by con.status_id,con.name,mar.id,cus.flexfield15,cus.id,mc.assign,ch.contact_date\n" +
//                        "				) as t\n" +
//                        "				where t.rn = 1\n" ;
        String query1 = "select distinct t.status_id,t.name,ISNULL(t.flexfield15,'N/A'),count(*) as amount \n" +
                        "                        from (\n" +
                        "select row_number() OVER(PARTITION BY asd.id ORDER BY ch.contact_date DESC) AS rn,mc.marketing_id,con.name,asd.id as assignment_id,con.status_id,NULLIF(cus.flexfield15,'') as flexfield15,mc.assign,ch.contact_date from contact_history as ch,assignment_detail as asd,contact_history_assignment as cha , contact_result as con ,customer as cus,marketing_customer as mc where ch.id = cha.contact_history_id and asd.id = cha.assignment_detail_id\n" +
                        "and ch.contact_date BETWEEN '"+ sdf.format(fromDate) +"' and '"+ sdf.format(toDate) +"' and con.id = ch.contact_result_id and asd.customer_id = cus.id and mc.customer_id = cus.id ";

        
          if (marketingId!=0)
          { query1 += " and mc.marketing_id  = " + marketingId + " ";}
        
//        if (marketingId!=0)
//        { query1 += " and t.marketID = " + marketingId + " ";}

            query1 +=  " ) as t  where t.rn = 1 ";
            query1 +=  " group by t.status_id,t.name,ISNULL(t.flexfield15,'N/A') ";
            query1 +=  " order by ISNULL(t.flexfield15,'N/A'),t.status_id ";  
          
          
        return query1;
    }
    
    public String getListUploadedQuery(String fx15){
//        String query =  "select count(*) from ( " +
//                        "select cus.id,ISNULL(cus.flexfield15,'N/A') as fx15 " +
//                        "from marketing_customer as mc,marketing as mar,assignment as asg ,contact_result as con,assignment_detail as asd,users as u,customer as cus " +
//                        " where mc.marketing_id = mar.id and mc.customer_id = cus.id and mar.id = asg.marketing_id and con.id = asd.contact_result_id and asd.assignment_id = asg.id and asd.user_id = u.id and asd.customer_id = cus.id and ISNULL(cus.flexfield15,'N/A') = '"+fx15+"' "; 
//        
//        String query =  "select count(*) from ( " +
//                        "select cus.id,ISNULL(cus.flexfield15,'N/A') as fx15 " +
//                        "from marketing as mar,assignment as asg ,contact_result as con,assignment_detail as asd,users as u,customer as cus,contact_history as ch,contact_history_assignment as cha " +
//                        " where mc.marketing_id = mar.id and mc.customer_id = cus.id and mar.id = asg.marketing_id and con.id = asd.contact_result_id and asd.assignment_id = asg.id and asd.user_id = u.id and asd.customer_id = cus.id and ISNULL(cus.flexfield15,'N/A') = '"+fx15+"' "; 
//        
//        String query = "select count(*) from ( select distinct cus.id,ISNULL(cus.flexfield15,'N/A') as fx15 from marketing_customer as mc,marketing as mar,assignment as asg ,contact_result as con,assignment_detail as asd,users as u,customer as cus,contact_history as ch,contact_history_assignment as cha  where mc.marketing_id = mar.id and mc.customer_id = cus.id and mar.id = asg.marketing_id and con.id = asd.contact_result_id and asd.assignment_id = asg.id and asd.user_id = u.id and asd.customer_id = cus.id and ISNULL(cus.flexfield15,'N/A') = 'EGENTIC-DATASHARE-0001-01082017' and ch.contact_date BETWEEN '2017-08-23 00:00:00' and '2017-08-23 23:59:59'  and mc.assign = 1 ";
//        if (marketingId!=0)
//        { query += " and mar.id = " + marketingId + " ";}
//        query +=  ") t";
        String query ="select  count(*) from (\n" +
                        "select row_number() OVER(PARTITION BY asd.id ORDER BY ch.contact_date DESC) AS rn,mc.marketing_id,con.name,asd.id as assignment_id,con.status_id,NULLIF(cus.flexfield15,'') as flexfield15,mc.assign,ch.contact_date from contact_history as ch,assignment_detail as asd,contact_history_assignment as cha , contact_result as con ,customer as cus,marketing_customer as mc where ch.id = cha.contact_history_id and asd.id = cha.assignment_detail_id\n" +
                        "and ch.contact_date BETWEEN '"+ sdf.format(fromDate) +"' and '"+ sdf.format(toDate) +"' and con.id = ch.contact_result_id and asd.customer_id = cus.id and mc.customer_id = cus.id  and ISNULL(cus.flexfield15,'N/A') = '"+fx15+"' ";
                    
        if (marketingId!=0)
        { query += " and mc.marketing_id = " + marketingId + " ";}
                      
        query += ") as t where t.rn =1";

        return query;
    }
    
    public void reportListener(ActionEvent event) {
        generateXLS();
        FacesContext.getCurrentInstance().responseComplete();
    }

    private void generateXLS() {
        FileInputStream file = null;
        Date d = new Date();
        String start = sdf2.format(d);
        fileName = "Call_Code_By_Campaign_" + start + ".xls";
        try {
            file = new FileInputStream(new File(JSFUtil.getuploadPath(), "report" + "//CallCodeByCampaign/Call_Code_By_Campaign_List_report_template.xls"));
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFCellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderTop(CellStyle.BORDER_THIN);
            dataStyle.setBorderRight(CellStyle.BORDER_THIN);
            dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
            dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
            dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
            dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("application/vnd.ms-excel");
            FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            //Fill Header
            HSSFRow row;
            HSSFCell cell;
            //
            row = sheet.getRow(5);
            cell = row.createCell(1);
            cell.setCellValue("Call Code By Campaign");
            row = sheet.getRow(6);
            cell = row.createCell(1);
            String contactDate = sdf1.format(fromDate) + " - " + sdf1.format(toDate);
            cell.setCellValue(contactDate);
            cell = sheet.getRow(7).createCell(1);
            cell.setCellValue(marketingName);
            
            retrieveData(workbook, sheet);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(CallCodeByCampaign.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(CallCodeByCampaign.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void retrieveData(HSSFWorkbook workbook, HSSFSheet sheet) throws SQLException {
        HSSFRow dataRow;
        HSSFCell dataCell;

        HSSFCellStyle detailCellStyle = workbook.createCellStyle();
        detailCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        detailCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        
        HSSFCellStyle detailRightCellStyle = workbook.createCellStyle();
        detailRightCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailRightCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailRightCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailRightCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailRightCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailRightCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        detailRightCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        detailRightCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFCellStyle detailLeftCellStyle = workbook.createCellStyle();
        detailLeftCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        detailLeftCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailLeftCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailLeftCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailLeftCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailLeftCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
//        detailLeftCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        detailLeftCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFCellStyle detailRightCallCodeCellStyle = workbook.createCellStyle();
        detailRightCallCodeCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailRightCallCodeCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailRightCallCodeCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailRightCallCodeCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailRightCallCodeCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailRightCallCodeCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        detailRightCallCodeCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        detailRightCallCodeCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);


        HSSFCellStyle summaryCellStyle = workbook.createCellStyle();
        summaryCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        summaryCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        summaryCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        summaryCellStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        summaryCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFFont fontBold = workbook.createFont();
        fontBold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        
        HSSFCellStyle detailBoldRightCellStyle = workbook.createCellStyle();
        detailBoldRightCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailBoldRightCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailBoldRightCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailBoldRightCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailBoldRightCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailBoldRightCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        detailBoldRightCellStyle.setFont(fontBold);
        
        HSSFCellStyle detailBoldLightGreyRightCellStyle = workbook.createCellStyle();
        detailBoldLightGreyRightCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailBoldLightGreyRightCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailBoldLightGreyRightCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailBoldLightGreyRightCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailBoldLightGreyRightCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailBoldLightGreyRightCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        detailBoldLightGreyRightCellStyle.setFont(fontBold);
        detailBoldLightGreyRightCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        detailBoldLightGreyRightCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFCellStyle detailBoldLightGreyCenterCellStyle = workbook.createCellStyle();
        detailBoldLightGreyCenterCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        detailBoldLightGreyCenterCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailBoldLightGreyCenterCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailBoldLightGreyCenterCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailBoldLightGreyCenterCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailBoldLightGreyCenterCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        detailBoldLightGreyCenterCellStyle.setFont(fontBold);
        detailBoldLightGreyCenterCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        detailBoldLightGreyCenterCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFCellStyle detailBoldCenterCellStyle = workbook.createCellStyle();
        detailBoldCenterCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        detailBoldCenterCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailBoldCenterCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailBoldCenterCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailBoldCenterCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailBoldCenterCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        detailBoldCenterCellStyle.setFont(fontBold);


        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();           

            //rs = statement.executeQuery(this.getHeaderFxQuery());
            //rs = statement.executeQuery("{call callCodeByCampaignId(fromDate,toDate,marketingId,Type,fx15 )}");
            //Type = FindHeader , FindCallCode , FindFxValue , FindListUploaded
            rs = statement.executeQuery("{call callCodeByCampaignId('" +sdf.format(fromDate)+"','"+sdf.format(toDate)+"',"+ marketingId+",'FindHeader','' )}");
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCol = rsmd.getColumnCount();
            int numStartRow = 11;
            int numStartRowFxHeader = 10;
            int numStartRow3 = 12;
            int cellInx = 0;
//            List<Pair> headerFx = new ArrayList<Pair>();
            Map<String,Integer> headerFx = new HashMap<String,Integer>();
            Map<String,Integer> callCodeList = new HashMap<String,Integer>();
            List<String> arrayCallCode = new ArrayList<String>();
            List<String> arrayHeaderFx = new ArrayList<String>();
            
            List<Integer> arrayListUploaded = new ArrayList<Integer>();

            HSSFRow row;
            HSSFCell cell;
            sheet = workbook.getSheetAt(0);
            dataRow = sheet.createRow(numStartRowFxHeader);
            if(rs.next())
            {   
                cellInx = 2;
                do {
                        sheet.setColumnWidth(cellInx, 10000);
                        for (int i = 1; i <= numCol; i++) {
                            dataCell = dataRow.createCell(cellInx);
                            dataCell.setCellStyle(detailBoldLightGreyCenterCellStyle);
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "N/A";
                            dataCell.setCellValue(d);
                            
                        }
                            arrayHeaderFx.add(rs.getString(1));
                            headerFx.put(rs.getString(1),cellInx);
                            cellInx++;

                } while (rs.next());
            }
            
            //rs = statement.executeQuery(this.getCallCodeValueQuery());
            rs = statement.executeQuery("{call callCodeByCampaignId('" +sdf.format(fromDate)+"','"+sdf.format(toDate)+"',"+ marketingId+",'FindCallCode','' )}");
            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            
            if (rs.next()) {
                do {

                        dataRow = sheet.createRow(numStartRow);
                        cellInx = 0;
                        for (int i = 1; i <= numCol; i++) {
                            dataCell = dataRow.createCell(cellInx);
                            if(i == 1)
                            {
                                dataCell.setCellStyle(detailRightCallCodeCellStyle);
                            }
                            else
                            {
                                dataCell.setCellStyle(detailLeftCellStyle);
                            }
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "N/A";
                            dataCell.setCellValue(d);

                            cellInx++;
                        }
                            //Calculate Sum for each column
                            arrayCallCode.add(rs.getString(1));
                            callCodeList.put(rs.getString(1),numStartRow);
                            numStartRow++;

                } while (rs.next());
                //Summation total

            }
            
//            rs = statement.executeQuery(this.getFxValueQuery());
            rs = statement.executeQuery("{call callCodeByCampaignId('" +sdf.format(fromDate)+"','"+sdf.format(toDate)+"',"+ marketingId+",'FindFxValue','' )}");
            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            int startNumCol = 0;
            numStartRow = 11;
            int countRow = 0;
            int countCol = 2;
            int colStartMergeHeader =0 ;
            int colEndMergeHeader =0;
            String rsString;
                    if (rs.next()) {
                        for(int y=0;y<arrayHeaderFx.size();y++)
                        {   
                            countRow = 0;
                            if(y==0)
                            {
                                colStartMergeHeader = headerFx.get(arrayHeaderFx.get(y));
                                if(arrayHeaderFx.size()==1)
                                {
                                   colEndMergeHeader = headerFx.get(arrayHeaderFx.get(y)); 
                                }
                            }
                            else
                            {
                                colEndMergeHeader = headerFx.get(arrayHeaderFx.get(y));
                            }
                            
                            for(int x=11;x<arrayCallCode.size()+11;x++)
                            {
                                    if(x==11)
                                    {
                                        
                                            dataRow = sheet.getRow(9);
                                        
                                        dataCell = dataRow.createCell(headerFx.get(arrayHeaderFx.get(y)));
                                        dataCell.setCellValue(sdf1.format(fromDate) + " - " + sdf1.format(toDate));
                                        dataCell.setCellStyle(detailBoldLightGreyCenterCellStyle);
                                    }
                                    
                                    dataRow = sheet.getRow(x);
                                    dataCell = dataRow.createCell(headerFx.get(arrayHeaderFx.get(y)));
                                    
                                    if(rs.getString(3).isEmpty())
                                    {
                                        rsString = "";
                                    }
                                    else
                                    {
                                        rsString = rs.getString(3);
                                    }
                                    
                                    if(!(startNumCol<=numCol))
                                    {
                                        rsString = "";
                                    }

                                    if(x == callCodeList.get(arrayCallCode.get(countRow))&& countCol == headerFx.get(arrayHeaderFx.get(y)) && rsString.equals(arrayHeaderFx.get(y)) && rs.getString(1).equals(arrayCallCode.get(countRow)))
                                    {   
                                        dataCell.setCellStyle(detailRightCallCodeCellStyle);
                                        Integer d = (rs.getString(4) != null && !rs.getString(4).isEmpty()) ? Integer.parseInt(rs.getString(4)) : 0;                    
                                        dataCell.setCellValue(d);
                                        countRow++;
                                        if(startNumCol<numCol)
                                        {
                                            startNumCol++;
                                        }
                                        
                                        if(!rs.isLast())
                                        {
                                            rs.next();
                                        }
                                        
                                    }
                                    else
                                    {
    //                                    dataCell = dataRow.createCell(x);
                                        dataCell.setCellStyle(detailRightCallCodeCellStyle);
                                        dataCell.setCellValue(0);
                                        countRow++;
                                    }
                            }
                            
                             countCol++;
                        }
                        
                        //Merge Contact Date Header Column
                        sheet.addMergedRegion(new CellRangeAddress(9 , 9, colStartMergeHeader, colEndMergeHeader));
                           
                            
                    }
                    
                    //Total Used
                    dataRow = sheet.createRow(arrayCallCode.size()+11);                                        
                    dataCell = dataRow.createCell(0);
                    dataCell.setCellStyle(detailBoldRightCellStyle);
//                    dataCell.setCellStyle(fontBold);
                    dataCell.setCellValue("Total Used");
                    dataCell = dataRow.createCell(1);
                    dataCell.setCellStyle(detailBoldRightCellStyle);
//                    dataCell.setFont(fontBold);
                    dataCell.setCellValue("Total Used");
                    sheet.addMergedRegion(new CellRangeAddress(arrayCallCode.size()+11 , arrayCallCode.size()+11, 0, 1));
                    int startRowCallCode = colStartMergeHeader;
                    if(startRowCallCode != 0)
                    {
                        for(int y=0;y<arrayHeaderFx.size();y++)
                        { 
                            dataCell = dataRow.createCell(headerFx.get(arrayHeaderFx.get(y)));
                            dataCell.setCellStyle(detailRightCellStyle);
                            dataCell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                            dataCell.setCellFormula("SUM("+ CellReference.convertNumToColString(startRowCallCode) +"12:"+CellReference.convertNumToColString(startRowCallCode)+(arrayCallCode.size()+11)+")" );
                            startRowCallCode++;
                        }

                            dataCell = dataRow.createCell(arrayHeaderFx.size()+2);
                            dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                            dataCell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                            dataCell.setCellFormula("SUM("+ CellReference.convertNumToColString(colStartMergeHeader) +(arrayCallCode.size()+12)+":"+CellReference.convertNumToColString(colEndMergeHeader)+(arrayCallCode.size()+12)+")" );
                            dataCell = dataRow.createCell(arrayHeaderFx.size()+3);
                            dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                            dataCell.setCellValue("");
                    }
                    else
                    {
                        dataCell = dataRow.createCell(arrayHeaderFx.size()+2);
                        dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                        dataCell.setCellValue(0);
                        dataCell = dataRow.createCell(arrayHeaderFx.size()+3);
                        dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                        dataCell.setCellValue("");
                    }
                        
                    //List Uploaded
                    dataRow = sheet.createRow(arrayCallCode.size()+12);                                        
                    dataCell = dataRow.createCell(0);
                    dataCell.setCellStyle(detailBoldRightCellStyle);
                    dataCell.setCellValue("List Uploaded");
                    dataCell = dataRow.createCell(1);
                    dataCell.setCellStyle(detailBoldRightCellStyle);
                    dataCell.setCellValue("List Uploaded");
                    sheet.addMergedRegion(new CellRangeAddress(arrayCallCode.size()+12 , arrayCallCode.size()+12, 0, 1));
                    if(startRowCallCode != 0)
                    {
                        for(int y=0;y<arrayHeaderFx.size();y++)
                        { 
                            //rs = statement.executeQuery(this.getListUploadedQuery(arrayHeaderFx.get(y)));
                            rs = statement.executeQuery("{call callCodeByCampaignId('" +sdf.format(fromDate)+"','"+sdf.format(toDate)+"',"+ marketingId+",'FindListUploaded','"+arrayHeaderFx.get(y)+"' )}");
                            rs.next();
                            dataCell = dataRow.createCell(y+2);
                            dataCell.setCellStyle(detailRightCellStyle);
                            dataCell.setCellValue(Integer.parseInt(rs.getString(1)));
                            arrayListUploaded.add(Integer.parseInt(rs.getString(1)));
                        }

                        dataCell = dataRow.createCell(arrayHeaderFx.size()+2);
                        dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                        dataCell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        dataCell.setCellFormula("SUM("+ CellReference.convertNumToColString(colStartMergeHeader) +(arrayCallCode.size()+13)+":"+CellReference.convertNumToColString(colEndMergeHeader)+(arrayCallCode.size()+13)+")" );
                        dataCell = dataRow.createCell(arrayHeaderFx.size()+3);
                        dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                        dataCell.setCellValue("");
                    }
                    else
                    {
                        dataCell = dataRow.createCell(arrayHeaderFx.size()+2);
                        dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                        dataCell.setCellValue(0);
                        dataCell = dataRow.createCell(arrayHeaderFx.size()+3);
                        dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                        dataCell.setCellValue("");
                    }
                    
                    //Not Used
                    dataRow = sheet.createRow(arrayCallCode.size()+13);                                        
                    dataCell = dataRow.createCell(0);
                    dataCell.setCellStyle(detailBoldRightCellStyle);
                    dataCell.setCellValue("Not Used");
                    dataCell = dataRow.createCell(1);
                    dataCell.setCellStyle(detailBoldRightCellStyle);
                    dataCell.setCellValue("Not Used");
                    sheet.addMergedRegion(new CellRangeAddress(arrayCallCode.size()+13 , arrayCallCode.size()+13, 0, 1));
                    startRowCallCode = colStartMergeHeader;
                    if(startRowCallCode != 0)
                    {
                        for(int y=0;y<arrayHeaderFx.size();y++)
                        { 
                            dataCell = dataRow.createCell(headerFx.get(arrayHeaderFx.get(y)));
                            dataCell.setCellStyle(detailRightCellStyle);
                            dataCell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                            dataCell.setCellFormula("ABS("+ "SUM("+ CellReference.convertNumToColString(startRowCallCode) +"12:"+CellReference.convertNumToColString(startRowCallCode)+(arrayCallCode.size()+11)+") -"+arrayListUploaded.get(y)+")" );
                            startRowCallCode++;
                        }

                        dataCell = dataRow.createCell(arrayHeaderFx.size()+2);
                        dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                        dataCell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        dataCell.setCellFormula("SUM("+ CellReference.convertNumToColString(colStartMergeHeader) +(arrayCallCode.size()+14)+":"+CellReference.convertNumToColString(colEndMergeHeader)+(arrayCallCode.size()+14)+")" );
                        dataCell = dataRow.createCell(arrayHeaderFx.size()+3);
                        dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                        dataCell.setCellValue("");
                    }
                    else
                    {
                        dataCell = dataRow.createCell(arrayHeaderFx.size()+2);
                        dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                        dataCell.setCellValue(0);
                        dataCell = dataRow.createCell(arrayHeaderFx.size()+3);
                        dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                        dataCell.setCellValue("");
                    }
                    
                    // %ListUsed 10/1008*100
                    dataRow = sheet.createRow(arrayCallCode.size()+14);                                        
                    dataCell = dataRow.createCell(0);
                    dataCell.setCellStyle(detailBoldRightCellStyle);
                    dataCell.setCellValue("%List Used");
                    dataCell = dataRow.createCell(1);
                    dataCell.setCellStyle(detailBoldRightCellStyle);
                    dataCell.setCellValue("%List Used");
                    sheet.addMergedRegion(new CellRangeAddress(arrayCallCode.size()+14 , arrayCallCode.size()+14, 0, 1));
                    startRowCallCode = colStartMergeHeader;
                    
                    if(startRowCallCode != 0)
                    {
                        for(int y=0;y<arrayHeaderFx.size();y++)
                        { 
                            dataCell = dataRow.createCell(headerFx.get(arrayHeaderFx.get(y)));
                            dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                            dataCell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                            dataCell.setCellFormula("CONCATENATE(ROUND(ABS("+ "SUM("+ CellReference.convertNumToColString(startRowCallCode) +"12:"+CellReference.convertNumToColString(startRowCallCode)+(arrayCallCode.size()+11)+") /"+arrayListUploaded.get(y)+")*100,2),\"%\")" );
                            startRowCallCode++;
                        }

                        dataCell = dataRow.createCell(arrayHeaderFx.size()+2);
                        dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                        dataCell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        dataCell.setCellFormula("CONCATENATE(ROUND(ABS("+ CellReference.convertNumToColString(colEndMergeHeader+1) +(arrayCallCode.size()+12)+"/"+CellReference.convertNumToColString(colEndMergeHeader+1)+(arrayCallCode.size()+13)+")*100,2),\"%\")" );
                        dataCell = dataRow.createCell(arrayHeaderFx.size()+3);
                        dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                        dataCell.setCellValue("");
                    }
                    else
                    {
                        dataCell = dataRow.createCell(arrayHeaderFx.size()+2);
                        dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                        dataCell.setCellValue("0%");
                        dataCell = dataRow.createCell(arrayHeaderFx.size()+3);
                        dataCell.setCellStyle(detailBoldLightGreyRightCellStyle);
                        dataCell.setCellValue("");
                    }
                    
                    //Total
                    dataRow = sheet.getRow(9);
                    dataCell = dataRow.createCell(arrayHeaderFx.size()+2);
                    dataCell.setCellStyle(detailBoldLightGreyCenterCellStyle);
                    dataCell.setCellValue("Total");
                    dataCell = dataRow.createCell(arrayHeaderFx.size()+3);
                    dataCell.setCellStyle(detailBoldLightGreyCenterCellStyle);
                    dataCell.setCellValue("Total");
                    dataRow = sheet.getRow(10);
                    dataCell = dataRow.createCell(arrayHeaderFx.size()+2);
                    dataCell.setCellStyle(detailBoldLightGreyCenterCellStyle);
                    dataCell.setCellValue("Total");
                    dataCell = dataRow.createCell(arrayHeaderFx.size()+3);
                    dataCell.setCellStyle(detailBoldLightGreyCenterCellStyle);
                    dataCell.setCellValue("Total");
                    sheet.addMergedRegion(new CellRangeAddress(9 , 10, arrayHeaderFx.size()+2, arrayHeaderFx.size()+3));
                    startRowCallCode = colStartMergeHeader;
                    
                    if(startRowCallCode != 0)
                    {
                        for(int x=11;x<arrayCallCode.size()+11;x++)
                        {
                            dataRow = sheet.getRow(x);
                            dataCell = dataRow.createCell(arrayHeaderFx.size()+2);
                            dataCell.setCellStyle(detailBoldRightCellStyle);
                            dataCell.setCellFormula("SUM("+ CellReference.convertNumToColString(startRowCallCode) +(x+1)+":"+CellReference.convertNumToColString(colEndMergeHeader)+(x+1)+")" );
                            dataCell = dataRow.createCell(arrayHeaderFx.size()+3);
                            dataCell.setCellStyle(detailBoldRightCellStyle);
                            dataCell.setCellFormula("CONCATENATE(ROUND(ABS("+ "SUM("+ CellReference.convertNumToColString(startRowCallCode) +(x+1)+":"+CellReference.convertNumToColString(colEndMergeHeader)+(x+1)+")"+" /"+"SUM("+ CellReference.convertNumToColString(colStartMergeHeader) +(arrayCallCode.size()+12)+":"+CellReference.convertNumToColString(colEndMergeHeader)+(arrayCallCode.size()+12)+")"+")*100,2),\"%\")" );
                        }
                    }
                    else
                    {
                        if(arrayCallCode.size() != 0)
                        {
                            for(int x=11;x<arrayCallCode.size()+11;x++)
                            {
                                dataRow = sheet.getRow(x);
                                dataCell = dataRow.createCell(arrayHeaderFx.size()+2);
                                dataCell.setCellStyle(detailBoldRightCellStyle);
                                dataCell.setCellValue(0);
                                dataCell = dataRow.createCell(arrayHeaderFx.size()+3);
                                dataCell.setCellStyle(detailBoldRightCellStyle);
                                dataCell.setCellValue("0%");
                            }
                        }
                        else
                        {    
                            dataCell = dataRow.createCell(arrayHeaderFx.size()+2);
                            dataCell.setCellStyle(detailBoldRightCellStyle);
                            dataCell.setCellValue(0);
                            dataCell = dataRow.createCell(arrayHeaderFx.size()+3);
                            dataCell.setCellStyle(detailBoldRightCellStyle);
                            dataCell.setCellValue("");
                        }
                    }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
