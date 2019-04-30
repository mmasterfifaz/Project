package com.maxelyz.core.controller.front.todolist;

import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.ActivityDAO;
import com.maxelyz.core.model.dao.ContactCaseDAO;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import java.util.*;

import com.maxelyz.core.model.dao.front.todolist.TodoListDAO;
import com.maxelyz.core.model.dao.ContactCaseWorkflowLogDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.core.model.value.front.todolist.IncomingCaseAcceptanceValue;
import com.maxelyz.core.model.value.front.todolist.ToDoListValue;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.JSFUtil;
/**
 *
 * @author Niranriths
 */
@ManagedBean (name="caseAcceptanceController")
//@RequestScoped
@ViewScoped
public class CaseAcceptanceController extends BaseController implements Serializable {

    private IncomingCaseAcceptanceValue incomingCaseAcceptance;
    private List<ToDoListValue> list;
    private Activity activity;
    private CustomerInfoValue customerInfoValue;
    private List<ActivityAttachment> activityAttachmentList = new ArrayList<ActivityAttachment>();
    
    @ManagedProperty(value = "#{todoListDAO}")
    private TodoListDAO todoListDAO;
    @ManagedProperty(value = "#{activityDAO}")
    private ActivityDAO activityDAO;
    @ManagedProperty(value = "#{contactCaseDAO}")
    private ContactCaseDAO contactCaseDAO;
    @ManagedProperty(value="#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
    @ManagedProperty(value="#{contactCaseWorkflowLogDAO}")
    private ContactCaseWorkflowLogDAO contactCaseWorkflowLogDAO;

    private Integer activityId;
    private String remark;
    private Integer contactCaseId;
    private String message="";
    private String acceptStatus=null;
    private Boolean chkAccept=false;
    private static String ACCEPT = "/front/customerHandling/caseHandling.xhtml";
    private static String REJECT = "incoming.jsf";
    private static String DUPACCEPT = "duplicateAcceptance.xhtml";

    @PostConstruct
    public void initialize() {
        remark = "";
        message = "";
        if (null != activityId && activityId != 0) {
            try {
                TodoListDAO dao = getTodoListDAO();
                incomingCaseAcceptance = dao.findIncomingCaseAcceptanceValue(activityId);
                activity = activityDAO.findActivity(activityId);
                activityAttachmentList = activityDAO.findActivityAttachment(activityId);
                if(activity.getReceiveStatus() == null) {
                    acceptStatus = null;
                } else {
                    acceptStatus = activity.getReceiveStatus() ;
                }
            } catch (Exception e) {
                System.out.println("Execption" + e.getMessage());
            }
        } else {
            incomingCaseAcceptance = new IncomingCaseAcceptanceValue();
        }
        if(acceptStatus != null) {
            JSFUtil.redirect("duplicateAcceptance.jsf");
        }
    }

    public void caseListener() {
        activityId = Integer.parseInt(getRequest("selectedID"));
        initialize();
    }

    public synchronized String saveActionAccept() {
        try {
            if (activityId != null && activityId != 0) {                
//                Activity chkRealTime = activityDAO.findActivity(activityId);
                activity = activityDAO.findActivity(activityId);
                Users userRecieve = JSFUtil.getUserSession().getUsers();
                String userRecieveName = userRecieve.getName();
                if(userRecieve.getSurname() != null && !userRecieve.getSurname().equals(""))
                    userRecieveName += " " + userRecieve.getSurname();
                if(activity.getReceiveStatus() == null) {
                    activity.setRemark(remark);
                    activity.setReceiveStatus("accepted");
                    activity.setUserReceiverId(userRecieve);
                    activity.setReceiverName(userRecieveName);
                    activity.setAcceptDate(new Date());
                    activityDAO.editAcceptance(activity);

                    if(activity.getContactCase().getWorkflow() != null && activity.getContactCase().getWorkflow()) {
                        ContactCaseWorkflowLog contactCaseWorkflowLog = contactCaseWorkflowLogDAO.findContactCaseWorkflowLogByContactCase(activity.getContactCase().getId());
                        contactCaseWorkflowLog.setAcceptDate(new Date());
                        contactCaseWorkflowLog.setAcceptStatus("accepted");
                        contactCaseWorkflowLog.setRemark(remark);
                        contactCaseWorkflowLog.setReceiveUsers(userRecieve);
                        contactCaseWorkflowLog.setReceiveUserName(userRecieve.getName());
                        contactCaseWorkflowLog.setReceiveUserSurname(userRecieve.getSurname());
                        contactCaseWorkflowLogDAO.editAcceptanceWorkflowLog(contactCaseWorkflowLog);
                    }

                    Integer selectedId = activity.getContactCase().getCustomerId().getId();
                    if (selectedId != null || selectedId != 0) {
                        customerInfoValue = getCustomerHandlingDAO().findCustomerHandling(selectedId);
                        JSFUtil.getUserSession().setCustomerDetail(customerInfoValue);
                    } else {

                    }
                    
                } else {
                    return DUPACCEPT;
                }
            } 
        } catch (Exception e) {
        }
        return ACCEPT;
    }
   
    public synchronized String saveActionRejectListener(ActionEvent event) {
        if(remark.equals("")) {
            message = "Remark is require";
            return null;
        } else {
            try {
                message = "";
                if (activityId != null && activityId != 0) {
                    activity = activityDAO.findActivity(activityId);
                    Users userRecieve = JSFUtil.getUserSession().getUsers();
                    String userRecieveName = userRecieve.getName();
                    if(userRecieve.getSurname() != null && !userRecieve.getSurname().equals(""))
                        userRecieveName += " " + userRecieve.getSurname();
                    if(activity.getReceiveStatus() == null) {   
                        activity.setRemark(remark);
                        activity.setReceiveStatus("reject");
                        activity.setUserReceiverId(userRecieve);
                        activity.setReceiverName(userRecieveName);
                        activity.setAcceptDate(new Date());
                        activityDAO.editAcceptance(activity);

                        if(activity.getContactCase().getWorkflow() != null && activity.getContactCase().getWorkflow()) {
                            ContactCaseWorkflowLog contactCaseWorkflowLog = contactCaseWorkflowLogDAO.findContactCaseWorkflowLogByContactCase(activity.getContactCase().getId());
                            contactCaseWorkflowLog.setAcceptDate(new Date());
                            contactCaseWorkflowLog.setAcceptStatus("reject");
                            contactCaseWorkflowLog.setRemark(remark);
                            contactCaseWorkflowLog.setReceiveUsers(userRecieve);
                            contactCaseWorkflowLog.setReceiveUserName(userRecieve.getName());
                            contactCaseWorkflowLog.setReceiveUserSurname(userRecieve.getSurname());
                            contactCaseWorkflowLogDAO.editAcceptanceWorkflowLog(contactCaseWorkflowLog);

                            // chaget contact case workflow
                            ContactCase contactCase = contactCaseDAO.findContactCase(activity.getContactCase().getId());
                            contactCase.setWorkflow(Boolean.FALSE);
                            contactCaseDAO.edit(contactCase);
                        }
                        
                    } else {
                        return DUPACCEPT;
                    }
                }
            } catch (Exception e) {
            }
            return REJECT;
        } 
    }

    /**
     * @return the todoListDAO
     */
    public TodoListDAO getTodoListDAO() {
        return todoListDAO;
    }

    /**
     * @param todoListDAO the todoListDAO to set
     */
    public void setTodoListDAO(TodoListDAO todoListDAO) {
        this.todoListDAO = todoListDAO;
    }

    /**
     * @return the incomingCaseAcceptance
     */
    public IncomingCaseAcceptanceValue getIncomingCaseAcceptance() {
        return incomingCaseAcceptance;
    }

    /**
     * @param incomingCaseAcceptance the incomingCaseAcceptance to set
     */
    public void setIncomingCaseAcceptance(IncomingCaseAcceptanceValue incomingCaseAcceptance) {
        this.incomingCaseAcceptance = incomingCaseAcceptance;
    }

    /**
     * @return the list
     */
    public List<ToDoListValue> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<ToDoListValue> list) {
        this.list = list;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ActivityDAO getActivityDAO() {
        return activityDAO;
    }

    public void setActivityDAO(ActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }

    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }

