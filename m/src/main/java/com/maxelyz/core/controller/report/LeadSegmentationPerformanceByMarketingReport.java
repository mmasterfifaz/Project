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
import java.sql.CallableStatement;

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
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;

/**
 *
 * @author admin
 */
@ManagedBean
@RequestScoped
public class LeadSegmentationPerformanceByMarketingReport extends AbstractReportBaseController {

    private static Logger log = Logger.getLogger(LeadSegmentationPerformanceByMarketingReport.class);
    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd__HHmmss", Locale.US);
    private SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private String fileName = "";
    DecimalFormat df = new DecimalFormat("#,##0.00");
    DecimalFormat df1 = new DecimalFormat("#,###");

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:leadsegmentationperformancebymarketing:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        super.init();
    }

    @Override
    protected String getReportPath() {
        return "/report/LeadSegmentationPerformanceByMarketingReport.jasper";
    }

    @Override
    protected Map getParameterMap() {
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
        return parameterMap;
    }

    @Override
    protected String getQuery() {

        String select = "";
        String query = select;

        return query;
    }

    public void reportListener(ActionEvent event) {
        generateXLS();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void initSummation() {

    }

    private void generateXLS() {
        initSummation();
        FileInputStream file = null;
        Date d = new Date();
        String start = sdf2.format(d);
        fileName = "Lead_Segmentation_Performance_By_Marketing_List_report_" + start + ".xls";
        try {
            file = new FileInputStream(new File(JSFUtil.getuploadPath(), "report" + "/LeadSegmentationPerformanceByMarketing/Lead_Segmentation_Perfromance_By_Marketing_report_template.xls"));
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(CellStyle.ALIGN_LEFT);
            titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("application/vnd.ms-excel");
            FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            //Fill Header
            String contactDate = sdf3.format(fromDate) + " - " + sdf3.format(toDate);
            sheet.getRow(3).getCell(1).setCellValue("Leads Segmentation Performance By Marketing Report");
            sheet.getRow(4).getCell(1).setCellValue(contactDate);;

            // sheet 1 title report by marketing fx6
            sheet = workbook.getSheetAt(1);
            sheet.getRow(3).getCell(1).setCellValue("Leads Segmentation Performance By Marketing Report");
            sheet.getRow(4).getCell(1).setCellValue(contactDate);

            retrieveData(workbook, sheet);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LeadSegmentationPerformanceByMarketingReport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(LeadSegmentationPerformanceByMarketingReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void retrieveData(HSSFWorkbook workbook, HSSFSheet sheet) throws SQLException {
        HSSFRow dataRow;
        HSSFCell dataCell;

        HSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        HSSFCellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
        dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        dataStyle.setBorderTop(CellStyle.BORDER_THIN);
        dataStyle.setBorderRight(CellStyle.BORDER_THIN);
        dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
        dataStyle.setBorderBottom(CellStyle.BORDER_THIN);

        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headerStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerStyle.setBorderRight(CellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle boldStyleRight = workbook.createCellStyle();
        boldStyleRight.setAlignment(CellStyle.ALIGN_RIGHT);
        boldStyleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        boldStyleRight.setBorderTop(CellStyle.BORDER_THIN);
        boldStyleRight.setBorderRight(CellStyle.BORDER_THIN);
        boldStyleRight.setBorderLeft(CellStyle.BORDER_THIN);
        boldStyleRight.setBorderBottom(CellStyle.BORDER_THIN);
        boldStyleRight.setFont(font);

        HSSFCellStyle detailCellStyle = workbook.createCellStyle();
        detailCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderLeft(CellStyle.BORDER_THIN);

        HSSFCellStyle descriptionCellStyle = workbook.createCellStyle();
        descriptionCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        descriptionCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        descriptionCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        descriptionCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        descriptionCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        descriptionCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        descriptionCellStyle.setFont(font);

        HSSFCellStyle summaryCellStyle = workbook.createCellStyle();
        summaryCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        summaryCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        summaryCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        summaryCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        summaryCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle sumRowStyle = workbook.createCellStyle();
        sumRowStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        sumRowStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        sumRowStyle.setBorderTop(CellStyle.BORDER_THIN);
        sumRowStyle.setBorderRight(CellStyle.BORDER_THIN);
        sumRowStyle.setBorderBottom(CellStyle.BORDER_THIN);
        sumRowStyle.setBorderLeft(CellStyle.BORDER_THIN);
        sumRowStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        sumRowStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle marketStyle = workbook.createCellStyle();
        marketStyle.setAlignment(CellStyle.ALIGN_CENTER);
        marketStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        marketStyle.setBorderTop(CellStyle.BORDER_THIN);
        marketStyle.setBorderRight(CellStyle.BORDER_THIN);
        marketStyle.setBorderBottom(CellStyle.BORDER_THIN);
        marketStyle.setBorderLeft(CellStyle.BORDER_THIN);
        marketStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        marketStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        boldStyleRight.setFont(font);

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();

            sheet = workbook.getSheetAt(0);
            CallableStatement callStatement = connection.prepareCall("{call [dbo].[sp_fwd_lead_segmentation_performance_by_marketing_header](?,?,?)}");
            callStatement.setString(1, sdf1.format(fromDate));
            callStatement.setString(2, sdf1.format(toDate));
            callStatement.setString(3, "1");

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure header");
            }

            // create header
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCol = rsmd.getColumnCount();
            int startRow = 7;
            int startCol = 0;
            int rowInx = startRow;
            int colInx = startCol;
            int lastCol = 0;
            int lastRow = 0;

            if (rs.next()) {
                do {
                    dataRow = sheet.getRow(rowInx);
                    for (int i = 1; i <= numCol; i++) {
                        colInx++;
                        dataCell = dataRow.createCell(colInx);

                        if (i <= numCol) {
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                            dataCell.setCellValue(d);
                            dataCell.setCellStyle(headerStyle);
                            lastCol = colInx;

                            sheet.getRow(rowInx - 1).createCell(colInx + 1).setCellValue("");
                            sheet.getRow(rowInx - 1).getCell(colInx + 1).setCellStyle(headerStyle);
                        }
                    }

                } while (rs.next());

                // set contact date
                String contactDate = sdf3.format(fromDate) + " - " + sdf3.format(toDate);
                sheet.getRow(rowInx - 1).createCell(startCol + 1).setCellValue(contactDate);
                sheet.getRow(rowInx - 1).getCell(startCol + 1).setCellStyle(headerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowInx - 1, rowInx - 1, startCol + 1, lastCol));

                // set total header
                sheet.getRow(rowInx - 1).createCell(colInx + 1).setCellValue("Total");
                sheet.getRow(rowInx - 1).getCell(colInx + 1).setCellStyle(headerStyle);
                sheet.getRow(rowInx).createCell(colInx + 1).setCellValue("");
                sheet.getRow(rowInx).getCell(colInx + 1).setCellStyle(headerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowInx - 1, rowInx, colInx + 1, colInx + 1));
                
                // set style central
                sheet.getRow(rowInx - 1).getCell(startCol).setCellStyle(headerStyle);
                sheet.getRow(rowInx).getCell(startCol).setCellStyle(headerStyle);
            }

            // create data leads
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_lead_segmentation_performance_by_marketing_data](?,?,?)}");
            callStatement.setString(1, sdf1.format(fromDate));
            callStatement.setString(2, sdf1.format(toDate));
            callStatement.setString(3, "1");

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            startCol = 1;
            startRow = 8;
            colInx = startCol;
            rowInx = startRow;

            int leadsReceivedRow = 0;
            int leadsUsedRow = 0;
            int contactableRow = 0;
            int grossAppRow = 0;
            int grossApeRow = 0;
            int netApeRow = 0;
            int netAppROw = 0;

            if (rs.next()) {
                do {
                    rowInx = startRow;

                    for (int i = 2; i <= 8; i++) {
                        sheet.getRow(rowInx);

                        if (i == 2) {

                            //insert leads received
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            rowInx++;
                            lastRow = rowInx;
                            leadsReceivedRow = rowInx;

                        }

                        if (i == 3) {

                            //insert leads used
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            rowInx++;
                            lastRow = rowInx;
                            leadsUsedRow = rowInx;

                            //insert % leads used = leads used / leads receiced
                            sheet.getRow(lastRow).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow - 1, CellReference.convertNumToColString(lastCol), lastRow, CellReference.convertNumToColString(lastCol), lastRow - 1));
                            sheet.getRow(lastRow).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            rowInx++;
                            lastRow = rowInx;

                        }

                        if (i == 4) {

                            //insert contactable
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            rowInx++;
                            lastRow = rowInx;
                            contactableRow = rowInx;

                            //insert % contactable = contactable / leads used
                            sheet.getRow(lastRow).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow - 2, CellReference.convertNumToColString(lastCol), lastRow, CellReference.convertNumToColString(lastCol), lastRow - 2));
                            sheet.getRow(lastRow).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            rowInx += 2;
                            lastRow = rowInx;

                        }

                        if (i == 5) {

                            //insert gross app
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                            //insert % response = gross app / contactable
                            sheet.getRow(lastRow - 1).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow - 1).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow - 2, CellReference.convertNumToColString(lastCol), lastRow + 1, CellReference.convertNumToColString(lastCol), lastRow - 2));
                            sheet.getRow(lastRow - 1).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow - 1).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            rowInx++;
                            lastRow = rowInx;
                            grossAppRow = rowInx;

                        }

                        if (i == 6) {

                            //insert gross ape
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getDouble(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            rowInx++;
                            lastRow = rowInx;
                            grossApeRow = rowInx;

                            //insert AARP = gross ape / gross app
                            sheet.getRow(lastRow).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow - 1, CellReference.convertNumToColString(lastCol), lastRow, CellReference.convertNumToColString(lastCol), lastRow - 1));
                            sheet.getRow(lastRow).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            rowInx++;
                            lastRow = rowInx;

                            //insert % list conversion = gross app / leads used
                            sheet.getRow(lastRow).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow - 7, CellReference.convertNumToColString(lastCol), lastRow - 2, CellReference.convertNumToColString(lastCol), lastRow - 7));
                            sheet.getRow(lastRow).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            rowInx += 2;
                            lastRow = rowInx;

                        }

                        if (i == 7) {

                            //insert net APE
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getDouble(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            rowInx++;
                            lastRow = rowInx;
                            netApeRow = rowInx;

                        }

                        if (i == 8) {

                            //insert net app
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            rowInx++;
                            lastRow = rowInx;
                            netAppROw = rowInx;

                            //insert % collection = net app / gross app
                            sheet.getRow(lastRow - 3).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow - 3).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow - 6, CellReference.convertNumToColString(lastCol), lastRow, CellReference.convertNumToColString(lastCol), lastRow - 6));
                            sheet.getRow(lastRow - 3).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow - 3).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                            //insert AARP = net ape / net app
                            sheet.getRow(lastRow).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow, CellReference.convertNumToColString(lastCol), lastRow - 1, CellReference.convertNumToColString(lastCol), lastRow));
                            sheet.getRow(lastRow).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            rowInx++;
                            lastRow = rowInx;

                            //insert % list convertion
                            sheet.getRow(lastRow).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow - 12, CellReference.convertNumToColString(lastCol), lastRow - 1, CellReference.convertNumToColString(lastCol), lastRow - 12));
                            sheet.getRow(lastRow).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            rowInx++;
                            lastRow = rowInx;

                        }

                        // get count last row, last col                        
                        lastCol = colInx;

                    }
                    colInx++;

                } while (rs.next());

            }

            // insert product and data count
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_lead_segmentation_performance_by_marketing_product](?,?,?)}");
            callStatement.setString(1, sdf1.format(fromDate));
            callStatement.setString(2, sdf1.format(toDate));
            callStatement.setString(3, "1");

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure product");
            }
            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();

            if (rs.next()) {

                do {

                    sheet.createRow(rowInx);
                    colInx = 0;

                    for (int i = 1; i <= numCol; i++) {

                        if (i == 1) {

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getString(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(descriptionCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("-"));

                        }

                        if (i > 1 && i <= numCol) {

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                        }

                        colInx++;
                    }
                    rowInx++;
                    lastRow = rowInx;

                } while (rs.next());
            }

            //insert Revenue/TSR
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_lead_segmentation_performance_by_marketing_tsr](?,?,?)}");
            callStatement.setString(1, sdf1.format(fromDate));
            callStatement.setString(2, sdf1.format(toDate));
            callStatement.setString(3, "1");

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure tsr");
            }
            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            colInx = 1;
            sheet.createRow(lastRow);
            if (rs.next()) {

                do {
                    // get value from column in DB
                    int tsr = rs.getInt(numCol);

                    //for (int i = 1; i <= numCol; i++) {
                    sheet.getRow(lastRow).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(lastRow).getCell(colInx).setCellFormula(String.format("IF(%d > 0,%s%d/%d, 0)", tsr, CellReference.convertNumToColString(colInx), netApeRow, tsr));
                    sheet.getRow(lastRow).getCell(colInx).setCellStyle(detailCellStyle);
                    CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));

                    colInx++;
                    //}

                } while (rs.next());
            }
            //inset header Revenue/TSR
            colInx = 0;
            sheet.getRow(lastRow).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
            sheet.getRow(lastRow).getCell(colInx).setCellValue("Revenue/TSR");
            sheet.getRow(lastRow).getCell(colInx).setCellStyle(descriptionCellStyle);

            rowInx++;
            lastRow = rowInx;

            //insert header Revenue/List
            sheet.createRow(lastRow);
            colInx = 0;
            sheet.getRow(lastRow).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
            sheet.getRow(lastRow).getCell(colInx).setCellValue("Revenue/List");
            sheet.getRow(lastRow).getCell(colInx).setCellStyle(descriptionCellStyle);
            colInx++;
            // insert Revenue/List
            for (int i = 1; i <= lastCol; i++) {

                sheet.getRow(lastRow).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                sheet.getRow(lastRow).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(colInx), leadsUsedRow, CellReference.convertNumToColString(colInx), netApeRow, CellReference.convertNumToColString(colInx), leadsUsedRow));
                sheet.getRow(lastRow).getCell(colInx).setCellStyle(detailCellStyle);
                CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                colInx++;
            }

            rowInx++;
            lastRow = rowInx;

            //insert summary all row
            for (int i = 9; i <= lastRow; i++) {

                int j = lastCol + 1;
                colInx = 1;

                if (i == 11) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), leadsReceivedRow, CellReference.convertNumToColString(j), leadsUsedRow, CellReference.convertNumToColString(j), leadsReceivedRow));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else if (i == 13) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), leadsUsedRow, CellReference.convertNumToColString(j), contactableRow, CellReference.convertNumToColString(j), leadsUsedRow));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else if (i == 14) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), contactableRow, CellReference.convertNumToColString(j), grossAppRow, CellReference.convertNumToColString(j), contactableRow));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else if (i == 17) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), grossAppRow, CellReference.convertNumToColString(j), grossApeRow, CellReference.convertNumToColString(j), grossAppRow));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else if (i == 18) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), leadsUsedRow, CellReference.convertNumToColString(j), grossAppRow, CellReference.convertNumToColString(j), leadsUsedRow));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else if (i == 19) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), grossAppRow, CellReference.convertNumToColString(j), netAppROw, CellReference.convertNumToColString(j), grossAppRow));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else if (i == 22) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), netAppROw, CellReference.convertNumToColString(j), netApeRow, CellReference.convertNumToColString(j), netAppROw));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else if (i == 23) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), leadsUsedRow, CellReference.convertNumToColString(j), netAppROw, CellReference.convertNumToColString(j), leadsUsedRow));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(colInx), i, CellReference.convertNumToColString(lastCol), i));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                }

                if (sheet.getRow(i - 1).getCell(j - 1).getCellStyle().getDataFormatString().equalsIgnoreCase("#,##0")) {
                    CellUtil.setCellStyleProperty(sheet.getRow(i - 1).getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                } else if (sheet.getRow(i - 1).getCell(j - 1).getCellStyle().getDataFormatString().equalsIgnoreCase("#,##0.00")) {
                    CellUtil.setCellStyleProperty(sheet.getRow(i - 1).getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                } else if (sheet.getRow(i - 1).getCell(j - 1).getCellStyle().getDataFormatString().equalsIgnoreCase("0.00%")) {
                    CellUtil.setCellStyleProperty(sheet.getRow(i - 1).getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                }
            }

            // create sheet 1
            sheet = workbook.getSheetAt(1);
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_lead_segmentation_performance_by_marketing_header](?,?,?)}");
            callStatement.setString(1, sdf1.format(fromDate));
            callStatement.setString(2, sdf1.format(toDate));
            callStatement.setString(3, "2");

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure header");
            }

            // create header
            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            startRow = 7;
            startCol = 0;
            rowInx = startRow;
            colInx = startCol;
            lastCol = 0;
            lastRow = 0;

            if (rs.next()) {
                do {
                    dataRow = sheet.getRow(rowInx);
                    for (int i = 1; i <= numCol; i++) {
                        colInx++;
                        dataCell = dataRow.createCell(colInx);

                        if (i <= numCol) {
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                            dataCell.setCellValue(d);
                            dataCell.setCellStyle(headerStyle);
                            lastCol = colInx;

                            sheet.getRow(rowInx - 1).createCell(colInx + 1).setCellValue("");
                            sheet.getRow(rowInx - 1).getCell(colInx + 1).setCellStyle(headerStyle);
                        }
                    }

                } while (rs.next());

                // set contact date
                String contactDate = sdf3.format(fromDate) + " - " + sdf3.format(toDate);
                sheet.getRow(rowInx - 1).createCell(startCol + 1).setCellValue(contactDate);
                sheet.getRow(rowInx - 1).getCell(startCol + 1).setCellStyle(headerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowInx - 1, rowInx - 1, startCol + 1, lastCol));

                // set total header
                sheet.getRow(rowInx - 1).createCell(colInx + 1).setCellValue("Total");
                sheet.getRow(rowInx - 1).getCell(colInx + 1).setCellStyle(headerStyle);
                sheet.getRow(rowInx).createCell(colInx + 1).setCellValue("");
                sheet.getRow(rowInx).getCell(colInx + 1).setCellStyle(headerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowInx - 1, rowInx, colInx + 1, colInx + 1));
                
                // set style central
                sheet.getRow(rowInx - 1).getCell(startCol).setCellStyle(headerStyle);
                sheet.getRow(rowInx).getCell(startCol).setCellStyle(headerStyle);

            }

            // create data leads
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_lead_segmentation_performance_by_marketing_data](?,?,?)}");
            callStatement.setString(1, sdf1.format(fromDate));
            callStatement.setString(2, sdf1.format(toDate));
            callStatement.setString(3, "2");

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            startCol = 1;
            startRow = 8;
            colInx = startCol;
            rowInx = startRow;

            leadsReceivedRow = 0;
            leadsUsedRow = 0;
            contactableRow = 0;
            grossAppRow = 0;
            grossApeRow = 0;
            netApeRow = 0;
            netAppROw = 0;

            if (rs.next()) {
                do {
                    rowInx = startRow;

                    for (int i = 2; i <= 8; i++) {
                        sheet.getRow(rowInx);

                        if (i == 2) {

                            //insert leads received
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            rowInx++;
                            lastRow = rowInx;
                            leadsReceivedRow = rowInx;
                        }

                        if (i == 3) {

                            //insert leads used
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            rowInx++;
                            lastRow = rowInx;
                            leadsUsedRow = rowInx;
                            leadsUsedRow = rowInx;

                            //insert % leads used = leads used / leads receiced
                            sheet.getRow(lastRow).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow - 1, CellReference.convertNumToColString(lastCol), lastRow, CellReference.convertNumToColString(lastCol), lastRow - 1));
                            sheet.getRow(lastRow).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            rowInx++;
                            lastRow = rowInx;

                        }

                        if (i == 4) {

                            //insert contactable
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            rowInx++;
                            lastRow = rowInx;
                            contactableRow = rowInx;

                            //insert % contactable = contactable / leads used
                            sheet.getRow(lastRow).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow - 2, CellReference.convertNumToColString(lastCol), lastRow, CellReference.convertNumToColString(lastCol), lastRow - 2));
                            sheet.getRow(lastRow).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            rowInx += 2;
                            lastRow = rowInx;

                        }

                        if (i == 5) {

                            //insert gross app
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                            //insert % response = gross app / contactable
                            sheet.getRow(lastRow - 1).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow - 1).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow - 2, CellReference.convertNumToColString(lastCol), lastRow + 1, CellReference.convertNumToColString(lastCol), lastRow - 2));
                            sheet.getRow(lastRow - 1).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow - 1).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            rowInx++;
                            lastRow = rowInx;
                            grossAppRow = rowInx;

                        }

                        if (i == 6) {

                            //insert gross ape
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getDouble(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            rowInx++;
                            lastRow = rowInx;
                            grossApeRow = rowInx;

                            //insert AARP = gross ape / gross app
                            sheet.getRow(lastRow).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow - 1, CellReference.convertNumToColString(lastCol), lastRow, CellReference.convertNumToColString(lastCol), lastRow - 1));
                            sheet.getRow(lastRow).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            rowInx++;
                            lastRow = rowInx;

                            //insert % list conversion = gross app / leads used
                            sheet.getRow(lastRow).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow - 7, CellReference.convertNumToColString(lastCol), lastRow - 2, CellReference.convertNumToColString(lastCol), lastRow - 7));
                            sheet.getRow(lastRow).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            rowInx += 2;
                            lastRow = rowInx;

                        }

                        if (i == 7) {

                            //insert net APE
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getDouble(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            rowInx++;
                            lastRow = rowInx;
                            netApeRow = rowInx;
                            netApeRow = rowInx;

                        }

                        if (i == 8) {

                            //insert net app
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            rowInx++;
                            lastRow = rowInx;
                            netAppROw = rowInx;

                            //insert % collection = net app / gross app
                            sheet.getRow(lastRow - 3).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow - 3).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow - 6, CellReference.convertNumToColString(lastCol), lastRow, CellReference.convertNumToColString(lastCol), lastRow - 6));
                            sheet.getRow(lastRow - 3).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow - 3).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                            //insert AARP = net ape / net app
                            sheet.getRow(lastRow).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow, CellReference.convertNumToColString(lastCol), lastRow - 1, CellReference.convertNumToColString(lastCol), lastRow));
                            sheet.getRow(lastRow).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            rowInx++;
                            lastRow = rowInx;

                            //insert % list convertion
                            sheet.getRow(lastRow).createCell(lastCol).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(lastRow).getCell(lastCol).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol), lastRow - 12, CellReference.convertNumToColString(lastCol), lastRow - 1, CellReference.convertNumToColString(lastCol), lastRow - 12));
                            sheet.getRow(lastRow).getCell(lastCol).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(lastCol), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            rowInx++;
                            lastRow = rowInx;

                        }

                        // get count last row, last col                        
                        lastCol = colInx;

                    }
                    colInx++;

                } while (rs.next());
            }

            // insert product and data count
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_lead_segmentation_performance_by_marketing_product](?,?,?)}");
            callStatement.setString(1, sdf1.format(fromDate));
            callStatement.setString(2, sdf1.format(toDate));
            callStatement.setString(3, "2");

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure product");
            }
            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();

            if (rs.next()) {

                do {

                    sheet.createRow(rowInx);
                    colInx = 0;

                    for (int i = 1; i <= numCol; i++) {

                        //dataCell = dataRow.createCell(colInx);
                        if (i == 1) {

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getString(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(descriptionCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("-"));

                        }

                        if (i > 1 && i <= numCol) {

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                        }

                        colInx++;
                    }
                    rowInx++;
                    lastRow = rowInx;

                } while (rs.next());
            }

            //insert Revenue/TSR
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_lead_segmentation_performance_by_marketing_tsr](?,?,?)}");
            callStatement.setString(1, sdf1.format(fromDate));
            callStatement.setString(2, sdf1.format(toDate));
            callStatement.setString(3, "2");

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure tsr");
            }
            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            colInx = 1;
            sheet.createRow(lastRow);
            if (rs.next()) {

                do {
                    // get value from column in DB
                    int tsr = rs.getInt(numCol);

                    //for (int i = 1; i <= numCol; i++) {
                    sheet.getRow(lastRow).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(lastRow).getCell(colInx).setCellFormula(String.format("IF(%d > 0,%s%d/%d, 0)", tsr, CellReference.convertNumToColString(colInx), netApeRow, tsr));
                    sheet.getRow(lastRow).getCell(colInx).setCellStyle(detailCellStyle);
                    CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));

                    colInx++;
                    //}

                } while (rs.next());
            }
            //inset header Revenue/TSR
            colInx = 0;
            sheet.getRow(lastRow).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
            sheet.getRow(lastRow).getCell(colInx).setCellValue("Revenue/TSR");
            sheet.getRow(lastRow).getCell(colInx).setCellStyle(descriptionCellStyle);

            rowInx++;
            lastRow = rowInx;

            //insert header Revenue/List
            sheet.createRow(lastRow);
            colInx = 0;
            sheet.getRow(lastRow).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
            sheet.getRow(lastRow).getCell(colInx).setCellValue("Revenue/List");
            sheet.getRow(lastRow).getCell(colInx).setCellStyle(descriptionCellStyle);
            colInx++;
            // insert Revenue/List
            for (int i = 1; i <= lastCol; i++) {

                sheet.getRow(lastRow).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                sheet.getRow(lastRow).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(colInx), leadsUsedRow, CellReference.convertNumToColString(colInx), netApeRow, CellReference.convertNumToColString(colInx), leadsUsedRow));
                sheet.getRow(lastRow).getCell(colInx).setCellStyle(detailCellStyle);
                CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                colInx++;
            }

            rowInx++;
            lastRow = rowInx;

            //insert summary all row
            for (int i = 9; i <= lastRow; i++) {

                int j = lastCol + 1;
                colInx = 1;

                if (i == 11) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), leadsReceivedRow, CellReference.convertNumToColString(j), leadsUsedRow, CellReference.convertNumToColString(j), leadsReceivedRow));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else if (i == 13) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), leadsUsedRow, CellReference.convertNumToColString(j), contactableRow, CellReference.convertNumToColString(j), leadsUsedRow));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else if (i == 14) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), contactableRow, CellReference.convertNumToColString(j), grossAppRow, CellReference.convertNumToColString(j), contactableRow));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else if (i == 17) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), grossAppRow, CellReference.convertNumToColString(j), grossApeRow, CellReference.convertNumToColString(j), grossAppRow));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else if (i == 18) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), leadsUsedRow, CellReference.convertNumToColString(j), grossAppRow, CellReference.convertNumToColString(j), leadsUsedRow));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else if (i == 19) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), grossAppRow, CellReference.convertNumToColString(j), netAppROw, CellReference.convertNumToColString(j), grossAppRow));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else if (i == 22) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), netAppROw, CellReference.convertNumToColString(j), netApeRow, CellReference.convertNumToColString(j), netAppROw));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else if (i == 23) {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j), leadsUsedRow, CellReference.convertNumToColString(j), netAppROw, CellReference.convertNumToColString(j), leadsUsedRow));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                } else {
                    sheet.getRow(i - 1).createCell(j).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    sheet.getRow(i - 1).getCell(j).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(colInx), i, CellReference.convertNumToColString(lastCol), i));
                    sheet.getRow(i - 1).getCell(j).setCellStyle(detailCellStyle);
                }

                if (sheet.getRow(i - 1).getCell(j - 1).getCellStyle().getDataFormatString().equalsIgnoreCase("#,##0")) {
                    CellUtil.setCellStyleProperty(sheet.getRow(i - 1).getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                } else if (sheet.getRow(i - 1).getCell(j - 1).getCellStyle().getDataFormatString().equalsIgnoreCase("#,##0.00")) {
                    CellUtil.setCellStyleProperty(sheet.getRow(i - 1).getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                } else if (sheet.getRow(i - 1).getCell(j - 1).getCellStyle().getDataFormatString().equalsIgnoreCase("0.00%")) {
                    CellUtil.setCellStyleProperty(sheet.getRow(i - 1).getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
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
