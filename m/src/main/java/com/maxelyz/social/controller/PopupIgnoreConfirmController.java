/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.controller;

import com.maxelyz.social.model.dao.SoActivityLogDAO;
import com.maxelyz.social.model.dao.SoCaseTypeDAO;
import com.maxelyz.social.model.dao.SoIgnoreReasonDAO;
import com.maxelyz.social.model.dao.SoMessageDAO;
import com.maxelyz.social.model.entity.SoActivityLog;
import com.maxelyz.social.model.entity.SoActivityType;
import com.maxelyz.social.model.entity.SoCaseType;
import com.maxelyz.social.model.entity.SoIgnoreReason;
import com.maxelyz.social.model.entity.SoMessage;
import com.maxelyz.social.model.entity.SoMsgCasetypeMap;
import com.maxelyz.social.model.entity.SoMsgUserAssigntime;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import org.apache.log4j.Logger;

/**
 *
 * @author ukritj
 */
@ManagedBean
@ViewScoped
public class PopupIgnoreConfirmController {

    private Integer soMessageId;
    private static Logger log = Logger.getLogger(PopupIgnoreConfirmController.class);
    private Integer ignoreId;
    private String ignoreRemark;
    private SoMessage soMessage;
    private List<SoCaseType> soCaseTypeList;
    private Integer soCaseTypeId;
    private List<SoCaseType> soCaseTypeSubList;
    private Integer soCaseTypeSubId;
    
    private List<SoIgnoreReason> ignoreList;
    
    @ManagedProperty(value = "#{soActivityLogDAO}")
    private SoActivityLogDAO soActivityLogDAO;
    @ManagedProperty(value = "#{soIgnoreReasonDAO}")
    private SoIgnoreReasonDAO soIgnoreReasonDAO;
    @ManagedProperty(value = "#{soMessageDAO}")
    private SoMessageDAO soMessageDAO;
    @ManagedProperty(value = "#{soCaseTypeDAO}")
    private SoCaseTypeDAO soCaseTypeDAO;

    @PostConstruct
    public void initialize() {
        if (JSFUtil.getRequestParameterMap("soMessageId") != null) {
            soMessageId = Integer.parseInt(JSFUtil.getRequestParameterMap("soMessageId"));
            if(soMessageId != null){
                soMessage = soMessageDAO.findSoMessage(soMessageId);
            }
        }
        soCaseTypeList = soCaseTypeDAO.findSoCaseTypeStatus();
        soCaseTypeSubList = new ArrayList<SoCaseType>();
        //caseTypeIgnoreList = soCaseTypeDAO.findSoCaseTypeEntities();
        ignoreList = soMessageDAO.findAllSoIgnoreReason();
    }
    
