/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.controller.admin;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.entity.Users;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import org.apache.log4j.Logger;

import com.maxelyz.social.model.entity.*;
import com.maxelyz.social.model.dao.*;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.model.SelectItem;

/**
 *
 * @author Prawait
 */
@ManagedBean
@ViewScoped
public class SocialServiceDetailController {
    
    private static Logger logger = Logger.getLogger(SocialServiceDetailController.class);
    private String SUCCESS = "service.xhtml?faces-redirect=true";
    private Integer step = 0;
    private SoService soService;
    private SoServiceKeyword soServiceKeyword;
    private List<SoAccount> soAccountList;
    private List<Integer> selectedSoAccount = new ArrayList<Integer>();
    private List<Integer> selectedUsers = new ArrayList<Integer>();
    private String selectedUsersString;
    private List<SoAccount> selectedSoAccountList;
    private SelectItem[] servicePriorityList;
    private SelectItem[] messagePriorityList;
    private String[] messagePriorityString;
    private String messagePriorityString2;
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private Map<String, Integer> userGroupList;
    private Integer userGroupId;
    private List<Users> userList = new ArrayList<Users>();
    private List<String> keywordList;
    private String strKeyword;
    private Integer matchType = 1;
    private Collection<SoServiceKeyword> SoServiceKeywords; 
    
    
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{soAccountDAO}")
    private SoAccountDAO soAccountDAO;
    @ManagedProperty(value = "#{soServiceDAO}")
    private SoServiceDAO soServiceDAO;
    
    @PostConstruct
    public void initialize() {
        step = 1;
        soAccountList = soAccountDAO.findSoAccountList();
        userGroupList = userGroupDAO.getUserGroupList();
        Integer soServiceId = 0;
        if(JSFUtil.getRequestParameterMap("soServiceId") != null) {
            soServiceId = Integer.parseInt(JSFUtil.getRequestParameterMap("soServiceId"));
            if(soServiceId != 0){
                soService = soServiceDAO.findSoService(soServiceId);
                soService.getSoAccountCollection().toString();
                soService.getSoServiceKeywordCollection().toString();
                soService.getUsersCollection().toString();
                userList = (List<Users>) soService.getUsersCollection();
            }else{
                soService = new SoService();
            }
            
            if(soService.getSoAccountCollection() != null && !soService.getSoAccountCollection().isEmpty()){
                for(SoAccount soAccount : soService.getSoAccountCollection()){
                    selectedSoAccount.add(soAccount.getId());
                }
            }
            
            if(soService.getMessagePriority() != null && !soService.getMessagePriority().isEmpty()){
                StringTokenizer stk = new StringTokenizer(soService.getMessagePriority(), ",");
                int c = stk.countTokens();
                messagePriorityString = new String[c];
                int i = 0;
                while (stk.hasMoreTokens()) {
                    String str = stk.nextToken();
                    messagePriorityString[i] = str;
                    i++;
                }
            }

//            if(soService.getSoServiceKeywordCollection() != null && !soService.getSoServiceKeywordCollection().isEmpty()){
//                SoServiceKeywords = soService.getSoServiceKeywordCollection();
//            }
            
//            if(soService.getUsersCollection()!= null && !soService.getUsersCollection().isEmpty()){
//                userList = soService.getUsersCollection();
//            }
            
            
        } else {
            soService = new SoService();
            soService.setSoServiceKeywordCollection(new ArrayList<SoServiceKeyword>());
            soService.setUsersCollection(new ArrayList<Users>());
            if(soService.getServicePriority() == 0) soService.setServicePriority(3);
            if(soService.getSentimental() == null || soService.getSentimental().isEmpty()) soService.setSentimental("neutral"); //positive,neutral,negative
            if(soService.getRoutingType() == null || soService.getRoutingType().isEmpty()) soService.setRoutingType("roundrobin"); //roundrobin,average,pooling
            
        }
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
        init3();
    }
    
    public void step3to4(){
        step = 4;
    }
    
    public void step4to3(){
        step = 3;
    }
    
    public void step3to2(){
        step = 2;
    }
    
    public void step2to1(){
        step = 1;
    }
    
    private void removeSoServiceAccount(){
        try{
            soServiceDAO.removeSoAccountBySoServiceId(soService.getId());
         }catch(Exception e){
            logger.error(e);
        }
    }
    
    private void removeSoServiceKeyword(){
        try{
            soServiceDAO.removeSoServiceKeywordBySoServiceId(soService.getId());
         }catch(Exception e){
            logger.error(e);
        }
    }
    
    private void removeSoServiceUser(){
        try{
            soServiceDAO.removeSoServiceUserBySoServiceId(soService.getId());
         }catch(Exception e){
            logger.error(e);
        }
    }
    
