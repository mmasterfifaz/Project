/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import com.maxelyz.core.service.CustomerService;
import com.maxelyz.core.model.entity.CustomerHistoricalGroup;
import com.maxelyz.core.model.entity.CustomerHistoricalColumn;
import com.maxelyz.core.model.entity.CustomerHistorical;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
//import org.richfaces.event.UploadEvent;
//import org.richfaces.model.UploadItem;
//2012 xlsx
import org.apache.poi.ss.usermodel.Cell; 
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row; 
import org.apache.poi.xssf.usermodel.*; 
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

/**
 *
 * @author Administrator
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class CustomerHistoricalImportController {
    private static Logger log = Logger.getLogger(CustomerHistoricalImportController.class);
    private static String REDIRECT_PAGE = "customerhistorical.jsf";
    private static String BACK = "customerhistorical.xhtml";
    private Map<String, Integer> sheetList = new LinkedHashMap<String, Integer>();
    private Map<String, Integer> columnList = new LinkedHashMap<String, Integer>();
    private List<String> dataList;
    private List<Integer> selectedColumn;
    private int sheetId;
    private int columnId;
    private int step;
//    public UploadItem item;
    public UploadedFile item;
    public boolean checkboxFirstRow=true;
    public String stringHeader="";
    public FileInputStream fileInputStream;
    public POIFSFileSystem fsFileSystem;
    private List<CustomerHistoricalColumn> customerHistoricalColumnList;
    private List<CustomerHistorical> customerHistoricalList;
    public Integer total,success,notMatch;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm           ", Locale.US);
            
    private CustomerHistoricalGroup customerHistoricalGroup;
    @ManagedProperty(value = "#{customerService}")
    private CustomerService customerService;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:customerhistorical:edit")) {  
            SecurityUtil.redirectUnauthorize();  
        }
        step=1; 
        if(selectedColumn != null)
            selectedColumn.clear();
        clearSheet();
        String selectedID  = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID==null) {
            JSFUtil.redirect(REDIRECT_PAGE);
        } else {
            customerHistoricalGroup = customerService.findCustomerHistoricalGroup(new Integer(selectedID));
        }
    }
    
    public String backAction() {
        return BACK;
    }
    //Listener
    public void step1Listener(ActionEvent event) {
        step = 1;
        selectedColumn.clear();
//        item.getFileName();
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public void step2Listener() {
        step = 2;
        preview();
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void step3Listener() {
        step = 3;
        saveAction();
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public void saveAction() {  
        //create customerHistoricalColumn
        customerHistoricalColumnList = new ArrayList<CustomerHistoricalColumn>();
        
        //create customerHistorical
        customerHistoricalList = new ArrayList<CustomerHistorical>();
        
        String username = JSFUtil.getUserSession().getUserName();
        Date now = new Date();
        String value;
        Integer cntRefCustomer=0;
        Integer noCol=0;
        total = 0;
         try {
            dataList =  new ArrayList();  
            fileInputStream = new FileInputStream(item.getName());   
            
            if(item.getName().contains(".xlsx")) {
                XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);
                XSSFSheet xssfSheet = workBook.getSheetAt(sheetId-1);                  
                Iterator rowIterator = xssfSheet.rowIterator(); 
                XSSFRow xssfRow;
                if(rowIterator.hasNext()){
                    xssfRow = (XSSFRow) rowIterator.next();
                    Iterator iterator = xssfRow.cellIterator(); 
                    noCol = (int) xssfRow.getLastCellNum();
                    if(checkboxFirstRow) {
                        total = xssfSheet.getLastRowNum();
                        while (iterator.hasNext())  {
                            CustomerHistoricalColumn customerHistoricalColumn = new CustomerHistoricalColumn();
                            XSSFCell xssfCell = (XSSFCell) iterator.next(); 
                            customerHistoricalColumn.setColumnName(xssfCell.getStringCellValue().toString());
                            customerHistoricalColumn.setColumnNo(xssfCell.getColumnIndex()+1);
                            customerHistoricalColumn.setCustomerHistoricalGroup(customerHistoricalGroup);
                            customerHistoricalColumnList.add(customerHistoricalColumn);
                        }
                    } else {
                        total = xssfSheet.getLastRowNum()+1;
                        while (iterator.hasNext())  {
                            CustomerHistoricalColumn customerHistoricalColumn = new CustomerHistoricalColumn();
                            XSSFCell xssfCell = (XSSFCell) iterator.next(); 
                            String temp="Column " +(xssfCell.getColumnIndex()+1);
                            customerHistoricalColumn.setColumnName(temp);
                            customerHistoricalColumn.setColumnNo(xssfCell.getColumnIndex()+1);
                            customerHistoricalColumn.setCustomerHistoricalGroup(customerHistoricalGroup);
                            customerHistoricalColumnList.add(customerHistoricalColumn);
                        }
                    }
                }
                rowIterator = xssfSheet.rowIterator(); 
                if(checkboxFirstRow) {
                    xssfRow = (XSSFRow) rowIterator.next();
                }
                while (rowIterator.hasNext())   {  
                    xssfRow = (XSSFRow) rowIterator.next();
                    XSSFCell xssfCell = xssfRow.getCell(columnId-1, Row.CREATE_NULL_AS_BLANK);
                    xssfCell.setCellType(Cell.CELL_TYPE_STRING);
                    Integer customer = customerService.findCustomerByReferenceNo(xssfCell.getStringCellValue().toString()).size();
                    if(customer > 0) {
                        CustomerHistorical customerHistorical = new CustomerHistorical();
                        customerHistorical.setCustomerHistoricalGroup(customerHistoricalGroup);
                        customerHistorical.setCustomerReferenceNo(xssfCell.getStringCellValue().toString());
                        customerHistorical.setCreateDate(now);
                        ++cntRefCustomer;
                        Iterator iterator = xssfRow.cellIterator(); 
                        while (iterator.hasNext()) {
                            xssfCell = (XSSFCell) iterator.next();
                            value = "";
                            if(xssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                if (DateUtil.isCellDateFormatted(xssfCell)) {
                                    value = dateFormat.format(xssfCell.getDateCellValue()).toString().trim();
                                } else {
                                    xssfCell.setCellType(Cell.CELL_TYPE_STRING);
                                    value = xssfCell.getStringCellValue().toString();
                                }
                            } else {
                                xssfCell.setCellType(Cell.CELL_TYPE_STRING);
                                value = xssfCell.getStringCellValue().toString();
                            }
                            if(xssfCell.getColumnIndex()+1 == 1) 
                                customerHistorical.setCol1(value);
                            else if((xssfCell.getColumnIndex()+1 == 2))
                                    customerHistorical.setCol2(value);
                            else if((xssfCell.getColumnIndex()+1 == 3))
                                    customerHistorical.setCol3(value);
                            else if((xssfCell.getColumnIndex()+1 == 4))
                                    customerHistorical.setCol4(value);
                            else if((xssfCell.getColumnIndex()+1 == 5))
                                    customerHistorical.setCol5(value);
                            else if((xssfCell.getColumnIndex()+1 == 6))
                                    customerHistorical.setCol6(value);
                            else if((xssfCell.getColumnIndex()+1 == 7))
                                    customerHistorical.setCol7(value);
                            else if((xssfCell.getColumnIndex()+1 == 8))
                                    customerHistorical.setCol8(value);
                            else if((xssfCell.getColumnIndex()+1 == 9))
                                    customerHistorical.setCol9(value);
                             else if((xssfCell.getColumnIndex()+1 == 10))
                                    customerHistorical.setCol10(value);
                            else if((xssfCell.getColumnIndex()+1 == 11))
                                    customerHistorical.setCol11(value);
                            else if((xssfCell.getColumnIndex()+1 == 12))
                                    customerHistorical.setCol12(value);
                            else if((xssfCell.getColumnIndex()+1 == 13))
                                    customerHistorical.setCol13(value);
                            else if((xssfCell.getColumnIndex()+1 == 14))
                                    customerHistorical.setCol14(value);
                            else if((xssfCell.getColumnIndex()+1 == 15))
                                    customerHistorical.setCol15(value);
                            else if((xssfCell.getColumnIndex()+1 == 16))
                                    customerHistorical.setCol16(value);
                            else if((xssfCell.getColumnIndex()+1 == 17))
                                    customerHistorical.setCol17(value);
                            else if((xssfCell.getColumnIndex()+1 == 18))
                                    customerHistorical.setCol18(value);
                            else if((xssfCell.getColumnIndex()+1 == 19))
                                    customerHistorical.setCol19(value);
                            else if((xssfCell.getColumnIndex()+1 == 20))
                                    customerHistorical.setCol20(value);
                            else if((xssfCell.getColumnIndex()+1 == 21))
                                    customerHistorical.setCol21(value);
                            else if((xssfCell.getColumnIndex()+1 == 22))
                                    customerHistorical.setCol22(value);
                            else if((xssfCell.getColumnIndex()+1 == 23))
                                    customerHistorical.setCol23(value);
                            else if((xssfCell.getColumnIndex()+1 == 24))
                                    customerHistorical.setCol24(value);
                            else if((xssfCell.getColumnIndex()+1 == 25))
                                    customerHistorical.setCol25(value);
                            else if((xssfCell.getColumnIndex()+1 == 26))
                                    customerHistorical.setCol26(value);
                            else if((xssfCell.getColumnIndex()+1 == 27))
                                    customerHistorical.setCol27(value);
                            else if((xssfCell.getColumnIndex()+1 == 28))
                                    customerHistorical.setCol28(value);
                            else if((xssfCell.getColumnIndex()+1 == 29))
                                    customerHistorical.setCol29(value);
                            else if((xssfCell.getColumnIndex()+1 == 30))
                                    customerHistorical.setCol30(value);
                        }
                        customerHistoricalList.add(customerHistorical);
                    }
                } 
            } else {
                    fsFileSystem = new POIFSFileSystem(fileInputStream);  
                    HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);  
                    HSSFSheet hssfSheet = workBook.getSheetAt(sheetId-1); 
                    Iterator rowIterator = hssfSheet.rowIterator(); 
                    HSSFRow hssfRow;
                    if(rowIterator.hasNext()){
                        hssfRow = (HSSFRow) rowIterator.next();
                        Iterator iterator = hssfRow.cellIterator(); 
                        noCol = (int) hssfRow.getLastCellNum();
                        if(checkboxFirstRow) {
                            total = hssfSheet.getLastRowNum();
                            while (iterator.hasNext())  {
                                CustomerHistoricalColumn customerHistoricalColumn = new CustomerHistoricalColumn();
                                HSSFCell hssfCell = (HSSFCell) iterator.next(); 
                                customerHistoricalColumn.setColumnName(hssfCell.getStringCellValue().toString());
                                customerHistoricalColumn.setColumnNo(hssfCell.getColumnIndex()+1);
                                customerHistoricalColumn.setCustomerHistoricalGroup(customerHistoricalGroup);
                                customerHistoricalColumnList.add(customerHistoricalColumn);
                            }
                        } else {
                            total = hssfSheet.getLastRowNum()+1;
                            while (iterator.hasNext())  {
                                CustomerHistoricalColumn customerHistoricalColumn = new CustomerHistoricalColumn();
                                HSSFCell hssfCell = (HSSFCell) iterator.next(); 
                                String temp="Column " +(hssfCell.getColumnIndex()+1);
                                customerHistoricalColumn.setColumnName(temp);
                                customerHistoricalColumn.setColumnNo(hssfCell.getColumnIndex()+1);
                                customerHistoricalColumn.setCustomerHistoricalGroup(customerHistoricalGroup);
                                customerHistoricalColumnList.add(customerHistoricalColumn);
                            }
                        }
                    }
                    rowIterator = hssfSheet.rowIterator(); 
                    if(checkboxFirstRow) {
                        hssfRow = (HSSFRow) rowIterator.next();
                    }
                    while (rowIterator.hasNext()) {
                        hssfRow = (HSSFRow) rowIterator.next();
                        HSSFCell hssfCell = hssfRow.getCell(columnId-1, Row.CREATE_NULL_AS_BLANK);
                        hssfCell.setCellType(Cell.CELL_TYPE_STRING);
                        Integer customer = customerService.findCustomerByReferenceNo(hssfCell.getStringCellValue().toString()).size();
                        if(customer > 0) {
                            CustomerHistorical customerHistorical = new CustomerHistorical();
                            customerHistorical.setCustomerHistoricalGroup(customerHistoricalGroup);
                            customerHistorical.setCustomerReferenceNo(hssfCell.getStringCellValue().toString());
                            customerHistorical.setCreateDate(now);
                            ++cntRefCustomer;
                            Iterator iterator = hssfRow.cellIterator(); 
                            while (iterator.hasNext()) {
                                hssfCell = (HSSFCell) iterator.next();
                                value = "";
                                if(hssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    if (DateUtil.isCellDateFormatted(hssfCell)) {
                                        value = dateFormat.format(hssfCell.getDateCellValue()).toString().trim();
                                    } else {
                                        hssfCell.setCellType(Cell.CELL_TYPE_STRING);
                                        value = hssfCell.getStringCellValue().toString();
                                    }
                                } else {
                                    hssfCell.setCellType(Cell.CELL_TYPE_STRING);
                                    value = hssfCell.getStringCellValue().toString();
                                }
                                if(hssfCell.getColumnIndex()+1 == 1) 
                                    customerHistorical.setCol1(value);
                                else if((hssfCell.getColumnIndex()+1 == 2))
                                        customerHistorical.setCol2(value);
                                else if((hssfCell.getColumnIndex()+1 == 3))
                                        customerHistorical.setCol3(value);
                                else if((hssfCell.getColumnIndex()+1 == 4))
                                        customerHistorical.setCol4(value);
                                else if((hssfCell.getColumnIndex()+1 == 5))
                                        customerHistorical.setCol5(value);
                                else if((hssfCell.getColumnIndex()+1 == 6))
                                        customerHistorical.setCol6(value);
                                else if((hssfCell.getColumnIndex()+1 == 7))
                                        customerHistorical.setCol7(value);
                                else if((hssfCell.getColumnIndex()+1 == 8))
                                        customerHistorical.setCol8(value);
                                else if((hssfCell.getColumnIndex()+1 == 9))
                                        customerHistorical.setCol9(value);
                                else if((hssfCell.getColumnIndex()+1 == 10))
                                        customerHistorical.setCol10(value);
                                else if((hssfCell.getColumnIndex()+1 == 11))
                                        customerHistorical.setCol11(value);
                                else if((hssfCell.getColumnIndex()+1 == 12))
                                        customerHistorical.setCol12(value);
                                else if((hssfCell.getColumnIndex()+1 == 13))
                                        customerHistorical.setCol13(value);
                                else if((hssfCell.getColumnIndex()+1 == 14))
                                        customerHistorical.setCol14(value);
                                else if((hssfCell.getColumnIndex()+1 == 15))
                                        customerHistorical.setCol15(value);
                                else if((hssfCell.getColumnIndex()+1 == 16))
                                        customerHistorical.setCol16(value);
                                else if((hssfCell.getColumnIndex()+1 == 17))
                                        customerHistorical.setCol17(value);
                                else if((hssfCell.getColumnIndex()+1 == 18))
                                        customerHistorical.setCol18(value);
                                else if((hssfCell.getColumnIndex()+1 == 19))
                                        customerHistorical.setCol19(value);
                                else if((hssfCell.getColumnIndex()+1 == 20))
                                        customerHistorical.setCol20(value);
                                else if((hssfCell.getColumnIndex()+1 == 21))
                                        customerHistorical.setCol21(value);
                                else if((hssfCell.getColumnIndex()+1 == 22))
                                        customerHistorical.setCol22(value);
                                else if((hssfCell.getColumnIndex()+1 == 23))
                                        customerHistorical.setCol23(value);
                                else if((hssfCell.getColumnIndex()+1 == 24))
                                        customerHistorical.setCol24(value);
                                else if((hssfCell.getColumnIndex()+1 == 25))
                                        customerHistorical.setCol25(value);
                                else if((hssfCell.getColumnIndex()+1 == 26))
                                        customerHistorical.setCol26(value);
                                else if((hssfCell.getColumnIndex()+1 == 27))
                                        customerHistorical.setCol27(value);
                                else if((hssfCell.getColumnIndex()+1 == 28))
                                        customerHistorical.setCol28(value);
                                else if((hssfCell.getColumnIndex()+1 == 29))
                                        customerHistorical.setCol29(value);
                                else if((hssfCell.getColumnIndex()+1 == 30))
                                        customerHistorical.setCol30(value);
                                
                                
                            }
                            customerHistoricalList.add(customerHistorical);
                        }
                    } 
            }
            //update customerHistoricalGroup
            customerHistoricalGroup.setNoColumn(noCol-1); 
            String  higlighCol = selectedColumn.toString().substring(1, selectedColumn.toString().length() - 1);
            customerHistoricalGroup.setHighlightColumn(higlighCol);
            customerHistoricalGroup.setUpdateBy(username);
            customerHistoricalGroup.setUpdateDate(now);
            customerHistoricalGroup.setCustomerHistoricalColumnCollection(customerHistoricalColumnList);
            customerHistoricalGroup.setCustomerHistoricalCollection(customerHistoricalList);
            customerService.editCustomerHistoricalGroup(customerHistoricalGroup);
            success = cntRefCustomer;
            notMatch = total - cntRefCustomer;
         }catch (Exception e) {
             
         }
    }
    
    public void preview() {
         try {
            dataList =  new ArrayList();  
            fileInputStream = new FileInputStream(item.getName());   
            
            if(item.getName().contains(".xlsx")) {
                XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);
                XSSFSheet xssfSheet = workBook.getSheetAt(sheetId-1);                  
                Iterator rowIterator = xssfSheet.rowIterator(); 
                stringHeader="";
                XSSFRow xssfRow;
                if(rowIterator.hasNext()){
                    if(checkboxFirstRow) {
                        xssfRow = (XSSFRow) rowIterator.next();
                        for(Integer o : selectedColumn) {
                            XSSFCell xssfCell = xssfRow.getCell(o.intValue()-1, Row.CREATE_NULL_AS_BLANK);
                            stringHeader += xssfCell.toString().format("%-30s", xssfCell.toString());
                        }
                    } else {
                        for(Integer o : selectedColumn) {
                            String temp="Column " +(o.intValue());
                            stringHeader += temp.format("%-30s", temp);
                        }
                    }
                }
                int cnt=1;
                while (rowIterator.hasNext())   {  
                    if(cnt > 100) {
                        break;
                    }
                    cnt++;
                    xssfRow = (XSSFRow) rowIterator.next();
                    String stringCellValue="";
                    for(Integer o : selectedColumn) {
                        XSSFCell xssfCell = xssfRow.getCell(o.intValue()-1, Row.CREATE_NULL_AS_BLANK);
                        if(xssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            if (DateUtil.isCellDateFormatted(xssfCell)) {
                                stringCellValue += dateFormat.format(xssfCell.getDateCellValue());
                            } else {
                                xssfCell.setCellType(Cell.CELL_TYPE_STRING);
                                stringCellValue += xssfCell.getStringCellValue().toString().format("%-30s", xssfCell.getStringCellValue().toString());
                            }
                        } else {
                            xssfCell.setCellType(Cell.CELL_TYPE_STRING);
                            stringCellValue += xssfCell.getStringCellValue().toString().format("%-30s", xssfCell.getStringCellValue().toString());
                        }
                    }
                    if(stringCellValue.trim().length() != 0)
                        dataList.add(stringCellValue);
                } 
            } else {
                fsFileSystem = new POIFSFileSystem(fileInputStream);  
                HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);  
                HSSFSheet hssfSheet = workBook.getSheetAt(sheetId-1);  
                Iterator rowIterator = hssfSheet.rowIterator(); 
                stringHeader="";
                HSSFRow hssfRow;
                if(rowIterator.hasNext()){
                    if(checkboxFirstRow) {
                        hssfRow = (HSSFRow) rowIterator.next();
                        for(Integer o : selectedColumn) {
                            HSSFCell hssfCell = hssfRow.getCell(o.intValue()-1, Row.CREATE_NULL_AS_BLANK);
                            stringHeader += hssfCell.toString().format("%-30s", hssfCell.toString());
                        }
                    } else {
                        for(Integer o : selectedColumn) {
                            String temp="Column " +(o.intValue());
                            stringHeader += temp.format("%-30s", temp);
                        }
                    }
                }
                int cnt=1;
                while (rowIterator.hasNext())   {  
                    if(cnt > 100) {
                        break;
                    }
                    cnt++;
                    hssfRow = (HSSFRow) rowIterator.next();
                    String stringCellValue="";
                    for(Integer o : selectedColumn) {
                        HSSFCell hssfCell = hssfRow.getCell(o.intValue()-1, Row.CREATE_NULL_AS_BLANK);
                        if(hssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            if (DateUtil.isCellDateFormatted(hssfCell)) {
                                stringCellValue += dateFormat.format(hssfCell.getDateCellValue());
                            } else {
                                hssfCell.setCellType(Cell.CELL_TYPE_STRING);
                                stringCellValue += hssfCell.getStringCellValue().toString().format("%-30s", hssfCell.getStringCellValue().toString());
                            }
                        } else {
                            hssfCell.setCellType(Cell.CELL_TYPE_STRING);
                            stringCellValue += hssfCell.getStringCellValue().toString().format("%-30s", hssfCell.getStringCellValue().toString());
                        }
                    }
                    
                    if(stringCellValue.trim().length() != 0)
                        dataList.add(stringCellValue);
                }
            }
            FacesContext.getCurrentInstance().renderResponse();
         }catch (Exception e) {
             
         }
    }
    
    public void readExcelFile(UploadedFile item) {   
        try {   
            fileInputStream =  new FileInputStream(item.getName());
            Map<String, Integer> values = new LinkedHashMap<String, Integer>();
            if(item.getName().contains(".xlsx")) {
                XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);
                Integer sheetAmount = workBook.getNumberOfSheets();
                for(int i=0;i<sheetAmount;i++){
                    XSSFSheet xssfSheet = workBook.getSheetAt(i); 
                    values.put(xssfSheet.getSheetName(), i+1);
                }
            } else { 
                fsFileSystem = new POIFSFileSystem(fileInputStream);  
                HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);
                Integer sheetAmount = workBook.getNumberOfSheets();
                for(int i=0;i<sheetAmount;i++){
                    HSSFSheet hssfSheet = workBook.getSheetAt(i); 
                    values.put(hssfSheet.getSheetName(), i+1);
                }
            }
            sheetList.clear();
            sheetList = values;
            sheetId = 0;
            FacesContext.getCurrentInstance().renderResponse();
        }  
        catch (Exception e)  {  
            //e.printStackTrace();  
        }   
    }  
    
    public void clearSheet() {
        sheetList.clear();
        sheetId = 0;
        columnList.clear();
        columnId = 0;
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public void changeSheet() {
        try {   
            columnList.clear();
            fileInputStream = new FileInputStream(item.getName());   
            Map<String, Integer> values = new LinkedHashMap<String, Integer>();
            int i=1;
            if(item.getName().contains(".xlsx")) {
                XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);
                XSSFSheet xssfSheet = workBook.getSheetAt(sheetId-1); 
                XSSFRow xssfRow = xssfSheet.getRow(0);
                Iterator iterator = xssfRow.cellIterator();  
                while (iterator.hasNext())  
                {  
                    XSSFCell xssfCell = (XSSFCell) iterator.next();  
                    xssfCell.setCellType(Cell.CELL_TYPE_STRING);
                    if(checkboxFirstRow)  {
                        values.put(xssfCell.getStringCellValue().toString(), i++);
                    } else {
                        values.put("Column "+i, i++);
                    }
                }  
            } else { 
                fsFileSystem = new POIFSFileSystem(fileInputStream);  
                HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);
                HSSFSheet hssfSheet = workBook.getSheetAt(sheetId-1); 
                HSSFRow hssfRow = hssfSheet.getRow(0);
                Iterator iterator = hssfRow.cellIterator();  
                while (iterator.hasNext())  
                {  
                    HSSFCell hssfCell = (HSSFCell) iterator.next();  
                    hssfCell.setCellType(Cell.CELL_TYPE_STRING);
                    if(checkboxFirstRow)  {
                        values.put(hssfCell.getStringCellValue().toString(), i++);
                    } else {
                        values.put("Column "+i, i++);
                    }
                }  
            }
            columnList = values;
            columnId = 0;
            FacesContext.getCurrentInstance().renderResponse();
        }  
        catch (Exception e)  {  
            //e.printStackTrace();  
        }  
    }
    
//    public void listener(UploadEvent event) throws Exception{
//        item = event.getUploadItem();
//        readExcelFile(item.getFile().getPath(),item.getFileName());
//    }  
    public void listener(FileUploadEvent event) throws Exception{
        item = event.getUploadedFile();
        readExcelFile(item);
    }  
    
    //Properties----------------------------------------------------------------
    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
    
   //Managed Properties
    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public CustomerHistoricalGroup getCustomerHistoricalGroup() {
        return customerHistoricalGroup;
    }

    public void setCustomerHistoricalGroup(CustomerHistoricalGroup customerHistoricalGroup) {
        this.customerHistoricalGroup = customerHistoricalGroup;
    }

    //List to UI
    public Map<String, Integer> getSheetList() {
        return sheetList;
    }
    
    public Map<String, Integer> getColumnList() {
        return columnList;
    }
    
    public List<String> getDataList() {
        return dataList;
    }
        
    public int getSheetId() {
        return sheetId;
    }

    public void setSheetId(int sheetId) {
        this.sheetId = sheetId;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }
    
    public List<Integer> getSelectedColumn() {
        return selectedColumn;
    }

    public void setSelectedColumn(List<Integer> selectedColumn) {
        this.selectedColumn = selectedColumn;
    }
    
    public boolean getCheckboxFirstRow() {
        return checkboxFirstRow;
    }

    public void setCheckboxFirstRow(boolean checkboxFirstRow) {
        this.checkboxFirstRow = checkboxFirstRow;
    }

    public String getStringHeader() {
        return stringHeader;
    }

    public void setStringHeader(String stringHeader) {
        this.stringHeader = stringHeader;
    }

//    public UploadItem getItem() {
//        return item;
//    }
//
//    public void setItem(UploadItem item) {
//        this.item = item;
//    }
    
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
    
    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }
    
    public Integer getNotMatch() {
        return notMatch;
    }

    public void setNotMatch(Integer notMatch) {
        this.notMatch = notMatch;
    }
    
}
