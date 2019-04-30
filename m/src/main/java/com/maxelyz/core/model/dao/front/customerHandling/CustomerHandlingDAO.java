/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.dao.front.customerHandling;

import com.maxelyz.core.model.entity.Customer;
import com.maxelyz.core.model.value.front.customerHandling.ActivityInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.ContactCaseInfoValue;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;

import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;

/**
 *
 * @author Manop
 */
public class CustomerHandlingDAO {

    @PersistenceContext
    private EntityManager em;

    public CustomerInfoValue findCustomerHandling(Integer customerId){
        return findCustomerHandling(customerId, null);
    }

    public CustomerInfoValue findCustomerInfo(Integer customerId) {
        /*
        String SQL_SELECT = "select "
                + "NEW "
                + CustomerInfoValue.class.getName()
                + "( c ) ";
        String SQL_FROM = "from Customer c ";
        if (customerId != null && !customerId.equals(0)){
            SQL_FROM += "where c.id = :customerId ";
        }

        String SQL_QUERY = SQL_SELECT + SQL_FROM;

        Query query = em.createQuery(SQL_QUERY);
        */
        String SQL_SELECT = "select * ";
        String SQL_FROM = "from customer c with(nolock) ";
        String SQL_WHERE = "";
        if (customerId != null && !customerId.equals(0)){
            SQL_WHERE += "where c.id = :customerId ";
        }

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE;

        Query query = em.createNativeQuery(SQL_QUERY, Customer.class);

        if (customerId != null && !customerId.equals(0)){
            query.setParameter("customerId", customerId);
        }
        
        //return (CustomerInfoValue) query.getSingleResult();
        return new CustomerInfoValue((Customer) query.getSingleResult());
    }