    public String save(){
        try{
            this.removeSoServiceAccount();
            this.removeSoServiceKeyword();
            this.removeSoServiceUser();
            soService.setSoAccountCollection(selectedSoAccountList);
            String strValue = "";
            if(messagePriorityString != null && messagePriorityString.length != 0){
                for(String str : messagePriorityString){
                    strValue += str + ",";
                }
                if(!strValue.isEmpty()) strValue = strValue.substring(0, strValue.length() - 1);
            }
            soService.setMessagePriority(strValue);

            //soService.setUsersCollection(userList);
            if(soService.getId() == null){
                soService.setEnable(Boolean.TRUE);
                soService.setStatus(Boolean.TRUE);
                soService.setCreateBy(JSFUtil.getUserSession().getUserName());
                soService.setCreateDate(new Date());
                soServiceDAO.create(soService);
            } else {
                soService.setUpdateBy(JSFUtil.getUserSession().getUserName());
                soService.setUpdateDate(new Date());
                soServiceDAO.edit(soService);
            }
            if(userList != null && !userList.isEmpty()){
                for(Users u : userList){
                    soServiceDAO.insertUser(soService.getId(), u.getId());
                }
            }
        
        }catch(Exception e){
            logger.error(e);
        }
        return SUCCESS;
    }
    
    public void selectUserActionListener(){
        StringTokenizer stk = new StringTokenizer(selectedUsersString,","); 
        //userList = (List<Users>) soService.getUsersCollection();
        if(userList == null || userList.isEmpty()){
            userList = new ArrayList<Users>();
        }
        boolean exist;
        while (stk.hasMoreTokens()) {
            Integer uId = Integer.parseInt(stk.nextToken());
            exist = false;
            for(Users user : userList){
                if(user.getId().intValue() == uId.intValue()){
                    exist = true;
                    break;
                }
            }
            if(!exist){
                Users u = usersDAO.findUsers(uId);
                userList.add(u);
            }
        }
        //soService.setUsersCollection(userList);
    }
    
