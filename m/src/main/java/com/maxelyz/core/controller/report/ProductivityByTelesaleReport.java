/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.MarketingDAO;
import com.maxelyz.utils.SecurityUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Thanapornchi
 */
@ManagedBean
@RequestScoped
public class ProductivityByTelesaleReport {

    private static Logger log = Logger.getLogger(ProductivityByTelesaleReport.class);

    private Integer campaignId;
    private String campaignName = "All";
    private Integer marketingId;
    private String marketingName = "All";
    private Date fromDate;
    private Date toDate;
    private Integer dayPassed;
    private Map<Integer, Integer> dayPassedList = new HashMap<>();

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:productivitybytelesale:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        for (int i = 1; i <= 30; i++) {
            dayPassedList.put(i, i);
        }

        fromDate = new Date();
        toDate = new Date();
    }

    private void generateXLS() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
        String date = df.format(new Date());
        String fileName = "ProductivityByTelesaleReport_" + date + ".xlsx";

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
        trow.createCell(0).setCellValue("Productivity By Telesale Report");
        trow.getCell(0).setCellStyle(titleStyle);

        trow = sheet.createRow(2);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        trow.createCell(0).setCellValue("Period: " + sdf.format(fromDate) + " - " + sdf.format(toDate));
        trow.getCell(0).setCellStyle(titleStyle);

        trow = sheet.createRow(3);
        trow.createCell(0).setCellValue("Campaign : " + this.campaignName);
        trow.getCell(0).setCellStyle(titleStyle);

        trow = sheet.createRow(4);
        trow.createCell(0).setCellValue("Marketing: " + this.marketingName);
        trow.getCell(0).setCellStyle(titleStyle);

        trow = sheet.createRow(5);
        trow.createCell(0).setCellValue("Day Passed: " + this.dayPassed);
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

        XSSFCellStyle skyBlueHeadStyle = workbook.createCellStyle();
        skyBlueHeadStyle.setFont(headFont);
        skyBlueHeadStyle.setBorderTop(CellStyle.BORDER_THIN);
        skyBlueHeadStyle.setBorderRight(CellStyle.BORDER_THIN);
        skyBlueHeadStyle.setBorderLeft(CellStyle.BORDER_THIN);
        skyBlueHeadStyle.setBorderBottom(CellStyle.BORDER_THIN);
        skyBlueHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
        skyBlueHeadStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        skyBlueHeadStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(170, 237, 246)));
        skyBlueHeadStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle issuedSalesHeadStyle = workbook.createCellStyle();
        issuedSalesHeadStyle.setFont(headFont);
        issuedSalesHeadStyle.setBorderTop(CellStyle.BORDER_THIN);
        issuedSalesHeadStyle.setBorderRight(CellStyle.BORDER_THIN);
        issuedSalesHeadStyle.setBorderLeft(CellStyle.BORDER_THIN);
        issuedSalesHeadStyle.setBorderBottom(CellStyle.BORDER_THIN);
        issuedSalesHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
        issuedSalesHeadStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        issuedSalesHeadStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(189, 215, 238)));
        issuedSalesHeadStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle paymentPassingHeadStyle = workbook.createCellStyle();
        paymentPassingHeadStyle.setFont(headFont);
        paymentPassingHeadStyle.setBorderTop(CellStyle.BORDER_THIN);
        paymentPassingHeadStyle.setBorderRight(CellStyle.BORDER_THIN);
        paymentPassingHeadStyle.setBorderLeft(CellStyle.BORDER_THIN);
        paymentPassingHeadStyle.setBorderBottom(CellStyle.BORDER_THIN);
        paymentPassingHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
        paymentPassingHeadStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        paymentPassingHeadStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(142, 169, 219)));
        paymentPassingHeadStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle runrateHeadStyle = workbook.createCellStyle();
        runrateHeadStyle.setFont(headFont);
        runrateHeadStyle.setBorderTop(CellStyle.BORDER_THIN);
        runrateHeadStyle.setBorderRight(CellStyle.BORDER_THIN);
        runrateHeadStyle.setBorderLeft(CellStyle.BORDER_THIN);
        runrateHeadStyle.setBorderBottom(CellStyle.BORDER_THIN);
        runrateHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
        runrateHeadStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        runrateHeadStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 0)));
        runrateHeadStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFFont whiteHeadFont = workbook.createFont();
        whiteHeadFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        whiteHeadFont.setColor(HSSFColor.WHITE.index);
        XSSFCellStyle greyHeadCellStyle = workbook.createCellStyle();
        greyHeadCellStyle.setFont(whiteHeadFont);
        greyHeadCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        greyHeadCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        greyHeadCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        greyHeadCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        greyHeadCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        greyHeadCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        greyHeadCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        greyHeadCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        
        XSSFCellStyle greyHeadCellStyleNoRight = workbook.createCellStyle();
        greyHeadCellStyleNoRight.setFont(whiteHeadFont);
        greyHeadCellStyleNoRight.setBorderTop(CellStyle.BORDER_THIN);
