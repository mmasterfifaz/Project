/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoEmailMessage;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Transactional
public class SoEmailMessageDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoEmailMessage soEmailMessage) throws PreexistingEntityException {
        em.persist(soEmailMessage);
    }

    public void edit(SoEmailMessage soEmailMessage) throws NonexistentEntityException, Exception {
        soEmailMessage = em.merge(soEmailMessage);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoEmailMessage soEmailMessage;
        try {
            soEmailMessage = em.getReference(SoEmailMessage.class, id);
            soEmailMessage.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soEmailMessage with id " + id + " no longer exists.", enfe);
        }
        em.remove(soEmailMessage);
    }

    public List<SoEmailMessage> findSoEmailMessage() {
        return findSoEmailMessage(true, -1, -1);
    }

    public List<SoEmailMessage> findSoEmailMessage(int maxResults, int firstResult) {
        return findSoEmailMessage(false, maxResults, firstResult);
    }

    private List<SoEmailMessage> findSoEmailMessage(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoEmailMessage as o order by o.id");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<SoEmailMessage> findSoEmailMessageTransferCond(String cond) {
        Query q = em.createQuery("select object(o) from SoEmailMessage o join o.soMessage m join m.soAccount ac join ac.soChannel ch" +
                " where o.id=o.id " +
                " and m.soService.id is not null " +

//                " and ch.id=5 " +    //=578
//                " and ac.id=15 " +    //=336
//                " and m.case_status='PS' " +    //=8
//                " and m.priority_enum_id='PRIORITY_HI' " +    //=10
//                " and o.emailFrom='apichat@terrabit.co.th' " +    //=17
//                " and m.id=4505 " +    //=1
//                " and m.parentSoMessage.id=4441 " +    //=3
//                " and m.receive_by_id in (select u.id from Users u where u.userGroup.id=1) " +    //=306
//                " and m.receive_by_id=125 " +    //=51

                cond +
                " order by o.id");

//        q.setMaxResults(20);

        return q.getResultList();
    }

    public SoEmailMessage findSoEmailMessage(Integer id) {
        return em.find(SoEmailMessage.class, id);
    }

    public int getSoEmailMessageCount() {
        Query q = em.createQuery("select count(o) from SoEmailMessage as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public Map<String, Integer> getEmailAccountList() {
        List<SoEmailMessage> soEmailMessages = this.findSoEmailMessage();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoEmailMessage obj : soEmailMessages) {
            values.put(obj.getEmailFrom(), obj.getId());
        }
        return values;
    }

}
