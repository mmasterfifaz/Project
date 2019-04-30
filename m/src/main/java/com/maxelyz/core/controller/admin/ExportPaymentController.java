/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.ExportPaymentValue;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

/**
 *
 * @author Administrator
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class ExportPaymentController {
     
    private static Logger log = Logger.getLogger(ExportPaymentController.class);
    private Date fromDate, toDate;
    private List<ExportPaymentValue> purchaseOrderInstallmentList;
    
    @ManagedProperty(value="#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:exportpayment:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        Date today = new Date();
        fromDate = today;
        toDate = today;      
    }
        

    private void writeCol(Row row, int columnNo, String value, CellStyle style) {
        Cell cell = row.createCell(columnNo); //begin at 0
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void generateXLS() {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.US);
         SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMdd", Locale.US);
         DecimalFormat dec = new DecimalFormat("###0.00");

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setHeader("Content-Disposition", "Attachment;filename=exportPayment"+sdfFileName.format(new Date())+".xls");
        OutputStream out = null;

        try {
            if (purchaseOrderInstallmentList.size()>0) {

                out = response.getOutputStream();

                Workbook wb = new HSSFWorkbook();
                // Create a new font and alter it.
                Font font = wb.createFont();
                font.setFontHeightInPoints((short)8);
                font.setFontName("Arial");
                // Fonts are set into a style so create a new one to use.
                CellStyle style = wb.createCellStyle();
                style.setFont(font);
                
                //Set Border
                style.setBorderBottom((short)1);
                style.setBorderLeft((short)1);
                style.setBorderRight((short)1);
                style .setBorderTop((short)1);

                Sheet sheet1 = wb.createSheet("Sheet1");
                // Create a row and put some cells in it. Rows are 0 based.
                Row row;
                int columnNo=0;
                int rowNo=0;

                row = sheet1.createRow(rowNo++);
                writeCol(row,columnNo++,"TYPE", style);         //A
                writeCol(row,columnNo++,"BANK_CODE", style);    //B
                writeCol(row,columnNo++,"BANK_ACC", style);     //C
                writeCol(row,columnNo++,"DATE", style);         //D
                writeCol(row,columnNo++,"PAYEE_NAME", style);   //E
                writeCol(row,columnNo++,"REF1", style);         //F
                writeCol(row,columnNo++,"REF2", style);         //G
                writeCol(row,columnNo++,"AMOUNT", style);       //H
                writeCol(row,columnNo++,"TRAN_CODE", style);    //I
                writeCol(row,columnNo++,"TRAN_TYPE", style);    //J
                writeCol(row,columnNo++,"CHEQUE_BANK", style);  //K
                writeCol(row,columnNo++,"CHEQUE_NBR", style);   //L
                writeCol(row,columnNo++,"REMARKS", style);      //M
                writeCol(row,columnNo++,"APPL_NBR", style);     //N
                writeCol(row,columnNo++,"POL_NBR", style);      //O
                writeCol(row,columnNo++,"ENDT_NBR", style);     //P
                writeCol(row,columnNo++,"COMP_POL_NBR", style); //Q
                writeCol(row,columnNo++,"Result of Processing", style); //R
                writeCol(row,columnNo++,"CARD_NO", style);              //S
                writeCol(row,columnNo++,"CARD_HOLDER_NAME", style);     //T
                writeCol(row,columnNo++,"CARD_EXPIRY_DATE", style);     //U
                writeCol(row,columnNo++,"CARD_ISSUER", style);          //V
                writeCol(row,columnNo++,"CARD_TYPE", style);            //W


                //detail
                for (ExportPaymentValue poi: purchaseOrderInstallmentList) {
                    row = sheet1.createRow(rowNo++);
                    columnNo=0;
                    String payName = (poi.getInitialLabel()!=null?poi.getInitialLabel():"")+(poi.getName()!=null?poi.getName():"")+" "+(poi.getSurname()!=null?poi.getSurname():"");
                            
                    writeCol(row, columnNo++, "", style); //A
                    writeCol(row, columnNo++, "", style); //B
                    writeCol(row, columnNo++, "", style); //C
                    writeCol(row, columnNo++, sdf.format(poi.getPurchaseDate()), style); //D
                    writeCol(row, columnNo++, payName, style);       //E
                    writeCol(row, columnNo++, poi.getPoRefNo(), style);  //F
                    writeCol(row, columnNo++, poi.getIdNo(), style);//G
                    writeCol(row, columnNo++, dec.format(poi.getInstallmentAmount()), style);   //H
                    writeCol(row, columnNo++, "", style); //I
                    writeCol(row, columnNo++, "", style); //J
                    writeCol(row, columnNo++, "", style); //K
                    writeCol(row, columnNo++, "", style); //L
                    writeCol(row, columnNo++, "", style); //M
                    writeCol(row, columnNo++, "", style); //N
                    writeCol(row, columnNo++, "", style); //O
                    writeCol(row, columnNo++, "", style); //P
                    writeCol(row, columnNo++, "", style); //Q
                    writeCol(row, columnNo++, "", style); //R
                    writeCol(row, columnNo++, poi.getCardNo(), style); //S
                    writeCol(row, columnNo++, poi.getCardHolderName(), style); //T
                    writeCol(row, columnNo++, poi.getCardExpiryMonth().toString()+"/"+poi.getCardExpiryYear().toString(), style); //U
                    writeCol(row, columnNo++, poi.getCardIssuerName(), style); //V
                    writeCol(row, columnNo++, poi.getCardType(), style); //W
                }
                //set autosize column
                for (short i=0;i<columnNo;i++) {
                    sheet1.autoSizeColumn(i);
                }
                wb.write(out);
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch(Exception e) {}
            }
        }
    }

    public void exportListener(ActionEvent event) {
        generateXLS();
        FacesContext.getCurrentInstance().responseComplete();
    }
        
    public void searchActionListener(ActionEvent event) {       
        purchaseOrderInstallmentList = this.purchaseOrderDAO.findPurchaseOrderValueByPurchaseDate(fromDate, toDate);
    }
    
    public List<ExportPaymentValue> getList() {
        return purchaseOrderInstallmentList;
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
  
}
