/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.MessageTemplate;
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
public class MessageTemplateDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(MessageTemplate messageTemplate) throws PreexistingEntityException {
//        this.validate(messageTemplate);
        em.persist(messageTemplate);
    }

    public void edit(MessageTemplate messageTemplate) throws NonexistentEntityException, Exception {
        messageTemplate = em.merge(messageTemplate);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        MessageTemplate messageTemplate;
        try {
            messageTemplate = em.getReference(MessageTemplate.class, id);
            messageTemplate.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The messageTemplate with id " + id + " no longer exists.", enfe);
        }
        em.remove(messageTemplate);
    }

    public List<MessageTemplate> findMessageTemplateEntities() {
        return findMessageTemplateEntities(true, -1, -1);
    }

    public List<MessageTemplate> findMessageTemplateEntities(int maxResults, int firstResult) {
        return findMessageTemplateEntities(false, maxResults, firstResult);
    }

    private List<MessageTemplate> findMessageTemplateEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from MessageTemplate as o where o.enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<MessageTemplate> findMessageTemplateStatus() {
        Query q = em.createQuery("select object(o) from MessageTemplate as o where o.enable = true and o.status = true order by o.name");
        return q.getResultList();
    }

    public MessageTemplate findMessageTemplate(Integer id) {
        return em.find(MessageTemplate.class, id);
    }

    public int getMessageTemplateCount() {
        Query q = em.createQuery("select count(o) from MessageTemplate as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<MessageTemplate> findMessageTemplateByName(String name) {
        Query q = em.createQuery("select object(o) from MessageTemplate as o where name like ?1");
        q.setParameter(1, "%" + name + "%");
        return q.getResultList();
    }

    public List<MessageTemplate> findMessageTemplateByMessage(String name) {
        Query q = em.createQuery("select object(o) from MessageTemplate as o where message like ?1");
        q.setParameter(1, "%" + name + "%");
        return q.getResultList();
    }

//    public List<MessageTemplate> findMessageTemplateModal(String name, MessageTemplateCategory cat) {
//        Query q = null;
//        if (cat==null) {
//            q = em.createQuery("select object(o) from MessageTemplate as o where message like ?1 order by o.name");
//            q.setParameter(1, "%" + name + "%");
//        } else {
//            q = em.createQuery("select object(o) from MessageTemplate as o where message like ?1 and messageTemplateCategory = ?2 order by o.name");
//            q.setParameter(1, "%" + name + "%");
//            q.setParameter(2, cat);
//        }
//        return q.getResultList();
//    }
    
    public List<MessageTemplate> findMessageTemplateModal(Integer categoryId, String keyword) {
        String sql = "select object(o) from MessageTemplate as o "
                + "where o.enable = true and o.status = true ";
        
        if(categoryId != null && categoryId != 0) {
            sql += "and o.messageTemplateCategory.id = "+categoryId+" ";
        }
        if(!keyword.isEmpty()) {
            sql += "and (o.name like '%"+keyword+"%' or o.message like '%"+keyword+"%') ";
        }
        
        sql += "order by o.name ";
        Query q = em.createQuery(sql);
        return q.getResultList();
    }

    private void validate(MessageTemplate messageTemplate) throws PreexistingEntityException {
        if (findMessageTemplateByName(messageTemplate.getName()).size() > 0) {
            throw new PreexistingEntityException("Duplicate Code");
        }
    }

    public Map<String, Integer> getMessageTemplateList() {
        List<MessageTemplate> messageTemplates = this.findMessageTemplateStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (MessageTemplate obj : messageTemplates) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public int checkMessageTemplateName(String name, Integer id) {
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from MessageTemplate as o where o.name =:name and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from MessageTemplate as o where o.name =:name and o.enable = true and o.id <> :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<MessageTemplate> findMessageTemplateByCategory(MessageTemplateCategory messageTemplateCategory) {
        Query q = null;
        if (messageTemplateCategory == null) {
            q = em.createQuery("select object(o) from MessageTemplate as o order by o.name");
        } else {
            q = em.createQuery("select object(o) from MessageTemplate as o where messageTemplateCategory = ?1 order by o.name");
            q.setParameter(1, messageTemplateCategory);
        }

        return q.getResultList();
    }
    
    public List<MessageTemplate> findMessageTemplateByCategoryId(Integer templateCategoryId) {
        Query q = em.createQuery("select object(o) from MessageTemplate as o where o.messageTemplateCategory.id = ?1 and o.enable = true order by o.name");
        q.setParameter(1, templateCategoryId);
        return q.getResultList();
    }
}
