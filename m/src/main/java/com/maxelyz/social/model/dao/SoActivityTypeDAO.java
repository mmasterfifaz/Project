/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoActivityType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Transactional
public class SoActivityTypeDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoActivityType soActivityType) throws PreexistingEntityException {
        em.persist(soActivityType);
    }

    public void edit(SoActivityType soActivityType) throws NonexistentEntityException, Exception {
        em.merge(soActivityType);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoActivityType soActivityType;
        try {
            soActivityType = em.getReference(SoActivityType.class, id);
            soActivityType.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soActivityType with id " + id + " no longer exists.", enfe);
        }
        em.remove(soActivityType);
    }

    public List<SoActivityType> findSoActivityTypeEntities() {
        return findSoActivityTypeEntities(true, -1, -1);
    }

    public List<SoActivityType> findSoActivityTypeEntities(int maxResults, int firstResult) {
        return findSoActivityTypeEntities(false, maxResults, firstResult);
    }

    private List<SoActivityType> findSoActivityTypeEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoActivityType as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SoActivityType findSoActivityType(Integer id) {
        return em.find(SoActivityType.class, id);
    }

}
