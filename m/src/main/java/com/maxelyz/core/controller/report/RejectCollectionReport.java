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
import java.util.Date;
import java.util.LinkedHashMap;
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
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

@ManagedBean
@RequestScoped
public class RejectCollectionReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(RejectCollectionReport.class);
    
    private SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
    private String fileName = "";
    DecimalFormat df = new DecimalFormat("#,##0.00");
    DecimalFormat df1 = new DecimalFormat("#,###");
    

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:rejectcollectionreport:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/rejectcollectionreport.jasper";
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
    
    public void reportListener(ActionEvent event) {
        generateXLS();
        FacesContext.getCurrentInstance().responseComplete();
    }

    private void generateXLS() {
        FileInputStream file = null;
        Date d = new Date();
        String start = sdf2.format(d);
        fileName = "Reject_Collection_Report_" + start + ".xls";
        try {
            file = new FileInputStream(new File(JSFUtil.getuploadPath(), "report" + "/RejectCollectionReport/Reject_collection_report_template.xls"));
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
            //Reject
//            row = sheet.getRow(5);
//            cell = row.createCell(1);
//            cell.setCellValue("Reject Collection Report");
            row = sheet.getRow(6);
            cell = row.createCell(1);
            String contactDate = sdf1.format(fromDate) + " - " + sdf1.format(toDate);
            cell.setCellValue(contactDate);
            cell = sheet.getRow(7).createCell(1);
            cell.setCellValue(campaignName);
            cell = sheet.getRow(8).createCell(1);
            cell.setCellValue(marketingName);
            
            //Stop Case 
            sheet = workbook.getSheetAt(1);
//            row = sheet.getRow(5);
//            cell = row.createCell(1);
//            cell.setCellValue("Reject Collection Report");
            row = sheet.getRow(6);
            cell = row.createCell(1);
            contactDate = sdf1.format(fromDate) + " - " + sdf1.format(toDate);
            cell.setCellValue(contactDate);
            cell = sheet.getRow(7).createCell(1);
            cell.setCellValue(campaignName);
            cell = sheet.getRow(8).createCell(1);
            cell.setCellValue(marketingName);
            
//            //Success Case 
//            sheet = workbook.getSheetAt(2);
////            row = sheet.getRow(5);
////            cell = row.createCell(1);
////            cell.setCellValue("Reject Collection Report");
//            row = sheet.getRow(6);
//            cell = row.createCell(1);
//            contactDate = sdf1.format(fromDate) + " - " + sdf1.format(toDate);
//            cell.setCellValue(contactDate);
//            cell = sheet.getRow(7).createCell(1);
//            cell.setCellValue(marketingName);
//            cell = sheet.getRow(8).createCell(1);
//            cell.setCellValue(campaignName);
            
            retrieveData(workbook, sheet);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RejectCollectionReport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(RejectCollectionReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void retrieveData(HSSFWorkbook workbook, HSSFSheet sheet) throws SQLException {
        HSSFRow dataRow;
        HSSFCell dataCell;

        HSSFCellStyle detailCellStyle = workbook.createCellStyle();
        detailCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        
        HSSFCellStyle detailLeftCellStyle = workbook.createCellStyle();
        detailLeftCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        detailLeftCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailLeftCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailLeftCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailLeftCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailLeftCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        
        HSSFCellStyle detailCenterCellStyle = workbook.createCellStyle();
        detailCenterCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        detailCenterCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailCenterCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailCenterCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailCenterCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailCenterCellStyle.setBorderLeft(CellStyle.BORDER_THIN);

        HSSFCellStyle summaryCellStyle = workbook.createCellStyle();
        summaryCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        summaryCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        summaryCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        summaryCellStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        summaryCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();           
            rs = statement.executeQuery("{call rejectCollectionReport('" +sdf.format(fromDate)+"','"+sdf.format(toDate)+"',"+ marketingId+","+campaignId + ")}");
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCol = rsmd.getColumnCount();
            int numStartRow1 = 12;
            int numStartRow2 = 12;
            int numStartRow3 = 12;
            int cellInx = 0;
            HSSFRow row;
            HSSFCell cell;

            if (rs.next()) {
                do {
                    if(rs.getString(1).equals("reject"))
                    {   sheet = workbook.getSheetAt(0);
                        dataRow = sheet.createRow(numStartRow1);
                        cellInx = 0;
                        for (int i = 2; i <= numCol; i++) {
                            dataCell = dataRow.createCell(cellInx);
                             
                            if (cellInx == 7)
                            {
                                dataCell.setCellStyle(detailCellStyle);
                            }
                            else if(cellInx == 6 || cellInx == 10 || cellInx == 11 || cellInx == 12)
                            {
                                dataCell.setCellStyle(detailLeftCellStyle);
                            }
                            else
                            {
                                dataCell.setCellStyle(detailCenterCellStyle);
                            }
                            
                                String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                                dataCell.setCellValue(d);

                            cellInx++;
                        }
                        //Calculate Sum for each column

                        numStartRow1++;
                    }
                    else if(rs.getString(1).equals("stop"))
                    {   sheet = workbook.getSheetAt(1);
                        dataRow = sheet.createRow(numStartRow2);
                        cellInx = 0;
                        for (int i = 2; i <= numCol; i++) {
                            dataCell = dataRow.createCell(cellInx);
                            if (cellInx == 7)
                            {
                                dataCell.setCellStyle(detailCellStyle);
                            }
                            else if(cellInx == 6 || cellInx == 10 || cellInx == 11 || cellInx == 12)
                            {
                                dataCell.setCellStyle(detailLeftCellStyle);
                            }
                            else
                            {
                                dataCell.setCellStyle(detailCenterCellStyle);
                            }
                                String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                                dataCell.setCellValue(d);

                            cellInx++;
                        }
                        //Calculate Sum for each column

                        numStartRow2++;
                    }
//                    else if(rs.getString(1).equals("success"))
//                    {   sheet = workbook.getSheetAt(2);
//                        dataRow = sheet.createRow(numStartRow3);
//                        cellInx = 0;
//                        for (int i = 2; i <= numCol; i++) {
//                            dataCell = dataRow.createCell(cellInx);
//                            dataCell.setCellStyle(detailCellStyle);
//                                String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
//                                dataCell.setCellValue(d);
//
//                            cellInx++;
//                        }
//                        //Calculate Sum for each column
//
//                        numStartRow3++;
//                    }
                        

                } while (rs.next());
                //Summation total

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
