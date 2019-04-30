/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.dao;

import com.maxelyz.core.controller.admin.exceptions.NonexistentEntityException;
import com.maxelyz.core.controller.admin.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.UnassignmentDetail;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.Unassignment;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Oat
 */
public class UnassignmentDetailDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(UnassignmentDetail unassignmentDetail) throws PreexistingEntityException {
            em.persist(unassignmentDetail);    
    }

    public void edit(UnassignmentDetail unassignmentDetail) throws NonexistentEntityException, Exception {
            unassignmentDetail = em.merge(unassignmentDetail);
    }


    public List<UnassignmentDetail> findUnassignmentDetailEntities() {
        return findUnassignmentDetailEntities(true, -1, -1);
    }

    public List<UnassignmentDetail> findUnassignmentDetailEntities(int maxResults, int firstResult) {
        return findUnassignmentDetailEntities(false, maxResults, firstResult);
    }

    private List<UnassignmentDetail> findUnassignmentDetailEntities(boolean all, int maxResults, int firstResult) {
            Query q = em.createQuery("select object(o) from UnassignmentDetail as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
    }

    public UnassignmentDetail findUnassignmentDetail(Integer id) {
            return em.find(UnassignmentDetail.class, id);
    }

    public int getUnassignmentDetailCount() {
            Query q = em.createQuery("select count(o) from UnassignmentDetail as o");
            return ((Long) q.getSingleResult()).intValue();
    }

}
