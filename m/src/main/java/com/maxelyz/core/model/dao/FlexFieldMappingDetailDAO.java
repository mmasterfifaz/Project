/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.FlexfieldMappingDetail;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class FlexFieldMappingDetailDAO {
    @PersistenceContext
    private EntityManager em;
    
    public void create(FlexfieldMappingDetail flexfieldMappingDetail) throws PreexistingEntityException, Exception {
        em.persist(flexfieldMappingDetail);

    }

    public void edit(FlexfieldMappingDetail flexfieldMappingDetail) throws IllegalOrphanException, NonexistentEntityException, Exception {
        flexfieldMappingDetail = em.merge(flexfieldMappingDetail);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        FlexfieldMappingDetail flexfieldMappingDetail;
        try {
            flexfieldMappingDetail = em.getReference(FlexfieldMappingDetail.class, id);
            flexfieldMappingDetail.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The customer with id " + id + " no longer exists.", enfe);
        }
        em.remove(flexfieldMappingDetail);
    }

    public List<FlexfieldMappingDetail> findFlexfieldMappingDetailEntities() {
        return findFlexfieldMappingDetailEntities(true, -1, -1);
    }

    public List<FlexfieldMappingDetail> findFlexfieldMappingDetailEntities(int maxResults, int firstResult) {
        return findFlexfieldMappingDetailEntities(false, maxResults, firstResult);
    }

    private List<FlexfieldMappingDetail> findFlexfieldMappingDetailEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from FlexfieldMappingDetail as o order by o.fxName");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public FlexfieldMappingDetail findFlexfieldMappingDetail(Integer id) {
        return em.find(FlexfieldMappingDetail.class, id);
    }
}
