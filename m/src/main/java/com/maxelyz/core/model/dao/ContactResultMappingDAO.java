package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.entity.ContactResultMapping;
import com.maxelyz.social.model.dao.exceptions.NonexistentEntityException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by support on 12/20/2017.
 */
@Transactional
public class ContactResultMappingDAO {
    @PersistenceContext
    private EntityManager em;

    public void create(ContactResultMapping contactResultMapping) throws NonexistentEntityException, Exception {
        em.persist(contactResultMapping);
    }

    public void edit(ContactResultMapping contactResultMapping) throws NonexistentEntityException, Exception {
        contactResultMapping = em.merge(contactResultMapping);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        ContactResultMapping contactResultMapping;
        try {
            contactResultMapping = em.getReference(ContactResultMapping.class, id);
            contactResultMapping.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The contactResultMapping with id " + id + " no longer exists.", enfe);
        }
        em.remove(contactResultMapping);
    }

    public List<ContactResultMapping> findContactResultMappingEntities() {
        return findContactResultMappingEntities(true, -1, -1);
    }

    public List<ContactResultMapping> findContactResultMappingEntities(int maxResults, int firstResult) {
        return findContactResultMappingEntities(false, maxResults, firstResult);
    }

    private List<ContactResultMapping> findContactResultMappingEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ContactResultMapping as o where enable = true order by code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public ContactResultMapping findContactResultMapping(Integer id) {
        return em.find(ContactResultMapping.class, id);
    }

    public int checkContactResultMappingCode(String code, Integer id) {
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from ContactResultMapping as o where o.code =:code and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from ContactResultMapping as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
}
