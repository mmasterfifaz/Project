/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao.front.search;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import com.maxelyz.core.model.value.front.search.SearchCustomerValue;
import com.ntier.utils.ParameterMap;
import java.util.Calendar;

/**
 *
 * @author Manop
 */
@Transactional
public class SearchCustomerDAO {

    @PersistenceContext
    private EntityManager em;
    private List<ParameterMap> params;

    public List<SearchCustomerValue> searchCustomer() {
        return searchCustomer(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, Integer.MAX_VALUE);
    }

    public List<SearchCustomerValue> searchCustomer(
            String customerName, String identityNo, String address, String phone, String gender, Integer customerType,
            String accountNo, String code, Date contactDateFrom, Date contactDateTo, Integer channelID, Integer caseTypeID,
            String priority, String description, String status, String createdBy, String carBrand, String carModel, Integer carFromYear, 
            Integer carToYear, String carGroup, String carNo, Integer serviceTypeId, int firstResult, int maxResults) {

        String SQL_QUERY = generateSearchCustomerSQLCmd(customerName, identityNo, address, phone, gender, customerType, accountNo, code, contactDateFrom, contactDateTo, channelID, caseTypeID, priority, description, status, createdBy, carBrand, carModel, carFromYear, carToYear, carGroup, carNo, serviceTypeId);
        Query query = em.createQuery(SQL_QUERY);

        for (ParameterMap parm : params) {
            query.setParameter(parm.getName(), parm.getValue());
        }

        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }//if
        if (firstResult >= 0) {
            query.setFirstResult(firstResult);
        }//if

