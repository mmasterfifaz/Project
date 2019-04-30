/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoAccount;
import com.maxelyz.social.model.entity.SoAccountUser;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Transactional
public class SoAccountUserDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoAccountUser soAccountUser) throws PreexistingEntityException {
//        this.validate(soAccountUser);
        em.persist(soAccountUser);
    }

    public void edit(SoAccountUser soAccountUser) throws NonexistentEntityException, Exception {
        soAccountUser = em.merge(soAccountUser);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoAccountUser soAccountUser;
        try {
            soAccountUser = em.getReference(SoAccountUser.class, id);
            soAccountUser.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soAccountUser with id " + id + " no longer exists.", enfe);
        }
        em.remove(soAccountUser);
    }

//    public List<SoAccountUser> findSoAccountUserEntities() {
//        return findSoAccountUserEntities(true, -1, -1);
//    }
//
//    public List<SoAccountUser> findSoAccountUserEntities(int maxResults, int firstResult) {
//        return findSoAccountUserEntities(false, maxResults, firstResult);
//    }
//
//    private List<SoAccountUser> findSoAccountUserEntities(boolean all, int maxResults, int firstResult) {
//        Query q = em.createQuery("select object(o) from SoAccountUser as o where o.enable = true order by o.name");
//        if (!all) {
//            q.setMaxResults(maxResults);
//            q.setFirstResult(firstResult);
//        }
//        return q.getResultList();
//    }
//
//    public List<SoAccountUser> findSoAccountUserStatus() {
//        Query q = em.createQuery("select object(o) from SoAccountUser as o where o.enable = true and o.status = true order by o.seqNo");
//        return q.getResultList();
//    }

    public List<SoAccountUser> findAll() {
        Query q = em.createQuery("select object(o) from SoAccountUser as o order by o.soAccount.code, o.userName");
        return q.getResultList();
    }

    public SoAccountUser findSoAccountUser(Integer id) {
        return em.find(SoAccountUser.class, id);
    }

    public int getSoAccountUserCount() {
        Query q = em.createQuery("select count(o) from SoAccountUser as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<SoAccountUser> findSoAccountUserByName(String name) {
        Query q = em.createQuery("select object(o) from SoAccountUser as o where userName = ?1");
        q.setParameter(1, name);
        return q.getResultList();
    }

    public List<SoAccountUser> findSoAccountUserBySoAccount(SoAccount soAccount) {
        Query q = null;
        if (soAccount == null) {
            q = em.createQuery("select object(o) from SoAccountUser as o order by o.userName");
        } else {
            q = em.createQuery("select object(o) from SoAccountUser as o where soAccount = ?1 order by o.userName");
            q.setParameter(1, soAccount);
        }

        return q.getResultList();
    }

//    private void validate(SoAccountUser soAccountUser) throws PreexistingEntityException {
//        if (findSoAccountUserByName(soAccountUser.getName()).size() > 0) {
//            throw new PreexistingEntityException("Duplicate Code");
//        }
//    }

    public Map<String, Integer> getSoAccountUserList(SoAccount soAccount) {
        List<SoAccountUser> soAccountUsers = this.findSoAccountUserBySoAccount(soAccount);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoAccountUser obj : soAccountUsers) {
            values.put(obj.getUserName(), obj.getId());
        }
        return values;
    }
    
//    public int checkSoAccountUserName(String name, Integer id) {
//        Query q;
//        if(id == 0){
//            q = em.createQuery("select count(o) from SoAccountUser as o where o.name =:name and o.enable = true");
//        } else {
//            q = em.createQuery("select count(o) from SoAccountUser as o where o.name =:name and o.enable = true and o.id <> :id");
//            q.setParameter("id", id);
//        }
//        q.setParameter("name", name);
//        return ((Long) q.getSingleResult()).intValue();
//    }
        
}
