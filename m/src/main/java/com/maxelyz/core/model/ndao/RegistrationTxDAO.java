/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.ndao;

import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.PurchaseOrderDetail;
import com.maxelyz.core.model.entity.PurchaseOrderRegister;
import com.maxelyz.core.model.entity.PurchaseOrderChildRegister;
import com.maxelyz.core.model.entity.PurchaseOrderDeclaration;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.hibernate.LazyInitializationException;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DevTeam
 */
@Transactional(rollbackFor = Exception.class)
public class RegistrationTxDAO {
    @PersistenceContext
    private EntityManager em;

    public RegistrationTxDAO() { 
        //param1 false = don't throw conversion exception
        //param2 true = if there is exception, use null as default value. 
        //param3 -1 = array types will be defaulted to null
        BeanUtilsBean.getInstance().getConvertUtils().register(false, true, -1);        
    }
    
    public void insertPurchaseOrderTx(PurchaseOrder po)throws Exception{
        em.persist(po);
    }
    
    public void updateNewApprovalQaFormOnPurchaseOrder(PurchaseOrder po) throws Exception{
        Query q1 = em.createQuery("update PurchaseOrder po set po.qaTransApproval.id=:qaTransFormId where po.id=:purchaseOrderId ");
        q1.setParameter("qaTransFormId", (po.getQaTransApproval()==null?null:po.getQaTransApproval().getId()) );
        q1.setParameter("purchaseOrderId", po.getId());
        q1.executeUpdate();
    }
    
    public void editPurchaseOrderTx(PurchaseOrder po)throws Exception{
        //Step1 : update purchase order detail data
        this._updatePODData(po);
        
        //Step2 : update purchase order data
        this._updatePOData(po);
    }

    
    public void markRollback(String message) throws RuntimeException{
        throw new RuntimeException("Rollback transaction :"+message);
    }
    
    /************ PRIVATE-METHOD ********************/
   
    private void _updatePODData(PurchaseOrder po)throws Exception{
        if (po!=null && po.getPurchaseOrderDetailCollection()!=null){
            Query q1 = em.createQuery("update PurchaseOrderDetail pod set "
                    + "pod.productPlan=:productPlan, "
                    + "pod.productPlanDetail = :productPlanDetail, "
                    + "pod.paymentMode=:paymentMode, "
                    + "pod.monthlyFirstPayment=:monthlyFirstPayment, "
                    + "pod.unitPrice=:unitPrice, "
                    + "pod.quantity=:quantity, "
                    + "pod.amount=:amount "
                    + "where pod.id=:id");            
            for(PurchaseOrderDetail purchaseOrderDetail : po.getPurchaseOrderDetailCollection() ){
                _updatePORData(purchaseOrderDetail);
                _updatePOChildRegisterData(purchaseOrderDetail);
                _updatePODeclarationData(purchaseOrderDetail);
                q1.setParameter("productPlan", purchaseOrderDetail.getProductPlan());
                q1.setParameter("productPlanDetail", purchaseOrderDetail.getProductPlanDetail());
                q1.setParameter("paymentMode", purchaseOrderDetail.getPaymentMode());
                q1.setParameter("monthlyFirstPayment", purchaseOrderDetail.getMonthlyFirstPayment());
                q1.setParameter("unitPrice", purchaseOrderDetail.getUnitPrice());
                q1.setParameter("quantity", purchaseOrderDetail.getQuantity());
                q1.setParameter("amount", purchaseOrderDetail.getAmount());
                q1.setParameter("id", purchaseOrderDetail.getId());
                q1.executeUpdate();
            }
        }
    }
   
    private void _updatePORData(PurchaseOrderDetail pod)throws Exception{
        if (pod!=null && pod.getPurchaseOrderRegisterCollection()!=null)
            for (PurchaseOrderRegister por : pod.getPurchaseOrderRegisterCollection()) {
                em.merge(por);
            }
    }
    
    private void _updatePOChildRegisterData(PurchaseOrderDetail pod)throws Exception{
        if (pod!=null && pod.getPurchaseOrderChildRegisterCollection()!=null)
            for (PurchaseOrderChildRegister poChildRegister : pod.getPurchaseOrderChildRegisterCollection()) {
                em.merge(poChildRegister);
            }
    }
    
