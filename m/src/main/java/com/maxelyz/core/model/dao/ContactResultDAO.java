/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ContactResult;
import com.maxelyz.utils.JSFUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ContactResultDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(ContactResult contactResult) throws PreexistingEntityException, Exception {
        em.persist(contactResult);
    }

    public void edit(ContactResult contactResult) throws NonexistentEntityException, Exception {
        contactResult = em.merge(contactResult);
    }

    public List<ContactResult> findContactResultEntities() {
        return findContactResultEntities(true, -1, -1);
    }

    public List<ContactResult> findContactResultEntities(int maxResults, int firstResult) {
        return findContactResultEntities(false, maxResults, firstResult);
    }

    private List<ContactResult> findContactResultEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ContactResult as o where o.enable=true order by o.contactStatus, o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<ContactResult> findContactResultByService(String service) {
        String where;
        if (service.equals("inbound")) {
            where = " and o.contactStatus='inbound' ";
        } else if (service.equals("outbound")) {
            where = " and o.contactStatus<>'inbound' ";
        } else {
            where = "";
        }
        String select = "select object(o) from ContactResult as o where o.enable=true ";
        String orderBy = "order by o.contactStatus, o.code ";
        String sql = select + where + orderBy;
        Query q = em.createQuery(sql);

        return q.getResultList();
    }

    public ContactResult findContactResult(Integer id) {
        return em.find(ContactResult.class, id);
    }

    public int getContactResultCount() {
        Query q = em.createQuery("select count(o) from ContactResult as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public static String getContactResultLabel(String value) {
        String result = "";
        if (value.equals(JSFUtil.getBundleValue("contactableValue"))) {
            result = JSFUtil.getBundleValue("contactable");
        } else if (value.equals(JSFUtil.getBundleValue("uncontactableValue"))) {
            result = JSFUtil.getBundleValue("uncontactable");
        } else if (value.equals(JSFUtil.getBundleValue("dmcValue"))) {
            result = JSFUtil.getBundleValue("dmc");
        } else if (value.equals(JSFUtil.getBundleValue("dmcNotOfferValue"))) {
            result = JSFUtil.getBundleValue("dmcNotOffer");
        } else if (value.equals(JSFUtil.getBundleValue("inboundValue"))) {
            result = JSFUtil.getBundleValue("inboundCS");
        } else if (value.equals(JSFUtil.getBundleValue("inboundTelesaleValue"))) {
            result = JSFUtil.getBundleValue("inboundTelesale");
        } else if (value.equals(JSFUtil.getBundleValue("newContactValue"))) {
            result = JSFUtil.getBundleValue("newContact");
        }
        return result;
    }

    public List<ContactResult> findByContactStatus(String contactStatus) {
        String sql = "select object(cr) from ContactResultPlan as crp join crp.contactResultCollection cr"
                + " where crp.enable=true and crp.isDefault =1";

        if (contactStatus.equalsIgnoreCase("dmc")) {
            sql += " and (UPPER(cr.contactStatus) = 'DMC' or UPPER(cr.contactStatus) = 'DMCNOTOFFER')";
        } else {
            sql += " and UPPER(cr.contactStatus) = :contactStatus";
        }
        sql += " order by cr.contactStatus, cr.seqNo, cr.code ";

        Query q = em.createQuery(sql);
        if (!contactStatus.equalsIgnoreCase("dmc")) {
            q.setParameter("contactStatus", contactStatus.toUpperCase());
        }

        return q.getResultList();
    }

    public List<ContactResult> findContactRecordByContactStatus(String contactStatus) {
        String sql = "select object(o) from ContactResult as o where o.enable=true";
        if(contactStatus.equalsIgnoreCase("dmc")){
            sql += " and (UPPER(o.contactStatus) = 'DMC' or UPPER(o.contactStatus) = 'DMCNOTOFFER')";
        }else{
            sql += " and UPPER(o.contactStatus) = :contactStatus";
        }
        sql += " order by o.contactStatus, o.seqNo, o.code ";

        Query q = em.createQuery(sql);
        if(!contactStatus.equalsIgnoreCase("dmc")){
            q.setParameter("contactStatus", contactStatus.toUpperCase());
        }

        return q.getResultList();
    }

//    public List<ContactResult> findByContactStatusAndNoYesSale(String contactStatus, List<Integer> purchaseOrderId,boolean yesSale) {
//        String sql = "select object(cr) from ContactResultPlan as crp join crp.contactResultCollection as cr"
//                + " where crp.enable=true and crp.id IN (select max(p.contactResultPlan.id) from PurchaseOrderDetail as pod "
//                + " join pod.product as p "
//                + " where pod.purchaseOrder.id IN (:purchaseOrderId))";
//        if (yesSale == true){
//            sql += " and cr.yesSaleRelated = 1 ";
//        }
//        if (contactStatus.equalsIgnoreCase("dmc")) {
//            sql += " and (UPPER(cr.contactStatus) = 'DMC' or UPPER(cr.contactStatus) = 'DMCNOTOFFER')";
//        } else {
//            sql += " and UPPER(cr.contactStatus) = :contactStatus";
//        }
//        sql += " order by cr.contactStatus, cr.seqNo, cr.code ";
//
//        Query q = em.createQuery(sql);
//        if (!contactStatus.equalsIgnoreCase("dmc")) {
//            q.setParameter("contactStatus", contactStatus.toUpperCase());
//        }
//        q.setParameter("purchaseOrderId", purchaseOrderId);
//        return q.getResultList();
//    }
//    
    public List<ContactResult> findByContactStatusAndYesSale(String contactStatus, Integer contactResultPlan, boolean yesSale) {
        String sql = "select object(cr) from ContactResultPlan as crp join crp.contactResultCollection as cr"
                + " where crp.enable=true and crp.id =:contactResultPlan";
        if (yesSale == true) {
            sql += " and cr.yesSaleRelated = 1 ";
        }else {
            sql += " and cr.yesSaleRelated != 1 ";
        }
        if (contactStatus.equalsIgnoreCase("dmc")) {
            sql += " and (UPPER(cr.contactStatus) = 'DMC' or UPPER(cr.contactStatus) = 'DMCNOTOFFER')";
        } else {
            sql += " and UPPER(cr.contactStatus) = :contactStatus";
        }
        sql += " order by cr.contactStatus, cr.seqNo, cr.code ";

        Query q = em.createQuery(sql);
        if (!contactStatus.equalsIgnoreCase("dmc")) {
            q.setParameter("contactStatus", contactStatus.toUpperCase());
        }
        q.setParameter("contactResultPlan", contactResultPlan);
        return q.getResultList();
    }

    public List<ContactResult> findByContactStatusLinkProduct(String contactStatus, Integer contactResultPlanId, boolean haveProduct) {
        String sql = "select object(cr) from ContactResultPlan as crp join crp.contactResultCollection cr"
                + " where crp.enable=true and crp.id =:contactResultPlanId";
        if(haveProduct == false){
            sql += " and cr.yesSaleRelated != 1 ";
        }
        if (contactStatus.equalsIgnoreCase("dmc")) {
            sql += " and (UPPER(cr.contactStatus) = 'DMC' or UPPER(cr.contactStatus) = 'DMCNOTOFFER')";
        } else {
            sql += " and UPPER(cr.contactStatus) = :contactStatus";
        }
        sql += " order by cr.contactStatus, cr.seqNo, cr.code ";

        Query q = em.createQuery(sql);
        if (!contactStatus.equalsIgnoreCase("dmc")) {
            q.setParameter("contactStatus", contactStatus.toUpperCase());
        }
        q.setParameter("contactResultPlanId", contactResultPlanId);
        return q.getResultList();
    }

    public List<ContactResult> findByContactStatusByProduct(String contactStatus, Integer productId) {
        String sql = "select object(o) from ContactResult as o where o.enable=true";
        if (contactStatus.equalsIgnoreCase("dmc")) {
            sql += " and (UPPER(o.contactStatus) = 'DMC' or UPPER(o.contactStatus) = 'DMCNOTOFFER')";
        } else {
            sql += " and UPPER(o.contactStatus) = :contactStatus";
        }
        sql += " order by o.contactStatus, o.seqNo, o.code ";

        Query q = em.createQuery(sql);
        if (!contactStatus.equalsIgnoreCase("dmc")) {
            q.setParameter("contactStatus", contactStatus.toUpperCase());
        }

        return q.getResultList();
    }

    public int checkDuplication(String code, String name, Integer id) {
        Query q;
        if (id == 0) {
            q = em.createQuery("select count(o) from ContactResult as o where o.code =:code and o.name =:name and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from ContactResult as o where o.code =:code and o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<ContactResult> findOpenContactResult() {
        String sql = "select object(o) from ContactResult as o where o.enable=true "
                + "and (o.contactStatus = 'Uncontactable' or o.contactStatus = 'Contactable') "
                + "order by o.name ";

        Query q = em.createQuery(sql);
        return q.getResultList();
    }

    public List<ContactResult> findFollowContactResult() {
        String sql = "select object(o) from ContactResult as o where o.enable=true "
                + "and (o.contactStatus = 'DMC' or o.contactStatus = 'DMCNotOffer') "
                + "order by o.name ";
        Query q = em.createQuery(sql);
        return q.getResultList();
    }

    public Map<String, Integer> getFollowContactResult() {
        List<ContactResult> contactResults = this.findFollowContactResult();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (ContactResult obj : contactResults) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    public List<ContactResult> findSelectedContactResult() {
        String sql = "select object(o) from ContactResult as o where o.enable = true "
                + "and o.systemContactResult = false "
                + "order by o.name ";
        Query q = em.createQuery(sql);
        return q.getResultList();
    }

    public int findSelectedContactResult(List<Integer> contactID) {
        String sql = "select count(o.id) from ContactResult as o where o.enable = true and o.systemContactResult = false and o.yesSaleRelated = true"
                + " and o.id IN (:contactID)";
        Query q = em.createQuery(sql);
        q.setParameter("contactID", contactID);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<ContactResult> findByFilter(String code, String statusID, String name, String contactStatus, int groupId){
        List<ContactResult> list = null;
        try {
            String sql = "select object(o) from ContactResult as o where o.enable = true";
            
            if(code != null && !code.equals("")){
                sql += " and upper(o.code) like :code";
            }
            
            if(statusID != null && !statusID.equals("")){
                sql += " and o.statusId like :statusID";
            }
            
            if(name != null && !name.equals("")){
                sql += " and o.name like :name";
            }
            
            if(contactStatus != null && !contactStatus.equals("")){
                sql += " and o.contactStatus = :contactStatus";
            }
            
            if(groupId != 0){
                sql += " and o.contactResultGroup.id = :groupId";
            }
            
            sql += " order by o.contactStatus, o.code";
            
            Query q = em.createQuery(sql);
            if(code != null && !code.equals("")){
                q.setParameter("code", "%" + code.toUpperCase() + "%");
            }
            
            if(statusID != null && !statusID.equals("")){
                q.setParameter("statusID", "%" + statusID + "%");
            }
            
            if(name != null && !name.equals("")){
                q.setParameter("name", "%" + name + "%");
            }
            
            if(contactStatus != null && !contactStatus.equals("")){
                q.setParameter("contactStatus", contactStatus);
            }
            
            if(groupId != 0){
                q.setParameter("groupId", groupId);
            }
            
            list = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }
}
