/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.search;

//import com.maxelyz.core.constant.CrmConstant;
//import com.maxelyz.core.controller.UserSession;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.dao.front.search.SearchMessageDAO;
import com.maxelyz.core.model.entity.UserGroup;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.social.model.dao.SoChannelDAO;
import com.maxelyz.social.model.dao.SoAccountDAO;
import com.maxelyz.social.model.dao.SoServiceDAO;
import com.maxelyz.social.model.entity.SoAccount;
import com.maxelyz.social.model.entity.SoChannel;
import com.maxelyz.social.model.entity.SoService;
import com.maxelyz.core.model.value.front.search.SearchMessageValue;
import com.maxelyz.social.model.dao.SoMessageDAO;
import com.maxelyz.social.model.entity.SoMessage;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrator
 */
@ManagedBean(name="searchMessageController")
@SessionScoped
public class SearchMessageController extends BaseController implements Serializable{
    private static String SEARCHRESULT = "searchMessage.xhtml";
    private static String DETAIL = "../social/scldetail.xhtml";
    
    private List<SearchMessageValue> searchMessageList;
    private List<SoAccount> accountList = new ArrayList<SoAccount>();
    private List<SoChannel> channelList = new ArrayList<SoChannel>();
    private List<SoService> serviceList = new ArrayList<SoService>();
    private List<UserGroup> userGroupList;
    private List<Users> userList;
    
    //search message post feed
    private Integer sourceTypeID = 0;
    private String emailFrom = "";
    private String emailTo = "";
    private Integer accountID = 0;
    private String messageID = "";
    private Date contactDateFrom = null;
    private Date contactDateTo = null;
    private String refMessageID = "";
    private String status = "";
    private String subject = "";
    private String priority = "";
    private String userName = "";
    private String userGroup = "";
    private String content = "";
    private Integer serviceID = 0;
    private Integer userGroupId = 0;
    private Integer userId = 0;

    //search ref to msg
    private String customerName = "";
    private String memberID = "";
    private String idNo = "";
    private String address = "";
    private String phone = "";
    private String email = "";
    private String ticketId = "";

    // Paging properties
    private Integer currentPage;
    private Integer totalPages;
    private Integer totalRows;
    private Integer scrollerPage;