    public CustomerInfoValue findCustomerHandling(Integer customerId, String referenceNo) {
        String SQL_SELECT = "select "
                + "NEW "
                + CustomerInfoValue.class.getName()
                + "( "
                + "c.id, "
                + "c.referenceNo, "
                + "a.id as accountId, "
                + "a.name as accountName, "
                + "c.initial, "
                + "c.name, "
                + "c.surname, "
                + "t.id as cardTypeId, "
                + "t.name as cardType, "
                + "c.idno, "
                + "c.gender, "
                + "c.dob, "
                + "0, "  // creditLimit
                + "0, "  // creditTerm
                + "' ', "  // privilege
                + "c.weight, "
                + "c.height, "
                + "c.nationality, "
                + "c.occupation, "
                + "c.homePhone, "
                + "c.homeExt, "
                + "c.officePhone, "
                + "c.officeExt, "
                + "c.mobilePhone, "
                + "c.fax, "
                + "c.faxExt, "
                + "c.email, "
                + "c.customerType.id, "
                + "c.customerType.name, "
                + "c.currentFullname, "
                + "c.currentAddressLine1, "
                + "c.currentAddressLine2, "
                + "c.currentSubDistrict, "
                + "c.currentDistrictId,  "
                + "c.currentPostalCode, "
                
                + "c.homeFullname, "
                + "c.homeAddressLine1, "
                + "c.homeAddressLine2, "
                + "c.homeSubDistrict, "
                + "c.homeDistrictId,  "
                + "c.homePostalCode, "
                
                + "c.billingFullname, "
                + "c.billingAddressLine1, "
                + "c.billingAddressLine2, "
                + "c.billingSubDistrict, "
                + "c.billingDistrictId,  "
                + "c.billingPostalCode, "
                               
                + "c.shippingFullname, "
                + "c.shippingAddressLine1, "
                + "c.shippingAddressLine2, "
                + "c.shippingSubDistrict, "
                + "c.shippingDistrictId,  "
                + "c.shippingPostalCode, "
                
                + "c.picture, "
                + "c.emoicon, "
                + "c.remark, "
                + "(select count(*) from c.contactCaseCollection cc) as totalCases, "
                + "(select count(*) from c.contactCaseCollection cc where cc.status = 'pending') as pendingCase, "
                + "(select count(*) from c.contactCaseCollection cc where cc.status = 'closed') as closedCase, "
                + "c.flexfield1, "
                + "c.flexfield2, "
                + "c.flexfield3, "
                + "c.flexfield4, "
                + "c.flexfield5, "
                + "c.flexfield6, "
                + "c.flexfield7, "
                + "c.flexfield8, "
                + "c.flexfield9, "
                + "c.flexfield10, "
                + "c.flexfield11, "
                + "c.flexfield12, "
                + "c.flexfield13, "
                + "c.flexfield14, "
                + "c.flexfield15, "
                + "c.flexfield16, "
                + "c.flexfield17, "
                + "c.flexfield18, "
                + "c.flexfield19, "
                + "c.flexfield20, "
                + "c.flexfield21, "
                + "c.flexfield22, "
                + "c.flexfield23, "
                + "c.flexfield24, "
                + "c.flexfield25, "
                + "c.flexfield26, "
                + "c.flexfield27, "
                + "c.flexfield28, "
                + "c.flexfield29, "
                + "c.flexfield30, "
                + "c.flexfield31, "
                + "c.flexfield32, "
                + "c.flexfield33, "
                + "c.flexfield34, "
                + "c.flexfield35, "
                + "c.flexfield36, "
                + "c.flexfield37, "
                + "c.flexfield38, "
                + "c.flexfield39, "
                + "c.flexfield40, "
                + "c.flexfield41, "
                + "c.flexfield42, "
                + "c.flexfield43, "
                + "c.flexfield44, "
                + "c.flexfield45, "
                + "c.flexfield46, "
                + "c.flexfield47, "
                + "c.flexfield48, "
                + "c.flexfield49, "
                + "c.flexfield50, "
                + "c.flexfield51, "
                + "c.flexfield52, "
                + "c.flexfield53, "
                + "c.flexfield54, "
                + "c.flexfield55, "
                + "c.flexfield56, "
                + "c.flexfield57, "
                + "c.flexfield58, "
                + "c.flexfield59, "
                + "c.flexfield60, "
                + "c.flexfield61, "
                + "c.flexfield62, "
                + "c.flexfield63, "
                + "c.flexfield64, "
                + "c.flexfield65, "
                + "c.flexfield66, "
                + "c.flexfield67, "
                + "c.flexfield68, "
                + "c.flexfield69, "
                + "c.flexfield70, "
                + "c.flexfield71, "
                + "c.flexfield72, "
                + "c.flexfield73, "
                + "c.flexfield74, "
                + "c.flexfield75, "
                + "c.flexfield76, "
                + "c.flexfield77, "
                + "c.flexfield78, "
                + "c.flexfield79, "
                + "c.flexfield80, "
                + "c.flexfield81, "
                + "c.flexfield82, "
                + "c.flexfield83, "
                + "c.flexfield84, "
                + "c.flexfield85, "
                + "c.flexfield86, "
                + "c.flexfield87, "
                + "c.flexfield88, "
                + "c.flexfield89, "
                + "c.flexfield90, "
                + "c.flexfield91, "
                + "c.flexfield92, "
                + "c.flexfield93, "
                + "c.flexfield94, "
                + "c.flexfield95, "
                + "c.flexfield96, "
                + "c.flexfield97, "
                + "c.flexfield98, "
                + "c.flexfield99, "
                + "c.flexfield100, "
                
                + "c.contactPhone1, "
                + "c.contactExt1, "
                + "c.contactPhone2, "
                + "c.contactExt2, "
                + "c.contactPhone3, "
                + "c.contactExt3, "
                + "c.contactPhone4, "
                + "c.contactExt4, "
                + "c.contactPhone5, "
                + "c.contactExt5, "
                
                + "c.telephoneDefault, "
                + "c.homePhoneClose, "
                + "c.officePhoneClose, "
                + "c.mobilePhoneClose, "
                + "c.contactPhone1Close, "
                + "c.contactPhone2Close, "
                + "c.contactPhone3Close, "
                + "c.contactPhone4Close, "
                + "c.contactPhone5Close  "
                + ") ";
        String SQL_FROM = "from Customer c "
                + "left join c.currentSubDistrict currentSubdistrict "
                + "left join c.currentDistrictId currentDistrict "
                + "left join c.homeSubDistrict homeSubdistrict "
                + "left join c.homeDistrictId homeDistrict "
                + "left join c.shippingSubDistrict shippingSubdistrict "
                + "left join c.shippingDistrictId shippingDistrict "           
                + "left join c.billingSubDistrict billingSubdistrict "
                + "left join c.billingDistrictId billingDistrict "
                + "left join c.idcardTypeId t "
                + "left join c.accountId a "
                + "left join c.customerType ct "
                + "";
        String SQL_WHERE = "where 1 = 1 ";

        if (customerId != null && !customerId.equals(0)){
            SQL_WHERE += "and c.id = :customerId ";
        }

         if (referenceNo != null && referenceNo.trim().length() > 0){
            SQL_WHERE += "and c.referenceNo = :referenceNo ";
        }

        String SQL_GROUP = "";

        String SQL_HAVING = "";

        String SQL_ORDER = "";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;

        Query query = em.createQuery(SQL_QUERY);

        if (customerId != null && !customerId.equals(0)){
            query.setParameter("customerId", customerId);
        }
        
        if (referenceNo != null && referenceNo.trim().length() > 0){
            query.setParameter("referenceNo", referenceNo);
        }

        return (CustomerInfoValue) query.getSingleResult();
    }

//    public List<ContactCaseInfoValue> findContactCase(Integer customerId){
//        List<ContactCaseInfoValue> list = null;
//        try{
//            String SQL_SELECT = "select "
//                    + "NEW "
//                    + ContactCaseInfoValue.class.getName()
//                    + "( "
//                    + "cc.id, "
//                    + "cc.channelId.name as channel, "
//                    + "cc.channelId.type as channelType, "
//                    + "ct.name as caseType, "
//                    + "cc.contactDate, "
//                    + "cc.status, "
//                    + "cc.code, "
//                    + "cc.caseDetailId.name as caseDetail, "
//                    + "at.name as activityTypeName, "
//                    + "av.description as description, "
//                    + "av.status as status, "
//                    + "concat(cn.name, ':', cn.type) as channelName "
//                    + ") ";
//            String SQL_FROM = "from ContactCase as cc "
//                    + "left join cc.activity as av "
//                    + "left join av.activityTypeId as at "
//                    + "left join av.channelId as cn "
//                    + "left join cc.caseDetailId as cd "
//                    + "left join cd.caseTopicId as cto "
//                    + "left join cto.caseTypeId as ct "
//                    + "";
//            String SQL_WHERE = "where cc.customerId.id = :customerId "
//                    + " ";
//
//            String SQL_GROUP = "";
//
//            String SQL_HAVING = "";
//
//            String SQL_ORDER = "order by cc.createDate desc";
//
//            String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
//            Query query = em.createQuery(SQL_QUERY);
//
//            query.setParameter("customerId", customerId);
//        
//            list = (List<ContactCaseInfoValue>)query.getResultList();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//       return list;
//    }

