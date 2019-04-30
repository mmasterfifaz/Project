/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.PurchaseOrderDetail;
import com.maxelyz.core.model.entity.TempPurchaseOrder;
import com.maxelyz.core.model.entity.TempPurchaseOrderDetail;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PurchaseOrderDetailDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(PurchaseOrderDetail purchaseOrderDetail) throws PreexistingEntityException, Exception {
        em.persist(purchaseOrderDetail);
    }

    public void edit(PurchaseOrderDetail purchaseOrderDetail) throws NonexistentEntityException, Exception {
        purchaseOrderDetail = em.merge(purchaseOrderDetail);
    }

    public void editTempPurchaseOrderDetail(TempPurchaseOrderDetail tempPurchaseOrderDetail) throws NonexistentEntityException, Exception {
        tempPurchaseOrderDetail = em.merge(tempPurchaseOrderDetail);
    }

    public void saveTpod(Integer tpodId, Double unitPrice, Integer quantity, Double amount) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update TempPurchaseOrderDetail set "
                + "unitPrice = :unitPrice, "
                + "quantity = :quantity, "
                + "amount = :amount "
                + "where id = :tpodId ");
        q.setParameter("unitPrice", unitPrice);
        q.setParameter("quantity", quantity);
        q.setParameter("amount", amount);
        q.setParameter("tpodId", tpodId);

        q.executeUpdate();
    }

    public void deleteTpod(Integer tpodId) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("delete from TempPurchaseOrderDetail "
                + "where id = :tpodId ");
        q.setParameter("tpodId", tpodId);
        q.executeUpdate();
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        PurchaseOrderDetail purchaseOrderDetail;
        try {
            purchaseOrderDetail = em.getReference(PurchaseOrderDetail.class, id);
            purchaseOrderDetail.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The purchaseOrderDetail with id " + id + " no longer exists.", enfe);
        }
        em.remove(purchaseOrderDetail);
    }

    public void destroyTemp(Integer id) throws NonexistentEntityException {
        TempPurchaseOrderDetail tempPurchaseOrderDetail;
        try {
            tempPurchaseOrderDetail = em.getReference(TempPurchaseOrderDetail.class, id);
            tempPurchaseOrderDetail.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The tempPurchaseOrderDetail with id " + id + " no longer exists.", enfe);
        }
        em.remove(tempPurchaseOrderDetail);
    }

    public List<PurchaseOrderDetail> findPurchaseOrderDetailEntities() {
        return findPurchaseOrderDetailEntities(true, -1, -1);
    }

    public List<PurchaseOrderDetail> findPurchaseOrderDetailEntities(int maxResults, int firstResult) {
        return findPurchaseOrderDetailEntities(false, maxResults, firstResult);
    }

    private List<PurchaseOrderDetail> findPurchaseOrderDetailEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from PurchaseOrderDetail as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public PurchaseOrderDetail findPurchaseOrderDetail(Integer id) {
        return em.find(PurchaseOrderDetail.class, id);
    }

    public int getPurchaseOrderDetailCount() {
        return ((Long) em.createQuery("select count(o) from PurchaseOrderDetail as o").getSingleResult()).intValue();
    }

    public TempPurchaseOrderDetail findTempPurchaseOrderDetail(Integer id) {
        return em.find(TempPurchaseOrderDetail.class, id);
    }

    public List<TempPurchaseOrderDetail> findTempPurchaseOrderDetailByAssignmentDetailId(Integer assignmentDetailId){
        Query q = em.createQuery("select object(o) from TempPurchaseOrderDetail as o where o.assignmentDetailId = ?1");
        q.setParameter(1, assignmentDetailId);
        return q.getResultList();
    }

    public List<TempPurchaseOrderDetail> findByTempPurchaseOrder(Integer tempPurchaseOrderId){
        Query q = em.createQuery("select object(o) from TempPurchaseOrderDetail as o where o.tempPurchaseOrder.id = ?1");
        q.setParameter(1, tempPurchaseOrderId);
        return q.getResultList();
    }

    public List<PurchaseOrderDetail> findByPurchaseOrder(Integer purchaseOrderId){
        Query q = em.createQuery("select object(o) from PurchaseOrderDetail as o where o.purchaseOrder.id = ?1");
        q.setParameter(1, purchaseOrderId);
        return q.getResultList();
    }
    
    public List<PurchaseOrderDetail> findFamilyPurchaseOrderDetail(Integer purchaseOrderId){
        try {
            Query q = em.createQuery("SELECT object(pod) FROM PurchaseOrderDetail AS pod WHERE pod.purchaseOrder.id = :id OR pod.purchaseOrder.mainPoId = :id ORDER BY pod.purchaseOrder.id");
            q.setParameter("id", purchaseOrderId);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<PurchaseOrderDetail> findPurchaseOrderDetailByMainPoId(Integer mainPoId){
        try {
            Query q = em.createQuery("select object(pod) from PurchaseOrderDetail as pod where pod.purchaseOrder.mainPoId = :mainPoId");
            q.setParameter("mainPoId", mainPoId);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<PurchaseOrderDetail> findPurchaseOrderDetailByPoId(Integer poId){
        try {
            Query q = em.createQuery("select object(pod) from PurchaseOrderDetail as pod where pod.purchaseOrder.id = :poId");
            q.setParameter("poId", poId);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
