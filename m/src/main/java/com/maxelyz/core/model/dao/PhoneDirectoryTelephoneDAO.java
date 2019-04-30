/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;
import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.PhoneDirectoryTelephone;
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
public class PhoneDirectoryTelephoneDAO {
    
    @PersistenceContext
    private EntityManager em;

    public void create(PhoneDirectoryTelephone phoneDirectoryTelephone) throws PreexistingEntityException, Exception {
        em.persist(phoneDirectoryTelephone);     
    }
    
    public void edit(PhoneDirectoryTelephone phoneDirectoryTelephone) throws IllegalOrphanException, NonexistentEntityException, Exception {
       phoneDirectoryTelephone = em.merge(phoneDirectoryTelephone);       
    }
    
    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        PhoneDirectoryTelephone phoneDirectoryTelephone;
        try {
            phoneDirectoryTelephone = em.getReference(PhoneDirectoryTelephone.class, id);
            phoneDirectoryTelephone.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The phoneDirectoryTelephone with id " + id + " no longer exists.", enfe);
        }
        List<String> illegalOrphanMessages = null;
        em.remove(phoneDirectoryTelephone);        
    }
    
    public List<PhoneDirectoryTelephone> findPhoneDirectoryTelephoneEntities() {
        return findPhoneDirectoryTelephoneEntities(true, -1, -1);
    }

    public List<PhoneDirectoryTelephone> findPhoneDirectoryTelephoneEntities(int maxResults, int firstResult) {
        return findPhoneDirectoryTelephoneEntities(false, maxResults, firstResult);
    }

    private List<PhoneDirectoryTelephone> findPhoneDirectoryTelephoneEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from PhoneDirectoryTelephone as o where o.enable=true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public PhoneDirectoryTelephone findPhoneDirectoryTelephone(Integer id) {
        return em.find(PhoneDirectoryTelephone.class, id);
    }

    public int getPhoneDirectoryTelephoneCount() {
        Query q = em.createQuery("select count(o) from PhoneDirectoryTelephone as o");
        return ((Long) q.getSingleResult()).intValue();
    }

}
