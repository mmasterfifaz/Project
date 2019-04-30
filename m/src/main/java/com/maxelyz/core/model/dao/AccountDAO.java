/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Account;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author Oat
 */
@Transactional
public class AccountDAO {

    @PersistenceContext
    private EntityManager em;


    public void create(Account account) {
        em.persist(account);
    }

    public void edit(Account account) {
         account = em.merge(account);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        Account account;
        try {
            account = em.getReference(Account.class, id);
            account.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The Account with id " + id + " no longer exists.", enfe);
        }
        em.remove(account);
    }

    public List<Account> findAccountEntities() {
        return findAccountEntities(true, -1, -1);
    }

    public List<Account> findAccountEntities(int maxResults, int firstResult) {
        return findAccountEntities(false, maxResults, firstResult);
    }

    private List<Account> findAccountEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Account as o order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Account findAccount(Integer id) {
        return em.find(Account.class, id);
    }

    public int getAccountCount() {
        return ((Long) em.createQuery("select count(o) from Account as o").getSingleResult()).intValue(); 
    }

}
