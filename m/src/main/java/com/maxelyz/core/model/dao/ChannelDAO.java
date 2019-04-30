/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Channel;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.ContactCase;
import java.util.ArrayList;
import java.util.Collection;
import com.maxelyz.core.model.entity.Activity;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ChannelDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Channel channel) {
        em.persist(channel);
    }

    public void edit(Channel channel) throws IllegalOrphanException, NonexistentEntityException, Exception {
        channel = em.merge(channel);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        Channel channel;
        try {
            channel = em.getReference(Channel.class, id);
            channel.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The channel with id " + id + " no longer exists.", enfe);
        }
        em.remove(channel);
    }

    public List<Channel> findChannelEntities() {
        return findChannelEntities(true, -1, -1);
    }

    public List<Channel> findChannelEntities(int maxResults, int firstResult) {
        return findChannelEntities(false, maxResults, firstResult);
    }

    private List<Channel> findChannelEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Channel as o order by o.type, o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Channel findChannel(Integer id) {
        return em.find(Channel.class, id);
    }

    public int getChannelCount() {
        Query q = em.createQuery("select count(o) from Channel as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    
    public Map<String, Integer> getChannelList() {
        List<Channel> channels = this.findChannelEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Channel obj : channels) {
            values.put(obj.getType()+":"+obj.getName(), obj.getId());
        }
        return values;
    }
    
    public List<String> findChannelTypeList() {
        Query q = em.createQuery("select distinct o.type from Channel as o group by o.type");       
        return q.getResultList();
    }

}
