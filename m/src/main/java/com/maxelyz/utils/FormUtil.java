/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.utils;

import com.maxelyz.core.common.log.RegistrationLogging;
import com.maxelyz.core.controller.front.customerHandling.RegistrationPoController;
import com.maxelyz.core.model.entity.ApprovalRuleDetail;
import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.District;
import com.maxelyz.core.model.entity.Occupation;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.PurchaseOrderDetail;
import com.maxelyz.core.model.entity.PurchaseOrderRegister;
import com.maxelyz.core.model.entity.PurchaseOrderChildRegister;
import com.maxelyz.core.model.entity.PurchaseOrderDeclaration;
import com.maxelyz.core.model.entity.QaTransDetail;
import com.maxelyz.core.model.entity.Subdistrict;
import com.maxelyz.core.model.value.front.customerHandling.FlexFieldInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.RegistrationInfoValue;
import com.maxelyz.core.service.mng.ContextResourceFinder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author DevTeam
 */
public class FormUtil {
    
    public static boolean isNotExistDistrict(District d){
        return d==null  || d.getId()== null || d.getId()==0;
    }    
    
    public static void setNoUseDistrict(PurchaseOrderRegister poReg) {
        if ( FormUtil.isNotExistDistrict(poReg.getCurrentDistrict())) poReg.setCurrentDistrict(null);
        if ( FormUtil.isNotExistDistrict(poReg.getHomeDistrict())) poReg.setHomeDistrict(null);
        if ( FormUtil.isNotExistDistrict(poReg.getBillingDistrict())) poReg.setBillingDistrict(null);
        if ( FormUtil.isNotExistDistrict(poReg.getShippingDistrict())) poReg.setShippingDistrict(null);
    }
    
    public static void recoverDistrict(PurchaseOrderRegister poReg) {
        if (poReg.getCurrentDistrict()== null) {
            poReg.setCurrentDistrict(new District());
        }
        if (poReg.getHomeDistrict()== null) {
            poReg.setHomeDistrict(new District());
        }
        if (poReg.getBillingDistrict() == null) {
            poReg.setBillingDistrict(new District());
        }
        if (poReg.getShippingDistrict() == null) {
            poReg.setShippingDistrict(new District());
        }
    } 
    
    public static String buildThaiIdNo(RegistrationInfoValue form) {
        if (form != null) {
            StringBuilder idno = new StringBuilder(13);
            if (form.getId1() != null) {
                idno.append(form.getId1());
            }
            if (form.getId2() != null) {
                idno.append(form.getId2());
            }
            if (form.getId3() != null) {
                idno.append(form.getId3());
            }
            if (form.getId4() != null) {
                idno.append(form.getId4());
            }
            if (form.getId5() != null) {
                idno.append(form.getId5());
            }
            return idno.toString();
        }
        return null;
    }

    public static String buildCreditCardNo(String cn1, String cn2, String cn3, String cn4) {
        StringBuilder cardno = new StringBuilder(20);
        if (cn1 != null) {
            cardno.append(cn1);
        }
        if (cn2 != null) {
            cardno.append(cn2);
        }
        if (cn3 != null) {
            cardno.append(cn3);
        }
        if (cn4 != null) {
            cardno.append(cn4);
        }
        return cardno.toString();
    }

    public static boolean isRegProductRefNoGen(Product p) {
        return p != null && p.getRefNoGenerateEvent() != null && "reg".equalsIgnoreCase(p.getRefNoGenerateEvent());
    }
    
    public static boolean isNotExistsIntegerId(Integer i){
        return  ( i==null || i.intValue()==0);
    }

    public static String nullWhenEmpty(String s){
        return (s==null || "".equals(s) )?null:s;
    }

    public static String emptyWhenNull(String s){
        return (s==null)?"":s;
    }
    
    public static String getFullName(String name, String surname){
        StringBuilder sb = new StringBuilder();
        sb.append(FormUtil.emptyWhenNull(name))
          .append(" ")
          .append(FormUtil.emptyWhenNull(surname));
        return sb.toString().trim();
    }
        
