/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.itextpdf.text.DocumentException;
import com.maxelyz.converter.PaymentModeConverter;
import com.maxelyz.core.common.dto.PaymentMethodStringCastor;
import com.maxelyz.core.controller.admin.dto.StringTemplateDTO;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.test.MergePDF;
import com.maxelyz.utils.FileUploadBean;
import com.maxelyz.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 *
 * @author Administrator
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class ExportNofileController {

    private static String REFRESH = "exportnofile.xhtml?faces-redirect=true";
    private static String EDIT = "exportnofile.xhtml";
    private static String SELECT = "nolog.xhtml?faces-redirect=true";
    private static Logger log = Logger.getLogger(ExportNofileController.class);
    private List<StringTemplateDTO> stringTemplateDTOList;

    //ProductSponsor
//    private ProductSponsor productSponsor;
    private SelectItem[] productSponsorList;
    private List<ProductSponsor> productSponsors;

    //Product
    private SelectItem[] productList;
    private List<Product> products;

    //Marketing
    private SelectItem[] marketingList;
    private List<Marketing> marketings;

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

    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;

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
    private Integer marketingId;
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

    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> surname = new ArrayList<String>();

    private String errorDetail = "";
    private String excelDetail = "";

    private Integer cntRecords;

    private MergePDF mergePDF;

    private static final int NUM_COLUMN_SEARCH = 7;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:exportnofile:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        Date today = new Date();

        fromDate = today;
        toDate = today;

        formatId = null;

        this.setMarketingList();

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

    private String genSQL(String tableView, String orderField, Integer productSponsorId, Integer productId, Integer lastpaymentApprovedReason, Boolean genRank, String fixfield, String fieldRefNo, Integer marketingId) {

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

            if (marketingId != null) {
                sql += " where marketing_id_1 = " + marketingId.toString();
            }

 /*           sql += " where sale_date_1 between '" + sqlFromDate + "'";
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
*/
            if (StringUtils.isNotBlank(orderField)) {
                sql += " order by " + orderField;
            }
        }

        return sql;
    }

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

        exportMode = "all";
        lastpaymentApprovedReason = 0;

        //System.out.println("product id"+productId);
        ArrayList<String> sourceFiles = new ArrayList<String>();
        ArrayList<String> sourceFilesDoc = new ArrayList<String>();
        ArrayList<String> sourceFilesWithDeclare = new ArrayList<String>();
        ArrayList<String> allSourceFilesTemp = new ArrayList<String>();
        ArrayList<String> sourceFilesTarget = new ArrayList<String>();
        String mergedFilePathTemp = "";
        Boolean haveDeclare = false;
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        SimpleDateFormat sdfFileName = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        fileNameExtend = sdfFileName.format(fromDate) + " " + sdf.format(cal.getTime());
        fileNameExtend1 = sdfFileName.format(fromDate);

        String marketingCode = marketingDAO.findMarketing(marketingId).getCode();
        FILENAME_YES_TITLE = "Nofile_" + marketingCode;
        FILENAME_VOICE_TITLE = "Nofile_" + marketingCode;
        FILENAME_APP_TITLE = "Appfile_" + marketingCode;

        fileName1 = FILENAME_YES_TITLE + " " + fileNameExtend + ".txt";
        fileName2 = FILENAME_VOICE_TITLE + " " + fileNameExtend + ".xlsx";
        fileName3 = FILENAME_APP_TITLE + " " + fileNameExtend + ".xlsx";

        filePath1 = urlUploadPath() + fileName1;
        filePath2 = urlUploadPath() + fileName2;
        filePath3 = urlUploadPath() + fileName3;

        generateTXTByView(1, genSQL("vwF13_po_no", "product_id_1,ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "PolicyNo", marketingId), fileName1, "ref_no_1", true, "|");
        generateXLSByView(1, genSQL("vwF13_po_no", "product_id_1,ref_no_1", null, productId, lastpaymentApprovedReason, false, "", "PolicyNo", marketingId), fileName2, "ref_no_1", true);

        System.out.println("sourceFiles " + sourceFiles.size());
//        if ((sourceFiles.size() != 0) || formatId == 3) {
            try {
                String username = JSFUtil.getUserSession().getUserName();

                YesLog yesLog = new YesLog();
                yesLog.setYesFilename(fileName1);
                yesLog.setYesFilepath(filePath1);
                yesLog.setAppFilename(fileName2);
                yesLog.setAppFilepath(filePath2);
//                yesLog.setVoiceFilename(fileName3);
//                yesLog.setVoiceFilepath(filePath3);
//            yesLog.setResult("");
                yesLog.setFileType(2);
                yesLog.setCreateDate(new Date());
                yesLog.setCreateBy(username);
                yesLogDAO.create(yesLog);
            } catch (Exception e) {
                log.error(e);
            }

//        FacesContext.getCurrentInstance().responseComplete();
            return SELECT;
//        }
//        return errorDetail;
    }

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
        
            messageMergeError = null;
            List<StringTemplateDTO> list = new ArrayList<StringTemplateDTO>();
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            name = new ArrayList<String>();
            surname = new ArrayList<String>();
            try {

                String sql = genSQL("maxar_po_no", "product_id_1,ref_no_1", productSponsorId, productId, lastpaymentApprovedReason, false, "", "ref_no", marketingId);

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
                colName1 = "Product";
                colName2 = "Ref No.";
                colName3 = "Insured Title";
                colName4 = "First Name";
                colName5 = "Last Name";
                colName6 = "Plan";
                colName7 = "Premium";
                while (rs.next()) {
                    int count = 0;
                    StringTemplateDTO fl = new StringTemplateDTO(rs.getString("product_code"), rs.getString("ref_no"), rs.getString("insured_title"), rs.getString("insured_name"), rs.getString("insured_surname"), rs.getString("po_plan_code"), rs.getString("po_net_premium"));
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

    public void setMarketingList() {

        marketings = marketingDAO.findMarketingEntities();
        marketingList = new SelectItem[marketings.size() + 1];
        int pos = 0;
        marketingList[pos++] = new SelectItem(null, "Select Marketing");
        for (Marketing mkt : marketings) {
            marketingList[pos++] = new SelectItem(mkt.getId(), mkt.getName());
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

    public void onMarketingIdChange(ValueChangeEvent event) {

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

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }

    public Integer getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(Integer marketingId) {
        this.marketingId = marketingId;
    }

    public List<Marketing> getMarketings() {
        return marketings;
    }

    public void setMarketings(List<Marketing> marketings) {
        this.marketings = marketings;
    }

    public SelectItem[] getMarketingList() {
        return marketingList;
    }

    public void setMarketingList(SelectItem[] marketingList) {
        this.marketingList = marketingList;
    }
}
