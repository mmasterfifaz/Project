/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.FollowupsaleReason;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class FollowupsaleReasonDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(FollowupsaleReason followupsaleReason) throws PreexistingEntityException, Exception {
//        this.validate(followupsaleReason);
        em.persist(followupsaleReason);
    }

    public void edit(FollowupsaleReason followupsaleReason) throws NonexistentEntityException, Exception {
        followupsaleReason = em.merge(followupsaleReason);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        FollowupsaleReason followupsaleReason;
        try {
            followupsaleReason = em.getReference(FollowupsaleReason.class, id);
            followupsaleReason.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The followupsaleReason with id " + id + " no longer exists.", enfe);
        }
        em.remove(followupsaleReason);
    }

    public List<FollowupsaleReason> findFollowupsaleReasonEntities() {
        return findFollowupsaleReasonEntities(true, -1, -1);
    }

    public List<FollowupsaleReason> findFollowupsaleReasonEntities(int maxResults, int firstResult) {
        return findFollowupsaleReasonEntities(false, maxResults, firstResult);
    }

    private List<FollowupsaleReason> findFollowupsaleReasonEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from FollowupsaleReason as o where enable = true order by code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public FollowupsaleReason findFollowupsaleReason(Integer id) {
        return em.find(FollowupsaleReason.class, id);
    }

    public List<FollowupsaleReason> findFollowupsaleReasonByCode(String code) {
        Query q = em.createQuery("select object(o) from FollowupsaleReason as o where code = ?1");
        q.setParameter(1, code);
        return q.getResultList();
    }

    public int getFollowupsaleReasonCount() {
        return ((Long) em.createQuery("select count(o) from FollowupsaleReason as o").getSingleResult()).intValue();
    }

    private void validate(FollowupsaleReason followupsaleReason) throws PreexistingEntityException {
        if (findFollowupsaleReasonByCode(followupsaleReason.getCode()).size() > 0) {
            throw new PreexistingEntityException("Duplicate Code");
        }
    }
    
    public int checkFollowupsaleReasonCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from FollowupsaleReason as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from FollowupsaleReason as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
}
