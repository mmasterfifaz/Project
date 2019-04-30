/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.OccupationCategory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.Occupation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class OccupationCategoryDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(OccupationCategory occupationCategory) throws PreexistingEntityException, Exception {
        em.persist(occupationCategory);
    }

    public void edit(OccupationCategory occupationCategory) throws IllegalOrphanException, NonexistentEntityException, Exception {
        occupationCategory = em.merge(occupationCategory);
    }

    public List<OccupationCategory> findOccupationCategoryEntities() {
        return findOccupationCategoryEntities(true, -1, -1);
    }

    public List<OccupationCategory> findOccupationCategoryEntities(int maxResults, int firstResult) {
        return findOccupationCategoryEntities(false, maxResults, firstResult);
    }

    private List<OccupationCategory> findOccupationCategoryEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from OccupationCategory as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public OccupationCategory findOccupationCategory(Integer id) {
        return em.find(OccupationCategory.class, id);
    }

    public int getOccupationCategoryCount() {
         Query q = em.createQuery("select count(o) from OccupationCategory as o");
         return ((Long) q.getSingleResult()).intValue();
    }

}
