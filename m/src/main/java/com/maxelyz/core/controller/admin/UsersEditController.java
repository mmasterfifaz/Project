package com.maxelyz.core.controller.admin;

import com.google.gson.Gson;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.front.customerHandling.RegistrationInfoValue;
import com.maxelyz.utils.FileUploadBean;
import com.maxelyz.utils.FormUtil;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import com.maxelyz.validator.ThaiIDValidator;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.io.File;
import java.util.*;

@ManagedBean
@ViewScoped
public class UsersEditController {

    public static String CASE_TYPE_ALL = "all";
    public static String CASE_TYPE_CUSTOM = "custom";

    private static Logger log = Logger.getLogger(UsersEditController.class);
    private static String SUCCESS = "users.xhtml";
    private static String FAILURE = "usersedit.xhtml";

    private Users users;
    private String mode;
    private String message;
    private String message2;

    private Map<String, Integer> userGroupList;
    private List<Integer> selectedGroupMember;
    private Map<String, Integer> groupMemberList = new LinkedHashMap<String, Integer>();
    private UserGroup userGroup;
    private String caseTypeRadioValue;
    private String id1;
    private String id2;
    private String id3;
    private String id4;
    private String id5;
    private String citizenId;
    private String msgIdno;
    private static File dataFile;
    private UsersStatus userStaus;
    private SelectItem[] usersStatusList;
    private Date effectiveFrom;
    private Date effectiveTo;
    private boolean changeUsersStatus = false;
    private List<UsersStatusHistory> userStausHistoryList;
    private List<ModuleAuditLog> auditLogs;
    private Boolean isAdmin;
    private Configuration configuration;
    private boolean userGroupRequired = false;
    
    private Integer userGroupId;

    //FileUpload
    private com.maxelyz.utils.FileUploadBean fileUploadBean = new com.maxelyz.utils.FileUploadBean(JSFUtil.getuploadPath(), "users");

    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;

    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;

    @ManagedProperty(value = "#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;

    @ManagedProperty(value = "#{usersStatusDAO}")
    private UsersStatusDAO usersStatusDAO;

    @ManagedProperty(value = "#{usersStatusHistoryDAO}")
    private UsersStatusHistoryDAO usersStatusHistoryDAO;

    @ManagedProperty(value = "#{mediaPlanDAO}")
    private MediaPlanDAO mediaPlanDAO;

    @ManagedProperty(value = "#{configurationDAO}")
    private ConfigurationDAO configurationDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:user:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        String selectedID = null;
        String userType = "user";
        try {
            selectedID = (String) JSFUtil.getRequestParameterMap("selectedID");
        } catch (NullPointerException e) {
        }
        try {
            userType = (String) JSFUtil.getRequestParameterMap("usertype");
        } catch (NullPointerException e) {
        }

        configuration = configurationDAO.findConfiguration("AuthenMethod");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            users = new Users();
            users.setStatus(Boolean.TRUE);
            users.setTelephonySso(Boolean.FALSE);
            userGroup = new UserGroup();
            users.setUserGroup(userGroup);
            users.setUserType(userType);
            users.setSurname("");
            userStaus = new UsersStatus();
            userStausHistoryList = new ArrayList<UsersStatusHistory>();
        } else {
            mode = "edit";
            UsersDAO dao = getUsersDAO();
            users = dao.findUsers(new Integer(selectedID));
            if (users != null) {
                if (users.getCitizenId() != null && !users.getCitizenId().isEmpty() && users.getCitizenId().length() == 13) {
                    id1 = users.getCitizenId().substring(0, 1);
                    id2 = users.getCitizenId().substring(1, 5);
                    id3 = users.getCitizenId().substring(5, 10);
                    id4 = users.getCitizenId().substring(10, 12);
                    id5 = users.getCitizenId().substring(12, 13);
                }
                if (users.getUsersStatusId() == null) {
                    userStaus = new UsersStatus();
                }
                if (users.getUsersStatusId() != null) {
                    userStaus = this.getUsersStatusDAO().findUsersStatus(users.getUsersStatusId());
                    List<UsersStatusHistory> hl = this.getUsersStatusHistoryDAO().findLastedStatus(users.getId());
                    UsersStatusHistory h = (hl == null || (hl.size() < 1)) ? new UsersStatusHistory() : hl.get(0);
                    effectiveFrom = h.getEffectiveFrom();
                    effectiveTo = h.getEffectiveTo();
                }
                userStausHistoryList = this.getUsersStatusHistoryDAO().findUsersStatusHistoryByUserId(users.getId());
                if (userStausHistoryList == null) {
                    userStausHistoryList = new ArrayList<UsersStatusHistory>();
                }
            }
        }
        userGroupId = users.getUserGroup().getId();
        isAdmin = JSFUtil.getUserSession().getUsers().getIsAdministrator();
        this.initGroupMember();
        message = "";
        message2 = "";

