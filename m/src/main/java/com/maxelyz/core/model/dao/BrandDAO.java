/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Brand;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class BrandDAO {

    @PersistenceContext
    protected EntityManager em;


    public void create(Brand brand) {
        em.persist(brand);           
    }

    public void edit(Brand brand) throws IllegalOrphanException, NonexistentEntityException, Exception {
        brand = em.merge(brand);  
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        Brand brand;
        try {
            brand = em.getReference(Brand.class, id);
            brand.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The brand with id " + id + " no longer exists.", enfe);
        }
        em.remove(brand);         
    }

    public List<Brand> findBrandEntities() {
        return findBrandEntities(true, -1, -1);
    }

    public List<Brand> findBrandEntities(int maxResults, int firstResult) {
        return findBrandEntities(false, maxResults, firstResult);
    }

    private List<Brand> findBrandEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Brand as o where o.enable = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Brand findBrand(Integer id) throws Exception {
        return em.find(Brand.class, id);

    }

    public int getBrandCount() {
        Query q = em.createQuery("select count(o) from Brand as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Brand> findByModelType(Integer modelTypeId) {
        String sql = "select distinct object(b) from Product p"
                + " inner join p.model m"
                + " inner join m.brand b"
                + " where p.modelType.id = :modelTypeId";
        
        Query q = em.createQuery(sql);
        q.setParameter("modelTypeId", modelTypeId);
        
        return q.getResultList();
    }
    
    public List<Brand> findAllBrand() {
        Query q = em.createQuery("select object(o) from Brand as o order by o.code");
        return q.getResultList();
    }
    
    public int checkBrandCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from Brand as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from Brand as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
}
