package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.ProspectlistSponsorDAO;
import com.maxelyz.core.model.entity.ProspectlistSponsor;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.*;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;

@ManagedBean
@ViewScoped
public class ProspectlistSponsorController implements Serializable {

    private static Logger log = Logger.getLogger(ProspectlistSponsorController.class);
    private static String REFRESH = "listsponsor.xhtml?faces-redirect=true";
    private static String EDIT = "listsponsoredit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<ProspectlistSponsor> prospectlistSponsor;
    private ProspectlistSponsor pros;
    private String keyword = "";
    private String message = "";
    @ManagedProperty(value = "#{prospectlistSponsorDAO}")
    private ProspectlistSponsorDAO prospectlistSponsorDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:marketinglistsource:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
               
        ProspectlistSponsorDAO dao = getProspectlistSponsorDAO();
        prospectlistSponsor = dao.findProspectlistSponsorEntities();
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:marketinglistsource:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:marketinglistsource:delete");
    }
    public List<ProspectlistSponsor> getList() {
        return getProspectlistSponsor();
    }
    
    public void searchActionListener(ActionEvent event){
        this.search();
    }

    public String searchAction() {
        this.search();
        return REFRESH;
    }
    
    public void search(){
        prospectlistSponsor = prospectlistSponsorDAO.findProspectlistSponsorByName(keyword);
    }

    public String deleteAction() throws Exception {
        ProspectlistSponsorDAO dao = getProspectlistSponsorDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    pros = dao.findProspectlistSponsor(item.getKey());
                    pros.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    pros.setUpdateDate(new Date());
                    pros.setEnable(false);
                    dao.edit(pros);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public String editAction() {
        return EDIT;
    }

    private List<ProspectlistSponsor> getProspectlistSponsor() {
        return prospectlistSponsor;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setProspectlistSponsor(List<ProspectlistSponsor> prospectlistSponsor) {
        this.prospectlistSponsor = prospectlistSponsor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ProspectlistSponsorDAO getProspectlistSponsorDAO() {
        return prospectlistSponsorDAO;
    }

    public void setProspectlistSponsorDAO(ProspectlistSponsorDAO prospectlistSponsorDAO) {
        this.prospectlistSponsorDAO = prospectlistSponsorDAO;
    }
}
