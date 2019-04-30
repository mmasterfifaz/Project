/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoTransferReason;
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
public class SoTransferReasonDAO {
    
    @PersistenceContext
    private EntityManager em;

    public void create(SoTransferReason soTransferReason) throws PreexistingEntityException {
        em.persist(soTransferReason);
    }

    public void edit(SoTransferReason soTransferReason) throws NonexistentEntityException, Exception {
        soTransferReason = em.merge(soTransferReason);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoTransferReason soTransferReason;
        try {
            soTransferReason = em.getReference(SoTransferReason.class, id);
            soTransferReason.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soTransferReason with id " + id + " no longer exists.", enfe);
        }
        em.remove(soTransferReason);
    }

    public List<SoTransferReason> findSoTransferReasonEntities() {
        return findSoTransferReasonEntities(true, -1, -1);
    }

    public List<SoTransferReason> findSoTransferReasonEntities(int maxResults, int firstResult) {
        return findSoTransferReasonEntities(false, maxResults, firstResult);
    }

    private List<SoTransferReason> findSoTransferReasonEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoTransferReason as o where o.enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        q.setParameter(1, JSFUtil.getUserSession().getUsers().getId());
        return q.getResultList();
    }
    
//    public List<SoTransferReason> findSoTransferReasonStatus() {
//        Query q = em.createQuery("select object(o) from SoTransferReason as o where o.users.id = ?1 and o.enable = true and o.status = true order by o.seqId, o.name");
//        q.setParameter(1, JSFUtil.getUserSession().getUsers().getId());
//        return q.getResultList();
//    }
    
    public List<SoTransferReason> findSoTransferReasonList() {
        Query q = em.createQuery("select object(o) from SoTransferReason as o where o.enable = true and o.status = true order by o.name");
        return q.getResultList();
    }
    
    public List<SoTransferReason> findSoTransferReasonListOrderByCode() {
        Query q = em.createQuery("select object(o) from SoTransferReason as o where o.enable = true order by o.name");
        return q.getResultList();
    }
        
    public SoTransferReason findSoTransferReason(Integer id) {
        return em.find(SoTransferReason.class, id);
    }

    public int getSoTransferReasonCount() {
        Query q = em.createQuery("select count(o) from SoTransferReason as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public int checkCode(String code, Integer id) {
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from SoTransferReason as o where o.code =:code and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from SoTransferReason as o where o.code =:code and o.enable = true and o.id <> :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
