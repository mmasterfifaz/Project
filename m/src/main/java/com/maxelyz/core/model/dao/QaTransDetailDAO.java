/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.QaTrans;
import com.maxelyz.core.model.entity.QaTransDetail;
import java.util.List;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class QaTransDetailDAO {

    @PersistenceContext
    private EntityManager em;
    
    public void create(QaTransDetail qaTransDetail) {
        em.persist(qaTransDetail); 
    }

    public void edit(QaTransDetail qaTransDetail) throws NonexistentEntityException, Exception {
        qaTransDetail = em.merge(qaTransDetail);      
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        QaTransDetail qaTransDetail;
        try {
            qaTransDetail = em.getReference(QaTransDetail.class, id);
            qaTransDetail.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The qaTransDetail with id " + id + " no longer exists.", enfe);
        }

        em.remove(qaTransDetail);

    }

    public List<QaTransDetail> findQaTransDetailEntities() {
        return findQaTransDetailEntities(true, -1, -1);
    }

    public List<QaTransDetail> findQaTransDetailEntities(int maxResults, int firstResult) {
        return findQaTransDetailEntities(false, maxResults, firstResult);
    }

    private List<QaTransDetail> findQaTransDetailEntities(boolean all, int maxResults, int firstResult) {
        
        Query q = em.createQuery("select object(o) from QaTransDetail as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    
    }

    public QaTransDetail findQaTransDetail(Integer id) {
        return em.find(QaTransDetail.class, id);
    }

    public int getQaTransDetailCount() {
        Query q = em.createQuery("select count(o) from QaTransDetail as o");
        return ((Long) q.getSingleResult()).intValue();

    }
    
    public List<QaTransDetail> findQaTransDetailByQaTransId(QaTrans qaTrans) {
        Query q = em.createQuery("select object(o) from QaTransDetail as o where qaTrans = ?1 order by o.seqNo");
        q.setParameter(1, qaTrans);
        return q.getResultList();
    }
    
    public void deleteQaTransDetails(QaTrans qaTrans) {
       Query q = em.createQuery("Delete from QaTransDetail where qaTrans = ?1");
       q.setParameter(1, qaTrans);
       q.executeUpdate();
    }
    
    public void deleteQaTransDetails(Integer qaTransId) {
       Query q = em.createQuery("Delete from QaTransDetail where qaTrans.id = ?1");
       q.setParameter(1, qaTransId);
       q.executeUpdate();
    }
    
    public List<QaTransDetail> findQaTransDetailByPO() {
        Query q = this.em.createNativeQuery("select distinct qt.qa_form_name from qa_trans_detail qtd"
                + " join qa_trans qt on qt.id = qtd.qa_trans_id"
                + " join qa_form qf on qf.id = qt.qa_form_id"
                + " where qf.enable = 1 and qt.id in (select po.qc_qa_trans_id from purchase_order po where po.qc_qa_trans_id is not null)");
        return q.getResultList();
    }
}
