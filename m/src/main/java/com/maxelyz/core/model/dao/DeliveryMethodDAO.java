/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.DeliveryMethod;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class DeliveryMethodDAO  {

    @PersistenceContext
    private EntityManager em;

    public void create(DeliveryMethod deliveryMethod) throws PreexistingEntityException, Exception {
        em.persist(deliveryMethod);   
    }

    public void edit(DeliveryMethod deliveryMethod) throws NonexistentEntityException, Exception {
        deliveryMethod = em.merge(deliveryMethod);
            
    }

  public List<DeliveryMethod> findDeliveryMethodEntities() {
        return findDeliveryMethodEntities(true, -1, -1);
    }

    public List<DeliveryMethod> findDeliveryMethodEntities(int maxResults, int firstResult) {
        return findDeliveryMethodEntities(false, maxResults, firstResult);
    }

    private List<DeliveryMethod> findDeliveryMethodEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from DeliveryMethod as o where o.enable = true");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public DeliveryMethod findDeliveryMethod(Integer id) {
        return em.find(DeliveryMethod.class, id);
    }

    public int getDeliveryMethodCount() {
        Query q = em.createQuery("select count(o) from DeliveryMethod as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<DeliveryMethod> findDeliveryMethodByCode(String code) {
        Query q = em.createQuery("select object(o) from DeliveryMethod as o where o.enable = true and o.code in ("+code.trim()+")");
        return q.getResultList();
    }
    
    public int checkDeliveryMethodCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from DeliveryMethod as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from DeliveryMethod as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
}