        if (JSFUtil.getUserSession().getUsers().getIsAdministrator()) {
            userGroupList = this.getUserGroupDAO().getUserGroupListByParentId(0);
        } else {
            userGroupList = this.getUserGroupDAO().getUserGroupListByParentId(JSFUtil.getUserSession().getUsers().getUserGroup().getId());
        }
    }

    public void initializeListener() {
        initialize();
    }

    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:user:add");
        } else {
            return SecurityUtil.isPermitted("admin:user:edit");
        }
    }

    public void initGroupMember() {
        if (users.getId() != null) {
            selectedGroupMember = new ArrayList<Integer>();
            List<UserMember> userMembers = (List) users.getUserMemberCollection();
            for (UserMember obj : userMembers) {
                selectedGroupMember.add(obj.getUserMemberPK().getMemberUserId());
            }
        } else {
            if (selectedGroupMember != null) {
                selectedGroupMember.clear();
            }
        }
        groupMemberList = usersDAO.getUsersList();
    }

    public String saveAction() {
        if (users.getUserType().equalsIgnoreCase("user") && !validateThaiIdNo(users)) {
            //msgIdno = " Invalid Citizen ID";
            return null;
        }
        message2 = "";
        if (checkUserByExternalId(users)) {
            if (checkUser(users)) {
                UsersDAO dao = getUsersDAO();
                UsersStatusHistory uHistory = new UsersStatusHistory();
                ModuleAuditLog log = new ModuleAuditLog();
                try {

                    if (users.getTelephonySso()) {
                        users.setTelephonyLoginName(users.getLdapLogin());
                    }

                    if (users.getIsSystem()) {
                        users.setStatus(false);
                        users.setEnable(false);
                    } else {
                        users.setEnable(true);
                    }
                    if (users.getExternalUserId().equals("")) {
                        users.setExternalUserId(null);
                    } else {
                        users.setExternalUserId(users.getExternalUserId().trim());
                    }
                    uHistory.setActionBy(JSFUtil.getUserSession().getUsers().getName());
                    uHistory.setActionDate(new Date());
                    uHistory.setEffectiveFrom(effectiveFrom);
                    uHistory.setEffectiveTo(effectiveTo);
                    log.setActionBy(JSFUtil.getUserSession().getUsers().getName());
                    log.setActionDate(new Date());
                    log.setModuleName("users");
                    if (getMode().equals("add")) {
                        users.setId(null);//fix some bug
                        users.setCountChangePassword(0);
                        users.setForceChangePassword(0);
                        if (this.fileUploadBean.getFiles().size() > 0) {
                            users.setImage(this.fileUploadBean.getFiles().get(0).getName());
                        } else {
                            users.setImage(null);
                        }
                        dao.create(users);
                        if (selectedGroupMember != null) {
                            List<UserMember> userMemberList = new ArrayList<UserMember>();
                            for (Integer value : selectedGroupMember) {
                                UserMemberPK userMemberPK = new UserMemberPK(value, users.getId());
                                UserMember userMember = new UserMember(userMemberPK);
                                userMemberList.add(userMember);
                            }
                            users.setUserMemberCollection(userMemberList);
                        }
                        if (this.fileUploadBean.getFiles().size() > 0) {
                            //if ((this.getDeleteThumbnailString() == null) || (this.getDeleteThumbnailString().length == 0)) {
                            this.fileUploadBean.createDirName(users.getId().toString());
                            this.fileUploadBean.moveFile(this.fileUploadBean.getFiles().get(0), this.fileUploadBean.getFolderPath());
                        }
                        dao.edit(users);
                        if (changeUsersStatus && users.getUsersStatusId() != null) {
                            uHistory.setUsers(users);
                            uHistory.setUsersStatusName(userStaus.getName());
                            this.getUsersStatusHistoryDAO().create(uHistory);
                            if (userStaus.getUsersEnableFlag()) {
                                users.setHiredDate(effectiveFrom);
                                users.setResignedDate(null);
                            }
                            if (!userStaus.getUsersEnableFlag()) {
                                users.setResignedDate(effectiveFrom);
                            }
                            dao.edit(users);
                        }
                        log.setRefNo(users.getId());
                        log.setActionType("Add New record");
                        Gson gson = new Gson();
                        UsersValue uv = userToUserValue(users);
                        String json = gson.toJson(uv);
                        log.setActionDetail(json);
                        this.getMediaPlanDAO().create(log);
                    } else {
                        if (selectedGroupMember != null) {
                            List<UserMember> userMemberList = new ArrayList<UserMember>();
                            for (Integer value : selectedGroupMember) {
                                UserMemberPK userMemberPK = new UserMemberPK(value, users.getId());
                                UserMember userMember = new UserMember(userMemberPK);
                                userMemberList.add(userMember);
                            }
                            dao.deleteUserMember(users.getId());
                            users.setUserMemberCollection(userMemberList);
                        }
                        if (this.fileUploadBean.getFiles().size() > 0) {
                            File f = new File(this.fileUploadBean.getFolderPath() + "/" + users.getId());
                            if (f.exists()) {
                                f.deleteOnExit();
                            }
                            this.fileUploadBean.createDirName(users.getId().toString());
                            this.fileUploadBean.moveFile(this.fileUploadBean.getFiles().get(0), this.fileUploadBean.getFolderPath());
                            users.setImage(this.fileUploadBean.getFiles().get(0).getName());
                        }
                        dao.edit(users);
                        if (changeUsersStatus && users.getUsersStatusId() != null) {
                            uHistory.setUsers(users);
                            uHistory.setUsersStatusName(userStaus.getName());
                            this.getUsersStatusHistoryDAO().create(uHistory);
                            if (userStaus.getUsersEnableFlag()) {
                                users.setHiredDate(effectiveFrom);
                                users.setResignedDate(null);
                            }
                            if (!userStaus.getUsersEnableFlag()) {
                                users.setResignedDate(effectiveFrom);
                            }
                            dao.edit(users);
                        }
                        log.setRefNo(users.getId());
                        log.setActionType("Update record");
                        Gson gson = new Gson();
                        UsersValue uv = userToUserValue(users);
                        String json = gson.toJson(uv);
                        log.setActionDetail(json);
                        this.getMediaPlanDAO().create(log);
                    }
                } catch (Exception e) {
                    this.log.error(e);
                    e.printStackTrace();
                    return FAILURE;
                }
                this.getUsersController().searchActionListener(null);
                return SUCCESS;
            } else {
                message = "Login name is already taken";
                return null;
            }
        } else {
            message2 = "External User Id is alerady taken";
            return null;
            //return FAILURE;
        }

    }

    public void usersGroupListener(ValueChangeEvent event ){
        userGroupId = (Integer) event.getNewValue();
        UserGroup userGroup = userGroupDAO.findUserGroup(userGroupId);
        String[] roles = userGroup.getRole().split(",");
        for(String role : roles){
            if(role.equals("Agent")){
                userGroupRequired = true;
                break;
            }else {
                userGroupRequired = false;
            }
        }
    }

    private UsersValue userToUserValue(Users user) {
        UsersValue uv = new UsersValue();
        if (user != null) {
            uv.setAgentCode(user.getAgentCode());
            uv.setCitizenId(user.getCitizenId());
            uv.setDefaultPage(user.getDefaultPage());
            uv.setDob(user.getDateOfBirth());
            uv.setEffectiveFrom(effectiveFrom);
            uv.setEffectiveTo(effectiveTo);
            uv.setEmail(user.getEmail());
            uv.setEnable(user.getEnable());
            uv.setIsAdmin(user.getIsAdministrator());
            uv.setLdapLogin(user.getLdapLogin());
            uv.setLicenseNo(user.getLicenseNo());
            uv.setLoginName(user.getLoginName());
            uv.setMobile(user.getMobile());
            uv.setName(user.getName());
            uv.setNickName(user.getNickname());
            uv.setStationId(user.getStationId());
            uv.setSureName(user.getSurname());
            uv.setTelephonyLoginName(user.getTelephonyLoginName());
            uv.setUserGroup(user.getUserGroup().getName());
            if (user.getUsersStatusId() != null && userStaus != null) {
                uv.setUserStatus(userStaus.getName());
            }
        }
        return uv;
    }

    public UsersController getUsersController() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ValueExpression vex = app.getExpressionFactory().
                createValueExpression(context.getELContext(), "#{usersController}", UsersController.class);
        UsersController g = (UsersController) vex.getValue(context.getELContext());
        return g;
    }

    public Boolean checkUser(Users user) {
        String loginName = user.getLoginName();
        Integer id = 0;
        if (user.getId() != null) {
            id = user.getId();
        }
        UsersDAO dao = getUsersDAO();
        List<Users> users = dao.checkUsersByLoginName(loginName, id);
        if (users.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkUserByExternalId(Users user) {
        String externalUserId = user.getExternalUserId();
        UsersDAO dao = getUsersDAO();
        int id =0;
        if(user.getId()!=null){
        id = user.getId();
        }
        List<Users> users = dao.checkUsersByExternalId(externalUserId,id);
        if (users.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void checkIdNo(Users user) {
        validateThaiIdNo(user);
    }

    private boolean validateThaiIdNo(Users user) {
        msgIdno = "";
        this.users.setCitizenId("");
        RegistrationInfoValue registration = new RegistrationInfoValue();
        registration.setId1(id1);
        registration.setId2(id2);
        registration.setId3(id3);
        registration.setId4(id4);
        registration.setId5(id5);
        String idno = FormUtil.buildThaiIdNo(registration);
        boolean valid = ThaiIDValidator.checkPID(idno);
        if (valid) {
            this.users.setCitizenId(idno);
            Integer id = 0;
            if (user.getId() != null) {
                id = user.getId();
            }

            if(!(userGroupId == 1 || users.getIsSystem() || users.getIsAdministrator()))
            {
                UsersDAO dao = getUsersDAO();
                long count = dao.checkCitizenID(idno, id);
                if (count != 0) {
                    valid = false;
                    msgIdno = "Citizen ID is already taken.";
                }
            }
        } else {
//          registration.setMsgIdno(" Invalid Citizen ID");
            msgIdno = "Invalid Citizen ID";
        }
        return valid;
    }

    public void deletePicThumbnailListener(ActionEvent event) {
        users.setImage(null);
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void userStatusChangeListener(ValueChangeEvent event) {
        if (event.getNewValue() != null && !event.getNewValue().toString().isEmpty()) {
            Integer id = (Integer) event.getNewValue();
            userStaus = new UsersStatus();
            userStaus = this.getUsersStatusDAO().findUsersStatus(id);
            users.setUsersStatusId(userStaus.getId());
            effectiveFrom = null;
            effectiveTo = null;
            changeUsersStatus = true;
        }
    }

    public void auditLogListener(ActionEvent event) {
        if (JSFUtil.getRequestParameterMap("userId") != null && !JSFUtil.getRequestParameterMap("userId").isEmpty()) {
            Integer mpId = Integer.parseInt(JSFUtil.getRequestParameterMap("userId"));
            auditLogs = this.getMediaPlanDAO().findModuleAuditLogByRefID("users", mpId);
        }
    }

    public void userStatusDateChangeListener() {
        changeUsersStatus = true;
    }

    public void caseTypeRadioListener(ValueChangeEvent event) {
        caseTypeRadioValue = (String) event.getNewValue();
    }

    public String backAction() {
        return SUCCESS;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users usr) {
        this.users = usr;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getcaseTypeRadioValue() {
        return caseTypeRadioValue;
    }

    public void setCaseTypeRadioValue(String caseTypeRadioValue) {
        this.caseTypeRadioValue = caseTypeRadioValue;
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

    public CaseTypeDAO getCaseTypeDAO() {
        return caseTypeDAO;
    }

    public void setCaseTypeDAO(CaseTypeDAO caseTypeDAO) {
        this.caseTypeDAO = caseTypeDAO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Integer> getGroupMemberList() {
        return groupMemberList;
    }

    public void setGroupMemberList(Map<String, Integer> groupMemberList) {
        this.groupMemberList = groupMemberList;
    }

    public List<Integer> getSelectedGroupMember() {
        return selectedGroupMember;
    }

    public void setSelectedGroupMember(List<Integer> selectedGroupMember) {
        this.selectedGroupMember = selectedGroupMember;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getId3() {
        return id3;
    }

    public void setId3(String id3) {
        this.id3 = id3;
    }

    public String getId4() {
        return id4;
    }

    public void setId4(String id4) {
        this.id4 = id4;
    }

    public String getId5() {
        return id5;
    }

    public void setId5(String id5) {
        this.id5 = id5;
    }

    public String getMsgIdno() {
        return msgIdno;
    }

    public void setMsgIdno(String msgIdno) {
        this.msgIdno = msgIdno;
    }

    public FileUploadBean getFileUploadBean() {
        return fileUploadBean;
    }

    public void setFileUploadBean(FileUploadBean fileUploadBean) {
        this.fileUploadBean = fileUploadBean;
    }

    public UsersStatusDAO getUsersStatusDAO() {
        return usersStatusDAO;
    }

    public void setUsersStatusDAO(UsersStatusDAO usersStatusDAO) {
        this.usersStatusDAO = usersStatusDAO;
    }

    public SelectItem[] getUsersStatusList() {
        UserGroupDAO dao = getUserGroupDAO();
        List<UsersStatus> uList = this.getUsersStatusDAO().findUsersStatusEntities();
        if (uList == null) {
            uList = new ArrayList<UsersStatus>();
        }
        usersStatusList = new SelectItem[uList.size() + 1];
        int pos = 0;
        usersStatusList[pos++] = new SelectItem(null, "Select User Status");
        for (UsersStatus ug : uList) {
            usersStatusList[pos++] = new SelectItem(ug.getId(), ug.getName());
        }
        return usersStatusList;
    }

    public void setUsersStatusList(SelectItem[] usersStatusList) {
        this.usersStatusList = usersStatusList;
    }

    public Date getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(Date effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public Date getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(Date effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public List<UsersStatusHistory> getUserStausHistoryList() {
        return userStausHistoryList;
    }

    public void setUserStausHistoryList(List<UsersStatusHistory> userStausHistoryList) {
        this.userStausHistoryList = userStausHistoryList;
    }

    public UsersStatusHistoryDAO getUsersStatusHistoryDAO() {
        return usersStatusHistoryDAO;
    }

    public void setUsersStatusHistoryDAO(UsersStatusHistoryDAO usersStatusHistoryDAO) {
        this.usersStatusHistoryDAO = usersStatusHistoryDAO;
    }

    public MediaPlanDAO getMediaPlanDAO() {
        return mediaPlanDAO;
    }

    public void setMediaPlanDAO(MediaPlanDAO mediaPlanDAO) {
        this.mediaPlanDAO = mediaPlanDAO;
    }

    public List<ModuleAuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(List<ModuleAuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }

    public Map<String, Integer> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(Map<String, Integer> userGroupList) {
        this.userGroupList = userGroupList;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getMessage2() {
        return message2;
    }

    public void setMessage2(String message2) {
        this.message2 = message2;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public boolean isUserGroupRequired() {
        return userGroupRequired;
    }

    public void setUserGroupRequired(boolean userGroupRequired) {
        this.userGroupRequired = userGroupRequired;
    }

    public ConfigurationDAO getConfigurationDAO() {
        return configurationDAO;
    }

    public void setConfigurationDAO(ConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }
    
    
}
