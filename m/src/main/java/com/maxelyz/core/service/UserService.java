/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.service;

import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.model.entity.LogoffType;
import com.maxelyz.social.model.entity.SoUserNotReadyReason;
import java.util.List;
import java.util.Map;

/**
 *
 * @author oat
 */
public interface UserService {
    public List<Users> findUsersByLoginName(String loginName);
    public void setLoginTime(Users u);   
    public void setLogoutTime(Users u, String logoutReason);
    public List<LogoffType> findLogoffTypes();
    public List<SoUserNotReadyReason> findNotReadyList();
}
