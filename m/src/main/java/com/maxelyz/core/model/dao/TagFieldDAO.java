/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.TagField;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrator
 */
@Transactional
public class TagFieldDAO {
    
    @PersistenceContext
    private EntityManager em;


    public void create(TagField tagfield) {
        em.persist(tagfield);
    }

    public void edit(TagField tagfield) {
         tagfield = em.merge(tagfield);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        TagField tagfield;
        try {
            tagfield = em.getReference(TagField.class, id);
            tagfield.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The TagField with id " + id + " no longer exists.", enfe);
        }
        em.remove(tagfield);
    }

    public List<TagField> findTagFieldEntities() {
        return findTagFieldEntities(true, -1, -1);
    }

    public List<TagField> findTagFieldEntities(int maxResults, int firstResult) {
        return findTagFieldEntities(false, maxResults, firstResult);
    }

    private List<TagField> findTagFieldEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from TagField as o order by o.id");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public TagField findTagField(Integer id) {
        return em.find(TagField.class, id);
    }

    public int getTagFieldCount() {
        return ((Long) em.createQuery("select count(o) from TagField as o").getSingleResult()).intValue(); 
    }

    public List<TagField> findTagFieldByTagType(String tagType) {
        Query q = em.createQuery("select object(o) from TagField as o where o.tagType = ?1 order by o.id");
        q.setParameter(1, tagType); 
        return q.getResultList();
    }
}
