/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.report;

import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;

import java.awt.*;
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
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author admin
 */
@ManagedBean
@RequestScoped
public class CallCodeLeadsSegmentAnalysis extends AbstractReportBaseController {

    private static Logger log = Logger.getLogger(CallCodeLeadsSegmentAnalysis.class);
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
        if (!SecurityUtil.isPermitted("report:callcodeleadssegmentanalysis:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        super.init();
    }

    @Override
    protected String getReportPath() {
        return "/report/callcodeLeadsSegmentAnalysis.jasper";
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

        String query = "";
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
        fileName = "CallCode_Leads_Segment_Analysis_report_" + start + ".xlsx";
        try {
            file = new FileInputStream(new File(JSFUtil.getuploadPath(), "report" + "/CallCodeLeadsSegmentAnalysis/Call_Code_Leads_Segment_Analysis_template.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFFont titleFont = workbook.createFont();
            titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
            titleFont.setColor(new XSSFColor(Color.BLACK));

            XSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(CellStyle.ALIGN_LEFT);
            titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("application/vnd.ms-excel");
            FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            //Fill Header
            XSSFRow row;
            XSSFCell cell;

            String contactDate = sdf3.format(fromDate) + " - " + sdf3.format(toDate);
            sheet.getRow(3).getCell(1).setCellValue("Call Code Leads Segment Analysis Report");
            sheet.getRow(4).getCell(1).setCellValue(contactDate);;
            sheet.getRow(5).getCell(1).setCellValue(marketingName);

            // sheet 1 title report by marketing fx6
            sheet = workbook.getSheetAt(1);
            sheet.getRow(3).getCell(1).setCellValue("Call Code Leads Segment Analysis Report");
            sheet.getRow(4).getCell(1).setCellValue(contactDate);;
            sheet.getRow(5).getCell(1).setCellValue(marketingName);

            retrieveData(workbook, sheet);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(CallCodeLeadsSegmentAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(CallCodeLeadsSegmentAnalysis.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void retrieveData(XSSFWorkbook workbook, XSSFSheet sheet) throws SQLException {
        XSSFRow dataRow;
        XSSFCell dataCell, dataCellTotal;

        XSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        DataFormat format = sheet.getWorkbook().createDataFormat();

        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(font);
        headerStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerStyle.setBorderRight(CellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headerStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        headerStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle headerRightStyle = workbook.createCellStyle();
        headerRightStyle.setFont(font);
        headerRightStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        headerRightStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headerRightStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerRightStyle.setBorderRight(CellStyle.BORDER_THIN);
        headerRightStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headerRightStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headerRightStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        headerRightStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle detailCellStyle = workbook.createCellStyle();
        detailCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderLeft(CellStyle.BORDER_THIN);

        XSSFCellStyle descriptionCellStyle = workbook.createCellStyle();
        descriptionCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        descriptionCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        descriptionCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        descriptionCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        descriptionCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        descriptionCellStyle.setBorderLeft(CellStyle.BORDER_THIN);

        XSSFCellStyle summaryCellStyle = workbook.createCellStyle();
        summaryCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        summaryCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        summaryCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        summaryCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        summaryCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        summaryCellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("#,##0"));

        XSSFCellStyle sumRowStyle = workbook.createCellStyle();
        sumRowStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        sumRowStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        sumRowStyle.setBorderTop(CellStyle.BORDER_THIN);
        sumRowStyle.setBorderRight(CellStyle.BORDER_THIN);
        sumRowStyle.setBorderBottom(CellStyle.BORDER_THIN);
        sumRowStyle.setBorderLeft(CellStyle.BORDER_THIN);
        sumRowStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        sumRowStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle marketStyle = workbook.createCellStyle();
        marketStyle.setAlignment(CellStyle.ALIGN_CENTER);
        marketStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        marketStyle.setBorderTop(CellStyle.BORDER_THIN);
        marketStyle.setBorderRight(CellStyle.BORDER_THIN);
        marketStyle.setBorderBottom(CellStyle.BORDER_THIN);
        marketStyle.setBorderLeft(CellStyle.BORDER_THIN);
        marketStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        marketStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        marketStyle.setFont(font);

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            sheet = workbook.getSheetAt(0);
            connection = dataSource.getConnection();

            CallableStatement callStatement = connection.prepareCall("{call [dbo].[sp_fwd_callcode_leads_segment_analysis_header](?,?,?,?)}");
            callStatement.setString(1, sdf1.format(fromDate));
            callStatement.setString(2, sdf1.format(toDate));
            callStatement.setInt(3, marketingId);
            callStatement.setString(4, "1");

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No Resultset from header");
            }

            // create header
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCol = rsmd.getColumnCount();
            int numStartRow = 8;
            int rowInx = numStartRow;
            int lastRow = 0;
            int startCol = 0;
            int lastCol = 0;
            int cellInx = 0;
            int colInx = cellInx;

            if (rs.next()) {

                sheet.createRow(rowInx);
                do {

                    for (int i = 1; i <= numCol; i++) {
                        colInx += 2;

                        if (i <= numCol) {

                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                            sheet.getRow(rowInx).createCell(colInx).setCellValue(d);
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(headerStyle);
                            sheet.getRow(rowInx).createCell(colInx + 1).setCellValue("");
                            sheet.getRow(rowInx).getCell(colInx + 1).setCellStyle(headerStyle);

                            String contactDate = sdf3.format(fromDate) + " - " + sdf3.format(toDate);
                            sheet.getRow(rowInx - 1).createCell(colInx).setCellValue(contactDate);
                            sheet.getRow(rowInx - 1).getCell(colInx).setCellStyle(headerStyle);
                            sheet.getRow(rowInx - 1).createCell(colInx + 1).setCellValue("");
                            sheet.getRow(rowInx - 1).getCell(colInx + 1).setCellStyle(headerStyle);

                        }
                    }
                    sheet.addMergedRegion(new CellRangeAddress(rowInx, rowInx, colInx, colInx + 1));

                } while (rs.next());

                // set contact date
                sheet.addMergedRegion(new CellRangeAddress(rowInx - 1, rowInx - 1, 2, colInx + 1));

                // set total header
                sheet.getRow(rowInx - 1).createCell(colInx + 2).setCellValue("Total");
                sheet.getRow(rowInx - 1).getCell(colInx + 2).setCellStyle(headerStyle);
                sheet.getRow(rowInx - 1).createCell(colInx + 3).setCellValue("");
                sheet.getRow(rowInx - 1).getCell(colInx + 3).setCellStyle(headerStyle);
                sheet.getRow(rowInx).createCell(colInx + 2).setCellValue("");
                sheet.getRow(rowInx).getCell(colInx + 2).setCellStyle(headerStyle);
                sheet.getRow(rowInx).createCell(colInx + 3).setCellValue("");
                sheet.getRow(rowInx).getCell(colInx + 3).setCellStyle(headerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowInx - 1, rowInx, colInx + 2, colInx + 3));

                // set call code
                colInx = 0;
                sheet.getRow(rowInx - 1).createCell(colInx).setCellValue("Call Code");
                sheet.getRow(rowInx - 1).getCell(colInx).setCellStyle(headerStyle);
                sheet.getRow(rowInx - 1).createCell(colInx + 1).setCellValue("");
                sheet.getRow(rowInx - 1).getCell(colInx + 1).setCellStyle(headerStyle);
                sheet.getRow(rowInx).createCell(colInx).setCellValue("");
                sheet.getRow(rowInx).getCell(colInx).setCellStyle(headerStyle);
                sheet.getRow(rowInx).createCell(colInx + 1).setCellValue("");
                sheet.getRow(rowInx).getCell(colInx + 1).setCellStyle(headerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowInx - 1, rowInx, colInx, colInx + 1));

            }

            // create call code data
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_callcode_leads_segment_analysis_data](?,?,?,?)}");
            callStatement.setString(1, sdf1.format(fromDate));
            callStatement.setString(2, sdf1.format(toDate));
            callStatement.setInt(3, marketingId);
            callStatement.setString(4, "1");

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No Resultset from pivot fx5");
            }
            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            numStartRow = 9;
            rowInx = numStartRow;

            if (rs.next()) {

                do {
                    dataRow = sheet.createRow(rowInx);
                    colInx = 0;
                    int sumRow = 0;
                    for (int i = 1; i <= numCol; i++) {

                        dataCell = dataRow.createCell(colInx);
                        if (i == 1) {
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                            dataCell.setCellValue(d);
                            dataCell.setCellStyle(detailCellStyle);
                        }
                        if (i == 2) {
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                            dataCell.setCellValue(d);
                            dataCell.setCellStyle(descriptionCellStyle);
                        }
                        if (i > 2 && i <= numCol) {
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? df1.format(rs.getInt(i)) : "0";
                            int data = Integer.parseInt(d.replace(",",""));

                            if (data != 0) {
                                sumRow = sumRow + data;
                                dataCell.setCellValue(data);
                                dataCell.setCellStyle(detailCellStyle);
                            } else {
                                dataCell.setCellValue(data);
                                dataCell.setCellStyle(detailCellStyle);
                            }
                            colInx++;
                        }
                        colInx++;
                    }
                    dataCellTotal = dataRow.createCell(colInx);
                    dataCellTotal.setCellValue(sumRow);
                    dataCellTotal.setCellStyle(detailCellStyle);
                    rowInx++;

                } while (rs.next());

                lastCol = colInx;
                lastRow = rowInx;

                // Summation total each column
                sheet.createRow(lastRow);
                rowInx++;
                startCol = 2;
                for (int i = 0; i <= lastCol - 2; i += 2) {

                    if (i == 0) {
                        sheet.getRow(lastRow).createCell(i).setCellValue("Total ");
                        sheet.getRow(lastRow).getCell(i).setCellStyle(headerRightStyle);
                        sheet.getRow(lastRow).createCell(i + 1).setCellValue("Total ");
                        sheet.getRow(lastRow).getCell(i + 1).setCellStyle(headerRightStyle);
                        sheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, i, i + 1));

                    } else {
                        sheet.getRow(lastRow).createCell(i).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(lastRow).getCell(i).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(i), numStartRow + 1, CellReference.convertNumToColString(i), lastRow));
                        sheet.getRow(lastRow).getCell(i).setCellStyle(summaryCellStyle);
                        sheet.getRow(lastRow).createCell(i + 1).setCellValue("");
                        sheet.getRow(lastRow).getCell(i + 1).setCellStyle(summaryCellStyle);
                    }
                }

                // find percentage value each column
                for (int j = 3; j <= lastCol - 1; j += 2) {

                    for (int k = 9; k <= lastRow - 1; k++) {
                        sheet.getRow(k).createCell(j).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(k).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j - 1), lastRow + 1, CellReference.convertNumToColString(j - 1), k + 1, CellReference.convertNumToColString(j - 1), lastRow + 1));
                        sheet.getRow(k).getCell(j).setCellStyle(detailCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(k).getCell(j), workbook, CellUtil.DATA_FORMAT, sheet.getWorkbook().createDataFormat().getFormat("0.00%"));
                    }
                }

                // find percentage value total column
                for (int i = lastCol + 1; i <= lastCol + 1; i++) {

                    for (int k = 9; k <= lastRow - 1; k++) {
                        sheet.getRow(k).createCell(i).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(k).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(i - 1), lastRow + 1, CellReference.convertNumToColString(i - 1), k + 1, CellReference.convertNumToColString(i - 1), lastRow + 1));
                        sheet.getRow(k).getCell(i).setCellStyle(detailCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(k).getCell(i), workbook, CellUtil.DATA_FORMAT, sheet.getWorkbook().createDataFormat().getFormat("0.00%"));
                    }
                }

                lastRow = rowInx;
                // sum total
                sheet.getRow(lastRow - 1).createCell(lastCol).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                sheet.getRow(lastRow - 1).getCell(lastCol).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(startCol), lastRow, CellReference.convertNumToColString(lastCol - 2), lastRow));
                sheet.getRow(lastRow - 1).getCell(lastCol).setCellStyle(summaryCellStyle);
                sheet.getRow(lastRow - 1).createCell(lastCol + 1).setCellValue("");
                sheet.getRow(lastRow - 1).getCell(lastCol + 1).setCellStyle(summaryCellStyle);

            }

            // create listUploads
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_callcode_leads_segment_analysis_uploads](?,?,?,?)}");
            callStatement.setString(1, sdf1.format(fromDate));
            callStatement.setString(2, sdf1.format(toDate));
            callStatement.setInt(3, marketingId);
            callStatement.setString(4, "1");

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No Resultset from uploads");
            }
            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            cellInx = 0;
            colInx = cellInx;
            lastRow = rowInx;
            startCol = 2;
            numStartRow = 10;

            sheet.createRow(lastRow);
            rowInx++;
            if (rs.next()) {

                do {
                    for (int i = 2; i <= numCol; i++) {
                        colInx += 2;
                        if (i <= numCol) {
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                            int data = Integer.parseInt(d.replace(",",""));
                            sheet.getRow(lastRow).createCell(colInx).setCellValue(data);
                            sheet.getRow(lastRow).getCell(colInx).setCellStyle(summaryCellStyle);
                            sheet.getRow(lastRow).createCell(colInx + 1).setCellValue("");
                            sheet.getRow(lastRow).getCell(colInx + 1).setCellStyle(summaryCellStyle);
                        }
                    }

                } while (rs.next());

                // create list uploaded header
                colInx = 0;
                sheet.getRow(lastRow).createCell(colInx).setCellValue("List Uploaded ");
                sheet.getRow(lastRow).getCell(colInx).setCellStyle(headerRightStyle);
                sheet.getRow(lastRow).createCell(colInx + 1).setCellValue("");
                sheet.getRow(lastRow).getCell(colInx + 1).setCellStyle(headerRightStyle);
                sheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, colInx, colInx + 1));
                lastRow = rowInx;

                // sum list uploaded
                sheet.getRow(lastRow - 1).createCell(lastCol).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                sheet.getRow(lastRow - 1).getCell(lastCol).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(startCol), lastRow, CellReference.convertNumToColString(lastCol - 2), lastRow));
                sheet.getRow(lastRow - 1).getCell(lastCol).setCellStyle(summaryCellStyle);
                sheet.getRow(lastRow - 1).createCell(lastCol + 1).setCellValue("");
                sheet.getRow(lastRow - 1).getCell(lastCol + 1).setCellStyle(summaryCellStyle);

                // create not used
                sheet.createRow(lastRow);
                rowInx++;
                for (int i = 0; i <= lastCol - 2; i += 2) {
                    if (i == 0) {
                        sheet.getRow(lastRow).createCell(i).setCellValue("Not Used ");
                        sheet.getRow(lastRow).getCell(i).setCellStyle(headerRightStyle);
                        sheet.getRow(lastRow).createCell(i + 1).setCellValue("");
                        sheet.getRow(lastRow).getCell(i + 1).setCellStyle(headerRightStyle);
                        sheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, i, i + 1));
                    } else {
                        sheet.getRow(lastRow).createCell(i).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(lastRow).getCell(i).setCellFormula(String.format("%s%d-%s%d", CellReference.convertNumToColString(i), lastRow, CellReference.convertNumToColString(i), lastRow - 1));
                        sheet.getRow(lastRow).getCell(i).setCellStyle(summaryCellStyle);
                        sheet.getRow(lastRow).createCell(i + 1).setCellValue("");
                        sheet.getRow(lastRow).getCell(i + 1).setCellStyle(summaryCellStyle);
                    }
                }
                lastRow = rowInx;

                // sum not used value
                sheet.getRow(lastRow - 1).createCell(lastCol).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                sheet.getRow(lastRow - 1).getCell(lastCol).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(startCol), lastRow, CellReference.convertNumToColString(lastCol - 2), lastRow));
                sheet.getRow(lastRow - 1).getCell(lastCol).setCellStyle(summaryCellStyle);
                sheet.getRow(lastRow - 1).createCell(lastCol + 1).setCellValue("");
                sheet.getRow(lastRow - 1).getCell(lastCol + 1).setCellStyle(summaryCellStyle);

                // create % list used
                sheet.createRow(lastRow);
                rowInx++;
                for (int i = 0; i <= lastCol; i += 2) {
                    if (i == 0) {
                        sheet.getRow(lastRow).createCell(i).setCellValue("% List Used ");
                        sheet.getRow(lastRow).getCell(i).setCellStyle(headerRightStyle);
                        sheet.getRow(lastRow).createCell(i + 1).setCellValue("");
                        sheet.getRow(lastRow).getCell(i + 1).setCellStyle(headerRightStyle);
                        sheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, i, i + 1));
                    } else {
                        sheet.getRow(lastRow).createCell(i).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(lastRow).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(i), lastRow - 1, CellReference.convertNumToColString(i), lastRow - 2, CellReference.convertNumToColString(i), lastRow - 1));
                        sheet.getRow(lastRow).getCell(i).setCellStyle(summaryCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(i), workbook, CellUtil.DATA_FORMAT, sheet.getWorkbook().createDataFormat().getFormat("0.00%"));
                        sheet.getRow(lastRow).createCell(i + 1).setCellValue("");
                        sheet.getRow(lastRow).getCell(i + 1).setCellStyle(summaryCellStyle);
                    }
                }
            }

            // create sheet 1 data
            sheet = workbook.getSheetAt(1);

            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_callcode_leads_segment_analysis_header](?,?,?,?)}");
            callStatement.setString(1, sdf1.format(fromDate));
            callStatement.setString(2, sdf1.format(toDate));
            callStatement.setInt(3, marketingId);
            callStatement.setString(4, "2");

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No Resultset from header");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            numStartRow = 8;
            rowInx = numStartRow;
            lastRow = 0;
            startCol = 0;
            lastCol = 0;
            cellInx = 0;
            colInx = cellInx;

            if (rs.next()) {

                sheet.createRow(rowInx);
                do {

                    for (int i = 1; i <= numCol; i++) {
                        colInx += 2;

                        if (i <= numCol) {

                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                            sheet.getRow(rowInx).createCell(colInx).setCellValue(d);
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(headerStyle);
                            sheet.getRow(rowInx).createCell(colInx + 1).setCellValue("");
                            sheet.getRow(rowInx).getCell(colInx + 1).setCellStyle(headerStyle);

                            // set contact date
                            String contactDate = sdf3.format(fromDate) + " - " + sdf3.format(toDate);
                            sheet.getRow(rowInx - 1).createCell(colInx).setCellValue(contactDate);
                            sheet.getRow(rowInx - 1).getCell(colInx).setCellStyle(headerStyle);
                            sheet.getRow(rowInx - 1).createCell(colInx + 1).setCellValue("");
                            sheet.getRow(rowInx - 1).getCell(colInx + 1).setCellStyle(headerStyle);

                        }
                    }
                    sheet.addMergedRegion(new CellRangeAddress(rowInx, rowInx, colInx, colInx + 1));

                } while (rs.next());

                // set contact date
                sheet.addMergedRegion(new CellRangeAddress(rowInx - 1, rowInx - 1, 2, colInx + 1));

                // set total header
                sheet.getRow(rowInx - 1).createCell(colInx + 2).setCellValue("Total");
                sheet.getRow(rowInx - 1).getCell(colInx + 2).setCellStyle(headerStyle);
                sheet.getRow(rowInx - 1).createCell(colInx + 3).setCellValue("");
                sheet.getRow(rowInx - 1).getCell(colInx + 3).setCellStyle(headerStyle);
                sheet.getRow(rowInx).createCell(colInx + 2).setCellValue("");
                sheet.getRow(rowInx).getCell(colInx + 2).setCellStyle(headerStyle);
                sheet.getRow(rowInx).createCell(colInx + 3).setCellValue("");
                sheet.getRow(rowInx).getCell(colInx + 3).setCellStyle(headerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowInx - 1, rowInx, colInx + 2, colInx + 3));

                // set call code
                colInx = 0;
                sheet.getRow(rowInx - 1).createCell(colInx).setCellValue("Call Code");
                sheet.getRow(rowInx - 1).getCell(colInx).setCellStyle(headerStyle);
                sheet.getRow(rowInx - 1).createCell(colInx + 1).setCellValue("");
                sheet.getRow(rowInx - 1).getCell(colInx + 1).setCellStyle(headerStyle);
                sheet.getRow(rowInx).createCell(colInx).setCellValue("");
                sheet.getRow(rowInx).getCell(colInx).setCellStyle(headerStyle);
                sheet.getRow(rowInx).createCell(colInx + 1).setCellValue("");
                sheet.getRow(rowInx).getCell(colInx + 1).setCellStyle(headerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowInx - 1, rowInx, colInx, colInx + 1));

            }

            // create call code data
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_callcode_leads_segment_analysis_data](?,?,?,?)}");
            callStatement.setString(1, sdf1.format(fromDate));
            callStatement.setString(2, sdf1.format(toDate));
            callStatement.setInt(3, marketingId);
            callStatement.setString(4, "2");

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No Resultset from pivot fx6");
            }
            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            numStartRow = 9;
            rowInx = numStartRow;

            if (rs.next()) {

                do {
                    dataRow = sheet.createRow(rowInx);
                    colInx = 0;
                    int sumRow = 0;
                    for (int i = 1; i <= numCol; i++) {

                        dataCell = dataRow.createCell(colInx);
                        if (i == 1) {
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                            dataCell.setCellValue(d);
                            dataCell.setCellStyle(detailCellStyle);
                        }
                        if (i == 2) {
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                            dataCell.setCellValue(d);
                            dataCell.setCellStyle(descriptionCellStyle);
                        }
                        if (i > 2 && i <= numCol) {
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? df1.format(rs.getInt(i)) : "0";
                            int data = Integer.parseInt(d.replace(",",""));

                            if (data != 0) {
                                sumRow = sumRow + data;
                                dataCell.setCellValue(data);
                                dataCell.setCellStyle(detailCellStyle);
                            } else {
                                dataCell.setCellValue(data);
                                dataCell.setCellStyle(detailCellStyle);
                            }
                            colInx++;
                        }
                        colInx++;
                    }
                    dataCellTotal = dataRow.createCell(colInx);
                    dataCellTotal.setCellValue(sumRow);
                    dataCellTotal.setCellStyle(detailCellStyle);
                    rowInx++;

                } while (rs.next());

                lastCol = colInx;
                lastRow = rowInx;

                // Summation total each column
                sheet.createRow(lastRow);
                rowInx++;
                startCol = 2;
                for (int i = 0; i <= lastCol - 2; i += 2) {

                    if (i == 0) {
                        sheet.getRow(lastRow).createCell(i).setCellValue("Total ");
                        sheet.getRow(lastRow).getCell(i).setCellStyle(headerRightStyle);
                        sheet.getRow(lastRow).createCell(i + 1).setCellValue("Total ");
                        sheet.getRow(lastRow).getCell(i + 1).setCellStyle(headerRightStyle);
                        sheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, i, i + 1));

                    } else {
                        sheet.getRow(lastRow).createCell(i).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(lastRow).getCell(i).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(i), numStartRow + 1, CellReference.convertNumToColString(i), lastRow));
                        sheet.getRow(lastRow).getCell(i).setCellStyle(summaryCellStyle);
                        sheet.getRow(lastRow).createCell(i + 1).setCellValue("");
                        sheet.getRow(lastRow).getCell(i + 1).setCellStyle(summaryCellStyle);
                    }
                }

                // find percentage value each column
                for (int j = 3; j <= lastCol - 1; j += 2) {

                    for (int k = 9; k <= lastRow - 1; k++) {
                        sheet.getRow(k).createCell(j).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(k).getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j - 1), lastRow + 1, CellReference.convertNumToColString(j - 1), k + 1, CellReference.convertNumToColString(j - 1), lastRow + 1));
                        sheet.getRow(k).getCell(j).setCellStyle(detailCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(k).getCell(j), workbook, CellUtil.DATA_FORMAT, sheet.getWorkbook().createDataFormat().getFormat("0.00%"));
                    }
                }

                // find percentage value total column
                for (int i = lastCol + 1; i <= lastCol + 1; i++) {

                    for (int k = 9; k <= lastRow - 1; k++) {
                        sheet.getRow(k).createCell(i).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(k).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(i - 1), lastRow + 1, CellReference.convertNumToColString(i - 1), k + 1, CellReference.convertNumToColString(i - 1), lastRow + 1));
                        sheet.getRow(k).getCell(i).setCellStyle(detailCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(k).getCell(i), workbook, CellUtil.DATA_FORMAT, sheet.getWorkbook().createDataFormat().getFormat("0.00%"));
                    }
                }

                lastRow = rowInx;
                // sum total
                sheet.getRow(lastRow - 1).createCell(lastCol).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                sheet.getRow(lastRow - 1).getCell(lastCol).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(startCol), lastRow, CellReference.convertNumToColString(lastCol - 2), lastRow));
                sheet.getRow(lastRow - 1).getCell(lastCol).setCellStyle(summaryCellStyle);
                sheet.getRow(lastRow - 1).createCell(lastCol + 1).setCellValue("");
                sheet.getRow(lastRow - 1).getCell(lastCol + 1).setCellStyle(summaryCellStyle);

            }

            // create listUploads
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_callcode_leads_segment_analysis_uploads](?,?,?,?)}");
            callStatement.setString(1, sdf1.format(fromDate));
            callStatement.setString(2, sdf1.format(toDate));
            callStatement.setInt(3, marketingId);
            callStatement.setString(4, "2");

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No Resultset from uploads");
            }
            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            cellInx = 0;
            colInx = cellInx;
            lastRow = rowInx;
            startCol = 2;
            numStartRow = 10;

            sheet.createRow(lastRow);
            rowInx++;
            if (rs.next()) {

                do {
                    for (int i = 2; i <= numCol; i++) {
                        colInx += 2;
                        if (i <= numCol) {
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                            int data = Integer.parseInt(d.replace(",",""));
                            sheet.getRow(lastRow).createCell(colInx).setCellValue(data);
                            sheet.getRow(lastRow).getCell(colInx).setCellStyle(summaryCellStyle);
                            sheet.getRow(lastRow).createCell(colInx + 1).setCellValue("");
                            sheet.getRow(lastRow).getCell(colInx + 1).setCellStyle(summaryCellStyle);
                        }
                    }

                } while (rs.next());

                // create list uploaded header
                colInx = 0;
                sheet.getRow(lastRow).createCell(colInx).setCellValue("List Uploaded ");
                sheet.getRow(lastRow).getCell(colInx).setCellStyle(headerRightStyle);
                sheet.getRow(lastRow).createCell(colInx + 1).setCellValue("");
                sheet.getRow(lastRow).getCell(colInx + 1).setCellStyle(headerRightStyle);
                sheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, colInx, colInx + 1));
                lastRow = rowInx;

                // sum list uploaded
                sheet.getRow(lastRow - 1).createCell(lastCol).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                sheet.getRow(lastRow - 1).getCell(lastCol).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(startCol), lastRow, CellReference.convertNumToColString(lastCol - 2), lastRow));
                sheet.getRow(lastRow - 1).getCell(lastCol).setCellStyle(summaryCellStyle);
                sheet.getRow(lastRow - 1).createCell(lastCol + 1).setCellValue("");
                sheet.getRow(lastRow - 1).getCell(lastCol + 1).setCellStyle(summaryCellStyle);

                // create not used
                sheet.createRow(lastRow);
                rowInx++;
                for (int i = 0; i <= lastCol - 2; i += 2) {
                    if (i == 0) {
                        sheet.getRow(lastRow).createCell(i).setCellValue("Not Used ");
                        sheet.getRow(lastRow).getCell(i).setCellStyle(headerRightStyle);
                        sheet.getRow(lastRow).createCell(i + 1).setCellValue("");
                        sheet.getRow(lastRow).getCell(i + 1).setCellStyle(headerRightStyle);
                        sheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, i, i + 1));
                    } else {
                        sheet.getRow(lastRow).createCell(i).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(lastRow).getCell(i).setCellFormula(String.format("%s%d-%s%d", CellReference.convertNumToColString(i), lastRow, CellReference.convertNumToColString(i), lastRow - 1));
                        sheet.getRow(lastRow).getCell(i).setCellStyle(summaryCellStyle);
                        sheet.getRow(lastRow).createCell(i + 1).setCellValue("");
                        sheet.getRow(lastRow).getCell(i + 1).setCellStyle(summaryCellStyle);
                    }
                }
                lastRow = rowInx;

                // sum not used value
                sheet.getRow(lastRow - 1).createCell(lastCol).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                sheet.getRow(lastRow - 1).getCell(lastCol).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(startCol), lastRow, CellReference.convertNumToColString(lastCol - 2), lastRow));
                sheet.getRow(lastRow - 1).getCell(lastCol).setCellStyle(summaryCellStyle);
                sheet.getRow(lastRow - 1).createCell(lastCol + 1).setCellValue("");
                sheet.getRow(lastRow - 1).getCell(lastCol + 1).setCellStyle(summaryCellStyle);

                // create % list used
                sheet.createRow(lastRow);
                rowInx++;
                for (int i = 0; i <= lastCol; i += 2) {
                    if (i == 0) {
                        sheet.getRow(lastRow).createCell(i).setCellValue("% List Used ");
                        sheet.getRow(lastRow).getCell(i).setCellStyle(headerRightStyle);
                        sheet.getRow(lastRow).createCell(i + 1).setCellValue("");
                        sheet.getRow(lastRow).getCell(i + 1).setCellStyle(headerRightStyle);
                        sheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, i, i + 1));
                    } else {
                        sheet.getRow(lastRow).createCell(i).setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(lastRow).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(i), lastRow - 1, CellReference.convertNumToColString(i), lastRow - 2, CellReference.convertNumToColString(i), lastRow - 1));
                        sheet.getRow(lastRow).getCell(i).setCellStyle(summaryCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(lastRow).getCell(i), workbook, CellUtil.DATA_FORMAT, sheet.getWorkbook().createDataFormat().getFormat("0.00%"));
                        sheet.getRow(lastRow).createCell(i + 1).setCellValue("");
                        sheet.getRow(lastRow).getCell(i + 1).setCellStyle(summaryCellStyle);
                    }
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
