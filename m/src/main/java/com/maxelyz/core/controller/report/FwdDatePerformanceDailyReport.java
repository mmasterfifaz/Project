package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.MarketingDAO;
import com.maxelyz.utils.SecurityUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


@ManagedBean
@RequestScoped
public class FwdDatePerformanceDailyReport {
    private static Logger log = Logger.getLogger(FwdDatePerformanceDailyReport.class);
    
    private Integer campaignId = 0;
    private String campaignName = "All";
    private Integer marketingId = 0;
    private String marketingName = "All";
    private Date fromDate;
    private Date toDate;
    private Integer group = 1;
      
    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:fwdsalesreport:view")) {
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

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private void generateXLS() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
        String date = df.format(new Date());
        String fileName = "New_Daily_Report_" + date + ".xls";
        
        FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("application/vnd.ms-excel");
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Report");
        
        HSSFFont titleFont = workbook.createFont();
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleFont.setColor(HSSFColor.BLACK.index);
        
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(CellStyle.ALIGN_LEFT);
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        
        HSSFRow trow;
        //trow = sheet.createRow(1);
        //trow.createCell(1).setCellValue("Summary Sales Report - Team"); trow.getCell(1).setCellStyle(titleStyle);
        trow = sheet.createRow(2);
        trow.createCell(0).setCellValue("Campaign"); trow.getCell(0).setCellStyle(titleStyle);
        trow.createCell(1).setCellValue(this.campaignName); trow.getCell(1).setCellStyle(titleStyle);
        trow = sheet.createRow(3);
        SimpleDateFormat sdtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.US);
        trow.createCell(0).setCellValue("Launch Date"); trow.getCell(0).setCellStyle(titleStyle);
        trow.createCell(1).setCellValue(sdtf.format(new Date())); trow.getCell(1).setCellStyle(titleStyle);
        trow = sheet.createRow(4);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
        trow.createCell(0).setCellValue("Start - End Report"); trow.getCell(0).setCellStyle(titleStyle);
        trow.createCell(1).setCellValue(sdf.format(fromDate) + " - " + sdf.format(toDate)); trow.getCell(1).setCellStyle(titleStyle);