    public List<ContactCaseInfoValue> findContactCaseByNoRec(Integer customerId, Integer noRec){
         String SQL_SELECT = "select "
                + "NEW "
                + ContactCaseInfoValue.class.getName()
                + "( "
                + "cc.id, "
                + "cc.channelId.name as channel, "
                + "cc.channelId.type as channelType, "
                + "ct.name as caseType, "
                + "cc.contactDate, "
                + "cc.status, "
                + "cc.code, "
                + "cc.caseDetailId.name as caseDetail, "
                + "at.name as activityTypeName, "
                + "av.description as description, "
                + "av.status as status, "
                + "concat(cn.name, ':', cn.type) as channelName, "
                + "cc.serviceType.id as serviceTypeId, "
                + "cc.businessUnit.id as businessUnitId, "
                + "cc.location.id as locationId, "
                + "ch.contactDate, "
                + "ch.channel.name, "
                + "chcn.type, "
                + "chcr.name, "
                + "ch.createBy, "
                + "ch.contactTo, "  
                + "'', "
                + "cc.serviceType.name as serviceTypeName, "
                + "'', "
                + "cc.priority, "
                + "cc.attachFile, "
                + "cc.customerId.id "  
                + ") ";
        String SQL_FROM = "from ContactCase as cc "
                + "left join cc.activity as av "
                + "left join av.activityTypeId as at "
                + "left join av.channelId as cn "
                + "left join cc.caseDetailId as cd "
                + "left join cd.caseTopicId as cto "
                + "left join cto.caseTypeId as ct "
                + "left join cc.contactHistory as ch "
                + "left join ch.channel as chcn "
                + "left join ch.contactResult as chcr "
                + "";
        String SQL_WHERE = "where cc.customerId.id = :customerId "
                + " ";

        String SQL_GROUP = "";

        String SQL_HAVING = "";

        String SQL_ORDER = "order by cc.createDate desc";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
        Query query = em.createQuery(SQL_QUERY);
        
        query.setParameter("customerId", customerId);
        if(noRec != 0){
            query.setMaxResults(noRec);
        }

       return (List<ContactCaseInfoValue>)query.getResultList();
    }

