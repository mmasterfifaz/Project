/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.Customer;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CustomerDAO {

    @PersistenceContext
    private EntityManager em;

    public String create(Customer customer) throws PreexistingEntityException, Exception {
        //Query q = em.createNativeQuery("{call GetNextSeq(:tableName)}");
        //q.setParameter("tableName", "CUSTOMER");
        //Object o = q.getSingleResult();
        //customer.setReferenceNo(o.toString());
        em.persist(customer);

        return customer.getReferenceNo();
    }

    public void edit(Customer customer) throws IllegalOrphanException, NonexistentEntityException, Exception {
        customer = em.merge(customer);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        Customer customer;
        try {
            customer = em.getReference(Customer.class, id);
            customer.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The customer with id " + id + " no longer exists.", enfe);
        }
        em.remove(customer);
    }

    public List<Customer> findCustomerEntities() {
        return findCustomerEntities(true, -1, -1);
    }

    public List<Customer> findCustomerEntities(int maxResults, int firstResult) {
        return findCustomerEntities(false, maxResults, firstResult);
    }

    private List<Customer> findCustomerEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Customer as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Customer findCustomer(Integer id) {
        return em.find(Customer.class, id);
    }

    public int getCustomerCount() {
        return ((Long) em.createQuery("select count(o) from Customer as o").getSingleResult()).intValue();
    }

    public int getMarketingCustomerCountByCriteria(int marketingId, String gender, int fromage, int toage, int provinceId) {
        Query q = em.createQuery("select count(a) from Customer as a "
                + "join a.marketingCustomerCollection as b "
                + "where b.marketingCustomerPK.id = ?1");
        q.setParameter(1, marketingId);

        return ((Long)q.getSingleResult()).intValue();
    }

    public List<Customer> findCustomerByTelephoneNo(String telephoneNumber) {
        Query q = em.createQuery("select o from Customer as o where o.mobilePhone = ?1");
        q.setParameter(1, telephoneNumber);
        return q.getResultList();
    }
    
    public List<Customer> findCustomerByRefNo(String refNo) {
        Query q = em.createQuery("select o from Customer as o where o.referenceNo = ?1");
        q.setParameter(1, refNo);
        return q.getResultList();
    }
    
    public Integer createByReferenceNo(Customer customer) {
        List<Customer> list = findCustomerByReferenceNo(customer.getReferenceNo());
        Integer newId = null;
        if (list.size() <= 0) {
            em.persist(customer);
            em.flush();
            em.refresh(customer);
            newId = customer.getId();
        } else {
            Customer item = list.get(0);
            newId = item.getId();
        }
        
        return newId;
    }
    
    public List<Customer> findCustomerByReferenceNo(String referenceNo) {
        Query q = em.createQuery("select o from Customer as o where o.referenceNo = ?1");
        q.setParameter(1, referenceNo);
        return q.getResultList();
    }
    
    public int checkDuplicateCustomer(Customer customer ) {
        String sql = "select count(c) from Customer c where c.name like '%"+customer.getName().trim()+"%' and c.dob = ?1 ";
         
        Query q = em.createQuery(sql);
       // q.setParameter(1, customer.getName());
        q.setParameter(1, customer.getDob());
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public int checkDuplicateCustomerByName(Customer customer ) {
        String sql = "select count(c) from Customer c where "
                + "(ltrim(rtrim(c.name)) + ' ' + ltrim(rtrim(c.surname)))  like '"+customer.getName().trim()+ ' ' +customer.getSurname().trim()+"'";

        Query q = em.createQuery(sql);
        return ((Long) q.getSingleResult()).intValue();
    }

    public int checkDuplicateCustomerByFlexField1(Customer customer ) {
        String sql = "select count(c) from Customer c where c.flexfield1 =:flexfield1 " ;
        Query q = em.createQuery(sql);
        q.setParameter("flexfield1",customer.getFlexfield1());
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public Customer findDuplicateCustomerByName(Customer customer ) {
        String sql = "select object(c) from Customer c where "
                + "(ltrim(rtrim(c.name)) + ' ' + ltrim(rtrim(c.surname)))  like '"+customer.getName().trim()+ ' ' +customer.getSurname().trim()+"'";

        try {
            Query q = em.createQuery(sql);
            return (Customer) q.getSingleResult();
        } catch(Exception e) {
            return null;
        }
    }
}
