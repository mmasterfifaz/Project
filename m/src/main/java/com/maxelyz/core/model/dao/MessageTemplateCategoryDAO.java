/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.MessageTemplateCategory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Transactional
public class MessageTemplateCategoryDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(MessageTemplateCategory messageTemplateCategory) throws PreexistingEntityException {
//        this.validate(messageTemplateCategory);
        em.persist(messageTemplateCategory);
    }

    public void edit(MessageTemplateCategory messageTemplateCategory) throws NonexistentEntityException, Exception {
        messageTemplateCategory = em.merge(messageTemplateCategory);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        MessageTemplateCategory messageTemplateCategory;
        try {
            messageTemplateCategory = em.getReference(MessageTemplateCategory.class, id);
            messageTemplateCategory.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The messageTemplateCategory with id " + id + " no longer exists.", enfe);
        }
        em.remove(messageTemplateCategory);
    }

    public List<MessageTemplateCategory> findMessageTemplateCategoryEntities() {
        return findMessageTemplateCategoryEntities(true, -1, -1);
    }

    public List<MessageTemplateCategory> findMessageTemplateCategoryEntities(int maxResults, int firstResult) {
        return findMessageTemplateCategoryEntities(false, maxResults, firstResult);
    }

    private List<MessageTemplateCategory> findMessageTemplateCategoryEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from MessageTemplateCategory as o where o.enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<MessageTemplateCategory> findMessageTemplateCategoryStatus() {
        Query q = em.createQuery("select object(o) from MessageTemplateCategory as o where o.enable = true and o.status = true order by o.name");
        return q.getResultList();
    }

    public MessageTemplateCategory findMessageTemplateCategory(Integer id) {
        return em.find(MessageTemplateCategory.class, id);
    }

    public int getMessageTemplateCategoryCount() {
        Query q = em.createQuery("select count(o) from MessageTemplateCategory as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<MessageTemplateCategory> findMessageTemplateCategoryByName(String name) {
        Query q = em.createQuery("select object(o) from MessageTemplateCategory as o where name = ?1");
        q.setParameter(1, name);
        return q.getResultList();
    }

    private void validate(MessageTemplateCategory messageTemplateCategory) throws PreexistingEntityException {
        if (findMessageTemplateCategoryByName(messageTemplateCategory.getName()).size() > 0) {
            throw new PreexistingEntityException("Duplicate Code");
        }
    }

    public Map<String, Integer> getMessageTemplateCategoryList() {
        List<MessageTemplateCategory> messageTemplateCategorys = this.findMessageTemplateCategoryStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (MessageTemplateCategory obj : messageTemplateCategorys) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public int checkMessageTemplateCategoryName(String name, Integer id) {
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from MessageTemplateCategory as o where o.name =:name and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from MessageTemplateCategory as o where o.name =:name and o.enable = true and o.id <> :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
        
}
