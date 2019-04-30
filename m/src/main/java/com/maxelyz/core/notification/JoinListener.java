/*
 * Comet4J Copyright(c) 2011, http://code.google.com/p/comet4j/ This code is
 * licensed under BSD license. Use it as you wish, but keep this copyright
 * intact.
 */
package com.maxelyz.core.notification;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.utils.JSFUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.comet4j.core.CometConnection;
import org.comet4j.core.event.ConnectEvent;
import org.comet4j.core.listener.ConnectListener;
import com.maxelyz.core.notification.dto.JoinDTO;
import java.util.Iterator;
import java.util.Map;

/**
 * 上线侦听
 *
 * @author jinghai.xiao@gmail.com
 * @date 2011-3-3
 */
public class JoinListener extends ConnectListener {

    /*
     * (non-Jsdoc)
     * @see org.comet4j.event.Listener#handleEvent(org.comet4j.event.Event)
     */
    @Override
    public boolean handleEvent(ConnectEvent anEvent) {
        CometConnection conn = anEvent.getConn();
        HttpServletRequest request = conn.getRequest();
        //String userName = getCookieValue(request.getCookies(), "userName", "");
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        String userId = userSession.getUsers().getId().toString();
        String userName = userSession.getUserName();
        try {
            userId = URLDecoder.decode(userId, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JoinDTO dto = new JoinDTO(conn.getId(), userId);
        
        Map<String, String> map = AppStore.getInstance().getMap();
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            String connId = entry.getKey();
            String connUserId = entry.getValue();
            if(userId.equals(connUserId)){
                AppStore.getInstance().getMap().remove(connId);
            }
        }
        
        AppStore.getInstance().put(conn.getId(), userId);
        //anEvent.getTarget().sendToAll(Constant.APP_CHANNEL, dto);
        return true;
    }

    public String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue) {
        String result = defaultValue;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return result;
    }
}
