/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.constant.CrmConstant;
import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import javax.faces.bean.ViewScoped;

import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.MailBean;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

/**
 *
 * @author prawait
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class ActivityEditController extends BaseController  {

    private Activity activity;
    private Integer activityTypeId;
    private Integer channelId;
    private String userReceiverId;
    private String userReceiverName;
    private Integer activityId;
    private String caseId;
    private ContactCase contactCase;
    private Integer contactCaseId;
    private ActivityType activityType;
    private List activityTypeList = new ArrayList();
    private List channelList = new ArrayList();
    private static String SUCCESS = "caseHandling.xhtml"; //"customerDetail.jsf?faces-redirect=true";
    private static String ADD = "activityEdit.xhtml";
    private static String EDIT = "activityEdit.xhtml";
    private static String FAILURE = "activityEdit.xhtml";
    private static String HISTORY = "../customerHandling/activityEdit.xhtml";   
    
    private List<String[]> attachFiles = new ArrayList<String[]>();
    private List<ActivityAttachment> activityAttachmentList = new ArrayList<ActivityAttachment>();
    private String customerPath = JSFUtil.getuploadPath() + "customer/"; //JSFUtil.getRealPath() + "upload\\customer\\";
    private String tmpPath = JSFUtil.getuploadPath() +"temp/"; //JSFUtil.getRealPath() + "upload\\temp\\";
    private String from="";
    
    private Integer workflowlogId;
    private ContactCaseWorkflowLog contactCaseWorkflowLog;
    
    @ManagedProperty(value = "#{activityDAO}")
    private ActivityDAO activityDAO;
    @ManagedProperty(value = "#{activityTypeDAO}")
    private ActivityTypeDAO activityTypeDAO;
    @ManagedProperty(value = "#{channelDAO}")
    private ChannelDAO channelDAO;
    @ManagedProperty(value = "#{contactCaseDAO}")
    private ContactCaseDAO contactCaseDAO;
    @ManagedProperty(value="#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value="#{holidayDAO}")
    private HolidayDAO holidayDAO;
    @ManagedProperty(value="#{contactCaseWorkflowLogDAO}")
    private ContactCaseWorkflowLogDAO contactCaseWorkflowLogDAO;
    
    @PostConstruct
    public void initialize() {
        if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CSCounter")) {
            channelId = 2;   //inbound:Phonecall
        } else {
            channelId = 6;  //inbound:Walk-in
        }
        if(JSFUtil.getRequestParameterMap("from") != null) {
            editAction();
        }
        //JSFUtil.getUserSession().showInboundContactSummary();
    }

    public String newActivityAction(){
        setCaseId(JSFUtil.getRequestParameterMap("contactCaseId"));

        activity = new Activity();
        if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CSCounter")) {
            channelId = 2;   //inbound:Phonecall
        } else {
            channelId = 6;   //inbound:Walk-in
        }
        activityTypeId = 0;
        
        return ADD;
    }

    public String back(){
        activityId = null;
        activity = null;
        activityAttachmentList = null;
        activityType = null;
        from = null;
//        JSFUtil.redirect(SUCCESS);
        return SUCCESS;
    }

    /*
     * get Account for edit
     */
    public void editAction() {
        if (JSFUtil.getRequestParameterMap("activityId") != null) {
            activityId = Integer.parseInt(JSFUtil.getRequestParameterMap("activityId"));
        }
        
        //get contact case workflow log
        if(JSFUtil.getRequestParameterMap("workflowlogId") != null && JSFUtil.getRequestParameterMap("workflowlogId") != "") {
            workflowlogId = Integer.parseInt(JSFUtil.getRequestParameterMap("workflowlogId"));
        } else {
            workflowlogId = JSFUtil.getUserSession().getWorkflowlogId();
        }
        
        channelList = setChannelList1(channelDAO.findChannelEntities());

        if (JSFUtil.getRequestParameterMap("contactCaseId") != null) {
            contactCaseId = Integer.parseInt(JSFUtil.getRequestParameterMap("contactCaseId"));
        } else {
            contactCaseId = JSFUtil.getUserSession().getContactCaseId();
        }

        if (contactCaseId != null && contactCaseId != 0) {
            contactCase = contactCaseDAO.findContactCase(contactCaseId);
            if (contactCase.getWorkflow()) {
                if(workflowlogId != null && workflowlogId != 0) {
                    contactCaseWorkflowLog = getContactCaseWorkflowLogDAO().findContactCaseWorkflowLog(workflowlogId);
                    ContactCaseWorkflowLog currentWorkflowLog = getContactCaseWorkflowLogDAO().findContactCaseWorkflowLogBySeq(contactCase.getId(),contactCase.getWorkflowSeqNo());
                    if(currentWorkflowLog.getReceiveUsers() != null && currentWorkflowLog.getReceiveUsers().equals(JSFUtil.getUserSession().getUsers()) && contactCaseWorkflowLog.getDueDate() == null) {
                        activityTypeList = setActivityTypeList1(activityTypeDAO.findActivityType());
                    } else {
                        activityTypeList = setActivityTypeList1(activityTypeDAO.findActivityTypeNoneDelegate());
                    }
                } else {
                    activityTypeList = setActivityTypeList1(activityTypeDAO.findActivityTypeNoneDelegate());
                }
            } else {
                activityTypeList = setActivityTypeList1(activityTypeDAO.findActivityType());
            }
        }

        if (activityId == null) {
            activity = new Activity();
            activity.setActivityDate(new Date());
            if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CSCounter")) {
                channelId = 2;   //inbound:Phonecall
            } else {
                channelId = 6;  //inbound:Walk-in
            }
            activityTypeId = 0;
            if(contactCase.getSlaAccept() != null){
                activity.setSlaAcceptDate(holidayDAO.getSLADate(activity.getActivityDate(), contactCase.getSlaAccept()));
            }
            try {
                userReceiverId = contactCase.getCaseDetailId().getToUsers().getId().toString();
            } catch(Exception e) {
                userReceiverId = null;
            }
            try {
                userReceiverName = contactCase.getCaseDetailId().getToUserName();
            } catch(Exception e) {
                userReceiverName = null;
            }
            activity.setUserReceiverEmail(contactCase.getCaseDetailId().getToUserEmail());
            activity.setUserReceiverTelephone(contactCase.getCaseDetailId().getToUserTelephone());
        } else {
            activity = activityDAO.findActivity(activityId);
            channelId = activity.getChannelId() != null ? activity.getChannelId().getId() : 0;
            activityType = activity.getActivityTypeId();
            activityTypeId = activity.getActivityTypeId() != null ? activity.getActivityTypeId().getId() : 0;
            activityAttachmentList = activityDAO.findActivityAttachment(activityId);
            if(activityType.getTaskDelegate()){
                userReceiverName = activity.getUserReceiverId().getLoginName();
            }
        }


