package com.maxelyz.core.controller.admin;

import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.AssignmentDAO;
import com.maxelyz.core.model.dao.AssignmentDetailDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.CustomerDAO;
import com.maxelyz.core.model.dao.MarketingDAO;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.dao.AssignmentTransferDAO;
import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.Campaign;
import com.maxelyz.core.model.entity.Marketing;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.model.entity.AssignmentTransfer;
import com.maxelyz.core.model.value.admin.CriteriaListValue;
import com.maxelyz.core.model.value.admin.UserAssignmentValue;
import com.maxelyz.utils.JSFUtil;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;

/**
 *
 * @author vee
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class CampaignTransferController extends BaseController {

    private static Logger log = Logger.getLogger(CampaignTransferController.class);

    private List<Integer> selectedCustomer;
    private List<Integer> selectedAssignment;
    private List<Users> selectedUser;
    private List<UserAssignmentValue> selectedAssignmentUser;
    private List<UserAssignmentValue> assignmentUsers;
    private List<AssignmentDetail> customerList;
    private static String REDIRECT_PAGE = "campaign.jsf";
    private static String SUCCESS = "campaignsummary.xhtml";
    private static String FAILURE = "campaigntransfer.xhtml";
    private Campaign campaign;
    private AssignmentTransfer assignmentTransfer;
    private int marketingId;
    private int fromUserGroupId;
    private int toUserGroupId;
    private int fromUserId;
    private int toUserId;
    private boolean contactStatusAssigned;
    private boolean contactStatusViewed;
    private boolean contactStatusOpened;
    private boolean contactStatusFollowup;
    private int remainRecord;
    private int transferRecord;
    private int simulateRecord;
    private String messageDup;
    private String messageExceed;
    
    // SEARCH CRITERIA
    private String customerName;
    private String customerSurname;
    private String gender;
    private int fromage, toage;
    private String homePhonePrefix;
    private String officePhonePrefix;
    private String mobilePhonePrefix;
    private boolean selectName;
    private boolean selectSurname;
    private boolean selectGender;
    private boolean selectAge;
    private boolean selectHomephone;
    private boolean selectOfficePhone;
    private boolean selectMobilePhone;
    
    private List<CriteriaListValue> selectedAdvanceCriteria;

    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{assignmentDAO}")
    private AssignmentDAO assignmentDAO;
    @ManagedProperty(value = "#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    @ManagedProperty(value = "#{assignmentTransferDAO}")
    private AssignmentTransferDAO assignmentTransferDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:campaignassignmenttransfer:add")) {
            SecurityUtil.redirectUnauthorize();
        }

        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null) {
            JSFUtil.redirect(REDIRECT_PAGE);
        } else {
            CampaignDAO dao = getCampaignDAO();
            campaign = dao.findCampaign(new Integer(selectedID));

            getUserGroupList();
            selectedAssignmentUser = new ArrayList<UserAssignmentValue>();
            customerList = new ArrayList<AssignmentDetail>();
            selectedAdvanceCriteria = new ArrayList<CriteriaListValue>();
        }
    }

    public void initializeListener(ActionEvent event) {
        if (selectedCustomer != null) {
            selectedCustomer.clear();
        }
        if (selectedAssignment != null) {
            selectedAssignment.clear();
        }
        if (customerList != null) {
            customerList.clear();
        }
        if(selectedAdvanceCriteria != null) {
            selectedAdvanceCriteria.clear();
        }
        marketingId = 0;
        contactStatusAssigned = false;
        contactStatusViewed = false;
        contactStatusOpened = false;
        contactStatusFollowup = false;
        fromage = 0;
        toage = 0;
        customerName = "";
        customerSurname = "";
        gender = "M";
        homePhonePrefix = "notspecific";
        officePhonePrefix = "notspecific";
        mobilePhonePrefix = "notspecific";
        selectAge = false;
        selectName = false;
        selectSurname = false;
        selectGender = false;
        selectHomephone = false;
        selectOfficePhone = false;
        selectMobilePhone = false;        
        fromUserGroupId = 0;
        toUserGroupId = 0;
        fromUserId = 0;
        toUserId = 0;
        messageDup = "";
        messageExceed = "";
        remainRecord = 0;
        transferRecord = 0;
        simulateRecord = 0;
        initialize();
    }

    public void initAdvanceCriteria() {   
        // CLEAR SELECT USER LIST
        if (!selectedAssignmentUser.isEmpty()) {
            selectedAssignmentUser.clear();
        }
        
        // CREATE CRITERIA CUSTOMER FLEXFIELD
        selectedAdvanceCriteria.clear();
        if(marketingId != 0) {
            Marketing marketing = marketingDAO.findMarketing(marketingId);
            if(marketing != null && marketing.getFileTemplate() != null && marketing.getFileTemplate().getId() != null) {
                selectedAdvanceCriteria = marketingDAO.findMarketinAdvanceCriteriaByFileTemplate(marketing.getFileTemplate().getId());
            }
        }

        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public void saveAction(ActionEvent event) {
        simulateTransfer();
        if (messageDup.equals("") && messageExceed.equals("") && transferRecord > 0) {
            try {
                assignmentTransfer = this.insertAssignmentTransfer();
            } catch (Exception e) {
                //log.error(e.getMessage());
            }
            JSFUtil.getServletContext().setAttribute("id", campaign.getId());
            JSFUtil.getServletContext().setAttribute("tab", "transfer");
            JSFUtil.redirect("campaignsummary.jsf");
        }
    }

    public AssignmentTransfer insertAssignmentTransfer() {
        Date today = new Date();
        assignmentTransfer = new AssignmentTransfer();
        assignmentTransfer.setCampaign(campaign);
        if (marketingId == 0) {
            assignmentTransfer.setMarketing(null);
        } else {
            assignmentTransfer.setMarketing(new Marketing(marketingId));
        }
        assignmentTransfer.setTransferDate(today);
        assignmentTransfer.setToUser(new Users(toUserId));
        assignmentTransfer.setNoCustomer(transferRecord);
        assignmentTransfer.setCreateBy(JSFUtil.getUserSession().getUserName());
        assignmentTransfer.setCreateDate(today);
        assignmentTransfer.setCreateByUser(JSFUtil.getUserSession().getUsers());
        this.getAssignmentTransferDAO().create(assignmentTransfer, assignmentUsers, new Users(toUserId), contactStatusAssigned, contactStatusOpened, 
                contactStatusViewed, contactStatusFollowup, marketingId, getCampaign().getId(), selectAge, selectName, selectSurname, selectGender, 
                selectHomephone, selectMobilePhone, selectOfficePhone, fromage, toage, customerName, customerSurname, gender, homePhonePrefix, 
                officePhonePrefix, mobilePhonePrefix, selectedAdvanceCriteria);

        return assignmentTransfer;
    }

    //List to UI----------------------------------------------------------------
    public Map<String, Integer> getMarketingList() {
        if (JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager")) {
            if (JSFUtil.getUserSession().getUsers().getIsAdministrator()) {
                return this.getMarketingDAO().getAvaialableTransferMarketList();
            } else {
                return this.getMarketingDAO().getAvaialableTransferMarketListByMarketingUserGroup(JSFUtil.getUserSession().getUsers().getUserGroup().getId());
            }
        } else {
            return this.getMarketingDAO().getAvaialableTransferMarketList();
        }
    }

    public Map<String, Integer> getUserGroupList() {
        return getUserGroupDAO().getUserGroupListByAgent();
    }

    public Map<String, Integer> getToUserList() {
        List<Users> usersList;
        if (toUserGroupId == 0) {
            usersList = this.getUsersDAO().findAgent(JSFUtil.getUserSession().getUsers());
        } else {
            usersList = this.getUsersDAO().findAgentByGroupId(toUserGroupId);
        }
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Users obj : usersList) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    //Listener------------------------------------------------------------------
    public void fromUserGroupChangeListener(ActionEvent event) {
        messageExceed = "";
        if (fromUserGroupId == 0) {
            selectedUser = this.getUsersDAO().findAgent(JSFUtil.getUserSession().getUsers());
        } else {
            selectedUser = this.getUsersDAO().findAgentByGroupId(fromUserGroupId);
        }
        this.initSelectedUsers();
        clearSearchList();
        FacesContext.getCurrentInstance().renderResponse();
    }

    public List<UserAssignmentValue> getSelectedAssignmentUser() {
        return selectedAssignmentUser;
    }

    private void initSelectedUsers() {
        if (selectedAssignmentUser != null) {
            selectedAssignmentUser.clear();
        }
        if (this.selectedUser != null) {
            for (Users u : this.selectedUser) {
                UserAssignmentValue uCnt = getAssignmentDetailDAO().countAssignRemain(u.getId(), contactStatusAssigned, contactStatusOpened, contactStatusViewed, contactStatusFollowup, 
                        marketingId, getCampaign().getId(), selectAge, selectName, selectSurname, selectGender, selectHomephone, selectMobilePhone, 
                        selectOfficePhone, fromage, toage, customerName, customerSurname, gender, homePhonePrefix, officePhonePrefix, mobilePhonePrefix, 
                        selectedAdvanceCriteria);
                UserAssignmentValue value;
                if (uCnt != null && uCnt.getTotals() > 0) {
                    value = new UserAssignmentValue(true, u, uCnt.getRecord() == null ? 0 : uCnt.getRecord(), uCnt.getTotals() == null ? 0 : uCnt.getTotals());
                    selectedAssignmentUser.add(value);
                }
            }
        }
    }

    public void toUserGroupChangeListener() {
        if (toUserGroupId != 0) {
            getToUserList();
        }
        messageDup = "";
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void searchCustomerListener() {
        customerList.clear();
        String userId = (String) JSFUtil.getRequestParameterMap("userId");
//        customerList = this.getAssignmentTransferDAO().findAssignmentDetail(Integer.parseInt(userId), customerName, contactStatusAssigned, contactStatusOpened, contactStatusViewed, contactStatusFollowup, marketingId, getCampaign().getId());
        customerList = this.getAssignmentTransferDAO().findAssignmentDetail(Integer.parseInt(userId), contactStatusAssigned, contactStatusOpened, 
                contactStatusViewed, contactStatusFollowup, marketingId, getCampaign().getId(), selectAge, selectName, selectSurname, selectGender, 
                selectHomephone, selectMobilePhone, selectOfficePhone, fromage, toage, customerName, customerSurname, gender, homePhonePrefix, 
                officePhonePrefix, mobilePhonePrefix, selectedAdvanceCriteria);

        FacesContext.getCurrentInstance().renderResponse();
    }

    public void clearSearchList() {
        if (!customerList.isEmpty()) {
            customerList.clear();
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void checkRecord() {
        messageExceed = "";
        assignmentUsers = this.getUserAssignmentFromCheckBox();
        for (UserAssignmentValue u : assignmentUsers) {
            if (u.getRecord() > u.getTotals()) {
                messageExceed = " Exceed transfer record ";
            }
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void simulateTransfer() {
        if (toUserId != 0) {
            messageDup = "";
            messageExceed = "";
            if (assignmentUsers != null) {
                assignmentUsers.clear();
            }
            assignmentUsers = this.getUserAssignmentFromCheckBox();
            if (!assignmentUsers.isEmpty()) {
                checkRecord();
                for (UserAssignmentValue u : assignmentUsers) {
                    if (u.getUser().getId() == toUserId) {
                        messageDup = " Duplicate user: " + u.getUser().getName();
                        if (u.getUser().getSurname() != null && !u.getUser().getSurname().isEmpty()) {
                            messageDup += " " + u.getUser().getSurname();
                        }
                    }
                }
                remainRecord = getAssignmentDetailDAO().countRecordRemain(toUserId, getCampaign().getId());
                if (messageDup.equals("") && messageExceed.equals("")) {
                    transferRecord = 0;
                    for (UserAssignmentValue u : assignmentUsers) {
                        transferRecord += u.getRecord();
                    }
                    simulateRecord = remainRecord + transferRecord;
                }
            } else {
                messageExceed = " Please select User!!!";
            }
        } else {
            messageDup = " Please select User!!!";
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    private List<UserAssignmentValue> getUserAssignmentFromCheckBox() {
        List<UserAssignmentValue> u = new ArrayList<UserAssignmentValue>();
        if (selectedAssignmentUser != null) {
            for (UserAssignmentValue obj : selectedAssignmentUser) {
                if (obj.isSelected()) {
                    u.add(obj);
                }
            }
        }
        return u;
    }

    //Properties----------------------------------------------------------------
    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setProductSponsor(Campaign campaign) {
        this.campaign = campaign;
    }

    public int getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(int marketingId) {
        this.marketingId = marketingId;
    }

    public List<Integer> getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(List<Integer> selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public int getFromUserGroupId() {
        return fromUserGroupId;
    }

    public void setFromUserGroupId(int fromUserGroupId) {
        this.fromUserGroupId = fromUserGroupId;
    }

    public int getToUserGroupId() {
        return toUserGroupId;
    }

    public void setToUserGroupId(int toUserGroupId) {
        this.toUserGroupId = toUserGroupId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public List<Integer> getSelectedAssignment() {
        return selectedAssignment;
    }

    public void setSelectedAssignment(List<Integer> selectedAssignment) {
        this.selectedAssignment = selectedAssignment;
    }

    //Managed Properties-------------------------------------------------------
    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public AssignmentDAO getAssignmentDAO() {
        return assignmentDAO;
    }

    public void setAssignmentDAO(AssignmentDAO assigmentDAO) {
        this.assignmentDAO = assigmentDAO;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assigmentDetailDAO) {
        this.assignmentDetailDAO = assigmentDetailDAO;
    }

    public AssignmentTransferDAO getAssignmentTransferDAO() {
        return assignmentTransferDAO;
    }

    public void setAssignmentTransferDAO(AssignmentTransferDAO assignmentTransferDAO) {
        this.assignmentTransferDAO = assignmentTransferDAO;
    }

    public boolean isContactStatusAssigned() {
        return contactStatusAssigned;
    }

    public void setContactStatusAssigned(boolean contactStatusAssigned) {
        this.contactStatusAssigned = contactStatusAssigned;
    }

    public boolean isContactStatusFollowup() {
        return contactStatusFollowup;
    }

    public void setContactStatusFollowup(boolean contactStatusFollowup) {
        this.contactStatusFollowup = contactStatusFollowup;
    }

    public boolean isContactStatusOpened() {
        return contactStatusOpened;
    }

    public void setContactStatusOpened(boolean contactStatusOpened) {
        this.contactStatusOpened = contactStatusOpened;
    }

    public boolean isContactStatusViewed() {
        return contactStatusViewed;
    }

    public void setContactStatusViewed(boolean contactStatusViewed) {
        this.contactStatusViewed = contactStatusViewed;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<AssignmentDetail> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<AssignmentDetail> customerList) {
        this.customerList = customerList;
    }

    public int getRemainRecord() {
        return remainRecord;
    }

    public void setRemainRecord(int remainRecord) {
        this.remainRecord = remainRecord;
    }

    public int getSimulateRecord() {
        return simulateRecord;
    }

    public void setSimulateRecord(int simulateRecord) {
        this.simulateRecord = simulateRecord;
    }

    public int getTransferRecord() {
        return transferRecord;
    }

    public void setTransferRecord(int transferRecord) {
        this.transferRecord = transferRecord;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public String getMessageExceed() {
        return messageExceed;
    }

    public void setMessageExceed(String messageExceed) {
        this.messageExceed = messageExceed;
    }
    
    // SEARCH PROPERTIES
    public List<Users> getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(List<Users> selectedUser) {
        this.selectedUser = selectedUser;
    }

    public static String getREDIRECT_PAGE() {
        return REDIRECT_PAGE;
    }

    public static void setREDIRECT_PAGE(String REDIRECT_PAGE) {
        CampaignTransferController.REDIRECT_PAGE = REDIRECT_PAGE;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getFromage() {
        return fromage;
    }

    public void setFromage(int fromage) {
        this.fromage = fromage;
    }

    public int getToage() {
        return toage;
    }

    public void setToage(int toage) {
        this.toage = toage;
    }

    public String getHomePhonePrefix() {
        return homePhonePrefix;
    }

    public void setHomePhonePrefix(String homePhonePrefix) {
        this.homePhonePrefix = homePhonePrefix;
    }

    public String getOfficePhonePrefix() {
        return officePhonePrefix;
    }

    public void setOfficePhonePrefix(String officePhonePrefix) {
        this.officePhonePrefix = officePhonePrefix;
    }

    public String getMobilePhonePrefix() {
        return mobilePhonePrefix;
    }

    public void setMobilePhonePrefix(String mobilePhonePrefix) {
        this.mobilePhonePrefix = mobilePhonePrefix;
    }

    public boolean isSelectName() {
        return selectName;
    }

    public void setSelectName(boolean selectName) {
        this.selectName = selectName;
    }

    public boolean isSelectSurname() {
        return selectSurname;
    }

    public void setSelectSurname(boolean selectSurname) {
        this.selectSurname = selectSurname;
    }

    public boolean isSelectGender() {
        return selectGender;
    }

    public void setSelectGender(boolean selectGender) {
        this.selectGender = selectGender;
    }

    public boolean isSelectAge() {
        return selectAge;
    }

    public void setSelectAge(boolean selectAge) {
        this.selectAge = selectAge;
    }

    public boolean isSelectHomephone() {
        return selectHomephone;
    }

    public void setSelectHomephone(boolean selectHomephone) {
        this.selectHomephone = selectHomephone;
    }

    public boolean isSelectOfficePhone() {
        return selectOfficePhone;
    }

    public void setSelectOfficePhone(boolean selectOfficePhone) {
        this.selectOfficePhone = selectOfficePhone;
    }

    public boolean isSelectMobilePhone() {
        return selectMobilePhone;
    }

    public void setSelectMobilePhone(boolean selectMobilePhone) {
        this.selectMobilePhone = selectMobilePhone;
    }

    public List<CriteriaListValue> getSelectedAdvanceCriteria() {
        return selectedAdvanceCriteria;
    }

    public void setSelectedAdvanceCriteria(List<CriteriaListValue> selectedAdvanceCriteria) {
        this.selectedAdvanceCriteria = selectedAdvanceCriteria;
    }
    

}