    @ManagedProperty(value="#{searchMessageDAO}")
    private SearchMessageDAO searchMessageDAO;
    @ManagedProperty(value="#{soAccountDAO}")
    private SoAccountDAO soAccountDAO;
    @ManagedProperty(value="#{soChannelDAO}")
    private SoChannelDAO soChannelDAO;         
    @ManagedProperty(value="#{soServiceDAO}")
    private SoServiceDAO soServiceDAO;  
    @ManagedProperty(value="#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value="#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{soMessageDAO}")
    private SoMessageDAO soMessageDAO;
    
    /** Creates a new instance of SearchCustomerController */
    public SearchMessageController() {
    }

    @PostConstruct
    public void initialize(){
        if (!SecurityUtil.isPermitted("searchmessage:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        userGroupList = userGroupDAO.findUserGroupEntities();
        userList = usersDAO.findUsersEntities();
//        if(userGroupId != null && userGroupId != 0){
//            userList = usersDAO.findUsersEntitiesByUserGroup(userGroupId.intValue());
//        }
        scrollerPage = 1;
    }
    
    public void checkStatusComposeListener(){
        Integer id = Integer.parseInt(JSFUtil.getRequestParameterMap("soMessageId"));
        SoMessage sm = soMessageDAO.findSoMessage(id);
        if((status.equals("PS") && !sm.getCase_status().equals("DF")) || (status.equals("WF") && !sm.getCase_status().equals("WF"))){ 
            searchMessageAction();
            JSFUtil.redirect(JSFUtil.getServletContext().getContextPath() + "/front/search/searchMessage.jsf?faces-redirect=true");
        }
    }
         
    public void userGroupChangeListener(ValueChangeEvent event) {
        userGroupId = (Integer) event.getNewValue();
        if(userGroupId != 0){
            userList = usersDAO.findUsersEntitiesByUserGroup(userGroupId.intValue());
        }else{
            userList = null;
        }
    }
    public String searchMessageAction(){
        if (!SecurityUtil.isPermitted("searchmessage:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
//        if (scrollerPage == null || scrollerPage == 0) {
            scrollerPage = 1;
//        }
        int maxResult = JSFUtil.getApplication().getMaxrows();
        int firstResult = ((scrollerPage - 1) * maxResult);
        
        searchMessageList = getSearchMessageDAO().searchMessage(sourceTypeID, emailFrom, emailTo, accountID,messageID.trim().length() > 0?Integer.valueOf(messageID):0, 
                            contactDateFrom, contactDateTo,refMessageID.trim().length() > 0?Integer.valueOf(refMessageID):0, status, subject, priority, userId, userGroupId, content, serviceID, 0, 1000, ticketId);

        return SEARCHRESULT;
    }

    public void clearValue() {
        sourceTypeID = 0;
        emailFrom = "";
        emailTo = "";
        accountID = 0;
        messageID = "";
        contactDateFrom = null;
        contactDateTo = null;
        refMessageID = "";
        status = "";
        userGroupId = 0;
        userId = 0;
        subject = "";
        priority = "";
        userName = "";
        userGroup = "";
        content = "";
        serviceID = 0;
        customerName = "";
        memberID = "";
        idNo = "";
        address = "";
        phone = "";
        email = "";
    }

    // Search Info
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

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public Date getContactDateFrom() {
        return contactDateFrom;
    }

    public void setContactDateFrom(Date contactDateFrom) {
        this.contactDateFrom = contactDateFrom;
    }

    public Date getContactDateTo() {
        return contactDateTo;
    }

    public void setContactDateTo(Date contactDateTo) {
        this.contactDateTo = contactDateTo;
    }

    public String getRefMessageID() {
        return refMessageID;
    }

    public void setRefMessageID(String refMessageID) {
        this.refMessageID = refMessageID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSourceTypeID() {
        return sourceTypeID;
    }

    public void setSourceTypeID(Integer sourceTypeID) {
        this.sourceTypeID = sourceTypeID;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Page Navigator
    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    
    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public Integer getScrollerPage() {
        return scrollerPage;
    }

    public void setScrollerPage(Integer scrollerPage) {
        this.scrollerPage = scrollerPage;
    }
    
    // Search Result
    public List<SearchMessageValue> getSearchMessageList() {
        return searchMessageList;
    }

    public void setSearchMessageList(List<SearchMessageValue> searchMessageList) {
        this.searchMessageList = searchMessageList;
    }
    
    // Get/Set DAO
    public SearchMessageDAO getSearchMessageDAO() {
        return searchMessageDAO;
    }

    public void setSearchMessageDAO(SearchMessageDAO searchMessageDAO) {
        this.searchMessageDAO = searchMessageDAO;
    }
    
    public SoAccountDAO getSoAccountDAO() {
        return soAccountDAO;
    }

    public void setSoAccountDAO(SoAccountDAO soAccountDAO) {
        this.soAccountDAO = soAccountDAO;
    }

    public SoChannelDAO getSoChannelDAO() {
        return soChannelDAO;
    }

    public void setSoChannelDAO(SoChannelDAO soChannelDAO) {
        this.soChannelDAO = soChannelDAO;
    }

    public SoServiceDAO getSoServiceDAO() {
        return soServiceDAO;
    }

    public void setSoServiceDAO(SoServiceDAO soServiceDAO) {
        this.soServiceDAO = soServiceDAO;
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
    
    // Get/Set List
    public Map<String, Integer> getAccountList() {
        return getSoAccountDAO().getSoAccountList();
    }

    public void setAccountList(List<SoAccount> accountList) {
        this.accountList = accountList;
    }

    public Map<String, Integer> getChannelList() {
        return getSoChannelDAO().getSoChannelList();
    }

    public void setChannelList(List<SoChannel> channelList) {
        this.channelList = channelList;
    }

    public Map<String, Integer> getServiceList() {
        return getSoServiceDAO().getSoServiceList();
    }

    public void setServiceList(List<SoService> serviceList) {
        this.serviceList = serviceList;
    }

    public List<UserGroup> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(List<UserGroup> userGroupList) {
        this.userGroupList = userGroupList;
    }

    public List<Users> getUserList() {
        return userList;
    }

    public void setUserList(List<Users> userList) {
        this.userList = userList;
    }

    public SoMessageDAO getSoMessageDAO() {
        return soMessageDAO;
    }

    public void setSoMessageDAO(SoMessageDAO soMessageDAO) {
        this.soMessageDAO = soMessageDAO;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
    
}
