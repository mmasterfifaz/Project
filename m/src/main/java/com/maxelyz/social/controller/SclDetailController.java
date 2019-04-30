/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller;

import com.maxelyz.core.model.dao.KnowledgebaseDAO;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.social.model.dao.SclUsersDAO;
import com.maxelyz.social.model.dao.SoAccountDAO;
import com.maxelyz.social.model.dao.SoAccountUserDAO;
import com.maxelyz.social.model.dao.SoActivityLogDAO;
import com.maxelyz.social.model.dao.SoActivityTypeDAO;
import com.maxelyz.social.model.dao.SoCaseTypeDAO;
import com.maxelyz.social.model.dao.SoChannelDAO;
import com.maxelyz.social.model.dao.SoIgnoreReasonDAO;
import com.maxelyz.social.model.dao.SoMessageDAO;
import com.maxelyz.social.model.dao.SoMessageHistoryDAO;
import com.maxelyz.social.model.dao.SoMsgCasetypeMapDAO;
import com.maxelyz.social.model.dao.SoMsgUserAssigntimeDAO;
import com.maxelyz.social.model.dao.SoMsgUserWorkingtimeDAO;
import com.maxelyz.social.model.dao.SoTransferReasonDAO;
import com.maxelyz.social.model.dao.SoWorkflowInstanceDAO;
import com.maxelyz.social.model.entity.SclUsers;
import com.maxelyz.social.model.entity.SoAccount;
import com.maxelyz.social.model.entity.SoActivityLog;
import com.maxelyz.social.model.entity.SoActivityType;
import com.maxelyz.social.model.entity.SoCaseType;
import com.maxelyz.social.model.entity.SoChannel;
import com.maxelyz.social.model.entity.SoEmailMessage;
import com.maxelyz.social.model.entity.SoEmailSignature;
import com.maxelyz.social.model.entity.SoIgnoreReason;
import com.maxelyz.social.model.entity.SoMessage;
import com.maxelyz.social.model.entity.SoMsgCasetypeMap;
import com.maxelyz.social.model.entity.SoMsgUserAssigntime;
import com.maxelyz.social.model.entity.SoMsgUserWorkingtime;
import com.maxelyz.social.model.entity.SoServiceOutgoingAccount;
import com.maxelyz.social.model.entity.SoTransferReason;
import com.maxelyz.social.model.entity.SoUserSignature;
import com.maxelyz.social.model.entity.SoWorkflowInstance;
import com.maxelyz.social.model.entity.SoWorkflowInstanceLog;
import com.maxelyz.utils.FileUploadBean;
import com.maxelyz.utils.JSFUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

//test
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.mail.*;
import javax.mail.event.*;
import javax.mail.internet.*;
/**
 *
 * @author ukritj
 */
@ManagedBean
@ViewScoped
public class SclDetailController {
    
    private Integer soMessageId;
    private static Logger log = Logger.getLogger(SclAssignmentController.class);
    private static final String REFRESH = "saleapproval.xhtml?faces-redirect=true";
    
    //unix
    //private static final String emailPath = "/Users/ukritj/maxar/userfiles/emailgatewaystore/";
    //private static final String separate = "/";
    
    //windows
    private String emailPath = JSFUtil.getApplication().getEmailFilePath();
    private String separate = JSFUtil.getApplication().getSeparatePath();
    
    private String pageType;
    private Date fromDate, toDate;
    private String tmrName;
    private String templateId;
    private String replyMode;
    private String replyDesc;
    private String emotion;
    private String priority;
    private String caseStatus;
    private String caseType;
    private String kbTag;
//    private String attachment;

//    private String campaignId;
//    private String productId;
    private String accountUserId;

    private String userNote;
    private String selectedTab;
    private String selectedMsgID;
    
    private MimeMessage msg;
    private boolean textIsHtml;
    
    private String emailSubject;
    private String emailFrom;
    private String emailTo;
    private String emailCc;
    private String emailBody;
    private String emailBodyText;
    private String emailBodyHtml;
    private Date emailDate;
    private List<String[]> attachmentList;
    private List<String> bodyList;
    
    private String psubject;
    private String pto;
    private String pcc;
    private String pbcc;
    private String pbody = "";
    private String paction;
    private List<SoEmailSignature> soEmailSignatureList;
    private String signatureId;
    private String signatureString;
    
        
    private String emailBodyTextParent;
    private String emailBodyHtmlParent;
    private MimeMessage msgParent;
    private String emailSubjectParent;
    private Date emailDateParent;
    private String emailFromParent;
    private String emailToParent;
    private String emailCcParent;
    private List<String[]> attachmentListParent;
    private List<String> bodyListParent;
    private boolean textIsHtmlParent;
    private String emailPriority;
    
        //Address Book
    private String selectedEmailString;
    private boolean TO = false;
    private boolean CC  = false;
    private boolean BCC = false;
    
    private boolean bTransfer = false;
        
    
//    private List<SclMessage> sclMessageNews;
    private List<SoMessage> soMessageAssignments;
    private List<SoMessage> soMessageSelects;
    private List<SoMessage> soMessageUsers;
    private SoMessage soMessage;
    private SoMessage soMessageParent;
    private SoMessage soMessageDraft;
    private SclUsers sclUsers;
    private SoUserInfoDTO soUserInfoDTO;
    @ManagedProperty(value = "#{soMessageDAO}")
    private SoMessageDAO soMessageDAO;
    @ManagedProperty(value = "#{soMessageHistoryDAO}")
    private SoMessageHistoryDAO soMessageHistoryDAO;
    @ManagedProperty(value = "#{sclUsersDAO}")
    private SclUsersDAO sclUsersDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{soCaseTypeDAO}")
    private SoCaseTypeDAO soCaseTypeDAO;
    @ManagedProperty(value = "#{knowledgebaseDAO}")
    private KnowledgebaseDAO knowledgebaseDAO;
    @ManagedProperty(value = "#{soAccountUserDAO}")
    private SoAccountUserDAO soAccountUserDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{soAccountDAO}")
    private SoAccountDAO soAccountDAO;
    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{soChannelDAO}")
    private SoChannelDAO soChannelDAO;
    @ManagedProperty(value = "#{soActivityLogDAO}")
    private SoActivityLogDAO soActivityLogDAO;
    @ManagedProperty(value = "#{soIgnoreReasonDAO}")
    private SoIgnoreReasonDAO soIgnoreReasonDAO;
    @ManagedProperty(value = "#{soWorkflowInstanceDAO}")
    private SoWorkflowInstanceDAO soWorkflowInstanceDAO;
    @ManagedProperty(value = "#{soMsgCasetypeMapDAO}")
    private SoMsgCasetypeMapDAO soMsgCasetypeMapDAO;
    @ManagedProperty(value = "#{soActivityTypeDAO}")
    private SoActivityTypeDAO soActivityTypeDAO;
    @ManagedProperty(value = "#{soTransferReasonDAO}")
    private SoTransferReasonDAO soTransferReasonDAO;
    @ManagedProperty(value = "#{soMsgUserAssigntimeDAO}")
    private SoMsgUserAssigntimeDAO soMsgUserAssigntimeDAO;
    @ManagedProperty(value = "#{soMsgUserWorkingtimeDAO}")
    private SoMsgUserWorkingtimeDAO soMsgUserWorkingtimeDAO;

//    List<SoCommentInfoDTO> commentInfos = new ArrayList<SoCommentInfoDTO>();
//    SoPostInfoDTO postInfo;
    List<SoContentDetailDTO> commentInfos = new ArrayList<SoContentDetailDTO>();
    SoContentDetailDTO postInfo;
    SoUserInfoDTO userInfo;

    Map<String, Integer> accountUserList;
    private Map<String, File> listFileUpload;
    private javax.servlet.http.Part fileUpload;
    private List<String[]> filesUpload;
    private List<String[]> filesUploadDraft;
    private String fileNameToDelete;
    
    private FileUploadBean fileUploadBean = new FileUploadBean(JSFUtil.getuploadPath(), "temp");
    
    private List<SoAccount> soAccountList = new ArrayList<SoAccount>();
    private List<SoChannel> soChannelList = new ArrayList<SoChannel>();
    private List<SoServiceOutgoingAccount> outList;
    private String outName;
    
    private List<SoCaseType> soCaseTypeList;
    private Integer soCaseTypeId;
    private List<SoCaseType> soCaseTypeSubList;
    private Integer soCaseTypeSubId;
    
    private List<SoMessage> contactHistoryList;
    
    private List<SoUserSignature> soUserSignatureList;
    private List<String[]> signatureList;
    
    private Integer ignoreId;
    private List<SoIgnoreReason> ignoreList;
    private Integer caseTypeIgnoreId;
    private List<SoCaseType> caseTypeIgnoreList;
    private String ignoreRemark;
    
    //tranfer
    private String transferTo;
    private Integer transferToServiceId;
    private Integer transferToSupervisorId;
    private Integer transferToUserId;
    private Integer transferReasonId;
    private String remark;
    private List<Users> transferUserList = new ArrayList<Users>();
    private List<Users> transferSupervisorList = new ArrayList<Users>();
    private List<SoTransferReason> transferReasonList = new ArrayList<SoTransferReason>();
    private String transferRemark;
    
    //approval
    private String remarkApproval;
    
    //CaseType
    private List<SoMsgCasetypeMap> soMsgCasetypeMapList;
    
    private boolean showPanelSendMail;
    
    private List<String[]> contentIdList;
    
    private Date ignoredDate;
    private String ignoredReason;
    private String ignoredCaseType;
    private String ignoredCaseTopic;
    private String ignoredRemark;
    private String ignoredBy;
    
    private Integer caseTypeSize;
    
    private String sessionId;
    private SoMsgUserWorkingtime workingTime;
    
    private String transferFrom;
    private Date transferDate;
    private String transferFromReason;
    private String transferFromRemark;
    
    private boolean keepWorkingTime;
    
