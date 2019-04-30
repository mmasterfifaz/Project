/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.model.dao;

import com.maxelyz.social.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.social.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoWorkflowInstance;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ukritj
 */
@Transactional
public class SoWorkflowInstanceDAO {
    @PersistenceContext
    private EntityManager em;

    public void create(SoWorkflowInstance soWorkflowInstance) throws PreexistingEntityException {
        em.persist(soWorkflowInstance);
    }

    public void edit(SoWorkflowInstance soWorkflowInstance) throws NonexistentEntityException, Exception {
        soWorkflowInstance = em.merge(soWorkflowInstance);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoWorkflowInstance soWorkflowInstance;
        try {
            soWorkflowInstance = em.getReference(SoWorkflowInstance.class, id);
            soWorkflowInstance.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soWorkflowInstance with id " + id + " no longer exists.", enfe);
        }
        em.remove(soWorkflowInstance);
    }
}
