/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoChannel;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Transactional
public class SoChannelDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoChannel soChannel) throws PreexistingEntityException {
        this.validate(soChannel);
        em.persist(soChannel);
    }

    public void edit(SoChannel soChannel) throws NonexistentEntityException, Exception {
        soChannel = em.merge(soChannel);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoChannel soChannel;
        try {
            soChannel = em.getReference(SoChannel.class, id);
            soChannel.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soChannel with id " + id + " no longer exists.", enfe);
        }
        em.remove(soChannel);
    }

    public List<SoChannel> findSoChannelEntities() {
        return findSoChannelEntities(true, -1, -1);
    }

    public List<SoChannel> findSoChannelEntities(int maxResults, int firstResult) {
        return findSoChannelEntities(false, maxResults, firstResult);
    }

    private List<SoChannel> findSoChannelEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoChannel as o where o.enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<SoChannel> findSoChannelStatus() {
        Query q = em.createQuery("select object(o) from SoChannel as o where o.enable = true and o.status = true order by o.seqNo");
        return q.getResultList();
    }

    public SoChannel findSoChannel(Integer id) {
        return em.find(SoChannel.class, id);
    }

    public int getSoChannelCount() {
        Query q = em.createQuery("select count(o) from SoChannel as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<SoChannel> findSoChannelByName(String name) {
        Query q = em.createQuery("select object(o) from SoChannel as o where name = ?1");
        q.setParameter(1, name);
        return q.getResultList();
    }

    private void validate(SoChannel soChannel) throws PreexistingEntityException {
        if (findSoChannelByName(soChannel.getName()).size() > 0) {
            throw new PreexistingEntityException("Duplicate Code");
        }
    }

    public Map<String, Integer> getSoChannelList() {
        List<SoChannel> soChannels = this.findSoChannelStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoChannel obj : soChannels) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public int checkSoChannelName(String name, Integer id) {
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from SoChannel as o where o.name =:name and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from SoChannel as o where o.name =:name and o.enable = true and o.id <> :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<SoChannel> findByUser(Integer userId) {
        String sql = "select a.so_channel_id from so_account a"
                + " inner join so_service_account b on a.id = b.so_account_id"
                + " inner join so_service_user c on c.so_service_id = b.so_service_id"
                + " where c.user_id = ?1"
                + " group by a.so_channel_id"
                + " order by a.so_channel_id";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, userId);
        List<Integer> list = q.getResultList();
        List<SoChannel> soChannelList = new ArrayList<SoChannel>();
        for(Integer i : list){
            soChannelList.add(findSoChannel(i));
        }
        return soChannelList;
    }
        
}
