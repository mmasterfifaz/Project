/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.dao.SettlementDAO;
import com.maxelyz.core.model.dao.SettlementDetailDAO;
import com.maxelyz.core.model.entity.PurchaseOrderInstallment;
import com.maxelyz.core.model.entity.Settlement;
import com.maxelyz.core.model.entity.SettlementDetail;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.richfaces.event.UploadEvent;
//import org.richfaces.model.UploadItem;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class ImportMailOrderController {
    
    private static Logger log = Logger.getLogger(ImportMailOrderController.class);
    private List<Settlement> settlementList;
    private String type;
//    private UploadItem item;
    private FileInputStream fileInputStream;
    private POIFSFileSystem fsFileSystem;
    private Settlement settlement;
    private List<SettlementDetail> settlementDetailList;
    private Integer refCol,amountCol,dateCol;
    private String message="";
    
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value = "#{settlementDAO}")
    private SettlementDAO settlementDAO;
    @ManagedProperty(value = "#{settlementDetailDAO}")
    private SettlementDetailDAO settlementDetailDAO;

    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:settlement:import")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        type = "mailorder";
        settlementList = getSettlementDAO().findSettlementByType(type);
    }
    
    //Listener
//    public void uploadListener(UploadEvent event) throws Exception{
//        item = event.getUploadItem();
//        readExcelFile(item.getFile().getPath(),item.getFileName());
//    }  
    
        
//    public void readExcelFile(String fileName,String fileType)   {   
//        try {
//            fileInputStream = new FileInputStream(item.getFile().getPath());
//            if(item.getFileName().contains(".xlsx")) {
//                XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);
//            } else {
//                fsFileSystem = new POIFSFileSystem(fileInputStream); 
//                HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);          
//            }
//        }catch (Exception e) {
//            log.error(e);
//        }
//    }  
    
