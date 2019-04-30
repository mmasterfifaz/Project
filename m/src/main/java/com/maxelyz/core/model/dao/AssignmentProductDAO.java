/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.AssignmentProduct;
import com.maxelyz.core.model.entity.AssignmentProductPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AssignmentProductDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(AssignmentProduct assignmentProduct) {
        em.persist(assignmentProduct);
    }

    public void edit(AssignmentProduct assignmentProduct) {
        assignmentProduct = em.merge(assignmentProduct);
    }

    public void destroy(AssignmentProductPK id) throws NonexistentEntityException {
        AssignmentProduct assignmentProduct;
        try {
            assignmentProduct = em.getReference(AssignmentProduct.class, id);
            assignmentProduct.getAssignmentProductPK();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The assignmentProduct with id " + id + " no longer exists.", enfe);
        }
        em.remove(assignmentProduct);
    }

    public List<AssignmentProduct> findAssignmentProductEntities() {
        return findAssignmentProductEntities(true, -1, -1);
    }

    public List<AssignmentProduct> findAssignmentProductEntities(int maxResults, int firstResult) {
        return findAssignmentProductEntities(false, maxResults, firstResult);
    }

    private List<AssignmentProduct> findAssignmentProductEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from AssignmentProduct as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public AssignmentProduct findAssignmentProduct(AssignmentProductPK id) {
        return em.find(AssignmentProduct.class, id);
    }

    public int getAssignmentProductCount() {
        return ((Long) em.createQuery("select count(o) from AssignmentProduct as o").getSingleResult()).intValue();
    }
}
