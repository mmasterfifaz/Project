/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.Location;
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
public class LocationDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Location location) throws PreexistingEntityException {
        em.persist(location);
    }

    public void edit(Location location) throws NonexistentEntityException, Exception {
        location = em.merge(location);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        Location location;
        try {
            location = em.getReference(Location.class, id);
            location.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The location with id " + id + " no longer exists.", enfe);
        }
        em.remove(location);
    }

    public List<Location> findLocationEntities() {
        return findLocationEntities(true, -1, -1);
    }

    public List<Location> findLocationEntities(int maxResults, int firstResult) {
        return findLocationEntities(false, maxResults, firstResult);
    }

    private List<Location> findLocationEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Location as o where o.enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
        
    public Location findLocation(Integer id) {
        return em.find(Location.class, id);
    }

    public int getLocationCount() {
        Query q = em.createQuery("select count(o) from Location as o");
        return ((Long) q.getSingleResult()).intValue();
    }
         
    public Map<String, Integer> getLocationList() {
        List<Location> locations = this.findLocationEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Location obj : locations) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public List<Location> findLocationByBusinessUnit(Integer businessUnitId) {
        Query q = em.createQuery("select object(o) from Location as o where o.businessUnit.id = :businessUnitId and o.enable = true order by o.name");
        q.setParameter("businessUnitId", businessUnitId);
        return q.getResultList();
    }
    
    public int checkLocationName(String name, Integer businessUnitId, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from Location as o where o.name =:name and o.businessUnit.id = :businessUnitId and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from Location as o where o.name =:name and o.businessUnit.id = :businessUnitId and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        q.setParameter("businessUnitId", businessUnitId);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Location> findLocationByBusinessUnitId(Integer businessUnitId) {
        Query q = em.createQuery("select object(o) from Location as o"
                + " where o.enable = true"
                + " and o.status = true"
                + " and o.businessUnit.id = :businessUnitId"
                + " order by o.name");
        q.setParameter("businessUnitId", businessUnitId);
        return q.getResultList();
    }
}
