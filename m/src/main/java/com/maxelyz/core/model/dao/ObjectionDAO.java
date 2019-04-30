/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.Objection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ObjectionDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Objection objection) throws PreexistingEntityException, Exception {
        em.persist(objection);
    }

    public void edit(Objection objection) throws NonexistentEntityException, Exception {
        objection = em.merge(objection);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        Objection objection;
        try {
            objection = em.getReference(Objection.class, id);
            objection.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The objection with id " + id + " no longer exists.", enfe);
        }
        em.remove(objection);
    }

    public List<Objection> findObjectionEntities() {
        return findObjectionEntities(true, -1, -1);
    }

    public List<Objection> findObjectionEntities(int maxResults, int firstResult) {
        return findObjectionEntities(false, maxResults, firstResult);
    }

    private List<Objection> findObjectionEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Objection as o where o.enable = true order by topic");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<Objection> findObjectionByName(String nameKeyword) {
        Query q = em.createQuery("select object(o) from Objection as o where o.topic like ?1 and o.enable = true and o.status = true order by topic");
        q.setParameter(1, "%"+nameKeyword+"%");
        return q.getResultList();
    }

    public Objection findObjection(Integer id) {
        return em.find(Objection.class, id);
    }

    public int getObjectionCount() {
        return ((Long) em.createQuery("select count(o) from Objection as o").getSingleResult()).intValue();
    }
    
    public int checkObjectionTopic(String topic, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from Objection as o where o.topic =:topic and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from Objection as o where o.topic =:topic and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("topic", topic);
        return ((Long) q.getSingleResult()).intValue();
    }
}
