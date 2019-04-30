/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.*;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.utils.JSFUtil;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ContactCaseDAO {

    private static Logger log = Logger.getLogger(ContactCaseDAO.class);
    @PersistenceContext
    private EntityManager em;

//    public synchronized void create(ContactCase contactCase) {
//        Query q = em.createNativeQuery("{call GetNextSeq(:tableName)}");
//        q.setParameter("tableName", "CONTACT_CASE");
//        Object o = q.getSingleResult();
//        contactCase.setCode(o.toString());
//
//        Users users = JSFUtil.getUserSession().getUsers();
//        contactCase.setUsersCreate(users);
//        contactCase.setUsersUpdate(users);
//        if (contactCase.getStatus().equals(JSFUtil.getBundleValue("closedValue"))) {
//            contactCase.setUsersClosed(users);
//            contactCase.setClosedDate(contactCase.getCreateDate());
//        }
//
//        em.persist(contactCase);
//
//        //update RptContactChannel Table
//        this.updateRptContactChannel(contactCase, false, "create");
//    }
    
    public synchronized void create(ContactCase contactCase) {
        Users users = JSFUtil.getUserSession().getUsers();
        contactCase.setUsersCreate(users);
        contactCase.setUsersUpdate(users);
        if (contactCase.getStatus().equals(JSFUtil.getBundleValue("closedValue"))) {
            contactCase.setUsersClosed(users);
            contactCase.setClosedDate(contactCase.getCreateDate());
        }

        contactCase.setCode(genRefCode());  //new gen code
        em.persist(contactCase);

        //update RptContactChannel Table
        this.updateRptContactChannel(contactCase, false, "create");
    }

    public synchronized String genRefCode() {
        String str = "";

        Query q = em.createNativeQuery("{call GetNextSeq(:tableName)}");
        q.setParameter("tableName", "CONTACT_CASE");
        Object o = q.getSingleResult();
        str = o.toString();
          
        return str;
    }
    
    public void edit(ContactCase contactCase) throws IllegalOrphanException, NonexistentEntityException, Exception {
        //Rollback old data from RptContactChannel
        ContactCase persistentContactCase = em.find(ContactCase.class, contactCase.getId());
        this.updateRptContactChannel(persistentContactCase, true, "edit");

        Users users = JSFUtil.getUserSession().getUsers();
        Date dateNow = new Date();
        contactCase.setUsersUpdate(users);
        contactCase.setUpdateBy(JSFUtil.getUserSession().getUserName());
        contactCase.setUpdateDate(dateNow);
        
        String sql = "update ContactCase set "
                + "usersUpdate = :usersUpdate, "
                + "updateBy = :updateBy, "
                + "updateDate = :updateDate, "
                + "caseDetailId = :caseDetailId, "
                + "caseRequestId = :caseRequestId, "
                + "relationshipId = :relationshipId, "
                + "channelId = :channelId, "
                + "contactCase = :contactCase, "
                + "serviceType = :serviceType, "
                + "businessUnit = :businessUnit, "
                + "location = :location, "
                + "locationName = :locationName, "
                + "attachFile = :attachFile, "
                + "contactPerson = :contactPerson, "
                + "priority = :priority, "
                + "description = :description, "
                + "remark = :remark, "
                + "relatedSale = :relatedSale, ";
        if (contactCase.getStatus().equals(JSFUtil.getBundleValue("closedValue"))) {
               sql += "status = :status, "
                    + "usersClosed = :usersClosed, "
                    + "closedDate = :closedDate, ";
            }
        sql += "workflow = :workflow where id = :ccId";
        
        Query q = em.createQuery(sql);
        q.setParameter("usersUpdate", users);
        q.setParameter("updateBy", JSFUtil.getUserSession().getUserName());
        q.setParameter("updateDate", dateNow);
        q.setParameter("caseDetailId", contactCase.getCaseDetailId());
        q.setParameter("caseRequestId", contactCase.getCaseRequestId());
        q.setParameter("relationshipId", contactCase.getRelationshipId());
        q.setParameter("channelId", contactCase.getChannelId());
        q.setParameter("contactCase", contactCase.getContactCase());
        q.setParameter("serviceType", contactCase.getServiceType());
        q.setParameter("businessUnit", contactCase.getBusinessUnit());
        q.setParameter("location", contactCase.getLocation());
        q.setParameter("locationName", contactCase.getLocationName());
        q.setParameter("attachFile", contactCase.getAttachFile());
        q.setParameter("contactPerson", contactCase.getContactPerson());
        q.setParameter("priority", contactCase.getPriority());
        q.setParameter("description", contactCase.getDescription());
        q.setParameter("remark", contactCase.getRemark());
        q.setParameter("relatedSale", contactCase.getRelatedSale());
        if (contactCase.getStatus().equals(JSFUtil.getBundleValue("closedValue"))) {
            q.setParameter("status", contactCase.getStatus());
            q.setParameter("usersClosed", users);
            q.setParameter("closedDate", dateNow);
            contactCase.setUsersClosed(users);
            contactCase.setClosedDate(dateNow);
        }
        q.setParameter("workflow", contactCase.getWorkflow());
        q.setParameter("ccId", contactCase.getId());
        try{
            q.executeUpdate();
            if(contactCase.getCaseAttachmentCollection() != null) {
                for(CaseAttachment obj: contactCase.getCaseAttachmentCollection()) {
                    em.merge(obj);
                }
            }   
        }catch(Exception e){
            log.error(e);
        }

        //Update RptContactChannel Table
        this.updateRptContactChannel(contactCase, false, "edit");
    }

    public void updateContactHistory(ContactCase contactCase) {
        try{
            Query q = em.createQuery("update ContactCase set contactHistory = ?1 where id = ?2");
            q.setParameter(1, contactCase.getContactHistory());
            q.setParameter(2, contactCase.getId());

            q.executeUpdate();
        }catch(Exception e){
            log.error(e);
    }
    }

    public void updateRejectContactCase(ContactCase contactCase) {
        try{
            Query q = em.createQuery("update ContactCase set workflow = ?1, usersUpdate = ?2 ,updateBy = ?3, updateDate = ?4 where id = ?5");
            q.setParameter(1, contactCase.getWorkflow());
            q.setParameter(2, JSFUtil.getUserSession().getUsers());
            q.setParameter(3, JSFUtil.getUserSession().getUserName());
            q.setParameter(4, new Date());
            q.setParameter(5, contactCase.getId());

            q.executeUpdate();
        }catch(Exception e){
            log.error(e);
        }
    }
    
    public void updateDelegateContactCase(ContactCase contactCase) {
        try{
            Query q = em.createQuery("update ContactCase "
                    + "set workflowSeqNo = :workflowSeqNo, "
                    + "activity = :activity, "
                    + "activityDelegate = :activityDelegate, "
                    + "usersUpdate = :usersUpdate, "
                    + "updateBy = :updateBy, "
                    + "updateDate = :updateDate "
                    + "where id = :ccId");
            q.setParameter("workflowSeqNo", contactCase.getWorkflowSeqNo());
            q.setParameter("activity", contactCase.getActivity());
            q.setParameter("activityDelegate", contactCase.getActivityDelegate());
            q.setParameter("usersUpdate", JSFUtil.getUserSession().getUsers());
            q.setParameter("updateBy", JSFUtil.getUserSession().getUserName());
            q.setParameter("updateDate", new Date());
            q.setParameter("ccId", contactCase.getId());

            q.executeUpdate();
        }catch(Exception e){
            log.error(e);
        }
    }
    
    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        ContactCase contactCase;
        try {
            contactCase = em.getReference(ContactCase.class, id);
            contactCase.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The contactCase with id " + id + " no longer exists.", enfe);
        }
        em.remove(contactCase);
    }

    public List<ContactCase> findContactCaseEntities() {
        return findContactCaseEntities(true, -1, -1);
    }

    public List<ContactCase> findContactCaseEntities(int maxResults, int firstResult) {
        return findContactCaseEntities(false, maxResults, firstResult);
    }

    private List<ContactCase> findContactCaseEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ContactCase as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<ContactCase> findByContactHistoryId(Integer contactHistoryId) {
        Query q = em.createQuery("select object(o) from ContactCase as o where o.contactHistory.id = ?1 order by o.contactDate desc");
        q.setParameter(1, contactHistoryId);
        return q.getResultList();
    }

    public ContactCase findContactCase(Integer id) {
        return em.find(ContactCase.class, id);
    }
    
    public ContactCase findContactCaseByCode(String code) {
        Query q = em.createQuery("select object(o) from ContactCase as o where o.code = ?1");
        q.setParameter(1, code);
        return (ContactCase) q.getSingleResult();
    }

    public int getContactCaseCount() {
        Query q = em.createQuery("select count(o) from ContactCase as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    private void computeRptContactChannelByStatus(RptContactChannel rptContactChannel, ContactCase contactCase, int value) {
        int total, pending, closed, firstCall;

        if (JSFUtil.getBundleValue("pendingValue").equals(contactCase.getStatus())) {
            pending = rptContactChannel.getPending() + value;
            closed = rptContactChannel.getClosed();
            rptContactChannel.setPending(pending);
            rptContactChannel.setClosed(closed);
        } else if (JSFUtil.getBundleValue("closedValue").equals(contactCase.getStatus())) {
            pending = rptContactChannel.getPending();
            closed = rptContactChannel.getClosed() + value;
            rptContactChannel.setPending(pending);
            rptContactChannel.setClosed(closed);

            if (contactCase.getCreateDate() == contactCase.getClosedDate()) {
                firstCall = rptContactChannel.getFirstcallResolution() + value;
                rptContactChannel.setFirstcallResolution(firstCall);
            }

            Calendar c1 = Calendar.getInstance();
            c1.setTime(contactCase.getContactDate());
            Calendar c2 = Calendar.getInstance();
            c2.setTime(contactCase.getClosedDate());
            int slaClose1 = JSFUtil.getApplication().getSlaClose1();
            int slaClose2 = JSFUtil.getApplication().getSlaClose2();
            int slaClose3 = JSFUtil.getApplication().getSlaClose3();
            int slaClose4 = JSFUtil.getApplication().getSlaClose4();
            int slaClose5 = JSFUtil.getApplication().getSlaClose5();

            long closeHour = (long) ((c2.getTimeInMillis() - c1.getTimeInMillis()) / 1000.0 / 60 / 60);
            if (closeHour <= slaClose1) {
                rptContactChannel.setSlaClosed1(rptContactChannel.getSlaClosed1() + value);
            } else if (closeHour <= slaClose2 && slaClose2 != -1) {
                rptContactChannel.setSlaClosed2(rptContactChannel.getSlaClosed2() + value);
            } else if (closeHour <= slaClose3 && slaClose3 != -1) {
                rptContactChannel.setSlaClosed3(rptContactChannel.getSlaClosed3() + value);
            } else if (closeHour <= slaClose4 && slaClose4 != -1) {
                rptContactChannel.setSlaClosed4(rptContactChannel.getSlaClosed4() + value);
            } else if (closeHour <= slaClose5 && slaClose5 != -1) {
                rptContactChannel.setSlaClosed5(rptContactChannel.getSlaClosed5() + value);
            }
         
        }
        total = rptContactChannel.getTotal() + value;
        rptContactChannel.setTotal(total);
        rptContactChannel = em.merge(rptContactChannel);
    }

    public synchronized void updateRptContactChannel(ContactCase contactCase, boolean isRollback, String mode) {
        RptContactChannelPK pk = new RptContactChannelPK();
        pk.setContactDate(JSFUtil.toDateWithoutTime(contactCase.getContactDate()));
        
        if(mode.equals("create")) {
            pk.setUserId(JSFUtil.getUserSession().getUsers().getId());
        } else if(mode.equals("edit")) {
            pk.setUserId(contactCase.getUsersCreate().getId());
        }
        
        pk.setChannelId(contactCase.getChannelId().getId());
        pk.setCaseDetailId(contactCase.getCaseDetailId().getId());
        pk.setCaseRequestId(contactCase.getCaseRequestId().getId());
        pk.setServiceTypeId(contactCase.getServiceType().getId());
        pk.setLocationId(contactCase.getLocation().getId());

        RptContactChannel rptContactChannel = this.findRptContactChannel(pk);
        if (isRollback) {       //do only when edit case
            if (rptContactChannel == null) {
                log.error("rptContactChannel: Data not consistentcy when rollback");
            } else {
                computeRptContactChannelByStatus(rptContactChannel, contactCase, -1);
            }
            return;
        }

        if (rptContactChannel == null) { //if create first case in a day
            //insert record
//            rptContactChannel = new RptContactChannel(JSFUtil.toDateWithoutTime(contactCase.getContactDate()), contactCase.getUsersUpdate().getId(), contactCase.getChannelId().getId(), contactCase.getCaseDetailId().getId(), contactCase.getCaseRequestId().getId(), contactCase.getServiceType().getId(), contactCase.getLocation().getId());
            rptContactChannel = new RptContactChannel(JSFUtil.toDateWithoutTime(contactCase.getContactDate()), contactCase.getUsersCreate().getId(), contactCase.getChannelId().getId(), contactCase.getCaseDetailId().getId(), contactCase.getCaseRequestId().getId(), contactCase.getServiceType().getId(), contactCase.getLocation().getId());
            rptContactChannel.setTotal(1);
            if (contactCase.getClosedDate() != null) {
                rptContactChannel.setFirstcallResolution(1);
                rptContactChannel.setSlaClosed1(1); //within 24 hr
            } else {
                rptContactChannel.setFirstcallResolution(0);
            }

            if (JSFUtil.getBundleValue("pendingValue").equals(contactCase.getStatus())) {
                rptContactChannel.setPending(1);
                rptContactChannel.setClosed(0);
            } else if (JSFUtil.getBundleValue("closedValue").equals(contactCase.getStatus())) {
                rptContactChannel.setPending(0);
                rptContactChannel.setClosed(1);
            }
            em.persist(rptContactChannel);
        } else {
            //update record
            computeRptContactChannelByStatus(rptContactChannel, contactCase, 1);
        }
    }

    public RptContactChannel findRptContactChannel(RptContactChannelPK id) {
        return em.find(RptContactChannel.class, id);
    }

    public void createAutomaticClosedCase(ContactHistory contactHistoryParam) {
        if(contactHistoryParam.getId() != null) {
            ContactHistory contactHistory = em.find(ContactHistory.class, contactHistoryParam.getId());
            Collection<ContactHistoryKnowledge> contactHistoryKnowledges = contactHistory.getContactHistoryKnowledgeCollection();
            Collection<ContactHistoryProduct> contactHistoryProducts = contactHistory.getContactHistoryProductCollection();
            if (contactHistoryKnowledges == null && contactHistoryProducts == null) {
                return;
            }
            boolean kbuseful = false;
            if (contactHistoryKnowledges != null) {
                for (ContactHistoryKnowledge chk : contactHistoryKnowledges) {
                    if (chk.getUseful()) {
                        kbuseful = true;
                        break;
                    }
                }
            }
            boolean productuseful = false;
            if (contactHistoryProducts != null) {
                for (ContactHistoryProduct chp : contactHistoryProducts) {
                    if (chp.getUseful()) {
                        productuseful = true;
                        break;
                    }
                }
            }
            if (!kbuseful && !productuseful) {
                return;
            }

            StringBuilder txt = new StringBuilder();
            txt.append("Automatic Create Case\n");
            txt.append("User viewed following information:\n");

            //Rpt Knowledgebase
            if (kbuseful) {
                txt.append("Knowledge Base\n");
                for (ContactHistoryKnowledge chk : contactHistoryKnowledges) {
                    if (chk.getUseful()) {
                        txt.append("-");
                        txt.append(chk.getKnowledgebase().getTopic());
                        txt.append("\n");

                    }
                }
                txt.append("\n");
            }
            //Rpt Product
            if (productuseful) {
                txt.append("Product Information\n");
                for (ContactHistoryProduct chp : contactHistoryProducts) {
                    if (chp.getUseful()) {
                        txt.append("-");
                        txt.append(chp.getProduct().getName());
                        txt.append("\n");
                    }
                }
            }


            try {
                ConfigurationAutocase configurationAutocase = em.find(ConfigurationAutocase.class, 1); 
                CaseRequest caseRequest = em.find(CaseRequest.class, configurationAutocase.getCaseRequestId()); 
                CaseDetail caseDetail = caseRequest.getCaseDetailId();
                ServiceType serviceType = em.find(ServiceType.class, configurationAutocase.getServicetypeId());
                Location location = em.find(Location.class, configurationAutocase.getLocationId());

                ContactCase contactCase = new ContactCase();
                Integer customerId = JSFUtil.getUserSession().getCustomerDetail().getId();
                contactCase.setCaseRequestId(caseRequest);
                contactCase.setCaseDetailId(caseDetail); //********
                contactCase.setChannelId(new Channel(1));  //***********
                contactCase.setServiceType(serviceType);//*****
                contactCase.setBusinessUnit(location.getBusinessUnit());//******
                contactCase.setLocation(location);
                contactCase.setContactDate(contactHistory.getContactDate());
                contactCase.setDescription(txt.toString());
                contactCase.setCreateDate(new Date());
                contactCase.setCreateBy(JSFUtil.getUserSession().getUserName());
                contactCase.setUpdateBy(JSFUtil.getUserSession().getUserName());
                contactCase.setContactHistory(JSFUtil.getUserSession().getContactHistory());

                contactCase.setStatus(JSFUtil.getBundleValue("closedValue"));
                contactCase.setPriority(JSFUtil.getBundleValue("mediumValue"));
                contactCase.setCustomerId(new Customer(customerId));
                this.create(contactCase);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }
}
