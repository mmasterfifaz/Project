/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Activity;
import com.maxelyz.core.model.entity.ActivityAttachment;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ActivityDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Activity activity) {
        em.persist(activity);
    }

    public void edit(Activity activity) throws NonexistentEntityException, Exception {
        activity = em.merge(activity);
    }
    
     public synchronized void editAcceptance(Activity activity) {
        Query q = em.createQuery("update Activity set remark=?1, receiveStatus=?2, updateDate=?3, userReceiverId=?4, receiverName=?5 where id = ?6");
        q.setParameter(1, activity.getRemark());
        q.setParameter(2, activity.getReceiveStatus());
        q.setParameter(3, activity.getUpdateDate());
        q.setParameter(4, activity.getUserReceiverId());
        q.setParameter(5, activity.getReceiverName());
        q.setParameter(6, activity.getId());
        q.executeUpdate();
    }
     
    public void destroy(Integer id) throws NonexistentEntityException {
        Activity activity;
        try {
            activity = em.getReference(Activity.class, id);
            activity.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The activity with id " + id + " no longer exists.", enfe);
        }
        em.remove(activity);
    }

    public void createActivityAttachment(ActivityAttachment activityAttachment) {
        em.persist(activityAttachment);
    }

    public void deleteActivityAttachment(Integer activityId) {
        //delete ActivityAttachment
        Query q = em.createQuery("Delete from ActivityAttachment aa where aa.activity.id = ?1");
        q.setParameter(1, activityId);
        q.executeUpdate();
    }

    public List<Activity> findActivityEntities() {
        return findActivityEntities(true, -1, -1);
    }

    public List<Activity> findActivityEntities(int maxResults, int firstResult) {
        return findActivityEntities(false, maxResults, firstResult);
    }

    private List<Activity> findActivityEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Activity as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Activity findActivity(Integer id) {
        return em.find(Activity.class, id);
    }

    public List<ActivityAttachment> findActivityAttachment(Integer activityId) {
        Query q = em.createQuery("select object(o) from ActivityAttachment as o where o.activity.id = ?1");
        q.setParameter(1, activityId);
        return q.getResultList();
    }

    public int getActivityCount() {
        Query q = em.createQuery("select count(o) from Activity as o");
        return ((Long) q.getSingleResult()).intValue();
    }
}
