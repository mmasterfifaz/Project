package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.Channel;
import com.maxelyz.core.model.entity.ContactResult;
import com.maxelyz.core.model.entity.UserGroup;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.model.value.admin.ContactRecordValue;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import java.util.Date;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.*;
//import net.sf.cglib.core.Local;

@ManagedBean
@ViewScoped
public class ContactRecordController implements Serializable{
    private static Logger log = Logger.getLogger(ContactRecordController.class);

    private static String REFRESH = "contactrecord.xhtml?faces-redirect=true";
    private static String EDIT = "contactrecordedit.xhtml";
    private static String SELF = "contactrecord.xhtml";

    private Date fromDate = new Date();
    private Date toDate = new Date();
    private String strFromDate;
    private String strToDate;
    private List<ContactRecordValue> list;
    private List<UserGroup> userGroupList = new ArrayList<UserGroup>();
    private Integer userGroupId;
    private List<Users> userList = new ArrayList<Users>();
    private Integer userId;
    private String textSearch;
    private String contactToSearch;
    private Integer scrollerPage;
    //private List<Channel> channelList = new ArrayList<Channel>();
    private Integer channelId;
//    private List<ContactResult> contactResultList;
    private List<ContactResult> mainContactResultList;
    private String contactStatus;
    private Integer contactResultId;
//    private Integer mainContactResultId;
//    private String mainContactResultCode;
    private List<String[]> contactStatusList;
    

    @ManagedProperty(value="#{contactHistoryDAO}")
    private ContactHistoryDAO contactHistoryDAO;
    @ManagedProperty(value="#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value="#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{channelDAO}")
    private ChannelDAO channelDAO;
    @ManagedProperty(value = "#{contactResultDAO}")
    private ContactResultDAO contactResultDAO;
    /*@ManagedProperty(value="#{resultDAO}")
    private ResultDAO resultDAO;*/

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:contactrecord:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        //initGetParam();
        userGroupList = userGroupDAO.findUserGroupEntities();
        if(userGroupId != null && userGroupId != 0){
            userList = usersDAO.findUsersEntitiesByUserGroup(userGroupId.intValue());
        }
        contactStatus = "all";
        search();
        contactStatusList = this.initContactStatusList();
        if(JSFUtil.getUserSession().getCustomerDetail() != null) {
            JSFUtil.getUserSession().setCustomerDetail(null);
        }
    }
    
    public String editAction(){
        return EDIT;
    }

    public void initClear(ActionEvent event) {
        fromDate = new Date();
        toDate = new Date();
        userGroupId = 0;
        userId = 0;
        textSearch = "";
        contactToSearch = "";
        userList = new ArrayList<Users>();
        list = null;
        scrollerPage = null;
    }

    public String selfAction() {
        //initGetParam();
        return SELF;
    }

    private void initGetParam(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
        try{
            if(JSFUtil.getRequestParameterMap("fromDate") != null){
                fromDate = sdf.parse(JSFUtil.getRequestParameterMap("fromDate"));
                strFromDate = sdf.format(fromDate);
            }
            if(JSFUtil.getRequestParameterMap("toDate") != null){
                toDate = sdf.parse(JSFUtil.getRequestParameterMap("toDate"));
                strToDate = sdf.format(toDate);
            }
            if(JSFUtil.getRequestParameterMap("userGroupId") != null){
                if(!((String)JSFUtil.getRequestParameterMap("userGroupId")).equals("")){
                    userGroupId = Integer.parseInt(JSFUtil.getRequestParameterMap("userGroupId"));
                }
            }
            if(JSFUtil.getRequestParameterMap("userId") != null){
                if(!((String)JSFUtil.getRequestParameterMap("userId")).equals("")){
                    userId = Integer.parseInt(JSFUtil.getRequestParameterMap("userId"));
                }
            }
            if(JSFUtil.getRequestParameterMap("textSearch") != null){
                textSearch = String.valueOf(JSFUtil.getRequestParameterMap("textSearch"));
            }
            if(JSFUtil.getRequestParameterMap("contactToSearch") != null){
                contactToSearch = String.valueOf(JSFUtil.getRequestParameterMap("contactToSearch"));
            }
        }catch (ParseException pe) {
            //log.error(pe.toString());
        }
    }

    public void searchActionListener(ActionEvent event){
        search();
    }

    private void search(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
        if(fromDate != null) strFromDate = sdf.format(fromDate);
        if(toDate != null) strToDate = sdf.format(toDate);
        if(userId == null) userId = 0;
        if(userGroupId == null) userGroupId = 0;
        if(textSearch == null) textSearch = "";
        if(contactToSearch == null) contactToSearch = "";
        if(channelId == null) channelId = 0;
        if(contactStatus == null) contactStatus = "";
        if(contactResultId == null) contactResultId = 0;
        
        
        String fName = "";
        String lName = "";
        if(!textSearch.equals("")){
            textSearch = textSearch.trim();
            if(textSearch.indexOf(" ") != -1){
                fName = textSearch.substring(0, textSearch.indexOf(" "));
                lName = textSearch.substring(textSearch.indexOf(" ") + 1, textSearch.length());
            }else{
                fName = textSearch;
            }
        }
        list = contactHistoryDAO.findContactHistoryValue(fromDate, toDate, userGroupId.intValue(), userId.intValue(), fName, lName, null, null, contactToSearch, channelId, contactStatus, contactResultId );
    }

