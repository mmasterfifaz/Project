/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Bank;
import com.maxelyz.core.model.entity.ProductWorkflow;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author ukritj
 */
@Transactional
public class ProductWorkflowDAO {

    @PersistenceContext
    protected EntityManager em;


    public void create(ProductWorkflow productWorkflow) {
        em.persist(productWorkflow);           
    }

    public void edit(ProductWorkflow productWorkflow) throws IllegalOrphanException, NonexistentEntityException, Exception {
        productWorkflow = em.merge(productWorkflow);  
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        ProductWorkflow productWorkflow;
        try {
            productWorkflow = em.getReference(ProductWorkflow.class, id);
            productWorkflow.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The ProductWorkflow with id " + id + " no longer exists.", enfe);
        }
        em.remove(productWorkflow);         
    }

    public List<ProductWorkflow> findProductWorkflowEntities() {
        return findProductWorkflowEntities(true, -1, -1);
    }

    public List<ProductWorkflow> findProductWorkflowEntities(int maxResults, int firstResult) {
        return findProductWorkflowEntities(false, maxResults, firstResult);
    }

    private List<ProductWorkflow> findProductWorkflowEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ProductWorkflow as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public ProductWorkflow findProductWorkflow(Integer id) throws Exception {
        return em.find(ProductWorkflow.class, id);

    }

    public List<ProductWorkflow> findProductWorkflowByProductId(Integer productId) {
        String sql = "select object(o) from ProductWorkflow as o where o.product.id = :productId order by o.stepNo";
        Query q = em.createQuery(sql);
        q.setParameter("productId", productId);
        return q.getResultList();
    }

    public int getProductWorkflowCount() {
        Query q = em.createQuery("select count(o) from ProductWorkflow as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
