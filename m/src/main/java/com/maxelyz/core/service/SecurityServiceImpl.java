
package com.maxelyz.core.service;

import com.maxelyz.core.model.dao.AuditLogDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.AuditLog;
import com.maxelyz.core.model.entity.Acl;
import com.maxelyz.core.model.entity.UserGroupLocation;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;


public class SecurityServiceImpl implements SecurityService, Serializable {
    private static Logger log = Logger.getLogger(SecurityServiceImpl.class);
    
    private UsersDAO usersDAO;
    private AuditLogDAO auditLogDAO;
    
    @Override
    public boolean isPermitted(Users users, String permission) {
        //List<Acl> userGroupAcls = usersDAO.findAclByUser(users);
        List<Acl> userGroupAcls = JSFUtil.getUserSession().getUserGroupAclList();
        boolean result = false;
        for (Acl acl : userGroupAcls) {
            if (acl.getCode().equals(permission)) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    @Override
    public void createAuditLog(AuditLog auditlog) throws Exception{
        auditLogDAO.create(auditlog);
    }
    
    @Override
    public List<AuditLog> findAccessLogEntities(Date fromDate, Date toDate, String category, String userName, String customerName ) {
        return auditLogDAO.findAccessLogEntities(fromDate, toDate, category, userName, customerName);
    }
    
    @Override
    public boolean isViewLocation(List<UserGroupLocation> userGroupLocationList, Integer serviceTypeId, Integer businessUnitId, Integer locationId) {
        boolean result = false;
        for (UserGroupLocation ugl : userGroupLocationList) {
            try {
                if ( ugl.getServiceType().getId().intValue() == serviceTypeId.intValue()
                        && ugl.getBusinessUnit().getId().intValue() == businessUnitId.intValue()
                        && ugl.getLocation().getId().intValue() == locationId.intValue()) {
                    result = true;
                    break;
                }
            } catch (Exception e) {
//                log.error(e);
            }
        }
        return result;
    }
    
    //DAO
    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public void setAuditLogDAO(AuditLogDAO auditLogDAO) {
        this.auditLogDAO = auditLogDAO;
    }
    
    
    
    
}
