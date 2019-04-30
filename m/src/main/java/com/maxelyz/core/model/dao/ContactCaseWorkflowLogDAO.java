/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.ContactCase;
import com.maxelyz.core.model.entity.ContactCaseWorkflowLog;
import com.maxelyz.utils.JSFUtil;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Oat
 */
@Transactional
public class ContactCaseWorkflowLogDAO  {


    private static Logger log = Logger.getLogger(ContactHistoryDAO.class);
    @PersistenceContext
    private EntityManager em;
    
    public void create(ContactCaseWorkflowLog contactCaseWorkflowLog) {
        
        em.persist(contactCaseWorkflowLog);
           
    }

    public void edit(ContactCaseWorkflowLog contactCaseWorkflowLog) throws  Exception {
        contactCaseWorkflowLog = em.merge(contactCaseWorkflowLog);
    }

    public synchronized void editAcceptanceWorkflowLog(ContactCaseWorkflowLog contactCaseWorkflowLog) {
        Query q = em.createQuery("update ContactCaseWorkflowLog set remark=?1, acceptDate=?2, acceptStatus=?3, receiveUsers=?4, "
                + "receiveUserName=?5, receiveUserSurname=?6 where id = ?7");
        q.setParameter(1, contactCaseWorkflowLog.getRemark());
        q.setParameter(2, contactCaseWorkflowLog.getAcceptDate());
        q.setParameter(3, contactCaseWorkflowLog.getAcceptStatus());
        q.setParameter(4, contactCaseWorkflowLog.getReceiveUsers());
        q.setParameter(5, contactCaseWorkflowLog.getReceiveUserName());
        q.setParameter(6, contactCaseWorkflowLog.getReceiveUserSurname());
        q.setParameter(7, contactCaseWorkflowLog.getId());
        q.executeUpdate();
    }
    
    public void destroy(Integer id) throws NonexistentEntityException {
        ContactCaseWorkflowLog contactCaseWorkflowLog;
        try {
            contactCaseWorkflowLog = em.getReference(ContactCaseWorkflowLog.class, id);
            contactCaseWorkflowLog.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The contactCaseWorkflowLog with id " + id + " no longer exists.", enfe);
        }
        ContactCase contactCase = contactCaseWorkflowLog.getContactCase();
        if (contactCase != null) {
            contactCase.getContactCaseWorkflowLogCollection().remove(contactCaseWorkflowLog);
            contactCase = em.merge(contactCase);
        }
        em.remove(contactCaseWorkflowLog);           
    }

    public List<ContactCaseWorkflowLog> findContactCaseWorkflowLogEntities() {
        return findContactCaseWorkflowLogEntities(true, -1, -1);
    }

    public List<ContactCaseWorkflowLog> findContactCaseWorkflowLogEntities(int maxResults, int firstResult) {
        return findContactCaseWorkflowLogEntities(false, maxResults, firstResult);
    }

    private List<ContactCaseWorkflowLog> findContactCaseWorkflowLogEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ContactCaseWorkflowLog as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public ContactCaseWorkflowLog findContactCaseWorkflowLog(Integer id) {
        return em.find(ContactCaseWorkflowLog.class, id);

    }

    public int getContactCaseWorkflowLogCount() {
        Query q = em.createQuery("select count(o) from ContactCaseWorkflowLog as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public ContactCaseWorkflowLog findContactCaseWorkflowLogByContactCase(Integer contactCaseId) {
        try{
            Query q = em.createQuery("select object(o) from ContactCaseWorkflowLog as o "
                    + "left join o.users.userMemberCollection m "
                    + "where o.contactCase.id = :contactCaseId and (o.users.id = :userId or m.usersMember.id = :userId) "
                    + "and o.receiveUsers is null and o.acceptStatus is null order by seq_no ");
            q.setParameter("contactCaseId", contactCaseId);
            q.setParameter("userId", JSFUtil.getUserSession().getUsers().getId());
            q.setMaxResults(1);
            return (ContactCaseWorkflowLog)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public ContactCaseWorkflowLog findContactCaseWorkflowLogAccept(Integer contactCaseId) {
        try{
            Query q = em.createQuery("select object(o) from ContactCaseWorkflowLog as o "
                    + "where o.contactCase.id = :contactCaseId and o.receiveUsers.id = :userId ");
            q.setParameter("contactCaseId", contactCaseId);
            q.setParameter("userId", JSFUtil.getUserSession().getUsers().getId());
            q.setMaxResults(1);
            return (ContactCaseWorkflowLog)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public ContactCaseWorkflowLog findContactCaseWorkflowLogBySeq(Integer contactCaseId, Integer seqNo) {
        try{
            Query q = em.createQuery("select object(o) from ContactCaseWorkflowLog as o "
                + "where o.contactCase.id = :contactCaseId and o.seqNo = :seqNo ");
            q.setParameter("contactCaseId", contactCaseId);
            q.setParameter("seqNo", seqNo);
            ContactCaseWorkflowLog contactCaseWorkflowLog = (ContactCaseWorkflowLog) q.getSingleResult();
            return contactCaseWorkflowLog;
        } catch (Exception e) {
            return null;
        }
    }
}