//        greyHeadCellStyleNoRight.setBorderRight(CellStyle.BORDER_THIN);
//        greyHeadCellStyleNoRight.setBorderLeft(CellStyle.BORDER_THIN);
        greyHeadCellStyleNoRight.setBorderBottom(CellStyle.BORDER_THIN);
        greyHeadCellStyleNoRight.setAlignment(CellStyle.ALIGN_CENTER);
        greyHeadCellStyleNoRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        greyHeadCellStyleNoRight.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        greyHeadCellStyleNoRight.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        
        XSSFCellStyle greyHeadCellStyleNoLeft = workbook.createCellStyle();
        greyHeadCellStyleNoLeft.setFont(whiteHeadFont);
        greyHeadCellStyleNoLeft.setBorderTop(CellStyle.BORDER_THIN);
        greyHeadCellStyleNoRight.setBorderRight(CellStyle.BORDER_THIN);
//        greyHeadCellStyleNoRight.setBorderLeft(CellStyle.BORDER_THIN);
        greyHeadCellStyleNoLeft.setBorderBottom(CellStyle.BORDER_THIN);
        greyHeadCellStyleNoLeft.setAlignment(CellStyle.ALIGN_CENTER);
        greyHeadCellStyleNoLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        greyHeadCellStyleNoLeft.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        greyHeadCellStyleNoLeft.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        
        XSSFCellStyle greyHeadCellStyleNoLeftAndRight = workbook.createCellStyle();
        greyHeadCellStyleNoLeftAndRight.setFont(whiteHeadFont);
        greyHeadCellStyleNoLeftAndRight.setBorderTop(CellStyle.BORDER_THIN);
