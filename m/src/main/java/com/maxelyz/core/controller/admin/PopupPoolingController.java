/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.AssignmentDAO;
import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.entity.Assignment;
import com.maxelyz.core.model.entity.AssignmentPooling;
import com.maxelyz.core.model.entity.Campaign;
import com.maxelyz.utils.JSFUtil;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import javax.faces.bean.*;
import javax.annotation.PostConstruct;
import com.maxelyz.utils.SecurityUtil;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.event.ActionEvent;

/**
 *
 * @author vee
 */

@ManagedBean(name="popupPoolingController")
//@RequestScoped
@ViewScoped
public class PopupPoolingController {
    
    private static Logger log = Logger.getLogger(PopupPoolingController.class);

    private List<AssignmentPooling> poolingList;
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //user from checkbox
    private AssignmentPooling selectedPooling;
     
    @ManagedProperty(value = "#{assignmentDAO}")
    private AssignmentDAO assignmentDAO;

    @PostConstruct
    public void initialize() {
    }
    
    public void poolingListener(ActionEvent event){
           String assignmentID  = (String) JSFUtil.getRequestParameterMap("selectId");
        
            AssignmentDAO  dao = getAssignmentDAO();
            poolingList = dao.findAssignmentPoolingByAssignment(new Integer(assignmentID));
    }
            
    public void savePoolingAction(){
        AssignmentDAO  dao = getAssignmentDAO();
        try{
            for(Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if(item.getValue()) {
                    selectedPooling = dao.findAssignmentPooling(item.getKey());
                    selectedPooling.setEnable(false);
                    dao.editAssignmentPooling(selectedPooling);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
            
            
    //List to UI-------------------------------------------------------
    public List<AssignmentPooling> getPoolingList() {
        return poolingList;
    }

    public void setPoolingList(List<AssignmentPooling> poolingList) {
        this.poolingList = poolingList;
    }

    //Managed Properties-------------------------------------------------------

    public AssignmentDAO getAssignmentDAO() {
        return assignmentDAO;
    }

    public void setAssignmentDAO(AssignmentDAO assigmentDAO) {
        this.assignmentDAO = assigmentDAO;
    }
    
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }
    
}
