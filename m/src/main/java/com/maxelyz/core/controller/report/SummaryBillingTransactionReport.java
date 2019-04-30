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
import org.apache.poi.hssf.usermodel.HSSFPalette;
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
public class SummaryBillingTransactionReport extends AbstractReportBaseController {

    private static Logger log = Logger.getLogger(SummaryBillingTransactionReport.class);
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
        if (!SecurityUtil.isPermitted("report:summarybillingtransaction:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        super.init();
    }

    @Override
    protected String getReportPath() {
        return "/report/SummaryBillingTransactionReport.jasper";
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
        fileName = "Summary_Billing_Transaction_report_" + start + ".xls";
        try {
            file = new FileInputStream(new File(JSFUtil.getuploadPath(), "report" + "/SummaryBillingTransaction/Summary_Billing_Transaction_report_template.xls"));
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(CellStyle.ALIGN_LEFT);
            titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("application/vnd.ms-excel");
            FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            //Fill Header
            String contactDate = sdf3.format(fromDate) + " - " + sdf3.format(toDate);
            sheet.getRow(3).getCell(1).setCellValue("Summary Billing Transaction");
            sheet.getRow(4).getCell(1).setCellValue(contactDate);;

            retrieveData(workbook, sheet);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SummaryBillingTransactionReport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(SummaryBillingTransactionReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void retrieveData(HSSFWorkbook workbook, HSSFSheet sheet) throws SQLException {
        HSSFRow dataRow;
        HSSFCell dataCell;

        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor colorTotal = palette.findSimilarColor(135, 206, 235);
        HSSFColor colorSub = palette.findSimilarColor(173, 216, 230);
        HSSFColor colorType = palette.findSimilarColor(220, 220, 220);
        HSSFColor colorHeader = palette.findSimilarColor(192, 192, 192);

        short palTotalIndex = colorTotal.getIndex();
        short palSubIndex = colorSub.getIndex();
        short palTypeIndex = colorType.getIndex();
        short palHeaderIndex = colorHeader.getIndex();

        HSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        HSSFCellStyle totalTitleStyle = workbook.createCellStyle();
        totalTitleStyle.setAlignment(CellStyle.ALIGN_LEFT);
        totalTitleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        totalTitleStyle.setBorderTop(CellStyle.BORDER_THIN);
        totalTitleStyle.setBorderRight(CellStyle.BORDER_THIN);
        totalTitleStyle.setBorderLeft(CellStyle.BORDER_THIN);
        totalTitleStyle.setBorderBottom(CellStyle.BORDER_THIN);
        totalTitleStyle.setFillForegroundColor(palTotalIndex);
        totalTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        totalTitleStyle.setFont(font);

        HSSFCellStyle totalDetailStyle = workbook.createCellStyle();
        totalDetailStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        totalDetailStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        totalDetailStyle.setBorderTop(CellStyle.BORDER_THIN);
        totalDetailStyle.setBorderRight(CellStyle.BORDER_THIN);
        totalDetailStyle.setBorderLeft(CellStyle.BORDER_THIN);
        totalDetailStyle.setBorderBottom(CellStyle.BORDER_THIN);
        totalDetailStyle.setFillForegroundColor(palTotalIndex);
        totalDetailStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle subTitleStyle = workbook.createCellStyle();
        subTitleStyle.setAlignment(CellStyle.ALIGN_LEFT);
        subTitleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        subTitleStyle.setBorderTop(CellStyle.BORDER_THIN);
        subTitleStyle.setBorderRight(CellStyle.BORDER_THIN);
        subTitleStyle.setBorderLeft(CellStyle.BORDER_THIN);
        subTitleStyle.setBorderBottom(CellStyle.BORDER_THIN);
        subTitleStyle.setFillForegroundColor(palSubIndex);
        subTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        subTitleStyle.setFont(font);

        HSSFCellStyle subDetailStyle = workbook.createCellStyle();
        subDetailStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        subDetailStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        subDetailStyle.setBorderTop(CellStyle.BORDER_THIN);
        subDetailStyle.setBorderRight(CellStyle.BORDER_THIN);
        subDetailStyle.setBorderLeft(CellStyle.BORDER_THIN);
        subDetailStyle.setBorderBottom(CellStyle.BORDER_THIN);
        subDetailStyle.setFillForegroundColor(palSubIndex);
        subDetailStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle typeTitleStyle = workbook.createCellStyle();
        typeTitleStyle.setAlignment(CellStyle.ALIGN_LEFT);
        typeTitleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        typeTitleStyle.setBorderTop(CellStyle.BORDER_THIN);
        typeTitleStyle.setBorderRight(CellStyle.BORDER_THIN);
        typeTitleStyle.setBorderLeft(CellStyle.BORDER_THIN);
        typeTitleStyle.setBorderBottom(CellStyle.BORDER_THIN);
        typeTitleStyle.setFillForegroundColor(palTypeIndex);
        typeTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle typeDetailStyle = workbook.createCellStyle();
        typeDetailStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        typeDetailStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        typeDetailStyle.setBorderTop(CellStyle.BORDER_THIN);
        typeDetailStyle.setBorderRight(CellStyle.BORDER_THIN);
        typeDetailStyle.setBorderLeft(CellStyle.BORDER_THIN);
        typeDetailStyle.setBorderBottom(CellStyle.BORDER_THIN);
        typeDetailStyle.setFillForegroundColor(palTypeIndex);
        typeDetailStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(CellStyle.ALIGN_LEFT);
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
        headerStyle.setFillForegroundColor(palHeaderIndex);
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

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

        sheet = workbook.getSheetAt(0);

        // initial value
        int titleRow = 0;
        int totalPaymentRow = 0;
        int successPaymentRow = 0;
        int lastSuccessRow = 0;
        int unsuccessPendingRow = 0;
        int unsuccessRejectRow = 0;
        int unsuccessPendingCreditRow = 0;
        int unsuccessRejectCreditRow = 0;
        int unsuccessPendingDebitRow = 0;
        int unsuccessRejectDebitRow = 0;
        int lastPendingCreditReasonRow = 0;
        int lastPendingDebitReasonRow = 0;
        int lastRejectCreditReasonRow = 0;
        int lastRejectDebitReasonRow = 0;

        int numCol = 0;
        int startRow = 6;
        int startCol = 0;
        int rowInx = startRow;
        int colInx = startCol;
        int lastCol = 0;
        int lastRow = 0;

        // create title header
        sheet.createRow(startRow);
        sheet.getRow(rowInx).createCell(startCol).setCellType(HSSFCell.CELL_TYPE_STRING);
        sheet.getRow(rowInx).getCell(colInx).setCellValue("Description");
        sheet.getRow(rowInx).getCell(colInx).setCellStyle(headerStyle);
        colInx++;

        String saleDate = sdf3.format(fromDate) + " - " + sdf3.format(toDate);
        sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
        sheet.getRow(rowInx).getCell(colInx).setCellValue(saleDate);
        sheet.getRow(rowInx).getCell(colInx).setCellStyle(headerStyle);
        colInx++;

        sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
        sheet.getRow(rowInx).getCell(colInx).setCellValue("");
        sheet.getRow(rowInx).getCell(colInx).setCellStyle(headerStyle);
        colInx++;

        sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
        sheet.getRow(rowInx).getCell(colInx).setCellValue("");
        sheet.getRow(rowInx).getCell(colInx).setCellStyle(headerStyle);
        colInx++;

        sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
        sheet.getRow(rowInx).getCell(colInx).setCellValue("");
        sheet.getRow(rowInx).getCell(colInx).setCellStyle(headerStyle);

        lastCol = colInx;
        rowInx++;
        lastRow = rowInx;

        sheet.createRow(lastRow);
        colInx = startCol;

        sheet.getRow(lastRow).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
        sheet.getRow(lastRow).getCell(colInx).setCellValue("");
        sheet.getRow(lastRow).getCell(colInx).setCellStyle(headerStyle);
        colInx++;

        sheet.getRow(lastRow).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
        sheet.getRow(lastRow).getCell(colInx).setCellValue("Count of Billing Transaction");
        sheet.getRow(lastRow).getCell(colInx).setCellStyle(headerStyle);
        colInx++;

        sheet.getRow(lastRow).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
        sheet.getRow(lastRow).getCell(colInx).setCellValue(" % ");
        sheet.getRow(lastRow).getCell(colInx).setCellStyle(headerStyle);
        colInx++;

        sheet.getRow(lastRow).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
        sheet.getRow(lastRow).getCell(colInx).setCellValue("Amount of Billing");
        sheet.getRow(lastRow).getCell(colInx).setCellStyle(headerStyle);
        colInx++;

        sheet.getRow(lastRow).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
        sheet.getRow(lastRow).getCell(colInx).setCellValue(" % ");
        sheet.getRow(lastRow).getCell(colInx).setCellStyle(headerStyle);

        // merge cell description, sale date
        sheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, startCol, startCol));
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, startCol + 1, lastCol));

        rowInx++;
        lastRow = rowInx;
        titleRow = rowInx;

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {

            connection = dataSource.getConnection();

            // insert Total Payment Transaction
            CallableStatement callStatement = connection.prepareCall("{call [dbo].[sp_fwd_summary_billing_transaction_payment](?,?,?)}");
            callStatement.setString(1, "1");
            callStatement.setString(2, sdf1.format(fromDate));
            callStatement.setString(3, sdf1.format(toDate));

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            ResultSetMetaData rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            startRow = titleRow;
            startCol = 0;
            rowInx = startRow;
            colInx = startCol;

            if (rs.next()) {

                do {

                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            sheet.createRow(startRow);
                            // add title total transaction
                            sheet.getRow(rowInx).createCell(startCol).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue("Total Transaction of First Payment");
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(totalTitleStyle);
                            colInx++;

                            // add count of billing transaction
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(totalDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            colInx++;

                            // add % billing transaction
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(1);
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(totalDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            colInx++;
                        }
                        if (i == 2) {
                            // add amount of billing
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(totalDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            colInx++;

                            // add % amount of billing
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(1);
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(totalDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                            rowInx++;
                            lastRow = rowInx;
                            totalPaymentRow = rowInx;
                        }
                    }

                } while (rs.next());
            }

            // insert Success Payment
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_summary_billing_transaction_payment](?,?,?)}");
            callStatement.setString(1, "2");
            callStatement.setString(2, sdf1.format(fromDate));
            callStatement.setString(3, sdf1.format(toDate));

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            startRow = totalPaymentRow;
            startCol = 0;
            rowInx = startRow;
            colInx = startCol;

            if (rs.next()) {

                do {

                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            sheet.createRow(startRow);
                            // add success title
                            sheet.getRow(rowInx).createCell(startCol).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue("Success");
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subTitleStyle);
                            colInx++;

                            // add success transaction
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            colInx++;

                            // add % success transaction
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startCol + 1), totalPaymentRow, CellReference.convertNumToColString(startCol + 1), lastRow + 1, CellReference.convertNumToColString(startCol + 1), totalPaymentRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            colInx++;
                        }
                        if (i == 2) {
                            // add amount of sucess
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            colInx++;

                            // add % amount of success
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol - 1), totalPaymentRow, CellReference.convertNumToColString(lastCol - 1), lastRow + 1, CellReference.convertNumToColString(lastCol - 1), totalPaymentRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                            rowInx++;
                            lastRow = rowInx;
                            successPaymentRow = rowInx;
                        }
                    }
                } while (rs.next());
            }

            // insert detail success payment
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_summary_billing_transaction_data](?,?,?)}");
            callStatement.setString(1, "1");
            callStatement.setString(2, sdf1.format(fromDate));
            callStatement.setString(3, sdf1.format(toDate));

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            startRow = successPaymentRow;
            startCol = 0;
            rowInx = startRow;

            if (rs.next()) {

                do {

                    sheet.createRow(startRow);
                    colInx = startCol;

                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            sheet.getRow(rowInx).createCell(startCol).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getString(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(dataStyle);
                            colInx++;
                        }
                        if (i == 2) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startCol + 1), successPaymentRow, CellReference.convertNumToColString(startCol + 1), lastRow + 1, CellReference.convertNumToColString(startCol + 1), successPaymentRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            colInx++;
                        }
                        if (i == 3) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol - 1), successPaymentRow, CellReference.convertNumToColString(lastCol - 1), lastRow + 1, CellReference.convertNumToColString(lastCol - 1), successPaymentRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                        }
                    }
                    rowInx++;
                    startRow = rowInx;
                    lastRow = rowInx;
                    lastSuccessRow = lastRow;

                } while (rs.next());
            }

            // insert Unsuccess Pending Payment
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_summary_billing_transaction_payment](?,?,?)}");
            callStatement.setString(1, "3");
            callStatement.setString(2, sdf1.format(fromDate));
            callStatement.setString(3, sdf1.format(toDate));

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            if (lastSuccessRow != 0) {
                startRow = lastSuccessRow;
            } else {
                startRow = successPaymentRow;
            }
            startCol = 0;
            rowInx = startRow;
            colInx = startCol;

            if (rs.next()) {

                do {

                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            sheet.createRow(startRow);
                            // insert unsuccess pending title
                            sheet.getRow(rowInx).createCell(startCol).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue("Unsuccess - Pending");
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subTitleStyle);
                            colInx++;

                            // add unsuccess transaction
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            colInx++;

                            // add % unsuccess transaction
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startCol + 1), totalPaymentRow, CellReference.convertNumToColString(startCol + 1), lastRow + 1, CellReference.convertNumToColString(startCol + 1), totalPaymentRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            colInx++;
                        }
                        if (i == 2) {
                            // add amount of unsuccess
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            colInx++;

                            // add % amount of unsuccess
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol - 1), totalPaymentRow, CellReference.convertNumToColString(lastCol - 1), lastRow + 1, CellReference.convertNumToColString(lastCol - 1), totalPaymentRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                            rowInx++;
                            lastRow = rowInx;
                            unsuccessPendingRow = rowInx;
                        }
                    }
                } while (rs.next());
            }

            // insert detail unsuccess pending credit payment
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_summary_billing_transaction_data](?,?,?)}");
            callStatement.setString(1, "2");
            callStatement.setString(2, sdf1.format(fromDate));
            callStatement.setString(3, sdf1.format(toDate));

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            startRow = unsuccessPendingRow;
            startCol = 0;
            rowInx = startRow;
            lastRow = rowInx;

            if (rs.next()) {

                do {

                    sheet.createRow(startRow);
                    colInx = startCol;

                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            sheet.getRow(rowInx).createCell(startCol).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getString(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeTitleStyle);
                            colInx++;
                        }
                        if (i == 2) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startCol + 1), unsuccessPendingRow, CellReference.convertNumToColString(startCol + 1), lastRow + 1, CellReference.convertNumToColString(startCol + 1), unsuccessPendingRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            colInx++;
                        }
                        if (i == 3) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol - 1), unsuccessPendingRow, CellReference.convertNumToColString(lastCol - 1), lastRow + 1, CellReference.convertNumToColString(lastCol - 1), unsuccessPendingRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                        }
                    }
                    rowInx++;
                    startRow = rowInx;
                    lastRow = rowInx;
                    unsuccessPendingCreditRow = rowInx;

                } while (rs.next());
            }

            // insert unsuccess pending credit reason
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_summary_billing_transaction_reason](?,?,?)}");
            callStatement.setString(1, "1");
            callStatement.setString(2, sdf1.format(fromDate));
            callStatement.setString(3, sdf1.format(toDate));

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            startRow = unsuccessPendingCreditRow;
            startCol = 0;
            rowInx = startRow;
            lastRow = rowInx;

            if (rs.next()) {

                do {

                    sheet.createRow(startRow);
                    colInx = startCol;

                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            sheet.getRow(rowInx).createCell(startCol).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getString(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(dataStyle);
                            colInx++;
                        }
                        if (i == 2) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startCol + 1), unsuccessPendingCreditRow, CellReference.convertNumToColString(startCol + 1), lastRow + 1, CellReference.convertNumToColString(startCol + 1), unsuccessPendingCreditRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            colInx++;
                        }
                        if (i == 3) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol - 1), unsuccessPendingCreditRow, CellReference.convertNumToColString(lastCol - 1), lastRow + 1, CellReference.convertNumToColString(lastCol - 1), unsuccessPendingCreditRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                            rowInx++;
                            startRow = rowInx;
                            lastRow = rowInx;
                            lastPendingCreditReasonRow = rowInx;
                        }
                    }

                } while (rs.next());
            }

            // insert detail unsuccess pending debit payment
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_summary_billing_transaction_data](?,?,?)}");
            callStatement.setString(1, "3");
            callStatement.setString(2, sdf1.format(fromDate));
            callStatement.setString(3, sdf1.format(toDate));

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            if (lastPendingCreditReasonRow != 0) {
                startRow = lastPendingCreditReasonRow;
            } else {
                startRow = unsuccessPendingRow;
            }
            startCol = 0;
            rowInx = startRow;
            lastRow = rowInx;

            if (rs.next()) {

                do {

                    sheet.createRow(startRow);
                    colInx = startCol;

                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            sheet.getRow(rowInx).createCell(startCol).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getString(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeTitleStyle);
                            colInx++;
                        }
                        if (i == 2) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startCol + 1), unsuccessPendingRow, CellReference.convertNumToColString(startCol + 1), lastRow + 1, CellReference.convertNumToColString(startCol + 1), unsuccessPendingRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            colInx++;
                        }
                        if (i == 3) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol - 1), unsuccessPendingRow, CellReference.convertNumToColString(lastCol - 1), lastRow + 1, CellReference.convertNumToColString(lastCol - 1), unsuccessPendingRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                        }
                    }
                    rowInx++;
                    startRow = rowInx;
                    lastRow = rowInx;
                    unsuccessPendingDebitRow = rowInx;

                } while (rs.next());
            }

            // insert unsuccess pending debit reason
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_summary_billing_transaction_reason](?,?,?)}");
            callStatement.setString(1, "2");
            callStatement.setString(2, sdf1.format(fromDate));
            callStatement.setString(3, sdf1.format(toDate));

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            startRow = unsuccessPendingDebitRow;
            startCol = 0;
            rowInx = startRow;
            lastRow = rowInx;

            if (rs.next()) {

                do {

                    sheet.createRow(startRow);
                    colInx = startCol;

                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            sheet.getRow(rowInx).createCell(startCol).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getString(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(dataStyle);
                            colInx++;
                        }
                        if (i == 2) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startCol + 1), unsuccessPendingDebitRow, CellReference.convertNumToColString(startCol + 1), lastRow + 1, CellReference.convertNumToColString(startCol + 1), unsuccessPendingDebitRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            colInx++;
                        }
                        if (i == 3) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol - 1), unsuccessPendingDebitRow, CellReference.convertNumToColString(lastCol - 1), lastRow + 1, CellReference.convertNumToColString(lastCol - 1), unsuccessPendingDebitRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                            rowInx++;
                            startRow = rowInx;
                            lastRow = rowInx;
                            lastPendingDebitReasonRow = rowInx;
                        }
                    }

                } while (rs.next());
            }

            // insert Unsuccess Rejected Payment
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_summary_billing_transaction_payment](?,?,?)}");
            callStatement.setString(1, "4");
            callStatement.setString(2, sdf1.format(fromDate));
            callStatement.setString(3, sdf1.format(toDate));

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            if (lastPendingDebitReasonRow != 0) {
                startRow = lastPendingDebitReasonRow;
            } else if (lastPendingCreditReasonRow != 0) {
                startRow = lastPendingCreditReasonRow;
            } else {
                startRow = unsuccessPendingRow;
            }
            startCol = 0;
            rowInx = startRow;
            colInx = startCol;

            if (rs.next()) {

                do {

                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            sheet.createRow(startRow);
                            // insert unsuccess rejected title
                            sheet.getRow(rowInx).createCell(startCol).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue("Unsuccess - Rejected");
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subTitleStyle);
                            colInx++;

                            // add unsuccess transaction
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            colInx++;

                            // add % unsuccess transaction
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startCol + 1), totalPaymentRow, CellReference.convertNumToColString(startCol + 1), lastRow + 1, CellReference.convertNumToColString(startCol + 1), totalPaymentRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            colInx++;
                        }
                        if (i == 2) {
                            // add amount of unsuccess
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            colInx++;

                            // add % amount of unsuccess
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol - 1), totalPaymentRow, CellReference.convertNumToColString(lastCol - 1), lastRow + 1, CellReference.convertNumToColString(lastCol - 1), totalPaymentRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(subDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                            rowInx++;
                            lastRow = rowInx;
                            unsuccessRejectRow = rowInx;
                        }
                    }
                } while (rs.next());
            }

            // insert detail unsuccess reject credit payment
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_summary_billing_transaction_data](?,?,?)}");
            callStatement.setString(1, "4");
            callStatement.setString(2, sdf1.format(fromDate));
            callStatement.setString(3, sdf1.format(toDate));

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            startRow = unsuccessRejectRow;
            startCol = 0;
            rowInx = startRow;
            lastRow = rowInx;

            if (rs.next()) {

                do {

                    sheet.createRow(startRow);
                    colInx = startCol;

                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            sheet.getRow(rowInx).createCell(startCol).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getString(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeTitleStyle);
                            colInx++;
                        }
                        if (i == 2) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startCol + 1), unsuccessRejectRow, CellReference.convertNumToColString(startCol + 1), lastRow + 1, CellReference.convertNumToColString(startCol + 1), unsuccessRejectRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            colInx++;
                        }
                        if (i == 3) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol - 1), unsuccessRejectRow, CellReference.convertNumToColString(lastCol - 1), lastRow + 1, CellReference.convertNumToColString(lastCol - 1), unsuccessRejectRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                        }
                    }
                    rowInx++;
                    startRow = rowInx;
                    lastRow = rowInx;
                    unsuccessRejectCreditRow = rowInx;

                } while (rs.next());
            }

            // insert unsuccess reject credit reason
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_summary_billing_transaction_reason](?,?,?)}");
            callStatement.setString(1, "3");
            callStatement.setString(2, sdf1.format(fromDate));
            callStatement.setString(3, sdf1.format(toDate));

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            startRow = unsuccessRejectCreditRow;
            startCol = 0;
            rowInx = startRow;
            lastRow = rowInx;

            if (rs.next()) {

                do {

                    sheet.createRow(startRow);
                    colInx = startCol;

                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            sheet.getRow(rowInx).createCell(startCol).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getString(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(dataStyle);
                            colInx++;
                        }
                        if (i == 2) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startCol + 1), unsuccessRejectCreditRow, CellReference.convertNumToColString(startCol + 1), lastRow + 1, CellReference.convertNumToColString(startCol + 1), unsuccessRejectCreditRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            colInx++;
                        }
                        if (i == 3) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol - 1), unsuccessRejectCreditRow, CellReference.convertNumToColString(lastCol - 1), lastRow + 1, CellReference.convertNumToColString(lastCol - 1), unsuccessRejectCreditRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                            rowInx++;
                            startRow = rowInx;
                            lastRow = rowInx;
                            lastRejectCreditReasonRow = rowInx;
                        }
                    }

                } while (rs.next());
            }

            // insert detail unsuccess reject debit payment
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_summary_billing_transaction_data](?,?,?)}");
            callStatement.setString(1, "5");
            callStatement.setString(2, sdf1.format(fromDate));
            callStatement.setString(3, sdf1.format(toDate));

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            if (lastRejectCreditReasonRow != 0) {
                startRow = lastRejectCreditReasonRow;
            } else {
                startRow = unsuccessRejectRow;
            }
            startCol = 0;
            rowInx = startRow;
            lastRow = rowInx;

            if (rs.next()) {

                do {

                    sheet.createRow(startRow);
                    colInx = startCol;

                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            sheet.getRow(rowInx).createCell(startCol).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getString(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeTitleStyle);
                            colInx++;
                        }
                        if (i == 2) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startCol + 1), unsuccessRejectRow, CellReference.convertNumToColString(startCol + 1), lastRow + 1, CellReference.convertNumToColString(startCol + 1), unsuccessRejectRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            colInx++;
                        }
                        if (i == 3) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol - 1), unsuccessRejectRow, CellReference.convertNumToColString(lastCol - 1), lastRow + 1, CellReference.convertNumToColString(lastCol - 1), unsuccessRejectRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(typeDetailStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                        }
                    }
                    rowInx++;
                    startRow = rowInx;
                    lastRow = rowInx;
                    unsuccessRejectDebitRow = rowInx;

                } while (rs.next());
            }

            // insert unsuccess reject debit reason
            callStatement = connection.prepareCall("{call [dbo].[sp_fwd_summary_billing_transaction_reason](?,?,?)}");
            callStatement.setString(1, "4");
            callStatement.setString(2, sdf1.format(fromDate));
            callStatement.setString(3, sdf1.format(toDate));

            try {
                rs = callStatement.executeQuery();
            } catch (Exception e) {
                System.out.println("No data from Stored Procedure");
            }

            rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            startRow = unsuccessRejectDebitRow;
            startCol = 0;
            rowInx = startRow;
            lastRow = rowInx;

            if (rs.next()) {

                do {

                    sheet.createRow(startRow);
                    colInx = startCol;

                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            sheet.getRow(rowInx).createCell(startCol).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getString(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(dataStyle);
                            colInx++;
                        }
                        if (i == 2) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startCol + 1), unsuccessRejectDebitRow, CellReference.convertNumToColString(startCol + 1), lastRow + 1, CellReference.convertNumToColString(startCol + 1), unsuccessRejectDebitRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            colInx++;
                        }
                        if (i == 3) {
                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_STRING);
                            sheet.getRow(rowInx).getCell(colInx).setCellValue(rs.getInt(i));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            colInx++;

                            sheet.getRow(rowInx).createCell(colInx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowInx).getCell(colInx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(lastCol - 1), unsuccessRejectDebitRow, CellReference.convertNumToColString(lastCol - 1), lastRow + 1, CellReference.convertNumToColString(lastCol - 1), unsuccessRejectDebitRow));
                            sheet.getRow(rowInx).getCell(colInx).setCellStyle(detailCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowInx).getCell(colInx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                            rowInx++;
                            startRow = rowInx;
                            lastRow = rowInx;
                            lastRejectDebitReasonRow = rowInx;
                        }
                    }

                } while (rs.next());
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
