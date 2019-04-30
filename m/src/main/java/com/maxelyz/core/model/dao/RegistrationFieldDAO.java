/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.RegistrationField;
import com.maxelyz.core.model.entity.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RegistrationFieldDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(RegistrationField registrationField) throws PreexistingEntityException, Exception {
        em.persist(registrationField);
    }

    public void edit(RegistrationField registrationField) throws NonexistentEntityException, Exception {
        registrationField = em.merge(registrationField);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        RegistrationField registrationField;
        try {
            registrationField = em.getReference(RegistrationField.class, id);
            registrationField.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The registrationField with id " + id + " no longer exists.", enfe);
        }
        em.remove(registrationField);
    }

    public List<RegistrationField> findRegistrationFieldEntities() {
        return findRegistrationFieldEntities(true, -1, -1);
    }

    public List<RegistrationField> findRegistrationFieldEntities(int maxResults, int firstResult) {
        return findRegistrationFieldEntities(false, maxResults, firstResult);
    }

    private List<RegistrationField> findRegistrationFieldEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from RegistrationField as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public RegistrationField findRegistrationField(Integer id) {
        return em.find(RegistrationField.class, id);
    }

    public int getRegistrationFieldCount() {
        return ((Long) em.createQuery("select count(o) from RegistrationField as o").getSingleResult()).intValue();
    }

    public List<RegistrationField> findRegistrationFieldByFromIdAndName(String name) {
        Query q = em.createQuery("select object(o) from RegistrationField as o where (o.registrationForm.id = 1103 or o.registrationForm.id = 1104)"
                + " and o.name = ?1");
        q.setParameter(1, name);
        return q.getResultList();
    }
}