    public List<ContactCaseInfoValue> findContactCaseByContactHistory(Integer customerId){
        String SQL_SELECT = "select "
                + "NEW "
                + ContactCaseInfoValue.class.getName()
                + "( "
                + "cc.id, "
                + "cc.channelId.name as channel, "
                + "cc.channelId.type as channelType, "
                + "ct.name as caseType, "
                + "cc.contactDate, "
                + "cc.status, "
                + "cc.code, "
                + "cc.caseDetailId.name as caseDetail, "
                + "at.name as activityTypeName, "
                + "av.description as description, "
                + "av.status as status, "
                + "concat(cn.name, ':', cn.type) as channelName, "
                + "cc.serviceType.id as serviceTypeId, "
                + "cc.businessUnit.id as businessUnitId, "
                + "cc.location.id as locationId, "
                + "ch.contactDate, "
                + "ch.channel.name, "
                + "chcn.type, "
                + "chcr.name, "
                + "ch.createBy, "
                + "ch.contactTo,"
                + "'',"
                + "'',"
                + "'' "  
                + ") ";
        String SQL_FROM = "from ContactCase as cc "
                + "left join cc.activity as av "
                + "left join av.activityTypeId as at "
                + "left join av.channelId as cn "
                + "left join cc.caseDetailId as cd "
                + "left join cd.caseTopicId as cto "
                + "left join cto.caseTypeId as ct "
                + "left join cc.contactHistory as ch "
                + "left join ch.channel as chcn "
                + "left join ch.contactResult as chcr "
                + "";
        String SQL_WHERE = " where cc.customerId.id = :customerId"
                + " ";

        String SQL_GROUP = "";

        String SQL_HAVING = "";

        String SQL_ORDER = " order by cc.contactDate desc";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
        Query query = em.createQuery(SQL_QUERY);

        query.setParameter("customerId", customerId);
        query.setMaxResults(3);

       return (List<ContactCaseInfoValue>)query.getResultList();
    }

    public List<ActivityInfoValue> findActivity(Integer caseId){
         String SQL_SELECT = "select "
                + "NEW "
                + ActivityInfoValue.class.getName()
                + "( "
                + "a.id, "
                + "a.activityDate, "
                + "a.activityTypeId.name as activityType, "
                + "a.description, "
                + "a.receiveStatus, "
                + "a.remark, "
                + "a.status, "
                + "concat(a.channelId.type,':',a.channelId.name) as channel, "
                + "a.channelId.type as channelType, "
                + "a.dueDate, "
                + "a.createBy, "
                + "CONCAT(u.name, ' ', u.surname) as userReceiverName, "
                + "cc.id as contactCaseId, "
                + "cc.code as contactCaseCode, "
                + "a.attachFile, "
                + "a.assignTo as userAssignTo "
                + ") ";
        String SQL_FROM = "from ContactCase as cc join cc.activityCollection as a "
                + "left join a.userReceiverId u ";
        String SQL_WHERE = "where cc.id = :caseId and (a.activityTypeId.system = false or a.activityTypeId.system is null)"
                + " ";

        String SQL_GROUP = "";

        String SQL_HAVING = "";

        String SQL_ORDER = "order by a.createDate desc";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
        Query query = em.createQuery(SQL_QUERY);

        query.setParameter("caseId", caseId);
       return (List<ActivityInfoValue>)query.getResultList();
    }

