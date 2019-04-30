/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.QaForm;
import com.maxelyz.core.model.entity.QaCategory;
import com.maxelyz.core.model.entity.QaSelectedCategory;
import com.maxelyz.core.model.entity.QaSelectedCategoryPK;
import java.util.List;
import javax.persistence.*;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class QaSelectedCategoryDAO  {

    @PersistenceContext
    private EntityManager em;

    public void create(QaSelectedCategory qaSelectedCategory) throws PreexistingEntityException, Exception {
        em.persist(qaSelectedCategory);       
    }

    public void edit(QaSelectedCategory qaSelectedCategory) throws NonexistentEntityException, Exception {
        qaSelectedCategory = em.merge(qaSelectedCategory);      
    }

    public void destroy(QaSelectedCategoryPK id) throws NonexistentEntityException {
        QaSelectedCategory qaSelectedCategory;
        try {
            qaSelectedCategory = em.getReference(QaSelectedCategory.class, id);
            qaSelectedCategory.getQaSelectedCategoryPK();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The qaSelectedCategory with id " + id + " no longer exists.", enfe);
        }
        QaForm qaForm = qaSelectedCategory.getQaForm();
        if (qaForm != null) {
            qaForm.getQaSelectedCategoryCollection().remove(qaSelectedCategory);
            qaForm = em.merge(qaForm);
        }
        QaCategory qaCategory = qaSelectedCategory.getQaCategory();
        if (qaCategory != null) {
            qaCategory.getQaSelectedCategoryCollection().remove(qaSelectedCategory);
            qaCategory = em.merge(qaCategory);
        }
        em.remove(qaSelectedCategory);
        em.getTransaction().commit(); 
    }

    public List<QaSelectedCategory> findQaSelectedCategoryEntities() {
        return findQaSelectedCategoryEntities(true, -1, -1);
    }

    public List<QaSelectedCategory> findQaSelectedCategoryEntities(int maxResults, int firstResult) {
        return findQaSelectedCategoryEntities(false, maxResults, firstResult);
    }

    private List<QaSelectedCategory> findQaSelectedCategoryEntities(boolean all, int maxResults, int firstResult) {
            Query q = em.createQuery("select object(o) from QaSelectedCategory as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
    }

    public QaSelectedCategory findQaSelectedCategory(QaSelectedCategoryPK id) {
            return em.find(QaSelectedCategory.class, id);
    }

    public int getQaSelectedCategoryCount() {
        Query q = em.createQuery("select count(o) from QaSelectedCategory as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<QaSelectedCategory> findQaSelectedCategoryByFormId(QaForm qaForm) {
        Query q = em.createQuery("select object(o) from QaSelectedCategory as o where qaForm = ?1 and qaCategory.enable=true order by o.seqNo");
        q.setParameter(1, qaForm);
        return q.getResultList();
    }
}
