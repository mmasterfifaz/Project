package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import static com.sun.faces.scripting.groovy.GroovyHelperFactory.createHelper;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
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
import net.sf.jasperreports.components.table.Cell;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

@ManagedBean
@RequestScoped
public class TotalSalesReport extends AbstractReportBaseController {

    private static Logger log = Logger.getLogger(TotalSalesReport.class);
    private SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
    private String fileName = "";
    private DecimalFormat df = new DecimalFormat("#,##0.00");
    private DecimalFormat df1 = new DecimalFormat("#,###");
    private Double sumAFYP = 0.0, sumPremiumMode = 0.0;

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:totalsalesreport:view")) {
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
        String where = " ";
        //and  a.campaign_id = 23 and a.marketing_id = 136 and pod.product_id = 113
        if (marketingId != null && marketingId != 0) {
            where += "AND a.marketing_id =  " + marketingId + " ";
        }
        if (campaignId != null && campaignId != 0) {
            where += "AND a.campaign_id = " + campaignId + " ";
        }
        if (productId != null && productId != 0) {
            where += "AND pod.product_id =  " + productId + " ";
        }
        String select = "SELECT TOP (100) PERCENT CASE WHEN por.fx1 = '01' THEN c.flexfield5 ELSE po.ref_no END AS [X-RefNo], po.qc_date AS [Sale Date], por.name + ' ' + por.surname AS [Customer name], u.description AS TSRName_Yesfile,u.code AS TSRCode_Yesfile, 0 AS SupCode_Yesfile, u.name + ' ' + u.surname AS TSRName_Comm, u.citizen_id AS TSRCode_Comm, ug.name AS SupCode_Comm, po.net_premium AS PremiumMode, (po.annual_net_premium) as AFYP,pm.name AS PaymentChannel,  "
                + "case pod.payment_mode when 1 then 'Monthly' when 2 then 'Quarterly' when 3 then 'Half Year ' when 4 then 'Yearly'  end AS PaymentMode,  "
                + "LEFT(CONVERT(varchar, po.qc_date,112),6) AS MonthSale,m.fx1 AS [Tier Condition],m.fx3 AS [Submit Condition], po.settlement_desc AS status, po.settlement_date AS [Paid Date], po.policy_no AS [Policy No.],po.policy_status_desc AS [Policy Status Desc],po.policy_issue_date AS [Issue Date], po.policy_approve_date AS [Appro Date] "
                + "FROM purchase_order AS po INNER JOIN purchase_order_detail AS pod ON pod.purchase_order_id = po.id INNER JOIN purchase_order_register AS por ON por.purchase_order_detail_id = pod.id INNER JOIN assignment_detail AS ad ON po.assignment_detail_id = ad.id INNER JOIN assignment AS a ON ad.assignment_id = a.id INNER JOIN users AS u ON ad.user_id = u.id INNER JOIN user_group AS ug ON u.user_group_id = ug.id INNER JOIN payment_method AS pm ON po.payment_method = pm.id INNER JOIN marketing AS m ON m.id = a.marketing_id INNER JOIN customer AS c ON c.id = po.customer_id "
                + "WHERE (po.qc_date BETWEEN '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "') AND (po.qc_status LIKE 'approved') AND (po.payment_status LIKE 'approved') AND (po.approval_status LIKE 'approved') and po.sale_result = 'Y' ";

//        String select = "SELECT TOP (100) PERCENT CASE WHEN reg.fx1 = '01' THEN c.flexfield5 ELSE po.ref_no END AS [X-RefNo], po.qc_date AS [Sale Date], por.name + ' ' + por.surname AS [Customer name], u.description AS TSRName_Yesfile,u.code AS TSRCode_Yesfile, 0 AS SupCode_Yesfile, u.name + ' ' + u.surname AS TSRName_Comm, u.citizen_id AS TSRCode_Comm, ug.name AS SupCode_Comm, po.net_premium AS PremiumMode, (po.annual_net_premium) as AFYP,pm.name AS PaymentChannel,  "
//                + "case pod.payment_mode when 1 then 'Monthly' when 2 then 'Quarterly' when 3 then 'Half Year ' when 4 then 'Yearly'  end AS PaymentMode,  "
//                + "LEFT(CONVERT(varchar, po.qc_date,112),6) AS MonthSale,m.fx1 AS [Tier Condition],m.fx3 AS [Submit Condition], po.settlement_desc AS status, po.settlement_date AS [Paid Date], po.policy_no AS [Policy No.],po.policy_status_desc AS [Policy Status Desc],po.policy_issue_date AS [Issue Date], po.policy_approve_date AS [Appro Date] "
//                + "FROM purchase_order AS po INNER JOIN purchase_order_detail AS pod ON pod.purchase_order_id = po.id INNER JOIN purchase_order_register AS por ON por.purchase_order_detail_id = pod.id INNER JOIN assignment_detail AS ad ON po.assignment_detail_id = ad.id INNER JOIN assignment AS a ON ad.assignment_id = a.id INNER JOIN users AS u ON ad.user_id = u.id INNER JOIN user_group AS ug ON u.user_group_id = ug.id INNER JOIN payment_method AS pm ON po.payment_method = pm.id INNER JOIN marketing AS m ON m.id = a.marketing_id "
//                + "WHERE (po.qc_date BETWEEN '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "') AND (po.qc_status LIKE 'approved') AND (po.payment_status LIKE 'approved') AND (po.approval_status LIKE 'approved') and po.yes_flag = 1 ";

//        String groupBy = "group by u.name, u.surname, u.id, p.name, p.id ";
        String orderBy = " ORDER BY [Sale Date]	 ";
        String query = select + where + orderBy;
        System.out.println("Total Sales Report Query : " + query);

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
        sumAFYP = 0.0;
        sumPremiumMode = 0.0;
        fileName = "DRTV_TotalSales_Report_" + start + ".xls";
        try {
            file = new FileInputStream(new File(JSFUtil.getuploadPath(), "report" + "/TotalSales/total_sale_template.xls"));
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFCellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderTop(CellStyle.BORDER_THIN);
            dataStyle.setBorderRight(CellStyle.BORDER_THIN);
            dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
            dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
            dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
            dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            HSSFCellStyle dataTextStyle = workbook.createCellStyle();
            dataTextStyle.setBorderTop(CellStyle.BORDER_THIN);
            dataTextStyle.setBorderRight(CellStyle.BORDER_THIN);
            dataTextStyle.setBorderLeft(CellStyle.BORDER_THIN);
            dataTextStyle.setBorderBottom(CellStyle.BORDER_THIN);
            dataTextStyle.setAlignment(CellStyle.ALIGN_CENTER);
            dataTextStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            HSSFDataFormat dft = workbook.createDataFormat();
//            dataTextStyle.setDataFormat(HSSFCell.CELL_TYPE_STRING);

            HSSFCellStyle dataStyleFloat = workbook.createCellStyle();
            dataStyleFloat.setBorderTop(CellStyle.BORDER_THIN);
            dataStyleFloat.setBorderRight(CellStyle.BORDER_THIN);
            dataStyleFloat.setBorderLeft(CellStyle.BORDER_THIN);
            dataStyleFloat.setBorderBottom(CellStyle.BORDER_THIN);
            dataStyleFloat.setAlignment(CellStyle.ALIGN_CENTER);
            dataStyleFloat.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            HSSFDataFormat df = workbook.createDataFormat();
            dataStyleFloat.setDataFormat(df.getFormat("#,##0.00"));

            HSSFCellStyle summaryCellStyle = workbook.createCellStyle();
            summaryCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
            summaryCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            summaryCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            summaryCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            summaryCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            summaryCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            summaryCellStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
            summaryCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

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
            cell.setCellValue(marketingName);
            cell = sheet.getRow(8).getCell(1);
            cell.setCellValue(campaignName);
            cell = sheet.getRow(9).getCell(1);
            cell.setCellValue(productName);

            retrieveData(row, cell, sheet, dataStyle, dataStyleFloat, summaryCellStyle, workbook);

            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TotalSalesReport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(TotalSalesReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void retrieveData(HSSFRow row, HSSFCell cell, HSSFSheet sheet, HSSFCellStyle dataStyle, HSSFCellStyle dataStyleFloat, HSSFCellStyle summaryCellStyle, HSSFWorkbook workbook) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(this.getExcelQuery());
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCol = rsmd.getColumnCount();
            int numStartRow = 12;
            if (rs.next()) {
                do {
                    row = sheet.createRow(numStartRow);
                    for (int i = 1; i <= numCol; i++) {
                        cell = row.createCell(i - 1);
                        cell.setCellStyle(dataStyle);
                        if (rs.getString(i) != null && !rs.getString(i).equalsIgnoreCase("null") && !rs.getString(i).isEmpty()) {
                            if (i == 2 || i == 18 || i == 21 || i == 22) {
                                cell.setCellStyle(dataStyle);
                                cell.setCellValue(sdf1.format(rs.getDate(i)));
                            } else if ((i == 10) || (i == 11)) {
                                cell.setCellStyle(dataStyle);
                                cell.setCellValue(df.format(rs.getDouble(i)));
                            } else if (i == 12) {
                                cell.setCellStyle(dataStyle);
                                cell.setCellValue(rs.getString(i));
                                /*
                                if (rs.getString(i).equalsIgnoreCase("VC")) {
                                    cell.setCellStyle(dataStyle);
                                    cell.setCellValue("Visa Card");
                                } else if (rs.getString(i).equalsIgnoreCase("MC")) {
                                    cell.setCellStyle(dataStyle);
                                    cell.setCellValue("Master Card");
                                } else if (rs.getString(i).equalsIgnoreCase("OC")) {
                                    cell.setCellStyle(dataStyle);
                                    cell.setCellValue("Other Card");
                                } else if (rs.getString(i).equalsIgnoreCase("DC")) {
                                    cell.setCellStyle(dataStyle);
                                    cell.setCellValue("Debit Card");
                                } else if (rs.getString(i).equalsIgnoreCase("DD")) {
                                    cell.setCellStyle(dataStyle);
                                    cell.setCellValue("Direct Debit");
                                } else if (rs.getString(i).equalsIgnoreCase("CASH")) {
                                    cell.setCellStyle(dataStyle);
                                    cell.setCellValue("เงินสด");
                                } else if (rs.getString(i).equalsIgnoreCase("RC")) {
                                    cell.setCellStyle(dataStyle);
                                    cell.setCellValue("CTB Ready Credit");
                                } else if (rs.getString(i).equalsIgnoreCase("DA")) {
                                    cell.setCellStyle(dataStyle);
                                    cell.setCellValue("ลูกค้าบัญชีเดิม");
                                } else if (rs.getString(i).equalsIgnoreCase("DP")) {
                                    cell.setCellStyle(dataStyle);
                                    cell.setCellValue("DDOP หักบัญชีอัตโนมัติ");
                                } else {
                                    cell.setCellStyle(dataStyle);
                                    cell.setCellValue("-");
                                }
                                */
                            } else {
                                cell.setCellStyle(dataStyle);
                                cell.setCellValue(rs.getString(i));
                            }
                        } else {
                            cell.setCellValue("-");
                        }
                    }
                    // calculate sum for AFYP , Premium mode
                    calculateSummation(rs);
                    numStartRow++;
                } while (rs.next());

                //Summation total
                row = sheet.createRow(numStartRow);
                for (int j = 0; j < 9; j++) {
                    cell = row.createCell(j);
                    cell.setCellStyle(summaryCellStyle);
                    cell.setCellValue("");
                }
                int cellInx = 9;
                row.createCell(cellInx).setCellStyle(summaryCellStyle);
                row.getCell(cellInx++).setCellValue(df.format(sumPremiumMode));
                row.createCell(cellInx).setCellStyle(summaryCellStyle);
                row.getCell(cellInx++).setCellValue(df.format(sumAFYP));
                for (int j = 11; j < 22; j++) {
                    cell = row.createCell(j);
                    cell.setCellStyle(summaryCellStyle);
                    cell.setCellValue("");
                }
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

    private void calculateSummation(ResultSet rs) {
        try {
            sumAFYP += (rs.getString(11) != null && !rs.getString(11).isEmpty()) ? rs.getDouble(11) : 0.0;
            sumPremiumMode += (rs.getString(10) != null && !rs.getString(10).isEmpty()) ? rs.getDouble(10) : 0.0;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MediaPlanReport.class.getName()).log(Level.SEVERE, null, ex);
        }
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
