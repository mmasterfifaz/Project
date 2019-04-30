/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Notification;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ukritj
 */
@Transactional
public class NotificationDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Notification notification) {
        em.persist(notification);
    }

    public void edit(Notification notification) {
        notification = em.merge(notification);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        Notification notification;
        try {
            notification = em.getReference(Notification.class, id);
            notification.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The Notification with id " + id + " no longer exists.", enfe);
        }
        em.remove(notification);
    }

    public List<Notification> findNotificationEntities() {
        return findNotificationEntities(true, -1, -1);
    }

    public List<Notification> findNotificationEntities(int maxResults, int firstResult) {
        return findNotificationEntities(false, maxResults, firstResult);
    }

    private List<Notification> findNotificationEntities(boolean all, int maxResults, int firstResult) {
        try {
            Query q = em.createQuery("select object(o) from Notification as o order by o.id");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Notification findNotification(Integer id) {
        try {
            return em.find(Notification.class, id);
        } finally {
            em.close();
        }
    }

    public int getNotificationCount() {
        try {
            Query q = em.createQuery("select count(o) from Notification as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Notification> findNewByToUserId(Integer userId) {
        try {
            Query q = em.createQuery("select object(o) from Notification as o left join o.assignmentDetail ad where o.enable = 1 and o.status = 1 and o.toUser.id = :userId order by o.id desc");
            q.setParameter("userId", userId);
            q.setMaxResults(3);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Notification> findNewNotification(Integer userId, Boolean status, String type) {
        try {
            String sql = "select object(o) from Notification as o"
                    + " left join o.assignmentDetail ad"
                    + " where o.enable = 1 and o.toUser.id = :userId";
            if(status != null) {
                sql += " and o.status = :status";
            }
            if(type != null && !type.equals("")){
                sql += " and o.type = :type";
            }
                    
            String order = " order by o.id desc";
            Query q = em.createQuery(sql + order);
            q.setParameter("userId", userId);
            if(status != null) {
                q.setParameter("status", status);
            }
            if(type != null && !type.equals("")){
                q.setParameter("type", type);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
}