    public void deleteUserActionListener(){
        try {
            
            Collection<Users> userListTmp = userList;
            userList = new ArrayList<Users>();
            boolean exist;
            for (Users user : userListTmp) {
                exist = false;
                for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                    if (item.getValue() && user.getId().intValue() == item.getKey().intValue()) {
                        exist = true;
                        break;
                    }
                }
                if(!exist){
                    userList.add(user);
                }
            }
            //soService.setUsersCollection(userList);
        } catch (Exception e) {
            logger.error(e);
        }
    }
    
    public void findUserListListener(){
        if(userGroupId != null && userGroupId != 0){
            userList = usersDAO.findUsersEntitiesByUserGroup(userGroupId);
        }
    }
    
    public void addKeywordActionListener(){
        String str1 = "";
        String strAll = "";
        if(!strKeyword.trim().isEmpty()){
            StringTokenizer stk = new StringTokenizer(strKeyword,"\r\n");
            while (stk.hasMoreTokens()) {
                str1 = stk.nextToken();
                if(!str1.isEmpty()){
                    strAll += str1 + ",";
                }
            }
            if(!strAll.isEmpty()){
                strAll = strAll.substring(0, strAll.length() - 1);
            }

            Collection<SoServiceKeyword> soServiceKeywords = null;
            if(soService.getId() == null || soService.getId() == 0){
                if(soService.getSoServiceKeywordCollection() == null || soService.getSoServiceKeywordCollection().isEmpty()){
                    soServiceKeywords = new ArrayList<SoServiceKeyword>();
                }else{
                    soServiceKeywords = soService.getSoServiceKeywordCollection();
                }
            }else{
                soServiceKeywords = soService.getSoServiceKeywordCollection();
            }
            Integer i = soServiceKeywords.size() + 1;
            soServiceKeyword = new SoServiceKeyword();
            soServiceKeyword.setKeyword(strAll);
            if(matchType == 1){
                soServiceKeyword.setMatchAll(false);
            }else if(matchType == 2){
                soServiceKeyword.setMatchAll(true);
            }
            soServiceKeyword.setOrderId(i);
            soServiceKeyword.setSoService(soService);

            soServiceKeywords.add(soServiceKeyword);

            soService.setSoServiceKeywordCollection(soServiceKeywords);

            strKeyword = "";
            matchType = 1;
        }
    }
    
    public void removeKeyword(){
        Integer orderId = JSFUtil.getRequestParameterMap("orderId") != null ? Integer.parseInt(JSFUtil.getRequestParameterMap("orderId")) : 0 ;
        int i = 0;
        Collection tmp = new ArrayList<SoServiceKeyword>();
        for(SoServiceKeyword s : soService.getSoServiceKeywordCollection()){
            i++;
            if(orderId.intValue() != i){
                tmp.add(s);
            }
        }
        soService.setSoServiceKeywordCollection(tmp);
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public SoService getSoService() {
        return soService;
    }

    public void setSoService(SoService soService) {
        this.soService = soService;
    }

    public List<SoAccount> getSoAccountList() {
        return soAccountList;
    }

    public void setSoAccountList(List<SoAccount> soAccountList) {
        this.soAccountList = soAccountList;
    }

    public SoAccountDAO getSoAccountDAO() {
        return soAccountDAO;
    }

    public void setSoAccountDAO(SoAccountDAO soAccountDAO) {
        this.soAccountDAO = soAccountDAO;
    }

    public List<Integer> getSelectedSoAccount() {
        return selectedSoAccount;
    }

    public void setSelectedSoAccount(List<Integer> selectedSoAccount) {
        this.selectedSoAccount = selectedSoAccount;
    }

    public SelectItem[] getServicePriorityList() {
        servicePriorityList = new SelectItem[5];
        servicePriorityList[0] = new SelectItem(1, "P1:Lowest");
        servicePriorityList[1] = new SelectItem(2, "P2:Low");
        servicePriorityList[2] = new SelectItem(3, "P3:Medium");
        servicePriorityList[3] = new SelectItem(4, "P4:High");
        servicePriorityList[4] = new SelectItem(5, "P5:Highest");
        
        return servicePriorityList;
    }

    public void setServicePriorityList(SelectItem[] servicePriorityList) {
        this.servicePriorityList = servicePriorityList;
    }

    public SelectItem[] getMessagePriorityList() {
        messagePriorityList = new SelectItem[4];
        messagePriorityList[0] = new SelectItem("1", "Low");
        messagePriorityList[1] = new SelectItem("2", "Medium");
        messagePriorityList[2] = new SelectItem("3", "High");
        messagePriorityList[3] = new SelectItem("4", "Immediately");
        return messagePriorityList;
    }

    public void setMessagePriorityList(SelectItem[] messagePriorityList) {
        this.messagePriorityList = messagePriorityList;
    }

    public String[] getMessagePriorityString() {
        return messagePriorityString;
    }

    public void setMessagePriorityString(String[] messagePriorityString) {
        this.messagePriorityString = messagePriorityString;
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

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
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

    public SoServiceDAO getSoServiceDAO() {
        return soServiceDAO;
    }

    public void setSoServiceDAO(SoServiceDAO soServiceDAO) {
        this.soServiceDAO = soServiceDAO;
    }

    public List<String> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<String> keywordList) {
        this.keywordList = keywordList;
    }

    public String getStrKeyword() {
        return strKeyword;
    }

    public void setStrKeyword(String strKeyword) {
        this.strKeyword = strKeyword;
    }

    public Integer getMatchType() {
        return matchType;
    }

    public void setMatchType(Integer matchType) {
        this.matchType = matchType;
    }

    public SoServiceKeyword getSoServiceKeyword() {
        return soServiceKeyword;
    }

    public void setSoServiceKeyword(SoServiceKeyword soServiceKeyword) {
        this.soServiceKeyword = soServiceKeyword;
    }

    public List<SoAccount> getSelectedSoAccountList() {
        selectedSoAccountList = new ArrayList<SoAccount>();
        if(selectedSoAccount != null && !selectedSoAccount.isEmpty()){
            for(Integer id : selectedSoAccount){
                SoAccount sa = soAccountDAO.findSoAccount(id);
                selectedSoAccountList.add(sa);
            }
        }
        return selectedSoAccountList;
    }

    public void setSelectedSoAccountList(List<SoAccount> selectedSoAccountList) {
        this.selectedSoAccountList = selectedSoAccountList;
    }

    public String getMessagePriorityString2() {
        String str = "";
        if(messagePriorityString != null){
            for(String s : messagePriorityString){
                int i = Integer.parseInt(s);
                switch (i) {
                case 1:
                    str += "Low, " ;
                    break;
                case 2:
                    str += "Medium, " ;
                    break;
                case 3:
                    str += "High, " ;
                    break;
                case 4:
                    str += "Immediately, " ;
                    break;
                default:
                    break;
                }
            }
        }
        if(!str.isEmpty()){
            str = str.substring(0, str.length() - 2);
        }
        messagePriorityString2 = str;
        return messagePriorityString2;
    }

    public void setMessagePriorityString2(String messagePriorityString2) {
        this.messagePriorityString2 = messagePriorityString2;
    }

    public List<Integer> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<Integer> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public String getSelectedUsersString() {
        return selectedUsersString;
    }

    public void setSelectedUsersString(String selectedUsersString) {
        this.selectedUsersString = selectedUsersString;
    }

    public Collection<SoServiceKeyword> getSoServiceKeywords() {
        return SoServiceKeywords;
    }

    public void setSoServiceKeywords(Collection<SoServiceKeyword> SoServiceKeywords) {
        this.SoServiceKeywords = SoServiceKeywords;
    }
}