    public ContactCaseInfoValue findContactCaseByPK(Integer contactCaseId){
         String SQL_SELECT = "select "
                + "NEW "
                + ContactCaseInfoValue.class.getName()
                + "( "
                + "cc.id, "
                + "cc.code, "
                + "cc.contactDate, "
                + "cc.contactPerson, "
                + "cc.createBy, "
                + "cc.createDate, "
                + "cc.description, "
                + "cc.priority, "
                + "cc.relationshipId, "
                + "cc.remark, "
                + "cc.scheduleDate, "
                + "cc.scheduleDescription, "
                + "cc.status, "
                + "cc.updateBy, "
                + "cc.updateDate, "
                + "cd.id as caseDetailId, "
                + "cd.name as caseDetail, "
                + "cr.id as caseRequestId, "
                + "cr.name as caseRequest, "
                + "cto.id as caseTopicId, "
                + "cto.name as caseTopic, "
                + "cty.id as caseTypeId, "
                + "cty.name as caseType, "
                + "ch.id as channelId, "
                + "ch.name as channel, "
                + "ch.type as channelType, "
                + "c.id as customerId, "
                + "CONCAT(c.initial, c.name, ' ', c.surname) as customerName, "
                + "ccr.id as refContactCaseId, "
                + "ccr.code as refContactCaseCode "
                + ") ";
        String SQL_FROM = "from ContactCase cc "
                + "join cc.customerId as c "
                + "join cc.channelId as ch "
                + "join cc.caseDetailId as cd "
                + "join cd.caseTopicId as cto "
                + "join cto.caseTypeId as cty "
                + "left join cc.contactCaseCollection as ccr "
                + "left join cc.caseRequestId as cr "
                + "";
        String SQL_WHERE = "where cc.id = :contactCaseId "
                + " ";

        String SQL_GROUP = "";

        String SQL_HAVING = "";

        String SQL_ORDER = "";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
        Query query = em.createQuery(SQL_QUERY);

        query.setParameter("contactCaseId", contactCaseId);

       return (ContactCaseInfoValue)query.getSingleResult();
    }

    public ActivityInfoValue findActivityByPK(Integer activityId){
         String SQL_SELECT = "select "
                + "NEW "
                + ActivityInfoValue.class.getName()
                + "( "
                + "a.id, "
                + "a.activityDate, "
                + "a.createBy, "
                + "a.createDate, "
                + "a.description, "
                + "a.dueDate, "
                + "a.receiveStatus, "
                + "a.status, "
                + "a.updateBy, "
                + "a.updateDate, "
                + "a.userReceiverId.id as receiverId, "
                + "CONCAT(a.userReceiverId.name, ' ', a.userReceiverId.surname) as userReceiverName, "
                + "a.userSenderId.id as senderId, "
                + "CONCAT(a.userSenderId.name, ' ', a.userSenderId.surname) as userSenderName, "
                + "a.activityTypeId.id as activityTypeId, "
                + "a.activityTypeId.name as activityType, "
                + "a.channelId.id as channelId, "
                + "a.channelId.name as channel, "
                + "a.channelId.type as channelType, "
                + "a.contactCaseId.id as contactCaseId, "
                + "a.contactCaseId.code as contactCaseCode "
                + ") ";
        String SQL_FROM = "from Activity a "
                + "";
        String SQL_WHERE = "where a.id = :activityId "
                + " ";

        String SQL_GROUP = "";

        String SQL_HAVING = "";

        String SQL_ORDER = "";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
        Query query = em.createQuery(SQL_QUERY);

        query.setParameter("activityId", activityId);

       return (ActivityInfoValue)query.getSingleResult();
    }

    public List<Object> findCampaignProductList(Integer customerId) {
        String SQL_SELECT = "select c.name as campaign_name"
                + ", p.name as product_name";

        String SQL_FROM = " from Assignment a"
                + " left join a.campaign c"
                + " left join a.assignmentDetailCollection ad"
                + " left join a.assignmentProductCollection ap"
                + " left join ap.product p";
        String SQL_WHERE = " where ad.customerId.id = :customerId"
                + "";

        String SQL_GROUP = " group by c.name, p.name";

        String SQL_HAVING = "";

        String SQL_ORDER = " order by c.name, p.name ";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
        Query query = em.createQuery(SQL_QUERY);

        query.setParameter("customerId", customerId);
        query.setMaxResults(5);
       return (List<Object>)query.getResultList();
    }
    
