/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.controller;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.dao.SoAccountDAO;
import com.maxelyz.social.model.dao.SoActivityLogDAO;
import com.maxelyz.social.model.dao.SoActivityTypeDAO;
import com.maxelyz.social.model.dao.SoCaseTypeDAO;
import com.maxelyz.social.model.dao.SoMessageDAO;
import com.maxelyz.social.model.dao.SoMsgCasetypeMapDAO;
import com.maxelyz.social.model.dao.SoMsgUserWorkingtimeDAO;
import com.maxelyz.social.model.dao.SoWorkflowInstanceDAO;
import com.maxelyz.social.model.entity.SoAccount;
import com.maxelyz.social.model.entity.SoActivityLog;
import com.maxelyz.social.model.entity.SoActivityType;
import com.maxelyz.social.model.entity.SoCaseType;
import com.maxelyz.social.model.entity.SoEmailMessage;
import com.maxelyz.social.model.entity.SoEmailSignature;
import com.maxelyz.social.model.entity.SoMessage;
import com.maxelyz.social.model.entity.SoMsgCasetypeMap;
import com.maxelyz.social.model.entity.SoServiceOutgoingAccount;
import com.maxelyz.social.model.entity.SoUserSignature;
import com.maxelyz.social.model.entity.SoWorkflowInstance;
import com.maxelyz.social.model.entity.SoWorkflowInstanceLog;
import com.maxelyz.utils.JSFUtil;
import java.io.File;
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
import javax.faces.event.ValueChangeEvent;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

/**
 *
 * @author ukritj
 */
@ManagedBean(name="composeController")
@ViewScoped
public class ComposeController {
    
    private static Logger log = Logger.getLogger(ComposeController.class);
    //private static String PATH = "/Users/ukritj/maxar/userfiles/emailgatewaystore/";
    //private static String PATH = "D:\\emailgatewaystore\\";
    
    private String emailPath = JSFUtil.getApplication().getEmailFilePath();
    private String separate = JSFUtil.getApplication().getSeparatePath();
    
    private String userId;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String pBody;
    private Integer soCaseTypeId;
    private boolean TO = false;
    private boolean CC  = false;
    private boolean BCC = false;
    private String remark;
    private String signatureId;
    private String signatureString;
    private String selectedEmailString;
    
    private Map<String, File> listFileUpload;
    private List<String[]> filesUpload;
    private List<String[]> filesUploadDraft;
    private List<SoServiceOutgoingAccount> outList;
    private List<SoCaseType> soCaseTypeList;
    private List<String[]> signatureList;
    private List<SoUserSignature> soUserSignatureList;
    
    private List<SoMsgCasetypeMap> soMsgCasetypeMapList;
    private Integer caseTypeSize;
    private List<SoCaseType> soCaseTypeSubList;
    private Integer soCaseTypeSubId;
    
    private Integer soMessageId;
    private SoMessage soMessage;
    private SoEmailMessage soEmailMessage;
    private SoAccount soAccount;
    private String emailPriority;
    private String remarkApproval;
    private boolean showPanelSendMail;
    
    private Date ignoredDate;
    private String ignoredReason;
    private String ignoredCaseType;
    private String ignoredCaseTopic;
    private String ignoredRemark;
    private String ignoredBy;
    
    private boolean btnIgnoreStatus;
    
    @ManagedProperty(value = "#{soMessageDAO}")
    private SoMessageDAO soMessageDAO;
    @ManagedProperty(value = "#{soAccountDAO}")
    private SoAccountDAO soAccountDAO;
    @ManagedProperty(value = "#{soCaseTypeDAO}")
    private SoCaseTypeDAO soCaseTypeDAO;
    @ManagedProperty(value = "#{soMsgCasetypeMapDAO}")
    private SoMsgCasetypeMapDAO soMsgCasetypeMapDAO;
    @ManagedProperty(value = "#{soWorkflowInstanceDAO}")
    private SoWorkflowInstanceDAO soWorkflowInstanceDAO;
    @ManagedProperty(value = "#{soActivityLogDAO}")
    private SoActivityLogDAO soActivityLogDAO;
    @ManagedProperty(value = "#{soActivityTypeDAO}")
    private SoActivityTypeDAO soActivityTypeDAO;
    @ManagedProperty(value = "#{soMsgUserWorkingtimeDAO}")
    private SoMsgUserWorkingtimeDAO soMsgUserWorkingtimeDAO;
    