//        trow = sheet.createRow(5);
//        trow.createCell(0).setCellValue("Total Working Days"); trow.getCell(0).setCellStyle(titleStyle);
//        trow.createCell(1).setCellValue("0");
        
        HSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);

        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font2.setColor(HSSFColor.BLACK.index);
        
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        
        HSSFCellStyle cellAltStyle = workbook.createCellStyle();
        cellAltStyle.setFont(font2);
        cellAltStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellAltStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellAltStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellAltStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellAltStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellAltStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellAltStyle.setWrapText(true);
        
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor color = palette.findSimilarColor(22, 54, 92);
        HSSFColor colorAlt = palette.findSimilarColor(255, 255, 26);
        
        // get the palette index of that color 
        short palIndex = color.getIndex();
        short palAltIndex = colorAlt.getIndex();
        
        // code to get the style for the cell goes here
        cellStyle.setFillForegroundColor(palIndex);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellAltStyle.setFillForegroundColor(palAltIndex);
        cellAltStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
             
        int colnum = 0;
        int rownum = 9;
        
        HSSFRow hrow1 = sheet.createRow(rownum);
        HSSFRow hrow2 = sheet.createRow(rownum + 1);
        HSSFRow hrow3 = sheet.createRow(rownum + 2);

        hrow1.createCell(colnum).setCellValue("Date"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellAltStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));

        hrow2.createCell(colnum).setCellValue("New"); hrow2.getCell(colnum).setCellStyle(cellAltStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 2, CellReference.convertNumToColString(colnum - 1), rownum + 3)));

        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue("Follow"); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 2, CellReference.convertNumToColString(colnum - 1), rownum + 3)));

        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue("Total"); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 2, CellReference.convertNumToColString(colnum - 1), rownum + 3)));

        hrow1.createCell(colnum -3).setCellValue("List Solicited"); hrow1.getCell(colnum - 3).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 3), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 1)));

        hrow1.createCell(colnum).setCellValue("Call Attempt No."); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));

        hrow1.createCell(colnum).setCellValue("Call Attempt Rate"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));

        hrow1.createCell(colnum).setCellValue("Accumulate Total List Used"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));

        hrow1.createCell(colnum).setCellValue("Daily Contactable"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));

        hrow1.createCell(colnum).setCellValue("Accumulate Daily Contactable"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));

        hrow1.createCell(colnum).setCellValue("Daily Contactable %"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));

        hrow1.createCell(colnum).setCellValue("Accumulate Daily Contactable %"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));

        hrow1.createCell(colnum).setCellValue("Apps. Response"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));

        hrow1.createCell(colnum).setCellValue("Sales Close Rate %"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));

        try {
            retrieveData(workbook, sheet);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
        } catch (Exception e) {
            log.error(e);
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void retrieveData(HSSFWorkbook workbook, HSSFSheet sheet) throws SQLException {
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
        int totalFollow = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
              
        HSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);

        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font2.setColor(HSSFColor.BLACK.index);
        
        HSSFCellStyle headCellStyle = workbook.createCellStyle();
        headCellStyle.setFont(font);
        headCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        headCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        headCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headCellStyle.setWrapText(true);

        HSSFCellStyle headCellStyle2 = workbook.createCellStyle();
        headCellStyle2.setFont(font);
        headCellStyle2.setBorderTop(CellStyle.BORDER_THIN);
        headCellStyle2.setBorderRight(CellStyle.BORDER_THIN);
        headCellStyle2.setBorderLeft(CellStyle.BORDER_THIN);
        headCellStyle2.setBorderBottom(CellStyle.BORDER_THIN);
        headCellStyle2.setAlignment(CellStyle.ALIGN_RIGHT);
        headCellStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headCellStyle2.setWrapText(true);
        
        HSSFCellStyle headCellAltStyle = workbook.createCellStyle();
        headCellAltStyle.setFont(font2);
        headCellAltStyle.setBorderTop(CellStyle.BORDER_THIN);
        headCellAltStyle.setBorderRight(CellStyle.BORDER_THIN);
        headCellAltStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headCellAltStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headCellAltStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headCellAltStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headCellAltStyle.setWrapText(true);
        
        HSSFPalette palette = workbook.getCustomPalette(); 
        HSSFColor color = palette.findSimilarColor(22, 54, 92);
        HSSFColor colorAlt = palette.findSimilarColor(255, 255, 26);
        
        // get the palette index of that color 
        short palIndex = color.getIndex();
        short palAltIndex = colorAlt.getIndex();
        
        // code to get the style for the cell goes here
        headCellStyle.setFillForegroundColor(palIndex);
        headCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headCellStyle2.setFillForegroundColor(palIndex);
        headCellStyle2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headCellAltStyle.setFillForegroundColor(palAltIndex);
        headCellAltStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
        
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        dateCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        dateCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        dateCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        dateCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        dateCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        
        HSSFCellStyle numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        numberCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        numberCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        numberCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        numberCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        numberCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        
        HSSFColor colorTotal = palette.findSimilarColor(255, 105, 180);
        short palTotalIndex = colorTotal.getIndex();
        
        HSSFCellStyle monthRowCellStyle = workbook.createCellStyle();
        HSSFFont monthFont = workbook.createFont();
        //monthFont.setColor(IndexedColors.WHITE.getIndex());
        monthFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        monthRowCellStyle.setFont(monthFont);
        monthRowCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        monthRowCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        monthRowCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        monthRowCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        monthRowCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        monthRowCellStyle.setBorderLeft(CellStyle.BORDER_THIN);        
        //monthRowCellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        monthRowCellStyle.setFillForegroundColor(palTotalIndex);
        monthRowCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle yearRowCellStyle = workbook.createCellStyle();
        HSSFFont yearFont = workbook.createFont();
        //yearFont.setColor(IndexedColors.WHITE.getIndex());
        yearFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        yearRowCellStyle.setFont(yearFont);
        yearRowCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        yearRowCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        yearRowCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        yearRowCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        yearRowCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        yearRowCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        //yearRowCellStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        yearRowCellStyle.setFillForegroundColor(palTotalIndex);
        yearRowCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFRow dataRow;
        HSSFCell dataCell;      
        //HSSFCreationHelper createHelper = workbook.getCreationHelper();
        
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            
            //CallableStatement callStatement = connection.prepareCall("{call [dbo].[sp_fwd_load_contact_history]}");
            //callStatement.executeUpdate();
            
            CallableStatement callStatement = connection.prepareCall("{call [dbo].[sp_fwd_sale_report_with_family_by_date](?,?,?,?)}");
            callStatement.setInt(1, campaignId);
            callStatement.setInt(2, marketingId);
            callStatement.setString(3, sdf.format(fromDate));
            callStatement.setString(4, sdf.format(toDate));
            rs = callStatement.executeQuery();          
            //statement = connection.createStatement();
            //rs = statement.executeQuery(this.getExcelQuery());
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            int startRowIndx = 12;
            int rowIndx = startRowIndx; 
            int totalColumnCountInExcel = 0;
            int lastRow=0;
            
            int startColSubmitAppsIndx = 0;
            int startColSuccessAppsIndx = 0;
            int startColHeaderProductRejectedIndex = 0;

            int rownum = 9;
            HSSFRow hrow1 = sheet.getRow(rownum);
            HSSFRow hrow2 = sheet.getRow(rownum + 1);
            HSSFRow hrow3 = sheet.getRow(rownum + 2);

            LinkedHashMap<String,Integer> totalSubmitAFYPEachOfProduct =new LinkedHashMap<String,Integer>();
            LinkedHashMap<String,Integer> totalSuccessAFYPEachOfProduct =new LinkedHashMap<String,Integer>();

            LinkedHashMap<String,Integer> totalSuccessAPPEachOfProduct =new LinkedHashMap<String,Integer>();


            if (rs.next()) {
               do {
                   int colIndx = 0;

                   dataRow = sheet.createRow(rowIndx);
                   boolean isSubmit = true;
                   boolean isSuccess = false;
                   boolean isListConversion = false;
                   boolean isConversionRate = false;
                   boolean isRejectCase = false;

                   boolean isSubmitAppsNoFamily = false;
                   boolean isSubmitAFYPNoFamily = false;

                   boolean isSuccessAppsNoFamily = false;
                   boolean isSuccessAFYPNoFamily = false;

                   boolean isSubmitAppsWithFamilyAppPerFamily = false;
                   boolean isSubmitAppsWithFamilyAppPerson = false;
                   boolean isSubmitAFYPWithFamily = false;

                   boolean isSuccessAppsWithFamilyAppPerFamily = false;
                   boolean isSuccessAppsWithFamilyAppPerson = false;
                   boolean isSuccessAFYPWithFamily = false;

                   boolean isTotalSubMit = false;
                   boolean isTotalSuccess = false;

                   boolean isAPPSRejectNoFamily = false;
                   boolean isNetPremiumRejectNoFamily = false;

                   boolean isAPPSRejectWithFamily = false;
                   boolean isNetPremiumRejectWithFamily = false;

                   int lastCols =0 ;
                   int startSubmitColumnAPPS =0;
                   int startSubmitColumnAFYP =0;

                   int startSuccessColumnAPPS =0;
                   int startSuccessColumnAFYP =0;

                   int startSubmitColumnAPPSWithFamilyAppPerFamily =0;
                   int startSubmitColumnAPPSWithFamilyAppPerPerson =0;
                   int startSubmitColumnAFYPWithFamily =0;
                   int startSubmitColumnTotal =0;

                   int startSuccessColumnAPPSWithFamilyAppPerFamily =0;
                   int startSuccessColumnAPPSWithFamilyAppPerPerson =0;
                   int startSuccessColumnAFYPWithFamily =0;
                   int startSuccessColumnTotal =0;

                   int startListConversion =0;
                   int startConversionRate = 0;
                   int startTotalConversionRate = 0;

                   int totalAFYPSuccess = 0;
                   int totalAFYPSubmit = 0;

                   int startAPPSRejectNoFamily = 0;
                   int startNetPremiumRejectNoFamily = 0;

                   int startAPPSRejectWithFamily = 0;
                   int startNetPremiumRejectWithFamily = 0;

                   LinkedHashMap<String,Double> conversionRateOfProducts=new LinkedHashMap<String,Double>();
                   LinkedHashMap<String,Integer> successAppsOfProducts =new LinkedHashMap<String,Integer>();

                   int i =1;
                   while( i<= colCount) {
                   // part of 1
                       if (i == 1) {
                           // set Date
                           sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_STRING);
                           sheet.getRow(rowIndx).getCell(colIndx).setCellValue(rs.getString(i));
                           sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                           CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                           colIndx++;
                       }
                       if (i == 2) {
                           //set New
                           sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_STRING);
                           sheet.getRow(rowIndx).getCell(colIndx).setCellValue(rs.getInt(i));
                           sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                           CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                           colIndx++;
                       }
                       if (i == 3) {
                           //set follow
                           sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_STRING);
                           sheet.getRow(rowIndx).getCell(colIndx).setCellValue(rs.getInt(i));
                           sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                           CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                           colIndx++;
                       }
                       if (i == 4) {
                           //set total follow
                               rs.getInt(i);

                       }
                       if (i == 5) {
                           //set total
                           sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                           sheet.getRow(rowIndx).getCell(colIndx).setCellValue(rs.getInt(i));
                           sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                           CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                           colIndx++;
                       }
                       if (i == 6) {
                           //set Call Attempt No.
                           sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                           sheet.getRow(rowIndx).getCell(colIndx).setCellValue(rs.getInt(i));
                           sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                           CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                           colIndx++;
                       }
                       if (i == 7) {
                           //set Call Attempt Rate
                           sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                           sheet.getRow(rowIndx).getCell(colIndx).setCellValue(rs.getDouble(i));
                           sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                           CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                           colIndx++;
                       }
                       if (i == 8) {
                           //set Accumulate Total List Used
                           sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                           sheet.getRow(rowIndx).getCell(colIndx).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(3), startRowIndx + 1, CellReference.convertNumToColString(3), rowIndx + 1));
                           sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                           CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                           colIndx++;
                       }
                       if (i == 9) {
                           //set Daily Contactable
                           sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                           sheet.getRow(rowIndx).getCell(colIndx).setCellValue(rs.getInt(i));
                           sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                           CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                           colIndx++;
                       }
                       if (i == 10) {
                           //set Accumulate Daily Contactable
                           sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                           sheet.getRow(rowIndx).getCell(colIndx).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(7), startRowIndx + 1, CellReference.convertNumToColString(7), rowIndx + 1));
                           sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                           CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                           colIndx++;
                       }
                       if (i == 11) {
                           //set Daily Contactable %
                           sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                           sheet.getRow(rowIndx).getCell(colIndx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(3), rowIndx + 1, CellReference.convertNumToColString(7), rowIndx + 1, CellReference.convertNumToColString(3), rowIndx + 1));
                           sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                           CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                           colIndx++;
                       }
                       if (i == 12) {
                           //Accumulate Daily Contactable %
                           sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                           sheet.getRow(rowIndx).getCell(colIndx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(6), rowIndx + 1,  CellReference.convertNumToColString(8), rowIndx + 1, CellReference.convertNumToColString(6), rowIndx + 1));
                           sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                           CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                           colIndx++;
                       }
                       if (i == 13) {
                           //set Apps. Response
                           sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                           sheet.getRow(rowIndx).getCell(colIndx).setCellValue(rs.getDouble(i));
                           sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                           colIndx++;
                       }
                       if(i == 14){
                           //set Sales Close Rate %
                           sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                           sheet.getRow(rowIndx).getCell(colIndx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(7), rowIndx + 1, CellReference.convertNumToColString(11), rowIndx + 1, CellReference.convertNumToColString(7), rowIndx + 1));
                           sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                           CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                           lastRow = rowIndx;
                           lastCols = colIndx;
                       }

                       // part of 2.1
                       if(isSubmit) {

                           if (rs.getMetaData().getColumnName(i).equals("Submit Apps no family")) {
                               i += 2;
                               if (startColSubmitAppsIndx == 0) {
                                   startColSubmitAppsIndx = lastCols + 1;
                               }
                               startSubmitColumnAPPS = lastCols + 1;
                               startSubmitColumnAFYP = lastCols + 2;
                               isSubmitAppsNoFamily = true;

                           } else if (rs.getMetaData().getColumnName(i).equals("Submit AFYP no family")) {
                               i += 2;
                               isSubmitAppsNoFamily = false;
                               isSubmitAFYPNoFamily = true;
                           } else if (rs.getMetaData().getColumnName(i).equals("Submit Apps/family with family")) {
                               i += 2;
                               if (startColSubmitAppsIndx == 0) {
                                   startColSubmitAppsIndx = lastCols + 1;
                               }
                               startSubmitColumnAPPSWithFamilyAppPerFamily = lastCols + 1;
                               startSubmitColumnAPPSWithFamilyAppPerPerson = lastCols + 2;
                               startSubmitColumnAFYPWithFamily = lastCols + 3;
                               isSubmitAFYPNoFamily = false;
                               isSubmitAppsWithFamilyAppPerFamily = true;
                           } else if (rs.getMetaData().getColumnName(i).equals("Submit Apps/person with family")) {
                               i += 2;
                               isSubmitAppsWithFamilyAppPerFamily = false;
                               isSubmitAppsWithFamilyAppPerson = true;

                           } else if (rs.getMetaData().getColumnName(i).equals("Submit AFYP with family")) {
                               i += 2;
                               isSubmitAppsWithFamilyAppPerson = false;
                               isSubmitAFYPWithFamily = true;

                           } else if (rs.getMetaData().getColumnName(i).equals("Total Submit")) {
                               // merge header submit
                               sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startColSubmitAppsIndx), rownum + 1, CellReference.convertNumToColString(lastCols), rownum + 1)));
                               i += 1;
                               isSubmitAFYPWithFamily = false;
                               isTotalSubMit = true;
                               isSubmit = false;
                               isSuccess = true;
                               startSubmitColumnTotal = lastCols + 1;
                           }
                           //set Submit No Family Product
                           if (isSubmitAppsNoFamily) {
                               // set header Apps
                               hrow2.createCell(startSubmitColumnAPPS).setCellValue("");
                               hrow2.getCell(startSubmitColumnAPPS).setCellStyle(headCellStyle);
                               hrow3.createCell(startSubmitColumnAPPS).setCellValue("APP");
                               hrow3.getCell(startSubmitColumnAPPS).setCellStyle(headCellStyle);

                               // set Apps
                               sheet.getRow(rowIndx).createCell(startSubmitColumnAPPS).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAPPS).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAPPS).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSubmitColumnAPPS), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               startSubmitColumnAPPS += 3;
                           }
                           if (isSubmitAFYPNoFamily) {
                               //set Group Header Product
                               hrow1.createCell(startColSubmitAppsIndx).setCellValue("Submit");
                               hrow1.getCell(startColSubmitAppsIndx).setCellStyle(headCellStyle);

                               //set header product name
                               hrow1.createCell(startSubmitColumnAFYP - 1).setCellValue("");
                               hrow1.getCell(startSubmitColumnAFYP - 1).setCellStyle(headCellStyle);
                               hrow2.createCell(startSubmitColumnAFYP - 1).setCellValue(rs.getMetaData().getColumnName(i));
                               hrow2.getCell(startSubmitColumnAFYP - 1).setCellStyle(headCellStyle);
                               sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startSubmitColumnAFYP - 1), rownum + 2, CellReference.convertNumToColString(startSubmitColumnAFYP + 1), rownum + 2)));

                               // set header AFYP
                               hrow1.createCell(startSubmitColumnAFYP).setCellValue("");
                               hrow1.getCell(startSubmitColumnAFYP).setCellStyle(headCellStyle);
                               hrow2.createCell(startSubmitColumnAFYP).setCellValue(rs.getMetaData().getColumnName(i));
                               hrow2.getCell(startSubmitColumnAFYP).setCellStyle(headCellStyle);
                               hrow3.createCell(startSubmitColumnAFYP).setCellValue("AFYP");
                               hrow3.getCell(startSubmitColumnAFYP).setCellStyle(headCellStyle);

                               //set AFYP

                               int total = 0;
                               if(rowIndx==12) totalSubmitAFYPEachOfProduct.put(rs.getMetaData().getColumnName(i),rs.getInt(i));

                               sheet.getRow(rowIndx).createCell(startSubmitColumnAFYP).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAFYP).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAFYP).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSubmitColumnAFYP), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               conversionRateOfProducts.put(rs.getMetaData().getColumnName(i),(double) rs.getInt(i));
                               if(rowIndx>12) {
                                   int oldDataSubmit = totalSubmitAFYPEachOfProduct.get(rs.getMetaData().getColumnName(i));
                                   total = oldDataSubmit + rs.getInt(i);
                                   totalSubmitAFYPEachOfProduct.put(rs.getMetaData().getColumnName(i), total);
                               }

                               // set header AARP
                               hrow1.createCell(startSubmitColumnAFYP + 1).setCellValue("");
                               hrow1.getCell(startSubmitColumnAFYP + 1).setCellStyle(headCellStyle);
                               hrow2.createCell(startSubmitColumnAFYP + 1).setCellValue("");
                               hrow2.getCell(startSubmitColumnAFYP + 1).setCellStyle(headCellStyle);
                               hrow3.createCell(startSubmitColumnAFYP + 1).setCellValue("AARP");
                               hrow3.getCell(startSubmitColumnAFYP + 1).setCellStyle(headCellStyle);

                               //set AARP = AFYP/apps
                               sheet.getRow(rowIndx).createCell(startSubmitColumnAFYP + 1).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAFYP + 1).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startSubmitColumnAFYP - 1), lastRow + 1, CellReference.convertNumToColString(startSubmitColumnAFYP), lastRow + 1, CellReference.convertNumToColString(startSubmitColumnAFYP - 1), lastRow + 1));
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAFYP + 1).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSubmitColumnAFYP + 1), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                               lastCols = startSubmitColumnAFYP + 1;
                               startSubmitColumnAFYP += 3;
                           }
                           if (isSubmitAppsWithFamilyAppPerFamily) {
                               // set header Apps/Family
                               hrow2.createCell(startSubmitColumnAPPSWithFamilyAppPerFamily).setCellValue("");
                               hrow2.getCell(startSubmitColumnAPPSWithFamilyAppPerFamily).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startSubmitColumnAPPSWithFamilyAppPerFamily).setCellValue("APP/Family");
                               hrow3.getCell(startSubmitColumnAPPSWithFamilyAppPerFamily).setCellStyle(headCellStyle);

                               // set Apps/Family
                               sheet.getRow(rowIndx).createCell(startSubmitColumnAPPSWithFamilyAppPerFamily).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAPPSWithFamilyAppPerFamily).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAPPSWithFamilyAppPerFamily).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSubmitColumnAPPSWithFamilyAppPerFamily), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               startSubmitColumnAPPSWithFamilyAppPerFamily += 5;
                           }
                           if (isSubmitAppsWithFamilyAppPerson) {
                               // set header APP/Person
                               hrow1.createCell(startSubmitColumnAPPSWithFamilyAppPerPerson).setCellValue("");
                               hrow1.getCell(startSubmitColumnAPPSWithFamilyAppPerPerson).setCellStyle(headCellStyle);
                               hrow2.createCell(startSubmitColumnAPPSWithFamilyAppPerPerson).setCellValue("");
                               hrow2.getCell(startSubmitColumnAPPSWithFamilyAppPerPerson).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startSubmitColumnAPPSWithFamilyAppPerPerson).setCellValue("APP/Person");
                               hrow3.getCell(startSubmitColumnAPPSWithFamilyAppPerPerson).setCellStyle(headCellAltStyle);

                               // set Apps/Person
                               sheet.getRow(rowIndx).createCell(startSubmitColumnAPPSWithFamilyAppPerPerson).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAPPSWithFamilyAppPerPerson).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAPPSWithFamilyAppPerPerson).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSubmitColumnAPPSWithFamilyAppPerPerson), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               startSubmitColumnAPPSWithFamilyAppPerPerson += 5;
                           }
                           if (isSubmitAFYPWithFamily) {
                               //set Group Header Product
                               hrow1.createCell(startColSubmitAppsIndx).setCellValue("Submit");
                               hrow1.getCell(startColSubmitAppsIndx).setCellStyle(headCellStyle);

                               //set header product name
                               hrow1.createCell(startSubmitColumnAFYPWithFamily-2).setCellValue("");
                               hrow1.getCell(startSubmitColumnAFYPWithFamily-2).setCellStyle(headCellStyle);
                               hrow2.createCell(startSubmitColumnAFYPWithFamily - 2).setCellValue(rs.getMetaData().getColumnName(i));
                               hrow2.getCell(startSubmitColumnAFYPWithFamily - 2).setCellStyle(headCellAltStyle);
                               sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startSubmitColumnAFYPWithFamily - 2), rownum + 2, CellReference.convertNumToColString(startSubmitColumnAFYPWithFamily + 2), rownum + 2)));

                               // set header AFYP with family
                               hrow1.createCell(startSubmitColumnAFYPWithFamily).setCellValue("");
                               hrow1.getCell(startSubmitColumnAFYPWithFamily).setCellStyle(headCellStyle);
                               hrow2.createCell(startSubmitColumnAFYPWithFamily).setCellValue(rs.getMetaData().getColumnName(i));
                               hrow2.getCell(startSubmitColumnAFYPWithFamily).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startSubmitColumnAFYPWithFamily).setCellValue("AFYP");
                               hrow3.getCell(startSubmitColumnAFYPWithFamily).setCellStyle(headCellStyle);

                               //set AFYP with family

                               int total = 0;
                               if(rowIndx==12) totalSubmitAFYPEachOfProduct.put(rs.getMetaData().getColumnName(i),rs.getInt(i));

                               sheet.getRow(rowIndx).createCell(startSubmitColumnAFYPWithFamily).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAFYPWithFamily).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAFYPWithFamily).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSubmitColumnAFYPWithFamily), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               conversionRateOfProducts.put(rs.getMetaData().getColumnName(i), (double) rs.getInt(i));
                               if(rowIndx>12) {
                                   int oldDataSubmit = totalSubmitAFYPEachOfProduct.get(rs.getMetaData().getColumnName(i));
                                   total = oldDataSubmit + rs.getInt(i);
                                   totalSubmitAFYPEachOfProduct.put(rs.getMetaData().getColumnName(i), total);
                               }

                               // set header AARP/Family
                               hrow1.createCell(startSubmitColumnAFYPWithFamily+1).setCellValue("");
                               hrow1.getCell(startSubmitColumnAFYPWithFamily+1).setCellStyle(headCellStyle);
                               hrow2.createCell(startSubmitColumnAFYPWithFamily + 1).setCellValue("");
                               hrow2.getCell(startSubmitColumnAFYPWithFamily + 1).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startSubmitColumnAFYPWithFamily + 1).setCellValue("AARP/Family");
                               hrow3.getCell(startSubmitColumnAFYPWithFamily + 1).setCellStyle(headCellStyle);

                               //set AARP|Family = AFYP/APP|Family
                               sheet.getRow(rowIndx).createCell(startSubmitColumnAFYPWithFamily + 1).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAFYPWithFamily + 1).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startSubmitColumnAFYPWithFamily - 2), lastRow + 1, CellReference.convertNumToColString(startSubmitColumnAFYPWithFamily), lastRow + 1, CellReference.convertNumToColString(startSubmitColumnAFYPWithFamily - 2), lastRow + 1));
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAFYPWithFamily + 1).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSubmitColumnAFYPWithFamily + 1), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                               // set header AARP/Person
                               hrow1.createCell(startSubmitColumnAFYPWithFamily+2).setCellValue("");
                               hrow1.getCell(startSubmitColumnAFYPWithFamily+2).setCellStyle(headCellStyle);
                               hrow2.createCell(startSubmitColumnAFYPWithFamily + 2).setCellValue("");
                               hrow2.getCell(startSubmitColumnAFYPWithFamily + 2).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startSubmitColumnAFYPWithFamily + 2).setCellValue("AARP/Person");
                               hrow3.getCell(startSubmitColumnAFYPWithFamily + 2).setCellStyle(headCellAltStyle);

                               //set AARP|Person = AFYP/APP|Person
                               sheet.getRow(rowIndx).createCell(startSubmitColumnAFYPWithFamily + 2).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAFYPWithFamily + 2).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startSubmitColumnAFYPWithFamily - 1), lastRow + 1, CellReference.convertNumToColString(startSubmitColumnAFYPWithFamily), lastRow + 1, CellReference.convertNumToColString(startSubmitColumnAFYPWithFamily - 1), lastRow + 1));
                               sheet.getRow(rowIndx).getCell(startSubmitColumnAFYPWithFamily + 2).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSubmitColumnAFYPWithFamily + 2), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                               lastCols = startSubmitColumnAFYPWithFamily + 2;
                               startSubmitColumnAFYPWithFamily += 5;
                           }

                           if (isTotalSubMit) {
                               // set header Apps
                               hrow1.createCell(startSubmitColumnTotal).setCellValue("Total");
                               hrow1.getCell(startSubmitColumnTotal).setCellStyle(headCellStyle);
                               hrow2.createCell(startSubmitColumnTotal).setCellValue("");
                               hrow2.getCell(startSubmitColumnTotal).setCellStyle(headCellStyle);
                               hrow3.createCell(startSubmitColumnTotal).setCellValue("APP");
                               hrow3.getCell(startSubmitColumnTotal).setCellStyle(headCellStyle);

                               // set total Apps
                               sheet.getRow(rowIndx).createCell(startSubmitColumnTotal).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSubmitColumnTotal).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startSubmitColumnTotal).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSubmitColumnTotal), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               i++;

                               // set header AFYP
                               hrow1.createCell(startSubmitColumnTotal + 1).setCellValue("");
                               hrow1.getCell(startSubmitColumnTotal + 1).setCellStyle(headCellStyle);
                               hrow2.createCell(startSubmitColumnTotal + 1).setCellValue("");
                               hrow2.getCell(startSubmitColumnTotal + 1).setCellStyle(headCellStyle);
                               hrow3.createCell(startSubmitColumnTotal + 1).setCellValue("AFYP");
                               hrow3.getCell(startSubmitColumnTotal + 1).setCellStyle(headCellStyle);

                               // set total AFYP
                               sheet.getRow(rowIndx).createCell(startSubmitColumnTotal + 1).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSubmitColumnTotal + 1).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startSubmitColumnTotal + 1).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSubmitColumnTotal + 1), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               totalAFYPSubmit = rs.getInt(i);

                               // set header AARP
                               hrow1.createCell(startSubmitColumnTotal + 2).setCellValue("");
                               hrow1.getCell(startSubmitColumnTotal + 2).setCellStyle(headCellStyle);
                               hrow2.createCell(startSubmitColumnTotal + 2).setCellValue("");
                               hrow2.getCell(startSubmitColumnTotal + 2).setCellStyle(headCellStyle);
                               hrow3.createCell(startSubmitColumnTotal + 2).setCellValue("AARP");
                               hrow3.getCell(startSubmitColumnTotal + 2).setCellStyle(headCellStyle);

                               // set total AARP = Apps/AFYP
                               sheet.getRow(rowIndx).createCell(startSubmitColumnTotal + 2).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSubmitColumnTotal + 2).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startSubmitColumnTotal), lastRow + 1, CellReference.convertNumToColString(startSubmitColumnTotal + 1), lastRow + 1, CellReference.convertNumToColString(startSubmitColumnTotal), lastRow + 1));
                               sheet.getRow(rowIndx).getCell(startSubmitColumnTotal + 2).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSubmitColumnTotal + 2), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                               sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startSubmitColumnTotal), rownum + 1, CellReference.convertNumToColString(startSubmitColumnTotal + 2), rownum + 2)));
                               lastCols = startSubmitColumnTotal + 2;
                               isTotalSubMit = false;
                           }
                       }

                       // part of 2.2
                       if(isSuccess) {
                           if (rs.getMetaData().getColumnName(i).equals("Success Apps no family")) {
                               i += 2;
                               if (startColSuccessAppsIndx == 0) {
                                   startColSuccessAppsIndx = lastCols + 1;
                               }
                               startSuccessColumnAPPS = lastCols + 1;
                               startSuccessColumnAFYP = lastCols + 2;
                               isSuccessAppsNoFamily = true;

                           } else if (rs.getMetaData().getColumnName(i).equals("Success AFYP no family")) {
                               i += 2;
                               isSuccessAppsNoFamily = false;
                               isSuccessAFYPNoFamily = true;
                           } else if (rs.getMetaData().getColumnName(i).equals("Success Apps/family with family")) {
                               i += 2;
                               if (startColSuccessAppsIndx == 0) {
                                   startColSuccessAppsIndx = lastCols + 1;
                               }
                               startSuccessColumnAPPSWithFamilyAppPerFamily = lastCols + 1;
                               startSuccessColumnAPPSWithFamilyAppPerPerson = lastCols + 2;
                               startSuccessColumnAFYPWithFamily = lastCols + 3;
                               isSuccessAFYPNoFamily = false;
                               isSuccessAppsWithFamilyAppPerFamily = true;
                           } else if (rs.getMetaData().getColumnName(i).equals("Success Apps/person with family")) {
                               i += 2;
                               isSuccessAppsWithFamilyAppPerFamily = false;
                               isSuccessAppsWithFamilyAppPerson = true;

                           } else if (rs.getMetaData().getColumnName(i).equals("Success AFYP with family")) {
                               i += 2;
                               isSuccessAppsWithFamilyAppPerson = false;
                               isSuccessAFYPWithFamily = true;

                           } else if (rs.getMetaData().getColumnName(i).equals("Total Success")) {
                               // merge header success
                               sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startColSuccessAppsIndx), rownum + 1, CellReference.convertNumToColString(lastCols), rownum + 1)));
                               i += 1;
                               isSuccessAFYPWithFamily = false;
                               isSuccess = false;
                               isTotalSuccess = true;
                               isListConversion = true;
                               startSuccessColumnTotal = lastCols + 1;
                           }

                           if (isSuccessAppsNoFamily) {
                               // set header Apps
                               hrow2.createCell(startSuccessColumnAPPS).setCellValue("");
                               hrow2.getCell(startSuccessColumnAPPS).setCellStyle(headCellStyle);
                               hrow3.createCell(startSuccessColumnAPPS).setCellValue("APP");
                               hrow3.getCell(startSuccessColumnAPPS).setCellStyle(headCellStyle);

                               // set Apps
                               int total = 0;
                               if(rowIndx==12) totalSuccessAPPEachOfProduct.put(rs.getMetaData().getColumnName(i),rs.getInt(i));

                               sheet.getRow(rowIndx).createCell(startSuccessColumnAPPS).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAPPS).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAPPS).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSuccessColumnAPPS), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               startSuccessColumnAPPS += 3;

                               if(rowIndx>12) {
                                   int oldDataSuccess = totalSuccessAPPEachOfProduct.get(rs.getMetaData().getColumnName(i));
                                   total = oldDataSuccess + rs.getInt(i);
                                   totalSuccessAPPEachOfProduct.put(rs.getMetaData().getColumnName(i), total);
                               }

                               successAppsOfProducts.put(rs.getMetaData().getColumnName(i),rs.getInt(i));
                           }

                           if (isSuccessAFYPNoFamily) {
                               //set Group Header Product
                               hrow1.createCell(startColSuccessAppsIndx).setCellValue("Success");
                               hrow1.getCell(startColSuccessAppsIndx).setCellStyle(headCellStyle);

                               //set header product name
                               hrow1.createCell(startSuccessColumnAFYP - 1).setCellValue("");
                               hrow1.getCell(startSuccessColumnAFYP - 1).setCellStyle(headCellStyle);
                               hrow2.createCell(startSuccessColumnAFYP - 1).setCellValue(rs.getMetaData().getColumnName(i));
                               hrow2.getCell(startSuccessColumnAFYP - 1).setCellStyle(headCellStyle);
                               sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startSuccessColumnAFYP - 1), rownum + 2, CellReference.convertNumToColString(startSuccessColumnAFYP + 1), rownum + 2)));

                               // set header AFYP
                               hrow1.createCell(startSuccessColumnAFYP).setCellValue("");
                               hrow1.getCell(startSuccessColumnAFYP).setCellStyle(headCellStyle);
                               hrow2.createCell(startSuccessColumnAFYP).setCellValue(rs.getMetaData().getColumnName(i));
                               hrow2.getCell(startSuccessColumnAFYP).setCellStyle(headCellStyle);
                               hrow3.createCell(startSuccessColumnAFYP).setCellValue("AFYP");
                               hrow3.getCell(startSuccessColumnAFYP).setCellStyle(headCellStyle);

                               //set AFYP
                               int total = 0;
                               if(rowIndx==12) totalSuccessAFYPEachOfProduct.put(rs.getMetaData().getColumnName(i),rs.getInt(i));

                               sheet.getRow(rowIndx).createCell(startSuccessColumnAFYP).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAFYP).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAFYP).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSuccessColumnAFYP), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               Double submitAFYP = conversionRateOfProducts.get(rs.getMetaData().getColumnName(i));
                               Double conversionRate = 0.00;
                               if (submitAFYP > 0) conversionRate = rs.getInt(i) / submitAFYP;
                               conversionRateOfProducts.put(rs.getMetaData().getColumnName(i), conversionRate);

                               if(rowIndx>12) {
                                   int oldDataSuccess = totalSuccessAFYPEachOfProduct.get(rs.getMetaData().getColumnName(i));
                                   total = oldDataSuccess + rs.getInt(i);
                                   totalSuccessAFYPEachOfProduct.put(rs.getMetaData().getColumnName(i), total);
                               }

                               // set header AARP
                               hrow1.createCell(startSuccessColumnAFYP + 1).setCellValue("");
                               hrow1.getCell(startSuccessColumnAFYP + 1).setCellStyle(headCellStyle);
                               hrow2.createCell(startSuccessColumnAFYP + 1).setCellValue("");
                               hrow2.getCell(startSuccessColumnAFYP + 1).setCellStyle(headCellStyle);
                               hrow3.createCell(startSuccessColumnAFYP + 1).setCellValue("AARP");
                               hrow3.getCell(startSuccessColumnAFYP + 1).setCellStyle(headCellStyle);

                               //set AARP = AFYP/apps
                               sheet.getRow(rowIndx).createCell(startSuccessColumnAFYP + 1).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAFYP + 1).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startSuccessColumnAFYP - 1), lastRow + 1, CellReference.convertNumToColString(startSuccessColumnAFYP), lastRow + 1, CellReference.convertNumToColString(startSuccessColumnAFYP - 1), lastRow + 1));
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAFYP + 1).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSuccessColumnAFYP + 1), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                               lastCols = startSuccessColumnAFYP + 1;
                               startSuccessColumnAFYP += 3;
                           }

                           if (isSuccessAppsWithFamilyAppPerFamily) {
                               // set header Apps/Family
                               hrow1.createCell(startSuccessColumnAPPSWithFamilyAppPerFamily).setCellValue("");
                               hrow1.getCell(startSuccessColumnAPPSWithFamilyAppPerFamily).setCellStyle(headCellStyle);
                               hrow2.createCell(startSuccessColumnAPPSWithFamilyAppPerFamily).setCellValue("");
                               hrow2.getCell(startSuccessColumnAPPSWithFamilyAppPerFamily).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startSuccessColumnAPPSWithFamilyAppPerFamily).setCellValue("APP/Family");
                               hrow3.getCell(startSuccessColumnAPPSWithFamilyAppPerFamily).setCellStyle(headCellStyle);

                               // set Apps/Family
                               sheet.getRow(rowIndx).createCell(startSuccessColumnAPPSWithFamilyAppPerFamily).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAPPSWithFamilyAppPerFamily).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAPPSWithFamilyAppPerFamily).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSuccessColumnAPPSWithFamilyAppPerFamily), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               startSuccessColumnAPPSWithFamilyAppPerFamily += 5;
                           }

                           if (isSuccessAppsWithFamilyAppPerson) {
                               // set header APP/Person
                               hrow1.createCell(startSuccessColumnAPPSWithFamilyAppPerPerson).setCellValue("");
                               hrow1.getCell(startSuccessColumnAPPSWithFamilyAppPerPerson).setCellStyle(headCellStyle);
                               hrow2.createCell(startSuccessColumnAPPSWithFamilyAppPerPerson).setCellValue("");
                               hrow2.getCell(startSuccessColumnAPPSWithFamilyAppPerPerson).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startSuccessColumnAPPSWithFamilyAppPerPerson).setCellValue("APP/Person");
                               hrow3.getCell(startSuccessColumnAPPSWithFamilyAppPerPerson).setCellStyle(headCellAltStyle);

                               // set Apps/Person

                               int total = 0;
                               if(rowIndx==12) totalSuccessAPPEachOfProduct.put(rs.getMetaData().getColumnName(i),rs.getInt(i));

                               sheet.getRow(rowIndx).createCell(startSuccessColumnAPPSWithFamilyAppPerPerson).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAPPSWithFamilyAppPerPerson).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAPPSWithFamilyAppPerPerson).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSuccessColumnAPPSWithFamilyAppPerPerson), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               startSuccessColumnAPPSWithFamilyAppPerPerson += 5;

                               if(rowIndx>12) {
                                   int oldDataSuccess = totalSuccessAPPEachOfProduct.get(rs.getMetaData().getColumnName(i));
                                   total = oldDataSuccess + rs.getInt(i);
                                   totalSuccessAPPEachOfProduct.put(rs.getMetaData().getColumnName(i), total);
                               }

                               successAppsOfProducts.put(rs.getMetaData().getColumnName(i),rs.getInt(i));
                           }

                           if (isSuccessAFYPWithFamily) {
                               //set Group Header Product
                               hrow1.createCell(startColSuccessAppsIndx).setCellValue("Success");
                               hrow1.getCell(startColSuccessAppsIndx).setCellStyle(headCellStyle);

                               //set header product name
                               hrow1.createCell(startSuccessColumnAFYPWithFamily - 2).setCellValue("");
                               hrow1.getCell(startSuccessColumnAFYPWithFamily - 2).setCellStyle(headCellStyle);
                               hrow2.createCell(startSuccessColumnAFYPWithFamily - 2).setCellValue(rs.getMetaData().getColumnName(i));
                               hrow2.getCell(startSuccessColumnAFYPWithFamily - 2).setCellStyle(headCellAltStyle);
                               sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startSuccessColumnAFYPWithFamily - 2), rownum + 2, CellReference.convertNumToColString(startSuccessColumnAFYPWithFamily + 2), rownum + 2)));

                               // set header AFYP with family
                               hrow1.createCell(startSuccessColumnAFYPWithFamily).setCellValue("");
                               hrow1.getCell(startSuccessColumnAFYPWithFamily).setCellStyle(headCellStyle);
                               hrow2.createCell(startSuccessColumnAFYPWithFamily).setCellValue(rs.getMetaData().getColumnName(i));
                               hrow2.getCell(startSuccessColumnAFYPWithFamily).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startSuccessColumnAFYPWithFamily).setCellValue("AFYP");
                               hrow3.getCell(startSuccessColumnAFYPWithFamily).setCellStyle(headCellStyle);


                               //set AFYP with family

                               int total = 0;
                               if(rowIndx==12) totalSuccessAFYPEachOfProduct.put(rs.getMetaData().getColumnName(i),rs.getInt(i));

                               sheet.getRow(rowIndx).createCell(startSuccessColumnAFYPWithFamily).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAFYPWithFamily).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAFYPWithFamily).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSuccessColumnAFYPWithFamily), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               Double submitAFYP = conversionRateOfProducts.get(rs.getMetaData().getColumnName(i));
                               Double conversionRate = 0.00;
                               if (submitAFYP > 0) conversionRate = rs.getInt(i) / submitAFYP;
                               conversionRateOfProducts.put(rs.getMetaData().getColumnName(i), conversionRate);

                               if(rowIndx>12) {
                                   int oldDataSuccess = totalSuccessAFYPEachOfProduct.get(rs.getMetaData().getColumnName(i));
                                   total = oldDataSuccess + rs.getInt(i);
                                   totalSuccessAFYPEachOfProduct.put(rs.getMetaData().getColumnName(i), total);
                               }

                               // set header AARP/Family
                               hrow1.createCell(startSuccessColumnAFYPWithFamily + 1).setCellValue("");
                               hrow1.getCell(startSuccessColumnAFYPWithFamily + 1).setCellStyle(headCellStyle);
                               hrow2.createCell(startSuccessColumnAFYPWithFamily + 1).setCellValue("");
                               hrow2.getCell(startSuccessColumnAFYPWithFamily + 1).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startSuccessColumnAFYPWithFamily + 1).setCellValue("AARP/Family");
                               hrow3.getCell(startSuccessColumnAFYPWithFamily + 1).setCellStyle(headCellStyle);

                               //set AARP|Family = AFYP/APP|Family
                               sheet.getRow(rowIndx).createCell(startSuccessColumnAFYPWithFamily + 1).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAFYPWithFamily + 1).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startSuccessColumnAFYPWithFamily - 2), lastRow + 1, CellReference.convertNumToColString(startSuccessColumnAFYPWithFamily), lastRow + 1, CellReference.convertNumToColString(startSuccessColumnAFYPWithFamily - 2), lastRow + 1));
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAFYPWithFamily + 1).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSuccessColumnAFYPWithFamily + 1), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                               // set header AARP/Person
                               hrow1.createCell(startSuccessColumnAFYPWithFamily + 2).setCellValue("");
                               hrow1.getCell(startSuccessColumnAFYPWithFamily + 2).setCellStyle(headCellStyle);
                               hrow2.createCell(startSuccessColumnAFYPWithFamily + 2).setCellValue("");
                               hrow2.getCell(startSuccessColumnAFYPWithFamily + 2).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startSuccessColumnAFYPWithFamily + 2).setCellValue("AARP/Person");
                               hrow3.getCell(startSuccessColumnAFYPWithFamily + 2).setCellStyle(headCellAltStyle);

                               //set AARP|Person = AFYP/APP|Person
                               sheet.getRow(rowIndx).createCell(startSuccessColumnAFYPWithFamily + 2).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAFYPWithFamily + 2).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startSuccessColumnAFYPWithFamily - 1), lastRow + 1, CellReference.convertNumToColString(startSuccessColumnAFYPWithFamily), lastRow + 1, CellReference.convertNumToColString(startSuccessColumnAFYPWithFamily - 1), lastRow + 1));
                               sheet.getRow(rowIndx).getCell(startSuccessColumnAFYPWithFamily + 2).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSuccessColumnAFYPWithFamily + 2), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                               lastCols = startSuccessColumnAFYPWithFamily + 2;
                               startSuccessColumnAFYPWithFamily += 5;
                           }