    public static void copyFlexFieldValue2Por(PurchaseOrderRegister por, List<FlexFieldInfoValue> fxInfo) {
        if (fxInfo != null && por != null) {
            for (FlexFieldInfoValue fx : fxInfo) {
                int no = fx.getNo();
                switch (no) {
                    case 1:
                        por.setFx1(fx.getPoFlexField());
                        break;
                    case 2:
                        por.setFx2(fx.getPoFlexField());
                        break;
                    case 3:
                        por.setFx3(fx.getPoFlexField());
                        break;
                    case 4:
                        por.setFx4(fx.getPoFlexField());
                        break;
                    case 5:
                        por.setFx5(fx.getPoFlexField());
                        break;
                    case 6:
                        por.setFx6(fx.getPoFlexField());
                        break;
                    case 7:
                        por.setFx7(fx.getPoFlexField());
                        break;
                    case 8:
                        por.setFx8(fx.getPoFlexField());
                        break;
                    case 9:
                        por.setFx9(fx.getPoFlexField());
                        break;
                    case 10:
                        por.setFx10(fx.getPoFlexField());
                        break;
                    case 11:
                        por.setFx11(fx.getPoFlexField());
                        break;
                    case 12:
                        por.setFx12(fx.getPoFlexField());
                        break;
                    case 13:
                        por.setFx13(fx.getPoFlexField());
                        break;
                    case 14:
                        por.setFx14(fx.getPoFlexField());
                        break;
                    case 15:
                        por.setFx15(fx.getPoFlexField());
                        break;
                    case 16:
                        por.setFx16(fx.getPoFlexField());
                        break;
                    case 17:
                        por.setFx17(fx.getPoFlexField());
                        break;
                    case 18:
                        por.setFx18(fx.getPoFlexField());
                        break;
                    case 19:
                        por.setFx19(fx.getPoFlexField());
                        break;
                    case 20:
                        por.setFx20(fx.getPoFlexField());
                        break;
                    case 21:
                        por.setFx21(fx.getPoFlexField());
                        break;
                    case 22:
                        por.setFx22(fx.getPoFlexField());
                        break;
                    case 23:
                        por.setFx23(fx.getPoFlexField());
                        break;
                    case 24:
                        por.setFx24(fx.getPoFlexField());
                        break;
                    case 25:
                        por.setFx25(fx.getPoFlexField());
                        break;
                    case 26:
                        por.setFx26(fx.getPoFlexField());
                        break;
                    case 27:
                        por.setFx27(fx.getPoFlexField());
                        break;
                    case 28:
                        por.setFx28(fx.getPoFlexField());
                        break;
                    case 29:
                        por.setFx29(fx.getPoFlexField());
                        break;
                    case 30:
                        por.setFx30(fx.getPoFlexField());
                        break;
                    case 31:
                        por.setFx31(fx.getPoFlexField());
                        break;
                    case 32:
                        por.setFx32(fx.getPoFlexField());
                        break;
                    case 33:
                        por.setFx33(fx.getPoFlexField());
                        break;
                    case 34:
                        por.setFx34(fx.getPoFlexField());
                        break;
                    case 35:
                        por.setFx35(fx.getPoFlexField());
                        break;
                    case 36:
                        por.setFx36(fx.getPoFlexField());
                        break;
                    case 37:
                        por.setFx37(fx.getPoFlexField());
                        break;
                    case 38:
                        por.setFx38(fx.getPoFlexField());
                        break;
                    case 39:
                        por.setFx39(fx.getPoFlexField());
                        break;
                    case 40:
                        por.setFx40(fx.getPoFlexField());
                        break;
                    case 41:
                        por.setFx41(fx.getPoFlexField());
                        break;
                    case 42:
                        por.setFx42(fx.getPoFlexField());
                        break;
                    case 43:
                        por.setFx43(fx.getPoFlexField());
                        break;
                    case 44:
                        por.setFx44(fx.getPoFlexField());
                        break;
                    case 45:
                        por.setFx45(fx.getPoFlexField());
                        break;
                    case 46:
                        por.setFx46(fx.getPoFlexField());
                        break;
                    case 47:
                        por.setFx47(fx.getPoFlexField());
                        break;
                    case 48:
                        por.setFx48(fx.getPoFlexField());
                        break;
                    case 49:
                        por.setFx49(fx.getPoFlexField());
                        break;
                    case 50:
                        por.setFx50(fx.getPoFlexField());
                        break;
                }
            }
        }
    }
    
