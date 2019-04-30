/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ProductChildRegType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author TBN
 */
public class ProductChildRegTypeDAO {
    @PersistenceContext
    private EntityManager em;

    public void create(ProductChildRegType productChildRegType) throws PreexistingEntityException, Exception {
        em.persist(productChildRegType);
    }

    public void edit(ProductChildRegType productChildRegType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        productChildRegType = em.merge(productChildRegType);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        ProductChildRegType productChildRegType;
        try {
            productChildRegType = em.getReference(ProductChildRegType.class, id);
//            productChildRegType.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The product child registration type with id " + id + " no longer exists.", enfe);
        }
        em.remove(productChildRegType);
    }
    
    public ProductChildRegType findProductChildRegType(Integer id) {
        return em.find(ProductChildRegType.class, id);
    }
    
    public List<ProductChildRegType> findProductChildRegTypeEntities() {
        return findProductChildRegTypeEntities(true, -1, -1);
    }

    public List<ProductChildRegType> findProductChildRegTypeEntities(int maxResults, int firstResult) {
        return findProductChildRegTypeEntities(false, maxResults, firstResult);
    }

    private List<ProductChildRegType> findProductChildRegTypeEntities(boolean all, int maxResults, int firstResult) {
        try {
            Query q = em.createQuery("select object(o) from ProductChildRegType as o where o.enable = true order by o.name");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } catch(Exception ex) {
            return null;
        }
    }
    
    public List<ProductChildRegType> findProductChildRegTypeByName(String nameKeyword) {
        Query q = em.createNamedQuery("ProductChildRegType.findByName");
        q.setParameter("name", nameKeyword);
        return q.getResultList();
    }
    
    public List<ProductChildRegType> findProductChildRegTypeByProductId(Integer productId) {
        Query q = em.createQuery("select object(o) from ProductChildRegType as o where o.enable = true and o.product.id = ?1");
        q.setParameter(1, productId);
        return q.getResultList();
    }
    
    public List<ProductChildRegType> findProductChildRegTypeByProductIdAndChildRegTypeID(Integer productId,Integer childRegTypeId) {
        Query q = em.createQuery("select object(o) from ProductChildRegType as o where o.enable = true and o.product.id = ?1 and o.childRegType.id = ?2");
        q.setParameter(1, productId);
        q.setParameter(2, childRegTypeId);
        return q.getResultList();
    }
    
    public int checkProductChildRegTypeName(Integer childRegTypeId,Integer productId) { 
        Query q;
        if(childRegTypeId == 0){
            q = em.createQuery("select count(o) from ProductChildRegType as o where o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from ProductChildRegType as o where o.enable = true and o.childRegType.id = :childRegTypeId and o.product.id = :productId");
            q.setParameter("childRegTypeId", childRegTypeId);
            q.setParameter("productId", productId);
        }
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<ProductChildRegType> checkProductChildRegTypeIsUsed(int id) {
        Query q = em.createQuery("select object(o) from ProductChildRegType as o where o.enable = true and o.product.enable = true and o.childRegType.id = ?1");
        q.setParameter(1, id);
        return q.getResultList();
    }
}