    public List<String[]> initContactStatusList() {
        List<String[]> list = new ArrayList<String[]>();

        String[] e = new String[2];
        e[0] = "ALL";
        e[1] = "all";
        list.add(e);

        String[] a = new String[2];
        a[0] = "DMC";
        a[1] = "dmc";
        list.add(a);

        String[] b = new String[2];
        b[0] = "Not DMC";
        b[1] = "contactable";
        list.add(b);

        String[] c = new String[2];
        c[0] = "Unreachable";
        c[1] = "uncontactable";
        list.add(c);

        String[] d = new String[2];
        d[0] = "CS";
        d[1] = "cs";
        list.add(d);


        return list;
    }
    
    public void contactStatusListener(){
        contactResultId = null;
//        mainContactResultId = null;
//        mainContactResultCode = null;
               
        contactStatusList = this.initContactStatusList();
        if(contactStatus.equals("dmc")){
            mainContactResultList = contactResultDAO.findContactRecordByContactStatus("dmc");
        }else if(contactStatus.equals("contactable")){
            mainContactResultList = contactResultDAO.findContactRecordByContactStatus("contactable");
        }else if(contactStatus.equals("cs")){
            mainContactResultList = contactResultDAO.findContactRecordByContactStatus("inbound");
        }else if(contactStatus.equals("ts")){
            mainContactResultList = contactResultDAO.findContactRecordByContactStatus("inboundtelesale");
        }else if(contactStatus.equals("uncontactable")){
            mainContactResultList = contactResultDAO.findContactRecordByContactStatus("uncontactable");
        }else if(contactStatus.equals("all")){
            mainContactResultList = null;
        }
    }
    
    public void changeContactResultListener(ValueChangeEvent event){
        if(event != null && event.getNewValue() != null){
            contactResultId = (Integer) event.getNewValue();
//            mainContactResultId = contactResultId;
            ContactResult contactResult = contactResultDAO.findContactResult(contactResultId);
//            mainContactResultCode = contactResult.getCode();
//            JSFUtil.getUserSession().setContactCaseId(mainContactResultId);
//            JSFUtil.getUserSession().setContactResultCode(mainContactResultCode);
        }
    }
        
    public void userGroupChangeListener(ValueChangeEvent event) {
        userList.clear();
        userGroupId = (Integer) event.getNewValue();
        userList = usersDAO.findUsersEntitiesByUserGroup(userGroupId.intValue());
    }
    
    

    public List<ContactRecordValue> getList() {
        return list;
    }

    public void setList(List<ContactRecordValue> list) {
        this.list = list;
    }

    public ContactHistoryDAO getContactHistoryDAO() {
        return contactHistoryDAO;
    }

    public void setContactHistoryDAO(ContactHistoryDAO contactHistoryDAO) {
        this.contactHistoryDAO = contactHistoryDAO;
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

    public List<Users> getUserList() {
        return userList;
    }

    public void setUserList(List<Users> userList) {
        this.userList = userList;
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

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<UserGroup> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(List<UserGroup> userGroupList) {
        this.userGroupList = userGroupList;
    }

    public String getTextSearch() {
        return textSearch;
    }

    public void setTextSearch(String textSearch) {
        this.textSearch = textSearch;
    }

    public String getContactToSearch() {
        return contactToSearch;
    }

    public void setContactToSearch(String contactToSearch) {
        this.contactToSearch = contactToSearch;
    }

    public String getStrFromDate() {
        return strFromDate;
    }

    public void setStrFromDate(String strFromDate) {
        this.strFromDate = strFromDate;
    }

    public String getStrToDate() {
        return strToDate;
    }

    public void setStrToDate(String strToDate) {
        this.strToDate = strToDate;
    }

    public Integer getScrollerPage() {
        return scrollerPage;
    }

    public void setScrollerPage(Integer scrollerPage) {
        this.scrollerPage = scrollerPage;
    }
    /*
    public List getChannelList() {
        return getChannelDAO().findChannelEntities();
    }

    private void setChannelList(List channelList) {
        this.channelList = channelList;
    }
    */
    
    public Map<String, Integer> getChannelList() {    
        return getChannelDAO().getChannelList();
    }
    
    public ChannelDAO getChannelDAO() {
        return channelDAO;
    }

    public void setChannelDAO(ChannelDAO channelDAO) {
        this.channelDAO = channelDAO;
    }
    
    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }
    
    public List<String[]> getContactStatusList() {
        return this.initContactStatusList();
    }

    public void setContactStatusList(List<String[]> contactStatusList) {
        this.contactStatusList = contactStatusList;
    }
    
     public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }

    /*
    public List getResultList() {
    return getResultDAO().findResultEntities();
    }
    private void setResultList(List resultList) {
    this.resultList = resultList;
    }
     */
    public ContactResultDAO getContactResultDAO() {
        return contactResultDAO;
    }

    public void setContactResultDAO(ContactResultDAO contactResultDAO) {
        this.contactResultDAO = contactResultDAO;
    }

    public Integer getContactResultId() {
        return contactResultId;
    }

    public void setContactResultId(Integer contactResultId) {
        this.contactResultId = contactResultId;
    }

    public List<ContactResult> getMainContactResultList() {
        return mainContactResultList;
    }

    public void setMainContactResultList(List<ContactResult> mainContactResultList) {
        this.mainContactResultList = mainContactResultList;
    }
}
