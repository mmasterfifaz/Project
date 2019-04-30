/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.dao.front.search;

import com.maxelyz.core.model.value.front.customerHandling.ContactCaseInfoValue;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.Calendar;
import org.springframework.transaction.annotation.Transactional;

import com.maxelyz.core.model.value.front.search.SearchCaseValue;
import com.ntier.utils.ParameterMap;

/**
 *
 * @author Manop
 */
@Transactional
public class SearchCaseDAO {

    @PersistenceContext
    private EntityManager em;

    private List<ParameterMap> params;

    public List<ContactCaseInfoValue> searchCase() {
        return searchCase(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, Integer.MAX_VALUE);
    }

    public List<ContactCaseInfoValue> searchCase(
            String code, Date contactDateFrom, Date contactDateTo, Integer channelID, Integer caseTypeID, Integer caseTopicID, Integer caseDetailID, Integer caseRequestID,
            String description, String status, String createdBy, String customerName, String identityNo,
            String address, String phone, String gender, Integer customerType, Integer caseId, Integer customerId, Integer serviceTypeId,
            Integer activityTypeId, String delegateStatus,
            int firstResult, int maxResults
            ){

        String SQL_QUERY = generateSearchCaseSQLCmd(code, contactDateFrom, contactDateTo, channelID, caseTypeID, caseTopicID, caseDetailID, caseRequestID, description, status, createdBy, customerName, identityNo, address, phone, gender, customerType, caseId, customerId, serviceTypeId, activityTypeId, delegateStatus);
        Query query = em.createQuery(SQL_QUERY);

        for (ParameterMap parm:params) {
            query.setParameter(parm.getName(), parm.getValue());
        }

        if ( maxResults > 0 ) {
            query.setMaxResults(maxResults);
        }//if
        if ( firstResult >= 0 ) {
            query.setFirstResult(firstResult);
        }//if

        return (List<ContactCaseInfoValue>)query.getResultList();

    }

    public Integer searchCaseRowCount(
            String code, Date contactDateFrom, Date contactDateTo, Integer channelID, Integer caseTypeID, Integer caseTopicID, Integer caseDetailID, Integer caseRequestID,
            String description, String status, String createdBy, String customerName, String identityNo,
            String address, String phone, String gender, Integer customerType, Integer caseId, Integer customerId, Integer serviceTypeId, Integer activityTypeId, String delegateStatus
            ){
        String SQL_QUERY = generateSearchCaseSQLCmd(code, contactDateFrom, contactDateTo, channelID, caseTypeID, caseTopicID, caseDetailID, caseRequestID, description, status, createdBy, customerName, identityNo, address, phone, gender, customerType, caseId, customerId, serviceTypeId, activityTypeId, delegateStatus);
        int startPos = SQL_QUERY.indexOf("NEW");
        int endPos = SQL_QUERY.indexOf("FROM");
        String replacedStr = SQL_QUERY.substring(startPos, endPos);
        SQL_QUERY = SQL_QUERY.replace(replacedStr, " count(*) ");
        Query query = em.createQuery(SQL_QUERY);

        for (ParameterMap parm:params) {
            query.setParameter(parm.getName(), parm.getValue());
        }

        return ((Long)query.getSingleResult()).intValue();
    }

