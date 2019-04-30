/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.CustomerHistoricalColumn;
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
public class CustomerHistoricalColumnDAO {
    
    @PersistenceContext
    private EntityManager em;


    public void create(CustomerHistoricalColumn customerHistoricalColumn)  {
        em.persist(customerHistoricalColumn);   
    }

    public void edit(CustomerHistoricalColumn customerHistoricalColumn) throws NonexistentEntityException, Exception {
        customerHistoricalColumn = em.merge(customerHistoricalColumn);       
    }

    public void destroy(Integer id) throws NonexistentEntityException {
            CustomerHistoricalColumn customerHistoricalColumn;
            try {
                customerHistoricalColumn = em.getReference(CustomerHistoricalColumn.class, id);
                customerHistoricalColumn.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customerHistoricalColumn with id " + id + " no longer exists.", enfe);
            }
           
            em.remove(customerHistoricalColumn);     
    }

    public List<CustomerHistoricalColumn> findCustomerHistoricalColumnEntities() {
        return findCustomerHistoricalColumnEntities(true, -1, -1);
    }

    public List<CustomerHistoricalColumn> findCustomerHistoricalColumnEntities(int maxResults, int firstResult) {
        return findCustomerHistoricalColumnEntities(false, maxResults, firstResult);
    }

    private List<CustomerHistoricalColumn> findCustomerHistoricalColumnEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CustomerHistoricalColumn as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public CustomerHistoricalColumn findCustomerHistoricalColumn(Integer id) {
        return em.find(CustomerHistoricalColumn.class, id);
    }

    public int getCustomerHistoricalColumnCount() {
        Query q = em.createQuery("select count(o) from CustomerHistoricalColumn as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<CustomerHistoricalColumn> findCustomerHistoricalHighlightColumnByGroup(CustomerHistoricalGroup customerHistoricalGroup,String highlightCol) {
        String sqlSelect = "select object(o) from CustomerHistoricalColumn as o where o.customerHistoricalGroup = ?1 ";
        StringBuilder sqlCol = new StringBuilder();

        if (highlightCol.length() > 0) {
            sqlCol.append("and o.columnNo in (");
            sqlCol.append(highlightCol);
            sqlCol.append(") ");
        }
        String sql = sqlSelect + sqlCol;
        Query q = em.createQuery(sql);
        q.setParameter(1, customerHistoricalGroup);
        return q.getResultList();
    }
    
    public List<CustomerHistoricalColumn> findCustomerHistoricalColumnByGroup(CustomerHistoricalGroup customerHistoricalGroup) {
        Query q = em.createQuery("select object(o) from CustomerHistoricalColumn as o where o.customerHistoricalGroup = ?1 ");
       
        q.setParameter(1, customerHistoricalGroup);
        return q.getResultList();
    }
}