    public static void copyFlexFieldValue2PoChildRegister(PurchaseOrderChildRegister poChildRegister, List<FlexFieldInfoValue> fxInfo) {
        if (fxInfo != null && poChildRegister != null) {
            for (FlexFieldInfoValue fx : fxInfo) {
                int no = fx.getNo();
                switch (no) {
                    case 1:
                        poChildRegister.setFx1(fx.getPoFlexField());
                        break;
                    case 2:
                        poChildRegister.setFx2(fx.getPoFlexField());
                        break;
                    case 3:
                        poChildRegister.setFx3(fx.getPoFlexField());
                        break;
                    case 4:
                        poChildRegister.setFx4(fx.getPoFlexField());
                        break;
                    case 5:
                        poChildRegister.setFx5(fx.getPoFlexField());
                        break;
                    case 6:
                        poChildRegister.setFx6(fx.getPoFlexField());
                        break;
                    case 7:
                        poChildRegister.setFx7(fx.getPoFlexField());
                        break;
                    case 8:
                        poChildRegister.setFx8(fx.getPoFlexField());
                        break;
                    case 9:
                        poChildRegister.setFx9(fx.getPoFlexField());
                        break;
                    case 10:
                        poChildRegister.setFx10(fx.getPoFlexField());
                        break;
                    case 11:
                        poChildRegister.setFx11(fx.getPoFlexField());
                        break;
                    case 12:
                        poChildRegister.setFx12(fx.getPoFlexField());
                        break;
                    case 13:
                        poChildRegister.setFx13(fx.getPoFlexField());
                        break;
                    case 14:
                        poChildRegister.setFx14(fx.getPoFlexField());
                        break;
                    case 15:
                        poChildRegister.setFx15(fx.getPoFlexField());
                        break;
                    case 16:
                        poChildRegister.setFx16(fx.getPoFlexField());
                        break;
                    case 17:
                        poChildRegister.setFx17(fx.getPoFlexField());
                        break;
                    case 18:
                        poChildRegister.setFx18(fx.getPoFlexField());
                        break;
                    case 19:
                        poChildRegister.setFx19(fx.getPoFlexField());
                        break;
                    case 20:
                        poChildRegister.setFx20(fx.getPoFlexField());
                        break;
                }
            }
        }
    }
    
    public static void copyFlexFieldValue2Pod(PurchaseOrderDeclaration pod, List<FlexFieldInfoValue> fxInfo) {
        if (fxInfo != null && pod != null) {
            for (FlexFieldInfoValue fx : fxInfo) {
                int no = fx.getNo();
                switch (no) {
                    case 1:
                        pod.setFx1(fx.getPoFlexField());
                        break;
                    case 2:
                        pod.setFx2(fx.getPoFlexField());
                        break;
                    case 3:
                        pod.setFx3(fx.getPoFlexField());
                        break;
                    case 4:
                        pod.setFx4(fx.getPoFlexField());
                        break;
                    case 5:
                        pod.setFx5(fx.getPoFlexField());
                        break;
                    case 6:
                        pod.setFx6(fx.getPoFlexField());
                        break;
                    case 7:
                        pod.setFx7(fx.getPoFlexField());
                        break;
                    case 8:
                        pod.setFx8(fx.getPoFlexField());
                        break;
                    case 9:
                        pod.setFx9(fx.getPoFlexField());
                        break;
                    case 10:
                        pod.setFx10(fx.getPoFlexField());
                        break;
                    case 11:
                        pod.setFx11(fx.getPoFlexField());
                        break;
                    case 12:
                        pod.setFx12(fx.getPoFlexField());
                        break;
                    case 13:
                        pod.setFx13(fx.getPoFlexField());
                        break;
                    case 14:
                        pod.setFx14(fx.getPoFlexField());
                        break;
                    case 15:
                        pod.setFx15(fx.getPoFlexField());
                        break;
                    case 16:
                        pod.setFx16(fx.getPoFlexField());
                        break;
                    case 17:
                        pod.setFx17(fx.getPoFlexField());
                        break;
                    case 18:
                        pod.setFx18(fx.getPoFlexField());
                        break;
                    case 19:
                        pod.setFx19(fx.getPoFlexField());
                        break;
                    case 20:
                        pod.setFx20(fx.getPoFlexField());
                        break;
                }
            }
        }
    }

