package com.maxelyz.core.controller.admin;

import com.maxelyz.core.controller.Global;
import com.maxelyz.core.model.dao.AclDAO;
import com.maxelyz.core.model.dao.BusinessUnitDAO;
import com.maxelyz.core.model.dao.CtiAdapterDAO;
import com.maxelyz.core.model.dao.LocationDAO;
import com.maxelyz.core.model.dao.ServiceTypeDAO;
import com.maxelyz.core.model.dao.UserGroupAclDAO;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.entity.UserGroup;
import javax.faces.bean.*;

import javax.faces.model.SelectItem;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.Acl;
import com.maxelyz.core.model.entity.BusinessUnit;
import com.maxelyz.core.model.entity.CtiAdapter;
import com.maxelyz.core.model.entity.Location;
import com.maxelyz.core.model.entity.ServiceType;
import com.maxelyz.core.model.entity.UserGroupAcl;
import com.maxelyz.core.model.entity.UserGroupLocation;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.model.value.admin.BusinessUnitValue;
import com.maxelyz.core.model.value.admin.LocationValue;
import com.maxelyz.core.model.value.admin.ServiceTypeValue;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.*;
import java.util.ArrayList;
import com.maxelyz.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

@ManagedBean (name="userGroupEditController")
//@RequestScoped
@ViewScoped
public class UserGroupEditController implements Serializable {

    private static Logger log = Logger.getLogger(UserGroupEditController.class);
    private static String SUCCESS = "usergroup.jsf";
    private static String FAILURE = "usergroupedit.xhtml";
    private UserGroup userGroup;
    private String mode;
    private SelectItem[] roleList;
    private String[] roleString;
    private List<Users> users;
    private int[] groupUsersList;
    private Collection<Users> usersCollection;
    private List<Integer> selectedUsers;
    private Integer parentId;
    private Map<String, Integer> parentList;
    private List<Acl> aclList;
    private List<Acl> aclListShow;
    private Acl custom;
    private List<Acl> customList;
    private List<UserGroupAcl> selectedAclList;
    private List<Acl> selectedCustomList;
    private boolean selected;
    private List<ServiceTypeValue> serviceTypeValueList;
    private String roles;
    private List<String> roleOperation;
    private List<String> roleSale;
    private List<String> roleKnowledge;
    private static String selectedTab;
    private String tab = "role";
    private String message = "";
    private String licenseGroup = "";
    private Integer ctiAdapterID;
    private Map<String, Integer> ctiAdapterList;
     
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{userGroupAclDAO}")
    private UserGroupAclDAO userGroupAclDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{aclDAO}")
    private AclDAO aclDAO;
    @ManagedProperty(value = "#{serviceTypeDAO}")
    private ServiceTypeDAO serviceTypeDAO;
    @ManagedProperty(value = "#{businessUnitDAO}")
    private BusinessUnitDAO businessUnitDAO;
    @ManagedProperty(value = "#{locationDAO}")
    private LocationDAO locationDAO;
    @ManagedProperty(value = "#{ctiAdapterDAO}")
    private CtiAdapterDAO ctiAdapterDAO;

