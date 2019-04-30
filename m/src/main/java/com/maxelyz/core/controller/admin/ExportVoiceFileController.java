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
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.utils.FileUploadBean;
import com.maxelyz.utils.*;
import com.maxelyz.webservicevoice.ArrayOfExportVoiceAspectExportResult;
import com.maxelyz.webservicevoice.ArrayOfstring;
import com.maxelyz.webservicevoice.ExportVoiceAspect;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import net.lingala.zip4j.io.ZipOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;

//import java.nio.file.Files;
//import java.nio.file.StandardCopyOption;
//import javax.faces.context.FacesContext;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.io.ByteArrayOutputStream;
//import javax.servlet.ServletOutputStream;

/**
 *
 * @author Administrator
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class ExportVoiceFileController {

    private static String REFRESH = "exportvoicefile.xhtml?faces-redirect=true";
    private static String EDIT = "exportvoicefile.xhtml";
    private static String SELECT = "yeslog.xhtml?faces-redirect=true";
    private static Logger log = Logger.getLogger(ExportVoiceFileController.class);
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

    @ManagedProperty(value="#{productSponsorDAO}")
    private ProductSponsorDAO productSponsorDAO;

    @ManagedProperty(value="#{productDAO}")
    private ProductDAO productDAO;

    @ManagedProperty(value="#{paymentMethodDAO}")
    private PaymentMethodDAO paymentMethodDAO;

    @ManagedProperty(value="#{approvalReasonDAO}")
    private ApprovalReasonDAO approvalReasonDAO;

    @ManagedProperty(value="#{documentDAO}")
    private DocumentDAO documentDAO;

    @ManagedProperty(value="#{productDocumentDAO}")
    private ProductDocumentDAO productDocumentDAO;

    @ManagedProperty(value="#{documentuploadtypeDAO}")
    private DocumentUploadTypeDAO documentUploadTypeDAO;

    @ManagedProperty(value="#{voiceFilePasswordDAO}")
    private VoiceFilePasswordDAO voiceFilePasswordDAO;

    @ManagedProperty(value="#{contactHistoryDAO}")
    private ContactHistoryDAO contactHistoryDAO;

    private Date fromDate, toDate;
    private String tableViewYes = "nicecall_po_aia";
    private String tableViewApp = "nicecall_po_app_aia";
    private String exportMode = "new";
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

    private String fileName1;
    private String fileName2;
    private String fileName3;

    private String filePath1;
    private String filePath2;
    private String filePath3;
    private boolean blExportVoice;

    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> surname = new ArrayList<String>();



    private String errorDetail="";

    private Integer cntRecords;

    private static final int NUM_COLUMN_SEARCH = 6;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:exportvoicefile:view")) {
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
        if (this.paymentMethods == null) this.paymentMethods = new ArrayList<PaymentMethod>();
        this.initialPaymentMethodList();

    }

    private void initialPaymentMethodList(){
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

    private String genSQLEX(String tableView, String orderField, Integer productSponsorId, Integer productId, Integer lastpaymentApprovedReason,  Boolean genRank, String Var ) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String sqlFromDate = sdf1.format(fromDate) + " 00:00:00";
        String sqlToDate = sdf1.format(toDate) + " 23:59:59";

        /*TODO-tai*/
        String sql="";

        if (genRank) {
            //sql = "SELECT SUBSTRING(REFERENCE_NO,6,5) AS SequenceNo, * FROM " + tableView;
            sql = "SELECT " + Var + " FROM " + tableView;
        } else {
            sql = "SELECT * FROM " + tableView;
        }
        sql += " where sale_date_1 between '" + sqlFromDate + "'";
        sql += " and '" + sqlToDate + "'";

//        if (exportMode.equals("new")) {
//            sql += " and (yes_flag is null or yes_flag = 0)";
//        }

        if (productSponsorId!=null) {
            sql += " and product_id_1 IN (SELECT id FROM product WHERE product_sponsor_id="+ productSponsorId.toString() + ")";
        }

        if (productId!=null) {
            sql += " and product_id_1 = "+ productId.toString();
        }

        if (paymentMethodId!=null) {
            sql += " and payment_method_1 = "+ paymentMethodId.toString();
        }

        if (lastpaymentApprovedReason!= null && lastpaymentApprovedReason!= 0) {
            sql += " and last_payment_approved_reason = "+ lastpaymentApprovedReason.toString();
        }

        if (StringUtils.isNotBlank(orderField)) {
            sql += " order by " + orderField;
        }
        //System.out.println(sql);
        return sql;
    }

    private String genSQL(String tableView, String orderField, Integer productSponsorId, Integer productId, Integer lastpaymentApprovedReason,  Boolean genRank, String fixField ) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String sqlFromDate = sdf1.format(fromDate) + " 00:00:00";
        String sqlToDate = sdf1.format(toDate) + " 23:59:59";

        /*TODO-tai*/
        String sql="";

        if (genRank) {
            sql = "SELECT SUBSTRING(REFERENCE_NO,6,5) AS SequenceNo, * FROM " + tableView;
        } else if (StringUtils.isNotBlank(fixField)) {
            sql = "SELECT "+fixField+" FROM " + tableView;
        } else {
            sql = "SELECT * FROM " + tableView;
        }
        sql += " where sale_date_1 between '" + sqlFromDate + "'";
        sql += " and '" + sqlToDate + "'";

