/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoIgnoreReason;
import java.util.LinkedHashMap;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Transactional
public class SoIgnoreReasonDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoIgnoreReason soIgnoreReason) throws PreexistingEntityException {
        em.persist(soIgnoreReason);
    }

    public void edit(SoIgnoreReason soIgnoreReason) throws NonexistentEntityException, Exception {
        em.merge(soIgnoreReason);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoIgnoreReason soIgnoreReason;
        try {
            soIgnoreReason = em.getReference(SoIgnoreReason.class, id);
            soIgnoreReason.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soIgnoreReason with id " + id + " no longer exists.", enfe);
        }
        em.remove(soIgnoreReason);
    }

    public List<SoIgnoreReason> findSoIgnoreReasonEntities() {
        return findSoIgnoreReasonEntities(true, -1, -1);
    }

    public List<SoIgnoreReason> findSoIgnoreReasonEntities(int maxResults, int firstResult) {
        return findSoIgnoreReasonEntities(false, maxResults, firstResult);
    }

    private List<SoIgnoreReason> findSoIgnoreReasonEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoIgnoreReason as o where o.enable = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SoIgnoreReason findSoIgnoreReason(Integer id) {
        return em.find(SoIgnoreReason.class, id);
    }
        
    public List<SoIgnoreReason> findSoIgnoreReasonStatus() {
        Query q = em.createQuery("select object(o) from SoIgnoreReason as o where o.enable = true and o.status = true order by o.code");
        return q.getResultList();
    }
        
    public int checkCode(String code, Integer id) {
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from SoIgnoreReason as o where o.code =:code and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from SoIgnoreReason as o where o.code =:code and o.enable = true and o.id <> :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public Map<String, Integer> getIgnoreReasonList() {
        List<SoIgnoreReason> ignoreReasons = this.findSoIgnoreReasonStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoIgnoreReason obj : ignoreReasons) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
}
