/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ListDetail;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author User
 */

@Transactional
public class ListDetailDAO {
    @PersistenceContext
    private EntityManager em;

    public void create(ListDetail listDetail) throws PreexistingEntityException, Exception  {
        em.persist(listDetail);
    }

    public void edit(ListDetail listDetail) {
        listDetail = em.merge(listDetail);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        ListDetail listDetail;
        try {
            listDetail = em.getReference(ListDetail.class, id);
            listDetail.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The listDetail with id " + id + " no longer exists.", enfe);
        }
        em.remove(listDetail);
    }

    public List<ListDetail> findListDetailEntities() {
        return findListDetailEntities(true, -1, -1);
    }

    public List<ListDetail> findListDetailEntities(int maxResults, int firstResult) {
        return findListDetailEntities(false, maxResults, firstResult);
    }

    private List<ListDetail> findListDetailEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ListDetail as o where o.enable = 1");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
    
    public List<ListDetail> findListDetailByListGroupID(Integer listGroupId) {
        Query q = em.createQuery("select object(o) from ListDetail as o where o.enable = 1 and o.listGroupId = ?1 order by o.seqNo");
        q.setParameter(1, listGroupId);
        return q.getResultList();
    }
    
    public List<ListDetail> findListMaritalStatus() {
        Query q = em.createQuery("select object(o) from ListDetail as o where o.enable = 1 and o.listGroupId = 5 order by o.seqNo");
        return q.getResultList();
    }

    public ListDetail findListDetail(Integer id) {
        return em.find(ListDetail.class, id);
    }
    
    public List<ListDetail> findListDetailfx(String code) {
        Query q = em.createQuery("select object(o) from ListDetail as o "
                + "where o.listGroupId IN "
                + "(SELECT g.id FROM ListGroup as g where g.code = '"+code+"') and o.enable = true order by o.seqNo");
        return q.getResultList();
    }  
}
