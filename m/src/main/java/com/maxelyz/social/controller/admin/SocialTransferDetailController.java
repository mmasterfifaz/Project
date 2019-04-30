/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.controller.admin;

import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.social.model.dao.*;
import com.maxelyz.social.model.entity.SoChannel;
import com.maxelyz.social.model.entity.SoEmailAccount;
import com.maxelyz.social.model.entity.SoEmailMessage;
import com.maxelyz.social.model.entity.SoMessage;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.MxString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Prawait
 */
@ManagedBean
@ViewScoped
public class SocialTransferDetailController {
    
    private static Logger logger = Logger.getLogger(SocialTransferDetailController.class);
    private String SUCCESS = "service.xhtml?faces-redirect=true";
    private Integer step = 0;

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private Map<String, Integer> userGroupList;
    private Map<String, Integer> targetUserGroupList;
    private List<Users> userList = new ArrayList<Users>();
    private List<Users> targetUserList = new ArrayList<Users>();
    private List<SoChannel> soChannelList = new ArrayList<SoChannel>();
    private List<SoEmailAccount> soEmailAccountList = new ArrayList<SoEmailAccount>();
    private List<SoEmailMessage> soEmailMessageList = new ArrayList<SoEmailMessage>();
    private SelectItem[] messagePriorityList;
    private SelectItem[] caseStatusList;

    private String selectedChannelId;
    private String selectedAccountId;
    private String selectedCaseStatus;
    private String selectedMessagePriority;
    private String selectedFromEmail;
    private Integer selectedMsgId;
    private Integer selectedRelatedMsgId;
    private Integer selectedUserGroupId;
    private String selectedUsers;
    private String selectedUsersId;
    private Date fromDate, toDate;
    private Integer cnt=0;

    private Integer targetUserGroupId;
    private String targetUsers;
    private String targetUsersId;
    private String targetReason;
    private String targetRemark;

    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{soChannelDAO}")
    private SoChannelDAO soChannelDAO;
    @ManagedProperty(value = "#{soEmailAccountDAO}")
    private SoEmailAccountDAO soEmailAccountDAO;
    @ManagedProperty(value = "#{soEmailMessageDAO}")
    private SoEmailMessageDAO soEmailMessageDAO;
    @ManagedProperty(value = "#{soServiceDAO}")
    private SoServiceDAO soServiceDAO;
    @ManagedProperty(value = "#{soMessageDAO}")
    private SoMessageDAO soMessageDAO;
    @ManagedProperty(value = "#{soMsgUserAssigntimeDAO}")
    private SoMsgUserAssigntimeDAO soMsgUserAssigntimeDAO;
    @ManagedProperty(value = "#{soActivityLogDAO}")
    private SoActivityLogDAO soActivityLogDAO;


    @PostConstruct
    public void initialize() {
//        selectedChannelId = "5";
//        selectedAccountId = "15";
//        selectedCaseStatus = "PS";
//        selectedMessagePriority = "PRIORITY_HI";
//        selectedFromEmail = "apichat@terrabit.co.th";
//        selectedMsgId = 4505;
//        selectedRelatedMsgId = 4441;
//        selectedUserGroupId = 1;
//        selectedUsers = "dev01";
//        selectedUsersId = "125";

        Date today = new Date();
        fromDate = today;
        toDate = today;

//        Calendar c = Calendar.getInstance();
//        c.setTime(fromDate);
//        c.add(Calendar.DATE, -6);
//        fromDate = c.getTime();

//        fromDate = DateUtils.addDays(fromDate, -6);

        userGroupList = userGroupDAO.getUserGroupList();
        targetUserGroupList = userGroupDAO.getUserGroupList();
        userList = usersDAO.findUsersEntities();
        targetUserList = usersDAO.findUsersEntities();

        soChannelList = soChannelDAO.findSoChannelStatus();
        soEmailAccountList = soEmailAccountDAO.findSoEmailAccountStatus();
        step = 1;

    }
    
