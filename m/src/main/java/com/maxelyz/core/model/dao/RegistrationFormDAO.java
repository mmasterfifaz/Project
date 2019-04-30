/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.RegistrationForm;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RegistrationFormDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(RegistrationForm registrationForm) throws PreexistingEntityException, Exception {
        em.persist(registrationForm);
    }

    public void edit(RegistrationForm registrationForm) throws IllegalOrphanException, NonexistentEntityException, Exception {
        //delete registratonfield
        Query q = em.createQuery("Delete from RegistrationField reg where reg.registrationForm = ?1");
        q.setParameter(1, registrationForm);
        q.executeUpdate();

        registrationForm = em.merge(registrationForm);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        RegistrationForm registrationForm;
        try {
            registrationForm = em.getReference(RegistrationForm.class, id);
            registrationForm.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The registration form with id " + id + " no longer exists.", enfe);
        }
        em.remove(registrationForm);
    }

    public List<RegistrationForm> findRegistrationFormEntities() {
        return findRegistrationFormEntities(true, -1, -1);
    }

    public List<RegistrationForm> findRegistrationFormEntities(int maxResults, int firstResult) {
        return findRegistrationFormEntities(false, maxResults, firstResult);
    }

    private List<RegistrationForm> findRegistrationFormEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from RegistrationForm as o where enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public RegistrationForm findRegistrationForm(Integer id) {
        return em.find(RegistrationForm.class, id);
    }

    public int getRegistrationFormCount() {
        return ((Long) em.createQuery("select count(o) from RegistrationForm as o").getSingleResult()).intValue();
    }

    public List<RegistrationForm> findRegistrationFormByName(String nameKeyword) {
        Query q = em.createNamedQuery("RegistrationForm.findByName");
        q.setParameter("name", nameKeyword);
        return q.getResultList();
    }

    public RegistrationForm findRegistrationFormByProductId(Integer productId) {
        Query q = em.createQuery("select object(reg) from Product p join p.registrationForm reg where p.id=?1");
        q.setParameter(1, productId);
        q.setMaxResults(1);
       
        return (RegistrationForm)q.getSingleResult();
    }
    
    public int checkRegistrationFormName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from RegistrationForm as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from RegistrationForm as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
}
