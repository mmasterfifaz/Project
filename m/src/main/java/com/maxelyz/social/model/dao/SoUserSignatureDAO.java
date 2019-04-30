/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoUserSignature;
import com.maxelyz.utils.JSFUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Administrator
 */
@Transactional
public class SoUserSignatureDAO {
    
    @PersistenceContext
    private EntityManager em;

    public void create(SoUserSignature soUserSignature) throws PreexistingEntityException {
        em.persist(soUserSignature);
    }

    public void edit(SoUserSignature soUserSignature) throws NonexistentEntityException, Exception {
        soUserSignature = em.merge(soUserSignature);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoUserSignature soUserSignature;
        try {
            soUserSignature = em.getReference(SoUserSignature.class, id);
            soUserSignature.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soUserSignature with id " + id + " no longer exists.", enfe);
        }
        em.remove(soUserSignature);
    }

    public List<SoUserSignature> findSoUserSignatureEntities() {
        return findSoUserSignatureEntities(true, -1, -1);
    }

    public List<SoUserSignature> findSoUserSignatureEntities(int maxResults, int firstResult) {
        return findSoUserSignatureEntities(false, maxResults, firstResult);
    }

    private List<SoUserSignature> findSoUserSignatureEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoUserSignature as o where o.users.id = ?1 and o.enable = true order by o.seqId, o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        q.setParameter(1, JSFUtil.getUserSession().getUsers().getId());
        return q.getResultList();
    }
    
    public List<SoUserSignature> findSoUserSignatureStatus() {
        Query q = em.createQuery("select object(o) from SoUserSignature as o where o.users.id = ?1 and o.enable = true and o.status = true order by o.seqId, o.name");
        q.setParameter(1, JSFUtil.getUserSession().getUsers().getId());
        return q.getResultList();
    }
    
    public List<SoUserSignature> findSoUserSignatureList() {
        Query q = em.createQuery("select object(o) from SoUserSignature as o where o.users.id = ?1 and o.enable = true order by o.seqId, o.name");
        q.setParameter(1, JSFUtil.getUserSession().getUsers().getId());
        return q.getResultList();
    }
         
    public SoUserSignature findSoUserSignature(Integer id) {
        return em.find(SoUserSignature.class, id);
    }

    public int getSoUserSignatureCount() {
        Query q = em.createQuery("select count(o) from SoUserSignature as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
//    public List<SoUserSignature> findSoUserSignatureByAccountId(Integer emailAccountId) {
//        Query q = em.createQuery("select object(o) from SoUserSignature as o where o.soEmailAccount.soAccount.id = ?1 and o.enable = true order by o.seqId, o.name");
//        q.setParameter(1, emailAccountId);
//        return q.getResultList();
//    }
    
    public int checkEmailSignatureName(String name, Integer id) {
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from SoUserSignature as o where o.users.id = :userId and o.name =:name and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from SoUserSignature as o where o.users.id = :userId and o.name =:name and o.enable = true and o.id <> :id");
            q.setParameter("id", id);
        }
        q.setParameter("userId", JSFUtil.getUserSession().getUsers().getId());
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
