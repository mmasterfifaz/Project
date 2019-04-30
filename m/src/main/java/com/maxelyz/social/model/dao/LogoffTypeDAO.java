/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.LogoffType;
import java.util.LinkedHashMap;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Transactional
public class LogoffTypeDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(LogoffType logoffType) throws PreexistingEntityException {
        em.persist(logoffType);
    }

    public void edit(LogoffType logoffType) throws NonexistentEntityException, Exception {
        em.merge(logoffType);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        LogoffType logoffType;
        try {
            logoffType = em.getReference(LogoffType.class, id);
            logoffType.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The logoffType with id " + id + " no longer exists.", enfe);
        }
        em.remove(logoffType);
    }

    public List<LogoffType> findLogoffTypeEntities() {
        return findLogoffTypeEntities(true, -1, -1);
    }

    public List<LogoffType> findLogoffTypeEntities(int maxResults, int firstResult) {
        return findLogoffTypeEntities(false, maxResults, firstResult);
    }

    private List<LogoffType> findLogoffTypeEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from LogoffType as o where o.enable = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public LogoffType findLogoffType(Integer id) {
        return em.find(LogoffType.class, id);
    }
        
    public List<LogoffType> findLogoffTypeStatus() {
        Query q = em.createQuery("select object(o) from LogoffType as o where o.enable = true and o.status = true order by o.code");
        return q.getResultList();
    }
        
    public int checkCode(String code, Integer id) {
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from LogoffType as o where o.code =:code and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from LogoffType as o where o.code =:code and o.enable = true and o.id <> :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public Map<String, Integer> getLogoffList() {
        List<LogoffType> LogoffTypes = this.findLogoffTypeStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (LogoffType obj : LogoffTypes) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
}
