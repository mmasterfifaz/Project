
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.AutoAssignmentConfig;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AutoAssignmentConfigDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(AutoAssignmentConfig autoAssignmentConfig) {
        em.persist(autoAssignmentConfig);
    }

    public void edit(AutoAssignmentConfig autoAssignmentConfig) {
        autoAssignmentConfig = em.merge(autoAssignmentConfig);
    }

    public AutoAssignmentConfig findAutoAssignmentConfigEntities() {
        Query q = em.createQuery("select object(o) from AutoAssignmentConfig as o");
        try { 
            return (AutoAssignmentConfig)q.getSingleResult();
        } catch (NoResultException ex) { 
            return null;
        }
    }

}
