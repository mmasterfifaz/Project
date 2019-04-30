/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.MessageBroadcast;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class MessageBroadcastDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(MessageBroadcast messageBroadcast) {
        em.persist(messageBroadcast);
    }

    public void edit(MessageBroadcast messageBroadcast) {
        messageBroadcast = em.merge(messageBroadcast);

    }

    public void destroy(Integer id) throws NonexistentEntityException {
        MessageBroadcast messageBroadcast;
        try {
            messageBroadcast = em.getReference(MessageBroadcast.class, id);
            messageBroadcast.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The messageboardcast with id " + id + " no longer exists.", enfe);
        }
        em.remove(messageBroadcast);

    }

    public List<MessageBroadcast> findMessageBroadcastEntities() {
        return findMessageBroadcastEntities(true, -1, -1);
    }

    public List<MessageBroadcast> findMessageBroadcastEntities(int maxResults, int firstResult) {
        return findMessageBroadcastEntities(false, maxResults, firstResult);
    }

    private List<MessageBroadcast> findMessageBroadcastEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from MessageBroadcast as o where o.enable=true order by o.priority, o.startDate");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public MessageBroadcast findMessageBroadcast(Integer id) {
        MessageBroadcast messageBroadcast = em.find(MessageBroadcast.class, id);
        messageBroadcast.getUsersCollection().size();
        return messageBroadcast;
    }

    public int getMessageBroadcastCount() {
        return ((Long) em.createQuery("select count(o) from MessageBroadcast as o").getSingleResult()).intValue();
    }

    public List<MessageBroadcast> findMessageBroadcastByName(String keyword) {
        Query q = em.createNamedQuery("MessageBroadcast.findByName");
        q.setParameter("name", keyword);
        return q.getResultList();
    }

    public List<MessageBroadcast> findMessageBroadcastByUser(Users user) {
        if(user != null && user.getUserGroup() != null) {
            String query = "select distinct object(o) from MessageBroadcast as o "
                    + "left join o.usersCollection u "
                    + "left join o.userGroup g "
                    + "where o.enable = true and (u = ?1 or g = ?2 or o.createByUser =?1) " //(u = ?1 or o.allUser = true or g = ?2) 
                    + "and o.startDate <= ?3 and (o.endDate is null or o.endDate >=?3) "
                    + "order by o.priority, o.startDate ";
            Query q = em.createQuery(query);
            q.setParameter(1, user);
            q.setParameter(2, user.getUserGroup());
            q.setParameter(3, JSFUtil.toDateWithoutTime(new Date()));
            return q.getResultList();
        } else {
            return null;
        }
    }
     
     public List<MessageBroadcast> findMessageBroadcastByUserCreate(Users user)  {
         Query q;
         
         if(user.getUserGroup().getRole().contains("CampaignManager")) {
            q = em.createQuery("select object(o) from MessageBroadcast as o where o.enable=true and (o.createByUser = ?1 "
                    + "or o.createByUser.userGroup.parentId = ?2) order by o.priority, o.startDate"); 
            q.setParameter(2, user.getUserGroup().getId()); 
         } else {
            q = em.createQuery("select object(o) from MessageBroadcast as o where o.enable=true and o.createByUser =?1 order by o.priority, o.startDate");
         }
        q.setParameter(1, user);
        return q.getResultList();
    }
}
