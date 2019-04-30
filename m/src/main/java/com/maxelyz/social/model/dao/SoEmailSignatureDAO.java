/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoEmailSignature;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Transactional
public class SoEmailSignatureDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoEmailSignature soEmailSignature) throws PreexistingEntityException {
        em.persist(soEmailSignature);
    }

    public void edit(SoEmailSignature soEmailSignature) throws NonexistentEntityException, Exception {
        soEmailSignature = em.merge(soEmailSignature);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoEmailSignature soEmailSignature;
        try {
            soEmailSignature = em.getReference(SoEmailSignature.class, id);
            soEmailSignature.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soEmailSignature with id " + id + " no longer exists.", enfe);
        }
        em.remove(soEmailSignature);
    }

    public List<SoEmailSignature> findSoEmailSignatureEntities() {
        return findSoEmailSignatureEntities(true, -1, -1);
    }

    public List<SoEmailSignature> findSoEmailSignatureEntities(int maxResults, int firstResult) {
        return findSoEmailSignatureEntities(false, maxResults, firstResult);
    }

    private List<SoEmailSignature> findSoEmailSignatureEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoEmailSignature as o where o.enable = true order by o.seqId, o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
    
    public List<SoEmailSignature> findSoEmailSignatureStatus() {
        Query q = em.createQuery("select object(o) from SoEmailSignature as o where o.enable = true and o.status = true order by o.seqId, o.name");
        return q.getResultList();
    }
    
    public List<SoEmailSignature> findSoEmailAccountSignatureList() {
        Query q = em.createQuery("select object(o) from SoEmailSignature o join o.soEmailAccount e join e.soAccount a "
                + "where o.enable = true and e.enable = true and e.status = true and a.enable = true and a.status = true order by o.seqId, o.name");
        return q.getResultList();
    }
    
    public List<SoEmailSignature> findSoEmailSignatureList() {
        Query q = em.createQuery("select object(o) from SoEmailSignature as o where o.enable = true order by o.seqId, o.name");
        return q.getResultList();
    }
         
    public SoEmailSignature findSoEmailSignature(Integer id) {
        return em.find(SoEmailSignature.class, id);
    }

    public int getSoEmailSignatureCount() {
        Query q = em.createQuery("select count(o) from SoEmailSignature as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<SoEmailSignature> findSoEmailSignatureByAccountId(Integer emailAccountId) {
        Query q = em.createQuery("select object(o) from SoEmailSignature as o where o.soEmailAccount.soAccount.id = ?1 and o.enable = true order by o.seqId, o.name");
        q.setParameter(1, emailAccountId);
        return q.getResultList();
    }
    
    /*
         select * from so_email_signature s join so_email_account a on s.so_email_account_id = a.id 
         where a.so_account_id = 15 or a.so_account_id = 20
     */
    
    public int checkEmailSignatureName(String name, Integer id) {
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from SoEmailSignature as o where o.name =:name and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from SoEmailSignature as o where o.name =:name and o.enable = true and o.id <> :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
}
