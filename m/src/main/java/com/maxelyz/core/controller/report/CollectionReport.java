package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.value.admin.CollectionReportValue;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
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
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;


@ManagedBean
@RequestScoped
public class CollectionReport extends AbstractReportBaseController {

    private static Logger log = Logger.getLogger(CollectionReport.class);
    private SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
    private String fileName = "";
    private Map<Integer, List<CollectionReportValue>> userList = new LinkedHashMap<Integer, List<CollectionReportValue>>();
    private List<String[]> paymentList;
    private List<CollectionReportValue> sumByPaymentList = new ArrayList<CollectionReportValue>();
    private int startRowData;
    private Integer cellLength;
    private List<String> listSumFormular = new ArrayList<String>();
    private DecimalFormat df = new DecimalFormat("#,##0.00");
    private DecimalFormat df1 = new DecimalFormat("#,###");

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:collectionreport:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "";
    }

    @Override
    public Map getParameterMap() {
        Map parameterMap = new LinkedHashMap();
        parameterMap.put("from", fromDate);
        return parameterMap;
    }

    @Override
    public String getQuery() {
        String subWhere = " ";
        String where = "WHERE po.purchase_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        if (campaignId != null && campaignId != 0) {
            where += "AND a.campaign_id =  " + campaignId + " ";
            subWhere += "and assignment.campaign_id = " + campaignId + " ";
        }
        if (productId != null && productId != 0) {
            where += "AND pod.product_id =  " + productId + " ";
            subWhere += "and purchase_order_detail.product_id = " + productId + " ";
        }
        String select = "select u.id,u.name + ' ' + u.surname  as [TSR Name],p.id,p.name as [Payment Method], "
                + "(select count(purchase_order.id) from purchase_order join assignment_detail on purchase_order.assignment_detail_id = assignment_detail.id join users on assignment_detail.user_id = users.id join assignment on assignment_detail.assignment_id = assignment.id join purchase_order_detail on purchase_order_detail.purchase_order_id = purchase_order.id where users.id = u.id and purchase_order.sale_result = 'Y' and purchase_order.qc_status = 'approved' and purchase_order.payment_status = 'approved' and purchase_order.payment_method = p.id and purchase_order.purchase_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' " + subWhere + "  ) as [Sales], "
                + "(select sum(purchase_order.net_premium) from purchase_order join assignment_detail on purchase_order.assignment_detail_id = assignment_detail.id join users on assignment_detail.user_id = users.id join assignment on assignment_detail.assignment_id = assignment.id join purchase_order_detail on purchase_order_detail.purchase_order_id = purchase_order.id where users.id = u.id and purchase_order.sale_result = 'Y' and purchase_order.qc_status = 'approved' and purchase_order.payment_status = 'approved' and purchase_order.payment_method = p.id and purchase_order.purchase_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' " + subWhere + " ) as [1st premium 1], "
                + "(select sum(purchase_order.annual_net_premium) from purchase_order join assignment_detail on purchase_order.assignment_detail_id = assignment_detail.id join users on assignment_detail.user_id = users.id join assignment on assignment_detail.assignment_id = assignment.id join purchase_order_detail on purchase_order_detail.purchase_order_id = purchase_order.id where users.id = u.id and purchase_order.sale_result = 'Y' and purchase_order.qc_status = 'approved' and purchase_order.payment_status = 'approved' and purchase_order.payment_method = p.id and purchase_order.purchase_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' " + subWhere + " ) as [AFYP 1], "
                + "(select count(purchase_order.id) from purchase_order join assignment_detail on purchase_order.assignment_detail_id = assignment_detail.id join users on assignment_detail.user_id = users.id join assignment on assignment_detail.assignment_id = assignment.id join purchase_order_detail on purchase_order_detail.purchase_order_id = purchase_order.id where users.id = u.id and purchase_order.sale_result = 'Y' and purchase_order.qc_status = 'approved' and purchase_order.payment_status = 'approved' and settlement = 1 and purchase_order.payment_method = p.id and purchase_order.purchase_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' " + subWhere + " ) as [Collected], "
                + "(select sum(purchase_order.net_premium) from purchase_order join assignment_detail on purchase_order.assignment_detail_id = assignment_detail.id join users on assignment_detail.user_id = users.id join assignment on assignment_detail.assignment_id = assignment.id join purchase_order_detail on purchase_order_detail.purchase_order_id = purchase_order.id where users.id = u.id and purchase_order.sale_result = 'Y' and purchase_order.qc_status = 'approved' and purchase_order.payment_status = 'approved' and settlement = 1 and purchase_order.payment_method = p.id and purchase_order.purchase_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' " + subWhere + " ) as [1st premium 2], "
                + "(select sum(purchase_order.annual_net_premium) from purchase_order join assignment_detail on purchase_order.assignment_detail_id = assignment_detail.id join users on assignment_detail.user_id = users.id join assignment on assignment_detail.assignment_id = assignment.id join purchase_order_detail on purchase_order_detail.purchase_order_id = purchase_order.id where users.id = u.id and purchase_order.sale_result = 'Y' and purchase_order.qc_status = 'approved' and purchase_order.payment_status = 'approved' and settlement = 1 and purchase_order.payment_method = p.id and purchase_order.purchase_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' " + subWhere + " ) as [AFYP 2] "
                + "from purchase_order po "
                + "inner join assignment_detail ad on po.assignment_detail_id = ad.id "
                + "inner join assignment a on ad.assignment_id = a.id "
                + "inner join users u on u.id = ad.user_id "
                + "inner join payment_method p on po.payment_method = p.id "
                + "inner join purchase_order_detail pod on po.id = pod.purchase_order_id ";

        String groupBy = "group by u.name, u.surname, u.id, p.name, p.id ";
        String orderBy = "order by u.name ";
        String query = select + where + groupBy + orderBy;
        System.out.println("Sales and Collected ReportQuery : " + query);

        return query;
    }

    public void reportListener(ActionEvent event) {
        generateXLS();
        FacesContext.getCurrentInstance().responseComplete();
    }

    private void generateXLS() {
        FileInputStream file = null;
        Date d = new Date();
        String start = sdf2.format(d);
        fileName = "DRTV_SalesAndCollected_Report_" + start + ".xls";
        try {
            file = new FileInputStream(new File(JSFUtil.getuploadPath(), "report" + "/CollectionReport/collection_report_template.xls"));
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFCellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderTop(CellStyle.BORDER_THIN);
            dataStyle.setBorderRight(CellStyle.BORDER_THIN);
            dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
            dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
            dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
            dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            
            HSSFCellStyle dataStyleString = workbook.createCellStyle();
            dataStyleString.setBorderTop(CellStyle.BORDER_THIN);
            dataStyleString.setBorderRight(CellStyle.BORDER_THIN);
            dataStyleString.setBorderLeft(CellStyle.BORDER_THIN);
            dataStyleString.setBorderBottom(CellStyle.BORDER_THIN);
            dataStyleString.setAlignment(CellStyle.ALIGN_LEFT);
            dataStyleString.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            HSSFCellStyle dataStyleFloat = workbook.createCellStyle();
            dataStyleFloat.setBorderTop(CellStyle.BORDER_THIN);
            dataStyleFloat.setBorderRight(CellStyle.BORDER_THIN);
            dataStyleFloat.setBorderLeft(CellStyle.BORDER_THIN);
            dataStyleFloat.setBorderBottom(CellStyle.BORDER_THIN);
            dataStyleFloat.setAlignment(CellStyle.ALIGN_RIGHT);
            dataStyleFloat.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            HSSFDataFormat df2 = workbook.createDataFormat();
            dataStyleFloat.setDataFormat(df2.getFormat("#,##0.00"));
            
            HSSFFont font_white = workbook.createFont();
            font_white.setColor(new HSSFColor.WHITE().getIndex());
            
            HSSFCellStyle totalStyle = workbook.createCellStyle();
            totalStyle.setBorderTop(CellStyle.BORDER_THIN);
            totalStyle.setBorderRight(CellStyle.BORDER_THIN);
            totalStyle.setBorderLeft(CellStyle.BORDER_THIN);
            totalStyle.setBorderBottom(CellStyle.BORDER_THIN);
            totalStyle.setAlignment(CellStyle.ALIGN_CENTER);
            totalStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            totalStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            totalStyle.setFillForegroundColor(new HSSFColor.DARK_TEAL().getIndex());
            totalStyle.setFont(font_white);
            
            HSSFCellStyle totalStyleFloat = workbook.createCellStyle();
            totalStyleFloat.setBorderTop(CellStyle.BORDER_THIN);
            totalStyleFloat.setBorderRight(CellStyle.BORDER_THIN);
            totalStyleFloat.setBorderLeft(CellStyle.BORDER_THIN);
            totalStyleFloat.setBorderBottom(CellStyle.BORDER_THIN);
            totalStyleFloat.setAlignment(CellStyle.ALIGN_RIGHT);
            totalStyleFloat.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            HSSFDataFormat df3 = workbook.createDataFormat();
            totalStyleFloat.setDataFormat(df3.getFormat("#,##0.00"));
            totalStyleFloat.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            totalStyleFloat.setFillForegroundColor(new HSSFColor.DARK_TEAL().getIndex());
            totalStyleFloat.setFont(font_white);
            
            HSSFCreationHelper createHelper = workbook.getCreationHelper();

            FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("application/vnd.ms-excel");
            FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            //Fill Header
            HSSFRow row;
            HSSFCell cell;
            row = sheet.getRow(6);
            cell = row.getCell(1);
            String saleDate = sdf1.format(fromDate) + " - " + sdf1.format(toDate);
            cell.setCellValue(saleDate);
            cell = sheet.getRow(7).getCell(1);
            cell.setCellValue(campaignName);
            cell = sheet.getRow(8).getCell(1);
            cell.setCellValue(productName);

            paymentList = this.getPurchaseOrderDAO().countDistinctPaymentMethodFromYesSale(fromDate, toDate, campaignId, productId);
            if (paymentList != null && paymentList.size() > 0) {
                // generate table header base on number of payment method
                Integer paymentSize = paymentList.size();
                cellLength = ((paymentSize + 1) * 7) + 1;
                for (int i = 0; i < 12; i++) {
                    row = sheet.getRow(i);
                    for (int j = cellLength; j < 85; j++) {
                        row.removeCell(row.getCell(j));
                    }
                }
                //Divide data to each user with list<payment method>
                divideData();
                //Fill Payment Header
                row = sheet.getRow(10);
                int i = 1;
                sumByPaymentList.clear();
                for (Object[] s : paymentList) {
                    row.getCell(i).setCellValue((String) s[1]);
                    i = i + 7;
//                  Integer collected, Double np2, Double a2
                    CollectionReportValue crv = new CollectionReportValue(new Integer((String) s[0]), 0, "", (String) s[1], 0, 0.0, 0.0, 0, 0.0, 0.0);
                    sumByPaymentList.add(crv);
                }
                row.getCell(i).setCellValue("Total");
                //Fill Data
                startRowData = 12;
                if (userList != null) {
                    for (Map.Entry<Integer, List<CollectionReportValue>> item : userList.entrySet()) {
                        row = sheet.createRow(startRowData);
                        row.createCell(0).setCellStyle(dataStyleString);
                        row.getCell(0).setCellValue(item.getValue().get(0).getName());
                        int in = 1;
                        int sumSale = 0, sumColl = 0;
                        Double sumNP1 = 0.0, sumNP2 = 0.0, sumAP1 = 0.0, sumAP2 = 0.0;
                        for (CollectionReportValue crv : item.getValue()) {
                            row.createCell(in).setCellStyle(dataStyle);
                            row.getCell(in).setCellValue(crv.getSales());
                            in++;
                            row.createCell(in).setCellStyle(dataStyleFloat);
                            row.getCell(in).setCellValue(crv.getNetPremium1());
                            in++;
                            row.createCell(in).setCellStyle(dataStyleFloat);
                            row.getCell(in).setCellValue(crv.getAFYP1());
                            in++;
                            row.createCell(in).setCellStyle(dataStyle);
                            row.getCell(in).setCellValue(crv.getCollected());
                            in++;
                            row.createCell(in).setCellStyle(dataStyleFloat);
                            row.getCell(in).setCellValue(df.format(crv.getPercent()) + "%");
                            in++;
                            row.createCell(in).setCellStyle(dataStyleFloat);
                            row.getCell(in).setCellValue(crv.getNetPremium2());
                            in++;
                            row.createCell(in).setCellStyle(dataStyleFloat);
                            row.getCell(in).setCellValue(crv.getAFYP2());
                            in++;
                            sumSale += crv.getSales();
                            sumColl += crv.getCollected();
                            sumNP1 += crv.getNetPremium1();
                            sumNP2 += crv.getNetPremium2();
                            sumAP1 += crv.getAFYP1();
                            sumAP2 += crv.getAFYP2();
                        }
                        //Fill Total Data per each TSR
                        row.createCell(in).setCellStyle(dataStyle);
                        row.getCell(in).setCellValue(sumSale);
                        in++;
                        row.createCell(in).setCellStyle(dataStyleFloat);
                        row.getCell(in).setCellValue(sumNP1);
                        in++;
                        row.createCell(in).setCellStyle(dataStyleFloat);
                        row.getCell(in).setCellValue(sumAP1);
                        in++;
                        row.createCell(in).setCellStyle(dataStyle);
                        row.getCell(in).setCellValue(sumColl);
                        in++;
                        row.createCell(in).setCellStyle(dataStyle);
                        Double sp = 0.0;
                        if (sumSale > 0) {
                            sp = (new Double(sumColl) / new Double(sumSale)) * 100.0;
                        }
                        row.getCell(in).setCellValue(df.format(sp) + "%");
                        in++;
                        row.createCell(in).setCellStyle(dataStyleFloat);
                        row.getCell(in).setCellValue(sumNP2);
                        in++;
                        row.createCell(in).setCellStyle(dataStyleFloat);
                        row.getCell(in).setCellValue(sumAP2);
                        in++;
                        startRowData++;
                    }
                }

//              System.out.println(cellLength + " , 13 , " + (startRowData + 1) + " ");
                calculateSumByPaymentMethod(cellLength-1, 13, startRowData);
//              displayFormularList();

                //Fill total per each payment method
                row = sheet.createRow(startRowData);
                row.createCell(0).setCellValue("Total");
                row.getCell(0).setCellStyle(totalStyle);
                int in = 1, chk = 1;
                for(String s : listSumFormular){
                    cell = row.createCell(in);
                    cell.setCellStyle(totalStyleFloat);
                    if(chk == 1 || chk == 4 ){
                        cell.setCellStyle(totalStyle);
                    }
                    if(chk == 5){
                        CellUtil.setCellStyleProperty(cell, workbook, CellUtil.DATA_FORMAT, createHelper.createDataFormat().getFormat("##0.00%"));
                    }
                    if(chk == 7){
                        chk = 0;
                    }
                    cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                    cell.setCellFormula(s);
                    in++;
                    chk++;
                }
                
                workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
                FacesContext.getCurrentInstance().responseComplete();
            }

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MediaPlanReport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(MediaPlanReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void divideData() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(this.getExcelQuery());
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCol = rsmd.getColumnCount();
            if (rs.next()) {
                do {
                    if (userList.containsKey(rs.getInt(1))) {
                        List<CollectionReportValue> li = userList.get(rs.getInt(1));
                        for (CollectionReportValue crv : li) {
                            if (crv.getPaymentId() == rs.getInt(3)) {
                                crv.setSales(rs.getInt(5));
                                crv.setNetPremium1((rs.getString(6) != null && !rs.getString(6).equalsIgnoreCase("null")) ? rs.getDouble(6) : 0.0);
                                crv.setAFYP1((rs.getString(7) != null && !rs.getString(7).equalsIgnoreCase("null")) ? rs.getDouble(7) : 0.0);
                                crv.setCollected(rs.getInt(8));
                                crv.setNetPremium2((rs.getString(9) != null && !rs.getString(9).equalsIgnoreCase("null")) ? rs.getDouble(9) : 0.0);
                                crv.setAFYP2((rs.getString(10) != null && !rs.getString(10).equalsIgnoreCase("null")) ? rs.getDouble(10) : 0.0);
                                break;
                            }
                        }
                        userList.put(rs.getInt(1), li);
                    } else {
                        List<CollectionReportValue> l = new ArrayList<CollectionReportValue>();
                        CollectionReportValue cv = null;
                        for (Object[] p : paymentList) {
                            cv = new CollectionReportValue(new Integer((String) p[0]), rs.getInt(1), rs.getString(2), (String) p[1], 0, 0.0, 0.0, 0, 0.0, 0.0);
                            l.add(cv);
                        }
                        for (CollectionReportValue c : l) {
                            if (c.getPaymentId() == rs.getInt(3)) {
                                c.setSales(rs.getInt(5));
                                c.setNetPremium1((rs.getString(6) != null && !rs.getString(6).equalsIgnoreCase("null")) ? rs.getDouble(6) : 0.0);
                                c.setAFYP1((rs.getString(7) != null && !rs.getString(7).equalsIgnoreCase("null")) ? rs.getDouble(7) : 0.0);
                                c.setCollected(rs.getInt(8));
                                c.setNetPremium2((rs.getString(9) != null && !rs.getString(9).equalsIgnoreCase("null")) ? rs.getDouble(9) : 0.0);
                                c.setAFYP2((rs.getString(10) != null && !rs.getString(10).equalsIgnoreCase("null")) ? rs.getDouble(10) : 0.0);
                                break;
                            }
                        }
                        userList.put(rs.getInt(1), l);
                    }
                } while (rs.next());
//              displayUserList();
                calculatePercent();
            }
        } catch (Exception e) {
            log.error(e);
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
    }

    private void displayUserList() {
        if (userList != null) {
            for (Map.Entry<Integer, List<CollectionReportValue>> item : userList.entrySet()) {
                System.out.println("==============================// " + item.getKey() + " \\=====================================");
                for (CollectionReportValue crv : item.getValue()) {
                    System.out.println(crv.getPaymentName() + "  ,  " + crv.getName() + "  ,  " + crv.getSales() + "  ,  " + crv.getNetPremium1() + "  ,  " + crv.getAFYP1() + "  ,  " + crv.getCollected() + "  ,  " + crv.getNetPremium2() + "  ,  " + crv.getAFYP2());
                }
                System.out.println("==============================//======================\\=====================================");
            }
        }
    }

    private void displayFormularList() {
        System.out.println("============================== List SUM Formular =====================================");
        if (listSumFormular != null && listSumFormular.size() > 0) {
            for (String s : listSumFormular) {
                System.out.println(s);
            }
        }
    }

    private void calculatePercent() {
        if (userList != null) {
            for (Map.Entry<Integer, List<CollectionReportValue>> item : userList.entrySet()) {
                for (CollectionReportValue crv : item.getValue()) {
                    Double p = 0.0;
                    if (crv.getSales() > 0) {
                        p = (crv.getCollected() / crv.getSales()) * 100.0;
                    }
                    crv.setPercent(p);
                }
            }
        }
    }

    private void calculateSumByPaymentMethod(int length, int start, int end) {
        int in = 66, chk = 1, temp = 0, t = 0;
        listSumFormular.clear();
        
        int columnNumber = 1;
        for (int j = 0; j < length; j++) {
            if (chk == 5) {
                String columnLetter1 = CellReference.convertNumToColString(columnNumber-1);
                String columnLetter2 = CellReference.convertNumToColString(columnNumber-3);
                listSumFormular.add(j, "IF(" + columnLetter2 + "" + (end + 1) + "=0,0," + columnLetter1 + "" + (end + 1) + "/" + columnLetter2 + "" + (end + 1) + ")");
            } else {
                String columnLetter = CellReference.convertNumToColString(columnNumber);
                listSumFormular.add(j, "SUM(" + columnLetter + start + ":" + columnLetter + end + ")");
            }
            
            if (chk == 7) {
                chk = 0;
            }
            columnNumber++;
            chk++;
        }
        
        /*
        for (int j = 0; j < length; j++) {
            if (j < 26) {
                temp = in + t;
                if (chk == 5) {
                    temp = temp - 1;
                    char ch1 = (char) temp;
                    temp = temp - 3;
                    char ch2 = (char) temp;
                    //listSumFormular.add(j, "(" + ch1 + "" + (end + 1) + "/" + ch2 + "" + (end + 1) + ")*100");
                    //listSumFormular.add(j, "(" + ch1 + "" + (end + 1) + "/" + ch2 + "" + (end + 1) + ")");
                    listSumFormular.add(j, "IF(" + ch2 + "" + (end + 1) + "=0,0," + ch1 + "" + (end + 1) + "/" + ch2 + "" + (end + 1) + ")");
                } else {
                    listSumFormular.add(j, "SUM(" + ((char) temp) + "" + start + ":" + ((char) temp) + end + ")");
                }
            } else if (j >= 26 && j < 51) {
                if (j == 26) {
                    in = 66;
                    t = 0;
                }
                temp = in + t;
                if (chk == 5) {
                    temp = temp - 1;
                    String ch1 = "A" + (char) temp;
                    temp = temp - 3;
                    String ch2 = "A" + (char) temp;
                    //listSumFormular.add(j, "(" + ch1 + "" + (end + 1) + "/" + ch2 + "" + (end + 1) + ")*100");
                    listSumFormular.add(j, "IF(" + ch2 + "" + (end + 1) + "=0,0," + ch1 + "" + (end + 1) + "/" + ch2 + "" + (end + 1) + ")");
                } else {
                    listSumFormular.add(j, "SUM(A" + ((char) temp) + "" + start + ":A" + ((char) temp) + end + ")");
                }
            } else if (j >= 51 && j < 76) {
                if (j == 51) {
                    in = 66;
                    t = 0;
                }
                temp = in + t;
                if (chk == 5) {
                    temp = temp - 1;
                    String ch1 = "B" + (char) temp;
                    temp = temp - 3;
                    String ch2 = "B" + (char) temp;
                    //listSumFormular.add(j, "(" + ch1 + "" + (end + 1) + "/" + ch2 + "" + (end + 1) + ")*100");
                    listSumFormular.add(j, "IF(" + ch2 + "" + (end + 1) + "=0,0," + ch1 + "" + (end + 1) + "/" + ch2 + "" + (end + 1) + ")");
                } else {
                    listSumFormular.add(j, "SUM(B" + ((char) temp) + "" + start + ":B" + ((char) temp) + end + ")");
                }
            } else if (j >= 76 && j < 101) {
                if (j == 76) {
                    in = 66;
                    t = 0;
                }
                temp = in + t;
                if (chk == 5) {
                    temp = temp - 1;
                    String ch1 = "C" + (char) temp;
                    temp = temp - 3;
                    String ch2 = "C" + (char) temp;
                    //listSumFormular.add(j, "(" + ch1 + "" + (end + 1) + "/" + ch2 + "" + (end + 1) + ")*100");
                    listSumFormular.add(j, "IF(" + ch2 + "" + (end + 1) + "=0,0," + ch1 + "" + (end + 1) + "/" + ch2 + "" + (end + 1) + ")");
                } else {
                    listSumFormular.add(j, "SUM(C" + ((char) temp) + "" + start + ":C" + ((char) temp) + end + ")");
                }
            }
            if (chk == 7) {
                chk = 0;
            }
            t++;
            chk++;
        }
        */
    }

    ////////////////////////////// GET - SET METHOD ///////////////////////////////
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }
}
