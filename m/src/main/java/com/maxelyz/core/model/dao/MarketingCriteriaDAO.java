/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.MarketingCriteria;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author admin
 */
public class MarketingCriteriaDAO {
    private static Logger log = Logger.getLogger(MarketingCriteriaDAO.class);
    @PersistenceContext
    private EntityManager em;

    public void create(MarketingCriteria marketingCriteria) throws PreexistingEntityException, Exception {
        em.persist(marketingCriteria);
    }

    public void edit(MarketingCriteria marketingCriteria) throws IllegalOrphanException, NonexistentEntityException, Exception {
        marketingCriteria = em.merge(marketingCriteria);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        MarketingCriteria marketingCriteria;
        try {
            marketingCriteria = em.getReference(MarketingCriteria.class, id);
            marketingCriteria.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The marketingCriteria with id " + id + " no longer exists.", enfe);
        }
        em.remove(marketingCriteria);
    }

    public List<MarketingCriteria> findMarketingCriteriaEntities() {
        return findMarketingCriteriaEntities(true, -1, -1);
    }

    public List<MarketingCriteria> findMarketingCriteriaEntities(int maxResults, int firstResult) {
        return findMarketingCriteriaEntities(false, maxResults, firstResult);
    }

    private List<MarketingCriteria> findMarketingCriteriaEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from MarketingCriteria as o where o.enable = true");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<MarketingCriteria> findByMarketingId(Integer marketingId) {
        Query q = em.createQuery("select object(o) from MarketingCriteria as o where o.marketing.id = :marketingId order by o.id");
        
        q.setParameter("marketingId", marketingId);
        
        return q.getResultList();
    }
    
}
