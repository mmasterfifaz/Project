/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;


import com.maxelyz.core.controller.admin.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.Settlement;
import com.maxelyz.core.model.entity.SettlementDetail;
import java.util.List;
import javax.persistence.*;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SettlementDetailDAO  {


    @PersistenceContext
    private EntityManager em;


    public void create(SettlementDetail settlementDetail) throws PreexistingEntityException, Exception {
        em.persist(settlementDetail); 
    }

    public void edit(SettlementDetail settlementDetail) throws NonexistentEntityException, Exception {
        settlementDetail = em.merge(settlementDetail);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SettlementDetail settlementDetail;
        try {
            settlementDetail = em.getReference(SettlementDetail.class, id);
            settlementDetail.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The settlementDetail with id " + id + " no longer exists.", enfe);
        }
        Settlement settlement = settlementDetail.getSettlement();
        if (settlement != null) {
            settlement.getSettlementDetailCollection().remove(settlementDetail);
            settlement = em.merge(settlement);
        }
        em.remove(settlementDetail);
    }

    public List<SettlementDetail> findSettlementDetailEntities() {
        return findSettlementDetailEntities(true, -1, -1);
    }

    public List<SettlementDetail> findSettlementDetailEntities(int maxResults, int firstResult) {
        return findSettlementDetailEntities(false, maxResults, firstResult);
    }

    private List<SettlementDetail> findSettlementDetailEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SettlementDetail as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SettlementDetail findSettlementDetail(Integer id) {
        return em.find(SettlementDetail.class, id);
    }

    public int getSettlementDetailCount() {
        Query q = em.createQuery("select count(o) from SettlementDetail as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<SettlementDetail> findSettlementDetailBySettlementId(Integer settlementId) {
        Query q = em.createQuery("select object(o) from SettlementDetail as o where settlement.id = ?1");
        q.setParameter(1, settlementId);
        return q.getResultList();
}
}
