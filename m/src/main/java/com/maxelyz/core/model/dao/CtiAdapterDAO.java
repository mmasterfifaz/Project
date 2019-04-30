/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.CtiAdapter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CtiAdapterDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(CtiAdapter ctiAdapter) {
        em.persist(ctiAdapter);
    }

    public void edit(CtiAdapter ctiAdapter) throws NonexistentEntityException, Exception {
        ctiAdapter = em.merge(ctiAdapter);
    }
     
    public void destroy(Integer id) throws NonexistentEntityException {
        CtiAdapter ctiAdapter;
        try {
            ctiAdapter = em.getReference(CtiAdapter.class, id);
            ctiAdapter.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The ctiAdapter with id " + id + " no longer exists.", enfe);
        }
        em.remove(ctiAdapter);
    }

    public List<CtiAdapter> findCtiAdapterEntities() {
        return findCtiAdapterEntities(true, -1, -1);
    }

    public List<CtiAdapter> findCtiAdapterEntities(int maxResults, int firstResult) {
        return findCtiAdapterEntities(false, maxResults, firstResult);
    }

    private List<CtiAdapter> findCtiAdapterEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CtiAdapter as o where o.enable = 1 order by o.name ");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<CtiAdapter> findCtiAdapterAvailable() {
        Query q = em.createQuery("select object(o) from CtiAdapter as o where o.enable = 1 and o.status = 1 order by o.name ");
        return q.getResultList();
    }
    
    public CtiAdapter findCtiAdapter(Integer id) {
        return em.find(CtiAdapter.class, id);
    }

    public int getCtiAdapterCount() {
        Query q = em.createQuery("select count(o) from CtiAdapter as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public int checkCtiAdapterName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from CtiAdapter as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from CtiAdapter as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    
    public Map<String, Integer> findCtiAdapterList() {
        List<CtiAdapter> ctiAdapterList = findCtiAdapterAvailable();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CtiAdapter obj : ctiAdapterList) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
}
