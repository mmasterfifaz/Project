/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.QaCategory;
import com.maxelyz.core.model.entity.QaChoice;
import com.maxelyz.core.model.entity.QaQuestion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author oat
 */
public class QaQuestionDAO  {

    @PersistenceContext
    private EntityManager em;

    public void create(QaQuestion qaQuestion) throws PreexistingEntityException, Exception {
        em.persist(qaQuestion);            
    }

    public void edit(QaQuestion qaQuestion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        qaQuestion = em.merge(qaQuestion);
           
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {

            QaQuestion qaQuestion;
            try {
                qaQuestion = em.getReference(QaQuestion.class, id);
                qaQuestion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The qaQuestion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<QaChoice> qaChoiceCollectionOrphanCheck = qaQuestion.getQaChoiceCollection();
            for (QaChoice qaChoiceCollectionOrphanCheckQaChoice : qaChoiceCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This QaQuestion (" + qaQuestion + ") cannot be destroyed since the QaChoice " + qaChoiceCollectionOrphanCheckQaChoice + " in its qaChoiceCollection field has a non-nullable qaQuestion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            QaCategory qaCategory = qaQuestion.getQaCategory();
            if (qaCategory != null) {
                qaCategory.getQaQuestionCollection().remove(qaQuestion);
                qaCategory = em.merge(qaCategory);
            }
            em.remove(qaQuestion);
            
    }

    public List<QaQuestion> findQaQuestionEntities() {
        return findQaQuestionEntities(true, -1, -1);
    }

    public List<QaQuestion> findQaQuestionEntities(int maxResults, int firstResult) {
        return findQaQuestionEntities(false, maxResults, firstResult);
    }

    private List<QaQuestion> findQaQuestionEntities(boolean all, int maxResults, int firstResult) {
 
        Query q = em.createQuery("select object(o) from QaQuestion as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
       
    }

    public QaQuestion findQaQuestion(Integer id) {
        return em.find(QaQuestion.class, id);
    }

    public int getQaQuestionCount() {
        Query q = em.createQuery("select count(o) from QaQuestion as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<QaQuestion> findQaQuestionByCategoryId(QaCategory qaCategory) {
        Query q = em.createQuery("select object(o) from QaQuestion as o where qaCategory = ?1 order by o.seqNo");
        q.setParameter(1, qaCategory);
        return q.getResultList(); 
    }
}