    public ContactCaseWorkflowLogDAO getContactCaseWorkflowLogDAO() {
        return contactCaseWorkflowLogDAO;
    }

    public void setContactCaseWorkflowLogDAO(ContactCaseWorkflowLogDAO contactCaseWorkflowLogDAO) {
        this.contactCaseWorkflowLogDAO = contactCaseWorkflowLogDAO;
    }

    public CustomerInfoValue getCustomerInfoValue() {
        return customerInfoValue;
    }

    public void setCustomerInfoValue(CustomerInfoValue customerInfoValue) {
        this.customerInfoValue = customerInfoValue;
    }

    public Integer getContactCaseId() {
        return contactCaseId;
    }

    public void setContactCaseId(Integer contactCaseId) {
        this.contactCaseId = contactCaseId;
    }

    public List<ActivityAttachment> getActivityAttachmentList() {
        return activityAttachmentList;
    }

    public void setActivityAttachmentList(List<ActivityAttachment> activityAttachmentList) {
        this.activityAttachmentList = activityAttachmentList;
    }

    public ContactCaseDAO getContactCaseDAO() {
        return contactCaseDAO;
    }

    public void setContactCaseDAO(ContactCaseDAO contactCaseDAO) {
        this.contactCaseDAO = contactCaseDAO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAcceptStatus() {
        return acceptStatus;
    }

    public void setAcceptStatus(String acceptStatus) {
        this.acceptStatus = acceptStatus;
    }
    
}
