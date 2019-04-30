/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao.front.search;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import com.maxelyz.core.model.value.front.search.SearchAccountValue;
import com.ntier.utils.ParameterMap;

/**
 *
 * @author Manop
 */
@Transactional
public class SearchAccountDAO {

    @PersistenceContext
    private EntityManager em;

    private List<ParameterMap> params;

    public List<SearchAccountValue> searchAccount() {
        return searchAccount(null, null, null, null, null, 0, Integer.MAX_VALUE);
    }

    public List<SearchAccountValue> searchAccount(
            Integer accountId, String accountName, String customerName, String phone, Integer customerType,
            int firstResult, int maxResults) {

        String SQL_QUERY = generateSearchAccountSQLCmd(accountId, accountName, customerName, phone, customerType);
        Query query = em.createQuery(SQL_QUERY);

        for (ParameterMap parm:params) {
            query.setParameter(parm.getName(), parm.getValue());
        }

        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }//if
        if (firstResult >= 0) {
            query.setFirstResult(firstResult);
        }//if

        return (List<SearchAccountValue>) query.getResultList();
    }
    
    public Integer searchAccountRowCount(
            Integer accountId, String accountName, String customerName, String phone, Integer customerType
            ){
        String SQL_QUERY = generateSearchAccountSQLCmd(accountId, accountName, customerName, phone, customerType);
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

    private String generateSearchAccountSQLCmd(
            Integer accountId, String accountName, String customerName, String phone, Integer customerType
            ){
                String SQL_SELECT = "SELECT "
                + "NEW "
                + SearchAccountValue.class.getName()
                + "( "
                + "a.id, "
                + "a.name, "
                + "a.addressLine1, "
                + "a.addressLine2, "
                + "dt.name, "
                + "pv.name "
                + ") ";
        String SQL_FROM = "FROM Account a "
                + "left join a.customerCollection cc "
                + "left join a.district dt "
                + "left join dt.provinceId pv ";
        String SQL_WHERE = "WHERE 1 = 1 ";

        params = new ArrayList<ParameterMap>();
        ParameterMap param = new ParameterMap();

        if (accountId != null && accountId != 0) {
            SQL_WHERE += "and a.id like :accountId ";
            param = new ParameterMap();
            param.setName("accountId");
            param.setValue(accountId);
            params.add(param);
        }

        if (accountName != null && accountName.toString().trim().length() > 0) {
            accountName = String.format("%s%s%s", "%", accountName, "%");
            SQL_WHERE += "and a.name like :accountName ";
            param = new ParameterMap();
            param.setName("accountName");
            param.setValue(accountName);
            params.add(param);
        }

        if (customerName != null && customerName.toString().trim().length() > 0) {
            customerName = String.format("%s%s%s", "%", customerName, "%");
            SQL_WHERE += "and (cc.initial + cc.name + ' ' + cc.surname) like :customerName ";
            param = new ParameterMap();
            param.setName("customerName");
            param.setValue(customerName);
            params.add(param);
        }

        if (phone != null && phone.toString().trim().length() > 0) {
            phone = String.format("%s%s%s", "%", phone, "%");
            SQL_WHERE += "and (cc.homePhone like :phone or cc.officePhone like :phone or cc.mobilePhone like :phone) ";
            param = new ParameterMap();
            param.setName("phone");
            param.setValue(phone);
            params.add(param);
        }

        if (customerType != null && customerType != 0) {
            SQL_WHERE += "and cc.customerType.id = :customerType ";
            param = new ParameterMap();
            param.setName("customerType");
            param.setValue(customerType);
            params.add(param);
        }

        String SQL_GROUP = "GROUP BY a.id, a.name, a.addressLine1, a.addressLine2, a.district.name, dt.name, pv.name ";

        String SQL_HAVING = "";

        String SQL_ORDER = "";

        return SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;

    }

    public SearchAccountValue findAccountByPK(
            Integer accountId
            ) {

        String SQL_SELECT = "select "
                + "NEW "
                + SearchAccountValue.class.getName()
                + "( "
                + "a.id, "
                + "a.name, "
                + "a.department, "
                + "a.addressLine1, "
                + "a.addressLine2, "
                + "a.district.id, "
                + "a.district.name, "
                + "a.district.provinceId.id, "
                + "a.district.provinceId.name, "
                + "a.postalCode, "
                + "a.remark, "
                + "a.telephone "
                + ") ";
        String SQL_FROM = "from Account a "
                + "left join a.customerCollection cc ";
        String SQL_WHERE = "where a.id = :accountId ";

        String SQL_GROUP = "";

        String SQL_HAVING = "";

        String SQL_ORDER = "";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
        Query query = em.createQuery(SQL_QUERY);

        query.setParameter("accountId", accountId);

        return (SearchAccountValue) query.getSingleResult();
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
}
