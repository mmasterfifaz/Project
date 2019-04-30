/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.value.admin;

import com.maxelyz.core.model.entity.PurchaseOrderApprovalLog;
import java.util.Comparator;


/**
 *
 * @author ukritj
 */
public class PurchaseOrderApprovalLogComparable implements Comparator<PurchaseOrderApprovalLog>{
 
    @Override
    public int compare(PurchaseOrderApprovalLog purchaseOrderApprovalLog1, PurchaseOrderApprovalLog purchaseOrderApprovalLog2) {
        return purchaseOrderApprovalLog1.getId().compareTo(purchaseOrderApprovalLog2.getId());
    }
} 
