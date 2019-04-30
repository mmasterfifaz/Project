/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.QaForm;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.QaSelectedCategory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.*;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class QaFormDAO  {

    @PersistenceContext
    private EntityManager em;


    public void create(QaForm qaForm) throws PreexistingEntityException, Exception {
        em.persist(qaForm);  
    }

    public void edit(QaForm qaForm) throws IllegalOrphanException, NonexistentEntityException, Exception {
       qaForm = em.merge(qaForm);
           
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
            QaForm qaForm;
            try {
                qaForm = em.getReference(QaForm.class, id);
                qaForm.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The qaForm with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<QaSelectedCategory> qaSelectedCategoryCollectionOrphanCheck = qaForm.getQaSelectedCategoryCollection();
            for (QaSelectedCategory qaSelectedCategoryCollectionOrphanCheckQaSelectedCategory : qaSelectedCategoryCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This QaForm (" + qaForm + ") cannot be destroyed since the QaSelectedCategory " + qaSelectedCategoryCollectionOrphanCheckQaSelectedCategory + " in its qaSelectedCategoryCollection field has a non-nullable qaForm field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(qaForm);
            
    }

    public List<QaForm> findQaFormEntities() {
        return findQaFormEntities(true, -1, -1);
    }

    public List<QaForm> findQaFormEntities(int maxResults, int firstResult) {
        return findQaFormEntities(false, maxResults, firstResult);
    }

    private List<QaForm> findQaFormEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from QaForm as o where o.enable=true order by o.name ");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
      
    }

    public QaForm findQaForm(Integer id) {
        return em.find(QaForm.class, id);
    }

    public int getQaFormCount() {
        Query q = em.createQuery("select count(o) from QaForm as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public void editQaForm(QaForm qaForm) throws IllegalOrphanException, NonexistentEntityException, Exception {
       Query q = em.createQuery("Delete from QaSelectedCategory where qaForm = ?1");
       q.setParameter(1, qaForm);
       q.executeUpdate();

      qaForm = em.merge(qaForm);       
    }
   
    public Map<String, Integer> getQaFormList() {
        List<QaForm> qaForm = this.findQaFormEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (QaForm obj : qaForm) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public List<QaForm> checkQaForm(QaForm qaForm) {
        Query q = em.createQuery("select object(o) from QaTrans as o where o.qaFormId = ?1 ");
        
        q.setParameter(1, qaForm.getId());
        return q.getResultList();       
    }
    
    public int checkQaFormName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from QaForm as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from QaForm as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
}
