/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.ModelType;
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
public class ModelTypeDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(ModelType modelType) {
        em.persist(modelType);
    }

    public void edit(ModelType modelType) throws NonexistentEntityException, Exception {
        modelType = em.merge(modelType);     
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        ModelType modelType;
        try {
            modelType = em.getReference(ModelType.class, id);
            modelType.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The modelType with id " + id + " no longer exists.", enfe);
        }
        em.remove(modelType);
         
    }

    public List<ModelType> findModelTypeEntities() {
        return findModelTypeEntities(true, -1, -1);
    }

    public List<ModelType> findModelTypeEntities(int maxResults, int firstResult) {
        return findModelTypeEntities(false, maxResults, firstResult);
    }

    private List<ModelType> findModelTypeEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ModelType as o where o.enable = 1");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();

    }

    public ModelType findModelType(Integer id) {
        return em.find(ModelType.class, id);
    }

    public int getModelTypeCount() {
        Query q = em.createQuery("select count(o) from ModelType as o");
        return ((Long) q.getSingleResult()).intValue();
    }
        
    public int checkModelTypeCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from ModelType as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from ModelType as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
