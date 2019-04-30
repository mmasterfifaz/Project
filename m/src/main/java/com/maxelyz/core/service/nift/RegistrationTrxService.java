/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.service.nift;

import com.maxelyz.core.common.dto.MxLocalServiceResponse;
import com.maxelyz.core.model.entity.PurchaseOrder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DevTeam
 */
public interface RegistrationTrxService {
    /**
     * METHOD
     *
     * @param po purchase order object which holds data for save
     * @param serviceResponse object will encapsulated the response result inside when done the method. It should not be null when send it to the method
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    void insertNewPO(PurchaseOrder po, MxLocalServiceResponse serviceResponse);

    /**
     * METHOD
     *
     * @param po purchase order object which holds data for save
     * @param serviceResponse object will encapsulated the response result inside when done the method. It should not be null when send it to the method
     */    
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    void updatePO(PurchaseOrder po, MxLocalServiceResponse serviceResponse);
    
}
