/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.dao.WorkflowRuleDAO;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.model.entity.WorkflowRule;
import com.maxelyz.core.model.entity.WorkflowRuleDetail;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

@ManagedBean (name="workflowRuleEditController")
//@RequestScoped
@ViewScoped
public class WorkflowRuleEditController implements Serializable{
    private static Logger log = Logger.getLogger(WorkflowRuleEditController.class);
    private static String REDIRECT_PAGE = "workflowrule.jsf";
    private static String SUCCESS = "workflowrule.xhtml?faces-redirect=true";
    private static String FAILURE = "workflowruleedit.xhtml";
    
    private WorkflowRule workflowRule;
    private List<WorkflowRuleDetail> workflowRuleDetailList;
    private String message;
    private String messageDup;
    private String mode;
    private String keyword;
    private int no;
    private List<Users> users;
    
    @ManagedProperty(value = "#{workflowRuleDAO}")
    private WorkflowRuleDAO workflowRuleDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
        
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:workflowrule:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        keyword = ""; 
        messageDup = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            workflowRule = new WorkflowRule();
            workflowRule.setEnable(Boolean.TRUE);
            workflowRule.setStatus(Boolean.TRUE);
            workflowRuleDetailList = new ArrayList<WorkflowRuleDetail>();
            for(int i = 0; i < 1; i++) {
                WorkflowRuleDetail wk = new WorkflowRuleDetail();
                wk.setSeqNo(i+1);
                wk.setWorkflowRule(workflowRule);
                workflowRuleDetailList.add(wk);
            }
        } else {
            mode = "edit";
            workflowRule = this.getWorkflowRuleDAO().findWorkflowRule(new Integer(selectedID));
            if(workflowRule == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
                return;
            }
            workflowRuleDetailList = (List<WorkflowRuleDetail>) workflowRule.getWorkflowRuleDetailCollection();
        }
    }
        
    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:workflowrule:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:workflowrule:edit");
       }
    }
    
    public boolean checkWorkRule() {
        for(WorkflowRuleDetail obj: workflowRuleDetailList) {
            if(obj.getUser() == null) {
//                chkUser = false;
                obj.setMessage("Delegate user is required");
                return false;
            } else
                obj.setMessage("");
        }
        return true;
    }

    public String saveAction() {
        messageDup = "";
        if(checkWorkRule()) {
            if(checkName(workflowRule)) {
                try {
                    String username = JSFUtil.getUserSession().getUserName();
                    Date now = new Date();
                    if(mode.equals("add")) {
                        workflowRule.setCreateBy(username);
                        workflowRule.setCreateDate(now);
                        workflowRule.setWorkflowRuleDetailCollection(workflowRuleDetailList);
                        this.getWorkflowRuleDAO().create(workflowRule);
                    } else {
                        workflowRule.setUpdateBy(username);
                        workflowRule.setUpdateDate(now);
                        workflowRule.setWorkflowRuleDetailCollection(workflowRuleDetailList);
                        this.getWorkflowRuleDAO().editWorkflowRule(workflowRule);
                    }
                } catch (Exception e) {
                    log.error(e);
                    message = e.getMessage();
                    return FAILURE;
                }
                return SUCCESS;
            } else {
                messageDup = " Name is already taken";
                return null;
            }
        } else {
             return null;
        }
    }
    
    public Boolean checkName(WorkflowRule workflowRule) {
        String name = workflowRule.getName();
        Integer id=0; 
        if(workflowRule.getId() != null) 
            id = workflowRule.getId();
            
        WorkflowRuleDAO dao = getWorkflowRuleDAO();
        Integer cnt = dao.checkName(name, id);
                    
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    public String backAction() {
        return SUCCESS;
    }
    
    public void selectUserListener() {
        int seqNo = Integer.parseInt(JSFUtil.getRequestParameterMap("seqNo"));
        String userId = (String) JSFUtil.getRequestParameterMap("userId");
        if (!userId.isEmpty()) {
            Users userTmp = usersDAO.findUsers(Integer.parseInt(userId));
            WorkflowRuleDetail wd = workflowRuleDetailList.get(seqNo-1);
            if(userTmp.getEmail() != null && !userTmp.getEmail().equals("")) {
                wd.setEmailTo(userTmp.getEmail());
            } else {
                wd.setEmailTo("");
            }
            wd.setUser(userTmp);
            wd.setMessage("");
            workflowRuleDetailList.set(seqNo-1, wd);
        }
    }
    
    public void selectUserNo() {
        no = Integer.parseInt(JSFUtil.getRequestParameterMap("seqNo"));
        if(users != null && !users.isEmpty())
            users.clear();
        UsersDAO dao = getUsersDAO();
        users = dao.findUsersDelegateByName(keyword); //, workflowRuleDetailList);
    }
    
    //Listener
    private void reCalculateSeqNo() {
        int seqNo=0;
        for(WorkflowRuleDetail w: workflowRuleDetailList) {
            w.setSeqNo(++seqNo);
        }
    }  
    
    public void addRuleListener(ActionEvent event) {
        boolean chkUser = true;
        for(WorkflowRuleDetail obj: workflowRuleDetailList) {
            if(obj.getUser() == null) {
                chkUser = false;
                obj.setMessage("Delegate user is required");
            } else
                obj.setMessage("");
        }
        if(chkUser) {
            int size = workflowRuleDetailList.size();
            WorkflowRuleDetail wd = new WorkflowRuleDetail();
            wd.setSeqNo(size+1);
            wd.setWorkflowRule(workflowRule);
            workflowRuleDetailList.add(wd);
        }
    }
            
    public void insertRuleListener(ActionEvent event) {
        boolean chkUser = true;
        for(WorkflowRuleDetail obj: workflowRuleDetailList) {
            if(obj.getUser() == null) {
                chkUser = false;
                obj.setMessage("Delegate user is required");
            } else
                obj.setMessage("");
        }
        if(chkUser) {
            int seqNo = Integer.parseInt(JSFUtil.getRequestParameterMap("seqNo"));
            WorkflowRuleDetail wd = new WorkflowRuleDetail();
            wd.setWorkflowRule(workflowRule);
            workflowRuleDetailList.add(seqNo-1,wd);
            reCalculateSeqNo();   
        }
    }
  
    public void deleteRuleListener(ActionEvent event) {
        int seqNo = Integer.parseInt(JSFUtil.getRequestParameterMap("seqNo"));
        workflowRuleDetailList.remove(seqNo-1);
        reCalculateSeqNo();
    }
    
    public void popupSearchAction(ActionEvent event) {
        UsersDAO dao = getUsersDAO();
        users = dao.findUsersDelegateByName(keyword); //, workflowRuleDetailList);
    }

    // Get/Set Properties
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public WorkflowRule getWorkflowRule() {
        return workflowRule;
    }

    public void setWorkflowRule(WorkflowRule workflowRule) {
        this.workflowRule = workflowRule;
    }

    public WorkflowRuleDAO getWorkflowRuleDAO() {
        return workflowRuleDAO;
    }

    public void setWorkflowRuleDAO(WorkflowRuleDAO workflowRuleDAO) {
        this.workflowRuleDAO = workflowRuleDAO;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public List<WorkflowRuleDetail> getWorkflowRuleDetailList() {
        return workflowRuleDetailList;
    }

    public void setWorkflowRuleDetailList(List<WorkflowRuleDetail> workflowRuleDetailList) {
        this.workflowRuleDetailList = workflowRuleDetailList;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

}
