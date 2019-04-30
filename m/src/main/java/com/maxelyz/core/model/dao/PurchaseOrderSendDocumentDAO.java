/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.PurchaseOrderSendDocument;
import com.maxelyz.core.model.value.admin.SaleApprovalValue;
import com.maxelyz.core.model.value.admin.SendDocumentValue;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class PurchaseOrderSendDocumentDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(PurchaseOrderSendDocument purchaseOrderSendDocument) {
        em.persist(purchaseOrderSendDocument);
    }

    public void edit(PurchaseOrderSendDocument purchaseOrderSendDocument) throws NonexistentEntityException, Exception {
        purchaseOrderSendDocument = em.merge(purchaseOrderSendDocument);
           
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        PurchaseOrderSendDocument purchaseOrderSendDocument;
        try {
            purchaseOrderSendDocument = em.getReference(PurchaseOrderSendDocument.class, id);
            purchaseOrderSendDocument.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The purchaseOrderSendDocument with id " + id + " no longer exists.", enfe);
        }
        em.remove(purchaseOrderSendDocument);
    }

    public List<PurchaseOrderSendDocument> findPurchaseOrderSendDocumentEntities() {
        return findPurchaseOrderSendDocumentEntities(true, -1, -1);
    }

    public List<PurchaseOrderSendDocument> findPurchaseOrderSendDocumentEntities(int maxResults, int firstResult) {
        return findPurchaseOrderSendDocumentEntities(false, maxResults, firstResult);
    }

    private List<PurchaseOrderSendDocument> findPurchaseOrderSendDocumentEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from PurchaseOrderSendDocument as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public PurchaseOrderSendDocument findPurchaseOrderSendDocument(Integer id) {
        return em.find(PurchaseOrderSendDocument.class, id);
    }

    public int getPurchaseOrderSendDocumentCount() {
        Query q = em.createQuery("select count(o) from PurchaseOrderSendDocument as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<SendDocumentValue> findBy(String fromDate, String toDate, String refNo, String firstName, String lastName) {
        String sql = "select NEW " + SendDocumentValue.class.getName() 
                + "("
                + " pos.id as id,"
                + " po.saleDate as saleDate,"
                + " po.refNo as refNo,"
                + " por.name as name,"
                + " por.surname as surname,"
                + " pos.itemNo as itemNo,"
                + " pos.documentType as documentType,"
                + " pos.amount as amount,"
                + " pos.remark as remark"
                + ")" 
                + " from PurchaseOrderSendDocument as pos"
                + " join pos.purchaseOrder as po"
                + " join po.purchaseOrderDetailCollection as pod"
                + " join pod.purchaseOrderRegisterCollection as por"
                + " where 1=1 "
                + " and por.no = 1";
        if(!fromDate.equals("") && !toDate.equals("")){
            sql += " and pos.sendDate between '" + fromDate + "' and '" + toDate + "'";
        }
        if(!refNo.equals("")){
            sql += " and po.refNo like '%" + refNo + "%'";
        }
        if(!firstName.equals("") && !lastName.equals("")) {
            sql += " and por.name like '%" + firstName + "%' and por.surname like '%" + lastName + "%'";
        }else if(!firstName.equals("") && lastName.equals("")) {
            sql += " and por.name like '%" + firstName + "%' or por.surname like '%" + lastName + "%'";
        }
        
        Query q = null;
        try{
            q = em.createQuery(sql);
        }catch(Exception e){
            e.printStackTrace();
        }
        List<SendDocumentValue> list = null;
        try{
            list = q.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return list;
    }
    
    public PurchaseOrderSendDocument findPurchaseOrderSendDocumentByRefNo(String refNo){
        String sql = "select object(posd) from PurchaseOrderSendDocument as posd"
                + " where posd.purchaseOrder.refNo = :refNo";
        
        Query q = em.createQuery(sql);
        q.setParameter("refNo", refNo);
        PurchaseOrderSendDocument obj = null;
        try{
            obj = (PurchaseOrderSendDocument) q.getSingleResult();
        }catch(Exception e){
            e.printStackTrace();
            obj = null;
        }
        
        return obj;
    
    }
    
    public List<PurchaseOrderSendDocument> findByPurchaseOrderId(Integer poId){
        String sql = "select object(posd) from PurchaseOrderSendDocument as posd"
                + " where posd.purchaseOrder.id = :poId";
        
        Query q = em.createQuery(sql);
        q.setParameter("poId", poId);
        List<PurchaseOrderSendDocument> list = null;
        try{
            list = q.getResultList();
        }catch(Exception e){
            e.printStackTrace();
            list = null;
        }
        
        return list;
    
    }
    
}
