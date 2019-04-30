/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Customer;
import com.maxelyz.core.model.entity.MarketingCustomer;
import com.maxelyz.core.model.entity.MarketingCustomerPK;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class MarketingCustomerDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(MarketingCustomer marketingCustomer) {
        em.persist(marketingCustomer);
    }

    public void createMc(int marketingId, List<Customer> list) {
        Date now = new Date();
        MarketingCustomer marketingCustomer = null;
        for (Customer c : list) {
            marketingCustomer = new MarketingCustomer(marketingId, c.getId());
            marketingCustomer.setNewList(Boolean.TRUE);
            marketingCustomer.setAssign(Boolean.FALSE);
            marketingCustomer.setCreateDate(now);
            em.persist(marketingCustomer);
        }
    }

    public void edit(MarketingCustomer marketingCustomer) throws NonexistentEntityException, Exception {
        marketingCustomer = em.merge(marketingCustomer);
    }

    public void edit1(MarketingCustomer marketingCustomer) throws NonexistentEntityException, Exception {
        Query q = em.createNativeQuery("update marketing_customer set assign = ?1 where customer_id = ?2 and marketing_id = ?3");
        q.setParameter(1, marketingCustomer.getAssign());
        q.setParameter(2, marketingCustomer.getMarketingCustomerPK().getCustomerId());
        q.setParameter(3, marketingCustomer.getMarketingCustomerPK().getMarketingId());

        q.executeUpdate();
    }

    public void destroy(MarketingCustomerPK id) throws NonexistentEntityException {
        MarketingCustomer marketingCustomer;
        try {
            marketingCustomer = em.getReference(MarketingCustomer.class, id);
            marketingCustomer.getMarketingCustomerPK();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The marketingCustomer with id " + id + " no longer exists.", enfe);
        }
        em.remove(marketingCustomer);

    }

    public List<MarketingCustomer> findMarketingCustomerEntities() {
        return findMarketingCustomerEntities(true, -1, -1);
    }

    public List<MarketingCustomer> findMarketingCustomerEntities(int maxResults, int firstResult) {
        return findMarketingCustomerEntities(false, maxResults, firstResult);
    }

    private List<MarketingCustomer> findMarketingCustomerEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from MarketingCustomer as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public MarketingCustomer findMarketingCustomer(MarketingCustomerPK id) {
        return em.find(MarketingCustomer.class, id);
    }

    public int getMarketingCustomerCount() {
        return ((Long) em.createQuery("select count(o) from MarketingCustomer as o").getSingleResult()).intValue();
    }

    public List<MarketingCustomer> findReferMarketingCustomer(String name, String surname, String mobilePhone, int marketingId) {
        List<MarketingCustomer> result = null;
        boolean isAll = true;
        String where = " ";
        if (name != null && !name.isEmpty()) {
            isAll = false;
            where += " o.customer.name like '%" + name + "%' and ";
        }
        if (surname != null && !surname.isEmpty()) {
            isAll = false;
            where += " o.customer.surname like '%" + surname + "%' and ";
        }
        if (mobilePhone != null && !mobilePhone.isEmpty()) {
            isAll = false;
            where += " o.customer.mobilePhone like '%" + mobilePhone + "%' and ";
        }
        if (marketingId != 0) {
            isAll = false;
            where += " o.marketingCustomerPK.marketingId = " + marketingId + " and ";
        }
        if (isAll) {
            where = " ";
        }
        if (!isAll) {
            where = " where " + where;
            where = where.substring(0, where.length() - 4);
        }
        Query q = em.createQuery("select object(o) from MarketingCustomer as o " + where);
        q.setMaxResults(500);
        result = q.getResultList();
        return result;
    }

}
