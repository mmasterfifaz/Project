
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.AutoAssignment;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AutoAssignmentDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(AutoAssignment autoAssignment) {
        em.persist(autoAssignment);
    }

    public void edit(AutoAssignment autoAssignment) {
        autoAssignment = em.merge(autoAssignment);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        AutoAssignment autoAssignment;
        try {
            autoAssignment = em.getReference(AutoAssignment.class, id);
            autoAssignment.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The autoAssignment with id " + id + " no longer exists.", enfe);
        }
        em.remove(autoAssignment);

    }

    public AutoAssignment findAutoAssignmentEntities() {
        Query q = em.createQuery("select object(o) from AutoAssignment as o");
        try { 
            return (AutoAssignment)q.getSingleResult();
        } catch (NoResultException ex) { 
            return null;
        }
    }
    
    public List<AutoAssignment> findAutoAssignmentServiceEnable() {   
        Query q = em.createQuery("select object(o) from AutoAssignment as o where o.enable = true order by o.name");
        return q.getResultList();
    }
    
    public AutoAssignment findAutoAssignment(Integer id) {
        AutoAssignment autoAssignment = em.find(AutoAssignment.class, id);
//        autoAssignment.getAgentsCollection().size();
        return autoAssignment;
    }
    
    public int checkAutoAssignmentServiceName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from AutoAssignment as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from AutoAssignment as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
