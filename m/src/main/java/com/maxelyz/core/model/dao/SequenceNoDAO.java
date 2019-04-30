/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.SequenceNo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ejb.Local;
import javax.persistence.*;

import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author oat
 */
@Transactional
public class SequenceNoDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SequenceNo sequenceNo) throws PreexistingEntityException, Exception {
        em.persist(sequenceNo);          
    }

    public void edit(SequenceNo sequenceNo) throws NonexistentEntityException, Exception {
        sequenceNo = em.merge(sequenceNo);    
    }

    public synchronized String getNextSeq() {
        return null;
    }
    
    public void destroy(Integer id) throws NonexistentEntityException {
        SequenceNo sequenceNo;
        try {
            sequenceNo = em.getReference(SequenceNo.class, id);
            sequenceNo.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The sequenceNo with id " + id + " no longer exists.", enfe);
        }
        em.remove(sequenceNo);
    }

    public List<SequenceNo> findSequenceNoEntities() {
        return findSequenceNoEntities(true, -1, -1);
    }

    public List<SequenceNo> findSequenceNoEntities(int maxResults, int firstResult) {
        return findSequenceNoEntities(false, maxResults, firstResult);
    }

    private List<SequenceNo> findSequenceNoEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SequenceNo as o where o.enable = true order by o.system desc, o.name asc");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SequenceNo findSequenceNo(Integer id) {
       return em.find(SequenceNo.class, id);
    }
    
    public int checkSequenceNoName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from SequenceNo as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from SequenceNo as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
      
    public synchronized void updateNextSeq(Integer id) {
        Query q = em.createQuery("update SequenceNo set nextRunno = nextRunno + 1 where id = :id");
        q.setParameter("id", id);
        try{
            q.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public synchronized String getNextSeqFromSequenceNoFile(Integer id) {
        Query q = em.createNativeQuery("SELECT TOP 1 sequence_no FROM sequence_no_file INNER JOIN sequence_no_file_detail ON sequence_no_file.id = sequence_no_file_detail.sequence_no_file_id WHERE sequence_no_file.sequence_no_id = :id AND sequence_no_file.enable = 1 AND sequence_no_file.status = 1 AND sequence_no_file_detail.status = 'Available' ORDER BY sequence_no_file_detail.id ASC");
        q.setParameter("id", id);
        return q.getSingleResult().toString();
    }
        
    public synchronized SequenceNo updateSequence(Integer id) {
        SequenceNo sequenceNo = findSequenceNo(id);
        if(sequenceNo != null) {
            if (sequenceNo.getGenerateType().equalsIgnoreCase("Internal")) {
                Date now = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String today = dateFormat.format(now);        
                Integer year = Integer.parseInt(today.substring(0, 4));
                Integer month = Integer.parseInt(today.substring(5, 7));
                String resetType = sequenceNo.getResetRunningType();

                if(sequenceNo.getYear() == null) {  // first save
                    sequenceNo.setYear(year);
                } else if(!sequenceNo.getYear().equals(year)) { // update current year
                        sequenceNo.setYear(year);
                        if(resetType.equals("newYear")) {       //reset running no 
                            sequenceNo.setNextRunno(1);
                        }
                }

                if(sequenceNo.getMonth() == null) { // first save
                    sequenceNo.setMonth(month);
                } else if (!sequenceNo.getMonth().equals(month)) {  // update current month
                        sequenceNo.setMonth(month);
                        if(resetType.equals("newMonth")) {          //reset running no 
                            sequenceNo.setNextRunno(1);
                        }
                }

                try{
                    edit(sequenceNo);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }else{
                
            }
         
        }
        return findSequenceNo(id);
    }

    public List<SequenceNo> findSequenceNoByGenerateType() {
        try {
            Query q = em.createQuery("select object(o) from SequenceNo as o where o.enable = true and o.generateType ='File Upload' order by o.id ");
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }


    
}
