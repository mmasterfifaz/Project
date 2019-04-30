/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller.report;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.maxelyz.core.controller.report.*;
import static com.maxelyz.social.controller.report.AbstractSocialReportBaseController.EXCEL_TEMPLATE;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

@ManagedBean
@ViewScoped
/**
 *
 * @author Administrator
 */
public class SlaApprovalByCaseTypeReport extends AbstractSocialReportBaseController {
    private static Logger log = Logger.getLogger(SlaApprovalByCaseTypeReport.class);
    private Integer users_id;
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:soslaapprovalbycase:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }   

    @Override
    public String getReportPath() {
        return "/report/casetypeByContactDate.jasper";
    }
    
    
    @Override
    public String getXLSTemplatePath() {
        return "\\socialreport\\slaApprovalByCaseType.xlsx";
    }

    
    
    @Override 
    public Map getParameterMap() { 
        Map parameterMap = new LinkedHashMap();
        parameterMap.put("from", fromDate);
        parameterMap.put("to", toDate);
        parameterMap.put("user_group_id", userGroupId);
        parameterMap.put("user_group_name", groupName);
        parameterMap.put("user_id", userId);
        parameterMap.put("user_name", userName);
                
        return parameterMap;
    }

    @Override
    public String getQuery() {
 
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        
        System.out.println("fromDate=" + fromDate);
        System.out.println("toDate=" + toDate);
        System.out.println("soPriorityName=" + soPriorityName);
        System.out.println("soCaseStatus=" + soCaseStatus);
        System.out.println("sourceTypeID=" + sourceTypeID);
        System.out.println("soMessageId=" + soMessageId);
        System.out.println("userOption=" + userOption);
        System.out.println("soServiceId=" + soServiceId);
        System.out.println("userGroupId=" + userGroupId);
        System.out.println("userId=" + userId);
        System.out.println("accountID=" + accountID);
        System.out.println("soCaseTypeId=" +soCaseTypeId );
        System.out.println("soSubCaseTypeId=" + soSubCaseTypeId);

        sourceTypeName = this.getSoSoruceTypeNameByID(soSubCaseTypeId);
                

        String query = "EXEC [dbo].[calculate_performance_by_user_v2] "
                + " '" + soPriorityName+ "',"
                + " '" + soCaseStatus + "',"
                + " '" + sourceTypeName + "',"
                + " '" + soMessageId + "',"
                + userGroupId + ","
                + soServiceId + ","
                + userId +  ","
                + accountID +  ","
                + soCaseTypeId +  ","
                + soSubCaseTypeId +  ","
                + " '" + sdfs.format(fromDate) + "',"
                + " '" + sdfs.format(toDate) + "'"

                ;
        System.out.println(query);
        log.debug(query);
        return query;
    }

    @Override
    protected void generateXLSFromTemplate() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        //response.setHeader("Content-Disposition", "Attachment;filename=performace_by_user.xlsx");
        OutputStream out = null;
        try {
            //out = response.getOutputStream();
            //InputStream excelStream = context.getExternalContext().getResourceAsStream(this.getXLSTemplatePath());
            
            //System.out.print(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
            //System.out.print(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
            InputStream excelStream = new FileInputStream(JSFUtil.getRealPath()+getXLSTemplatePath());
            //InputStream excelStream = new FileInputStream(getXLSTemplatePath());
            ArrayList resultArrayList = getResultArray();
            Iterator iT = resultArrayList.iterator();
            Object[] resultArray;       
            
            XSSFWorkbook wb = new XSSFWorkbook(excelStream);
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFPrintSetup printSetup;
             
            //Row row;
            //Cell cell;
  
            // Calculate Total month in this query
            String lastGroupRecord = "";
            int countWorkSheet = 0;
            
            wb.cloneSheet(0);
            countWorkSheet++;
            wb.setSheetName(countWorkSheet, "Result");         
            
                int countRow = 0;
                lastGroupRecord= "";
                
                while(iT.hasNext()){
                    
                    resultArray = (Object[])iT.next(); 

                    countRow ++;
                    
                    
                    sheet = wb.getSheetAt(countWorkSheet);
                    printSetup = sheet.getPrintSetup(); 

                    sheet.setAutobreaks(true); 
                    sheet.setFitToPage(true); 
                    printSetup.setFitWidth((short)1);
                    printSetup.setFitHeight((short)0); 


                    printSetup.setPaperSize(XSSFPrintSetup.A4_PAPERSIZE);
                    printSetup.setLandscape(true);
                    Row row = sheet.createRow(10+countRow);
                    CellStyle style ; 
                    //CellStyle style  = sheet.getRow(10).getCell(0).getCellStyle();


                    int styleRow = 0;
                    String fillerString = "";
                    
                    if(resultArray[17] !=null) {
                        if(resultArray[17].toString().equals("user_name")){
                            fillerString = "   ";
                            styleRow= 10;
                        }else if (resultArray[17].toString().equals("user_group")){
                            fillerString = "";
                            styleRow= 9;
                        }else if (resultArray[17].toString().equals("service")){
                            fillerString = "";
                            styleRow= 8;
                        }
                    }

                    
                    Cell cell = row.createCell(0); style = sheet.getRow(styleRow).getCell(0).getCellStyle();
                    cell.setCellValue(fillerString + resultArray[0].toString()); 
                    cell.setCellStyle(style);

                    
                    //cell = row.createCell(1); //begin at 0
                    //cell.setCellValue(resultArray[1].toString());
                    
                    cell = row.createCell(1); cell.setCellValue(Integer.parseInt(resultArray[1].toString())); style = sheet.getRow(styleRow).getCell(1).getCellStyle();  if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(2); cell.setCellValue(Integer.parseInt(resultArray[2].toString())); style = sheet.getRow(styleRow).getCell(2).getCellStyle();  if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(3); cell.setCellValue(Integer.parseInt(resultArray[3].toString())); style = sheet.getRow(styleRow).getCell(3).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(4); cell.setCellValue(Integer.parseInt(resultArray[4].toString())); style = sheet.getRow(styleRow).getCell(4).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(5); cell.setCellValue(Integer.parseInt(resultArray[5].toString())); style = sheet.getRow(styleRow).getCell(5).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(6); cell.setCellValue(Integer.parseInt(resultArray[6].toString())); style = sheet.getRow(styleRow).getCell(6).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(7); cell.setCellValue(Integer.parseInt(resultArray[7].toString())); style = sheet.getRow(styleRow).getCell(7).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(8); cell.setCellValue(Integer.parseInt(resultArray[8].toString())); style = sheet.getRow(styleRow).getCell(8).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(9); cell.setCellValue((resultArray[9].toString())); style = sheet.getRow(styleRow).getCell(9).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(10); cell.setCellValue((resultArray[10].toString())); style = sheet.getRow(styleRow).getCell(10).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(11); cell.setCellValue((resultArray[11].toString())); style = sheet.getRow(styleRow).getCell(11).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(12); cell.setCellValue((resultArray[12].toString())); style = sheet.getRow(styleRow).getCell(12).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(13); cell.setCellValue((resultArray[13].toString())); style = sheet.getRow(styleRow).getCell(13).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(14); cell.setCellValue((resultArray[14].toString())); style = sheet.getRow(styleRow).getCell(14).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(15); cell.setCellValue((resultArray[15].toString())); style = sheet.getRow(styleRow).getCell(15).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
                    cell = row.createCell(16); cell.setCellValue((resultArray[16].toString())); style = sheet.getRow(styleRow).getCell(16).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
                    
                    
                }
                
                
                //sheet.removeRow(sheet.getRow(13));


                
                
            
            /*    
            iT = resultArrayList.iterator();
            int j =0;
            while(iT.hasNext()){
               resultArray = (Object[])iT.next(); 
               for(int i = 0;i<resultArray.length;i++)
                {
                    Row row = sheet.getRow(10+j);
                    if(row == null) break;
                    Cell cell = row.getCell(i); // A
                    if(cell == null) break;
                     
                   if(resultArray[i]!= null){
                       
                   cell.setCellValue(resultArray[i].toString());  
                   System.out.println(resultArray[i].toString());
                   
                   }else{
                       cell.setCellValue("");
                       System.out.println("NULL");
                   }
                   
                }
               j++; 
            }
            */
                

                
                sheet = wb.getSheetAt(1);
                removeRow(sheet, 8);
                removeRow(sheet, 8);
                removeRow(sheet, 8);

                wb.setSheetHidden(0, 0);
                wb.removeSheetAt(0);

       


  
                System.out.println("END1");
                
                
                SimpleDateFormat msdfs = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ssSSS", Locale.US);
            String fileString = msdfs.format(new Date());
            msdfs = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String folderString = msdfs.format(new Date());
            String filename ="performace_by_user-" + fileString ;
            
          

       
            
            //FileOutputStream fileOut = new FileOutputStream("c:\\socialreport\\" + folderString + "\\ " + filename + ".xlsx");

            if(outputFormat.equals(EXCEL_TEMPLATE)){
                
                response.setHeader("Content-Disposition", "Attachment;filename="  +filename + ".xlsx");
                out = response.getOutputStream();
                wb.write(out);
            }
            else if(outputFormat.equals(PDF_TEMPLATE)){
                OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
                connection.connect();
                DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
                Path pathToFile = Paths.get("c:\\socialreport\\" + folderString + "\\ " + filename + ".xlsx");
                Files.createDirectories(pathToFile.getParent());
                Files.createFile(pathToFile);
                response.setHeader("Content-Disposition", "Attachment;filename="  +  filename + ".pdf");
                out = response.getOutputStream();
                FileOutputStream fileOut =  new FileOutputStream(new File("c:\\socialreport\\" + folderString + "\\ " + filename + ".xlsx"));
                wb.write(fileOut);

                fileOut.flush();
                fileOut.close();
                
                File inputFile = new File("c:\\socialreport\\" + folderString + "\\ " + filename + ".xlsx");
                File outputFile = new File("c:\\socialreport\\" + folderString + "\\ " + filename + ".pdf");  
                converter.convert(inputFile, outputFile); 
                
                //inputFile = new File("c:\\socialreport\\Daily_message_by_case.xlsx");
                //outputFile = new File("c:\\socialreport\\Daily_message_by_case.pdf");  
                //converter.convert(inputFile, outputFile); 
                    
                byte[] byteBuffer = new byte[4096];
                DataInputStream in = new DataInputStream(new FileInputStream(outputFile));
                int length   = 0;
                // reads the file's bytes and writes them to the response stream
                while ((in != null) && ((length = in.read(byteBuffer)) != -1))
                {
                    out.write(byteBuffer,0,length);
                }
                out.close();
                in.close();
            }
  
        
            
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch(Exception e) {
                e.printStackTrace();
                }
            }
        }

    }
    
}