        return (List<SearchCustomerValue>) query.getResultList();

    }

    public Integer searchCustomereRowCount(
            String customerName, String identityNo, String address, String phone, String gender, Integer customerType,
            String accountNo, String code, Date contactDateFrom, Date contactDateTo, Integer channelID, Integer caseTypeID,
            String priority, String description, String status, String createdBy) {
        String SQL_QUERY = generateSearchCustomerSQLCmdCount(customerName, identityNo, address, phone, gender, customerType, accountNo, code, contactDateFrom, contactDateTo, channelID, caseTypeID, priority, description, status, createdBy);
        Query query = em.createQuery(SQL_QUERY);

        for (ParameterMap parm : params) {
            query.setParameter(parm.getName(), parm.getValue());
        }

        return query.getResultList().size();
    }

    private String generateSearchCustomerSQLCmd(
            String customerName, String identityNo, String address, String phone, String gender, Integer customerType,
            String accountNo, String code, Date contactDateFrom, Date contactDateTo, Integer channelID, Integer caseTypeID,
            String priority, String description, String status, String createdBy, String carBrand, String carModel, 
            Integer carFromYear, Integer carToYear, String carGroup, String carNo, Integer serviceTypeId) {
        String SQL_SELECT = "SELECT "
                + "NEW "
                + SearchCustomerValue.class.getName()
                + "( "
                + "c.id as customerId, "
                + "coalesce(c.initial,'') + c.name + ' ' + coalesce(c.surname,'') as customerName, "
                + "c.homePhone + ', ' + c.officePhone + ', ' + c.mobilePhone as phone, "
                + "ct.name as customerType,"
                + "(select count(*) from c.contactCaseCollection cc where cc.status = 'pending') as pendingCase, " //CASE WHEN cc.status = 'pending' THEN 1 ELSE 0 END
                + "(select count(*) from c.contactCaseCollection cc where cc.status = 'closed') as closedCase " //CASE WHEN cc.status = 'closed' THEN 1 ELSE 0 END
                + ", c.carBrand as carBrand"
                + ", c.carModel"
                + ", c.carYear"
                + ", c.carCharacterGroup"
                + ", c.carNumber"
                + ") ";
        String SQL_FROM = "FROM Customer c "
                + "left join c.contactCaseCollection as cc "
                + "join c.customerType as ct "
                + "left join cc.caseDetailId as cd "
                + "left join cd.caseTopicId as ctp "
                + "left join ctp.caseTypeId as cty "
                + "left join c.accountId as acc ";
        String SQL_WHERE = "WHERE 1 = 1 ";

        params = new ArrayList<ParameterMap>();
        ParameterMap param = new ParameterMap();

        if (customerName != null && customerName.toString().trim().length() > 0) {
            customerName = String.format("%s%s%s", "%", customerName, "%");
            SQL_WHERE += "and (c.name + ' ' + c.surname) like :customerName ";
            param = new ParameterMap();
            param.setName("customerName");
            param.setValue(customerName);
            params.add(param);
        }

        if (identityNo != null && identityNo.toString().trim().length() > 0) {
            identityNo = String.format("%s%s%s", "%", identityNo, "%");
            SQL_WHERE += "and c.idno like :identityNo ";
            param = new ParameterMap();
            param.setName("identityNo");
            param.setValue(identityNo);
            params.add(param);
        }

        if (address != null && address.toString().trim().length() > 0) {
            address = String.format("%s%s%s", "%", address, "%");
            SQL_WHERE += "and (c.currentAddressLine1 like :address or c.currentAddressLine2 like :address) ";
            param = new ParameterMap();
            param.setName("address");
            param.setValue(address);
            params.add(param);
        }

        if (phone != null && phone.toString().trim().length() > 0) {
            phone = String.format("%s%s%s", "%", phone, "%");
            SQL_WHERE += "and (c.homePhone like :phone or c.officePhone like :phone or c.mobilePhone like :phone) ";
            param = new ParameterMap();
            param.setName("phone");
            param.setValue(phone);
            params.add(param);
        }

        if (gender != null && gender.toString().trim().length() > 0) {
            SQL_WHERE += "and c.gender = :gender ";
            param = new ParameterMap();
            param.setName("gender");
            param.setValue(gender);
            params.add(param);
        }

        if (customerType != null && customerType != 0) {
            SQL_WHERE += "and c.customerType.id = :customerType ";
            param = new ParameterMap();
            param.setName("customerType");
            param.setValue(customerType);
            params.add(param);
        }

        if (accountNo != null && accountNo.toString().trim().length() > 0) {
            accountNo = String.format("%s%s%s", "%", accountNo, "%");
            SQL_WHERE += "and acc.name like :accountNo ";
            param = new ParameterMap();
            param.setName("accountNo");
            param.setValue(accountNo);
            params.add(param);
        }

        if (code != null && code.toString().trim().length() > 0) {
            code = String.format("%s%s%s", "%", code, "%");
            SQL_WHERE += "and cc.code like :code ";
            param = new ParameterMap();
            param.setName("code");
            param.setValue(code);
            params.add(param);
        }

        if (contactDateFrom != null) {
            SQL_WHERE += "and cc.contactDate >= :contactDateFrom ";
            param = new ParameterMap();
            param.setName("contactDateFrom");
            param.setValue(contactDateFrom);
            params.add(param);
        }

        if (contactDateTo != null) {
            SQL_WHERE += "and cc.contactDate <= :contactDateTo ";
            param = new ParameterMap();
            param.setName("contactDateTo");

            Calendar date = Calendar.getInstance();
            date.setTime(contactDateTo);
            date.add(Calendar.DATE, 1);
            param.setValue(date.getTime());
            params.add(param);
        }

        if (channelID != null && channelID != 0) {
            SQL_WHERE += "and cc.channelId.id = :channelID ";
            param = new ParameterMap();
            param.setName("channelID");
            param.setValue(channelID);
            params.add(param);
        }

        if (serviceTypeId != null && serviceTypeId != 0){
            SQL_WHERE += "and cc.serviceType.id = :serviceTypeId ";
            param = new ParameterMap();
            param.setName("serviceTypeId");
            param.setValue(serviceTypeId);
            params.add(param);
        }
        
        if (caseTypeID != null && caseTypeID != 0) {
            SQL_WHERE += "and cty.id = :caseTypeID ";
            param = new ParameterMap();
            param.setName("caseTypeID");
            param.setValue(caseTypeID);
            params.add(param);
        }

        if (priority != null && priority.toString().trim().length() > 0) {
            SQL_WHERE += "and cc.priority = :priority ";
            param = new ParameterMap();
            param.setName("priority");
            param.setValue(priority);
            params.add(param);
        }

        if (description != null && description.toString().trim().length() > 0) {
            description = String.format("%s%s%s", "%", description, "%");
            SQL_WHERE += "and cc.description like :description ";
            param = new ParameterMap();
            param.setName("description");
            param.setValue(description);
            params.add(param);
        }

        if (status != null && status.toString().trim().length() > 0) {
            SQL_WHERE += "and cc.status = :status ";
            param = new ParameterMap();
            param.setName("status");
            param.setValue(status);
            params.add(param);
        }

        if (createdBy != null && createdBy.toString().trim().length() > 0) {
            createdBy = String.format("%s%s%s", "%", createdBy, "%");
            SQL_WHERE += "and cc.createBy like :createdBy ";
            param = new ParameterMap();
            param.setName("createdBy");
            param.setValue(createdBy);
            params.add(param);
        }

        if (carBrand != null && carBrand.toString().trim().length() > 0) {
            carBrand = String.format("%s%s%s", "%", carBrand, "%");
            SQL_WHERE += "and c.carBrand like :carBrand ";
            param = new ParameterMap();
            param.setName("carBrand");
            param.setValue(carBrand);
            params.add(param);
        }

        if (carModel != null && carModel.toString().trim().length() > 0) {
            carModel = String.format("%s%s%s", "%", carModel, "%");
            SQL_WHERE += "and c.carModel like :carModel ";
            param = new ParameterMap();
            param.setName("carModel");
            param.setValue(carModel);
            params.add(param);
        }

        if (carFromYear != null && carFromYear != 0) {
            SQL_WHERE += "and c.carYear >= :carFromYear ";
            param = new ParameterMap();
            param.setName("carFromYear");
            param.setValue(carFromYear);
            params.add(param);
        }

        if (carToYear != null && carToYear != 0) {
            SQL_WHERE += "and c.carYear <= :carToYear ";
            param = new ParameterMap();
            param.setName("carToYear");
            param.setValue(carToYear);
            params.add(param);
        }

        if (carGroup != null && carGroup.toString().trim().length() > 0) {
            carGroup = String.format("%s%s%s", "%", carGroup, "%");
            SQL_WHERE += "and c.carCharacterGroup like :carGroup ";
            param = new ParameterMap();
            param.setName("carGroup");
            param.setValue(carGroup);
            params.add(param);
        }

        if (carNo != null && carNo.toString().trim().length() > 0) {
            carNo = String.format("%s%s%s", "%", carNo, "%");
            SQL_WHERE += "and c.carNumber like :carNo ";
            param = new ParameterMap();
            param.setName("carNo");
            param.setValue(carNo);
            params.add(param);
        }

        String SQL_GROUP = "GROUP BY c.id, c.initial, c.name, c.surname, c.homePhone, c.officePhone, c.mobilePhone, ct.name, c.carBrand, c.carModel, c.carYear, c.carCharacterGroup, c.carNumber ";

        String SQL_HAVING = "";

        String SQL_ORDER = " ORDER BY c.name, c.surname ";

        return SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
    }

    private String generateSearchCustomerSQLCmdCount(
            String customerName, String identityNo, String address, String phone, String gender, Integer customerType,
            String accountNo, String code, Date contactDateFrom, Date contactDateTo, Integer channelID, Integer caseTypeID,
            String priority, String description, String status, String createdBy) {
        String SQL_SELECT = "SELECT "
                + "c.id ";
        String SQL_FROM = "FROM Customer c "
                + "left join c.contactCaseCollection as cc "
                + "join c.customerType as ct "
                + "left join cc.caseDetailId as cd "
                + "left join cd.caseTopicId as ctp "
                + "left join ctp.caseTypeId as cty "
                + "left join c.accountId as acc ";
        String SQL_WHERE = "WHERE 1 = 1 ";

        params = new ArrayList<ParameterMap>();
        ParameterMap param = new ParameterMap();

        if (customerName != null && customerName.toString().trim().length() > 0) {
            customerName = String.format("%s%s%s", "%", customerName, "%");
            SQL_WHERE += "and (c.initial + c.name + ' ' + c.surname) like :customerName ";
            param = new ParameterMap();
            param.setName("customerName");
            param.setValue(customerName);
            params.add(param);
        }

        if (identityNo != null && identityNo.toString().trim().length() > 0) {
            identityNo = String.format("%s%s%s", "%", identityNo, "%");
            SQL_WHERE += "and c.idno like :identityNo ";
            param = new ParameterMap();
            param.setName("identityNo");
            param.setValue(identityNo);
            params.add(param);
        }

        if (address != null && address.toString().trim().length() > 0) {
            address = String.format("%s%s%s", "%", address, "%");
            SQL_WHERE += "and (c.currentAddressLine1 like :address or c.currentAddressLine2 like :address) ";
            param = new ParameterMap();
            param.setName("address");
            param.setValue(address);
            params.add(param);
        }

        if (phone != null && phone.toString().trim().length() > 0) {
            phone = String.format("%s%s%s", "%", phone, "%");
            SQL_WHERE += "and (c.homePhone like :phone or c.officePhone like :phone or c.mobilePhone like :phone) ";
            param = new ParameterMap();
            param.setName("phone");
            param.setValue(phone);
            params.add(param);
        }

        if (gender != null && gender.toString().trim().length() > 0) {
            SQL_WHERE += "and c.gender = :gender ";
            param = new ParameterMap();
            param.setName("gender");
            param.setValue(gender);
            params.add(param);
        }

        if (customerType != null && customerType != 0) {
            SQL_WHERE += "and c.customerType.id = :customerType ";
            param = new ParameterMap();
            param.setName("customerType");
            param.setValue(customerType);
            params.add(param);
        }

        if (accountNo != null && accountNo.toString().trim().length() > 0) {
            accountNo = String.format("%s%s%s", "%", accountNo, "%");
            SQL_WHERE += "and acc.name like :accountNo ";
            param = new ParameterMap();
            param.setName("accountNo");
            param.setValue(accountNo);
            params.add(param);
        }

        if (code != null && code.toString().trim().length() > 0) {
            code = String.format("%s%s%s", "%", code, "%");
            SQL_WHERE += "and cc.code like :code ";
            param = new ParameterMap();
            param.setName("code");
            param.setValue(code);
            params.add(param);
        }

        if (contactDateFrom != null) {
            SQL_WHERE += "and cc.contactDate >= :contactDateFrom ";
            param = new ParameterMap();
            param.setName("contactDateFrom");
            param.setValue(contactDateFrom);
            params.add(param);
        }

        if (contactDateTo != null) {
            SQL_WHERE += "and cc.contactDate <= :contactDateTo ";
            param = new ParameterMap();
            param.setName("contactDateTo");

            Calendar date = Calendar.getInstance();
            date.setTime(contactDateTo);
            date.add(Calendar.DATE, 1);
            param.setValue(date.getTime());
            params.add(param);
        }

        if (channelID != null && channelID != 0) {
            SQL_WHERE += "and cc.channelId.id = :channelID ";
            param = new ParameterMap();
            param.setName("channelID");
            param.setValue(channelID);
            params.add(param);
        }

        if (caseTypeID != null && caseTypeID != 0) {
            SQL_WHERE += "and cty.id = :caseTypeID ";
            param = new ParameterMap();
            param.setName("caseTypeID");
            param.setValue(caseTypeID);
            params.add(param);
        }

        if (priority != null && priority.toString().trim().length() > 0) {
            SQL_WHERE += "and cc.priority = :priority ";
            param = new ParameterMap();
            param.setName("priority");
            param.setValue(priority);
            params.add(param);
        }

        if (description != null && description.toString().trim().length() > 0) {
            description = String.format("%s%s%s", "%", description, "%");
            SQL_WHERE += "and cc.description like :description ";
            param = new ParameterMap();
            param.setName("description");
            param.setValue(description);
            params.add(param);
        }

        if (status != null && status.toString().trim().length() > 0) {
            SQL_WHERE += "and cc.status = :status ";
            param = new ParameterMap();
            param.setName("status");
            param.setValue(status);
            params.add(param);
        }

        if (createdBy != null && createdBy.toString().trim().length() > 0) {
            createdBy = String.format("%s%s%s", "%", createdBy, "%");
            SQL_WHERE += "and cc.createBy like :createdBy ";
            param = new ParameterMap();
            param.setName("createdBy");
            param.setValue(createdBy);
            params.add(param);
        }


        String SQL_GROUP = "GROUP BY c.id ";

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
     * @param params the params to set
     */
    public void setParams(List<ParameterMap> params) {
        this.params = params;
    }
    
    public List<SearchCustomerValue> searchMaxarAnswer(String phoneNoSearch , int firstResult, int maxResults) {      
        String SQL_SELECT = "SELECT "
                + "NEW "
                + SearchCustomerValue.class.getName()
                + "( "
                + "c.id as customerId, "
                + "coalesce(c.initial,'') + c.name + ' ' + coalesce(c.surname,'') as customerName, "
                + "c.mobilePhone as telephone, "
                + "ct.name as customerType, "
                + "c.email as email, "
                + "c.referenceNo as wsCustId, "
                + "c.flexfield1 as displayName "
                + ") "
                + "FROM Customer c "
                + "join c.customerType as ct "
                + "WHERE 1 = 1 and (c.mobilePhone = ?1) ORDER BY c.name, c.surname ";

        Query query = em.createQuery(SQL_SELECT);

        query.setParameter(1, phoneNoSearch);
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }//if
        if (firstResult >= 0) {
            query.setFirstResult(firstResult);
        }//if
         
        return (List<SearchCustomerValue>) query.getResultList();
    }

    public List<SearchCustomerValue> searchCustomerMaxar(String firstNameSearch , String lastNameSearch , String displaySearch , 
                                                         String phoneNoSearch , String emailSearch , int firstResult, int maxResults) {
     
        String SQL_SELECT = "SELECT "
                + "NEW "
                + SearchCustomerValue.class.getName()
                + "( "
                + "c.id as customerId, "
                + "coalesce(c.initial,'') + c.name + ' ' + coalesce(c.surname,'') as customerName, "
                + "c.mobilePhone as telephone, "
                + "ct.name as customerType, "
                + "c.email as email, "
                + "c.referenceNo as wsCustId, "
                + "c.flexfield1 as displayName "
                + ") ";
        String SQL_FROM = "FROM Customer c join c.customerType as ct ";
        String SQL_WHERE = "WHERE 1 = 1 ";

        params = new ArrayList<ParameterMap>();
        ParameterMap param = new ParameterMap();

        if (firstNameSearch != null && firstNameSearch.toString().trim().length() > 0) {
            firstNameSearch = String.format("%s%s%s", "%", firstNameSearch, "%");
            SQL_WHERE += "and c.name like :firstNameSearch ";
            param = new ParameterMap();
            param.setName("firstNameSearch");
            param.setValue(firstNameSearch);
            params.add(param);
        }
        if (lastNameSearch != null && lastNameSearch.toString().trim().length() > 0) {
            lastNameSearch = String.format("%s%s%s", "%", lastNameSearch, "%");
            SQL_WHERE += "and c.surname like :lastNameSearch ";
            param = new ParameterMap();
            param.setName("lastNameSearch");
            param.setValue(lastNameSearch);
            params.add(param);
        }
        if (emailSearch != null && emailSearch.toString().trim().length() > 0) {
            emailSearch = String.format("%s%s%s", "%", emailSearch, "%");
            SQL_WHERE += "and c.email like :emailSearch ";
            param = new ParameterMap();
            param.setName("emailSearch");
            param.setValue(emailSearch);
            params.add(param);
        }
        if (phoneNoSearch != null && phoneNoSearch.toString().trim().length() > 0) {
            phoneNoSearch = String.format("%s%s%s", "%", phoneNoSearch, "%");
            SQL_WHERE += "and c.mobilePhone like :phoneNoSearch ";
            param = new ParameterMap();
            param.setName("phoneNoSearch");
            param.setValue(phoneNoSearch);
            params.add(param);
        }
        if (displaySearch != null && displaySearch.toString().trim().length() > 0) {
            displaySearch = String.format("%s%s%s", "%", displaySearch, "%");
            SQL_WHERE += "and c.flexfield1 like :displaySearch ";
            param = new ParameterMap();
            param.setName("displaySearch");
            param.setValue(displaySearch);
            params.add(param);
        }

        String SQL_ORDER = " ORDER BY c.name, c.surname ";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_ORDER;
        Query query = em.createQuery(SQL_QUERY);

        for (ParameterMap parm : params) {
            query.setParameter(parm.getName(), parm.getValue());
        }

        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }//if
        if (firstResult >= 0) {
            query.setFirstResult(firstResult);
        }//if
        
        return (List<SearchCustomerValue>) query.getResultList();
    }
}
