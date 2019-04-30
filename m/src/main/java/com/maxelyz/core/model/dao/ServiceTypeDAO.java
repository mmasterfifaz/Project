/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ServiceType;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ServiceTypeDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(ServiceType serviceType) throws PreexistingEntityException {
        em.persist(serviceType);
    }

    public void edit(ServiceType serviceType) throws NonexistentEntityException, Exception {
        serviceType = em.merge(serviceType);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        ServiceType serviceType;
        try {
            serviceType = em.getReference(ServiceType.class, id);
            serviceType.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The serviceType with id " + id + " no longer exists.", enfe);
        }
        em.remove(serviceType);
    }

    public List<ServiceType> findServiceTypeEntities() {
        return findServiceTypeEntities(true, -1, -1);
    }

    public List<ServiceType> findServiceTypeEntities(int maxResults, int firstResult) {
        return findServiceTypeEntities(false, maxResults, firstResult);
    }

    private List<ServiceType> findServiceTypeEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ServiceType as o where o.enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<ServiceType> findServiceTypeStatus() {
        Query q = em.createQuery("select object(o) from ServiceType as o where o.enable = true and o.status = true order by o.name");
        
        return q.getResultList();
    }

    public ServiceType findServiceType(Integer id) {
        return em.find(ServiceType.class, id);
    }

    public int getServiceTypeCount() {
        Query q = em.createQuery("select count(o) from ServiceType as o");
        return ((Long) q.getSingleResult()).intValue();
    }


    public Map<String, Integer> getServiceTypeList() {
        List<ServiceType> serviceTypes = this.findServiceTypeEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (ServiceType obj : serviceTypes) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public int checkServiceTypeName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from ServiceType as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from ServiceType as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
}
