/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.ContactRecordValue;
import com.maxelyz.utils.JSFUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.faces.bean.ManagedProperty;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ContactHistoryDAO {

    private static Logger log = Logger.getLogger(ContactHistoryDAO.class);
    @PersistenceContext
    private EntityManager em;

    public void create(ContactHistory contactHistory) {
        em.persist(contactHistory);
        //this.updateRptContactHistory(contactHistory); -> move to called by controller
    }
    public void createContactHistorySaleResult(ContactHistorySaleResult contactHistorySaleResult) {
        em.persist(contactHistorySaleResult);
    }

    public void createContactHistoryKnowledge(List<ContactHistoryKnowledge> contactHistoryKnowledges) {
        for (ContactHistoryKnowledge value : contactHistoryKnowledges) {
            em.persist(value);
        }
    }

    public void createContactHistoryProduct(List<ContactHistoryProduct> contactHistoryProducts) {
        for (ContactHistoryProduct value : contactHistoryProducts) {
            em.persist(value);
        }
    }

    public void edit(ContactHistory contactHistory) throws NonexistentEntityException, Exception {
//        contactHistory = em.merge(contactHistory);
        Query q1 = em.createQuery("update ContactHistory set "
                + "contactDate = :contactDate, "
                + "contactTo = :contactTo, "
                + "contactStatus = :contactStatus, "
                + "customer = :customer, "
                + "callSuccess = :callSuccess, "
                + "followupsale = :followupsale, "
                + "contactResult = :contactResult, "
                + "telephonyTrackId = :telephonyTrackId, "
                + "channel = :channel, "
                + "contactClose = :contactClose, "
                + "remark = :remark, "
                + "contactToName = :contactToName, "
                + "talkTime = :talkTime "
                + "where id = :id");
        q1.setParameter("contactDate", contactHistory.getContactDate());
        q1.setParameter("contactTo", contactHistory.getContactTo());
        q1.setParameter("contactStatus", contactHistory.getContactStatus());
        q1.setParameter("customer", contactHistory.getCustomer());
        q1.setParameter("callSuccess", contactHistory.getCallSuccess());
        q1.setParameter("followupsale", contactHistory.getFollowupsale());
        q1.setParameter("contactResult", contactHistory.getContactResult());
        q1.setParameter("telephonyTrackId", contactHistory.getTelephonyTrackId());
        q1.setParameter("channel", contactHistory.getChannel());
        q1.setParameter("contactClose", contactHistory.getContactClose());
        q1.setParameter("remark", contactHistory.getRemark());
        q1.setParameter("contactToName", contactHistory.getContactToName());
        q1.setParameter("talkTime", contactHistory.getTalkTime());
        q1.setParameter("id", contactHistory.getId());
        q1.executeUpdate();
        
        JSFUtil.getUserSession().setContactHistory(contactHistory);
    }
    
    public void updateMaxCallContact(ContactHistory contactHistory) throws NonexistentEntityException, Exception {
        Query q1 = em.createQuery("update ContactHistory set contactResult = :contactResult where id = :id");
        q1.setParameter("contactResult", contactHistory.getContactResult());
        q1.setParameter("id", contactHistory.getId());
        q1.executeUpdate();
    }

    public void updateTrackId(ContactHistory contactHistory, String telephonyTrackId) throws NonexistentEntityException, Exception {
//        contactHistory = em.merge(contactHistory);
        if((contactHistory.getTelephonyTrackId() == null || contactHistory.getTelephonyTrackId().equals("0")) && (telephonyTrackId != null && !telephonyTrackId.equals("0"))){
            Query q1 = em.createQuery("update ContactHistory set "
                    + "telephonyTrackId = :telephonyTrackId "
                    + "where id = :id");
            q1.setParameter("telephonyTrackId", telephonyTrackId);
            q1.setParameter("id", contactHistory.getId());
            q1.executeUpdate();
            
            contactHistory.setTelephonyTrackId(telephonyTrackId);
            JSFUtil.getUserSession().setContactHistory(contactHistory);
        }
    }

    public void updateTalkTime(ContactHistory contactHistory, Integer talkTime) throws NonexistentEntityException, Exception {
//        contactHistory = em.merge(contactHistory);
        if(contactHistory.getTalkTime() == null){
            Query q1 = em.createQuery("update ContactHistory set "
                    + "talkTime = :talkTime "
                    + "where id = :id");
            q1.setParameter("talkTime", talkTime);
            q1.setParameter("id", contactHistory.getId());
            q1.executeUpdate();

            contactHistory.setTalkTime(talkTime);
            JSFUtil.getUserSession().setContactHistory(contactHistory);
        }
    }

    public void updateContactTo(ContactHistory contactHistory, String contactTo) throws NonexistentEntityException, Exception {
//        contactHistory = em.merge(contactHistory);
        if(contactTo != null && !contactTo.equals("")){
            Query q1 = em.createQuery("update ContactHistory set "
                    + "contactTo = :contactTo "
                    + "where id = :id");
            q1.setParameter("contactTo", contactTo);
            q1.setParameter("id", contactHistory.getId());
            q1.executeUpdate();

            contactHistory.setContactTo(contactTo);
            JSFUtil.getUserSession().setContactHistory(contactHistory);
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        ContactHistory contactHistory;
        try {
            contactHistory = em.getReference(ContactHistory.class, id);
            contactHistory.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The contactHistory with id " + id + " no longer exists.", enfe);
        }
        em.remove(contactHistory);
    }

    public List<ContactHistory> findContactHistoryEntities() {
        return findContactHistoryEntities(true, -1, -1);
    }

    public List<ContactHistory> findContactHistoryEntities(int maxResults, int firstResult) {
        return findContactHistoryEntities(false, maxResults, firstResult);
    }

    private List<ContactHistory> findContactHistoryEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ContactHistory as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public ContactHistory findContactHistory(Integer id) {
        return em.find(ContactHistory.class, id);
    }

    public int getContactHistoryCount() {
        return ((Long) em.createQuery("select count(o) from ContactHistory as o").getSingleResult()).intValue();
    }

    public void updateRptContactHistory(ContactHistory contactHistory) {
        //ContactHistory contactHistory = findContactHistory(contactHistoryParam.getId());
        try {
            Date contactDate = JSFUtil.toDateWithoutTime(contactHistory.getContactDate());
            //Rpt Knowledgebase
            Collection<ContactHistoryKnowledge> contactHistoryKnowledges = contactHistory.getContactHistoryKnowledgeCollection();
            if (contactHistoryKnowledges != null) {
                for (ContactHistoryKnowledge chk : contactHistoryKnowledges) {
                    Integer kbid = chk.getContactHistoryKnowledgePK().getKnowledgebaseId();
                    RptContactHistoryKnowledgePK rptKnowledgePK = new RptContactHistoryKnowledgePK(contactDate, kbid);
                    RptContactHistoryKnowledge rptContactHistoryKnowledge = this.findRptContactHistoryKnowledge(rptKnowledgePK);
                   
                        if (rptContactHistoryKnowledge == null) {
                            rptContactHistoryKnowledge = new RptContactHistoryKnowledge(contactDate, kbid);
                            rptContactHistoryKnowledge.setTotal(1);
                            if (chk.getUseful()) {
                                rptContactHistoryKnowledge.setUsefulTotal(1);
                            } else {
                                rptContactHistoryKnowledge.setUsefulTotal(0);
                            }
                            em.persist(rptContactHistoryKnowledge); //insert
                        } else {
                            int total = rptContactHistoryKnowledge.getTotal() + 1;
                            rptContactHistoryKnowledge.setTotal(total);
                            if (chk.getUseful()) {
                                int usefulTotal = rptContactHistoryKnowledge.getUsefulTotal() + 1;
                                 rptContactHistoryKnowledge.setUsefulTotal(usefulTotal);
                            }
                            rptContactHistoryKnowledge = em.merge(rptContactHistoryKnowledge); //update
                        }
                    
                }
            }

            //Rpt Knowledgebase
            Collection<ContactHistoryProduct> contactHistoryProducts = contactHistory.getContactHistoryProductCollection();
            if (contactHistoryProducts != null) {
                for (ContactHistoryProduct chp : contactHistoryProducts) {
                    Integer productId = chp.getContactHistoryProductPK().getProductId();
                    RptContactHistoryProductPK rptProductPK = new RptContactHistoryProductPK(contactDate, productId);
                    RptContactHistoryProduct rptContactHistoryProduct = this.findRptContactHistoryProduct(rptProductPK);
                    
                        if (rptContactHistoryProduct == null) {
                            rptContactHistoryProduct = new RptContactHistoryProduct(contactDate, productId);
                            rptContactHistoryProduct.setTotal(1);
                            if (chp.getUseful()) {
                                rptContactHistoryProduct.setUsefulTotal(1);
                            } else {
                                rptContactHistoryProduct.setUsefulTotal(0);
                            }
                            em.persist(rptContactHistoryProduct); //insert
                        } else {
                            int total = rptContactHistoryProduct.getTotal() + 1;
                            rptContactHistoryProduct.setTotal(total);
                            if (chp.getUseful()) {
                                int usefulTotal = rptContactHistoryProduct.getUsefulTotal() + 1;
                                rptContactHistoryProduct.setUsefulTotal(usefulTotal);
                            }
                            rptContactHistoryProduct = em.merge(rptContactHistoryProduct); //update
                        }
                    
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public RptContactHistoryKnowledge findRptContactHistoryKnowledge(RptContactHistoryKnowledgePK id) {
        return em.find(RptContactHistoryKnowledge.class, id);
    }

    public RptContactHistoryProduct findRptContactHistoryProduct(RptContactHistoryProductPK id) {
        return em.find(RptContactHistoryProduct.class, id);
    }

    public List<ContactHistory> findContactHistoryByAssignmentDetail(Integer adId) {
        Query q = em.createQuery("select object(o) from ContactHistory as o"
                + " join o.assignmentDetailCollection ad"
                + " where ad.id = :adId"
                + " order by o.contactDate desc");
        q.setParameter("adId", adId);

        return q.getResultList();
    }

    public List<ContactRecordValue> findByAssignmentDetail(Integer adId) {
        String sql = "select NEW "
                + ContactRecordValue.class.getName()
                + " (a.contactDate as contactDate"
                + ", a.contactTo as contactTo"
                //+ ", a.contactStatus as contactStatus"    //conflictmerge - kleasing have this
                + ", b.name as contactResult"
                + ", a.remark as remark"
                + ", a.createBy as createBy)"
                + " from ContactHistory as a "
                + " left join a.contactResult as b"
                + " join a.assignmentDetailCollection ad"
                + " where ad.id = :adId"
                + " order by a.contactDate desc";
        Query q = em.createQuery(sql);
        q.setParameter("adId", adId);

        return q.getResultList();
    }
    
    public List<ContactHistory> findContactHistoryByCustomer(Integer customerId) {
        Query q = em.createQuery("select object(o) from ContactHistory as o"
                + " join o.customer a"
                + " where a.id = :customerId"
                + " order by o.contactDate");
        q.setParameter("customerId", customerId);

        return q.getResultList();
    }

    public List<ContactHistory> findContactHistoryByCustomer1(Integer customerId) {
        Query q = em.createQuery("select object(o) from ContactHistory as o"
                + " join o.customer a"
                + " where a.id = :customerId"
                + " order by o.contactDate desc");
        q.setParameter("customerId", customerId);

        q.setMaxResults(5);
        q.setFirstResult(0);
        return q.getResultList();
    }
/*
    public List<ContactRecordValue> findContactHistoryValue(Date fromDate, Date toDate, int userId, String fName, String lName, Integer customerId, 
Integer contactHistoryId, String contactToSearch, int channelId) {
        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String sql = "select NEW "
                + ContactRecordValue.class.getName()
                + " (a.id as id"
                + ", a.contactDate as contactDate"
                + ", c.id as customerId"
                + ", c.name + ' ' + c.surname as customerName"
                + ", a.contactTo as contactTo"
                + ", b.name as contactResult"
                + ", a.telephonyTrackId as trackId"
                + ", a.remark as remark"
                + ", a.createBy as createBy"
                + ", a.createDate as createDate"
                + ", a.talkTime as talkTime"
                + ", a.users.id as createById"
                + ", ch.name as channel"
                + ", ch.type as channelType)"
                + " from ContactHistory as a"
                + " left join a.contactResult as b"
                + " left join a.customer as c"
                + " left join a.channel as ch"
                + " where a.customer.id is not null";
        if(fromDate != null && toDate != null){
            sql += " and a.contactDate >= '" + sdfFrom.format(fromDate) + "' and a.contactDate <= '" + sdfTo.format(toDate) + "'";
        }
        if(userId != 0){
            sql += " and a.users.id = " + userId;
        }
        if(channelId != 0){
            sql += " and a.channel.id = " + channelId;
        }
        if(!fName.equals("") && !lName.equals("")){
            sql += " and (c.name like '%" + fName + "%' and c.surname like '%" + lName + "%')";
        }else if(!fName.equals("") && lName.equals("")){
            sql += " and (c.name like '%" + fName + "%' or c.surname like '%" + fName + "%')";
        }
        if(customerId != null && customerId != 0){
            sql += " and c.id = " + customerId;
        }
        if(contactHistoryId != null && contactHistoryId != 0){
            sql += " and a.id = " + contactHistoryId;
        }
        if(!contactToSearch.equals("")){
            sql += " and a.contactTo like '%" + contactToSearch + "%'";
        }
        sql += " order by a.contactDate";
        Query q = em.createQuery(sql);
        List<ContactRecordValue> list = null;

        //q.setMaxResults(1000);
        
        try{
            list = (List<ContactRecordValue>) q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }*/
    
    // for contact record on admin menu
    public List<ContactRecordValue> findContactHistoryValue(Date fromDate, Date toDate, int userGroupId, int userId, String fName, String lName, Integer customerId, 
    Integer contactHistoryId, String contactToSearch, int channelId, String contactStatus, Integer contactResultId) {
        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
        SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59", Locale.US);
        String sql = "select NEW "
                + ContactRecordValue.class.getName()
                + " (a.id as id"
                + ", a.contactDate as contactDate"
                + ", c.id as customerId"
                + ", ISNULL(c.name, '') + ' ' + ISNULL(c.surname, '') as customerName"
                + ", a.contactTo as contactTo"
                + ", b.contactStatus as contactStatus"
                + ", b.name as contactResult"
                + ", a.telephonyTrackId as trackId"
                + ", a.remark as remark"
                + ", a.createBy as createBy"
                + ", a.createDate as createDate"
                + ", a.talkTime as talkTime"
                + ", a.users.id as createById"
                + ", ch.name as channel"
                + ", ch.type as channelType"
                + ", a.voiceSource as voiceSource"
                + ", count(cc.contactHistory.id) as cntCase"
                + ", count (po) as cntSale"
                + ", a.stationNo as stationNo)" //19
                + " from ContactCase as cc"
                + " right join cc.contactHistory as a "
                + " left join a.purchaseOrderCollection po"
                + " left join a.contactResult as b"
                + " left join a.customer as c"
                + " left join a.channel as ch"
                + " where 1=1"; //+ " where a.customer.id is not null";
        if(fromDate != null && toDate != null){
            sql += " and a.contactDate >= '" + sdfFrom.format(fromDate) + "' and a.contactDate <= '" + sdfTo.format(toDate) + "'";
        }
        if(userId != 0){
            sql += " and a.users.id = " + userId;
        }
        if(userGroupId != 0){
            sql += " and a.users.userGroup.id = " + userGroupId;
        }
        if(channelId != 0){
            sql += " and a.channel.id = " + channelId;
        }
        
        if(contactResultId != 0) {
            sql += " and b.id = " + contactResultId;
        }
        
        if(!fName.equals("") && !lName.equals("")){
            sql += " and (c.name like '%" + fName + "%' and c.surname like '%" + lName + "%')";
        }else if(!fName.equals("") && lName.equals("")){
            sql += " and (c.name like '%" + fName + "%' or c.surname like '%" + fName + "%')";
        }
        if(customerId != null && customerId != 0){
            sql += " and c.id = " + customerId;
        }
        if(contactHistoryId != null && contactHistoryId != 0){
            sql += " and a.id = " + contactHistoryId;
        }
        if(!contactToSearch.equals("")){
            sql += " and a.contactTo like '%" + contactToSearch + "%'";
        }
//        if(!contactStatus.equals("") && !contactStatus.equals("all")){
//            sql += " and b.contactStatus like '%" + contactStatus + "%'";
//        }
        if(!contactStatus.equals("") && contactStatus.equals("dmc")){
            sql += " and b.contactStatus = 'DMC'";
        }
        if(!contactStatus.equals("") && contactStatus.equals("contactable")){
            sql += " and b.contactStatus = 'Contactable'";
        }
        if(!contactStatus.equals("") && contactStatus.equals("uncontactable")){
            sql += " and b.contactStatus = 'Uncontactable'";
        }
        if(!contactStatus.equals("") && contactStatus.equals("cs")){
            sql += " and b.contactStatus = 'Inbound'";
        }
        if(contactResultId != null && contactResultId != 0){
            sql += " and b.id = " + contactResultId;
        }
        sql += " group by a.id,a.contactDate,c.id,c.name,c.surname,a.contactTo,b.contactStatus,b.name,a.telephonyTrackId,a.remark,a.createBy,a.createDate,a.talkTime,a.users.id,ch.name,ch.type,cc.contactHistory.id,a.stationNo,a.voiceSource";
        sql += " order by a.contactDate";
        Query q = em.createQuery(sql);
        List<ContactRecordValue> list = null;

        //q.setMaxResults(1000);
        
        try{
            list = (List<ContactRecordValue>) q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }
    
    //for contact history on front customer handling
     public List<ContactRecordValue> findContactHistoryByCustomerId(Integer customerId) {
        String sql = "select NEW "
                + ContactRecordValue.class.getName()
                + " (a.id as id"
                + ", a.contactDate as contactDate"
                + ", c.id as customerId"
                + ", ISNULL(c.name, '') + ' ' + ISNULL(c.surname, '') as customerName"
                + ", a.contactTo as contactTo"
                + ", b.name as contactResult"
                + ", a.telephonyTrackId as trackId"
                + ", a.remark as remark"
                + ", a.createBy as createBy"
                + ", a.createDate as createDate"
                + ", a.talkTime as talkTime"
                + ", a.users.id as createById"
                + ", ch.name as channel"
                + ", ch.type as channelType"
                + ", count(cc.contactHistory.id) as cntCase"
                + ", count (po) as cntSale)"    //16 Field
                + " from ContactCase as cc"
                + " right join cc.contactHistory as a "
                + " left join a.purchaseOrderCollection po"
                + " left join a.contactResult as b"
                + " left join a.customer as c"
                + " left join a.channel as ch"
                + " where c.id = " + customerId;
        sql += " group by a.id,a.contactDate,c.id,c.name,c.surname,a.contactTo,b.name,a.telephonyTrackId,a.remark,a.createBy,a.createDate,a.talkTime,a.users.id,ch.name,ch.type,cc.contactHistory.id";
        sql += " order by a.contactDate desc";
        Query q = em.createQuery(sql);
        List<ContactRecordValue> list = null;
        try{
            list = (List<ContactRecordValue>) q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }
     
    public List<PurchaseOrderDetail> findContactHistorySaleResult(Integer contactHistoryId) {
        String sql = "select object(pd) from ContactHistory as a"
                + " join a.purchaseOrderCollection po"
                + " join po.purchaseOrderDetailCollection pd"
                + " where 1=1";
/*
select * from contact_history_purchase_order ch 
join purchase_order_detail po on ch.purchase_order_id = po.purchase_order_id 

                + " from ContactCase as cc"
                + " right join cc.contactHistory as a "
                + " left join a.purchaseOrderCollection po"         */
        
        if(contactHistoryId != null && contactHistoryId != 0){
            sql += " and a.id = " + contactHistoryId;
        }
        sql += " order by a.contactDate";
        Query q = em.createQuery(sql);

        return q.getResultList();
    }
    /*
      
    public List<ContactHistorySaleResult> findContactHistorySaleResult(Integer customerId, Integer contactHistoryId) {
        String sql = "select object(o) from ContactHistorySaleResult as o"
                + " join o.contactHistory a"
                + " join a.customer b"
                + " where 1=1";

        if(customerId != null && customerId != 0){
            sql += " and b.id = " + customerId;
        }
        if(contactHistoryId != null && contactHistoryId != 0){
            sql += " and a.id = " + contactHistoryId;
        }
        sql += " order by a.contactDate";
        Query q = em.createQuery(sql);

        return q.getResultList();
     }     */

     public List<ContactHistorySaleResult> findContactHistoryPurchaseOrder(Integer customerId, Integer contactHistoryId) {
        String sql = "select object(o) from ContactHistory as a"
                    + " join a.purchaseOrderCollection po"
                    + " where 1=1";
         /*           
        if(customerId != null && customerId != 0){
            sql += " and b.id = " + customerId;
        }*/
        if(contactHistoryId != null && contactHistoryId != 0){
            sql += " and a.id = " + contactHistoryId;
        }
        sql += " order by a.contactDate";
        Query q = em.createQuery(sql);

        return q.getResultList();
    }

    public void updateTalkTime(String trackId, Integer talkTime) {
        try{           
            Query q1 = em.createQuery("update ContactHistory set"
                    + " talkTime = :talkTime"
                    + " where telephonyTrackId = :trackId");
            q1.setParameter("talkTime", talkTime);
            q1.setParameter("trackId", trackId);
            if(trackId != null && !trackId.isEmpty() && !trackId.equals("0")){
                //System.out.println(trackId + " : " + talkTime);
                q1.executeUpdate();
            }
        }catch(Exception e){
            //log.error("updateTalkTime : " + e);
        }
    }

    public void saveParam(Integer contactHistoryId, String trackId, Integer talkTime, String phoneNo) {
        try{           
            Query q1 = em.createNativeQuery("update contact_history set"
                    + " telephony_track_id = :trackId"
                    + ", talk_time = :talkTime"
                    + ", contact_to = :phoneNo"
                    + " where id = :contactHistoryId");
            q1.setParameter("trackId", trackId);
            q1.setParameter("talkTime", talkTime);
            q1.setParameter("phoneNo", phoneNo);
            q1.setParameter("contactHistoryId", contactHistoryId);
            q1.executeUpdate();
        }catch(Exception e){
            log.error("saveParam : " + e);
        }
    }
     
//    public void updateTalkTime(String trackId, Integer talkTime) {
//        try{
//            Query q1 = em.createQuery("update ContactHistory set"
//                    + " talkTime = :talkTime"
//                    + " where telephonyTrackId = :trackId");
//            q1.setParameter("talkTime", talkTime);
//            q1.setParameter("trackId", trackId);
//            if(trackId != null && !trackId.isEmpty() && !trackId.equals("0")){
//                q1.executeUpdate();
//            }
//        }catch(Exception e){
//            //log.error("updateTalkTime : " + e);
//        }
//    }
    
    public List<ContactRecordValue> findByPoId(Integer poId) {
        List<ContactRecordValue> list = null;
        try {
            Query q = em.createQuery("select NEW "
                    + ContactRecordValue.class.getName()
                    + " (a.id as id"
                    + ", a.contactDate as contactDate"
                    + ", 0"
                    + ", ''"
                    + ", a.contactTo as contactTo"
                    + ", cr.name as contactResult"
                    + ", a.telephonyTrackId as trackId"
                    + ", a.remark as remark"
                    + ", a.createBy as createBy"
                    + ", a.createDate as createDate"
                    + ", a.talkTime as talkTime"
                    + ", a.users.id as createById)"
                    + " from ContactHistory as a"
                    + " join a.purchaseOrderCollection po"
                    + " left join a.contactResult cr"
                    + " where po.id = :poId"
                    + " order by a.id desc");
            q.setParameter("poId", poId);
            list = q.getResultList();
            
        }catch(Exception e){
            log.error(e);
        }
        return list;
    }
    
    public List<ContactRecordValue> findByAssignmentDetailId(Integer assignmentDetailId) {
        List<ContactRecordValue> list = null;
        try {
            Query q = em.createQuery("select NEW "
                    + ContactRecordValue.class.getName()
                    + " (a.id as id"
                    + ", a.contactDate as contactDate"
                    + ", 0"
                    + ", ''"
                    + ", a.contactTo as contactTo"
                    + ", cr.name as contactResult"
                    + ", a.telephonyTrackId as trackId"
                    + ", a.remark as remark"
                    + ", a.createBy as createBy"
                    + ", a.createDate as createDate"
                    + ", a.talkTime as talkTime"
                    + ", a.users.id as createById"
                    + ", a.stationNo as stationNo"
                    + ", a.voiceSource as voiceSource)"
                    + " from ContactHistory as a"
                    + " join a.assignmentDetailCollection ad"
                    + " left join a.contactResult cr"
                    + " where ad.id = :assignmentDetailId"
                    + " order by a.id desc");
            q.setParameter("assignmentDetailId", assignmentDetailId);
            list = q.getResultList();
            
        }catch(Exception e){
            log.error(e);
        }
        return list;
    }

    public void updateContactHistory(ContactHistory contactHistory) throws NonexistentEntityException, Exception {
        Query q1 = em.createQuery("update ContactHistory set"
                + " contactClose = :contactClose"
                + " , remark = :remark"
                + " where id = :id");
        q1.setParameter("contactClose", contactHistory.getContactClose());
        q1.setParameter("remark", contactHistory.getRemark());
        q1.setParameter("id", contactHistory.getId());
        q1.executeUpdate();
    }

    public void createContactHistoryPurchaseOrder(Integer purchaseOrderId, Integer contactHistoryId) throws NonexistentEntityException, Exception {
        Query q1 = em.createNativeQuery("insert into contact_history_purchase_order(contact_history_id, purchase_order_id)"
                + " values(:contactHistoryId, :purchaseOrderId)");
        q1.setParameter("purchaseOrderId", purchaseOrderId);
        q1.setParameter("contactHistoryId", contactHistoryId);
        
        q1.executeUpdate();
    }

    public void createContactHistoryAssignmentDetail(Integer contactHistoryId, Integer assignmentDetailId) throws NonexistentEntityException, Exception {
        Query q1 = em.createNativeQuery("insert into contact_history_assignment(contact_history_id, assignment_detail_id)"
                + " values(:contactHistoryId, :assignmentDetailId)");
        q1.setParameter("contactHistoryId", contactHistoryId);
        q1.setParameter("assignmentDetailId", assignmentDetailId);
        
        q1.executeUpdate();
    }
    
    public void updateContactToName(ContactHistory contactHistory) {//Integer contactHistoryId, String contactToName) {
         Query q1 = em.createQuery("update ContactHistory set"
                + " contactToName = :contactToName,"
                + " customer = :customer"
                + " where id = :id");
        q1.setParameter("contactToName", contactHistory.getContactToName());
        q1.setParameter("customer", contactHistory.getCustomer());
        q1.setParameter("id", contactHistory.getId());
        q1.executeUpdate();
    }

     public ContactHistory findByCustomerTrackId(Integer customerId, String trackId) {
        String sql = "select object(a) from ContactHistory as a where a.customer.id = :customerId and a.telephonyTrackId = :trackId";
        Query q = em.createQuery(sql);
        q.setParameter("customerId", customerId);
        q.setParameter("trackId", trackId);

        ContactHistory ch = null;
        if(q.getResultList().size() > 0) {
            ch = (ContactHistory) q.getResultList().get(0);
        }
        return ch;
    }

     public ContactHistory findByCustomerTrackId(String trackId) {
        String sql = "select object(o) from ContactHistory as o where o.telephonyTrackId = :trackId";
        Query q = em.createQuery(sql);
        q.setParameter("trackId", trackId);

        return (ContactHistory) q.getSingleResult();
    }
}
