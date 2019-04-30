/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.IdcardType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class IdcardTypeDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(IdcardType idcardType) {
        em.persist(idcardType);
    }

    public void edit(IdcardType idcardType) {
        idcardType = em.merge(idcardType);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        IdcardType idcardType;
        try {
            idcardType = em.getReference(IdcardType.class, id);
            idcardType.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The idcardType with id " + id + " no longer exists.", enfe);
        }
        em.remove(idcardType);
    }

    public List<IdcardType> findIdcardTypeEntities() {
        return findIdcardTypeEntities(true, -1, -1);
    }

    public List<IdcardType> findIdcardTypeEntities(int maxResults, int firstResult) {
        return findIdcardTypeEntities(false, maxResults, firstResult);
    }

    private List<IdcardType> findIdcardTypeEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from IdcardType as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public IdcardType findIdcardType(Integer id) {
        return em.find(IdcardType.class, id);
    }

    public int getIdcardTypeCount() {
        return ((Long) em.createQuery("select count(o) from IdcardType as o").getSingleResult()).intValue();
    }
}
