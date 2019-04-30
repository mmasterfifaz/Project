/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.dao.front.customerHandling;

import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.value.front.customerHandling.ContactHistorySaleResultInfoValue;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;

import com.maxelyz.core.model.value.front.customerHandling.PurchaseOrderInfoValue;
import com.ntier.utils.ParameterMap;
import java.util.ArrayList;


public class ContactSummaryDAO {

    @PersistenceContext
    private EntityManager em;

    public List<ContactHistorySaleResultInfoValue> findProductsByPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
        Query q = em.createQuery("select NEW "
                + ContactHistorySaleResultInfoValue.class.getName() 
                + "(po, p) "
                + " from PurchaseOrderDetail pod "
                + "join pod.product p "
                + "join pod.purchaseOrder po "
                + "where po in (?1)");
        q.setParameter(1, purchaseOrders);
        return q.getResultList();
      
       
    }
}
