/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.DmccontactReason;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DmccontactReasonDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(DmccontactReason dmccontactReason) throws PreexistingEntityException {
        this.validate(dmccontactReason);
        em.persist(dmccontactReason);
    }

    public void edit(DmccontactReason dmccontactReason) {
        dmccontactReason = em.merge(dmccontactReason);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        DmccontactReason dmccontactReason;
        try {
            dmccontactReason = em.getReference(DmccontactReason.class, id);
            dmccontactReason.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The dmccontactReason with id " + id + " no longer exists.", enfe);
        }
        em.remove(dmccontactReason);
    }

    public List<DmccontactReason> findDmccontactReasonEntities() {
        return findDmccontactReasonEntities(true, -1, -1);
    }

    public List<DmccontactReason> findDmccontactReasonEntities(int maxResults, int firstResult) {
        return findDmccontactReasonEntities(false, maxResults, firstResult);
    }

    private List<DmccontactReason> findDmccontactReasonEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from DmccontactReason as o where enable=true");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public DmccontactReason findDmccontactReason(Integer id) {
        return em.find(DmccontactReason.class, id);
    }

    public int getDmccontactReasonCount() {
        Query q = em.createQuery("select count(o) from DmccontactReason as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<DmccontactReason> findDmccontactReasonByCode(String code) {
        Query q = em.createQuery("select object(o) from DmccontactReason as o where code = ?1");
        q.setParameter(1, code);
        return q.getResultList();
    }

    private void validate(DmccontactReason dmccontactReason) throws PreexistingEntityException {
        if (findDmccontactReasonByCode(dmccontactReason.getCode()).size() > 0) {
            throw new PreexistingEntityException("Duplicate Code");
        }
    }
}
