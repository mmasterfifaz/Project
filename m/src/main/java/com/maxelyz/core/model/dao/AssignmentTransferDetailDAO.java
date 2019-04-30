/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.AssignmentTransfer;
import com.maxelyz.core.model.entity.AssignmentTransferDetail;
import java.util.List;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class AssignmentTransferDetailDAO implements Serializable {

    @PersistenceContext
    private EntityManager em;

    public void create(AssignmentTransferDetail assignmentTransferDetail) {
        em.persist(assignmentTransferDetail);        
    }

    public void edit(AssignmentTransferDetail assignmentTransferDetail) throws NonexistentEntityException, Exception {
        assignmentTransferDetail = em.merge(assignmentTransferDetail);     
    }

   

    public List<AssignmentTransferDetail> findAssignmentTransferDetailEntities() {
        return findAssignmentTransferDetailEntities(true, -1, -1);
    }

    public List<AssignmentTransferDetail> findAssignmentTransferDetailEntities(int maxResults, int firstResult) {
        return findAssignmentTransferDetailEntities(false, maxResults, firstResult);
    }

    private List<AssignmentTransferDetail> findAssignmentTransferDetailEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from AssignmentTransferDetail as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();     
    }

    public AssignmentTransferDetail findAssignmentTransferDetail(Integer id) {
        return em.find(AssignmentTransferDetail.class, id);
    }

    public int getAssignmentTransferDetailCount() {
        Query q = em.createQuery("select count(o) from AssignmentTransferDetail as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