//        channelId = activity.getChannelId().getId();
//        activityTypeId = activity.getActivityTypeId().getId();
//        if(activity.getUserReceiverId() != null){
//            userReceiverName = activity.getUserReceiverId().getName() + " " + activity.getUserReceiverId().getSurname();
//        }
        if(JSFUtil.getRequestParameterMap("from") != null) {
            from = JSFUtil.getRequestParameterMap("from");
        } else {
            from = JSFUtil.getUserSession().getFrom();
        }
        
//        if(from != null && from.equals("caseHistory")) {
//            System.out.println("history");
//            return HISTORY;
//        } else {
//            System.out.println("edit");
//            return EDIT;
//        }
    }
    /**
     * pack and save customer detail.
     */
    public String saveAction() {
        try {
            Date activityDate = activity.getActivityDate();
            Date slaAcceptDate = activity.getSlaAcceptDate();
            ContactCase contactCases = getContactCaseDAO().findContactCase(contactCaseId);
            activity.setContactCase(contactCases);
            activity.setActivityTypeId(getActivityTypeDAO().findActivityType(activityTypeId));
            activity.setChannelId(getChannelDAO().findChannel(channelId));
         
            if(activityType.getTaskDelegate()) {
                if(contactCases.getWorkflow() != null && contactCases.getWorkflow() && contactCaseWorkflowLog != null ) {
                    activity.setUserReceiverId(contactCaseWorkflowLog.getUsers());
                    activity.setAssignTo(contactCaseWorkflowLog.getUserName()+" "+contactCaseWorkflowLog.getUserSurname());  
                } else {
                    if (StringUtils.isNotEmpty(userReceiverId)) {
                        Users user = getUsersDAO().findUsers(Integer.parseInt(userReceiverId));
                        activity.setUserReceiverId(user);
                        activity.setAssignTo(user.getName()+" "+user.getSurname());
                    } else {
                        activity.setUserReceiverId(null);
                    }
                }  
            } else {
                activity.setUserReceiverId(null);
                activity.setUserReceiverEmail(null);
                activity.setUserReceiverTelephone(null);
            }
            activity.setUserSenderId(getUsersDAO().findUsers(getLoginUser().getId()));
            activity.setCreateBy(getLoginUser().getName());
            activity.setCreateDate(new Date());
            if (!attachFiles.isEmpty()) {
                activity.setAttachFile(Boolean.TRUE);
            } else {
                activity.setAttachFile(Boolean.FALSE);
            }
            activityDAO.create(activity);

            //if have workflow
            if(contactCases.getWorkflow() != null && contactCases.getWorkflow() && contactCaseWorkflowLog != null && activityType.getTaskDelegate()) {
                if(contactCaseWorkflowLog.getUsers() == null)
                    contactCaseWorkflowLog = getContactCaseWorkflowLogDAO().findContactCaseWorkflowLog(workflowlogId);
                //save next due date workflow
                contactCaseWorkflowLog.setDueDate(activity.getSlaAcceptDate());  //get sla Accept Date of Next Recipient
                contactCaseWorkflowLogDAO.edit(contactCaseWorkflowLog);
                contactCases.setWorkflowSeqNo(contactCaseWorkflowLog.getSeqNo());  //save next seq of workflow in contact case
            }
            
            contactCases.setActivity(activity);
            if(activityType.getTaskDelegate()) {
                contactCases.setActivityDelegate(activity);
            }
            getContactCaseDAO().edit(contactCases);
            if(contactCaseWorkflowLog != null) {
                if(activityType.getTaskDelegate() && contactCaseWorkflowLog.getSentEmail())
                    sendMail("workflow");
            } else if(activityType.getTaskDelegate()){
                sendMail("activity");
            } 
        } catch (Exception e) {
            System.out.println("#######ADD####################" + e);
            return FAILURE;
        }

        saveAttachFile();
//        this.getCaseHandlingController().initialize();
//        String to = String.format("caseHandling.jsf?faces-redirect=true&selectId=%s&from=activity", getCaseId());
        return SUCCESS;
    }
    
    public CaseHandlingController getCaseHandlingController() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ValueExpression vex = app.getExpressionFactory().
                createValueExpression(context.getELContext(), "#{caseHandlingController}", CaseHandlingController.class);
        CaseHandlingController g = (CaseHandlingController) vex.getValue(context.getELContext());
        return g;
    }
    
    private void sendMail(String type){
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMMM yyyy HH:mm", Locale.US);
        String to = "";
        String cc = "";
        String bcc = "";
        if(type.equals("activity")) {
            to = activity.getUserReceiverEmail();
        } else if(type.equals("workflow")) {
            to = contactCaseWorkflowLog.getEmailTo();
            cc = contactCaseWorkflowLog.getEmailCc();
            bcc = contactCaseWorkflowLog.getEmailBcc();
        }
        try{
            if(!to.equals("") || !cc.equals("") || !bcc.equals("")) {
                MailBean mail = new MailBean();
                //mail.setFrom("CSR_CRC@terrabit.co.th");

                int count = 0;
                String splitMail[] = new String[2];
                String email = "";//String email = JSFUtil.getUserSession().getUsers().getEmail();
                if (email==null || StringUtils.isEmpty(email)) {
                    email = JSFUtil.getApplication().getAdminEmail();
                    count = StringUtils.countMatches(email, ",");
                    if(count>0) {
                        splitMail = email.split(",");
                    }
                }
                
                String alias = null;
                try {
                    if(count>0) {
                        alias = splitMail[0];
                    } else {
                        alias = JSFUtil.getUserSession().getUsers().getUserGroup().getName();
                        alias += "-" + JSFUtil.getUserSession().getUserName();
                    }
                } catch(Exception e) {
                    alias = null;
                }
                String priorityText = "";
                try {
                    priorityText = " : "+contactCase.getPriority() + " priority";
                } catch(Exception e) {
                    priorityText = "";
                }

                if(count>0) {
                    mail.setFrom(splitMail[1]);
                } else {
                    mail.setFrom(email);
                }
                mail.setAlias(alias);
                mail.setTo(to);
                mail.setCc(cc);
                mail.setBcc(bcc);

                //mail.setSubject("Call Center Case Delegation ID:"+activity.getContactCase().getCode()+priorityText);
                mail.setSubject(activity.getActivityTypeId().getName() + " : " + contactCase.getPriority() + "" +
                        " : " +   contactCase.getCode() + " : " +StringUtils.trim(activity.getContactCase().getCaseDetailId().getCaseTopicId().getCaseTypeId().getName()) +"" +
                        " / "+ StringUtils.trim(activity.getContactCase().getCaseDetailId().getCaseTopicId().getName()) +"" +
                        " / " + StringUtils.trim(activity.getContactCase().getCaseDetailId().getName()));

                String str = "To Whom it may concern,<br/>"
                        + "Please handle this urgent case. Below is information. Should you have further queries<br/><br/>"
                        + "<table width='700' border=\"0\" cellspacing=\"0\" cellpadding=\"3\">"
                        + "<tr>"
                        + "   <td width='150' align='right' bgcolor='#76923C'><b>Case</b> : </td>"
                        + "   <td bgcolor='#76923C'>" + StringUtils.trim(activity.getContactCase().getCaseDetailId().getCaseTopicId().getCaseTypeId().getName()) + " / "
                        + "       " + StringUtils.trim(activity.getContactCase().getCaseDetailId().getCaseTopicId().getName()) + " / "
                        + "       " + StringUtils.trim(activity.getContactCase().getCaseDetailId().getName()) + "</td>"
                        + "   <td align='right' bgcolor='#76923C'><b>Case ID</b> : </td>"
                        + "   <td bgcolor='#76923C'>" + activity.getContactCase().getCode() + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Contact Date</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(sdf.format(activity.getContactCase().getContactDate())) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Customer Name</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getCustomerId().getName())
                        + " " + StringUtils.trim(activity.getContactCase().getCustomerId().getSurname()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Contact No.</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getContactHistory() != null ? activity.getContactCase().getContactHistory().getContactTo() : "") + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Service Type</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getServiceType().getName()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Business Unit</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getBusinessUnit().getName()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Location</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getLocationName() != null ? activity.getContactCase().getLocationName() : "") + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Description</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getDescription()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Remark</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getRemark()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Priority</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getPriority()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Status</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getStatus()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Channel</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getChannelId().getType()) + ":" + StringUtils.trim(activity.getContactCase().getChannelId().getName()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Due Date</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(sdf.format(activity.getContactCase().getSlaCloseDate())) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Create Case By</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getCreateBy()) + "</td>"
                        + "</tr>"
                        + "</table>"
                        + "<br/><br/>"
                        + "<table width='700' border=\"0\" cellspacing=\"0\" cellpadding=\"3\">"
                        + "<tr>"
                        + "   <td colspan='6' bgcolor='#C4BC96'><b>Activity</b></td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td width='150' align='right'><b>Activity Date</b> : </td>"
                        + "   <td>" + StringUtils.trim(sdf.format(activity.getActivityDate())) + "</td>"
                        + "   <td align='right'><b>Channel</b> : </td>"
                        + "   <td>" + StringUtils.trim(activity.getChannelId().getName())+ "</td>"
                        + "   <td align='right'><b>Activity Type</b> : </td>"
                        + "   <td>" + StringUtils.trim(activity.getActivityTypeId().getName()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Description</b> : </td>"
                        + "   <td colspan='5'>" + StringUtils.trim(activity.getDescription()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Create Activity By</b> : </td>"
                        + "   <td colspan='5'>" + StringUtils.trim(activity.getCreateBy()) + "</td>"
                        + "</tr>"
                        + "</table>"
                        + "";

                String text = str;
                mail.setContent(text);
                mail.sendMail();
            }
        } catch (Exception e) {
            //log.error(e.toString());
        }
    }
 
    private void saveAttachFile(){
        activityDAO.deleteActivityAttachment(activity.getId());
        if (!attachFiles.isEmpty()) {
            for (String[] stra : attachFiles) {
                String fName = getRealFileName(stra[1]);
                File fTmp = new File(stra[1]);
                if (fTmp.exists()) {
                    ActivityAttachment activityAttachment = new ActivityAttachment();
                    activityAttachment.setActivity(activity);
                    activityAttachment.setCreateBy(getLoginUser().getName());
                    activityAttachment.setCreateDate(new Date());
                    activityAttachment.setFilename(fName);
                    activityDAO.createActivityAttachment(activityAttachment);
                }
            }
        }
        moveFile();
    }

    private void moveFile(){
        if (!attachFiles.isEmpty()) {
            String path = getActivityPath();
            Boolean create = true;
            File fDir = new File(path);
            if (!fDir.exists()) {
                create = fDir.mkdir();
            }
            if (create) {
                for (String[] stra : attachFiles) {
                    String fName = getRealFileName(stra[1]);
                    File fTmp = new File(stra[1]);
                    if (fTmp.exists()) {
                        File fCust = new File(path + fName);
                        fTmp.renameTo(fCust);
                    }
                }
            }
        }
    }

    private String getActivityPath(){
        String path = "";
        File fDir = new File(customerPath + contactCase.getCustomerId().getId());
        if (!fDir.exists()) {
            fDir.mkdir();
        }
        fDir = new File(customerPath + contactCase.getCustomerId().getId() + "/case");
        if (!fDir.exists()) {
            fDir.mkdir();
        }
        fDir = new File(customerPath + contactCase.getCustomerId().getId() + "/case/" + contactCase.getId());
        if (!fDir.exists()) {
            fDir.mkdir();
        }
        fDir = new File(customerPath + contactCase.getCustomerId().getId() + "/case/" + contactCase.getId() + "/activity");
        if (!fDir.exists()) {
            fDir.mkdir();
        }
        fDir = new File(customerPath + contactCase.getCustomerId().getId() + "/case/" + contactCase.getId() + "/activity/" + activity.getId());
        if (!fDir.exists()) {
            fDir.mkdir();
        }
        path = customerPath + contactCase.getCustomerId().getId() + "/case/" + contactCase.getId() + "/activity/" + activity.getId() + "/";

        return path;
    }

    public void selectUserListener() {
        userReceiverName = JSFUtil.getRequestParameterMap("userReceiverName");
        userReceiverId = JSFUtil.getRequestParameterMap("userReceiverId");
        if (!userReceiverId.isEmpty()) {
            Users userTmp = usersDAO.findUsers(Integer.parseInt(userReceiverId));
            activity.setUserReceiverEmail(userTmp.getEmail());
            activity.setUserReceiverTelephone(userTmp.getMobile());
        } else {
            userReceiverId = null;
            userReceiverName = null;
        }
    }

    public void clearUserListener(ActionEvent event) {
        userReceiverId = null;
        userReceiverName = null;
    }

    public void activityTypeListener() {
        if(activityTypeId != null && activityTypeId != 0){
            activityType = activityTypeDAO.findActivityType(activityTypeId);
        }
    }
    
    private boolean saveToFile(InputStream inputStream, File file) {    
        FileOutputStream fos = null;
        BufferedInputStream bis = null;   
        boolean result = false;        
        try { 
            byte[] buffer = new byte[1024];
            fos = new FileOutputStream(file);            
            bis = new BufferedInputStream(inputStream, buffer.length);            
            int numRead = -1;            
            while ((numRead = bis.read(buffer, 0, buffer.length)) != -1) {                
                fos.write(buffer, 0, numRead);
            }          
            result = true;        
        } catch (IOException ex) {            
            log.error(ex, ex);
            log.error("Exception during download file " + file.getAbsolutePath());        
        } finally {            
            try {                
                fos.close();
            } catch (IOException ex) {                
                log.error(ex, ex);
                log.error("Exception during closing file output stream " + file.getAbsolutePath());            
            }            
            try {                
                bis.close();
            } catch (IOException ex) {                
                log.error(ex, ex);
                log.error("Exception during closing buffered input stream " + file.getAbsolutePath());            
            }   
        }    
        return result; 
    }

    public void uploadListener(FileUploadEvent event) throws Exception {
        String[] stra = new String[3];
        UploadedFile item = event.getUploadedFile();
        String str = item.getName();
        str = getRealFileName(str);
        stra[0] = str;//file name
        str = JSFUtil.getDateFormat1(new Date()) + "_" + str;
        stra[2] = str;
        str = tmpPath + str;
        stra[1] = str;//full path file name
        File file = new File(str);
        
        if(saveToFile(item.getInputStream(),file)) {  
            attachFiles.add(stra);
        }
    }

    public String getRealFileName(String str) {
        String str1 = str;
        if(str.lastIndexOf("/") != -1){
            str1 = StringUtils.substring(str, (str.lastIndexOf("/")) + 1, str.length());
        }else if(str.lastIndexOf("\\") != -1){
            str1 = StringUtils.substring(str, (str.lastIndexOf("\\")) + 1, str.length());
        }
        return str1;
    }

    public void removeFileTemp(ActionEvent event) {
        String rmFile = StringUtils.trim(getRequest("rmFile"));
        File f = new File(rmFile);
        if (f.exists()) {
            f.delete();
        }
        List<String[]> list = new ArrayList<String[]>();
        for (String[] s : attachFiles) {
            if (!StringUtils.equals(rmFile, StringUtils.trim(s[1]))) {
                list.add(s);
            }
        }
        attachFiles = list;
    }

    public List setActivityTypeList1(List<ActivityType> list) {
        List activityTypeValueList = new ArrayList();
        activityTypeValueList.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        for (ActivityType activityType : list) {
            if (activityType.getId() != null) {
                activityTypeValueList.add(new SelectItem(activityType.getId(), activityType.getName()));
            }
        }
        return activityTypeValueList;
    }

    public List setChannelList1(List<Channel> list) {
        List channelValueList = new ArrayList();
        channelValueList.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        for (Channel channel : list) {
            if (channel.getId() != null) {
                channelValueList.add(new SelectItem(channel.getId(),channel.getType()+":"+ channel.getName()));
            }
        }
        return channelValueList;
    }

    /**
     * @return the activity
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * @return the activityTypeList
     */
    public List getActivityTypeList() {
        return activityTypeList;
    }

    /**
     * @param activityTypeList the activityTypeList to set
     */
    public void setActivityTypeList(List activityTypeList) {
        this.activityTypeList = activityTypeList;
    }

    /**
     * @return the channelList
     */
    public List getChannelList() {
        return channelList;
    }

    /**
     * @param channelList the channelList to set
     */
    public void setChannelList(List channelList) {
        this.channelList = channelList;
    }

    /**
     * @return the activityDAO
     */
    public ActivityDAO getActivityDAO() {
        return activityDAO;
    }

    /**
     * @param activityDAO the activityDAO to set
     */
    public void setActivityDAO(ActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    /**
     * @return the activityTypeDAO
     */
    public ActivityTypeDAO getActivityTypeDAO() {
        return activityTypeDAO;
    }

    /**
     * @param activityTypeDAO the activityTypeDAO to set
     */
    public void setActivityTypeDAO(ActivityTypeDAO activityTypeDAO) {
        this.activityTypeDAO = activityTypeDAO;
    }

    /**
     * @return the channelDAO
     */
    public ChannelDAO getChannelDAO() {
        return channelDAO;
    }

    /**
     * @param channelDAO the channelDAO to set
     */
    public void setChannelDAO(ChannelDAO channelDAO) {
        this.channelDAO = channelDAO;
    }

    /**
     * @return the activityTypeId
     */
    public Integer getActivityTypeId() {
        return activityTypeId;
    }

    /**
     * @param activityTypeId the activityTypeId to set
     */
    public void setActivityTypeId(Integer activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    
    /**
     * @return the channnelId
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * @param channelId the channnelId to set
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * @return the userReceiverId
     */
    public String getUserReceiverId() {
        return userReceiverId;
    }

    /**
     * @param userReceiverId the userReceiverId to set
     */
    public void setUserReceiverId(String userReceiverId) {
        this.userReceiverId = userReceiverId;
    }

    /**
     * @return the userReceiver
     */
    public String getUserReceiverName() {
        return userReceiverName;
    }

    /**
     * @param userReceiverName the userReceiver to set
     */
    public void setUserReceiverName(String userReceiverName) {
        this.userReceiverName = userReceiverName;
    }

    /**
     * @return the contactCaseDAO
     */
    public ContactCaseDAO getContactCaseDAO() {
        return contactCaseDAO;
    }

    /**
     * @param contactCaseDAO the contactCaseDAO to set
     */
    public void setContactCaseDAO(ContactCaseDAO contactCaseDAO) {
        this.contactCaseDAO = contactCaseDAO;
    }

    /**
     * @return the activityId
     */
    public Integer getActivityId() {
        return activityId;
    }

    /**
     * @param activityId the activityId to set
     */
    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    /**
     * @return the caseId
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     * @param caseId the caseId to set
     */
    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    /**
     * @return the usersDAO
     */
    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    /**
     * @param usersDAO the usersDAO to set
     */
    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public Integer getContactCaseId() {
        return contactCaseId;
    }

    public void setContactCaseId(Integer contactCaseId) {
        this.contactCaseId = contactCaseId;
    }

    public ContactCase getContactCase() {
        return contactCase;
    }

    public void setContactCase(ContactCase contactCase) {
        this.contactCase = contactCase;
    }

    public List<String[]> getAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(List<String[]> attachFiles) {
        this.attachFiles = attachFiles;
    }

    public List<ActivityAttachment> getActivityAttachmentList() {
        return activityAttachmentList;
    }

    public void setActivityAttachmentList(List<ActivityAttachment> activityAttachmentList) {
        this.activityAttachmentList = activityAttachmentList;
    }

    public HolidayDAO getHolidayDAO() {
        return holidayDAO;
    }

    public void setHolidayDAO(HolidayDAO holidayDAO) {
        this.holidayDAO = holidayDAO;
    }
    
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getUploadpath() {
        return JSFUtil.getuploadPath();
    }
    
    public void setUploadpath() {}
    
    //workflow

    public ContactCaseWorkflowLogDAO getContactCaseWorkflowLogDAO() {
        return contactCaseWorkflowLogDAO;
    }

    public void setContactCaseWorkflowLogDAO(ContactCaseWorkflowLogDAO contactCaseWorkflowLogDAO) {
        this.contactCaseWorkflowLogDAO = contactCaseWorkflowLogDAO;
    }

    public ContactCaseWorkflowLog getContactCaseWorkflowLog() {
        return contactCaseWorkflowLog;
    }

    public void setContactCaseWorkflowLog(ContactCaseWorkflowLog contactCaseWorkflowLog) {
        this.contactCaseWorkflowLog = contactCaseWorkflowLog;
    }

    public Integer getWorkflowlogId() {
        return workflowlogId;
    }

    public void setWorkflowlogId(Integer workflowlogId) {
        this.workflowlogId = workflowlogId;
    }
    
}
