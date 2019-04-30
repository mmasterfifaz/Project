/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoMsgCasetypeMap;
import com.maxelyz.social.model.entity.SoMsgUserAssigntime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ukritj
 */
@Transactional
public class SoMsgCasetypeMapDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoMsgCasetypeMap soMsgCasetypeMap) throws PreexistingEntityException {
        em.persist(soMsgCasetypeMap);
    }

    public void edit(SoMsgCasetypeMap soMsgCasetypeMap) throws NonexistentEntityException, Exception {
        soMsgCasetypeMap = em.merge(soMsgCasetypeMap);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoMsgCasetypeMap soMsgCasetypeMap;
        try {
            soMsgCasetypeMap = em.getReference(SoMsgCasetypeMap.class, id);
            soMsgCasetypeMap.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soMsgCasetypeMap with id " + id + " no longer exists.", enfe);
        }
        em.remove(soMsgCasetypeMap);
    }

    public List<SoMsgCasetypeMap> findBySoMessageId(Integer soMessageId) {
        String sql = "select object(o) from SoMsgCasetypeMap as o where o.soMessage.id = :soMessageId order by id desc";
        Query q = em.createQuery(sql);
        q.setParameter("soMessageId", soMessageId);
        return q.getResultList();
    }
    
    public void removeBySoMessageId(Integer soMessageId) throws NonexistentEntityException{
        try{
            String sql = "delete from SoMsgCasetypeMap as o where o.soMessage.id = :soMessageId ";
            Query q = em.createQuery(sql);
            q.setParameter("soMessageId", soMessageId);
            q.executeUpdate();
        
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soMsgCasetypeMap with id " + soMessageId + " no longer exists.", enfe);
        }
    }
    
}
