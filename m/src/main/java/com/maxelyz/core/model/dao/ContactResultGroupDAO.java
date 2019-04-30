/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ContactResult;
import com.maxelyz.core.model.entity.ContactResultGroup;
import com.maxelyz.utils.JSFUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ContactResultGroupDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(ContactResultGroup contactResultGroup) throws PreexistingEntityException, Exception {
        em.persist(contactResultGroup);
    }

    public void edit(ContactResultGroup contactResultGroup) throws NonexistentEntityException, Exception {
        contactResultGroup = em.merge(contactResultGroup);
    }

    public List<ContactResultGroup> findContactResultGroupEntities() {
        return findContactResultGroupEntities(true, -1, -1);
    }

    public List<ContactResultGroup> findContactResultGroupEntities(int maxResults, int firstResult) {
        return findContactResultGroupEntities(false, maxResults, firstResult);
    }

    private List<ContactResultGroup> findContactResultGroupEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ContactResultGroup as o where o.enable=true order by o.seqNo");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public ContactResultGroup findContactResultGroup(Integer id) {
        return em.find(ContactResultGroup.class, id);
    }

    public int getContactResultGroupCount() {
        Query q = em.createQuery("select count(o) from ContactResultGroup as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public int checkDuplication(String code, String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from ContactResultGroup as o where o.code =:code and o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from ContactResultGroup as o where o.code =:code and o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public Map<String, Integer> getContactResultGroupList() {
        List<ContactResultGroup> contactResultGroups = this.findContactResultGroupEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (ContactResultGroup obj : contactResultGroups) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
}
