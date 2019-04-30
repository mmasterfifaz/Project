/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.DocumentType;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class DocumentTypeDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(DocumentType documentType) {
        em.persist(documentType);         
    }

    public void edit(DocumentType documentType) throws NonexistentEntityException, Exception {
       documentType = em.merge(documentType);
            
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        DocumentType documentType;
        try {
            documentType = em.getReference(DocumentType.class, id);
            documentType.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The documentType with id " + id + " no longer exists.", enfe);
        }
        em.remove(documentType);
          
    }

    public List<DocumentType> findDocumentTypeEntities() {
        return findDocumentTypeEntities(true, -1, -1);
    }

    public List<DocumentType> findDocumentTypeEntities(int maxResults, int firstResult) {
        return findDocumentTypeEntities(false, maxResults, firstResult);
    }

    private List<DocumentType> findDocumentTypeEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from DocumentType as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public DocumentType findDocumentType(Integer id) {
        return em.find(DocumentType.class, id);
    }

    public int getDocumentTypeCount() {
        Query q = em.createQuery("select count(o) from DocumentType as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
