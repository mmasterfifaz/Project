package com.maxelyz.core.controller.admin;

import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.CriteriaListValue;
import com.maxelyz.core.model.value.admin.UserAssignmentValue;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.event.ValueChangeEvent;

@ManagedBean
//@RequestScoped
@ViewScoped
public class CampaignUnAssignmentController extends BaseController implements Serializable {

    private static Logger log = Logger.getLogger(CampaignUnAssignmentController.class);
    private static String REDIRECT_PAGE = "campaign.jsf";
    private static String SUCCESS = "campaignsummary.xhtml";
    private static String FAILURE = "campaignunassignment.xhtml";
    private Campaign campaign;
    private Map<Integer, Boolean> selectedProductIds = new LinkedHashMap<Integer, Boolean>();
    private Map<Integer, Boolean> selectedUserIds = new LinkedHashMap<Integer, Boolean>();
    private Map<Integer, Boolean> selectedFollowupIds = new LinkedHashMap<Integer, Boolean>();
    private Map<Integer, Boolean> selectedOpenIds = new LinkedHashMap<Integer, Boolean>();
    private Map<Integer, Boolean> selectedMarketingIds = new LinkedHashMap<Integer, Boolean>();
    private String selectingUser;
    private String unAssignmentMode;
    private List<Users> selectedUser;
    private List<UserAssignmentValue> selectedUnassignmentUser;
    private List<UserAssignmentValue> unassignmentUsers;
    private List<Product> selectedProduct;
    private List<Marketing> selectedMarketing;
    //private List<FollowupsaleReason> selectedFollowup;
    private List<ContactResult> selectedFollowup;
    private List<ContactResult> selectedOpen;
    private int userGroupId;
    private boolean contactStatusAssigned;
    private boolean contactStatusViewed;
    private boolean contactStatusOpened;
    private boolean contactStatusFollowup;
    private List<Integer> productIds;
    private List<Integer> marketingIds;
    private List<Integer> followupIds;
    private List<Integer> openIds;
    private boolean newList = false;
    private boolean oldList = false;
    private String listType;
    private String messageExceed;
    private String userIds;
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

