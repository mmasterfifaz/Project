/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SclUsers;
import com.maxelyz.utils.JSFUtil;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;

@Transactional
public class SclUsersDAO {

    private static Logger log = Logger.getLogger(SclUsers.class);
    @PersistenceContext
    private EntityManager em;

    public void create(SclUsers sclUsers) throws PreexistingEntityException, Exception {
        em.persist(sclUsers);
    }

    public void edit(SclUsers sclUsers) throws NonexistentEntityException, Exception {
        sclUsers = em.merge(sclUsers);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SclUsers sclUsers;
        try {
            sclUsers = em.getReference(SclUsers.class, id);
            sclUsers.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The sclUsers with id " + id + " no longer exists.", enfe);
        }
        em.remove(sclUsers);
    }

    public List<SclUsers> findSclUsersEntities() {
        return findSclUsersEntities(true, -1, -1);
    }

    public List<SclUsers> findSclUsersEntities(int maxResults, int firstResult) {
        return findSclUsersEntities(false, maxResults, firstResult);
    }

    private List<SclUsers> findSclUsersEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SclUsers as o order by o.id");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SclUsers findSclUsers(Integer id) {
        return em.find(SclUsers.class, id);
    }

    public int getSclUsersCount() {
        return ((Long) em.createQuery("select count(o) from SclUsers as o").getSingleResult()).intValue();
    }

    public SclUsers findSclUsersByUserId(BigInteger userId) {
//        String sql = "select object(o) from SclMessage o "
//                + " where o.relatedId = ?1"
//                + " order by o.activityTime";
        String sql = "select object(o) from SclUsers o where o.userId = ?1";
        Query q = em.createQuery(sql);
        q.setMaxResults(1);

        q.setParameter(1, userId);
        return (SclUsers) q.getSingleResult();
    }
}
