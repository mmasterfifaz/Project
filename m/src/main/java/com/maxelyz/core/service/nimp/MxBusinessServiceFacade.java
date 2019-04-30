/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.service.nimp;

import com.maxelyz.core.service.nift.RegistrationTrxService;
import com.maxelyz.core.common.dto.MxLocalServiceResponse;
import com.maxelyz.core.common.log.RegistrationLogging;
import com.maxelyz.core.common.log.TimeCollector;
import com.maxelyz.core.model.entity.PurchaseOrder;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DevTeam
 */
public class MxBusinessServiceFacade implements RegistrationTrxService{
    /********************* PROPERTY*******************/
    private RegistrationTrxLocalService registrationTrxService;

    /********************* CONSTRUCTOR*******************/
    public MxBusinessServiceFacade() {
        this.registrationTrxService = new RegistrationTrxLocalService();
    }

    /********************* METHOD *******************/

    @Override
    public void insertNewPO(PurchaseOrder po,MxLocalServiceResponse serviceResponse)  {
        TimeCollector tc = new TimeCollector(true);
        try{   
            PurchaseOrder newPO =  this.registrationTrxService.insertNewPO(po);
            serviceResponse.setOK(newPO);
        }catch(Exception e){  
            serviceResponse.setError(e.getMessage());
            RegistrationLogging.logError(e.getMessage()+", ["+RegistrationLogging.getObjectValueToString("purchaseOrder", po)+"]" , e);
        }finally{
            RegistrationLogging.debugElapseTime(tc, "Duration of insert new po");
            RegistrationLogging.logInfo("Insert new po : Service Response="+serviceResponse);
        }
    }

    @Override
    public void updatePO(PurchaseOrder po, MxLocalServiceResponse serviceResponse) {
        TimeCollector tc = new TimeCollector(true);
        try{   
            PurchaseOrder successPO =  this.registrationTrxService.updatePO(po);
            serviceResponse.setOK(successPO);
        }catch(Exception e){ 
            serviceResponse.setError(e.getMessage());
            RegistrationLogging.logError(e.getMessage()+", ["+RegistrationLogging.getObjectValueToString("purchaseOrder", po)+"]" , e);
        }finally{
            RegistrationLogging.debugElapseTime(tc, "Duration of update po");
            RegistrationLogging.logInfo("Update po : Service Response="+serviceResponse);
        }
    }
    
    
   

    
    
}
