/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.test;

//import com.concerto.UD.AgentService.AgentWebService;
//import com.concerto.UD.AgentService.AgentWebServiceServiceLocator;
//import com.concerto.UD.AgentService.Client.UDAgent;
//import com.concerto.UD.AgentService.Client.UDAgentInfo;
//import com.concerto.UD.AgentService.Client.UDInfo;
//import com.concerto.UD.EventService.EventService;
//import com.concerto.UD.EventService.EventServiceServiceLocator;
//import com.concerto.UD.EventService.Notify.AgentEvent;
//import com.concerto.UD.ProvisioningService.Client.Beans.Agent.AgentSetting;
//import com.concerto.UD.ProvisioningService.Client.Beans.Agent.ServiceSettings;
//import com.concerto.UD.ProvisioningService.Client.Beans.Agent.User;
import java.rmi.RemoteException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
//import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;

/**
 *
 * @author admin
 */
@ManagedBean
@RequestScoped
public class LoginAspectController {

    //private static Logger logger = Logger.getLogger(LoginAspectController.class);
    private long lastEventSeq = 0;
//    private AgentWebService agentWebService;
//    private EventService eventService;
    private String loginName;
    private String pwd;
    private Integer stationId = 5004;
    private String domain = "192.168.4.36:8180";
    private String status;
    Integer udAgentIndex;

    public void loginAction(ActionEvent event) throws RemoteException {

        boolean userExist = false;
        boolean loginSuccess = false;
        status = "";

//        AgentWebServiceServiceLocator agentWebServiceServiceLocator = new AgentWebServiceServiceLocator();
//        agentWebService = agentWebServiceServiceLocator.getConcertoAgentPortal();

//        EventServiceServiceLocator eventWSLoc = new EventServiceServiceLocator();
//        eventService = eventWSLoc.getEventService();

        //Check agent's existence
//        UDAgent udAgent = new UDAgent();
//        udAgent.setLDAPUserId(loginName);

//        UDInfo udInfo = new UDInfo();
//        udInfo.setListType(143);

//        User user = (User) agentWebService.getInfo(udAgent, udInfo);

//        if (user == null) {
//            status = "UserId '" + loginName + "' not found";
//            userExist = false;
//        } else {
//            status = "UserId '" + loginName + "' found";
//            userExist = true;
//        }
//
//
//        if (userExist) {
//            udAgent.setClientRole(user.getUsertypemask());
//            udAgent.setServerType(1);
//            udAgent.setAgentLoginName(user.getUserid());
//
//            UDAgentInfo udAgentInfo = new UDAgentInfo();
//            udAgentInfo.setAgentLoginName(user.getUserid());
//            udAgentInfo.setPassword(pwd);
//
//            try {
//                agentWebService.authenticate(udAgent, udAgentInfo);
//                status = "Authenticate Success";
//                loginSuccess = true;
//            } catch (Exception e) {
//                logger.error(e);
//                status = "Login Fail.";
//                loginSuccess = false;
//            }
//
//            if (loginSuccess) {
//                UDInfo info = new UDInfo();
//                info.setListType(107);
//                info.setServiceId(0);
//                info.setServiceType(11);
//                ServiceSettings serviceSettings = (ServiceSettings) agentWebService.getInfo(udAgent, info);
//
//                UDInfo info1 = new UDInfo();
//                info1.setListType(162);
//                AgentSetting agentSetting = (AgentSetting) agentWebService.getInfo(udAgent, info1);
//
//                udAgentIndex = eventService.registerAgentEventSubscriber(udAgent.getAgentLoginName());
//                AgentEvent agentEvent = eventService.getNextAgentEventAck(udAgentIndex, lastEventSeq);//m_AgentIndex from Subscribe To Agent Events
//                if (agentEvent != null) {
//                    lastEventSeq = agentEvent.getEventTimestamp();
//                    System.out.println("AWS Event: id<" + agentEvent.getEventId() + ">, payload<" + agentEvent.getPayload().toString() + ">");
//                    //TODO: Have your application handle event as appropriate
//                    //(eg, update application state and UI)
//                }
//                agentWebService.register(udAgent, udAgentInfo);
//
//            }
//        }

    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public AgentWebService getAgentWebService() {
//        return agentWebService;
//    }
//
//    public void setAgentWebService(AgentWebService agentWebService) {
//        this.agentWebService = agentWebService;
//    }
//
//    public EventService getEventService() {
//        return eventService;
//    }
//
//    public void setEventService(EventService eventService) {
//        this.eventService = eventService;
//    }

    public Integer getUdAgentIndex() {
        return udAgentIndex;
    }

    public void setUdAgentIndex(Integer udAgentIndex) {
        this.udAgentIndex = udAgentIndex;
    }

    public long getLastEventSeq() {
        return lastEventSeq;
    }

    public void setLastEventSeq(long lastEventSeq) {
        this.lastEventSeq = lastEventSeq;
    }
    

}
