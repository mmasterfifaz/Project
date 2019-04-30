/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.Reminder;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ReminderDAO implements Serializable {

    @PersistenceContext
    private EntityManager em;

    public void create(Reminder reminder) throws PreexistingEntityException, Exception {
        em.persist(reminder);        
    }

    public void edit(Reminder reminder) throws NonexistentEntityException, Exception {
        reminder = em.merge(reminder);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        Reminder reminder;
        try {
            reminder = em.getReference(Reminder.class, id);
            reminder.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The reminder with id " + id + " no longer exists.", enfe);
        }
        em.remove(reminder);       
    }

    public List<Reminder> findReminderEntities() {
        return findReminderEntities(true, -1, -1);
    }

    public List<Reminder> findReminderEntities(int maxResults, int firstResult) {
        return findReminderEntities(false, maxResults, firstResult);
    }

    private List<Reminder> findReminderEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Reminder as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Reminder findReminder(Integer id) {
        return em.find(Reminder.class, id);
    }

    public int getReminderCount() {
        Query q = em.createQuery("select count(o) from Reminder as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
