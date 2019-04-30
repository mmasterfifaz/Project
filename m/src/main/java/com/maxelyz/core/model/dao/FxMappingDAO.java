/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.FxMapping;
import com.maxelyz.core.model.entity.FxMappingPK;
import com.maxelyz.core.model.value.admin.FlexFieldValue;
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
public class FxMappingDAO {


    @PersistenceContext
    private EntityManager em;

    public void create(FxMapping fxMapping) throws PreexistingEntityException, Exception {

        em.persist(fxMapping);
            
    }

    public void edit(FxMapping fxMapping) throws NonexistentEntityException, Exception {
        fxMapping = em.merge(fxMapping);     
    }

    public void destroy(FxMappingPK id) throws NonexistentEntityException {
        FxMapping fxMapping;
        try {
            fxMapping = em.getReference(FxMapping.class, id);
            fxMapping.getFxMappingPK();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The fxMapping with id " + id + " no longer exists.", enfe);
        }
        em.remove(fxMapping);
    }

    public List<FxMapping> findFxMappingEntities() {
        return findFxMappingEntities(true, -1, -1);
    }

    public List<FxMapping> findFxMappingEntities(int maxResults, int firstResult) {
        return findFxMappingEntities(false, maxResults, firstResult);
    }

    private List<FxMapping> findFxMappingEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from FxMapping as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
        
    }

    public FxMapping findFxMapping(FxMappingPK id) {
        return em.find(FxMapping.class, id);
    }

    public int getFxMappingCount() {
        Query q = em.createQuery("select count(o) from FxMapping as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<FxMapping> findFxMappingByTableName(String tableName) {
        Query q = em.createQuery("select object(o) from FxMapping as o where o.fxMappingPK.tableName = ?1 order by o.fxMappingPK.fxName");
        q.setParameter(1, tableName);
        return q.getResultList();
    }

    public String findCustomName(String tableName, String fieldName) {
        Query q = em.createQuery("select o.customName from FxMapping as o where o.fxMappingPK.tableName = ?1 and o.fxMappingPK.fxName = ?2");
        q.setParameter(1, tableName);
        q.setParameter(2, fieldName);
        return q.getSingleResult().toString();
    }
    
    public void deleteFxMapping(String tableName) {
        Query q = em.createQuery("Delete from FxMapping as o where o.fxMappingPK.tableName = ?1");
        q.setParameter(1, tableName);
        q.executeUpdate();
    }
        
     public void create(List<FxMapping> fxMappings, String tableName) throws PreexistingEntityException {
        deleteFxMapping(tableName);
        for (FxMapping value : fxMappings) {
            em.persist(value);
        }
     }
     
     public void createFlexFieldValue(List<FlexFieldValue> selectedFxFiedls, String tableName) throws PreexistingEntityException {
        deleteFxMapping(tableName);
        for(FlexFieldValue obj : selectedFxFiedls){
            FxMapping value = new FxMapping();
            FxMappingPK valuePK = new FxMappingPK();

            valuePK.setFxName(obj.getFxMapping().getFxMappingPK().getFxName());
            valuePK.setTableName(tableName);

            value.setCustomName(obj.getFxMapping().getCustomName());
            value.setFxMappingPK(valuePK);

            em.persist(value);
        }

    }
   
}
