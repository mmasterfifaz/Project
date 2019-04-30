/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ActivityType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ActivityTypeDAO {

   @PersistenceContext
    private EntityManager em;

    public void create(ActivityType activityType) throws Exception {
//        this.validate(activityType);
        em.persist(activityType);
    }

    public void edit(ActivityType activityType) throws Exception {      
            activityType = em.merge(activityType);
    }

    public List<ActivityType> findActivityTypeEntities() {
        return findActivityTypeEntities(true, -1, -1);
    }

    public List<ActivityType> findActivityTypeEntities(int maxResults, int firstResult) {
        return findActivityTypeEntities(false, maxResults, firstResult);
    }

    private List<ActivityType> findActivityTypeEntities(boolean all, int maxResults, int firstResult) {   
        Query q = em.createQuery("select object(o) from ActivityType as o where enable = true order by code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }    
    
    public List<ActivityType> findActivityTypeNonsystem() {   
        Query q = em.createQuery("select object(o) from ActivityType as o where o.enable = true and "
                + "(o.system = false or o.system is null) order by code");
        return q.getResultList();
    }
    
    public ActivityType findActivityType(Integer id) {
        return em.find(ActivityType.class, id);
       
    }

    public List<ActivityType> findActivityTypeByCode(String code) {   
        Query q = em.createQuery("select object(o) from ActivityType as o where code = ?1");
        q.setParameter(1, code);
        return q.getResultList();
        
    }

    public int getActivityTypeCount() {
        Query q = em.createQuery("select count(o) from ActivityType as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public void validate(ActivityType activityType) throws PreexistingEntityException {
        if (findActivityTypeByCode(activityType.getCode()).size()>0)
            throw new PreexistingEntityException("Duplicate Code");
    }
 
    public int checkActivityTypeCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from ActivityType as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from ActivityType as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public ActivityType findActivityTypeSystemDelegate() {    //activity system delegate
        Query q = em.createQuery("select object(o) from ActivityType as o where o.enable = true and o.status = true "
                + "and o.taskDelegate = true and o.system = true");
        return (ActivityType) q.getSingleResult();
    }
    
    public ActivityType findActivityTypeDelegate() {        //activity manual delegate
        Query q = em.createQuery("select object(o) from ActivityType as o where o.enable = true and o.status = true "
                + "and o.taskDelegate = true and (o.system = false or o.system is null)");
        return (ActivityType) q.getSingleResult();
    }
    
    public List<ActivityType> findActivityType() {           //all activity not system
        Query q = em.createQuery("select object(o) from ActivityType as o where o.enable = true and o.status = true and "
                + "(o.system = false or o.system is null) order by code");
        return q.getResultList();
    }
    
    public List<ActivityType> findActivityTypeNoneDelegate() {      //all activity not delegate 
        Query q = em.createQuery("select object(o) from ActivityType as o where o.enable = true and o.status = true and "
                + "(o.taskDelegate = false or o.taskDelegate is null)");
        return q.getResultList();
    }
}