    public void resetSelected(){
//        selectedChannelId = "";
//        selectedAccountId = "";
//        selectedCaseStatus = "";
//        selectedMessagePriority = "";
//        selectedFromEmail = "";
        selectedMsgId = null;
        selectedRelatedMsgId = null;
//        selectedUserGroupId = 0;
//        selectedUsers = "";
//        selectedUsersId = "";

//        targetUserGroupId = 0;
//        targetUsers = "";
//        targetUsersId = "";

        cnt = 0;
        soEmailMessageList.clear();
        selectedIds.clear();
    }

    private void init2(){
    
    }
    
    private void init3(){
    
    }
    
    public String previous(){
        return SUCCESS;
    }
    
    public void step1to2(){
        step = 2;
        init2();
    }
    
    public void step2to3(){
        step = 3;
    }

    public void step3to2(){
        step = 2;
    }
    
    public void step2to1(){
        step = 1;
    }
    
    public String save(){
        try{

        
        }catch(Exception e){
            logger.error(e);
        }
        return SUCCESS;
    }

    public void testActionListener(ActionEvent event) {
        List<Users> userses = soServiceDAO.findSoServiceUsers(11);
        for (Users u : userses){
            System.out.println(u.getId()+" : "+u.getLoginName());

        }
    }

    public void selectedUserGroupListener(ValueChangeEvent event) {
        Integer id = (Integer) event.getNewValue();
        if (id!=null) {
            userList = usersDAO.findSoAgentByGroupId(id);
        } else {
            userList = usersDAO.findUsersEntities();
        }
    }

    public void targetUserGroupListener(ValueChangeEvent event) {
        Integer id = (Integer) event.getNewValue();
        if (id!=null) {
            targetUserList = usersDAO.findSoAgentByGroupId(id);
        } else {
            targetUserList = usersDAO.findUsersEntities();
        }
    }

    public void transferActionListener() {

//        System.out.println("transferActionListener() ============");
//        System.out.println("targetUserGroupId : "+targetUserGroupId);
//        System.out.println("targetUsers : "+targetUsers);
//        System.out.println("targetReason : "+targetReason);
//        System.out.println("targetRemark : "+targetRemark);

        if (StringUtils.isBlank(targetUsersId)) return;

        Users u = usersDAO.findUsers(Integer.valueOf(targetUsersId));

        if (u==null) return;
        if (soEmailMessageList.size()<=0) return;

//        for (SoEmailMessage soEmailMessage : soEmailMessageList){
//            System.out.println("transfering --> "+soEmailMessage.getSoMessage().getId()+" : "+soEmailMessage.getEmailFrom()
//                    + " --> " + soEmailMessage.getSoMessage().getReceive_by_name()
//                    + " --> " + u.getLoginName());
//
//            soMessageDAO.assignToUser(soEmailMessage.getSoMessage().getId(), u);
//            soMsgUserAssigntimeDAO.closeTransfer(soEmailMessage.getSoMessage().getId(), u.getId());
//            soMsgUserAssigntimeDAO.insertTransfer(soEmailMessage.getSoMessage().getId(), u.getId(), u.getId());
//        }

        for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
            if (item.getValue()) {
                SoMessage soMessage = soMessageDAO.findSoMessage(item.getKey());
                Users lastUser = soMessage.getLastReceiveByUsers();

                System.out.println("transfering --> "+soMessage.getId()+" : "+soMessage.getUser_id()
                        + " --> " + soMessage.getLastReceiveByName()
                        + " --> " + u.getLoginName());

                Integer createById =  JSFUtil.getUserSession().getUsers().getId();

                if (lastUser==null) {
                    soMessageDAO.assignToUserFirst(soMessage.getId(), u);
                } else {
                    soMessageDAO.assignToUser(soMessage.getId(), u);
                }

                soMsgUserAssigntimeDAO.closeTransfer(soMessage.getId(), createById);
                soMsgUserAssigntimeDAO.insertTransfer(soMessage.getId(), u.getId(), createById, lastUser==null?null:lastUser.getId());
                soActivityLogDAO.insertActivityLog(soMessage.getId(), createById, 12, targetReason, targetRemark);
            }
        }

