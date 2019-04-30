/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.CaseAttachment;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CaseAttachmentDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(CaseAttachment caseAttachment) {
        em.persist(caseAttachment);
    }

    public void edit(CaseAttachment caseAttachment) throws NonexistentEntityException, Exception {
        caseAttachment = em.merge(caseAttachment);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        CaseAttachment caseAttachment;
        try {
            caseAttachment = em.getReference(CaseAttachment.class, id);
            caseAttachment.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The caseAttachment with id " + id + " no longer exists.", enfe);
        }
        em.remove(caseAttachment);
    }

    public List<CaseAttachment> findCaseAttachmentEntities() {
        return findCaseAttachmentEntities(true, -1, -1);
    }

    public List<CaseAttachment> findCaseAttachmentEntities(int maxResults, int firstResult) {
        return findCaseAttachmentEntities(false, maxResults, firstResult);
    }

    private List<CaseAttachment> findCaseAttachmentEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CaseAttachment as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public CaseAttachment findCaseAttachment(Integer id) {
        return em.find(CaseAttachment.class, id);
    }

    public int getCaseAttachmentCount() {
        Query q = em.createQuery("select count(o) from CaseAttachment as o");
        return ((Long) q.getSingleResult()).intValue();
    }
}
