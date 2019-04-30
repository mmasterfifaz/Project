/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.PurchaseHistoryType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PurchaseHistoryTypeDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(PurchaseHistoryType purchaseHistoryType) throws PreexistingEntityException, Exception {
        em.persist(purchaseHistoryType);
    }

    public void edit(PurchaseHistoryType purchaseHistoryType) throws NonexistentEntityException, Exception {
        purchaseHistoryType = em.merge(purchaseHistoryType);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        PurchaseHistoryType purchaseHistoryType;
        try {
            purchaseHistoryType = em.getReference(PurchaseHistoryType.class, id);
            purchaseHistoryType.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The purchaseHistoryType with id " + id + " no longer exists.", enfe);
        }
        em.remove(purchaseHistoryType);
    }

    public List<PurchaseHistoryType> findPurchaseHistoryTypeEntities() {
        return findPurchaseHistoryTypeEntities(true, -1, -1);
    }

    public List<PurchaseHistoryType> findPurchaseHistoryTypeEntities(int maxResults, int firstResult) {
        return findPurchaseHistoryTypeEntities(false, maxResults, firstResult);
    }

    private List<PurchaseHistoryType> findPurchaseHistoryTypeEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from PurchaseHistoryType as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public PurchaseHistoryType findPurchaseHistoryType(Integer id) {
        return em.find(PurchaseHistoryType.class, id);
    }

    public int getPurchaseHistoryTypeCount() {
        return ((Long) em.createQuery("select count(o) from PurchaseHistoryType as o").getSingleResult()).intValue();
    }
}
