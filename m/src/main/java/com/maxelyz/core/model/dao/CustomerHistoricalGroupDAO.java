/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import com.maxelyz.core.model.entity.CustomerHistoricalColumn;
import com.maxelyz.core.model.entity.CustomerHistorical;
import com.maxelyz.core.model.entity.CustomerHistoricalGroup;
import java.util.List;
import javax.persistence.*;
import org.springframework.transaction.annotation.Transactional;
import com.maxelyz.core.model.value.front.customerHandling.CustomerHistoricalValue;

/**
 *
 * @author oat
 */
@Transactional
public class CustomerHistoricalGroupDAO  {

    @PersistenceContext
    private EntityManager em;

    public void create(CustomerHistoricalGroup customerHistoricalGroup) throws PreexistingEntityException, Exception {
        em.persist(customerHistoricalGroup); 
    }

    public void edit(CustomerHistoricalGroup customerHistoricalGroup) throws IllegalOrphanException, NonexistentEntityException, Exception {
        customerHistoricalGroup = em.merge(customerHistoricalGroup);     
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        CustomerHistoricalGroup customerHistoricalGroup;
        try {
            customerHistoricalGroup = em.getReference(CustomerHistoricalGroup.class, id);
            customerHistoricalGroup.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The customerHistoricalGroup with id " + id + " no longer exists.", enfe);
        }
        
        em.remove(customerHistoricalGroup);
           
    }

    public List<CustomerHistoricalGroup> findCustomerHistoricalGroupEntities() {
        return findCustomerHistoricalGroupEntities(true, -1, -1);
    }

    public List<CustomerHistoricalGroup> findCustomerHistoricalGroupEntities(int maxResults, int firstResult) {
        return findCustomerHistoricalGroupEntities(false, maxResults, firstResult);
    }

    private List<CustomerHistoricalGroup> findCustomerHistoricalGroupEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CustomerHistoricalGroup as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public CustomerHistoricalGroup findCustomerHistoricalGroup(Integer id) {
        return em.find(CustomerHistoricalGroup.class, id);
    }

    public int getCustomerHistoricalGroupCount() {
        Query q = em.createQuery("select count(o) from CustomerHistoricalGroup as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
            
    public void editCustomerHistroricalGroup(CustomerHistoricalGroup customerHistoricalGroup) throws IllegalOrphanException, NonexistentEntityException, Exception {
       Query q = em.createQuery("Delete from CustomerHistoricalColumn where customerHistoricalGroup = ?1");
       q.setParameter(1, customerHistoricalGroup);
       q.executeUpdate();

       q = em.createQuery("Delete from CustomerHistorical where customerHistoricalGroup = ?1");
       q.setParameter(1, customerHistoricalGroup);
       q.executeUpdate();
       
       customerHistoricalGroup = em.merge(customerHistoricalGroup);       
    }

}
