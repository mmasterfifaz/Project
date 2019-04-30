/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@Transactional
public class SoMessageHistoryDAO {

    //private static Logger log = Logger.getLogger(SoMessageHistory.class);
    @PersistenceContext
    private EntityManager em;

//    public void createSoMessageHistoryKnowledgebase(SoMessageHistoryKnowledgebase SoMessageHistoryKnowledgebase) {
//        em.persist(SoMessageHistoryKnowledgebase);
//    }
    
//    public void removeSoMessageHistoryKnowledgebase(SoMessageHistoryKnowledgebasePK SoMessageHistoryKnowledgebasePK) throws IllegalOrphanException, NonexistentEntityException {
//        SoMessageHistoryKnowledgebase SoMessageHistoryKnowledgebase;
//        try {
//            SoMessageHistoryKnowledgebase = em.getReference(SoMessageHistoryKnowledgebase.class, SoMessageHistoryKnowledgebasePK);
////            userGroupAcl.getId();
//        } catch (EntityNotFoundException enfe) {
//            throw new NonexistentEntityException("The SoMessageHistoryKnowledgebase with id " + SoMessageHistoryKnowledgebasePK + " no longer exists.", enfe);
//        }
//        em.remove(SoMessageHistoryKnowledgebase);
//    }

    public int getSoMessageHistoryCount() {
        return ((Long) em.createQuery("select count(o) from SoMessageHistory as o").getSingleResult()).intValue();
    }

    public int getMessageCount(String user_id, String src) {
        try {
            Query q = em.createQuery("select count(o) from SoMessageHistory as o where o.user_id=?1 and o.source_type_enum_id = ?2");
            q.setParameter(1, user_id);
            q.setParameter(2, src);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

//    public List<SoMessageHistoryKnowledgebase> findSoMessageHistoryKnowledgebase(Integer id) {
//        Query q = em.createQuery("select object(o) from SoMessageHistoryKnowledgebase as o"
//                + " where o.SoMessageHistoryKnowledgebasePK.SoMessageHistoryId = ?1"
//                + " order by o.SoMessageHistoryKnowledgebasePK.knowledgebaseId");
//        q.setParameter(1, id);
//        return q.getResultList();
//    }

//    public void deleteActivityAttachment(Integer activityId) {
//        //delete ActivityAttachment
//        Query q = em.createQuery("Delete from ActivityAttachment aa where aa.activity.id = ?1");
//        q.setParameter(1, activityId);
//        q.executeUpdate();
//    }


    public int checkSoMessageHistoryDup(String source_id, BigInteger related_id, Integer activity_time) {
        Query q;
        q = em.createQuery("select count(o) from SoMessageHistory as o where o.source_id =:source_id and o.related_id = :related_id and o.activity_time = :activity_time");
        q.setParameter("source_id", source_id);
        q.setParameter("related_id", related_id);
        q.setParameter("activity_time", activity_time);

        return ((Long) q.getSingleResult()).intValue();
    }

    public void deactiveById(Integer id) {
        Query q = em.createQuery("update SoMessageHistory set active=false where so_message_id = ?1");
        q.setParameter(1, id);
        q.executeUpdate();
    }

}
