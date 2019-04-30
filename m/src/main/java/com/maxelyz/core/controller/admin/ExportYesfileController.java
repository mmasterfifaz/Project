/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.maxelyz.converter.PaymentModeConverter;
import com.maxelyz.core.common.dto.PaymentMethodStringCastor;
import com.maxelyz.core.controller.admin.dto.StringTemplateDTO;
import com.maxelyz.core.model.dao.ApprovalReasonDAO;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.DocumentDAO;
import com.maxelyz.core.model.dao.DocumentUploadTypeDAO;
import com.maxelyz.core.model.dao.ProductSponsorDAO;
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.dao.YesLogDAO;
import com.maxelyz.core.model.dao.PaymentMethodDAO;
import com.maxelyz.core.model.dao.ProductDocumentDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.utils.*;

import java.io.*;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
//import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.sql.DataSource;

import com.maxelyz.utils.FileUploadBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import fr.opensagres.xdocreport.core.XDocReportException;

import fr.opensagres.xdocreport.template.TemplateEngineKind;
import java.nio.charset.Charset;

import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import org.apache.commons.io.FileUtils;
import javax.swing.JOptionPane;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;
import org.apache.commons.io.IOUtils;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.io.ByteArrayOutputStream;
//import javax.servlet.ServletOutputStream;
import com.itextpdf.text.DocumentException;
import com.maxelyz.core.model.dao.DeclarationFormDAO;
import com.maxelyz.core.model.dao.ProductDeclarationMappingDAO;
import com.maxelyz.test.MergePDF;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Administrator
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class ExportYesfileController {

    private static String REFRESH = "exportyesfile.xhtml?faces-redirect=true";
    private static String EDIT = "exportyesfile.xhtml";
    private static String SELECT = "yeslog.xhtml?faces-redirect=true";
    private static Logger log = Logger.getLogger(ExportYesfileController.class);
    private List<StringTemplateDTO> stringTemplateDTOList;

    //ProductSponsor
//    private ProductSponsor productSponsor;
    private SelectItem[] productSponsorList;
    private List<ProductSponsor> productSponsors;

    //Product
    private SelectItem[] productList;
    private List<Product> products;

    //PaymentMethod
    private SelectItem[] paymentMethodList;
    private List<PaymentMethod> paymentMethods;

    private SelectItem[] approvalReasonList;
    private List<ApprovalReason> approvalReasons;

    private List<Document> mergeDocumentList;
    private SelectItem[] mergeDocuments;

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;

    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;

    @ManagedProperty(value = "#{yesLogDAO}")
    private YesLogDAO yesLogDAO;

    @ManagedProperty(value = "#{productSponsorDAO}")
    private ProductSponsorDAO productSponsorDAO;

    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;

    @ManagedProperty(value = "#{paymentMethodDAO}")
    private PaymentMethodDAO paymentMethodDAO;

    @ManagedProperty(value = "#{approvalReasonDAO}")
    private ApprovalReasonDAO approvalReasonDAO;

    @ManagedProperty(value = "#{documentDAO}")
    private DocumentDAO documentDAO;

    @ManagedProperty(value = "#{productDocumentDAO}")
    private ProductDocumentDAO productDocumentDAO;

    @ManagedProperty(value = "#{documentuploadtypeDAO}")
    private DocumentUploadTypeDAO documentUploadTypeDAO;

    @ManagedProperty(value = "#{declarationFormDAO}")
    private DeclarationFormDAO declarationFormDAO;

    @ManagedProperty(value = "#{productDeclarationMappingDAO}")
    private ProductDeclarationMappingDAO productDeclarationMappingDAO;

    private Date fromDate, toDate;
    private String tableViewYes = "nicecall_po_aia";
    private String tableViewApp = "nicecall_po_app_aia";
    private String exportMode = "new";
    private String generateBy = "date"; //include generate by date/ref_no
    private String listRefNumber;
    private String mergeDocumentData;
    private String messageError;
    private String messageMergeError;
    private Integer productSponsorId;
    private Integer productId;
    private Integer paymentMethodId;
    private Integer lastpaymentApprovedReason;
    private Integer mergedocumentId;
    private Integer usergroupId;
    private Integer formatId;
    private String colName1;
    private String colName2;
    private String colName3;
    private String colName4;
    private String colName5;
    private String colName6;
    private String colName7;
    private String fileNameExtend;
    private String fileNameExtend1;
    private String FILENAME_YES_TITLE = "AIA_YES_File";
    private String FILENAME_APP_TITLE = "AIA_APP_File";
    private String FILENAME_VOICE_TITLE = "AIA_VOICE_File";
    private static String FILENAME_VOICE = "_LOPOS_Voice_File";
    private String FOLDERNAME_VOICE = "";
    private String refNos;

    private String fileName1;
    private String fileName2;
    private String fileName3;

    private String filePath1;
    private String filePath2;
    private String filePath3;

    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> surname = new ArrayList<String>();

    private String errorDetail = "";
    private String excelDetail = "";

    private Integer cntRecords;
    private Date executeDate;

    private MergePDF mergePDF;

    private static final int NUM_COLUMN_SEARCH = 7;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:exportyesfile:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        Date today = new Date();

        fromDate = today;
        toDate = today;

        formatId = null;

//        mergeDocumentList = documentDAO.getMergeDocument(new Integer(85));
        this.setProductList();

        this.setApprovalReasonList();
        this.paymentMethods = paymentMethodDAO.findPaymentMethodEntities();
        if (this.paymentMethods == null) {
            this.paymentMethods = new ArrayList<PaymentMethod>();
        }
        this.initialPaymentMethodList();

    }

    private void initialPaymentMethodList() {
        this.paymentMethodList = new SelectItem[1];
        paymentMethodList[0] = new SelectItem(null, "Select PaymentMethod");
        this.paymentMethodId = null;
    }

    private void writeCol(Row row, int columnNo, String value, CellStyle style) {
        Cell cell = row.createCell(columnNo); //begin at 0
        //cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(style);
        cell.setCellValue(value);

    }

    private String genSQLDeclare(String tableView, String orderField, Integer productSponsorId, Integer productId, Integer lastpaymentApprovedReason, Boolean genRank, String fixfield, String ref, String fieldRefNo, String listRefNumber) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(fromDate); 
