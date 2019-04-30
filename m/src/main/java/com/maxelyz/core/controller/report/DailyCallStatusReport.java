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

/**
 *
 * @author admin
 */
@ManagedBean
@RequestScoped
public class DailyCallStatusReport extends AbstractReportBaseController {

    private static Logger log = Logger.getLogger(DailyCallStatusReport.class);
    private SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
    private Integer sumContactValue = 0;
    private Double contactRate = 0.0;
    private Double sumContactRate = 0.0;
    private String fileName = "";
    DecimalFormat df = new DecimalFormat("#,##0.00");
    DecimalFormat df1 = new DecimalFormat("#,###");

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:dailycallstatus:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        super.init();
    }

    @Override
    protected String getReportPath() {
        return "/report/dailyCallStatus.jasper";
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

        String select = "select contact_result.status_id, contact_result.name, count(contact_result.status_id) as value"
                + " (select count(contact_result.status_id)"
                + " from users,assignment_detail,contact_result,assignment"
                + " where contact_result.id = assignment_detail.contact_result_id"
                + " and assignment.id = assignment_detail.assignment_id"
                + " and assignment_detail.user_id = users.id"
                + " and assignment_detail.contact_date between '" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "') as total"
                + " from users,assignment_detail, contact_result,assignment"
                + " where contact_result.id = assignment_detail.contact_result_id and assignment.id = assignment_detail.assignment_id and assignment_detail.user_id = users.id";
        String where = "where (assignment_detail.contact_date between '" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "') ";
        if (campaignId != 0 && campaignId != null) {
            where += "and campaign_id = " + campaignId + " ";
        }
        if (marketingId != 0 && marketingId != null) {
            where += "marketing_id = " + marketingId + " ";
        }
        if (userGroupId != 0 && userGroupId != null) {
            where += "and user_group_id = " + userGroupId + " ";
        }
        String groupBy = "group by contact_result.status_id,contact_result.name";
        String orderBy = "order by contact_result.status_id";
        String query = select + where + groupBy + orderBy;

        return query;
    }

    public void reportListener(ActionEvent event) {
        generateXLS();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void initSummation() {
        sumContactValue = 0;
        sumContactRate = 0.0;
    }

    private void generateXLS() {
        initSummation();
        FileInputStream file = null;
        Date d = new Date();
        String start = sdf2.format(d);
        fileName = "Daily_Call_Status_Report_" + start + ".xls";
        try {
            file = new FileInputStream(new File(JSFUtil.getuploadPath(), "report" + "/DailyCallStatus/Daily_call_status_report_template.xls"));
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
            row = sheet.getRow(6);
            cell = row.getCell(1);
            String contactDate = sdf1.format(fromDate) + " - " + sdf1.format(toDate);
            cell.setCellValue(contactDate);
            cell = sheet.getRow(7).getCell(1);
            cell.setCellValue(campaignName);
            cell = sheet.getRow(8).getCell(1);
            cell.setCellValue(marketingName);
            cell = sheet.getRow(9).getCell(1);
            cell.setCellValue(groupName);

            retrieveData(workbook, sheet);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(DailyCallStatusReport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(DailyCallStatusReport.class.getName()).log(Level.SEVERE, null, ex);
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
        
        HSSFCellStyle descriptionCellStyle = workbook.createCellStyle();
        descriptionCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        descriptionCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        descriptionCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        descriptionCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        descriptionCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        descriptionCellStyle.setBorderLeft(CellStyle.BORDER_THIN);

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
            rs = statement.executeQuery("{call sp_fwd_daily_call_status_report(" + "'" + sdf.format(fromDate) + "','" + sdf.format(toDate) + "','" + campaignId + "','" + marketingId + "','" + userGroupId + "')}");
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCol = rsmd.getColumnCount();
            int numStartRow = 12;
            int cellInx = 0;

            if (rs.next()) {
                do {
                    dataRow = sheet.createRow(numStartRow);
                    cellInx = 0;
                    for (int i = 1; i < numCol; i++) {
                        dataCell = dataRow.createCell(cellInx);
                        
                        if (i < 2) {
                            dataCell.setCellStyle(detailCellStyle);
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                            dataCell.setCellValue(d);
                        }
                        if (i == 2) {
                            dataCell.setCellStyle(descriptionCellStyle);
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                            dataCell.setCellValue(d);
                        }
                        if (i == 3) {
                            dataCell.setCellStyle(detailCellStyle);
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? df1.format(rs.getInt(i)) : "0";
                            dataCell.setCellValue(d);
                        }

                        if (i == 3) {
                            dataCell.setCellStyle(detailCellStyle);
                            Double sub = 0.0, total = 0.0;
                            contactRate = 0.0;
                            if (rs.getString(4) != null && !rs.getString(4).isEmpty() && rs.getDouble(4) > 0) {
                                total = rs.getDouble(4);
                                if (rs.getString(3) != null && !rs.getString(3).isEmpty()) {
                                    sub = rs.getDouble(3);
                                }
                                contactRate = (sub / total) * 100;
                            }
                            cellInx++;
                            dataCell = dataRow.createCell(cellInx);
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellValue(df.format(contactRate) + "%");
                        }
                        cellInx++;
                    }
                    //Calculate Sum for each column
                    calculateSummation(rs);
                    numStartRow++;

                } while (rs.next());
                //Summation total
                dataRow = sheet.createRow(numStartRow);
                for (int j = 0; j < 1; j++) {
                    dataCell = dataRow.createCell(j);
                    dataCell.setCellStyle(summaryCellStyle);
                    dataCell.setCellValue("");
                }
                cellInx = 1;
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue("Total");
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumContactValue));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df.format(sumContactRate) + "%");
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

    public void calculateSummation(ResultSet rs) {

        try {
            sumContactValue += (rs.getString(3) != null && !rs.getString(3).isEmpty()) ? rs.getInt(3) : 0;
            sumContactRate += contactRate;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(DailyCallStatusReport.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getSumContactValue() {
        return sumContactValue;
    }

    public void setSumContactValue(Integer sumContactValue) {
        this.sumContactValue = sumContactValue;
    }

    public Double getContactRate() {
        return contactRate;
    }

    public void setContactRate(Double contactRate) {
        this.contactRate = contactRate;
    }

    public Double getSumContactRate() {
        return sumContactRate;
    }

    public void setSumContactRate(Double sumContactRate) {
        this.sumContactRate = sumContactRate;
    }

}