    private void _updatePODeclarationData(PurchaseOrderDetail pod)throws Exception{
        if (pod!=null && pod.getPurchaseOrderDeclarationCollection()!=null)
            for (PurchaseOrderDeclaration poDeclaration : pod.getPurchaseOrderDeclarationCollection()) {
                em.merge(poDeclaration);
            }
    }
    
    private void _updatePOData(PurchaseOrder po)throws Exception{
        String sql = "update PurchaseOrder po set"
                + "  po.paymentMethod=:paymentMethod "
                + ", po.paymentMethodName=:paymentMethodName "
                + ", po.totalAmount=:totalAmount "
                + ", po.cardType=:cardType "
                + ", po.cardIssuer=:cardIssuer "
                + ", po.cardHolderName=:cardHolderName "
                + ", po.cardExpiryMonth=:cardExpiryMonth "
                + ", po.cardExpiryYear=:cardExpiryYear "
                + ", po.cardNo=:cardNo "
                + ", po.traceNo=:traceNo "
                + ", po.settlement = :settlement"
                + ", po.settlementDate = :settlementDate"
                + ", po.settlementBy = :settlementBy"
                + ", po.saleResult=:saleResult "
                + ", po.nosaleReason=:nosaleReason "
                + ", po.followupsaleReason=:followupsaleReason "
                + ", po.followupsaleDate=:followupsaleDate "
                + ", po.approvalStatus=:approvalStatus "
                + ", po.approveBy=:approveBy "
                + ", po.approveDate=:approveDate "
                + ", po.qcStatus = :qcStatus "
                + ", po.paymentStatus=:paymentStatus "
                + ", po.updateDate = :updateDate "
                + ", po.noInstallment = :noInstallment "
                + ", po.dueDate = :dueDate "
                + ", po.amount1 = :amount1 "
                + ", po.amount2 = :amount2 "
                + ", po.discount = :discount "
                + ", po.discountPercent = :discountPercent "
                + ", po.channelCashType =:channelCashType "
                + ", po.channelCashCode =:channelCashCode "
                + ", po.agentCashCode =:agentCashCode "
                + ", po.discountType = :discountType "
                + ", po.vat = :vat "
                + ", po.amountBfVat = :amountBfVat "
                + ", po.qaTransQc = :qaTransQc "
                + ", po.refNo = :refNo "
                + ", po.refNo2 = :refNo2 "
                + ", po.price = :price "
                + ", po.netPremium = :netPremium "
                + ", po.annualNetPremium = :annualNetPremium "
                + ", po.annualPrice = :annualPrice "
                + ", po.sumInsured = :sumInsured "
                + ", po.insurePerson = :insurePerson "
                + ", po.idno = :idno "
                + ", po.firstDamage = :firstDamage "
                + ", po.discountGroup = :discountGroup "
                + ", po.discountGoodRecord = :discountGoodRecord "
                + ", po.bankAccountNo = :bankAccountNo "
                + ", po.bank = :bank "
                + ", po.payerSameAsInsured = :payerSameAsInsured"
                + ", po.payerType = :payerType"
                + ", po.payerIdtype = :payerIdtype"
                + ", po.payerInitial = :payerInitial"
                + ", po.payerName = :payerName"
                + ", po.payerSurname = :payerSurname"
                + ", po.payerIdcard = :payerIdcard"
                + ", po.payerIdcardExpiryDate = :payerIdcardExpiryDate"
                + ", po.payerGender = :payerGender"
                + ", po.payerDob = :payerDob"
                + ", po.payerMaritalStatus = :payerMaritalStatus"
                + ", po.payerHomePhone = :payerHomePhone"
                + ", po.payerMobilePhone = :payerMobilePhone"
                + ", po.payerOfficePhone = :payerOfficePhone"
                + ", po.payerEmail = :payerEmail"
                + ", po.payerRelation = :payerRelation"
                + ", po.payerAddress1 = :payerAddress1"
                + ", po.payerAddress2 = :payerAddress2"
                + ", po.payerAddress3 = :payerAddress3"
                + ", po.payerSubDistrict = :payerSubDistrict"
                + ", po.payerDistrict = :payerDistrict"
                + ", po.payerPostalCode = :payerPostalCode"
                + ", po.payerOccupation = :payerOccupation"
                + ", po.payerOccupationCode = :payerOccupationCode"
                + ", po.payerOccupationOther = :payerOccupationOther"
                + ", po.payerOccupationPosition = :payerOccupationPosition"
                + ", po.paidDate = :paidDate"
                + ", po.yesFlag = :yesFlag"
                + ", po.productPlanCode = :productPlanCode"
                + ", po.productPlanName = :productPlanName"
                + ", po.latestStatus = :latestStatus"
                + ", po.latestStatusDate = :latestStatusDate"
                + ", po.latestDelegateToRole = :latestDelegateToRole"
                + ", po.latestDelegateFromRole = :latestDelegateFromRole"
                + ", po.latestStatusBy = :latestStatusBy"
                + ", po.latestStatusByUser = :latestStatusByUser"
                + ", po.purchaseDate = :purchaseDate"
                + ", po.updateBy = :updateBy"
                + ", po.updateByUser = :updateByUser"
                + ", po.paymentAccountNo = :paymentAccountNo"
                + ", po.paymentExpectedDate = :paymentExpectedDate"
                + ", po.paymentRemarks = :paymentRemarks"
                + ", po.productPlanDetailInfo = :productPlanDetailInfo"
                + "  where po.id = :id";

        Query q = em.createQuery(sql);
        q.setParameter("updateBy", po.getUpdateBy());
        q.setParameter("updateByUser", po.getUpdateByUser());
        q.setParameter("latestStatus",po.getLatestStatus());
        q.setParameter("approveBy",po.getApproveBy());
        q.setParameter("approveDate",po.getApproveDate());
        
        q.setParameter("purchaseDate", po.getPurchaseDate());
        q.setParameter("paymentMethod", po.getPaymentMethod());
        q.setParameter("paymentMethodName", po.getPaymentMethodName());
        q.setParameter("totalAmount", po.getTotalAmount());
        q.setParameter("cardType", po.getCardType());
        q.setParameter("cardIssuer", po.getCardIssuer());
        q.setParameter("cardHolderName", po.getCardHolderName());
        q.setParameter("cardExpiryMonth", po.getCardExpiryMonth());
        q.setParameter("cardExpiryYear", po.getCardExpiryYear());
        q.setParameter("cardNo", po.getCardNo());
        q.setParameter("traceNo", po.getTraceNo());
        q.setParameter("settlement", po.getSettlement());
        q.setParameter("settlementDate", po.getSettlementDate());
        q.setParameter("settlementBy", po.getSettlementBy());
        q.setParameter("traceNo", po.getTraceNo());
        q.setParameter("saleResult", po.getSaleResult());
        q.setParameter("nosaleReason", po.getNosaleReason());
        q.setParameter("followupsaleReason", po.getFollowupsaleReason());
        q.setParameter("followupsaleDate", po.getFollowupsaleDate());
        q.setParameter("approvalStatus", po.getApprovalStatus());
        q.setParameter("qcStatus", po.getQcStatus());
        q.setParameter("paymentStatus", po.getPaymentStatus());
        
        q.setParameter("updateDate", po.getUpdateDate());
        q.setParameter("noInstallment", po.getNoInstallment());
        q.setParameter("dueDate", po.getDueDate());
        q.setParameter("amount1", po.getAmount1());
        q.setParameter("amount2", po.getAmount2());
        q.setParameter("discount", po.getDiscount());
        q.setParameter("discountPercent", po.getDiscountPercent());
        q.setParameter("discountType", po.getDiscountType());
        q.setParameter("channelCashType", po.getChannelCashType());
        q.setParameter("channelCashCode", po.getChannelCashCode());
        q.setParameter("agentCashCode", po.getAgentCashCode());
        q.setParameter("vat", po.getVat());
        q.setParameter("amountBfVat", po.getAmountBfVat());
        q.setParameter("qaTransQc", po.getQaTransQc());
        q.setParameter("refNo", po.getRefNo());
        if (JSFUtil.getApplication().isExternalRefNo()) {
            if (po.getRefNo2() != null && !po.getRefNo2().isEmpty()) {
                q.setParameter("refNo", po.getRefNo2());
            }
        }
        q.setParameter("refNo2", po.getRefNo2());        
        q.setParameter("id", po.getId());
        
        q.setParameter("price",po.getPrice());
        q.setParameter("netPremium",po.getNetPremium());
        q.setParameter("annualNetPremium",po.getAnnualNetPremium());
        q.setParameter("annualPrice",po.getAnnualPrice());
        q.setParameter("sumInsured",po.getSumInsured());
        q.setParameter("insurePerson",po.getInsurePerson());
        q.setParameter("idno",po.getIdno());
        
        q.setParameter("firstDamage",po.getFirstDamage());
        q.setParameter("discountGroup",po.getDiscountGroup());
        q.setParameter("discountGoodRecord",po.getDiscountGoodRecord());
        q.setParameter("bank",po.getBank());
        q.setParameter("bankAccountNo",po.getBankAccountNo());
        
        q.setParameter("payerSameAsInsured", po.getPayerSameAsInsured());
        q.setParameter("payerIdtype", po.getPayerIdtype());
        q.setParameter("payerType", po.getPayerType());
        q.setParameter("payerInitial", po.getPayerInitial());
        q.setParameter("payerName", po.getPayerName());
        q.setParameter("payerSurname", po.getPayerSurname());
        q.setParameter("payerIdcard", po.getPayerIdcard());
        q.setParameter("payerIdcardExpiryDate", po.getPayerIdcardExpiryDate());
        q.setParameter("payerGender", po.getPayerGender());
        q.setParameter("payerDob", po.getPayerDob());
        q.setParameter("payerMaritalStatus", po.getPayerMaritalStatus());
        q.setParameter("payerHomePhone", po.getPayerHomePhone());
        q.setParameter("payerMobilePhone", po.getPayerMobilePhone());
        q.setParameter("payerOfficePhone", po.getPayerOfficePhone());
        q.setParameter("payerEmail", po.getPayerEmail());
        q.setParameter("payerRelation", po.getPayerRelation());
        q.setParameter("payerAddress1", po.getPayerAddress1());
        q.setParameter("payerAddress2", po.getPayerAddress2());
        q.setParameter("payerAddress3", po.getPayerAddress3());
        q.setParameter("payerSubDistrict", po.getPayerSubDistrict());
        q.setParameter("payerDistrict", po.getPayerDistrict());
        q.setParameter("payerPostalCode", po.getPayerPostalCode());
        q.setParameter("payerOccupation", po.getPayerOccupation());
        q.setParameter("payerOccupationCode", po.getPayerOccupationCode());
        q.setParameter("payerOccupationOther", po.getPayerOccupationOther());
        q.setParameter("payerOccupationPosition", po.getPayerOccupationPosition());
        q.setParameter("paidDate", po.getPaidDate());
        q.setParameter("yesFlag", po.getYesFlag());
        q.setParameter("productPlanCode", po.getProductPlanCode());
        q.setParameter("productPlanName", po.getProductPlanName());
        q.setParameter("latestStatusDate", po.getLatestStatusDate());
        q.setParameter("latestDelegateToRole", po.getLatestDelegateToRole());
        q.setParameter("latestDelegateFromRole", po.getLatestDelegateFromRole());
        q.setParameter("latestStatusBy", po.getLatestStatusBy());
        q.setParameter("latestStatusByUser", po.getLatestStatusByUser());
        q.setParameter("paymentAccountNo", po.getPaymentAccountNo());
        q.setParameter("paymentExpectedDate", po.getPaymentExpectedDate());
        q.setParameter("paymentRemarks", po.getPaymentRemarks());
        q.setParameter("productPlanDetailInfo", po.getProductPlanDetailInfo());
        
        q.executeUpdate();
    }
}  