    @ManagedProperty(value = "#{followupsaleReasonDAO}")
    private FollowupsaleReasonDAO followupsaleReasonDAO;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{unassignmentDAO}")
    private UnassignmentDAO unassignmentDAO;
    @ManagedProperty(value = "#{assignmentDAO}")
    private AssignmentDAO assignmentDAO;
    @ManagedProperty(value = "#{contactResultDAO}")
    private ContactResultDAO contactResultDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:campaignunassignment:add")) {
            SecurityUtil.redirectUnauthorize();
        }

        if (JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager")) {
            unAssignmentMode = "manager";
        } else {
            unAssignmentMode = "supervisor";
        }

        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            JSFUtil.redirect(REDIRECT_PAGE);
        } else {
            Integer campaignId = new Integer(selectedID);
            CampaignDAO dao = getCampaignDAO();
            campaign = dao.findCampaign(campaignId);

            messageExceed = "";
            listType = "allList";
            this.selectedProduct = (List<Product>) campaign.getProductCollection();
            if (JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager")) {
                if(JSFUtil.getUserSession().getUsers().getIsAdministrator()){
                     this.selectedMarketing = (List<Marketing>) assignmentDAO.findUsedMarketingByCampaign(campaignId, listType);
                } else {
                     this.selectedMarketing = (List<Marketing>) assignmentDAO.findUsedMarketingByCampaignAndUserGroupMarketing(campaignId, listType, JSFUtil.getUserSession().getUsers().getUserGroup().getId());
                }
               
            } else {
                this.selectedMarketing = (List<Marketing>) assignmentDAO.findUsedMarketingByCampaign(campaignId, listType);
            }
            //this.selectedFollowup = this.getFollowupsaleReasonDAO().findFollowupsaleReasonEntities();
            this.selectedFollowup = this.getContactResultDAO().findFollowContactResult();
            this.selectedOpen = this.getContactResultDAO().findOpenContactResult();

            selectedUnassignmentUser = new ArrayList<UserAssignmentValue>();
            selectedAdvanceCriteria = new ArrayList<CriteriaListValue>();
        }
    }
    
    public void initAdvanceCriteria() {       
        // CREATE CRITERIA CUSTOMER FLEXFIELD
        selectedAdvanceCriteria.clear();
        boolean sameMarketing = true;
        Integer fID = null;
        if (selectedMarketing != null && selectedMarketingIds != null) {
            for (Marketing dataItem : selectedMarketing) {
                if (selectedMarketingIds.get(dataItem.getId()) != null && selectedMarketingIds.get(dataItem.getId()).booleanValue()) {  
                    if(fID == null) {
                        fID = dataItem.getFileTemplate().getId();
                    } else if (fID != dataItem.getFileTemplate().getId()) {
                        sameMarketing = false;
                        break;
                    }
                }
            }
        }
        if(sameMarketing) {
            selectedAdvanceCriteria = marketingDAO.findMarketinAdvanceCriteriaByFileTemplate(fID);
        }

        FacesContext.getCurrentInstance().renderResponse();
    }

    public void newlistTypeChangeListener(ValueChangeEvent event) {
        newList = (Boolean) event.getNewValue();
        listTypeChange(newList, oldList);
    }

    public void oldlistTypeChangeListener(ValueChangeEvent event) {
        oldList = (Boolean) event.getNewValue();
        listTypeChange(newList, oldList);
    }

    public void listTypeChange(Boolean newL, Boolean oldL) {
        if (newL && oldL) {
            listType = "allList";
        } else if (newL) {
            listType = "newList";
        } else if (oldL) {
            listType = "oldList";
        } else {
            listType = "allList";
        }
        if (JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager")) {
                if(JSFUtil.getUserSession().getUsers().getIsAdministrator()){
                     this.selectedMarketing = (List<Marketing>) assignmentDAO.findUsedMarketingByCampaign(campaign.getId(), listType);
                } else {
                     this.selectedMarketing = (List<Marketing>) assignmentDAO.findUsedMarketingByCampaignAndUserGroupMarketing(campaign.getId(), listType, JSFUtil.getUserSession().getUsers().getUserGroup().getId());
                }
               
            } else {
                this.selectedMarketing = (List<Marketing>) assignmentDAO.findUsedMarketingByCampaign(campaign.getId(), listType);
            }
    }

    public void clearValueListener() {
        contactStatusAssigned = false;
        contactStatusFollowup = false;
        contactStatusOpened = false;
        contactStatusViewed = false;
        if (selectedFollowupIds != null) {
            selectedFollowupIds.clear();
        }
        if (selectedOpenIds != null) {
            selectedOpenIds.clear();
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void initializeListener(ActionEvent event) {
        userGroupId = 0;
        fromage = 0;
        toage = 0;
        customerName = "";
        customerSurname = "";
        gender = "M";
        homePhonePrefix = "notspecific";
        officePhonePrefix = "notspecific";
        mobilePhonePrefix = "notspecific";
        contactStatusAssigned = false;
        contactStatusViewed = false;
        contactStatusOpened = false;
        contactStatusFollowup = false;
        selectAge = false;
        selectName = false;
        selectSurname = false;
        selectGender = false;
        selectHomephone = false;
        selectOfficePhone = false;
        selectMobilePhone = false;
        newList = false;
        oldList = false;

        if (selectedMarketingIds != null) {
            selectedMarketingIds.clear();
        }
        if (selectedProductIds != null) {
            selectedProductIds.clear();
        }
        if (selectedUserIds != null) {
            selectedUserIds.clear();
        }
        if (selectedFollowupIds != null) {
            selectedFollowupIds.clear();
        }
        if (selectedOpenIds != null) {
            selectedOpenIds.clear();
        }
        if (selectedUser != null) {
            selectedUser.clear();
        }
        if (selectedUnassignmentUser != null) {
            selectedUnassignmentUser.clear();
        }
        if(selectedAdvanceCriteria != null) {
            selectedAdvanceCriteria.clear();
        }
        selectingUser = null;
        messageExceed = "";
        initialize();
    }

    public boolean isManagerPermitted() {
        String role = JSFUtil.getUserSession().getUserGroup().getRole();
        if (role.contains("CampaignManager")) {
            return true;
        } else {
            return false;
        }
    }

    private String listToString(List<Integer> l) {
        StringBuilder sb = new StringBuilder();
        for (int id : l) {
            sb.append(id);
            sb.append(',');
        }
        return sb.toString();
    }

    private Unassignment insertUnassignment() {
        unassignmentUsers = this.getUserUnassignmentFromCheckBox();
        productIds = this.getProductIdsFromCheckBox();
        marketingIds = this.getMarketingIdsFromCheckBox();
        followupIds = null;
        openIds = null;
        Unassignment unassignment = new Unassignment();
        unassignment.setCampaign(campaign);
        unassignment.setUnassignmentMode(unAssignmentMode);
        unassignment.setListType(listType);
        unassignment.setMarketingList(this.listToString(marketingIds));
        unassignment.setCreateDate(new Date());
        unassignment.setCreateBy(getLoginUserName());

        unassignment.setUsers(userIds);
        unassignment.setProduct(this.listToString(productIds));
        if (unAssignmentMode.equals("supervisor")) {
            followupIds = this.getFollowupIdsFromCheckBox();
            openIds = this.getOpenIdsFromCheckBox();
            StringBuilder contactStatus = new StringBuilder();
            contactStatus.append(contactStatusAssigned ? "assigned," : "");
            contactStatus.append(contactStatusViewed ? "viewed," : "");
            if (contactStatusOpened || (openIds != null && !openIds.isEmpty())) {
                contactStatus.append("opened,");
                contactStatus.append(this.listToString(openIds));
            }
            if (contactStatusFollowup || (followupIds != null && !followupIds.isEmpty())) {
                contactStatus.append("follow up,");
                contactStatus.append(this.listToString(followupIds));
            }
            unassignment.setContactStatus(contactStatus.toString());
        }

        this.getUnassignmentDAO().createUnassignment(unAssignmentMode, unassignment, unassignmentUsers, productIds,
                contactStatusAssigned, contactStatusOpened, contactStatusViewed, contactStatusFollowup, followupIds, openIds, marketingIds, 
                listType, selectAge, selectName, selectSurname, selectGender, selectHomephone, selectMobilePhone, selectOfficePhone, fromage, toage, 
                customerName, customerSurname, gender, homePhonePrefix, officePhonePrefix, mobilePhonePrefix, selectedAdvanceCriteria);
        return unassignment;
    }

    private List<UserAssignmentValue> getUserUnassignmentFromCheckBox() {
        userIds = "";
        List<UserAssignmentValue> u = new ArrayList<UserAssignmentValue>();
        if (selectedUnassignmentUser != null) {
            for (UserAssignmentValue obj : selectedUnassignmentUser) {
                if (obj.isSelected()) {
                    u.add(obj);
                    userIds += obj.getUser().getId().toString() + ",";
                }
            }
        }
        return u;
    }

    public void checkRecord() {
        messageExceed = "";
        unassignmentUsers = this.getUserUnassignmentFromCheckBox();
        for (UserAssignmentValue u : unassignmentUsers) {
            if (u.getRecord() > u.getTotals()) {
                messageExceed = " Exceed unassign record ";
            }
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void saveAction(ActionEvent event) {
        checkRecord();
        if (messageExceed.equals("")) {
            try {
                Unassignment u = insertUnassignment();
            } catch (Exception e) {
                //log.error(e.getMessage());
            }
            JSFUtil.getServletContext().setAttribute("id", campaign.getId());
            JSFUtil.getServletContext().setAttribute("tab", "unassignment");
            JSFUtil.redirect("campaignsummary.jsf");
        }
    }

    public String backAction() {
        return SUCCESS;
    }

    public void assignmentModeChangeListener() {
        selectingUser = null;
        if (selectedUnassignmentUser != null) {
            selectedUnassignmentUser.clear();
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void userSelectingChangeListener() {
        if (selectedUnassignmentUser != null) {
            selectedUnassignmentUser.clear();
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void userGroupChangeListener(ActionEvent event) {
        if (selectedUnassignmentUser != null) {
            selectedUnassignmentUser.clear();
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void initSelectedUser() {
        messageExceed = "";
        if (selectingUser.equals("all")) {
            if (unAssignmentMode.equals("manager")) {
                selectedUser = this.getUsersDAO().findSupervisor(JSFUtil.getUserSession().getUsers());
            } else {
                selectedUser = this.getUsersDAO().findAgentWithSystem(JSFUtil.getUserSession().getUsers());
            }
        } else {
            selectedUser = this.getUsersDAO().findUsersWithSystemEntitiesByUserGroup(userGroupId);
        }
        productIds = this.getProductIdsFromCheckBox();
        marketingIds = this.getMarketingIdsFromCheckBox();
        followupIds = null;
        openIds = null;
        if (unAssignmentMode.equals("supervisor")) {
            followupIds = this.getFollowupIdsFromCheckBox();
            openIds = this.getOpenIdsFromCheckBox();
            StringBuilder contactStatus = new StringBuilder();
            contactStatus.append(contactStatusAssigned ? "assigned," : "");
            contactStatus.append(contactStatusViewed ? "viewed," : "");
            if (contactStatusOpened || (openIds != null && !openIds.isEmpty())) {
                contactStatus.append("opened,");
                contactStatus.append(this.listToString(openIds));
            }
            if (contactStatusFollowup || (followupIds != null && !followupIds.isEmpty())) {
                contactStatus.append("follow up,");
                contactStatus.append(this.listToString(followupIds));
            }
        }
        if (selectedUnassignmentUser != null) {
            selectedUnassignmentUser.clear();
        }
        if (this.selectedUser != null) {
            for (Users u : this.selectedUser) {
                int uCnt = getUnassignmentDAO().countAssignmentDetail(unAssignmentMode, u.getId(), productIds, contactStatusAssigned, contactStatusOpened,
                        contactStatusViewed, contactStatusFollowup, followupIds, openIds, marketingIds, listType, campaign.getId(), selectAge, selectName, selectSurname,
                        selectGender, selectHomephone, selectMobilePhone, selectOfficePhone, fromage, toage, customerName, customerSurname, gender, homePhonePrefix,
                        officePhonePrefix, mobilePhonePrefix, selectedAdvanceCriteria);
//                long record=0;
                UserAssignmentValue value;
                if (uCnt > 0) {
//                    record = uCnt;
//                    value =  new UserAssignmentValue(true,u,record,(long)uCnt);
                    value = new UserAssignmentValue(true, u, (long) uCnt, (long) uCnt);
                    selectedUnassignmentUser.add(value);
                }

            }
        }
    }

    public List<UserAssignmentValue> getSelectedUnassignmentUser() {
        return selectedUnassignmentUser;
    }

    //Properties and method
    private List<Integer> getProductIdsFromCheckBox() {
        List<Integer> productIds = new ArrayList<Integer>();
        if (selectedProduct != null && selectedProductIds != null) {
            for (Product dataItem : selectedProduct) {
                if (selectedProductIds.get(dataItem.getId()).booleanValue()) {
                    productIds.add(dataItem.getId());
                }
            }
        }
        return productIds;
    }

    private List<Integer> getFollowupIdsFromCheckBox() {
        List<Integer> followupIds = new ArrayList<Integer>();
        if (selectedFollowup != null && !selectedFollowupIds.isEmpty() && selectedFollowupIds != null) {
            for (ContactResult dataItem : selectedFollowup) {
                if (selectedFollowupIds.get(dataItem.getId()).booleanValue()) {
                    followupIds.add(dataItem.getId());
                }
            }
        }
        return followupIds;
    }

    private List<Integer> getOpenIdsFromCheckBox() {
        List<Integer> openIds = new ArrayList<Integer>();
        if (selectedOpen != null && !selectedOpenIds.isEmpty() && selectedOpenIds != null) {
            for (ContactResult dataItem : selectedOpen) {
                if (selectedOpenIds.get(dataItem.getId()).booleanValue()) {
                    openIds.add(dataItem.getId());
                }
            }
        }
        return openIds;
    }

    private List<Integer> getMarketingIdsFromCheckBox() {
        List<Integer> marketingIds = new ArrayList<Integer>();
        if (selectedMarketing != null && selectedMarketingIds != null) {
            for (Marketing dataItem : selectedMarketing) {
                if (selectedMarketingIds.get(dataItem.getId()).booleanValue()) {
                    marketingIds.add(dataItem.getId());
                }
            }
        }
        return marketingIds;
    }

    public Map<Integer, Boolean> getSelectedFollowupIds() {
        return selectedFollowupIds;
    }

    public Map<Integer, Boolean> getSelectedOpenIds() {
        return selectedOpenIds;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setProductSponsor(Campaign campaign) {
        this.campaign = campaign;
    }

    public List<Product> getProductList() {
        return selectedProduct;
    }

    public List<ContactResult> getFollowupsaleReasonList() {
        return selectedFollowup;
    }

    public List<ContactResult> getOpensaleReasonList() {
        return selectedOpen;
    }

    public List<Marketing> getMarketingList() {
        return selectedMarketing;
    }

    public Map<Integer, Boolean> getSelectedProductIds() {
        return selectedProductIds;
    }

    public void setSelectedProductIds(Map<Integer, Boolean> selectedProductIds) {
        this.selectedProductIds = selectedProductIds;
    }

    public Map<Integer, Boolean> getSelectedMarketingIds() {
        return selectedMarketingIds;
    }

    public void setSelectedMarketingIds(Map<Integer, Boolean> selectedMarketingIds) {
        this.selectedMarketingIds = selectedMarketingIds;
    }

    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Map<String, Integer> getUserGroupList() {
        if (unAssignmentMode.equals("manager")) {
            return this.getUserGroupDAO().getUserGroupListBySupervisor();
        } else {
            return this.getUserGroupDAO().getUserGroupListByAgent();
        }
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

    public boolean getNewList() {
        return newList;
    }

    public void setNewList(boolean newList) {
        this.newList = newList;
    }

    public boolean getOldList() {
        return oldList;
    }

    public void setOldList(boolean oldList) {
        this.oldList = oldList;
    }

    //Managed Properties
    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public FollowupsaleReasonDAO getFollowupsaleReasonDAO() {
        return followupsaleReasonDAO;
    }

    public void setFollowupsaleReasonDAO(FollowupsaleReasonDAO followupsaleReasonDAO) {
        this.followupsaleReasonDAO = followupsaleReasonDAO;
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

    public UnassignmentDAO getUnassignmentDAO() {
        return unassignmentDAO;
    }

    public void setUnassignmentDAO(UnassignmentDAO unassignmentDAO) {
        this.unassignmentDAO = unassignmentDAO;
    }

    public AssignmentDAO getAssignmentDAO() {
        return assignmentDAO;
    }

    public void setAssignmentDAO(AssignmentDAO assignmentDAO) {
        this.assignmentDAO = assignmentDAO;
    }

    public String getUnAssignmentMode() {
        return unAssignmentMode;
    }

    public void setUnAssignmentMode(String unAssignmentMode) {
        this.unAssignmentMode = unAssignmentMode;
    }

    public ContactResultDAO getContactResultDAO() {
        return contactResultDAO;
    }

    public void setContactResultDAO(ContactResultDAO contactResultDAO) {
        this.contactResultDAO = contactResultDAO;
    }

    public String getSelectingUser() {
        return selectingUser;
    }

    public void setSelectingUser(String selectingUser) {
        this.selectingUser = selectingUser;
    }

    public String getMessageExceed() {
        return messageExceed;
    }

    public void setMessageExceed(String messageExceed) {
        this.messageExceed = messageExceed;
    }

    public int getFromage() {
        return fromage;
    }

    public void setFromage(int fromage) {
        this.fromage = fromage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHomePhonePrefix() {
        return homePhonePrefix;
    }

    public void setHomePhonePrefix(String homePhonePrefix) {
        this.homePhonePrefix = homePhonePrefix;
    }

    public String getMobilePhonePrefix() {
        return mobilePhonePrefix;
    }

    public void setMobilePhonePrefix(String mobilePhonePrefix) {
        this.mobilePhonePrefix = mobilePhonePrefix;
    }

    public String getOfficePhonePrefix() {
        return officePhonePrefix;
    }

    public void setOfficePhonePrefix(String officePhonePrefix) {
        this.officePhonePrefix = officePhonePrefix;
    }

    public int getToage() {
        return toage;
    }

    public void setToage(int toage) {
        this.toage = toage;
    }

    public boolean isSelectAge() {
        return selectAge;
    }

    public void setSelectAge(boolean selectAge) {
        this.selectAge = selectAge;
    }

    public boolean isSelectGender() {
        return selectGender;
    }

    public void setSelectGender(boolean selectGender) {
        this.selectGender = selectGender;
    }

    public boolean isSelectHomephone() {
        return selectHomephone;
    }

    public void setSelectHomephone(boolean selectHomephone) {
        this.selectHomephone = selectHomephone;
    }

    public boolean isSelectMobilePhone() {
        return selectMobilePhone;
    }

    public void setSelectMobilePhone(boolean selectMobilePhone) {
        this.selectMobilePhone = selectMobilePhone;
    }

    public boolean isSelectOfficePhone() {
        return selectOfficePhone;
    }

    public void setSelectOfficePhone(boolean selectOfficePhone) {
        this.selectOfficePhone = selectOfficePhone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public List<CriteriaListValue> getSelectedAdvanceCriteria() {
        return selectedAdvanceCriteria;
    }

    public void setSelectedAdvanceCriteria(List<CriteriaListValue> selectedAdvanceCriteria) {
        this.selectedAdvanceCriteria = selectedAdvanceCriteria;
    }

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }
    
    
}
