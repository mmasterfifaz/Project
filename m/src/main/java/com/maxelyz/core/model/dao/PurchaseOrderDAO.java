/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.ExportPaymentValue;
import com.maxelyz.core.model.value.admin.SaleApprovalValue;
import com.maxelyz.core.model.value.front.campaign.PendingListInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.SaleHistoryInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.TotalPremiumInfoValue;
import com.maxelyz.utils.JSFUtil;
import com.ntier.utils.ParameterMap;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.faces.bean.ManagedProperty;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PurchaseOrderDAO {

    @PersistenceContext
    private EntityManager em;

    public int findFamilyYesSaleMasterPlan(Integer id) {
        Query q = em.createQuery("select count(pod) from PurchaseOrderDetail as pod "
                + "where pod.purchaseOrder.id =:id and pod.product.familyProduct = true "
                + "and pod.productPlan.masterPlan = true "
                + "and pod.purchaseOrder.saleResult <> 'N' "
                + "and pod.purchaseOrder.mainPoId = null");
        q.setParameter("id", id);
        return ((Long) q.getSingleResult()).intValue();
    }

    public Integer findProductPlanFromMainPoId(Integer id) {
        try {
            Query q = em.createQuery("select pod.productPlan.id from PurchaseOrderDetail as pod where pod.purchaseOrder.id = :id");
            q.setParameter("id", id);
            return ((Integer) q.getSingleResult()).intValue();
        } catch (NoResultException e) {
            return null;
        }
    }

    public int findPurchaseOrderIsYesSale(List<Integer> id) {
        Query q = em.createQuery("select count(po) from PurchaseOrder as po "
                + "where po.id IN (:id) and po.saleResult = 'Y' ");
        q.setParameter("id", id);
        return ((Long) q.getSingleResult()).intValue();
    }

    public int findPurchaseOrderIsYesSaleByCustomerId(Integer customerId) {
        Query q = em.createQuery("select count(po) from PurchaseOrder as po "
                + "where po.saleResult = 'Y' and po.customer.id =:customerId");
        q.setParameter("customerId", customerId);
        return ((Long) q.getSingleResult()).intValue();
    }

    public Integer findTopContactResultPlanInPurchaseOrderIsYesSaleByCustomerIdDESC(Integer customerId) {
        try {
            Query q = em.createQuery("select p.contactResultPlan.id from PurchaseOrderDetail as pod "
                    + "join pod.purchaseOrder as po "
                    + "join pod.product as p "
                    + " where po.saleResult = 'Y' and po.customer.id =:customerId "
                    + " order by po.id DESC");
            q.setMaxResults(1);
            q.setParameter("customerId", customerId);
            return ((Integer) q.getSingleResult()).intValue();
        }catch (NoResultException e){
            return 0;
        }

    }
//
//    public int findContactResultPlanByPurchaseOrderId(List<Integer> purchaseOrderId) {
//        Query q = em.createQuery("select p.contactResultPlan.id from PurchaseOrderDetail as pod "
//                + " join pod.product as p "
//                + " where pod.purchaseOrder.id IN (:purchaseOrderId) order by po.id desc");
//        q.setMaxResults(1);
//        q.setParameter("purchaseOrderId", purchaseOrderId);
//        return ((Integer) q.getSingleResult()).intValue();
//    }

    public int findContactResultPlanByPurchaseOrderIdAndCheckYesSale(List<Integer> purchaseOrderId, boolean yesSale) {
        String sql = "select p.contactResultPlan.id from PurchaseOrderDetail as pod"
                + " join pod.product as p "
                + " join pod.purchaseOrder as po "
                + " where pod.purchaseOrder.id IN (:purchaseOrderId) ";
        if (yesSale == true) {
            sql += " and po.saleResult = 'Y' ";
        }
        if (yesSale == true) {
        sql += "order by po.id";
        }else{
        sql += "order by po.id desc";
        }
        Query q = em.createQuery(sql);
        q.setMaxResults(1);
        q.setParameter("purchaseOrderId", purchaseOrderId);
        return ((Integer) q.getSingleResult()).intValue();
    }

    public void create(PurchaseOrder purchaseOrder) throws PreexistingEntityException, Exception {
        em.persist(purchaseOrder);
    }

    public void createTemp(TempPurchaseOrder tempPurchaseOrder) throws PreexistingEntityException, Exception {
        em.persist(tempPurchaseOrder);
    }

    public void createInstallment(PurchaseOrderInstallment purchaseOrderInstallment) throws PreexistingEntityException, Exception {
        em.persist(purchaseOrderInstallment);
    }

    public void edit(PurchaseOrder purchaseOrder) throws NonexistentEntityException, Exception {
        purchaseOrder = em.merge(purchaseOrder);
    }

    public void updateStatus(PurchaseOrder purchaseOrder) throws NonexistentEntityException, Exception {
        Date dateNow = new Date();
        String sql = "update PurchaseOrder po set"
                + " po.refNo = :refNo"
                + " , po.approvalStatus = :uwStatus"
                + " , po.approveDate = :uwDate"
                + " , po.approveBy = :uwBy"
                + " , po.approveByUser = :uwByUser"
                + " , po.paymentStatus = :paymentStatus"
                + " , po.paymentDate = :paymentDate"
                + " , po.paymentBy = :paymentBy"
                + " , po.paymentByUser = :paymentByUser"
                + " , po.qcStatus = :qcStatus"
                + " , po.qcDate = :qcDate"
                + " , po.qcBy = :qcBy"
                + " , po.qcByUser = :qcByUser"
                + " , po.latestReason = :latestReason"
                + " , po.remark = :remark"
                + " , po.remark2 = :remark2"
                + " , po.saleDate = :saleDate"
                + " , po.otherExclusionCode = :otherExclusionCode"
                + " , po.otherExclusionName = :otherExclusionName"
                + " , po.approvalSupStatus = :supStatus"
                + " , po.approveSupDate = :supDate"
                + " , po.approveSupBy = :supBy"
                + " , po.approveSupByUser = :supByUser"
                + " , po.latestStatus = :latestStatus"
                + " , po.latestStatusDate = :latestStatusDate"
                + " , po.latestDelegateToRole = :latestDelegateToRole"
                + " , po.latestDelegateFromRole = :latestDelegateFromRole"
                + " , po.latestStatusBy = :latestStatusBy"
                + " , po.latestStatusByUser = :latestStatusByUser"
                + " , po.confirmFlag = :confirmFlag"
                + " , po.confirmDate = :confirmDate"
                + " , po.confirmBy = :confirmBy"
                + " , po.confirmByUser = :confirmByUser"
                + " where po.id = :poId";
        Query q = em.createQuery(sql);

        q.setParameter("refNo", purchaseOrder.getRefNo());
        q.setParameter("uwStatus", purchaseOrder.getApprovalStatus());
        q.setParameter("uwDate", purchaseOrder.getApproveDate());
        q.setParameter("uwBy", purchaseOrder.getApproveBy());
        q.setParameter("uwByUser", purchaseOrder.getApproveByUser());
        q.setParameter("paymentStatus", purchaseOrder.getPaymentStatus());
        q.setParameter("paymentDate", purchaseOrder.getPaymentDate());
        q.setParameter("paymentBy", purchaseOrder.getPaymentBy());
        q.setParameter("paymentByUser", purchaseOrder.getPaymentByUser());
        q.setParameter("qcStatus", purchaseOrder.getQcStatus());
        q.setParameter("qcDate", purchaseOrder.getQcDate());
        q.setParameter("qcBy", purchaseOrder.getQcBy());
        q.setParameter("qcByUser", purchaseOrder.getQcByUser());
        q.setParameter("latestReason", purchaseOrder.getLatestReason());
        q.setParameter("remark", purchaseOrder.getRemark());
        q.setParameter("remark2", purchaseOrder.getRemark2());
        q.setParameter("saleDate", purchaseOrder.getSaleDate());
        q.setParameter("otherExclusionCode", purchaseOrder.getOtherExclusionCode());
        q.setParameter("otherExclusionName", purchaseOrder.getOtherExclusionName());
        q.setParameter("supStatus", purchaseOrder.getApprovalSupStatus());
        q.setParameter("supDate", purchaseOrder.getApproveSupDate());
        q.setParameter("supBy", purchaseOrder.getApproveSupBy());
        q.setParameter("supByUser", purchaseOrder.getApproveSupByUser());
        q.setParameter("latestStatus", purchaseOrder.getLatestStatus());
        q.setParameter("latestStatusDate", purchaseOrder.getLatestStatusDate());
        q.setParameter("latestDelegateToRole", purchaseOrder.getLatestDelegateToRole());
        q.setParameter("latestDelegateFromRole", purchaseOrder.getLatestDelegateFromRole());
        q.setParameter("latestStatusBy", purchaseOrder.getLatestStatusBy());
        q.setParameter("latestStatusByUser", purchaseOrder.getLatestStatusByUser());
        q.setParameter("confirmFlag", purchaseOrder.getConfirmFlag());
        q.setParameter("confirmDate", purchaseOrder.getConfirmDate());
        q.setParameter("confirmBy", purchaseOrder.getConfirmBy());
        q.setParameter("confirmByUser", purchaseOrder.getConfirmByUser());

        q.setParameter("poId", purchaseOrder.getId());
        try {
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editRetail(PurchaseOrder purchaseOrder) throws NonexistentEntityException, Exception {

        //choose update PurchaseOrder
        String sql = "update PurchaseOrder po set "
                + "po.billingFullname = :billingFullname "
                + ", po.billingAddressLine1 = :billingAddressLine1 "
                + ", po.billingAddressLine2 = :billingAddressLine2 "
                + ", po.billingDistrict = :billingDistrict "
                + ", po.billingPostalCode = :billingPostalCode "
                + ", po.shippingFullname = :shippingFullname "
                + ", po.shippingAddressLine1 = :shippingAddressLine1 "
                + ", po.shippingAddressLine2 = :shippingAddressLine2 "
                + ", po.shippingDistrict = :shippingDistrict "
                + ", po.shippingPostalCode = :shippingPostalCode "
                + ", po.paymentMethod=:paymentMethod "
                + ", po.paymentMethodName=:paymentMethodName "
                + ", po.cardType=:cardType "
                + ", po.cardHolderName=:cardHolderName "
                + ", po.cardExpiryMonth=:cardExpiryMonth "
                + ", po.cardExpiryYear=:cardExpiryYear "
                + ", po.cardNo=:cardNo "
                + ", po.traceNo=:traceNo "
                + ", po.saleResult=:saleResult "
                + ", po.nosaleReason=:nosaleReason "
                + ", po.followupsaleReason=:followupsaleReason "
                + ", po.followupsaleDate=:followupsaleDate "
                + ", po.updateDate = GETDATE() ";
        if (String.valueOf(purchaseOrder.getSaleResult()).equals("Y")) {
            if (purchaseOrder.getPurchaseDate() == null) {
                sql += ", po.purchaseDate = GETDATE() ";
            }
        } else {
            sql += ", po.purchaseDate = null ";
        }
        sql += "where po.id=:id";

        Query q = em.createQuery(sql);

        q.setParameter("billingFullname", purchaseOrder.getBillingFullname());
        q.setParameter("billingAddressLine1", purchaseOrder.getBillingAddressLine1());
        q.setParameter("billingAddressLine2", purchaseOrder.getBillingAddressLine2());
        q.setParameter("billingDistrict", purchaseOrder.getBillingDistrict());
        q.setParameter("billingPostalCode", purchaseOrder.getBillingPostalCode());
        q.setParameter("shippingFullname", purchaseOrder.getShippingFullname());
        q.setParameter("shippingAddressLine1", purchaseOrder.getShippingAddressLine1());
        q.setParameter("shippingAddressLine2", purchaseOrder.getShippingAddressLine2());
        q.setParameter("shippingDistrict", purchaseOrder.getShippingDistrict());
        q.setParameter("shippingPostalCode", purchaseOrder.getShippingPostalCode());

        q.setParameter("paymentMethod", purchaseOrder.getPaymentMethod());
        q.setParameter("paymentMethodName", purchaseOrder.getPaymentMethodName());
        q.setParameter("cardType", purchaseOrder.getCardType());
        q.setParameter("cardHolderName", purchaseOrder.getCardHolderName());
        q.setParameter("cardExpiryMonth", purchaseOrder.getCardExpiryMonth());
        q.setParameter("cardExpiryYear", purchaseOrder.getCardExpiryYear());
        q.setParameter("cardNo", purchaseOrder.getCardNo());
        q.setParameter("traceNo", purchaseOrder.getTraceNo());
        q.setParameter("saleResult", purchaseOrder.getSaleResult());
        q.setParameter("nosaleReason", purchaseOrder.getNosaleReason());
        q.setParameter("followupsaleReason", purchaseOrder.getFollowupsaleReason());
        q.setParameter("followupsaleDate", purchaseOrder.getFollowupsaleDate());
        q.setParameter("id", purchaseOrder.getId());
        q.executeUpdate();

    }

    public void editApproval(PurchaseOrder purchaseOrder, String paymentMode, boolean newApproval) throws NonexistentEntityException, Exception {
        if (newApproval) {
            Query q = em.createNativeQuery("{call GetNextSeqFalcon(:tableName,:paymentMode)}");
            //Query q = em.createNativeQuery("{call GetNextSeq(:tableName)}");
            q.setParameter("tableName", "PURCHASE_ORDER_FALCON");
            if ("1".equals(paymentMode)) {
                //Monthly
                q.setParameter("paymentMode", "M");
            } else if ("2".equals(paymentMode)) {
                //Quarterly
                q.setParameter("paymentMode", "Q");
            } else if ("3".equals(paymentMode)) {
                //Half Year
                q.setParameter("paymentMode", "H");
            } else if ("4".equals(paymentMode)) {
                //Yearly
                q.setParameter("paymentMode", "Y");
            } else {
                q.setParameter("paymentMode", "");
            }
            Object o = q.getSingleResult();
            purchaseOrder.setRefNo(o.toString());
        }
        purchaseOrder = em.merge(purchaseOrder);
    }

    public synchronized String genRefNo(String paymentMode) throws NonexistentEntityException, Exception {
        String str = "";

        Query q = em.createNativeQuery("{call GetNextSeqFalcon(:tableName,:paymentMode)}");
        //Query q = em.createNativeQuery("{call GetNextSeq(:tableName)}");
        q.setParameter("tableName", "PURCHASE_ORDER_FALCON");
        if ("1".equals(paymentMode)) {
            //Monthly
            q.setParameter("paymentMode", "M");
        } else if ("2".equals(paymentMode)) {
            //Quarterly
            q.setParameter("paymentMode", "Q");
        } else if ("3".equals(paymentMode)) {
            //Half Year
            q.setParameter("paymentMode", "H");
        } else if ("4".equals(paymentMode)) {
            //Yearly
            q.setParameter("paymentMode", "Y");
        } else {
            q.setParameter("paymentMode", "");
        }
        Object o = q.getSingleResult();
        str = o.toString();

        return str;
    }

    public synchronized String genRefNo2(String paymentMode) throws NonexistentEntityException, Exception {
        String str = "";

        Query q = em.createNativeQuery("{call GetNextSeqFalcon(:tableName,:paymentMode)}");
        //Query q = em.createNativeQuery("{call GetNextSeq(:tableName)}");
        q.setParameter("tableName", "PURCHASE_ORDER_REFNO2");
        if ("1".equals(paymentMode)) {
            //Monthly
            q.setParameter("paymentMode", "M");
        } else if ("2".equals(paymentMode)) {
            //Quarterly
            q.setParameter("paymentMode", "Q");
        } else if ("3".equals(paymentMode)) {
            //Half Year
            q.setParameter("paymentMode", "H");
        } else if ("4".equals(paymentMode)) {
            //Yearly
            q.setParameter("paymentMode", "Y");
        } else {
            q.setParameter("paymentMode", "");
        }
        Object o = q.getSingleResult();
        str = o.toString();

        return str;
    }

    public void editTemp(TempPurchaseOrder tempPurchaseOrder) {
        tempPurchaseOrder = em.merge(tempPurchaseOrder);
    }

    public void editTemp(TempPurchaseOrder tempPurchaseOrder, TempPurchaseOrderDetail tempPurchaseOrderDetail) throws NonexistentEntityException, Exception {
        //manual update for better performance
        List<TempPurchaseOrderRegister> regDetails = (List<TempPurchaseOrderRegister>) tempPurchaseOrderDetail.getTempPurchaseOrderRegisterCollection();
        //  List<TempPurchaseOrderQuestionaire> poQuestionaires = (List<TempPurchaseOrderQuestionaire>) tempPurchaseOrderDetail.getTempPurchaseOrderQuestionaireCollection();

        //delete then insert
        for (TempPurchaseOrderRegister regDetail : regDetails) {
            if (regDetail.getId() != null) {
                try {
                    this.destroyTempPurchseOrderRegister(regDetail.getId());
                } catch (Exception e) {
                    //no need to recover
                    e.printStackTrace();
                }
            }
            regDetail.setId(null);
            em.persist(regDetail);
        }
        /* for (TempPurchaseOrderQuestionaire poQuestionaire : poQuestionaires) {
         if (poQuestionaire.getId() != null) {
         try {
         this.destroyTempPurchseOrderQuestionnaire(poQuestionaire.getId());
         } catch (Exception e) {
         //no need to recover
         e.printStackTrace();
         }
         }
         poQuestionaire.setId(null);
         em.persist(poQuestionaire);
         }*/

        //not need  merge tempPurchaseOrderDetail
        String sql = "update TempPurchaseOrder po set "
                + "po.approvalStatus=:approvalStatus "
                + ", po.paymentMethod=:paymentMethod "
                + ", po.totalAmount=:totalAmount "
                + ", po.cardType=:cardType "
                + ", po.cardHolderName=:cardHolderName "
                + ", po.cardExpiryMonth=:cardExpiryMonth "
                + ", po.cardExpiryYear=:cardExpiryYear "
                + ", po.cardNo=:cardNo "
                + ", po.traceNo=:traceNo "
                + ", po.saleResult=:saleResult "
                + ", po.nosaleReason=:nosaleReason "
                + ", po.followupsaleReason=:followupsaleReason"
                + ", po.followupsaleDate=:followupsaleDate "
                + ", po.updateDate = GETDATE() ";
        if (String.valueOf(tempPurchaseOrder.getSaleResult()).equals("Y")) {
            if (tempPurchaseOrder.getPurchaseDate() == null) {
                sql += ", po.purchaseDate = GETDATE() ";
            }
        } else {
            sql += ", po.purchaseDate = null ";
        }
        sql += "where po.id=:id";

        Query q = em.createQuery(sql);

        q.setParameter("approvalStatus", tempPurchaseOrder.getApprovalStatus());
        q.setParameter("paymentMethod", tempPurchaseOrder.getPaymentMethod());
        q.setParameter("totalAmount", tempPurchaseOrder.getTotalAmount());
        q.setParameter("cardType", tempPurchaseOrder.getCardType());
        q.setParameter("cardHolderName", tempPurchaseOrder.getCardHolderName());
        q.setParameter("cardExpiryMonth", tempPurchaseOrder.getCardExpiryMonth());
        q.setParameter("cardExpiryYear", tempPurchaseOrder.getCardExpiryYear());
        q.setParameter("cardNo", tempPurchaseOrder.getCardNo());
        q.setParameter("traceNo", tempPurchaseOrder.getTraceNo());
        q.setParameter("saleResult", tempPurchaseOrder.getSaleResult());
        q.setParameter("nosaleReason", tempPurchaseOrder.getNosaleReason());
        q.setParameter("followupsaleReason", tempPurchaseOrder.getFollowupsaleReason());
        q.setParameter("followupsaleDate", tempPurchaseOrder.getFollowupsaleDate());
        q.setParameter("id", tempPurchaseOrder.getId());
        q.executeUpdate();

    }

    public void editTempDetail(TempPurchaseOrderDetail tempPurchaseOrderDetail) throws NonexistentEntityException, Exception {
        tempPurchaseOrderDetail = em.merge(tempPurchaseOrderDetail);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        PurchaseOrder purchaseOrder;
        try {
            purchaseOrder = em.getReference(PurchaseOrder.class, id);
            purchaseOrder.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The purchaseOrder with id " + id + " no longer exists.", enfe);
        }
        em.remove(purchaseOrder);
    }

    public void destroyTempByCustomer(Customer customer) throws NonexistentEntityException {
        Query q = em.createQuery("delete from TempPurchaseOrder where customer = ?1");
        q.setParameter(1, customer);
        q.executeUpdate();
    }

    public void destroyTemp(Integer id) throws NonexistentEntityException {
        TempPurchaseOrder tempPurchaseOrder;
        try {
            tempPurchaseOrder = em.getReference(TempPurchaseOrder.class, id);
            tempPurchaseOrder.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The tempPurchaseOrder with id " + id + " no longer exists.", enfe);
        }
        em.remove(tempPurchaseOrder);
    }

    public void destroyTempPurchseOrderRegister(Long id) throws NonexistentEntityException {
        TempPurchaseOrderRegister tempPurchaseOrderRegister;
        try {
            tempPurchaseOrderRegister = em.getReference(TempPurchaseOrderRegister.class, id);
            tempPurchaseOrderRegister.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The tempPurchaseOrderRegister with id " + id + " no longer exists.", enfe);
        }
        em.remove(tempPurchaseOrderRegister);
    }
    /*
     public void destroyTempPurchseOrderQuestionnaire(Integer id) throws NonexistentEntityException {
     TempPurchaseOrderQuestionaire tempPurchaseOrderQuestionaire;
     try {
     tempPurchaseOrderQuestionaire = em.getReference(TempPurchaseOrderQuestionaire.class, id);
     tempPurchaseOrderQuestionaire.getId();
     } catch (EntityNotFoundException enfe) {
     throw new NonexistentEntityException("The tempPurchaseOrderQuestionnaire with id " + id + " no longer exists.", enfe);
     }
     em.remove(tempPurchaseOrderQuestionaire);
     }*/

    public List<PurchaseOrder> findPurchaseOrderEntities() {
        return findPurchaseOrderEntities(true, -1, -1);
    }

    public List<PurchaseOrder> findPurchaseOrderEntities(int maxResults, int firstResult) {
        return findPurchaseOrderEntities(false, maxResults, firstResult);
    }

    private List<PurchaseOrder> findPurchaseOrderEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from PurchaseOrder as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public PurchaseOrder findPurchaseOrder(Integer id) {
        return em.find(PurchaseOrder.class, id);
    }

    public int getPurchaseOrderCount() {
        return ((Long) em.createQuery("select count(o) from PurchaseOrder as o").getSingleResult()).intValue();
    }

    public int purchaseOrderCountByCustomerId(Integer customerId) {
        return ((Long) em.createQuery("select count(o) from PurchaseOrder as o where o.customer.id ="+ customerId).getSingleResult()).intValue();
    }

    public List<PurchaseOrder> findByCustomer(Customer customer) {
        Query q = em.createQuery("select object(o) from PurchaseOrder as o where o.customer = :customer order by o.id desc");
        q.setParameter("customer", customer);

        List<PurchaseOrder> list = q.getResultList();

        return list;
    }

    public List<TempPurchaseOrder> findTempByCustomer(Customer customer) {
        Query q = em.createQuery("select object(o) from TempPurchaseOrder as o where o.customer = :customer order by o.id");
        q.setParameter("customer", customer);

        List<TempPurchaseOrder> list = q.getResultList();

        return list;
    }

    public TempPurchaseOrder findTempPurchaseOrder(Integer id) {
        return em.find(TempPurchaseOrder.class, id);
    }

    public TempPurchaseOrderDetail findTempPurchaseOrderDetailByProduct(Integer tempPurchaseOrderId, Integer productId) {
        Query q = em.createQuery("select poDetail from TempPurchaseOrder po join po.tempPurchaseOrderDetailCollection poDetail join poDetail.product p  where po.id=?1 and p.id=?2");
        q.setParameter(1, tempPurchaseOrderId);
        q.setParameter(2, productId);
        q.setMaxResults(1);
        return (TempPurchaseOrderDetail) q.getSingleResult();
    }

    public PurchaseOrderDetail findPurchaseOrderDetailByProduct(Integer purchaseOrderId, Integer productId) {
        Query q = em.createQuery("select poDetail from PurchaseOrder po join po.purchaseOrderDetailCollection poDetail join poDetail.product p  where po.id=?1 and p.id=?2");
        q.setParameter(1, purchaseOrderId);
        q.setParameter(2, productId);
        q.setMaxResults(1);
        return (PurchaseOrderDetail) q.getSingleResult();
    }

    public void createTempRegister(TempPurchaseOrderRegister tempPurchaseOrderRegister) throws NonexistentEntityException, Exception {
        tempPurchaseOrderRegister = em.merge(tempPurchaseOrderRegister);
    }

    public List<SaleApprovalValue> findPurchaseOrderBySaleApproval(Date fromSaleDate, Date toSaleDate, String tmrName, String approvalStatus, String salesResult) {
        String statusValue = "";
        if (!approvalStatus.equals("all")) {
            statusValue = approvalStatus;
        }

        String select = "select new " + SaleApprovalValue.class.getName() + "(po.id, po.refNo, cust.id, cust.name+' '+coalesce(cust.surname,''), po.purchaseDate, po.saleResult, createByUser.name,updateByUser.name, po.approvalStatus, po.paymentStatus) "
                + "from PurchaseOrder po join po.customer cust "
                + "left join po.createByUser createByUser "
                + "left join po.updateByUser updateByUser ";
        String where = "where (po.purchaseDate between ?1 and ?2) ";
        if (tmrName.trim().length() > 0) {
            where += "and (createByUser.name like ?3 or createByUser.surname like ?3) ";
        }
        if (!approvalStatus.equals("all")) {
            where += "and po.approvalStatus = '" + approvalStatus + "' ";
        }
        if (!salesResult.equals("all")) {
            where += "and po.saleResult = '" + salesResult + "' ";
        }

        String orderBy = "order by po.refNo, po.purchaseDate ";
        String sql = select + where + orderBy;
        Query q = em.createQuery(sql);
        q.setParameter(1, JSFUtil.toDateWithoutTime(fromSaleDate));
        q.setParameter(2, JSFUtil.toDateWithMaxTime(toSaleDate));

        if (tmrName.trim().length() > 0) {
            q.setParameter(3, "%" + tmrName + "%");
        }

        return q.getResultList();
    }

    public List<SaleApprovalValue> findPurchaseOrderBySaleApproval(Date fromSaleDate, Date toSaleDate, String dummy) {
        String statusValue = "";

        String select = "select new " + SaleApprovalValue.class.getName() + "(po.id, po.refNo, cust.id, cust.name+' '+coalesce(cust.surname,''), po.purchaseDate, po.saleResult, createByUser.name,updateByUser.name, po.approvalStatus, po.paymentStatus) "
                + "from PurchaseOrder po join po.customer cust "
                + "left join po.createByUser createByUser "
                + "left join po.updateByUser updateByUser ";
        String where = "where (po.saleDate between ?1 and ?2) ";

        String orderBy = "order by po.refNo, po.saleDate ";
        String sql = select + where + orderBy;
        Query q = em.createQuery(sql);
        q.setParameter(1, JSFUtil.toDateWithoutTime(fromSaleDate));
        q.setParameter(2, JSFUtil.toDateWithMaxTime(toSaleDate));

        return q.getResultList();
    }

    public List<PurchaseOrder> findPurchaseOrderBySaleApproval(Date fromSaleDate, Date toSaleDate) {
        String select = "select po from PurchaseOrder po ";
        String where = "where (po.purchaseDate between ?1 and ?2) and po.approvalStatus = 'approved' and po.saleResult='Y' ";
        String orderBy = "order by po.refNo, po.purchaseDate";
        String sql = select + where + orderBy;
        Query q = em.createQuery(sql);
        q.setParameter(1, JSFUtil.toDateWithoutTime(fromSaleDate));
        q.setParameter(2, JSFUtil.toDateWithMaxTime(toSaleDate));
        return q.getResultList();
    }

    public List<PurchaseOrder> findPurchaseOrderBySaleApproval(Date fromSaleDate, Date toSaleDate, boolean approvalstatus) {
        String select = "select po from PurchaseOrder po ";
        String where = "";
        if (approvalstatus) {
            where = "where (po.saleDate between ?1 and ?2) and po.approvalStatus = 'approved' and po.saleResult='Y' ";
        } else {
            where = "where (po.purchaseDate between ?1 and ?2) and po.approvalStatus = 'approved' and (po.paymentStatus = 'waiting' or po.paymentStatus = 'pending') and po.saleResult='Y' ";
        }

        String orderBy = "order by po.refNo, po.purchaseDate";
        String sql = select + where + orderBy;
        Query q = em.createQuery(sql);
        q.setParameter(1, JSFUtil.toDateWithoutTime(fromSaleDate));
        q.setParameter(2, JSFUtil.toDateWithMaxTime(toSaleDate));

        return q.getResultList();
    }

    public void createApprovalLog(PurchaseOrderApprovalLog purchaseOrderApprovalLog) throws PreexistingEntityException, Exception {
        em.persist(purchaseOrderApprovalLog);
    }

    public void editApprovalLog(PurchaseOrderApprovalLog purchaseOrderApprovalLog) throws PreexistingEntityException, Exception {
        em.merge(purchaseOrderApprovalLog);
    }

    public List<TempPurchaseOrderRegister> findTempPurchaseOrderRegisterByPoDetail(Integer poDetailId) {
        Query q = em.createQuery("select reg from TempPurchaseOrderDetail poDetail join poDetail.tempPurchaseOrderRegisterCollection as reg where poDetail.id=?1");
        q.setParameter(1, poDetailId);
        return q.getResultList();
    }

    public List<PurchaseOrderRegister> findPurchaseOrderRegisterByPoDetail(Integer poDetailId) {
        Query q = em.createQuery("select reg from PurchaseOrderDetail poDetail join poDetail.purchaseOrderRegisterCollection as reg where poDetail.id=?1");
        q.setParameter(1, poDetailId);
        return q.getResultList();
    }

    public List<PurchaseOrderDeclaration> findPurchaseOrderDeclarationByPoDetail(Integer poDetailId) {
        Query q = em.createQuery("select declaration from PurchaseOrderDetail poDetail join poDetail.purchaseOrderDeclarationCollection as poDeclaration where poDetail.id=?1");
        q.setParameter(1, poDetailId);
        return q.getResultList();
    }

    public void editPo(PurchaseOrder purchaseOrder, PurchaseOrderDetail purchaseOrderDetail) throws NonexistentEntityException, Exception {
        //manual update for better performance
        List<PurchaseOrderRegister> regDetails = (List<PurchaseOrderRegister>) purchaseOrderDetail.getPurchaseOrderRegisterCollection();

        String insurePerson = "";
        String idno = "";
        //delete then insert
        for (PurchaseOrderRegister regDetail : regDetails) {
            /*if (regDetail.getId() != null) {
             try {
             this.destroyPurchseOrderRegister(regDetail.getId());
             } catch (Exception e) {
             //no need to recover
             //     e.printStackTrace();
             }
             }*/
            //regDetail.setId(null);
            if (regDetail.getSurname() != null) {
                insurePerson += regDetail.getName() + " " + regDetail.getSurname() + ",";
            } else {
                insurePerson += regDetail.getName() + ",";
            }
            idno = regDetail.getIdno() + ",";
            try {
                //em.merge(regDetail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*  if(purchaseOrderDetail.getProduct().getRegistrationForm().getQuestionnaire() != null){
         for (PurchaseOrderQuestionaire poQuestionaire : poQuestionaires) {
         if (poQuestionaire.getId() != null) {
         try {
         this.destroyPurchseOrderQuestionnaire(poQuestionaire.getId());
         } catch (Exception e) {
         //no need to recover
         e.printStackTrace();
         }
         }
         poQuestionaire.setId(null);
         em.persist(poQuestionaire);
         }
         }*/
//        System.out.println("paymentmode=" + purchaseOrderDetail.getPaymentMode());
        if (!insurePerson.equals("")) {
            insurePerson = insurePerson.substring(0, insurePerson.length() - 1);
        }
        if (!idno.equals("")) {
            idno = idno.substring(0, idno.length() - 1);
        }
        //choose update PurchaseOrderDetail
        Query q1 = em.createQuery("update PurchaseOrderDetail pod set "
                + "pod.productPlan=:productPlan, "
                + "pod.productPlanDetail = :productPlanDetail, "
                + "pod.paymentMode=:paymentMode, "
                + "pod.monthlyFirstPayment=:monthlyFirstPayment, "
                + "pod.unitPrice=:unitPrice, "
                + "pod.quantity=:quantity, "
                + "pod.amount=:amount "
                + "where pod.id=:id");
        q1.setParameter("productPlan", purchaseOrderDetail.getProductPlan());
        q1.setParameter("productPlanDetail", purchaseOrderDetail.getProductPlanDetail());
        q1.setParameter("paymentMode", purchaseOrderDetail.getPaymentMode());
        q1.setParameter("monthlyFirstPayment", purchaseOrderDetail.getMonthlyFirstPayment());
        q1.setParameter("unitPrice", purchaseOrderDetail.getUnitPrice());
        q1.setParameter("quantity", purchaseOrderDetail.getQuantity());
        q1.setParameter("amount", purchaseOrderDetail.getAmount());
        q1.setParameter("id", purchaseOrderDetail.getId());
        try {
            q1.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql = "update PurchaseOrder po set "
                + "po.paymentMethod=:paymentMethod "
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
                + ", po.qcStatus = :qcStatus "
                + ", po.paymentStatus=:paymentStatus "
                + ", po.updateDate = :updateDate "
                + ", po.noInstallment = :noInstallment "
                + ", po.dueDate = :dueDate "
                + ", po.amount1 = :amount1 "
                + ", po.amount2 = :amount2 "
                + ", po.discount = :discount "
                + ", po.vat = :vat "
                + ", po.amountBfVat = :amountBfVat "
                + ", po.qaTransQc = :qaTransQc "
                + ", po.refNo2 = :refNo "
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
                + ", po.latestStatusDate = :latestStatusDate"
                + ", po.latestDelegateToRole = :latestDelegateToRole"
                + ", po.latestDelegateFromRole = :latestDelegateFromRole"
                + ", po.latestStatusBy = :latestStatusBy"
                + ", po.latestStatusByUser = :latestStatusByUser ";

        if (String.valueOf(purchaseOrder.getSaleResult()).equals("Y")) {
            if (purchaseOrder.getPurchaseDate() == null) {
                sql += ", po.purchaseDate = GETDATE() ";
            }
        } else {
            sql += ", po.purchaseDate = null ";
        }
        sql += " where po.id=:id";

        Query q = em.createQuery(sql);

        //q.setParameter("approvalStatus", purchaseOrder.getApprovalStatus());
        q.setParameter("paymentMethod", purchaseOrder.getPaymentMethod());
        q.setParameter("paymentMethodName", purchaseOrder.getPaymentMethodName());
        q.setParameter("totalAmount", purchaseOrder.getTotalAmount());
        q.setParameter("cardType", purchaseOrder.getCardType());
        q.setParameter("cardIssuer", purchaseOrder.getCardIssuer());
        q.setParameter("cardHolderName", purchaseOrder.getCardHolderName());
        q.setParameter("cardExpiryMonth", purchaseOrder.getCardExpiryMonth());
        q.setParameter("cardExpiryYear", purchaseOrder.getCardExpiryYear());
        q.setParameter("cardNo", purchaseOrder.getCardNo());
        q.setParameter("traceNo", purchaseOrder.getTraceNo());
        q.setParameter("settlement", purchaseOrder.getSettlement());
        q.setParameter("settlementDate", purchaseOrder.getSettlementDate());
        q.setParameter("settlementBy", purchaseOrder.getSettlementBy());
        q.setParameter("traceNo", purchaseOrder.getTraceNo());
        q.setParameter("saleResult", purchaseOrder.getSaleResult());
        q.setParameter("nosaleReason", purchaseOrder.getNosaleReason());
        q.setParameter("followupsaleReason", purchaseOrder.getFollowupsaleReason());
        q.setParameter("followupsaleDate", purchaseOrder.getFollowupsaleDate());
        q.setParameter("approvalStatus", purchaseOrder.getApprovalStatus());
        q.setParameter("qcStatus", purchaseOrder.getQcStatus());
        q.setParameter("paymentStatus", purchaseOrder.getPaymentStatus());

        q.setParameter("updateDate", purchaseOrder.getUpdateDate());
        q.setParameter("noInstallment", purchaseOrder.getNoInstallment());
        q.setParameter("dueDate", purchaseOrder.getDueDate());
        q.setParameter("amount1", purchaseOrder.getAmount1());
        q.setParameter("amount2", purchaseOrder.getAmount2());
        q.setParameter("discount", purchaseOrder.getDiscount());
        q.setParameter("vat", purchaseOrder.getVat());
        q.setParameter("amountBfVat", purchaseOrder.getAmountBfVat());
        q.setParameter("qaTransQc", purchaseOrder.getQaTransQc());
        q.setParameter("refNo", purchaseOrder.getRefNo() == null ? null : purchaseOrder.getRefNo());
        q.setParameter("id", purchaseOrder.getId());

        q.setParameter("price", purchaseOrder.getPrice());
        q.setParameter("netPremium", purchaseOrder.getNetPremium());
        q.setParameter("annualNetPremium", purchaseOrder.getAnnualNetPremium());
        q.setParameter("annualPrice", purchaseOrder.getAnnualPrice());
        q.setParameter("sumInsured", purchaseOrder.getSumInsured());
        q.setParameter("insurePerson", insurePerson);
        q.setParameter("idno", idno);

        q.setParameter("firstDamage", purchaseOrder.getFirstDamage());
        q.setParameter("discountGroup", purchaseOrder.getDiscountGroup());
        q.setParameter("discountGoodRecord", purchaseOrder.getDiscountGoodRecord());
        q.setParameter("bank", purchaseOrder.getBank());
        q.setParameter("bankAccountNo", purchaseOrder.getBankAccountNo());

        q.setParameter("payerSameAsInsured", purchaseOrder.getPayerSameAsInsured());
        q.setParameter("payerIdtype", purchaseOrder.getPayerIdtype());
        q.setParameter("payerType", purchaseOrder.getPayerType());
        q.setParameter("payerInitial", purchaseOrder.getPayerInitial());
        q.setParameter("payerName", purchaseOrder.getPayerName());
        q.setParameter("payerSurname", purchaseOrder.getPayerSurname());
        q.setParameter("payerIdcard", purchaseOrder.getPayerIdcard());
        q.setParameter("payerIdcardExpiryDate", purchaseOrder.getPayerIdcardExpiryDate());
        q.setParameter("payerGender", purchaseOrder.getPayerGender());
        q.setParameter("payerDob", purchaseOrder.getPayerDob());
        q.setParameter("payerMaritalStatus", purchaseOrder.getPayerMaritalStatus());
        q.setParameter("payerHomePhone", purchaseOrder.getPayerHomePhone());
        q.setParameter("payerMobilePhone", purchaseOrder.getPayerMobilePhone());
        q.setParameter("payerOfficePhone", purchaseOrder.getPayerOfficePhone());
        q.setParameter("payerEmail", purchaseOrder.getPayerEmail());
        q.setParameter("payerRelation", purchaseOrder.getPayerRelation());
        q.setParameter("payerAddress1", purchaseOrder.getPayerAddress1());
        q.setParameter("payerAddress2", purchaseOrder.getPayerAddress2());
        q.setParameter("payerAddress3", purchaseOrder.getPayerAddress3());
        q.setParameter("payerSubDistrict", purchaseOrder.getPayerSubDistrict());
        q.setParameter("payerDistrict", purchaseOrder.getPayerDistrict());
        q.setParameter("payerPostalCode", purchaseOrder.getPayerPostalCode());
        q.setParameter("payerOccupation", purchaseOrder.getPayerOccupation());
        q.setParameter("payerOccupationCode", purchaseOrder.getPayerOccupationCode());
        q.setParameter("payerOccupationOther", purchaseOrder.getPayerOccupationOther());
        q.setParameter("payerOccupationPosition", purchaseOrder.getPayerOccupationPosition());
        q.setParameter("paidDate", purchaseOrder.getPaidDate() == null ? null : purchaseOrder.getPaidDate());
        q.setParameter("yesFlag", purchaseOrder.getYesFlag());
        q.setParameter("productPlanCode", purchaseOrder.getProductPlanCode());
        q.setParameter("productPlanName", purchaseOrder.getProductPlanName());
        q.setParameter("latestStatusDate", purchaseOrder.getLatestStatusDate());
        q.setParameter("latestDelegateToRole", purchaseOrder.getLatestDelegateToRole());
        q.setParameter("latestDelegateFromRole", purchaseOrder.getLatestDelegateFromRole());
        q.setParameter("latestStatusBy", purchaseOrder.getLatestStatusBy());
        q.setParameter("latestStatusByUser", purchaseOrder.getLatestStatusByUser());

        q.executeUpdate();

        for (PurchaseOrderRegister regDetail : regDetails) {
            try {
                em.merge(regDetail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void destroyPurchseOrderRegister(Long id) throws NonexistentEntityException {
        PurchaseOrderRegister purchaseOrderRegister;
        try {
            purchaseOrderRegister = em.getReference(PurchaseOrderRegister.class, id);
            purchaseOrderRegister.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The purchaseOrderRegister with id " + id + " no longer exists.", enfe);
        }
        em.remove(purchaseOrderRegister);
    }
    /*
     public void destroyPurchseOrderQuestionnaire(Integer id) throws NonexistentEntityException {
     PurchaseOrderQuestionaire purchaseOrderQuestionaire;
     try {
     purchaseOrderQuestionaire = em.getReference(PurchaseOrderQuestionaire.class, id);
     purchaseOrderQuestionaire.getId();
     } catch (EntityNotFoundException enfe) {
     throw new NonexistentEntityException("The purchaseOrderQuestionnaire with id " + id + " no longer exists.", enfe);
     }
     em.remove(purchaseOrderQuestionaire);
     }*/

    public Collection<PurchaseOrder> findByAssignmentDetail(Integer assignmentDetailId) {
        Query q = em.createQuery("select object(o) from PurchaseOrder as o where o.assignmentDetail.id = :assignmentDetailId order by o.purchaseDate");
        q.setParameter("assignmentDetailId", assignmentDetailId);

        Collection<PurchaseOrder> list = q.getResultList();

        return list;
    }

    public void editSaleResult(PurchaseOrder po) {
        Date today = new Date();
        Query q = em.createQuery("update PurchaseOrder po set "
                + "po.saleResult = :saleResult, "
                + "po.nosaleReason = :nosaleReason, "
                + "po.followupsaleReason = :followupsaleReason, "
                + "po.followupsaleDate = :followupsaleDate, "
                //                + "po.purchaseDate = :purchaseDate, "
                + "po.approvalStatus = :approvalStatus, "
                + "po.paymentStatus = :paymentStatus, "
                + "po.updateDate = :updateDate, "
                + "po.updateBy = :updateBy, "
                + "po.updateByUser = :updateByUser "
                + "where po.id = :poid ");
        q.setParameter("saleResult", po.getSaleResult());
        q.setParameter("nosaleReason", po.getNosaleReason());
        q.setParameter("followupsaleReason", po.getFollowupsaleReason());
        q.setParameter("followupsaleDate", po.getFollowupsaleDate());
//        q.setParameter("purchaseDate", today);
        q.setParameter("approvalStatus", po.getApprovalStatus());
        q.setParameter("paymentStatus", po.getPaymentStatus());
        q.setParameter("updateDate", today);
        q.setParameter("updateBy", JSFUtil.getUserSession().getUserName());
        q.setParameter("updateByUser", JSFUtil.getUserSession().getUsers());
        q.setParameter("poid", po.getId());

        q.executeUpdate();
    }

    public List findByCustomerUser(Integer customerId, Integer userId) {
        Query q = em.createQuery("select ad.marketingCode"
                + ", po.refNo"
                + ", po.purchaseDate "
                + ", po.id "
                + "from PurchaseOrder as po "
                + "left join po.assignmentDetail ad "
                //+ "where po.createByUser.id = :userId "
                + "where po.customer.id = :customerId "
                + "and po.saleResult = 'Y' "
                + "and po.approvalStatus = 'approved' "
                + "order by po.purchaseDate desc");
        //q.setParameter("userId", userId);
        q.setParameter("customerId", customerId);
        q.setMaxResults(5);

        List list = q.getResultList();

        return list;
    }

    public List<PurchaseOrderDetail> findPurchaseOrderDetailByPurchaseOrderId(Integer poId) {
        Query q = em.createQuery("select object(o) from PurchaseOrderDetail as o where o.purchaseOrder.id = :poId order by o.id");
        q.setParameter("poId", poId);

        List<PurchaseOrderDetail> list = q.getResultList();

        return list;
    }

    public List<TempPurchaseOrderDetail> findTpodByPoId(Integer poId) {
        Query q = em.createQuery("select object(o) from TempPurchaseOrderDetail as o where o.tempPurchaseOrder.id = :poId order by o.id");
        q.setParameter("poId", poId);

        List<TempPurchaseOrderDetail> list = q.getResultList();

        return list;
    }

    public List<PurchaseOrderInstallment> findInstallment(Integer poId) {
        Query q = em.createQuery("select object(o) from PurchaseOrderInstallment o"
                + " where o.purchaseOrder.id = :poId order by o.installmentNo");
        q.setParameter("poId", poId);

        List<PurchaseOrderInstallment> list = null;
        try {
            list = q.getResultList();
        } catch (Exception e) {
            list = null;
        }

        return list;
    }

    public void deletePurchaseOrderInstallment(Integer purchaseId) {
        Query q = em.createQuery("Delete from PurchaseOrderInstallment poi where poi.purchaseOrder.id = ?1");
        q.setParameter(1, purchaseId);
        q.executeUpdate();
    }

    public PurchaseOrder findByRefNo(String refNo) {
        Query q = em.createQuery("select object(po) from PurchaseOrder po"
                + " where po.refNo = :refNo");
        q.setParameter("refNo", refNo);

        PurchaseOrder po = null;
        try {
            po = (PurchaseOrder) q.getSingleResult();
        } catch (Exception e) {
            System.out.println("Duplicate refno : " + refNo);
            e.printStackTrace();
            po = null;
        }

        return po;
    }

    public List<SaleApprovalValue> findSaleApproval(String refNo, String policyNo, Integer campaignId, Date fromDate, Date toDate, String qcStatus, String paymentStatus, String uwStatus, String confirmStatus, Integer userId, String idcard, Integer userGroupId, Integer productId, String customerName, Integer paymentMethodId, String orderBy, String orderType, int firstResult, int maxResults) {
        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
        SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59", Locale.US);
        List<SaleApprovalValue> list = new ArrayList<SaleApprovalValue>();
        if (confirmStatus == null) {
            confirmStatus = "";
        }
        try {
            String select = "select new " + SaleApprovalValue.class.getName()
                    + "("
                    + " po.id"//1
                    + " ,coalesce(po.refNo,'')"//2
                    + " ,coalesce(po.refNo2,'')"//3
                    + " ,cust.id"//4
                    + " ,cust.name + ' ' + coalesce(cust.surname,'')"//5
                    + " ,coalesce(ad.campaignName,'')"//6
                    + " ,coalesce(po.purchaseDate,'')"//7
                    + " ,coalesce(createByUser.name,'')"//8
                    + " ,coalesce(po.approvalStatus,'')"//9
                    + " ,coalesce(po.paymentStatus,'')"//10
                    + " ,coalesce(po.qcStatus,'')"//11
                    + " ,coalesce(updateByUser.name,'')"//12
                    + " ,coalesce(poDetail.product.name,'')"//13
                    + " ,coalesce(poDetail.productPlan.name,'')"//14
                    + " ,coalesce(po.approveBy,'')"//15
                    + " ,coalesce(po.qcBy,'')"//16
                    + " ,coalesce(po.paymentBy,'')"//17
                    + " ,coalesce(po.insurePerson,'')"//18
                    + " ,coalesce(po.policyNo,'')"//19
                    + " ,po.issueDate"//20
                    + " ,coalesce(po.reason,'')"//21
                    + " ,coalesce(po.reasonCode,'')"//22
                    + " ,coalesce(po.approvalSupStatus,'')"//23
                    + " ,coalesce(po.approveSupBy,'')"//24
                    + " ,coalesce(po.confirmFlag,0)"//25
                    + " ,po"//26
                    + " ,po.paymentMethodName "//27
                    + " ,po.mainPoId "//28
                    + " ,poDetail.product.familyProduct"//29
                    + " ,coalesce(poReg.relationMainInsured,'')"//30
                    + " ,po.fx2 " //31
                    + ")"
                    + " from PurchaseOrder po join po.customer cust "
                    + " left join po.createByUser createByUser "
                    + " left join po.updateByUser updateByUser "
                    + " join po.assignmentDetail ad "
                    + " join po.purchaseOrderDetailCollection poDetail"
                    + " join poDetail.purchaseOrderRegisterCollection poReg";
            String where = " where po.saleResult = 'Y'";//reg.no = 1 and 
            if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Supervisor")) {
                where += " and createByUser.userGroup.id IN (SELECT g.id FROM UserGroup g WHERE "
                        + "(g.role like '%Agent%' AND g.parentId = " + JSFUtil.getUserSession().getUserGroup().getId() + "))";
            }
            if (fromDate != null) {
                where += " and po.purchaseDate >= '" + sdfFrom.format(fromDate) + "'";
            }
            if (toDate != null) {
                where += " and po.purchaseDate <= '" + sdfTo.format(toDate) + "'";
            }
            if (!refNo.equals("")) {
                where += " and po.refNo like '%" + refNo + "%'";
            }
            if (!policyNo.equals("")) {
                where += " and po.policyNo like '%" + policyNo + "%'";
            }
            if (campaignId != null && campaignId != 0) {
                where += " and ad.assignmentId.campaign.id = " + campaignId;
            }
            if (!qcStatus.equals("0")) {
                if (qcStatus.equals("both")) {
                    where += " and (po.qcStatus = 'pending' or po.qcStatus = 'waiting')";
                } else {
                    where += " and po.qcStatus = '" + qcStatus + "'";
                }
            }
            if (!paymentStatus.equals("0")) {
                if (qcStatus.equals("both") && paymentStatus.equals("both")) {
                    where += " and (po.paymentStatus = 'pending' or po.paymentStatus = 'waiting')";
                } else if (paymentStatus.equals("both")) {
                    where += " and (po.paymentStatus = 'pending' or po.paymentStatus = 'waiting')";
                } else {
                    where += " and po.paymentStatus = '" + paymentStatus + "'";
                }
            }
            if (!uwStatus.equals("0")) {
                if ((qcStatus.equals("both") || paymentStatus.equals("both")) && uwStatus.equals("both")) {
                    where += " and (po.approvalStatus = 'pending' or po.approvalStatus = 'waiting')";
                } else if (uwStatus.equals("both")) {
                    where += " and (po.approvalStatus = 'pending' or po.approvalStatus = 'waiting')";
                } else {
                    where += " and po.approvalStatus = '" + uwStatus + "'";
                }
            }

            if (userGroupId != null && userGroupId != 0) {
                where += " and createByUser.userGroup.id = " + userGroupId;
            }
            if (userId != null && userId != 0) {
                where += " and createByUser.id = " + userId;
            }

            if (!idcard.equals("")) {
                where += " and po.idno like '%" + idcard + "%'";
            }
            if (!customerName.equals("")) {
                if (customerName.trim().indexOf(" ") != -1) {
                    String name = customerName.substring(0, customerName.indexOf(" "));
                    String surname = customerName.substring(customerName.indexOf(" ") + 1, customerName.length());
                    where += " and ((cust.name like '%" + name.trim() + "%' and cust.surname like '%" + surname.trim() + "%') or (po.insurePerson like '%" + name.trim() + "%' and po.insurePerson like '%" + surname.trim() + "%'))";
                } else {
                    where += " and (cust.name like '%" + customerName.trim() + "%' or cust.surname like '%" + customerName.trim() + "%' or po.insurePerson like '%" + customerName.trim() + "%')";
                }
            }
            if (productId != null && productId != 0) {
                where += " and poDetail.product.id = " + productId;
            }
            if (uwStatus.equals("0") && paymentStatus.equals("0") && qcStatus.equals("0")) {
                where += " and ((po.approvalStatus <> 'rejected') or (po.paymentStatus <> 'rejected') or (po.qcStatus <> 'rejected'))";
                if (confirmStatus.equals("1")) {
                    where += " and (po.confirmFlag = true and po.reasonCode = 'IF')";
                } else if (confirmStatus.equals("0")) {
                    where += " and (po.confirmFlag = false and po.reasonCode = 'IF')";
                } else {
                    where += " and ((po.approvalStatus <> 'approved') or (po.paymentStatus <> 'approved') or (po.qcStatus <> 'approved'))";
                }
            } else if (!uwStatus.equals("approved") && !paymentStatus.equals("approved") && !qcStatus.equals("approved")) {
                where += " and ((po.approvalStatus <> 'approved') or (po.paymentStatus <> 'approved') or (po.qcStatus <> 'approved'))";
                if (confirmStatus.equals("1")) {
                    where += " and (po.confirmFlag = true and po.reasonCode = 'IF')";
                } else if (confirmStatus.equals("0")) {
                    where += " and (po.confirmFlag = false and po.reasonCode = 'IF')";
                }
            } else {
                if (confirmStatus.equals("1")) {
                    where += " and (po.confirmFlag = true and po.reasonCode = 'IF')";
                } else if (confirmStatus.equals("0")) {
                    where += " and (po.confirmFlag = false and po.reasonCode = 'IF')";
                }
            }
            if (paymentMethodId != null && paymentMethodId != 0) {
                where += " and po.paymentMethod = " + paymentMethodId;
            }

            String order = " order by " + orderBy + " " + orderType; // order by po.refNo, po.purchaseDate

            String sql = select + where + order;

            Query q = em.createQuery(sql);
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
            list = (List<SaleApprovalValue>) q.getResultList();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer countSaleApproval(String refNo, String policyNo, Integer campaignId, Date fromDate, Date toDate, String qcStatus, String paymentStatus, String uwStatus, String confirmStatus, Integer userId, String idcard, Integer userGroupId, Integer productId, String customerName, Integer paymentMethodId) {//, String orderBy, String orderType) {

        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
        SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59", Locale.US);
        List<SaleApprovalValue> list = new ArrayList<SaleApprovalValue>();
        if (confirmStatus == null) {
            confirmStatus = "";
        }
        try {
            String select = "select count(po) "
                    + " from PurchaseOrder po join po.customer cust "
                    + " left join po.createByUser createByUser "
                    + " left join po.updateByUser updateByUser "
                    + " join po.assignmentDetail ad "
                    + " join po.purchaseOrderDetailCollection poDetail";
            String where = " where po.saleResult = 'Y'"; //reg.no = 1 and
            if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Supervisor")) {
                where += " and createByUser.userGroup.id IN (SELECT g.id FROM UserGroup g WHERE "
                        + "(g.role like '%Agent%' AND g.parentId = " + JSFUtil.getUserSession().getUserGroup().getId() + "))";
            }
            if (fromDate != null) {
                where += " and po.purchaseDate >= '" + sdfFrom.format(fromDate) + "'";
            }
            if (toDate != null) {
                where += " and po.purchaseDate <= '" + sdfTo.format(toDate) + "'";
            }
            if (!refNo.equals("")) {
                where += " and po.refNo like '%" + refNo + "%'";
            }
            if (!policyNo.equals("")) {
                where += " and po.policyNo like '%" + policyNo + "%'";
            }
            if (campaignId != null && campaignId != 0) {
                where += " and ad.assignmentId.campaign.id = " + campaignId;
            }
            if (!qcStatus.equals("0")) {
                if (qcStatus.equals("both")) {
                    where += " and (po.qcStatus = 'pending' or po.qcStatus = 'waiting')";
                } else {
                    where += " and po.qcStatus = '" + qcStatus + "'";
                }
            }
            if (!paymentStatus.equals("0")) {
                if (qcStatus.equals("both") && paymentStatus.equals("both")) {
                    where += " and (po.paymentStatus = 'pending' or po.paymentStatus = 'waiting')";
                } else if (paymentStatus.equals("both")) {
                    where += " and (po.paymentStatus = 'pending' or po.paymentStatus = 'waiting')";
                } else {
                    where += " and po.paymentStatus = '" + paymentStatus + "'";
                }
            }
            if (!uwStatus.equals("0")) {
                if ((qcStatus.equals("both") || paymentStatus.equals("both")) && uwStatus.equals("both")) {
                    where += " and (po.approvalStatus = 'pending' or po.approvalStatus = 'waiting')";
                } else if (uwStatus.equals("both")) {
                    where += " and (po.approvalStatus = 'pending' or po.approvalStatus = 'waiting')";
                } else {
                    where += " and po.approvalStatus = '" + uwStatus + "'";
                }
            }

            if (userGroupId != null && userGroupId != 0) {
                where += " and createByUser.userGroup.id = " + userGroupId;
            }
            if (userId != null && userId != 0) {
                where += " and createByUser.id = " + userId;
            }
            if (!idcard.equals("")) {
                where += " and po.idno like '%" + idcard + "%'";
            }
            if (!customerName.equals("")) {
                if (customerName.trim().indexOf(" ") != -1) {
                    String name = customerName.substring(0, customerName.indexOf(" "));
                    String surname = customerName.substring(customerName.indexOf(" ") + 1, customerName.length());
                    where += " and ((cust.name like '%" + name.trim() + "%' and cust.surname like '%" + surname.trim() + "%') or (po.insurePerson like '%" + name.trim() + "%' and po.insurePerson like '%" + surname.trim() + "%'))";
                } else {
                    where += " and (cust.name like '%" + customerName.trim() + "%' or cust.surname like '%" + customerName.trim() + "%' or po.insurePerson like '%" + customerName.trim() + "%')";
                }
            }
            if (productId != null && productId != 0) {
                where += " and poDetail.product.id = " + productId;
            }
            if (uwStatus.equals("0") && paymentStatus.equals("0") && qcStatus.equals("0")) {
                where += " and ((po.approvalStatus <> 'rejected') or (po.paymentStatus <> 'rejected') or (po.qcStatus <> 'rejected'))";
                if (confirmStatus.equals("1")) {
                    where += " and (po.confirmFlag = true and po.reasonCode = 'IF')";
                } else if (confirmStatus.equals("0")) {
                    where += " and (po.confirmFlag = false and po.reasonCode = 'IF')";
                } else {
                    where += " and ((po.approvalStatus <> 'approved') or (po.paymentStatus <> 'approved') or (po.qcStatus <> 'approved'))";
                }
            } else if (!uwStatus.equals("approved") && !paymentStatus.equals("approved") && !qcStatus.equals("approved")) {
                where += " and ((po.approvalStatus <> 'approved') or (po.paymentStatus <> 'approved') or (po.qcStatus <> 'approved'))";
                if (confirmStatus.equals("1")) {
                    where += " and (po.confirmFlag = true and po.reasonCode = 'IF')";
                } else if (confirmStatus.equals("0")) {
                    where += " and (po.confirmFlag = false and po.reasonCode = 'IF')";
                }
            } else {
                if (confirmStatus.equals("1")) {
                    where += " and (po.confirmFlag = true and po.reasonCode = 'IF')";
                } else if (confirmStatus.equals("0")) {
                    where += " and (po.confirmFlag = false and po.reasonCode = 'IF')";
                }
            }
            if (paymentMethodId != null && paymentMethodId != 0) {
                where += " and po.paymentMethod = " + paymentMethodId;
            }

            String sql = select + where;
            Query q = em.createQuery(sql);
            return Integer.valueOf(((Long) q.getSingleResult()).intValue());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public List<SaleApprovalValue> findSaleApproval(String refNo, Integer campaignId, Date fromDate, Date toDate, String qcStatus, String paymentStatus, String uwStatus
//            , Integer userId, String idcard, Integer userGroupId, Integer productId, String customerName, String orderBy, String orderType) {        
//        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
//        SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59", Locale.US);
//        try{
//            String select = "select new " + SaleApprovalValue.class.getName() 
//                    + "("
//                    + " po.id"//1
//                    + " , coalesce(po.refNo,'')"//2
//                    + " , coalesce(po.refNo2,'')"//3
//                    + " , cust.id"//4
//                    + " , cust.name + ' ' + coalesce(cust.surname,'')"//5
//                    + " , coalesce(ad.campaignName,'')"//6
//                    + " , coalesce(po.purchaseDate,'')"//7
//                    + " , coalesce(createByUser.name,'')"//8
//                    + " , coalesce(po.approvalStatus,'')"//9
//                    + " , coalesce(po.paymentStatus,'')"//10
//                    + " , coalesce(po.qcStatus,'')"//11
//                    + " , coalesce(updateByUser.name,'')"//12
//                    + " , coalesce(poDetail.product.name,'')"//13
//                    + " , coalesce(po.approveBy,'')"//14
//                    + " , coalesce(po.qcBy,'')"//15
//                    + " , coalesce(po.paymentBy,'')"//16
//                    + " , po"//17
//                    + " , '' "//18
//                    + " ) "
//                    + " from PurchaseOrder po join po.customer cust "
//                    + " left join po.createByUser createByUser "
//                    + " left join po.updateByUser updateByUser "
//                    + " join po.assignmentDetail ad "
//                    + " join po.purchaseOrderDetailCollection poDetail"
//                    + " join poDetail.purchaseOrderRegisterCollection reg";
//            String where = " where reg.no = 1 and po.saleResult = 'Y'";
//            if(fromDate != null){
//                where += " and po.purchaseDate >= '" + sdfFrom.format(fromDate) +"'";
//            }
//            if(toDate != null){
//                where += " and po.purchaseDate <= '" + sdfTo.format(toDate) +"'";
//            }
//            if(!refNo.equals("")){
//                where += " and po.refNo like '%" + refNo + "%'";
//            }
//            if(campaignId != null && campaignId != 0){
//                where += " and ad.assignmentId.campaign.id = " + campaignId;
//            }
//            if(!qcStatus.equals("0")){
//                if(qcStatus.equals("both")) {
//                    where += " and (po.qcStatus = 'pending' or po.qcStatus = 'waiting')";
//                } else {
//                    where += " and po.qcStatus = '" + qcStatus + "'";
//                }
//            }
//            if(!paymentStatus.equals("0")){
//                if(qcStatus.equals("both") && paymentStatus.equals("both")) {
//                    where += " and (po.paymentStatus = 'pending' or po.paymentStatus = 'waiting')";
//                } else if(paymentStatus.equals("both")) {
//                    where += " and (po.paymentStatus = 'pending' or po.paymentStatus = 'waiting')";
//                } else {
//                    where += " and po.paymentStatus = '" + paymentStatus + "'";
//                }
//            }
//            if(!uwStatus.equals("0")){
//                if((qcStatus.equals("both") || paymentStatus.equals("both")) && uwStatus.equals("both")) {
//                    where += " and (po.approvalStatus = 'pending' or po.approvalStatus = 'waiting')";
//                } else if(uwStatus.equals("both")) {
//                    where += " and (po.approvalStatus = 'pending' or po.approvalStatus = 'waiting')";
//                } else {
//                    where += " and po.approvalStatus = '" + uwStatus + "'";
//                }
//            }
//            if(userGroupId != null && userGroupId != 0) {                
//                where += " and createByUser.userGroup.id = " + userGroupId;    
//            }
//            if(userId != null && userId != 0){
//                where += " and createByUser.id = " + userId;
//            }    
//            if(!idcard.equals("")) {
//                where += " and reg.idno like '%" + idcard + "%'";
//            }
//            if(!customerName.equals("")) {
//                if(customerName.trim().indexOf(" ") != -1){
//                    String name = customerName.substring(0, customerName.indexOf(" "));
//                    String surname = customerName.substring(customerName.indexOf(" ") + 1, customerName.length());
//                    where += " and ((cust.name like '%" + name.trim() + "%' and cust.surname like '%" + surname.trim() + "%') or (reg.name like '%" + name.trim() + "%' and reg.surname like '%" + surname.trim() + "%'))";
//                }else{
//                    where += " and (cust.name like '%" + customerName.trim() + "%' or cust.surname like '%" + customerName.trim() + "%' or reg.name like '%" + customerName.trim() + "%' or reg.surname like '%" + customerName.trim() + "%')";
//            }
//                
//            }
//            if(productId != null && productId != 0) {
//                where += " and poDetail.product.id = " + productId;
//            }
//            if(!uwStatus.equals("approved") && !paymentStatus.equals("approved") && !qcStatus.equals("approved")) {
//                where += " and ((po.approvalStatus not like 'approved') or (po.paymentStatus not like 'approved') or (po.qcStatus not like 'approved'))";
//            }
//            
//            String order = " order by " + orderBy + " " + orderType; // order by po.refNo, po.purchaseDate 
//
//            String sql = select + where + order;
//
//            Query q = em.createQuery(sql);
//            return q.getResultList();
//        }catch(Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
    public List<PendingListInfoValue> findPending(String subPage, Users users, Integer firstResult, Integer maxResults, String other_where) {
        return findPending(subPage, users, firstResult, maxResults, other_where, null);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
    public List<PendingListInfoValue> findPending(String subPage, Users users, Integer firstResult, Integer maxResults, String other_where, String listType) {
        List<PendingListInfoValue> list = null;
        try {
            String select = "select new " + PendingListInfoValue.class.getName() + "(po) ";
            String order = " order by po.updateDate desc";

            //BEGIN : 2015/11/13 Feature => for pending list , additional condition for ordering data
            if (listType != null && "pendingList".equalsIgnoreCase(listType)) {
                boolean isAgentOrSup = false;
                if (users != null && users.getUserGroup() != null && users.getUserGroup().getRole() != null) {
                    String roleListStr = users.getUserGroup().getRole().toUpperCase();
                    isAgentOrSup = roleListStr.contains("AGENT") || roleListStr.contains("SUPERVISOR");
                }
                if (isAgentOrSup) {
                    order = " order by "
                            + "( CASE WHEN po.latestDelegateFromRole!='Agent' AND po.latestDelegateToRole='Agent' THEN 1 \n"
                            + "       WHEN po.latestDelegateFromRole!='Agent' THEN 2 \n"
                            + "       WHEN po.latestDelegateFromRole ='Agent' THEN 3 ELSE 4 END ),"
                            + "po.updateDate desc";
                }
            }
            //END : 2015/11/13 

            String sql = select + genPendingSQL(subPage, other_where, users) + order;
            Query q = em.createQuery(sql);
            if (users.getUserGroup().getRole().contains("Supervisor")) {
                q.setParameter("userGroupId", users.getUserGroup().getId());
            }
            if (users.getUserGroup().getRole().contains("Agent")) {
                q.setParameter("userId", users.getId());
            }

            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
            list = q.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
    public Integer countPending(String subPage, Users users, String other_where) {
        Integer count = 0;
        try {

            String select = "select count(po.id)";

            String sql = select + genPendingSQL(subPage, other_where, users);
            Query q = em.createQuery(sql);
            if (users.getUserGroup().getRole().contains("Supervisor")) {
                q.setParameter("userGroupId", users.getUserGroup().getId());
            }
            if (users.getUserGroup().getRole().contains("Agent")) {
                q.setParameter("userId", users.getId());
            }
            count = ((Long) q.getSingleResult()).intValue();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return count;
    }

    private String genPendingSQL(String subPage, String other_where, Users users) {

        String from = " from PurchaseOrder po";

        String where = " where po.saleResult = 'Y'";

        String strRole = "";
        if (users.getUserGroup().getRole().contains("Supervisor")
                || users.getUserGroup().getRole().contains("Agent")
                || users.getUserGroup().getRole().contains("Confirm")) {
            where += " and (";
            if (users.getUserGroup().getRole().contains("Supervisor")) {
                strRole += " po.createByUser.userGroup.parentId = :userGroupId";
            }
            if (users.getUserGroup().getRole().contains("Agent")) {
                if (!strRole.isEmpty()) {
                    strRole = strRole + " or";
                }
                strRole += " po.createByUser.id = :userId";
            }
            if (users.getUserGroup().getRole().contains("Confirm")) {
                if (!strRole.isEmpty()) {
                    strRole = strRole + " or";
                }
                strRole += " (UPPER(po.reasonCode) = 'IF' and po.confirmFlag = false and po.approvalStatus = 'approved' and po.paymentStatus = 'approved' and po.qcStatus = 'approved')";
            }

            where += strRole + ")";
        }

        if (subPage.equals("reject")) {
            where += " and (po.approvalStatus = 'rejected' or po.paymentStatus = 'rejected' or po.qcStatus = 'rejected')";
        } else if (subPage.equals("uw")) {
            where += " and (po.approvalStatus = 'waiting' or po.approvalStatus = 'pending' or po.approvalStatus = 'reconfirm' or po.approvalStatus = 'request')";
        } else if (subPage.equals("payment")) {
            where += " and po.approvalStatus = 'approved' and (po.paymentStatus = 'waiting' or po.paymentStatus = 'pending' or po.paymentStatus = 'reconfirm' or po.paymentStatus = 'request')";
        } else if (subPage.equals("qc")) {
            where += " and po.approvalStatus = 'approved' and po.paymentStatus = 'approved' and (po.qcStatus = 'waiting' or po.qcStatus = 'pending' or po.qcStatus = 'reconfirm' or po.qcStatus = 'request')";
        } else if (subPage.equals("confirm")) {
            where += " and UPPER(po.reasonCode) = 'IF' and po.confirmFlag = false and po.approvalStatus = 'approved' and po.paymentStatus = 'approved' and po.qcStatus = 'approved'";
        } else {
            where += " and (po.approvalStatus = 'waiting' or po.paymentStatus = 'waiting' or po.qcStatus = 'waiting' "
                    + "  or po.approvalStatus = 'pending' or po.paymentStatus = 'pending' or po.qcStatus = 'pending'"
                    + "  or po.approvalStatus = 'request' or po.paymentStatus = 'request' or po.qcStatus = 'request'"
                    + "  or po.approvalStatus = 'reconfirm' or po.paymentStatus = 'reconfirm' or po.qcStatus = 'reconfirm'"
                    + "  or po.approvalStatus = 'resend' or po.paymentStatus = 'resend' or po.qcStatus = 'resend'"
                    + "  or (po.approvalStatus = 'rejected' and (po.approveDate between DATEADD(d, -" + JSFUtil.getApplication().getCloseAssignmentList() + ", DATEDIFF(d, 0, GETDATE())) and DATEADD(d, 1, DATEDIFF(d, 0, GETDATE()))))"
                    + "  or (po.paymentStatus = 'rejected' and (po.paymentDate between DATEADD(d, -" + JSFUtil.getApplication().getCloseAssignmentList() + ", DATEDIFF(d, 0, GETDATE())) and DATEADD(d, 1, DATEDIFF(d, 0, GETDATE()))))"
                    + "  or (po.qcStatus = 'rejected' and (po.qcDate between DATEADD(d, -" + JSFUtil.getApplication().getCloseAssignmentList() + ", DATEDIFF(d, 0, GETDATE())) and DATEADD(d, 1, DATEDIFF(d, 0, GETDATE()))))"
                    + "  or (UPPER(po.reasonCode) = 'IF' and po.confirmFlag = false)) ";
        }

        where += " " + other_where;
        // where += " and po.id.customer_id like '%สามชาย%' ";

        String sql = from + where;

        return sql;
    }

    public List<PurchaseOrderApprovalLog> findApprovalLog(Integer poId) {
        List<PurchaseOrderApprovalLog> list = null;
        try {

            String select = "select object(poa)";

            String from = " from PurchaseOrderApprovalLog poa";

            String where = " where poa.purchaseOrder.id = ?1";

            String order = " order by poa.id desc";

            String sql = select + from + where + order;
            Query q = em.createQuery(sql);
            q.setParameter(1, poId);

            list = q.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public PurchaseOrderApprovalLog findApprovalLogLatest(Integer poId) {
        PurchaseOrderApprovalLog poal = null;
        try {

            String select = "select object(poa)";

            String from = " from PurchaseOrderApprovalLog poa";

            String where = " where poa.purchaseOrder.id = ?1";

            String order = " order by poa.id desc";

            String sql = select + from + where + order;
            Query q = em.createQuery(sql);
            q.setParameter(1, poId);

            poal = (PurchaseOrderApprovalLog) q.getResultList().get(0);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return poal;
    }

    public void saveNoInstallment(PurchaseOrder po) {
        Query q = em.createQuery("update PurchaseOrder po set "
                + "po.noInstallment = :noInstallment "
                + ", po.dueDate = :dueDate "
                + ", po.settlement = :settlement "
                + ", po.settlementBy = :settlementBy "
                + ", po.settlementDate = :settlementDate "
                + ", po.paymentStatus = :paymentStatus "
                + ", po.paymentBy = :paymentBy "
                + ", po.paymentByUser = :paymentByUser "
                + ", po.paymentDate = :paymentDate "
                + "where po.id = :poId ");
        q.setParameter("noInstallment", po.getNoInstallment());
        q.setParameter("dueDate", po.getDueDate());
        q.setParameter("settlement", po.getSettlement());
        q.setParameter("settlementBy", po.getSettlementBy());
        q.setParameter("settlementDate", po.getSettlementDate());
        q.setParameter("paymentStatus", po.getPaymentStatus());
        q.setParameter("paymentBy", po.getPaymentBy());
        q.setParameter("paymentByUser", po.getPaymentByUser());
        q.setParameter("paymentDate", po.getPaymentDate());
        q.setParameter("poId", po.getId());

        q.executeUpdate();
    }

    public List<PurchaseOrder> findPurchaseOrderByPaymentStatus() {
        Query q = em.createQuery("select object(o) from PurchaseOrder as o where paymentStatus = 'approved' and saleResult='Y' order by purchaseDate ");
        //select * from purchase_order po join purchase_order_installment poi on po.id = poi.purchase_order_id where po.payment_status = 'approved'
        //Query q = em.createQuery("select object(o) from PurchaseOrderInstallment as o join o.purchaseOrder po where po.paymentStatus = 'approved' and po.saleResult='Y' order by po.purchaseDate ");
        return q.getResultList();
    }

    public PurchaseOrderInstallment findPurchaseOrderInstallmentSettlement(String refNo, Double amount) {
        Query qCnt = em.createQuery("select count(o) from PurchaseOrderInstallment as o "
                + "join o.paymentMethod p "
                + "join o.purchaseOrder po "
                + "join po.purchaseOrderDetailCollection pod "
                + "join pod.purchaseOrderRegisterCollection por "
                + "where p.online = false and po.saleResult = 'Y' and o.paymentStatus like 'notpaid' "
                + "and por.no = 1 and por.idno = ?1 and o.installmentAmount = ?2 ");
        qCnt.setParameter(1, refNo);
        qCnt.setParameter(2, BigDecimal.valueOf(amount));
        Integer cntDup = ((Long) qCnt.getSingleResult()).intValue();

        if (cntDup > 1) { //many id card in list
            return null;
        } else {
            Query q = null;
            if (cntDup == 1) {  //found id card match 1 record
                q = em.createQuery("select object(o) from PurchaseOrderInstallment as o "
                        + "join o.paymentMethod p "
                        + "join o.purchaseOrder po "
                        + "join po.purchaseOrderDetailCollection pod "
                        + "join pod.purchaseOrderRegisterCollection por "
                        + "where p.online = false and po.saleResult = 'Y' and o.paymentStatus like 'notpaid' "
                        + "and por.no = 1 and por.idno = ?1 and o.installmentAmount = ?2 "
                        + "order by po.purchaseDate, o.installmentNo, o.id");
            } else if (cntDup == 0) {  //find poRefNo
                q = em.createQuery("select object(o) from PurchaseOrderInstallment as o "
                        + "join o.paymentMethod p "
                        + "join o.purchaseOrder po "
                        + "join po.purchaseOrderDetailCollection pod "
                        + "join pod.purchaseOrderRegisterCollection por "
                        + "where p.online = false and po.saleResult = 'Y' and o.paymentStatus like 'notpaid' "
                        + "and por.no = 1 and po.refNo = ?1 and o.installmentAmount = ?2 "
                        + "order by po.purchaseDate, o.installmentNo, o.id");
            }
            q.setParameter(1, refNo);
            q.setParameter(2, BigDecimal.valueOf(amount));
            q.setMaxResults(1);
            try {
                return (PurchaseOrderInstallment) q.getSingleResult();
            } catch (Exception e) {
                return null;
            }
        }
    }

    public PurchaseOrderInstallment findPOISettlementMail(String refNo, Double amount, Date purchaseDate) {
        Query q = em.createQuery("select object(o) from PurchaseOrderInstallment as o "
                + "join o.purchaseOrder po where o.installmentRefNo = ?1 and o.installmentAmount = ?2  "
                + "and CONVERT(VARCHAR(10),po.purchaseDate, 120) = CONVERT(VARCHAR(10), ?3, 120)");
        q.setParameter(1, refNo);
        q.setParameter(2, BigDecimal.valueOf(amount));
        q.setParameter(3, purchaseDate);
        q.setMaxResults(1);
        try {
            return (PurchaseOrderInstallment) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void updatePaymentStatusInstallmentById(Integer id, String refNo) throws NonexistentEntityException, Exception {
        Date dateNow = new Date();
        String userName = JSFUtil.getUserSession().getUserName();
        Query q = em.createQuery("update PurchaseOrderInstallment poi set poi.paymentStatus = :paymentStatus, poi.paidDate = :paidDate, poi.refNo = :refNo, "
                + "poi.settlement = :settlement, poi.settlementDate = :settlementDate, poi.settlementBy = :settlementBy where poi.id = :poiId");
        q.setParameter("paymentStatus", "paid");
        q.setParameter("paidDate", dateNow);
        q.setParameter("refNo", refNo);
        q.setParameter("poiId", id);
        q.setParameter("settlement", Boolean.TRUE);
        q.setParameter("settlementDate", dateNow);
        q.setParameter("settlementBy", userName);
        q.executeUpdate();
        approvePayment(id);
    }
    
    public void updateQaTransQc(PurchaseOrder purchaseOrder) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update PurchaseOrder po set po.qaTransQc = :qaTransQc, po.updateDate = :updateDate, po.updateBy = :updateBy, po.updateByUser = :updateByUser where po.id = :poId");
        q.setParameter("qaTransQc", purchaseOrder.getQaTransQc());
        q.setParameter("updateDate", new Date());
        q.setParameter("updateBy", JSFUtil.getUserSession().getUsers().getLoginName());
        q.setParameter("updateByUser", JSFUtil.getUserSession().getUsers());
        q.setParameter("poId", purchaseOrder.getId());
        
        q.executeUpdate();
    }

    public void approvePayment(Integer id) {
        Date dateNow = new Date();
        String userName = JSFUtil.getUserSession().getUsers().getLoginName();
        Query q = em.createQuery("select object(po) from PurchaseOrderInstallment as o "
                + "join o.purchaseOrder po where o.id = ?1 ");
        q.setParameter(1, id);
        PurchaseOrder po = (PurchaseOrder) q.getSingleResult();

        Query q2 = em.createQuery("select count(o) from PurchaseOrderInstallment as o "
                + "where o.purchaseOrder.id = ?2 and o.paymentStatus like 'notpaid'");
        q2.setParameter(2, po.getId());
        Integer cntPaid = ((Long) q2.getSingleResult()).intValue();

        if (cntPaid == 0) {
            Query q3 = em.createQuery("update PurchaseOrder po set po.paymentStatus = :paymentStatus, po.paymentDate = :paymentDate, "
                    + "po.paymentBy = :paymentBy, po.paymentByUser =:paymentByUser where po.id = :poId");
            q3.setParameter("paymentStatus", "approved");
            q3.setParameter("paymentDate", dateNow);
            q3.setParameter("paymentBy", userName);
            q3.setParameter("paymentByUser", JSFUtil.getUserSession().getUsers());
            q3.setParameter("poId", po.getId());
            q3.executeUpdate();

            PurchaseOrderApprovalLog approveLog = new PurchaseOrderApprovalLog();
            approveLog.setPurchaseOrder(po);
            approveLog.setToRole("");
            approveLog.setApprovalStatus("approved");
            approveLog.setUsers(JSFUtil.getUserSession().getUsers());
            approveLog.setCreateBy(userName);
            approveLog.setCreateDate(dateNow);
            approveLog.setCreateByRole("Payment");
            try {
                createApprovalLog(approveLog);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<PurchaseOrderInstallment> findPurchaseOrderByPurchaseDate(Date fromPurchaseDate, Date toPurchaseDate) {
        Query q = em.createQuery("select object(o) from PurchaseOrderInstallment as o "
                + "join o.purchaseOrder po "
                + "where (po.purchaseDate >= ?1 and po.purchaseDate <= ?2) "
                + "and po.saleResult = 'Y' and o.paymentStatus like '%notpaid%' order by po.refNo, po.purchaseDate ");
        q.setParameter(1, JSFUtil.toDateWithoutTime(fromPurchaseDate));
        q.setParameter(2, JSFUtil.toDateWithMaxTime(toPurchaseDate));

        return q.getResultList();
    }

    public List<ExportPaymentValue> findPurchaseOrderValueByPurchaseDate(Date fromPurchaseDate, Date toPurchaseDate) {
        String sql = "select NEW " + ExportPaymentValue.class.getName()
                + " (po.refNo, o.installmentAmount, po.purchaseDate, por.idno, por.initialLabel, por.name, por.surname, o.cardNo, o.cardHolderName, "
                + " o.cardExpiryMonth, o.cardExpiryYear, o.cardIssuerName, o.cardType)"
                + " from PurchaseOrderInstallment as o"
                + " join o.paymentMethod p"
                + " join o.purchaseOrder po"
                + " join po.purchaseOrderDetailCollection pod"
                + " join pod.purchaseOrderRegisterCollection por"
                + " where p.online = false and por.no = 1 and (po.purchaseDate >= ?1 and po.purchaseDate <= ?2)"
                + " and po.saleResult = 'Y' and o.paymentStatus like '%notpaid%' order by po.refNo, po.purchaseDate";
        Query q = em.createQuery(sql);
        q.setParameter(1, JSFUtil.toDateWithoutTime(fromPurchaseDate));
        q.setParameter(2, JSFUtil.toDateWithMaxTime(toPurchaseDate));

        return q.getResultList();
    }

    public List<ParameterMap> findYesSaleByCampaignToday() {
        Query q = em.createQuery("select NEW "
                + ParameterMap.class.getName()
                + "(c.name, count(distinct po.id )) "
                + "from PurchaseOrder po "
                //+ "join po.createByUser u  "
                + "join po.assignmentDetail ad  "
                + "join ad.assignmentId a "
                + "join a.campaign c  "
                + "where po.purchaseDate > ?1 and po.saleResult = 'Y' "
                + "group by c.name "
                + "order by c.name ");
        Date today = new Date();
        //q.setParameter(1, userGroupId);
        //q.setParameter(4, campaignId);
        q.setParameter(1, JSFUtil.toDateWithoutTime(today));
        //q.setParameter(2, JSFUtil.toDateWithMaxTime(today));
        return q.getResultList();
    }

    public List<ParameterMap> findYesSaleByUserToday() {
        Query q = em.createQuery("select NEW "
                + ParameterMap.class.getName()
                + "(u.loginName, count(distinct po.id )) "
                + "from PurchaseOrder po "
                + "join po.createByUser u  "
                + "join po.assignmentDetail ad  "
                + "join ad.assignmentId a "
                + "join a.campaign c  "
                + "where po.purchaseDate > ?1 and po.saleResult = 'Y' "
                + "group by u.loginName "
                + "order by u.loginName ");
        Date today = new Date();
        //q.setParameter(1, userGroupId);
        //q.setParameter(4, campaignId);
        q.setParameter(1, JSFUtil.toDateWithoutTime(today));
        //q.setParameter(2, JSFUtil.toDateWithMaxTime(today));
        return q.getResultList();
    }

    public List<SaleHistoryInfoValue> findSaleHistory(Integer customerId) {
        List<SaleHistoryInfoValue> list = null;
        try {
            String sql = "select NEW " + SaleHistoryInfoValue.class.getName()
                    + "( po )"
                    + " from PurchaseOrder po"
                    + " where po.customer.id = :customerId"
                    + " order by po.id desc";
            Query q = em.createQuery(sql);
            q.setParameter("customerId", customerId);
            list = (List<SaleHistoryInfoValue>) q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Double findMonthlyPremium(Users user) {
        try {
            Query q = em.createQuery("select sum(annualNetPremium) from PurchaseOrder po "
                    + "where po.saleResult = 'Y' and po.createByUser.id = ?1 and MONTH(po.purchaseDate) = MONTH(?2) "
                    + "group by po.createByUser.id ");
            q.setParameter(1, user.getId());
            q.setParameter(2, new Date());

            return (Double) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<SaleHistoryInfoValue> findYesPurchaseOrderHistory(Integer customerId) {
        List<SaleHistoryInfoValue> list = null;
        try {
            String sql = "select NEW " + SaleHistoryInfoValue.class.getName()
                    + "( po ) from PurchaseOrder po "
                    + "where po.saleResult='Y' and po.customer.id = :customerId "
                    + "order by po.purchaseDate desc";
            Query q = em.createQuery(sql);
            q.setParameter("customerId", customerId);
            list = (List<SaleHistoryInfoValue>) q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PurchaseOrder> findPurchaseOrderByCampaignAndUser(String campaignId, String userId) {

        String sql = "select object(po) from PurchaseOrder as po "
                + "join po.assignmentDetail ad ";

        String sqlWhere = "where ad.unassign is null and po.saleResult = 'Y' ";
        if (userId != null && !userId.equals("")) {
            sqlWhere += "and po.createByUser.id = " + userId + " ";
        }

        if (campaignId != null && !campaignId.equals("0")) {
            sqlWhere += "and ad.assignmentId.campaign.id = " + campaignId + " ";
        } else {
            sqlWhere += "and ad.assignmentId.campaign.status = 1 and ad.assignmentId.campaign.enable = 1 and (ad.assignmentId.campaign.startDate <= GETDATE() and ad.assignmentId.campaign.endDate >= GETDATE()) ";
        }

        String orderBy = "order by po.refNo, po.purchaseDate ";

        Query q = em.createQuery(sql + sqlWhere + orderBy);
        return (List<PurchaseOrder>) q.getResultList();
    }

    public List<PurchaseOrder> findPurchaseOrderByCampaignAndUser(String campaignId, String userId, String mode) {

        String sql = "select object(po) from PurchaseOrder as po "
                + "join po.assignmentDetail ad "
                + "join ad.assignmentId a ";

        String sqlWhere = "where ad.unassign is null and po.saleResult = 'Y' ";
        if (userId != null && !userId.equals("")) {
            sqlWhere += "and po.createByUser.id = " + userId + " ";
        }

        if (campaignId != null && !campaignId.equals("0")) {
            sqlWhere += "and a.campaign.id = " + campaignId + " ";
        } else {
            sqlWhere += "and a.campaign.status = 1 and a.campaign.enable = 1 and (a.campaign.startDate <= GETDATE() and a.campaign.endDate >= GETDATE()) ";
        }

        if (mode.equals("today")) {
            sqlWhere += "and ad.updateDate between ?1 and ?2 ";
        } else if (mode.equals("realtime")) {
            sqlWhere += "and a.marketing.status = 1 and a.marketing.enable = 1 and (GETDATE() between a.marketing.startDate and a.marketing.endDate) "
                    + "and (ad.status <> 'closed' or ad.status = 'closed' and ad.updateDate between DATEADD(d, -" + JSFUtil.getApplication().getCloseAssignmentList() + ", DATEDIFF(d, 0, GETDATE())) and DATEADD(d, 1, DATEDIFF(d, 0, GETDATE()))) ";
        }

        String orderBy = "order by po.purchaseDate desc, po.refNo";

        Query q = em.createQuery(sql + sqlWhere + orderBy);

        if (mode.equals("today")) {
            Date today = new Date();
            q.setParameter(1, JSFUtil.toDateWithoutTime(today));
            q.setParameter(2, JSFUtil.toDateWithMaxTime(today));
        }

        return (List<PurchaseOrder>) q.getResultList();
    }

    public List<Object[]> findCreditCardUsageByCardNo(String cardNo) {
        List<Object[]> result = null;
        String sql = "SELECT "
                + "dbo.customer.initial as customer_init, dbo.customer.name as customer_name, dbo.customer.surname as customer_surname, "
                + "dbo.purchase_order_register.initial_text as insure_init, dbo.purchase_order_register.name AS insure_name,   dbo.purchase_order_register.surname AS insure_surname, "
                + "dbo.purchase_order.purchase_date, dbo.purchase_order.create_by "
                + "FROM  dbo.purchase_order INNER JOIN "
                + "dbo.purchase_order_detail ON dbo.purchase_order.id = dbo.purchase_order_detail.purchase_order_id AND  "
                + "dbo.purchase_order.id = dbo.purchase_order_detail.purchase_order_id INNER JOIN "
                + "dbo.purchase_order_register ON dbo.purchase_order_detail.id = dbo.purchase_order_register.purchase_order_detail_id AND  "
                + "dbo.purchase_order_detail.id = dbo.purchase_order_register.purchase_order_detail_id INNER JOIN "
                + "dbo.customer ON dbo.purchase_order.customer_id = dbo.customer.id AND dbo.purchase_order.customer_id = dbo.customer.id "
                + "where sale_result = 'Y'  and dbo.purchase_order.card_no = '" + cardNo + "' order by dbo.purchase_order.purchase_date desc ";
        System.out.println("findCreditCardUsageByCardNo : " + sql);
        Query q = em.createNativeQuery(sql);
        result = q.getResultList();
        return result;
    }

    public List<String[]> countDistinctPaymentMethodFromYesSale(Date fromSaleDate, Date toSaleDate, Integer campaignId, Integer productId) {
        List<String[]> list = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
            String where = " ";
            if (campaignId != null && campaignId != 0) {
                where += " and a.campaign_id = " + campaignId + " ";
            }
            if (productId != null && productId != 0) {
                where += " and pod.product_id = " + productId + " ";
            }
            String qq = "select distinct(tbl.id), tbl.name from (select convert(varchar(100),p.id) as [id],p.name as [name] from purchase_order po "
                    + "inner join assignment_detail ad on po.assignment_detail_id = ad.id "
                    + "inner join assignment a on ad.assignment_id = a.id "
                    + "inner join users u on u.id = ad.user_id "
                    + "inner join payment_method p on po.payment_method = p.id "
                    + "inner join purchase_order_detail pod on po.id = pod.purchase_order_id "
                    + "where po.sale_result = 'Y' and po.sale_date between '" + sdf.format(fromSaleDate) + "' and '" + sdf.format(toSaleDate) + "' " + where + " ) as tbl ";
            Query q = em.createNativeQuery(qq);
            list = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public synchronized String getJsonVoiceTrack(String folderName, String refNos, String separator) {
        String ret = "";
        try {
            Query q = em.createNativeQuery("{call spGetJsonVoiceTrack(:folderName, :refNos, :separator)}");
            q.setParameter("folderName", folderName);
            q.setParameter("refNos", refNos);
            q.setParameter("separator", separator);
            Object o = q.getSingleResult();
            if (o != null) {
                ret = o.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    public List<PurchaseOrder> findFamilyPurchaseOrder(Integer purchaseOrderId) {
        List<PurchaseOrder> list = null;
        try {
            Query q = em.createQuery("SELECT object(po) FROM PurchaseOrder AS po WHERE po.id = :id OR po.mainPoId = :id ORDER BY po.id");
            q.setParameter("id", purchaseOrderId);
            list = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<TotalPremiumInfoValue> findTotalPremiumFamily(Integer mainPoId) {
        try {
           Query q = em.createQuery("select new " + TotalPremiumInfoValue.class.getName() 
                    + " (po.id, po.refNo, po.insurePerson, por.gender, por.age, por.relationMainInsured, po.saleResult, "
                    + "po.purchaseDate, po.approvalStatus, po.qcStatus, po.paymentStatus, po.netPremium) "
                    + "from PurchaseOrder po "
                    + "join po.purchaseOrderDetailCollection pod "
                    + "join pod.purchaseOrderRegisterCollection por "
                    + "where po.id = :mainPoId or po.mainPoId = :mainPoId");      
            q.setParameter("mainPoId", mainPoId);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String validateFamilyPurchaseOrder(Integer purchaseOrderId, Integer mainPurchaseOrderId, Integer productPlanId, String paymentModeId, String saleResult) {
        String sql = "EXEC spValidateSaveFamilyPurchaseOrder :purchaseOrderId, :mainPurchaseOrderId, :productPlanId, :paymentModeId, :saleResult";

        Query q = em.createNativeQuery(sql);
        q.setParameter("purchaseOrderId", purchaseOrderId);
        q.setParameter("mainPurchaseOrderId", mainPurchaseOrderId);
        q.setParameter("productPlanId", productPlanId);
        q.setParameter("paymentModeId", paymentModeId);
        q.setParameter("saleResult", saleResult);
        
        String s = "";
        try {
            //s = q.getResultList();
            s = q.getSingleResult().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
    
    public String validateApproveFamilyPurchaseOrder(Integer purchaseOrderId, String approveStatus) {
        String sql = "EXEC spValidateApproveFamilyPurchaseOrder :purchaseOrderId, :approveStatus";

        Query q = em.createNativeQuery(sql);
        q.setParameter("purchaseOrderId", purchaseOrderId);
        q.setParameter("approveStatus", approveStatus);
        
        String s = "";
        try {
            s = q.getSingleResult().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
    
    public void updateFamilyPurchaseOrder(Integer purchaseOrderId) {      
        String sql = "EXEC spUpdateSaveFamilyPurchaseOrder :purchaseOrderId";

        Query q = em.createNativeQuery(sql);
        q.setParameter("purchaseOrderId", purchaseOrderId);

        try {
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateFamilyPurchaseOrderApprovalLog(Integer purchaseOrderId) {      
        String sql = "EXEC spUpdateSaveFamilyPurchaseOrderApprovalLog :purchaseOrderId";

        Query q = em.createNativeQuery(sql);
        q.setParameter("purchaseOrderId", purchaseOrderId);

        try {
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updatePurchaseOrderInfoFromBCIB(Double grossPremium, Double totalPremium, String leadId) {
        try {
            Query q = em.createNativeQuery("update po set po.price =?1, po.net_premium = ?2 ,po.annual_net_premium = ?2 "+
                    "from purchase_order po " +
                    "where id in " +
                    "(select max(po.id) from purchase_order po inner join customer c on c.id = po.customer_id where c.flexfield1 =?3)");
            q.setParameter(1, grossPremium);
            q.setParameter(2, totalPremium);
            q.setParameter(3, leadId);
            q.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePurchaseOrderApproveFromBCIB(String saleCode, Date datetime,String status, String leadId) {
        try {
            Query q = em.createNativeQuery("update po set po.preapproval_by = ?1, po.preapproval_date = ?2 ,po.preapproval_status = ?3 "+
                    "from purchase_order po " +
                    "where id in " +
                    "(select max(po.id) from purchase_order po inner join customer c on c.id = po.customer_id where c.flexfield1 =?4)");
            q.setParameter(1, saleCode);
            q.setParameter(2, datetime);
            if(status.equalsIgnoreCase("Complete")) {
                q.setParameter(3, "approved");
            }else if(status.equalsIgnoreCase("InComplete")){
                q.setParameter(3, "rejected");
            }
            q.setParameter(4, leadId);
            q.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePurchaseOrderPolicyNoFromBCIB(String datetime, String policyNo,String policyDate, String leadId) {
        try {
            Query q = em.createNativeQuery("update po set po.fx1 = ?1, po.fx2 = ?2 ,po.fx3 = ?3 "+
                    "from purchase_order po " +
                    "where id in " +
                    "(select max(po.id) from purchase_order po inner join customer c on c.id = po.customer_id where c.flexfield1 =?4)");
            q.setParameter(1, datetime);
            q.setParameter(2, policyNo);
            q.setParameter(3, policyDate);
            q.setParameter(4, leadId);
            q.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updatePaymentCountNo(String number, Integer purchaseOrderId) {
        String sql = "update purchase_order set payment_count_no =:number where id =:purchaseOrderId";
        Query q = em.createNativeQuery(sql);
        q.setParameter("purchaseOrderId", purchaseOrderId);
        q.setParameter("number", number);

        try {
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePaymentSubmitDate(Integer purchaseOrderId,String refno1,Date date) {
        String sql = "update purchase_order set payment_submit_date = getdate(),payment_reference_no1 =:refno1,payment_gateway_submit_date =:date where id =:purchaseOrderId";
        Query q = em.createNativeQuery(sql);
        q.setParameter("purchaseOrderId", purchaseOrderId);
        q.setParameter("refno1", refno1);
        q.setParameter("date", date);

        try {
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePaymentGatewaySubmitDate(Integer purchaseOrderId) {
        String sql = "update purchase_order set payment_gateway_submit_date =getdate() where id =:purchaseOrderId";
        Query q = em.createNativeQuery(sql);
        q.setParameter("purchaseOrderId", purchaseOrderId);
        try {
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSentSMS(Integer purchaseOrderId) {
        String sql = "update purchase_order set sms_submit_date = getdate(),sms_result='PENDING' where id =:purchaseOrderId";
        Query q = em.createNativeQuery(sql);
        q.setParameter("purchaseOrderId", purchaseOrderId);

        try {
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
