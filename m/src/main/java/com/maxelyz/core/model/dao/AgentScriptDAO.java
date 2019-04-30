/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.AgentScript;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AgentScriptDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(AgentScript agentScript) {
        em.persist(agentScript);
    }

    public void edit(AgentScript agentScript) {
        agentScript = em.merge(agentScript);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        AgentScript agentScript;
        try {
            agentScript = em.getReference(AgentScript.class, id);
            agentScript.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The agentScript with id " + id + " no longer exists.", enfe);
        }
        em.remove(agentScript);
    }

    public List<AgentScript> findAgentScriptEntities() {
        return findAgentScriptEntities(true, -1, -1);
    }

    public List<AgentScript> findAgentScriptEntities(int maxResults, int firstResult) {
        return findAgentScriptEntities(false, maxResults, firstResult);
    }

    private List<AgentScript> findAgentScriptEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from AgentScript as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public AgentScript findAgentScript(Integer id) {
        return em.find(AgentScript.class, id);
    }

    public int getAgentScriptCount() {
        Query q = em.createQuery("select count(o) from AgentScript as o");
        return ((Long) q.getSingleResult()).intValue();
    }
}