    public void ignoreAction() {
        String status = "";
        try {
            //save SoActivityLog
            SoActivityLog soActivityLog = new SoActivityLog();
            soActivityLog.setSoMessage(soMessage);
            soActivityLog.setLogDate(new Date());
            soActivityLog.setSoActivityType(new SoActivityType(7));
            soActivityLog.setLogType("Ignored");
            if(ignoreId != null){
                soActivityLog.setReason(soIgnoreReasonDAO.findSoIgnoreReason(ignoreId).getName());
            }
            soActivityLog.setRemark(ignoreRemark);
            soActivityLog.setCreateBy(JSFUtil.getUserSession().getUsers());
            soActivityLogDAO.edit(soActivityLog);
            
            //save SoMsgCasetypeMap
            SoMsgCasetypeMap somCaseType = new SoMsgCasetypeMap();
            somCaseType.setCaseSeq(1);
            somCaseType.setSoMessage(soMessage);
            if(soCaseTypeId != null){
                SoCaseType soCaseType = soCaseTypeDAO.findSoCaseType(soCaseTypeId);
                somCaseType.setSoCaseType(soCaseType);
                somCaseType.setCaseTypeCode(soCaseType.getCode());
            }
            if(soCaseTypeSubId != null){
                SoCaseType soCaseTypeSub = soCaseTypeDAO.findSoCaseType(soCaseTypeSubId);
                somCaseType.setSoCaseTypeSub(soCaseTypeSub);
                somCaseType.setCaseSubtypeCode(soCaseTypeSub.getCode());
            }
            soMessageDAO.saveSoMsgCaseTypeMap(somCaseType);
            
            //save end time when closed
            this.saveEndTime(soMessage.getId());
            
            //save SoMessage
            soMessage.setRespEndDate(new Date());
            soMessage.setUpdate_by(JSFUtil.getUserSession().getUsers().getId());
            soMessage.setUpdate_date(new Date());
            soMessage.setCase_status("IG");
            soMessageDAO.edit(soMessage);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(soMessage.getSource_subtype_enum_id().equals("EM_OUT") && soMessage.getParentSoMessage() == null){
            JSFUtil.redirect("emailthankyou.jsf");
        }else{
            JSFUtil.redirect(JSFUtil.getServletContext().getContextPath() + "/social/sclassignment.jsf?pagetype=CL");
        }
    }
    
    private void saveEndTime(Integer soMessageId){
        try{
            SoMsgUserAssigntime obj = soMessageDAO.findSoMsgUserAssigntimeBySoMessageId(soMessageId);
            if(obj != null){
                obj.setTurnaroundEnd(new Date());
                obj.setUpdateBy(JSFUtil.getUserSession().getUsers().getId());
                obj.setUpdateDate(new Date());
                soMessageDAO.saveSoMsgUserAssigntime(obj);
            }
        }catch(Exception e){
            log.error(e);
        }
    }
    
    public void soCaseTypeListener(ValueChangeEvent event){
        soCaseTypeId = (Integer) event.getNewValue();
        if(soCaseTypeId != null){
            soCaseTypeSubList = soCaseTypeDAO.findSoCaseTopicListById(soCaseTypeId);
        }
    }
    
    public void soCaseTypeSubListener(ValueChangeEvent event){
        soCaseTypeSubId = (Integer) event.getNewValue();
    }

    public Integer getSoMessageId() {
        return soMessageId;
    }

    public void setSoMessageId(Integer soMessageId) {
        this.soMessageId = soMessageId;
    }

    public Integer getIgnoreId() {
        return ignoreId;
    }

    public void setIgnoreId(Integer ignoreId) {
        this.ignoreId = ignoreId;
    }

    public List<SoIgnoreReason> getIgnoreList() {
        return ignoreList;
    }

    public void setIgnoreList(List<SoIgnoreReason> ignoreList) {
        this.ignoreList = ignoreList;
    }

    public String getIgnoreRemark() {
        return ignoreRemark;
    }

    public void setIgnoreRemark(String ignoreRemark) {
        this.ignoreRemark = ignoreRemark;
    }

    public SoActivityLogDAO getSoActivityLogDAO() {
        return soActivityLogDAO;
    }

    public void setSoActivityLogDAO(SoActivityLogDAO soActivityLogDAO) {
        this.soActivityLogDAO = soActivityLogDAO;
    }

    public SoIgnoreReasonDAO getSoIgnoreReasonDAO() {
        return soIgnoreReasonDAO;
    }

    public void setSoIgnoreReasonDAO(SoIgnoreReasonDAO soIgnoreReasonDAO) {
        this.soIgnoreReasonDAO = soIgnoreReasonDAO;
    }

    public SoMessageDAO getSoMessageDAO() {
        return soMessageDAO;
    }

    public void setSoMessageDAO(SoMessageDAO soMessageDAO) {
        this.soMessageDAO = soMessageDAO;
    }

    public SoCaseTypeDAO getSoCaseTypeDAO() {
        return soCaseTypeDAO;
    }

    public void setSoCaseTypeDAO(SoCaseTypeDAO soCaseTypeDAO) {
        this.soCaseTypeDAO = soCaseTypeDAO;
    }

    public SoMessage getSoMessage() {
        return soMessage;
    }

    public void setSoMessage(SoMessage soMessage) {
        this.soMessage = soMessage;
    }

    public List<SoCaseType> getSoCaseTypeList() {
        return soCaseTypeList;
    }

    public void setSoCaseTypeList(List<SoCaseType> soCaseTypeList) {
        this.soCaseTypeList = soCaseTypeList;
    }

    public Integer getSoCaseTypeId() {
        return soCaseTypeId;
    }

    public void setSoCaseTypeId(Integer soCaseTypeId) {
        this.soCaseTypeId = soCaseTypeId;
    }

    public List<SoCaseType> getSoCaseTypeSubList() {
        return soCaseTypeSubList;
    }

    public void setSoCaseTypeSubList(List<SoCaseType> soCaseTypeSubList) {
        this.soCaseTypeSubList = soCaseTypeSubList;
    }

    public Integer getSoCaseTypeSubId() {
        return soCaseTypeSubId;
    }

    public void setSoCaseTypeSubId(Integer soCaseTypeSubId) {
        this.soCaseTypeSubId = soCaseTypeSubId;
    }
}
