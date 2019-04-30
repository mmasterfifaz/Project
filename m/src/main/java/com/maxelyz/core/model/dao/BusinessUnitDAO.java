/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.BusinessUnit;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class BusinessUnitDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(BusinessUnit businessUnit) {
        em.persist(businessUnit);
    }

    public void edit(BusinessUnit businessUnit) {
        businessUnit = em.merge(businessUnit);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        BusinessUnit businessUnit;
        try {
            businessUnit = em.getReference(BusinessUnit.class, id);
            businessUnit.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The businessUnit with id " + id + " no longer exists.", enfe);
        }
        em.remove(businessUnit); 
    }

    public List<BusinessUnit> findBusinessUnitEntities() {
        return findBusinessUnitEntities(true, -1, -1);
    }

    public List<BusinessUnit> findBusinessUnitEntities(int maxResults, int firstResult) {
        return findBusinessUnitEntities(false, maxResults, firstResult);
    }

    private List<BusinessUnit> findBusinessUnitEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from BusinessUnit as o where o.enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public BusinessUnit findBusinessUnit(Integer id) {
        return em.find(BusinessUnit.class, id);
    }

    public int getBusinessUnitCount() {
        Query q = em.createQuery("select count(o) from BusinessUnit as o");
        return ((Long) q.getSingleResult()).intValue();
    }
        
    public Map<String, Integer> getBusinessUnitList() {
        List<BusinessUnit> businessunits = this.findBusinessUnitEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (BusinessUnit obj : businessunits) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public int checkBusinessUnitName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from BusinessUnit as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from BusinessUnit as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<BusinessUnit> findBusinessUnitByServiceTypeId(Integer serviceTypeId) {
        Query q = em.createQuery("select object(o) from BusinessUnit as o"
                + " join o.serviceTypeCollection s"
                + " where o.enable = true"
                + " and o.status = true"
                + " and s.id = :serviceTypeId"
                + " order by o.name");
        q.setParameter("serviceTypeId", serviceTypeId);
        return q.getResultList();
    }
}