/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.service;

import com.maxelyz.core.model.entity.AuditLog;
import com.maxelyz.core.model.entity.UserGroupLocation;
import com.maxelyz.core.model.entity.Users;
import java.util.Date;
import java.util.List;

/**
 *
 * @author oat
 */
public interface SecurityService {
    boolean isPermitted(Users users, String permission);  
    boolean isViewLocation(List<UserGroupLocation> userGroupLocationList, Integer serviceTypeId, Integer businessUnitId, Integer locationId);
    public void createAuditLog(AuditLog auditLog) throws Exception;
    public List<AuditLog> findAccessLogEntities(Date fromDate, Date toDate, String category, String userName, String customerName );

    
}
