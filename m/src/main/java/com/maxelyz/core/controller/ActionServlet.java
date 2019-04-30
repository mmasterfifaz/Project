/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller;

import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.Campaign;
import com.maxelyz.core.model.entity.Channel;
import com.maxelyz.core.model.entity.ContactHistory;
import com.maxelyz.core.model.entity.MarketingCustomer;
import com.maxelyz.core.model.entity.MarketingCustomerPK;
import com.maxelyz.utils.JSFUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author admin
 */
public class ActionServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(ActionServlet.class);
    private String trackId = "";
    private String contactTo = "";
    private String channel = "";
    private String uniqueId = "";
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");        
        String mode = request.getParameter("mode");
        if(mode.equals("create")){
            createContactHistory(request, response);
        }else if(mode.equals("talkTime")){
            setTalkTime(request, response);
        }else if(mode.equals("status")){
            setStatus(request, response);
        }else if(mode.equals("statusTelephone")){
            setStatusTelephone(request, response);
        }else if(mode.equals("showContactSummary")){
            setShowContactSummary(request, response);
        }else if(mode.equals("dialNo")){
            setDialNo(request, response);
        }else if(mode.equals("inbound")){
            setInbound(request, response);
        }
        
        response.sendRedirect("action.jsf");
        
    }
    
    private void setInbound(HttpServletRequest req, HttpServletResponse res) {
        UserSession userSession = (UserSession) req.getSession().getAttribute("userSession");
        if(userSession != null){
            userSession.setInboundCall(true);
        }
    }
    
    private void setShowContactSummary(HttpServletRequest req, HttpServletResponse res) {
        UserSession userSession = (UserSession) req.getSession().getAttribute("userSession");
        userSession.setShowContactSummary(true);    
    }
    
    private void setStatus(HttpServletRequest req, HttpServletResponse res) {
        UserSession userSession = (UserSession) req.getSession().getAttribute("userSession");
        String status = req.getParameter("status");
        if(userSession != null){
            if(status.equalsIgnoreCase("ringing")){
                userSession.setDialShow(true);
            }
            userSession.setDialStatus(status);
            userSession.setShowContactSummary(true);
            
            /*
            if(status.equals("WRAP")){
                String talkTime = req.getParameter("talkTime");
                trackId = req.getParameter("trackId");
                String phoneNo = req.getParameter("phoneNo");
                Integer talkTimeInt = 0;
                if(talkTime != null && !talkTime.equals("")){
                    talkTimeInt = Integer.parseInt(talkTime);
                }
                
                if(userSession.getContactHistory() != null && userSession.getContactHistory().getId() != null){
                    userSession.getContactHistory().setContactTo(phoneNo);
                    userSession.getContactHistory().setTelephonyTrackId(trackId);
                    userSession.getContactHistory().setTalkTime(talkTimeInt);
                    userSession.getContactHistoryDAO().saveParam(userSession.getContactHistory().getId(), trackId, talkTimeInt, phoneNo);
                }
            }*/
        }
    }
    
    private void setStatusTelephone(HttpServletRequest req, HttpServletResponse res) {
        UserSession userSession = (UserSession) req.getSession().getAttribute("userSession");
        String status = req.getParameter("status");
        if(userSession != null){
            userSession.setDialStatus(status);
        }
    }
    
    private void setTalkTime(HttpServletRequest req, HttpServletResponse res) {
        try{
            UserSession userSession = (UserSession) req.getSession().getAttribute("userSession");
            String talkTime = req.getParameter("talkTime");
            trackId = req.getParameter("trackId");
            userSession.setTelephonyTrackId(trackId);
            Integer talkTimeInt = 0;
            if(talkTime != null && !talkTime.equals("")){
                talkTimeInt = Integer.parseInt(talkTime);
                userSession.getContactHistory().setTalkTime(talkTimeInt);
                userSession.setTalkTime(talkTimeInt);
            }
            if(trackId != null && !trackId.equals("") && !trackId.equals("0")){
                userSession.getContactHistoryDAO().updateTalkTime(trackId, talkTimeInt);
            }
        }catch(NumberFormatException e){
            log.error(e);
        }        
    }
    
    private void setDialNo(HttpServletRequest req, HttpServletResponse res) {
        try{
            UserSession userSession = (UserSession) req.getSession().getAttribute("userSession");
            String dialNo = req.getParameter("dialNo");
            if(userSession != null){
                if(dialNo != null && !dialNo.equals("")){
                    userSession.setDialNo(dialNo);
                    userSession.setContactTo(dialNo);
                    userSession.setShowContactSummary(true);
                }
            }
        }catch(Exception e){
            log.error(e);
        }        
    }
    
    private void createContactHistory(HttpServletRequest req, HttpServletResponse res) {   
        try {
            contactTo = req.getParameter("phoneNo");
            channel = req.getParameter("callCategory");
            trackId = req.getParameter("trackId");
            uniqueId = req.getParameter("uniqueId");

            UserSession userSession = (UserSession) req.getSession().getAttribute("userSession");
            //if(userSession != null){
            //    userSession.setContactTo(contactTo);
            //    userSession.setTelephonyTrackId(trackId);
            //}

            //if (userSession.getContactHistory() != null) {
            //    if (userSession.getContactHistory().getId() != null) {
            //        userSession.removeContactHistory();
            //    }
            //}

                /*if (contactTo == null || contactTo.equals("")) {
                    if (userSession != null) {
                        if (userSession.getContactTo() == null || userSession.getContactTo().equals("")) {
                            if (userSession.getDialNo() != null && !userSession.getDialNo().equals("")) {
                                contactTo = userSession.getDialNo();
                            }
                        }
                    }
                }*/
                if(trackId != null && !trackId.isEmpty()){
                    userSession.setTelephonyTrackId(trackId);
                }
                ContactHistory contactHistory = null;


                Integer channelId = 0;

                if (channel.equals("inbound") || userSession.isInboundCall()) {
                    userSession.setInboundCall(true);
                    channelId = 2;
                } else if (channel.equals("outbound") || userSession.isOutboundCall()) {
                    userSession.setOutboundCall(true);
                    channelId = 9;
                }

                if (userSession.getContactHistory() == null) {
                    contactHistory = new ContactHistory();
                } else {
                    contactHistory = userSession.getContactHistory();
                }
                if (contactHistory.getCustomer() == null) {
                    if (userSession.getCustomerDetail() != null) {
                        contactHistory.setCustomer(userSession.getCustomerDAO().findCustomer(userSession.getCustomerDetail().getId()));
                    }
                }
                if (contactHistory.getId() == null) {
                    contactHistory.setUniqueId(uniqueId);
                    contactHistory.setContactDate(new Date());
                    contactHistory.setContactTo(contactTo);
                    contactHistory.setContactStatus("c");//close
                    contactHistory.setDmccontact(true);
                    contactHistory.setCallSuccess(true);
                    contactHistory.setFollowupsale(false);
                    contactHistory.setContactClose(false);
                    contactHistory.setTelephonyTrackId(trackId);
                    contactHistory.setCreateDate(new Date());
                    contactHistory.setCreateBy(userSession.getUserName());
                    contactHistory.setUsers(userSession.getUsers());
                }
                if (channelId != null && channelId != 0) {
                    contactHistory.setChannel(new Channel(channelId));
                }
                //if (contactHistory.getContactTo() == null || contactHistory.getContactTo().equals("")) {
                //    contactHistory.setContactTo(contactTo);
                //}
                //if (contactHistory.getTelephonyTrackId() == null || contactHistory.getTelephonyTrackId().equals("") || contactHistory.getTelephonyTrackId().equals("0")) {
                //    contactHistory.setTelephonyTrackId(trackId);
                //}

                try {
                    if (contactHistory.getId() == null) {
                        if (userSession.getAssignmentDetail() != null && userSession.getAssignmentDetail().getId() != null) {
                            try {
                                if (userSession.getAssignmentDetail().getNewList() != null && userSession.getAssignmentDetail().getNewList()) {
                                    userSession.getAssignmentDetail().setNewList(Boolean.FALSE);
                                }
                                userSession.getAssignmentDetailDAO().setOpenStatus(userSession.getAssignmentDetail());
                                userSession.statusChangeAction();
                                AssignmentDetail assignmentDetail = userSession.getAssignmentDetailDAO().findAssignmentDetail(userSession.getAssignmentDetail().getId());
                                userSession.setAssignmentDetail(assignmentDetail);

                                //if(userSession.getAssignmentDetail().getAssignmentId().getParentAssignmentSupervisorId() != null){

                                //    userSession.getAssignmentDetailDAO().setNewListToOldList(userSession.getAssignmentDetail().getAssignmentId().getParentAssignmentSupervisorId(), userSession.getAssignmentDetail().getCustomerId().getId());

                                //}
                            } catch (Exception e) {
                                log.error(e);
                            }

                            //update Marketing Customer status to old where press dial
                            if (userSession.getAssignmentDetail().getAssignmentId() != null && userSession.getCustomerDetail().getId() != null) {
                                try {
                                    MarketingCustomerPK mck = new MarketingCustomerPK(userSession.getAssignmentDetail().getAssignmentId().getMarketing().getId(), userSession.getCustomerDetail().getId());
                                    MarketingCustomer mc = userSession.getMarketingCustomerDAO().findMarketingCustomer(mck);
                                    mc.setNewList(Boolean.FALSE);
                                    userSession.getMarketingCustomerDAO().edit(mc);
                                } catch (Exception e) {
                                    log.error(e);
                                }

                            }

                            List<AssignmentDetail> assignmentDetails = new ArrayList<AssignmentDetail>();
                            assignmentDetails.add(userSession.getAssignmentDetail());
                            contactHistory.setAssignmentDetailCollection(assignmentDetails);
                        }
                        /*
                         if(userSession.getAssignmentDetail().getId() == null){
                         Campaign cp = null;

                         if(userSession.getAssignmentDetail().getAssignmentId() == null){
                         cp = userSession.getCampaignDAO().findCampaignInbound().get(0);
                         if(cp != null && cp.getInbound()){
                         userSession.getAssignmentDetail().setMarketingCode(cp.getMarketingInbound().getCode());
                         userSession.getAssignmentDetail().setCampaignName(cp.getName());
                         userSession.getAssignmentDetail().setAssignmentId(cp.getAssignmentInbound());
                         }
                         }

                         userSession.getAssignmentDetailDAO().create(userSession.getAssignmentDetail());
                         }
                         */
                        userSession.getContactHistoryDAO().create(contactHistory);
                        countCall(userSession);
                    }
                } catch (Exception e) {
                log.error("userSession.createContactHistory 1 :" + e);
                }
                userSession.setContactHistory(contactHistory);
        } catch (Exception e) {
            log.error("userSession.createContactHistory 2 :" + e);
        }
    }
    

    private void countCall(UserSession userSession) {
        //String telephonyTrackId = userSession.getTelephonyTrackId();
        AssignmentDetail assignmentDetail = userSession.getAssignmentDetail();
        //String contactTo = userSession.getContactTo();
//        String dialNo = userSession.getDialNo();
        
//        if (trackId != null && !trackId.equals("") && assignmentDetail != null) {//Have Telephony
        if (assignmentDetail != null) { //Don't have Telephony
            if(contactTo != null && !contactTo.equals("")){
                userSession.setDialNo(contactTo);
            }
            
            Integer max = assignmentDetail.getMaxCall() != null ? assignmentDetail.getMaxCall() : 0;
            Integer count = assignmentDetail.getCallAttempt() != null ? assignmentDetail.getCallAttempt() : 0;
            Integer max2 = assignmentDetail.getMaxCall2() != null ? assignmentDetail.getMaxCall2() : 0;
            Integer count2 = assignmentDetail.getCallAttempt2() != null ? assignmentDetail.getCallAttempt2() : 0;
            boolean dmc = assignmentDetail.isDmc();
            if (!dmc) {
                if ((count < max)) {
                    count++;
                    assignmentDetail.setCallAttempt(count);
                    try {
                        userSession.getAssignmentDetailDAO().saveCall(assignmentDetail.getId(), count);
                    } catch (Exception e) {
                    }
                } else if (count >= max) {
                    userSession.setDialShow(false);
                }
            } else if (dmc) {
                if ((count2 < max2)) {
                    count2++;
                    assignmentDetail.setCallAttempt2(count2);
                    try {
                        userSession.getAssignmentDetailDAO().saveCall2(assignmentDetail.getId(), count2);
                    } catch (Exception e) {
                    }
                } else if (count2 >= max2) {
                    userSession.setDialShow(false);
                }

            }
        }
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getContactTo() {
        return contactTo;
    }

    public void setContactTo(String contactTo) {
        this.contactTo = contactTo;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
