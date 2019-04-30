/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.NotificationMessage;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author songwisit
 */
@Transactional
public class NotificationMessageDAO {
    private static Logger log = Logger.getLogger(NotificationMessageDAO.class);

    @PersistenceContext
    private EntityManager em;

    public void create(NotificationMessage notificationMessage) {
        em.persist(notificationMessage);
    }

    public void edit(NotificationMessage notificationMessage) {
        notificationMessage = em.merge(notificationMessage);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        NotificationMessage notificationMessage;
        try {
            notificationMessage = em.getReference(NotificationMessage.class, id);
            notificationMessage.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The Notification Message with id " + id + " no longer exists.", enfe);
        }
        em.remove(notificationMessage);
    }

    public List<NotificationMessage> findNotificationMessageEntities() {
        return findNotificationMessageEntities(true, -1, -1);
    }

    public List<NotificationMessage> findNotificationMessageEntities(int maxResults, int firstResult) {
        return findNotificationMessageEntities(false, maxResults, firstResult);
    }

    private List<NotificationMessage> findNotificationMessageEntities(boolean all, int maxResults, int firstResult) {
        try {
            Query q = em.createQuery("select object(o) from NotificationMessage as o order by o.id");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public NotificationMessage findNotificationMessage(Integer id) {
        try {
            return em.find(NotificationMessage.class, id);
        } finally {
            em.close();
        }
    }

    public int getNotificationMessageCount(String loginName) {
        Query q = em.createQuery("select count(o) from NotificationMessage as o where o.loginName = ?1 and o.status = 1 and o.notifyDate >= ?2");
        q.setParameter(1, loginName);
        q.setParameter(2, new Date(), TemporalType.DATE);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<NotificationMessage> findNotificationMessageByLoginName(String loginName) {
        if(loginName != null && !loginName.isEmpty()) {
            String query = "select distinct object(o) from NotificationMessage as o "
                    + "where o.loginName = ?1 and o.status = 1 and o.notifyDate >= ?2 "
                    + "order by o.priority, o.createDate desc";
            Query q = em.createQuery(query);
            q.setParameter(1, loginName);
            q.setParameter(2, new Date(), TemporalType.DATE);
            return q.getResultList();
        } else {
            return null;
        }
    }
    
    public void updateStatusByUUID(String uuid) {
        try {
            Query q = em.createNativeQuery("UPDATE notification_message SET status = 0 WHERE uuid =?1");
            q.setParameter(1, uuid);
            q.executeUpdate();
        }catch(Exception e){
            log.error(e);
        }
    }
    
    public void updateStatusByRefId(Integer refId) {
        try {
            Query q = em.createNativeQuery("UPDATE notification_message SET status = 0 WHERE ref_id =?1");
            q.setParameter(1, refId);
            q.executeUpdate();
        }catch(Exception e){
            log.error(e);
        }
    }
     
    public void createRequestLog(String uri, String header, String message, String response) {
        Query q = em.createNativeQuery("INSERT INTO notification_request_log(request_date, request_uri, request_header, request_message, response_message)"
                + " VALUES(:date, :uri, :header, :message, :response)");
        q.setParameter("date", new Date());
        q.setParameter("uri", uri);
        q.setParameter("header", header);
        q.setParameter("message", message);
        q.setParameter("response", response);
        
        q.executeUpdate();
    }
}