      public CustomerInfoValue findCustomerHandlingByReferenceNo(String referenceNo) {
        String SQL_SELECT = "select "
                + "NEW "
                + CustomerInfoValue.class.getName()
                + "( "
                + "c.id, "
                + "(select count(*) from c.contactCaseCollection cc) as totalCases, "
                + "(select count(*) from c.contactCaseCollection cc where cc.status = 'pending') as pendingCase, "
                + "(select count(*) from c.contactCaseCollection cc where cc.status = 'closed') as closedCase "
                + ") ";
        String SQL_FROM = "from Customer c ";
        String SQL_WHERE = "where 1 = 1 ";

         if (referenceNo != null && referenceNo.trim().length() > 0){
            SQL_WHERE += "and c.referenceNo = :referenceNo ";
        }

        String SQL_GROUP = "";

        String SQL_HAVING = "";

        String SQL_ORDER = "";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
        Query query = em.createQuery(SQL_QUERY);

        if (referenceNo != null && referenceNo.trim().length() > 0){
            query.setParameter("referenceNo", referenceNo);
        }
         query.setMaxResults(1);

        return (CustomerInfoValue) query.getSingleResult();
    }

    public List<ContactCaseInfoValue> findContactCaseByContactHistoryId(Integer contactHistoryId){
        /*
         String SQL_SELECT = "select "
                + "NEW "
                + ContactCaseInfoValue.class.getName()
                + "( "
                + "cc.id, "
                + "cc.channelId.name as channel, "
                + "cc.channelId.type as channelType, "
                + "ct.name as caseType, "
                + "cc.contactDate, "
                + "cc.status, "
                + "cc.code, "
                + "cc.caseDetailId.name as caseDetail, "
                + "at.name as activityTypeName, "
                + "cc.description as description, "
                + "av.status as status, "
                + "concat(cn.name, ':', cn.type) as channelName, "
                + "cc.serviceType.id as serviceTypeId, "
                + "cc.businessUnit.id as businessUnitId, "
                + "cc.location.id as locationId, "
                + "ch.contactDate, "
                + "ch.channel.name, "
                + "chcn.type, "
                + "chcr.name, "
                + "ch.createBy, "
                + "ch.contactTo, "  
                + "'',"
                + "cc.serviceType.name as serviceTypeName, "
                + "'' , "
                + "cc.contactPerson as contactPerson, "
                + "cd.name as caseTopic"  
                + ") ";*/
                String SQL_SELECT = "select "
                + "NEW "
                + ContactCaseInfoValue.class.getName()
                + "( "
                + "cc.id, "
                + "cc.channelId.name as channel, "
                + "cc.channelId.type as channelType, "
                + "ct.name as caseType, "
                + "cc.contactDate, "
                + "cc.status, "
                + "cc.code, "
                + "cc.caseDetailId.name as caseDetail, "
                + "at.name as activityTypeName, "
                + "av.description as description, "
                + "av.status as status, "
                + "concat(cn.name, ':', cn.type) as channelName, "
                + "cc.serviceType.id as serviceTypeId, "
                + "cc.businessUnit.id as businessUnitId, "
                + "cc.location.id as locationId, "
                + "ch.contactDate, "
                + "ch.channel.name, "
                + "chcn.type, "
                + "chcr.name, "
                + "ch.createBy, "
                + "ch.contactTo,"
                + "'',"
                + "cc.serviceType.name as serviceTypeName,"
                + "'' "  
                + ") ";
        String SQL_FROM = "from ContactCase as cc "
                + "left join cc.activity as av "
                + "left join av.activityTypeId as at "
                + "left join av.channelId as cn "
                + "left join cc.caseDetailId as cd "
                + "left join cd.caseTopicId as cto "
                + "left join cto.caseTypeId as ct "
                + "left join cc.contactHistory as ch "
                + "left join ch.channel as chcn "
                + "left join ch.contactResult as chcr "
                + "";
        String SQL_WHERE = "where cc.contactHistory.id = :contactHistoryId "
                + " ";
        
        String SQL_GROUP = "";

        String SQL_HAVING = "";

        String SQL_ORDER = "order by cc.createDate desc";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
        Query query = em.createQuery(SQL_QUERY);

        query.setParameter("contactHistoryId", contactHistoryId);

       return (List<ContactCaseInfoValue>)query.getResultList();
    }

      
}
