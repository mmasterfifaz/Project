/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ContactTransfer;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class ContactTransferDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(ContactTransfer contactTransfer) throws Exception {
        em.persist(contactTransfer);   
    }

    public void edit(ContactTransfer contactTransfer) throws NonexistentEntityException, Exception {
        contactTransfer = em.merge(contactTransfer);           
    }

   

    public List<ContactTransfer> findContactTransferEntities() {
        return findContactTransferEntities(true, -1, -1);
    }

    public List<ContactTransfer> findContactTransferEntities(int maxResults, int firstResult) {
        return findContactTransferEntities(false, maxResults, firstResult);
    }

    private List<ContactTransfer> findContactTransferEntities(boolean all, int maxResults, int firstResult) {
         Query q = em.createQuery("select object(o) from ContactTransfer as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
       
    }

    public ContactTransfer findContactTransfer(Integer id) {
        return em.find(ContactTransfer.class, id);   
    }

    public int getContactTransferCount() {
        Query q = em.createQuery("select count(o) from ContactTransfer as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
