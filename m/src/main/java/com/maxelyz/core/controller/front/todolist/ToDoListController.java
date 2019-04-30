package com.maxelyz.core.controller.front.todolist;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.ContactCaseDAO;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import java.util.*;
import com.maxelyz.utils.SecurityUtil;
import com.maxelyz.core.model.dao.front.todolist.TodoListDAO;
import com.maxelyz.core.model.entity.ContactCase;
import com.maxelyz.core.model.value.front.todolist.ToDoListValue;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;

/**
 *
 * @author Niranriths
 */
@ManagedBean(name="toDoListController")
//@RequestScoped
@ViewScoped
public class ToDoListController extends BaseController{

    private static String REFRESH = "incoming.xhtml";
    private static String EDIT = "/front/customerHandling/caseHandling.xhtml";
        
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<ToDoListValue> incomingList;
    private List<ToDoListValue> pendingList;
    private String keyword="";
    private ToDoListValue selected;
    private Integer activityId;
    private Boolean rejectCase = false;
    private Boolean overdueClose = false;
    private Integer customerId;
    private CustomerInfoValue customerInfoValue;
   
    @ManagedProperty(value="#{todoListDAO}")
    private TodoListDAO todoListDAO;
    @ManagedProperty(value="#{contactCaseDAO}")
    private ContactCaseDAO contactCaseDAO;
    @ManagedProperty(value="#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
     
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("todolist:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        JSFUtil.getUserSession().setCustomerDetail(null);
        JSFUtil.getUserSession().setFirstPage("todolist");
        JSFUtil.getUserSession().setMainPage("todolist");
        this.pendingSearch();
    }
    
    public String editCase() {
        String selectedId = null;
        if (JSFUtil.getRequestParameterMap("customerId") != null && !JSFUtil.getRequestParameterMap("customerId").isEmpty()) {
            selectedId = JSFUtil.getRequestParameterMap("customerId");
            customerId = Integer.parseInt(selectedId);   
            customerInfoValue = customerHandlingDAO.findCustomerHandling(customerId);
            JSFUtil.getUserSession().setCustomerDetail(customerInfoValue);
        }
        return EDIT;
    }
    
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public TodoListDAO getTodoListDAO() {
        return todoListDAO;
    }

    public void setTodoListDAO(TodoListDAO todoListDAO) {
        this.todoListDAO = todoListDAO;
    }

    /**
     * @return the incomingList
     */
    public List<ToDoListValue> getIncomingList() {
            incomingList = todoListDAO.findIncomingValues(getLoginUser().getId());
        return incomingList;
    }

    /**
     * @param incomingList the incomingList to set
     */
    public void setIncomingList(List<ToDoListValue> incomingList) {
        this.incomingList = incomingList;
    }

    /**
     * @return the pendingList
     */
    public List<ToDoListValue> getPendingList() {
        /*
        List<ToDoListValue> toDoListValue = new ArrayList<ToDoListValue>();
        List<Integer> listContactCase = todoListDAO.findContactCasePending(getLoginUser().getId());
        for(Integer id : listContactCase){
            ContactCase cc = contactCaseDAO.findContactCase(id);
            ToDoListValue value = new ToDoListValue(cc);
            toDoListValue.add(value);
        }
//        pendingList = todoListDAO.findPendingValues(getLoginUser().getId());
        pendingList = toDoListValue;
        */
        return pendingList;
    }
    
    public void pendingSearchListener(ActionEvent action) {
        pendingSearch();   
    }
    
    public void pendingSearch() {
        List<ToDoListValue> toDoListValue = new ArrayList<ToDoListValue>();
        List<Integer> listContactCase = todoListDAO.findContactCasePending(getLoginUser().getId());
        
        for(Integer id : listContactCase){
            ContactCase cc = contactCaseDAO.findContactCase(id);
            ToDoListValue value = new ToDoListValue(cc);
            boolean add=false;
            
                String fname, lname;
                try {
                    fname = value.getContactCase().getCustomerId().getName().toLowerCase();
                } catch (Exception e) {
                    fname = "";
                }
                try {
                    lname = value.getContactCase().getCustomerId().getSurname().toLowerCase();
                } catch (Exception e) {
                    lname = "";
                }
                
                if ((fname+" "+lname).contains(keyword)) {   
                    if (this.getRejectCase()) {
                       if (value.getRejectFlag()) {
                           toDoListValue.add(value); 
                       }
                    } else {
                        toDoListValue.add(value); 
                    }
                }
            
        }
        pendingList = toDoListValue;
        
    }

    /**
     * @param pendingList the pendingList to set
     */
    public void setPendingList(List<ToDoListValue> pendingList) {
        this.pendingList = pendingList;
    }

    public ToDoListValue getSelected() {
        return selected;
    }

    public void setSelected(ToDoListValue selected) {
        this.selected = selected;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Boolean getOverdueClose() {
        return overdueClose;
    }

    public void setOverdueClose(Boolean overdueClose) {
        this.overdueClose = overdueClose;
    }

    public Boolean getRejectCase() {
        return rejectCase;
    }

    public void setRejectCase(Boolean rejectCase) {
        this.rejectCase = rejectCase;
    }

    public ContactCaseDAO getContactCaseDAO() {
        return contactCaseDAO;
    }

    public void setContactCaseDAO(ContactCaseDAO contactCaseDAO) {
        this.contactCaseDAO = contactCaseDAO;
    }
    
    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }

    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }
}
