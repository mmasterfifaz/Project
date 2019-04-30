/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.Brand;
import com.maxelyz.core.model.entity.Model;
import java.util.List;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class ModelDAO {

    @PersistenceContext
    private EntityManager em;
    

    public void create(Model model) {
        em.persist(model);        
    }

    public void edit(Model model) throws NonexistentEntityException, Exception {
        model = em.merge(model);      
    }

    public void destroy(Integer id) throws NonexistentEntityException {
            Model model;
            try {
                model = em.getReference(Model.class, id);
                model.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The model with id " + id + " no longer exists.", enfe);
            }
            em.remove(model);
    }

    public List<Model> findModelEntities() {
        return findModelEntities(true, -1, -1);
    }

    public List<Model> findModelEntities(int maxResults, int firstResult) {
        return findModelEntities(false, maxResults, firstResult);
    }

    private List<Model> findModelEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Model as o where o.enable = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<Model> findModelByBrand(Integer brandId) {
        Query q = em.createQuery("select object(o) from Model as o "
                + "where o.brand.id = :brandId");
        q.setParameter("brandId", brandId);
        return q.getResultList();
    }

    public List<Model> findModelByModelTypeBrand(Integer modelTypeId, Integer brandId) {
        Query q = em.createQuery("select distinct object(m) from Product as p"
                + " inner join p.model m"
                + " inner join m.brand b"
                + " where b.id = :brandId and p.modelType.id = :modelTypeId");
        q.setParameter("brandId", brandId);
        q.setParameter("modelTypeId", modelTypeId);
        return q.getResultList();
    }

    public List<Model> findBy(String txtSearch, Integer brandId) {
        String sql = "select object(o) from Model as o"
                + " where o.enable = true";
        if(!txtSearch.equals("")){
            sql += " and name like :txtSearch";
        }
        if(brandId != null && brandId != 0){
            sql += " and o.brand.id = :brandId";
        }
        sql += "  order by o.code";
        Query q = em.createQuery(sql);
        
        if (!txtSearch.equals("")) {
            q.setParameter("txtSearch", "%" + txtSearch + "%");
        }
        if (brandId != null && brandId != 0) {
            q.setParameter("brandId", brandId);
        }
        return q.getResultList();
    }

    public Model findModel(Integer id) {
        return em.find(Model.class, id);

    }

    public int getModelCount() {
        Query q = em.createQuery("select count(o) from Model as o");
        return ((Long) q.getSingleResult()).intValue();
 
    }
    
    public int checkModelCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from Model as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from Model as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
}
