/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.QaCategory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.*;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class QaCategoryDAO  {

    @PersistenceContext
    private EntityManager em;


    public void create(QaCategory qaCategory) throws PreexistingEntityException, Exception {
        em.persist(qaCategory);     
    }
    
    public void edit(QaCategory qaCategory) throws IllegalOrphanException, NonexistentEntityException, Exception {
       qaCategory = em.merge(qaCategory);       
    }
        
    public void editQa(QaCategory qaCategory) throws IllegalOrphanException, NonexistentEntityException, Exception {
       Query q = em.createQuery("Delete from QaQuestion qq where qq.qaCategory = ?1");
       q.setParameter(1, qaCategory);
       q.executeUpdate();

       qaCategory = em.merge(qaCategory);       
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        QaCategory qaCategory;
        try {
            qaCategory = em.getReference(QaCategory.class, id);
            qaCategory.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The qaCategory with id " + id + " no longer exists.", enfe);
        }
        List<String> illegalOrphanMessages = null;
        em.remove(qaCategory);        
    }

    public List<QaCategory> findQaCategoryEntities() {
        return findQaCategoryEntities(true, -1, -1);
    }

    public List<QaCategory> findQaCategoryEntities(int maxResults, int firstResult) {
        return findQaCategoryEntities(false, maxResults, firstResult);
    }

    private List<QaCategory> findQaCategoryEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from QaCategory as o where o.enable=true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public QaCategory findQaCategory(Integer id) {
        return em.find(QaCategory.class, id);

    }

    public int getQaCategoryCount() {
        Query q = em.createQuery("select count(o) from QaCategory as o");
        return ((Long) q.getSingleResult()).intValue();
    }
   
    public Map<String, Integer> getQuestionnaireList() {
        List<QaCategory> qaCategory = this.findQaCategoryEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (QaCategory obj : qaCategory) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
     public static Map<String, String> getTypeList() {
        Map<String, String> values = new LinkedHashMap<String, String>();
        values.put("Dropdown", "dropdown");
        values.put("CheckBox", "checkbox");
        values.put("Radio", "radio");
        values.put("Number", "textfield");
        return values;
    }
     
     public static Map<String, Integer> getItemList() {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for(int i=0;i<=20;i++) {
            values.put(""+i+"" ,i);
        }
        return values;
    }
          
    public static String getDisplayType(String key) {
        String result;
        if ("dropdown".equals(key)) {
            result = "Dropdown";
        } else if ("checkbox".equals(key)) {
            result = "CheckBox";
        } else if ("radio".equals(key)) {
            result = "Radio";
        } else if ("textfield".equals(key)) {
            result = "Number";
        } else {
            result = "";
        }
        return result;
    } 
    
    public List<QaCategory> checkQaCategory(QaCategory qaCategory) {
        Query q = em.createQuery("select object(o) from QaTransDetail as o where o.qaCategory = ?1 ");
        
        q.setParameter(1, qaCategory);
        return q.getResultList();       
    }
        
    public int checkQaCategoryName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from QaCategory as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from QaCategory as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }

    public int countQaCategoryNameLike(String name, Integer length) {
        Query q;
        q = em.createQuery("select count(o) from QaCategory as o where o.name like CONCAT('',:name,'%') and o.enable = true and LENGTH(o.name) <= :length");
        q.setParameter("name", name);
        q.setParameter("length", length);
        return ((Long) q.getSingleResult()).intValue();
    }

    public String getQaCategoryNameForCheckCopy(String name,Integer length) {
        Query q;
        if(length==0) {
            q = em.createQuery("select o.name from QaCategory as o where o.id in (select max(o.id) from QaCategory as o where o.name like CONCAT('',:name,'%') and o.enable = true and LENGTH(o.name) >= :length)");
        }else{
            q = em.createQuery("select o.name from QaCategory as o where o.id in (select max(o.id) from QaCategory as o where o.name like CONCAT('',:name,'%') and o.enable = true and LENGTH(o.name) <= :length)");
        }
            q.setParameter("name", name);
            q.setParameter("length", length);
        return  q.getSingleResult().toString();
    }
}
