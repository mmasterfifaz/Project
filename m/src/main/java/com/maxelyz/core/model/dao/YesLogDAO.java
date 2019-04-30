/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.YesLog;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class YesLogDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(YesLog yesLog) throws PreexistingEntityException, Exception {
        em.persist(yesLog);
    }

    public void edit(YesLog yesLog) throws NonexistentEntityException, Exception {
        yesLog = em.merge(yesLog);
    }

    public void destroy(Integer id) throws NonexistentEntityException {

        YesLog yesLog;
        try {
            yesLog = em.getReference(YesLog.class, id);
            yesLog.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The yesLog with id " + id + " no longer exists.", enfe);
        }
        em.remove(yesLog);
    }

    public List<YesLog> findYesLogEntities() {
        return findYesLogEntities(true, -1, -1);
    }

    public List<YesLog> findYesLogEntities(int maxResults, int firstResult) {
        return findYesLogEntities(false, maxResults, firstResult);
    }

    private List<YesLog> findYesLogEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from YesLog as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<YesLog> findYesLogEntitiesDesc() {
        return findYesLogEntitiesDesc(true, -1, -1);
    }

    private List<YesLog> findYesLogEntitiesDesc(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from YesLog as o order by o.createDate desc");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<YesLog> findYesLogByTypeDesc(boolean all, int maxResults, int firstResult, int fileType) {
        Query q = em.createQuery("select object(o) from YesLog as o where o.fileType = :fileType order by o.createDate desc");
        q.setParameter("fileType", fileType);

        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public YesLog findYesLog(Integer id) {
        YesLog p = em.find(YesLog.class, id);
        return p;
    }

    public int getYesLogCount() {
        return ((Long) em.createQuery("select count(o) from YesLog as o").getSingleResult()).intValue();
    }

//    public List<YesLog> findYesLogByName(String nameKeyword) {
//        Query q = em.createNamedQuery("YesLog.findByName");
//        q.setParameter("name", nameKeyword);
//        return q.getResultList();
//    }


}
