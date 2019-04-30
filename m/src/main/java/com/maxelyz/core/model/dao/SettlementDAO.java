/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.controller.admin.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.Settlement;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.SettlementDetail;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SettlementDAO {

    @PersistenceContext
    private EntityManager em;


    public void create(Settlement settlement) throws PreexistingEntityException, Exception {
        em.persist(settlement);           
    }

    public void edit(Settlement settlement) throws IllegalOrphanException, NonexistentEntityException, Exception {
        settlement = em.merge(settlement); 
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        Settlement settlement;
        try {
            settlement = em.getReference(Settlement.class, id);
            settlement.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The settlement with id " + id + " no longer exists.", enfe);
        }
        List<String> illegalOrphanMessages = null;
        Collection<SettlementDetail> settlementDetailCollectionOrphanCheck = settlement.getSettlementDetailCollection();
        for (SettlementDetail settlementDetailCollectionOrphanCheckSettlementDetail : settlementDetailCollectionOrphanCheck) {
            if (illegalOrphanMessages == null) {
                illegalOrphanMessages = new ArrayList<String>();
            }
            illegalOrphanMessages.add("This Settlement (" + settlement + ") cannot be destroyed since the SettlementDetail " + settlementDetailCollectionOrphanCheckSettlementDetail + " in its settlementDetailCollection field has a non-nullable settlement field.");
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        em.remove(settlement);
    }

    public List<Settlement> findSettlementEntities() {
        return findSettlementEntities(true, -1, -1);
    }

    public List<Settlement> findSettlementEntities(int maxResults, int firstResult) {
        return findSettlementEntities(false, maxResults, firstResult);
    }

    private List<Settlement> findSettlementEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Settlement as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Settlement findSettlement(Integer id) {
        return em.find(Settlement.class, id);
    }

    public int getSettlementCount() {
        Query q = em.createQuery("select count(o) from Settlement as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<Settlement> findSettlementByType(String type) {
        Query q = em.createQuery("select object(o) from Settlement as o where type = ?1 order by createDate desc");
        q.setParameter(1, type);
        return q.getResultList();
    }
}
