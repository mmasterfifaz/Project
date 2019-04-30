/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.service.nimp;

import com.maxelyz.core.model.entity.ContactHistory;
import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.PurchaseOrderApprovalLog;
import com.maxelyz.core.model.entity.QaTransDetail;
import com.maxelyz.core.model.ndao.RegistrationTxDAO;
import com.maxelyz.core.service.mng.ContextResourceFinder;

/**
 *
 * @author DevTeam
 */


class RegistrationTrxLocalService {
    
    protected PurchaseOrder insertNewPO(PurchaseOrder po) throws Exception {
        //Step1 : Insert QaTransApproval, Purchase Order and Purchase Order's Children    
//        if (po.getQaTransApproval()!=null && po.getQaTransApproval().getQaFormId()!=null){
//            ContextResourceFinder.getQaTransDAO().create(po.getQaTransApproval());
//        }
        ContextResourceFinder.getRegistrationTxDAO().insertPurchaseOrderTx(po);
        //Step2 : Setup Assignment Detail and update status
        ContextResourceFinder.getAssignmentDetailDAO().updateAssignmentDetail(po.getAssignmentDetail());
        //Step3 : Retrieve new info
        //PurchaseOrder successPO = ContextResourceFinder.getPurchaseOrderDAO().findPurchaseOrder(po.getId());
        ContextResourceFinder.getPurchaseOrderDAO().updateFamilyPurchaseOrder(po.getId());
        return po;
        
    }    
    
    protected PurchaseOrder updatePO(PurchaseOrder po) throws Exception {
        RegistrationTxDAO registrationDAO = ContextResourceFinder.getRegistrationTxDAO();
        try{
            //Step1 : (delete/insert) QaTransApproval and update Purchase Order
//            if (po.getQaTransApproval()!=null && po.getQaTransApproval().getQaFormId()!=null){
//                if ( po.getQaTransApproval().getId() == null ){ //A1: In case, not have qa trans approval before
//                    ContextResourceFinder.getQaTransDAO().create(po.getQaTransApproval()); 
//                    registrationDAO.updateNewApprovalQaFormOnPurchaseOrder(po);
//                }else{ //A2: In case, qa trans approval is exists in po, so delete all qa trans detail and insert new qa trans detail.
//                    ContextResourceFinder.getQaTransDetailDAO().deleteQaTransDetails(po.getQaTransApproval().getId());
//                    for (QaTransDetail qatd  : po.getQaTransApproval().getQaTransDetailCollection() ){
//                        ContextResourceFinder.getQaTransDetailDAO().create(qatd);
//                    }
//                }
//            }            
            registrationDAO.editPurchaseOrderTx(po);

            //Step2 : Setup Assignment Detail and update status
            ContextResourceFinder.getAssignmentDetailDAO().updateAssignmentDetail(po.getAssignmentDetail());

            //Step3 : Inser New PO Approval Log
            if (po.getPurchaseOrderApprovalLogCollection() != null) {
                for (PurchaseOrderApprovalLog apLog : po.getPurchaseOrderApprovalLogCollection()) {
                    if (apLog.getId() == null){
                        //apLog.setPurchaseOrder(po);
                        ContextResourceFinder.getPurchaseOrderDAO().createApprovalLog(apLog);
                    }
                }
            }

            //Step4 : Insert New Contact History
            if (po.getContactHistoryCollection() != null) {
                for (ContactHistory ch : po.getContactHistoryCollection()) {
                    if (ch.getId() == null){
                        ContextResourceFinder.getContactHistoryDAO().create(ch);
                        ContextResourceFinder.getContactHistoryDAO().createContactHistoryPurchaseOrder(po.getId(), ch.getId());
                    }
                }
            }
            //Step5 : Retrieve new info
            //PurchaseOrder  successPO = ContextResourceFinder.getPurchaseOrderDAO().findPurchaseOrder(po.getId());
            
            ContextResourceFinder.getPurchaseOrderDAO().updateFamilyPurchaseOrder(po.getId());
            return po;
        }catch(Exception e){
            registrationDAO.markRollback(e.getMessage());
            throw e;
        }
        
    }  
}
