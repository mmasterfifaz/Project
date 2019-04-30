/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.CustomerAuditLog;
import java.io.Serializable;
import java.util.Date;
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
 * @author Oat
 */
@Transactional
public class CustomerAuditLogDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(CustomerAuditLog customerAuditLog) {
        em.persist(customerAuditLog);
    }

    public void edit(CustomerAuditLog customerAuditLog) throws Exception {
        customerAuditLog = em.merge(customerAuditLog);         
    }

    

    public List<CustomerAuditLog> findCustomerAuditLogEntities() {
        return findCustomerAuditLogEntities(true, -1, -1);
    }

    public List<CustomerAuditLog> findCustomerAuditLogEntities(int maxResults, int firstResult) {
        return findCustomerAuditLogEntities(false, maxResults, firstResult);
    }

    private List<CustomerAuditLog> findCustomerAuditLogEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CustomerAuditLog as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public CustomerAuditLog findCustomerAuditLog(Integer id) {

        return em.find(CustomerAuditLog.class, id);

    }

    public int getCustomerAuditLogCount() {

        Query q = em.createQuery("select count(o) from CustomerAuditLog as o");
        return ((Long) q.getSingleResult()).intValue();

    }
    
    public List<CustomerAuditLog> findCustomerAuditLogList(Integer customerId, Date fromDate, Date toDate){
        List<CustomerAuditLog> list = null;
        try{
            String sql = "select object(o) from CustomerAuditLog as o where 1 = 1";
            if(customerId != null && customerId != 0){
                sql += " and o.customerId = :customerId";
            }
            if(fromDate != null && toDate != null){
                sql += " and o.createDate between :fromDate and DATEADD(day,1,:toDate)";
            }
            sql += " order by o.createDate desc";
            
            Query q = em.createQuery(sql);
            q.setParameter("customerId", customerId.intValue());
            q.setParameter("fromDate", fromDate);
            q.setParameter("toDate", toDate);
            
            list = q.getResultList();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return list;
    }
    
}