//    public void saveAction() {
//        if(item != null) {
//            message = "";
//            String username = JSFUtil.getUserSession().getUserName();
//            Date now = new Date();
//            Integer cntTotalRecord=0;
//            Integer cntComplete=0;
//            settlement = new Settlement();
//            settlementDetailList = new ArrayList<SettlementDetail>();
//            try {
//                //create settlement
//                settlement.setType(type);
//                settlement.setCreateBy(username);
//                settlement.setCreateDate(now);
//                getSettlementDAO().create(settlement);
//
//                fileInputStream = new FileInputStream(item.getFile().getPath());
//                if(item.getFileName().contains(".xlsx")) {
//                    XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);
//                    XSSFSheet xssfSheet = workBook.getSheetAt(0);                  
//                    Iterator rowIterator = xssfSheet.rowIterator(); 
//                    cntTotalRecord = xssfSheet.getLastRowNum(); // count total record
//
//                    XSSFRow xssfRow;
//                    if(rowIterator.hasNext()) {     //check header row
//                        xssfRow = (XSSFRow) rowIterator.next();
//                        Iterator iterator = xssfRow.cellIterator(); 
//                        while(iterator.hasNext())  {   //find column index --> REF1 REF2 AMOUNT DATE
//                            XSSFCell xssfCell = (XSSFCell) iterator.next(); 
//                            if(xssfCell.getStringCellValue().toString().equals("Merchant Ref.")) {
//                                refCol = xssfCell.getColumnIndex();
//                            } else if(xssfCell.getStringCellValue().toString().equals("Amount")) {
//                                        amountCol = xssfCell.getColumnIndex();
//                            } else if(xssfCell.getStringCellValue().toString().equals("Transaction Date")) {
//                                        dateCol = xssfCell.getColumnIndex();
//                            }
//                        }
//                    }
//                    while(rowIterator.hasNext()) {  //check Po.Ref_no, po.customer_ref_no, po.purchase_date, po.amount  --> save settlement detail
//                        xssfRow = (XSSFRow) rowIterator.next();
//                        XSSFCell xssfCell;
//
//                        xssfCell = xssfRow.getCell(refCol, Row.CREATE_NULL_AS_BLANK);
//                        xssfCell.setCellType(Cell.CELL_TYPE_STRING);
//                        String refNo = xssfCell.getStringCellValue().toString();
//
//                        xssfCell = xssfRow.getCell(amountCol, Row.CREATE_NULL_AS_BLANK);
//                        xssfCell.setCellType(Cell.CELL_TYPE_STRING);
//                        String amountStr = xssfCell.getStringCellValue();
//                        Double amount = Double.valueOf(amountStr);
//
//                        xssfCell = xssfRow.getCell(dateCol, Row.CREATE_NULL_AS_BLANK);
//                        xssfCell.setCellType(Cell.CELL_TYPE_STRING);
//                        String dateStr = xssfCell.getStringCellValue();
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//                        Date purchaseDate = dateFormat.parse(dateStr);
//                        
//                        SettlementDetail settlementDetail = new SettlementDetail();
//                        PurchaseOrderInstallment poi = getPurchaseOrderDAO().findPOISettlementMail(refNo, amount, purchaseDate);
//                        String result="";
//                        if(poi != null){    // match
//                            if(poi.getPaymentStatus().equals("notpaid") || poi.getPaymentStatus() == null) {       //complete
//                                cntComplete++;
//                                result = "Complete";
//                                getPurchaseOrderDAO().updatePaymentStatusInstallmentById(poi.getId(), refNo);
//                            } else if(poi.getPaymentStatus().equals("paid")) {   //duplicate
//                                    result = "Duplicate";
//                            }
//                        } else {   // not match
//                            result = "Not Match";
//                        }
//                        settlementDetail.setSettlement(settlement);
//                        settlementDetail.setResult(result);
//                        settlementDetail.setSaleRefNo(refNo);
//                        settlementDetail.setCustomerRefNo(refNo);
//                        settlementDetail.setAmount(BigDecimal.valueOf(amount));
//                        settlementDetail.setTransDate(purchaseDate);
//                        settlementDetailList.add(settlementDetail);
//                    }
//                } else {
//                    fsFileSystem = new POIFSFileSystem(fileInputStream); 
//                    HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);
//                    HSSFSheet hssfSheet = workBook.getSheetAt(0);                  
//                    Iterator rowIterator = hssfSheet.rowIterator(); 
//                    cntTotalRecord = hssfSheet.getLastRowNum(); // count total record
//
//                    HSSFRow hssfRow;
//                    if(rowIterator.hasNext()) {     //check header row
//                        hssfRow = (HSSFRow) rowIterator.next();
//                        Iterator iterator = hssfRow.cellIterator(); 
//                        while(iterator.hasNext())  {   //find column index --> REF1 REF2 AMOUNT DATE
//                            HSSFCell hssfCell = (HSSFCell) iterator.next(); 
//                            if(hssfCell.getStringCellValue().toString().equals("Merchant Ref.")) {
//                                refCol = hssfCell.getColumnIndex();
//                            } else if(hssfCell.getStringCellValue().toString().equals("Amount")) {
//                                        amountCol = hssfCell.getColumnIndex();
//                            } else if(hssfCell.getStringCellValue().toString().equals("Transaction Date")) {
//                                        dateCol = hssfCell.getColumnIndex();
//                            }
//                        }
//                    }
//                    while(rowIterator.hasNext()) {  //check Po.Ref_no, po.customer_ref_no, po.purchase_date, po.amount  --> save settlement detail
//                        hssfRow = (HSSFRow) rowIterator.next();
//                        HSSFCell hssfCell;
//
//                        hssfCell = hssfRow.getCell(refCol, Row.CREATE_NULL_AS_BLANK);
//                        hssfCell.setCellType(Cell.CELL_TYPE_STRING);
//                        String refNo = hssfCell.getStringCellValue().toString();
//
//                        hssfCell = hssfRow.getCell(amountCol, Row.CREATE_NULL_AS_BLANK);
//                        hssfCell.setCellType(Cell.CELL_TYPE_STRING);
//                        String amountStr = hssfCell.getStringCellValue();
//                        Double amount = Double.valueOf(amountStr);
//                        
//                        hssfCell = hssfRow.getCell(dateCol, Row.CREATE_NULL_AS_BLANK);
//                        hssfCell.setCellType(Cell.CELL_TYPE_STRING);
//                        String dateStr = hssfCell.getStringCellValue();
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//                        Date purchaseDate = dateFormat.parse(dateStr);
//
//                        SettlementDetail settlementDetail = new SettlementDetail();
//                        PurchaseOrderInstallment poi = getPurchaseOrderDAO().findPOISettlementMail(refNo, amount, purchaseDate);
//                        String result="";
//                        if(poi != null){    // match
//                            if(poi.getPaymentStatus().equals("notpaid") || poi.getPaymentStatus() == null) {       //complete
//                                cntComplete++;
//                                result = "Complete";
//                                getPurchaseOrderDAO().updatePaymentStatusInstallmentById(poi.getId(), refNo);
//                            } else if(poi.getPaymentStatus().equals("paid")) {   //duplicate
//                                    result = "Duplicate";
//                            }
//                        } else {   // not match
//                            result = "Not Match";
//                        }
//                        settlementDetail.setSettlement(settlement);
//                        settlementDetail.setResult(result);
//                        settlementDetail.setSaleRefNo(refNo);
//                        settlementDetail.setCustomerRefNo(refNo);
//                        settlementDetail.setAmount(BigDecimal.valueOf(amount));
//                        settlementDetail.setTransDate(purchaseDate);
//                        settlementDetailList.add(settlementDetail);
//                    }
//                }
//                settlement.setTotalRecord(cntTotalRecord);
//                settlement.setCompleteRecord(cntComplete);
//                settlement.setSettlementDetailCollection(settlementDetailList);
//                getSettlementDAO().edit(settlement);       
//            }catch (Exception e) {
//                log.error(e);
//            }
//        } else {
//            message = "Import file is require";
//        }
//        item = null;
//        settlementList = getSettlementDAO().findSettlementByType(type);
//        FacesContext.getCurrentInstance().renderResponse();
//    }
           
    public void settlementDetailListener () {
        if(JSFUtil.getRequestParameterMap("settlementId") != null) {
            Integer settlementId = Integer.parseInt(JSFUtil.getRequestParameterMap("settlementId"));
            settlementDetailList = getSettlementDetailDAO().findSettlementDetailBySettlementId(settlementId);
        }
    }
    
    //List to UI
    public List<Settlement> getSettlementList() {
        return settlementList;
    }
    
    public List<SettlementDetail> getSettlementDetailList() {
        return settlementDetailList;
    }
    //Set DAO
    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }
    
    public SettlementDAO getSettlementDAO() {
        return settlementDAO;
    }

    public void setSettlementDAO(SettlementDAO settlementDAO) {
        this.settlementDAO = settlementDAO;
    }
    
    public SettlementDetailDAO getSettlementDetailDAO() {
        return settlementDetailDAO;
    }

    public void setSettlementDetailDAO(SettlementDetailDAO settlementDetailDAO) {
        this.settlementDetailDAO = settlementDetailDAO;
    }
    //Set properties
//    public UploadItem getItem() {
//        return item;
//    }
//
//    public void setItem(UploadItem item) {
//        this.item = item;
//    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

