package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.SequenceNoFileDetail;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by support on 11/13/2017.
 */
@Transactional
public class SequenceNoFileDetailDAO {
    @PersistenceContext
    private EntityManager em;

    public void create(SequenceNoFileDetail sequenceNoFileDetail) throws PreexistingEntityException, Exception {
        em.persist(sequenceNoFileDetail);
    }

    public void edit(SequenceNoFileDetail sequenceNoFileDetail) throws NonexistentEntityException, Exception {
        em.merge(sequenceNoFileDetail);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SequenceNoFileDetail sequenceNoFileDetail;
        try {
            sequenceNoFileDetail = em.getReference(SequenceNoFileDetail.class, id);
            sequenceNoFileDetail.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The sequenceNo with id " + id + " no longer exists.", enfe);
        }
        em.remove(sequenceNoFileDetail);
    }

    public SequenceNoFileDetail findSequenceNoFileDetail(Integer id) {
        return em.find(SequenceNoFileDetail.class, id);
    }

    public List<SequenceNoFileDetail> findSequenceNoDetailBySeauenceNoFileId(Integer squenceNoFileId){
        Query q = em.createQuery("select object(snfd) from SequenceNoFileDetail as snfd join snfd.sequenceNoFile as snf where snf.id = :squenceNoFileId ");
        q.setParameter("squenceNoFileId",squenceNoFileId);
        return q.getResultList();
    }
    
    public List<SequenceNoFileDetail> findSequenceNoDetailBySeauenceNoId(Integer squenceNoId){
        Query q = em.createQuery("select object(snfd) from SequenceNoFileDetail as snfd join snfd.sequenceNoFile as snf where snf.sequenceNo.id = :squenceNoId ");
        q.setParameter("squenceNoId",squenceNoId);
        return q.getResultList();
    }
    
    public synchronized void updateUsedStatus(Integer squenceNoId, String squenceNo) {
        Query q = em.createNativeQuery("UPDATE sequence_no_file_detail SET status = 'Used' FROM sequence_no_file INNER JOIN sequence_no_file_detail ON sequence_no_file.id = sequence_no_file_detail.sequence_no_file_id AND sequence_no_file.sequence_no_id = :squenceNoId AND sequence_no_file_detail.sequence_no = :squenceNo");
        q.setParameter("squenceNoId", squenceNoId);
        q.setParameter("squenceNo", squenceNo);
        
        try{
            q.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
