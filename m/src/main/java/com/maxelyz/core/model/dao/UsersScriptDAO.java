/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.UsersScript;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UsersScriptDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(UsersScript usersScript) throws PreexistingEntityException, Exception {
        em.persist(usersScript);
    }

    public void edit(UsersScript usersScript) throws NonexistentEntityException, Exception {
        usersScript = em.merge(usersScript);
    }

    public void destroy(Long id) throws NonexistentEntityException {
        UsersScript usersScript;
        try {
            usersScript = em.getReference(UsersScript.class, id);
            usersScript.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The userscript with id " + id + " no longer exists.", enfe);
        }
        em.remove(usersScript);
    }

    public List<UsersScript> findUsersScriptEntities() {
        return findUsersScriptEntities(true, -1, -1);
    }

    public List<UsersScript> findUsersScriptEntities(int maxResults, int firstResult) {
        return findUsersScriptEntities(false, maxResults, firstResult);
    }

    private List<UsersScript> findUsersScriptEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from UsersScript as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();

    }

    public UsersScript findUsersScript(Long id) {
        return em.find(UsersScript.class, id);
    }

    public int getUsersScriptCount() {
        Query q = em.createQuery("select count(o) from UsersScript as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<UsersScript> findUsersScriptByUserId(Integer userId) { 
        Query q = em.createQuery("select object(o) from UsersScript as o where o.users.id = ?1");
        q.setParameter(1, userId);
        return q.getResultList();
    }

    public List<UsersScript> findUsersScriptByKeyword(Integer userId, String keyword) {
        Query q = em.createQuery("select object(o) from UsersScript as o where o.users.id = ?1 and (o.topic like ?2 or o.description like ?2)");
        q.setParameter(1, userId);
        q.setParameter(2, "%"+keyword+"%");
        return q.getResultList();
    }
    
}
