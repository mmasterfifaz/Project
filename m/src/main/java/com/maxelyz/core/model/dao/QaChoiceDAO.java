/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.QaChoice;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.QaQuestion;
import java.util.List;
import javax.persistence.*;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class QaChoiceDAO  {

    @PersistenceContext
    private EntityManager em;

    public void create(QaChoice qaChoice) throws PreexistingEntityException, Exception {
        em.persist(qaChoice);    
    }

    public void edit(QaChoice qaChoice) throws NonexistentEntityException, Exception {
        qaChoice = em.merge(qaChoice);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        QaChoice qaChoice;
        try {
            qaChoice = em.getReference(QaChoice.class, id);
            qaChoice.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The qaChoice with id " + id + " no longer exists.", enfe);
        }
        QaQuestion qaQuestion = qaChoice.getQaQuestion();
        if (qaQuestion != null) {
            qaQuestion.getQaChoiceCollection().remove(qaChoice);
            qaQuestion = em.merge(qaQuestion);
        }
        em.remove(qaChoice);
        
    }

    public List<QaChoice> findQaChoiceEntities() {
        return findQaChoiceEntities(true, -1, -1);
    }

    public List<QaChoice> findQaChoiceEntities(int maxResults, int firstResult) {
        return findQaChoiceEntities(false, maxResults, firstResult);
    }

    private List<QaChoice> findQaChoiceEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from QaChoice as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public QaChoice findQaChoice(Integer id) {
        return em.find(QaChoice.class, id);
    }

    public int getQaChoiceCount() {
        Query q = em.createQuery("select count(o) from QaChoice as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<QaChoice> findQaChoiceByQuestionId(QaQuestion qaQuestion) {
        Query q = em.createQuery("select object(o) from QaChoice as o where qaQuestion =?1 order by o.seqNo");
        q.setParameter(1, qaQuestion);
        return q.getResultList();
    }
}