//
                           if (isTotalSuccess) {
                               // set header Apps
                               hrow1.createCell(startSuccessColumnTotal).setCellValue("Total");
                               hrow1.getCell(startSuccessColumnTotal).setCellStyle(headCellStyle);
                               hrow2.createCell(startSuccessColumnTotal).setCellValue("");
                               hrow2.getCell(startSuccessColumnTotal).setCellStyle(headCellStyle);
                               hrow3.createCell(startSuccessColumnTotal).setCellValue("APP");
                               hrow3.getCell(startSuccessColumnTotal).setCellStyle(headCellStyle);

                               // set total Apps
                               sheet.getRow(rowIndx).createCell(startSuccessColumnTotal).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSuccessColumnTotal).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startSuccessColumnTotal).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSuccessColumnTotal), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               i++;

                               // set header AFYP
                               hrow1.createCell(startSuccessColumnTotal + 1).setCellValue("");
                               hrow1.getCell(startSuccessColumnTotal + 1).setCellStyle(headCellStyle);
                               hrow2.createCell(startSuccessColumnTotal + 1).setCellValue("");
                               hrow2.getCell(startSuccessColumnTotal + 1).setCellStyle(headCellStyle);
                               hrow3.createCell(startSuccessColumnTotal + 1).setCellValue("AFYP");
                               hrow3.getCell(startSuccessColumnTotal + 1).setCellStyle(headCellStyle);

                               // set total AFYP
                               sheet.getRow(rowIndx).createCell(startSuccessColumnTotal + 1).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSuccessColumnTotal + 1).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startSuccessColumnTotal + 1).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSuccessColumnTotal + 1), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               totalAFYPSuccess = rs.getInt(i);
                               i++;

                               // set header AARP
                               hrow1.createCell(startSuccessColumnTotal + 2).setCellValue("");
                               hrow1.getCell(startSuccessColumnTotal + 2).setCellStyle(headCellStyle);
                               hrow2.createCell(startSuccessColumnTotal + 2).setCellValue("");
                               hrow2.getCell(startSuccessColumnTotal + 2).setCellStyle(headCellStyle);
                               hrow3.createCell(startSuccessColumnTotal + 2).setCellValue("AARP");
                               hrow3.getCell(startSuccessColumnTotal + 2).setCellStyle(headCellStyle);

                               // set total AARP = Apps/AFYP
                               sheet.getRow(rowIndx).createCell(startSuccessColumnTotal + 2).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startSuccessColumnTotal + 2).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startSuccessColumnTotal), lastRow + 1, CellReference.convertNumToColString(startSuccessColumnTotal + 1), lastRow + 1, CellReference.convertNumToColString(startSuccessColumnTotal), lastRow + 1));
                               sheet.getRow(rowIndx).getCell(startSuccessColumnTotal + 2).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startSuccessColumnTotal + 2), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                               sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startSuccessColumnTotal), rownum + 1, CellReference.convertNumToColString(startSuccessColumnTotal + 2), rownum + 2)));
                               lastCols = startSuccessColumnTotal + 2;
                               isTotalSuccess = false;
                           }
                       }

                       if(isListConversion){
                           startListConversion = lastCols +1;
                           // set Head List Conversion
                           hrow1.createCell(startListConversion).setCellValue("List Conversion %");
                           hrow1.getCell(startListConversion).setCellStyle(headCellStyle);
                           hrow2.createCell(startListConversion).setCellValue("");
                           hrow2.getCell(startListConversion).setCellStyle(headCellStyle);
                           hrow3.createCell(startListConversion).setCellValue("");
                           hrow3.getCell(startListConversion).setCellStyle(headCellStyle);
                           sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startListConversion), rownum + 1, CellReference.convertNumToColString(startListConversion), rownum + 3)));

                           // set List Conversion
                           sheet.getRow(rowIndx).createCell(startListConversion).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                           sheet.getRow(rowIndx).getCell(startListConversion).setCellValue(rs.getDouble(i));
                           sheet.getRow(rowIndx).getCell(startListConversion).setCellStyle(numberCellStyle);
                           CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startListConversion), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                           i++;
                           lastCols = startListConversion;
                           isListConversion = false;
                           isConversionRate = true;
                       }

                       if(isConversionRate){
                           startConversionRate = lastCols +1;
                           int mergeHeaderStart = lastCols +1;
                           for(Map.Entry m:conversionRateOfProducts.entrySet()){

                               // set Header of products
                               hrow1.createCell(startConversionRate).setCellValue("Conversion Rate %");
                               hrow1.getCell(startConversionRate).setCellStyle(headCellStyle);
                               hrow2.createCell(startConversionRate).setCellValue(m.getKey()+"");
                               hrow2.getCell(startConversionRate).setCellStyle(headCellStyle);
                               hrow3.createCell(startConversionRate).setCellValue("");
                               hrow3.getCell(startConversionRate).setCellStyle(headCellStyle);
                               sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startConversionRate), rownum + 2, CellReference.convertNumToColString(startConversionRate), rownum + 3)));


                               // set Conversion Rate of product
                               sheet.getRow(rowIndx).createCell(startConversionRate).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startConversionRate).setCellValue((double) m.getValue());
                               sheet.getRow(rowIndx).getCell(startConversionRate).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startConversionRate), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                               lastCols = startConversionRate;
                               startConversionRate++;
                           }
                           startTotalConversionRate = lastCols + 1;
                           // set Header Total of products
                           hrow1.createCell(startTotalConversionRate).setCellValue("Conversion Rate %");
                           hrow1.getCell(startTotalConversionRate).setCellStyle(headCellStyle);
                           hrow2.createCell(startTotalConversionRate).setCellValue("Total");
                           hrow2.getCell(startTotalConversionRate).setCellStyle(headCellStyle);
                           hrow3.createCell(startTotalConversionRate).setCellValue("");
                           hrow3.getCell(startTotalConversionRate).setCellStyle(headCellStyle);
                           sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startTotalConversionRate), rownum + 2, CellReference.convertNumToColString(startTotalConversionRate), rownum + 3)));

                           Double totalConversionRate = 0.00;
                           if(totalAFYPSubmit>0) totalConversionRate = (double)totalAFYPSuccess/totalAFYPSubmit;

                           // set Total Conversion Rate of products
                           sheet.getRow(rowIndx).createCell(startTotalConversionRate).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                           sheet.getRow(rowIndx).getCell(startTotalConversionRate).setCellValue(totalConversionRate);
                           sheet.getRow(rowIndx).getCell(startTotalConversionRate).setCellStyle(numberCellStyle);
                           CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startTotalConversionRate), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                           lastCols = startTotalConversionRate;

                           sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(mergeHeaderStart), rownum + 1, CellReference.convertNumToColString(lastCols), rownum + 1)));

                           isRejectCase = true;
                           isConversionRate = false;
                       }

                       if(isRejectCase) {
                           if (rs.getMetaData().getColumnName(i).equals("Apps reject no family")) {
                               i += 2;
                               if (startColHeaderProductRejectedIndex == 0) {
                                   startColHeaderProductRejectedIndex = lastCols +1;
                               }
                               startAPPSRejectNoFamily = lastCols +1;
                               startNetPremiumRejectNoFamily = lastCols + 2;
                               isAPPSRejectNoFamily = true;
                           }else if(rs.getMetaData().getColumnName(i).equals("Net premium reject no family")) {
                               i += 2;
                               isAPPSRejectNoFamily = false;
                               isNetPremiumRejectNoFamily = true; // test
                           }else if(rs.getMetaData().getColumnName(i).equals("Apps reject with family")){
                               i += 2;
                               if (startColHeaderProductRejectedIndex == 0) {
                                   startColHeaderProductRejectedIndex = lastCols +1;
                               }
                               isNetPremiumRejectNoFamily = false;
                               isAPPSRejectWithFamily = true;
                               startAPPSRejectWithFamily = lastCols +1;
                               startNetPremiumRejectWithFamily = lastCols + 2;
                           }else if(rs.getMetaData().getColumnName(i).equals("Net premium reject with family")){
                               i += 2;
                               isAPPSRejectWithFamily = false;
                               isNetPremiumRejectWithFamily = true;

                           }


                           if(isAPPSRejectNoFamily){
                               // set Header App Reject
                               hrow1.createCell(startAPPSRejectNoFamily).setCellValue("");
                               hrow1.getCell(startAPPSRejectNoFamily).setCellStyle(headCellAltStyle);
                               hrow2.createCell(startAPPSRejectNoFamily).setCellValue("");
                               hrow2.getCell(startAPPSRejectNoFamily).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startAPPSRejectNoFamily).setCellValue("APP");
                               hrow3.getCell(startAPPSRejectNoFamily).setCellStyle(headCellAltStyle);

                               // set App Reject
                               sheet.getRow(rowIndx).createCell(startAPPSRejectNoFamily).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startAPPSRejectNoFamily).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startAPPSRejectNoFamily).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startAPPSRejectNoFamily), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               startAPPSRejectNoFamily +=3;

                           }


                           if(isNetPremiumRejectNoFamily){

                               //set Group Header Product No family
                               hrow1.createCell(startColHeaderProductRejectedIndex).setCellValue("Reject Case");
                               hrow1.getCell(startColHeaderProductRejectedIndex).setCellStyle(headCellAltStyle);
//
                               //set header product name No family
                               hrow1.createCell(startNetPremiumRejectNoFamily-1).setCellValue("");
                               hrow1.getCell(startNetPremiumRejectNoFamily-1).setCellStyle(headCellAltStyle);
                               hrow2.createCell(startNetPremiumRejectNoFamily - 1).setCellValue(rs.getMetaData().getColumnName(i));
                               hrow2.getCell(startNetPremiumRejectNoFamily - 1).setCellStyle(headCellAltStyle);
                               sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startNetPremiumRejectNoFamily - 1), rownum + 2, CellReference.convertNumToColString(startNetPremiumRejectNoFamily + 1), rownum + 2)));

                               //set Header net premium Reject No family
                               hrow1.createCell(startNetPremiumRejectNoFamily).setCellValue("");
                               hrow1.getCell(startNetPremiumRejectNoFamily).setCellStyle(headCellAltStyle);
                               hrow2.createCell(startNetPremiumRejectNoFamily).setCellValue("");
                               hrow2.getCell(startNetPremiumRejectNoFamily).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startNetPremiumRejectNoFamily).setCellValue("Premium");
                               hrow3.getCell(startNetPremiumRejectNoFamily).setCellStyle(headCellAltStyle);
