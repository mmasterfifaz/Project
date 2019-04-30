package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.MarketingDAO;
import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;

@ManagedBean
@RequestScoped
public class MtlOutcomeBreakdownReport {

    private static Logger log = Logger.getLogger(MtlOutcomeBreakdownReport.class);

    private Integer campaignId;
    private String campaignName = "All";
    private Integer marketingId;
    private String marketingName = "All";
    private Date fromDate;
    private Date toDate;
    private Integer group;

    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
    private String fileName = "";

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:outcomebreakdownreport:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        
        fromDate = new Date();
        toDate = new Date();
    }
    
    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }
    
    public Map<String, Integer> getCampaignList() {
        return this.getCampaignDAO().getCampaignList();
    }

    public Map<String, Integer> getMarketingList() {
        return this.getMarketingDAO().getMarketList();
    }   
    
    public String getCampaignName() {
        return campaignName;
    }
    
    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
        this.campaignName = "All";
        try {
            if (campaignId!=null && campaignId!=0) {
                this.campaignName = this.getCampaignDAO().findCampaign(campaignId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    
    public Integer getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(Integer marketingId) {
        this.marketingId = marketingId;
        this.marketingName = "All";
        try {
            if (marketingId != null && marketingId != 0) {
                this.marketingName = this.getMarketingDAO().findMarketing(marketingId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public String getMarketingName() {
        return marketingName;
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
        Calendar cal = Calendar.getInstance();
        cal.setTime(toDate);
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        this.toDate = cal.getTime();
    }
    
    public void setGroup(Integer group) {
        this.group = group;
    }
    
    public Integer getGroup() {
        return group;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public String getExcelQuery() {
        return getQuery();
    }

    public String getQuery() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        String query = "";
        if (group == 1) {
            //query = "SELECT [RowStyle],[Status],[Total],[%Total] FROM udf_outcome_breakdown_report(" + campaignId + "," + marketingId + ",'" + sdf.format(fromDate) + "','" + sdf.format(toDate) + "') ORDER BY [SeqNo]";       
            query = "{call [dbo].[sp_mtl_outcome_breakdown_report_by_date](" + campaignId + "," + marketingId + ",'" + sdf.format(fromDate) + "','" + sdf.format(toDate) + "')}";
        }else{
            query = "{call [dbo].[sp_mtl_outcome_breakdown_report_by_tsr](" + campaignId + "," + marketingId + ",'" + sdf.format(fromDate) + "','" + sdf.format(toDate) + "')}";
        }
        //System.out.println("Query : " + query);
        return query;
    }

    private void generateXLS() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
        String date = df.format(new Date());
        String fileName = "OutcomeBreakdownReport_" + date + ".xls";
        
        FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("application/vnd.ms-excel");
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Report");
            
        HSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(font);
        titleStyle.setAlignment(CellStyle.ALIGN_LEFT);
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        
        HSSFRow trow = sheet.createRow(1);
        
        trow.createCell(0).setCellValue("Outcome Breakdown Report - Team"); trow.getCell(0).setCellStyle(titleStyle);
        trow = sheet.createRow(4);
        trow.createCell(0).setCellValue("Campaign : " + this.campaignName); trow.getCell(0).setCellStyle(titleStyle);
        trow = sheet.createRow(5);
        trow.createCell(0).setCellValue("Marketing: " + this.marketingName); trow.getCell(0).setCellStyle(titleStyle);
        trow = sheet.createRow(6);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
        trow.createCell(0).setCellValue("Period: " + sdf.format(fromDate) + " - " + sdf.format(toDate)); trow.getCell(0).setCellStyle(titleStyle);
        
        try {
            retrieveData(workbook, sheet);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
        } catch (Exception e) {
            log.error(e);
        }
        FacesContext.getCurrentInstance().responseComplete();
    }
    
    public void retrieveData(HSSFWorkbook workbook, HSSFSheet sheet) throws SQLException {
        HSSFPalette palette = workbook.getCustomPalette(); 
        palette.setColorAtIndex(HSSFColor.GREY_50_PERCENT.index, (byte) 166, (byte) 166, (byte) 166);
        palette.setColorAtIndex(HSSFColor.GREY_25_PERCENT.index, (byte) 217, (byte) 217, (byte) 217);
        HSSFCreationHelper createHelper = workbook.getCreationHelper();
        
        HSSFCellStyle headCellStyle = workbook.createCellStyle();
        HSSFFont headFont = workbook.createFont();
        headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headCellStyle.setFont(headFont);
        headCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        headCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        headCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        HSSFRow dataRow;
        HSSFCell dataCell;

        HSSFCellStyle detailCellStyle = workbook.createCellStyle();
        detailCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderLeft(CellStyle.BORDER_THIN);

        HSSFCellStyle summaryCellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        //font.setColor(IndexedColors.WHITE.getIndex());
        //summaryCellStyle.setFont(font);
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
            
            CallableStatement callStatement = connection.prepareCall("{call [dbo].[sp_fwd_load_contact_history]}");
            callStatement.executeUpdate();
            
            //statement = connection.createStatement();
            //rs = statement.executeQuery(this.getExcelQuery());        
            callStatement = connection.prepareCall(this.getExcelQuery());
            rs = callStatement.executeQuery();
            //System.out.println(rs.getMetaData().getColumnCount());
            
            HSSFRow hrow = sheet.createRow(10);
            for (int i = 3; i <= rs.getMetaData().getColumnCount(); i++ ) {
                dataCell = hrow.createCell(i-3);
                dataCell.setCellValue(rs.getMetaData().getColumnName(i));
                dataCell.setCellStyle(headCellStyle);

                if (i == 3) {
                    CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.FILL_FOREGROUND_COLOR, HSSFColor.GREY_50_PERCENT.index);
                }else{
                    CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.FILL_FOREGROUND_COLOR, HSSFColor.GREY_25_PERCENT.index);
                }
                CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.FILL_PATTERN, CellStyle.SOLID_FOREGROUND);
            }
                   
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCol = rsmd.getColumnCount();
            int numStartRow = 11;
            boolean startSubHeader = false;
            if (rs.next()) {
                do {
                    if (Integer.parseInt(rs.getString(2)) >= 3000 && !startSubHeader) {
                        startSubHeader = true;
                        hrow = sheet.createRow(numStartRow++);
                        for (int i = 3; i <= rs.getMetaData().getColumnCount(); i++) {
                            dataCell = hrow.createCell(i-3);
                            dataCell.setCellStyle(headCellStyle);
                            if (i == 3) {
                                dataCell.setCellValue(rs.getMetaData().getColumnName(i));
                                CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.FILL_FOREGROUND_COLOR, HSSFColor.GREY_50_PERCENT.index);
                            }else if (i%2 == 0){
                                dataCell.setCellValue("Total");
                                CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.FILL_FOREGROUND_COLOR, HSSFColor.GREY_25_PERCENT.index);
                            }else if (i%2 == 1) {
                                dataCell.setCellValue("%");
                                CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.FILL_FOREGROUND_COLOR, HSSFColor.GREY_25_PERCENT.index);        
                            }
                            CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.FILL_PATTERN, CellStyle.SOLID_FOREGROUND);
                        }
                    }
                    dataRow = sheet.createRow(numStartRow);
                    for (int i = 3; i <= numCol; i++) {
                        dataCell = dataRow.createCell(i-3);
                        if (rs.getString(1).equalsIgnoreCase("Detail") || rs.getString(2).equalsIgnoreCase("2000")) {
                            dataCell.setCellStyle(detailCellStyle);
                        }else if (rs.getString(1).equalsIgnoreCase("Summary") && !rs.getString(2).equalsIgnoreCase("2000")) {
                            dataCell.setCellStyle(summaryCellStyle);
                            if ( i==3 ) {
                                CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.FILL_FOREGROUND_COLOR, HSSFColor.GREY_50_PERCENT.index);
                                CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.FILL_PATTERN, CellStyle.SOLID_FOREGROUND);
                            }else{
                                CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.FILL_FOREGROUND_COLOR, HSSFColor.GREY_25_PERCENT.index);
                                CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.FILL_PATTERN, CellStyle.SOLID_FOREGROUND);
                            }
                        }

                        if (rsmd.getColumnName(i).equalsIgnoreCase("Status")) {
                            CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_LEFT);
                            dataCell.setCellValue(rs.getString(i));
                        }else if (i % 2 == 0){
                            CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.DATA_FORMAT, createHelper.createDataFormat().getFormat("###,###,##0"));
                            dataCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            dataCell.setCellValue(Integer.parseInt(rs.getString(i)));
                        }else if (i % 2 == 1){
                            //CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.DATA_FORMAT, createHelper.createDataFormat().getFormat("###,###,##0.00"));
                            //dataCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            //dataCell.setCellValue(Float.parseFloat(rs.getString(i)));
                            dataCell.setCellValue(rs.getString(i) + "%");
                        }
                    }
                    numStartRow++;
                } while (rs.next());
                
                for (int col = 0; col < numCol; col++) {
                    workbook.getSheetAt(0).autoSizeColumn(col);
                }
            }         
        } catch (Exception e) {
            log.error(e);
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

    public void retrieveData2(HSSFWorkbook workbook, HSSFSheet sheet) throws SQLException {

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            HSSFRow dataRow = null;
            HSSFCell dataCell;
            HSSFCellStyle body_default = workbook.createCellStyle();
            body_default.setAlignment(CellStyle.ALIGN_CENTER);
            body_default.setBorderBottom(CellStyle.BORDER_THIN);
            body_default.setBorderLeft(CellStyle.BORDER_THIN);
            body_default.setBorderRight(CellStyle.BORDER_THIN);
            body_default.setBorderTop(CellStyle.BORDER_THIN);
            body_default.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            HSSFCellStyle body_sum = workbook.createCellStyle();
            body_sum.setAlignment(CellStyle.ALIGN_CENTER);
            body_sum.setBorderBottom(CellStyle.BORDER_THIN);
            body_sum.setBorderLeft(CellStyle.BORDER_THIN);
            body_sum.setBorderRight(CellStyle.BORDER_THIN);
            body_sum.setBorderTop(CellStyle.BORDER_THIN);
            body_sum.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            body_sum.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
            body_sum.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(this.getExcelQuery());
            
            ResultSetMetaData rsmd = rs.getMetaData();
            int numOfCol = rsmd.getColumnCount();
            int numStartRowData = 1;
            if (rs.next()) {
                do {
                    dataRow = sheet.createRow(numStartRowData);
                    for (int i = 1; i < numOfCol; i++) {
                        dataCell = dataRow.createCell(i - 1);
                        dataCell.setCellStyle(body_default);
                        System.out.println("rs.getString(numOfCol) : " + rs.getString(numOfCol) + " | " + rs.getString(numOfCol).contains("Summary"));
                        if (rs.getString(numOfCol).contains("Summary")) {
                            dataCell.setCellStyle(body_sum);
                        }
                        configFillData(dataCell, rs, rsmd, i);
                    }
                    numStartRowData++;
                } while (rs.next());
            }
        } catch (Exception e) {
            log.error(e);
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
    
    public void configFillData(HSSFCell dataCell, ResultSet rs, ResultSetMetaData rsmd, int i) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yy", Locale.US);
        try {
            if (rsmd.getColumnTypeName(i).equalsIgnoreCase("varchar")) {
                dataCell.setCellValue(rs.getString(i));
                if (i == 8) {
                    if (rs.getString(i) == null || (rs.getString(i) != null && rs.getString(i).equalsIgnoreCase("null"))) {
                        dataCell.setCellValue("0");
                    }
                }
            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("integer")) {
                dataCell.setCellValue("integer " + rs.getInt(i));
            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("char")) {
                dataCell.setCellValue("char " + rs.getString(i));
            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("float")) {
                dataCell.setCellValue("float " + rs.getFloat(i));
            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("DATE")) {
                dataCell.setCellValue(rs.getDate(i).toString());
            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("DATETIME")) {
                if (rs.getTimestamp(i) != null) {
                    dataCell.setCellValue(sdf1.format(new Date(rs.getTimestamp(i).getTime())));
                } else {
                    dataCell.setCellValue(" ");
                }
            } else {
                if (rs.getString(i) != null) {
                    dataCell.setCellValue(rs.getString(i));
                } else {
                    dataCell.setCellValue(" ");
                }
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MtlOutcomeBreakdownReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reportListener(ActionEvent event) {
        generateXLS();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}