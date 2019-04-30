/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoAccount;
import com.maxelyz.social.model.entity.SoEmailAccount;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Transactional
public class SoEmailAccountDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoEmailAccount soEmailAccount) throws PreexistingEntityException {
        em.persist(soEmailAccount);
    }

    public void edit(SoEmailAccount soEmailAccount) throws NonexistentEntityException, Exception {
        soEmailAccount = em.merge(soEmailAccount);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoEmailAccount soEmailAccount;
        try {
            soEmailAccount = em.getReference(SoEmailAccount.class, id);
            soEmailAccount.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soEmailAccount with id " + id + " no longer exists.", enfe);
        }
        em.remove(soEmailAccount);
    }

    public List<SoEmailAccount> findSoEmailAccountEntities() {
        return findSoEmailAccountEntities(true, -1, -1);
    }

    public List<SoEmailAccount> findSoEmailAccountEntities(int maxResults, int firstResult) {
        return findSoEmailAccountEntities(false, maxResults, firstResult);
    }

    private List<SoEmailAccount> findSoEmailAccountEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoEmailAccount as o where o.enable = true order by o.username");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
    
    public List<SoEmailAccount> findSoEmailAccountStatus() {
        Query q = em.createQuery("select object(o) from SoEmailAccount as o where o.enable = true and o.status = true order by o.username");
        return q.getResultList();
    }

    public List<SoEmailAccount> findSoEmailAccountList() {
        Query q = em.createQuery("select object(o) from SoEmailAccount as o where o.enable = true order by o.username");
        return q.getResultList();
    }
         
    public List<SoAccount> findSoAccountList() {
        Query q = em.createQuery("select sa from SoEmailAccount o join o.soAccount sa where o.enable = true and o.status = true "
                + "and sa.enable = true and sa.status = true order by sa.name");
        return q.getResultList();
    }
        
    public SoEmailAccount findSoEmailAccount(Integer id) {
        return em.find(SoEmailAccount.class, id);
    }

    public int getSoEmailAccountCount() {
        Query q = em.createQuery("select count(o) from SoEmailAccount as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public Map<String, Integer> getEmailAccountList() {
        List<SoEmailAccount> soEmailAccounts = this.findSoEmailAccountStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoEmailAccount obj : soEmailAccounts) {
            values.put(obj.getUsername(), obj.getId());
        }
        return values;
    }
    
    public Map<String, Integer> getAccountList() {
        List<SoAccount> soAccounts = this.findSoAccountList();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoAccount obj : soAccounts) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public SoEmailAccount findSoEmailAccountBySoAccountId(Integer soAccountId) {
        Query q = em.createQuery("select object(o) from SoEmailAccount as o where o.soAccount.id = ?1");
        q.setParameter(1, soAccountId);
        return (SoEmailAccount) q.getSingleResult();
    }
}