    @PostConstruct
    public void initialize() {
        
        soAccount = soAccountDAO.findSoAccount(9);
        emailPriority = "PRIORITY_MED";
        showPanelSendMail = true;
        
        if(JSFUtil.getRequestParameterMap("soMessageId") != null){
            soMessageId = Integer.parseInt(JSFUtil.getRequestParameterMap("soMessageId"));
            initEmailDetail();
        }else{
            soMessage = new SoMessage();
            soEmailMessage = new SoEmailMessage();
            soMessage.setSource_type_enum_id("EM");
            soMessage.setSource_subtype_enum_id("EM_OUT");
            soMessage.setSoAccount(soAccount);
            soMessage.setLastReceiveByUsers(JSFUtil.getUserSession().getUsers());
            soMessage.setLastReceiveByName(JSFUtil.getUserSession().getUsers().getLoginName());
            soMessage.setParentSoMessage(null);
        }
        
        userId = JSFUtil.getRequestParameterMap("userId");
        listFileUpload = new LinkedHashMap<String, File>();
        filesUpload = new ArrayList<String[]>();
        
        initSignature();
        
        outList = soMessageDAO.findAllSoServiceOutgoingAccount();
        soCaseTypeList = soCaseTypeDAO.findSoCaseTypeStatus();
        soCaseTypeSubList = new ArrayList<SoCaseType>();
        initSoMsgCasetypeMapList();
        initShowEditWfSup();
        initBtnIgnore();
    }
    
    private void initBtnIgnore(){
        btnIgnoreStatus = true;
        if(soMessage == null || soMessage.getId() == null){
            btnIgnoreStatus = false;
        }else if(soMessage.getCase_status() != null && (soMessage.getCase_status().equals("IG") || soMessage.getCase_status().equals("CL"))){
            btnIgnoreStatus = false;
        }else if(soMessage.getCase_status().equals("WF")){
            if(JSFUtil.getUserSession().getUserGroup().getRole().contains("Supervisor")){
                btnIgnoreStatus = true;
            }else{
                btnIgnoreStatus = false;
            }
        }else if(soMessage.getLastReceiveByUsers().getId().intValue() != JSFUtil.getUserSession().getUsers().getId().intValue()){
            btnIgnoreStatus = false;
        }
    }
    
