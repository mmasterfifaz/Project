package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.MarketingDAO;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.logging.Level;
import javax.faces.event.ValueChangeEvent;
import javax.sql.DataSource;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;


@ManagedBean
@RequestScoped
public class FwdPerformanceReport {
    private static Logger log = Logger.getLogger(FwdPerformanceReport.class);
    
    private Integer campaignId = 0;
    private String campaignName = "All";
    private Integer marketingId = 0;
    private String marketingName = "All";
    private Date fromDate;
    private Date toDate;
    private Integer group;
      
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
        
        group = 1;
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

    public String getExcelQuery() {
        return getQuery(this.group);
    }

    public String getQuery() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);  
        String query = "";
        //System.out.println("Query : " + query);
        return query;
    }
    
    public String getQuery(Integer group) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        String query = "";
        //System.out.println("Query : " + query);
        return query;
    }
    
    private void generateXLS(Integer group) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
        String date = df.format(new Date());
        String fileName = "FWD_PerformanceReport_" + date + ".xls";
        
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
        trow.createCell(0).setCellValue("Date"); trow.getCell(0).setCellStyle(titleStyle);
        trow.createCell(1).setCellValue(""); trow.getCell(1).setCellStyle(titleStyle);
        if (group == 1) {
            trow.getCell(1).setCellValue(sdf.format(fromDate));
        }
        
        HSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        
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
        cellAltStyle.setFont(font);
        cellAltStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellAltStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellAltStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellAltStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellAltStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellAltStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellAltStyle.setWrapText(true);
        
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor color = palette.findSimilarColor(22, 54, 92);
        HSSFColor colorAlt = palette.findSimilarColor(49, 134, 155);
        
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
        
        HSSFRow hrow = sheet.createRow(rownum);
        
        hrow.createCell(colnum).setCellValue("TSR Name"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("New List"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("Old List"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("List Used"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("Call Attempt"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("Bad List"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("No Contact"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("DMC"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("DMC %"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("Normal Contact"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("Normal Contact %"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("Success Case"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("APE"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("Average Case Size"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("LC %"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        hrow.createCell(colnum).setCellValue("Respond Rate %"); hrow.getCell(colnum++).setCellStyle(cellStyle);
        
        try {
            retrieveData(workbook, sheet);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
        } catch (Exception e) {
            log.error(e);
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void retrieveData(HSSFWorkbook workbook, HSSFSheet sheet) throws SQLException {
        //SimpleDateFormat sdf_head = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
              
        HSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        
        HSSFCellStyle headCellStyle = workbook.createCellStyle();
        headCellStyle.setFont(font);
        headCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        headCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        headCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headCellStyle.setWrapText(true);
        
        HSSFCellStyle headCellAltStyle = workbook.createCellStyle();
        headCellAltStyle.setFont(font);
        headCellAltStyle.setBorderTop(CellStyle.BORDER_THIN);
        headCellAltStyle.setBorderRight(CellStyle.BORDER_THIN);
        headCellAltStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headCellAltStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headCellAltStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headCellAltStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headCellAltStyle.setWrapText(true);
        
        HSSFPalette palette = workbook.getCustomPalette(); 
        HSSFColor color = palette.findSimilarColor(22, 54, 92);
        HSSFColor colorAlt = palette.findSimilarColor(49, 134, 155);
        
        // get the palette index of that color 
        short palIndex = color.getIndex();
        short palAltIndex = colorAlt.getIndex();
        
        // code to get the style for the cell goes here
        headCellStyle.setFillForegroundColor(palIndex);
        headCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headCellAltStyle.setFillForegroundColor(palIndex);
        headCellAltStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
        
        HSSFCellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        dataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        dataCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        dataCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        dataCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        dataCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        
        HSSFCellStyle stringCellStyle = workbook.createCellStyle();
        stringCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        stringCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        stringCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        stringCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        stringCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        stringCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        
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
            
            CallableStatement callStatement = connection.prepareCall("{call [dbo].[sp_fwd_performance_report_by_tsr](?,?,?,?)}");
            callStatement.setInt(1, campaignId);
            callStatement.setInt(2, marketingId);
            if (group == 1) {
                callStatement.setString(3, sdf.format(fromDate));
                callStatement.setString(4, sdf.format(fromDate));
                //sheet.getRow(4).getCell(1).setCellValue(sdf_head.format(fromDate));
            }else{
                String date = sdf.format(fromDate);
                if (FacesContext.getCurrentInstance().getViewRoot().findComponent("frm:selectDate").getAttributes().get("value").toString() != null) {
                    //System.out.println(FacesContext.getCurrentInstance().getViewRoot().findComponent("frm:selectDate").getAttributes().get("value").toString());
                    date = FacesContext.getCurrentInstance().getViewRoot().findComponent("frm:selectDate").getAttributes().get("value").toString();
                }
                //date = (FacesContext.getCurrentInstance().getViewRoot().findComponent("frm:selectDate").getAttributes().get("value") == null) ? sdf.format(fromDate) : FacesContext.getCurrentInstance().getViewRoot().findComponent("frm:selectDate").getAttributes().get("value").toString();
                LocalDate localDate = LocalDate.parse(date,DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US));
                //System.out.println("First: " + localDate.withDayOfMonth(1).toString());
                //System.out.println("Last: " + localDate.withDayOfMonth(localDate.lengthOfMonth()).toString());
                callStatement.setString(3, localDate.withDayOfMonth(1).toString());
                callStatement.setString(4, localDate.withDayOfMonth(localDate.lengthOfMonth()).toString());
                
                Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(localDate.withDayOfMonth(1).toString());
                Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(localDate.withDayOfMonth(localDate.lengthOfMonth()).toString());
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                sheet.getRow(4).getCell(1).setCellValue(df.format(startDate) + " - " + df.format(endDate));
            }
            rs = callStatement.executeQuery();          

            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            int startRowIndx = 10;
            int rowIndx = startRowIndx; 
            int rowCount = 0;
            
            if (rs.next()) {
                do {
                    int colIndx = 0;
                    dataRow = sheet.createRow(rowIndx);
                    for (int i = 1; i <= colCount; i++) {
                                
                        dataCell = dataRow.createCell(colIndx);
                        dataCell.setCellStyle(dataCellStyle);
                        
                        if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("TSR Name")) {
                            dataCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            dataCell.setCellValue(rs.getString(i));
                            //CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_CENTER);
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("DMC") || rs.getMetaData().getColumnName(i).equalsIgnoreCase("Normal Contact")) {
                            dataCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            dataCell.setCellValue(rs.getDouble(i));
                            dataCell.setCellStyle(numberCellStyle);
                            
                            colIndx++;
                            dataRow.createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            dataRow.getCell(colIndx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(3), rowIndx + 1, CellReference.convertNumToColString(colIndx - 1), rowIndx + 1, CellReference.convertNumToColString(3), rowIndx + 1));
                            dataRow.getCell(colIndx).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(dataRow.getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("APE")) {
                            dataCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            dataCell.setCellValue(rs.getDouble(i));
                            dataCell.setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(dataRow.getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            
                            colIndx++;
                            dataRow.createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_FORMULA); // Average Case
                            dataRow.getCell(colIndx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(11), rowIndx + 1, CellReference.convertNumToColString(colIndx - 1), rowIndx + 1, CellReference.convertNumToColString(11), rowIndx + 1));
                            dataRow.getCell(colIndx).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(dataRow.getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            
                            colIndx++;
                            dataRow.createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_FORMULA); // LC %
                            dataRow.getCell(colIndx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(1), rowIndx + 1, CellReference.convertNumToColString(11), rowIndx + 1, CellReference.convertNumToColString(1), rowIndx + 1));
                            dataRow.getCell(colIndx).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(dataRow.getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            
                            colIndx++;
                            dataRow.createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_FORMULA); // Respond Rate %
                            dataRow.getCell(colIndx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(7), rowIndx + 1, CellReference.convertNumToColString(11), rowIndx + 1, CellReference.convertNumToColString(7), rowIndx + 1));
                            dataRow.getCell(colIndx).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(dataRow.getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                        }else{
                            dataCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            dataCell.setCellValue(rs.getDouble(i));
                            dataCell.setCellStyle(numberCellStyle);
                        }
                        
                        //System.out.println(rs.getMetaData().getColumnName(i));
                        colIndx++;
                        
                    }
                    rowIndx++;
                    rowCount++;
                } while (rs.next());
                
                sheet.createRow(rowIndx);
                
                for (int i = 0; i <= 15; i++) {
                    sheet.getRow(rowIndx).createCell(i).setCellStyle(headCellStyle);
                    if (i == 0) {
                        sheet.getRow(rowIndx).getCell(i).setCellType(HSSFCell.CELL_TYPE_STRING);
                        sheet.getRow(rowIndx).getCell(i).setCellValue("Total");
                        //sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                    }else if (i == 8) {
                        sheet.getRow(rowIndx).getCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(3), rowIndx + 1, CellReference.convertNumToColString(i - 1), rowIndx + 1, CellReference.convertNumToColString(3), rowIndx + 1));
                        //sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                        //CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }else if (i == 10) {
                        sheet.getRow(rowIndx).getCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(3), rowIndx + 1, CellReference.convertNumToColString(i - 1), rowIndx + 1, CellReference.convertNumToColString(3), rowIndx + 1));
                        //sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                        //CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }else if (i == 13) {
                        sheet.getRow(rowIndx).getCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(11), rowIndx + 1, CellReference.convertNumToColString(i - 1), rowIndx + 1, CellReference.convertNumToColString(11), rowIndx + 1));
                        //sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                        //CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }else if (i == 14) {
                        sheet.getRow(rowIndx).getCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(1), rowIndx + 1, CellReference.convertNumToColString(11), rowIndx + 1, CellReference.convertNumToColString(1), rowIndx + 1));
                        //sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                        //CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }else if (i == 15) {
                        sheet.getRow(rowIndx).getCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(7), rowIndx + 1, CellReference.convertNumToColString(11), rowIndx + 1, CellReference.convertNumToColString(7), rowIndx + 1));
                    }else{
                        sheet.getRow(rowIndx).getCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(i), startRowIndx + 1, CellReference.convertNumToColString(i), rowIndx));
                        ///sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                        ///CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }
                    
                    //Set Cell Format 
                    if (i == 8 || i == 10 || i == 14 || i == 15) {
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%")); 
                    }else if (i == 12 || i == 13) {
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00")); 
                    }else if (i > 0) {
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }
                }
                
                
                workbook.getSheetAt(0).setColumnWidth(0, 6000);
                for (int col = 1; col < colCount + 5; col++) {
                    //workbook.getSheetAt(0).autoSizeColumn(col);
                    workbook.getSheetAt(0).setColumnWidth(col, 4000);
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
           
    public void reportListener(ActionEvent event) {
        generateXLS(this.group);
    }
    
    public void groupChangeListener(ValueChangeEvent event) {
        group = (Integer) event.getNewValue();
    }

}
