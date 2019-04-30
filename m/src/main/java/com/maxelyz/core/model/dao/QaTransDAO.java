/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.QaTrans;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.QaTransDetail;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class QaTransDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(QaTrans qaTrans) {
        em.persist(qaTrans);    
    }

    public void edit(QaTrans qaTrans) throws IllegalOrphanException, NonexistentEntityException, Exception {
        qaTrans = em.merge(qaTrans);    
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        QaTrans qaTrans;
        try {
            qaTrans = em.getReference(QaTrans.class, id);
            qaTrans.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The qaTrans with id " + id + " no longer exists.", enfe);
        }
       
        em.remove(qaTrans);
          
    }

    public List<QaTrans> findQaTransEntities() {
        return findQaTransEntities(true, -1, -1);
    }

    public List<QaTrans> findQaTransEntities(int maxResults, int firstResult) {
        return findQaTransEntities(false, maxResults, firstResult);
    }

    private List<QaTrans> findQaTransEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from QaTrans as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
        
    }

    public QaTrans findQaTrans(Integer id) {
        return em.find(QaTrans.class, id);

    }

    public int getQaTransCount() {
        Query q = em.createQuery("select count(o) from QaTrans as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    
}