    @PostConstruct
    public void initialize() {        
        message = "";
        parentId = 0;
        //Tab
        tab = JSFUtil.getRequestParameterMap("tab");
        this.setSelectedTab(tab); 
        
        String selectedID = null;
        try {
            selectedID = (String) JSFUtil.getRequestParameterMap("selectedID");
            if (!SecurityUtil.isPermitted("admin:usergroup:view")) {
                SecurityUtil.redirectUnauthorize();
            }
            if (selectedID == null || selectedID.isEmpty()) {
                mode = "add";
                licenseGroup = "";
                userGroup = new UserGroup();
                userGroup.setStatus(Boolean.TRUE);
                parentList = userGroupDAO.getUserGroupList();
                roleOperation = new ArrayList<String>();
                roleSale = new ArrayList<String>();
                roleKnowledge = new ArrayList<String>();
            } else {
                mode = "edit";
                userGroup = userGroupDAO.findUserGroup(new Integer(selectedID));
                parentId = userGroup.getParentId();
                parentList = userGroupDAO.getUserGroupListExcept(new Integer(selectedID));
                selectedUsers = new ArrayList<Integer>();
                for (Users usr : userGroup.getUsersCollection()) {
                    selectedUsers.add(usr.getId());
                }
                licenseGroup = userGroup.getConcurrentGroup();
                if(userGroup.getCtiAdapter() != null) {
                    ctiAdapterID = userGroup.getCtiAdapter().getId();
                }
                String[] roleArr = userGroup.getRole().split(",");
                roleOperation = new ArrayList<String>();
                roleSale = new ArrayList<String>();
                roleKnowledge = new ArrayList<String>();
                for (String role : roleArr) {
                    if(role.equals("CampaignManager") || role.equals("Supervisor") || role.equals("Agent") || role.equals("CSCounter") || role.equals("SOAgent") || role.equals("SOSupervisor"))
                        roleOperation.add(role);
                    else if(role.equals("Uw") || role.equals("Payment") || role.equals("Qc"))
                            roleSale.add(role);
                    else if(role.equals("KBManager") || role.equals("KBApproval") || role.equals("KBDataEntry"))
                            roleKnowledge.add(role);
                }
            }
        } catch (NullPointerException e) {
            JSFUtil.redirect(SUCCESS);
        }
        fillServiceType();
        fillAcl();
       
        // SET CTI ADAPTER LIST
        ctiAdapterList = this.getCtiAdapterDAO().findCtiAdapterList();
        
        message = "";
    }
    
//    public void initializeListener() {
//        message = "";
//        parentId = 0;
//        initialize();
//    }
    
    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:usergroup:add");
        } else {
            return SecurityUtil.isPermitted("admin:usergroup:edit");
        }
    }

    private void fillServiceType() {
        serviceTypeValueList = new ArrayList<ServiceTypeValue>();
        List<ServiceType> list = serviceTypeDAO.findServiceTypeStatus();
        List<Integer> serviceTypeIdList = null;
        if (userGroup.getId() != null) {
            serviceTypeIdList = userGroupDAO.findServiceId(userGroup.getId());
        }
        for (ServiceType s : list) {
            ServiceTypeValue serviceTypeValue = new ServiceTypeValue(s);
            serviceTypeValue.setBusinessUnitValueList(fillBu(s));
            if (userGroup.getId() != null) {
                for (Integer serviceTypeId : serviceTypeIdList) {
                    if (serviceTypeId.intValue() == s.getId().intValue()) {
                        serviceTypeValue.setSelected(true);
                        break;
                    }
                }
            } else {
                serviceTypeValue.setSelected(true);
            }
            serviceTypeValueList.add(serviceTypeValue);
        }

    }

    private List<BusinessUnitValue> fillBu(ServiceType serviceType) {
        List<BusinessUnitValue> businessUnitValueList = new ArrayList<BusinessUnitValue>();
//        Collection<BusinessUnit> list = serviceType.getBusinessUnitCollection();
        Collection<BusinessUnit> list = businessUnitDAO.findBusinessUnitByServiceTypeId(serviceType.getId());
        List<Integer> businessUnitIdList = null;
        if (userGroup.getId() != null) {
            businessUnitIdList = userGroupDAO.findBusinessUnitId(userGroup.getId(), serviceType.getId());
        }
        for (BusinessUnit b : list) {
            BusinessUnitValue businessUnitValue = new BusinessUnitValue(b);
            businessUnitValue.setLocationValueList(fillLocation(serviceType.getId(), b));
            if (userGroup.getId() != null) {
                for (Integer businessUnitId : businessUnitIdList) {
                    if (b.getId().intValue() == businessUnitId.intValue()) {
                        businessUnitValue.setSelected(true);
                        break;
                    }
                }
            } else {
                businessUnitValue.setSelected(true);
            }
            businessUnitValueList.add(businessUnitValue);
        }

        return businessUnitValueList;

    }

    private List<LocationValue> fillLocation(Integer serviceTypeId, BusinessUnit businessUnit) {
        List<LocationValue> locationValueList = new ArrayList<LocationValue>();
//        Collection<Location> list = businessUnit.getLocationCollection();
        Collection<Location> list = locationDAO.findLocationByBusinessUnitId(businessUnit.getId());
        List<Integer> locationIdList = null;
        if (userGroup.getId() != null) {
            locationIdList = userGroupDAO.findLocationId(userGroup.getId(), serviceTypeId, businessUnit.getId());
        }
        for (Location location : list) {
            LocationValue locationValue = new LocationValue(location);
            if (userGroup.getId() != null) {
                for (Integer locationId : locationIdList) {
                    if (location.getId().intValue() == locationId.intValue()) {
                        locationValue.setSelected(true);
                        break;
                    }
                }
            } else {
                locationValue.setSelected(true);
            }
            locationValueList.add(locationValue);
        }
        return locationValueList;
    }

    private void fillAcl() {
        List<UserGroupAcl> list = null;
        if (userGroup.getId() != null) {
            list = userGroupAclDAO.findUserGroupAclByUserGroupId(userGroup.getId());
        } else {
            list = new ArrayList<UserGroupAcl>();
        }

        aclList = aclDAO.findAclEntities();
        List<Acl> list1 = new ArrayList<Acl>();
        for (Acl acl : aclList) {
            if (userGroup.getId() != null) {
                for (UserGroupAcl userGroupAcl : list) {
                    if (userGroupAcl.getAcl().getCode().equals(acl.getCode())) {
                        acl.setSelected(Boolean.TRUE);
                        break;
                    }
                }
            } else {
                acl.setSelected(true);
            }
            list1.add(acl);
        }
        aclList = list1;

//        aclListShow = new ArrayList<Acl>();
//        for (Acl acl : aclList) {
//            if (acl.getParentCode() == null) {
//                aclListShow.add(acl);
//                List<Acl> aclList2 = aclDAO.findByParentCode(acl.getCode());
//                for (Acl acl1 : aclList2) {
//                    aclListShow.add(acl1);
//                }
//            }
//        }
    }

    public Map<String, Integer> getUsersList() {

        UsersDAO dao = getUsersDAO();//new UsersDAO();
        List<Users> listUsers = dao.findUsersEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Users user : listUsers) {
            values.put(user.getName(), user.getId());
        }
        return values;
    }

    public List<Integer> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<Integer> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public List<Users> getSelectedUsersCollection() {
        List<Users> usr = new ArrayList<Users>();
        for (int uid : getSelectedUsers()) {
            Users u = new Users();
            u.setId(uid);
            usr.add(u);
        }
        return usr;
    }

    private List<UserGroupAcl> mergeUserGroupAcl() {
        try {
            List<UserGroupAcl> list = null;
            if (userGroup != null && userGroup.getId() != null) {
                list = userGroupAclDAO.findUserGroupAclByUserGroupId(userGroup.getId());
                for (UserGroupAcl userGroupAcl : list) {
                    userGroupAclDAO.destroy(userGroupAcl.getUserGroupAclPK());
                }
            }
        } catch (Exception e) {
            log.error(e);
        }

//        List<Acl> list = new ArrayList<Acl>();
//
//        for (Acl acl1 : aclList) {
//            for (Acl acl : aclListShow) {
//                if (acl.getCode().equals(acl1.getCode())) {
//                    if (acl.getSelected()) {
//                        acl1.setSelected(Boolean.TRUE);
//                    }
//                    break;
//                }
//            }
//            list.add(acl1);
//        }
//        aclList = list;


        List<UserGroupAcl> list1 = new ArrayList<UserGroupAcl>();
        try {
            for (Acl acl : aclList) {
                if (acl.getSelected() != null && acl.getSelected()) {
                    UserGroupAcl userGroupAcl = new UserGroupAcl(acl.getCode(), userGroup.getId());
                    userGroupAcl.setAcl(acl);
                    userGroupAcl.setUserGroup(userGroup);
                    list1.add(userGroupAcl);
                }
            }
//            if (selectedCustomList != null && !selectedCustomList.isEmpty()) {
//                for (Acl acl : selectedCustomList) {
//                    if (acl.getSelected()) {
//                        UserGroupAcl userGroupAcl1 = new UserGroupAcl(acl.getCode(), userGroup.getId());
//                        userGroupAcl1.setAcl(acl);
//                        userGroupAcl1.setUserGroup(userGroup);
//                        selectedAclList.add(userGroupAcl1);
//                    }
//                }
//            }
        } catch (Exception e) {
            log.error(e);
        }
        return list1;
    }

