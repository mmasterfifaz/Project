/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;
import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.PhoneDirectoryCategory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import java.util.*;
import javax.persistence.*;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrator
 */
@Transactional
public class PhoneDirectoryCategoryDAO {
        
    @PersistenceContext
    private EntityManager em;

    public void create(PhoneDirectoryCategory phoneDirectoryCategory) throws PreexistingEntityException, Exception {
        em.persist(phoneDirectoryCategory);     
    }
    
    public void edit(PhoneDirectoryCategory phoneDirectoryCategory) throws IllegalOrphanException, NonexistentEntityException, Exception {
       phoneDirectoryCategory = em.merge(phoneDirectoryCategory);       
    }
    
    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        PhoneDirectoryCategory phoneDirectoryCategory;
        try {
            phoneDirectoryCategory = em.getReference(PhoneDirectoryCategory.class, id);
            phoneDirectoryCategory.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The phoneDirectoryCategory with id " + id + " no longer exists.", enfe);
        }
        List<String> illegalOrphanMessages = null;
        em.remove(phoneDirectoryCategory);        
    }
    
    public List<PhoneDirectoryCategory> findPhoneDirectoryCategoryEntities() {
        return findPhoneDirectoryCategoryEntities(true, -1, -1);
    }

    public List<PhoneDirectoryCategory> findPhoneDirectoryCategoryEntities(int maxResults, int firstResult) {
        return findPhoneDirectoryCategoryEntities(false, maxResults, firstResult);
    }

    private List<PhoneDirectoryCategory> findPhoneDirectoryCategoryEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from PhoneDirectoryCategory as o where o.enable=true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
    
    public List<PhoneDirectoryCategory> findPhoneDirectoryCategoryEnable() {
        Query q = em.createQuery("select object(o) from PhoneDirectoryCategory as o where o.enable=true and o.status=true order by o.name");
        return q.getResultList();
    }

    public PhoneDirectoryCategory findPhoneDirectoryCategory(Integer id) {
        return em.find(PhoneDirectoryCategory.class, id);
    }

    public int getPhoneDirectoryCategoryCount() {
        Query q = em.createQuery("select count(o) from PhoneDirectoryCategory as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public int checkPhoneDirectoryCategoryName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from PhoneDirectoryCategory as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from PhoneDirectoryCategory as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
}
