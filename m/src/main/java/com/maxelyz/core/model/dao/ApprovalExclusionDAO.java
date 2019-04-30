/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.ApprovalExclusion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.PurchaseOrderApprovalLog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class ApprovalExclusionDAO  {

    @PersistenceContext
    private EntityManager em;

    public void create(ApprovalExclusion approvalExclusion) {  
           em.persist(approvalExclusion);        
    }

    public void edit(ApprovalExclusion approvalExclusion) throws NonexistentEntityException, Exception {
        approvalExclusion = em.merge(approvalExclusion);
    }


    public List<ApprovalExclusion> findApprovalExclusionEntities() {
        return findApprovalExclusionEntities(true, -1, -1);
    }

    public List<ApprovalExclusion> findApprovalExclusionEntities(int maxResults, int firstResult) {
        return findApprovalExclusionEntities(false, maxResults, firstResult);
    }

    private List<ApprovalExclusion> findApprovalExclusionEntities(boolean all, int maxResults, int firstResult) {
       
            Query q = em.createQuery("select object(o) from ApprovalExclusion as o order by o.code ");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();   
    }

    public ApprovalExclusion findApprovalExclusion(Integer id) {    
            return em.find(ApprovalExclusion.class, id);   
    }

    public int getApprovalExclusionCount() {
            Query q = em.createQuery("select count(o) from ApprovalExclusion as o");
            return ((Long) q.getSingleResult()).intValue();    
    }
    
}
