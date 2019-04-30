package com.maxelyz.core.model.dao;

import static com.google.common.collect.Iterators.all;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ContactResultPlan;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ContactResultPlanDAO {
    @PersistenceContext
    private EntityManager em;
    
    public void create(ContactResultPlan contactResultplan) throws PreexistingEntityException, Exception {
        if (contactResultplan.getIsDefault().equals(true)) {
            String sql = "update ContactResultPlan set isDefault = false";
            Query q = em.createQuery(sql);
            q.executeUpdate();
        }
        em.persist(contactResultplan);
    }

    public void deleteContactResultPlanWithContactResultId(Integer contactResultId) {
        Query q = em.createNativeQuery("Delete from contact_result_plan_detail where contact_result_id = ?1");
        q.setParameter(1,contactResultId);
        q.executeUpdate();
    }

    public void edit(ContactResultPlan contactResultplan) throws NonexistentEntityException, Exception {
        if (contactResultplan.getIsDefault().equals(true)) {
            String sql = "update ContactResultPlan set isDefault = false";
            Query q = em.createQuery(sql);
            q.executeUpdate();
        }
        contactResultplan = em.merge(contactResultplan);
    }

    public List<ContactResultPlan> findContactResultPlanEntities() {
        return findContactResultPlanEntities(true, -1, -1);
    }

    public List<ContactResultPlan> findContactResultPlanEntities(int maxResults, int firstResult) {
        return findContactResultPlanEntities(false, maxResults, firstResult);
    }

    private List<ContactResultPlan> findContactResultPlanEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ContactResultPlan as o where o.enable=true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
    
    public List<ContactResultPlan> findContactResultPlanExceptDefaultEntities() {
        return findContactResultPlanExceptDefaultEntities(true, -1, -1);
    }
    
    private List<ContactResultPlan> findContactResultPlanExceptDefaultEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ContactResultPlan as o where o.enable=true and o.isDefault=false order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
    
    public ContactResultPlan findContactResultPlan(Integer id) {
        return em.find(ContactResultPlan.class, id);
    }
    
    public Integer findContactResultPlanByIsDefault(int isDefault) {
        try {
        Query q = em.createQuery("select crp.id from ContactResultPlan as crp where crp.enable=true and crp.isDefault ="+isDefault+" ");
        return ((Integer) q.getSingleResult()).intValue();
        }catch(NoResultException e){
        return 0;
        }
    }
    
    public int checkContactResultPlan(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from ContactResultPlan as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from ContactResultPlan as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public int checkIsDefault() {
        String sql = "select count(o) from ContactResultPlan as o where o.isDefault = true";
        Query q = em.createQuery(sql);
        return ((Long) q.getSingleResult()).intValue();
    }


}
