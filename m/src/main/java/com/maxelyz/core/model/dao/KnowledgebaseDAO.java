/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.utils.JSFUtil;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.NoResultException;
import org.springframework.transaction.annotation.Transactional;
import sun.text.resources.CollationData;

@Transactional
public class KnowledgebaseDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Knowledgebase knowledgebase) {
        em.persist(knowledgebase);
    }

    public void createLocation(KnowledgebaseLocation knowledgebaseLocation) {
        em.persist(knowledgebaseLocation);
    }
    //// anuwat
    public void createAttFile(KnowledgebaseAttfile knowledgebaseAttfile) {
        em.persist(knowledgebaseAttfile);
    }
    
    public void createUrl(KnowledgebaseUrl knowledgebaseUrl) {
        em.persist(knowledgebaseUrl);
    }
    
    public void createDivision(KnowledgebaseDivision knowledgebaseDivision) {
        em.persist(knowledgebaseDivision);
    }
    public void editDivision(KnowledgebaseDivision knowledgebaseDivision) throws NonexistentEntityException, Exception {
        knowledgebaseDivision = em.merge(knowledgebaseDivision);
    }
    public void destroyKnowledgebaseDivision(KnowledgebaseDivisionPK knowledgebaseDivisionPK) throws IllegalOrphanException, NonexistentEntityException {
        KnowledgebaseDivision knowledgebaseDivision;
        try {
            knowledgebaseDivision = em.getReference(KnowledgebaseDivision.class, knowledgebaseDivisionPK);
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The userGroupLocation with id " + knowledgebaseDivisionPK + " no longer exists.", enfe);
        }
        em.remove(knowledgebaseDivision);
    } 
    public void createRelated(KnowledgebaseRelated knowledgebaseRelated) {
        em.persist(knowledgebaseRelated);
    }
    public void createComment(KnowledgebaseBoard knowledgebaseBoard) {
        em.persist(knowledgebaseBoard);
    }
    public void createVote(KnowledgebaseVote knowledgebaseVote) {
        em.persist(knowledgebaseVote);
    }
    //// anuwat 
    
    
    
    public void edit(Knowledgebase knowledgebase) throws NonexistentEntityException, Exception {
        knowledgebase = em.merge(knowledgebase);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        Knowledgebase knowledgebase;
        try {
            knowledgebase = em.getReference(Knowledgebase.class, id);
            knowledgebase.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The knowledgebase with id " + id + " no longer exists.", enfe);
        }
        em.remove(knowledgebase);
    }

    public void destroyKnowledgebaseLocation(KnowledgebaseLocationPK knowledgebaseLocationPK) throws IllegalOrphanException, NonexistentEntityException {
        KnowledgebaseLocation knowledgebaseLocation;
        try {
            knowledgebaseLocation = em.getReference(KnowledgebaseLocation.class, knowledgebaseLocationPK);
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The knowledgebaseLocation with id " + knowledgebaseLocationPK + " no longer exists.", enfe);
        }
        em.remove(knowledgebaseLocation);
    }
      
    public List<Knowledgebase> findKnowledgebaseEntities() {
        return findKnowledgebaseEntities(true, -1, -1);
    }

    public List<Knowledgebase> findKnowledgebaseEntities(int maxResults, int firstResult) {
        return findKnowledgebaseEntities(false, maxResults, firstResult);
    }

    private List<Knowledgebase> findKnowledgebaseEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Knowledgebase as o where o.enable = true order by o.topic");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Knowledgebase findKnowledgebase(Integer id) {
        return em.find(Knowledgebase.class, id);
    }
    
    public Knowledgebase findApproveKnowledgebase(Integer id) {
        Query q = em.createQuery("select object(o) from Knowledgebase as o where o.approval = true and o.enable = true and o.id = ?1");
        q.setParameter(1, id);
        return (Knowledgebase) q.getSingleResult();
    }
        
    public List<KnowledgebaseLocation> findKnowledgebaseLocationByKnowledgebaseId(Integer knowledgebaseId) {
        Query q = em.createQuery("select object(o) from KnowledgebaseLocation as o where o.knowledgebaseLocationPK.knowledgebaseId = ?1 order by o.knowledgebaseLocationPK.locationId");
        q.setParameter(1, knowledgebaseId);

        return q.getResultList();
    }
            
    public int getKnowledgebaseCount() {
        return ((Long) em.createQuery("select count(o) from Knowledgebase as o").getSingleResult()).intValue();
    }

    public List<Knowledgebase> findTop() {
        Query q = em.createQuery("select object(o) from Knowledgebase as o where o.enable = true and o.knowledgebase = null order by o.topic");
        return q.getResultList();
    }

    public Map<String, Integer> getSocialKBList() {
        List<Knowledgebase> kbs = this.findTop();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Knowledgebase obj : kbs) {
            values.put(obj.getTopic(), obj.getId());
        }
        return values;
    }
    public List<Knowledgebase> findKnowledgebaseByName(Integer id,String nameKeyword) {
        //Query q = em.createQuery("select object(o) from Knowledgebase as o where o.enable = true and (o.topic like ?1 or o.description like ?1) order by o.topic");
        //Query q = em.createQuery("SELECT object(o) FROM Knowledgebase as o WHERE o.id = (SELECT DISTINCT k.knowledgebase.id FROM o.knowledgebaseLocationCollection k,  UserGroupLocation u WHERE k.location.id = u.location.id AND k.serviceType.id = u.serviceType.id AND u.userGroup.id = ?1) and o.enable = true and (o.schedule = false or (o.schedule = true and CONVERT (date, GETDATE())  between o.startdate and o.enddate )) and o.approval = true and (o.topic like ?2 or o.description like ?2) order by o.topic");
        //Query q = em.createQuery("SELECT object(o) FROM Knowledgebase as o WHERE o.id = (SELECT DISTINCT k.knowledgebase.id FROM o.knowledgebaseLocationCollection k,  UserGroupLocation u WHERE k.location.id = u.location.id AND k.serviceType.id =u.serviceType.id AND u.userGroup.id = ?1) and o.enable = true and (o.schedule = false or (o.schedule = true and CONVERT (date, GETDATE())  between o.startdate and o.enddate )) and o.approval = true and o.knowledgebase = null and (o.topic like ?2 or o.description like ?2) order by o.topic");
        Query q = em.createQuery("SELECT object(o) FROM Knowledgebase as o WHERE o.id = (SELECT DISTINCT k.knowledgebase.id FROM o.knowledgebaseLocationCollection k,  UserGroupLocation u WHERE k.location.id = u.location.id AND k.serviceType.id =u.serviceType.id AND u.userGroup.id = ?1) and o.enable = true and (o.schedule = false or (o.schedule = true and CONVERT (date, GETDATE())  between o.startdate and o.enddate )) and o.approval = true and (o.topic like ?2 or o.description like ?2) order by o.topic");
        q.setParameter(1, id);
        q.setParameter(2, "%"+nameKeyword+"%");
        return q.getResultList();
    }

    public void disableKnowledgebaseEntity(Integer id) throws Exception {
        Knowledgebase kb = findKnowledgebase(id);
        kb.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
        kb.setUpdateDate(new Date());
        kb.setEnable(false);
        this.edit(kb);

        Knowledgebase parentRefId = kb.getKnowledgebase();
        Query q = em.createQuery("update Knowledgebase set knowledgebase = ?1 where knowledgebase.id = ?2");
        q.setParameter(1, parentRefId);
        q.setParameter(2, id);
        q.executeUpdate();

    }
     
    public List<Integer> findServiceId(int knowIedgebaseId) {
        Query q = em.createQuery("select distinct o.serviceType.id from KnowledgebaseLocation as o where o.knowledgebase.id = ?1 order by o.serviceType.id");
        q.setParameter(1, knowIedgebaseId);
        return q.getResultList();
    }
    
    public List<Integer> findBusinessUnitId(int knowIedgebaseId, Integer serviceTypeId) {
        Query q = em.createQuery("select distinct o.businessUnit.id from KnowledgebaseLocation as o where o.knowledgebase.id = ?1 and o.serviceType.id = ?2 order by o.businessUnit.id");
        q.setParameter(1, knowIedgebaseId);
        q.setParameter(2, serviceTypeId);
        return q.getResultList();
    }
        
    public List<Integer> findLocationId(int knowIedgebaseId, Integer serviceTypeId, Integer businessUnitId) {
        Query q = em.createQuery("select distinct o.location.id from KnowledgebaseLocation as o where o.knowledgebase.id = ?1 and o.serviceType.id = ?2 and o.businessUnit.id = ?3 order by o.location.id");
        q.setParameter(1, knowIedgebaseId);
        q.setParameter(2, serviceTypeId);
        q.setParameter(3, businessUnitId);
        return q.getResultList();
    }
    
    public List<Knowledgebase> findKnowledgebaseByUserGroupId(Integer id) {
        //// Query q = em.createQuery("SELECT object(o) FROM Knowledgebase as o WHERE o.id = (SELECT DISTINCT k.knowledgebase.id FROM o.knowledgebaseLocationCollection k,  UserGroupLocation u WHERE k.location.id = u.location.id AND k.serviceType.id =u.serviceType.id AND u.userGroup.id = ?1) and o.enable = true and o.knowledgebase = null order by o.topic");
        //// anuwat
        Query q = em.createQuery("SELECT object(o) FROM Knowledgebase as o WHERE o.id = "
                + "(SELECT DISTINCT k.knowledgebase.id FROM o.knowledgebaseLocationCollection k,  "
                + "UserGroupLocation u WHERE k.location.id = u.location.id AND k.serviceType.id =u.serviceType.id AND u.userGroup.id = ?1) "
                + "and o.enable = true and (o.schedule = false or (o.schedule = true and CONVERT (date, GETDATE())  between o.startdate and o.enddate )) "
                + "and o.approval = true and o.knowledgebase = null order by o.topic");
        q.setParameter(1, id);

        return q.getResultList();
    }
    ////// anuwat
    public List<Knowledgebase> findKnowledgebaseAdminByName(Integer id,String nameKeyword ,String approv) {
        String sqlAppv ="";
        if(approv!=null && !approv.trim().equalsIgnoreCase("")){
            if(approv.trim().equalsIgnoreCase("true")){
            sqlAppv = " and o.approval = 1 ";
            }else{
             sqlAppv = " and (o.approval = 0 or o.approval=null)";   
            }
        }
        //Query q = em.createQuery("select object(o) from Knowledgebase as o where o.enable = true and (o.topic like ?1 or o.description like ?1) order by o.topic");
        Query q = em.createQuery("SELECT object(o) FROM Knowledgebase as o WHERE o.id = (SELECT DISTINCT k.knowledgebase.id FROM o.knowledgebaseLocationCollection k,  UserGroupLocation u WHERE k.location.id = u.location.id AND k.serviceType.id = u.serviceType.id AND u.userGroup.id = ?1) "+ sqlAppv +" and o.enable = true and (o.topic like ?2 or o.description like ?2) and o.knowledgebase = null  order by o.topic");
        q.setParameter(1, id);
        q.setParameter(2, "%"+nameKeyword+"%");
        
        return q.getResultList();
    }
    public List<Knowledgebase> findKnowledgebaseAdminByUserGroupId(Integer id) {
        Query q = em.createQuery("SELECT object(o) FROM Knowledgebase as o WHERE o.id = "
                + "(SELECT DISTINCT k.knowledgebase.id FROM o.knowledgebaseLocationCollection k,  "
                + "UserGroupLocation u WHERE k.location.id = u.location.id AND k.serviceType.id =u.serviceType.id AND u.userGroup.id = ?1) "
                + "and o.enable = true and o.knowledgebase = null order by o.topic");
        q.setParameter(1, id);

        return q.getResultList();
    }
    
    public List<KnowledgebaseDivision> findKnowledgebaseDivisionByIdVersion(Integer id,String version) {
        Query q = em.createQuery("select object(o) from KnowledgebaseDivision as o where o.knowledgebaseDivisionPK.knowledgebaseId = ?1 and o.knowledgebaseDivisionPK.version = ?2  order by o.createDate desc ");
        q.setParameter(1, id);
        q.setParameter(2, version);
       
        return q.getResultList();
    }
    
    public KnowledgebaseDivision findKnowledgebaseVersion(Integer id, String version) {
        Query q = em.createQuery("select object(o) from KnowledgebaseDivision as o where o.knowledgebaseDivisionPK.knowledgebaseId = ?1 "
                + "and o.knowledgebaseDivisionPK.version = ?2  order by o.createDate desc ");
        q.setParameter(1, id);
        q.setParameter(2, version);
        try {
            return (KnowledgebaseDivision) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
        
    public List<KnowledgebaseDivision> findKnowledgebaseDivisionById(Integer id) {
        Query q = em.createQuery("select object(o) from KnowledgebaseDivision as o where o.knowledgebaseDivisionPK.knowledgebaseId = ?1  order by o.createDate desc ");
        q.setParameter(1, id); 
       
        return q.getResultList();
    }
    
    public List<Knowledgebase> findKnowledgebaseRelateId(Integer id,String version ) {
        Query q = em.createQuery("SELECT object(o) FROM Knowledgebase as o WHERE o.id in (SELECT DISTINCT k.knowledgebaseRelatedPK.relateKnowledgebaseId FROM KnowledgebaseRelated k WHERE k.knowledgebaseRelatedPK.knowledgebaseId = ?1 AND k.knowledgebaseRelatedPK.knowledgebaseVersion = ?2) and o.enable = true and o.knowledgebase = null order by o.topic");
        q.setParameter(1, id);
        q.setParameter(2, version);
        return q.getResultList();
    }
    
    public List<Knowledgebase> findKnowledgebaseNotId(Integer id) {
        Query q = em.createQuery("SELECT object(o) FROM Knowledgebase as o WHERE   o.enable = true and o.id <> ?1 and o.knowledgebase = null order by o.topic");
        q.setParameter(1, id); 
        return q.getResultList();
    }
    
    public void deleteAllKnowledgebaseRelateId(Integer id,String version) throws Exception {
        Query q = em.createQuery("Delete FROM KnowledgebaseRelated k WHERE k.knowledgebaseRelatedPK.knowledgebaseId = ?1 AND k.knowledgebaseRelatedPK.knowledgebaseVersion = ?2 ");
        q.setParameter(1, id);
        q.setParameter(2, version);
        q.executeUpdate();
    }
    
    public List<KnowledgebaseBoard> findKnowledgebaseBoardById(Integer id,String version) {
        Query q = em.createQuery("select object(o) from KnowledgebaseBoard as o where o.knowledgebaseId = ?1 AND o.knowledgebaseVersion = ?2  order by o.createDate desc ");
        q.setParameter(1, id);
        q.setParameter(2, version);
        return q.getResultList();
    }
    
    public void updateViewKnowledgebaseById(Integer id) throws Exception {
        Query q = em.createQuery("update Knowledgebase as o set o.viewcount=(ISNULL(o.viewcount,0)+1) WHERE o.id = ?1  ");
        q.setParameter(1, id);
        q.executeUpdate();
    }
    
    public int getKnowledgebaseVoteAverage(Integer id,String version)throws Exception {
       try{
        Query q = em.createQuery("select ROUND(avg(o.vote),0) from KnowledgebaseVote as o where o.knowledgebaseVotePK.knowledgebaseId = ?1 AND o.knowledgebaseVotePK.knowledgebaseVersion = ?2  GROUP BY o.knowledgebaseVotePK.knowledgebaseId , o.knowledgebaseVotePK.knowledgebaseVersion ");
        q.setParameter(1, id);
        q.setParameter(2, version);
         
           return ((Double) q.getSingleResult()).intValue();
        }catch(Exception e){
            e.getStackTrace();
           return 0;
        }
    }
        
    public List<KnowledgebaseVote> findKnowledgebaseBoardVoteUserId(Integer id,String version,Integer userid) {
        Query q = em.createQuery("select object(o) from KnowledgebaseVote as o where o.knowledgebaseVotePK.knowledgebaseId = ?1 AND o.knowledgebaseVotePK.knowledgebaseVersion = ?2 AND o.knowledgebaseVotePK.userId = ?3 ");
        q.setParameter(1, id);
        q.setParameter(2, version);
        q.setParameter(3, userid);
        return q.getResultList();
    }
    
    public List<KnowledgebaseAttfile> findKnowledgebaseAttFileById(Integer id) {
        Query q = em.createQuery("select object(o) from KnowledgebaseAttfile as o where o.knowledgebaseId = ?1  order by o.createDate asc ");
        q.setParameter(1, id);
        return q.getResultList();
    }
    
    
    public void deleteKnowledgebaseAttFileById(Integer id) throws Exception {
        Query q = em.createQuery("Delete FROM KnowledgebaseAttfile k WHERE k.kbattfileId = ?1  ");
        q.setParameter(1, id); 
        q.executeUpdate();
    }
    
    public KnowledgebaseDivision findKnowledgebaseDivision(Integer id,String version) {
        Query q = em.createQuery("select object(o) from KnowledgebaseDivision as o where o.knowledgebaseDivisionPK.knowledgebaseId = ?1 and o.knowledgebaseDivisionPK.version = ?2  order by o.createDate desc ");
        q.setParameter(1, id);
        q.setParameter(2, version);
       
        return ((KnowledgebaseDivision)q.getSingleResult());
    }
    
    public void updateApprovalKnowledgebaseById(Integer id, boolean approve) throws Exception {
        Query q = em.createQuery("update Knowledgebase as o set o.approval = ?2, o.viewcount = 0  WHERE o.id = ?1  ");
        q.setParameter(1, id);
        q.setParameter(2, approve);
        q.executeUpdate();
    }
    
    public List<Knowledgebase> findFAQKBByName(Integer id,String nameKeyword) {
//        Query q = em.createQuery("SELECT object(o) FROM Knowledgebase as o WHERE o.id = (SELECT DISTINCT k.knowledgebase.id FROM o.knowledgebaseLocationCollection k,  UserGroupLocation u WHERE k.location.id = u.location.id AND k.serviceType.id = u.serviceType.id AND u.userGroup.id = ?1) and o.enable = true and (o.schedule = false or (o.schedule = true and CONVERT (date, GETDATE())  between o.startdate and o.enddate )) and o.approval = true and (o.topic like ?2 or o.description like ?2) and o.faq = true and o.knowledgebase = null order by o.topic");
        Query q = em.createQuery("SELECT object(o) FROM Knowledgebase as o WHERE o.id = (SELECT DISTINCT k.knowledgebase.id FROM o.knowledgebaseLocationCollection k,  UserGroupLocation u WHERE k.location.id = u.location.id AND k.serviceType.id = u.serviceType.id AND u.userGroup.id = ?1) and o.enable = true and (o.schedule = false or (o.schedule = true and CONVERT (date, GETDATE())  between o.startdate and o.enddate )) and o.approval = true and (o.topic like ?2 or o.description like ?2) and o.faq = true order by o.viewcount Desc, o.topic");
        q.setParameter(1, id);
        q.setParameter(2, "%"+nameKeyword+"%");
        return q.getResultList();
    }
    
    public List<Knowledgebase> findFAQKBByUserGroupId(Integer id) {
      //Query q = em.createQuery("SELECT object(o) FROM Knowledgebase as o WHERE o.id = (SELECT DISTINCT k.knowledgebase.id FROM o.knowledgebaseLocationCollection k,  UserGroupLocation u WHERE k.location.id = u.location.id AND k.serviceType.id =u.serviceType.id AND u.userGroup.id = ?1) and o.enable = true and (o.schedule = false or (o.schedule = true and CONVERT (date, GETDATE())  between o.startdate and o.enddate )) and o.approval = true and  (o.faq = true or o.id in (select l.id FROM Knowledgebase as l where l.enable = true and l.faq = false))  and o.knowledgebase = null order by o.topic");
        
//        List<Integer> valueList = new ArrayList<Integer>();
//        String csql = "";
//        Query q2 = em.createQuery("SELECT o.id FROM Knowledgebase as o WHERE o.id = "
//                + "(SELECT DISTINCT k.knowledgebase.id FROM o.knowledgebaseLocationCollection k,  "
//                + "UserGroupLocation u WHERE k.location.id = u.location.id AND k.serviceType.id =u.serviceType.id AND u.userGroup.id = ?1) "
//                + "and o.enable = true and (o.schedule = false or (o.schedule = true and CONVERT (date, GETDATE())  between o.startdate and o.enddate )) "
//                + "and o.approval = true and (o.faq = true or o.faq = null ) and o.knowledgebase = null order by o.viewcount Desc");
//        q2.setParameter(1, id);
//        q2.setFirstResult(0);
//        q2.setMaxResults(JSFUtil.getApplication().getKbTopView());
//        valueList.addAll(q2.getResultList()); 
//        for(int cb :valueList){
//            csql += ","+ String.valueOf(cb);
//        }
        
//        Query q = em.createQuery("SELECT object(o) FROM Knowledgebase as o WHERE o.id = (SELECT DISTINCT k.knowledgebase.id FROM o.knowledgebaseLocationCollection k,  UserGroupLocation u WHERE k.location.id = u.location.id AND k.serviceType.id =u.serviceType.id AND u.userGroup.id = ?1) and o.enable = true and (o.schedule = false or (o.schedule = true and CONVERT (date, GETDATE())  between o.startdate and o.enddate )) and o.approval = true and  ( o.faq = true or o.id in (0"+ csql +")) and o.knowledgebase = null order by o.topic");
        Query q = em.createQuery("SELECT object(o) FROM Knowledgebase as o WHERE o.id = (SELECT DISTINCT k.knowledgebase.id FROM o.knowledgebaseLocationCollection k,  UserGroupLocation u WHERE k.location.id = u.location.id AND k.serviceType.id = u.serviceType.id AND u.userGroup.id = ?1) and o.enable = true and (o.schedule = false or (o.schedule = true and CONVERT (date, GETDATE())  between o.startdate and o.enddate )) and o.approval = true and o.faq = true order by o.viewcount Desc, o.topic");
        q.setParameter(1, id); 
        return q.getResultList();
    }
         
     
    public void deleteAllKnowledgebaseURLById(Integer id,String version) throws Exception {
        Query q = em.createQuery("Delete FROM KnowledgebaseUrl k WHERE k.knowledgebaseId = ?1  AND k.knowledgebaseVersion = ?2 ");
        q.setParameter(1, id); 
        q.setParameter(2, version);
        q.executeUpdate();
    }
    
    public List<KnowledgebaseUrl> findKnowledgebaseUrlById(Integer id,String version) {
        Query q = em.createQuery("select object(o) from KnowledgebaseUrl as o where o.knowledgebaseId = ?1 AND o.knowledgebaseVersion = ?2  ");
        q.setParameter(1, id);
        q.setParameter(2, version);
        return q.getResultList();
    }
    
    public List<KnowledgebaseAttfile> findKnowledgebaseAttFileByName(String attname) {
        Query q = em.createQuery("select object(o) from KnowledgebaseAttfile as o where o.kbattfileFilename = ?1  order by o.createDate asc ");
        q.setParameter(1, attname);
        return q.getResultList();
    }
    
    public Map<String, Integer> getAttList (String[] attname) {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for(String uz:attname){
        List<KnowledgebaseAttfile> attList = findKnowledgebaseAttFileByName(uz);
        
        for (KnowledgebaseAttfile obj : attList) {
            values.put(obj.getKbattfileFilename(), obj.getKnowledgebaseId());
        }
        }
        return values;
    }
        
    public int checkVersion(String version, Integer id) { 
        Query q;
        q = em.createQuery("select count(o.knowledgebaseDivisionPK.knowledgebaseId) from KnowledgebaseDivision as o "
                + "where o.knowledgebaseDivisionPK.knowledgebaseId = :id and o.knowledgebaseDivisionPK.version = :version and o.enable = true");        
        q.setParameter("id", id);
        q.setParameter("version", version);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public Boolean isCircular(Integer sourceid, Integer targetid) {
        int i=0;
        while (true) {
            Knowledgebase kb = findKnowledgebase(targetid);
            if (kb==null || kb.getKnowledgebase()==null) {
                break;
            }
            kb = kb.getKnowledgebase();
            targetid = kb.getId();
            if (targetid.intValue()==sourceid.intValue()) {
                return true;
            }
        }
        return false;
    }
}