//
                               // set net premium Reject No family
                               sheet.getRow(rowIndx).createCell(startNetPremiumRejectNoFamily).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startNetPremiumRejectNoFamily).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startNetPremiumRejectNoFamily).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startNetPremiumRejectNoFamily), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                               // set Header % No family
                               hrow1.createCell(startNetPremiumRejectNoFamily+1).setCellValue("");
                               hrow1.getCell(startNetPremiumRejectNoFamily+1).setCellStyle(headCellAltStyle);
                               hrow2.createCell(startNetPremiumRejectNoFamily+1).setCellValue("");
                               hrow2.getCell(startNetPremiumRejectNoFamily+1).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startNetPremiumRejectNoFamily+1).setCellValue("%");
                               hrow3.getCell(startNetPremiumRejectNoFamily+1).setCellStyle(headCellAltStyle);

                               int successApp = successAppsOfProducts.get(rs.getMetaData().getColumnName(i));

                               //set % No family
                               sheet.getRow(rowIndx).createCell(startNetPremiumRejectNoFamily + 1).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startNetPremiumRejectNoFamily + 1).setCellFormula(String.format("IF(%s%d> 0,%s%d/(%d+%s%d), 0)",CellReference.convertNumToColString(startNetPremiumRejectNoFamily-1), lastRow + 1, CellReference.convertNumToColString(startNetPremiumRejectNoFamily-1), lastRow + 1, successApp,CellReference.convertNumToColString(startNetPremiumRejectNoFamily-1), lastRow + 1));
                               sheet.getRow(rowIndx).getCell(startNetPremiumRejectNoFamily + 1).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startNetPremiumRejectNoFamily + 1), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                               lastCols = startNetPremiumRejectNoFamily + 1;
                               startNetPremiumRejectNoFamily +=3;

                               if(i==colCount){
                               // merge header Reject Case
                                   sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startColHeaderProductRejectedIndex), rownum + 1, CellReference.convertNumToColString(lastCols), rownum + 1)));
                                   isNetPremiumRejectNoFamily = false;
                                   totalColumnCountInExcel = lastCols;
                               }

                           }

                           if(isAPPSRejectWithFamily){
                               // set Header App Reject
                               hrow1.createCell(startAPPSRejectWithFamily).setCellValue("");
                               hrow1.getCell(startAPPSRejectWithFamily).setCellStyle(headCellAltStyle);
                               hrow2.createCell(startAPPSRejectWithFamily).setCellValue("");
                               hrow2.getCell(startAPPSRejectWithFamily).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startAPPSRejectWithFamily).setCellValue("APP/Person");
                               hrow3.getCell(startAPPSRejectWithFamily).setCellStyle(headCellAltStyle);

                               // set App Reject
                               sheet.getRow(rowIndx).createCell(startAPPSRejectWithFamily).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startAPPSRejectWithFamily).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startAPPSRejectWithFamily).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startAPPSRejectWithFamily), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                               startAPPSRejectWithFamily +=3;

                           }

                           if(isNetPremiumRejectWithFamily){

                               //set Group Header Product with family
                               hrow1.createCell(startColHeaderProductRejectedIndex).setCellValue("Reject Case");
                               hrow1.getCell(startColHeaderProductRejectedIndex).setCellStyle(headCellAltStyle);
//
                               //set header product name with family
                               hrow1.createCell(startNetPremiumRejectWithFamily-1).setCellValue("");
                               hrow1.getCell(startNetPremiumRejectWithFamily-1).setCellStyle(headCellAltStyle);
                               hrow2.createCell(startNetPremiumRejectWithFamily - 1).setCellValue(rs.getMetaData().getColumnName(i));
                               hrow2.getCell(startNetPremiumRejectWithFamily - 1).setCellStyle(headCellAltStyle);
                               sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startNetPremiumRejectWithFamily - 1), rownum + 2, CellReference.convertNumToColString(startNetPremiumRejectWithFamily + 1), rownum + 2)));

                               //set Header net premium Reject No family
                               hrow1.createCell(startNetPremiumRejectWithFamily).setCellValue("");
                               hrow1.getCell(startNetPremiumRejectWithFamily).setCellStyle(headCellAltStyle);
                               hrow2.createCell(startNetPremiumRejectWithFamily).setCellValue("");
                               hrow2.getCell(startNetPremiumRejectWithFamily).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startNetPremiumRejectWithFamily).setCellValue("Premium");
                               hrow3.getCell(startNetPremiumRejectWithFamily).setCellStyle(headCellAltStyle);
