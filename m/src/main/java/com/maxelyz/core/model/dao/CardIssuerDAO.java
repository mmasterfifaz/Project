/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.CardIssuer;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class CardIssuerDAO  {

    @PersistenceContext
    private EntityManager em;

    public void create(CardIssuer cardIssuer) throws PreexistingEntityException, Exception {
        em.persist(cardIssuer); 
    }

    public void edit(CardIssuer cardIssuer) throws NonexistentEntityException, Exception {
        cardIssuer = em.merge(cardIssuer);
        
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        CardIssuer cardIssuer;
        try {
            cardIssuer = em.getReference(CardIssuer.class, id);
            cardIssuer.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The cardIssuer with id " + id + " no longer exists.", enfe);
        }
        em.remove(cardIssuer);
    }

    public List<CardIssuer> findCardIssuerEntities() {
        return findCardIssuerEntities(true, -1, -1);
    }

    public List<CardIssuer> findCardIssuerEntities(int maxResults, int firstResult) {
        return findCardIssuerEntities(false, maxResults, firstResult);
    }

    private List<CardIssuer> findCardIssuerEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CardIssuer as o order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public CardIssuer findCardIssuer(Integer id) {
        return em.find(CardIssuer.class, id);
    }

    public int getCardIssuerCount() {
        Query q = em.createQuery("select count(o) from CardIssuer as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public CardIssuer findByCardNo(String cardNo) {
        CardIssuer obj = null;
        Query q = em.createQuery("select object(o) from CardIssuer as o where o.code = :cardNo");
        q.setParameter("cardNo", cardNo);
        try {
            obj = (CardIssuer) q.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            obj = null;
        }
        return obj;
    }
    
    public List findCardIssuerName(String cardIssuer) {
        Query q = em.createQuery("select b.id, b.name from CardIssuer a"
                + " join a.bank b"
                + " where b.id in (" + cardIssuer + ")"
                + " group by b.id, b.name"
                + " order by b.name");
        return q.getResultList();
    }
    
    public Map<String, String> getCardIssuerList(String cardIssuer) {
        List<CardIssuer> cardIssuers = findCardIssuerName(cardIssuer);
        Map<String, String> values = new LinkedHashMap<String, String>();
        for (CardIssuer obj : cardIssuers) {
            values.put(obj.getIssuerName(), obj.getIssuerName());
        }
        return values;
    }
    
    public CardIssuer findByCardList(String cardNo) {
        if(cardNo.length() > 6) {
            cardNo = cardNo.substring(0, 6);
            Query q = em.createQuery("select object(o) from CardIssuer as o where o.cardList like '%"+cardNo+"%'");
            try {
                q.setMaxResults(1);
                return (CardIssuer) q.getSingleResult();
            } catch (NoResultException ex) { 
                return null;
            }
        } else {
            return null;
        }
    }
    
    public CardIssuer findByCardList(String cardNo, String paymentCard) {
        if(cardNo.length() > 6) {
            cardNo = cardNo.substring(0, 6);
            Query q = em.createQuery("select object(o) from CardIssuer as o where o.cardList like '%"+cardNo+"%' and paymentCard = '" + paymentCard + "'");
            try {
                q.setMaxResults(1);
                return (CardIssuer) q.getSingleResult();
            } catch (NoResultException ex) { 
                return null;
            }
        } else {
            return null;
        }
    }
    
}
