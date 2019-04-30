/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.CustomerType;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CustomerTypeDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(CustomerType customerType) {
        em.persist(customerType);
    }

    public void edit(CustomerType customerType) {
        customerType = em.merge(customerType);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        CustomerType customerType;
        try {
            customerType = em.getReference(CustomerType.class, id);
            customerType.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The customerType with id " + id + " no longer exists.", enfe);
        }

        em.remove(customerType);
    }

    public List<CustomerType> findCustomerTypeEntities() {
        return findCustomerTypeEntities(true, -1, -1);
    }

    public List<CustomerType> findCustomerTypeEntities(int maxResults, int firstResult) {
        return findCustomerTypeEntities(false, maxResults, firstResult);
    }

    private List<CustomerType> findCustomerTypeEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CustomerType as o order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public CustomerType findCustomerType(Integer id) {
        return em.find(CustomerType.class, id);
    }

    public int getCustomerTypeCount() {
        return ((Long) em.createQuery("select count(o) from CustomerType as o").getSingleResult()).intValue();
    }

     public Map<String, Integer> getCustomeTypeList() {
        List<CustomerType> customerTypes = this.findCustomerTypeEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CustomerType obj : customerTypes) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
}
