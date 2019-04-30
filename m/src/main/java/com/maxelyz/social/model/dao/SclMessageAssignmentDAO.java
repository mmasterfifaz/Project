/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SclMessageAssignment;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Transactional
public class SclMessageAssignmentDAO {

    private static Logger log = Logger.getLogger(SclMessageAssignment.class);
    @PersistenceContext
    private EntityManager em;

    public void create(SclMessageAssignment sclMessageAssignment) throws PreexistingEntityException, Exception {
        em.persist(sclMessageAssignment);
    }

    public void edit(SclMessageAssignment sclMessageAssignment) throws NonexistentEntityException, Exception {
        sclMessageAssignment = em.merge(sclMessageAssignment);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SclMessageAssignment sclMessageAssignment;
        try {
            sclMessageAssignment = em.getReference(SclMessageAssignment.class, id);
            sclMessageAssignment.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The sclMessageAssignment with id " + id + " no longer exists.", enfe);
        }
        em.remove(sclMessageAssignment);
    }

    public List<SclMessageAssignment> findSclMessageAssignmentEntities() {
        return findSclMessageAssignmentEntities(true, -1, -1);
    }

    public List<SclMessageAssignment> findSclMessageAssignmentEntities(int maxResults, int firstResult) {
        return findSclMessageAssignmentEntities(false, maxResults, firstResult);
    }

    private List<SclMessageAssignment> findSclMessageAssignmentEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SclMessageAssignment as o order by o.id");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SclMessageAssignment findSclMessageAssignment(Integer id) {
        return em.find(SclMessageAssignment.class, id);
    }

    public int getSclMessageAssignmentCount() {
        return ((Long) em.createQuery("select count(o) from SclMessageAssignment as o").getSingleResult()).intValue();
    }

}
