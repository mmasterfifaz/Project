/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.MotorProtectionClass;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class MotorProtectionClassDAO implements Serializable {

    @PersistenceContext
    private EntityManager em;



    public void create(MotorProtectionClass motorProtectionClass) throws PreexistingEntityException, Exception {
            em.persist(motorProtectionClass);
    }

    public void edit(MotorProtectionClass motorProtectionClass) throws NonexistentEntityException, Exception {
            motorProtectionClass = em.merge(motorProtectionClass);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
            MotorProtectionClass motorProtectionClass;
            try {
                motorProtectionClass = em.getReference(MotorProtectionClass.class, id);
                motorProtectionClass.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The motorProtectionClass with id " + id + " no longer exists.", enfe);
            }
            em.remove(motorProtectionClass);
    }

    public List<MotorProtectionClass> findMotorProtectionClassEntities() {
        return findMotorProtectionClassEntities(true, -1, -1);
    }

    public List<MotorProtectionClass> findMotorProtectionClassEntities(int maxResults, int firstResult) {
        return findMotorProtectionClassEntities(false, maxResults, firstResult);
    }

    private List<MotorProtectionClass> findMotorProtectionClassEntities(boolean all, int maxResults, int firstResult) {
            Query q = em.createQuery("select object(o) from MotorProtectionClass as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
    }

    public MotorProtectionClass findMotorProtectionClass(Integer id) {
            return em.find(MotorProtectionClass.class, id);
    }

    public int getMotorProtectionClassCount() {
            Query q = em.createQuery("select count(o) from MotorProtectionClass as o");
            return ((Long) q.getSingleResult()).intValue();
    }
    
}
