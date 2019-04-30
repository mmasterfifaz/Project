/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.DocumentFolder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DocumentFolderDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(DocumentFolder documentFolder) throws PreexistingEntityException, Exception {
        em.persist(documentFolder);
    }

    public void edit(DocumentFolder documentFolder) throws NonexistentEntityException, Exception {
        documentFolder = em.merge(documentFolder);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        DocumentFolder documentFolder;
        try {
            documentFolder = em.getReference(DocumentFolder.class, id);
            documentFolder.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The documentFolder with id " + id + " no longer exists.", enfe);
        }
        em.remove(documentFolder);
    }

    public List<DocumentFolder> findDocumentFolderEntities() {
        return findDocumentFolderEntities(true, -1, -1);
    }

    public List<DocumentFolder> findDocumentFolderEntities(int maxResults, int firstResult) {
        return findDocumentFolderEntities(false, maxResults, firstResult);
    }

    private List<DocumentFolder> findDocumentFolderEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from DocumentFolder as o ");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

//    public List<DocumentFolder> findDocumentFolderByName(String nameKeyword) {
//        String sql = "select object(o) from DocumentFolder as o ";
//        if(!nameKeyword.isEmpty()){
//            sql += " and o.name like :nameKeyword";
//        }
//        Query q = em.createQuery(sql);
//        if(!nameKeyword.isEmpty()){
//            q.setParameter("nameKeyword", "%" + nameKeyword + "%");
//        }
//        return q.getResultList();
//    }

    public DocumentFolder findDocumentFolder(Integer id) {
        return em.find(DocumentFolder.class, id);
    }

    public int getDocumentFolderCount() {
        return ((Long) em.createQuery("select count(o) from DocumentFolder as o").getSingleResult()).intValue();
    }

    public Map<String, Integer> getDocumentFolderList() {
        List<DocumentFolder> documentFolder = this.findDocumentFolderEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (DocumentFolder obj : documentFolder) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    
//    public int checkProspectListName(String name, Integer id) { 
//        Query q;
//        if(id == 0){
//            q = em.createQuery("select count(o) from DocumentFolder as o where o.name =:name and o.enable = true");        
//        } else {
//            q = em.createQuery("select count(o) from DocumentFolder as o where o.name =:name and o.enable = true and o.id not like :id");
//            q.setParameter("id", id);
//        }
//        q.setParameter("name", name);
//        return ((Long) q.getSingleResult()).intValue();
//    }

}
