/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.DeclarationField;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author User
 */
@Transactional
public class DeclarationFieldDAO {
    @PersistenceContext
    private EntityManager em;

    public void create(DeclarationField declarationField) throws PreexistingEntityException, Exception {
        em.persist(declarationField);
    }

    public void edit(DeclarationField declarationField) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("UPDATE DeclarationField reg SET reg.seqNo = ?1 WHERE reg.name = ?2 and reg.groupNo = ?3");
        
        q.setParameter(1, declarationField.getSeqNo());
         q.setParameter(2, declarationField.getName());
         q.setParameter(3, declarationField.getGroupNo());
         q.executeUpdate();

//         declarationFields = em.merge(declarationFields);
        
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        DeclarationField declarationField;
        try {
            declarationField = em.getReference(DeclarationField.class, id);
            declarationField.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The declarationField with id " + id + " no longer exists.", enfe);
        }
        em.remove(declarationField);
    }

    public List<DeclarationField> findDeclarationFieldEntities() {
        return findDeclarationFieldEntities(true, -1, -1);
    }

    public List<DeclarationField> findDeclarationFieldEntities(int maxResults, int firstResult) {
        return findDeclarationFieldEntities(false, maxResults, firstResult);
    }

    private List<DeclarationField> findDeclarationFieldEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from DeclarationField as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public DeclarationField findDeclarationField(Integer id) {
        return em.find(DeclarationField.class, id);
    }

    public int getDeclarationFieldCount() {
        return ((Long) em.createQuery("select count(o) from DeclarationField as o").getSingleResult()).intValue();
    }
    
    public List<DeclarationField> getDeclarationFieldByDeclarationFormID(Integer declarationFormId) {
        Query q = em.createQuery("select object(o) from DeclarationField as o where o.declarationForm.id = ?1 order by o.seqNo");
        q.setParameter(1, declarationFormId);
        return q.getResultList();
    }
}
