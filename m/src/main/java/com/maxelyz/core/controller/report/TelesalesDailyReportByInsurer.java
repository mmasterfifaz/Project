/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.MarketingDAO;
import com.maxelyz.core.model.dao.RegistrationFieldDAO;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.RegistrationField;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.SecurityUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import javax.faces.event.ValueChangeEvent;
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
 * @author Thanapornchai
 */
@ManagedBean
@RequestScoped
public class TelesalesDailyReportByInsurer {

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
    @ManagedProperty(value = "#{registrationFieldDAO}")
    private RegistrationFieldDAO registrationFieldDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:telesalesdailyreportbyinsurer:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        fromDate = new Date();
        toDate = new Date();
    }

    public String getQuery(int type) {
        String query = "";
        if (type == 1) {
//            query = "select distinct por.fx44, por.fx46 from purchase_order po "
//                    + " join purchase_order_detail pod on pod.purchase_order_id = po.id"
//                    + " join purchase_order_register por on por.purchase_order_detail_id = pod.id"
//                    + " join assignment_detail ad on ad.id = po.assignment_detail_id"
//                    + " join assignment a on a.id = ad.assignment_id"
//                    + " join users u on u.id = ad.user_id"
//                    + " where por.fx44 is not null and por.fx46 is not null and por.fx44 != '' and por.fx46 != ''"
//                    + " and po.sale_date BETWEEN '" + sdf1.format(fromDate) + " 00:00:00' AND '" + sdf1.format(toDate) + " 23:59:59' AND po.sale_result = 'Y'"
//                    + " AND CASE WHEN " + campaignId + " = 0 THEN 0 ELSE a.campaign_id END = " + campaignId
//                    + " AND CASE WHEN " + marketingId + " = 0 THEN 0 ELSE a.marketing_id END = " + marketingId
//                    + " AND CASE WHEN " + userGroupId + " = 0 THEN 0 ELSE u.user_group_id END = " + userGroupId
//                    + " AND CASE WHEN " + userId + " = 0 THEN 0 ELSE u.id END = " + userId;

            query = "select distinct por.fx44 from purchase_order po "
                    + " join purchase_order_detail pod on pod.purchase_order_id = po.id"
                    + " join purchase_order_register por on por.purchase_order_detail_id = pod.id"
                    + " join assignment_detail ad on ad.id = po.assignment_detail_id"
                    + " join assignment a on a.id = ad.assignment_id"
                    + " join users u on u.id = ad.user_id"
                    + " where por.fx44 is not null and por.fx46 is not null and por.fx44 != '' and por.fx46 != ''"
                    + " and po.purchase_date BETWEEN '" + sdf1.format(fromDate) + " 00:00:00' AND '" + sdf1.format(toDate) + " 23:59:59' AND po.sale_result = 'Y'"
                    + " AND CASE WHEN " + campaignId + " = 0 THEN 0 ELSE a.campaign_id END = " + campaignId
                    + " AND CASE WHEN " + marketingId + " = 0 THEN 0 ELSE a.marketing_id END = " + marketingId
                    + " AND CASE WHEN " + userGroupId + " = 0 THEN 0 ELSE u.user_group_id END = " + userGroupId
                    + " AND CASE WHEN " + userId + " = 0 THEN 0 ELSE u.id END = " + userId;
        }
        return query;
    }

    //Sort Map by Values
    public static LinkedHashMap sortHashMapByValues(Map passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap someMap = new LinkedHashMap();
        Iterator valueIt = mapValues.iterator();

        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();
            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                if (passedMap.get(key).toString().equals(val.toString())) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    someMap.put(key, val);
                    break;
                }
            }
        }
        return someMap;
    }

    private void generateXLS() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
        String date = df.format(new Date());
        String fileName = "TelesalesDailyReportByInsurer_" + date + ".xlsx";

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
        trow.createCell(0).setCellValue("Telesales Daily Report By Insurer");
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

    public void retrieveData(XSSFWorkbook workbook, XSSFSheet sheet) throws SQLException, ParseException {
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

        XSSFRow dataRow = null;
        Cell dataCell;

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

        XSSFCellStyle summaryCellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        summaryCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        summaryCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        summaryCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        summaryCellStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        summaryCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        int numCol;
        int lastCell = 0;
        List<String> headCode = new ArrayList<>();
        //Set Header
        try {
            connection = dataSource.getConnection();

            statement = connection.createStatement();
            rs = statement.executeQuery(this.getQuery(1));
            ResultSetMetaData rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            dataRow = sheet.createRow(8);
            int cell1 = 0;
            int cell2 = 0;
            int cell3 = 0;
            int hStart = 0;
            int hStop = 0;
            int hStart2 = 0;
            int hStop2 = 0;

            XSSFRow headRow1 = sheet.createRow(8);
            XSSFRow headRow2 = sheet.createRow(9);
            XSSFRow headRow3 = sheet.createRow(10);
            Cell headCelll = headRow1.createCell(cell1++);
            Cell headCell2 = headRow2.createCell(cell2++);
            Cell headCell3 = headRow3.createCell(cell3++);

            headCelll.setCellValue("Date");
            headCelll.setCellStyle(pinkHeadStyle);
            headCelll.setCellType(Cell.CELL_TYPE_STRING);

            headCell2.setCellValue("");
            headCell2.setCellStyle(pinkCellStyle);
            headCell2.setCellType(Cell.CELL_TYPE_STRING);

            headCell3.setCellValue("");
            headCell3.setCellStyle(pinkCellStyle);
            headCell3.setCellType(Cell.CELL_TYPE_STRING);

            sheet.addMergedRegion(new CellRangeAddress(8, 10, 0, 0));
            hStart = cell1;

            List<RegistrationField> rf = registrationFieldDAO.findRegistrationFieldByFromIdAndName("fx44");
            String fx = rf.get(0).getItems();
            Map<String, String> company = new HashMap<>();
            String[] spilt = fx.split(",");
            for (String datas : spilt) {
                String[] data = datas.split(":");
                company.put(data[0], data[1]);
            }

            Map<String, String> mClass = new HashMap<>();
            mClass.put("01", "Class1");
            mClass.put("02", "Class2+");
            mClass.put("03", "Class3+");
            mClass.put("04", "Class3");
            mClass.put("05", "CMI(Car)");

            List<String> classCode = new ArrayList<>();
            classCode.add("01");
            classCode.add("02");
            classCode.add("03");
            classCode.add("04");
            classCode.add("05");

            List<String> fx44 = new ArrayList<>();
            List<String> fx46 = new ArrayList<>();
            Map<String, String> qCompany = new HashMap<>();

            if (rs.next()) {
                do {
                    for (int i = 1; i <= numCol; i++) {
                        qCompany.put(rs.getString(i), company.get(rs.getString(i)));
                    }
                } while (rs.next());
            }
            LinkedHashMap<String, String> companySort = sortHashMapByValues(qCompany);
            int i = 0;
            for (String key : companySort.keySet()) {
                hStart = cell1;
                fx44.add(key);
                i++;

                headCelll = headRow1.createCell(cell1++);
                headCelll.setCellValue(company.get(key));
                headCelll.setCellStyle(headCellStyle);
                headCelll.setCellType(Cell.CELL_TYPE_STRING);

                headCelll = headRow1.createCell(cell1++);
                headCelll.setCellValue("");
                headCelll.setCellStyle(headCellStyle);
                headCelll.setCellType(Cell.CELL_TYPE_STRING);

                for (int j = 0; j < classCode.size(); j++) {
                    headCelll = headRow1.createCell(cell1++);
                    headCelll.setCellValue("");
                    headCelll.setCellStyle(headCellStyle);
                    headCelll.setCellType(Cell.CELL_TYPE_STRING);

                    headCelll = headRow1.createCell(cell1++);
                    headCelll.setCellValue("");
                    headCelll.setCellStyle(headCellStyle);
                    headCelll.setCellType(Cell.CELL_TYPE_STRING);

                    hStart2 = cell2;
                    fx46.add(classCode.get(j));
                    headCell2 = headRow2.createCell(cell2++);
                    headCell2.setCellValue(mClass.get(classCode.get(j)));
                    headCell2.setCellStyle(totalCellStyle);
                    headCell2.setCellType(Cell.CELL_TYPE_STRING);

                    hStop2 = cell2;
                    headCell2 = headRow2.createCell(cell2++);
                    headCell2.setCellValue("");
                    headCell2.setCellStyle(totalCellStyle);
                    headCell2.setCellType(Cell.CELL_TYPE_STRING);
                    sheet.addMergedRegion(new CellRangeAddress(9, 9, hStart2, hStop2));

                    headCell3 = headRow3.createCell(cell3++);
                    headCell3.setCellValue("App");
                    headCell3.setCellStyle(totalCellStyle);
                    headCell3.setCellType(Cell.CELL_TYPE_STRING);

                    headCell3 = headRow3.createCell(cell3++);
                    headCell3.setCellValue("Premium");
                    headCell3.setCellStyle(totalCellStyle);
                    headCell3.setCellType(Cell.CELL_TYPE_STRING);

                    if (j == classCode.size() - 1) {
                        hStop = cell1 - 1;
                        sheet.addMergedRegion(new CellRangeAddress(8, 8, hStart, hStop));

                        hStart2 = cell2;
                        headCell2 = headRow2.createCell(cell2++);
                        headCell2.setCellValue("Total");
                        headCell2.setCellStyle(totalCellStyle);
                        headCell2.setCellType(Cell.CELL_TYPE_STRING);

                        hStop2 = cell2;
                        headCell2 = headRow2.createCell(cell2++);
                        headCell2.setCellValue("");
                        headCell2.setCellStyle(totalCellStyle);
                        headCell2.setCellType(Cell.CELL_TYPE_STRING);
                        sheet.addMergedRegion(new CellRangeAddress(9, 9, hStart2, hStop2));

                        headCell3 = headRow3.createCell(cell3++);
                        headCell3.setCellValue("App");
                        headCell3.setCellStyle(totalCellStyle);
                        headCell3.setCellType(Cell.CELL_TYPE_STRING);

                        headCell3 = headRow3.createCell(cell3++);
                        headCell3.setCellValue("Premium");
                        headCell3.setCellStyle(totalCellStyle);
                        headCell3.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    headCode.add(fx44.get(i - 1) + ":" + fx46.get(j));
                }
                headCode.add("yes");
            }
            lastCell = cell3;

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
        //Set Data
        String query = "exec [sp_telesales_daily_report_by_insurer] '" + sdf1.format(fromDate) + " 00:00:00','" + sdf1.format(toDate) + " 23:59:59','"
                + campaignId + "','" + marketingId + "','" + userGroupId + "','" + userId + "'";

        int row = 11;
        int cell = 0;
        List<String> appCell = null;
        List<String> totalCell = null;
        int firstRow = 0;
        int lastRow = 0;
        String sDate = "";
        String companyCode = "";
        List<Integer> ytd = new ArrayList<>();

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

        String[] monthArr = null;

        String key = "";
        String value = "";
        List<String> code = new ArrayList<>();
        List<String> data = new ArrayList<>();

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();

            if (rs.next()) {
                do {
                    for (int i = 1; i <= numCol; i++) {
                        if (i == 1) {
                            key = rs.getString(i);
                        } else if (value.equals("")) {
                            value += rs.getString(i);
                        } else {
                            value += "," + rs.getString(i);
                        }
                        if (i == numCol) {
                            code.add(key);
                            data.add(value);
                            key = "";
                            value = "";
                        }
                    }
                } while (rs.next());
            }
        } catch (SQLException e) {
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
        List<String> sortData = new ArrayList<>();

        List<String> sortDataCode = new ArrayList<>();
        Map<Date, Integer> sortDate = new TreeMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        int count = 1;

        for (int i = 0; i < headCode.size(); i++) {
            for (int j = 0; j < code.size(); j++) {
                if (headCode.get(i).equals(code.get(j))) {
                    sortDataCode.add(code.get(j) + "," + data.get(j));
                    
                    String[] mapDate = data.get(j).split(",");
                    sortDate.put(sdf.parse(mapDate[0]), count++);
                }
            }
        }
        for (Date date : sortDate.keySet()) {
            for (int i = 0; i < sortDataCode.size(); i++) {
                String[] dataDate = sortDataCode.get(i).split(",");
                if (sdf.format(date).equals(dataDate[1])) {
                    sortData.add(sortDataCode.get(i));
                }
            }
        }
        int cellSum = 0;
        int j = 0;

        try {
            for (int i = 0; i < headCode.size(); i++) {
                String[] sortDataSpilt;
                if (j < sortData.size()) {
                    sortDataSpilt = sortData.get(j).split(",");
                } else {
                    sortDataSpilt = null;
                }
                if (sortDataSpilt != null) {
                    if (sDate.equals("")) {
                        cell = 0;
                        sDate = sortDataSpilt[1];
                        dataRow = sheet.createRow(row++);
                        dataCell = dataRow.createCell(cell++);
                        dataCell.setCellValue(sortDataSpilt[1]);
                        dataCell.setCellStyle(dateCellStyle);
                        cellSum = row;

                        appCell = new ArrayList<>();
                        totalCell = new ArrayList<>();
                        if (firstRow == 0) {
                            firstRow = row;
                        }
                        lastRow = row;
                    } else if (!sDate.equals(sortDataSpilt[1])) {
                        //Check  Last Row = headCode size()
                        if (cell < lastCell) {
                            for (int k = cell; k < lastCell; k += 2) {
                                if (headCode.get(i).equals("yes")) {
                                    String formula = "";
                                    if (appCell != null && !appCell.isEmpty()) {
                                        for (int l = 0; l < appCell.size(); l++) {
                                            formula += appCell.get(l) + ",";
                                        }
                                        formula = formula.substring(0, formula.length() - 1);
                                    }
                                    dataCell = dataRow.createCell(cell++);
                                    dataCell.setCellFormula("SUM(" + formula + ")");
                                    dataCell.setCellStyle(detailCellStyle);
                                    dataCell.setCellType(Cell.CELL_TYPE_FORMULA);

                                    formula = "";
                                    if (totalCell != null && !totalCell.isEmpty()) {
                                        for (int l = 0; l < totalCell.size(); l++) {
                                            formula += totalCell.get(l) + ",";
                                        }
                                        formula = formula.substring(0, formula.length() - 1);
                                    }
                                    dataCell = dataRow.createCell(cell++);
                                    dataCell.setCellFormula("SUM(" + formula + ")");
                                    dataCell.setCellStyle(decimalCellStyle);
                                    dataCell.setCellType(Cell.CELL_TYPE_FORMULA);

                                    appCell = new ArrayList<>();
                                    totalCell = new ArrayList<>();
                                    i++;
                                } else {
                                    String txt = String.format("%s%d", CellReference.convertNumToColString(cell), cellSum);
                                    appCell.add(txt);
                                    int num = Integer.parseInt("0");
                                    dataCell = dataRow.createCell(cell++);
                                    dataCell.setCellValue(num);
                                    dataCell.setCellStyle(detailCellStyle);
                                    dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);

                                    String txt1 = String.format("%s%d", CellReference.convertNumToColString(cell), cellSum);
                                    totalCell.add(txt1);
                                    double num1 = Double.parseDouble("0.00");
                                    dataCell = dataRow.createCell(cell++);
                                    dataCell.setCellValue(num1);
                                    dataCell.setCellStyle(decimalCellStyle);
                                    dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                    i++;
                                }
                            }
                            i = 0;
                        }

                        //Check Month
                        String[] thisMonth = sortDataSpilt[1].split(" ");
                        monthArr = sDate.split(" ");
                        if (!sDate.equals("") && !thisMonth[1].equals(monthArr[1])) {
                            dataRow = sheet.createRow(row++);

                            dataCell = dataRow.createCell(0);
                            dataCell.setCellValue(month.get(monthArr[1]));
                            dataCell.setCellStyle(pinkCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_STRING);
                            for (int k = 1; k < cell; k++) {
                                dataCell = dataRow.createCell(k);
                                dataCell.setCellFormula(String.format("SUM(%s%d:%s%d)",
                                        CellReference.convertNumToColString(k), firstRow, CellReference.convertNumToColString(k), lastRow));
                                if (k % 2 == 0) {
                                    dataCell.setCellStyle(decimalPinkCellStyle);
                                } else {
                                    dataCell.setCellStyle(detailPinkCellStyle);
                                }
                                dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
                            }
                            ytd.add(row);
                            i = 0;
                            firstRow = 0;
                        }

                        cell = 0;
                        sDate = sortDataSpilt[1];
                        dataRow = sheet.createRow(row++);
                        dataCell = dataRow.createCell(cell++);
                        dataCell.setCellValue(sortDataSpilt[1]);
                        dataCell.setCellStyle(dateCellStyle);
                        cellSum = row;

                        appCell = new ArrayList<>();
                        totalCell = new ArrayList<>();
                        if (firstRow == 0) {
                            firstRow = row;
                        }
                        lastRow = row;
                    }

                }
                if (dataRow != null) {
                    if (sortDataSpilt != null) {
                        if (headCode.get(i).equals(sortDataSpilt[0]) && appCell != null && totalCell != null) {
                            String txt = String.format("%s%d", CellReference.convertNumToColString(cell), cellSum);
                            appCell.add(txt);

                            int num = Integer.parseInt(sortDataSpilt[2]);
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellValue(num);
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);

                            String txt1 = String.format("%s%d", CellReference.convertNumToColString(cell), cellSum);
                            totalCell.add(txt1);
                            double num1 = Double.parseDouble(sortDataSpilt[3]);
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellValue(num1);
                            dataCell.setCellStyle(decimalCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            j++;
                        } else if (headCode.get(i).equals("yes")) {
                            String formula = "";
                            if (appCell != null && !appCell.isEmpty()) {
                                for (int k = 0; k < appCell.size(); k++) {
                                    formula += appCell.get(k) + ",";
                                }
                                formula = formula.substring(0, formula.length() - 1);
                            }
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellFormula("SUM(" + formula + ")");
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_FORMULA);

                            formula = "";
                            if (totalCell != null && !totalCell.isEmpty()) {
                                for (int k = 0; k < totalCell.size(); k++) {
                                    formula += totalCell.get(k) + ",";
                                }
                                formula = formula.substring(0, formula.length() - 1);
                            }
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellFormula("SUM(" + formula + ")");
                            dataCell.setCellStyle(decimalCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_FORMULA);

                            appCell = new ArrayList<>();
                            totalCell = new ArrayList<>();
                        } else {
                            String txt = String.format("%s%d", CellReference.convertNumToColString(cell), cellSum);
                            appCell.add(txt);
                            int num = Integer.parseInt("0");
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellValue(num);
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);

                            String txt1 = String.format("%s%d", CellReference.convertNumToColString(cell), cellSum);
                            totalCell.add(txt1);
                            double num1 = Double.parseDouble("0.00");
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellValue(num1);
                            dataCell.setCellStyle(decimalCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        }
                    } else {
                        if (headCode.get(i).equals("yes")) {
                            String formula = "";
                            if (appCell != null && !appCell.isEmpty()) {
                                for (int k = 0; k < appCell.size(); k++) {
                                    formula += appCell.get(k) + ",";
                                }
                                formula = formula.substring(0, formula.length() - 1);
                            }
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellFormula("SUM(" + formula + ")");
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_FORMULA);

                            formula = "";
                            if (totalCell != null && !totalCell.isEmpty()) {
                                for (int k = 0; k < totalCell.size(); k++) {
                                    formula += totalCell.get(k) + ",";
                                }
                                formula = formula.substring(0, formula.length() - 1);
                            }
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellFormula("SUM(" + formula + ")");
                            dataCell.setCellStyle(decimalCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_FORMULA);

                            appCell = new ArrayList<>();
                            totalCell = new ArrayList<>();
                        } else {
                            String txt = String.format("%s%d", CellReference.convertNumToColString(cell), cellSum);
                            appCell.add(txt);
                            int num = Integer.parseInt("0");
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellValue(num);
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);

                            String txt1 = String.format("%s%d", CellReference.convertNumToColString(cell), cellSum);
                            totalCell.add(txt1);
                            double num1 = Double.parseDouble("0.00");
                            dataCell = dataRow.createCell(cell++);
                            dataCell.setCellValue(num1);
                            dataCell.setCellStyle(decimalCellStyle);
                            dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        }
                    }
                }
            }
            //Last Month
            monthArr = sDate.split(" ");
            dataRow = sheet.createRow(row++);

            dataCell = dataRow.createCell(0);
            dataCell.setCellValue(month.get(monthArr[1]));
            dataCell.setCellStyle(pinkCellStyle);
            dataCell.setCellType(Cell.CELL_TYPE_STRING);
            for (int i = 1; i < cell; i++) {
                dataCell = dataRow.createCell(i);
                dataCell.setCellFormula(String.format("SUM(%s%d:%s%d)",
                        CellReference.convertNumToColString(i), firstRow, CellReference.convertNumToColString(i), lastRow));
                if (i % 2 == 0) {
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
            for (int i = 1; i < cell; i++) {
                for (int k = 0; k < ytd.size(); k++) {
                    ytdString += String.format("%s%d", CellReference.convertNumToColString(i), ytd.get(k)) + ",";
                }
                ytdString = ytdString.substring(0, ytdString.length() - 1);

                dataCell = dataRow.createCell(i);
                dataCell.setCellFormula("SUM(" + ytdString + ")");
                ytdString = "";
                if (i % 2 == 0) {
                    dataCell.setCellStyle(decimalPinkCellStyle);
                } else {
                    dataCell.setCellStyle(detailPinkCellStyle);
                }
                dataCell.setCellType(Cell.CELL_TYPE_FORMULA);
            }
        } catch (Exception e) {
            log.error(e);
        }

        sheet.autoSizeColumn(
                0);
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

    public RegistrationFieldDAO getRegistrationFieldDAO() {
        return registrationFieldDAO;
    }

    public void setRegistrationFieldDAO(RegistrationFieldDAO registrationFieldDAO) {
        this.registrationFieldDAO = registrationFieldDAO;
    }

}
