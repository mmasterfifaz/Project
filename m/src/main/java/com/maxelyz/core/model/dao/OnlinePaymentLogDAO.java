/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.OnlinePaymentLog;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class OnlinePaymentLogDAO  {


    @PersistenceContext
    private EntityManager em;


    public void create(OnlinePaymentLog onlinePaymentLog) throws PreexistingEntityException, Exception {
        em.persist(onlinePaymentLog);          
    }

    public void edit(OnlinePaymentLog onlinePaymentLog) throws NonexistentEntityException, Exception {
        onlinePaymentLog = em.merge(onlinePaymentLog);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        OnlinePaymentLog onlinePaymentLog;
        try {
            onlinePaymentLog = em.getReference(OnlinePaymentLog.class, id);
            onlinePaymentLog.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The onlinePaymentLog with id " + id + " no longer exists.", enfe);
        }
        em.remove(onlinePaymentLog);
    }

    public List<OnlinePaymentLog> findOnlinePaymentLogEntities() {
        return findOnlinePaymentLogEntities(true, -1, -1);
    }

    public List<OnlinePaymentLog> findOnlinePaymentLogEntities(int maxResults, int firstResult) {
        return findOnlinePaymentLogEntities(false, maxResults, firstResult);
    }

    private List<OnlinePaymentLog> findOnlinePaymentLogEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from OnlinePaymentLog as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
    
    public List<OnlinePaymentLog> findOnlinePaymentLog(Date fromDate, Date toDate, String postParam, String returnParam, String createBy) {
        Query q = em.createQuery("select object(o) from OnlinePaymentLog as o where o.createDate between ?1 and ?2 and o.postParam like ?3 and o.returnParam like ?4 and o.createBy like ?5 order by o.createDate ");   
        q.setParameter(1, fromDate);
        q.setParameter(2, toDate);
        q.setParameter(3, "%"+postParam+"%");
        q.setParameter(4, "%"+returnParam+"%");
        q.setParameter(5, "%"+createBy+"%");
        
        return q.getResultList();
    }

    public OnlinePaymentLog findOnlinePaymentLog(Integer id) {
        return em.find(OnlinePaymentLog.class, id);
    }

    public int getOnlinePaymentLogCount() {
        Query q = em.createQuery("select count(o) from OnlinePaymentLog as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
