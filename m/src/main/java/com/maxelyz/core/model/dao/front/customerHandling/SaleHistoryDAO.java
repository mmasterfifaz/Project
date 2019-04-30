/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.dao.front.customerHandling;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;

import com.maxelyz.core.model.value.front.customerHandling.PurchaseOrderInfoValue;
import com.ntier.utils.ParameterMap;
import java.util.ArrayList;

/**
 *
 * @author Manop
 */
public class SaleHistoryDAO {

    @PersistenceContext
    private EntityManager em;

    private List<ParameterMap> params;

    public PurchaseOrderInfoValue findPurchaseOrderByPK(String referenceNo) {
        String SQL_SELECT = "SELECT "
                + "NEW "
                + PurchaseOrderInfoValue.class.getName()
                + "( "
                + "po.contactDate, "
                + "po.refNo, "
                + "pr.code, "
                + "pr.name, "
                + "po.quantity, "
                + "po.amount, "
                + "po.updateBy, "
                + "po.updateDate, "
                + "po.channelType, "
                + "po.approve, "
                + "po.qc, "
                + "po.settlement "
                + ") ";
        String SQL_FROM = "FROM PurchaseOrder po "
                + "JOIN po.productRegisterCollection prc "
                + "JOIN prc.productId pr "
                + "";
        String SQL_WHERE = "WHERE po.refNo = :referenceNo "
                + " ";

        String SQL_GROUP = "";

        String SQL_HAVING = "";

        String SQL_ORDER = "";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
        Query query = em.createQuery(SQL_QUERY);

        query.setParameter("referenceNo", referenceNo);

        return (PurchaseOrderInfoValue) query.getSingleResult();
    }

    public List<PurchaseOrderInfoValue> findPurchaseOrder(){
        return findPurchaseOrder(null, 0, Integer.MAX_VALUE);
    }

    public List<PurchaseOrderInfoValue> findPurchaseOrder(Integer customerId, int firstResult, int maxResults){
        String SQL_QUERY = generateFindPurchaseOrderSQLCmd(customerId);
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

       return (List<PurchaseOrderInfoValue>)query.getResultList();
    }

    public Integer findPurchaseOrderRowCount(Integer customerId){
        String SQL_QUERY = generateFindPurchaseOrderSQLCmd(customerId);
        int startPos = SQL_QUERY.indexOf("NEW");
        int endPos = SQL_QUERY.indexOf("FROM");
        String replacedStr = SQL_QUERY.substring(startPos, endPos);
        SQL_QUERY = SQL_QUERY.replace(replacedStr, " count(*) ");
        Query query = em.createQuery(SQL_QUERY);

        for (ParameterMap parm:params) {
            query.setParameter(parm.getName(), parm.getValue());
        }

        return ((Long) query.getSingleResult()).intValue();
    }

    private String generateFindPurchaseOrderSQLCmd(Integer customerId){
        String SQL_SELECT = "SELECT "
                + "NEW "
                + PurchaseOrderInfoValue.class.getName()
                + "( "
                + "po.contactDate, "
                + "po.refNo, "
                + "pr.code, "
                + "pr.name, "
                + "po.quantity, "
                + "po.amount, "
                + "po.updateBy, "
                + "po.updateDate, "
                + "po.channelType, "
                + "po.approve, "
                + "po.qc, "
                + "po.settlement "
                + ") ";
        String SQL_FROM = "FROM PurchaseOrder po "
                + "JOIN po.productRegisterCollection prc "
                + "JOIN prc.productId pr "
                + "JOIN po.customerId c "
                + "";

        String SQL_WHERE = "WHERE 1 = 1 ";

        params = new ArrayList<ParameterMap>();
        ParameterMap param = new ParameterMap();

        if (customerId != null && customerId != 0){
            SQL_WHERE += "and c.id = :customerId ";
            param = new ParameterMap();
            param.setName("customerId");
            param.setValue(customerId);
            params.add(param);
        }

        String SQL_GROUP = "";

        String SQL_HAVING = "";

        String SQL_ORDER = "ORDER BY po.contactDate DESC";

        return SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
    }
}
