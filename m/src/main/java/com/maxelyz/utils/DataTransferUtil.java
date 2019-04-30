/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.utils;

import com.maxelyz.core.common.log.RegistrationLogging;
import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.ContactHistory;
import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.PurchaseOrderApprovalLog;
import com.maxelyz.core.model.entity.PurchaseOrderDetail;
import com.maxelyz.core.model.entity.PurchaseOrderQuestionaire;
import com.maxelyz.core.model.entity.PurchaseOrderRegister;
import com.maxelyz.core.model.entity.PurchaseOrderChildRegister;
import com.maxelyz.core.model.entity.PurchaseOrderDeclaration;
import com.maxelyz.core.model.entity.QaTrans;
import com.maxelyz.core.model.entity.QaTransDetail;
import java.util.ArrayList;
import org.apache.commons.beanutils.BeanUtilsBean;

/**
 *
 * @author DevTeam
 */
public class DataTransferUtil {
    private static final BeanUtilsBean beanutil = BeanUtilsBean.getInstance();
    static{
        beanutil.getConvertUtils().register(false, true, -1);   
    }
    
    public static void transferPurchaseOrderData(PurchaseOrder dest, PurchaseOrder source) throws Exception {
        // copy po
        transferPurchaseOrderDataWoCl(dest, source);
        // copy po's children without collection
        try{
            if (  source.getPurchaseOrderCollection() != null ){ // children
                dest.setPurchaseOrderCollection(new ArrayList<PurchaseOrder>(source.getPurchaseOrderCollection().size()));
                for(PurchaseOrder pochild : source.getPurchaseOrderCollection() ){
                    PurchaseOrder newpochild = new PurchaseOrder();
                    transferPurchaseOrderDataWoCl(newpochild, pochild);
                    newpochild.setParentPurchaseOrder(dest); // set parent
                    dest.getPurchaseOrderCollection().add(newpochild);
                }
            } 
        }catch(org.hibernate.LazyInitializationException lazye){ /*do nothing*/ RegistrationLogging.logInfo("Cannot copy purchase order data : "+lazye.getMessage());}
        // copy po's pod 
        try{
            if (  source.getPurchaseOrderDetailCollection() != null ){ 
                dest.setPurchaseOrderDetailCollection(new ArrayList<PurchaseOrderDetail>(source.getPurchaseOrderDetailCollection().size()));
                for(PurchaseOrderDetail podchild : source.getPurchaseOrderDetailCollection() ){
                    PurchaseOrderDetail newpodchild = new PurchaseOrderDetail();
                    transferPurchaseOrderDetailData(newpodchild, podchild);
                    newpodchild.setPurchaseOrder(dest); // set parent
                    dest.getPurchaseOrderDetailCollection().add(newpodchild);
                }
            } 
        }catch(org.hibernate.LazyInitializationException lazye){ /*do nothing*/ RegistrationLogging.logInfo("Cannot copy purchase order detail data : "+lazye.getMessage());}

        /* entity was lazy loaded 
        // copy po's approval log
        try{ // property is lazy load
            if (  source.getPurchaseOrderApprovalLogCollection() != null ){ 
                dest.setPurchaseOrderApprovalLogCollection(new ArrayList<PurchaseOrderApprovalLog>(source.getPurchaseOrderApprovalLogCollection().size()));
                for(PurchaseOrderApprovalLog aplog : source.getPurchaseOrderApprovalLogCollection() ){
                    PurchaseOrderApprovalLog newAplog = new PurchaseOrderApprovalLog();
                    transferPurchaseOrderApprovalLogData(newAplog, aplog);
                    newAplog.setPurchaseOrder(dest);
                    dest.getPurchaseOrderApprovalLogCollection().add(newAplog);
                }
            }
            
        }catch(org.hibernate.LazyInitializationException lazye){ 
            RegistrationLogging.logInfo("Cannot copy purchase order approval log data : "+lazye.getMessage()); 
        }    */ 
        /* entity was lazy loaded
        // copy po's contact history without collection
        try{
            if (   source.getContactHistoryCollection() != null ){ 
                dest.setContactHistoryCollection(new ArrayList<ContactHistory>(source.getContactHistoryCollection().size()));
                for(ContactHistory ch : source.getContactHistoryCollection() ){
                    ContactHistory newCh = new ContactHistory();
                    transferContactHistoryDataWoCl(newCh, ch);
                    dest.getContactHistoryCollection().add(newCh);
                }
            } 
        }catch(org.hibernate.LazyInitializationException lazye){ RegistrationLogging.logInfo("Cannot copy purchase order contact history data : "+lazye.getMessage()); }              
        */
        
    }
    
