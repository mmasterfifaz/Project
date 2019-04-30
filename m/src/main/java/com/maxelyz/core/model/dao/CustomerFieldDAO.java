/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;


import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.CustomerField;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CustomerFieldDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(CustomerField customerField) {
        em.persist(customerField);
    }

    public void edit(CustomerField customerField) {
        customerField = em.merge(customerField);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        CustomerField customerField;
        try {
            customerField = em.getReference(CustomerField.class, id);
            customerField.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The customerField with id " + id + " no longer exists.", enfe);
        }
        em.remove(customerField);
    }

    public List<CustomerField> findCustomerFieldEntities() {
        return findCustomerFieldEntities(true, -1, -1);
    }

    public List<CustomerField> findCustomerFieldEntities(int maxResults, int firstResult) {
        return findCustomerFieldEntities(false, maxResults, firstResult);
    }

    private List<CustomerField> findCustomerFieldEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CustomerField as o where o.enable = true order by o.seq");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public CustomerField findCustomerField(Integer id) {
        return em.find(CustomerField.class, id);
    }

    public int getCustomerFieldCount() {
        return ((Long) em.createQuery("select count(o) from CustomerField as o").getSingleResult()).intValue();
    }

    public Map<String, Integer> getMapCustomerField() {
        List<CustomerField> customerFields = this.findCustomerFieldEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CustomerField obj : customerFields) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
        public Map<String, String> getMapCustomerFieldName() {
        List<CustomerField> customerFields = this.findCustomerFieldEntities();
        Map<String, String> values = new LinkedHashMap<String, String>();
        for (CustomerField obj : customerFields) {
            values.put(obj.getName(), obj.getMappingField());
        }
        return values;
    }
    
    public CustomerField findCustomerFieldIdByMappingField(String mappingField) {
        Query q = em.createQuery("select object(o) from CustomerField as o where o.mappingField = :mappingField and o.enable = true");
        q.setParameter("mappingField", mappingField);
        try {
            q.setMaxResults(1);
            return (CustomerField) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
