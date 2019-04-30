/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoPriority;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Transactional
public class SoPriorityDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoPriority soPriority) throws PreexistingEntityException {
        this.validate(soPriority);
        em.persist(soPriority);
    }

    public void edit(SoPriority soPriority) throws NonexistentEntityException, Exception {
        soPriority = em.merge(soPriority);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoPriority soPriority;
        try {
            soPriority = em.getReference(SoPriority.class, id);
            soPriority.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soPriority with id " + id + " no longer exists.", enfe);
        }
        em.remove(soPriority);
    }

    public List<SoPriority> findSoPriorityEntities() {
        return findSoPriorityEntities(true, -1, -1);
    }

    public List<SoPriority> findSoPriorityEntities(int maxResults, int firstResult) {
        return findSoPriorityEntities(false, maxResults, firstResult);
    }

    private List<SoPriority> findSoPriorityEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoPriority as o where o.enable = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<SoPriority> findSoPriorityStatus() {
        Query q = em.createQuery("select object(o) from SoPriority as o where o.enable = true and o.status = true order by o.code");
        return q.getResultList();
    }

    public SoPriority findSoPriority(Integer id) {
        return em.find(SoPriority.class, id);
    }

    public int getSoPriorityCount() {
        Query q = em.createQuery("select count(o) from SoPriority as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<SoPriority> findSoPriorityByCode(String code) {
        Query q = em.createQuery("select object(o) from SoPriority as o where code = ?1");
        q.setParameter(1, code);
        return q.getResultList();
    }

    private void validate(SoPriority soPriority) throws PreexistingEntityException {
        if (findSoPriorityByCode(soPriority.getCode()).size() > 0) {
            throw new PreexistingEntityException("Duplicate Code");
        }
    }

    public Map<String, Integer> getSoPriorityList() {
        List<SoPriority> soPrioritys = this.findSoPriorityStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoPriority obj : soPrioritys) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public int checkSoPriorityCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from SoPriority as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from SoPriority as o where o.code =:code and o.enable = true and o.id <> :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
        
}
