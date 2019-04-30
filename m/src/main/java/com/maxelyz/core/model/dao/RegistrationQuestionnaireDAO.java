/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.RegistrationQuestionnaire;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RegistrationQuestionnaireDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(RegistrationQuestionnaire registrationQuestionnaire) throws PreexistingEntityException, Exception {
        em.persist(registrationQuestionnaire);
    }

    public void edit(RegistrationQuestionnaire registrationQuestionnaire) throws NonexistentEntityException, Exception {
        registrationQuestionnaire = em.merge(registrationQuestionnaire);
    }

    public List<RegistrationQuestionnaire> findRegistrationQuestionnaireEntities() {
        return findRegistrationQuestionnaireEntities(true, -1, -1);
    }

    public List<RegistrationQuestionnaire> findRegistrationQuestionnaireEntities(int maxResults, int firstResult) {
        return findRegistrationQuestionnaireEntities(false, maxResults, firstResult);
    }

    private List<RegistrationQuestionnaire> findRegistrationQuestionnaireEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from RegistrationQuestionnaire as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public RegistrationQuestionnaire findRegistrationQuestionnaire(Integer id) {
        return em.find(RegistrationQuestionnaire.class, id);
    }

    public int getRegistrationQuestionnaireCount() {
        return ((Long) em.createQuery("select count(o) from RegistrationQuestionnaire as o").getSingleResult()).intValue();
    }
}
