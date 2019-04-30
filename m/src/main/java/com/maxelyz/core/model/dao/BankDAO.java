/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Bank;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author ukritj
 */
@Transactional
public class BankDAO {

    @PersistenceContext
    protected EntityManager em;


    public void create(Bank bank) {
        em.persist(bank);           
    }

    public void edit(Bank bank) throws IllegalOrphanException, NonexistentEntityException, Exception {
        bank = em.merge(bank);  
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        Bank bank;
        try {
            bank = em.getReference(Bank.class, id);
            bank.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The bank with id " + id + " no longer exists.", enfe);
        }
        em.remove(bank);         
    }

    public List<Bank> findBankEntities() {
        return findBankEntities(true, -1, -1);
    }

    public List<Bank> findBankEntities(int maxResults, int firstResult) {
        return findBankEntities(false, maxResults, firstResult);
    }

    private List<Bank> findBankEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Bank as o where o.enable = true and o.status = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Bank findBank(Integer id) throws Exception {
        return em.find(Bank.class, id);

    }

    public int getBankCount() {
        Query q = em.createQuery("select count(o) from Bank as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<Bank> findByFilter(String txtSearch, String status){
        List<Bank> list = null;
        try{
            String sql = "select object(o) from Bank as o where o.enable = true";
            if(txtSearch != null && !txtSearch.equals("")){
                sql += " and (UPPER(o.code) like :txtSearch or UPPER(o.name) like :txtSearch or UPPER(o.nameEn) like :txtSearch)";
            }
            if(status != null && !status.equals("")){
                if(status.equals("0")){
                    sql += " and (o.status = false or o.status is null)";
                }else if(status.equals("1")) {
                    sql += " and o.status = true";
                }
            }
            sql += " order by o.name";
            
            Query q = em.createQuery(sql);
            if(txtSearch != null && !txtSearch.equals("")){
                q.setParameter("txtSearch", "%" + txtSearch.toUpperCase() + "%");
            }
            
            list = q.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    
    public List<Bank> findBankByIds(String ids){
        List<Bank> list = null;
        String sql = "";
        try{
            if(ids.equals("")) {
                sql = "select object(o) from Bank as o where o.enable = true and o.status = true order by o.name";
            } else {
                sql = "select object(o) from Bank as o where o.enable = true and o.status = true and id in (" + ids + ") order by o.name";
            }
            Query q = em.createQuery(sql);
            list = q.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    
    public int checkBankCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from Bank as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from Bank as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public int checkBankName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from Bank as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from Bank as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
