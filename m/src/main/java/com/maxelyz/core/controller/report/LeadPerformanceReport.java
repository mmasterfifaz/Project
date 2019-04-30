/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.utils.SecurityUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
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
public class LeadPerformanceReport {

    private static Logger log = Logger.getLogger(LeadPerformanceReport.class);

    private Integer campaignId;
    private String campaignName = "All";

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:leadperformancereport:view")) {
            SecurityUtil.redirectUnauthorize();
        }
    }

    private void generateXLS() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
        String date = df.format(new Date());
        String fileName = "MarketingListPerformanceReport_" + date + ".xlsx";

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

        SimpleDateFormat df1 = new SimpleDateFormat("MMM dd,yyyy", Locale.US);
        date = df1.format(new Date());
        XSSFRow trow = sheet.createRow(0);
        trow.createCell(0).setCellValue("Marketing List Performance as of " + date);
        trow.getCell(0).setCellStyle(titleStyle);

        trow = sheet.createRow(2);
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
        XSSFFont whiteHeadFont = workbook.createFont();
        whiteHeadFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        whiteHeadFont.setColor(HSSFColor.WHITE.index);

        XSSFCellStyle brownHeadStyle = workbook.createCellStyle();
        brownHeadStyle.setFont(whiteHeadFont);
        brownHeadStyle.setBorderTop(CellStyle.BORDER_THIN);
        brownHeadStyle.setBorderRight(CellStyle.BORDER_THIN);
        brownHeadStyle.setBorderLeft(CellStyle.BORDER_THIN);
        brownHeadStyle.setBorderBottom(CellStyle.BORDER_THIN);
        brownHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
        brownHeadStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        brownHeadStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(117, 113, 113)));
        brownHeadStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle blueHeadStyle = workbook.createCellStyle();
        blueHeadStyle.setFont(whiteHeadFont);
        blueHeadStyle.setBorderTop(CellStyle.BORDER_THIN);
        blueHeadStyle.setBorderRight(CellStyle.BORDER_THIN);
        blueHeadStyle.setBorderLeft(CellStyle.BORDER_THIN);
        blueHeadStyle.setBorderBottom(CellStyle.BORDER_THIN);
        blueHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
        blueHeadStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        blueHeadStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(132, 151, 176)));
        blueHeadStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle blackHeadStyle = workbook.createCellStyle();
        blackHeadStyle.setFont(whiteHeadFont);
        blackHeadStyle.setBorderTop(CellStyle.BORDER_THIN);
        blackHeadStyle.setBorderRight(CellStyle.BORDER_THIN);
        blackHeadStyle.setBorderLeft(CellStyle.BORDER_THIN);
        blackHeadStyle.setBorderBottom(CellStyle.BORDER_THIN);
        blackHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
        blackHeadStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        blackHeadStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 0, 0)));
        blackHeadStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle listCellStyle = workbook.createCellStyle();
        listCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        listCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        listCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        listCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        listCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        listCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        DataFormat format = workbook.createDataFormat();
        XSSFCellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        dataCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        dataCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        dataCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        dataCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        dataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        XSSFCellStyle percentageCellStyle = workbook.createCellStyle();
        percentageCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        percentageCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        percentageCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        percentageCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        percentageCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        percentageCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        percentageCellStyle.setDataFormat(format.getFormat("#,##0.00%"));

        XSSFCellStyle summaryCellStyle = workbook.createCellStyle();
        summaryCellStyle.setFont(whiteHeadFont);
        summaryCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        summaryCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        summaryCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        summaryCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(89, 89, 89)));
        summaryCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        summaryCellStyle.setDataFormat(format.getFormat("#,##0"));

        XSSFCellStyle summaryDataCellStyle = workbook.createCellStyle();
        summaryDataCellStyle.setFont(whiteHeadFont);
        summaryDataCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        summaryDataCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        summaryDataCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        summaryDataCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        summaryDataCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        summaryDataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        summaryDataCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(89, 89, 89)));
        summaryDataCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        summaryDataCellStyle.setDataFormat(format.getFormat("#,##0"));

        XSSFCellStyle summaryPercentageCellStyle = workbook.createCellStyle();
        summaryPercentageCellStyle.setFont(whiteHeadFont);
        summaryPercentageCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        summaryPercentageCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        summaryPercentageCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        summaryPercentageCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        summaryPercentageCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        summaryPercentageCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        summaryPercentageCellStyle.setDataFormat(format.getFormat("#,##0.00%"));
        summaryPercentageCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(89, 89, 89)));
        summaryPercentageCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFFont whiteCellFont = workbook.createFont();
        whiteCellFont.setColor(HSSFColor.WHITE.index);
        
        XSSFCellStyle blueCellStyle = workbook.createCellStyle();
        blueCellStyle.setFont(whiteCellFont);
        blueCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        blueCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        blueCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        blueCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        blueCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        blueCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        blueCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(132, 151, 176)));
        blueCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        blueCellStyle.setDataFormat(format.getFormat("#,##0"));

        XSSFCellStyle skyBlueCellStyle = workbook.createCellStyle();
        skyBlueCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        skyBlueCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        skyBlueCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        skyBlueCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        skyBlueCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        skyBlueCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        skyBlueCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(189, 215, 238)));
        skyBlueCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        skyBlueCellStyle.setDataFormat(format.getFormat("#,##0"));

        XSSFCellStyle blackPercentageCellStyle = workbook.createCellStyle();
        blackPercentageCellStyle.setFont(whiteCellFont);
        blackPercentageCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        blackPercentageCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        blackPercentageCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        blackPercentageCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        blackPercentageCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        blackPercentageCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        blackPercentageCellStyle.setDataFormat(format.getFormat("#,##0.00%"));
        blackPercentageCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 0, 0)));
        blackPercentageCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFRow dataRow = null;
        Cell dataCell = null;

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        int numCol = 0;

        String query = "exec [sp_lead_performance] " + campaignId;
        int row = 5;
        int cell = 0;
        int firstRow = row + 1;
        int lastRow = 0;

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();

            if (rs.next()) {
                setHeader(sheet, dataRow, dataCell, brownHeadStyle, blueHeadStyle, blackHeadStyle);

                do {
                    dataRow = sheet.createRow(row++);
                    cell = 0;
                    for (int i = 1; i <= numCol; i++) {
                        sheet.setColumnWidth(cell, 3000);
                        dataCell = dataRow.createCell(cell++);
                        if (i == 1 || i == 3 || i == 4) {
                            dataCell.setCellValue(rs.getString(i));
                            if (i == 1) {
                                dataCell.setCellStyle(listCellStyle);
                            } else {
                                dataCell.setCellStyle(dataCellStyle);
                            }
                            dataCell.setCellType(Cell.CELL_TYPE_STRING);
                        } else {
                            if (i == 8 || i == 9 || i == 10 || i == 17) {
                                dataCell.setCellValue(rs.getDouble(i));
                                if (i == 17) {
                                    dataCell.setCellStyle(blackPercentageCellStyle);
                                } else {
                                    dataCell.setCellStyle(percentageCellStyle);
                                }
                            } else {
                                dataCell.setCellValue(rs.getInt(i));
                                if (i == 11 || i == 12 || i == 13) {
                                    dataCell.setCellStyle(blueCellStyle);
                                } else if (i == 14 || i == 15 || i == 16) {
                                    dataCell.setCellStyle(skyBlueCellStyle);
                                } else {
                                    dataCell.setCellStyle(dataCellStyle);
                                }
                            }
                            dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        }
                        lastRow = row;
                    }
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
        cell = 0;
        dataRow = sheet.createRow(row++);
        for (int i = 0; i < numCol; i++) {
            if (i == 0 || i == 2 || i == 3) {
                dataCell = dataRow.createCell(cell++);
                dataCell.setCellValue("");
                dataCell.setCellStyle(summaryCellStyle);
                dataCell.setCellType(Cell.CELL_TYPE_STRING);
            } else if (i == 7) {
                dataCell = dataRow.createCell(cell++);
                dataCell.setCellFormula(String.format("G%d/E%d", row, row));
                dataCell.setCellStyle(summaryPercentageCellStyle);
                dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
            } else if (i == 8) {
                dataCell = dataRow.createCell(cell++);
                dataCell.setCellFormula(String.format("K%d/E%d", row, row));
                dataCell.setCellStyle(summaryPercentageCellStyle);
                dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
            } else if (i == 9) {
                dataCell = dataRow.createCell(cell++);
                dataCell.setCellFormula(String.format("K%d/G%d", row, row));
                dataCell.setCellStyle(summaryPercentageCellStyle);
                dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
            } else if (i == 12) {
                dataCell = dataRow.createCell(cell++);
                dataCell.setCellFormula(String.format("L%d/K%d", row, row));
                dataCell.setCellStyle(summaryDataCellStyle);
                dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
            } else if (i == 15) {
                dataCell = dataRow.createCell(cell++);
                dataCell.setCellFormula(String.format("O%d/N%d", row, row));
                dataCell.setCellStyle(summaryDataCellStyle);
                dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
            } else if (i == 16) {
                dataCell = dataRow.createCell(cell++);
                dataCell.setCellFormula(String.format("N%d/K%d", row, row));
                dataCell.setCellStyle(summaryPercentageCellStyle);
                dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
            } else {
                dataCell = dataRow.createCell(cell++);
                dataCell.setCellFormula(String.format("SUM(%s%d:%s%d)",
                        CellReference.convertNumToColString(i), firstRow, CellReference.convertNumToColString(i), lastRow));
                dataCell.setCellStyle(summaryDataCellStyle);
                dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
            }
        }
        sheet.autoSizeColumn(0);
    }

    public void setHeader(XSSFSheet sheet, XSSFRow dataRow, Cell dataCell, XSSFCellStyle brownHeadStyle, XSSFCellStyle blueHeadStyle, XSSFCellStyle blackHeadStyle) {
        int cell = 0;
        dataRow = sheet.createRow(4);
        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("Source list");
        dataCell.setCellStyle(brownHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("Database");
        dataCell.setCellStyle(brownHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("Started Date ");
        dataCell.setCellStyle(brownHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("Expired Date");
        dataCell.setCellStyle(brownHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("Total Used");
        dataCell.setCellStyle(brownHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("SYS Disp");
        dataCell.setCellStyle(brownHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("Contactable");
        dataCell.setCellStyle(brownHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("Contact%");
        dataCell.setCellStyle(brownHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("RR1%");
        dataCell.setCellStyle(brownHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("RR2%");
        dataCell.setCellStyle(brownHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("App");
        dataCell.setCellStyle(blueHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("AFYP");
        dataCell.setCellStyle(blueHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("AARP");
        dataCell.setCellStyle(blueHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("App");
        dataCell.setCellStyle(brownHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("AFYP");
        dataCell.setCellStyle(brownHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("AARP");
        dataCell.setCellStyle(brownHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue("Passing %");
        dataCell.setCellStyle(blackHeadStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);
    }

    public void reportListener(ActionEvent event) {
        generateXLS();
        FacesContext.getCurrentInstance().responseComplete();
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

}
