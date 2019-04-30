/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.CampaignChannel;
import com.maxelyz.core.model.entity.Marketing;
import com.maxelyz.core.model.entity.MarketingAuto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author support
 */
@Transactional
public class MarketingAutoDAO {

    @PersistenceContext
    private EntityManager em;
    private static Logger log = Logger.getLogger(MarketingAutoDAO.class);

    public MarketingAuto findMarketingAutoId(Integer id) {
        return em.find(MarketingAuto.class, id);
    }

    public List<MarketingAuto> findMarketingAutoEntities() {
        return findMarketingAutoEntities(true, -1, -1);
    }

    public List<MarketingAuto> findMarketingAutoEntities(int maxResults, int firstResult) {
        return findMarketingAutoEntities(false, maxResults, firstResult);
    }

    private List<MarketingAuto> findMarketingAutoEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(m) from MarketingAuto as m where m.enable = true order by m.prefixCode");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public int checkMarketingAutoPrefixCode(String prefixCode, Integer id) {
        Query q;
        if (id == 0) {
            q = em.createQuery("select count(m) from MarketingAuto as m where m.prefixCode =:prefixCode and m.enable = true");
        } else {
            q = em.createQuery("select count(m) from MarketingAuto as m where m.prefixCode =:prefixCode and m.enable = true and m.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("prefixCode", prefixCode);
        return ((Long) q.getSingleResult()).intValue();
    }

    public void create(MarketingAuto marketingAuto) throws PreexistingEntityException, Exception {
        em.persist(marketingAuto);
    }

    public void edit(MarketingAuto marketingAuto) throws NonexistentEntityException, Exception {
        marketingAuto = em.merge(marketingAuto);
    }

}