//    public void changeServiceTypeListener(ValueChangeEvent event) {
//        String str = (String) event.getNewValue();
//    }
    public void customListener(ActionEvent event) {
        String code = JSFUtil.getRequestParameterMap("code");
        customList = new ArrayList<Acl>();
        custom = null;
        for (Acl acl : aclList) {
            if (acl.getParentCode() != null) {
                if (acl.getParentCode().getCode().equals(code)) {
                    customList.add(acl);
                    custom = acl.getParentCode();
                }
            }
        }
//
//        for (Acl acl : customList) {
//            for (Acl acl1 : aclList) {
//                try {
//                    if (acl.getCode().equals(acl1.getCode()) && acl1.getSelected()) {
//                        acl.setSelected(Boolean.TRUE);
//                        break;
//                    }
//                } catch (Exception e) {
//                    log.error(e);
//                }
//            }
//        }
    }

    private String listToString(List<String> list) {
        String result = "";
        for (String l : list) {
            result += l + ",";
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public String saveAction() {
        roles = null;
        if(checkUserGroup(userGroup)) {
            try {
                userGroup.setParentId(parentId);
    //            userGroup.setAuthServiceType(fillAuthenServiceType());
    //            userGroup.setAuthBusinessUnit(fillAuthenBusinessUnit());
                userGroup.setEnable(true);
                //userGroup.setRole(this.convertRoleString());
                //userGroup.setUsersCollection(this.getSelectedUsersCollection());
                userGroup.setConcurrentGroup(licenseGroup);
                if(!roleOperation.isEmpty())
                    roles = listToString(roleOperation);
                if(!roleSale.isEmpty())
                    roles += ","+listToString(roleSale);
                if(!roleKnowledge.isEmpty())
                    roles += ","+listToString(roleKnowledge);
                userGroup.setRole(roles==null?"":roles);
                
                if(ctiAdapterID != null && ctiAdapterID != 0) {
                    userGroup.setCtiAdapter(new CtiAdapter(ctiAdapterID));
                } else {
                    userGroup.setCtiAdapter(null);
                }
                
                if (getMode().equals("add")) {
                    userGroup.setId(null);//fix some bug
                    userGroupDAO.create(userGroup);
                } else {
                    userGroup.setUserGroupAclCollection(null);
                    userGroupDAO.edit(userGroup);
                }
                
                userGroupAclDAO.createUserGroupAcl(mergeUserGroupAcl());
                saveServiceType();
                this.getUserGroupController().searchActionListener(null);
            } catch (Exception e) {
                log.error(e);
                return FAILURE;
            }
            return SUCCESS;
        } else {
            message = " Group name is already taken";
            return null;            
        }
    }
    
    public UserGroupController getUserGroupController() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ValueExpression vex = app.getExpressionFactory().
                createValueExpression(context.getELContext(), "#{userGroupController}", UserGroupController.class);
        UserGroupController g = (UserGroupController) vex.getValue(context.getELContext());
        return g;
    }

    public Boolean checkUserGroup(UserGroup userGreoup) {
        String name = userGreoup.getName();
        Integer id=0;
        if(userGreoup.getId() != null)
            id = userGreoup.getId();
        UserGroupDAO dao = getUserGroupDAO();
        List<UserGroup> userGroup = dao.checkUserGroupByName(name, id);
        if(userGroup.size() == 0)
            return true;
        else
            return false;
    }
    
    private void saveServiceType() {
        try {
            List<UserGroupLocation> list = null;
            if (userGroup != null && userGroup.getId() != null) {
                list = userGroupDAO.findUserGroupLocationByUserGroupId(userGroup.getId());
                for (UserGroupLocation userGroupLocation : list) {
                    userGroupDAO.destroyUserGroupLocation(userGroupLocation.getUserGroupLocationPK());
                }
            }
        } catch (Exception e) {
            log.error(e);
        }

        try {
            for (ServiceTypeValue s : serviceTypeValueList) {
                for (BusinessUnitValue b : s.getBusinessUnitValueList()) {
                    for (LocationValue l : b.getLocationValueList()) {
                        if (l.isSelected()) {
                            UserGroupLocation userGroupLocation = new UserGroupLocation(userGroup.getId(), s.getServiceType().getId(), b.getBusinessUnit().getId(), l.getLocation().getId());
//                            userGroupLocation.setServiceType(s.getServiceType());
//                            userGroupLocation.setBusinessUnit(b.getBusinessUnit());
//                            userGroupLocation.setLocation(l.getLocation());
                            userGroupDAO.createUserGroupLocation(userGroupLocation);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

//    private String fillAuthenServiceType(){
//        String str = "";
//        for(ServiceTypeValue s : serviceTypeValueList){
//            if(s.isSelected()){
//                str += s.getServiceType().getId().toString() + ",";
//            }
//        }
//        str = str.substring(0, str.length()-1);
//        return str;
//    }
//    
//    private String fillAuthenBusinessUnit(){
//        String str = "";
//        for(BusinessUnitValue obj : businessUnitValueList){
//            if(obj.isSelected()){
//                str += obj.getBusinessUnit().getId().toString() + ",";
//            }
//        }
//        if(!str.isEmpty()){
//            str = str.substring(0, str.length()-1);
//        }
//        return str;
//    }
    public void saveCustomAction(ActionEvent event) {
        try {
//            List<Acl> tmpList = aclList;
//            for (Acl acl : tmpList) {
//                for (Acl acl1 : customList) {
//                    if (acl.getCode().equals(acl1.getCode())) {
//                        aclList.remove(acl);
//                    }
//                }
//            }
            List<Acl> list = new ArrayList<Acl>();
            for (Acl acl : aclList) {
                for (Acl acl1 : customList) {
                    if (acl.getCode().equals(acl1.getCode())) {
                        if (acl.getSelected()) {
                            acl.setSelected(Boolean.TRUE);
                        }
                        break;
                    }
                }
                list.add(acl);
            }
            aclList = list;

        } catch (Exception e) {
            log.error(e);
        }
    }

    public String backAction() {
        return SUCCESS;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return the roleList
     */
    public Map<String, String> getRoleList() {
        return this.userGroupDAO.getRoleList();
    }

    /**
     * @return the roleString
     */
    public String[] getRoleString() {
        String role = userGroup.getRole();
        if (role == null) {
            role = "0";
        }
        this.roleString = role.split(",");
        return roleString;
    }

    /**
     * @param roleString the roleString to set
     */
    public void setRoleString(String[] roleString) {
        this.roleString = roleString;
    }

    public String convertRoleString() {
        String role = "";
        for (int i = 0; i
                < this.roleString.length; i++) {
            role += this.roleString[i];
            if ((this.roleString.length - 1) > i) {
                role += ",";
            }
        }
        return role;
    }

    /**
     * @return the usersList
     */
    /*
    public SelectItem[] getUsersList() {
    UsersDAO dao = getUsersDAO();
    users = dao.findUsersEntities();
    this.usersList = new SelectItem[users.size()];
    int pos=0;
    for(Users p : users){
    this.usersList[pos++] = new SelectItem(p.getId(), p.getName()) ;
    }
    return usersList;
    }
     */
    /**
     * @param usersList the usersList to set
     */
    /*
    public void setUsersList(SelectItem[] usersList) {
    this.usersList = usersList;
    }
     *
     */
    /**
     * @return the usersCollection
     */
    public Collection<Users> getUsersCollection() {
        usersCollection = new ArrayList<Users>();
        for (int i = 0; i
                < this.groupUsersList.length; i++) {
            usersCollection.add(new Users(this.groupUsersList[i]));
        }
        return usersCollection;
    }

    /**
     * @param usersCollection the usersCollection to set
     */
    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    /**
     * @return the groupUsersList
     */
    public int[] getGroupUsersList() {
        UsersDAO dao = getUsersDAO();
        if (userGroup.getId() != null) {
            users = dao.findUsersEntitiesByUserGroup(userGroup.getId());
            this.groupUsersList = new int[users.size()];
            int pos = 0;
            for (Users p : users) {
                this.groupUsersList[pos++] = p.getId();
            }
        }
        return groupUsersList;
    }

    public void serviceTypeListener() {
        Integer param1 = Integer.parseInt(JSFUtil.getRequestParameterMap("serviceTypeId"));
        Boolean param2 = !Boolean.parseBoolean(JSFUtil.getRequestParameterMap("serviceTypeStatus"));
        List<ServiceTypeValue> list = new ArrayList<ServiceTypeValue>();
        for (ServiceTypeValue s : serviceTypeValueList) {
            if (s.getServiceType().getId().intValue() == param1.intValue()) {
                List<BusinessUnitValue> list1 = new ArrayList<BusinessUnitValue>();
                for (BusinessUnitValue b : s.getBusinessUnitValueList()) {
                    if (param2) {
                        b.setSelected(true);
                    } else {
                        b.setSelected(false);
                    }

                    List<LocationValue> list2 = new ArrayList<LocationValue>();
                    for (LocationValue l : b.getLocationValueList()) {
                        if (param2) {
                            l.setSelected(true);
                        } else {
                            l.setSelected(false);
                        }
                        list2.add(l);
                    }
                    b.setLocationValueList(list2);
                    list1.add(b);
                }
                s.setBusinessUnitValueList(list1);
                list.add(s);

            } else {
                list.add(s);
            }
        }
        serviceTypeValueList = list;

    }

    public void businessUnitListener() {
        Integer businessUnitId = Integer.parseInt(JSFUtil.getRequestParameterMap("businessUnitId"));
        Integer serviceTypeId = Integer.parseInt(JSFUtil.getRequestParameterMap("serviceTypeId"));
        Boolean param2 = !Boolean.parseBoolean(JSFUtil.getRequestParameterMap("businessUnitStatus"));
        List<ServiceTypeValue> list = new ArrayList<ServiceTypeValue>();
        for (ServiceTypeValue s : serviceTypeValueList) {
            List<BusinessUnitValue> list1 = new ArrayList<BusinessUnitValue>();
            for (BusinessUnitValue b : s.getBusinessUnitValueList()) {
                List<LocationValue> list2 = new ArrayList<LocationValue>();
                for (LocationValue l : b.getLocationValueList()) {
                    if (s.getServiceType().getId() == serviceTypeId.intValue()
                            && b.getBusinessUnit().getId().intValue() == businessUnitId.intValue()) {
                        if (param2) {
                            l.setSelected(true);
                            b.setSelected(true);
                            s.setSelected(true);
                        } else {
                            l.setSelected(false);
                        }
                    }
                    list2.add(l);
                }
                b.setLocationValueList(list2);
                list1.add(b);
            }
            s.setBusinessUnitValueList(list1);
            list.add(s);
        }
        serviceTypeValueList = list;

    }

    public void locationListener() {
        Integer locationId = Integer.parseInt(JSFUtil.getRequestParameterMap("locationId"));
        Integer businessUnitId = Integer.parseInt(JSFUtil.getRequestParameterMap("businessUnitId"));
        Integer serviceTypeId = Integer.parseInt(JSFUtil.getRequestParameterMap("serviceTypeId"));
        Boolean param2 = !Boolean.parseBoolean(JSFUtil.getRequestParameterMap("locationStatus"));
        List<ServiceTypeValue> list = new ArrayList<ServiceTypeValue>();
        for (ServiceTypeValue s : serviceTypeValueList) {
            List<BusinessUnitValue> list1 = new ArrayList<BusinessUnitValue>();
            for (BusinessUnitValue b : s.getBusinessUnitValueList()) {
                List<LocationValue> list2 = new ArrayList<LocationValue>();
                for (LocationValue l : b.getLocationValueList()) {
                    if (s.getServiceType().getId() == serviceTypeId.intValue()
                            && b.getBusinessUnit().getId() == businessUnitId.intValue()
                            && l.getLocation().getId().intValue() == locationId.intValue()) {
                        if (param2) {
                            l.setSelected(true);
                            b.setSelected(true);
                            s.setSelected(true);
                        } else {
                            l.setSelected(false);
                        }
                    }
                    list2.add(l);
                }
                b.setLocationValueList(list2);
                list1.add(b);
            }
            s.setBusinessUnitValueList(list1);
            list.add(s);
        }
        serviceTypeValueList = list;
    }

    /**
     * @param groupUsersList the groupUsersList to set
     */
    public void setGroupUsersList(int[] groupUsersList) {
        this.groupUsersList = groupUsersList;
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

    public Map<String, Integer> getParentList() {
        return parentList;
    }

    public void setParentList(Map<String, Integer> parentList) {
        this.parentList = parentList;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<Acl> getAclList() {
        return aclList;
    }

    public void setAclList(List<Acl> aclList) {
        this.aclList = aclList;
    }

    public Acl getCustom() {
        return custom;
    }

    public void setCustom(Acl custom) {
        this.custom = custom;
    }

    public List<Acl> getCustomList() {
        return customList;
    }

    public void setCustomList(List<Acl> customList) {
        this.customList = customList;
    }

    public List<UserGroupAcl> getSelectedAclList() {
        return selectedAclList;
    }

    public void setSelectedAclList(List<UserGroupAcl> selectedAclList) {
        this.selectedAclList = selectedAclList;
    }

    public List<Acl> getSelectedCustomList() {
        return selectedCustomList;
    }

    public void setSelectedCustomList(List<Acl> selectedCustomList) {
        this.selectedCustomList = selectedCustomList;
    }

    public UserGroupAclDAO getUserGroupAclDAO() {
        return userGroupAclDAO;
    }

    public void setUserGroupAclDAO(UserGroupAclDAO userGroupAclDAO) {
        this.userGroupAclDAO = userGroupAclDAO;
    }

    public AclDAO getAclDAO() {
        return aclDAO;
    }

    public void setAclDAO(AclDAO aclDAO) {
        this.aclDAO = aclDAO;
    }

    public List<Acl> getAclListShow() {
        return aclListShow;
    }

    public void setAclListShow(List<Acl> aclListShow) {
        this.aclListShow = aclListShow;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ServiceTypeDAO getServiceTypeDAO() {
        return serviceTypeDAO;
    }

    public void setServiceTypeDAO(ServiceTypeDAO serviceTypeDAO) {
        this.serviceTypeDAO = serviceTypeDAO;
    }

    public LocationDAO getLocationDAO() {
        return locationDAO;
    }

    public void setLocationDAO(LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }

    public List<ServiceTypeValue> getServiceTypeValueList() {
        return serviceTypeValueList;
    }

    public void setServiceTypeValueList(List<ServiceTypeValue> serviceTypeValueList) {
        this.serviceTypeValueList = serviceTypeValueList;
    }

    public BusinessUnitDAO getBusinessUnitDAO() {
        return businessUnitDAO;
    }

    public void setBusinessUnitDAO(BusinessUnitDAO businessUnitDAO) {
        this.businessUnitDAO = businessUnitDAO;
    }

    public List<String> getRoleKnowledge() {
        return roleKnowledge;
    }

    public void setRoleKnowledge(List<String> roleKnowledge) {
        this.roleKnowledge = roleKnowledge;
    }

    public List<String> getRoleOperation() {
        return roleOperation;
    }

    public void setRoleOperation(List<String> roleOperation) {
        this.roleOperation = roleOperation;
    }

    public List<String> getRoleSale() {
        return roleSale;
    }

    public void setRoleSale(List<String> roleSale) {
        this.roleSale = roleSale;
    }
    
    //Tab
     public String getSelectedTab() {  
         return selectedTab;  
     }  
    
     public void setSelectedTab(String selectedTab) {  
         this.selectedTab = selectedTab;  
     }  
     
     public void aclSelectedListener() {
        String aclParentCode = (String) JSFUtil.getRequestParameterMap("aclParent");    //admin:view
        String chk = (String) JSFUtil.getRequestParameterMap("aclStatus");
        //aclList = aclDAO.findAclEntities();
        List<Acl> list1 = new ArrayList<Acl>();
        for (Acl acl : aclList) {
            if(acl.getParentCode() != null) {
                if(acl.getParentCode().getCode().equals(aclParentCode)){  //Set Child Step 1 
                    if(chk.equals("true")) {
                        acl.setSelected(false);
                    } else {
                        acl.setSelected(true);
                    }
                }
                if(acl.getParentCode().getParentCode() != null) {       //Set Child Step 2 or Custom 
                    if(acl.getParentCode().getParentCode().getCode().equals(aclParentCode)){
                        if(chk.equals("true")) {
                            acl.setSelected(false);
                        } else {
                            acl.setSelected(true);
                        }
                    } else if(acl.getParentCode().getParentCode().getParentCode()  != null) {
                                if(acl.getParentCode().getParentCode().getParentCode().getCode().equals(aclParentCode)){
                                    if(chk.equals("true")) {
                                        acl.setSelected(false);
                                    } else {
                                        acl.setSelected(true);
                                    }
                                }
                            }
                }
            }
            if(chk.equals("false")) {    
                if(acl.getParentCode() != null){
                    if(acl.getCode().equals(aclParentCode)) 
                        acl.getParentCode().setSelected(true);
                    
                    if(acl.getParentCode().getParentCode() != null)
                        if(acl.getCode().equals(aclParentCode)) 
                            acl.getParentCode().getParentCode().setSelected(true);
                }
            }
            
            list1.add(acl);
        }
        aclList = list1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLicenseGroup() {
        return licenseGroup;
    }

    public void setLicenseGroup(String licenseGroup) {
        this.licenseGroup = licenseGroup;
    }

    public Integer getCtiAdapterID() {
        return ctiAdapterID;
    }

    public void setCtiAdapterID(Integer ctiAdapterID) {
        this.ctiAdapterID = ctiAdapterID;
    }

    public Map<String, Integer> getCtiAdapterList() {
        return ctiAdapterList;
    }

    public void setCtiAdapterList(Map<String, Integer> ctiAdapterList) {
        this.ctiAdapterList = ctiAdapterList;
    }

    public CtiAdapterDAO getCtiAdapterDAO() {
        return ctiAdapterDAO;
    }

    public void setCtiAdapterDAO(CtiAdapterDAO ctiAdapterDAO) {
        this.ctiAdapterDAO = ctiAdapterDAO;
    }
     
}
