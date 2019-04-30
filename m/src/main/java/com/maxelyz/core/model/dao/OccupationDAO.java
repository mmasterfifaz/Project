/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.Occupation;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.OccupationCategory;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class OccupationDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Occupation occupation) throws PreexistingEntityException, Exception {
        em.persist(occupation);

    }

    public void edit(Occupation occupation) throws NonexistentEntityException, Exception {
        occupation = em.merge(occupation);

    }

    public List<Occupation> findOccupationEntities() {
        return findOccupationEntities(true, -1, -1);
    }

    public List<Occupation> findOccupationEntities(int maxResults, int firstResult) {
        return findOccupationEntities(false, maxResults, firstResult);
    }

    private List<Occupation> findOccupationEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Occupation as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();

    }

    public Occupation findOccupation(Integer id) {
        return em.find(Occupation.class, id);
    }

    public int getOccupationCount() {
        Query q = em.createQuery("select count(o) from Occupation as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Occupation> findOccupationByOccupationCategoryId(Integer occupationCategoryId) {
         Query q = em.createQuery("select object(o) from Occupation as o where o.occupationCategory.id = ?1 order by o.name");
         q.setParameter(1, occupationCategoryId);
         return q.getResultList();
    }

    public List findOccupationByOccupationCategoryId1(Integer occupationCategoryId) {
         Query q = em.createQuery("select o.id, o.name from Occupation as o where o.occupationCategory.id = ?1 order by o.name");
         q.setParameter(1, occupationCategoryId);
         return q.getResultList();
    }

    public Map<String, Integer> getOccupationList() {
        List<Occupation> list = this.findOccupationEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Occupation obj : list) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
}
