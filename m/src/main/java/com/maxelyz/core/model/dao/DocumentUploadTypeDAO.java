/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.DocumentUploadType;
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
public class DocumentUploadTypeDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(DocumentUploadType documentUploadType) throws PreexistingEntityException {
//        this.validate(documentUploadType);
        em.persist(documentUploadType);
    }

    public void edit(DocumentUploadType documentUploadType) throws NonexistentEntityException, Exception {
        documentUploadType = em.merge(documentUploadType);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        DocumentUploadType documentUploadType;
        try {
            documentUploadType = em.getReference(DocumentUploadType.class, id);
            documentUploadType.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The documentUploadType with id " + id + " no longer exists.", enfe);
        }
        em.remove(documentUploadType);
    }

    public List<DocumentUploadType> findDocumentUploadTypeEntities() {
        return findDocumentUploadTypeEntities(true, -1, -1);
    }

    public List<DocumentUploadType> findDocumentUploadTypeEntities(int maxResults, int firstResult) {
        return findDocumentUploadTypeEntities(false, maxResults, firstResult);
    }

    private List<DocumentUploadType> findDocumentUploadTypeEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from DocumentUploadType as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }


    public DocumentUploadType findDocumentUploadType(Integer id) {
        return em.find(DocumentUploadType.class, id);
    }

    public int getDocumentUploadTypeCount() {
        Query q = em.createQuery("select count(o) from DocumentUploadType as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<DocumentUploadType> findDocumentUploadTypeByCode(String code) {
        Query q = em.createQuery("select object(o) from DocumentUploadType as o where code = ?1");
        q.setParameter(1, code);
        return q.getResultList();
    }

    private void validate(DocumentUploadType documentUploadType) throws PreexistingEntityException {
        if (findDocumentUploadTypeByCode(documentUploadType.getCode()).size() > 0) {
            throw new PreexistingEntityException("Duplicate Code");
        }
    }

    public Map<String, Integer> getDocumentUploadTypeList() {
        List<DocumentUploadType> documentUploadTypes = this.findDocumentUploadTypeEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (DocumentUploadType obj : documentUploadTypes) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public int checkDocumentUploadTypeCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from DocumentUploadType as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from DocumentUploadType as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
        
}
