/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.CallsuccessReason;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CallsuccessReasonDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(CallsuccessReason callsuccessReason) throws Exception {
        this.validate(callsuccessReason);
        em.persist(callsuccessReason);
  
    }

    public void edit(CallsuccessReason callsuccessReason) throws Exception {
        //this.validate(callsuccessReason);
        callsuccessReason = em.merge(callsuccessReason);
    }

    public List<CallsuccessReason> findCallsuccessReasonEntities() {
        return findCallsuccessReasonEntities(true, -1, -1);
    }

    public List<CallsuccessReason> findCallsuccessReasonEntities(int maxResults, int firstResult) {
        return findCallsuccessReasonEntities(false, maxResults, firstResult);
    }

    private List<CallsuccessReason> findCallsuccessReasonEntities(boolean all, int maxResults, int firstResult) {      
        Query q = em.createQuery("select object(o) from CallsuccessReason as o where enable = true order by code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public CallsuccessReason findCallsuccessReason(Integer id) {
        return em.find(CallsuccessReason.class, id);
    }

    public List<CallsuccessReason> findCallsuccessReasonByCode(String code) {    
        Query q = em.createQuery("select object(o) from CallsuccessReason as o where code = ?1");
        q.setParameter(1, code);
        return q.getResultList();
    }

    public int getCallsuccessReasonCount() {     
        Query q = em.createQuery("select count(o) from CallsuccessReason as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    private void validate(CallsuccessReason callsuccessReason) throws PreexistingEntityException {
         if (findCallsuccessReasonByCode(callsuccessReason.getCode()).size()>0)
            throw new PreexistingEntityException("Duplicate Code");
    }

}
