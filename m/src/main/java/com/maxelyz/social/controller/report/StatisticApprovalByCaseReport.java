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
/**
 *
 * @author Administrator
 */

@ManagedBean
@ViewScoped
public class StatisticApprovalByCaseReport extends AbstractSocialReportBaseController {
    
    private static Logger log = Logger.getLogger(StatisticApprovalByCaseReport.class);
    private Integer users_id;
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:soapprovalbycase:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/casetypeByContactDate.jasper";  //no use
    }
    
    
    @Override
    public String getXLSTemplatePath() {
        return "\\socialreport\\statisticApprovalByCase.xlsx";
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
        /*
        users_id = JSFUtil.getUserSession().getUserGroup().getId();
        String select = "SELECT rpt.contact_date, c.name AS case_type_name, sum(rpt.total) AS total "
                + "FROM rpt_contact_channel AS rpt "
                + "INNER JOIN channel AS d ON rpt.channel_id = d.id "
                + "INNER JOIN case_detail AS a ON a.id = rpt.case_detail_id "
                + "INNER JOIN case_topic AS b ON b.id = a.case_topic_id "
                + "INNER JOIN case_type AS c ON c.id = b.case_type_id "
                + "INNER JOIN users AS u ON u.id = rpt.user_id "
                + "INNER JOIN user_group AS g ON g.id = u.user_group_id "
                + "RIGHT JOIN (SELECT * FROM user_group_location WHERE user_group_id = '" + users_id + "') AS l ON l.location_id = rpt.location_id AND l.service_type_id = rpt.service_type_id ";

        String where = "WHERE rpt.contact_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        if (serviceTypeId!=0) 
            where += "AND rpt.service_type_id = " + serviceTypeId + " ";
        //---------------------
        if (businessUnitId!=0){
            where += "AND l.business_unit_id = " + businessUnitId + " ";
            if (locationId!=0)
                where += "AND rpt.location_id = " + locationId + " ";
        }
        //----------------
        if (userGroupId!=0)
            where += "AND g.id = " + userGroupId + " ";
        if (userId!=0)
            where += "AND u.id = " + userId + " ";

        String groupBy = "group by rpt.contact_date, c.name ";
        String orderBy = "order by rpt.contact_date, c.name";
        
        String query = select+where+groupBy;
        */
        System.out.println("case Status:" + soCaseStatus);
        System.out.println("service ID:" + soServiceId);
        System.out.println("service Name:" +soServiceName);
        System.out.println("PriorityID:" + soPriorityId);
        System.out.println("PriorityName:" + soPriorityName);
        System.out.println("User Group ID:" + userGroupId);
        System.out.println("User ID:" +userId);
        System.out.println("User Name:" +userName);
        System.out.println("From Date:" + sdfs.format(fromDate) );
        System.out.println("To Date:" + sdfs.format(toDate));
                
        
        String query = "EXEC [dbo].[calculate_daily_message_by_case]" 
                + "'" + soPriorityName + "'," 
                + "'" + soCaseStatus + "'," 
                + "" + soServiceId + "," 
                + "" + userGroupId + "," 
                + "" + userId + "," 
                + "'" + sdfs.format(fromDate) + "',"
                + "'" + sdfs.format(toDate) + "'"
                ;
        System.out.println(query);
        log.debug(query); 
        return query;
    }


    
    @Override
    protected void generateXLSFromTemplate() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
