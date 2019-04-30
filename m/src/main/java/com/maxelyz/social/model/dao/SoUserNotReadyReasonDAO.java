/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoUserNotReadyReason;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Administrator
 */
@Transactional
public class SoUserNotReadyReasonDAO {
    
    @PersistenceContext
    private EntityManager em;

    public void create(SoUserNotReadyReason soUserNotReadyReason) throws PreexistingEntityException {
        em.persist(soUserNotReadyReason);
    }

    public void edit(SoUserNotReadyReason soUserNotReadyReason) throws NonexistentEntityException, Exception {
        soUserNotReadyReason = em.merge(soUserNotReadyReason);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoUserNotReadyReason soUserNotReadyReason;
        try {
            soUserNotReadyReason = em.getReference(SoUserNotReadyReason.class, id);
            soUserNotReadyReason.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soUserNotReadyReason with id " + id + " no longer exists.", enfe);
        }
        em.remove(soUserNotReadyReason);
    }

    public List<SoUserNotReadyReason> findSoUserNotReadyReasonEntities() {
        return findSoUserNotReadyReasonEntities(true, -1, -1);
    }

    public List<SoUserNotReadyReason> findSoUserNotReadyReasonEntities(int maxResults, int firstResult) {
        return findSoUserNotReadyReasonEntities(false, maxResults, firstResult);
    }

    private List<SoUserNotReadyReason> findSoUserNotReadyReasonEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoUserNotReadyReason as o where o.enable = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
    
    public List<SoUserNotReadyReason> findSoUserNotReadyReasonStatus() {
        Query q = em.createQuery("select object(o) from SoUserNotReadyReason as o where o.enable = true and o.status = true order by o.code");
        return q.getResultList();
    }
       
//    public List<SoUserNotReadyReason> findSoUserNotReadyReasonList() {
//        Query q = em.createQuery("select object(o) from SoUserNotReadyReason as o where o.enable = true order by o.code");
//        return q.getResultList();
//    }
         
    public SoUserNotReadyReason findSoUserNotReadyReason(Integer id) {
        return em.find(SoUserNotReadyReason.class, id);
    }

    public int getSoUserNotReadyReasonCount() {
        Query q = em.createQuery("select count(o) from SoUserNotReadyReason as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public int checkCode(String code, Integer id) {
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from SoUserNotReadyReason as o where o.code =:code and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from SoUserNotReadyReason as o where o.code =:code and o.enable = true and o.id <> :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }

}