    public static Double computeBmi(Double w, Double h) {
        Double bmi = null;
        if (w != null && h != null && h > 0) {
            h = h / 100;
            bmi = w / (h * h);
            bmi = Math.round(bmi * 10) / 10.0;

        } else {
            bmi = null;
        }
        return bmi;
    }
    
    public static boolean checkQuestion(List<QaTransDetail> qaTransDetailList, Integer no, String operator, String value){
        Boolean b = false;
        for(QaTransDetail q : qaTransDetailList){
            if(q.getSeqNo() == no.intValue()){
                String[] qaAnswer;
                qaAnswer = q.getAnswer().split(",");
                for(int i=0; i<qaAnswer.length; i++){
                    b = ContextResourceFinder.getApprovalRuleDAO().checkParam(Integer.parseInt(qaAnswer[i]), operator, value);
                }
                break;
            }
        }
        return b;
    } 
        
    public static String autoExecuteRejectApprovalRules(Integer productApprovalRuleId,PurchaseOrder po, List<QaTransDetail> approvalQuestionList) throws Exception{
        return autoExecuteApprovalRules("reject", productApprovalRuleId, po,  approvalQuestionList) ;
    }   
    
    public static String autoExecuteUwApprovalRules(Integer productApprovalRuleId,PurchaseOrder po, List<QaTransDetail> approvalQuestionList) throws Exception{
        return autoExecuteApprovalRules("uw", productApprovalRuleId, po,  approvalQuestionList) ;
    }        
    
    public static PurchaseOrderRegister findPurchaseOrderRegisterOfApplicant(List<PurchaseOrderRegister> porList  ){
        if (porList!=null){
            for(PurchaseOrderRegister por : porList){
                if ( por.getNo()==1 ) return por;
            }
        }
        return null;
    }    
    
    public static List<PurchaseOrderRegister> getPurchaseOrderRegisterListOfFirstPod(PurchaseOrder po){
        List<PurchaseOrderRegister> result = null;
        if (po!=null && po.getPurchaseOrderCollection()!=null){
            if ( !po.getPurchaseOrderDetailCollection().isEmpty() ){
                PurchaseOrderDetail  pod = ((ArrayList<PurchaseOrderDetail>)po.getPurchaseOrderDetailCollection()).get(0);
                result = (ArrayList<PurchaseOrderRegister>)pod.getPurchaseOrderRegisterCollection();
            }
        }
        return result;
    }
    
    public static void setupPayerIsOtherPerson(PurchaseOrder po,Integer payerOccupationId, Integer payerSubDistrictId)throws Exception{
        po.setPayerOccupation( FormUtil.isNotExistsIntegerId(payerOccupationId)? null : new Occupation(payerOccupationId) );
        if( FormUtil.isNotExistsIntegerId(payerSubDistrictId) );
        else{
            Subdistrict subd = null;
            try{
                subd = ContextResourceFinder.getRegistrationMasterDataService().findShortenSubDistrictById(payerSubDistrictId);
            }catch(Exception e){
                RegistrationLogging.logError("Purchase order was configured the payer is other person but program cannot find payer subdistrict [purchase order id ="+po.getId()+", customer id="+po.getCustomer()+", payerSubDistrictId="+payerSubDistrictId+" ]",e);
            }
            if (subd!=null){
                po.setPayerSubDistrict(subd);
                po.setPayerDistrict(subd.getDistrict());
                po.setPayerPostalCode(subd.getPostalCode());
            }
            
        }
    }
    
