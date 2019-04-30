package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.VoiceFilePasswordDAO;
import com.maxelyz.core.model.entity.VoiceFilePassword;
import com.maxelyz.utils.SecurityUtil;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ManagedBean
@RequestScoped
public class ExportVoiceFilePasswordController {

    private static Logger log = Logger.getLogger(ExportVoiceFilePasswordController.class);
    private static String REFRESH = "exportvoicefilepassword.xhtml?faces-redirect=true";
    private static String EDIT = "exportvoicefilepasswordedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<VoiceFilePassword> dataList;
    private VoiceFilePassword voiceFilePassword;
    
    private String txtSearch = "";
    private String status = "1";
    
    @ManagedProperty(value = "#{voiceFilePasswordDAO}")
    private VoiceFilePasswordDAO voiceFilePasswordDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:exportvoicefilepassword:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        this.search();
        
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:exportvoicefilepassword:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:exportvoicefilepassword:delete");
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    voiceFilePassword = voiceFilePasswordDAO.findVoiceFilePassword(item.getKey());
                    voiceFilePassword.setEnable(false);
                    voiceFilePasswordDAO.edit(voiceFilePassword);
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
            dataList = voiceFilePasswordDAO.findByFilter("",status,1);
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

    public List<VoiceFilePassword> getDataList() {
        return dataList;
    }

    public void setDataList(List<VoiceFilePassword> dataList) {
        this.dataList = dataList;
    }

    public VoiceFilePassword getVoiceFilePassword() {
        return voiceFilePassword;
    }

    public void setVoiceFilePassword(VoiceFilePassword voiceFilePassword) {
        this.voiceFilePassword = voiceFilePassword;
    }

    public VoiceFilePasswordDAO getVoiceFilePasswordDAO() {
        return voiceFilePasswordDAO;
    }

    public void setVoiceFilePasswordDAO(VoiceFilePasswordDAO voiceFilePasswordDAO) {
        this.voiceFilePasswordDAO = voiceFilePasswordDAO;
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
