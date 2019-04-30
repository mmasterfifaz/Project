/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Questionnaire;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.RegistrationForm;
import java.util.ArrayList;
import java.util.Collection;
import com.maxelyz.core.model.entity.QuestionnaireDetail;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class QuestionnaireDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Questionnaire questionnaire) {
        em.persist(questionnaire);

    }

    public void edit(Questionnaire questionnaire) throws IllegalOrphanException, NonexistentEntityException, Exception {
       //delete questionnairedetail
        Query q = em.createQuery("Delete from QuestionnaireDetail reg where reg.questionnaire = ?1");
        q.setParameter(1, questionnaire);
        q.executeUpdate();

        questionnaire = em.merge(questionnaire);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {

        Questionnaire questionnaire;
        try {
            questionnaire = em.getReference(Questionnaire.class, id);
            questionnaire.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The questionnaire with id " + id + " no longer exists.", enfe);
        }

        em.remove(questionnaire);


    }

    public List<Questionnaire> findQuestionnaireEntities() {
        return findQuestionnaireEntities(true, -1, -1);
    }

    public List<Questionnaire> findQuestionnaireEntities(int maxResults, int firstResult) {
        return findQuestionnaireEntities(false, maxResults, firstResult);
    }

    private List<Questionnaire> findQuestionnaireEntities(boolean all, int maxResults, int firstResult) {

        Query q = em.createQuery("select object(o) from Questionnaire as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();

    }

    public Questionnaire findQuestionnaire(Integer id) {
        return em.find(Questionnaire.class, id);
    }

    public int getQuestionnaireCount() {
        Query q = em.createQuery("select count(o) from Questionnaire as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public Map<String, Integer> getQuestionnaireList() {
        List<Questionnaire> questionnaires = this.findQuestionnaireEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Questionnaire obj : questionnaires) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
     public static Map<String, String> getTypeList() {
        Map<String, String> values = new LinkedHashMap<String, String>();
        values.put("Fill-in", "open");
        values.put("True/False", "multiple2");
        values.put("3 Multiple Choices", "multiple3");
        values.put("4 Multiple Choices", "multiple4");
        values.put("5 Multiple Choices", "multiple5");
        return values;
    }
     
    public static String getDisplayType(String key) {
        String result;
        if ("open".equals(key)) {
            result = "Fill-in";
        } else if ("multiple2".equals(key)) {
            result = "True/False";
        } else if ("multiple3".equals(key)) {
            result = "3 Multiple Choices";
        } else if ("multiple4".equals(key)) {
            result = "4 Multiple Choices";
        } else if ("multiple5".equals(key)) {
            result = "5 Multiple Choices";
        } else {
            result = "";
        }
        return result;
    } 
}