//        cal.add(Calendar.YEAR, 543); 
//        Date fromDate1 = cal.getTime();
//        cal.setTime(toDate); 
//        cal.add(Calendar.YEAR, 543); 
//        Date toDate1 = cal.getTime();
//        
//        String sqlFromDate = sdf1.format(fromDate1);
//        String sqlToDate = sdf1.format(toDate1);

        String sqlFromDate = sdf1.format(fromDate) + " 00:00:00";
        String sqlToDate = sdf1.format(toDate) + " 23:59:59";

        /*TODO-tai*/
        String sql = "";

        if (this.listRefNumber == null) {
            if (genRank) {
                //sql = "SELECT SUBSTRING(REFERENCE_NO,6,5) AS SequenceNo, * FROM " + tableView;
                sql = "SELECT ROW_NUMBER() OVER(ORDER BY " + orderField + ") RUNNING_NO, * FROM " + tableView;
            } else if (StringUtils.isNotBlank(fixfield)) {
                sql = "SELECT " + fixfield + " FROM " + tableView;
            } else {
                sql = "SELECT * FROM " + tableView;
            }
            sql += " where sale_date between '" + sqlFromDate + "'";
            sql += " and '" + sqlToDate + "'";

            if (exportMode.equals("new")) {
                sql += " and (yes_flag is null or yes_flag = 0)";
            }

            if (productSponsorId != null) {
                sql += " and product_id_1 IN (SELECT id FROM product WHERE product_sponsor_id=" + productSponsorId.toString() + ")";
            }

            if (productId != null) {
                sql += " and product_id_1 = " + productId.toString();
            }

            if (paymentMethodId != null) {
                sql += " and payment_method_1 = " + paymentMethodId.toString();
            }

            if (lastpaymentApprovedReason != null && lastpaymentApprovedReason != 0) {
                sql += " and last_payment_approved_reason = " + lastpaymentApprovedReason.toString();
            }

            if (ref != null && ref != "" && StringUtils.isNotBlank(orderField)) {
                sql += " and " + orderField + " = " + MxString.quotedStrEmpty(ref);
            }

            if (StringUtils.isNotBlank(orderField)) {
                sql += " order by " + orderField;
            }
        } else {
            if (genRank) {
                //sql = "SELECT SUBSTRING(REFERENCE_NO,6,5) AS SequenceNo, * FROM " + tableView;
                sql = "SELECT ROW_NUMBER() OVER(ORDER BY " + orderField + ") RUNNING_NO, * FROM " + tableView;
            } else if (StringUtils.isNotBlank(fixfield)) {
                sql = "SELECT " + fixfield + " FROM " + tableView;
            } else {
                sql = "SELECT * FROM " + tableView;
            }

            if (listRefNumber != null) {
                sql += " where " + fieldRefNo + " in ('" + listRefNumber + "')";
            }

            if (StringUtils.isNotBlank(orderField)) {
                sql += " order by " + orderField;
            }
        }
        //System.out.println(sql);
        return sql;
    }

    private String genSQLEX(String tableView, String orderField, Integer productSponsorId, Integer productId, Integer lastpaymentApprovedReason, Boolean genRank, String fixfield, String fieldRefNo, String listRefNumber) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String sqlFromDate = sdf1.format(fromDate) + " 00:00:00";
        String sqlToDate = sdf1.format(toDate) + " 23:59:59";

        /*TODO-tai*/
        String sql = "";

        if (listRefNumber == null) {
            if (genRank) {
                //sql = "SELECT SUBSTRING(REFERENCE_NO,6,5) AS SequenceNo, * FROM " + tableView;
                sql = "SELECT ROW_NUMBER() OVER(ORDER BY " + orderField + ") RUNNING_NO, * FROM " + tableView;
            } else if (StringUtils.isNotBlank(fixfield)) {
                sql = "SELECT " + fixfield + " FROM " + tableView;
            } else {
                sql = "SELECT * FROM " + tableView;
            }
            sql += " where sale_date_1 between '" + sqlFromDate + "'";
            sql += " and '" + sqlToDate + "'";

            if (exportMode.equals("new")) {
                sql += " and (yes_flag is null or yes_flag = 0)";
            }

            if (productSponsorId != null) {
                sql += " and product_id_1 IN (SELECT id FROM product WHERE product_sponsor_id=" + productSponsorId.toString() + ")";
            }

            if (productId != null) {
                sql += " and product_id_1 = " + productId.toString();
            }

            if (paymentMethodId != null) {
                sql += " and payment_method_1 = " + paymentMethodId.toString();
            }

            if (lastpaymentApprovedReason != null && lastpaymentApprovedReason != 0) {
                sql += " and last_payment_approved_reason = " + lastpaymentApprovedReason.toString();
            }

            if (StringUtils.isNotBlank(orderField)) {
                sql += " order by " + orderField;
            }
        } else {
            if (genRank) {
                //sql = "SELECT SUBSTRING(REFERENCE_NO,6,5) AS SequenceNo, * FROM " + tableView;
                sql = "SELECT ROW_NUMBER() OVER(ORDER BY " + orderField + ") RUNNING_NO, * FROM " + tableView;
            } else if (StringUtils.isNotBlank(fixfield)) {
                sql = "SELECT " + fixfield + " FROM " + tableView;
            } else {
                sql = "SELECT * FROM " + tableView;
            }

            if (listRefNumber != null) {
                sql += " where " + fieldRefNo + " in ('" + listRefNumber + "')";
            }

            if (StringUtils.isNotBlank(orderField)) {
                sql += " order by " + orderField;
            }
        }
        //System.out.println(sql);
        return sql;
    }

    private String genSQL(String tableView, String orderField, Integer productSponsorId, Integer productId, Integer lastpaymentApprovedReason, Boolean genRank, String fixfield, String fieldRefNo, String listRefNumber) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String sqlFromDate = sdf1.format(fromDate) + " 00:00:00";
        String sqlToDate = sdf1.format(toDate) + " 23:59:59";

        /*TODO-tai*/
        String sql = "";

        if (listRefNumber == null) {
            if (genRank) {
                //sql = "SELECT SUBSTRING(REFERENCE_NO,6,5) AS SequenceNo, * FROM " + tableView;
                sql = "SELECT ROW_NUMBER() OVER(ORDER BY " + orderField + ") RUNNING_NO, * FROM " + tableView;
            } else if (StringUtils.isNotBlank(fixfield)) {
                sql = "SELECT " + fixfield + " FROM " + tableView;
            } else {
                sql = "SELECT * FROM " + tableView;
            }
            sql += " where sale_date_1 between '" + sqlFromDate + "'";
            sql += " and '" + sqlToDate + "'";

            if (exportMode.equals("new")) {
                sql += " and (yes_flag is null or yes_flag = 0)";
            }

            if (productSponsorId != null) {
                sql += " and product_id_1 IN (SELECT id FROM product WHERE product_sponsor_id=" + productSponsorId.toString() + ")";
            }

            if (productId != null) {
                sql += " and product_id_1 = " + productId.toString();
            }

            if (paymentMethodId != null) {
                sql += " and payment_method_1 = " + paymentMethodId.toString();
            }

            if (lastpaymentApprovedReason != null && lastpaymentApprovedReason != 0) {
                sql += " and last_payment_approved_reason = " + lastpaymentApprovedReason.toString();
            }

            if (StringUtils.isNotBlank(orderField)) {
                sql += " order by " + orderField;
            }

        } else {
            if (genRank) {
                //sql = "SELECT SUBSTRING(REFERENCE_NO,6,5) AS SequenceNo, * FROM " + tableView;
                sql = "SELECT ROW_NUMBER() OVER(ORDER BY " + orderField + ") RUNNING_NO, * FROM " + tableView;
            } else if (StringUtils.isNotBlank(fixfield)) {
                sql = "SELECT " + fixfield + " FROM " + tableView;
            } else {
                sql = "SELECT * FROM " + tableView;
            }

            if (listRefNumber != null) {
                sql += " where " + fieldRefNo + " in ('" + listRefNumber + "')";
            }

            if (StringUtils.isNotBlank(orderField)) {
                sql += " order by " + orderField;
            }
        }
        return sql;
    }

    private String splitTextRefNo(String listRefNumber) {
        String listrefno = "";
        String splitListRefNo = "";
        listrefno = listRefNumber;
        StringTokenizer st = new StringTokenizer(listrefno, "\n ");
        while (st.hasMoreTokens()) {
            String refnoResult = st.nextToken();
            splitListRefNo += "'" + refnoResult + "',";
        }
        splitListRefNo = splitListRefNo.substring(0, splitListRefNo.length() - 1);
        return splitListRefNo;
    }
//    private genMerge(Integer mergedocumentid)
//    {
//        String sql="";
//        
//        sql = "Select M"
//    }
    //    private void generateTXTByView(Integer reportType) {

    private void generateTXTByView(Integer reportType, String sql, String fileName, String fieldRefNo, Boolean isHeader, String seperator) {
        Calendar now = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
//        DecimalFormat dec = new DecimalFormat("###0.00");

        String dirPath = createBaseUploadPath2();

        String physicalName = "";
//        String sql = "";

//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
/*
        if (reportType == 1) {
//            response.setHeader("Content-Disposition", "Attachment;filename=yesfile" + sdfFileName.format(new Date()) + ".xls");
//            excelPhysicalName = dirPath + "yesfile" + fileNameExtend + ".xls";
            physicalName = dirPath + FILENAME_YES_TITLE + fileNameExtend + ".txt";
            sql = genSQL(tableViewYes,"POLICY_NUMBER");
        } else if (reportType == 2) {
//            response.setHeader("Content-Disposition", "Attachment;filename=application" + sdfFileName.format(new Date()) + ".xls");
//            excelPhysicalName = dirPath + "application" + fileNameExtend + ".xls";
            physicalName = dirPath + FILENAME_APP_TITLE + fileNameExtend + ".txt";
            sql = genSQL(tableViewApp,"ref_no");
        }
         */
//        physicalName = dirPath + fileName + fileSuffix + "." + fileExt;
        physicalName = dirPath + fileName;

        File file;
        //FileOutputStream fileOut = null;
        Writer out = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();

            file = new File(physicalName);
            //fileOut = new FileOutputStream(physicalName);
            out = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file), "Windows-874"));
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // Create a row and put some cells in it. Rows are 0 based.
            int columnNo = 0;
            int rowNo = 0;

            String content = "";
            //byte[] contentInBytes;

            if (isHeader) {
                for (int i = 1; i <= columnCount - NUM_COLUMN_SEARCH; i++) {
                    if (!StringUtils.isEmpty(content)) {
//                        content += "\t";
                        content += seperator;
                    }
                    content += metadata.getColumnName(i);
                }
//                contentInBytes = content.getBytes();
//                fileOut.write(contentInBytes);
//                fileOut.write("\r\n".getBytes());
                out.append(content);
                out.append("\r\n");
            }

            refNos = "";
            while (rs.next()) {
                content = "";
                for (int i = 1; i <= columnCount - NUM_COLUMN_SEARCH; i++) {
                    if (!StringUtils.isEmpty(content)) {
//                        content += "\t";
                        content += seperator;
                    }

                    if (rs.getString(i) == null) {
                        content += "";
                    } else {
                        content += rs.getString(i).trim();
                    }

                }
                //contentInBytes = content.getBytes();
                //fileOut.write(contentInBytes);
                //fileOut.write("\r\n".getBytes());
                out.append(content);
                out.append("\r\n");

                /*TODO-tai*/
//                System.out.println(rs.getString("Proposal_No"));
                if (reportType == 2) {
                    PurchaseOrder po = purchaseOrderDAO.findByRefNo(rs.getString(fieldRefNo));
                    po.setYesFlag(Boolean.TRUE);
                    po.setYesGenBy(JSFUtil.getUserSession().getUserName());
                    po.setYesGenDate(new Date());
                    purchaseOrderDAO.edit(po);
                }

                /*
                if (reportType == 1) {
                    collectVoice(po.getRefNo());
                }
                 */
                if (StringUtils.isNotBlank(refNos)) {
                    refNos += ',';
                }
                refNos += fieldRefNo;
            }

        } catch (Exception e) {
//            throw new ServletException(e.getMessage());
            log.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (Exception e) {
//                }
//            }
            if (out != null) {
                try {
                    //fileOut.close();
                    out.close();
                } catch (Exception e) {
                }
            }
        }
        runningTime("Time(s.) generateTXTByView > "+fileName+" : ", now);
    }

    //    private void generateXLSByView(Integer reportType) {
    private void generateXLSByView(Integer reportType, String sql, String fileName, String fieldRefNo, Boolean isHeader) {
        Calendar now = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
//        DecimalFormat dec = new DecimalFormat("###0.00");

        String dirPath = createBaseUploadPath2();

        String excelPhysicalName = "";
//        String sql = "";

//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
/*
        if (reportType == 1) {
//            response.setHeader("Content-Disposition", "Attachment;filename=yesfile" + sdfFileName.format(new Date()) + ".xls");
//            excelPhysicalName = dirPath + "yesfile" + fileNameExtend + ".xls";
            excelPhysicalName = dirPath + FILENAME_YES_TITLE + fileNameExtend + ".xls";
            sql = genSQL(tableViewYes,"POLICY_NUMBER");
        } else if (reportType == 2) {
//            response.setHeader("Content-Disposition", "Attachment;filename=application" + sdfFileName.format(new Date()) + ".xls");
//            excelPhysicalName = dirPath + "application" + fileNameExtend + ".xls";
            excelPhysicalName = dirPath + FILENAME_APP_TITLE + fileNameExtend + ".xls";
            sql = genSQL(tableViewApp,"ref_no");
        }
         */
//        excelPhysicalName = dirPath + fileName + fileSuffix + "." + fileExt;
        excelPhysicalName = dirPath + fileName;

//        OutputStream out = null;
//        PrintWriter out2 = null;
        FileOutputStream fileOut = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();

//            out = response.getOutputStream();
//            out2 = new PrintWriter("c://Temp//testyes.txt");
            fileOut = new FileOutputStream(excelPhysicalName);

            XSSFWorkbook wb = new XSSFWorkbook();

            /*HEADER SECTION*/
            // Create a new font and alter it.
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 8);
            font.setFontName("Arial");

            // Fonts are set into a style so create a new one to use.
            CellStyle style = wb.createCellStyle();
            style.setFont(font);
            //font.setColNameor((short) 5);

            //Set Border
            style.setBorderBottom((short) 1);
            style.setBorderLeft((short) 1);
            style.setBorderRight((short) 1);
            style.setBorderTop((short) 1);

            XSSFSheet sheet1 = wb.createSheet("Sheet1");
            // Create a row and put some cells in it. Rows are 0 based.
            Row row;
            int columnNo = 0;
            int rowNo = 0;

            if (isHeader) {
                row = sheet1.createRow(rowNo++);
                for (int i = 1; i <= columnCount - NUM_COLUMN_SEARCH; i++) {
                    writeCol(row, columnNo++, metadata.getColumnName(i), style);         //
                }
            }

            /*DETAIL SECTION*/
            Font fontDetail = wb.createFont();
            fontDetail.setFontHeightInPoints((short) 8);
            fontDetail.setFontName("Arial");

            CellStyle styleDetail = wb.createCellStyle();
            styleDetail.setFont(fontDetail);

            DataFormat format = wb.createDataFormat();
            styleDetail.setDataFormat(format.getFormat("@"));

            refNos = "";
            while (rs.next()) {
                row = sheet1.createRow(rowNo++);
                columnNo = 0;
                for (int i = 1; i <= columnCount - NUM_COLUMN_SEARCH; i++) {
                    writeCol(row, columnNo++, rs.getString(i), styleDetail);         //
                }
                /*TODO-tai*/
//                System.out.println(rs.getString("Proposal_No"));
                if (reportType == 2) {
                    PurchaseOrder po = purchaseOrderDAO.findByRefNo(rs.getString(fieldRefNo));
                    po.setYesFlag(Boolean.TRUE);
                    po.setYesGenBy(JSFUtil.getUserSession().getUserName());
                    po.setYesGenDate(new Date());
                    purchaseOrderDAO.edit(po);
                }

                /*
                if (reportType == 1) {
                    collectVoice(po.getRefNo());
                }
                 */
                if (StringUtils.isNotBlank(refNos)) {
                    refNos += ',';
                }
                refNos += rs.getString(fieldRefNo);
            }
            //set autosize column
            for (short i = 0; i < columnNo - 2; i++) {
                sheet1.autoSizeColumn(i);
            }
