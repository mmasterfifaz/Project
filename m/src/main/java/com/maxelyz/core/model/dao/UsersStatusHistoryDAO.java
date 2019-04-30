package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.UsersStatusHistory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UsersStatusHistoryDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(UsersStatusHistory usersStatusHistory) throws PreexistingEntityException, Exception {
        em.persist(usersStatusHistory);
    }

    public void edit(UsersStatusHistory usersStatusHistory) throws NonexistentEntityException, Exception {
        usersStatusHistory = em.merge(usersStatusHistory);
    }

    public List<UsersStatusHistory> findUsersStatusHistoryEntities() {
        return findUsersStatusHistoryEntities(true, -1, -1);
    }

    public List<UsersStatusHistory> findUsersStatusHistoryEntities(int maxResults, int firstResult) {
        return findUsersStatusHistoryEntities(false, maxResults, firstResult);
    }

    private List<UsersStatusHistory> findUsersStatusHistoryEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from UsersStatusHistory as o ");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public UsersStatusHistory findUsersStatusHistory(Integer id) {
        return em.find(UsersStatusHistory.class, id);
    }

    public List<UsersStatusHistory> findUsersStatusHistoryByUserId(Integer id) {
        Query q = em.createQuery("select object(o) from UsersStatusHistory as o where o.users.id = ?1 order by o.actionDate");
        q.setParameter(1, id);
        return q.getResultList();
    }

    public List<UsersStatusHistory> findLastedStatus(Integer id) {
        Query q = em.createQuery("select object(o) from UsersStatusHistory as o where o.users.id = ?1 order by id desc");
        q.setParameter(1, id);
        q.setMaxResults(1);
        return q.getResultList();
    }
}
