/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.QaCategoryDAO;
import com.maxelyz.core.model.dao.QaTransDAO;
import com.maxelyz.core.model.dao.QaTransDetailDAO;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.QaTrans;
import com.maxelyz.core.model.entity.QaTransDetail;
import com.maxelyz.core.model.entity.UserGroup;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.SecurityUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;

/**
 *
 * @author Fifa
 */
@ManagedBean
@RequestScoped
public class AgentPerformanceSummaryReportController extends AbstractReportBaseController {

    private static Logger log = Logger.getLogger(AgentPerformanceSummaryReportController.class);

    private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
    private SimpleDateFormat ddMMyy = new SimpleDateFormat("dd/MM/yy", Locale.US);

    private String refNo;
    private Integer qaCategoryId;
    private String qaFormName;
    private String createBy;
    private boolean type;

    private List<QaTrans> createByList;
    private List<QaTransDetail> qaFormNameList;
    private List<String> qcByList;

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{qaCategoryDAO}")
    private QaCategoryDAO qaCategoryDAO;
    @ManagedProperty(value = "#{qaTransDAO}")
    private QaTransDAO qaTransDAO;
    @ManagedProperty(value = "#{qaTransDetailDAO}")
    private QaTransDetailDAO qaTransDetailDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:agentperformancesummaryreport:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        qaFormNameList = this.qaTransDetailDAO.findQaTransDetailByPO();
        qcByList = this.usersDAO.getUserListByUserGroupRole();
//        createByList = this.qaTransDAO.getQaTransDistinctCreateBy();
        super.init();
    }

    @Override
    protected String getReportPath() {
        return "";
    }

    @Override
    protected Map getParameterMap() {
        Map parameterMap = new LinkedHashMap();
        parameterMap.put("user_group_id", userGroupId);
        parameterMap.put("campaign_id", campaignId);
        parameterMap.put("product_id", productId);
        parameterMap.put("from", fromDate);
        parameterMap.put("to", toDate);
        parameterMap.put("user_id", userId);
        return parameterMap;
    }

    @Override
    protected String getQuery() {
        String query = "exec sp_fwd_agent_performance_summary_report_by_question "
                + "'" + yyyyMMdd.format(fromDate) + " 00:00:00' ,'" + yyyyMMdd.format(toDate) + " 23:59:59', " + campaignId.toString()
                + ", " + productId.toString() + ", '" + qaFormName + "', " + userGroupId.toString() + ", " + userId.toString()
                + ", '" + createBy + "', " + type;
        return query;
    }

    private void generateXLS() {
        String date = yyyyMMddHHmmss.format(new Date());
        String fileName = "AgentPerformanceSummaryReport_" + date + ".xls";

        FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("application/vnd.ms-excel");
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet1");

        sheet.setDefaultColumnWidth(20);

        HSSFFont font1 = workbook.createFont();
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font1.setFontName("Calibri");
        font1.setFontHeight((short) (20 * 20));

        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font2.setFontName("Calibri");
        font2.setFontHeight((short) (14 * 20));

        HSSFFont font3 = workbook.createFont();
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font3.setFontName("Calibri");
        font3.setFontHeight((short) (10 * 20));

        HSSFFont font4 = workbook.createFont();
        font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font4.setFontName("Calibri");
        font4.setFontHeight((short) (10 * 20));
        font4.setColor(IndexedColors.WHITE.getIndex());

        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor color1 = palette.findSimilarColor(235, 241, 222);
        short palIndex1 = color1.getIndex();

        HSSFColor color2 = palette.findSimilarColor(196, 215, 155);
        short palIndex2 = color2.getIndex();

        HSSFColor color3 = palette.findSimilarColor(224, 224, 224);
        short palIndex3 = color3.getIndex();

        HSSFCellStyle titleStyle1 = workbook.createCellStyle();
        titleStyle1.setAlignment(CellStyle.ALIGN_LEFT);
        titleStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        titleStyle1.setFillForegroundColor(palIndex1);
        titleStyle1.setFillForegroundColor(HSSFColor.LIME.index);
        titleStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleStyle1.setFont(font1);

        HSSFCellStyle titleStyle2 = workbook.createCellStyle();
        titleStyle2.setAlignment(CellStyle.ALIGN_LEFT);
        titleStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        titleStyle2.setFillForegroundColor(palIndex2);
//        titleStyle2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleStyle2.setFont(font2);

        HSSFCellStyle mainStyle = workbook.createCellStyle();
        mainStyle.setAlignment(CellStyle.ALIGN_CENTER);
        mainStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        mainStyle.setBorderTop(CellStyle.BORDER_THIN);
        mainStyle.setBorderRight(CellStyle.BORDER_THIN);
        mainStyle.setBorderLeft(CellStyle.BORDER_THIN);
        mainStyle.setBorderBottom(CellStyle.BORDER_THIN);
//        mainStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        mainStyle.setFillForegroundColor(palIndex3);
        mainStyle.setFont(font3);

        int row = 0;
        HSSFRow trow = sheet.createRow(row++);
        trow.setHeight((short) 620);
        trow.createCell(0).setCellValue("Agent Performance Summary Report by Question");
        trow.getCell(0).setCellStyle(titleStyle1);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("A%d:E%d", row, row)));

        trow = sheet.createRow(row++);
        trow.setHeight((short) 420);
        trow.createCell(0).setCellValue("Campaign  : ");
        trow.getCell(0).setCellStyle(titleStyle2);
        trow.createCell(1).setCellValue(campaignName);
        trow.getCell(1).setCellStyle(titleStyle2);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("B%d:E%d", row, row)));

        trow = sheet.createRow(row++);
        trow.setHeight((short) 420);
        trow.createCell(0).setCellValue("Product  : ");
        trow.getCell(0).setCellStyle(titleStyle2);
        trow.createCell(1).setCellValue(productName);
        trow.getCell(1).setCellStyle(titleStyle2);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("B%d:E%d", row, row)));

        trow = sheet.createRow(row++);
        trow.setHeight((short) 420);
        trow.createCell(0).setCellValue("QC Form  : ");
        trow.getCell(0).setCellStyle(titleStyle2);
        trow.createCell(1).setCellValue(qaFormName);
        trow.getCell(1).setCellStyle(titleStyle2);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("B%d:E%d", row, row)));

        trow = sheet.createRow(row++);
        trow.setHeight((short) 420);
        trow.createCell(0).setCellValue("Approved Date : ");
        trow.getCell(0).setCellStyle(titleStyle2);
        trow.createCell(1).setCellValue(ddMMyy.format(fromDate) + " To " + ddMMyy.format(toDate));
        trow.getCell(1).setCellStyle(titleStyle2);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("B%d:E%d", row, row)));

        trow = sheet.createRow(row++);
        trow.setHeight((short) 420);
        trow.createCell(0).setCellValue("User Group : ");
        trow.getCell(0).setCellStyle(titleStyle2);
        trow.createCell(1).setCellValue(groupName);
        trow.getCell(1).setCellStyle(titleStyle2);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("B%d:E%d", row, row)));

        trow = sheet.createRow(row++);
        trow.setHeight((short) 420);
        trow.createCell(0).setCellValue("TMR : ");
        trow.getCell(0).setCellStyle(titleStyle2);
        trow.createCell(1).setCellValue(userName);
        trow.getCell(1).setCellStyle(titleStyle2);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("B%d:E%d", row, row)));

        trow = sheet.createRow(row++);
        trow.setHeight((short) 420);
        trow.createCell(0).setCellValue("QC User : ");
        trow.getCell(0).setCellStyle(titleStyle2);
        trow.createCell(1).setCellValue(createBy);
        trow.getCell(1).setCellStyle(titleStyle2);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("B%d:E%d", row, row)));

        try {
            retrieveData(workbook, sheet, mainStyle, trow, font3, font4, palette);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
        } catch (IOException | SQLException e) {
            log.error(e);
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void retrieveData(HSSFWorkbook workbook, HSSFSheet sheet, HSSFCellStyle mainStyle, HSSFRow trow, HSSFFont font3, HSSFFont font4,
            HSSFPalette palette) throws SQLException {
        HSSFColor color4 = palette.findSimilarColor(146, 205, 220);
        short palIndex4 = color4.getIndex();

        HSSFColor color5 = palette.findSimilarColor(204, 204, 255);
        short palIndex5 = color5.getIndex();

        HSSFCellStyle headStyle = workbook.createCellStyle();
        headStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headStyle.setBorderTop(CellStyle.BORDER_THIN);
        headStyle.setBorderRight(CellStyle.BORDER_THIN);
        headStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headStyle.setBorderBottom(CellStyle.BORDER_THIN);
//        headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        headStyle.setFillForegroundColor(palIndex4);
//        headStyle.setFillForegroundColor(palIndex5);
        headStyle.setFont(font3);
        headStyle.setWrapText(true);

        HSSFCellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
        dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        dataStyle.setBorderTop(CellStyle.BORDER_THIN);
        dataStyle.setBorderRight(CellStyle.BORDER_THIN);
        dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
        dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
        dataStyle.setFont(font3);
        dataStyle.setWrapText(true);

        HSSFCellStyle averageStyle = workbook.createCellStyle();
        averageStyle.setAlignment(CellStyle.ALIGN_LEFT);
        averageStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        averageStyle.setBorderTop(CellStyle.BORDER_THIN);
//        averageStyle.setBorderRight(CellStyle.BORDER_THIN);
//        averageStyle.setBorderLeft(CellStyle.BORDER_THIN);
//        averageStyle.setBorderBottom(CellStyle.BORDER_THIN);
//        averageStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        averageStyle.setFillForegroundColor(palIndex5);
//        averageStyle.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
        averageStyle.setFont(font4);

        HSSFCell tcell;
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        int maxValue = 0;

        int width = 5;
        int lastCell = 0;
        int numCol;
        int cell;
        try {
            type = false;
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(getQuery());

            ResultSetMetaData rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            cell = 0;
            trow = sheet.createRow(10);
            trow.setHeight((short) 2020);

            trow.createCell(cell).setCellValue("No");
            trow.getCell(cell++).setCellStyle(mainStyle);
            trow.createCell(cell).setCellValue("Agent Name");
            trow.getCell(cell++).setCellStyle(mainStyle);
            trow.createCell(cell).setCellValue("Total Jobs");
            trow.getCell(cell++).setCellStyle(mainStyle);
            trow.createCell(cell).setCellValue("Grade");
            trow.getCell(cell++).setCellStyle(mainStyle);
            trow.createCell(cell).setCellValue("% Avg Score");
            trow.getCell(cell++).setCellStyle(mainStyle);

            if (rs.next()) {
                do {
                    width += numCol;
                    for (int i = 1; i <= numCol; i++) {
                        tcell = trow.createCell(cell++);
                        tcell.setCellValue(rs.getString(i));
                        tcell.setCellStyle(headStyle);
                        tcell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    }
                } while (rs.next());
            }

            if (cell > 5) {
//                trow = sheet.createRow(9);
//                tcell = trow.createCell(5);
//                tcell.setCellValue("Average Score");
//                tcell.setCellStyle(averageStyle);
//                tcell.setCellType(HSSFCell.CELL_TYPE_STRING);
//                sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("F10:%s10", CellReference.convertNumToColString(--cell))));
                lastCell = cell;
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

        try {
            type = true;
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(getQuery());
            int divide = 0;

            ResultSetMetaData rsmd = rs.getMetaData();
            numCol = rsmd.getColumnCount();
            int no = 1;
            int startRow = 11;
            int data = 5;
            int totalJob = 0;

            if (rs.next()) {
                do {
                    divide = 0;
                    totalJob = 0;
                    trow = sheet.createRow(startRow);
                    for (int i = 1; i <= numCol; i++) {
                        if (i == numCol) {
                            maxValue = rs.getInt(i);
                        } else {
                            if (i == 1) {
                                tcell = trow.createCell(i);
                                tcell.setCellValue(rs.getString(i));
                                tcell.setCellStyle(dataStyle);
                                tcell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            } else if (i == 2) {
                                totalJob = rs.getInt(i);
                                tcell = trow.createCell(i);
                                tcell.setCellValue(rs.getInt(i));
                                //                            tcell.setCellValue(rs.getInt(i));
                                tcell.setCellStyle(dataStyle);
                                tcell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            } else {
                                tcell = trow.createCell(data++);
                                divide += 5;
                                if (rs.getString(i) == null || rs.getString(i).isEmpty() || rs.getString(i).equals("")) {
                                    tcell.setCellValue(0);
                                } else {
                                    tcell.setCellValue(rs.getInt(i));
                                    //                                if (rs.getInt(i) != 0) {
                                    //                                    divide += 5;
                                    //                                }
                                }
                                tcell.setCellStyle(dataStyle);
                                tcell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            }
                        }

                    }
                    startRow++;
                    data = 5;
                    if (divide == 0) {
                        divide = 1;
                    }

                    tcell = trow.createCell(0);
                    tcell.setCellValue(no++);
                    tcell.setCellStyle(dataStyle);
                    tcell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    tcell = trow.createCell(3);
                    tcell.setCellFormula(String.format("IF(%s%d>=80,\"good\",(IF(%s%d>50,\"pass\",\"fail\")))",
                            CellReference.convertNumToColString(4), startRow,
                            CellReference.convertNumToColString(4), startRow));
                    tcell.setCellStyle(dataStyle);
                    tcell.setCellType(HSSFCell.CELL_TYPE_FORMULA);

                    tcell = trow.createCell(4);
//                    tcell.setCellFormula(String.format("ROUND(((SUM(%s%d:%s%d)/(" + divide + "*" + totalJob + "))*100),0)",
//                            CellReference.convertNumToColString(5), startRow,
//                            CellReference.convertNumToColString(lastCell), startRow));
                    tcell.setCellFormula(String.format("ROUND(((SUM(%s%d:%s%d)/(" + maxValue + "*" + totalJob + "))*100),0)",
                            CellReference.convertNumToColString(5), startRow,
                            CellReference.convertNumToColString(lastCell), startRow));

//                    tcell.setCellFormula(String.format("ROUND(((SUM(%s%d:%s%d)/(" + maxValue + "))*100),0)",
//                            CellReference.convertNumToColString(5), startRow,
//                            CellReference.convertNumToColString(lastCell), startRow));
                    tcell.setCellStyle(dataStyle);
                    tcell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
//        for (int i = 5; i < width; i++) {
//            workbook.getSheetAt(0).setColumnWidth(i, 15000);
//        }

    }

    @Override
    public void reportListener(ActionEvent event) {
        generateXLS();
        FacesContext.getCurrentInstance().responseComplete();
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public QaCategoryDAO getQaCategoryDAO() {
        return qaCategoryDAO;
    }

    public void setQaCategoryDAO(QaCategoryDAO qaCategoryDAO) {
        this.qaCategoryDAO = qaCategoryDAO;
    }

    public Integer getQaCategoryId() {
        return qaCategoryId;
    }

    public void setQaCategoryId(Integer qaCategoryId) {
        this.qaCategoryId = qaCategoryId;
    }

    public QaTransDAO getQaTransDAO() {
        return qaTransDAO;
    }

    public void setQaTransDAO(QaTransDAO qaTransDAO) {
        this.qaTransDAO = qaTransDAO;
    }

    public List<QaTrans> getCreateByList() {
        return createByList;
    }

    public void setCreateByList(List<QaTrans> createByList) {
        this.createByList = createByList;
    }

    public List<QaTransDetail> getQaFormNameList() {
        return qaFormNameList;
    }

    public void setQaFormNameList(List<QaTransDetail> qaFormNameList) {
        this.qaFormNameList = qaFormNameList;
    }

    public QaTransDetailDAO getQaTransDetailDAO() {
        return qaTransDetailDAO;
    }

    public void setQaTransDetailDAO(QaTransDetailDAO qaTransDetailDAO) {
        this.qaTransDetailDAO = qaTransDetailDAO;
    }

    public String getQaFormName() {
        return qaFormName;
    }

    public void setQaFormName(String qaFormName) {
        this.qaFormName = qaFormName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public List<String> getQcByList() {
        return qcByList;
    }

    public void setQcByList(List<String> qcByList) {
        this.qcByList = qcByList;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

}
