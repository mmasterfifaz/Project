package com.maxelyz.webservice;

import com.google.gson.Gson;
import com.maxelyz.core.model.value.admin.SessionMonitorValue;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Endpoint;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: apichatt
 * Date: 20/3/2557
 * Time: 15:56 .
 * To change this template use File | Settings | File Templates.
 */
@WebService()
public class MaxarWS {

    @Resource
    private WebServiceContext wsContext;

    @WebMethod
    public String sayHelloWorldFrom(String from) {
        String result = "Hello, world, from " + from;
        System.out.println(result);
        return result;
    }
    public String sayHelloGalaxyFrom(String from) {
        String result = "Hello, galaxy, from " + from;
        System.out.println(result);
        return result;
    }
    public static void main(String[] argv) {
        Object implementor = new MaxarWS();
        String address = "http://localhost:9000/MaxarWS";
        Endpoint.publish(address, implementor);
    }

    public static String removeLastComma(String str) {
        if (str.length() > 0)
            str = str.substring(0, str.length() - 1);
        else
            str = "";
        return str;
    }

    public Integer countActiveUser() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
//        ServletContext application = session.getServletContext();

        MessageContext mc = wsContext.getMessageContext();
        HttpSession session = ((javax.servlet.http.HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST)).getSession();
        ServletContext application = session.getServletContext();

        Integer ret=0;

        for (int i=1; i<=5; i++) {
            String conGroup="concurrentusersgroup"+ Integer.toString(i);
            if (application.getAttribute(conGroup) != null) {
                Map<String, SessionMonitorValue> concurrentUserMap = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute(conGroup);
                for (Map.Entry<String, SessionMonitorValue> entry : concurrentUserMap.entrySet()) {
//                    SessionMonitorValue sessionUser = new SessionMonitorValue(entry.getValue().getUser(), entry.getValue().getLoginTime(), entry.getValue().getRemoteAddr(), entry.getKey());
//                concurrentUser1.add(sessionUser);
                    ret++;
                }
            }
        }

        //System.out.println("countActiveUser(): "+ret);
        return ret;
    }

    @WebMethod
    public String getActiveUsers() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
//        ServletContext application = session.getServletContext();

        MessageContext mc = wsContext.getMessageContext();
        HttpSession session = ((javax.servlet.http.HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST)).getSession();
        ServletContext application = session.getServletContext();

        String ret="";
        List<Users> usersList = new ArrayList<Users>();

        for (int i=1; i<=5; i++) {
            String conGroup="concurrentusersgroup"+ Integer.toString(i);
            if (application.getAttribute(conGroup) != null) {
                Map<String, SessionMonitorValue> concurrentUserMap = (ConcurrentHashMap<String, SessionMonitorValue>) application.getAttribute(conGroup);
                for (Map.Entry<String, SessionMonitorValue> entry : concurrentUserMap.entrySet()) {
//                    SessionMonitorValue sessionUser = new SessionMonitorValue(entry.getValue().getUser(), entry.getValue().getLoginTime(), entry.getValue().getRemoteAddr(), entry.getKey());
//                concurrentUser1.add(sessionUser);

                    //Method 1
//                    ret += entry.getValue().getUser().getId()+",";
//                    System.out.println("concurrentusersgroup"+ Integer.toString(i)+" "+entry.getValue().getUser().getId()+" "+entry.getValue().getUser().getLoginName());

                    //Method 2
//                    JSONObject obj = new JSONObject();
//                    obj.put("userId", entry.getValue().getUser().getId());
//                    obj.put("userLoginName", entry.getValue().getUser().getLoginName());

                    //Method 3
                    if (entry.getValue().getAgentStatus().equals("Ready")) {
                        Users u = new Users();
                        u.setId(entry.getValue().getUser().getId());
                        u.setName(entry.getValue().getUser().getName());
                        u.setSurname(entry.getValue().getUser().getSurname());
                        u.setLoginName(entry.getValue().getUser().getLoginName());
                        u.setEmail(entry.getValue().getUser().getEmail());
                        u.setUserGroup(entry.getValue().getUser().getUserGroup().getId());
                        u.setStatus(entry.getValue().getUser().getStatus());
                        u.setEnable(entry.getValue().getUser().getEnable());
                        usersList.add(u);
                    }
                }
            }
        }

        //Method 1
//        ret = removeLastComma(ret);
//        System.out.println("activeUserList(): "+ret);

        Gson gson = new Gson();
        ret = gson.toJson(usersList);
        //System.out.println(ret);
        
        session.invalidate();

        return ret;
    }

}
