/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.MarketingDAO;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.SecurityUtil;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFPalette;
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
 * @author Thanapornchai
 */
@ManagedBean
@RequestScoped
public class TelesalesDailyReportByProduct {

    private static Logger log = Logger.getLogger(TelesalesDailyReportByInsurer.class);

    private Integer campaignId;
    private String campaignName = "All";
    private Integer marketingId;
    private String marketingName = "All";
    private Date fromDate;
    private Date toDate;
    private Integer userGroupId;
    private String groupName = "All";
    private Integer userId;
    private String userName = "All";
    private Map<String, Integer> usersList;

    private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:telesalesdailyreportbyproduct:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        fromDate = new Date();
        toDate = new Date();
    }

    private void generateXLS() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
        String date = df.format(new Date());
        String fileName = "TelesalesDailyReportByProduct_" + date + ".xlsx";

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
        trow.createCell(0).setCellValue("Telesales Daily Report By Product");
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
        trow.createCell(0).setCellValue("User Group : " + this.groupName);
        trow.getCell(0).setCellStyle(titleStyle);

        trow = sheet.createRow(6);
        trow.createCell(0).setCellValue("User: " + this.userName);
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
        DataFormat format = workbook.createDataFormat();

        XSSFCellStyle headCellStyle = workbook.createCellStyle();
        XSSFFont headFont = workbook.createFont();
        headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headCellStyle.setFont(headFont);
        headCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        headCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        headCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        headCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle totalCellStyle = workbook.createCellStyle();
        totalCellStyle.setFont(headFont);
        totalCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        totalCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        totalCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        totalCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        totalCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        totalCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        totalCellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        totalCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle pinkHeadStyle = workbook.createCellStyle();
        pinkHeadStyle.setFont(headFont);
        pinkHeadStyle.setBorderTop(CellStyle.BORDER_THIN);
        pinkHeadStyle.setBorderRight(CellStyle.BORDER_THIN);
        pinkHeadStyle.setBorderLeft(CellStyle.BORDER_THIN);
        pinkHeadStyle.setBorderBottom(CellStyle.BORDER_THIN);
        pinkHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
        pinkHeadStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        pinkHeadStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 153, 204)));
        pinkHeadStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle pinkCellStyle = workbook.createCellStyle();
        pinkCellStyle.setFont(headFont);
        pinkCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        pinkCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        pinkCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        pinkCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        pinkCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        pinkCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        pinkCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 153, 204)));
        pinkCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle detailPinkCellStyle = workbook.createCellStyle();
        detailPinkCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailPinkCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailPinkCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailPinkCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailPinkCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailPinkCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        detailPinkCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 153, 204)));
        detailPinkCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        XSSFCellStyle decimalPinkCellStyle = workbook.createCellStyle();
        decimalPinkCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        decimalPinkCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        decimalPinkCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        decimalPinkCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        decimalPinkCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        decimalPinkCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        decimalPinkCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 153, 204)));
        decimalPinkCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        decimalPinkCellStyle.setDataFormat(format.getFormat("#,##0.00"));

        XSSFCellStyle percentagePinkCellStyle = workbook.createCellStyle();
        percentagePinkCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        percentagePinkCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        percentagePinkCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        percentagePinkCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        percentagePinkCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        percentagePinkCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        percentagePinkCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 153, 204)));
        percentagePinkCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        percentagePinkCellStyle.setDataFormat(format.getFormat("#,##0.00%"));

        XSSFRow dataRow = null;
        Cell dataCell = null;

        XSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        dateCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        dateCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        dateCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        dateCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        dateCellStyle.setBorderLeft(CellStyle.BORDER_THIN);

        XSSFCellStyle detailCellStyle = workbook.createCellStyle();
        detailCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderLeft(CellStyle.BORDER_THIN);

        XSSFCellStyle decimalCellStyle = workbook.createCellStyle();
        decimalCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        decimalCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        decimalCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        decimalCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        decimalCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        decimalCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        decimalCellStyle.setDataFormat(format.getFormat("#,##0.00"));

        XSSFCellStyle percentageCellStyle = workbook.createCellStyle();
        percentageCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        percentageCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        percentageCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        percentageCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        percentageCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        percentageCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        percentageCellStyle.setDataFormat(format.getFormat("#,##0.00%"));

        XSSFCellStyle summaryCellStyle = workbook.createCellStyle();
        summaryCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        summaryCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        summaryCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        summaryCellStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        summaryCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        XSSFCellStyle greenHeadStyle = workbook.createCellStyle();
        greenHeadStyle.setFont(headFont);
        greenHeadStyle.setBorderTop(CellStyle.BORDER_THIN);
        greenHeadStyle.setBorderRight(CellStyle.BORDER_THIN);
        greenHeadStyle.setBorderLeft(CellStyle.BORDER_THIN);
        greenHeadStyle.setBorderBottom(CellStyle.BORDER_THIN);
        greenHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
        greenHeadStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        greenHeadStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        greenHeadStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        int numCol;
        int cell1 = 0;
        int cell2 = 0;

        XSSFRow headRow1 = sheet.createRow(8);
        XSSFRow headRow2 = sheet.createRow(9);
        Cell headCelll = headRow1.createCell(cell1++);
        Cell headCell2 = headRow2.createCell(cell2++);

        headCelll.setCellValue("Date");
        headCelll.setCellStyle(pinkHeadStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2.setCellValue("");
        headCell2.setCellStyle(pinkCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.addMergedRegion(new CellRangeAddress(8, 9, 0, 0));

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("LEAD USE");
        headCelll.setCellStyle(greenHeadStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("Total Lead Use");
        headCell2.setCellStyle(headCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.autoSizeColumn(cell2 - 1);

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("");
        headCelll.setCellStyle(greenHeadStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("NEW Lead Use");
        headCell2.setCellStyle(headCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.autoSizeColumn(cell2 - 1);

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("");
        headCelll.setCellStyle(greenHeadStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("Old Lead Use");
        headCell2.setCellStyle(headCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.addMergedRegion(new CellRangeAddress(8, 8, 1, 3));
        sheet.autoSizeColumn(cell2 - 1);

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("CONTACTABLE");
        headCelll.setCellStyle(greenHeadStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("Case");
        headCell2.setCellStyle(headCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("");
        headCelll.setCellStyle(pinkHeadStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("%");
        headCell2.setCellStyle(headCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.addMergedRegion(new CellRangeAddress(8, 8, 4, 5));

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("#Success");
        headCelll.setCellStyle(greenHeadStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("Total Yes");
        headCell2.setCellStyle(headCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.autoSizeColumn(cell2 - 1);

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("");
        headCelll.setCellStyle(greenHeadStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("Yes - New Lead");
        headCell2.setCellStyle(headCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.autoSizeColumn(cell2 - 1);

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("");
        headCelll.setCellStyle(greenHeadStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("Yes - Old Lead");
        headCell2.setCellStyle(headCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.autoSizeColumn(cell2 - 1);
        sheet.addMergedRegion(new CellRangeAddress(8, 8, 6, 8));

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("% Response on contable");
        headCelll.setCellStyle(greenHeadStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("");
        headCell2.setCellStyle(greenHeadStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.autoSizeColumn(cell1 - 1);
        sheet.addMergedRegion(new CellRangeAddress(8, 9, cell2 - 1, cell2 - 1));

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("% List Conversion (New)");
        headCelll.setCellStyle(greenHeadStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("");
        headCell2.setCellStyle(greenHeadStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.autoSizeColumn(cell1 - 1);
        sheet.addMergedRegion(new CellRangeAddress(8, 9, cell2 - 1, cell2 - 1));

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("AARP");
        headCelll.setCellStyle(greenHeadStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("");
        headCell2.setCellStyle(greenHeadStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.addMergedRegion(new CellRangeAddress(8, 9, cell2 - 1, cell2 - 1));

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("Total Premium");
        headCelll.setCellStyle(greenHeadStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("");
        headCell2.setCellStyle(greenHeadStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.autoSizeColumn(cell1 - 1);
        sheet.addMergedRegion(new CellRangeAddress(8, 9, cell2 - 1, cell2 - 1));

        int premiumHeadStart = 0;
        int premiumHeadStop = 0;
        List<String> head = new ArrayList<>();

        Map<String, String> mClass = new HashMap<>();
        mClass.put("01", "Class1");
        mClass.put("02", "Class2+");
        mClass.put("03", "Class3+");
        mClass.put("04", "Class3");
        mClass.put("05", "CMI(Car)");

        for (int i = 1; i <= 5; i++) {
            String txt = "0" + Integer.toString(i);
            if (i == 1) {
                if (premiumHeadStart == 0 && premiumHeadStop == 0) {
                    premiumHeadStart = cell1;
                    premiumHeadStop = cell1;
                } else {
                    premiumHeadStop++;
                }
                head.add(mClass.get(txt));

                headCelll = headRow1.createCell(cell1++);
                headCelll.setCellValue("Yes case by product");
                headCelll.setCellStyle(greenHeadStyle);
                headCelll.setCellType(Cell.CELL_TYPE_STRING);

                headCell2 = headRow2.createCell(cell2++);
                headCell2.setCellValue(mClass.get(txt));
                headCell2.setCellStyle(headCellStyle);
                headCell2.setCellType(Cell.CELL_TYPE_STRING);
            } else {
                premiumHeadStop++;
                head.add(mClass.get(txt));

                headCelll = headRow1.createCell(cell1++);
                headCelll.setCellValue("");
                headCelll.setCellStyle(greenHeadStyle);
                headCelll.setCellType(Cell.CELL_TYPE_STRING);

                headCell2 = headRow2.createCell(cell2++);
                headCell2.setCellValue(mClass.get(txt));
                headCell2.setCellStyle(headCellStyle);
                headCell2.setCellType(Cell.CELL_TYPE_STRING);
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(8, 8, premiumHeadStart, premiumHeadStop));
        List<String> payment = new ArrayList<>();
        payment.add("F-CC");
        payment.add("F-DC");
        payment.add("F-CASH");
        payment.add("I-CC");
        payment.add("I-CASH");

        for (int i = 0; i < 4; i++) {
            if (i == 0 || i == 3) {
                for (int j = 0; j < payment.size(); j++) {
                    if (j == 0) {
                        premiumHeadStart = cell1;
                    }
                    headCelll = headRow1.createCell(cell1++);
                    if (i == 0) {
                        headCelll.setCellValue("Yes case by payment type");
                    } else if (i == 3) {
                        headCelll.setCellValue("Premium (after discount) by payment type");
                    }
                    headCelll.setCellStyle(greenHeadStyle);
                    headCelll.setCellType(Cell.CELL_TYPE_STRING);

                    headCell2 = headRow2.createCell(cell2++);
                    headCell2.setCellValue(payment.get(j));
                    headCell2.setCellStyle(headCellStyle);
                    headCell2.setCellType(Cell.CELL_TYPE_STRING);
                }
            } else {
                for (int j = 0; j < head.size(); j++) {
                    if (j == 0) {
                        premiumHeadStart = cell1;
                    }
                    headCelll = headRow1.createCell(cell1++);
                    if(i == 2){
                        headCelll.setCellValue("Premium by product (after discount)");
//                        headCelll.setCellValue("Premium by product");
                    } else {
                        headCelll.setCellValue("Premium by product");
//                        headCelll.setCellValue("Premium by product (after discount)");
                    }
                   
                    headCelll.setCellStyle(headCellStyle);
                    headCelll.setCellType(Cell.CELL_TYPE_STRING);

                    headCell2 = headRow2.createCell(cell2++);
                    headCell2.setCellValue(head.get(j));
                    headCell2.setCellStyle(headCellStyle);
                    headCell2.setCellType(Cell.CELL_TYPE_STRING);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(8, 8, premiumHeadStart, cell1 - 1));
        }

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("Collected Case");
        headCelll.setCellStyle(headCellStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("");
        headCell2.setCellStyle(headCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.autoSizeColumn(cell1 - 1);
        sheet.addMergedRegion(new CellRangeAddress(8, 9, cell2 - 1, cell2 - 1));

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("Collected Premium");
        headCelll.setCellStyle(headCellStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("");
        headCell2.setCellStyle(headCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.autoSizeColumn(cell1 - 1);
        sheet.addMergedRegion(new CellRangeAddress(8, 9, cell2 - 1, cell2 - 1));

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("% Collection Case");
        headCelll.setCellStyle(headCellStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("");
        headCell2.setCellStyle(headCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.autoSizeColumn(cell1 - 1);
        sheet.addMergedRegion(new CellRangeAddress(8, 9, cell2 - 1, cell2 - 1));

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("% Collection Premium");
        headCelll.setCellStyle(headCellStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("");
        headCell2.setCellStyle(headCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.autoSizeColumn(cell1 - 1);
        sheet.addMergedRegion(new CellRangeAddress(8, 9, cell2 - 1, cell2 - 1));

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("TSRs");
        headCelll.setCellStyle(headCellStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("");
        headCell2.setCellStyle(headCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.addMergedRegion(new CellRangeAddress(8, 9, cell2 - 1, cell2 - 1));

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("Call Attemp");
        headCelll.setCellStyle(pinkHeadStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("");
        headCell2.setCellStyle(pinkHeadStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.autoSizeColumn(cell1 - 1);
        sheet.addMergedRegion(new CellRangeAddress(8, 9, cell2 - 1, cell2 - 1));

        headCelll = headRow1.createCell(cell1++);
        headCelll.setCellValue("Working Day");
        headCelll.setCellStyle(headCellStyle);
        headCelll.setCellType(Cell.CELL_TYPE_STRING);

        headCell2 = headRow2.createCell(cell2++);
        headCell2.setCellValue("");
        headCell2.setCellStyle(headCellStyle);
        headCell2.setCellType(Cell.CELL_TYPE_STRING);
        sheet.autoSizeColumn(cell1 - 1);
        sheet.addMergedRegion(new CellRangeAddress(8, 9, cell2 - 1, cell2 - 1));

        //Set Data
        String query = "exec [sp_telesales_daily_report_by_product] '" + sdf1.format(fromDate) + " 00:00:00','" + sdf1.format(toDate) + " 23:59:59','"
                + campaignId + "','" + marketingId + "','" + userGroupId + "','" + userId + "'";

        System.out.println(query);
        int row = 0;
        int lastCell = 0;
        List<Integer> ytd = new ArrayList<>();
        int lastRow = 0;
        int firstRow = 0;
        int calRow = 0;
        String oldMonth = "";
        int cell = 0;

        Map<String, String> month = new HashMap<>();
        month.put("Jan", "January");
        month.put("Feb", "February");
        month.put("Mar", "March");
        month.put("Apr", "April");
        month.put("May", "May");
        month.put("Jun", "June");
        month.put("Jul", "July");
        month.put("Aug", "August");
        month.put("Sep", "September");
        month.put("Oct", "October");
        month.put("Nov", "November");
        month.put("Dec", "December");

        String tsrs = "";

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            row = 10;

            if (rs.next()) {
                do {
                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            String[] newMonth = rs.getString(i).split(" ");
                            if ("".equals(oldMonth)) {
                                String[] monthArr = rs.getString(i).split(" ");
                                oldMonth = monthArr[1];
                            } else if (!oldMonth.equals(newMonth[1])) {
                                dataRow = sheet.createRow(row++);
                                dataCell = dataRow.createCell(cell++);
                                dataCell.setCellValue(month.get(oldMonth));
                                dataCell.setCellStyle(pinkCellStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_STRING);

                                for (int j = 1; j < lastCell; j++) {
                                    dataCell = dataRow.createCell(cell++);
                                    String formula = String.format("%s%d:%s%d", CellReference.convertNumToColString(j), firstRow,
                                            CellReference.convertNumToColString(j), lastRow);
                                    if (j == 42) {
                                        tsrs += formula + ",";
                                    }
                                    if (j == 5 || j == 9 || j == 10 || j == 40 || j == 41) {
                                        if (j == 5) {
                                            dataCell.setCellFormula(String.format("IF(C%d>0,E%d/C%d,0)", row, row, row));
                                        } else if (j == 9) {
                                            dataCell.setCellFormula(String.format("IF(E%d>0,G%d/E%d,0)", row, row, row));
                                        } else if (j == 10) {
                                            dataCell.setCellFormula(String.format("IF(C%d>0,G%d/C%d,0)", row, row, row));
                                        } else if (j == 40) {
                                            dataCell.setCellFormula(String.format("IF(G%d>0,AM%d/G%d,0)", row, row, row));
                                        } else if (j == 41) {
                                            dataCell.setCellFormula(String.format("IF(M%d>0,AN%d/M%d,0)", row, row, row));
                                        }
                                        dataCell.setCellStyle(percentagePinkCellStyle);
                                    } else if ((j >= 23 && j <= 37)) {
                                        dataCell.setCellFormula("SUM(" + formula + ")");
                                        dataCell.setCellStyle(decimalPinkCellStyle);
                                    } else {
                                        dataCell.setCellFormula("SUM(" + formula + ")");
                                        dataCell.setCellStyle(detailPinkCellStyle);
                                    }
                                    dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
                                }
                                ytd.add(row);
                                oldMonth = newMonth[1];
                                cell = 0;
                                firstRow = 0;
                            }
                            dataRow = sheet.createRow(row++);
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellValue(rs.getString(i));
                            dataCell.setCellStyle(dateCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_STRING);

                            if (firstRow == 0) {
                                firstRow = row;
                                calRow = row;
                            }
                        } else if (i == 6 || i == 10 || i == 11 || (i >= 24 && i <= 38) || i == 41 || i == 42) {
                            if (dataRow != null) {
                                dataCell = dataRow.createCell(cell++);
                                dataCell.setCellValue(rs.getDouble(i));
                                if (i == 6 || i == 10 || i == 11 || i == 41 || i == 42) {
                                    dataCell.setCellStyle(percentageCellStyle);
                                } else {
                                    dataCell.setCellStyle(decimalCellStyle);
                                }
                                dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            }
                        } else {
                            if (dataRow != null) {
                                dataCell = dataRow.createCell(cell++);
                                dataCell.setCellValue(rs.getInt(i));
                                dataCell.setCellStyle(detailCellStyle);
                                dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            }
                        }
                        lastRow = row;
                        lastCell = cell;
                    }
                    cell = 0;
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
        //Last Month
        dataRow = sheet.createRow(row++);
        dataCell = dataRow.createCell(cell++);
        dataCell.setCellValue(month.get(oldMonth));
        dataCell.setCellStyle(pinkCellStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);

        for (int i = 1; i < lastCell; i++) {
            dataCell = dataRow.createCell(cell++);
            String formula = String.format("%s%d:%s%d", CellReference.convertNumToColString(i), calRow,
                    CellReference.convertNumToColString(i), lastRow);
            if (i == 5) {
                dataCell.setCellFormula(String.format("IF(C%d>0,E%d/C%d,0)", row, row, row));
            } else if (i == 9) {
                dataCell.setCellFormula(String.format("IF(E%d>0,G%d/E%d,0)", row, row, row));
            } else if (i == 10) {
                dataCell.setCellFormula(String.format("IF(C%d>0,G%d/C%d,0)", row, row, row));
            } else if (i == 40) {
                dataCell.setCellFormula(String.format("IF(G%d>0,AM%d/G%d,0)", row, row, row));
            } else if (i == 41) {
                dataCell.setCellFormula(String.format("IF(M%d>0,AN%d/M%d,0)", row, row, row));
            } else if (i == 42) {
                tsrs += formula + ",";
                dataCell.setCellFormula("SUM(" + formula + ")");
            } else {
                dataCell.setCellFormula("SUM(" + formula + ")");
            }
            if (i == 5 || i == 9 || i == 10 || i == 40 || i == 41) {
                dataCell.setCellStyle(percentagePinkCellStyle);
            } else if ((i >= 23 && i <= 37)) {
                dataCell.setCellStyle(decimalPinkCellStyle);
            } else {
                dataCell.setCellStyle(detailPinkCellStyle);
            }
            dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
        }
        ytd.add(row);

        // YTD
        String ytdString = "";
        dataRow = sheet.createRow(row++);
        dataCell = dataRow.createCell(0);
        dataCell.setCellValue("YTD");
        dataCell.setCellStyle(pinkCellStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);
        for (int i = 1; i < lastCell; i++) {
            dataCell = dataRow.createCell(i);
            if (i == 5) {
                dataCell.setCellFormula(String.format("IF(C%d>0,E%d/C%d,0)", row, row, row));
            } else if (i == 9) {
                dataCell.setCellFormula(String.format("IF(E%d>0,G%d/E%d,0)", row, row, row));
            } else if (i == 10) {
                dataCell.setCellFormula(String.format("IF(C%d>0,G%d/C%d,0)", row, row, row));
            } else if (i == 40) {
                dataCell.setCellFormula(String.format("IF(G%d>0,AM%d/G%d,0)", row, row, row));
            } else if (i == 41) {
                dataCell.setCellFormula(String.format("IF(M%d>0,AN%d/M%d,0)", row, row, row));
            } else if (i == 42) {
                tsrs = tsrs.substring(0, tsrs.length() - 1);
                dataCell.setCellFormula("SUM(" + tsrs + ")/AS" + row);
            } else {
                ytdString = "";
                for (int j = 0; j < ytd.size(); j++) {
                    ytdString += String.format("%s%d", CellReference.convertNumToColString(i), ytd.get(j)) + ",";
                }
                ytdString = ytdString.substring(0, ytdString.length() - 1);

                dataCell.setCellFormula("SUM(" + ytdString + ")");
            }
            if (i == 5 || i == 9 || i == 10 || i == 40 || i == 41) {
                dataCell.setCellStyle(percentagePinkCellStyle);
            } else if ((i >= 23 && i <= 37) || i == 42) {
                dataCell.setCellStyle(decimalPinkCellStyle);
            } else {
                dataCell.setCellStyle(detailPinkCellStyle);
            }
            dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
        this.userName = "All";

        Users u = this.usersDAO.findUsers(userId);
        if (u != null) {
            userName = u.getName() + " " + u.getSurname();
        }
    }

    public void userGroupListener(ValueChangeEvent event) {
        userGroupId = (Integer) event.getNewValue();
        setUsersList(userGroupId);
    }

    public Map<String, Integer> getUsergroupList() {
        return this.userGroupDAO.getUserGroupList();
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
        this.groupName = "All";
        try {
            if (userGroupId != null && userGroupId != 0) {
                groupName = this.userGroupDAO.findUserGroup(userGroupId).getName();
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUsersList(Map<String, Integer> usersList) {
        this.usersList = usersList;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public void setUsersList(Integer userGroupId) {
        if (userGroupId == 0) {
            this.usersList = this.getUsersDAO().getUsersList();
        } else {
            this.usersList = this.getUsersDAO().getUsersListByUserGroup(userGroupId);
        }
    }

    public Map<String, Integer> getUsersList() {
        if (usersList == null) {
            setUsersList(0);
        }
        return usersList;
    }

}
