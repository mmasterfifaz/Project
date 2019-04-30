/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.PaymentMethod;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author admin
 */
@Transactional
public class PaymentMethodDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(PaymentMethod paymentMethod) {
        em.persist(paymentMethod);
    }

    public void edit(PaymentMethod paymentMethod) throws IllegalOrphanException, NonexistentEntityException, Exception {
        paymentMethod = em.merge(paymentMethod);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        PaymentMethod paymentMethod;
        try {
            paymentMethod = em.getReference(PaymentMethod.class, id);
            paymentMethod.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The PaymentMethod with id " + id + " no longer exists.", enfe);
        }
        em.remove(paymentMethod);
    }

    public List<PaymentMethod> findPaymentMethodEntities() {
        return findPaymentMethodEntities(true, -1, -1);
    }

    public List<PaymentMethod> findPaymentMethodEntities(int maxResults, int firstResult) {
        return findPaymentMethodEntities(false, maxResults, firstResult);
    }

    private List<PaymentMethod> findPaymentMethodEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from PaymentMethod as o where o.enable = true order by o.seqNo");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public PaymentMethod findPaymentMethod(Integer id) throws Exception {
        return em.find(PaymentMethod.class, id);

    }

    public int getPaymentMethodCount() {
        Query q = em.createQuery("select count(o) from PaymentMethod as o");
        return ((Long) q.getSingleResult()).intValue();
    }    
        
    public int checkPaymentMethodCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from PaymentMethod as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from PaymentMethod as o where o.code =:code and o.enable = true and o.id <> :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
    
     public List<PaymentMethod> findPaymentMethod(int status, int enable ) {
        Query q = em.createQuery("select object(o) from PaymentMethod as o where o.enable = :enable and o.status=:status order by o.seqNo");
        q.setParameter("enable", enable==1);
        q.setParameter("status", status==1);
        return q.getResultList();
    }
     
      public Map<String, Integer> getPaymentMethodList() {
      List<PaymentMethod> paymentMethods = this.findPaymentMethodEntities();
      Map<String, Integer> values = new LinkedHashMap<String, Integer>();
      for (PaymentMethod obj : paymentMethods) {
            values.put(obj.getName(), obj.getId());
      }
            return values;
    }
      
    public PaymentMethod getPaymentMethodByName(String name) {
        Query q = em.createQuery("select object(o) from PaymentMethod as o where o.enable = true and o.status=true and o.name=:name order by o.seqNo");
        q.setParameter("name", name);
        return (PaymentMethod) q.getSingleResult();
    }
    
}