    public static void transferPurchaseOrderDetailData(PurchaseOrderDetail dest, PurchaseOrderDetail source) throws Exception {
        // copy po detail
        transferPurchaseOrderDetailDataWoCl(dest, source);
        // copy por
        try{
            if ( source.getPurchaseOrderRegisterCollection() != null ){ // children
                dest.setPurchaseOrderRegisterCollection(new ArrayList<PurchaseOrderRegister>(source.getPurchaseOrderRegisterCollection().size()));
                for(PurchaseOrderRegister por : source.getPurchaseOrderRegisterCollection() ){
                    PurchaseOrderRegister newpor = new PurchaseOrderRegister();
                    transferPurchaseOrderRegisterData(newpor, por);
                    newpor.setPurchaseOrderDetail(dest); //set new parent
                    dest.getPurchaseOrderRegisterCollection().add(newpor);
                }
            } 
        }catch(org.hibernate.LazyInitializationException lazye){ /*do nothing*/ RegistrationLogging.logInfo("Cannot copy purchase order data : "+lazye.getMessage());}
        
        /* lazy load
        // copy po questionaire
        try{ // 
            if (  source.getPurchaseOrderQuestionaireCollection() != null ){ // children
                dest.setPurchaseOrderQuestionaireCollection(new ArrayList<PurchaseOrderQuestionaire>(source.getPurchaseOrderQuestionaireCollection().size()));
                for(PurchaseOrderQuestionaire poq : source.getPurchaseOrderQuestionaireCollection() ){
                    PurchaseOrderQuestionaire newpoq = new PurchaseOrderQuestionaire();
                    transferPurchaseOrderQuestionaireData(newpoq, poq);
                    newpoq.setPurchaseOrderDetail(dest);
                    dest.getPurchaseOrderQuestionaireCollection().add(newpoq);
                    
                }
            } 
        }catch(org.hibernate.LazyInitializationException lazye){  RegistrationLogging.logInfo("Cannot copy purchase order data : "+lazye.getMessage());}
        */
    }
    
    public static void transferPurchaseOrderRegisterData(PurchaseOrderRegister dest, PurchaseOrderRegister source) throws Exception {
        beanutil.copyProperties(dest, source);   
    }
    
    public static void transferPurchaseOrderChildRegisterData(PurchaseOrderChildRegister dest, PurchaseOrderChildRegister source) throws Exception {
        beanutil.copyProperties(dest, source);   
    }
    
    public static void transferPurchaseOrderDeclarationData(PurchaseOrderDeclaration dest, PurchaseOrderDeclaration source) throws Exception {
        beanutil.copyProperties(dest, source);   
    }
     
    public static void transferPurchaseOrderApprovalLogData(PurchaseOrderApprovalLog dest, PurchaseOrderApprovalLog source) throws Exception {
        beanutil.copyProperties(dest, source);
    }
    
    public static void transferPurchaseOrderQuestionaireData(PurchaseOrderQuestionaire dest, PurchaseOrderQuestionaire source) throws Exception {
        beanutil.copyProperties(dest, source);
    }
     
    public static void transferQaTransData(QaTrans dest, QaTrans source) throws Exception {
        // copy qa trans
        transferQaTransDataWoCl(dest, source);
        // copy qa trans detail
        try{
            if ( source.getQaTransDetailCollection() != null ){ // children
                dest.setQaTransDetailCollection(new ArrayList<QaTransDetail>(source.getQaTransDetailCollection().size()));
                for(QaTransDetail qad : source.getQaTransDetailCollection() ){
                    QaTransDetail newqad = new QaTransDetail();
                    transferQaTransDetailData(newqad, qad);
                    dest.getQaTransDetailCollection().add(newqad);
                }
            } 
        }catch(org.hibernate.LazyInitializationException lazye){ /*do nothing*/ RegistrationLogging.logInfo("Cannot copy qa trans detail data : "+lazye.getMessage());}
    }
    
    
    public static void transferPurchaseOrderDataWoCl(PurchaseOrder dest, PurchaseOrder source) throws Exception {
        beanutil.copyProperties(dest, source);
        // remove collection
        dest.setPurchaseOrderApprovalLogCollection(null);
        dest.setContactHistoryCollection(null);
        dest.setPurchaseOrderCollection(null);
        dest.setPurchaseOrderDetailCollection(null);
    }
    
    public static void transferPurchaseOrderDetailDataWoCl(PurchaseOrderDetail dest, PurchaseOrderDetail source) throws Exception {
        beanutil.copyProperties(dest, source);
        // remove collection
        dest.setPurchaseOrderQuestionaireCollection(null);
        dest.setPurchaseOrderRegisterCollection(null);

    }
    
    public static void transferContactHistoryDataWoCl(ContactHistory dest, ContactHistory source) throws Exception {
        beanutil.copyProperties(dest, source);
        // remove collection
        dest.setAssignmentDetailCollection(null);
        dest.setContactCaseCollection(null);
        dest.setContactHistoryKnowledgeCollection(null);
        dest.setContactHistoryProductCollection(null);
        dest.setContactHistorySaleResultCollection(null);
        dest.setPurchaseOrderCollection(null);
    }
    
    public static void transferAssignmentDetailDataWoCl(AssignmentDetail dest, AssignmentDetail source) throws Exception {
        beanutil.copyProperties(dest, source);
        // remove collection
        dest.setCampaignCustomerCollection(null);
        dest.setContactHistoryCollection(null);
        dest.setPurchaseOrderCollection(null);
        dest.setPurchaseOrderDetailCollection(null);
        dest.setTempPurchaseOrderCollection(null);
    }

    public static void transferQaTransDataWoCl(QaTrans dest, QaTrans source)throws Exception{
        beanutil.copyProperties(dest, source);
        // remove collection
        dest.setQaTransDetailCollection(null);
    }
    
    public static void transferQaTransDetailData(QaTransDetail dest, QaTransDetail source)throws Exception{
        beanutil.copyProperties(dest, source);
    }
}
