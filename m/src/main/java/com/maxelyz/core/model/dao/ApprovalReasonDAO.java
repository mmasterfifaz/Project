/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ApprovalReason;
import com.maxelyz.core.model.entity.NosaleReason;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class ApprovalReasonDAO {

    @PersistenceContext
    private EntityManager em;


    public void create(ApprovalReason approvalReason) throws PreexistingEntityException, Exception {
        em.persist(approvalReason);
    }

    public void edit(ApprovalReason approvalReason) throws NonexistentEntityException, Exception {
        approvalReason = em.merge(approvalReason);    
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        ApprovalReason approvalReason;
        try {
            approvalReason = em.getReference(ApprovalReason.class, id);
            approvalReason.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The approvalReason with id " + id + " no longer exists.", enfe);
        }
        em.remove(approvalReason);
    }

    public List<ApprovalReason> findApprovalReasonEntities() {
        return findApprovalReasonEntities(true, -1, -1);
    }

    public List<ApprovalReason> findApprovalReasonEntities(int maxResults, int firstResult) {
        return findApprovalReasonEntities(false, maxResults, firstResult);
    }

    private List<ApprovalReason> findApprovalReasonEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ApprovalReason as o where o.enable=true order by o.type, o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
    
    public List<ApprovalReason> findApprovalReason(Boolean status, String type) {
        Query q = em.createQuery("select object(o) from ApprovalReason as o where o.enable = true and o.status = ?1 and o.type = ?2 order by o.type, o.code");
        q.setParameter(1, status);
        q.setParameter(2, type);
        return q.getResultList();
    }

    public ApprovalReason findApprovalReason(Integer id) {
        return em.find(ApprovalReason.class, id);
        
    }

    public int getApprovalReasonCount() {
        Query q = em.createQuery("select count(o) from ApprovalReason as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<ApprovalReason> findByStatus(String type, String status) {
        Query q = em.createQuery("select object(o) from ApprovalReason as o where o.enable = true and o.status = true and o.type = ?1 and o.approvalStatus = ?2 order by o.name");
        q.setParameter(1, type);
        q.setParameter(2, status);
        return q.getResultList();
    }

    public Map<String, Integer> getApprovalReasonList(String type, String status) {
        List<ApprovalReason> list = this.findByStatus(type, status);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (ApprovalReason obj : list) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public List<ApprovalReason> findByStatus(String type) {
        Query q = em.createQuery("select object(o) from ApprovalReason as o where o.enable = true and o.status = true and o.type = ?1 order by o.name");
        q.setParameter(1, type);
        return q.getResultList();
    }
    
    public int checkApprovalCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from ApprovalReason as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from ApprovalReason as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
}