    private void initShowEditWfSup(){
        if(soMessage != null && soMessage.getCase_status() != null && soMessage.getCase_status().equals("WF")){
            if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Supervisor")){
                showPanelSendMail = true;
            }else{
                showPanelSendMail = false;
            }
        }else if(soMessage.getLastReceiveByUsers().getId().intValue() == JSFUtil.getUserSession().getUsers().getId().intValue()){
            showPanelSendMail = true;
        }else{
            showPanelSendMail = false;
        }
    }
    
    private void initEmailDetail(){
        if(soMessageId != null){
            soMessage = soMessageDAO.findSoMessage(soMessageId);
            soEmailMessage = soMessage.getSoEmailMessage();
            from = soEmailMessage.getEmailFrom();
            to = soEmailMessage.getEmailTo();
            cc = soEmailMessage.getEmailCc();
            bcc = soEmailMessage.getEmailBcc();
            subject = soEmailMessage.getSubject();
            pBody = soEmailMessage.getBody();
            remark = soMessage.getRemark();
            emailPriority = soMessage.getPriority_enum_id();
            
            //list file in draft
            if(filesUploadDraft == null) filesUploadDraft = new ArrayList<String[]>();
            File file = null;
            String fName = "";
            String fNameMap = "";
            if(soEmailMessage.getAttachment() != null && !soEmailMessage.getAttachment().isEmpty()){
                String str = soEmailMessage.getAttachment();
                String strMap = soEmailMessage.getAttachmentMap();
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
        }
    }
    
    public void sendToSupAction(){
        
        saveDraft();
        
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
            //saveWorkingTime();
        }catch(Exception e){
            log.error(e);
        }
        JSFUtil.redirect("emailthankyou.jsf");
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
    
    /*public void saveWorkingTime(){
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
    }*/
    
    public void saveDraftAction(){
        this.saveDraft();
        JSFUtil.redirect("emailthankyou.jsf");
    }
    
    private void saveDraft(){
        
        soMessage.setUser_id(to);
        soMessage.setPriority_enum_id((emailPriority == null || emailPriority.equals("")) ? soMessage.getPriority_enum_id() : emailPriority);
        soMessage.setContent(pBody);
        if(soMessage.getCase_status() != null && soMessage.getCase_status().equals("WF")){
            soMessage.setCase_status("WF");
        }else{
            soMessage.setCase_status("DF");
        }
        soMessage.setRemark(remark);
        if (soCaseTypeId != null && soCaseTypeId != 0) {
            soMessage.setCasetype_enum_id(soCaseTypeId.toString());
        }
        
        soEmailMessage.setSoMessage(soMessage);
        if(from != null && !from.isEmpty()){
            soEmailMessage.setEmailFrom(from);
        }else{
            soEmailMessage.setEmailFrom("ropsvc@thaiairways.com");
        }
        soEmailMessage.setEmailTo(to);
        soEmailMessage.setEmailReplyTo("ropsvc@thaiairways.com");
        soEmailMessage.setEmailCc(cc);
        soEmailMessage.setEmailBcc(bcc);
        soEmailMessage.setSubject(subject);
        soEmailMessage.setBody(pBody);
        soEmailMessage.setDirection("OUT");
        soEmailMessage.setSendStatus("NO");
        soEmailMessage.setRetryCnt(0);
        
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
        soEmailMessage.setAttachment(strFileName);
        soEmailMessage.setAttachmentMap(strFileName);

        soMessage.setSoEmailMessage(soEmailMessage);
        
        try{
            if(soMessageId == null){
                soMessage.setCreate_by(JSFUtil.getUserSession().getUsers().getId());
                soMessage.setCreate_date(new Date());
                soMessage.setUpdate_date(new Date());
                soMessage.setUpdate_by(JSFUtil.getUserSession().getUsers().getId());
                soMessage.setActivity_time_format(new Date());
                soMessageDAO.create(soMessage);
            }else{
                soMessage.setUpdate_date(new Date());
                soMessage.setUpdate_by(JSFUtil.getUserSession().getUsers().getId());
                soMessageDAO.edit(soMessage);
            }
        }catch(Exception e){
            log.error(e);
        }
        
        this.insertSoMsgCasetypeMap(soMessage);
        
        try{
            if(listFileUpload != null && !listFileUpload.isEmpty()){
                for (Map.Entry<String, File> item : listFileUpload.entrySet()) {
                    String fileName = item.getKey();
                    File f2 = item.getValue();

                    File f1 = new File(emailPath + soMessage.getSoAccount().getId());
                    if(!f1.exists()) {
                        f1.mkdir();
                    }
                    //f1 = new File(emailPath + soMessageDraft.getSoAccount().getId() + "/" + soMessageDraft.getId());
                    f1 = new File(emailPath + soMessage.getSoAccount().getId() + separate + soMessage.getId());
                    if(!f1.exists()) {
                        f1.mkdir();
                    }
                    //f1 = new File(emailPath + soMessageDraft.getSoAccount().getId() + "/" + soMessageDraft.getId() + "/temp");
                    f1 = new File(emailPath + soMessage.getSoAccount().getId() + separate + soMessage.getId() + separate + "temp");
                    if(!f1.exists()) {
                        f1.mkdir();
                    }
                    //f1 = new File(emailPath + soMessageDraft.getSoAccount().getId() + "/" + soMessageDraft.getId() + "/temp/" + fileName);
                    f1 = new File(emailPath + soMessage.getSoAccount().getId() + separate + soMessage.getId() + separate + "temp" + separate + fileName);
                    if (f2.exists()) {
                        f2.renameTo(f1);
                    }
                }
            }
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
                    //c.setSoMessage(soMessage);
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
                    //c.setSoMessage(soMessage);
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
        try{
            if(soMessage != null){
                soMsgCasetypeMapList = soMsgCasetypeMapDAO.findBySoMessageId(soMessage.getId());
            }else{
                soMsgCasetypeMapList = new ArrayList<SoMsgCasetypeMap>();
            }
        }catch(Exception e){
            log.error(e);
        }
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
    
    private void insertSoMsgCasetypeMap(SoMessage soMessage){
    
        try {
            //if(soMessage.getSoMsgCasetypeMapCollection() != null && !soMessage.getSoMsgCasetypeMapCollection().isEmpty()){
                soMsgCasetypeMapDAO.removeBySoMessageId(soMessage.getId());
            //}
            if(soMsgCasetypeMapList != null && !soMsgCasetypeMapList.isEmpty()){
                for(SoMsgCasetypeMap c1 : soMsgCasetypeMapList){
                    c1.setId(null);
                    c1.setSoMessage(soMessage);
                    soMsgCasetypeMapDAO.create(c1);
                }
            }
        } catch(NonexistentEntityException e){
            log.error(e);
        } catch (PreexistingEntityException e) {
            log.error(e);
        }
        
    }
    
    private void initSignature(){
        signatureList = new ArrayList<String[]>();
        //soEmailSignatureList = soMessageDAO.findSoEmailSignatureByAccountId(soMessage.getSoAccount().getId());
        soUserSignatureList = soMessageDAO.findSoUserSignatureByUserId(JSFUtil.getUserSession().getUsers().getId());
        for(SoUserSignature u : soUserSignatureList){
            String[] stra = new String[2];
            stra[0] = "U" + u.getId();
            stra[1] = u.getName();
            signatureList.add(stra);
        }
        /*for(SoEmailSignature e : soEmailSignatureList){
            String[] stra1 = new String[2];
            stra1[0] = "A" + e.getId();
            stra1[1] = e.getName();
            signatureList.add(stra1);
        }*/
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
            if(pBody == null) pBody = "";
            if(pBody.indexOf(beginTxt) != -1 && pBody.indexOf(endTxt) != -1){
                pBody = pBody.substring(0, pBody.indexOf(beginTxt)) + "" + beginTxt + str + endTxt + "" + pBody.substring(pBody.indexOf(endTxt) + endTxt.length(), pBody.length());
            }else{
                pBody = pBody + "<br/>" + beginTxt + str + endTxt + "<br/>";
            }
            */
        }
    }

    public void selectTemplateListener() {
        pBody += "<br/>";
        pBody += JSFUtil.getRequestParameterMap("dlgReturnValue");

        FacesContext.getCurrentInstance().renderResponse();
    }

    public void removeFileTemp() {
        String rmFile = StringUtils.trim(JSFUtil.getRequestParameterMap("rmFile"));
        File f = new File(emailPath + separate+ "temp" + separate + rmFile);
        //File f = new File(PATH + "/temp/" + rmFile);
        if (f.exists()) {
            f.delete();
        }
        filesUpload = new ArrayList<String[]>();
        Map<String, File> tmp = new LinkedHashMap<String, File>();
        for (Map.Entry<String, File> item : listFileUpload.entrySet()) {
            if(!item.getValue().getName().equals(rmFile)){
                tmp.put(item.getKey(), item.getValue());
                String[] stra = new String[2];
                stra[0] = item.getKey();
                stra[1] = item.getValue().getName();
                filesUpload.add(stra);
            }
        }
        listFileUpload = tmp;
    }

    public void removeFileTempDraft() {
        String rmFile = StringUtils.trim(JSFUtil.getRequestParameterMap("rmFile"));
        //File f = new File(emailPath + "/" + soMessageDraft.getSoAccount().getId() + "/" + soMessageDraft.getId() + "/temp/" + rmFile);
        File f = new File(emailPath + separate + soMessage.getSoAccount().getId() + separate + soMessage.getId() + separate + "temp" + separate + rmFile);
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
    
    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        File file = new File(emailPath + separate + "temp"+ separate + (new Date()).getTime() + fileName.substring(fileName.lastIndexOf("."), fileName.length()));
        this.copy(inputStream, file);
        listFileUpload.put(fileName, file);
        String[] stra = new String[2];
        stra[0] = fileName;
        stra[1] = file.getName();
        filesUpload.add(stra);

    } 
    
    public void sendAction() {
        String status = "";
        try {
            
            this.updateWorkflow();
            status = JSFUtil.getRequestParameterMap("statusEmail");
            createMessage(to, cc, bcc, from, subject, pBody, null);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JSFUtil.redirect("emailthankyou.jsf");
    }
    
    private void createMessage(String to, String cc, String bcc, String from, String subject, String pBody, List<File> attachments) {
        try {
            String ticketId = soMessageDAO.getTicketId();
            
            SoAccount soAccount = soAccountDAO.findSoAccount(9);
            
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
            String subjectMain = ("PrimeTime [Service Ticket # " + ticketId + "] - ") + subject;
            message.setSubject(subjectMain);
            // create the message part 
            //MimeBodyPart content = new MimeBodyPart();
            // fill message
            String str = "<html>"
                    + "<head></head>"
                    + "<body>"
                    + "<table width='100%' border='0' cellpadding='0' cellspacing='0'>"
                    + "<tr><td style='height: 20px'></td></tr>"
                    + "<tr><td>PrimeTime [Service Ticket # " + ticketId + "]</td></tr>"
                    + "<tr><td style='height: 20px'></td></tr>"
                    + "<tr>"
                    + "<td>"
                    + pBody
                    + "</td>"
                    + "</tr>"
                    + "<tr><td style='height: 20px'></td></tr>"
                    + "</table>"
                    + "</body>"
                    + "</html>";
            
            Multipart multipart = new MimeMultipart("mixed");
            
            MimeBodyPart html = new MimeBodyPart();
            html.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
            html.setContent(str, "text/html; charset=utf-8");     //set the text/html version
            multipart.addBodyPart(html);
            
            String strFileName = "";
            DataHandler dataHandler = null;
            if(filesUploadDraft != null){
                for (String[] filename : filesUploadDraft) {
                    strFileName += filename[0] + ",";
                    MimeBodyPart attachment = new MimeBodyPart();
                    File file = new File(emailPath + separate + soMessage.getSoAccount().getId() + separate + soMessage.getId() + separate + "temp" + separate + filename[0]);
                    if(file.exists()){
                        FileDataSource source = new FileDataSource(file);
                        dataHandler = new DataHandler(source);
                        attachment.setDataHandler(dataHandler);
                        attachment.setFileName(MimeUtility.encodeText(filename[0], "UTF-8", null));
                        multipart.addBodyPart(attachment);
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
                multipart.addBodyPart(attachment);
            }
            if(!strFileName.isEmpty()){
                strFileName = strFileName.substring(0, strFileName.length() - 1);
            }
            
            /*
            String fileStr1 = "";
            DataHandler dataHandler = null;
            if(listFileUpload != null && !listFileUpload.isEmpty()){
                for (Map.Entry<String, File> item : listFileUpload.entrySet()) {
                    fileStr1 += item.getKey() + ",";
                    MimeBodyPart attachment = new MimeBodyPart();
                    FileDataSource source = new FileDataSource(item.getValue());
                    dataHandler = new DataHandler(source);
                    attachment.setDataHandler(dataHandler);
                    attachment.setFileName(item.getKey());
                    multipart.addBodyPart(attachment);
                }
            }
            
            if(!fileStr1.isEmpty()){
                fileStr1 = fileStr1.substring(0, fileStr1.length() - 1);
            }
            */
            
            // integration
            message.setContent(multipart);
            // store file
            //message.writeTo(new FileOutputStream(new File("/Users/ukritj/temp/test_save_email.eml")));
            //userId = "ukrit@terrabo.co.th";
            //SoMessage som = new SoMessage();
            soMessage.setUser_id(to);
            soMessage.setPriority_enum_id(emailPriority);
            soMessage.setContent(pBody);
            soMessage.setSource_type_enum_id("EM");
            soMessage.setSource_subtype_enum_id("EM_OUT");
            soMessage.setSoAccount(soAccount);
            if(soMessage.getCreate_by() == null) soMessage.setCreate_by(JSFUtil.getUserSession().getUsers().getId());
            if(soMessage.getCreate_date() == null) soMessage.setCreate_date(new Date());
            soMessage.setUpdate_date(new Date());
            soMessage.setUpdate_by(JSFUtil.getUserSession().getUsers().getId());
            soMessage.setLastReceiveByUsers(JSFUtil.getUserSession().getUsers());
            soMessage.setLastReceiveByName(JSFUtil.getUserSession().getUsers().getLoginName());
            soMessage.setLastReceiveDate(new Date());
            if(soMessage.getActivity_time_format() == null) soMessage.setActivity_time_format(new Date());
            soMessage.setCase_status("CL");
            soMessage.setRemark(remark);
            soMessage.setParentSoMessage(null);
            soMessage.setTicketId(ticketId);
            if(soCaseTypeId != null && soCaseTypeId != 0){
                soMessage.setCasetype_enum_id(soCaseTypeId.toString());
            }
            
            //SoEmailMessage soem = new SoEmailMessage();
            soEmailMessage.setSoMessage(soMessage);
            if(from != null && !from.isEmpty()){
                soEmailMessage.setEmailFrom(from);
            }else{
                soEmailMessage.setEmailFrom("ropsvc@thaiairways.com");
            }
            soEmailMessage.setEmailTo(to);
            soEmailMessage.setEmailReplyTo("ropsvc@thaiairways.com");
            soEmailMessage.setEmailCc(cc);
            soEmailMessage.setEmailBcc(bcc);
            soEmailMessage.setSubject(subjectMain);
            soEmailMessage.setBody(pBody);
            soEmailMessage.setDirection("OUT");
            soEmailMessage.setSendStatus("RD");
            soEmailMessage.setRetryCnt(0);
            soEmailMessage.setAttachment(strFileName);
            soEmailMessage.setAttachmentMap(strFileName);
            
            soMessage.setSoEmailMessage(soEmailMessage);
            if(soMessage.getId() == null){
                soMessageDAO.create(soMessage);
            }else{
                soMessageDAO.edit(soMessage);
            }
            
            this.insertSoMsgCasetypeMap(soMessage);
            
            File f1 = new File(emailPath + soAccount.getId());
            if(!f1.exists()) {
                f1.mkdir();
            }
            File f2 = new File(emailPath + soAccount.getId() + separate + soMessage.getId());
            //File f2 = new File(PATH + soAccount.getId() + "/" + som.getId());
            if(!f2.exists()) {
                f2.mkdir();
            }
            if(f2.exists()) {
                message.writeTo(new FileOutputStream(new File(emailPath + soAccount.getId() + separate + soMessage.getId() + separate + soMessage.getId() + ".eml")));
                //message.writeTo(new FileOutputStream(new File(PATH + soAccount.getId() + "/" + som.getId() + "/" + som.getId() + ".eml")));
            }
            
            try{
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
            java.util.logging.Logger.getLogger(ComposeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void updateWorkflow(){
        try{
            if(soMessage != null && soMessage.getId() != null) {
                if(soMessage.getCase_status() != null && soMessage.getCase_status().equals("WF") && JSFUtil.getUserSession().getUsers().getUserGroup().getRole().contains("Supervisor")){
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
            }
        }catch(Exception e){
            log.error(e);
        }
    }
    
    public void selectUserActionListener(){
        if(TO) {
            if(to != null && !to.isEmpty()) {
                to += ","+selectedEmailString;
            } else{
                to = selectedEmailString;
            }
        } else if(CC) {
            if(cc != null && !cc.isEmpty()) {
                cc += ","+selectedEmailString;
            } else {
                cc = selectedEmailString;
            }
        } else if(BCC) {
            if(bcc != null && !bcc.isEmpty()) {
                bcc += ","+selectedEmailString;
            } else {
                bcc = selectedEmailString;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getpBody() {
        return pBody;
    }

    public void setpBody(String pBody) {
        this.pBody = pBody;
    }

    public Integer getSoCaseTypeId() {
        return soCaseTypeId;
    }

    public void setSoCaseTypeId(Integer soCaseTypeId) {
        this.soCaseTypeId = soCaseTypeId;
    }

    public Map<String, File> getListFileUpload() {
        return listFileUpload;
    }

    public void setListFileUpload(Map<String, File> listFileUpload) {
        this.listFileUpload = listFileUpload;
    }

    public List<String[]> getFilesUpload() {
        return filesUpload;
    }

    public void setFilesUpload(List<String[]> filesUpload) {
        this.filesUpload = filesUpload;
    }

    public SoMessageDAO getSoMessageDAO() {
        return soMessageDAO;
    }

    public void setSoMessageDAO(SoMessageDAO soMessageDAO) {
        this.soMessageDAO = soMessageDAO;
    }

    public SoAccountDAO getSoAccountDAO() {
        return soAccountDAO;
    }

    public void setSoAccountDAO(SoAccountDAO soAccountDAO) {
        this.soAccountDAO = soAccountDAO;
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

    public List<SoServiceOutgoingAccount> getOutList() {
        return outList;
    }

    public void setOutList(List<SoServiceOutgoingAccount> outList) {
        this.outList = outList;
    }

    public List<SoCaseType> getSoCaseTypeList() {
        return soCaseTypeList;
    }

    public void setSoCaseTypeList(List<SoCaseType> soCaseTypeList) {
        this.soCaseTypeList = soCaseTypeList;
    }

    public SoCaseTypeDAO getSoCaseTypeDAO() {
        return soCaseTypeDAO;
    }

    public void setSoCaseTypeDAO(SoCaseTypeDAO soCaseTypeDAO) {
        this.soCaseTypeDAO = soCaseTypeDAO;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(String signatureId) {
        this.signatureId = signatureId;
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

    public String getSelectedEmailString() {
        return selectedEmailString;
    }

    public void setSelectedEmailString(String selectedEmailString) {
        this.selectedEmailString = selectedEmailString;
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

    public List<SoMsgCasetypeMap> getSoMsgCasetypeMapList() {
        return soMsgCasetypeMapList;
    }

    public void setSoMsgCasetypeMapList(List<SoMsgCasetypeMap> soMsgCasetypeMapList) {
        this.soMsgCasetypeMapList = soMsgCasetypeMapList;
    }

    public SoMsgCasetypeMapDAO getSoMsgCasetypeMapDAO() {
        return soMsgCasetypeMapDAO;
    }

    public void setSoMsgCasetypeMapDAO(SoMsgCasetypeMapDAO soMsgCasetypeMapDAO) {
        this.soMsgCasetypeMapDAO = soMsgCasetypeMapDAO;
    }

    public String getSignatureString() {
        return signatureString;
    }

    public void setSignatureString(String signatureString) {
        this.signatureString = signatureString;
    }

    public Integer getSoMessageId() {
        return soMessageId;
    }

    public void setSoMessageId(Integer soMessageId) {
        this.soMessageId = soMessageId;
    }

    public SoMessage getSoMessage() {
        return soMessage;
    }

    public void setSoMessage(SoMessage soMessage) {
        this.soMessage = soMessage;
    }

    public SoEmailMessage getSoEmailMessage() {
        return soEmailMessage;
    }

    public void setSoEmailMessage(SoEmailMessage soEmailMessage) {
        this.soEmailMessage = soEmailMessage;
    }

    public String getEmailPriority() {
        return emailPriority;
    }

    public void setEmailPriority(String emailPriority) {
        this.emailPriority = emailPriority;
    }

    public List<String[]> getFilesUploadDraft() {
        return filesUploadDraft;
    }

    public void setFilesUploadDraft(List<String[]> filesUploadDraft) {
        this.filesUploadDraft = filesUploadDraft;
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

    public SoActivityLogDAO getSoActivityLogDAO() {
        return soActivityLogDAO;
    }

    public void setSoActivityLogDAO(SoActivityLogDAO soActivityLogDAO) {
        this.soActivityLogDAO = soActivityLogDAO;
    }

    public SoActivityTypeDAO getSoActivityTypeDAO() {
        return soActivityTypeDAO;
    }

    public void setSoActivityTypeDAO(SoActivityTypeDAO soActivityTypeDAO) {
        this.soActivityTypeDAO = soActivityTypeDAO;
    }

    public SoMsgUserWorkingtimeDAO getSoMsgUserWorkingtimeDAO() {
        return soMsgUserWorkingtimeDAO;
    }

    public void setSoMsgUserWorkingtimeDAO(SoMsgUserWorkingtimeDAO soMsgUserWorkingtimeDAO) {
        this.soMsgUserWorkingtimeDAO = soMsgUserWorkingtimeDAO;
    }

    public boolean isShowPanelSendMail() {
        return showPanelSendMail;
    }

    public void setShowPanelSendMail(boolean showPanelSendMail) {
        this.showPanelSendMail = showPanelSendMail;
    }

    public Date getIgnoredDate() {
        return ignoredDate;
    }

    public void setIgnoredDate(Date ignoredDate) {
        this.ignoredDate = ignoredDate;
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

    public String getIgnoredCaseTopic() {
        return ignoredCaseTopic;
    }

    public void setIgnoredCaseTopic(String ignoredCaseTopic) {
        this.ignoredCaseTopic = ignoredCaseTopic;
    }

    public String getIgnoredRemark() {
        return ignoredRemark;
    }

    public void setIgnoredRemark(String ignoredRemark) {
        this.ignoredRemark = ignoredRemark;
    }

    public String getIgnoredBy() {
        return ignoredBy;
    }

    public void setIgnoredBy(String ignoredBy) {
        this.ignoredBy = ignoredBy;
    }

    public boolean isBtnIgnoreStatus() {
        return btnIgnoreStatus;
    }

    public void setBtnIgnoreStatus(boolean btnIgnoreStatus) {
        this.btnIgnoreStatus = btnIgnoreStatus;
    }
}
