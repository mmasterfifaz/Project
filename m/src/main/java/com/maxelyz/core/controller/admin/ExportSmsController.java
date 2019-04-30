/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.SaleApprovalValue;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
@RequestScoped
@ViewScoped
public class ExportSmsController {
    
    private static Logger log = Logger.getLogger(ExportSmsController.class);
    private Date fromDate, toDate;
    private String approvalStatus;
    private String salesResult;
    private List<PurchaseOrderInstallment> purchaseOrderInstallmentList;
    
    @ManagedProperty(value="#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:exportsalesresult:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        Date today = new Date();
        fromDate = today;
        toDate = today;
        
        approvalStatus=JSFUtil.getBundleValue("approvalApprovedValue");
        salesResult = "Y";
       // this.searchActionListener(null);
    }
        
    private String wrapCol(String value) {
        return "<td style='mso-number-format:\"\\@\"'>"+(value!=null?value:"")+"</td>";
    }

    private void writeCol(Row row, int columnNo, String value, CellStyle style) {
        Cell cell = row.createCell(columnNo); //begin at 0
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void generateXLS() {
         List<PurchaseOrder> purchaseOrders = this.getPurchaseOrderDAO().findPurchaseOrderBySaleApproval(fromDate,toDate);
         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
         SimpleDateFormat sdfNoTime = new SimpleDateFormat("dd/MM/yyyy");
         SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMdd");
         DecimalFormat dec = new DecimalFormat("#,##0.00");
        ////log.error(purchaseOrders.size());
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        //response.setContentType("application/vnd.ms-excel");
        //response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "Attachment;filename=saleresult"+sdfFileName.format(new Date())+".xls");
        PrintWriter out = null;
        try {    
            if (purchaseOrders.size()>0) {

                out = response.getWriter();

                out.println("<html><head><meta http-equiv=\"Content-Type\" content=\"application/vnd.ms-excel;charset=utf-8\"></head><body>");
                out.println("<table border='1'>");

                //column name 
                out.print("<tr>");
                out.print(wrapCol("Reference No."));
                out.print(wrapCol("Sale Date"));
                out.print(wrapCol("Product Code"));
                out.print(wrapCol("Product Plan"));
                out.print(wrapCol("Initial"));
                out.print(wrapCol("Name"));
                out.print(wrapCol("Surname"));
                out.print(wrapCol("DOB"));
                out.print(wrapCol("ID Card"));
                out.print(wrapCol("Gender"));
                out.print(wrapCol("Weight"));
                out.print(wrapCol("Height"));
                out.print(wrapCol("Current Address1"));
                out.print(wrapCol("Current Address2"));
                out.print(wrapCol("Current District"));
                out.print(wrapCol("Current Province"));
                out.print(wrapCol("Current Postal Code"));
                out.print(wrapCol("Home Phone"));
                out.print(wrapCol("Office Phone"));
                out.print(wrapCol("Mobile Phone"));
                out.print(wrapCol("Email"));
                out.print(wrapCol("Nationality"));
                out.print(wrapCol("Occupation"));
                out.print(wrapCol("JobDescription"));
                out.print(wrapCol("Payment Method"));
                out.print(wrapCol("Card Type"));
                out.print(wrapCol("Card Holder Name"));
                out.print(wrapCol("Card Expiry"));
                out.print(wrapCol("Trace No."));


                out.println("</tr>");
                 
                //detail
                for (PurchaseOrder p: purchaseOrders) {
                    List<PurchaseOrderDetail> purchaseOrderDetails = (List<PurchaseOrderDetail>)p.getPurchaseOrderDetailCollection();
                    PurchaseOrderDetail poDetail = purchaseOrderDetails.get(0);
                    List<PurchaseOrderRegister> poRegs = (List<PurchaseOrderRegister>)poDetail.getPurchaseOrderRegisterCollection();
                    PurchaseOrderRegister poReg = poRegs.get(0);
                    out.print("<tr>");
                    out.print(wrapCol(p.getRefNo()));
                    out.print(wrapCol(sdf.format(p.getSaleDate())));
                    //out.print(wrapCol(p.getCustomer().getInitial()));
                    //out.print(wrapCol(p.getCustomer().getName()));
                    //out.print(wrapCol(p.getCustomer().getSurname()));
                    //out.print(wrapCol(sdfNoTime.format(p.getCustomer().getDob())));
                    out.print(wrapCol(poDetail.getProduct().getCode()));
                    out.print(wrapCol(poDetail.getProductPlan().getName()));
                    out.print(wrapCol(poReg.getInitial()));
                    out.print(wrapCol(poReg.getName()));
                    out.print(wrapCol(poReg.getSurname()));
                    out.print(wrapCol(sdfNoTime.format(poReg.getDob())));
                    out.print(wrapCol(poReg.getIdno()));
                    out.print(wrapCol(poReg.getGender()));
                    out.print(wrapCol(dec.format(poReg.getWeight())));
                    out.print(wrapCol(dec.format(poReg.getHeight())));
                    out.print(wrapCol(poReg.getCurrentAddressLine1()));
                    out.print(wrapCol(poReg.getCurrentAddressLine2()));
                    if (poReg.getCurrentDistrict()!=null) {
                        out.print(wrapCol(poReg.getCurrentDistrict().getName()));
                        out.print(wrapCol(poReg.getCurrentDistrict().getProvinceId().getName()));
                    } else {
                       out.print(wrapCol(""));
                       out.print(wrapCol(""));
                    }
                    out.print(wrapCol(poReg.getCurrentPostalCode()));
                    out.print(wrapCol(poReg.getHomePhone()));
                    out.print(wrapCol(poReg.getOfficePhone()));
                    out.print(wrapCol(poReg.getMobilePhone()));
                    out.print(wrapCol(poReg.getEmail()));
                    out.print(wrapCol(poReg.getNationality()));
                    //out.print(wrapCol(poReg.getOccupation()));//######occupation######################
                    out.print(wrapCol(poReg.getJobDescription()));
                    //payment
                    out.print(wrapCol(p.getPaymentMethod()));
                    out.print(wrapCol(p.getCardType()));
                    out.print(wrapCol(p.getCardHolderName().toUpperCase()));
                    out.print(wrapCol(""+p.getCardExpiryMonth()+p.getCardExpiryYear()));
                    out.print(wrapCol(p.getTraceNo()));
                    out.println("</tr>");
                }
                out.println("</table></body></html>");
                out.flush();
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    
    private void generateFalconXLS(boolean approvalStatus) {
       List<PurchaseOrder> purchaseOrders = this.getPurchaseOrderDAO().findPurchaseOrderBySaleApproval(fromDate, toDate, approvalStatus);
         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
         SimpleDateFormat sdfNoTime = new SimpleDateFormat("dd/MM/yyyy");
         SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMdd");
         DecimalFormat dec = new DecimalFormat("#######0");

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setHeader("Content-Disposition", "Attachment;filename=falcon_saleresult"+sdfFileName.format(new Date())+".xls");
        OutputStream out = null;


        try {
            if (purchaseOrders.size()>0) {

                out = response.getOutputStream();

                Workbook wb = new HSSFWorkbook();
                // Create a new font and alter it.
                Font font = wb.createFont();
                font.setFontHeightInPoints((short)8);
                font.setFontName("Arial");
                // Fonts are set into a style so create a new one to use.
                CellStyle style = wb.createCellStyle();
                style.setFont(font);

                Sheet sheet1 = wb.createSheet("Sheet1");
                // Create a row and put some cells in it. Rows are 0 based.
                Row row;
                int columnNo=0;
                int rowNo=0;

                row = sheet1.createRow(rowNo++);
                writeCol(row,columnNo++,"Quatation_No", style); //A
                writeCol(row,columnNo++,"Cust_Salutation", style); //B
                writeCol(row,columnNo++,"Cust_FirstName", style); //C
                writeCol(row,columnNo++,"Cust_LastName", style); //D
                writeCol(row,columnNo++,"Cust_Address", style); //E
                writeCol(row,columnNo++,"Cust_District", style); //F
                writeCol(row,columnNo++,"Cust_Amphur", style); //G
                writeCol(row,columnNo++,"Cust_Province", style); //H
                writeCol(row,columnNo++,"Cust_Zipcode", style); //I
                writeCol(row,columnNo++,"Cust_Telephone1", style); //J
                writeCol(row,columnNo++,"Cust_Telephone2", style); //K
                writeCol(row,columnNo++,"Cust_Telephone3", style); //L
                writeCol(row,columnNo++,"Insured_Salutation", style); //M
                writeCol(row,columnNo++,"Insured_NationalID", style); //N
                writeCol(row,columnNo++,"Insured_FirstName", style); //O
                writeCol(row,columnNo++,"Insured_LastName", style); //P
                writeCol(row,columnNo++,"Insured_Address", style); //Q
                writeCol(row,columnNo++,"Insured_District", style); //R
                writeCol(row,columnNo++,"Insured_Amphur", style); //S
                writeCol(row,columnNo++,"Insured_Province", style); //T
                writeCol(row,columnNo++,"Insured_Zipcode", style); //U
                writeCol(row,columnNo++,"Insured_Telephone1", style); //V
                writeCol(row,columnNo++,"Insured_Telephone2", style); //W
                writeCol(row,columnNo++,"Insured_Telephone3", style); //X
                writeCol(row,columnNo++,"Insured_DOB", style); //Y
                writeCol(row,columnNo++,"Insured_Gender", style); //Z
                writeCol(row,columnNo++,"Insured_Marital_Status", style); //AA
                writeCol(row,columnNo++,"Insured_Occupation", style); //AB
                writeCol(row,columnNo++,"Insured_Occupation_Detail", style); //AC
                writeCol(row,columnNo++,"Benefit1_Salutation", style); //AD
                writeCol(row,columnNo++,"Benefit1_FirstName", style); //AE
                writeCol(row,columnNo++,"Benefit1_LastName", style); //AF
                writeCol(row,columnNo++,"Benefit1_Address", style); //AG
                writeCol(row,columnNo++,"Benefit1_District", style); //AH
                writeCol(row,columnNo++,"Benefit1_Amphur", style); //AI
                writeCol(row,columnNo++,"Benefit1_Province", style); //AJ
                writeCol(row,columnNo++,"Benefit1_Relation", style); //AK
                writeCol(row,columnNo++,"Benefit1_Rate", style); //AL
                writeCol(row,columnNo++,"Benefit2_Salutation", style); //AM
                writeCol(row,columnNo++,"Benefit2_FirstName", style); //AN
                writeCol(row,columnNo++,"Benefit2_LastName", style); //AO
                writeCol(row,columnNo++,"Benefit2_Address", style); //AP
                writeCol(row,columnNo++,"Benefit2_District", style); //AQ
                writeCol(row,columnNo++,"Benefit2_Amphur", style); //AR
                writeCol(row,columnNo++,"Benefit2_Province", style); //AS
                writeCol(row,columnNo++,"Benefit2_Relation", style); //AT
                writeCol(row,columnNo++,"Benefit2_Rate", style); //AU
                writeCol(row,columnNo++,"Policy_EffectiveDate", style); //AV
                writeCol(row,columnNo++,"Policy_Plan", style); //AW
                writeCol(row,columnNo++,"Policy_SumInsured", style); //AX
                writeCol(row,columnNo++,"Policy_NetPremium", style); //AY
                writeCol(row,columnNo++,"Policy_Vat", style); //AZ
                writeCol(row,columnNo++,"Policy_Stamp", style); //BA
                writeCol(row,columnNo++,"Payment_Amount", style); //BB
                writeCol(row,columnNo++,"Payment_Type", style); //BC
                writeCol(row,columnNo++,"Policy_Premium", style); //BD
                writeCol(row,columnNo++,"Paid_Date", style); //BE
                writeCol(row,columnNo++,"Credit_IDCard", style); //BF
                writeCol(row,columnNo++,"Credit_Type", style); //BG
                writeCol(row,columnNo++,"Credit_CardName", style); //BH
                writeCol(row,columnNo++,"CreditCardExpDate", style); //BI
                writeCol(row,columnNo++,"UW_1", style); //BJ
                writeCol(row,columnNo++,"UW_1Description", style); //BK
                writeCol(row,columnNo++,"AgentID", style); //BL
                writeCol(row,columnNo++,"t_tsrName", style); //BM
                writeCol(row,columnNo++,"Remark", style); //BN

                //detail
                for (PurchaseOrder p: purchaseOrders) {
                    List<PurchaseOrderDetail> purchaseOrderDetails = (List<PurchaseOrderDetail>)p.getPurchaseOrderDetailCollection();
                    PurchaseOrderDetail poDetail = purchaseOrderDetails.get(0);
                    List<PurchaseOrderRegister> poRegs = (List<PurchaseOrderRegister>)poDetail.getPurchaseOrderRegisterCollection();
                    PurchaseOrderRegister poReg = poRegs.get(0);
                    List<PurchaseOrderQuestionaire> poQuestionnaires = (List<PurchaseOrderQuestionaire>) poDetail.getPurchaseOrderQuestionaireCollection();
                    PurchaseOrderQuestionaire poQuestionnaire = poQuestionnaires.get(0);
                    String prefixTumbol;
                    String prefixDistrict;
                    try {
                        if (poReg.getCurrentDistrict().getProvinceId().getName().equals("กรุงเทพมหานคร")) {
                            prefixTumbol="แขวง";
                            prefixDistrict="เขต";
                        } else {
                            prefixTumbol="ตำบล";
                            prefixDistrict="อำเภอ";
                        }
                    } catch (Exception e) {
                        prefixTumbol="";
                        prefixDistrict="";
                    }
                    List<ProductPlanDetail> planDetails = (List<ProductPlanDetail>)poDetail.getProductPlan().getProductPlanDetailCollection();
                    int age = 0;
                    ProductPlanDetail planDetail=null;
                    if(poReg.getDob() != null && p.getSaleDate() != null){
//                        age = this.calulateAge(poReg.getDob(), p.getSaleDate());
                        for (ProductPlanDetail pDetail : planDetails) {
                             if (pDetail.getFromVal()<=age && age<=pDetail.getToVal()) {
                                 planDetail = pDetail;
                                 break;
                             }
                        }
                    }
                    
                    //PaymentType
                    String paymentType="";
                    if ("1".equals(poDetail.getProductPlan().getPaymentMode())) {
                        paymentType="Monthly";
                    } else if ("4".equals(poDetail.getProductPlan().getPaymentMode())) {
                        paymentType="Yearly";
                    }
                    //Marital Status
                    String maritalStatus="";
                    if ("single".equals(poReg.getMaritalStatus())) {
                        maritalStatus="โสด";
                    } else if ("married".equals(poReg.getMaritalStatus())) {
                        maritalStatus="สมรส";
                    } else  if ("divorce".equals(poReg.getMaritalStatus())) {
                        maritalStatus="หย่า";
                    } else  if ("widowed".equals(poReg.getMaritalStatus())) {
                        maritalStatus="หม้าย";
                    }

                    String benefitRate1, benefitRate2;
                    if (poReg.getFx11()==null || poReg.getFx11().trim().length()>0) {//if have ชื่อผู้รับผลประโยชน์2
                        benefitRate1 = "50%";
                        benefitRate2 = "50%";
                    } else {
                        benefitRate1 = "100%";
                        benefitRate2 = "";
                    }

                    row = sheet1.createRow(rowNo++);
                    columnNo=0;
                    writeCol(row, columnNo++, p.getRefNo(), style); //A
                    writeCol(row, columnNo++, poReg.getInitial(), style); //B
                    writeCol(row, columnNo++, poReg.getName(), style); //C
                    writeCol(row, columnNo++, poReg.getSurname(), style); //D
                    writeCol(row, columnNo++, poReg.getCurrentAddressLine1(), style); //E
                    writeCol(row, columnNo++, prefixTumbol+poReg.getCurrentAddressLine2(), style); //F

                    if (poReg.getCurrentDistrict()!=null) {
                        writeCol(row, columnNo++, prefixDistrict+poReg.getCurrentDistrict().getName() , style); //G
                        writeCol(row, columnNo++, poReg.getCurrentDistrict().getProvinceId().getName(), style); //H
                    } else {
                        writeCol(row, columnNo++, "", style); //G
                        writeCol(row, columnNo++, "", style); //H
                    }
                    writeCol(row, columnNo++, poReg.getCurrentPostalCode(), style); //I
                    writeCol(row, columnNo++, poReg.getMobilePhone(), style); //J
                    writeCol(row, columnNo++, poReg.getHomePhone(), style); //K
                    writeCol(row, columnNo++, poReg.getOfficePhone(), style); //L
                    writeCol(row, columnNo++, poReg.getInitial(), style); //M
                    writeCol(row, columnNo++, poReg.getIdno(), style); //N
                    writeCol(row, columnNo++, poReg.getName(), style); //O
                    writeCol(row, columnNo++, poReg.getSurname(), style); //P
                    writeCol(row, columnNo++, poReg.getCurrentAddressLine1(), style); //Q
                    writeCol(row, columnNo++, prefixTumbol+poReg.getCurrentAddressLine2(), style); //R

                    if (poReg.getCurrentDistrict()!=null) {
                        writeCol(row, columnNo++, prefixDistrict+poReg.getCurrentDistrict().getName(), style); //S
                        writeCol(row, columnNo++, poReg.getCurrentDistrict().getProvinceId().getName(), style); //T
                    } else {
                        writeCol(row, columnNo++, "", style); //
                        writeCol(row, columnNo++, "", style); //
                    }
                    writeCol(row, columnNo++, poReg.getCurrentPostalCode(), style); //U
                    writeCol(row, columnNo++, poReg.getMobilePhone(), style); //V
                    writeCol(row, columnNo++, poReg.getHomePhone(), style); //W
                    writeCol(row, columnNo++, poReg.getOfficePhone(), style); //X
                    writeCol(row, columnNo++, sdfNoTime.format(poReg.getDob()), style); //Y
                    writeCol(row, columnNo++, poReg.getGender().equals("Male")?"ชาย":"หญิง", style); //Z
                    writeCol(row, columnNo++, maritalStatus, style); //AA
                    if (poReg.getOccupation()!=null) {
                        writeCol(row, columnNo++, "ขั้นอาชีพ "+poReg.getOccupation().getGroupName(), style); //AB
                        writeCol(row, columnNo++, poReg.getOccupation().getOccupationCategory().getName(), style); //AC
                    } else {
                        writeCol(row, columnNo++, "", style); //
                        writeCol(row, columnNo++, "", style); //
                    }
                    writeCol(row, columnNo++, poReg.getFx6(), style); //AD
                    writeCol(row, columnNo++, poReg.getFx7(), style); //AE
                    writeCol(row, columnNo++, poReg.getFx8(), style); //AF
                    writeCol(row, columnNo++, "", style); //AG
                    writeCol(row, columnNo++, "", style); //AH
                    writeCol(row, columnNo++, "", style); //AI
                    writeCol(row, columnNo++, "", style); //AJ
                    writeCol(row, columnNo++, poReg.getFx9(), style); //AK
                    writeCol(row, columnNo++, benefitRate1, style); //AL
                    writeCol(row, columnNo++, poReg.getFx10(), style); //AM
                    writeCol(row, columnNo++, poReg.getFx11(), style); //AN
                    writeCol(row, columnNo++, poReg.getFx12(), style); //AO
                    writeCol(row, columnNo++, "", style); //AP
                    writeCol(row, columnNo++, "", style); //AQ
                    writeCol(row, columnNo++, "", style); //AR
                    writeCol(row, columnNo++, "", style); //AS
                    writeCol(row, columnNo++, poReg.getFx13(), style); //AT
                    writeCol(row, columnNo++, benefitRate2, style); //AU
                    writeCol(row, columnNo++, p.getSaleDate() != null ? sdfNoTime.format(p.getSaleDate()) : "", style); //AV
                    //writeCol(row, columnNo++, "iPlus "+poDetail.getProductPlan().getName(), style); //AW
                    writeCol(row, columnNo++, "i-Care "+poDetail.getProductPlan().getName().substring(0, 1), style); //AW
                    
                    writeCol(row, columnNo++, dec.format(poDetail.getProductPlan().getSumInsured()), style); //AX
                    writeCol(row, columnNo++, "", style); //AY
                    writeCol(row, columnNo++, "", style); //AZ
                    writeCol(row, columnNo++, "", style); //BA
                    
                    
                    
                    if ("1".equals(poDetail.getProductPlan().getPaymentMode())) {
                        writeCol(row, columnNo++, dec.format(planDetail != null ? planDetail.getNetPremium()*2 : 0), style); //BB
                    } else {
                        writeCol(row, columnNo++, dec.format(planDetail != null ? planDetail.getNetPremium() : 0), style); //BB
                    }
                    /*
                    if ("1".equals(poDetail.getPaymentMode())) {
                        writeCol(row, columnNo++, dec.format(planDetail != null ? planDetail.getNetPremium()*2 : 0), style); //BB
                    } else {
                        writeCol(row, columnNo++, dec.format(planDetail != null ? planDetail.getNetPremium() : 0), style); //BB
                    }*/
                    writeCol(row, columnNo++, paymentType, style); //BC

                    if ("1".equals(poDetail.getProductPlan().getPaymentMode())) {
                        writeCol(row, columnNo++, dec.format(planDetail != null ? planDetail.getNetPremium()*12 : 0), style); //BD
                    } else {
                        writeCol(row, columnNo++, dec.format(planDetail != null ? planDetail.getNetPremium() : 0), style); //BD
                    }
                    writeCol(row, columnNo++, p.getSaleDate() != null ? sdfNoTime.format(p.getSaleDate()) : "", style); //BE
                    writeCol(row, columnNo++, p.getCardNo(), style); //BF
                    writeCol(row, columnNo++, p.getCardType().toUpperCase()+" CARD" , style); //BG
                    writeCol(row, columnNo++, p.getCardHolderName().toUpperCase(), style); //BH

                    String cardExpiryMonth;
                    if (p.getCardExpiryMonth()<10) {
                        cardExpiryMonth = "0"+p.getCardExpiryMonth();
                    } else {
                        cardExpiryMonth = ""+p.getCardExpiryMonth();
                    }
                    writeCol(row, columnNo++, cardExpiryMonth+"/"+p.getCardExpiryYear(), style); //BI
                    writeCol(row, columnNo++, poQuestionnaire.getAnswer(), style); //BJ
                    writeCol(row, columnNo++, poQuestionnaire.getDescription(), style); //BK
                    writeCol(row, columnNo++, p.getCreateByUser().getId().toString(), style); //BL
                    writeCol(row, columnNo++, p.getCreateByUser().getName()+" "+p.getCreateByUser().getSurname(), style); //BM
                    //writeCol(row, columnNo++, "", style); //BN  Remark

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

    /**
     * Same as generateFalconXLS()...  
     * 1. net premium @AY
     * 2. append age @BO
     * 3. decimal with comma
     */
    private void generateFalconMailMergeXLS() {

       List<PurchaseOrder> purchaseOrders = this.getPurchaseOrderDAO().findPurchaseOrderBySaleApproval(fromDate,toDate);
         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
         SimpleDateFormat sdfNoTime = new SimpleDateFormat("dd/MM/yyyy");
         SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMdd");
         DecimalFormat dec = new DecimalFormat("##,###,##0");

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setHeader("Content-Disposition", "Attachment;filename=falcon_application.xls");
        OutputStream out = null;


        try {
            if (purchaseOrders.size()>0) {

                out = response.getOutputStream();

                Workbook wb = new HSSFWorkbook();
                // Create a new font and alter it.
                Font font = wb.createFont();
                font.setFontHeightInPoints((short)8);
                font.setFontName("Arial");
                // Fonts are set into a style so create a new one to use.
                CellStyle style = wb.createCellStyle();
                style.setFont(font);

                Sheet sheet1 = wb.createSheet("Sheet1");
                // Create a row and put some cells in it. Rows are 0 based.
                Row row;
                int columnNo=0;
                int rowNo=0;

                row = sheet1.createRow(rowNo++);
                writeCol(row,columnNo++,"Quatation_No", style); //A
                writeCol(row,columnNo++,"Cust_Salutation", style); //B
                writeCol(row,columnNo++,"Cust_FirstName", style); //C
                writeCol(row,columnNo++,"Cust_LastName", style); //D
                writeCol(row,columnNo++,"Cust_Address", style); //E
                writeCol(row,columnNo++,"Cust_District", style); //F
                writeCol(row,columnNo++,"Cust_Amphur", style); //G
                writeCol(row,columnNo++,"Cust_Province", style); //H
                writeCol(row,columnNo++,"Cust_Zipcode", style); //I
                writeCol(row,columnNo++,"Cust_Telephone1", style); //J
                writeCol(row,columnNo++,"Cust_Telephone2", style); //K
                writeCol(row,columnNo++,"Cust_Telephone3", style); //L
                writeCol(row,columnNo++,"Insured_Salutation", style); //M
                writeCol(row,columnNo++,"Insured_NationalID", style); //N
                writeCol(row,columnNo++,"Insured_FirstName", style); //O
                writeCol(row,columnNo++,"Insured_LastName", style); //P
                writeCol(row,columnNo++,"Insured_Address", style); //Q
                writeCol(row,columnNo++,"Insured_District", style); //R
                writeCol(row,columnNo++,"Insured_Amphur", style); //S
                writeCol(row,columnNo++,"Insured_Province", style); //T
                writeCol(row,columnNo++,"Insured_Zipcode", style); //U
                writeCol(row,columnNo++,"Insured_Telephone1", style); //V
                writeCol(row,columnNo++,"Insured_Telephone2", style); //W
                writeCol(row,columnNo++,"Insured_Telephone3", style); //X
                writeCol(row,columnNo++,"Insured_DOB", style); //Y
                writeCol(row,columnNo++,"Insured_Gender", style); //Z
                writeCol(row,columnNo++,"Insured_Marital_Status", style); //AA
                writeCol(row,columnNo++,"Insured_Occupation", style); //AB
                writeCol(row,columnNo++,"Insured_Occupation_Detail", style); //AC
                writeCol(row,columnNo++,"Benefit1_Salutation", style); //AD
                writeCol(row,columnNo++,"Benefit1_FirstName", style); //AE
                writeCol(row,columnNo++,"Benefit1_LastName", style); //AF
                writeCol(row,columnNo++,"Benefit1_Address", style); //AG
                writeCol(row,columnNo++,"Benefit1_District", style); //AH
                writeCol(row,columnNo++,"Benefit1_Amphur", style); //AI
                writeCol(row,columnNo++,"Benefit1_Province", style); //AJ
                writeCol(row,columnNo++,"Benefit1_Relation", style); //AK
                writeCol(row,columnNo++,"Benefit1_Rate", style); //AL
                writeCol(row,columnNo++,"Benefit2_Salutation", style); //AM
                writeCol(row,columnNo++,"Benefit2_FirstName", style); //AN
                writeCol(row,columnNo++,"Benefit2_LastName", style); //AO
                writeCol(row,columnNo++,"Benefit2_Address", style); //AP
                writeCol(row,columnNo++,"Benefit2_District", style); //AQ
                writeCol(row,columnNo++,"Benefit2_Amphur", style); //AR
                writeCol(row,columnNo++,"Benefit2_Province", style); //AS
                writeCol(row,columnNo++,"Benefit2_Relation", style); //AT
                writeCol(row,columnNo++,"Benefit2_Rate", style); //AU
                writeCol(row,columnNo++,"Policy_EffectiveDate", style); //AV
                writeCol(row,columnNo++,"Policy_Plan", style); //AW
                writeCol(row,columnNo++,"Policy_SumInsured", style); //AX
                writeCol(row,columnNo++,"Policy_NetPremium", style); //AY
                writeCol(row,columnNo++,"Policy_Vat", style); //AZ
                writeCol(row,columnNo++,"Policy_Stamp", style); //BA
                writeCol(row,columnNo++,"Payment_Amount", style); //BB
                writeCol(row,columnNo++,"Payment_Type", style); //BC
                writeCol(row,columnNo++,"Policy_Premium", style); //BD
                writeCol(row,columnNo++,"Paid_Date", style); //BE
                writeCol(row,columnNo++,"Credit_IDCard", style); //BF
                writeCol(row,columnNo++,"Credit_Type", style); //BG
                writeCol(row,columnNo++,"Credit_CardName", style); //BH
                writeCol(row,columnNo++,"CreditCardExpDate", style); //BI
                writeCol(row,columnNo++,"UW_1", style); //BJ
                writeCol(row,columnNo++,"UW_1Description", style); //BK
                writeCol(row,columnNo++,"AgentID", style); //BL
                writeCol(row,columnNo++,"t_tsrName", style); //BM
                writeCol(row,columnNo++,"Remark", style); //BN
                writeCol(row,columnNo++,"Age", style); //BO

                //detail
                for (PurchaseOrder p: purchaseOrders) {
                    List<PurchaseOrderDetail> purchaseOrderDetails = (List<PurchaseOrderDetail>)p.getPurchaseOrderDetailCollection();
                    PurchaseOrderDetail poDetail = purchaseOrderDetails.get(0);
                    List<PurchaseOrderRegister> poRegs = (List<PurchaseOrderRegister>)poDetail.getPurchaseOrderRegisterCollection();
                    PurchaseOrderRegister poReg = poRegs.get(0);
                    List<PurchaseOrderQuestionaire> poQuestionnaires = (List<PurchaseOrderQuestionaire>) poDetail.getPurchaseOrderQuestionaireCollection();
                    PurchaseOrderQuestionaire poQuestionnaire = poQuestionnaires.get(0);
                    String prefixTumbol;
                    String prefixDistrict;
                    try {
                    if (poReg.getCurrentDistrict().getProvinceId().getName().equals("กรุงเทพมหานคร")) {
                        prefixTumbol="แขวง";
                        prefixDistrict="เขต";
                    } else {
                        prefixTumbol="ตำบล";
                        prefixDistrict="อำเภอ";
                    }
                    } catch(Exception e) {
                        prefixTumbol="";
                        prefixDistrict="";
                    }
                    List<ProductPlanDetail> planDetails = (List<ProductPlanDetail>)poDetail.getProductPlan().getProductPlanDetailCollection();
                    int age = 0;                    
                    ProductPlanDetail planDetail=null;
                    if(poReg.getDob() != null && p.getSaleDate() != null){
//                        age = this.calulateAge(poReg.getDob(), p.getSaleDate());
                        for (ProductPlanDetail pDetail : planDetails) {
                             if (pDetail.getFromVal()<=age && age<=pDetail.getToVal()) {
                                 planDetail = pDetail;
                                 break;
                             }
                        }
                    }
                    //PaymentType
                    String paymentType="";
                    if ("1".equals(poDetail.getProductPlan().getPaymentMode())) {
                        paymentType="Monthly";
                    } else if ("4".equals(poDetail.getProductPlan().getPaymentMode())) {
                        paymentType="Yearly";
                    }
                    //Marital Status
                    String maritalStatus="";
                    if ("single".equals(poReg.getMaritalStatus())) {
                        maritalStatus="โสด";
                    } else if ("married".equals(poReg.getMaritalStatus())) {
                        maritalStatus="สมรส";
                    } else  if ("divorce".equals(poReg.getMaritalStatus())) {
                        maritalStatus="หย่า";
                    } else  if ("widowed".equals(poReg.getMaritalStatus())) {
                        maritalStatus="หม้าย";
                    }

                    String benefitRate1, benefitRate2;
                    if (poReg.getFx11()==null || poReg.getFx11().trim().length()>0) {//if have ชื่อผู้รับผลประโยชน์2
                        benefitRate1 = "50%";
                        benefitRate2 = "50%";
                    } else {
                        benefitRate1 = "100%";
                        benefitRate2 = "";
                    }

                    /*Calendar poCal = Calendar.getInstance();
                    poCal.setTime(p.getSaleDate());
                    poCal.add(Calendar.DATE, 1);
                    Date effectiveDate = poCal.getTime();*/

                    row = sheet1.createRow(rowNo++);
                    columnNo=0;
                    writeCol(row, columnNo++, p.getRefNo(), style); //A
                    writeCol(row, columnNo++, poReg.getInitial(), style); //B
                    writeCol(row, columnNo++, poReg.getName(), style); //C
                    writeCol(row, columnNo++, poReg.getSurname(), style); //D
                    writeCol(row, columnNo++, poReg.getCurrentAddressLine1(), style); //E
                    writeCol(row, columnNo++, prefixTumbol+poReg.getCurrentAddressLine2(), style); //F

                    if (poReg.getCurrentDistrict()!=null) {
                        writeCol(row, columnNo++, prefixDistrict+poReg.getCurrentDistrict().getName() , style); //G
                        writeCol(row, columnNo++, poReg.getCurrentDistrict().getProvinceId().getName(), style); //H
                    } else {
                        writeCol(row, columnNo++, "", style); //G
                        writeCol(row, columnNo++, "", style); //H
                    }
                    writeCol(row, columnNo++, poReg.getCurrentPostalCode(), style); //I
                    writeCol(row, columnNo++, poReg.getMobilePhone(), style); //J
                    writeCol(row, columnNo++, poReg.getHomePhone(), style); //K
                    writeCol(row, columnNo++, poReg.getOfficePhone(), style); //L
                    writeCol(row, columnNo++, poReg.getInitial(), style); //M
                    writeCol(row, columnNo++, poReg.getIdno(), style); //N
                    writeCol(row, columnNo++, poReg.getName(), style); //O
                    writeCol(row, columnNo++, poReg.getSurname(), style); //P
                    writeCol(row, columnNo++, poReg.getCurrentAddressLine1(), style); //Q
                    writeCol(row, columnNo++, prefixTumbol+poReg.getCurrentAddressLine2(), style); //R

                    if (poReg.getCurrentDistrict()!=null) {
                        writeCol(row, columnNo++, prefixDistrict+poReg.getCurrentDistrict().getName(), style); //S
                        writeCol(row, columnNo++, poReg.getCurrentDistrict().getProvinceId().getName(), style); //T
                    } else {
                        writeCol(row, columnNo++, "", style); //
                        writeCol(row, columnNo++, "", style); //
                    }
                    writeCol(row, columnNo++, poReg.getCurrentPostalCode(), style); //U
                    writeCol(row, columnNo++, poReg.getMobilePhone(), style); //V
                    writeCol(row, columnNo++, poReg.getHomePhone(), style); //W
                    writeCol(row, columnNo++, poReg.getOfficePhone(), style); //X
                    writeCol(row, columnNo++, sdfNoTime.format(poReg.getDob()), style); //Y
                    writeCol(row, columnNo++, poReg.getGender().equals("Male")?"ชาย":"หญิง", style); //Z
                    writeCol(row, columnNo++, maritalStatus, style); //AA
                    if (poReg.getOccupation()!=null) {       
                        writeCol(row, columnNo++, poReg.getOccupation().getOccupationCategory().getName(), style); //AB
                        writeCol(row, columnNo++, poReg.getOccupation().getName(), style); //AC
                    } else {
                        writeCol(row, columnNo++, "", style); //
                        writeCol(row, columnNo++, "", style); //
                    }
                    writeCol(row, columnNo++, poReg.getFx6(), style); //AD
                    writeCol(row, columnNo++, poReg.getFx7(), style); //AE
                    writeCol(row, columnNo++, poReg.getFx8(), style); //AF
                    writeCol(row, columnNo++, "", style); //AG
                    writeCol(row, columnNo++, "", style); //AH
                    writeCol(row, columnNo++, "", style); //AI
                    writeCol(row, columnNo++, "", style); //AJ
                    writeCol(row, columnNo++, poReg.getFx9(), style); //AK
                    writeCol(row, columnNo++, benefitRate1, style); //AL
                    writeCol(row, columnNo++, poReg.getFx10(), style); //AM
                    writeCol(row, columnNo++, poReg.getFx11(), style); //AN
                    writeCol(row, columnNo++, poReg.getFx12(), style); //AO
                    writeCol(row, columnNo++, "", style); //AP
                    writeCol(row, columnNo++, "", style); //AQ
                    writeCol(row, columnNo++, "", style); //AR
                    writeCol(row, columnNo++, "", style); //AS
                    writeCol(row, columnNo++, poReg.getFx13(), style); //AT
                    writeCol(row, columnNo++, benefitRate2, style); //AU
                    writeCol(row, columnNo++, p.getSaleDate() != null ? sdfNoTime.format(p.getSaleDate()) : "", style); //AV
                    writeCol(row, columnNo++, "I-Care "+poDetail.getProductPlan().getName().substring(0, 1), style); //AW
                    writeCol(row, columnNo++, dec.format(poDetail.getProductPlan().getSumInsured()), style); //AX
                    writeCol(row, columnNo++, dec.format(planDetail != null ? planDetail.getNetPremium() : 0), style); //AY
                    writeCol(row, columnNo++, "", style); //AZ
                    writeCol(row, columnNo++, "", style); //BA
                    if ("1".equals(poDetail.getProductPlan().getPaymentMode())) {
                        writeCol(row, columnNo++, dec.format(planDetail != null ? planDetail.getNetPremium()*2 : 0), style); //BB
                    } else {
                        writeCol(row, columnNo++, dec.format(planDetail != null ? planDetail.getNetPremium() : 0), style); //BB
                    }
                    writeCol(row, columnNo++, paymentType, style); //BC

                    if ("1".equals(poDetail.getProductPlan().getPaymentMode())) {
                        writeCol(row, columnNo++, dec.format(planDetail != null ? planDetail.getNetPremium()*12 : 0), style); //BD
                    } else {
                        writeCol(row, columnNo++, dec.format(planDetail != null ? planDetail.getNetPremium() : 0), style); //BD
                    }
                    writeCol(row, columnNo++, p.getSaleDate() != null ? sdfNoTime.format(p.getSaleDate()) : "", style); //BE
                    writeCol(row, columnNo++, p.getCardNo(), style); //BF
                    writeCol(row, columnNo++, p.getCardType().toUpperCase()+" CARD" , style); //BG
                    writeCol(row, columnNo++, p.getCardHolderName(), style); //BH

                    String cardExpiryMonth;
                    if (p.getCardExpiryMonth()<10) {
                        cardExpiryMonth = "0"+p.getCardExpiryMonth();
                    } else {
                        cardExpiryMonth = ""+p.getCardExpiryMonth();
                    }
                    writeCol(row, columnNo++, cardExpiryMonth+"/"+p.getCardExpiryYear(), style); //BI
                    writeCol(row, columnNo++, poQuestionnaire.getAnswer(), style); //BJ
                    writeCol(row, columnNo++, poQuestionnaire.getDescription(), style); //BK
                    writeCol(row, columnNo++, p.getCreateByUser().getId().toString(), style); //BL
                    writeCol(row, columnNo++, p.getCreateByUser().getName()+" "+p.getCreateByUser().getSurname(), style); //BM
                    writeCol(row, columnNo++, "", style); //BN
                    writeCol(row, columnNo++, age+"", style); //BO
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

    public List<PurchaseOrderInstallment> getList() {
        return purchaseOrderInstallmentList;
    }


    public void searchActionListener(ActionEvent event) {       
        purchaseOrderInstallmentList = this.purchaseOrderDAO.findPurchaseOrderByPurchaseDate(fromDate, toDate);
    }

    public void exportListener(ActionEvent event) {
        //generateXLS();
        generateFalconXLS(true);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void exportPendingListener(ActionEvent event) {
        generateFalconXLS(false);
        FacesContext.getCurrentInstance().responseComplete();
    }
    
    public void exportMailMergeListener(ActionEvent event) {
        generateFalconMailMergeXLS();
        FacesContext.getCurrentInstance().responseComplete();
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

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getSalesResult() {
        return salesResult;
    }

    public void setSalesResult(String salesResult) {
        this.salesResult = salesResult;
    }
/*
    public int calulateAge(Date dobDate, Date referenceDate) {
        int age;
        Calendar dob = Calendar.getInstance();
        dob.setTime(dobDate);
        Calendar today = Calendar.getInstance();
        today.setTime(referenceDate);
        int y1 = dob.get(Calendar.YEAR);
        int y2 = today.get(Calendar.YEAR);
        int m1 = dob.get(Calendar.MONTH);
        int m2 = today.get(Calendar.MONTH);
        int d1 = dob.get(Calendar.DATE);
        int d2 = today.get(Calendar.DATE);
        age = y2 - y1;
        if ((m1 > m2) || (m1 == m2 && d1 > d2)) //ถ้าเดือนเกิดมากกว่าเดือนปัจจุบัน หรือวันเกิดมากกว่าวันปัจจุบัน -> ยังไม่ครบปี
        {
            age--;
        }
        return age;
    }*/
    
    //Managed Propterties
    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

  
}
