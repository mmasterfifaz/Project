/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.MarketingDAO;
import com.maxelyz.core.model.entity.Assignment;
import com.maxelyz.utils.SecurityUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Thanapornchai
 */
@ManagedBean
@RequestScoped
public class DailySalePerfomanceReport {

    private static Logger log = Logger.getLogger(DailySalePerfomanceReport.class);

    private Integer campaignId = 0;
    private String campaignName = "All";
    private Date fromDate;
    private Date toDate;
    
    private String fDate;

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:dailysaleperfomancereport:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        fromDate = new Date();
        toDate = new Date();
    }

    private void generateXLS() {
        Map<String, String> dateMap = new HashMap<>();
        if (FacesContext.getCurrentInstance().getViewRoot().findComponent("frm:selectDate").getAttributes().get("value").toString() != null) {
            fDate = FacesContext.getCurrentInstance().getViewRoot().findComponent("frm:selectDate").getAttributes().get("value").toString();
            dateMap.put("01", "Jan");
            dateMap.put("02", "Feb");
            dateMap.put("03", "Mar");
            dateMap.put("04", "Apr");
            dateMap.put("05", "May");
            dateMap.put("06", "Jun");
            dateMap.put("07", "Jul");
            dateMap.put("08", "Aug");
            dateMap.put("09", "Sep");
            dateMap.put("10", "Oct");
            dateMap.put("11", "Nov");
            dateMap.put("12", "Dec");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
        String date = df.format(new Date());
        String fileName = "DailySalePerfomanceReport_" + date + ".xlsx";

        FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("application/vnd.ms-excel");
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Report");

        XSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        XSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(font);
        titleStyle.setAlignment(CellStyle.ALIGN_LEFT);
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        XSSFRow trow = sheet.createRow(0);
        trow.createCell(0).setCellValue("Daily Sale Perfomance Report");
        trow.getCell(0).setCellStyle(titleStyle);

        trow = sheet.createRow(2);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.US);
        if(fDate != null && !fDate.isEmpty()){
            String[] spilt = fDate.split("-");
            trow.createCell(0).setCellValue("Month Period: " + dateMap.get(spilt[1]) +  " " + spilt[0]);
        } else {
            trow.createCell(0).setCellValue("Month Period: " + sdf.format(fromDate));
        }
        trow.getCell(0).setCellStyle(titleStyle);

        trow = sheet.createRow(3);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        trow.createCell(0).setCellValue("Report Date: " + sdf1.format(fromDate));
        trow.getCell(0).setCellStyle(titleStyle);
      
        trow = sheet.createRow(4);
        trow.createCell(0).setCellValue("Campaign : " + this.campaignName);
        trow.getCell(0).setCellStyle(titleStyle);

        try {
            retrieveData(workbook, sheet);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
        } catch (Exception e) {
            log.error(e);
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void retrieveData(XSSFWorkbook workbook, XSSFSheet sheet) throws SQLException {
        XSSFFont headFont = workbook.createFont();
        headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

        XSSFCellStyle headCellStyle = workbook.createCellStyle();
        headCellStyle.setFont(headFont);
        headCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        headCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        headCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        DataFormat format = workbook.createDataFormat();
        XSSFCellStyle rightDataCellStyle = workbook.createCellStyle();
        rightDataCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        rightDataCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        rightDataCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        rightDataCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        rightDataCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        rightDataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        rightDataCellStyle.setDataFormat(format.getFormat("#,##0"));

        XSSFCellStyle rightPercentDataCellStyle = workbook.createCellStyle();
        rightPercentDataCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        rightPercentDataCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        rightPercentDataCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        rightPercentDataCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        rightPercentDataCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        rightPercentDataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        rightPercentDataCellStyle.setDataFormat(format.getFormat("#,##0.00%"));

        XSSFCellStyle leftDataCellStyle = workbook.createCellStyle();
        leftDataCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        leftDataCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        leftDataCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        leftDataCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        leftDataCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        leftDataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String campaignMarkering = "";
        List<Assignment> assignmentList = marketingDAO.findMarketingByCampaignId(campaignId);

        Map<Integer, String> map = new TreeMap<>();
        List<String> dateList = null;
        Map<String, Integer> dateMap = new TreeMap<>();

        for (int i = 0; i < assignmentList.size(); i++) {
            map.put(assignmentList.get(i).getMarketing().getId(), assignmentList.get(i).getCampaign().getName() + " - " + assignmentList.get(i).getMarketing().getName());
        }

        XSSFRow dataRow = null;
        Cell dataCell;

        Calendar calendar = GregorianCalendar.getInstance();

        int row = 8;
        int lastCell = 0;
        int headRow = 6;
        Map<Integer, Integer> weekMap = new TreeMap<>();
        int weekPrevious;
        int sumFirstWeek;
        int sumLastWeek;
        Map<String, Integer> rowMap;

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        int numCol = 0;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
//            String query = "exec [sp_daily_sale_perfomance] '" + sdf.format(fromDate) + " 00:00:00', " + campaignId + ", " + entry.getKey();
            String query = "exec [sp_daily_sale_perfomance] '" + fDate + " 00:00:00', " + campaignId + ", " + entry.getKey();
            campaignMarkering = entry.getValue();

            try {
                connection = dataSource.getConnection();
                statement = connection.createStatement();
                rs = statement.executeQuery(query);
                ResultSetMetaData rsmd = rs.getMetaData();
                numCol = rsmd.getColumnCount();
                dateList = new ArrayList<>();
                rowMap = new HashMap<>();
                weekPrevious = 0;

                if (rs.next()) {
                    do {
                        dataRow = sheet.createRow(row++);
                        sumFirstWeek = 3;
                        sumLastWeek = 2;
                        for (int i = 1; i <= numCol; i++) {
                            sheet.setColumnWidth(i, 3000);

                            switch (i) {
                                case 1:
                                case 2:
                                    dataCell = dataRow.createCell(i - 1);
                                    dataCell.setCellValue(rs.getString(i));
                                    dataCell.setCellStyle(leftDataCellStyle);
                                    dataCell.setCellType(Cell.CELL_TYPE_STRING);
                                    if (i == 1 && (rs.getString(i).equals("Lead uploaded") || rs.getString(i).equals("Contactable") || rs.getString(i).equals("Issued Sales (Submit)")
                                            || rs.getString(i).equals("Gross TARP (Submit)") || rs.getString(i).equals("Collectted Premium")
                                            || rs.getString(i).equals("Collectted Case"))) {
                                        rowMap.put(rs.getString(i), row);
                                    }
                                    break;
                                case 3:
                                    break;
                                default:
                                    dataCell = dataRow.createCell(i - 1);
                                    if (rs.getString(i) != null) {
                                        if (rs.getString(1).equals("% Contact Rate on new lead used") || rs.getString(1).equals("% Response on contactable")
                                                || rs.getString(1).equals("% Collection Case") || rs.getString(1).equals("% List Conversion on new lead used")
                                                || rs.getString(1).equals("% Collection Premium")) {
                                            Double data = rs.getDouble(i) / 100;
                                            dataCell.setCellValue(data);
                                            dataCell.setCellStyle(rightPercentDataCellStyle);
                                        } else {
                                            dataCell.setCellValue(rs.getInt(i));
                                            dataCell.setCellStyle(rightDataCellStyle);
                                        }
                                    } else {
                                        if (rs.getString(1).equals("% Contact Rate on new lead used") || rs.getString(1).equals("% Response on contactable")
                                                || rs.getString(1).equals("% Collection Case") || rs.getString(1).equals("% List Conversion on new lead used")
                                                || rs.getString(1).equals("% Collection Premium")) {
                                            dataCell.setCellValue(new Double(0));
                                            dataCell.setCellStyle(rightPercentDataCellStyle);
                                        } else {
                                            dataCell.setCellValue(0);
                                            dataCell.setCellStyle(rightDataCellStyle);
                                        }
                                    }

                                    if (i >= 4 && dateList.size() <= numCol - 4) {
                                        String[] yyyyMMdd = rsmd.getColumnName(i).split("-");
                                        calendar.set(Integer.parseInt(yyyyMMdd[0]), Integer.parseInt(yyyyMMdd[1]), Integer.parseInt(yyyyMMdd[2]));
                                        dateMap.put(rsmd.getColumnName(i), calendar.get(Calendar.WEEK_OF_MONTH));

                                        if (weekPrevious == 0) {
                                            weekPrevious = calendar.get(Calendar.WEEK_OF_MONTH);
                                        } else if (weekPrevious != calendar.get(Calendar.WEEK_OF_MONTH)) {
                                            weekMap.put(sumFirstWeek, sumLastWeek);
                                            weekPrevious = calendar.get(Calendar.WEEK_OF_MONTH);
                                            sumFirstWeek = sumLastWeek + 1;
                                        }
                                        sumLastWeek++;
                                    }

                                    dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);

                                    if (i == numCol) {
                                        lastCell = i - 1;
                                        dataCell = dataRow.createCell(2);

                                        if (rs.getString(1).equals("% Contact Rate on new lead used") || rs.getString(1).equals("% Response on contactable")
                                                || rs.getString(1).equals("% Collection Case") || rs.getString(1).equals("% List Conversion on new lead used")
                                                || rs.getString(1).equals("% Collection Premium")) {
                                            int text1 = 0;
                                            int text2 = 0;
                                            if (rs.getString(1).equals("% Contact Rate on new lead used")) {
                                                if (rowMap.get("Contactable") != null && rowMap.get("Lead uploaded") != null) {
                                                    text1 = rowMap.get("Contactable");
                                                    text2 = rowMap.get("Lead uploaded");
                                                }
                                            } else if (rs.getString(1).equals("% Response on contactable")) {
                                                if (rowMap.get("Issued Sales (Submit)") != null && rowMap.get("Lead uploaded") != null) {
                                                    text1 = rowMap.get("Issued Sales (Submit)");
                                                    text2 = rowMap.get("Lead uploaded");
                                                }
                                            } else if (rs.getString(1).equals("% List Conversion on new lead used")) {
                                                if (rowMap.get("Issued Sales (Submit)") != null && rowMap.get("Contactable") != null) {
                                                    text1 = rowMap.get("Issued Sales (Submit)");
                                                    text2 = rowMap.get("Contactable");
                                                }
                                            } else if (rs.getString(1).equals("% Collection Case")) {
                                                if (rowMap.get("Collectted Case") != null && rowMap.get("Gross TARP (Submit)") != null) {
                                                    text1 = rowMap.get("Collectted Case");
                                                    text2 = rowMap.get("Gross TARP (Submit)");
                                                }
                                            } else if (rs.getString(1).equals("% Collection Premium")) {
                                                if (rowMap.get("Collectted Premium") != null && rowMap.get("Gross TARP (Submit)") != null) {
                                                    text1 = rowMap.get("Collectted Premium");
                                                    text2 = rowMap.get("Gross TARP (Submit)");
                                                }
                                            }
                                            if (text1 != 0 && text2 != 0) {
                                                dataCell.setCellFormula(String.format("IFERROR((%s%d/%s%d),0)",
                                                        CellReference.convertNumToColString(2), text1, CellReference.convertNumToColString(2), text2));
                                            } else {
                                                dataCell.setCellValue(new Double(0));
                                            }
                                            dataCell.setCellStyle(rightPercentDataCellStyle);
                                        } else {
                                            dataCell.setCellFormula(String.format("SUM(%s%d:%s%d)",
                                                    CellReference.convertNumToColString(3), row, CellReference.convertNumToColString(lastCell), row));
                                            dataCell.setCellStyle(rightDataCellStyle);
                                        }
                                        dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
                                        weekMap.put(sumFirstWeek, sumLastWeek);

                                        for (Map.Entry<Integer, Integer> entry1 : weekMap.entrySet()) {
                                            dataCell = dataRow.createCell(++i);
                                            if (rs.getString(1).equals("% Contact Rate on new lead used") || rs.getString(1).equals("% Response on contactable")
                                                    || rs.getString(1).equals("% Collection Case") || rs.getString(1).equals("% List Conversion on new lead used")
                                                    || rs.getString(1).equals("% Collection Premium")) {
                                                int text1 = 0;
                                                int text2 = 0;
                                                if (rs.getString(1).equals("% Contact Rate on new lead used")) {
                                                    if (rowMap.get("Contactable") != null && rowMap.get("Lead uploaded") != null) {
                                                        text1 = rowMap.get("Contactable");
                                                        text2 = rowMap.get("Lead uploaded");
                                                    }
                                                } else if (rs.getString(1).equals("% Response on contactable")) {
                                                    if (rowMap.get("Issued Sales (Submit)") != null && rowMap.get("Lead uploaded") != null) {
                                                        text1 = rowMap.get("Issued Sales (Submit)");
                                                        text2 = rowMap.get("Lead uploaded");
                                                    }
                                                } else if (rs.getString(1).equals("% List Conversion on new lead used")) {
                                                    if (rowMap.get("Issued Sales (Submit)") != null && rowMap.get("Contactable") != null) {
                                                        text1 = rowMap.get("Issued Sales (Submit)");
                                                        text2 = rowMap.get("Contactable");
                                                    }
                                                } else if (rs.getString(1).equals("% Collection Case")) {
                                                    if (rowMap.get("Collectted Case") != null && rowMap.get("Gross TARP (Submit)") != null) {
                                                        text1 = rowMap.get("Collectted Case");
                                                        text2 = rowMap.get("Gross TARP (Submit)");
                                                    }
                                                } else if (rs.getString(1).equals("% Collection Premium")) {
                                                    if (rowMap.get("Collectted Premium") != null && rowMap.get("Gross TARP (Submit)") != null) {
                                                        text1 = rowMap.get("Collectted Premium");
                                                        text2 = rowMap.get("Gross TARP (Submit)");
                                                    }
                                                }
                                                if (text1 != 0 && text2 != 0) {
                                                    dataCell.setCellFormula(String.format("IFERROR((%s%d/%s%d),0)",
                                                            CellReference.convertNumToColString(i), text1, CellReference.convertNumToColString(i), text2));
                                                } else {
                                                    dataCell.setCellValue(new Double(0));
                                                }
                                                dataCell.setCellStyle(rightPercentDataCellStyle);
                                            } else {
                                                dataCell.setCellFormula(String.format("SUM(%s%d:%s%d)",
                                                        CellReference.convertNumToColString(entry1.getKey()), row, CellReference.convertNumToColString(entry1.getValue()), row));
                                                dataCell.setCellStyle(rightDataCellStyle);
                                            }
                                            dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
                                        }
                                        weekMap = new TreeMap<>();
                                    }
                                    break;
                            }
                        }
                    } while (rs.next());
                }
                int previousWeek = 0;
                int firstWeekCell = 0;
                int lastWeekCell = 0;
                int cell = 0;
                int count = 0;
                Integer week = 0;

                for (int i = 1; i <= 2; i++) {
                    dataRow = sheet.createRow(headRow++);
                    cell = 0;
                    for (Map.Entry<String, Integer> entry1 : dateMap.entrySet()) {
                        String date = entry1.getKey();
                        week = entry1.getValue();
                        count++;
                        if (i == 1) {
                            if (cell < 3) {
                                dataCell = dataRow.createCell(cell++);
                                dataCell.setCellValue(campaignMarkering);
                                dataCell.setCellStyle(headCellStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);

                                dataCell = dataRow.createCell(cell++);
                                dataCell.setCellValue("Target");
                                dataCell.setCellStyle(headCellStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);

                                dataCell = dataRow.createCell(cell++);
                                dataCell.setCellValue("TOTAL");
                                dataCell.setCellStyle(headCellStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                            }

                            if (firstWeekCell == 0) {
                                firstWeekCell = cell - 1;
                            }
                            lastWeekCell = cell - 1;
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellValue("Week " + String.valueOf(week));
                            dataCell.setCellStyle(headCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_STRING);

                            if (previousWeek == 0) {
                                previousWeek = week;
                            } else if (previousWeek != week) {
                                if (previousWeek == 1) {
                                    sheet.addMergedRegion(new CellRangeAddress(headRow - 1, headRow - 1, 3, lastWeekCell));
                                } else {
                                    sheet.addMergedRegion(new CellRangeAddress(headRow - 1, headRow - 1, firstWeekCell, lastWeekCell));
                                }
                                firstWeekCell = 0;
                                previousWeek = 0;
                            }

                            if (count == dateMap.size()) {
                                sheet.addMergedRegion(new CellRangeAddress(headRow - 1, headRow - 1, firstWeekCell, lastWeekCell + 1));
                                cell += 1;
                                firstWeekCell = cell;
                                for (int j = 1; j <= week; j++) {
                                    dataCell = dataRow.createCell(cell++);
                                    dataCell.setCellValue("Week");
                                    dataCell.setCellStyle(headCellStyle);
                                    dataCell.setCellType(Cell.CELL_TYPE_STRING);
                                    lastWeekCell = cell;
                                }
                                firstWeekCell = 0;
                                previousWeek = 0;
                            }

                        } else if (i == 2) {
                            if (cell < 3) {
                                for (int j = 0; j < 3; j++) {
                                    dataCell = dataRow.createCell(cell++);
                                    dataCell.setCellValue("");
                                    dataCell.setCellStyle(headCellStyle);
                                    dataCell.setCellType(Cell.CELL_TYPE_STRING);

                                    sheet.addMergedRegion(new CellRangeAddress(headRow - 2, headRow - 1, j, j));
                                }
                            }
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellValue(date);
                            dataCell.setCellStyle(headCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_STRING);
                        }
                    }
                }

                if (dataRow != null) {
                    cell += 1;
                    firstWeekCell = cell;
                    for (int j = 1; j <= week; j++) {
                        dataCell = dataRow.createCell(cell++);
                        dataCell.setCellValue("Week " + Integer.toString(j));
                        dataCell.setCellStyle(headCellStyle);
                        dataCell.setCellType(Cell.CELL_TYPE_STRING);
                        lastWeekCell = cell;
                    }
                    sheet.addMergedRegion(new CellRangeAddress(headRow - 2, headRow - 2, firstWeekCell, lastWeekCell - 1));

                    firstWeekCell = 0;
                    previousWeek = 0;
                }

                headRow = row + 1;
                row += 3;

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
        sheet.autoSizeColumn(0);
    }

    public void reportListener(ActionEvent event) {
        generateXLS();
        FacesContext.getCurrentInstance().responseComplete();
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
            if (campaignId != null && campaignId != 0) {
                this.campaignName = this.getCampaignDAO().findCampaign(campaignId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public Map<String, Integer> getCampaignList() {
        return this.getCampaignDAO().getCampaignList();
    }

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }

    public String getFDate() {
        return fDate;
    }

    public void setFDate(String fDate) {
        this.fDate = fDate;
    }

}