//        greyHeadCellStyleNoRight.setBorderRight(CellStyle.BORDER_THIN);
//        greyHeadCellStyleNoRight.setBorderLeft(CellStyle.BORDER_THIN);
        greyHeadCellStyleNoLeftAndRight.setBorderBottom(CellStyle.BORDER_THIN);
        greyHeadCellStyleNoLeftAndRight.setAlignment(CellStyle.ALIGN_CENTER);
        greyHeadCellStyleNoLeftAndRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        greyHeadCellStyleNoLeftAndRight.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        greyHeadCellStyleNoLeftAndRight.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle userCellStyle = workbook.createCellStyle();
        userCellStyle.setFont(headFont);
        userCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        userCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        userCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        userCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        userCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        userCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        DataFormat format = workbook.createDataFormat();
        XSSFCellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        dataCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        dataCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        dataCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        dataCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        dataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        dataCellStyle.setDataFormat(format.getFormat("#,##0"));

        XSSFCellStyle summaryCellStyle = workbook.createCellStyle();
        summaryCellStyle.setFont(headFont);
        summaryCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        summaryCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        summaryCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        summaryCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        summaryCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        summaryCellStyle.setDataFormat(format.getFormat("#,##0"));

        XSSFRow dataRow = null;
        Cell dataCell;

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        int numCol;
        int firstRow = 11;

        String query = "exec [sp_productivity_by_telesale_report] '" + sdf.format(fromDate) + " 00:00:00','" + sdf.format(toDate) + " 23:59:59'," + campaignId + "," + marketingId;

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            int row = firstRow - 1;
            int cell = 0;
            int lastRow = 0;
            int lastCell = 0;

            if (rs.next()) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 7; j++) {
                        if (i == 0 && j == 0) {
                            dataRow = sheet.createRow(i + 7);
                            dataCell = dataRow.createCell(j);
                            dataCell.setCellValue("Name");
                            dataCell.setCellStyle(skyBlueHeadStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_STRING);
                        } else if (j == 0) {
                            dataRow = sheet.createRow(i + 7);
                            dataCell = dataRow.createCell(j);
                            dataCell.setCellValue("");
                            dataCell.setCellStyle(skyBlueHeadStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_STRING);
                            if (i == 2) {
                                sheet.addMergedRegion(new CellRangeAddress(7, 9, 0, 0));
                            }
                        }

                        if (dataRow != null) {
                            if (i == 0 && j == 1) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("TARGET-TARP");
                                dataCell.setCellStyle(skyBlueHeadStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                            } else if (j == 1) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("");
                                dataCell.setCellStyle(skyBlueHeadStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                                if (i == 2) {
                                    sheet.addMergedRegion(new CellRangeAddress(7, 9, 1, 1));
                                }
                            }

                            if (i == 0 && j == 2) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue(21);
                                dataCell.setCellStyle(greyHeadCellStyleNoLeftAndRight);
                                dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            } else if (i == 1 && j == 2) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("Issued Sales (Submit)");
                                dataCell.setCellStyle(issuedSalesHeadStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                            } else if (i == 2 && j == 2) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("APP");
                                dataCell.setCellStyle(issuedSalesHeadStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                            }

                            if (i == 0 && j == 3) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("Working Days");
                                dataCell.setCellStyle(greyHeadCellStyleNoLeftAndRight);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                            } else if (i == 1 && j == 3) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("");
                                dataCell.setCellStyle(issuedSalesHeadStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                                sheet.addMergedRegion(new CellRangeAddress(8, 8, 2, 3));
                            } else if (i == 2 && j == 3) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("TARP");
                                dataCell.setCellStyle(issuedSalesHeadStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                            }

                            if (i == 0 && j == 4) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("H/C");
                                dataCell.setCellStyle(greyHeadCellStyleNoLeftAndRight);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                            } else if (i == 1 && j == 4) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("Payment Passing");
                                dataCell.setCellStyle(paymentPassingHeadStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                            } else if (i == 2 && j == 4) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("APP");
                                dataCell.setCellStyle(paymentPassingHeadStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                            }

                            if (i == 0 && j == 5) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("");
                                dataCell.setCellStyle(greyHeadCellStyleNoLeft);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                            } else if (i == 1 && j == 5) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("");
                                dataCell.setCellStyle(paymentPassingHeadStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                                sheet.addMergedRegion(new CellRangeAddress(8, 8, 4, 5));
                            } else if (i == 2 && j == 5) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("TARP");
                                dataCell.setCellStyle(paymentPassingHeadStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                            }

                            if (i == 0 && j == 6) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("");
                                dataCell.setCellStyle(greyHeadCellStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                                sheet.addMergedRegion(new CellRangeAddress(7, 7, 5, 6));
                            } else if (i == 1 && j == 6) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("Runrate (Submit)");
                                dataCell.setCellStyle(runrateHeadStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                            } else if (i == 2 && j == 6) {
                                dataCell = dataRow.createCell(j);
                                dataCell.setCellValue("");
                                dataCell.setCellStyle(runrateHeadStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);
                                sheet.addMergedRegion(new CellRangeAddress(8, 9, 6, 6));
                            }
                        }
                    }
                }

                do {
                    dataRow = sheet.createRow(row++);
                    lastRow = row;
                    for (int i = 1; i <= numCol; i++) {
                        sheet.setColumnWidth(i, 4000);
                        if (i == 1 || i == 2) {
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellValue(rs.getString(i));
                            dataCell.setCellStyle(userCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_STRING);
                        } else if (i == 7) {
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellFormula(String.format("(D%d/" + dayPassed + ")*C8", row));
                            dataCell.setCellStyle(dataCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
                            lastCell = cell - 1;
                            cell = 0;
                        } else {
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellValue(rs.getInt(i));
                            dataCell.setCellStyle(dataCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        }
                    }
                } while (rs.next());
            }

            //Total
            dataRow = sheet.createRow(row++);

            dataCell = dataRow.createCell(cell++);
            dataCell.setCellValue("");
            dataCell.setCellStyle(summaryCellStyle);
            dataCell.setCellType(Cell.CELL_TYPE_STRING);

            dataCell = dataRow.createCell(cell++);
            dataCell.setCellValue("");
            dataCell.setCellStyle(summaryCellStyle);
            dataCell.setCellType(Cell.CELL_TYPE_STRING);

            for (int i = 1; i < lastCell; i++) {
                dataCell = dataRow.createCell(cell++);
                dataCell.setCellFormula(String.format("SUM(%s%d:%s%d)",
                        CellReference.convertNumToColString(i + 1), firstRow, CellReference.convertNumToColString(i + 1), lastRow));
                dataCell.setCellStyle(summaryCellStyle);
                dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
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
        sheet.autoSizeColumn(0);
    }

    public void reportListener(ActionEvent event) {
        generateXLS();
        FacesContext.getCurrentInstance().responseComplete();
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
            if (campaignId != null && campaignId != 0) {
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

    public DataSource getDataSource() {
        return dataSource;
    }

    public Integer getDayPassed() {
        return dayPassed;
    }

    public void setDayPassed(Integer dayPassed) {
        this.dayPassed = dayPassed;
    }

    public Map<Integer, Integer> getDayPassedList() {
        return dayPassedList;
    }

    public void setDayPassedList(Map<Integer, Integer> dayPassedList) {
        this.dayPassedList = dayPassedList;
    }

}
