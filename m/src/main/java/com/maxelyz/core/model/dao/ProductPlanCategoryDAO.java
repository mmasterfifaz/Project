/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ProductPlanCategory;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class ProductPlanCategoryDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(ProductPlanCategory productPlanCategory) throws PreexistingEntityException, Exception {
        em.persist(productPlanCategory);
    }

    public void edit(ProductPlanCategory productPlanCategory) throws NonexistentEntityException, Exception {

        productPlanCategory = em.merge(productPlanCategory);
         
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        ProductPlanCategory productPlanCategory;
        try {
            productPlanCategory = em.getReference(ProductPlanCategory.class, id);
            productPlanCategory.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The productPlanCategory with id " + id + " no longer exists.", enfe);
        }
        em.remove(productPlanCategory);
    }

    public List<ProductPlanCategory> findProductPlanCategoryEntities() {
        return findProductPlanCategoryEntities(true, -1, -1);
    }

    public List<ProductPlanCategory> findProductPlanCategoryEntities(int maxResults, int firstResult) {
        return findProductPlanCategoryEntities(false, maxResults, firstResult);
    }

    private List<ProductPlanCategory> findProductPlanCategoryEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ProductPlanCategory as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
      
    }

    public ProductPlanCategory findProductPlanCategory(Integer id) {
        return em.find(ProductPlanCategory.class, id);
    }

    public int getProductPlanCategoryCount() {
        Query q = em.createQuery("select count(o) from ProductPlanCategory as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
