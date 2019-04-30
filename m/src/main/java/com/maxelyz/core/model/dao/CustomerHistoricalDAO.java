/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.CustomerHistorical;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.CustomerHistoricalGroup;
import java.util.List;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class CustomerHistoricalDAO  {

    @PersistenceContext
    private EntityManager em;

    public void create(CustomerHistorical customerHistorical) throws PreexistingEntityException, Exception {
        em.persist(customerHistorical);    
    }

    public void edit(CustomerHistorical customerHistorical) throws NonexistentEntityException, Exception {
        customerHistorical = em.merge(customerHistorical);   
    }

    public void destroy(Integer id) throws NonexistentEntityException {
            CustomerHistorical customerHistorical;
            try {
                customerHistorical = em.getReference(CustomerHistorical.class, id);
                customerHistorical.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customerHistorical with id " + id + " no longer exists.", enfe);
            }
           
            em.remove(customerHistorical); 
    }

    public List<CustomerHistorical> findCustomerHistoricalEntities() {
        return findCustomerHistoricalEntities(true, -1, -1);
    }

    public List<CustomerHistorical> findCustomerHistoricalEntities(int maxResults, int firstResult) {
        return findCustomerHistoricalEntities(false, maxResults, firstResult);
    }

    private List<CustomerHistorical> findCustomerHistoricalEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CustomerHistorical as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public CustomerHistorical findCustomerHistorical(Integer id) {
        return em.find(CustomerHistorical.class, id);
    }

    public int getCustomerHistoricalCount() {
        Query q = em.createQuery("select count(o) from CustomerHistorical as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<CustomerHistorical> findCustomerHistoricalByReferenceCustomer(CustomerHistoricalGroup customerHistoricalGroup, String refCustomer) {
        Query q = em.createQuery("select object(o) from CustomerHistorical as o where  o.customerHistoricalGroup = ?1 and o.customerReferenceNo = ?2");
        q.setParameter(1, customerHistoricalGroup);
        q.setParameter(2, refCustomer);
        return q.getResultList();
    }
}