    @PostConstruct
    public void initialize() {
        
        emailPath = JSFUtil.getApplication().getEmailFilePath();
        separate = JSFUtil.getApplication().getSeparatePath();
        if(JSFUtil.getRequestParameterMap("soMessageId") != null){
            soMessageId = Integer.parseInt(JSFUtil.getRequestParameterMap("soMessageId"));
        }
        filesUploadDraft = new ArrayList<String[]>();
        initEmail();
        Integer soAccountId = 0;
        Integer soAccountParentId = 0;
        outList = soMessageDAO.findAllSoServiceOutgoingAccount();
        soCaseTypeList = soCaseTypeDAO.findSoCaseTypeStatus();
        soCaseTypeSubList = new ArrayList<SoCaseType>();
        initSoMsgCasetypeMapList();
        caseTypeIgnoreList = soCaseTypeList;
        //ignoreList = soMessageDAO.findAllSoIgnoreReason();
        
        if(soMessageId != null && soMessageId != 0){
            soMessage = soMessageDAO.findSoMessage(soMessageId);
            if(soMessage != null){
                this.initWorkingTime();
                if(keepWorkingTime){
                    if(sessionId == null){
                        sessionId = soMessageId.toString() + JSFUtil.getUserSession().getUsers().getId().toString() + (new Date()).getTime();
                        if(workingTime == null){
                            workingTime = new SoMsgUserWorkingtime();
                            workingTime.setSessionId(sessionId);
                            workingTime.setSoMessage(soMessage);
                            workingTime.setUsers(JSFUtil.getUserSession().getUsers());
                            workingTime.setWorkingStart(new Date());
                            try{
                                soMsgUserWorkingtimeDAO.create(workingTime);
                            }catch(Exception e){
                                log.error(e);
                            }
                        }
                    }
                }
            }
            if(soMessage != null && soMessage.getSoAccount() != null){
                soAccountId = soMessage.getSoAccount().getId();
                contactHistoryList = soMessageDAO.findByEmail(soMessage.getUser_id());
                initSignature();
            }
            if(soMessage.getParentSoMessage() != null){
                soMessageParent = soMessage.getParentSoMessage();
                if(soMessageParent != null && soMessageParent.getSoAccount() != null){
                    soAccountParentId = soMessageParent.getSoAccount().getId();
                    if(soMessageParent.getCasetype_enum_id() != null && !soMessageParent.getCasetype_enum_id().isEmpty()){
                        soCaseTypeId = Integer.parseInt(soMessageParent.getCasetype_enum_id());
                    }
                }
            }
            
            //find draft mail
            soMessageDraft = soMessageDAO.findDraft(soMessage.getId());
            if(soMessageDraft != null) {
                SoEmailMessage soem = soMessageDraft.getSoEmailMessage();
                pto = soem.getEmailTo();
                pcc = soem.getEmailCc();
                pbcc = soem.getEmailBcc();
                psubject = soem.getSubject();
                pbody = soem.getBody();
                
                //list file in draft
                if(filesUploadDraft == null) filesUploadDraft = new ArrayList<String[]>();
                File file = null;
                String fName = "";
                String fNameMap = "";
                if(soem.getAttachment() != null && !soem.getAttachment().isEmpty()){
                    String str = soem.getAttachment();
                    String strMap = soem.getAttachmentMap();
                    if(str.indexOf(",") != -1 && strMap.indexOf(",") != -1){
                        StringTokenizer stk = new StringTokenizer(str, ",");
                        StringTokenizer stkMap = new StringTokenizer(strMap, ",");
                        while (stk.hasMoreTokens() && stkMap.hasMoreTokens()) {
                            String[] stra = new String[2];
                            fName = (String) stk.nextToken();
                            fNameMap = (String) stkMap.nextToken();
                            stra[0] = fName;
                            stra[1] =fNameMap;
                            filesUploadDraft.add(stra);
                        }
                    }else if(str.indexOf(",") == -1 && strMap.indexOf(",") == -1){
                        String[] stra1 = new String[2];
                        stra1[0] = str;
                        stra1[1] = strMap;
                        filesUploadDraft.add(stra1);
                    }
                }
            }
            
            SoMsgUserAssigntime obj = soMessageDAO.findSoMsgUserAssigntimeBySoMessageId(soMessageId);
            if(obj != null && obj.getType() != null && obj.getType().equals("TR")){
                bTransfer = true;
                transferFrom = obj.getPreviousUsers() != null ? obj.getPreviousUsers().getName() + ' ' + obj.getPreviousUsers().getSurname() : "";
                transferDate = obj.getCreateDate();
                SoActivityLog act = soActivityLogDAO.findSoActivityTransferBySoMessage(soMessageId);
                if(act != null){
                    transferFromReason = act.getReason();
                    transferFromRemark = act.getRemark();
                }
            }
                
            this.displaysoMsgCasetypeMapList();
            emailPriority = soMessage.getPriority_enum_id();
        }        
        try {
            if(soMessage.getCase_status().equals("AS") && soMessage.getLastReceiveByUsers().getId().intValue() == JSFUtil.getUserSession().getUsers().getId().intValue()){
                //soMessage.setReceive_by_name(JSFUtil.getUserSession().getUserName());
                //soMessage.setReceive_date(new Date());
                soMessage.setCase_status("PS");
                soMessage.setUpdate_date(new Date());
                soMessage.setUpdate_by(JSFUtil.getUserSession().getUsers().getId());
                soMessageDAO.edit(soMessage);
                
                //Processing Log id = 4
                this.toSoActivityLog(new SoActivityType(4), soMessage, "", "");
            }
            
            if(soMessage.getCase_status().equals("IG")){
                if(soMessage.getSoActivityLogCollection() != null) {
                    for(SoActivityLog soActivityLog : soMessage.getSoActivityLogCollection()) {
                        if(soActivityLog.getSoActivityType() != null && soActivityLog.getSoActivityType().getId().intValue() == 7) {
                            ignoredReason = soActivityLog.getReason();
                            ignoredRemark = soActivityLog.getRemark();
                            ignoredDate = soActivityLog.getLogDate();
                            ignoredBy = soActivityLog.getCreateBy().getName() + ' ' + soActivityLog.getCreateBy().getSurname();
                            break;
                        }
                    }
                }
                
                if(soMessage.getSoMsgCasetypeMapCollection() != null){
                    for(SoMsgCasetypeMap ign : soMessage.getSoMsgCasetypeMapCollection()){
                        if(ign.getSoCaseTypeSub() != null){
                            ignoredCaseTopic = ign.getSoCaseTypeSub().getName();
                            ignoredCaseType = ign.getSoCaseTypeSub().getSoCaseType().getName();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        
        try{
            this.initContentId();
            displayEmail(new File(emailPath + soAccountId + separate + soMessage.getId() + separate + soMessage.getId() + ".eml"));
            if(soMessageParent != null) displayEmailParent(new File(emailPath + soAccountParentId + separate + soMessageParent.getId() + separate + soMessageParent.getId() + ".eml"));
            
            //displayEmail(new File(emailPath + soAccountId + "/" + soMessage.getId() + "/" + soMessage.getId() + ".eml"));
            //if(soMessageParent != null) displayEmailParent(new File(emailPath + soAccountParentId + "/" + soMessageParent.getId() + "/" + soMessageParent.getId() + ".eml"));
           
            //displayEmail(new File("/Users/ukritj/temp/008.eml"));
            //if(soMessageParent != null) displayEmailParent(new File("/Users/ukritj/temp/013.eml"));
            
            this.replyAction();
            this.initShowEditWfSup();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void initWorkingTime(){
        keepWorkingTime = false;
        try{
            if(soMessage.getLastReceiveByUsers() != null){
                if(soMessage.getLastReceiveByUsers().getId().intValue() == JSFUtil.getUserSession().getUsers().getId().intValue()
                        && !soMessage.getCase_status().equals("CL")
                        && !soMessage.getCase_status().equals("IG")
                        && soMessage.getSource_subtype_enum_id().equals("EM_IN")){
                    keepWorkingTime = true;
                }
            }
        }catch(Exception e){
            log.error(e);
        }
    }
    
    public void saveWorkingTime(){
        try{
            if(keepWorkingTime){
                Date endDate = new Date();
                Long diff = new Long(0);
                if(workingTime != null){
                    Date startDate = workingTime.getWorkingStart();
                    if(startDate != null && endDate != null){
                        diff = (endDate.getTime() - startDate.getTime())/1000;
                    }
                    workingTime.setWorkingEnd(endDate);
                    workingTime.setDurationMilis(Integer.valueOf(diff.intValue()));
                    soMsgUserWorkingtimeDAO.edit(workingTime);
                }
            }
        }catch(Exception e){
            log.error(e);
        }
    }
    
    public void transferUserListListener() {
        if (transferTo != null) {
            if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Supervisor")){
                if (transferTo.equals("agent")) {
                    transferUserList = usersDAO.findUserUnderParentId(JSFUtil.getUserSession().getUsers());
                }else if (transferTo.equals("supervisor")) {
                    transferSupervisorList = usersDAO.findSoAgentByGroupId(JSFUtil.getUserSession().getUsers().getUserGroup().getId());
                    if (transferSupervisorList != null) {
                        List<Users> tmp = new ArrayList<Users>();
                        for (Users u : transferSupervisorList) {
                            if (u.getId().intValue() != JSFUtil.getUserSession().getUsers().getId().intValue()) {
                                tmp.add(u);
                            }
                        }
                        transferSupervisorList = tmp;
                    }
                }
            }else if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Agent")){
                if (transferTo.equals("agent")) {
                    transferUserList = usersDAO.findSoAgentByGroupId(JSFUtil.getUserSession().getUsers().getUserGroup().getId());
                    if (transferUserList != null) {
                        List<Users> tmp = new ArrayList<Users>();
                        for (Users u : transferUserList) {
                            if (u.getId().intValue() != JSFUtil.getUserSession().getUsers().getId().intValue()) {
                                tmp.add(u);
                            }
                        }
                        transferUserList = tmp;
                    }
                }else if (transferTo.equals("supervisor")) {
                    transferSupervisorList = usersDAO.findSupervisorByParentId(JSFUtil.getUserSession().getUsers().getUserGroup().getParentId());
                }
            }
        }
    }
    
    public void transferAction(){
        try{
            
            saveDraftAction();
            
            Users u = null;
            SoTransferReason st = null;
            String stStr = "";
            if(transferTo != null){
                if(transferTo.equals("supervisor")){
                    u = usersDAO.findUsers(transferToSupervisorId);
                }else if(transferTo.equals("agent")){
                    u = usersDAO.findUsers(transferToUserId);
                }

                if(transferReasonId != null){
                    st = soTransferReasonDAO.findSoTransferReason(transferReasonId);
                    if(st != null) stStr = st.getName();
                }

                soMessageDAO.assignToUser(soMessage.getId(), u);
                soMsgUserAssigntimeDAO.closeTransfer(soMessage.getId(), JSFUtil.getUserSession().getUsers().getId());
                soMsgUserAssigntimeDAO.insertTransfer(soMessage.getId(), u.getId(), JSFUtil.getUserSession().getUsers().getId(), JSFUtil.getUserSession().getUsers().getId());
                soActivityLogDAO.insertActivityLog(soMessage.getId(), JSFUtil.getUserSession().getUsers().getId(), 11, stStr, transferRemark);

            }
            saveWorkingTime();
        }catch(Exception e){
            log.error(e);
        }
        JSFUtil.redirect(JSFUtil.getServletContext().getContextPath() + "/social/sclassignment.jsf?pagetype=" + JSFUtil.getUserSession().getSubPage());
    }
    
    private void toSoActivityLog(SoActivityType soActivityType, SoMessage sm, String rm, String rs){
        try{
            SoActivityLog act = new SoActivityLog();
            act.setLogDate(new Date());
            SoActivityType sa = soActivityTypeDAO.findSoActivityType(soActivityType.getId());
            if(sa != null){
                act.setSoActivityType(sa);
                act.setLogType(sa.getName());
            }
            act.setCreateBy(JSFUtil.getUserSession().getUsers());
            act.setSoMessage(sm);
            act.setRemark(rm);
            act.setReason(rs);
            soActivityLogDAO.create(act);
        
        }catch(Exception e){
            log.error(e);
        }
    }
    
    private void initContentId(){
        contentIdList = new ArrayList<String[]>();
    }
    
    private void initShowEditWfSup(){
        if(soMessage.getCase_status().equals("WF")){
            if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Supervisor")){
                showPanelSendMail = true;
            }else{
                showPanelSendMail = false;
            }
        }else if(soMessage.getCase_status().equals("CL") || soMessage.getCase_status().equals("IG")) {
            showPanelSendMail = false;
        }else if(soMessage.getLastReceiveByUsers().getId().intValue() == JSFUtil.getUserSession().getUsers().getId().intValue()){
            showPanelSendMail = true;
        }
    }
    
    private void displaysoMsgCasetypeMapList(){
        try{
            soMsgCasetypeMapList = soMsgCasetypeMapDAO.findBySoMessageId(soMessage.getId());
            //soMsgCasetypeMapList = new ArrayList<SoMsgCasetypeMap>();
            //if(soMessage != null && soMessage.getSoMsgCasetypeMapCollection() != null && !soMessage.getSoMsgCasetypeMapCollection().isEmpty()){
            //    for(SoMsgCasetypeMap c : soMessage.getSoMsgCasetypeMapCollection()){
            //        soMsgCasetypeMapList.add(c);
            //    }
            //}
        }catch(Exception e){
            log.error(e);
        }
    }
    
    public void insertSoMsgCasetypeMapListener() {
        //int seqNo = Integer.parseInt(JSFUtil.getRequestParameterMap("seqNo"));
        boolean exist = false;
        for(SoMsgCasetypeMap obj : soMsgCasetypeMapList){
            if(soCaseTypeSubId != null){
                if(obj.getSoCaseTypeSub() != null && obj.getSoCaseTypeSub().getId().intValue() == soCaseTypeSubId){
                    exist = true;
                    break;
                }
            }else if(soCaseTypeId != null){
                if(obj.getSoCaseType().getId().intValue() == soCaseTypeId && obj.getSoCaseTypeSub() == null){
                    exist = true;
                    break;
                }
            }
        }
        if(!exist){
            if(soCaseTypeSubId != null){
                SoCaseType sub = soCaseTypeDAO.findSoCaseType(soCaseTypeSubId);
                if(sub != null) {
                    SoMsgCasetypeMap c = new SoMsgCasetypeMap();
                    c.setSoMessage(soMessage);
                    c.setCaseSeq(soMsgCasetypeMapList.size() + 1);
                    c.setSoCaseType(sub.getSoCaseType());
                    c.setCaseTypeCode(sub.getSoCaseType().getCode());
                    c.setSoCaseTypeSub(sub);
                    c.setCaseSubtypeCode(sub.getCode());
                    soMsgCasetypeMapList.add(c);
                }
            }else if(soCaseTypeId != null){
                SoCaseType caseType = soCaseTypeDAO.findSoCaseType(soCaseTypeId);
                if(caseType != null) {
                    SoMsgCasetypeMap c = new SoMsgCasetypeMap();
                    c.setSoMessage(soMessage);
                    c.setCaseSeq(soMsgCasetypeMapList.size() + 1);
                    c.setSoCaseType(caseType);
                    c.setCaseTypeCode(caseType.getCode());
                    soMsgCasetypeMapList.add(c);
                }
            }
            soCaseTypeSubId = null;
            soCaseTypeId = null;
        }
        caseTypeSize = soMsgCasetypeMapList.size();
    }

    public void deleteSoMsgCasetypeMapListener() {
        int idx = Integer.parseInt(JSFUtil.getRequestParameterMap("idx"));
        soMsgCasetypeMapList.remove(idx);
        caseTypeSize = soMsgCasetypeMapList.size();
    }
    
    private void initSoMsgCasetypeMapList(){
        soMsgCasetypeMapList = new ArrayList<SoMsgCasetypeMap>();
        /*for (int i = 0; i < 1; i++) {
            SoMsgCasetypeMap c = new SoMsgCasetypeMap();
            c.setCaseSeq(i + 1);
            c.setSoMessage(soMessage);
            soMsgCasetypeMapList.add(c);
        }*/
    
    }
    
    public void soCaseTypeListener(){
        //soCaseTypeId = (Integer) event.getNewValue();
        if(soCaseTypeId != null){
            soCaseTypeSubList = soCaseTypeDAO.findSoCaseTopicListById(soCaseTypeId);
        }else{
            soCaseTypeSubList = new ArrayList<SoCaseType>();
            soCaseTypeSubId = null;
        }
    }
    
    public void soCaseTypeSubListener(ValueChangeEvent event){
        soCaseTypeSubId = (Integer) event.getNewValue();
    }
    
    public void sendToSupAction(){
        
        saveDraftAction();
        
        SoWorkflowInstance soWorkflowInstance = null;
        SoWorkflowInstanceLog soWorkflowInstanceLog = null;
        try{
            if(soMessage.getSoWorkflowInstance() == null){

                soWorkflowInstance = new SoWorkflowInstance();
                //soWorkflowInstance.setApprovalId(null);
                soWorkflowInstance.setSoMessage(soMessage);
                soWorkflowInstance.setNote(remarkApproval);
                soWorkflowInstance.setCreateByUsers(JSFUtil.getUserSession().getUsers());
                soWorkflowInstance.setCreateDate(new Date());
                soWorkflowInstance.setUpdateBy(JSFUtil.getUserSession().getUsers().getId().intValue());
                soWorkflowInstance.setUpdateDate(new Date());
                soWorkflowInstance.setStatus("RD");

                soWorkflowInstanceDAO.create(soWorkflowInstance);
            }else{
                soWorkflowInstance = soMessage.getSoWorkflowInstance();
            }
            
            soMessage.setSoWorkflowInstance(soWorkflowInstance);
            soMessage.setCase_status("WF");
            soMessage.setUpdate_by(JSFUtil.getUserSession().getUsers().getId());
            soMessage.setUpdate_date(new Date());
            
            soMessageDAO.edit(soMessage);
            
            //Workflow Log id = 5
            this.toSoActivityLog(new SoActivityType(5), soMessage, remarkApproval, "");
            saveWorkingTime();
        }catch(Exception e){
            log.error(e);
        }
        JSFUtil.redirect(JSFUtil.getServletContext().getContextPath() + "/social/sclassignment.jsf?pagetype=WF");
    }

    public void selectRecCustomerListener() {
        String customerId = JSFUtil.getRequestParameterMap("recCustomerId");
        String customerName = JSFUtil.getRequestParameterMap("recCustomerName");
        boolean exist = false;
        if(customerId != null){
            
        }
    }
    
    private void initEmail(){
        
        commentInfos.clear();
        userInfo = new SoUserInfoDTO();
        soMessageUsers = new ArrayList<SoMessage>();
        listFileUpload = new LinkedHashMap<String, File>();
        filesUpload = new ArrayList<String[]>();
        
        attachmentList = null;
        emailSubject = null;
        emailFrom = null;
        emailTo = null;
        emailCc = null;
        emailBody = null;
        emailBodyText = null;
        emailBodyHtml = null;
        emailDate = null;
        
        pto = null;
        pcc = null;
        pbcc = null;
        psubject = null;
        pbody = "";
        
        bodyList = null;
        paction = null;
        transferTo = "supervisor";
        transferReasonList = soTransferReasonDAO.findSoTransferReasonList();
    }
    
    private void initSignature(){
        signatureList = new ArrayList<String[]>();
        soEmailSignatureList = soMessageDAO.findSoEmailSignatureByAccountId(soMessage.getSoAccount().getId());
        soUserSignatureList = soMessageDAO.findSoUserSignatureByUserId(JSFUtil.getUserSession().getUsers().getId());
        for(SoUserSignature u : soUserSignatureList){
            String[] stra = new String[2];
            stra[0] = "U" + u.getId();
            stra[1] = u.getName();
            signatureList.add(stra);
        }
        for(SoEmailSignature e : soEmailSignatureList){
            String[] stra1 = new String[2];
            stra1[0] = "A" + e.getId();
            stra1[1] = e.getName();
            signatureList.add(stra1);
        }
        
        if(pbody == null && !soUserSignatureList.isEmpty()){
            //pBody = "<br/><!--beginSignature-->" + soUserSignatureList.get(0).getSignature() + "<!--endSignature--><br/>";
        }
    }

    public void signatureValueChangeListener(ValueChangeEvent event){
        signatureId = (String) event.getNewValue();
        if(signatureId != null && !signatureId.isEmpty()){
            Integer id = Integer.parseInt(signatureId.substring(1, signatureId.length()));
            String str = "";
            if(signatureId.contains("A")){
                SoEmailSignature e = soMessageDAO.findSoEmailSignatureById(id);
                str = e.getSignature();
            }else{
                SoUserSignature a = soMessageDAO.findSoUserSignatureById(id);
                str = a.getSignature();
            }
            signatureString = str;
            
            /*
            String beginTxt = "<!--beginSignature-->";
            String endTxt = "<!--endSignature-->";
            if(pbody == null) pbody = "";
            if(pbody.indexOf(beginTxt) != -1 && pbody.indexOf(endTxt) != -1){
                pbody = pbody.substring(0, pbody.indexOf(beginTxt)) + "" + beginTxt + str + endTxt + "" + pbody.substring(pbody.indexOf(endTxt) + endTxt.length(), pbody.length());
            }else{
                pbody = pbody + "<br/><br/><br/>" + beginTxt + str + endTxt + "<br/>";
            }
            */
        }
    }
    
    private void replyAction(){
    
        try{
            this.paction = "re";
            this.pto = (pto == null || pto.isEmpty()) ? InternetAddress.toString(msg.getReplyTo()) : pto;
            this.pcc = "";
            this.pbcc = "";
            this.psubject = (psubject != null && !psubject.isEmpty()) ? psubject : "Re: " + ((soMessage != null && soMessage.getParentSoMessage() == null) ? ("PrimeTime [Service Ticket # " + soMessage.getTicketId() + "] - ") : "") + msg.getSubject();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void replyActionListener() {
        this.replyAction();
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public void replyAllActionListener() {
        try{
            this.paction = "reAll";
            this.pto = InternetAddress.toString(msg.getReplyTo());
            this.pcc = InternetAddress.toString(msg.getAllRecipients());
            this.psubject = "Re: " + ((soMessage != null && soMessage.getParentSoMessage() == null) ? ("PrimeTime [Service Ticket # " + soMessage.getTicketId() + "] - ") : "") + msg.getSubject();
        }catch(Exception e){
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public void forwardActionListener() {
        try{
            this.paction = "fw";
            this.pto = null;
            this.pcc = null;
            this.psubject = "Fw: " + ((soMessage != null && soMessage.getParentSoMessage() == null) ? ("PrimeTime [Service Ticket # " + soMessage.getTicketId() + "] - ") : "") + msg.getSubject();
        }catch(Exception e){
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public void sendAction() {
        String status = "";
        try {
            status = JSFUtil.getRequestParameterMap("statusEmail");
            createMessage(pto, pcc, pbcc, emailFrom, psubject, pbody, null, status);
            
            //this.checkWorkflow();
            saveWorkingTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JSFUtil.redirect(JSFUtil.getServletContext().getContextPath() + "/social/sclassignment.jsf?pagetype=" + status);
    }
    
    private void saveEndTime(Integer soMessageId){
        try{
            SoMsgUserAssigntime obj = soMessageDAO.findSoMsgUserAssigntimeBySoMessageId(soMessageId);
            if(obj != null){
                obj.setTurnaroundEnd(new Date());
                soMessageDAO.saveSoMsgUserAssigntime(obj);
            }
        }catch(Exception e){
            log.error(e);
        }
    }
    
    public void saveDraft(){
        String status = JSFUtil.getRequestParameterMap("saveStatus");
        
        saveDraftAction();
        saveWorkingTime();
        if(status != null && status.equals("Y")){
            JSFUtil.redirect(JSFUtil.getServletContext().getContextPath() + "/social/sclassignment.jsf?pagetype=" + JSFUtil.getUserSession().getSubPage());
        }
    }
    
    private void saveDraftAction(){
        
        SoEmailMessage soem = null;
        
        if (soMessageDraft == null || soMessageDraft.getId() == null) {
            soMessageDraft = new SoMessage();
            soMessageDraft.setCreate_by(JSFUtil.getUserSession().getUsers().getId());
            soMessageDraft.setCreate_date(new Date());
            soMessageDraft.setParentSoMessage(soMessage);
            soMessageDraft.setSoAccount(soMessage.getSoAccount());
            soMessageDraft.setSource_type_enum_id("EM");
            soMessageDraft.setSource_subtype_enum_id("EM_OUT");
            soMessageDraft.setCase_status("DF");
            if(soMessageDraft.getTicketId() == null){
                if(soMessage.getTicketId() != null){
                    soMessageDraft.setTicketId(soMessage.getTicketId());
                }
            }
            
            soem = new SoEmailMessage();
        } else {
            soem = soMessageDraft.getSoEmailMessage();
        }
        soMessageDraft.setContent(pbody);
        
        soem.setSoMessage(soMessageDraft);
        if(outName != null && !outName.isEmpty()){
            soem.setEmailFrom(outName);
        }else{
            soem.setEmailFrom("ropsvc@thaiairways.com");
        }
        //soem.setEmailFrom("ukrit@terrabit.co.th");//***************
        soem.setEmailTo(pto);
        soem.setEmailCc(pcc);
        soem.setEmailBcc(pbcc);
        soem.setSubject(psubject);
        soem.setBody(pbody);
        soem.setDirection("OUT");
        soem.setSendStatus("NO");
        soem.setRetryCnt(0);
        
        //store file name
        String strFileName = "";
        if(filesUploadDraft != null){
            for(String[] s : filesUploadDraft){
                strFileName += s[0] + ",";
            }
        }
        for (Map.Entry<String, File> item : listFileUpload.entrySet()) {
            strFileName += item.getKey() + ",";
        }
        if(!strFileName.isEmpty()){
            strFileName = strFileName.substring(0, strFileName.length() - 1);
        }
        soem.setAttachment(strFileName);
        soem.setAttachmentMap(strFileName);
                
        soMessageDraft.setPriority_enum_id((emailPriority == null || emailPriority.equals("")) ? soMessage.getPriority_enum_id() : emailPriority);
        soMessageDraft.setSoEmailMessage(soem);
        soMessageDraft.setUpdate_by(JSFUtil.getUserSession().getUsers().getId());
        soMessageDraft.setUpdate_date(new Date());
        
        
        try{
            if(soMessageDraft == null || soMessageDraft.getId() == null) {
                soMessageDAO.create(soMessageDraft);
            }else{
                soMessageDAO.edit(soMessageDraft);
            }
            
            soMessage.setPriority_enum_id((emailPriority == null || emailPriority.equals("")) ? soMessage.getPriority_enum_id() : emailPriority);
            if(soMessage.getCase_status().equals("PS")){
                soMessage.setCase_status("DF");
            }
            soMessage.setUpdate_by(JSFUtil.getUserSession().getUsers().getId());
            soMessage.setUpdate_date(new Date());
            soMessageDAO.edit(soMessage);
        }catch(Exception e){
            log.error(e);
        }
        
        //
        try{
            if(listFileUpload != null && !listFileUpload.isEmpty()){
                for (Map.Entry<String, File> item : listFileUpload.entrySet()) {
                    String fileName = item.getKey();
                    File f2 = item.getValue();

                    File f1 = new File(emailPath + soMessageDraft.getSoAccount().getId());
                    if(!f1.exists()) {
                        f1.mkdir();
                    }
                    //f1 = new File(emailPath + soMessageDraft.getSoAccount().getId() + "/" + soMessageDraft.getId());
                    f1 = new File(emailPath + soMessageDraft.getSoAccount().getId() + separate + soMessageDraft.getId());
                    if(!f1.exists()) {
                        f1.mkdir();
                    }
                    //f1 = new File(emailPath + soMessageDraft.getSoAccount().getId() + "/" + soMessageDraft.getId() + "/temp");
                    f1 = new File(emailPath + soMessageDraft.getSoAccount().getId() + separate + soMessageDraft.getId() + separate + "temp");
                    if(!f1.exists()) {
                        f1.mkdir();
                    }
                    //f1 = new File(emailPath + soMessageDraft.getSoAccount().getId() + "/" + soMessageDraft.getId() + "/temp/" + fileName);
                    f1 = new File(emailPath + soMessageDraft.getSoAccount().getId() + separate + soMessageDraft.getId() + separate + "temp" + separate + fileName);
                    if (f2.exists()) {
                        f2.renameTo(f1);
                    }
                }
            }
        }catch(Exception e){
            log.error(e);
        }
        
        this.insertSoMsgCasetypeMap();
    }
    
    private void insertSoMsgCasetypeMap(){
    
        try {
            //if(soMessage.getSoMsgCasetypeMapCollection() != null && !soMessage.getSoMsgCasetypeMapCollection().isEmpty()){
                soMsgCasetypeMapDAO.removeBySoMessageId(soMessage.getId());
            //}
            if(soMsgCasetypeMapList != null && !soMsgCasetypeMapList.isEmpty()){
                for(SoMsgCasetypeMap c1 : soMsgCasetypeMapList){
                    c1.setId(null);
                    soMsgCasetypeMapDAO.create(c1);
                }
            }
        } catch(NonexistentEntityException e){
            log.error(e);
        } catch (PreexistingEntityException e) {
            log.error(e);
        }
        
    }
    
    private void displayEmail(File emlFile) throws Exception{
        emailBodyText = "";
        emailBodyHtml = "";
        InputStream source = new FileInputStream(emlFile);
        msg = new MimeMessage(null, source);


        emailSubject = soMessage.getSoEmailMessage().getSubject();//msg.getSubject() != null ? MimeUtility.decodeText(msg.getSubject()) : "";
        emailDate = soMessage.getSoEmailMessage().getSentDate();//msg.getSentDate();
        emailFrom = soMessage.getSoEmailMessage().getEmailFrom();//msg.getFrom() != null ? MimeUtility.decodeText(InternetAddress.toString(msg.getFrom())) : "";
        emailTo = soMessage.getSoEmailMessage().getEmailTo();//msg.getRecipients(Message.RecipientType.TO) != null ? MimeUtility.decodeText(InternetAddress.toString(msg.getRecipients(Message.RecipientType.TO))) : "";
        emailCc = soMessage.getSoEmailMessage().getEmailCc();//msg.getRecipients(Message.RecipientType.CC) != null ? MimeUtility.decodeText(InternetAddress.toString(msg.getRecipients(Message.RecipientType.CC))) : "";
        
        attachmentList = new ArrayList<String[]>();
        bodyList = new ArrayList<String>();
        /*String contentType = msg.getContentType();
        if (msg.isMimeType("multipart/alternative")) {
            String txt = this.getText(msg);
            bodyList.add(txt);
        } else if (msg.isMimeType("multipart/*") || msg.isMimeType("MULTIPART/*")) {
            Multipart mp = (Multipart) msg.getContent();
            int totalAttachments = mp.getCount();
            if (totalAttachments > 0) {
                for (int i = 0; i < totalAttachments; i++) {
                    textIsHtml = false;
                    BodyPart part = mp.getBodyPart(i);
                    String txt = this.getText(part);
                    if(txt != null && !txt.isEmpty()) bodyList.add(txt);
                    //String attachFileName = part.getFileName();
                    //if(Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) && attachFileName != null && !attachFileName.isEmpty()){
                    //    attachmentList.add(attachFileName);
                    //}
                }
            }
        } else if(msg.isMimeType("text/plain") || msg.isMimeType("text/html")) {
            String tmpText = this.getText(msg);
            bodyList.add(tmpText);
        }*/
        
        if(soMessage.getSoEmailMessage().getAttachment() != null){
            try{
                String str = soMessage.getSoEmailMessage().getAttachment();
                String strMap = soMessage.getSoEmailMessage().getAttachmentMap();
                if(str.indexOf(",") != -1 && strMap.indexOf(",") != -1){
                    StringTokenizer stk = new StringTokenizer(str, ",");
                    StringTokenizer stkMap = new StringTokenizer(strMap, ",");
                    while (stk.hasMoreTokens() && stkMap.hasMoreTokens()) {
                        String[] stra = new String[2];
                        String fName = (String) stk.nextToken();
                        String fNameMap = (String) stkMap.nextToken();
                        stra[0] = fName;
                        stra[1] = fNameMap;
                        attachmentList.add(stra);
                    }
                }else if(str.indexOf(",") == -1 && strMap.indexOf(",") == -1){
                    String[] stra1 = new String[2];
                    stra1[0] = str;
                    stra1[1] = strMap;
                    attachmentList.add(stra1);
                }
            }catch(Exception e){
                log.error(e);
            }
        }

        //Multipart mp = (Multipart) msg.getContent();

        //for (int i = 0, n = mp.getCount(); i < n; i++) {
        //    Part part = mp.getBodyPart(i);
        //dumpPart(msg, bodyList, soMessage.getSoAccount().getId(), soMessage.getId());
//            String disposition = part.getDisposition();
//
//            if ((disposition != null)
//                    && ((disposition.equals(Part.ATTACHMENT)
//                    || (disposition.equals(Part.INLINE)))))   {
//                copy(part.getInputStream(), new File((emailPath + soMessage.getSoAccount().getId() + "/" + soMessage.getId()) + "/" + part.getFileName()));
//            }
        //}
        
        /*List<String> sta = new ArrayList<String>();
        for(String body : bodyList){
            for(String[] stra : contentIdList){
                if(body.contains(stra[0])){
                    body = body.replace("cid:"+stra[0], (JSFUtil.getRequestContextPath() + "/emailfiles/" + soMessage.getSoAccount().getId() + "/" + soMessage.getId() + "/" + stra[1]));
                }
            }
            sta.add(body);
        }
        bodyList = sta;*/
        
        source.close();
    }
    
    private void displayEmailParent(File emlFile) throws Exception{
        emailBodyTextParent = "";
        emailBodyHtmlParent = "";
        InputStream source = new FileInputStream(emlFile);
        msgParent = new MimeMessage(null, source);

        emailSubjectParent = soMessageParent.getSoEmailMessage().getSubject();//msgParent.getSubject() != null ? MimeUtility.decodeText(msgParent.getSubject()) : "";
        emailDateParent = soMessageParent.getSoEmailMessage().getSentDate();//msgParent.getSentDate();
        emailFromParent = soMessageParent.getSoEmailMessage().getEmailFrom();//msgParent.getFrom() != null ? MimeUtility.decodeText(InternetAddress.toString(msgParent.getFrom())) : "";
        emailToParent = soMessageParent.getSoEmailMessage().getEmailTo();//msgParent.getRecipients(Message.RecipientType.TO) != null ? MimeUtility.decodeText(InternetAddress.toString(msgParent.getRecipients(Message.RecipientType.TO))) : "";
        emailCcParent = soMessageParent.getSoEmailMessage().getEmailCc();//msgParent.getRecipients(Message.RecipientType.CC) != null ? MimeUtility.decodeText(InternetAddress.toString(msgParent.getRecipients(Message.RecipientType.CC))) : "";
        
        //emailSubjectParent = msgParent.getSubject();
        //emailDateParent = msgParent.getSentDate();
        //emailFromParent = InternetAddress.toString(msgParent.getFrom());
        //emailToParent = InternetAddress.toString(msgParent.getRecipients(Message.RecipientType.TO));
        //emailCcParent = InternetAddress.toString(msgParent.getRecipients(Message.RecipientType.CC));
        
        attachmentListParent = new ArrayList<String[]>();
        bodyListParent = new ArrayList<String>();
        /*String contentType = msgParent.getContentType();
        if (msgParent.isMimeType("multipart/alternative")) {
            String txt = this.getText(msgParent);
            //bodyListParent.add(txt);
        } else if (msgParent.isMimeType("multipart/*") || msgParent.isMimeType("MULTIPART/*")) {
            Multipart mp = (Multipart) msgParent.getContent();
            int totalAttachments = mp.getCount();
            if (totalAttachments > 0) {
                for (int i = 0; i < totalAttachments; i++) {
                    textIsHtmlParent = false;
                    BodyPart part = mp.getBodyPart(i);
                    String attachFileName = part.getFileName();
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) && attachFileName != null && !attachFileName.isEmpty()) {
                        //attachmentListParent.add(attachFileName);
                    } else {
                        String txt = this.getText(part);
                        if (txt != null && !txt.isEmpty()) {
                            //bodyListParent.add(txt);
                        }
                    }
                }
            }
        } else if(msgParent.isMimeType("text/plain") || msgParent.isMimeType("text/html")) {
            String tmpText = this.getText(msgParent);
            //bodyListParent.add(tmpText);
        }*/
        
        /*if(soMessageParent.getSoEmailMessage().getAttachment() != null){
            String str = soMessageParent.getSoEmailMessage().getAttachment();
            if(str.indexOf(",") != -1){
                StringTokenizer stk = new StringTokenizer(str, ",");
                while (stk.hasMoreTokens()) {
                    String fName = (String) stk.nextToken();
                    attachmentListParent.add(fName);
                }
            }else{
                attachmentListParent.add(str);
            }
        }*/
        
        if(soMessageParent.getSoEmailMessage().getAttachment() != null){
            try{
                String str = soMessageParent.getSoEmailMessage().getAttachment();
                String strMap = soMessageParent.getSoEmailMessage().getAttachmentMap();
                if(str.indexOf(",") != -1 && strMap.indexOf(",") != -1){
                    StringTokenizer stk = new StringTokenizer(str, ",");
                    StringTokenizer stkMap = new StringTokenizer(strMap, ",");
                    while (stk.hasMoreTokens() && stkMap.hasMoreTokens()) {
                        String[] stra = new String[2];
                        String fName = (String) stk.nextToken();
                        String fNameMap = (String) stkMap.nextToken();
                        stra[0] = fName;
                        stra[1] = fNameMap;
                        attachmentListParent.add(stra);
                    }
                }else if(str.indexOf(",") == -1 && strMap.indexOf(",") == -1){
                    String[] stra1 = new String[2];
                    stra1[0] = str;
                    stra1[1] = strMap;
                    attachmentListParent.add(stra1);
                }
            }catch(Exception e){
                log.error(e);
            }
        }

        //Multipart mp = (Multipart) msgParent.getContent();
        
        //for (int i = 0, n = mp.getCount(); i < n; i++) {
        //    Part part = mp.getBodyPart(i);
        //dumpPart(msgParent, bodyListParent, soMessageParent.getSoAccount().getId(), soMessageParent.getId());
            /*String fname = part.getFileName();
            String[] cid = part.getHeader("Content-ID");
            
            String disposition = part.getDisposition();

            if ((disposition != null)
                    && ((disposition.equals(Part.ATTACHMENT)
                    || (disposition.equals(Part.INLINE)))))   {
                copy(part.getInputStream(), new File((emailPath + soMessage.getSoAccount().getId() + "/" + soMessage.getId()) + "/" + part.getFileName()));
            }*/
        //}
        
        /*List<String> sta = new ArrayList<String>();
        for(String body : bodyListParent){
            for(String[] stra : contentIdList){
                if(body.contains(stra[0])){
                    body = body.replace("cid:"+stra[0], (JSFUtil.getRequestContextPath() + "/emailfiles/" + soMessageParent.getSoAccount().getId() + "/" + soMessageParent.getId() + "/" + stra[1]));
                }
            }
            sta.add(body);
        }
        bodyListParent = sta;*/
        
        source.close();
    }
    
    public void viewFileListener(){
        String fileName = JSFUtil.getRequestParameterMap("fileName");
        this.viewFile(msg, fileName);
    }
    
    public void viewFileParentListener(){
        String fileName = JSFUtil.getRequestParameterMap("fileName");
        this.viewFile(msgParent, fileName);
    }
    
    public void viewFile(MimeMessage mm, String fileName) {
        ServletOutputStream out = null;
        try {
            //String fileName = JSFUtil.getRequestParameterMap("fileName");
            if (mm.isMimeType("multipart/*") || mm.isMimeType("MULTIPART/*")) {
                Multipart mp = (Multipart) mm.getContent();
                int totalAttachments = mp.getCount();
                if (totalAttachments > 0) {
                    for (int i = 0; i < totalAttachments; i++) {
                        Part part = mp.getBodyPart(i);
                        String attachFileName = part.getFileName();
                        String disposition = part.getDisposition();
                        String contentType = part.getContentType();

                        if (attachFileName != null && !attachFileName.isEmpty() && fileName.equals(attachFileName)) {
                            FacesContext context = FacesContext.getCurrentInstance();
                            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                            response.setContentType(contentType);
                            out = response.getOutputStream();

                            BufferedInputStream bin = new BufferedInputStream(part.getInputStream());
                            BufferedOutputStream bout = new BufferedOutputStream(out);
                            int ch = 0;;
                            while ((ch = bin.read()) != -1) {
                                bout.write(ch);
                            }

                            bin.close();
                            //fin.close();
                            bout.close();
                            out.flush();
                            out.close();
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException ex) {
            java.util.logging.Logger.getLogger(SclAssignmentController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }
    
    private String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }
//        if (p.isMimeType("text/plain")) {
//            String s = (String)p.getContent();
//            emailBodyText += s;
//            return s;
//        } else if (p.isMimeType("text/html")) {
//            String s = (String)p.getContent();
//            emailBodyHtml += s;
//            return s;
//        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }
    
    /*public String getFromTextAddress() {
        String ret = "";

        try {
            for (int j = 0; j < message.getFrom().length; j++) {
                InternetAddress ia = (InternetAddress) message.getFrom()[j];
                if (ia.getAddress()!=null) {
                    ret += ia.getAddress() + ",";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return fnString.removeLastComma(ret);
    }*/
    
    private String getEmailAddress(Message message){
            
        String emailReplyTo = "";
        try {
            for (int j = 0; j < message.getFrom().length; j++) {
                InternetAddress ia = (InternetAddress) message.getFrom()[j];
                if (ia.getAddress() != null) {
                    emailReplyTo += ia.getAddress() + ",";
                }
            }
            emailReplyTo = emailReplyTo.substring(0, emailReplyTo.length() - 1);
        } catch (Exception e) {
            log.error(e);
        }

        return emailReplyTo;
    }
    
    private void createMessage(String to, String cc, String bcc, String from, String subject, String body, List<File> attachments, String status) {
        try {
            Message message = new MimeMessage(Session.getInstance(System.getProperties()));
            message.setFrom(new InternetAddress(from));
            if(to != null && !to.isEmpty()){
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            }
            if(cc != null && !cc.isEmpty()){
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            }
            if(bcc != null && !bcc.isEmpty()){
                message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
            } 
            message.setSubject(subject);
            
            // fill message
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm", new Locale("th","TH"));
            String str = "<table width='100%' border='0' cellpadding='0' cellspacing='1'>"
                    + "<tr>"
                    + "<td>"
                    + body
                    + "</td>"
                    + "</tr>"
                    + "<tr><td style='height: 20px'></td></tr>"
                    + "<tr><td style='height: 1px; background-color: #CCCCCC;'></td></tr>"
                    + "</table>";
            
            if(msg != null){
                str += "<table width='100%' border='0' cellpadding='0' cellspacing='1'>"
                    + "<tr>"
                    + "<td>"
                    + "<b>From: </b>" + emailFrom + "<br/>" 
                    + "<b>Sent: </b>" + sdf.format(emailDate) + "<br/>" 
                    + "<b>To: </b>" + emailTo + "<br/>" 
                    + "<b>Cc: </b>" + (emailCc != null ? emailCc : "") + "<br/>" 
                    + "<b>Subject: </b>" + (emailSubject != null ? emailSubject : "") + "<br/><br/>" 
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>"
                    + this.getText(msg)
                    + "</td>"
                    + "</tr>"
                    + "<tr><td style='height: 20px'></td></tr>"
                    + "<tr><td style='height: 1px; background-color: #CCCCCC;'></td></tr>"
                    + "</table>";
            }
            
            if(msgParent != null){
                str += "<table width='100%' border='0' cellpadding='' cellspacing='1'>"
                    + "<tr>"
                    + "<td>"
                    + "<b>From: </b>" + emailFromParent + "<br/>" 
                    + "<b>Sent: </b>" + sdf.format(emailDateParent) + "<br/>" 
                    + "<b>To: </b>" + emailToParent + "<br/>" 
                    + "<b>Cc: </b>" + (emailCcParent != null ? emailCcParent : "") + "<br/>" 
                    + "<b>Subject: </b>" + (emailSubjectParent != null ? emailSubjectParent : "") + "<br/><br/>" 
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>"
                    + this.getText(msgParent)
                    + "</td>"
                    + "</tr>"
                    + "<tr><td style='height: 20px'></td></tr>"
                    + "<tr><td style='height: 1px; background-color: #CCCCCC;'></td></tr>"
                    + "</table>";
            }
            
            
            //create email part
            //****************************************************************
                // the parent or main part if you will
            Multipart mainMultipart = new MimeMultipart("mixed");

            //Answer Body
            //****************************************************************
            // this will hold text and html and tells the client there are 2 versions of the message (html and text). presumably text
            // being the alternative to html
            Multipart htmlAndTextMultipart1 = new MimeMultipart("alternative");

            // set text
            MimeBodyPart textBodyPart1 = new MimeBodyPart();
            textBodyPart1.setText("");
            htmlAndTextMultipart1.addBodyPart(textBodyPart1);
            
            // set html
            MimeBodyPart htmlBodyPart1 = new MimeBodyPart();
            htmlBodyPart1.setContent(str, "text/html; charset=utf-8");
            htmlAndTextMultipart1.addBodyPart(htmlBodyPart1);

            // stuff the multipart into a bodypart and add the bodyPart to the mainMultipart
            MimeBodyPart htmlAndTextBodyPart1 = new MimeBodyPart();
            htmlAndTextBodyPart1.setContent(htmlAndTextMultipart1);
            mainMultipart.addBodyPart(htmlAndTextBodyPart1);

            // attach file body parts directly to the mainMultipart
            /*
            MimeBodyPart filePart = new MimeBodyPart();
            FileDataSource fds = new FileDataSource("/path/to/some/file.txt");
            filePart.setDataHandler(new DataHandler(fds));
            filePart.setFileName(fds.getName());
            mainMultipart.addBodyPart(filePart);
            */
            
            String strFileName = "";
            DataHandler dataHandler = null;
            if(filesUploadDraft != null){
                for (String[] filename : filesUploadDraft) {
                    strFileName += filename[0] + ",";
                    MimeBodyPart attachment = new MimeBodyPart();
                    File file = new File(emailPath + separate + soMessageDraft.getSoAccount().getId() + separate + soMessageDraft.getId() + separate + "temp" + separate + filename[0]);
                    if(file.exists()){
                        FileDataSource source = new FileDataSource(file);
                        dataHandler = new DataHandler(source);
                        attachment.setDataHandler(dataHandler);
                        attachment.setFileName(MimeUtility.encodeText(filename[0], "UTF-8", null));
                        mainMultipart.addBodyPart(attachment);
                    }
                }
            }
            for (Map.Entry<String, File> item : listFileUpload.entrySet()) {
                strFileName += item.getKey() + ",";
                MimeBodyPart attachment = new MimeBodyPart();
                FileDataSource source = new FileDataSource(item.getValue());
                dataHandler = new DataHandler(source);
                attachment.setDataHandler(dataHandler);
                attachment.setFileName(MimeUtility.encodeText(item.getKey(), "UTF-8", null));
                mainMultipart.addBodyPart(attachment);
            }
            if(!strFileName.isEmpty()){
                strFileName = strFileName.substring(0, strFileName.length() - 1);
            }
            
            if(this.paction != null && this.paction.equalsIgnoreCase("fw")){
                if (msg.isMimeType("multipart/alternative")) {
                    String txt = this.getText(msg);
                } else if (msg.isMimeType("multipart/*") || msg.isMimeType("MULTIPART/*")) {
                    Multipart mp = (Multipart) msg.getContent();
                    int totalAttachments = mp.getCount();
                    if (totalAttachments > 0) {
                        for (int i = 0; i < totalAttachments; i++) {
                            BodyPart part = mp.getBodyPart(i);
                            mainMultipart.addBodyPart(part);
                        }
                    }
                }
                
                if (msgParent != null){
                    if (msgParent.isMimeType("multipart/alternative")) {
                        String txt = this.getText(msgParent);
                    } else if (msgParent.isMimeType("multipart/*") || msgParent.isMimeType("MULTIPART/*")) {
                        Multipart mp = (Multipart) msgParent.getContent();
                        int totalAttachments = mp.getCount();
                        if (totalAttachments > 0) {
                            for (int i = 0; i < totalAttachments; i++) {
                                BodyPart part = mp.getBodyPart(i);
                                mainMultipart.addBodyPart(part);
                            }
                        }
                    }
                }
            }
            
            

            // set message content
            message.setContent(mainMultipart);
            //****************************************************************
            
            SoMessage som = new SoMessage();
            if(soMessageDraft != null && soMessageDraft.getId() != null){
                som = soMessageDraft;
            }
            som.setUser_id(soMessage.getUser_id());
            som.setPriority_enum_id((emailPriority == null || emailPriority.equals("")) ? soMessage.getPriority_enum_id() : emailPriority);
            som.setContent(pbody);
            som.setSource_type_enum_id("EM");
            som.setSource_subtype_enum_id("EM_OUT");
            som.setSoAccount(soMessage.getSoAccount());
            som.setCreate_by(JSFUtil.getUserSession().getUsers().getId());
            som.setCreate_date(new Date());
            som.setReceive_date(new Date());
            if(som.getTicketId() == null){
                if(soMessage.getTicketId() != null){
                    som.setTicketId(soMessage.getTicketId());
                }
            }
            som.setParentSoMessage(soMessage);
            if(soCaseTypeId != null && soCaseTypeId != 0){
                som.setCasetype_enum_id(soCaseTypeId.toString());
            }
            
            SoEmailMessage soem = new SoEmailMessage();
            if(som != null && som.getSoEmailMessage() != null && som.getSoEmailMessage().getId() != null) {
                soem = som.getSoEmailMessage();
            }
            soem.setSoMessage(som);
            if(outName != null && !outName.isEmpty()){
                soem.setEmailFrom(outName);
            }else{
                soem.setEmailFrom("ropsvc@thaiairways.com");
            }
            soem.setEmailTo(to);
            soem.setEmailReplyTo(getEmailAddress(message));
            soem.setEmailCc(cc);
            soem.setEmailBcc(bcc);
            soem.setSubject(subject);
            soem.setBody(body);
            soem.setDirection("OUT");
            soem.setSendStatus("RD");
            soem.setRetryCnt(0);
            soem.setAttachment(strFileName);
            soem.setAttachmentMap(strFileName);
            
            som.setCase_status("CL");
            som.setSoEmailMessage(soem);
            som.setUpdate_date(new Date());
            som.setUpdate_by(JSFUtil.getUserSession().getUsers().getId());
            som.setLastReceiveByUsers(JSFUtil.getUserSession().getUsers());
            som.setLastReceiveByName(JSFUtil.getUserSession().getUsers().getLoginName());
            som.setActivity_time_format(new Date());
            
            this.updateWorkflow();
            
            if(soMessageDraft != null && soMessageDraft.getId() != null){
                soMessageDAO.edit(som);
            } else {
                soMessageDAO.create(som);
            }
            
            File f1 = new File(emailPath + soMessage.getSoAccount().getId());
            if(!f1.exists()) {
                f1.mkdir();
            }
            //File f2 = new File(emailPath + soMessage.getSoAccount().getId() + "/" + som.getId());
            File f2 = new File(emailPath + soMessage.getSoAccount().getId() + separate + som.getId());
            if(!f2.exists()) {
                f2.mkdir();
            }
            if(f2.exists()) {
                //message.writeTo(new FileOutputStream(new File(emailPath + soMessage.getSoAccount().getId() + "/" + som.getId() + "/" + som.getId() + ".eml")));
                message.writeTo(new FileOutputStream(new File(emailPath + soMessage.getSoAccount().getId() + separate + som.getId() + separate + som.getId() + ".eml")));
            }
            
            soMessage.setPriority_enum_id((emailPriority == null || emailPriority.equals("")) ? soMessage.getPriority_enum_id() : emailPriority);
            soMessage.setUpdate_date(new Date());
            soMessage.setUpdate_by(JSFUtil.getUserSession().getUsers().getId());
            if(soMessage.getRespEndDate() == null){
                soMessage.setRespEndDate(new Date());
            }
            
            if(soMessage.getReplyCnt() == null) soMessage.setReplyCnt(0);
            if(soMessage.getReplyallCnt() == null) soMessage.setReplyallCnt(0);
            if(soMessage.getForwardCnt() == null) soMessage.setForwardCnt(0);
            
            Integer replyCount = soMessage.getReplyCnt();
            Integer replyAllCount = soMessage.getReplyallCnt();
            Integer forwwardCount = soMessage.getForwardCnt();
            
            if(paction.equals("re")){
                soMessage.setReplyCnt(replyCount + 1);
                this.toSoActivityLog(new SoActivityType(6), soMessage, soMessage.getRemark(), "");
            }else if(paction.equals("reAll")){
                soMessage.setReplyallCnt(replyAllCount + 1);
                this.toSoActivityLog(new SoActivityType(6), soMessage, soMessage.getRemark(), "");
            }else if(paction.equals("fw")){
                soMessage.setForwardCnt(forwwardCount + 1);
                this.toSoActivityLog(new SoActivityType(9), soMessage, soMessage.getRemark(), "");
            }
            
            if(status != null && !status.isEmpty()) {
                soMessage.setCase_status(status);
                //if(status.equals("FL")){
                    this.toSoActivityLog(new SoActivityType(8), soMessage, "", "");
                //}else if(status.equals("CL")){  
                    //stamp time when closed
                    try{
                        soMsgUserAssigntimeDAO.closeTransfer(soMessage.getId(), JSFUtil.getUserSession().getUsers().getId());
                    }catch(Exception e){
                        log.error(e);
                    }
                //}
            }
            
            soMessageDAO.edit(soMessage);
            
            this.insertSoMsgCasetypeMap();
            
            try{
                for (String[] filename : filesUploadDraft) {
                    //File file = new File(emailPath + "/" + soMessageDraft.getSoAccount().getId() + "/" + soMessageDraft.getId() + "/temp/" + filename);
                    File file = new File(emailPath + separate + soMessageDraft.getSoAccount().getId() + separate + soMessageDraft.getId() + separate + "temp" + separate + filename[1]);
                    if(file.exists()){
                        file.delete();
                    }
                }
                
                for (Map.Entry<String, File> item : listFileUpload.entrySet()) {
                    File f = item.getValue();
                    if(f.exists()){
                        f.delete();
                    }
                }
            }catch(Exception e){
            
            }
            
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SclAssignmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void updateWorkflow(){
        try{
            if(soMessage.getCase_status().equals("WF") && JSFUtil.getUserSession().getUsers().getUserGroup().getRole().contains("Supervisor")){
                if(soMessage.getSoWorkflowInstance() != null){
                    SoWorkflowInstance wf = soMessage.getSoWorkflowInstance();
                    wf.setApprovalId(JSFUtil.getUserSession().getUsers().getId());
                    wf.setUpdateBy(JSFUtil.getUserSession().getUsers().getId());
                    wf.setUpdateDate(new Date());
                    wf.setStatus("AP");
                    soWorkflowInstanceDAO.edit(wf);
                    
                    this.toSoActivityLog(new SoActivityType(5), soMessage, "Approved", "");
                }
            }
        }catch(Exception e){
            log.error(e);
        }
    }

    public void uploadCompleteListener() {
        File file = fileUploadBean.getFiles().get(0);
    }

    public void removeFileTemp() {
        String rmFile = StringUtils.trim(JSFUtil.getRequestParameterMap("rmFile"));
        //File f = new File(emailPath + "/" + soMessageDraft.getSoAccount().getId() + "/" + soMessageDraft.getId() + "/temp/" + rmFile);
        File f = new File(emailPath + separate + "temp" + separate + rmFile);
        if (f.exists()) {
            f.delete();
        }
        List<String[]> fu = new ArrayList<String[]>();
        for (String[] item : filesUpload) {
            if(!item[1].equals(rmFile)){
                fu.add(item);
            }
        }
        filesUpload = fu;
        
        Map<String, File> f2 = new LinkedHashMap<String, File>();
        for (Map.Entry<String, File> f1 : listFileUpload.entrySet()) {
            if(!f1.getValue().equals(f)){
                f2.put(f1.getKey(), f1.getValue());
            }
        }
        listFileUpload = f2;
    }

    public void removeFileTempDraft() {
        String rmFile = StringUtils.trim(JSFUtil.getRequestParameterMap("rmFile"));
        //File f = new File(emailPath + "/" + soMessageDraft.getSoAccount().getId() + "/" + soMessageDraft.getId() + "/temp/" + rmFile);
        File f = new File(emailPath + separate + soMessageDraft.getSoAccount().getId() + separate + soMessageDraft.getId() + separate + "temp" + separate + rmFile);
        if (f.exists()) {
            f.delete();
        }
        List<String[]> fu = new ArrayList<String[]>();
        for (String[] item : filesUploadDraft) {
            if(!item[1].equals(rmFile)){
                fu.add(item);
            }
        }
        filesUploadDraft = fu;
    }
    
    public void uploadFileListener(FileUploadEvent event) throws Exception {
        
        UploadedFile item = event.getUploadedFile();
        String fileName = item.getName();
        if(fileName.lastIndexOf("\\") != -1) {
            fileName = fileName.substring(fileName.lastIndexOf("\\")+1, fileName.length());
        }
        if(!fileName.isEmpty() && fileName.indexOf(",") != -1)
            fileName = fileName.replace(",", "");
        InputStream inputStream = item.getInputStream();
        //File fTemp = new File(emailPath + "/temp");
        File fTemp = new File(emailPath + "temp");
        if(!fTemp.exists()){
            fTemp.mkdir();
        }
        
        //File file = new File(emailPath + "/temp/" + (new Date()).getTime() + fileName.substring(fileName.lastIndexOf("."), fileName.length()));
        File file = new File(emailPath + separate + "temp" + separate + (new Date()).getTime() + fileName.substring(fileName.lastIndexOf("."), fileName.length()));
        this.copy(inputStream, file);
        listFileUpload.put(fileName, file);
        String[] stra = new String[2];
        stra[0] = fileName;
        stra[1] = file.getName();
        filesUpload.add(stra);
        

//        int idx = fileName.lastIndexOf(File.separator);
//        int idx1 = fileName.lastIndexOf("\\");
//        if (idx!=-1) {
//            fileName = fileName.substring(idx,fileName.length());
//        }else if(idx1 != -1){
//            fileName = fileName.substring(idx1+1,fileName.length());
//        }
        //File f = new File(this.getAbsolutePath(), fileName);
        //if(saveToFile(item.getInputStream(), f)) {                    
//            tempFile = f;
//            this.files.add(tempFile);
        //    this.files.add(f);
        //}
//        this.files.add(f);

    }  
    
    private void copy(InputStream in, File file) {
        try {
            if(!file.exists()){
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectTemplateListener() {
        pbody += "<br/>";
        pbody += JSFUtil.getRequestParameterMap("dlgReturnValue");

        FacesContext.getCurrentInstance().renderResponse();
    }
    
    private String convertContentId(String cId){
        String str = "";
        if(cId.indexOf("<") != -1 && cId.indexOf(">") != -1){
            str = cId.substring(cId.indexOf("<") + 1, cId.indexOf(">"));
        }
        return str;
    }
    
    private String convertContentIdToFileName(String cId){
        String str = cId;
        if(cId.indexOf("@") != -1){
            str = cId.substring(0, cId.indexOf("@"));
        }
        return str;
    }
    
    public Integer getSoMessageId() {
        return soMessageId;
    }

    public void setSoMessageId(Integer soMessageId) {
        this.soMessageId = soMessageId;
    }

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        SclDetailController.log = log;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getTmrName() {
        return tmrName;
    }

    public void setTmrName(String tmrName) {
        this.tmrName = tmrName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getReplyMode() {
        return replyMode;
    }

    public void setReplyMode(String replyMode) {
        this.replyMode = replyMode;
    }

    public String getReplyDesc() {
        return replyDesc;
    }

    public void setReplyDesc(String replyDesc) {
        this.replyDesc = replyDesc;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getKbTag() {
        return kbTag;
    }

    public void setKbTag(String kbTag) {
        this.kbTag = kbTag;
    }

    public String getAccountUserId() {
        return accountUserId;
    }

    public void setAccountUserId(String accountUserId) {
        this.accountUserId = accountUserId;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public String getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }

    public String getSelectedMsgID() {
        return selectedMsgID;
    }

    public void setSelectedMsgID(String selectedMsgID) {
        this.selectedMsgID = selectedMsgID;
    }

    public MimeMessage getMsg() {
        return msg;
    }

    public void setMsg(MimeMessage msg) {
        this.msg = msg;
    }

    public boolean isTextIsHtml() {
        return textIsHtml;
    }

    public void setTextIsHtml(boolean textIsHtml) {
        this.textIsHtml = textIsHtml;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getEmailCc() {
        return emailCc;
    }

    public void setEmailCc(String emailCc) {
        this.emailCc = emailCc;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getEmailBodyText() {
        return emailBodyText;
    }

    public void setEmailBodyText(String emailBodyText) {
        this.emailBodyText = emailBodyText;
    }

    public String getEmailBodyHtml() {
        return emailBodyHtml;
    }

    public void setEmailBodyHtml(String emailBodyHtml) {
        this.emailBodyHtml = emailBodyHtml;
    }

    public Date getEmailDate() {
        return emailDate;
    }

    public void setEmailDate(Date emailDate) {
        this.emailDate = emailDate;
    }

    public List<String[]> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<String[]> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<String> getBodyList() {
        return bodyList;
    }

    public void setBodyList(List<String> bodyList) {
        this.bodyList = bodyList;
    }

    public String getPsubject() {
        return psubject;
    }

    public void setPsubject(String psubject) {
        this.psubject = psubject;
    }

    public String getPto() {
        return pto;
    }

    public void setPto(String pto) {
        this.pto = pto;
    }

    public String getPcc() {
        return pcc;
    }

    public void setPcc(String pcc) {
        this.pcc = pcc;
    }

    public String getPbcc() {
        return pbcc;
    }

    public void setPbcc(String pbcc) {
        this.pbcc = pbcc;
    }

    public String getPbody() {
        return pbody;
    }

    public void setPbody(String pbody) {
        this.pbody = pbody;
    }

    public String getPaction() {
        return paction;
    }

    public void setPaction(String paction) {
        this.paction = paction;
    }

    public List<SoMessage> getSoMessageAssignments() {
        return soMessageAssignments;
    }

    public void setSoMessageAssignments(List<SoMessage> soMessageAssignments) {
        this.soMessageAssignments = soMessageAssignments;
    }

    public List<SoMessage> getSoMessageSelects() {
        return soMessageSelects;
    }

    public void setSoMessageSelects(List<SoMessage> soMessageSelects) {
        this.soMessageSelects = soMessageSelects;
    }

    public List<SoMessage> getSoMessageUsers() {
        return soMessageUsers;
    }

    public void setSoMessageUsers(List<SoMessage> soMessageUsers) {
        this.soMessageUsers = soMessageUsers;
    }

    public SoMessage getSoMessage() {
        return soMessage;
    }

    public void setSoMessage(SoMessage soMessage) {
        this.soMessage = soMessage;
    }

    public SclUsers getSclUsers() {
        return sclUsers;
    }

    public void setSclUsers(SclUsers sclUsers) {
        this.sclUsers = sclUsers;
    }

    public SoUserInfoDTO getSoUserInfoDTO() {
        return soUserInfoDTO;
    }

    public void setSoUserInfoDTO(SoUserInfoDTO soUserInfoDTO) {
        this.soUserInfoDTO = soUserInfoDTO;
    }

    public SoMessageDAO getSoMessageDAO() {
        return soMessageDAO;
    }

    public void setSoMessageDAO(SoMessageDAO soMessageDAO) {
        this.soMessageDAO = soMessageDAO;
    }

    public SoMessageHistoryDAO getSoMessageHistoryDAO() {
        return soMessageHistoryDAO;
    }

    public void setSoMessageHistoryDAO(SoMessageHistoryDAO soMessageHistoryDAO) {
        this.soMessageHistoryDAO = soMessageHistoryDAO;
    }

    public SclUsersDAO getSclUsersDAO() {
        return sclUsersDAO;
    }

    public void setSclUsersDAO(SclUsersDAO sclUsersDAO) {
        this.sclUsersDAO = sclUsersDAO;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public SoCaseTypeDAO getSoCaseTypeDAO() {
        return soCaseTypeDAO;
    }

    public void setSoCaseTypeDAO(SoCaseTypeDAO soCaseTypeDAO) {
        this.soCaseTypeDAO = soCaseTypeDAO;
    }

    public KnowledgebaseDAO getKnowledgebaseDAO() {
        return knowledgebaseDAO;
    }

    public void setKnowledgebaseDAO(KnowledgebaseDAO knowledgebaseDAO) {
        this.knowledgebaseDAO = knowledgebaseDAO;
    }

    public SoAccountUserDAO getSoAccountUserDAO() {
        return soAccountUserDAO;
    }

    public void setSoAccountUserDAO(SoAccountUserDAO soAccountUserDAO) {
        this.soAccountUserDAO = soAccountUserDAO;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public SoAccountDAO getSoAccountDAO() {
        return soAccountDAO;
    }

    public void setSoAccountDAO(SoAccountDAO soAccountDAO) {
        this.soAccountDAO = soAccountDAO;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SoChannelDAO getSoChannelDAO() {
        return soChannelDAO;
    }

    public void setSoChannelDAO(SoChannelDAO soChannelDAO) {
        this.soChannelDAO = soChannelDAO;
    }

    public List<SoContentDetailDTO> getCommentInfos() {
        return commentInfos;
    }

    public void setCommentInfos(List<SoContentDetailDTO> commentInfos) {
        this.commentInfos = commentInfos;
    }

    public SoContentDetailDTO getPostInfo() {
        return postInfo;
    }

    public void setPostInfo(SoContentDetailDTO postInfo) {
        this.postInfo = postInfo;
    }

    public SoUserInfoDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(SoUserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }

    public Map<String, Integer> getAccountUserList() {
        return accountUserList;
    }

    public void setAccountUserList(Map<String, Integer> accountUserList) {
        this.accountUserList = accountUserList;
    }

    public Map<String, File> getListFileUpload() {
        return listFileUpload;
    }

    public void setListFileUpload(Map<String, File> listFileUpload) {
        this.listFileUpload = listFileUpload;
    }

    public javax.servlet.http.Part getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(javax.servlet.http.Part fileUpload) {
        this.fileUpload = fileUpload;
    }

    public List<String[]> getFilesUpload() {
        return filesUpload;
    }

    public void setFilesUpload(List<String[]> filesUpload) {
        this.filesUpload = filesUpload;
    }

    public String getFileNameToDelete() {
        return fileNameToDelete;
    }

    public void setFileNameToDelete(String fileNameToDelete) {
        this.fileNameToDelete = fileNameToDelete;
    }

    public FileUploadBean getFileUploadBean() {
        return fileUploadBean;
    }

    public void setFileUploadBean(FileUploadBean fileUploadBean) {
        this.fileUploadBean = fileUploadBean;
    }

    public List<SoAccount> getSoAccountList() {
        return soAccountList;
    }

    public void setSoAccountList(List<SoAccount> soAccountList) {
        this.soAccountList = soAccountList;
    }

    public List<SoChannel> getSoChannelList() {
        return soChannelList;
    }

    public void setSoChannelList(List<SoChannel> soChannelList) {
        this.soChannelList = soChannelList;
    }

    public String getEmailBodyTextParent() {
        return emailBodyTextParent;
    }

    public void setEmailBodyTextParent(String emailBodyTextParent) {
        this.emailBodyTextParent = emailBodyTextParent;
    }

    public String getEmailBodyHtmlParent() {
        return emailBodyHtmlParent;
    }

    public void setEmailBodyHtmlParent(String emailBodyHtmlParent) {
        this.emailBodyHtmlParent = emailBodyHtmlParent;
    }

    public MimeMessage getMsgParent() {
        return msgParent;
    }

    public void setMsgParent(MimeMessage msgParent) {
        this.msgParent = msgParent;
    }

    public String getEmailSubjectParent() {
        return emailSubjectParent;
    }

    public void setEmailSubjectParent(String emailSubjectParent) {
        this.emailSubjectParent = emailSubjectParent;
    }

    public Date getEmailDateParent() {
        return emailDateParent;
    }

    public void setEmailDateParent(Date emailDateParent) {
        this.emailDateParent = emailDateParent;
    }

    public String getEmailFromParent() {
        return emailFromParent;
    }

    public void setEmailFromParent(String emailFromParent) {
        this.emailFromParent = emailFromParent;
    }

    public String getEmailToParent() {
        return emailToParent;
    }

    public void setEmailToParent(String emailToParent) {
        this.emailToParent = emailToParent;
    }

    public String getEmailCcParent() {
        return emailCcParent;
    }

    public void setEmailCcParent(String emailCcParent) {
        this.emailCcParent = emailCcParent;
    }

    public List<String[]> getAttachmentListParent() {
        return attachmentListParent;
    }

    public void setAttachmentListParent(List<String[]> attachmentListParent) {
        this.attachmentListParent = attachmentListParent;
    }

    public List<String> getBodyListParent() {
        return bodyListParent;
    }

    public void setBodyListParent(List<String> bodyListParent) {
        this.bodyListParent = bodyListParent;
    }

    public boolean isTextIsHtmlParent() {
        return textIsHtmlParent;
    }

    public void setTextIsHtmlParent(boolean textIsHtmlParent) {
        this.textIsHtmlParent = textIsHtmlParent;
    }

    public SoMessage getSoMessageParent() {
        return soMessageParent;
    }

    public void setSoMessageParent(SoMessage soMessageParent) {
        this.soMessageParent = soMessageParent;
    }

    //Address Book
    public String getSelectedEmailString() {
        return selectedEmailString;
    }

    public void setSelectedEmailString(String selectedEmailString) {
        this.selectedEmailString = selectedEmailString;
    }
    
    public void selectUserActionListener(){
        if(TO) {
            if(pto != null && !pto.isEmpty()) {
                pto += ","+selectedEmailString;
            } else{
                pto = selectedEmailString;
            }
        } else if(CC) {
            if(pcc != null && !pcc.isEmpty()) {
                pcc += ","+selectedEmailString;
            } else {
                pcc = selectedEmailString;
            }
        } else if(BCC) {
            if(pbcc != null && !pbcc.isEmpty()) {
                pbcc += ","+selectedEmailString;
            } else {
                pbcc = selectedEmailString;
            }
        }
    }
    
    public void checkTO() {
        TO = true;
        CC = false;
        BCC = false;
    }
     
    public void checkCC() {
        TO = false;
        CC = true;
        BCC = false;
    }
            
    public void checkBCC() {
        TO = false;
        CC = false;
        BCC = true;
    }

    public boolean isTO() {
        return TO;
    }

    public void setTO(boolean TO) {
        this.TO = TO;
    }

    public boolean isCC() {
        return CC;
    }

    public void setCC(boolean CC) {
        this.CC = CC;
    }

    public boolean isBCC() {
        return BCC;
    }

    public void setBCC(boolean BCC) {
        this.BCC = BCC;
    }

    public SoMessage getSoMessageDraft() {
        return soMessageDraft;
    }

    public void setSoMessageDraft(SoMessage soMessageDraft) {
        this.soMessageDraft = soMessageDraft;
    }

    public List<SoEmailSignature> getSoEmailSignatureList() {
        return soEmailSignatureList;
    }

    public void setSoEmailSignatureList(List<SoEmailSignature> soEmailSignatureList) {
        this.soEmailSignatureList = soEmailSignatureList;
    }

    public String getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(String signatureId) {
        this.signatureId = signatureId;
    }

    public String getSignatureString() {
        return signatureString;
    }

    public void setSignatureString(String signatureString) {
        this.signatureString = signatureString;
    }

    public List<SoServiceOutgoingAccount> getOutList() {
        return outList;
    }

    public void setOutList(List<SoServiceOutgoingAccount> outList) {
        this.outList = outList;
    }

    public String getOutName() {
        return outName;
    }

    public void setOutName(String outName) {
        this.outName = outName;
    }

    public List<SoCaseType> getSoCaseTypeList() {
        return soCaseTypeList;
    }

    public void setSoCaseTypeList(List<SoCaseType> soCaseTypeList) {
        this.soCaseTypeList = soCaseTypeList;
    }

    public Integer getSoCaseTypeId() {
        return soCaseTypeId;
    }

    public void setSoCaseTypeId(Integer soCaseTypeId) {
        this.soCaseTypeId = soCaseTypeId;
    }

    public List<SoMessage> getContactHistoryList() {
        return contactHistoryList;
    }

    public void setContactHistoryList(List<SoMessage> contactHistoryList) {
        this.contactHistoryList = contactHistoryList;
    }

    public List<String[]> getSignatureList() {
        return signatureList;
    }

    public void setSignatureList(List<String[]> signatureList) {
        this.signatureList = signatureList;
    }

    public List<SoUserSignature> getSoUserSignatureList() {
        return soUserSignatureList;
    }

    public void setSoUserSignatureList(List<SoUserSignature> soUserSignatureList) {
        this.soUserSignatureList = soUserSignatureList;
    }

    public Integer getIgnoreId() {
        return ignoreId;
    }

    public void setIgnoreId(Integer ignoreId) {
        this.ignoreId = ignoreId;
    }

    public List<SoIgnoreReason> getIgnoreList() {
        return ignoreList;
    }

    public void setIgnoreList(List<SoIgnoreReason> ignoreList) {
        this.ignoreList = ignoreList;
    }

    public Integer getCaseTypeIgnoreId() {
        return caseTypeIgnoreId;
    }

    public void setCaseTypeIgnoreId(Integer caseTypeIgnoreId) {
        this.caseTypeIgnoreId = caseTypeIgnoreId;
    }

    public List<SoCaseType> getCaseTypeIgnoreList() {
        return caseTypeIgnoreList;
    }

    public void setCaseTypeIgnoreList(List<SoCaseType> caseTypeIgnoreList) {
        this.caseTypeIgnoreList = caseTypeIgnoreList;
    }

    public SoActivityLogDAO getSoActivityLogDAO() {
        return soActivityLogDAO;
    }

    public void setSoActivityLogDAO(SoActivityLogDAO soActivityLogDAO) {
        this.soActivityLogDAO = soActivityLogDAO;
    }

    public SoIgnoreReasonDAO getSoIgnoreReasonDAO() {
        return soIgnoreReasonDAO;
    }

    public void setSoIgnoreReasonDAO(SoIgnoreReasonDAO soIgnoreReasonDAO) {
        this.soIgnoreReasonDAO = soIgnoreReasonDAO;
    }

    public String getIgnoreRemark() {
        return ignoreRemark;
    }

    public void setIgnoreRemark(String ignoreRemark) {
        this.ignoreRemark = ignoreRemark;
    }

    public String getEmailPriority() {
        return emailPriority;
    }

    public void setEmailPriority(String emailPriority) {
        this.emailPriority = emailPriority;
    }

    public String getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(String transferTo) {
        this.transferTo = transferTo;
    }

    public Integer getTransferToServiceId() {
        return transferToServiceId;
    }

    public void setTransferToServiceId(Integer transferToServiceId) {
        this.transferToServiceId = transferToServiceId;
    }

    public Integer getTransferToUserId() {
        return transferToUserId;
    }

    public void setTransferToUserId(Integer transferToUserId) {
        this.transferToUserId = transferToUserId;
    }

    public Integer getTransferReasonId() {
        return transferReasonId;
    }

    public void setTransferReasonId(Integer transferReasonId) {
        this.transferReasonId = transferReasonId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkApproval() {
        return remarkApproval;
    }

    public void setRemarkApproval(String remarkApproval) {
        this.remarkApproval = remarkApproval;
    }

    public SoWorkflowInstanceDAO getSoWorkflowInstanceDAO() {
        return soWorkflowInstanceDAO;
    }

    public void setSoWorkflowInstanceDAO(SoWorkflowInstanceDAO soWorkflowInstanceDAO) {
        this.soWorkflowInstanceDAO = soWorkflowInstanceDAO;
    }

    public List<SoCaseType> getSoCaseTypeSubList() {
        return soCaseTypeSubList;
    }

    public void setSoCaseTypeSubList(List<SoCaseType> soCaseTypeSubList) {
        this.soCaseTypeSubList = soCaseTypeSubList;
    }

    public Integer getSoCaseTypeSubId() {
        return soCaseTypeSubId;
    }

    public void setSoCaseTypeSubId(Integer soCaseTypeSubId) {
        this.soCaseTypeSubId = soCaseTypeSubId;
    }

    public SoMsgCasetypeMapDAO getSoMsgCasetypeMapDAO() {
        return soMsgCasetypeMapDAO;
    }

    public void setSoMsgCasetypeMapDAO(SoMsgCasetypeMapDAO soMsgCasetypeMapDAO) {
        this.soMsgCasetypeMapDAO = soMsgCasetypeMapDAO;
    }

    public List<SoMsgCasetypeMap> getSoMsgCasetypeMapList() {
        return soMsgCasetypeMapList;
    }

    public void setSoMsgCasetypeMapList(List<SoMsgCasetypeMap> soMsgCasetypeMapList) {
        this.soMsgCasetypeMapList = soMsgCasetypeMapList;
    }

    public boolean isShowPanelSendMail() {
        return showPanelSendMail;
    }

    public void setShowPanelSendMail(boolean showPanelSendMail) {
        this.showPanelSendMail = showPanelSendMail;
    }

    public List<String[]> getFilesUploadDraft() {
        return filesUploadDraft;
    }

    public void setFilesUploadDraft(List<String[]> filesUploadDraft) {
        this.filesUploadDraft = filesUploadDraft;
    }

    public String getIgnoredReason() {
        return ignoredReason;
    }

    public void setIgnoredReason(String ignoredReason) {
        this.ignoredReason = ignoredReason;
    }

    public String getIgnoredCaseType() {
        return ignoredCaseType;
    }

    public void setIgnoredCaseType(String ignoredCaseType) {
        this.ignoredCaseType = ignoredCaseType;
    }

    public String getIgnoredRemark() {
        return ignoredRemark;
    }

    public void setIgnoredRemark(String ignoredRemark) {
        this.ignoredRemark = ignoredRemark;
    }

    public Date getIgnoredDate() {
        return ignoredDate;
    }

    public void setIgnoredDate(Date ignoredDate) {
        this.ignoredDate = ignoredDate;
    }

    public String getIgnoredCaseTopic() {
        return ignoredCaseTopic;
    }

    public void setIgnoredCaseTopic(String ignoredCaseTopic) {
        this.ignoredCaseTopic = ignoredCaseTopic;
    }

    public String getIgnoredBy() {
        return ignoredBy;
    }

    public void setIgnoredBy(String ignoredBy) {
        this.ignoredBy = ignoredBy;
    }

    public String getEmailPath() {
        return emailPath;
    }

    public void setEmailPath(String emailPath) {
        this.emailPath = emailPath;
    }

    public String getSeparate() {
        return separate;
    }

    public void setSeparate(String separate) {
        this.separate = separate;
    }

    public SoActivityTypeDAO getSoActivityTypeDAO() {
        return soActivityTypeDAO;
    }

    public void setSoActivityTypeDAO(SoActivityTypeDAO soActivityTypeDAO) {
        this.soActivityTypeDAO = soActivityTypeDAO;
    }

    public List<Users> getTransferUserList() {
        return transferUserList;
    }

    public void setTransferUserList(List<Users> transferUserList) {
        this.transferUserList = transferUserList;
    }

    public List<SoTransferReason> getTransferReasonList() {
        return transferReasonList;
    }

    public void setTransferReasonList(List<SoTransferReason> transferReasonList) {
        this.transferReasonList = transferReasonList;
    }

    public String getTransferRemark() {
        return transferRemark;
    }

    public void setTransferRemark(String transferRemark) {
        this.transferRemark = transferRemark;
    }

    public SoTransferReasonDAO getSoTransferReasonDAO() {
        return soTransferReasonDAO;
    }

    public void setSoTransferReasonDAO(SoTransferReasonDAO soTransferReasonDAO) {
        this.soTransferReasonDAO = soTransferReasonDAO;
    }

    public Integer getTransferToSupervisorId() {
        return transferToSupervisorId;
    }

    public void setTransferToSupervisorId(Integer transferToSupervisorId) {
        this.transferToSupervisorId = transferToSupervisorId;
    }

    public SoMsgUserAssigntimeDAO getSoMsgUserAssigntimeDAO() {
        return soMsgUserAssigntimeDAO;
    }

    public void setSoMsgUserAssigntimeDAO(SoMsgUserAssigntimeDAO soMsgUserAssigntimeDAO) {
        this.soMsgUserAssigntimeDAO = soMsgUserAssigntimeDAO;
    }

    public List<Users> getTransferSupervisorList() {
        return transferSupervisorList;
    }

    public void setTransferSupervisorList(List<Users> transferSupervisorList) {
        this.transferSupervisorList = transferSupervisorList;
    }

    public Integer getCaseTypeSize() {
        if(soMsgCasetypeMapList != null){
            caseTypeSize = soMsgCasetypeMapList.size();
        }
        return caseTypeSize;
    }

    public void setCaseTypeSize(Integer caseTypeSize) {
        this.caseTypeSize = caseTypeSize;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public SoMsgUserWorkingtime getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(SoMsgUserWorkingtime workingTime) {
        this.workingTime = workingTime;
    }

    public SoMsgUserWorkingtimeDAO getSoMsgUserWorkingtimeDAO() {
        return soMsgUserWorkingtimeDAO;
    }

    public void setSoMsgUserWorkingtimeDAO(SoMsgUserWorkingtimeDAO soMsgUserWorkingtimeDAO) {
        this.soMsgUserWorkingtimeDAO = soMsgUserWorkingtimeDAO;
    }

    public String getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(String transferFrom) {
        this.transferFrom = transferFrom;
    }

    public String getTransferFromReason() {
        return transferFromReason;
    }

    public void setTransferFromReason(String transferFromReason) {
        this.transferFromReason = transferFromReason;
    }

    public String getTransferFromRemark() {
        return transferFromRemark;
    }

    public void setTransferFromRemark(String transferFromRemark) {
        this.transferFromRemark = transferFromRemark;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public boolean isKeepWorkingTime() {
        return keepWorkingTime;
    }

    public void setKeepWorkingTime(boolean keepWorkingTime) {
        this.keepWorkingTime = keepWorkingTime;
    }

    public boolean isbTransfer() {
        return bTransfer;
    }

    public void setbTransfer(boolean bTransfer) {
        this.bTransfer = bTransfer;
    }
}