    public static void setupPayerIsApplicant(PurchaseOrder po,List<PurchaseOrderRegister> porList ) throws Exception{
        //Step1 : Find Main Insured ( main insured which is por number 1 )
        PurchaseOrderRegister mainInsuredPor = FormUtil.findPurchaseOrderRegisterOfApplicant(porList);
        
        //Step2 : Setup value
        if (mainInsuredPor==null){
            RegistrationLogging.logInfo("Purchase order was configured the payer is applicant but the applicant is null [purchase order id ="+po.getId()+", customer id="+po.getCustomer()+", purchase order register list="+porList+"]");
            FormUtil.setupEmptyPayer(po);            
        }else{
            po.setPayerInitial(mainInsuredPor.getInitial()); //1
            po.setPayerName(mainInsuredPor.getName()); //2
            po.setPayerSurname(mainInsuredPor.getSurname()); //3
            po.setPayerIdtype( FormUtil.isNotExistsIntegerId(mainInsuredPor.getIdcardTypeId())?null:mainInsuredPor.getIdcardTypeId().toString() ); //4
            po.setPayerIdcard(mainInsuredPor.getIdno()); //5
            po.setPayerGender(mainInsuredPor.getGender()); //6
            po.setPayerDob(mainInsuredPor.getDob()); //7
            po.setPayerAddress1(mainInsuredPor.getCurrentAddressLine1()); //8
            po.setPayerAddress2(mainInsuredPor.getCurrentAddressLine2()); //9
            po.setPayerAddress3(mainInsuredPor.getCurrentAddressLine3()); //10
            po.setPayerSubDistrict(mainInsuredPor.getCurrentSubDistrict()); //11
            po.setPayerDistrict(mainInsuredPor.getCurrentDistrict()); //12
            po.setPayerPostalCode(mainInsuredPor.getCurrentPostalCode()); //13
            po.setPayerMaritalStatus(mainInsuredPor.getMaritalStatus()); //14
            po.setPayerHomePhone(mainInsuredPor.getHomePhone()); //15
            po.setPayerMobilePhone(mainInsuredPor.getMobilePhone()); //16
            po.setPayerOfficePhone(mainInsuredPor.getOfficePhone()); //17
            po.setPayerOccupation(mainInsuredPor.getOccupation()); //18
            po.setPayerOccupationOther(mainInsuredPor.getPosition()); //19
            po.setPayerEmail(mainInsuredPor.getEmail()); //20
        }
    }
    
    public static void setupPayerIsMainInsured(PurchaseOrder po,Product product) throws Exception {
        PurchaseOrder parentPo = po.getParentPurchaseOrder();
        if(product!=null && product.getFamilyProduct() && parentPo != null ){
            po.setPayerInitial(parentPo.getPayerInitial()); //1
            po.setPayerName(parentPo.getPayerName()); //2
            po.setPayerSurname(parentPo.getPayerSurname()); //3
            po.setPayerIdtype(parentPo.getPayerIdtype()); //4
            po.setPayerIdcard(parentPo.getPayerIdcard()); //5
            po.setPayerGender(parentPo.getPayerGender()); //6
            po.setPayerDob(parentPo.getPayerDob()); //7
            po.setPayerAddress1(parentPo.getPayerAddress1()); //8
            po.setPayerAddress2(parentPo.getPayerAddress2()); //9
            po.setPayerAddress3(parentPo.getPayerAddress3()); //10
            po.setPayerSubDistrict(parentPo.getPayerSubDistrict() != null ? parentPo.getPayerSubDistrict() : null); //11
            po.setPayerDistrict(parentPo.getPayerDistrict() != null ? parentPo.getPayerDistrict() : null); //12
            po.setPayerPostalCode(parentPo.getPayerPostalCode()); //13
            po.setPayerMaritalStatus(parentPo.getPayerMaritalStatus()); //14
            po.setPayerHomePhone(parentPo.getPayerHomePhone()); //15
            po.setPayerMobilePhone(parentPo.getPayerMobilePhone()); //16
            po.setPayerOfficePhone(parentPo.getPayerOfficePhone()); //17
            po.setPayerOccupation(parentPo.getPayerOccupation()); //18
            po.setPayerOccupationOther(parentPo.getPayerOccupationOther()); //19
            po.setPayerOccupationCode(parentPo.getPayerOccupationCode()); //19
            po.setPayerEmail(parentPo.getPayerEmail()); //20
        }else{
            RegistrationLogging.logInfo("Purchase order was configured the payer is main insured but product isnot a family product or the purchase order has no parent purchase order  [purchase order id ="+po.getId()+", customer id="+po.getCustomer()+", product plan code="+po.getProductPlanCode()+", parent po="+parentPo+" ]");
            FormUtil.setupEmptyPayer(po); 
        }
    }
    
