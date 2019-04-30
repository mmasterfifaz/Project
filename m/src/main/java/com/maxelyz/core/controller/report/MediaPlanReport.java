package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.MediaPlanDAO;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellUtil;

@ManagedBean
@RequestScoped
public class MediaPlanReport extends AbstractReportBaseController {

    private static Logger log = Logger.getLogger(MediaPlanReport.class);
    private SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
    private String fileName = "";
    private Double abandonRate = 0.0, contactableRate = 0.0, responseRate = 0.0, appConversionRate = 0.0, costPerLead = 0.0, costPerSale = 0.0,responseAARP = 0.0, costPerCollection = 0.0;
    private Double sumNetPerSpot = 0.0, sumAbandonRate = 0.0, sumContactableRate = 0.0, sumAppResponseRate = 0.0, sumRespopnseTarp = 0.0, sumResponseAarp = 0.0, sumConvertApp = 0.0, sumConvertTarp = 0.0, sumCostPerLead = 0.0, sumCostPerSale = 0.0;
    private Integer sumCopyLength = 0,sumInbound = 0, sumVoiceBox = 0, sumAbandon = 0, sumAcceptedCall = 0, sumHotTrans = 0, sumQuali = 0, sumUnquali = 0, sumOnline = 0, sumReferred = 0, sumDataDup = 0, sumDataBlackList = 0, sumExclusion = 0, sumEffective = 0, sumLeadUsed = 0, sumContactable = 0, sumAppResponse = 0, sumAppConversion = 0, appConversion = 0, appResponse = 0;
    DecimalFormat df = new DecimalFormat("#,##0.00");
    DecimalFormat df1 = new DecimalFormat("#,###");
    private String spotType = "";

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:mediaplanreport:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/contactOutcome.jasper";
    }

    @Override
    public Map getParameterMap() {
        Map parameterMap = new LinkedHashMap();
        return parameterMap;
    }

    @Override
    public String getQuery() {
//        Query q = em.createNativeQuery("{call mediaPlan(:fromdate ,:todate,:program_name,:spot_ref_id,:spot_type)}");
//        q.setParameter("fromdate", sdf.format(fromDate));
//        q.setParameter("todate", sdf.format(toDate));
//        q.setParameter("program_name",programName );
//        q.setParameter("spot_ref_id",spotRefId );
//        q.setParameter("spot_type",spotType );
//        Object o = q.getSingleResult();
        String select = "SELECT spot_ref_id, day_of_spot, date_of_spot, spot_telephone_number, channel,spot_type, program_name, program_type, copy_length, net_cost_per_spot,  "
                + "total_inbound_call, voice_box, abandon, accepted_call, unqualified_leads, qualified_leads, "
                + "(SELECT COUNT(ad.id) AS Expr1 FROM assignment_detail AS ad INNER JOIN customer AS c ON ad.customer_id = c.id WHERE (c.flexfield15 LIKE mp.spot_ref_id) AND (c.flexfield20 like 'R')) AS 'referred_leads', "
                + " (SELECT COUNT(ad.id) AS Expr1 FROM assignment_detail AS ad INNER JOIN customer AS c ON ad.customer_id = c.id WHERE (c.flexfield15 LIKE mp.spot_ref_id) AND (c.flexfield20 like 'O')) AS 'online_leads', "
                + "(SELECT COUNT(id) AS Expr1 FROM marketing_temp_customer AS m WHERE (dup = 1) AND (flexfield15 = mp.spot_ref_id)) AS 'Data duplication', "
                + "(SELECT COUNT(id) AS Expr1 FROM marketing_temp_customer AS m WHERE (dnc = 1) AND (mib = 1) AND (flexfield15 = mp.spot_ref_id)) AS 'Data black list', "
                + "(SELECT COUNT(id) AS Expr1 FROM marketing_temp_customer AS m WHERE (dnc = 1) AND (mib = 1) AND (flexfield15 = mp.spot_ref_id) OR (flexfield15 = mp.spot_ref_id) AND (dup = 1)) AS 'Exclusion (data duplication and blacklist)', "
                + "(SELECT COUNT(id) AS Expr1 FROM marketing_temp_customer AS m WHERE (marketing_id IS NOT NULL) AND (flexfield15 = mp.spot_ref_id)) AS 'Effective Leads (ready for sales)', "
                + "(SELECT COUNT(ad.id) AS Expr1 FROM assignment_detail AS ad INNER JOIN customer AS c ON ad.customer_id = c.id WHERE (c.flexfield15 LIKE mp.spot_ref_id)) AS 'Lead Used', "
                + "(SELECT COUNT(ad.id) AS Expr1 FROM assignment_detail AS ad INNER JOIN customer AS c ON ad.customer_id = c.id WHERE (c.flexfield15 LIKE mp.spot_ref_id) AND (ad.dmc = 1)) AS 'Contactable', "
                + "(SELECT COUNT(pod.id) AS Expr1 FROM purchase_order_detail AS pod INNER JOIN purchase_order AS po ON pod.purchase_order_id = po.id INNER JOIN customer AS c ON po.customer_id = c.id WHERE (c.flexfield15 LIKE mp.spot_ref_id) AND (po.qc_status LIKE 'approved') and po.yes_flag = 1) AS 'App Response', "
                + "(SELECT SUM(po.annual_net_premium) AS Expr1 FROM purchase_order AS po INNER JOIN customer AS c ON po.customer_id = c.id WHERE (c.flexfield15 LIKE mp.spot_ref_id) AND (po.qc_status = 'approved') and po.yes_flag = 1) AS 'Response TARP', "
                + "(SELECT COUNT(pod.id) AS Expr1 FROM purchase_order_detail AS pod INNER JOIN purchase_order AS po ON pod.purchase_order_id = po.id INNER JOIN customer AS c ON po.customer_id = c.id WHERE (c.flexfield15 LIKE mp.spot_ref_id) AND (po.qc_status LIKE 'approved') and po.yes_flag = 1 AND (po.settlement = 1)) AS 'App Conversion', "
                + "(SELECT SUM(po.annual_net_premium) AS Expr1 FROM purchase_order AS po INNER JOIN customer AS c ON po.customer_id = c.id WHERE (c.flexfield15 LIKE mp.spot_ref_id) AND (po.qc_status = 'approved') and po.yes_flag = 1 AND (po.settlement = 1)) AS 'Converted TARP' "
                + "FROM media_plan AS mp ";

        String where = "WHERE (date_of_spot BETWEEN '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "') ";
        if (programName != null && !programName.isEmpty() && !programName.equalsIgnoreCase("All")) {
            where += "and program_name like  '" + programName + "' ";
        }
        if (spotRefId != null && !spotRefId.isEmpty() && !spotRefId.equalsIgnoreCase("All")) {
            where += "and spot_ref_id like '" + spotRefId + "' ";
        }
        if (spotType != null && !spotType.isEmpty()) {
            where += "and spot_type like '" + spotType + "%' ";
        }

//        String groupBy = "GROUP BY cr.contact_status, cr.name, seq_no ";
        String orderBy = "ORDER BY date_of_spot	";
        String query = select + where + orderBy;
        System.out.println("DRTV Report Query : " + query);

        return query;
    }

    public void reportListener(ActionEvent event) {
        generateXLS();
        FacesContext.getCurrentInstance().responseComplete();
    }

    private void initSummation() {
        sumCopyLength = 0;
        sumNetPerSpot = 0.0;
        sumAbandonRate = 0.0;
        sumContactableRate = 0.0;
        sumAppResponseRate = 0.0;
        sumRespopnseTarp = 0.0;
        sumResponseAarp = 0.0;
        sumConvertApp = 0.0;
        sumConvertTarp = 0.0;
        sumCostPerLead = 0.0;
        sumCostPerSale = 0.0;
        sumInbound = 0;
        sumVoiceBox = 0;
        sumAbandon = 0;
        sumAcceptedCall = 0;
        sumHotTrans = 0;
        sumQuali = 0;
        sumUnquali = 0;
        sumOnline = 0;
        sumReferred = 0;
        sumDataDup = 0;
        sumDataBlackList = 0;
        sumExclusion = 0;
        sumEffective = 0;
        sumLeadUsed = 0;
        sumContactable = 0;
        sumAppResponse = 0;
        sumAppConversion = 0;
        abandonRate = 0.0;
        contactableRate = 0.0;
        responseRate = 0.0;
        appConversion = 0;
        appConversionRate = 0.0;
        costPerLead = 0.0;
        costPerSale = 0.0;
        responseAARP = 0.0;
        appResponse = 0;
        costPerCollection = 0.0;
    }

    private void generateXLS() {
        initSummation();
        FileInputStream file = null;
        Date d = new Date();
        String start = sdf2.format(d);
        fileName = "DRTV_Report_" + start + ".xls";
        try {
            file = new FileInputStream(new File(JSFUtil.getuploadPath(), "report" + "/DRTVMediaplan/DRTV_report_mediaplan_template.xls"));
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFCellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderTop(CellStyle.BORDER_THIN);
            dataStyle.setBorderRight(CellStyle.BORDER_THIN);
            dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
            dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
            dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
            dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("application/vnd.ms-excel");
            FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            //Fill Header
            HSSFRow row;
            HSSFCell cell;
            row = sheet.getRow(5);
            cell = row.createCell(1);
            String mediaDate = sdf1.format(fromDate) + " - " + sdf1.format(toDate);
            cell.setCellValue(mediaDate);
            cell = sheet.getRow(6).createCell(1);
            cell.setCellValue(programName);
            cell = sheet.getRow(7).createCell(1);
            cell.setCellValue(spotRefId);

            retrieveData(workbook, sheet);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
            FacesContext.getCurrentInstance().responseComplete();
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

    public void retrieveData(HSSFWorkbook workbook, HSSFSheet sheet) throws SQLException {
        HSSFRow dataRow;
        HSSFCell dataCell;

        HSSFCellStyle detailCellStyle = workbook.createCellStyle();
        detailCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailCellStyle.setBorderLeft(CellStyle.BORDER_THIN);

        HSSFCellStyle summaryCellStyle = workbook.createCellStyle();
        summaryCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        summaryCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        summaryCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        summaryCellStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        summaryCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
//            rs = statement.executeQuery(this.getExcelQuery());
            rs = statement.executeQuery("{call mediaPlan("+"'"+sdf.format(fromDate)+"','"+sdf.format(toDate)+"','"+ programName +"','"+ spotRefId +"','"+spotType+"')}");
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCol = rsmd.getColumnCount();
            int numStartRow = 10;
            int cellInx = 0;
            if (rs.next()) {
                do {
                    dataRow = sheet.createRow(numStartRow);
                    cellInx = 0;
                    for (int i = 1; i <= numCol; i++) {
                        dataCell = dataRow.createCell(cellInx);
                        dataCell.setCellStyle(detailCellStyle);
                        if (i < 9) {
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "-";
                            dataCell.setCellValue(d);
                        }
                        if ((i == 10) || (i == 26) || (i == 28)) {
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? df.format(rs.getDouble(i)) : "0.00";
                            dataCell.setCellValue(d);
                        }
                        if ((i == 9) || (i > 10 && i < 26) || (i == 27)) {
                            String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? df1.format(rs.getInt(i)) : "0";
                            dataCell.setCellValue(d);
                        }
                        if (i == 13) {
                            Double total = 0.0, sub = 0.0;
                            abandonRate = 0.0;
                            if (rs.getString(11) != null && !rs.getString(11).isEmpty() && rs.getDouble(11) > 0.0) {
                                total = rs.getDouble(11);
                                if (rs.getString(13) != null && !rs.getString(13).isEmpty()) {
                                    sub = rs.getDouble(13);
                                }
                                abandonRate = (sub / total) * 100;
                            }
                            cellInx++;
                            dataCell = dataRow.createCell(cellInx);
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellValue(df.format(abandonRate) + "%");
                        }
                        if (i == 24) {
                            Double total = 0.0, sub = 0.0;
                            contactableRate = 0.0;
                            if (rs.getString(23) != null && !rs.getString(23).isEmpty() && rs.getDouble(23) > 0.0) {
                                total = rs.getDouble(23);
                                if (rs.getString(24) != null && !rs.getString(24).isEmpty()) {
                                    sub = rs.getDouble(24);
                                }
                                contactableRate = (sub / total) * 100;
                            }
                            cellInx++;
                            dataCell = dataRow.createCell(cellInx);
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellValue(df.format(contactableRate) + "%");
                        }
                        if (i == 25) {
                            Double total = 0.0, sub = 0.0;
                            appResponse = 0;
                            responseRate = 0.0;
                            if (rs.getString(23) != null && !rs.getString(23).isEmpty() && rs.getDouble(23) > 0.0) {
                                total = rs.getDouble(23);
                                if (rs.getString(25) != null && !rs.getString(25).isEmpty()) {
                                    sub = rs.getDouble(25);
                                    appResponse = rs.getInt(25);
                                }
                                responseRate = (sub / total) * 100;
                            }
                            cellInx++;
                            dataCell = dataRow.createCell(cellInx);
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellValue(df.format(responseRate) + "%");
                        }
                        if (i == 26) {
                            Double total = 0.0, sub = 0.0;
                            responseAARP = 0.0;
                            if (rs.getString(25) != null && !rs.getString(25).isEmpty() && rs.getDouble(25) > 0.0) {
                                total = rs.getDouble(25);
                                if (rs.getString(26) != null && !rs.getString(26).isEmpty()) {
                                    sub = rs.getDouble(26);
                                }
                                responseAARP = (sub / total);
                            }
                            cellInx++;
                            dataCell = dataRow.createCell(cellInx);
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellValue(df.format(responseAARP));
                        }
                        ///////////////////////////////////////////
                        if (i == 27) {
                            Double total = 0.0, sub = 0.0;
                            appConversion = 0;
                            appConversionRate = 0.0;
                            if (rs.getString(25) != null && !rs.getString(25).isEmpty() && rs.getDouble(25) > 0.0) {
                                total = rs.getDouble(25);
                                if (rs.getString(27) != null && !rs.getString(27).isEmpty()) {
                                    sub = rs.getDouble(27);
                                }
                                appConversion = rs.getInt(27);
                                appConversionRate = (sub / total) * 100;
                            }
                            dataCell = dataRow.createCell(cellInx);
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellValue(df1.format(appConversion));
                            cellInx++;
                            dataCell = dataRow.createCell(cellInx);
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellValue(df.format(appConversionRate) + "%");
                        }
                        if (i == 28) {
                            Double total = 0.0, sub = 0.0;
                            costPerLead = 0.0;
                            costPerSale = 0.0;
                            costPerCollection = 0.0;
                            if (rs.getString(23) != null && !rs.getString(23).isEmpty() && rs.getDouble(23) > 0.0) {
                                total = rs.getDouble(23);
                                if (rs.getString(10) != null && !rs.getString(10).isEmpty()) {
                                    sub = rs.getDouble(10);
                                }
                                costPerLead = (sub / total);
                            }
                            cellInx++;
                            dataCell = dataRow.createCell(cellInx);
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellValue(df.format(costPerLead));
                            if (appResponse > 0.0) {
                                costPerSale = sub / appResponse;
                            }
                            cellInx++;
                            dataCell = dataRow.createCell(cellInx);
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellValue(df.format(costPerSale));
                            if (appConversion > 0.0) {
                                costPerCollection = sub / appConversion;
                            }
                            cellInx++;
                            dataCell = dataRow.createCell(cellInx);
                            dataCell.setCellStyle(detailCellStyle);
                            dataCell.setCellValue(df.format(costPerCollection));
                        }
                        cellInx++;
                    }
                    //Calculate Sum for each column
                    calculateSummation(rs);
                    numStartRow++;
                } while (rs.next());

                //Summation total
                dataRow = sheet.createRow(numStartRow);
                for (int j = 0; j < 8; j++) {
                    dataCell = dataRow.createCell(j);
                    dataCell.setCellStyle(summaryCellStyle);
                    dataCell.setCellValue("");
                }
                cellInx = 8;
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumCopyLength));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df.format(sumNetPerSpot));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumInbound));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumVoiceBox));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumAbandon));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue((sumInbound > 0) ? df.format((new Double(sumAbandon) / new Double(sumInbound)) * 100.00) + "%" : df.format(sumInbound) + "%");
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumAcceptedCall));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumUnquali));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumQuali));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumReferred));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumOnline));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumDataDup));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumDataBlackList));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumExclusion));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumEffective));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumLeadUsed));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumContactable));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue((sumLeadUsed > 0) ? df.format((new Double(sumContactable) / new Double(sumLeadUsed)) * 100.0) + "%" : df.format(sumLeadUsed) + "%");
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumAppResponse));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue((sumLeadUsed > 0) ? df.format((new Double(sumAppResponse) / new Double(sumLeadUsed)) * 100.0) + "%" : df.format(sumLeadUsed) + "%");
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df.format(sumRespopnseTarp));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue((sumAppResponse > 0) ? df.format(new Double(sumRespopnseTarp) / new Double(sumAppResponse)) : df.format(sumAppResponse));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df1.format(sumAppConversion));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue((sumAppResponse > 0) ? df.format((new Double(sumAppConversion) / new Double(sumAppResponse)) * 100.0) + "%" : df.format(sumAppResponse) + "%");
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                dataRow.getCell(cellInx++).setCellValue(df.format(sumConvertTarp));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                //dataRow.getCell(cellInx++).setCellValue((sumEffective > 0) ? df.format((new Double(sumInbound) / new Double(sumEffective))) : df.format(sumEffective));
                dataRow.getCell(cellInx++).setCellValue((sumLeadUsed > 0) ? df.format((new Double(sumNetPerSpot) / new Double(sumLeadUsed))) : df.format(sumLeadUsed));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                //dataRow.getCell(cellInx++).setCellValue((sumAppResponse > 0) ? df.format((new Double(sumInbound) / new Double(sumAppResponse))) : df.format(sumAppResponse));
                dataRow.getCell(cellInx++).setCellValue((sumAppResponse > 0) ? df.format((new Double(sumNetPerSpot) / new Double(sumAppResponse))) : df.format(sumAppResponse));
                dataRow.createCell(cellInx).setCellStyle(summaryCellStyle);
                //dataRow.getCell(cellInx++).setCellValue((sumAppConversion > 0) ? df.format((new Double(sumInbound) / new Double(sumAppConversion))) : df.format(sumAppConversion));
                dataRow.getCell(cellInx++).setCellValue((sumAppConversion > 0) ? df.format((new Double(sumNetPerSpot) / new Double(sumAppConversion))) : df.format(sumAppConversion));
                
            }
        } catch (Exception e) {
            System.out.println(e);
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

    private void calculateSummation(ResultSet rs) {
        try {
            sumCopyLength += (rs.getString(9) != null && !rs.getString(9).isEmpty()) ? rs.getInt(9) : 0;
            sumNetPerSpot += (rs.getString(10) != null && !rs.getString(10).isEmpty()) ? rs.getDouble(10) : 0.0;
            sumInbound += (rs.getString(11) != null && !rs.getString(11).isEmpty()) ? rs.getInt(11) : 0;
            sumVoiceBox += (rs.getString(12) != null && !rs.getString(12).isEmpty()) ? rs.getInt(12) : 0;
            sumAbandon += (rs.getString(13) != null && !rs.getString(13).isEmpty()) ? rs.getInt(13) : 0;
            sumAcceptedCall += (rs.getString(14) != null && !rs.getString(14).isEmpty()) ? rs.getInt(14) : 0;
//            sumHotTrans += (rs.getString(14) != null && !rs.getString(14).isEmpty()) ? rs.getInt(14) : 0;
            sumUnquali += (rs.getString(15) != null && !rs.getString(15).isEmpty()) ? rs.getInt(15) : 0;
            sumQuali += (rs.getString(16) != null && !rs.getString(16).isEmpty()) ? rs.getInt(16) : 0;
            sumReferred += (rs.getString(17) != null && !rs.getString(17).isEmpty()) ? rs.getInt(17) : 0;
            sumOnline += (rs.getString(18) != null && !rs.getString(18).isEmpty()) ? rs.getInt(18) : 0;
            sumDataDup += (rs.getString(19) != null && !rs.getString(19).isEmpty()) ? rs.getInt(19) : 0;
            sumDataBlackList += (rs.getString(20) != null && !rs.getString(20).isEmpty()) ? rs.getInt(20) : 0;
            sumExclusion += (rs.getString(21) != null && !rs.getString(21).isEmpty()) ? rs.getInt(21) : 0;
            sumEffective += (rs.getString(22) != null && !rs.getString(22).isEmpty()) ? rs.getInt(22) : 0;
            sumLeadUsed += (rs.getString(23) != null && !rs.getString(23).isEmpty()) ? rs.getInt(23) : 0;
            sumContactable += (rs.getString(24) != null && !rs.getString(24).isEmpty()) ? rs.getInt(24) : 0;
            sumAppResponse += (rs.getString(25) != null && !rs.getString(25).isEmpty()) ? rs.getInt(25) : 0;
            sumRespopnseTarp += (rs.getString(26) != null && !rs.getString(26).isEmpty()) ? rs.getDouble(26) : 0.0;
//            sumResponseAarp += (rs.getString(26) != null && !rs.getString(26).isEmpty()) ? rs.getInt(26) : 0;
            sumAppConversion += (rs.getString(27) != null && !rs.getString(27).isEmpty()) ? rs.getInt(27) : 0;
            sumConvertTarp += (rs.getString(28) != null && !rs.getString(28).isEmpty()) ? rs.getDouble(28) : 0.0;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MediaPlanReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
    
    public String getSpotType() {
        return spotType;
    }

    public void setSpotType(String spotType) {
        this.spotType = spotType;
    }     
}
