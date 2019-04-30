/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.AutoAssignmentListDAO;
import com.maxelyz.core.model.entity.AutoAssignmentList;
import com.maxelyz.core.model.value.admin.ExportPaymentValue;
import com.maxelyz.utils.SecurityUtil;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
/**
 *
 * @author vee
 */
@ManagedBean
@ViewScoped
public class AutoAssignmentQueueController implements Serializable{
    private static Logger log = Logger.getLogger(AutoAssignmentQueueController.class);
    private static String REFRESH = "autoassignmentqueue.xhtml?faces-redirect=true";
    private static String SEARCHRESULT = "autoassignmentqueue.xhtml"; 

    private Date importDateFrom;
    private Date importDateTo;
    private String status;
    private List<AutoAssignmentList> autoAssigmentList;
      
    @ManagedProperty(value="#{autoAssignmentListDAO}")
    private AutoAssignmentListDAO autoAssignmentListDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:autoassignmentqueue:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        AutoAssignmentListDAO dao = getAutoAssignmentListDAO();
        
        Date today =  new Date();
        importDateFrom = today;
        importDateTo =today;
        autoAssigmentList = dao.findAutoAssignmentList(today, null, "");
    }
        
    public boolean isExportPermitted() {
       return SecurityUtil.isPermitted("admin:autoassignmentqueue:export"); 
    }
     
    public void searchAutoAssignList(){
        autoAssigmentList = this.getAutoAssignmentListDAO().findAutoAssignmentList(importDateFrom, importDateTo, status);
    }

    public Date convertStrToDate(String date) {
//        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US);
        try {
            return dateFormat.parse(date);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
//        dateFormat.setLenient(false);
//        dateFormat.parse(prospect.getTransactionDate().trim());
    }
    
    private void writeCol(Row row, int columnNo, String value, CellStyle style) {
        Cell cell = row.createCell(columnNo); //begin at 0
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
    
    private void generateXLS() {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.US);
         SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMdd", Locale.US);
//         DecimalFormat dec = new DecimalFormat("###0.00");

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setHeader("Content-Disposition", "Attachment;filename=exportWS"+sdfFileName.format(new Date())+".xls");
        OutputStream out = null;

        try {
            if (autoAssigmentList.size() > 0) {

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
                style.setBorderTop((short)1);

                Sheet sheet1 = wb.createSheet("Sheet1");
                // Create a row and put some cells in it. Rows are 0 based.
                Row row;
                int columnNo=0;
                int rowNo=0;

                row = sheet1.createRow(rowNo++);
                writeCol(row,columnNo++,"REFERENCE ID", style);         
                writeCol(row,columnNo++,"TRANSACTION DATE", style);     
                writeCol(row,columnNo++,"FIRST NAME", style);           
                writeCol(row,columnNo++,"LAST NAME", style);            
                writeCol(row,columnNo++,"EMAIL", style);                
                writeCol(row,columnNo++,"TELEPHONE", style);            
                writeCol(row,columnNo++,"MESSAGE", style);              
                writeCol(row,columnNo++,"SOURCE", style);               
                writeCol(row,columnNo++,"LANGUAGE", style);                        
                writeCol(row,columnNo++,"ASSIGN STATUS", style);                   
                writeCol(row,columnNo++,"CREATE DATE", style);                    
                writeCol(row,columnNo++,"UPDATE DATE", style);                      
                writeCol(row,columnNo++,"CAR PLATE NUMBER", style);    
                writeCol(row,columnNo++,"CAR YEAR", style);            
                writeCol(row,columnNo++,"CAR MAKE", style);           
                writeCol(row,columnNo++,"CAR MODEL", style);           
                writeCol(row,columnNo++,"CAR DESCRIPTION", style);     
                writeCol(row,columnNo++,"DATE OF BIRTH", style);        
                writeCol(row,columnNo++,"GENDER", style);               
                writeCol(row,columnNo++,"MARITAL STATUS", style);      
                writeCol(row,columnNo++,"MAIN DRIVER DRIVE", style);    
                writeCol(row,columnNo++,"MAIN DRIVER USE", style);     
                writeCol(row,columnNo++,"INSURANCE TYPE", style);       
                writeCol(row,columnNo++,"PREMIUM", style);              
                writeCol(row,columnNo++,"CAMPAIGN", style);             
                writeCol(row,columnNo++,"RENEWAL MONTH", style);        
                writeCol(row,columnNo++,"NATIONAL ID", style);       
                writeCol(row,columnNo++,"MT NUMBER", style);            
                writeCol(row,columnNo++,"POLICY START DATE", style);                  
                writeCol(row,columnNo++,"OTHER1", style);               
                writeCol(row,columnNo++,"OTHER2", style);               
                writeCol(row,columnNo++,"OTHER3", style);              
                writeCol(row,columnNo++,"OTHER4", style);               
                writeCol(row,columnNo++,"OTHER5", style);               
                writeCol(row,columnNo++,"OTHER6", style);               
                writeCol(row,columnNo++,"OTHER7", style);               
                writeCol(row,columnNo++,"OTHER8", style);               
                writeCol(row,columnNo++,"OTHER9", style);              
                writeCol(row,columnNo++,"OTHER10", style);              
                writeCol(row,columnNo++,"OTHER11", style);              
                writeCol(row,columnNo++,"OTHER12", style);             

                //detail
                for (AutoAssignmentList assignList: autoAssigmentList) {
                    row = sheet1.createRow(rowNo++);
                    columnNo=0;
                    writeCol(row, columnNo++, assignList.getReferenceId(), style);      
//                    writeCol(row, columnNo++, sdf.format(assignList.getTransactionDate()), style); 
                    writeCol(row, columnNo++, assignList.getTransactionDate(), style); 
                    writeCol(row, columnNo++, assignList.getFirstName(), style); 
                    writeCol(row, columnNo++, assignList.getLastName(), style); 
                    writeCol(row, columnNo++, assignList.getEmail(), style);      
                    writeCol(row, columnNo++, assignList.getTelephone(), style);  
                    writeCol(row, columnNo++, assignList.getMessage(), style);
//                    writeCol(row, columnNo++, dec.format(assignList.getInstallmentAmount()), style);   //H
                    writeCol(row, columnNo++, assignList.getSource(), style); 
                    writeCol(row, columnNo++, assignList.getLanguage(), style); 
                    writeCol(row, columnNo++, assignList.getAssignStatus(), style); 
                    writeCol(row, columnNo++, sdf.format(assignList.getCreateDate()), style); 
                    
                    if(assignList.getUpdateDate() == null) {
                        writeCol(row, columnNo++, "" , style); 
                    } else {
                        writeCol(row, columnNo++, sdf.format(assignList.getUpdateDate()) , style); 
                    }
                    
                    writeCol(row, columnNo++, assignList.getCarPlateNumber(), style); 
                    writeCol(row, columnNo++, assignList.getCarYear(), style); 
                    writeCol(row, columnNo++, assignList.getCarMake(), style); 
                    writeCol(row, columnNo++, assignList.getCarModel(), style); 
                    writeCol(row, columnNo++, assignList.getCarDescription(), style); 
                    writeCol(row, columnNo++, assignList.getDob(), style); 
                    writeCol(row, columnNo++, assignList.getGender(), style); 
                    writeCol(row, columnNo++, assignList.getMaritalStatus(), style); 
                    writeCol(row, columnNo++, assignList.getMainDriverDrive(), style); 
                    writeCol(row, columnNo++, assignList.getMainDriverUse(), style); 
                    writeCol(row, columnNo++, assignList.getInsuranceType(), style); 
                    writeCol(row, columnNo++, assignList.getPremium(), style); 
                    writeCol(row, columnNo++, assignList.getCampaign(), style); 
                    writeCol(row, columnNo++, assignList.getRenewalMonth(), style); 
                    writeCol(row, columnNo++, assignList.getNationalID(), style); 
                    writeCol(row, columnNo++, assignList.getMtNumber(), style); 
                    writeCol(row, columnNo++, assignList.getPolicyStartDate(), style); 
                    writeCol(row, columnNo++, assignList.getOther1(), style); 
                    writeCol(row, columnNo++, assignList.getOther2(), style); 
                    writeCol(row, columnNo++, assignList.getOther3(), style); 
                    writeCol(row, columnNo++, assignList.getOther4(), style); 
                    writeCol(row, columnNo++, assignList.getOther5(), style); 
                    writeCol(row, columnNo++, assignList.getOther6(), style); 
                    writeCol(row, columnNo++, assignList.getOther7(), style); 
                    writeCol(row, columnNo++, assignList.getOther8(), style); 
                    writeCol(row, columnNo++, assignList.getOther9(), style); 
                    writeCol(row, columnNo++, assignList.getOther10(), style); 
                    writeCol(row, columnNo++, assignList.getOther11(), style); 
                    writeCol(row, columnNo++, assignList.getOther12(), style);                    
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
        
    public Date getImportDateFrom() {
        return importDateFrom;
    }

    public void setImportDateFrom(Date importDateFrom) {
        this.importDateFrom = importDateFrom;
    }

    public Date getImportDateTo() {
        return importDateTo;
    }

    public void setImportDateTo(Date importDateTo) {
        this.importDateTo = importDateTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AutoAssignmentList> getAutoAssigmentList() {
        return autoAssigmentList;
    }

    public void setAutoAssigmentList(List<AutoAssignmentList> autoAssigmentList) {
        this.autoAssigmentList = autoAssigmentList;
    }

    public AutoAssignmentListDAO getAutoAssignmentListDAO() {
        return autoAssignmentListDAO;
    }

    public void setAutoAssignmentListDAO(AutoAssignmentListDAO autoAssignmentListDAO) {
        this.autoAssignmentListDAO = autoAssignmentListDAO;
    }
    
}
