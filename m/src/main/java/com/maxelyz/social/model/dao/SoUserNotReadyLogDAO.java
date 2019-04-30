/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoUserNotReadyLog;
import com.maxelyz.utils.JSFUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
//import java.util.LinkedHashMap;
import java.util.List;
//import java.util.Map;
/**
 *
 * @author Administrator
 */
@Transactional
public class SoUserNotReadyLogDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoUserNotReadyLog soUserNotReadyLog) throws PreexistingEntityException {
        em.persist(soUserNotReadyLog);
    }

    public void edit(SoUserNotReadyLog soUserNotReadyLog) throws NonexistentEntityException, Exception {
        soUserNotReadyLog = em.merge(soUserNotReadyLog);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoUserNotReadyLog soUserNotReadyLog;
        try {
            soUserNotReadyLog = em.getReference(SoUserNotReadyLog.class, id);
            soUserNotReadyLog.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soUserNotReadyLog with id " + id + " no longer exists.", enfe);
        }
        em.remove(soUserNotReadyLog);
    }

    public List<SoUserNotReadyLog> findSoUserNotReadyLogEntities() {
        return findSoUserNotReadyLogEntities(true, -1, -1);
    }

    public List<SoUserNotReadyLog> findSoUserNotReadyLogEntities(int maxResults, int firstResult) {
        return findSoUserNotReadyLogEntities(false, maxResults, firstResult);
    }

    private List<SoUserNotReadyLog> findSoUserNotReadyLogEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoUserNotReadyLog as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        q.setParameter(1, JSFUtil.getUserSession().getUsers().getId());
        return q.getResultList();
    }
    
    public int getSoUserNotReadyLogCount() {
        Query q = em.createQuery("select count(o) from SoUserNotReadyLog as o");
        return ((Long) q.getSingleResult()).intValue();
    }

   
}