    private String generateSearchCaseSQLCmd(
            String code, Date contactDateFrom, Date contactDateTo, Integer channelID, Integer caseTypeID, Integer caseTopicID, Integer caseDetailID, Integer caseRequestID,
            String description, String status, String createdBy, String customerName, String identityNo,
            String address, String phone, String gender, Integer customerType, Integer caseId, Integer customerId, Integer serviceTypeId,
            Integer activityTypeId, String delegateStatus
            ){
         String SQL_SELECT = "select "
                + "NEW "
                + ContactCaseInfoValue.class.getName()
                + "( "
                + "cc.id, "
                + "cccn.name as channel, "
                + "cccn.type as channelType, "
                + "cty.name as caseType, "
                + "cc.contactDate, "
                + "cc.status, "
                + "cc.code, "
                + "cd.name as caseDetail, "
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
                + "rtrim(ltrim(c.name)) + ' ' + rtrim(ltrim(c.surname)) as customerName,"
                + "cc.serviceType.name as serviceTypeName, "
                + "us.name + ' ' + us.surname as assignTo, "
                + "cc.priority, "
                + "cc.attachFile, "
                + "c.id "                
                + ") ";
        String SQL_FROM = "FROM ContactCase as cc "
                + "left join cc.customerId as c "
                + "left join cc.caseRequestId as cr "
                + "left join cc.caseDetailId as cd "
                + "left join cd.caseTopicId as cto "
                + "left join cto.caseTypeId as cty "
                + "left join cc.channelId as cccn "
                + "left join cc.activity as av "
                + "left join av.activityTypeId as at "
                + "left join av.channelId as cn "
                + "left join av.userReceiverId as us "
                + "left join cc.contactHistory as ch "
                + "left join ch.channel as chcn "
                + "left join ch.contactResult as chcr "
                ;
        String SQL_WHERE = "WHERE 1 = 1 ";

        params = new ArrayList<ParameterMap>();
        ParameterMap param = new ParameterMap();

        if (code != null && code.toString().trim().length() > 0){
            code = String.format("%s%s%s", "%", code, "%");
            SQL_WHERE += "and cc.code like :code ";
            param = new ParameterMap();
            param.setName("code");
            param.setValue(code);
            params.add(param);
        }

        if (contactDateFrom != null){
            SQL_WHERE += "and cc.contactDate >= :contactDateFrom ";
            param = new ParameterMap();
            param.setName("contactDateFrom");
                        param.setValue(contactDateFrom);
            params.add(param);
        }

        if (contactDateTo != null){
            SQL_WHERE += "and cc.contactDate <= :contactDateTo ";
            param = new ParameterMap();
            param.setName("contactDateTo");

            Calendar date = Calendar.getInstance();
            date.setTime(contactDateTo);
            date.add(Calendar.DATE, 1);
            param.setValue(date.getTime());
            params.add(param);
        }

        if (channelID != null && channelID != 0){
            SQL_WHERE += "and cccn.id = :channelID ";
            param = new ParameterMap();
            param.setName("channelID");
            param.setValue(channelID);
            params.add(param);
        }

        if (caseTypeID != null && caseTypeID != 0){
            SQL_WHERE += "and cty.id = :caseTypeID ";
            param = new ParameterMap();
            param.setName("caseTypeID");
            param.setValue(caseTypeID);
            params.add(param);
        }

        if (caseTopicID != null && caseTopicID != 0){
            SQL_WHERE += "and cto.id = :caseTopicID ";
            param = new ParameterMap();
            param.setName("caseTopicID");
            param.setValue(caseTopicID);
            params.add(param);
        }
        
        if (caseDetailID != null && caseDetailID != 0){
            SQL_WHERE += "and cd.id = :caseDetailID ";
            param = new ParameterMap();
            param.setName("caseDetailID");
            param.setValue(caseDetailID);
            params.add(param);
        }
        
        if (caseRequestID != null && caseRequestID != 0){
            SQL_WHERE += "and cr.id = :caseRequestID ";
            param = new ParameterMap();
            param.setName("caseRequestID");
            param.setValue(caseRequestID);
            params.add(param);
        }
        
        if (description != null && description.toString().trim().length() > 0){
            description = String.format("%s%s%s", "%", description, "%");
            SQL_WHERE += "and cc.description like :description ";
            param = new ParameterMap();
            param.setName("description");
            param.setValue(description);
            params.add(param);
        }

        if (status != null && status.toString().trim().length() > 0){
            SQL_WHERE += "and cc.status = :status ";
            param = new ParameterMap();
            param.setName("status");
            param.setValue(status);
            params.add(param);
        }

        if (createdBy != null && createdBy.toString().trim().length() > 0){
            createdBy = String.format("%s%s%s", "%", createdBy, "%");
            SQL_WHERE += "and cc.createBy like :createdBy ";
            param = new ParameterMap();
            param.setName("createdBy");
            param.setValue(createdBy);
            params.add(param);
        }

        if (customerName != null && customerName.toString().trim().length() > 0){
            customerName = String.format("%s%s%s", "%", customerName, "%");
            SQL_WHERE += "and (c.name + ' ' + c.surname) like :customerName ";
            param = new ParameterMap();
            param.setName("customerName");
            param.setValue(customerName);
            params.add(param);
        }

        if (identityNo != null && identityNo.toString().trim().length() > 0){
            identityNo = String.format("%s%s%s", "%", identityNo, "%");
            SQL_WHERE += "and c.idno like :identityNo ";
            param = new ParameterMap();
            param.setName("identityNo");
            param.setValue(identityNo);
            params.add(param);
        }

        if (address != null && address.toString().trim().length() > 0){
            address = String.format("%s%s%s", "%", address, "%");
            SQL_WHERE += "and (c.currentAddressLine1 like :address or c.currentAddressLine2 like :address) ";
            param = new ParameterMap();
            param.setName("code");
            param.setValue(code);
            params.add(param);
        }

        if (phone != null && phone.toString().trim().length() > 0){
            phone = String.format("%s%s%s", "%", phone, "%");
            SQL_WHERE += "and (c.homePhone like :phone or c.officePhone like :phone or c.mobilePhone like :phone) ";
            param = new ParameterMap();
            param.setName("address");
            param.setValue(address);
            params.add(param);
        }

        if (gender != null && gender.toString().trim().length() > 0){
            SQL_WHERE += "and c.gender = :gender ";
            param = new ParameterMap();
            param.setName("gender");
            param.setValue(gender);
            params.add(param);
        }

        if (customerType != null && customerType != 0){
            SQL_WHERE += "and c.customerType.id = :customerType ";
            param = new ParameterMap();
            param.setName("customerType");
            param.setValue(customerType);
            params.add(param);
        }

        if (caseId != null){
            SQL_WHERE += "and cc.id = :caseId ";
            param = new ParameterMap();
            param.setName("caseId");
            param.setValue(caseId);
            params.add(param);
        }

        if (customerId != null){
            SQL_WHERE += "and c.id = :customerId ";
            param = new ParameterMap();
            param.setName("customerId");
            param.setValue(customerId);
            params.add(param);
        }

        if (serviceTypeId != null && serviceTypeId != 0){
            SQL_WHERE += "and cc.serviceType.id = :serviceTypeId ";
            param = new ParameterMap();
            param.setName("serviceTypeId");
            param.setValue(serviceTypeId);
            params.add(param);
        }

        if (activityTypeId != null && activityTypeId != 0){
            if(activityTypeId == 101){
                SQL_WHERE += "and cc.activity is null ";
            }else{
                SQL_WHERE += "and cc.activity.activityTypeId.id = :activityTypeId ";
                param = new ParameterMap();
                param.setName("activityTypeId");
                param.setValue(activityTypeId);
                params.add(param);
            }
        }

        if (delegateStatus != null && !delegateStatus.equals("")){
            SQL_WHERE += "and cc.activity.receiveStatus = :delegateStatus ";
            param = new ParameterMap();
            param.setName("delegateStatus");
            param.setValue(delegateStatus);
            params.add(param);
        }

        String SQL_GROUP = "";

        String SQL_HAVING = "";

        String SQL_ORDER = "";

        return SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
    }

    /**
     * @return the params
     */
    public List<ParameterMap> getParams() {
        return params;
    }

    /**
     * @param parms the params to set
     */
    public void setParms(List<ParameterMap> params) {
        this.params = params;
    }
}
