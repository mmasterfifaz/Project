/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.VoiceFilePassword;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 *
 * @author ukritj
 */
@Transactional
public class VoiceFilePasswordDAO {

    @PersistenceContext
    protected EntityManager em;


    public void create(VoiceFilePassword voiceFilePassword) {
        em.persist(voiceFilePassword);
    }

    public void edit(VoiceFilePassword voiceFilePassword) throws IllegalOrphanException, NonexistentEntityException, Exception {
        voiceFilePassword = em.merge(voiceFilePassword);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        VoiceFilePassword voiceFilePassword;
        try {
            voiceFilePassword = em.getReference(VoiceFilePassword.class, id);
            voiceFilePassword.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The voiceFilePassword with id " + id + " no longer exists.", enfe);
        }
        em.remove(voiceFilePassword);
    }

    public List<VoiceFilePassword> findVoiceFilePasswordEntities() {
        return findVoiceFilePasswordEntities(true, -1, -1);
    }

    public List<VoiceFilePassword> findVoiceFilePasswordEntities(int maxResults, int firstResult) {
        return findVoiceFilePasswordEntities(false, maxResults, firstResult);
    }

    private List<VoiceFilePassword> findVoiceFilePasswordEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from VoiceFilePassword as o where o.enable = true and o.status = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public VoiceFilePassword findVoiceFilePassword(Integer id) throws Exception {
        return em.find(VoiceFilePassword.class, id);

    }

    public int getVoiceFilePasswordCount() {
        Query q = em.createQuery("select count(o) from VoiceFilePassword as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<VoiceFilePassword> findByFilter(String txtSearch, String status, Integer desc){
        List<VoiceFilePassword> list = null;
        try{
            String sql = "select object(o) from VoiceFilePassword as o where o.enable = true";
            if(txtSearch != null && !txtSearch.equals("")){
                sql += " and (UPPER(o.password) like :txtSearch)";
            }
            if(status != null && !status.equals("")){
                if(status.equals("0")){
                    sql += " and (o.status = false or o.status is null)";
                }else if(status.equals("1")) {
                    sql += " and o.status = true";
                }
            }
            sql += " order by o.effectiveDate" + (desc==1?" desc":"");

            Query q = em.createQuery(sql);
            if(txtSearch != null && !txtSearch.equals("")){
                q.setParameter("txtSearch", "%" + txtSearch.toUpperCase() + "%");
            }

            list = q.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public String findActivePassword()
    {
        Query q = em.createNativeQuery("select TOP 1 password FROM dbo.voicefile_password \n" +
                "WHERE status = 1 AND enable = 1\n" +
                "AND effective_date < GETDATE()\n" +
                "ORDER BY effective_date desc");

        return ((String)q.getSingleResult());
    }

    public List<String> returnStringList(String sql) {
        Query q = em.createNativeQuery(sql);
        return q.getResultList();
    }
}
