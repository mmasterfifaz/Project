package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.UsersStatus;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UsersStatusDAO {
    @PersistenceContext
    private EntityManager em;
    
    public void create(UsersStatus usersStatus) throws PreexistingEntityException, Exception {
        em.persist(usersStatus);
    }

    public void edit(UsersStatus usersStatus) throws NonexistentEntityException, Exception {
        usersStatus = em.merge(usersStatus);
    }

    public List<UsersStatus> findUsersStatusEntities() {
        return findUsersStatusEntities(true, -1, -1);
    }

    public List<UsersStatus> findUsersStatusEntities(int maxResults, int firstResult) {
        return findUsersStatusEntities(false, maxResults, firstResult);
    }

    private List<UsersStatus> findUsersStatusEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from UsersStatus as o where o.enable=true ");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
    
    public UsersStatus findUsersStatus(Integer id) {
        return em.find(UsersStatus.class, id);
    }
}