//            wb.write(out);
            wb.write(fileOut);

        } catch (Exception e) {
//            throw new ServletException(e.getMessage());
            log.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (Exception e) {
//                }
//            }
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (Exception e) {
                }
            }
        }
        runningTime("Time(s.) generateXLSByView > "+fileName+" : ", now);

    }
    
    private void runningTime(String text, Calendar beginTime) {
        System.out.println(new Date()+": " +text+(Calendar.getInstance().getTimeInMillis() - beginTime.getTimeInMillis())/1000);//running time in seconds
    }
    
    public String exportListener() {
        Calendar now = Calendar.getInstance();
        //this for refresh Property name surname
        searchActionListener(null);
        executeDate = new Date();

        //String testsql = genSQL("vwMTL_po_yes_hip", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", listRefNumber);
        //System.out.println("test "+testsql);
        //check type generate by if = refno get productId
        if (generateBy.equals("refno")) {
            try {
                exportMode = "all";
                productId = productDAO.getProductByRefNumber(listRefNumber);
                lastpaymentApprovedReason = 0;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //System.out.println("product id"+productId);
        ArrayList<String> sourceFiles = new ArrayList<String>();
        ArrayList<String> sourceFilesDoc = new ArrayList<String>();
        ArrayList<String> sourceFilesWithDeclare = new ArrayList<String>();
        ArrayList<String> allSourceFilesTemp = new ArrayList<String>();
        ArrayList<String> sourceFilesTarget = new ArrayList<String>();
        String mergedFilePathTemp = "";
        Boolean haveDeclare = false;
        Calendar cal = Calendar.getInstance();

        Product product = productDAO.findProduct(productId);
        String PAYMENT_TITLE = "";
        if (!generateBy.equals("refno")) {
            try {
                PAYMENT_TITLE = paymentMethodDAO.findPaymentMethod(paymentMethodId).getYesFileTitle();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                errorDetail = "error PAYMENT_TITLE";
                return errorDetail;
            }
        }
        else
        {
            try {
                PAYMENT_TITLE = paymentMethodDAO.getPaymentMethodByName(purchaseOrderDAO.findByRefNo(listRefNumber).getPaymentMethodName()).getYesFileTitle();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                errorDetail = "error PAYMENT_TITLE";
                return errorDetail;
            }

        }

        if (product.getProductSponsor().getName().equalsIgnoreCase("MTL") || product.getProductSponsor().getName().equalsIgnoreCase("FA")
                || product.getProductSponsor().getName().equalsIgnoreCase("KBANK") || product.getProductSponsor().getName().equalsIgnoreCase("INH-Kleasing")) {    //FA
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            SimpleDateFormat sdfFileName = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            fileNameExtend = sdfFileName.format(fromDate) + " " + sdf.format(cal.getTime());
            fileNameExtend1 = sdfFileName.format(fromDate);

            FILENAME_YES_TITLE = "Yesfile_" + product.getFx4();
            FILENAME_VOICE_TITLE = "YesfileDetail_" + product.getFx4();
            FILENAME_APP_TITLE = "Appfile_" + product.getFx4();

            fileName1 = FILENAME_YES_TITLE + " " + fileNameExtend1 + "_" + PAYMENT_TITLE + ".txt";
            fileName2 = FILENAME_VOICE_TITLE + " " + fileNameExtend1 + "_" + PAYMENT_TITLE + ".txt";
            fileName3 = FILENAME_APP_TITLE + " " + fileNameExtend + "_" + PAYMENT_TITLE + ".xlsx";

        } else if (product.getProductSponsor().getName().equalsIgnoreCase("SEG")) {     //GNL
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            SimpleDateFormat sdfFileName = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            fileNameExtend = sdfFileName.format(fromDate) + "_" + sdf.format(cal.getTime());
            fileNameExtend1 = sdfFileName.format(fromDate);

            FILENAME_YES_TITLE = "Yesfile_" + product.getFx4();
            FILENAME_VOICE_TITLE = "Yesfile_" + product.getFx4();
            FILENAME_APP_TITLE = "Appfile_" + product.getFx4();

            fileName1 = FILENAME_YES_TITLE + " " + fileNameExtend + ".txt";
            fileName2 = FILENAME_VOICE_TITLE + " " + fileNameExtend + ".xlsx";
            fileName3 = FILENAME_APP_TITLE + " " + fileNameExtend + ".xlsx";

        } else if (product.getProductSponsor().getName().equalsIgnoreCase("FWD")) {     //GNL
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            SimpleDateFormat sdfFileName = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            SimpleDateFormat sdfFolderName = new SimpleDateFormat("yyyy\\MM\\dd\\", Locale.US);
            fileNameExtend = sdfFileName.format(fromDate) + "_" + sdf.format(cal.getTime());
            fileNameExtend1 = sdfFileName.format(fromDate);

            FILENAME_YES_TITLE = "Yesfile_" + product.getFx4();
            FILENAME_VOICE_TITLE = "Yesfile_" + product.getFx4();
            FILENAME_APP_TITLE = "Appfile_" + product.getFx4();

//            FOLDERNAME_VOICE = "FWD\\Voicefile_" + product.getFx4() + " " + fileNameExtend;
            if (StringUtils.isNotBlank(JSFUtil.getApplication().getConvertVoiceFolder())) {
                FOLDERNAME_VOICE = JSFUtil.getApplication().getConvertVoiceFolder() + sdfFolderName.format(new Date()) + "Voicefile_" + product.getFx4() + " " + fileNameExtend;
            } else {
                FOLDERNAME_VOICE = sdfFolderName.format(new Date()) + "Voicefile_" + product.getFx4() + " " + fileNameExtend;
            }

            fileName1 = FILENAME_YES_TITLE + " " + fileNameExtend + ".txt";
            fileName2 = FILENAME_VOICE_TITLE + " " + fileNameExtend + ".xlsx";
            fileName3 = FILENAME_APP_TITLE + " " + fileNameExtend + ".xlsx";

        } else if (product.getProductSponsor().getName().equalsIgnoreCase("GNL")) {     //GNL
            SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            fileNameExtend = sdfFileName.format(new Date());

            FILENAME_YES_TITLE = "YES_" + product.getFx4();
            FILENAME_VOICE_TITLE = "DCR_" + product.getFx4();
            FILENAME_APP_TITLE = "APP_" + product.getFx4();

            fileName1 = FILENAME_YES_TITLE + "_" + fileNameExtend + ".xlsx";
            fileName2 = FILENAME_VOICE_TITLE + "_" + fileNameExtend + ".xlsx";
            fileName3 = FILENAME_APP_TITLE + "_" + fileNameExtend + ".xlsx";
        } else {
            SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            fileNameExtend = sdfFileName.format(new Date());

            FILENAME_YES_TITLE = "yesfile";
            FILENAME_VOICE_TITLE = "dcrfile";
            FILENAME_APP_TITLE = "appfile";

            fileName1 = FILENAME_YES_TITLE + "_" + fileNameExtend + ".xlsx";
            fileName2 = FILENAME_VOICE_TITLE + "_" + fileNameExtend + ".xlsx";
            fileName3 = FILENAME_APP_TITLE + "_" + fileNameExtend + ".xlsx";
        }

        filePath1 = urlUploadPath() + fileName1;
        filePath2 = urlUploadPath() + fileName2;
        filePath3 = urlUploadPath() + fileName3;

        String formatYes = productDAO.getFX11(productId);
        formatYes = StringUtils.isBlank(formatYes) ? "" : formatYes;

        if (formatYes.equals("format1")) {
            generateTXTByView(1, genSQL("vwMTL_po_yes_hip", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwMTL_po_yes_hip_ben", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format5")) {
            generateTXTByView(1, genSQL("vwF5_po_yes_hip", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwF5_po_yes_hip_ben", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format7")) {
            generateTXTByView(1, genSQL("vwF7_po_yes_hip", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwF7_po_yes_hip_ben", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format9")) {
            generateTXTByView(1, genSQL("vwF9_po_yes_hip", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwF9_po_yes_hip_ben", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format11")) {
            generateTXTByView(1, genSQL("vwF11_po_yes_hip", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwF11_po_yes_hip_ben", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format2")) {
            generateTXTByView(1, genSQL("vwMTL_po_yes_hip_fatca", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwMTL_po_yes_hip_ben_fatca", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format6")) {
            generateTXTByView(1, genSQL("vwF6_po_yes_hip_fatca", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwF6_po_yes_hip_ben_fatca", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format8")) {
            generateTXTByView(1, genSQL("vwF8_po_yes_hip_fatca", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwF8_po_yes_hip_ben_fatca", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format10")) {
            generateTXTByView(1, genSQL("vwF10_po_yes_hip_fatca", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwF10_po_yes_hip_ben_fatca", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format12")) {
            generateTXTByView(1, genSQL("vwF12_po_yes_hip_fatca", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwF12_po_yes_hip_ben_fatca", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");
        } else if (formatYes.equals("format13")) {
            generateTXTByView(1, genSQL("vwF13_po_yes_cc", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName1, "ref_no_1", true, "|");
            generateXLSByView(1, genSQL("vwF13_po_yes_cc", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName2, "ref_no_1", true);
        } else if (formatYes.equals("format14")) {
            generateTXTByView(1, genSQL("vwF14_po_yes_pa", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName1, "ref_no_1", true, "|");
            generateXLSByView(1, genSQL("vwF14_po_yes_pa", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName2, "ref_no_1", true);
        } else if (formatYes.equals("format15")) {
            generateTXTByView(1, genSQL("vwF15_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName1, "ref_no_1", true, "|");
            generateXLSByView(1, genSQL("vwF15_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName2, "ref_no_1", true);
        } else if (formatYes.equals("format16")) {
            generateTXTByView(1, genSQL("vwF16_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName1, "ref_no_1", true, "|");
            generateXLSByView(1, genSQL("vwF16_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName2, "ref_no_1", true);
        } else if (formatYes.equals("format17")) {
            generateTXTByView(1, genSQL("vwF17_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName1, "ref_no_1", true, "|");
            generateXLSByView(1, genSQL("vwF17_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName2, "ref_no_1", true);
        } else if (formatYes.equals("format18")) {
            generateTXTByView(1, genSQL("vwF18_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName1, "ref_no_1", true, "|");
            generateXLSByView(1, genSQL("vwF18_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName2, "ref_no_1", true);
        } else if (formatYes.equals("format19")) {
            generateTXTByView(1, genSQL("vwF19_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName1, "ref_no_1", true, "|");
            generateXLSByView(1, genSQL("vwF19_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName2, "ref_no_1", true);
        } else if (formatYes.equals("format15a")) {
            generateTXTByView(1, genSQL("vwF15a_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName1, "ref_no_1", true, "|");
            generateXLSByView(1, genSQL("vwF15a_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName2, "ref_no_1", true);
        } else if (formatYes.equals("format16a")) {
            generateTXTByView(1, genSQL("vwF16a_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName1, "ref_no_1", true, "|");
            generateXLSByView(1, genSQL("vwF16a_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName2, "ref_no_1", true);
        } else if (formatYes.equals("format17a")) {
            generateTXTByView(1, genSQL("vwF17a_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName1, "ref_no_1", true, "|");
            generateXLSByView(1, genSQL("vwF17a_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName2, "ref_no_1", true);
        } else if (formatYes.equals("format18a")) {
            generateTXTByView(1, genSQL("vwF18a_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName1, "ref_no_1", true, "|");
            generateXLSByView(1, genSQL("vwF18a_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName2, "ref_no_1", true);
        } else if (formatYes.equals("format19a")) {
            generateTXTByView(1, genSQL("vwF19a_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName1, "ref_no_1", true, "|");
            generateXLSByView(1, genSQL("vwF19a_po_yes", "ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber), fileName2, "ref_no_1", true);

        } else if (formatYes.equals("format3")) {
            generateXLSByView(1, genSQL("generali_po_yes_pa", "PROPOSAL_NUMBER", null, productId, lastpaymentApprovedReason, false, "", "PROPOSAL_NUMBER", listRefNumber), fileName1, "PROPOSAL_NUMBER", true);
            generateXLSByView(1, genSQL("generali_po_dcr", "PROPOSAL_NUMBER", null, productId, lastpaymentApprovedReason, true, "", "PROPOSAL_NUMBER", listRefNumber), fileName2, "PROPOSAL_NUMBER", true);

        } else if (formatYes.equals("format4")) {
            generateXLSByView(1, genSQL("generali_po_yes_nonguarantee", "PROPOSAL_NUMBER", null, productId, lastpaymentApprovedReason, false, "", "PROPOSAL_NUMBER", listRefNumber), fileName1, "PROPOSAL_NUMBER", true);
            generateXLSByView(1, genSQL("generali_po_dcr", "PROPOSAL_NUMBER", null, productId, lastpaymentApprovedReason, true, "", "PROPOSAL_NUMBER", listRefNumber), fileName2, "PROPOSAL_NUMBER", true);

        } else if (formatYes.equals("format1newb")) {
            generateTXTByView(1, genSQL("vwNewbMTL_po_yes_hip", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwNewbMTL_po_yes_hip_ben", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format5newb")) {
            generateTXTByView(1, genSQL("vwNewbF5_po_yes_hip", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwNewbF5_po_yes_hip_ben", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format7newb")) {
            generateTXTByView(1, genSQL("vwNewbF7_po_yes_hip", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwNewbF7_po_yes_hip_ben", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format9newb")) {
            generateTXTByView(1, genSQL("vwNewbF9_po_yes_hip", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwNewbF9_po_yes_hip_ben", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format11newb")) {
            generateTXTByView(1, genSQL("vwNewbF11_po_yes_hip", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwNewbF11_po_yes_hip_ben", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format2newb")) {
            generateTXTByView(1, genSQL("vwNewbMTL_po_yes_hip_fatca", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwNewbMTL_po_yes_hip_ben_fatca", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format6newb")) {
            generateTXTByView(1, genSQL("vwNewbF6_po_yes_hip_fatca", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwNewbF6_po_yes_hip_ben_fatca", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format8newb")) {
            generateTXTByView(1, genSQL("vwNewbF8_po_yes_hip_fatca", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwNewbF8_po_yes_hip_ben_fatca", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format10newb")) {
            generateTXTByView(1, genSQL("vwNewbF10_po_yes_hip_fatca", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwNewbF10_po_yes_hip_ben_fatca", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");

        } else if (formatYes.equals("format12newb")) {
            generateTXTByView(1, genSQL("vwNewbF12_po_yes_hip_fatca", "REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName1, "ref_no_1", false, "|");
            generateTXTByView(1, genSQL("vwNewbF12_po_yes_hip_ben_fatca", "cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, "", "cxrefno", listRefNumber), fileName2, "ref_no_1", true, "|");
        } else {
            log.error("check product config at product.fx11");
        }

        if (formatId == 3) //3=EXCEL
        {
            if (formatYes.equals("format1")) {
                generateXLSByView(2, genSQL("vwMTL_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format5")) {
                generateXLSByView(2, genSQL("vwF5_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format7")) {
                generateXLSByView(2, genSQL("vwF7_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format9")) {
                generateXLSByView(2, genSQL("vwF9_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format11")) {
                generateXLSByView(2, genSQL("vwF11_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);

            } else if (formatYes.equals("format2")) {
                generateXLSByView(2, genSQL("vwMTL_po_app_fatca", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format6")) {
                generateXLSByView(2, genSQL("vwF6_po_app_fatca", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format8")) {
                generateXLSByView(2, genSQL("vwF8_po_app_fatca", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format10")) {
                generateXLSByView(2, genSQL("vwF10_po_app_fatca", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format12")) {
                generateXLSByView(2, genSQL("vwF12_po_app_fatca", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format13") || formatYes.equals("format14")) {
                generateXLSByView(2, genSQL("vwF13_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format15") || formatYes.equals("format16") || formatYes.equals("format17") || formatYes.equals("format18")) {
                generateXLSByView(2, genSQL("vwF15_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format19")) {
                generateXLSByView(2, genSQL("vwF19_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format15a") || formatYes.equals("format16a") || formatYes.equals("format17a") || formatYes.equals("format18a")) {
                generateXLSByView(2, genSQL("vwF15a_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format19a")) {
                generateXLSByView(2, genSQL("vwF19a_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);

            } else if (formatYes.equals("format3")) {
                generateXLSByView(2, genSQL("generali_po_app_pa", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format4")) {
                generateXLSByView(2, genSQL("generali_po_app_nonguarantee", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);

            } else if (formatYes.equals("format1newb")) {
                generateXLSByView(2, genSQL("vwNewbMTL_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format5newb")) {
                generateXLSByView(2, genSQL("vwNewbF5_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format7newb")) {
                generateXLSByView(2, genSQL("vwNewbF7_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format9newb")) {
                generateXLSByView(2, genSQL("vwNewbF9_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format11newb")) {
                generateXLSByView(2, genSQL("vwNewbF11_po_app", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);

            } else if (formatYes.equals("format2newb")) {
                generateXLSByView(2, genSQL("vwNewbMTL_po_app_fatca", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format6newb")) {
                generateXLSByView(2, genSQL("vwNewbF6_po_app_fatca", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format8newb")) {
                generateXLSByView(2, genSQL("vwNewbF8_po_app_fatca", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format10newb")) {
                generateXLSByView(2, genSQL("vwNewbF10_po_app_fatca", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            } else if (formatYes.equals("format12newb")) {
                generateXLSByView(2, genSQL("vwNewbF12_po_app_fatca", "REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, "", "REFERENCE_NO", listRefNumber), fileName3, "ref_no_1", true);
            }
        }

        Calendar now2 = Calendar.getInstance();
        if (formatId == 1 || formatId == 2) //1=PDF, 2=WORD
        {

            fileName3 = FILENAME_APP_TITLE + " " + fileNameExtend + "_" + PAYMENT_TITLE + ".zip";
            filePath3 = urlUploadPath() + fileName3;

            ProductDocumentDAO dao = this.getProductDocumentDAO();
            DocumentDAO dao1 = this.getDocumentDAO();
            String viewName = dao.findViewNameByDocumentId(mergedocumentId,productId);

            Boolean isGenSuccess = false;
            
            List<ProductDeclarationMapping> productDeclarationMappingList = this.productDeclarationMappingDAO.findProductDeclarationMappingByProductId(productId);

            String[] declareViewName = new String[productDeclarationMappingList.size()];
            String[] declareName = new String[productDeclarationMappingList.size()];
            for (int i = 0; i < productDeclarationMappingList.size(); i++) {
                declareName[i] = this.declarationFormDAO.findNameByDeclarationId(productDeclarationMappingList.get(i).getProductDeclarationMappingPK().getDeclarationFormId());
                declareViewName[i] = this.declarationFormDAO.findViewNameByDeclarationId(productDeclarationMappingList.get(i).getProductDeclarationMappingPK().getDeclarationFormId());
//                System.out.println("******declareViewName["+i+"] : "+declareViewName[i]);
            }

            int counter = 0;

            counter++;
            List columnName = dao.findColumnName(viewName);
            Object[] allCol = columnName.toArray();
            int countAllCol = allCol.length;
            String fileNames = dao1.findFileNamebyDocumentId(mergedocumentId);
            String templateFileName = fileNames.split(":")[1];
            templateFileName = templateFileName.substring(templateFileName.lastIndexOf("/") + 1, templateFileName.length());
            String sql = genSQLEX(viewName, allCol[0].toString(), productSponsorId, productId, lastpaymentApprovedReason, false, "", "ref_no_1", listRefNumber);

            List<Object> viewName1 = productDocumentDAO.selectQuery(sql);
            Object[] viewName2 = viewName1.toArray();
            int amount = viewName2.length;
            
            runningTime("Time(s.) appview > "+sql+" : ", now2);
            now2 = Calendar.getInstance();

            int count = 0;
            int countDeclare = 0;

            String templatePath = JSFUtil.getuploadPath() + dao1.findDocument(mergedocumentId).getDocumentFolder().getPath() + templateFileName;
            String templateDeclarePath = JSFUtil.getuploadPath() + "document/declaration/";   //JSFUtil.getRealPath() + "upload\\document\\declaration";;
//            System.out.println("templatePath : " + templatePath);
            SimpleDateFormat sdfFolder = new SimpleDateFormat("yyyyMMdd_HHmmss");
//            String tmpFolder = "C:\\Temp\\" + sdfFolder.format(new Date()) + "\\";
            String tmpFolder = JSFUtil.getuploadPath()+"registration\\"+ sdfFolder.format(new Date()) + "\\";
            FileUtil.createFolder(tmpFolder);

            String[] templateDeclare = new String[productDeclarationMappingList.size()];

            for (int i = 0; i < productDeclarationMappingList.size(); i++) {
                String a = this.declarationFormDAO.findmergefileByDeclarationId(productDeclarationMappingList.get(i).getProductDeclarationMappingPK().getDeclarationFormId());
                templateDeclare[i] = a;

            }

            String sql2 = "Select * from product_declaration_mapping where product_id = " + productId;
            List<Object> viewDeclare1 = productDocumentDAO.selectQuery(sql2);
            Object[] viewDeclare2 = viewDeclare1.toArray();
            int amountDeclare = viewDeclare2.length;

            while (count < amount) {
                Map<String, Object> nonImageVariableMap = new HashMap<String, Object>();
                Map<String, Object> nonImageVariableMap1 = new HashMap<String, Object>();
                String ref = "";
                String ref2 = "";
                int count1 = 0;

                String a = "ref_no_1";
                String check_ref = allCol[0].toString();

                List<String> filePDF = new ArrayList<String>();
                String mergedFilePDFName = "";

                String mergedFilePath = "";

                //registration
                try {
                    while (count1 < countAllCol) {
                        nonImageVariableMap.put(allCol[count1].toString(), ((Object[]) viewName1.get(count))[count1]);

                        if (allCol[count1].toString().equals(a)) {
                            Object[] b = (Object[]) viewName1.get(count);
                            ref = b[count1].toString();
                        } else if (allCol[count1].toString().equals(check_ref)) {
                            Object[] b = (Object[]) viewName1.get(count);
                            ref2 = b[count1].toString();
                        }
                        // else (allCol[count1].toString()=="")
                        count1++;
                    }
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(ExportYesfileController.class.getName()).log(Level.SEVERE, null, ex);
                    errorDetail = "Document mapping error.";
                    isGenSuccess = false;
                }

                Map<String, String> imageVariableOfPathMap = new HashMap<String, String>();
                //imageVariableOfPathMap.put("header_image_logo", "C:\\TEMP\\20160210_6621.jpg");

                DocxDocumentMergerAndConverter docxDocumentMergerAndConverter = new DocxDocumentMergerAndConverter();
                try {
                    byte[] mergedOutput = docxDocumentMergerAndConverter.mergeAndGenerateOutput(templatePath, TemplateEngineKind.Freemarker, nonImageVariableMap, imageVariableOfPathMap);
                    //String Allname = "Thank_" + System.nanoTime();

                    String Name1 = new String(name.get(count).getBytes("TIS-620"), "UTF-8");
                    String Surname1 = new String(surname.get(count).getBytes("TIS-620"), "UTF-8");
                    //String Allname = ref + "_" + Name1 + "_" + Surname1;
                    String Allname = ref2 + "_" + name.get(count) + "_" + surname.get(count);
                    String Docxname = Allname;

                    if (formatId == 1) {
                        Docxname = Docxname + ".doc";
                    } else {
                        Docxname = Docxname + ".docx";
                    }
                    String Pdfname = Allname + ".pdf";
                    String OutPutfile = tmpFolder + Docxname;
                    String OutPutfile1 = tmpFolder + Pdfname;
                    mergedFilePath = tmpFolder + "target";
                    mergedFilePathTemp = mergedFilePath;
                    mergedFilePDFName = Pdfname;
//                    mergedFileWordName = Docxname;
                    FileOutputStream os = new FileOutputStream(OutPutfile);
                    os.write(mergedOutput);
                    os.flush();
                    os.close();
                    try {
                        if (formatId == 1) {
                            allSourceFilesTemp.add(OutPutfile);
                            allSourceFilesTemp.add(OutPutfile1);
                            filePDF.add(OutPutfile1);
                            File inputFile = new File(OutPutfile);
                            File outputFile = new File(OutPutfile1);

                            // connect to an OpenOffice.org instance running on port 8100
                            OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
                            connection.connect();

                            // convert
                            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
                            converter.convert(inputFile, outputFile);
                            sourceFiles.add(outputFile.toString());
                            sourceFilesDoc.add(inputFile.toString());

                            // close the connection
                            connection.disconnect();
                        } else {
                            sourceFiles.add(OutPutfile);
                            allSourceFilesTemp.add(OutPutfile);
                        }

                        isGenSuccess = true;
//                        System.out.println("OpenOffice Success 1 !!!!!!!");
                    } catch (Exception ex) {
                        log.error(ex);
                        errorDetail = "Start OpenOffice.org as a service";
                        isGenSuccess = false;
                    }

                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(ExportYesfileController.class.getName()).log(Level.SEVERE, null, ex);
                    errorDetail = "File not found in system";
                    isGenSuccess = false;

                } catch (XDocReportException ex) {
                    java.util.logging.Logger.getLogger(ExportYesfileController.class.getName()).log(Level.SEVERE, null, ex);
                    errorDetail = "Registration Form error please contact admin";
                    isGenSuccess = false;

                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(ExportYesfileController.class.getName()).log(Level.SEVERE, null, ex);
                    errorDetail = "Error please contact admin";
                    isGenSuccess = false;
                }
//////////////////////////Declaration Form
                countDeclare = 0;

                while (countDeclare < amountDeclare) {   //declaration

                    List declareColumnName = dao.findColumnName(declareViewName[countDeclare]);

                    Object[] allDeclareCol = declareColumnName.toArray();

                    int countAllDeclareCol = allDeclareCol.length;

                    String sql1 = genSQLDeclare(declareViewName[countDeclare], allDeclareCol[0].toString(), productSponsorId, productId, lastpaymentApprovedReason, false, "", ref, "ref_no2", listRefNumber);

                    List<Object> viewDeclare3 = productDocumentDAO.selectQuery(sql1);
//                    int countViewDeclare = viewDeclare3.size();

                    int count2 = 0;
                    if (viewDeclare3.size() > 0) {
                        haveDeclare = true;
                        try {
                            while (count2 < countAllDeclareCol) {

                                nonImageVariableMap1.put(allDeclareCol[count2].toString(), ((Object[]) viewDeclare3.get(0))[count2]);

                                count2++;

                            }
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(ExportYesfileController.class.getName()).log(Level.SEVERE, null, ex);
                            errorDetail = "Declaration Form mapping error.";
                            isGenSuccess = false;
                        }

                        Map<String, String> imageVariableOfPathMap1 = new HashMap<String, String>();

                        try {
                            byte[] mergedOutput = docxDocumentMergerAndConverter.mergeAndGenerateOutput(templateDeclarePath + templateDeclare[countDeclare], TemplateEngineKind.Freemarker, nonImageVariableMap1, imageVariableOfPathMap1);
                            //String Allname = "Thank_" + System.nanoTime();

                            String Name1 = new String(name.get(count).getBytes("TIS-620"), "UTF-8");
                            String Surname1 = new String(surname.get(count).getBytes("TIS-620"), "UTF-8");
                            String Allname = ref + "_" + name.get(count) + "_" + surname.get(count) + "_" + declareName[countDeclare];
                            String Docxname = Allname;

                            if (formatId == 1) {
                                Docxname = Docxname + ".doc";
                            } else {
                                Docxname = Docxname + ".docx";
                            }
                            String Pdfname = Allname + ".pdf";
                            String OutPutfile = tmpFolder + Docxname;
                            String OutPutfile1 = tmpFolder + Pdfname;
                            FileOutputStream os = new FileOutputStream(OutPutfile);
                            os.write(mergedOutput);
                            os.flush();
                            os.close();
                            try {
                                if (formatId == 1) {
                                    allSourceFilesTemp.add(OutPutfile);
                                    allSourceFilesTemp.add(OutPutfile1);
                                    filePDF.add(OutPutfile1);
                                    File inputFile = new File(OutPutfile);
                                    File outputFile = new File(OutPutfile1);

                                    // connect to an OpenOffice.org instance running on port 8100
                                    OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
                                    connection.connect();

                                    // convert
                                    DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
                                    converter.convert(inputFile, outputFile);
                                    sourceFiles.add(outputFile.toString());
                                    sourceFilesDoc.add(inputFile.toString());

                                    // close the connection
                                    connection.disconnect();
                                } else {
                                    sourceFiles.add(OutPutfile);
                                    allSourceFilesTemp.add(OutPutfile);
                                }

                                isGenSuccess = true;
                            } catch (Exception ex) {
                                log.error(ex);
                                errorDetail = "Start OpenOffice.org as a service";
                                isGenSuccess = false;
                            }

                        } catch (IOException ex) {
                            java.util.logging.Logger.getLogger(ExportYesfileController.class.getName()).log(Level.SEVERE, null, ex);
                            errorDetail = "File not found in system";
                            isGenSuccess = false;

                        } catch (XDocReportException ex) {
                            java.util.logging.Logger.getLogger(ExportYesfileController.class.getName()).log(Level.SEVERE, null, ex);
                            errorDetail = "Declaration Form error please contact admin";
                            isGenSuccess = false;

                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(ExportYesfileController.class.getName()).log(Level.SEVERE, null, ex);
                            errorDetail = "Error please contact admin";
                            isGenSuccess = false;
                        }
                    }
                    countDeclare++;
                }
                
                try
                {
                    if(isGenSuccess)
                    {
                        PurchaseOrder po = purchaseOrderDAO.findByRefNo(ref);
                        po.setYesFlag(Boolean.TRUE);
                        po.setYesGenBy(JSFUtil.getUserSession().getUserName());
                        po.setYesGenDate(new Date());
                        purchaseOrderDAO.edit(po);
                    } 
                }
                catch (Exception ex) {
                    log.error(ex);
//                    errorDetail = "Start OpenOffice.org as a service";
                }

                if (formatId == 1 && filePDF.size() > 1) {
                    List list = new ArrayList();
                    OutputStream out = null;
                    try {
                        //input source pdf files
                        for (int i = 0; i < filePDF.size(); i++) {
                            File file = new File(filePDF.get(i));
                            list.add(new FileInputStream(file));

                        }
                        File file = new File(mergedFilePath);
                        file.mkdir();
                        //output pdf files
                        out = new FileOutputStream(new File(mergedFilePath + "\\" + mergedFilePDFName));
                        sourceFilesWithDeclare.add(mergedFilePath + "\\" + mergedFilePDFName);
                        allSourceFilesTemp.add(mergedFilePath + "\\" + mergedFilePDFName);

                        mergePDF.mergeFiles(list, out);

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Exception e) {
                            }
                        }
                    }
                } else if (formatId == 1) {
                    sourceFilesWithDeclare.add(filePDF.get(0));
                }

                count++;
            }

            try {
                if (haveDeclare && formatId == 1) {
                    if (sourceFilesWithDeclare.size() != 0) {
                        String zipFile1 = JSFUtil.getuploadPath();
                        zipFile1 = zipFile1.substring(0, zipFile1.lastIndexOf("\\"));
                        String zipFile = zipFile1 + "\\" + filePath3;

                        byte[] buffer = new byte[1024];

                        FileOutputStream fout = null;
                        ZipArchiveOutputStream zout = null;

                    try {
                        fout = new FileOutputStream(zipFile);
                        zout = new ZipArchiveOutputStream(fout);
                        zout.setEncoding("Cp437"); // This should handle your "special" characters
                        zout.setFallbackToUTF8(true); // For "unknown" characters!
                        zout.setUseLanguageEncodingFlag(true);
                        zout.setCreateUnicodeExtraFields(UnicodeExtraFieldPolicy.ALWAYS);
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zout, Charset.forName("UTF-8")));
                        // zout.setC

                        for (int i = 0; i < sourceFilesWithDeclare.size(); i++) {
                            String str = sourceFilesWithDeclare.get(i);
                            String entryName = str.substring(str.lastIndexOf("\\") + 1, str.length());
//                            System.out.println("Adding " + sourceFilesWithDeclare.get(i));

                            ZipArchiveEntry zae = new ZipArchiveEntry(entryName);
                            zae.setSize(sourceFilesWithDeclare.get(i).length());
                            zout.putArchiveEntry(zae);
                            FileInputStream fis = new FileInputStream(sourceFilesWithDeclare.get(i));
                            IOUtils.copy(fis, zout);
                            zout.closeArchiveEntry();
                        }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                                if (zout != null) {
                                    try {
                                        zout.close();
                                    } catch (Exception e) {
                                    }
                                }
                                if (fout != null) {
                                    try {
                                        fout.close();
                                    } catch (Exception e) {
                                    }
                                }
                        }

                    }
                } else {
                    if (sourceFiles.size() != 0) {
                        String zipFile1 = JSFUtil.getuploadPath();
                        zipFile1 = zipFile1.substring(0, zipFile1.lastIndexOf("\\"));
                        String zipFile = zipFile1 + "\\" + filePath3;

                        byte[] buffer = new byte[1024];

                        FileOutputStream fout = null;
                        ZipArchiveOutputStream zout = null;

                    try {
                        fout = new FileOutputStream(zipFile);
                        zout = new ZipArchiveOutputStream(fout);
                        zout.setEncoding("Cp437"); // This should handle your "special" characters
                        zout.setFallbackToUTF8(true); // For "unknown" characters!
                        zout.setUseLanguageEncodingFlag(true);
                        zout.setCreateUnicodeExtraFields(UnicodeExtraFieldPolicy.ALWAYS);
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zout, Charset.forName("UTF-8")));
                        // zout.setC

                        for (int i = 0; i < sourceFiles.size(); i++) {
                            String str = sourceFiles.get(i);
                            String entryName = str.substring(str.lastIndexOf("\\") + 1, str.length());
//                        System.out.println("Adding " + sourceFiles.get(i));

                            ZipArchiveEntry zae = new ZipArchiveEntry(entryName);
                            zae.setSize(sourceFiles.get(i).length());
                            zout.putArchiveEntry(zae);
                            FileInputStream fis = new FileInputStream(sourceFiles.get(i));
                            IOUtils.copy(fis, zout);
                            zout.closeArchiveEntry();

                        }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (zout != null) {
                                try {
                                    zout.close();
                                } catch (Exception e) {
                                }
                            }
                            if (fout != null) {
                                try {
                                    fout.close();
                                } catch (Exception e) {
                                }
                            }
                        }

                    }
                }

            } catch (Exception ex) {
                System.out.println("Exception :" + ex);
            }
            /*if (formatId == 1)
        { for(int i=0; i < sourceFiles.size(); i++)
         {
             File file = new File(sourceFiles.get(i));
             File filedoc = new File(sourceFilesDoc.get(i));

             if(file.delete() && filedoc.delete()){
                 System.out.println(file.getName() + " is deleted!");
                 System.out.println(filedoc.getName() + " is deleted!");
             }else{
                 System.out.println("Delete operation is failed.");
             }

         }
        }
         else
        {
            for(int i=0; i < sourceFiles.size(); i++)
            {
                File file = new File(sourceFiles.get(i));

                if(file.delete()){
                    System.out.println(file.getName() + " is deleted!");
                }else{
                    System.out.println("Delete operation is failed.");
                }

            }
        }*/
        }

        /*zip voice file --begin*/
 /*
        String dirPath = createBaseUploadPath2();
        String workPath = createBaseUploadPath2() + FILENAME_VOICE_TITLE + fileNameExtend + File.separator;
        String voiceName = FILENAME_VOICE_TITLE + fileNameExtend + ".zip";

        File file = new File(workPath);
        if (!file.exists()) {
            file.mkdir();
        }

        ZipUtil.zipFolder(workPath, voiceName, dirPath);
        try {
            FileUtils.deleteDirectory(new File(workPath));
        } catch (Exception e) {
            log.error(e);
        }
         */
 /*zip voice file --end*/

        if (StringUtils.isNotBlank(JSFUtil.getApplication().getConvertVoiceRestful())) {
            try {
                VoiceFileRestful2 voiceFileRestful = new VoiceFileRestful2(JSFUtil.getApplication().getConvertVoiceRestful());
                String json = purchaseOrderDAO.getJsonVoiceTrack(FOLDERNAME_VOICE,refNos,",");
                System.out.println(json);
                if (StringUtils.isNotBlank(json)) {
                    String ret = voiceFileRestful.post(json);
                    System.out.println(ret);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        runningTime("Time(s.) mergedoc : ", now2);
        runningTime("Time(s.) exportListener Total : ", now);
        System.out.println("sourceFiles " + sourceFiles.size());
        if ((sourceFiles.size() != 0) || formatId == 3) {
            try {
                String username = JSFUtil.getUserSession().getUserName();

                YesLog yesLog = new YesLog();
                yesLog.setYesFilename(fileName1);
                yesLog.setYesFilepath(filePath1);
                yesLog.setAppFilename(fileName2);
                yesLog.setAppFilepath(filePath2);
                yesLog.setVoiceFilename(fileName3);
                yesLog.setVoiceFilepath(filePath3);
//            yesLog.setResult("");
                yesLog.setFileType(1);
                yesLog.setCreateDate(new Date());
                yesLog.setCreateBy(username);
                yesLogDAO.create(yesLog);
            } catch (Exception e) {
                log.error(e);
            }

//        FacesContext.getCurrentInstance().responseComplete();
            return SELECT;
        }

        return errorDetail;
    }

    private void collectVoice(String refNo) {
        String dirPath = createBaseUploadPath2();
        String workPath = createBaseUploadPath2() + FILENAME_VOICE_TITLE + fileNameExtend + File.separator;
//        String refNoPath = createBaseUploadPath2() + "voice" + fileNameExtend + File.separator + refNo + File.separator;
        String refNoPath = createBaseUploadPath2() + FILENAME_VOICE_TITLE + fileNameExtend + File.separator;

        File file = new File(workPath);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(refNoPath);
        if (!file.exists()) {
            file.mkdir();
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = dataSource.getConnection();

            String sql = "select ch.create_date, ch.telephony_track_id from purchase_order po, contact_history_assignment cha, contact_history ch "
                    + "where po.assignment_detail_id = cha.assignment_detail_id "
                    + "and cha.contact_history_id = ch.id "
                    + "and po.ref_no = '" + refNo + "'";
//            sql = "select * from voice_bird";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            ResultSetMetaData metadata = rs.getMetaData();
            while (rs.next()) {
                File f = new File(getVoiceSource(rs.getDate("create_date"), rs.getString("telephony_track_id")));
                try {
//                    System.out.println(getVoiceSource(rs.getDate("create_date"), rs.getString("telephony_track_id")) + " --> " + refNoPath + refNo + "_" + f.getName());
//                    copyFile(getVoiceSource(rs.getDate("create_date"), rs.getString("telephony_track_id")), refNoPath + refNo + "_" + f.getName());

                    SimpleDateFormat sdfDt = new SimpleDateFormat("_yyMMdd", Locale.US);
                    SimpleDateFormat sdfTm = new SimpleDateFormat("HHmm", Locale.US);
                    String strDate = sdfDt.format(rs.getDate("create_date")) + sdfTm.format(rs.getTime("create_date"));

                    System.out.println(getVoiceSource(rs.getDate("create_date"), rs.getString("telephony_track_id")) + " --> " + refNoPath + refNo + FILENAME_VOICE + strDate + ".mp3");
                    copyFile(getVoiceSource(rs.getDate("create_date"), rs.getString("telephony_track_id")), refNoPath + refNo + FILENAME_VOICE + strDate + ".mp3");
                } catch (Exception e) {
                    log.error(e);
                }
            }

        } catch (Exception e) {
            log.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }

        }

    }

    private void zipVoice(String refNo) {
        String dirName = createBaseUploadPath2();
        String zipPath = createBaseUploadPath2() + "tmp_voice_" + refNo + File.separator;
        String zipName = refNo + fileNameExtend + ".zip";

        File file = new File(createBaseUploadPath2() + "tmp_voice_" + refNo);
        if (!file.exists()) {
            file.mkdir();
        }

        try {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            conn = dataSource.getConnection();

            String sql = "select ch.create_date, ch.telephony_track_id from purchase_order po, contact_history_assignment cha, contact_history ch "
                    + "where po.assignment_detail_id = cha.assignment_detail_id "
                    + "and cha.contact_history_id = ch.id "
                    + "and po.ref_no = '" + refNo + "'";
//            sql = "select * from voice_bird";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            ResultSetMetaData metadata = rs.getMetaData();
            while (rs.next()) {
                File f = new File(getVoiceSource(rs.getDate("contact_date"), rs.getString("telephony_track_id")));
                copyFile(getVoiceSource(rs.getDate("contact_date"), rs.getString("telephony_track_id")), zipPath + f.getName());
            }

        } catch (Exception e) {
            log.error(e);
        }

        ZipUtil.zipFolder(zipPath, zipName, dirName);

        try {
            FileUtils.deleteDirectory(new File(zipPath));
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void copyFile(String srcFileName, String destFileName) throws IOException {
//        String source = "http://192.168.4.13:81/2013-06/2013-06-04/2481085.mp3";
//        String source = "D:/voice_src/2013-06/2013-06-04/2481085.mp3";
//        String target = "D:/test1/";
//
//        File sourceFile = new File(source);
//        String name = sourceFile.getName();
//
//        File targetFile = new File(target + name);
//        FileUtils.copyFile(sourceFile, targetFile);

        File sourceFile = new File(srcFileName);
        File targetFile = new File(destFileName);
        FileUtils.copyFile(sourceFile, targetFile);

    }

    private String getVoiceSource(Date createDate, String trackId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM/yyyy-MM-dd/", Locale.US);
        String str = "";
        String url = JSFUtil.getApplication().getVoiceFilePath();
        String strDate = "";
        if (trackId != null) {
            if (createDate != null) {
                strDate = sdf.format(createDate);
            }
            str = url + strDate + trackId + ".mp3";
        }
        return str;
    }

    /*
    public void exportListenerApp(ActionEvent event) {
        generateXLSByView(2);
//        FacesContext.getCurrentInstance().responseComplete();
    }
     */
    private String urlUploadPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.US);
        String dirName = sdf.format(executeDate);

        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dirTime = sdfTime.format(executeDate);

//        return "../upload/yesfile/" + dir+"/";
//        return "/upload/yesfile/" + dir + "/";
        return "/upload/yesfile/" + dirName + "/" + dirTime + "/";
    }

    public String createBaseUploadPath2() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.US);
        String dirName = sdf.format(executeDate);

        FileUploadBean fileUploadBean = new FileUploadBean(JSFUtil.getuploadPath(), "yesfile");
        fileUploadBean.createDirName(dirName);
        String dirOld = fileUploadBean.getFolderPath();

        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dirTime = sdfTime.format(executeDate);

        FileUploadBean fileUploadBean2 = new FileUploadBean(dirOld);
        fileUploadBean2.createDirName(dirTime);

        return fileUploadBean2.getFolderPath();
    }

    public void createBaseUploadPath() {
        String yesfilePath = JSFUtil.getuploadPath() + "yesfile/"; //JSFUtil.getRealPath() + "upload\\customer\\";
        log.error(yesfilePath);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.US);
        String dirName = sdf.format(new Date());
        log.error(dirName);

        String path = yesfilePath + dirName + "/";
        File fDir = new File(path);
        if (!fDir.exists()) {
            fDir.mkdir();
        }
    }

    public void searchActionListener(ActionEvent event) {
        
        if (mergedocumentId == null) {
            messageMergeError = " Merge Document is required!";
        } else {
            messageMergeError = null;
            List<StringTemplateDTO> list = new ArrayList<StringTemplateDTO>();
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            name = new ArrayList<String>();
            surname = new ArrayList<String>();
            try {

                String sql = genSQL("maxar_po_yes", "ref_no_1", productSponsorId, productId, lastpaymentApprovedReason, false, "", "ref_no", listRefNumber);

                conn = dataSource.getConnection();
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();

//            colName1 = rs.getMetaData().getColumnName(1);
//            colName2 = rs.getMetaData().getColumnName(2);
//            colName3 = rs.getMetaData().getColumnName(3);
//            colName4 = rs.getMetaData().getColumnName(4);
//            colName5 = rs.getMetaData().getColumnName(5);
//            colName6 = rs.getMetaData().getColumnName(6);
//            colName7 = rs.getMetaData().getColumnName(7);
//            while (rs.next()) {
//                StringTemplateDTO fl = new StringTemplateDTO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
//                list.add(fl);
//            }
                colName1 = "Sale Date";
                colName2 = "Ref No.";
                colName3 = "Insured Title";
                colName4 = "First Name";
                colName5 = "Last Name";
                colName6 = "Plan";
                colName7 = "Premium";
                while (rs.next()) {
                    int count = 0;
                    StringTemplateDTO fl = new StringTemplateDTO(rs.getString("sale_date"), rs.getString("ref_no"), rs.getString("insured_title"), rs.getString("insured_name"), rs.getString("insured_surname"), rs.getString("po_plan_code"), rs.getString("po_net_premium"));
                    list.add(fl);
                    name.add(fl.getCol4().toString());
                    surname.add(fl.getCol5().toString());

                    count++;
                }
            } catch (Exception e) {
//            throw new ServletException(e.getMessage());
                log.error(e);
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException e) {
                }
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                } catch (SQLException e) {
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                }
            }
            stringTemplateDTOList = list;
        }
    }

    public List<StringTemplateDTO> getList() {
        return stringTemplateDTOList;
    }

    //getter/setter
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
        this.toDate = toDate;
    }

    //Managed Propterties
    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getTableViewYes() {
        return tableViewYes;
    }

    public void setTableViewYes(String tableViewYes) {
        this.tableViewYes = tableViewYes;
    }

    public String getTableViewApp() {
        return tableViewApp;
    }

    public void setTableViewApp(String tableViewApp) {
        this.tableViewApp = tableViewApp;
    }

    public String getColName1() {
        return colName1;
    }

    public void setColName1(String colName1) {
        this.colName1 = colName1;
    }

    public String getColName2() {
        return colName2;
    }

    public void setColName2(String colName2) {
        this.colName2 = colName2;
    }

    public String getColName3() {
        return colName3;
    }

    public void setColName3(String colName3) {
        this.colName3 = colName3;
    }

    public String getColName4() {
        return colName4;
    }

    public void setColName4(String colName4) {
        this.colName4 = colName4;
    }

    public String getColName5() {
        return colName5;
    }

    public void setColName5(String colName5) {
        this.colName5 = colName5;
    }

    public String getColName6() {
        return colName6;
    }

    public String getColName7() {
        return colName7;
    }

    public void setColName7(String colName7) {
        this.colName7 = colName7;
    }

    public void setColName6(String colName6) {
        this.colName6 = colName6;
    }

    public String getExportMode() {
        return exportMode;
    }

    public void setExportMode(String exportMode) {
        this.exportMode = exportMode;
    }

    //add getter/setter generateBy
    public String getGenerateBy() {
        return generateBy;
    }

    public void setGenerateBy(String generateBy) {
        this.generateBy = generateBy;
    }

    public YesLogDAO getYesLogDAO() {
        return yesLogDAO;
    }

    public void setYesLogDAO(YesLogDAO yesLogDAO) {
        this.yesLogDAO = yesLogDAO;
    }

    public static String Unicode2ASCII(String unicode) {

        StringBuffer ascii = new StringBuffer(unicode);

        int code;

        for (int i = 0; i < unicode.length(); i++) {

            code = (int) unicode.charAt(i);

            if ((0xE01 <= code) && (code <= 0xE5B)) {
                ascii.setCharAt(i, (char) (code - 0xD60));
            }

        }

        return ascii.toString();

    }

    public static String ASCII2Unicode(String ascii) {

        StringBuffer unicode = new StringBuffer(ascii);

        int code;

        for (int i = 0; i < ascii.length(); i++) {

            code = (int) ascii.charAt(i);

            if ((0xA1 <= code) && (code <= 0xFB)) {
                unicode.setCharAt(i, (char) (code + 0xD60));
            }

        }

        return unicode.toString();

    }

    //ProductSponsor
    public Integer getProductSponsorId() {
        return productSponsorId;
    }

    public void setProductSponsorId(Integer productSponsorId) {
        this.productSponsorId = productSponsorId;
    }

    public ProductSponsorDAO getProductSponsorDAO() {
        return productSponsorDAO;
    }

    public void setProductSponsorDAO(ProductSponsorDAO productSponsorDAO) {
        this.productSponsorDAO = productSponsorDAO;
    }

    public List<ProductSponsor> getProductSponsors() {
        return productSponsors;
    }

    public void setProductSponsors(List<ProductSponsor> productSponsors) {
        this.productSponsors = productSponsors;
    }

    public void setProductSponsorList() {

        ProductSponsorDAO dao = getProductSponsorDAO();
        productSponsors = dao.findProductSponsorEntities();
        productSponsorList = new SelectItem[productSponsors.size() + 1];
        int pos = 0;
        productSponsorList[pos++] = new SelectItem(null, "Select Product Sponsor");
        for (ProductSponsor ps : productSponsors) {
            productSponsorList[pos++] = new SelectItem(ps.getId(), ps.getName());
        }
    }

    public SelectItem[] getProductSponsorList() {
        return productSponsorList;
    }

    public void setProductSponsorList(SelectItem[] productSponsorList) {
        this.productSponsorList = productSponsorList;
    }

    //Product
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public List<ApprovalReason> getApprovalReasons() {
        return approvalReasons;
    }

    public void setApprovalReasons(List<ApprovalReason> approvalReasons) {
        this.approvalReasons = approvalReasons;
    }

    public Integer getLastpaymentApprovedReason() {
        return lastpaymentApprovedReason;
    }

    public Integer getMergedocumentId() {
        return mergedocumentId;
    }

    public void setMergedocumentId(Integer mergedocumentId) {
        this.mergedocumentId = mergedocumentId;
    }

    public void setLastpaymentApprovedReason(Integer lastpaymentApprovedReason) {
        this.lastpaymentApprovedReason = lastpaymentApprovedReason;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setProductList() {

        ProductDAO dao = getProductDAO();
        products = dao.findProductEntities();
        productList = new SelectItem[products.size() + 1];
        int pos = 0;
        productList[pos++] = new SelectItem(null, "Select Product");
        for (Product ps : products) {
            productList[pos++] = new SelectItem(ps.getId(), ps.getName());
        }
    }

    public SelectItem[] getProductList() {
        return productList;
    }

    public void setProductList(SelectItem[] productList) {
        this.productList = productList;
    }

    public void setApprovalReasonList() {

        ApprovalReasonDAO dao = getApprovalReasonDAO();
        approvalReasons = dao.findByStatus("payment", "approved");
        approvalReasonList = new SelectItem[approvalReasons.size() + 1];
        int pos = 0;
        approvalReasonList[pos++] = new SelectItem(null, "All");
        for (ApprovalReason ps : approvalReasons) {
            approvalReasonList[pos++] = new SelectItem(ps.getId(), ps.getName());
        }
    }

    public SelectItem[] getApprovalReasonList() {
        return approvalReasonList;
    }

    public void setApprovalReasonList(SelectItem[] approvalReasonList) {
        this.approvalReasonList = approvalReasonList;
    }

    //PaymentMethod
    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public PaymentMethodDAO getPaymentMethodDAO() {
        return paymentMethodDAO;
    }

    public void setPaymentMethodDAO(PaymentMethodDAO paymentMethodDAO) {
        this.paymentMethodDAO = paymentMethodDAO;
    }

    public ApprovalReasonDAO getApprovalReasonDAO() {
        return approvalReasonDAO;
    }

    public void setApprovalReasonDAO(ApprovalReasonDAO approvalReasonDAO) {
        this.approvalReasonDAO = approvalReasonDAO;
    }

    public SelectItem[] getPaymentMethodList() {
        return paymentMethodList;
    }

    public void setPaymentMethodList(SelectItem[] paymentMethodList) {
        this.paymentMethodList = paymentMethodList;
    }

    public List<Document> getMergeDocumentList() {
        return mergeDocumentList;
    }

    public void setMergeDocumentList(List<Document> mergeDocumentList) {
        this.mergeDocumentList = mergeDocumentList;
    }

    public Integer getCntRecords() {
        return cntRecords;
    }

    public void setCntRecords(Integer cntRecords) {
        this.cntRecords = cntRecords;
    }

    public void onProductIdChange(ValueChangeEvent event) {
        //Step1: clear payment method listS

        usergroupId = JSFUtil.getUserSession().getUserGroup().getId();
        this.productId = (Integer) event.getNewValue();
        mergeDocumentList = documentDAO.getMergeDocument(productId, usergroupId);
        this.paymentMethodList = new SelectItem[1];
        paymentMethodList[0] = new SelectItem(null, "Select PaymentMethod");
        this.paymentMethodId = null;

        //Step2: get new product object
        Product selectedProduct = getSelectedProductObj(this.productId);
        //Step3: find payment method of product
        LinkedHashMap<Integer, String> pmtList = new LinkedHashMap<Integer, String>();
        if (selectedProduct != null) {
            try {
                // case : product with new payment method structure
                List<PaymentMethodStringCastor> pmstc = PaymentModeConverter.convertToPaymentModeList(selectedProduct.getPaymentMode());
                if (pmstc != null) {
                    for (PaymentMethodStringCastor o : pmstc) {
                        pmtList.put(o.getPaymentMethodId(), getPaymentMethodName(o.getPaymentMethodId()));
                    }
                }
            } catch (Exception e) {
                log.error("Old Pattern payment method  [product id=" + selectedProduct.getId() + ", payment method=" + selectedProduct.getPaymentMethod() + ", payment mode=" + selectedProduct.getPaymentMode() + "], message=" + e.getMessage());
                // case : product with old payment method structure
                String paymentMethodStr = selectedProduct.getPaymentMethod();
                try {
                    String[] paymentMethodStrList = paymentMethodStr.split(",");
                    for (String o : paymentMethodStrList) {
                        Integer oid = new Integer(o);
                        pmtList.put(oid, getPaymentMethodName(oid));
                    }
                } catch (Exception ex) {
                    log.error("Error when load payment method list from [product id=" + selectedProduct.getId() + ", payment method=" + selectedProduct.getPaymentMethod() + "], error=" + ex.getMessage());
                    ex.printStackTrace();
                    for (PaymentMethod o : this.paymentMethods) {
                        pmtList.put(o.getId(), o.getName());
                    } // show allt
                }
            }
        }
        //Step4: change item
        this.paymentMethodList = new SelectItem[pmtList.size() + 1];
        int pos = 0;
        paymentMethodList[pos++] = new SelectItem(null, "Select PaymentMethod");

        for (Integer key : pmtList.keySet()) {
            paymentMethodList[pos++] = new SelectItem(key, pmtList.get(key));
        }

    }

    public void onFormatIdChange(ValueChangeEvent event) {
        this.formatId = (Integer) event.getNewValue();
        if (this.formatId == 3) {
            this.excelDetail = "Not include declaration form.";
        } else {
            this.excelDetail = "";
        }
    }

    //create function get merge document list when generate by have event change
    public void generateByChange(ValueChangeEvent event) {
        try {
            if (event.getNewValue().equals("refno")) {
                mergeDocumentList = documentDAO.getAvailableMergeDocument();
            } else {
                this.listRefNumber = "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //create function get merge document when click ex button
    public void refreshAction() {
        clearMergeDocument();
        messageError = null;
        stringTemplateDTOList = null;
        try {
            //get merge document that find bu ref number
            mergeDocumentList = documentDAO.getMergeDocumentByRefNumber(listRefNumber);
            productId = productDAO.getProductByRefNumber(listRefNumber);
            //check merge document equal empty ?
            if (mergeDocumentList.isEmpty() || mergeDocumentList.size() < 1) {
                messageError = " Merge Document is missing from this product!";
            }
            //check product id equals 0 ?
            if (productId == 0) {
                messageError = " Ref Number does not exist!";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //create function clear only merge document
    public void clearMergeDocument() {
        //check if merge doc not empty clear it
        if (mergeDocumentList != null) {
            mergeDocumentList.clear();
        }
    }

    //create function clear all data when change generate type search
    public void clearGenerateTypeChange() {
        //check if merge doc not empty clear it
        if (mergeDocumentList != null) {
            mergeDocumentList.clear();
        }
        //clear date,product,paymentMethod,mergeDocument,dataList,refNumber,errorMessage
        Date today = new Date();
        fromDate = today;
        toDate = today;
        productId = 0;
        this.paymentMethodList = new SelectItem[1];
        paymentMethodList[0] = new SelectItem(null, "Select PaymentMethod");
        this.paymentMethodId = null;
        mergedocumentId = 0;
        messageMergeError = null;
        messageError = null;
        stringTemplateDTOList = null;
        listRefNumber = null;
    }

    protected String getPaymentMethodName(Integer paymentMethodId) {
        String result = null;
        if (paymentMethods != null && paymentMethodId != null) {
            for (PaymentMethod o : paymentMethods) {
                if (o.getId().intValue() == paymentMethodId) {
                    result = o.getName();
                }
            }
        }
        return result;
    }

    protected Product getSelectedProductObj(Integer productId) {
        Product result = null;
        if (productId != null) {
            if (this.products != null) {
                for (Product p : products) {
                    if (p.getId().intValue() == productId.intValue()) {
                        result = p;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public DocumentDAO getDocumentDAO() {
        return documentDAO;
    }

    public void setDocumentDAO(DocumentDAO documentDAO) {
        this.documentDAO = documentDAO;
    }

    public ProductDocumentDAO getProductDocumentDAO() {
        return productDocumentDAO;
    }

    public void setProductDocumentDAO(ProductDocumentDAO productDocumentDAO) {
        this.productDocumentDAO = productDocumentDAO;
    }

    public DocumentUploadTypeDAO getDocumentUploadTypeDAO() {
        return documentUploadTypeDAO;
    }

    public void setDocumentUploadTypeDAO(DocumentUploadTypeDAO documentUploadTypeDAO) {
        this.documentUploadTypeDAO = documentUploadTypeDAO;
    }

    public DeclarationFormDAO getDeclarationFormDAO() {
        return declarationFormDAO;
    }

    public void setDeclarationFormDAO(DeclarationFormDAO declarationFormDAO) {
        this.declarationFormDAO = declarationFormDAO;
    }

    public ProductDeclarationMappingDAO getProductDeclarationMappingDAO() {
        return productDeclarationMappingDAO;
    }

    public void setProductDeclarationMappingDAO(ProductDeclarationMappingDAO productDeclarationMappingDAO) {
        this.productDeclarationMappingDAO = productDeclarationMappingDAO;
    }

    public Integer getFormatId() {
        return formatId;
    }

    public void setFormatId(Integer formatId) {
        this.formatId = formatId;
    }

    public Integer getUsergroupId() {
        return usergroupId;
    }

    public void setUsergroupId(Integer usergroupId) {
        this.usergroupId = usergroupId;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public ArrayList<String> getSurname() {
        return surname;
    }

    public void setSurname(ArrayList<String> surname) {
        this.surname = surname;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public MergePDF getMergePDF() {
        return mergePDF;
    }

    public void setMergePDF(MergePDF mergePDF) {
        this.mergePDF = mergePDF;
    }

    public String getExcelDetail() {
        return excelDetail;
    }

    public void setExcelDetail(String excelDetail) {
        this.excelDetail = excelDetail;
    }

    public String getListRefNumber() {
        return listRefNumber;
    }

    public void setListRefNumber(String listRefNumber) {
        this.listRefNumber = listRefNumber;
    }

    public String getMergeDocumentData() {
        return mergeDocumentData;
    }

    public void setMergeDocumentData(String mergeDocumentData) {
        this.mergeDocumentData = mergeDocumentData;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public String getMessageMergeError() {
        return messageMergeError;
    }

    public void setMessageMergeError(String messageMergeError) {
        this.messageMergeError = messageMergeError;
    }

    public Date getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(Date executeDate) {
        this.executeDate = executeDate;
    }
}
