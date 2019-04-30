/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Relationship;
import java.util.List;
import javax.persistence.EntityManager;

import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;

import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RelationshipDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Relationship relationship) {
        em.persist(relationship);
    }

    public void edit(Relationship relationship) throws IllegalOrphanException, NonexistentEntityException, Exception {
        relationship = em.merge(relationship);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        Relationship relationship;
        try {
            relationship = em.getReference(Relationship.class, id);
            relationship.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The relationship with id " + id + " no longer exists.", enfe);
        }
        em.remove(relationship);
    }

    public List<Relationship> findRelationshipEntities() {
        return findRelationshipEntities(true, -1, -1);
    }

    public List<Relationship> findRelationshipEntities(int maxResults, int firstResult) {
        return findRelationshipEntities(false, maxResults, firstResult);
    }

    private List<Relationship> findRelationshipEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Relationship as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Relationship findRelationship(Integer id) {
        return em.find(Relationship.class, id);
    }

    public int getRelationshipCount() {
        Query q = em.createQuery("select count(o) from Relationship as o");
        return ((Long) q.getSingleResult()).intValue();
    }
}
