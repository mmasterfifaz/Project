package com.maxelyz.core.controller.admin;

import com.google.gson.Gson;
import com.maxelyz.core.model.dao.MediaPlanDAO;
import com.maxelyz.core.model.entity.MediaPlan;
import com.maxelyz.core.model.entity.ModuleAuditLog;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class MediaPlanEditController {

    private static Logger log = Logger.getLogger(MediaPlanEditController.class);
    private static String SUCCESS = "mediaplan.xhtml?faces-redirect=true";
    private static String FAILURE = "mediaplanedit.xhtml";
    private MediaPlan mediaplan;
    private String mode, messageDup = "";
    private List<ModuleAuditLog> auditLogs;
    private String netCostperSpot = "",msgNet = "";
    private String startTimeHH = "0", startTimeMM = "0", startTimeSS = "0", endTimeHH = "0", endTimeMM = "0", endTimeSS = "0", acTimeHH = "0", acTimeMM = "0", acTimeSS = "0";

    @ManagedProperty(value = "#{mediaPlanDAO}")
    private MediaPlanDAO mediaPlanDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:mediaplan:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        msgNet = "";
        auditLogs = new ArrayList<ModuleAuditLog>();
        String selectedID = (JSFUtil.getRequestParameterMap("id") != null && !JSFUtil.getRequestParameterMap("id").isEmpty()) ? (String) JSFUtil.getRequestParameterMap("id") : "";
        mediaplan = new MediaPlan();
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            mediaplan = new MediaPlan();
        } else {
            mode = "edit";
            mediaplan = getMediaPlanDAO().findMediaPlan(new Integer(selectedID));
            if (mediaplan == null) {
                return;
            }
            identifyShowTime();
            if (mediaplan.getNetCostPerSpot() != null) {
                netCostperSpot = mediaplan.getNetCostPerSpot().toString();
            }
        }
    }

    public void auditLogListener(ActionEvent event) {
        if (JSFUtil.getRequestParameterMap("mediaPlanId") != null && !JSFUtil.getRequestParameterMap("mediaPlanId").isEmpty()) {
            Integer mpId = Integer.parseInt(JSFUtil.getRequestParameterMap("mediaPlanId"));
            auditLogs = mediaPlanDAO.findModuleAuditLogByRefID("media_plan",mpId);
        }
    }

    public String backAction() {
        return SUCCESS;
    }

    public String saveAction() {
        try {
            msgNet = "";
            messageDup = "";
            //Check dup spot ref id
            MediaPlan temp = mediaPlanDAO.findExistingMediaPlanByRefID(mediaplan.getSpotRefId());
            if (temp != null && temp.getId() != null && mode.equalsIgnoreCase("add")) {
                messageDup = " Spot Reference ID is already taken";
                return null;
            }
            mediaplan.setEnable(Boolean.TRUE);
            mediaplan.setUpdateDate(new Date());
            mediaplan.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
            finishedShowTime();
            if (netCostperSpot != null && !netCostperSpot.isEmpty()) {
                if(netCostperSpot.contains(",")){
                    netCostperSpot = netCostperSpot.replace(",", "");
                }
                BigDecimal d;
                try{
                    d = new BigDecimal(netCostperSpot);
                } catch(Exception c){
                    msgNet = "Must be a number";
                    d = new BigDecimal(0.0);
                    mediaplan.setNetCostPerSpot(d);
                    return null;
                }
                mediaplan.setNetCostPerSpot(d);
            }

            //Insert to module audit log
            ModuleAuditLog log = new ModuleAuditLog();
            log.setModuleName("media_plan");

            if (mode.equals("add")) {
                mediaplan.setCreateDate(new Date());
                mediaplan.setCreateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                mediaPlanDAO.create(mediaplan);
                log.setActionType("Add new record");
            }
            if (mode.equals("edit")) {
                mediaPlanDAO.edit(mediaplan);
                log.setActionType("Update record");
            }

            log.setRefNo(mediaplan.getId());
            log.setActionDate(new Date());
            log.setActionBy(JSFUtil.getUserSession().getUsers().getLoginName());
            //Create JSON MediaPlan
            Gson gson = new Gson();
            String tempJson = gson.toJson(mediaplan);
            String[] b = tempJson.split("spotRefId");
            String json = "{\"spotRefId" + b[1];
            log.setActionDetail(json);
            mediaPlanDAO.create(log);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MediaPlanEditController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
            return FAILURE;
        }
        return SUCCESS;
    }

    private void finishedShowTime() {
        String showTimeStart = "", showTimeEnd = "", actualTime = "", chk = "";
        if (startTimeHH != null && !startTimeHH.isEmpty()) {
            chk = (startTimeHH.length() > 1) ? startTimeHH : "0" + startTimeHH;
        }
        showTimeStart += (chk != null && chk.length() > 1) ? chk + ":" : "00:";
        chk = "";
        if (startTimeMM != null && !startTimeMM.isEmpty()) {
            chk = (startTimeMM.length() > 1) ? startTimeMM : "0" + startTimeMM;
        }
        showTimeStart += (chk != null && chk.length() > 1) ? chk + ":" : "00:";
        chk = "";
        if (startTimeSS != null && !startTimeSS.isEmpty()) {
            chk = (startTimeSS.length() > 1) ? startTimeSS : "0" + startTimeSS;
        }
        showTimeStart += (chk != null && chk.length() > 1) ? chk : "00";
        chk = "";
        if (endTimeHH != null && !endTimeHH.isEmpty()) {
            chk = (endTimeHH.length() > 1) ? endTimeHH : "0" + endTimeHH;
        }
        showTimeEnd += (chk != null && chk.length() > 1) ? chk + ":" : "00:";
        chk = "";
        if (endTimeMM != null && !endTimeMM.isEmpty()) {
            chk = (endTimeMM.length() > 1) ? endTimeMM : "0" + endTimeMM;
        }
        showTimeEnd += (chk != null && chk.length() > 1) ? chk + ":" : "00:";
        chk = "";
        if (endTimeSS != null && !endTimeSS.isEmpty()) {
            chk = (endTimeSS.length() > 1) ? endTimeSS : "0" + endTimeSS;
        }
        showTimeEnd += (chk != null && chk.length() > 1) ? chk : "00";
        chk = "";
        if (acTimeHH != null && !acTimeHH.isEmpty()) {
            chk = (acTimeHH.length() > 1) ? acTimeHH : "0" + acTimeHH;
        }
        actualTime += (chk != null && chk.length() > 1) ? chk + ":" : "00:";
        chk = "";
        if (acTimeMM != null && !acTimeMM.isEmpty()) {
            chk = (acTimeMM.length() > 1) ? acTimeMM : "0" + acTimeMM;
        }
        actualTime += (chk != null && chk.length() > 1) ? chk + ":" : "00:";
        chk = "";
        if (acTimeSS != null && !acTimeSS.isEmpty()) {
            chk = (acTimeSS.length() > 1) ? acTimeSS : "0" + acTimeSS;
        }
        actualTime += (chk != null && chk.length() > 1) ? chk : "00";
        chk = "";
        mediaplan.setShowTimeStart(showTimeStart);
        mediaplan.setShowTimeEnd(showTimeEnd);
        mediaplan.setActualOnAirTime(actualTime);
    }

    private void identifyShowTime() {
        String[] a = mediaplan.getShowTimeStart().split(":");
        String[] b = mediaplan.getShowTimeEnd().split(":");
        String[] c = mediaplan.getActualOnAirTime().split(":");
        startTimeHH = a[0];
        startTimeMM = a[1];
        startTimeSS = a[2];
        endTimeHH = b[0];
        endTimeMM = b[1];
        endTimeSS = b[2];
        acTimeHH = c[0];
        acTimeMM = c[1];
        acTimeSS = c[2];
    }

    public boolean isSavePermitted() {
        if (mode.equals("add")) {
            return SecurityUtil.isPermitted("admin:mediaplan:add");
        } else {
            return SecurityUtil.isPermitted("admin:mediaplan:edit");
        }
    }

    public void initializeListener() {

    }

    ///////////////////////////////////////////// GET SET METHOD //////////////////////////////////////////////
    public static String getSUCCESS() {
        return SUCCESS;
    }

    public static void setSUCCESS(String SUCCESS) {
        MediaPlanEditController.SUCCESS = SUCCESS;
    }

    public static String getFAILURE() {
        return FAILURE;
    }

    public static void setFAILURE(String FAILURE) {
        MediaPlanEditController.FAILURE = FAILURE;
    }

    public MediaPlan getMediaplan() {
        return mediaplan;
    }

    public void setMediaplan(MediaPlan mediaplan) {
        this.mediaplan = mediaplan;
    }

    public MediaPlanDAO getMediaPlanDAO() {
        return mediaPlanDAO;
    }

    public void setMediaPlanDAO(MediaPlanDAO mediaPlanDAO) {
        this.mediaPlanDAO = mediaPlanDAO;
    }

    public String getStartTimeHH() {
        return startTimeHH;
    }

    public void setStartTimeHH(String startTimeHH) {
        this.startTimeHH = startTimeHH;
    }

    public String getStartTimeMM() {
        return startTimeMM;
    }

    public void setStartTimeMM(String startTimeMM) {
        this.startTimeMM = startTimeMM;
    }

    public String getStartTimeSS() {
        return startTimeSS;
    }

    public void setStartTimeSS(String startTimeSS) {
        this.startTimeSS = startTimeSS;
    }

    public String getEndTimeHH() {
        return endTimeHH;
    }

    public void setEndTimeHH(String endTimeHH) {
        this.endTimeHH = endTimeHH;
    }

    public String getEndTimeMM() {
        return endTimeMM;
    }

    public void setEndTimeMM(String endTimeMM) {
        this.endTimeMM = endTimeMM;
    }

    public String getEndTimeSS() {
        return endTimeSS;
    }

    public void setEndTimeSS(String endTimeSS) {
        this.endTimeSS = endTimeSS;
    }

    public String getAcTimeHH() {
        return acTimeHH;
    }

    public void setAcTimeHH(String acTimeHH) {
        this.acTimeHH = acTimeHH;
    }

    public String getAcTimeMM() {
        return acTimeMM;
    }

    public void setAcTimeMM(String acTimeMM) {
        this.acTimeMM = acTimeMM;
    }

    public String getAcTimeSS() {
        return acTimeSS;
    }

    public void setAcTimeSS(String acTimeSS) {
        this.acTimeSS = acTimeSS;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public List<ModuleAuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(List<ModuleAuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }

    public String getNetCostperSpot() {
        return netCostperSpot;
    }

    public void setNetCostperSpot(String netCostperSpot) {
        this.netCostperSpot = netCostperSpot;
    }

    public String getMsgNet() {
        return msgNet;
    }

    public void setMsgNet(String msgNet) {
        this.msgNet = msgNet;
    }

}