    public static void setupEmptyPayer(PurchaseOrder po){
        po.setPayerInitial(null); //1
        po.setPayerName(null); //2
        po.setPayerSurname(null); //3
        po.setPayerIdtype(null); //4
        po.setPayerIdcard(null); //5
        po.setPayerGender(null); //6
        po.setPayerDob(null); //7
        po.setPayerAddress1(null); //8
        po.setPayerAddress2(null); //9
        po.setPayerAddress3(null); //10
        po.setPayerSubDistrict(null); //11
        po.setPayerDistrict(null); //12
        po.setPayerPostalCode(null); //13
        po.setPayerMaritalStatus(null); //14
        po.setPayerHomePhone(null); //15
        po.setPayerMobilePhone(null); //16
        po.setPayerOfficePhone(null); //17
        po.setPayerOccupation(null); //18
        po.setPayerOccupationOther(null); //19
        po.setPayerOccupationCode(null); //19
        po.setPayerEmail(null); //20
        RegistrationLogging.logInfo("Purchase order is set to have empty payer info.  [purchase order id ="+po.getId()+", customer id="+po.getCustomer()+", payer type="+po.getPayerType()+" ]");
    }
 
    public static void setupAssignmentDetailStatusFollowingPOStatus(PurchaseOrder po) throws Exception{
        AssignmentDetail ad = po.getAssignmentDetail();
        if (ad != null) {
            switch (po.getSaleResult()) {
                case 'N':
                    if (ad.getSaleResult() == null || !ad.getSaleResult().equalsIgnoreCase("Y")) {
                        ad.setSaleResult("N");
                    }
                    break;
                case 'Y':
                    if (ad.getSaleResult() == null || !ad.getSaleResult().equalsIgnoreCase("Y")) {
                        ad.setSaleResult("Y");
                    }
                    break;
                case 'F':
                    ad.setFollowupsaleDate(po.getFollowupsaleDate());
                    break;
                default :
                    RegistrationLogging.logInfo("Purchase order has invalid sale result [ad id="+ad.getId()+", ad sale result="+ad.getSaleResult()+", ad status="+ad.getStatus()+", po id="+po.getId()+", po sale reuslt="+po.getSaleResult());
            }
            if (ad.getStatus() != null && !ad.getStatus().equals("closed")) {
                ad.setStatus("followup"); // set status of ad to followup after po has occured.
            }
        }
    }
    
    /****************** PRIVATE METHOD *****************/
    private static String autoExecuteApprovalRules(String approvalType, Integer productApprovalRuleId,PurchaseOrder po, List<QaTransDetail> approvalQuestionList) throws Exception{
        String executeResult = null;    //waiting,reject,approved
        boolean bUw = false;
        
        List<ApprovalRuleDetail> approvalRules  = ContextResourceFinder.getApprovalRuleDAO().findApprovalRuleDetail(productApprovalRuleId, approvalType);
        // In case , there are automatic approval rule
        if( approvalRules != null ) {
            List<PurchaseOrderRegister> porList  = FormUtil.getPurchaseOrderRegisterListOfFirstPod(po);
            PurchaseOrderRegister applicantPor = FormUtil.findPurchaseOrderRegisterOfApplicant(porList);
            for (ApprovalRuleDetail ard : approvalRules) {
                bUw = false;
                switch (RegistrationPoController.FieldName.valueOf(ard.getFieldName().toUpperCase())) {
                    case AGE:
                        Integer age = (applicantPor == null )?0:applicantPor.getAge();
                        bUw = ContextResourceFinder.getApprovalRuleDAO().checkParam(age, ard.getOperation(), ard.getValue());
                        break;
                    case BMI:
                        Double bmi = (applicantPor == null )? 0.00 : computeBmi(applicantPor.getWeight(), applicantPor.getHeight() );
                        bUw = bmi == null ? false : ContextResourceFinder.getApprovalRuleDAO().checkBmi(bmi, ard.getOperation(), ard.getValue());
                        break;
                    default:
                        Integer no = null;
                        try { no = Integer.parseInt(ard.getFieldName().substring(8)); }catch(Exception e){}
                        bUw = approvalQuestionList == null ? false : checkQuestion(approvalQuestionList, no, ard.getOperation(), ard.getValue());
                }
                //Not pass rule
                if (bUw) {
                    if(ard.getApprovalRuleDetailPK().getType().equals("reject")) {
                        executeResult = "rejected";
                    }else if(ard.getApprovalRuleDetailPK().getType().equals("uw")){
                        executeResult = "waiting";
                    }
                    break;
                }
            }
        } 
        // pass rule
//        if (!bUw) {
//            executeResult = "approved";
//        }   
        return executeResult; 
    }
    
    
}
