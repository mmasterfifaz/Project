/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoMsgUserWorkingtime;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Transactional
public class SoMsgUserWorkingtimeDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoMsgUserWorkingtime soMsgUserWorkingtime) throws PreexistingEntityException {
//        this.validate(soMsgUserWorkingtime);
        em.persist(soMsgUserWorkingtime);
    }

    public void edit(SoMsgUserWorkingtime soMsgUserWorkingtime) throws NonexistentEntityException, Exception {
        soMsgUserWorkingtime = em.merge(soMsgUserWorkingtime);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoMsgUserWorkingtime soMsgUserWorkingtime;
        try {
            soMsgUserWorkingtime = em.getReference(SoMsgUserWorkingtime.class, id);
            soMsgUserWorkingtime.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soMsgUserWorkingtime with id " + id + " no longer exists.", enfe);
        }
        em.remove(soMsgUserWorkingtime);
    }

    public List<SoMsgUserWorkingtime> findSoMsgUserWorkingtimeEntities() {
        return findSoMsgUserWorkingtimeEntities(true, -1, -1);
    }

    public List<SoMsgUserWorkingtime> findSoMsgUserWorkingtimeEntities(int maxResults, int firstResult) {
        return findSoMsgUserWorkingtimeEntities(false, maxResults, firstResult);
    }

    private List<SoMsgUserWorkingtime> findSoMsgUserWorkingtimeEntities(boolean all, int maxResults, int firstResult) {
//        Query q = em.createQuery("select object(o) from SoMsgUserWorkingtime as o where o.enable = true order by o.name");
        Query q = em.createQuery("select object(o) from SoMsgUserWorkingtime as o order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SoMsgUserWorkingtime findSoMsgUserWorkingtime(Integer id) {
        return em.find(SoMsgUserWorkingtime.class, id);
    }

    public int getSoMsgUserWorkingtimeCount() {
        Query q = em.createQuery("select count(o) from SoMsgUserWorkingtime as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<SoMsgUserWorkingtime> findBysoMessageId(Integer soMessageId) {
        Query q = em.createQuery("select object(o) from SoMsgUserWorkingtime as o where soMessageId = ?1");
        q.setParameter(1, soMessageId);
        return q.getResultList();
    }

    public List<SoMsgUserWorkingtime> findBysoMessageIdOpen(Integer soMessageId) {
        Query q = em.createQuery("select object(o) from SoMsgUserWorkingtime as o where soMessageId = ?1 and turnaroundEnd is null");
        q.setParameter(1, soMessageId);
        return q.getResultList();
    }

}
