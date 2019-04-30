/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.Acl;
import javax.persistence.PersistenceContext;

/**
 *
 * @author admin
 */
public class AclDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Acl acl) {
        em.persist(acl);
    }

    public void edit(Acl acl) {
        acl = em.merge(acl);
    }

    public void destroy(String code) throws NonexistentEntityException {
        Acl acl;
        try {
            acl = em.getReference(Acl.class, code);
            acl.getCode();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The Acl with id " + code + " no longer exists.", enfe);
        }
        em.remove(acl);
    }

    public List<Acl> findAclEntities() {
        return findAclEntities(true, -1, -1);
    }

    public List<Acl> findAclEntities(int maxResults, int firstResult) {
        return findAclEntities(false, maxResults, firstResult);
    }

    private List<Acl> findAclEntities(boolean all, int maxResults, int firstResult) {
        try {
            Query q = em.createQuery("select object(o) from Acl as o order by o.seqNo");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Acl findAcl(String id) {
        try {
            return em.find(Acl.class, id);
        } finally {
            em.close();
        }
    }

    public List<Acl> findByParentCode(String code) {
        try {
            Query q = em.createQuery("select object(o) from Acl as o where o.parentCode.code = ?1 order by o.code");
            q.setParameter(1, code);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public int getAclCount() {
        try {
            Query q = em.createQuery("select count(o) from Acl as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
