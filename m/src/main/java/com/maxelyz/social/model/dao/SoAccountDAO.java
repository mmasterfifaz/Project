/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoAccount;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Transactional
public class SoAccountDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoAccount soAccount) throws PreexistingEntityException {
        this.validate(soAccount);
        em.persist(soAccount);
    }

    public void edit(SoAccount soAccount) throws NonexistentEntityException, Exception {
        soAccount = em.merge(soAccount);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoAccount soAccount;
        try {
            soAccount = em.getReference(SoAccount.class, id);
            soAccount.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soAccount with id " + id + " no longer exists.", enfe);
        }
        em.remove(soAccount);
    }

    public List<SoAccount> findSoAccountEntities() {
        return findSoAccountEntities(true, -1, -1);
    }

    public List<SoAccount> findSoAccountEntities(int maxResults, int firstResult) {
        return findSoAccountEntities(false, maxResults, firstResult);
    }

    private List<SoAccount> findSoAccountEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoAccount as o where o.enable = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<SoAccount> findSoAccountStatus() {
        Query q = em.createQuery("select object(o) from SoAccount as o where o.enable = true and o.status = true order by o.code");
        return q.getResultList();
    }

    public List<SoAccount> findSoAccountList() {
        Query q = em.createQuery("select object(o) from SoAccount as o where o.enable = true and o.status = true order by o.name");
        return q.getResultList();
    }

    public SoAccount findSoAccount(Integer id) {
        return em.find(SoAccount.class, id);
    }

    public int getSoAccountCount() {
        Query q = em.createQuery("select count(o) from SoAccount as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public SoAccount find1SoAccountByCode(String code) {
        Query q = em.createQuery("select object(o) from SoAccount as o where code = ?1");
        q.setParameter(1, code);
        return (SoAccount) q.getSingleResult();
    }

    public List<SoAccount> findSoAccountByCode(String code) {
        Query q = em.createQuery("select object(o) from SoAccount as o where code = ?1");
        q.setParameter(1, code);
        return q.getResultList();
    }

    private void validate(SoAccount soAccount) throws PreexistingEntityException {
        if (findSoAccountByCode(soAccount.getCode()).size() > 0) {
            throw new PreexistingEntityException("Duplicate Code");
        }
    }

    public Map<String, Integer> getSoAccountList() {
        List<SoAccount> soAccounts = this.findSoAccountStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoAccount obj : soAccounts) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public int checkSoAccountCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from SoAccount as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from SoAccount as o where o.code =:code and o.enable = true and o.id <> :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<SoAccount> findSoAccountByUser(Integer userId) {
        String sql = "select a.id from so_account a"
                + " inner join so_service_account b on a.id = b.so_account_id"
                + " inner join so_service_user c on c.so_service_id = b.so_service_id"
                + " where c.user_id = ?1"
                + " group by a.id"
                + " order by a.id";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, userId);
        List<Integer> list = q.getResultList();
        List<SoAccount> soAccountList = new ArrayList<SoAccount>();
        for(Integer i : list){
            soAccountList.add(findSoAccount(i));
        }
        return soAccountList;
    }

    public List<SoAccount> findBySoChannelId(Integer userId, Integer soChannelId) {
        String sql = "select a.id from so_account a"
                + " inner join so_service_account b on a.id = b.so_account_id"
                + " inner join so_service_user c on c.so_service_id = b.so_service_id"
                + " where c.user_id = ?1"
                + " and a.so_channel_id = ?2"
                + " group by a.id"
                + " order by a.id";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, userId);
        q.setParameter(2, soChannelId);
        List<Integer> list = q.getResultList();
        List<SoAccount> soAccountList = new ArrayList<SoAccount>();
        for(Integer i : list){
            soAccountList.add(findSoAccount(i));
        }
        return soAccountList;
    }
    
    public List<SoAccount> findSoAccountByEmailAccount() {
        Query q = em.createQuery("select sa from SoEmailAccount o join o.soAccount sa where o.enable = true and o.status = true "
                + "and sa.enable = true order by sa.name");
        return q.getResultList();
    }
        
}
