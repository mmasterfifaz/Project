/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.service;

import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.dao.UsersTimeAttendanceDAO;
import com.maxelyz.social.model.dao.SoUserNotReadyLogDAO;
import com.maxelyz.core.model.entity.LogoffType;
import com.maxelyz.core.model.entity.UserGroup;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.model.entity.UsersTimeAttendance;
import com.maxelyz.social.model.entity.SoUserNotReadyReason;
import com.maxelyz.social.model.entity.SoUserNotReadyLog;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class UserServiceImpl implements UserService, Serializable {
    @PersistenceContext
    private EntityManager em;
    
    private static Logger log = Logger.getLogger(UserServiceImpl.class);
    
    private UsersDAO usersDAO;
    private UsersTimeAttendanceDAO usersTimeAttendanceDAO;
    private SoUserNotReadyLogDAO soUserNotReadyLogDAO;
    
    public List<Users> findUsersByLoginName(String loginName) { 
        return usersDAO.findUsersByLoginName(loginName);
    }
    
    public void setLoginTime(Users u) {
        UsersTimeAttendance ut = new UsersTimeAttendance();
        ut.setUser(u);
        ut.setTimestamp(new Date());
        ut.setType("login");
        this.usersTimeAttendanceDAO.create(ut);
    } 
    
    public void setLogoutTime(Users u, String logoutReason) {        
        UsersTimeAttendance ut = new UsersTimeAttendance();
        ut.setUser(u);
        ut.setTimestamp(new Date());
        ut.setLogoffReason(logoutReason);
        ut.setType("logout");
        this.usersTimeAttendanceDAO.create(ut);
    }
    
    @Override
    public List<LogoffType> findLogoffTypes() {
        Query q = em.createQuery("select object(o) from LogoffType as o where enable = 1 and status = 1 order by code"); 
        return q.getResultList();
    }
  
    @Override
    public List<SoUserNotReadyReason> findNotReadyList() {
        Query q = em.createQuery("select object(o) from SoUserNotReadyReason as o where enable = 1 and status = 1 order by code"); 
        return q.getResultList();
    }
    
    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public UsersTimeAttendanceDAO getUsersTimeAttendanceDAO() {
        return usersTimeAttendanceDAO;
    }

    public void setUsersTimeAttendanceDAO(UsersTimeAttendanceDAO usersTimeAttendanceDAO) {
        this.usersTimeAttendanceDAO = usersTimeAttendanceDAO;
    }

    public SoUserNotReadyLogDAO getSoUserNotReadyLogDAO() {
        return soUserNotReadyLogDAO;
    }

    public void setSoUserNotReadyLogDAO(SoUserNotReadyLogDAO soUserNotReadyLogDAO) {
        this.soUserNotReadyLogDAO = soUserNotReadyLogDAO;
    }
    
}
