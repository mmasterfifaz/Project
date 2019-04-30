package com.maxelyz.core.controller.admin;


//import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.NosaleReasonDAO;
//import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.NosaleReason;
import com.maxelyz.utils.JSFUtil;
//import com.maxelyz.core.service.SecurityService;
//import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DevTeam
 */
@ManagedBean
@ViewScoped
public class NosaleReasonController {
    private static Logger log = Logger.getLogger(NosaleReasonController.class);
    private static String REFRESH = "nosalereason.xhtml?faces-redirect=true";
    private static String EDIT = "nosalereasonedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<NosaleReason> nosaleReasonList;
    private NosaleReason nosaleReason;
    @ManagedProperty(value = "#{nosaleReasonDAO}")
    private NosaleReasonDAO nosaleReasonDAO;
    
    private String txtSearch;
    private String status;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:nosalereason:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        nosaleReasonList = this.getNosaleReasonDAO().findAllNosaleReason();
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:nosalereason:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:nosalereason:delete");
    }
    
   

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() throws Exception {
        NosaleReasonDAO dao = getNosaleReasonDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    nosaleReason = dao.findNosaleReason(item.getKey());
                    nosaleReason.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    nosaleReason.setUpdateDate(new Date());
                    nosaleReason.setEnable(false);
                    dao.edit(nosaleReason);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public void searchActionListener(ActionEvent event){
        
        this.search();
    }
    
    private void search(){
        try{
            nosaleReasonList = nosaleReasonDAO.findByFilter(txtSearch, status);
        }catch(Exception e){
            log.error(e);
        }
    
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<NosaleReason> getNosaleReasonList() {
        return nosaleReasonList;
    }

    public void setNosaleReasonList(List<NosaleReason> nosaleReasonList) {
        this.nosaleReasonList = nosaleReasonList;
    }

    public NosaleReason getNosaleReason() {
        return nosaleReason;
    }

    public void setNosaleReason(NosaleReason nosaleReason) {
        this.nosaleReason = nosaleReason;
    }

    public NosaleReasonDAO getNosaleReasonDAO() {
        return nosaleReasonDAO;
    }

    public void setNosaleReasonDAO(NosaleReasonDAO nosaleReasonDAO) {
        this.nosaleReasonDAO = nosaleReasonDAO;
    }

    public String getTxtSearch() {
        return txtSearch;
    }

    public void setTxtSearch(String txtSearch) {
        this.txtSearch = txtSearch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   
}