//        if (exportMode.equals("new")) {
//            sql += " and (yes_flag is null or yes_flag = 0)";
//        }

        if (productSponsorId!=null) {
            sql += " and product_id_1 IN (SELECT id FROM product WHERE product_sponsor_id="+ productSponsorId.toString() + ")";
        }

        if (productId!=null && productId!=0) {
            sql += " and product_id_1 = "+ productId.toString();
        }

        if (paymentMethodId!=null) {
            sql += " and payment_method_1 = "+ paymentMethodId.toString();
        }

        if (lastpaymentApprovedReason!= null && lastpaymentApprovedReason!= 0) {
            sql += " and last_payment_approved_reason = "+ lastpaymentApprovedReason.toString();
        }

        if (StringUtils.isNotBlank(orderField)) {
            sql += " order by " + orderField;
        }
        //System.out.println(sql);
        return sql;
    }

//    private genMerge(Integer mergedocumentid)
//    {
//        String sql="";
//
//        sql = "Select M"
//    }

    //    private void generateTXTByView(Integer reportType) {
    private void generateTXTByView(Integer reportType, String sql, String fileName, String fieldRefNo, Boolean isHeader, String seperator) {
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

            String content="";
            //byte[] contentInBytes;

            if (isHeader) {
                for (int i = 1; i <= columnCount - NUM_COLUMN_SEARCH; i++) {
                    if (!StringUtils.isEmpty(content)){
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

            while (rs.next()) {
                content = "";
                for (int i = 1; i <= columnCount - NUM_COLUMN_SEARCH; i++) {
                    if (!StringUtils.isEmpty(content)){
//                        content += "\t";
                        content += seperator;
                    }

                    if (rs.getString(i)==null) {
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
    }

    //    private void generateXLSByView(Integer reportType) {
    private void generateXLSByView(Integer reportType, String sql, String fileName, String fieldRefNo, Boolean isHeader) {
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

    }

    public String exportListener() {
        ArrayList<String> sourceFiles = new ArrayList<String>();
        ArrayList<String> sourceFilesDoc = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        SimpleDateFormat sdfFileName = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        fileNameExtend = sdfFileName.format(fromDate)+" "+sdf.format(cal.getTime());
        fileNameExtend1 = sdfFileName.format(fromDate);

        FILENAME_YES_TITLE = "Yesfile_" + productDAO.findProduct(productId).getFx4();
        FILENAME_VOICE_TITLE = "YesfileDetail_" + productDAO.findProduct(productId).getFx4();
        FILENAME_APP_TITLE = "Appfile_" + productDAO.findProduct(productId).getFx4();

        String PAYMENT_TITLE = "";
        try {
            PAYMENT_TITLE = paymentMethodDAO.findPaymentMethod(paymentMethodId).getYesFileTitle();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        fileName1 = FILENAME_YES_TITLE + " " + fileNameExtend1 + "_" + PAYMENT_TITLE + ".txt";
        fileName2 = FILENAME_VOICE_TITLE + " " + fileNameExtend1 + "_" + PAYMENT_TITLE + ".txt";

        filePath1 = urlUploadPath() + fileName1;
        filePath2 = urlUploadPath() + fileName2;

        String formatYes = productDAO.getFX11(productId);
        formatYes = StringUtils.isBlank(formatYes)?"":formatYes;
//        generateXLSByView(1, genSQL("vwMTL_po_yes_hip","REFERENCE_NO,ref_seq", null, productId, false), fileName1, "REFERENCE_NO", true);
//        generateXLSByView(1, genSQL("vwMTL_po_yes_hip_ben","cxrefno,benefit_no", null, productId, false), fileName2, "cxrefno", true);

        if (formatYes.equals("format1")) {
            generateTXTByView(1, genSQL("vwMTL_po_yes_hip","REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, ""), fileName1, "REFERENCE_NO", false, "|");
            generateTXTByView(1, genSQL("vwMTL_po_yes_hip_ben","cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, ""), fileName2, "cxrefno", true, "|");

        } else if (formatYes.equals("format2")) {
            generateTXTByView(1, genSQL("vwMTL_po_yes_hip_fatca","REFERENCE_NO,ref_seq", null, productId, lastpaymentApprovedReason, false, ""), fileName1, "REFERENCE_NO", false, "|");
            generateTXTByView(1, genSQL("vwMTL_po_yes_hip_ben_fatca","cxrefno,benefit_no", null, productId, lastpaymentApprovedReason, false, ""), fileName2, "cxrefno", true, "|");

        }

       if (formatId == 3)
       {//        SimpleDateFormat sdfFileName = new SimpleDateFormat("_yyyyMMdd_HHmmss", Locale.US);
//        SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

/*
        generateTXTByView(1);
//        generateXLSByView(1);
        generateXLSByView(2);
*/



        /*test excel*/
//        fileName1 = FILENAME_YES_TITLE + " " + fileNameExtend + "_CC.xlsx";
//        fileName2 = FILENAME_VOICE_TITLE + " " + fileNameExtend + "_CC.xlsx";
//        generateXLSByView(1, genSQL("vwMTL_po_yes_hip","REFERENCE_NO,ref_seq", null, productId, false), fileName1, "REFERENCE_NO", true);
//        generateXLSByView(1, genSQL("vwMTL_po_yes_hip_ben","cxrefno,benefit_no", null, productId, false), fileName2, "cxrefno", true);
        /*test excel*/

        fileName3 = FILENAME_APP_TITLE + " " + fileNameExtend + "_" + PAYMENT_TITLE + ".xlsx";
//        fileName3 = FILENAME_VOICE_TITLE + "_" + fileNameExtend + ".zip";
        filePath3 = urlUploadPath() + fileName3;

//        generateXLSByView(1, genSQL("vwMTL_po_yes_hip","REFERENCE_NO,ref_seq", null, productId, false), fileName1, "REFERENCE_NO", true);
//        generateXLSByView(1, genSQL("vwMTL_po_yes_hip_ben","cxrefno,benefit_no", null, productId, false), fileName2, "cxrefno", true);

        if (formatYes.equals("format1")) {
            generateXLSByView(2, genSQL("vwMTL_po_app","REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, ""), fileName3, "REFERENCE_NO", true);
        } else if (formatYes.equals("format2")) {
            generateXLSByView(2, genSQL("vwMTL_po_app_fatca","REFERENCE_NO", null, productId, lastpaymentApprovedReason, false, ""), fileName3, "REFERENCE_NO", true);
        }


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

//        String fileName1 = FILENAME_YES_TITLE + fileNameExtend + ".txt";
//        String fileName2 = FILENAME_APP_TITLE + fileNameExtend + ".xls";
//        String fileName3 = FILENAME_VOICE_TITLE + fileNameExtend + ".zip";
//
//        String filePath1 = urlUploadPath() + FILENAME_YES_TITLE + fileNameExtend + ".txt";
//        String filePath2 = urlUploadPath() + FILENAME_APP_TITLE + fileNameExtend + ".xls";
//        String filePath3 = urlUploadPath() + FILENAME_VOICE_TITLE + fileNameExtend + ".zip";
//
//        System.out.println(fileName1);
//        System.out.println(fileName2);
//        System.out.println(fileName3);
     if (formatId == 1 || formatId == 2)
     {
         String dirPath = createBaseUploadPath2();

        /*test excel*/
//        fileName1 = FILENAME_YES_TITLE + " " + fileNameExtend + "_CC.xlsx";
//        fileName2 = FILENAME_VOICE_TITLE + " " + fileNameExtend + "_CC.xlsx";
//        generateXLSByView(1, genSQL("vwMTL_po_yes_hip","REFERENCE_NO,ref_seq", null, productId, false), fileName1, "REFERENCE_NO", true);
//        generateXLSByView(1, genSQL("vwMTL_po_yes_hip_ben","cxrefno,benefit_no", null, productId, false), fileName2, "cxrefno", true);
        /*test excel*/


         fileName3 = FILENAME_APP_TITLE + " " + fileNameExtend + "_" + PAYMENT_TITLE + ".zip";
//        fileName3 = FILENAME_VOICE_TITLE + "_" + fileNameExtend + ".zip";
         filePath3 = urlUploadPath() + fileName3;

         ProductDocumentDAO dao = this.getProductDocumentDAO();
         DocumentDAO dao1 = this.getDocumentDAO();
         String viewName =  dao.findViewNameByDocumentId(mergedocumentId);
       //  String column_Name = dao.findColumnNameByDocumentId(mergedocumentId);
         int counter = 0;
        /* for( int i=0; i<column_Name.length(); i++ ) {
             if( column_Name.charAt(i) == ',' ) {
                 counter++;
             }
         }   */
         counter++;
         List columnName = dao.findColumnName(viewName);
         Object[] allCol = columnName.toArray();
         int countAllCol = allCol.length;
        String fileNames = dao1.findFileNamebyDocumentId(mergedocumentId);
        String templateFileName = fileNames.split(":")[1];
        templateFileName = templateFileName.substring(templateFileName.lastIndexOf("/")+1, templateFileName.length());
        String sql = genSQLEX(viewName, allCol[0].toString(), productSponsorId, productId, lastpaymentApprovedReason, false,"");
      //  String thank_date = genSQLEX(viewName,"ref_no", productSponsorId, productId, lastpaymentApprovedReason, true,"thank_date");
      //  String name = genSQLEX(viewName,"ref_no", productSponsorId, productId, lastpaymentApprovedReason, true,"name");
      //  String author_name = genSQLEX(viewName,"ref_no", productSponsorId, productId, lastpaymentApprovedReason, true,"author_name");

//        List Thank_date = productDocumentDAO.selectQuery(thank_date);
 //       List Name = productDocumentDAO.selectQuery(name);
   //     List Author_name = productDocumentDAO.selectQuery(author_name);


        List<Object> viewName1 = productDocumentDAO.selectQuery(sql);
        Object[] viewName2 = viewName1.toArray();
        int amount = viewName2.length;

        int count = 0;

//         String templatePath = JSFUtil.getuploadPath()+"document\\product\\"+fileName;
         String templatePath = JSFUtil.getuploadPath()+dao1.findDocument(mergedocumentId).getDocumentFolder().getPath()+templateFileName;
         System.out.println("templatePath : " + templatePath);

        while(count < amount)
        {   Map<String,Object> nonImageVariableMap = new HashMap<String,Object>();
            String ref="";
            int count1=0;
            String a = allCol[0].toString();


            while(count1 < countAllCol)
            {
                try
                {//nonImageVariableMap.put(allCol[0].toString(),Thank_date.get(count) );
                    //nonImageVariableMap.put(allCol[22].toString(),Name.get(count) );
                    //nonImageVariableMap.put(allCol[20].toString(),Author_name.get(count) );
                    //nonImageVariableMap.put("website","www.TerraBit.co.th");
                    nonImageVariableMap.put(allCol[count1].toString(),  ((Object[])viewName1.get(count))[count1]);
                    //nonImageVariableMap.put(column_Name.split(",")[count1],  ((Object[])viewName1.get(count))[count1]);


                    if (allCol[count1].toString() == a)
                    {
                        Object[] b = (Object[])viewName1.get(count);
                        ref = b[count1].toString();
                    }
                    // else (allCol[count1].toString()=="")
                    count1++;
                }
                catch (Exception ex)
                {
                    log.error(ex);
                }

            }

            Map<String,String> imageVariableOfPathMap = new HashMap<String, String>();
            //imageVariableOfPathMap.put("header_image_logo", "C:\\TEMP\\20160210_6621.jpg");


            DocxDocumentMergerAndConverter docxDocumentMergerAndConverter = new DocxDocumentMergerAndConverter();
            try {
                byte[] mergedOutput = docxDocumentMergerAndConverter.mergeAndGenerateOutput(templatePath, TemplateEngineKind.Freemarker, nonImageVariableMap, imageVariableOfPathMap);
                //String Allname = "Thank_" + System.nanoTime();

                String Name1 = new String(name.get(count).getBytes("TIS-620"), "UTF-8");
                String Surname1 = new String(surname.get(count).getBytes("TIS-620"), "UTF-8");
                //String Allname = ref + "_" + Name1 + "_" + Surname1;
                String Allname = ref + "_" + name.get(count) + "_" + surname.get(count);
                String Docxname = Allname;

                if (formatId == 1)
                {
                    Docxname = Docxname + ".doc";
                }
                else
                {
                    Docxname = Docxname + ".docx";
                }
                String Pdfname = Allname + ".pdf";
                String OutPutfile = "C:\\Temp\\"+Docxname;
                String OutPutfile1= "C:\\Temp\\"+Pdfname;
                FileOutputStream os = new FileOutputStream(OutPutfile);
                os.write(mergedOutput);
                os.flush();
                os.close();
                try{
                    if(formatId == 1)
                    {
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
                    }
                    else
                    {
                        sourceFiles.add(OutPutfile);
                    }
                }
                catch (Exception ex)
                {
                    log.error(ex);
                    errorDetail = "Start OpenOffice.org as a service";
                }

            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ExportVoiceFileController.class.getName()).log(Level.SEVERE, null, ex);
                errorDetail = "File not found in system";

            } catch (XDocReportException ex) {
                java.util.logging.Logger.getLogger(ExportVoiceFileController.class.getName()).log(Level.SEVERE, null, ex);
                errorDetail = "Document error please contact admin";

            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(ExportVoiceFileController.class.getName()).log(Level.SEVERE, null, ex);
                errorDetail = "Error please contact admin";
            }


            count++;
        }

         try
         {
             if (sourceFiles.size() != 0)
             {  String zipFile1 = JSFUtil.getuploadPath();
                 zipFile1 = zipFile1.substring(0,zipFile1.lastIndexOf("\\"));
                 String zipFile = zipFile1+"\\"+filePath3;
                 //String[] sourceFiles = {"C:/sourcefile1.doc", "C:/sourcefile2.doc"};

                 //create byte buffer
                 byte[] buffer = new byte[1024];

                 /*
                         * To create a zip file, use
                         *
                         * ZipOutputStream(OutputStream out)
                         * constructor of ZipOutputStream class.
                        */

                 //create object of FileOutputStream
                 FileOutputStream fout = new FileOutputStream(zipFile);

                 //create object of ZipOutputStream from FileOutputStream
                 //ZipOutputStream zout = new ZipOutputStream(fout, Charset.forName("UTF-8"));
                 //zos = new ZipOutputStream(fos, Charset.forName("UTF-8"));

                 ZipArchiveOutputStream zout = new ZipArchiveOutputStream(fout);
                 zout.setEncoding("Cp437"); // This should handle your "special" characters
                 zout.setFallbackToUTF8(true); // For "unknown" characters!
                 zout.setUseLanguageEncodingFlag(true);
                 zout.setCreateUnicodeExtraFields(UnicodeExtraFieldPolicy.ALWAYS);
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zout, Charset.forName("UTF-8")));
                 // zout.setC

                 for(int i=0; i < sourceFiles.size(); i++)
                 {
                     String str = sourceFiles.get(i);
                     String entryName = str.substring(str.lastIndexOf("\\")+1, str.length());
                     System.out.println("Adding " + sourceFiles.get(i));
                
                     ZipArchiveEntry zae = new ZipArchiveEntry(entryName);
                     zae.setSize(sourceFiles.get(i).length());
                     zout.putArchiveEntry(zae);
                     FileInputStream fis = new FileInputStream(sourceFiles.get(i));
                     IOUtils.copy(fis, zout);
                     zout.closeArchiveEntry();
                     //create object of FileInputStream for source file
                     //FileInputStream fin = new FileInputStream(sourceFiles.get(i));

                     /*
                                 * To begin writing ZipEntry in the zip file, use
                                 *
                                 * void putNextEntry(ZipEntry entry)
                                 * method of ZipOutputStream class.
                                 *
                                 * This method begins writing a new Zip entry to
                                 * the zip file and positions the stream to the start
                                 * of the entry data.
                                 */

                     // zout.putNextEntry(new ZipEntry(entryName));

                     /*
                                 * After creating entry in the zip file, actually
                                 * write the file.
                                 */
               /* int length;

                while((length = fin.read(buffer)) > 0)
                {
                    zout.write(buffer, 0, length);
                }*/

                                /*
                                 * After writing the file to ZipOutputStream, use
                                 *
                                 * void closeEntry() method of ZipOutputStream class to
                                 * close the current entry and position the stream to
                                 * write the next entry.
                                 */

               // zout.closeEntry();

                //close the InputStream
               // fin.close();

            }


            //close the ZipOutputStream
            
             zout.close();
             System.out.println("Zip file has been created!");
         }

        }
        catch(IOException ex)
        {
            System.out.println("IOException :" + ex);
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

    public String renameVoiceFile(String filename) {

/* error */
//        String[] splits = filename.split(".");
//        String name = splits[0];
//        String ext = splits[1];
        String name = null;
        String ext = null;
        try {
            name = filename.substring(0,filename.lastIndexOf("."));
            ext = filename.substring(filename.lastIndexOf(".") + 1);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.US);

            ContactHistory contactHistory = contactHistoryDAO.findByCustomerTrackId(name);
//            for (PurchaseOrder po : contactHistory.getPurchaseOrderCollection()) {
//                name = po.getRefNo() + "_" + name;
//            }

            String sql = genSQL("maxar_po_yes","", productSponsorId, productId, lastpaymentApprovedReason, false, "ref_no");
            sql = "SELECT distinct po.ref_no FROM dbo.purchase_order po, dbo.contact_history ch, dbo.assignment_detail ad, dbo.assignment a\n" +
                    "WHERE po.customer_id = ch.customer_id\n" +
                    "AND po.assignment_detail_id = ad.id\n" +
                    "AND ad.assignment_id = a.id\n" +
                    "AND ch.create_date BETWEEN a.assign_date AND po.qc_date\n" +
                    "AND telephony_track_id IS NOT NULL\n" +
                    "AND po.ref_no IN (\n" +
                    "" + sql + "\n" +
                    ")" + "\n" +
                    "AND ch.id = " + contactHistory.getId();
//            System.out.println(sql);
            List<String> refNos = voiceFilePasswordDAO.returnStringList(sql);
            for (String refNo : refNos) {
                name = refNo + "_" + name;
            }

            name = name + "_" + sdf.format(contactHistory.getContactDate());

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(filename + " --> " + name + "." + ext);
        return name+"."+ext;
    }

    public void exportVoiceFileListener() {

        Calendar now;
        now = Calendar.getInstance();
        System.out.println(1 + " : START exportVoiceFileListener : " + new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        String filenameDate = sdf.format(new Date());

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd", Locale.US);

        String sProductCode = "ALL";
        if (productId!=null && productId!=0) {
            sProductCode = productDAO.findProduct(productId).getCode();
            sProductCode = sProductCode.replaceAll("[^a-zA-Z0-9.-]", "");
            sProductCode = sProductCode.toUpperCase();
        }
        String sZipFilename = "Voice_" + sProductCode + "_" + sdf2.format(fromDate) + "_" + sdf2.format(toDate) + ".zip";

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setHeader("Content-Disposition", "Attachment;filename="+sZipFilename);

        try {
            //Collect track_id
            System.out.println(2 + " : START exportVoiceFileListener");
            String sql = genSQL("maxar_po_yes","", productSponsorId, productId, lastpaymentApprovedReason, false, "ref_no");
            sql = "SELECT distinct telephony_track_id FROM dbo.purchase_order po, dbo.contact_history ch, dbo.assignment_detail ad, dbo.assignment a\n" +
                    "WHERE po.customer_id = ch.customer_id\n" +
                    "AND po.assignment_detail_id = ad.id\n" +
                    "AND ad.assignment_id = a.id\n" +
                    "AND ch.create_date BETWEEN a.assign_date AND po.qc_date\n" +
                    "AND telephony_track_id IS NOT NULL\n" +
                    "AND po.ref_no IN (\n" +
                    "" + sql +"\n" +
                    ")";
            /* for test */
//            sql = "SELECT TOP 10 telephony_track_id FROM dbo.contact_history where telephony_track_id < 13240 ORDER BY id desc";
            System.out.println(sql);
            List<String> trackIds = voiceFilePasswordDAO.returnStringList(sql);

            if (trackIds.size()==0) return;

            System.out.println(3 + " : START exportVoiceFileListener");
            ArrayOfstring arrTrack = new ArrayOfstring();
//            for (int i=0; i<5; i++) {
//                arrStr.getString().add(Integer.valueOf(1231140+i).toString());
//            }
            for (String trackId : trackIds){
                arrTrack.getString().add(trackId);
            }

            //Call WebService
//            URL url = new URL("http://192.168.51.124:99/ExportVoiceAspect.svc?wsdl");
            URL url = new URL(JSFUtil.getApplication().getConvertVoiceWSDL());
            ArrayOfExportVoiceAspectExportResult exportVoiceAspectExportResult = new ExportVoiceAspect(url).getBasicHttpBindingIExportVoiceAspect().exportVoice(arrTrack);

            System.out.println(4 + " : START exportVoiceFileListener");
            List<String> selectedFiles = new ArrayList<String>();
            List<File> files = new ArrayList();
//            String baseSrcPath = "V:/";
//            String baseDstPath = "C:/tmp/";
            String baseSrcPath = JSFUtil.getApplication().getVoiceBasePath();
            createPath(JSFUtil.getuploadPath() + "voicefile/");
            String baseDstPath = JSFUtil.getuploadPath() + "voicefile/";

            System.out.println(5 + " : START exportVoiceFileListener");
            for (int i=0; i<exportVoiceAspectExportResult.getExportVoiceAspectExportResult().size(); i++) {
                //System.out.println(exportVoiceAspectExportResult.getExportVoiceAspectExportResult().get(i).getConvertResult().getValue());
                //System.out.println(exportVoiceAspectExportResult.getExportVoiceAspectExportResult().get(i).getTrackId().getValue());

                System.out.println(6 + " : START exportVoiceFileListener >> " + i);
                String voiceFileFullPath = exportVoiceAspectExportResult.getExportVoiceAspectExportResult().get(i).getConvertResult().getValue();
//                voiceFileFullPath = StringUtils.substring(voiceFileFullPath, StringUtils.indexOf(voiceFileFullPath,":")+1);
//                voiceFileFullPath = StringUtils.substring(voiceFileFullPath, StringUtils.indexOf(voiceFileFullPath,":")+1);
//                voiceFileFullPath = StringUtils.substring(voiceFileFullPath, StringUtils.indexOf(voiceFileFullPath,"/")+1);
                voiceFileFullPath = StringUtils.substring(voiceFileFullPath, StringUtils.indexOf(voiceFileFullPath,"download")+9);

                File f = new File(baseSrcPath + voiceFileFullPath);
                if (f.exists()) {
                    System.out.println(6.1 + " : START exportVoiceFileListener >> " + i);
                    selectedFiles.add(baseSrcPath + voiceFileFullPath);
                    files.add(f);
                    System.out.println("Found : " + baseSrcPath + voiceFileFullPath);
                } else {
                    System.out.println(6.2 + " : START exportVoiceFileListener >> " + i);
                    System.out.println("Not Found : " + baseSrcPath + voiceFileFullPath);
                }

            }

            //List VoiceFile
//            for (String s : selectedFiles) {
//                File f = new File(s);
//                System.out.println(s + "-->" + f.exists());
//                System.out.println(f.toPath());
//            }

            String zipPath = baseDstPath + filenameDate + "/";

            //File tempPath = new File(zipPath);
            //tempPath.mkdir();
            FileUtil.createFolder(zipPath);

            for(File file : files) {
                /* only java7 */
//                Files.copy(file.toPath(),
//                        (new File(zipPath + file.getName())).toPath(),
//                        StandardCopyOption.REPLACE_EXISTING);
                FileUtils.copyFile(file, new File(zipPath + renameVoiceFile(file.getName())));
            }

            //String zipName = "Voice_" + filenameDate + ".zip";
            //String outPath = baseDstPath;
            final ZipOutputStream zOut = new ZipOutputStream(response.getOutputStream());
            String password = voiceFilePasswordDAO.findActivePassword();

            //ZipUtil.zipFolder(zipPath, zipName, outPath);
            //Zip4jUtil.zipFolder(zipPath, zipName, outPath, "1234");
            Zip4jUtil.zipFolderStream(zOut, zipPath, password);
            FileUtil.delete(new File(zipPath));

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(10 + " : END exportVoiceFileListener : " + new Date());
        runningTime("exportVoiceFileListener Running Time(sec):", now);

        FacesContext.getCurrentInstance().responseComplete();
    }

    private void runningTime(String text, Calendar beginTime) {
        System.out.println(text+(Calendar.getInstance().getTimeInMillis() - beginTime.getTimeInMillis())/1000);//running time in seconds

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
        String dir = sdf.format(new Date());
//        return "../upload/yesfile/" + dir+"/";
        return "/upload/yesfile/" + dir + "/";
    }

    public static String createBaseUploadPath2() {
        FileUploadBean fileUploadBean = new FileUploadBean(JSFUtil.getuploadPath(), "yesfile");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.US);
        String dirName = sdf.format(new Date());

        fileUploadBean.createDirName(dirName);
        return fileUploadBean.getFolderPath();
    }

    public static void createPath(String filePath) {
        String path = filePath;
        File fDir = new File(path);
        if (!fDir.exists()) {
            fDir.mkdir();
        }
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
        List<StringTemplateDTO> list = new ArrayList<StringTemplateDTO>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            String sql = genSQL("maxar_po_yes","ref_no", productSponsorId, productId, lastpaymentApprovedReason, false, "");
            
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

    public YesLogDAO getYesLogDAO() {
        return yesLogDAO;
    }

    public void setYesLogDAO(YesLogDAO yesLogDAO) {
        this.yesLogDAO = yesLogDAO;
    }

    public static String Unicode2ASCII(String unicode) {

        StringBuffer ascii = new StringBuffer(unicode);

        int code;

        for(int i = 0; i < unicode.length(); i++) {

            code = (int)unicode.charAt(i);

            if ((0xE01<=code) && (code <= 0xE5B ))

                ascii.setCharAt( i, (char)(code - 0xD60));

        }

        return ascii.toString();

    }

    public static String ASCII2Unicode(String ascii) {

        StringBuffer unicode = new StringBuffer(ascii);

        int code;

        for(int i = 0; i < ascii.length(); i++) {

            code = (int)ascii.charAt(i);

            if ((0xA1 <= code) && (code <= 0xFB))

                unicode.setCharAt( i, (char)(code + 0xD60));

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
        productSponsorList = new SelectItem[productSponsors.size()+1];
        int pos=0;
        productSponsorList[pos++] = new SelectItem(null, "Select Product Sponsor");
        for(ProductSponsor ps : productSponsors){
            productSponsorList[pos++] = new SelectItem(ps.getId(), ps.getName()) ;
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
        productList = new SelectItem[products.size()+1];
        int pos=0;
        productList[pos++] = new SelectItem(null, "All");
        for(Product ps : products){
            productList[pos++] = new SelectItem(ps.getId(), ps.getName()) ;
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
        approvalReasons = dao.findByStatus("payment" , "approved");
        approvalReasonList = new SelectItem[approvalReasons.size()+1];
        int pos=0;
        approvalReasonList[pos++] = new SelectItem(null, "All");
        for(ApprovalReason ps : approvalReasons){
            approvalReasonList[pos++] = new SelectItem(ps.getId(), ps.getName()) ;
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

    public void onProductIdChange(ValueChangeEvent event){
        //Step1: clear payment method listS
        
        usergroupId = JSFUtil.getUserSession().getUserGroup().getId();
        this.productId = (Integer) event.getNewValue();
        mergeDocumentList = documentDAO.getMergeDocument(productId,usergroupId);
        this.paymentMethodList = new SelectItem[1];
        paymentMethodList[0] = new SelectItem(null, "Select PaymentMethod");
        this.paymentMethodId = null;

        //Step2: get new product object
        Product selectedProduct = getSelectedProductObj(this.productId);
        //Step3: find payment method of product
        LinkedHashMap<Integer,String> pmtList = new LinkedHashMap<Integer,String>();
        if (selectedProduct!=null){
            try{
                // case : product with new payment method structure
                List<PaymentMethodStringCastor> pmstc  =  PaymentModeConverter.convertToPaymentModeList(selectedProduct.getPaymentMode());
                if (pmstc!=null){
                    for(PaymentMethodStringCastor o : pmstc){
                        pmtList.put(o.getPaymentMethodId(), getPaymentMethodName(o.getPaymentMethodId()));
                    }
                }
            }catch(Exception e){
                log.error("Old Pattern payment method  [product id="+selectedProduct.getId()+", payment method="+selectedProduct.getPaymentMethod()+", payment mode="+selectedProduct.getPaymentMode()+"], message="+e.getMessage());
                // case : product with old payment method structure
                String paymentMethodStr = selectedProduct.getPaymentMethod();
                try{
                    String[] paymentMethodStrList = paymentMethodStr.split(",");
                    for(String o : paymentMethodStrList){
                        Integer oid = new Integer(o);
                        pmtList.put(oid, getPaymentMethodName(oid));
                    }
                }catch(Exception ex){
                    log.error("Error when load payment method list from [product id="+selectedProduct.getId()+", payment method="+selectedProduct.getPaymentMethod()+"], error="+ex.getMessage());
                    ex.printStackTrace();
                    for(PaymentMethod o : this.paymentMethods){pmtList.put(o.getId(), o.getName());} // show allt
                }
            }
        }
        //Step4: change item
        this.paymentMethodList = new SelectItem[pmtList.size()+1];
        int pos=0;
        paymentMethodList[pos++] = new SelectItem(null, "Select PaymentMethod");

        for(Integer key : pmtList.keySet() ){
            paymentMethodList[pos++] = new SelectItem(key, pmtList.get(key)) ;
        }
        
        
    }
    protected String getPaymentMethodName(Integer paymentMethodId){
        String result = null;
        if( paymentMethods != null && paymentMethodId!=null){
            for(PaymentMethod o : paymentMethods){
                if (o.getId().intValue() == paymentMethodId ){
                    result = o.getName();
                }
            }
        }
        return result;
    }
    protected Product getSelectedProductObj(Integer productId){
        Product result = null;
        if (productId!=null){
            if (this.products!=null){
                for(Product p : products){
                    if (p.getId().intValue() == productId.intValue() ){  result=p; break;  }
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

    public boolean isBlExportVoice() {
        return blExportVoice;
    }

    public void setBlExportVoice(boolean blExportVoice) {
        this.blExportVoice = blExportVoice;
    }

    public VoiceFilePasswordDAO getVoiceFilePasswordDAO() {
        return voiceFilePasswordDAO;
    }

    public void setVoiceFilePasswordDAO(VoiceFilePasswordDAO voiceFilePasswordDAO) {
        this.voiceFilePasswordDAO = voiceFilePasswordDAO;
    }

    public ContactHistoryDAO getContactHistoryDAO() {
        return contactHistoryDAO;
    }

    public void setContactHistoryDAO(ContactHistoryDAO contactHistoryDAO) {
        this.contactHistoryDAO = contactHistoryDAO;
    }

    public static void main (String[] args) throws Exception {

        try {
            //Collect track_id
            ArrayOfstring arrStr = new ArrayOfstring();
            for (int i=0; i<5; i++) {
                arrStr.getString().add(Integer.valueOf(1231140+i).toString());
            }

            //Call WebService
            URL url = new URL("http://192.168.51.124:99/ExportVoiceAspect.svc?wsdl");
            ArrayOfExportVoiceAspectExportResult exportVoiceAspectExportResult = new ExportVoiceAspect(url).getBasicHttpBindingIExportVoiceAspect().exportVoice(arrStr);

            List<String> selectedFiles = new ArrayList<String>();
            List<File> files = new ArrayList();
            String baseSrcPath = "V:/";
            String baseDstPath = "C:/tmp/";

            for (int i=0; i<exportVoiceAspectExportResult.getExportVoiceAspectExportResult().size(); i++) {
                //System.out.println(exportVoiceAspectExportResult.getExportVoiceAspectExportResult().get(i).getConvertResult().getValue());
                //System.out.println(exportVoiceAspectExportResult.getExportVoiceAspectExportResult().get(i).getTrackId().getValue());

                String voiceFileFullPath = exportVoiceAspectExportResult.getExportVoiceAspectExportResult().get(i).getConvertResult().getValue();
                voiceFileFullPath = StringUtils.substring(voiceFileFullPath, StringUtils.indexOf(voiceFileFullPath,":")+1);
                voiceFileFullPath = StringUtils.substring(voiceFileFullPath, StringUtils.indexOf(voiceFileFullPath,":")+1);
                voiceFileFullPath = StringUtils.substring(voiceFileFullPath, StringUtils.indexOf(voiceFileFullPath,"/")+1);


                File f = new File(baseSrcPath + voiceFileFullPath);
                if (f.exists()) {
                    selectedFiles.add(baseSrcPath + voiceFileFullPath);
                    files.add(f);
                }

            }

            //List VoiceFile
            for (String s : selectedFiles) {
                File f = new File(s);
                //System.out.println(s + "-->" + f.exists());
                //System.out.println(f.toPath());
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            String filenameDate = sdf.format(new Date());
            String zipPath = baseDstPath + filenameDate + "/";

            //File tempPath = new File(zipPath);
            //tempPath.mkdir();
            FileUtil.createFolder(zipPath);

            for(File file : files) {
                /* only java7 */
//                Files.copy(file.toPath(),
//                        (new File(zipPath + file.getName())).toPath(),
//                        StandardCopyOption.REPLACE_EXISTING);
                FileUtils.copyFile(file, new File(zipPath + file.getName()));
            }

            String zipName = "Voice_" + filenameDate + ".zip";
            String outPath = baseDstPath;

            //ZipUtil.zipFolder(zipPath, zipName, outPath);
            Zip4jUtil.zipFolder(zipPath, zipName, outPath, "1234");
            FileUtil.delete(new File(zipPath));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        System.out.println("--== FINISHED ==--");
    }

}