//
                               // set net premium Reject with family
                               sheet.getRow(rowIndx).createCell(startNetPremiumRejectWithFamily).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startNetPremiumRejectWithFamily).setCellValue(rs.getInt(i));
                               sheet.getRow(rowIndx).getCell(startNetPremiumRejectWithFamily).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startNetPremiumRejectWithFamily), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                               // set Header % with family
                               hrow1.createCell(startNetPremiumRejectWithFamily+1).setCellValue("");
                               hrow1.getCell(startNetPremiumRejectWithFamily+1).setCellStyle(headCellAltStyle);
                               hrow2.createCell(startNetPremiumRejectWithFamily+1).setCellValue("");
                               hrow2.getCell(startNetPremiumRejectWithFamily+1).setCellStyle(headCellAltStyle);
                               hrow3.createCell(startNetPremiumRejectWithFamily+1).setCellValue("%");
                               hrow3.getCell(startNetPremiumRejectWithFamily+1).setCellStyle(headCellAltStyle);

                               int successApp = successAppsOfProducts.get(rs.getMetaData().getColumnName(i));

                               //set % with family
                               sheet.getRow(rowIndx).createCell(startNetPremiumRejectWithFamily + 1).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                               sheet.getRow(rowIndx).getCell(startNetPremiumRejectWithFamily + 1).setCellFormula(String.format("IF(%s%d > 0,%s%d/(%d+%s%d), 0)",CellReference.convertNumToColString(startNetPremiumRejectWithFamily-1), lastRow + 1, CellReference.convertNumToColString(startNetPremiumRejectWithFamily-1), lastRow + 1,successApp,CellReference.convertNumToColString(startNetPremiumRejectWithFamily-1), lastRow + 1));
                               sheet.getRow(rowIndx).getCell(startNetPremiumRejectWithFamily + 1).setCellStyle(numberCellStyle);
                               CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(startNetPremiumRejectWithFamily + 1), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                               lastCols = startNetPremiumRejectWithFamily + 1;
                               startNetPremiumRejectWithFamily +=3;

                               if(i==colCount){
                                   // merge header Reject Case
                                   sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startColHeaderProductRejectedIndex), rownum + 1, CellReference.convertNumToColString(lastCols), rownum + 1)));
                                   isNetPremiumRejectWithFamily = false;
                                   totalColumnCountInExcel = lastCols;
                               }

                           }

                       }
                       i++;
                   }

                   rowIndx++;
               }while (rs.next());
            }

            int rowTotal = lastRow+1 ;
            int startData = 13;
            int lastData = lastRow+1;
            int totalSuccessAFYPOfProduct =0;
            int totalSubmitAFYPOfProduct = 0;

            HSSFRow hrow4 = sheet.createRow(rowTotal);
            HSSFRow hrow5 = sheet.createRow(rowTotal+1);

            //set Header Total
            hrow4.createCell(0).setCellValue("Total");
            hrow4.getCell(0).setCellStyle(headCellStyle);
            //sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(0), rowTotal+1, CellReference.convertNumToColString(2), rowTotal+1)));

            hrow5.createCell(0).setCellValue("Avg./Day");
            hrow5.getCell(0).setCellStyle(dateCellStyle);

            for(int j = 1 ;j<= totalColumnCountInExcel;j++) {

                if(j==1){
                    hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow4.getCell(j).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow4.getCell(j).setCellStyle(headCellStyle2);
                    CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                    //average
                    hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow5.getCell(j).setCellStyle(numberCellStyle);
                    CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                }else if(j==2){
                    //set total follow
                    hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow4.getCell(j).setCellValue(totalFollow);
                    hrow4.getCell(j).setCellStyle(headCellStyle2);
                    CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                    //average
                    hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow5.getCell(j).setCellStyle(numberCellStyle);
                    CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                }else if(j==3){
                    //set total follow (distinct)+ total new
                    hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow4.getCell(j).setCellFormula(String.format("(%s%d+%s%d)", CellReference.convertNumToColString(j-2), rowTotal + 1, CellReference.convertNumToColString(j-1), rowTotal + 1));
                    hrow4.getCell(j).setCellStyle(headCellStyle2);
                    CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                    //average
                    hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow5.getCell(j).setCellStyle(numberCellStyle);
                    CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                }else if(j==5){
                    // set Call Attempt Rate
                    hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow4.getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j - 2), rowTotal + 1, CellReference.convertNumToColString(j - 1), rowTotal + 1, CellReference.convertNumToColString(j - 2), rowTotal + 1));
                    hrow4.getCell(j).setCellStyle(headCellStyle2);
                    CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));

                    //average
                    hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow5.getCell(j).setCellStyle(numberCellStyle);
                    CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                } else if(j==9){
                    // set Daily Contactable %
                    hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow4.getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j - 6), rowTotal + 1, CellReference.convertNumToColString(j - 2), rowTotal + 1, CellReference.convertNumToColString(j - 6), rowTotal + 1));
                    hrow4.getCell(j).setCellStyle(headCellStyle2);
                    CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                    //average
                    hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow5.getCell(j).setCellStyle(numberCellStyle);
                    CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                } else if(j==10){
                    // set Accumulate Daily Contactable %
                    hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow4.getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j - 4), rowTotal + 1, CellReference.convertNumToColString(j - 2), rowTotal + 1, CellReference.convertNumToColString(j - 4), rowTotal + 1));
                    hrow4.getCell(j).setCellStyle(headCellStyle2);
                    CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                    //average
                    hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow5.getCell(j).setCellStyle(numberCellStyle);
                    CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                } else if(j==12){
                    // set Sales Close Rate %
                    hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow4.getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j - 5), rowTotal + 1, CellReference.convertNumToColString(j - 1), rowTotal + 1, CellReference.convertNumToColString(j - 5), rowTotal + 1));
                    hrow4.getCell(j).setCellStyle(headCellStyle2);
                    CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                    //average
                    hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow5.getCell(j).setCellStyle(numberCellStyle);
                    CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                } else if(hrow3.getCell(j).getStringCellValue().trim().equals("AARP")){
                    hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow4.getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j-2), rowTotal+1, CellReference.convertNumToColString(j-1), rowTotal+1,CellReference.convertNumToColString(j-2), rowTotal+1));
                    hrow4.getCell(j).setCellStyle(headCellStyle2);
                    CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                    //average
                    hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow5.getCell(j).setCellStyle(numberCellStyle);
                    CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                } else if(hrow3.getCell(j).getStringCellValue().trim().equals("AARP/Family")){
                    hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow4.getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j-3), rowTotal+1, CellReference.convertNumToColString(j-1), rowTotal+1,CellReference.convertNumToColString(j-3), rowTotal+1));
                    hrow4.getCell(j).setCellStyle(headCellStyle2);
                    CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                    //average
                    hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow5.getCell(j).setCellStyle(numberCellStyle);
                    CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                } else if(hrow3.getCell(j).getStringCellValue().trim().equals("AARP/Person")){
                    hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow4.getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(j-3), rowTotal+1, CellReference.convertNumToColString(j-2), rowTotal+1,CellReference.convertNumToColString(j-3), rowTotal+1));
                    hrow4.getCell(j).setCellStyle(headCellStyle2);
                    CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                    //average
                    hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow5.getCell(j).setCellStyle(numberCellStyle);
                    CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                } else if(hrow1.getCell(j).getStringCellValue().trim().equals("List Conversion %")){
                    hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow4.getCell(j).setCellFormula(String.format("ROUND(IF(%s%d > 0,%s%d/%s%d, 0),2)", CellReference.convertNumToColString(1),rowTotal+1, CellReference.convertNumToColString(j-3),rowTotal+1, CellReference.convertNumToColString(1),rowTotal+1));
                    hrow4.getCell(j).setCellStyle(headCellStyle2);
                    CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                    //average
                    hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow5.getCell(j).setCellStyle(numberCellStyle);
                    CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                } else if(hrow1.getCell(j).getStringCellValue().trim().equals("Conversion Rate %")){
                    if(hrow2.getCell(j).getStringCellValue().trim().equals("Total")){
                        hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        hrow4.getCell(j).setCellFormula(String.format("IF(%d > 0,%d/%d, 0)",totalSubmitAFYPOfProduct,totalSuccessAFYPOfProduct,totalSubmitAFYPOfProduct));
                        hrow4.getCell(j).setCellStyle(headCellStyle2);
                        CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                        //average
                        hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                        hrow5.getCell(j).setCellStyle(numberCellStyle);
                        CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                    }else {
                        int submitAFYP = totalSubmitAFYPEachOfProduct.get(hrow2.getCell(j).getStringCellValue().trim());
                        totalSubmitAFYPOfProduct += submitAFYP;
                        int successAFYP = totalSuccessAFYPEachOfProduct.get(hrow2.getCell(j).getStringCellValue().trim());
                        totalSuccessAFYPOfProduct += successAFYP;
                        hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        hrow4.getCell(j).setCellFormula(String.format("IF(%d > 0,%d/%d, 0)",submitAFYP,successAFYP,submitAFYP));
                        hrow4.getCell(j).setCellStyle(headCellStyle2);
                        CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                        //average
                        hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                        hrow5.getCell(j).setCellStyle(numberCellStyle);
                        CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                    }
                } else if(hrow3.getCell(j).getStringCellValue().trim().equals("%")){
                    int successApp = totalSuccessAPPEachOfProduct.get(hrow2.getCell(j-2).getStringCellValue().trim());
                    hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow4.getCell(j).setCellFormula(String.format("IF(%s%d > 0,%s%d/(%d+%s%d), 0)",CellReference.convertNumToColString(j-2),rowTotal+1,CellReference.convertNumToColString(j-2),rowTotal+1,successApp,CellReference.convertNumToColString(j-2),rowTotal+1));
                    hrow4.getCell(j).setCellStyle(headCellStyle2);
                    CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                    //average
                    hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow5.getCell(j).setCellStyle(numberCellStyle);
                    CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));

                } else {
                    hrow4.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow4.getCell(j).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow4.getCell(j).setCellStyle(headCellStyle2);
                    CellUtil.setCellStyleProperty(hrow4.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));

                    //average
                    hrow5.createCell(j).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    hrow5.getCell(j).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(j), startData, CellReference.convertNumToColString(j), lastData));
                    hrow5.getCell(j).setCellStyle(numberCellStyle);
                    CellUtil.setCellStyleProperty(hrow5.getCell(j), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
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

    public void reportListener(ActionEvent event) {
        //generateXLS(this.group);
        generateXLS();
    }

}