        resetSelected();
    }

    public void searchActionListener() {
        selectedIds.clear();

//        selectedMsgID = JSFUtil.getRequestParameterMap("selectedMsgID");

//        System.out.println("searchActionListener() ============");
//        System.out.println("selectedChannelId : "+selectedChannelId);
//        System.out.println("selectedAccountId : "+selectedAccountId);
//        System.out.println("selectedCaseStatus : "+selectedCaseStatus);
//        System.out.println("selectedMessagePriority : "+selectedMessagePriority);
//        System.out.println("selectedFromEmail : "+selectedFromEmail);
//        System.out.println("selectedMsgId : "+selectedMsgId);
//        System.out.println("selectedRelatedMsgId : "+selectedRelatedMsgId);
//        System.out.println("selectedUserGroupId : "+selectedUserGroupId);
//        System.out.println("selectedUsers : "+selectedUsers);
//        System.out.println("fromDate : "+fromDate);
//        System.out.println("toDate : "+toDate);

//                " and ch.id=5 " +    //=578
//                " and ac.id=15 " +    //=336
//                " and m.case_status='PS' " +    //=8
//                " and m.priority_enum_id='PRIORITY_HI' " +    //=10
//                " and o.emailFrom='apichat@terrabit.co.th' " +    //=17
//                " and m.id=4505 " +    //=1
//                " and m.parentSoMessage.id=4441 " +    //=3
//                " and m.receive_by_id=125 " +    //=51
//                " and m.receive_by_id in (select u.id from Users u where u.userGroup.id=1) " +    //=306

        String strCond="";

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        if ((fromDate!=null) && (toDate!=null)) {
            String sqlFromDate = sdf1.format(fromDate) + " 00:00:00";
            String sqlToDate = sdf1.format(toDate) + " 23:59:59";
            strCond += String.format(" and m.activity_time_format between %s and %s ", MxString.quotedStrNull(sqlFromDate), MxString.quotedStrNull(sqlToDate));
        }

        if (StringUtils.isNotBlank(selectedChannelId) && !selectedChannelId.equals("0")){
            strCond += String.format(" and ch.id=%s ", selectedChannelId);
        }
        if (StringUtils.isNotBlank(selectedAccountId) && !selectedAccountId.equals("0")){
            strCond += String.format(" and ac.id=%s ", selectedAccountId);
        }
        if (StringUtils.isNotBlank(selectedCaseStatus) && !selectedCaseStatus.equals("0")){
            strCond += String.format(" and m.case_status in (%s) ", MxString.quotedInStrNull(selectedCaseStatus));
        }
        if (StringUtils.isNotBlank(selectedMessagePriority) && !selectedMessagePriority.equals("0")){
            strCond += String.format(" and m.priority_enum_id=%s ", MxString.quotedStrNull(selectedMessagePriority));
        }
        if (StringUtils.isNotBlank(selectedFromEmail)){
//            strCond += String.format(" and o.emailFrom=%s ", MxString.quotedStrNull(selectedFromEmail));
            strCond += String.format(" and o.emailReplyTo=%s ", MxString.quotedStrNull(selectedFromEmail));
        }
        if (MxString.moreThanZero(selectedMsgId)){
            strCond += String.format(" and m.id=%s ", selectedMsgId);
        }
        if (MxString.moreThanZero(selectedRelatedMsgId)){
            strCond += String.format(" and m.parentSoMessage.id=%s ", selectedRelatedMsgId);
        }
        if (StringUtils.isNotBlank(selectedUsersId) && !selectedUsersId.equals("0")){
            strCond += String.format(" and m.lastReceiveByUsers.id=%s ", MxString.quotedStrNull(selectedUsersId));
        } else {
            if (MxString.moreThanZero(selectedUserGroupId)){
                strCond += String.format(" and m.lastReceiveByUsers.id in (select u.id from Users u where u.userGroup.id=%s) ", selectedUserGroupId);
            }
        }

        System.out.println("strCond : "+strCond);
        soEmailMessageList = soEmailMessageDAO.findSoEmailMessageTransferCond(strCond);
        System.out.println(soEmailMessageList.size());
        cnt = soEmailMessageList.size();

//        Integer cnt=0;
//        for (SoEmailMessage som : soEmailMessageList){
//            try {
//                System.out.println((++cnt)+" : "+som.getSoMessage().getId()+" : "+som.getEmailFrom());
//            } catch (Exception e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//        }
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public Map<String, Integer> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(Map<String, Integer> userGroupList) {
        this.userGroupList = userGroupList;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public List<Users> getUserList() {
        return userList;
    }

    public void setUserList(List<Users> userList) {
        this.userList = userList;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getSelectedUserGroupId() {
        return selectedUserGroupId;
    }

    public void setSelectedUserGroupId(Integer selectedUserGroupId) {
        this.selectedUserGroupId = selectedUserGroupId;
    }

    public SelectItem[] getCaseStatusList() {
//        caseStatusList = new SelectItem[8];
//        caseStatusList[0] = new SelectItem("NW", "NEW");
//        caseStatusList[1] = new SelectItem("PL", "POOLED");
//        caseStatusList[2] = new SelectItem("AS", "ASSIGNED");
//        caseStatusList[3] = new SelectItem("PS", "PROCESSING");
//        caseStatusList[4] = new SelectItem("WF", "PROCESS_WORKFLOW");
//        caseStatusList[5] = new SelectItem("FL", "PENDING");
//        caseStatusList[6] = new SelectItem("CL", "CLOSED");
//        caseStatusList[7] = new SelectItem("OP", "DRAFT");

        caseStatusList = new SelectItem[7];
        caseStatusList[0] = new SelectItem("NW,PL", "QUEUED");
        caseStatusList[1] = new SelectItem("AS", "ASSIGNED");
        caseStatusList[2] = new SelectItem("PS,DF", "OPENED");
        caseStatusList[3] = new SelectItem("WF", "Request FOR APPROVAL");
        caseStatusList[4] = new SelectItem("FL", "FOLLOWED");
        caseStatusList[5] = new SelectItem("IG", "IGNORED");
        caseStatusList[6] = new SelectItem("CL", "CLOSED");

        return caseStatusList;
    }

    public void setCaseStatusList(SelectItem[] caseStatusList) {
        this.caseStatusList = caseStatusList;
    }

    public SelectItem[] getMessagePriorityList() {
        messagePriorityList = new SelectItem[4];
//        messagePriorityList[0] = new SelectItem("1", "Low");
//        messagePriorityList[1] = new SelectItem("2", "Medium");
//        messagePriorityList[2] = new SelectItem("3", "High");
//        messagePriorityList[3] = new SelectItem("4", "Immediately");
        messagePriorityList[0] = new SelectItem("PRIORITY_LOW", "Low");
        messagePriorityList[1] = new SelectItem("PRIORITY_MED", "Medium");
        messagePriorityList[2] = new SelectItem("PRIORITY_HI", "High");
        messagePriorityList[3] = new SelectItem("PRIORITY_IMM", "Immediately");
        return messagePriorityList;
    }

    public void setMessagePriorityList(SelectItem[] messagePriorityList) {
        this.messagePriorityList = messagePriorityList;
    }

    public List<SoChannel> getSoChannelList() {
        return soChannelList;
    }

    public void setSoChannelList(List<SoChannel> soChannelList) {
        this.soChannelList = soChannelList;
    }

    public SoChannelDAO getSoChannelDAO() {
        return soChannelDAO;
    }

    public void setSoChannelDAO(SoChannelDAO soChannelDAO) {
        this.soChannelDAO = soChannelDAO;
    }

    public Integer getSelectedMsgId() {
        return selectedMsgId;
    }

    public void setSelectedMsgId(Integer selectedMsgId) {
        this.selectedMsgId = selectedMsgId;
    }

    public Integer getSelectedRelatedMsgId() {
        return selectedRelatedMsgId;
    }

    public void setSelectedRelatedMsgId(Integer selectedRelatedMsgId) {
        this.selectedRelatedMsgId = selectedRelatedMsgId;
    }

    public String getSelectedFromEmail() {
        return selectedFromEmail;
    }

    public void setSelectedFromEmail(String selectedFromEmail) {
        this.selectedFromEmail = selectedFromEmail;
    }

    public String getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(String selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public String getSelectedMessagePriority() {
        return selectedMessagePriority;
    }

    public void setSelectedMessagePriority(String selectedMessagePriority) {
        this.selectedMessagePriority = selectedMessagePriority;
    }

    public String getSelectedCaseStatus() {
        return selectedCaseStatus;
    }

    public void setSelectedCaseStatus(String selectedCaseStatus) {
        this.selectedCaseStatus = selectedCaseStatus;
    }

    public List<SoEmailAccount> getSoEmailAccountList() {
        return soEmailAccountList;
    }

    public void setSoEmailAccountList(List<SoEmailAccount> soEmailAccountList) {
        this.soEmailAccountList = soEmailAccountList;
    }

    public String getSelectedAccountId() {
        return selectedAccountId;
    }

    public void setSelectedAccountId(String selectedAccountId) {
        this.selectedAccountId = selectedAccountId;
    }

    public SoEmailAccountDAO getSoEmailAccountDAO() {
        return soEmailAccountDAO;
    }

    public void setSoEmailAccountDAO(SoEmailAccountDAO soEmailAccountDAO) {
        this.soEmailAccountDAO = soEmailAccountDAO;
    }

    public String getSelectedChannelId() {
        return selectedChannelId;
    }

    public void setSelectedChannelId(String selectedChannelId) {
        this.selectedChannelId = selectedChannelId;
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

    public List<SoEmailMessage> getSoEmailMessageList() {
        return soEmailMessageList;
    }

    public void setSoEmailMessageList(List<SoEmailMessage> soEmailMessageList) {
        this.soEmailMessageList = soEmailMessageList;
    }

    public SoEmailMessageDAO getSoEmailMessageDAO() {
        return soEmailMessageDAO;
    }

    public void setSoEmailMessageDAO(SoEmailMessageDAO soEmailMessageDAO) {
        this.soEmailMessageDAO = soEmailMessageDAO;
    }

    public SoServiceDAO getSoServiceDAO() {
        return soServiceDAO;
    }

    public void setSoServiceDAO(SoServiceDAO soServiceDAO) {
        this.soServiceDAO = soServiceDAO;
    }

    public String getSelectedUsersId() {
        return selectedUsersId;
    }

    public void setSelectedUsersId(String selectedUsersId) {
        this.selectedUsersId = selectedUsersId;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public String getTargetUsers() {
        return targetUsers;
    }

    public void setTargetUsers(String targetUsers) {
        this.targetUsers = targetUsers;
    }

    public String getTargetUsersId() {
        return targetUsersId;
    }

    public void setTargetUsersId(String targetUsersId) {
        this.targetUsersId = targetUsersId;
    }

    public Integer getTargetUserGroupId() {
        return targetUserGroupId;
    }

    public void setTargetUserGroupId(Integer targetUserGroupId) {
        this.targetUserGroupId = targetUserGroupId;
    }

    public String getTargetReason() {
        return targetReason;
    }

    public void setTargetReason(String targetReason) {
        this.targetReason = targetReason;
    }

    public String getTargetRemark() {
        return targetRemark;
    }

    public void setTargetRemark(String targetRemark) {
        this.targetRemark = targetRemark;
    }

    public List<Users> getTargetUserList() {
        return targetUserList;
    }

    public void setTargetUserList(List<Users> targetUserList) {
        this.targetUserList = targetUserList;
    }

    public SoMessageDAO getSoMessageDAO() {
        return soMessageDAO;
    }

    public void setSoMessageDAO(SoMessageDAO soMessageDAO) {
        this.soMessageDAO = soMessageDAO;
    }

    public SoMsgUserAssigntimeDAO getSoMsgUserAssigntimeDAO() {
        return soMsgUserAssigntimeDAO;
    }

    public void setSoMsgUserAssigntimeDAO(SoMsgUserAssigntimeDAO soMsgUserAssigntimeDAO) {
        this.soMsgUserAssigntimeDAO = soMsgUserAssigntimeDAO;
    }

    public SoActivityLogDAO getSoActivityLogDAO() {
        return soActivityLogDAO;
    }

    public void setSoActivityLogDAO(SoActivityLogDAO soActivityLogDAO) {
        this.soActivityLogDAO = soActivityLogDAO;
    }

    public Map<String, Integer> getTargetUserGroupList() {
        return targetUserGroupList;
    }

    public void setTargetUserGroupList(Map<String, Integer> targetUserGroupList) {
        this.targetUserGroupList = targetUserGroupList;
    }
}
