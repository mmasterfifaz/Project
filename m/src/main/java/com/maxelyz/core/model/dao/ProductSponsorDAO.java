/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ProductSponsor;
import com.maxelyz.core.model.entity.ProductSponsorUserGroup;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ProductSponsorDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(ProductSponsor productSponsor) throws PreexistingEntityException, Exception {
        em.persist(productSponsor);
    }

    public void edit(ProductSponsor productSponsor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        productSponsor = em.merge(productSponsor);
    }
    
    public void create(ProductSponsorUserGroup productSponsorUserGroup) throws PreexistingEntityException, Exception {
        em.persist(productSponsorUserGroup);
    }

    public void edit(ProductSponsorUserGroup productSponsorUserGroup) throws IllegalOrphanException, NonexistentEntityException, Exception {
        productSponsorUserGroup = em.merge(productSponsorUserGroup);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        ProductSponsor productSponsor;
        try {
            productSponsor = em.getReference(ProductSponsor.class, id);
            productSponsor.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The product sponsor with id " + id + " no longer exists.", enfe);
        }
        em.remove(productSponsor);
    }

    public List<ProductSponsor> findProductSponsorEntities() {
        return findProductSponsorEntities(true, -1, -1);
    }

    public List<ProductSponsor> findProductSponsorEntities(int maxResults, int firstResult) {
        return findProductSponsorEntities(false, maxResults, firstResult);
    }

    private List<ProductSponsor> findProductSponsorEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ProductSponsor as o where enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public ProductSponsor findProductSponsor(Integer id) {
        return em.find(ProductSponsor.class, id);
    }

    public int getProductSponsorCount() {
        return ((Long) em.createQuery("select count(o) from ProductSponsor as o").getSingleResult()).intValue();
    }

    public List<ProductSponsor> findProductSponsorByName(String nameKeyword) {
        Query q = em.createNamedQuery("ProductSponsor.findByName");
        q.setParameter("name", nameKeyword);
        return q.getResultList();
    }
    
    public List<ProductSponsor> findBy(String keyword) {
        String sql = "select object(o) from ProductSponsor as o where o.enable = true";
        if(!keyword.isEmpty()){
            sql += " and o.name like :keyword";
        }
        sql += " order by o.name";
        Query q = em.createQuery(sql);
        if(!keyword.isEmpty()){
            q.setParameter("keyword", "%" + keyword + "%");
        }
        return q.getResultList();
    }
    
    public int checkProductName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from ProductSponsor as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from ProductSponsor as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public void destroyByProspectSponsorId(int id) {
       Query q = em.createQuery("delete from ProductSponsorUserGroup as o where o.productSponsorUserGroupPK.productSponsorId = ?1");
       q.setParameter(1, id);
       q.executeUpdate();
    }
    
    public List<ProductSponsorUserGroup> findProductSponsorUserGroupEntitiesByProspectSponsorId(int id) {
        Query q = em.createQuery("select object(o) from ProductSponsorUserGroup as o where o.productSponsorUserGroupPK.productSponsorId = ?1");
        q.setParameter(1, id);
        return q.getResultList();
    }
}
