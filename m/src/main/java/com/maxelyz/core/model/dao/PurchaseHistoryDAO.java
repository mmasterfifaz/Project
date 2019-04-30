/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.PurchaseHistory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PurchaseHistoryDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(PurchaseHistory purchaseHistory) throws IllegalOrphanException, PreexistingEntityException, Exception {
        em.persist(purchaseHistory);
    }

    public void edit(PurchaseHistory purchaseHistory) throws IllegalOrphanException, NonexistentEntityException, Exception {
        purchaseHistory = em.merge(purchaseHistory);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        PurchaseHistory purchaseHistory;
        try {
            purchaseHistory = em.getReference(PurchaseHistory.class, id);
            purchaseHistory.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The purchaseHistory with id " + id + " no longer exists.", enfe);
        }
        em.remove(purchaseHistory);
    }

    public List<PurchaseHistory> findPurchaseHistoryEntities() {
        return findPurchaseHistoryEntities(true, -1, -1);
    }

    public List<PurchaseHistory> findPurchaseHistoryEntities(int maxResults, int firstResult) {
        return findPurchaseHistoryEntities(false, maxResults, firstResult);
    }

    private List<PurchaseHistory> findPurchaseHistoryEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from PurchaseHistory as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public PurchaseHistory findPurchaseHistory(Integer id) {
        return em.find(PurchaseHistory.class, id);
    }

    public int getPurchaseHistoryCount() {
        return ((Long) em.createQuery("select count(o) from PurchaseHistory as o").getSingleResult()).intValue();
    }
}
