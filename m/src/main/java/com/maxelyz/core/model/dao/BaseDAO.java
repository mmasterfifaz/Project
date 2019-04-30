/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


public class BaseDAO<T extends Object> {

    @PersistenceContext
    protected EntityManager em;

    public void create(T obj) {
        em.persist(obj);
    }

    public void edit(T obj) {
         obj = em.merge(obj);
    }

    public void destroy(Integer id) throws NonexistentEntityException {

    }

    public List<T> findEntities() {
        return findEntities(true, -1, -1);
    }

    public List<T> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    protected List<T> findEntities(boolean all, int maxResults, int firstResult) {
        T obj = null;
        Query q = em.createQuery("select object(o) from "+ obj.getClass() +" as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
}