//        //response.setHeader("Content-Disposition", "Attachment;filename=Daily_message_by_case.xlsx");
//        OutputStream out = null;
//        try {
//            
//            //InputStream excelStream = context.getExternalContext().getResourceAsStream(this.getXLSTemplatePath());
//            
//            //System.out.print(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
//            //System.out.print(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
//            InputStream excelStream = new FileInputStream(JSFUtil.getRealPath()+getXLSTemplatePath());
//            //InputStream excelStream = new FileInputStream(getXLSTemplatePath());
//            ArrayList resultArrayList = getResultArray();
//            Iterator iT = resultArrayList.iterator();
//            Object[] resultArray;       
//            
//            XSSFWorkbook wb = new XSSFWorkbook(excelStream);
//            XSSFSheet sheet = wb.getSheetAt(0);
//            XSSFPrintSetup printSetup;
//            //Row row;
//            //Cell cell;
//  
//            // Calculate Total month in this query
//            String lastGroupRecord = "";
//            int countWorkSheet = 0;
//            
//       
//            
//            sheet.getRow(5).getCell(1).setCellValue(fromDate);
//            if(soPriorityName == null){
//                soPriorityName = "ALL";
//            }
//            sheet.getRow(5).getCell(6).setCellValue(soPriorityName);
//            if(serviceTypeName == null){
//                serviceTypeName = "ALL";
//            }
//            sheet.getRow(5).getCell(10).setCellValue(serviceTypeName);
//            if(userName == null){
//                userName = "ALL";
//            }
//            sheet.getRow(5).getCell(14).setCellValue(userName);
//            
//            
//            
//            sheet.getRow(6).getCell(1).setCellValue(toDate);
//            if(soCaseStatus == null){
//                soCaseStatus = "ALL";
//            }
//            sheet.getRow(6).getCell(6).setCellValue(soCaseStatus);
//            if(userGroupId == null){
//                userGroupId = 0;
//            }
//            sheet.getRow(6).getCell(10).setCellValue(userGroupId);
//
//            
//            
//            while(iT.hasNext()){
//                resultArray = (Object[])iT.next(); 
//                // Calculate Total month in result set Sheet Name  = 2014-05
//                if(!resultArray[35].toString().equals(lastGroupRecord)){
//                    
//                    lastGroupRecord = resultArray[35].toString();
//                    wb.cloneSheet(0);
//                    countWorkSheet++;
//                    wb.setSheetName(countWorkSheet, lastGroupRecord);
//                }
//               
//                // Calculate Total month in result set S
//                    if(resultArray[36] !=null && Integer.parseInt(resultArray[36].toString()) == 9999){
//                        
//                        
//                        sheet = wb.getSheetAt(countWorkSheet);
//                        printSetup = sheet.getPrintSetup(); 
//                       
//                        sheet.setAutobreaks(true); 
//                        sheet.setFitToPage(true); 
//                        printSetup.setFitWidth((short)1);
//                        printSetup.setFitHeight((short)0); 
//                        
//                        
//                        printSetup.setPaperSize(XSSFPrintSetup.A4_PAPERSIZE);
//                        printSetup.setLandscape(true);
//                        
//                        
//                        Row row = sheet.getRow(8);
//                        Cell cell = row.getCell(0); // A
//                        cell.setCellValue("Total: " + resultArray[32]); 
//                        cell = row.getCell(1); // B
//                        cell.setCellValue("" + resultArray[33]);  
//                    }
//                }
//                int countRow = 0;
//                iT = resultArrayList.iterator();
//                lastGroupRecord= "";
//                while(iT.hasNext()){
//                    
//                    resultArray = (Object[])iT.next(); 
//                    if(!resultArray[35].toString().equals(lastGroupRecord)){
//                    countRow=0;
//                    lastGroupRecord = resultArray[35].toString();
//                    }
//                    countRow ++;
//                    
//                    sheet = wb.getSheet(resultArray[35].toString());
//                    System.out.println(resultArray[35].toString());
//                    Row row = sheet.createRow(13+countRow);
//                    CellStyle style ; 
//                    //CellStyle style  = sheet.getRow(10).getCell(0).getCellStyle();
//
//
//                    int styleRow = 0;
//                    String fillerString = "";
//                    
//                    if(resultArray[34] !=null) {
//                        if(Integer.parseInt(resultArray[34].toString()) == 0){
//                            fillerString = "";
//                        }else{
//                            fillerString = "   ";
//                        }
//                    }
//                    
//                    if(resultArray[36] !=null) {
//                        if(Integer.parseInt(resultArray[36].toString()) == 9999){
//                            styleRow= 12;
//                        } else if(Integer.parseInt(resultArray[36].toString()) == 0){
//                            styleRow= 10;
//                        } else {
//                            styleRow = 11;
//                        }
//                    
//                    }
//                    
//                    Cell cell = row.createCell(0); style = sheet.getRow(styleRow).getCell(0).getCellStyle();
//                    cell.setCellValue(fillerString + resultArray[0].toString()); 
//                    cell.setCellStyle(style);
//
//                    
//                    //cell = row.createCell(1); //begin at 0
//                    //cell.setCellValue(resultArray[1].toString());
//                    
//                    cell = row.createCell(1); cell.setCellValue(Integer.parseInt(resultArray[1].toString())); style = sheet.getRow(styleRow).getCell(1).getCellStyle();  if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(2); cell.setCellValue(Integer.parseInt(resultArray[2].toString())); style = sheet.getRow(styleRow).getCell(2).getCellStyle();  if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(3); cell.setCellValue(Integer.parseInt(resultArray[3].toString())); style = sheet.getRow(styleRow).getCell(3).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(4); cell.setCellValue(Integer.parseInt(resultArray[4].toString())); style = sheet.getRow(styleRow).getCell(4).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(5); cell.setCellValue(Integer.parseInt(resultArray[5].toString())); style = sheet.getRow(styleRow).getCell(5).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(6); cell.setCellValue(Integer.parseInt(resultArray[6].toString())); style = sheet.getRow(styleRow).getCell(6).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(7); cell.setCellValue(Integer.parseInt(resultArray[7].toString())); style = sheet.getRow(styleRow).getCell(7).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(8); cell.setCellValue(Integer.parseInt(resultArray[8].toString())); style = sheet.getRow(styleRow).getCell(8).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(9); cell.setCellValue(Integer.parseInt(resultArray[9].toString())); style = sheet.getRow(styleRow).getCell(9).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(10); cell.setCellValue(Integer.parseInt(resultArray[10].toString())); style = sheet.getRow(styleRow).getCell(10).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(11); cell.setCellValue(Integer.parseInt(resultArray[11].toString())); style = sheet.getRow(styleRow).getCell(11).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(12); cell.setCellValue(Integer.parseInt(resultArray[12].toString())); style = sheet.getRow(styleRow).getCell(12).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(13); cell.setCellValue(Integer.parseInt(resultArray[13].toString())); style = sheet.getRow(styleRow).getCell(13).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(14); cell.setCellValue(Integer.parseInt(resultArray[14].toString())); style = sheet.getRow(styleRow).getCell(14).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(15); cell.setCellValue(Integer.parseInt(resultArray[15].toString())); style = sheet.getRow(styleRow).getCell(15).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(16); cell.setCellValue(Integer.parseInt(resultArray[16].toString())); style = sheet.getRow(styleRow).getCell(16).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(17); cell.setCellValue(Integer.parseInt(resultArray[17].toString())); style = sheet.getRow(styleRow).getCell(17).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(18); cell.setCellValue(Integer.parseInt(resultArray[18].toString())); style = sheet.getRow(styleRow).getCell(18).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(19); cell.setCellValue(Integer.parseInt(resultArray[19].toString())); style = sheet.getRow(styleRow).getCell(19).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(20); cell.setCellValue(Integer.parseInt(resultArray[20].toString())); style = sheet.getRow(styleRow).getCell(20).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(21); cell.setCellValue(Integer.parseInt(resultArray[21].toString())); style = sheet.getRow(styleRow).getCell(21).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(22); cell.setCellValue(Integer.parseInt(resultArray[22].toString())); style = sheet.getRow(styleRow).getCell(22).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(23); cell.setCellValue(Integer.parseInt(resultArray[23].toString())); style = sheet.getRow(styleRow).getCell(23).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(24); cell.setCellValue(Integer.parseInt(resultArray[24].toString())); style = sheet.getRow(styleRow).getCell(24).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(25); cell.setCellValue(Integer.parseInt(resultArray[25].toString())); style = sheet.getRow(styleRow).getCell(25).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(26); cell.setCellValue(Integer.parseInt(resultArray[26].toString())); style = sheet.getRow(styleRow).getCell(26).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(27); cell.setCellValue(Integer.parseInt(resultArray[27].toString())); style = sheet.getRow(styleRow).getCell(27).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(28); cell.setCellValue(Integer.parseInt(resultArray[28].toString())); style = sheet.getRow(styleRow).getCell(28).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(29); cell.setCellValue(Integer.parseInt(resultArray[29].toString())); style = sheet.getRow(styleRow).getCell(29).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(30); cell.setCellValue(Integer.parseInt(resultArray[30].toString())); style = sheet.getRow(styleRow).getCell(30).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(31); cell.setCellValue(Integer.parseInt(resultArray[31].toString())); style = sheet.getRow(styleRow).getCell(31).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    cell = row.createCell(32); cell.setCellValue(Integer.parseInt(resultArray[32].toString())); style = sheet.getRow(styleRow).getCell(32).getCellStyle(); if(style!=null)  cell.setCellStyle(style);
//                    //cell = row.createCell(33); cell.setCellValue(resultArray[35].toString()); 
//                    
//                }
//                
//                
//                //sheet.removeRow(sheet.getRow(13));
//
//
//                
//                
//            
//            /*    
//            iT = resultArrayList.iterator();
//            int j =0;
//            while(iT.hasNext()){
//               resultArray = (Object[])iT.next(); 
//               for(int i = 0;i<resultArray.length;i++)
//                {
//                    Row row = sheet.getRow(10+j);
//                    if(row == null) break;
//                    Cell cell = row.getCell(i); // A
//                    if(cell == null) break;
//                     
//                   if(resultArray[i]!= null){
//                       
//                   cell.setCellValue(resultArray[i].toString());  
//                   System.out.println(resultArray[i].toString());
//                   
//                   }else{
//                       cell.setCellValue("");
//                       System.out.println("NULL");
//                   }
//                   
//                }
//               j++; 
//            }
//            */
//            if(resultArrayList.size()>0)
//            {
//            wb.setSheetHidden(0, 0);
//            wb.removeSheetAt(0);
//            }
//            for(int i = 0; i< wb.getNumberOfSheets();i++){
//                sheet = wb.getSheetAt(i);       
//                
//                
//                sheet.createRow(sheet.getLastRowNum()+1);
//                Row row = sheet.getRow(sheet.getLastRowNum());
//                
//                
//                row.createCell(0).setCellValue(sheet.getRow(13).getCell(0).getStringCellValue());
//                row.createCell(28).setCellValue(sheet.getRow(13).getCell(28).getStringCellValue() + " " + (i+1)+ "/" + wb.getNumberOfSheets() );
//                //sheet.removeRow(sheet.getRow(10));
//                //sheet.removeRow(sheet.getRow(11));
//                //sheet.removeRow(sheet.getRow(12));
//                //sheet.removeRow(sheet.getRow(13));
//                
//                //CellStyle hiddenstyle = wb.createCellStyle();
//                //hiddenstyle.setHidden(true);
//                //sheet.getRow(10).setHeight((short) 0);
//                //sheet.getRow(11).setHeight((short) 0);
//                //sheet.getRow(12).setHeight((short) 0);
//                //sheet.getRow(13).setHeight((short) 0);
//                
//                removeRow(sheet, 10);
//                removeRow(sheet, 10);
//                removeRow(sheet, 10);
//                removeRow(sheet, 10);
//                //sheet.shiftRows(10, 13, -4);
// 
//                System.out.println("END");
//                 
//            }
//        
//            
//
//            SimpleDateFormat msdfs = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ssSSS", Locale.US);
//            String fileString = msdfs.format(new Date());
//            msdfs = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//            String folderString = msdfs.format(new Date());
//            String filename ="Daily_message_by_case-" + fileString ;
//            
//          
//
//       
//            
//            //FileOutputStream fileOut = new FileOutputStream("c:\\socialreport\\" + folderString + "\\ " + filename + ".xlsx");
//
//            if(outputFormat.equals(EXCEL_TEMPLATE)){
//                
//                response.setHeader("Content-Disposition", "Attachment;filename="  +filename + ".xlsx");
//                out = response.getOutputStream();
//                wb.write(out);
//            }
//            else if(outputFormat.equals(PDF_TEMPLATE)){
//                OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
//                connection.connect();
//                DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
//                Path pathToFile = Paths.get("c:\\socialreport\\" + folderString + "\\ " + filename + ".xlsx");
//                Files.createDirectories(pathToFile.getParent());
//                Files.createFile(pathToFile);
//                response.setHeader("Content-Disposition", "Attachment;filename="  +  filename + ".pdf");
//                out = response.getOutputStream();
//                FileOutputStream fileOut =  new FileOutputStream(new File("c:\\socialreport\\" + folderString + "\\ " + filename + ".xlsx"));
//                wb.write(fileOut);
//
//                fileOut.flush();
//                fileOut.close();
//                
//                File inputFile = new File("c:\\socialreport\\" + folderString + "\\ " + filename + ".xlsx");
//                File outputFile = new File("c:\\socialreport\\" + folderString + "\\ " + filename + ".pdf");  
//                converter.convert(inputFile, outputFile); 
//                
//                //inputFile = new File("c:\\socialreport\\Daily_message_by_case.xlsx");
//                //outputFile = new File("c:\\socialreport\\Daily_message_by_case.pdf");  
//                //converter.convert(inputFile, outputFile); 
//                    
//                byte[] byteBuffer = new byte[4096];
//                DataInputStream in = new DataInputStream(new FileInputStream(outputFile));
//                int length   = 0;
//                // reads the file's bytes and writes them to the response stream
//                while ((in != null) && ((length = in.read(byteBuffer)) != -1))
//                {
//                    out.write(byteBuffer,0,length);
//                }
//                out.close();
//                in.close();
//            }
//            
//           //File inputFile = new File(filename);
//
//            
//            
//            
//
//            
//        } catch (Exception e) {
//            log.error(e);
//            e.printStackTrace();
//            
//        } finally {
//            if (out != null) {
//                try {
//                    out.close();
//
//            
//                } catch(Exception e) {
//                e.printStackTrace();
//                }
//            }
//        }

    }
    
}
