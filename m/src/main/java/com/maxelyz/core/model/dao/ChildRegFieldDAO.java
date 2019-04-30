/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ChildRegField;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TBN
 */
@Transactional
public class ChildRegFieldDAO {
    @PersistenceContext
    private EntityManager em;

    public void create(ChildRegField childRegField) throws PreexistingEntityException, Exception {
        em.persist(childRegField);
    }

    public void edit(ChildRegField childRegField) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("UPDATE ChildRegField reg SET reg.seqNo = ?1 WHERE reg.name = ?2 and reg.groupNo = ?3");
        
         q.setParameter(1, childRegField.getSeqNo());
         q.setParameter(2, childRegField.getName());
         q.setParameter(3, childRegField.getGroupNo());
         q.executeUpdate();

//         declarationFields = em.merge(declarationFields);
        
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        ChildRegField childRegField;
        try {
            childRegField = em.getReference(ChildRegField.class, id);
            childRegField.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The childRegField with id " + id + " no longer exists.", enfe);
        }
        em.remove(childRegField);
    }

    public List<ChildRegField> findChildRegFieldEntities() {
        return findChildRegFieldEntities(true, -1, -1);
    }

    public List<ChildRegField> findChildRegFieldEntities(int maxResults, int firstResult) {
        return findChildRegFieldEntities(false, maxResults, firstResult);
    }

    private List<ChildRegField> findChildRegFieldEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ChildRegField as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public ChildRegField findChildRegField(Integer id) {
        return em.find(ChildRegField.class, id);
    }

    public int getChildRegFieldCount() {
        return ((Long) em.createQuery("select count(o) from DeclarationField as o").getSingleResult()).intValue();
    }
    
    public List<ChildRegField> getChildRegFieldByChildRegFormID(Integer declarationFormId) {
        Query q = em.createQuery("select object(o) from ChildRegField as o where o.childRegForm.id = ?1 order by o.seqNo");
        q.setParameter(1, declarationFormId);
        return q.getResultList();
    }
    
    public void deleteChildRegFieldByChildRegFormID(Integer childRegFormId) {
        Query q = em.createQuery("Delete from ChildRegField as o where o.childRegForm.id = ?1");
        q.setParameter(1, childRegFormId);
        q.executeUpdate();
    }
}
