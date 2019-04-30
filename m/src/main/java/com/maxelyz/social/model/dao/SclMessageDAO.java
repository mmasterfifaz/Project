/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SclMessage;
import com.maxelyz.social.model.entity.SclMessageAssignment;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import com.maxelyz.social.model.entity.SclUsers;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;

@Transactional
public class SclMessageDAO {

    private static Logger log = Logger.getLogger(SclMessage.class);
    @PersistenceContext
    private EntityManager em;

    public void create(SclMessage sclMessage) throws PreexistingEntityException, Exception {
        em.persist(sclMessage);
    }

    public void edit(SclMessage sclMessage) throws NonexistentEntityException, Exception {
        sclMessage = em.merge(sclMessage);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SclMessage sclMessage;
        try {
            sclMessage = em.getReference(SclMessage.class, id);
            sclMessage.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The sclMessage with id " + id + " no longer exists.", enfe);
        }
        em.remove(sclMessage);
    }

    public List<SclMessage> findSclMessageEntities() {
        return findSclMessageEntities(true, -1, -1);
    }

    public List<SclMessage> findSclMessageEntities(int maxResults, int firstResult) {
        return findSclMessageEntities(false, maxResults, firstResult);
    }

    private List<SclMessage> findSclMessageEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SclMessage as o order by o.id");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SclMessage findSclMessage(Integer id) {
        return em.find(SclMessage.class, id);
    }

    public int getSclMessageCount() {
        return ((Long) em.createQuery("select count(o) from SclMessage as o").getSingleResult()).intValue();
    }

//    public List<SclMessage> findSclMessageByReceivedBy(Integer userId) {
////        String sql = "select object(o) from SclMessage as o";
////                + " where o.relatedId = ?1"
////                + " order by o.activityTime";
////        Query q = em.createQuery(sql);
//
//        String sql = "select object(o) from SclMessage o where o.id in (select a.messageId from SclMessageAssignment a where a.receivedBy = ?1) order by o.activityTime";
//        Query q = em.createQuery(sql);
//
//        q.setParameter(1, userId);
//        return q.getResultList();
//    }

    public List<SclMessageAssignment> findSclMessageByReceivedBy2(Integer userId, Integer pageType) {

        String sql;
        if (pageType==1) {
            sql = "select object(o) from SclMessageAssignment o where o.receivedBy = ?1 and o.actionId is null order by o.assignDate";
        } else {
            sql = "select object(o) from SclMessageAssignment o where o.receivedBy = ?1 and o.statusId="+pageType+" order by o.assignDate";
        }

        Query q = em.createQuery(sql);

        q.setParameter(1, userId);
        return q.getResultList();
    }

    public List<SclMessage> findSclMessageRelatedById(Integer id) {
//        String sql = "select object(o) from SclMessage o "
//                + " where o.relatedId = ?1"
//                + " order by o.activityTime";
        String sql = "select object(o) from SclMessage o where o.relatedId in (select oo.relatedId from SclMessage oo where oo.id = ?1) order by o.activityTime";
        Query q = em.createQuery(sql);

        q.setParameter(1, id);
        return q.getResultList();
    }

    public List<SclMessage> findSclMessageByUserId(SclUsers sclUsers) {
//        String sql = "select object(o) from SclMessage o "
//                + " where o.relatedId = ?1"
//                + " order by o.activityTime";
        String sql = "select object(o) from SclMessage o where o.sclUsers=?1 order by o.activityTime desc";
        Query q = em.createQuery(sql);

        q.setParameter(1, sclUsers);
        return q.getResultList();
    }

    public int getMessageCount(String srcType) {
        try {
            Query q = em.createQuery("select count(o) from SclMessage as o where o.sourceType = ?1");
            q.setParameter(1, srcType);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

