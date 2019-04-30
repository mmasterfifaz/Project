/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.value.admin;

import com.maxelyz.utils.JSFUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author admin
 */
public class ContactRecordValue {
    private Integer contactHistoryId;
    private Date contactDate;
    private Integer customerId;
    private String customerName;
    private String contactTo;
    private String contactResult;    
    private String contactStatus;
    private String trackId;
    private String mediaPath;
    private String filePath;
    private String fileName;
    private String stationNo;
    private String remark;
    private String createBy;
    private Date createDate;
    private Integer talkTime;
    private String strTalkTime;
    private Integer createById;
    private Long cntSale;
    private Long cntCase;
    private String channel;
    private String channelType;
    private String voiceSource;
    
    //13 Fields
    public ContactRecordValue(Integer contactHistoryId
            , Date contactDate
            , Integer customerId
            , String customerName
            , String contactTo
            , String contactResult
            , String trackId
            , String remark
            , String createBy
            , Date createDate
            , Integer talkTime
            , Integer createById
            , String stationNo){
        this.contactHistoryId = contactHistoryId;
        this.contactDate = contactDate;
        this.customerId = customerId;
        this.customerName = customerName;
        if(contactTo != null && !contactTo.equals("")){
            this.contactTo = getHidePhoneNo(contactTo);
        }else{
            this.contactTo = "";            
        }
        this.contactResult = contactResult;
        this.trackId = trackId;
        this.remark = remark;
        this.createBy = createBy;
        this.createDate = createDate;
        this.mediaPath = toMediaPath();
        this.filePath = toFilePath();
        this.talkTime = talkTime;
        if(talkTime != null && talkTime != 0){
            strTalkTime = JSFUtil.convertSecondToHHMMSS(talkTime);
        }else{
            strTalkTime = "";
        }
        this.createById = createById;
        this.stationNo = stationNo;
        this.fileName = (this.stationNo != null ? this.stationNo : "0000") + convertDateToStr(this.createDate) + this.contactHistoryId;
    }
    
    //14 Fields For Admin Sale Approval Edit Page
    public ContactRecordValue(Integer contactHistoryId
            , Date contactDate
            , Integer customerId
            , String customerName
            , String contactTo
            , String contactResult
            , String trackId
            , String remark
            , String createBy
            , Date createDate
            , Integer talkTime
            , Integer createById
            , String stationNo            
            , String voiceSource){
        this.contactHistoryId = contactHistoryId;
        this.contactDate = contactDate;
        this.customerId = customerId;
        this.customerName = customerName;
        if(contactTo != null && !contactTo.equals("")){
            this.contactTo = getHidePhoneNo(contactTo);
        }else{
            this.contactTo = "";            
        }
        this.contactResult = contactResult;
        this.trackId = trackId;
        this.remark = remark;
        this.createBy = createBy;
        this.createDate = createDate;
        this.mediaPath = toMediaPath();
        this.filePath = toFilePath();
        this.talkTime = talkTime;
        if(talkTime != null && talkTime != 0){
            strTalkTime = JSFUtil.convertSecondToHHMMSS(talkTime);
        }else{
            strTalkTime = "";
        }
        this.createById = createById;
        this.stationNo = stationNo;
        this.fileName = (this.stationNo != null ? this.stationNo : "0000") + convertDateToStr(this.createDate) + this.contactHistoryId;
        if(voiceSource != null && !voiceSource.equals("")) {
            this.voiceSource = voiceSource;
        } else {
            this.voiceSource = JSFUtil.getApplication().getVoiceSource();
        }
    }

    // 16 Field for contact history on front customer handling
    public ContactRecordValue(Integer contactHistoryId
            , Date contactDate
            , Integer customerId
            , String customerName
            , String contactTo
            , String contactResult
            , String trackId
            , String remark
            , String createBy
            , Date createDate
            , Integer talkTime
            , Integer createById
            , String channel
            , String channelType
            , Long cntCase
            , Long cntSale){
        this.contactHistoryId = contactHistoryId;
        this.contactDate = contactDate;
        this.customerId = customerId;
        this.customerName = customerName;
        if(contactTo != null && !contactTo.equals("")){
            this.contactTo = getHidePhoneNo(contactTo);
        }else{
            this.contactTo = "";            
        }
        this.contactResult = contactResult;
        this.trackId = trackId;
        this.remark = remark;
        this.createBy = createBy;
        this.createDate = createDate;
        this.mediaPath = toMediaPath();
        this.filePath = toFilePath();
        this.talkTime = talkTime;
        if(talkTime != null && talkTime != 0){
            strTalkTime = JSFUtil.convertSecondToHHMMSS(talkTime);
        }else{
            strTalkTime = "";
        }
        this.createById = createById;
        this.channel = channel;
        this.channelType = channelType;
        this.cntCase = cntCase;
        this.cntSale = cntSale;
    }
    
    //17 Fields
    public ContactRecordValue(Integer contactHistoryId
            , Date contactDate
            , Integer customerId
            , String customerName
            , String contactTo
            , String contactResult
            , String trackId
            , String remark
            , String createBy
            , Date createDate
            , Integer talkTime
            , Integer createById
            , String channel
            , String channelType
            , Long cntCase
            , Long cntSale
            , String stationNo){
        this.contactHistoryId = contactHistoryId;
        this.contactDate = contactDate;
        this.customerId = customerId;
        this.customerName = customerName;
        if(contactTo != null && !contactTo.equals("")){
            this.contactTo = getHidePhoneNo(contactTo);
        }else{
            this.contactTo = "";            
        }
        this.contactResult = contactResult;
        this.trackId = trackId;
        this.remark = remark;
        this.createBy = createBy;
        this.createDate = createDate;
        this.mediaPath = toMediaPath();
        this.filePath = toFilePath();
        this.talkTime = talkTime;
        if(talkTime != null && talkTime != 0){
            strTalkTime = JSFUtil.convertSecondToHHMMSS(talkTime);
        }else{
            strTalkTime = "";
        }
        this.createById = createById;
        this.channel = channel;
        this.channelType = channelType;
        this.cntCase = cntCase;
        this.cntSale = cntSale;
        this.stationNo = stationNo;
        this.fileName = (this.stationNo != null ? this.stationNo : "0000") + convertDateToStr(this.createDate) + this.contactHistoryId;
    }

    //5 field contactHistoryDAO.findByAssignmentDetail
    public ContactRecordValue(Date contactDate
            , String contactTo
            , String contactResult
            , String remark
            , String createBy){
        this.contactDate = contactDate;
        if(contactTo != null && !contactTo.equals("")){
            this.contactTo = getHidePhoneNo(contactTo);
        }else{
            this.contactTo = "";            
        }
        this.contactResult = contactResult;
        this.remark = remark;
        this.createBy = createBy;
    }
    
    private String convertDateToStr(Date date){
        DateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
        if(date != null){
            return formatter.format(date);
        }else{
            return formatter.format(new Date());
        }
    }

    private String toMediaPath(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/", Locale.US);
        String str = "";
        String url = JSFUtil.getApplication().getVoiceMMSUrl();
        String strDate = "";
        if(trackId != null){
            if(createDate != null){
                strDate = sdf.format(createDate);
            }
            str = url + strDate;
        }
        return str;
    }
    private String toFilePath(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/", Locale.US);
        String str = "";
        String url = JSFUtil.getApplication().getVoiceFileUrl();
        String strDate = "";
        if(trackId != null){
            if(createDate != null){
                strDate = sdf.format(createDate);
            }
            str = url + strDate;
        }
        return str;
    }

    private String getHidePhoneNo(String no){
        String str = no;
        String hide = JSFUtil.getUserSession().getUserGroup().getHidePhoneNo();
        if(hide != null && !hide.equals("") && no != null && !no.equals("") && no.substring(0, 1).equals("0")){
            if(no.length() == 10){
                if(hide.equals("hideall")){
                    str = no.replaceAll(no, "**********");
                }else if(hide.equals("hideleft")){
                    str = "***" + no.substring(3);
                }else if(hide.equals("hideright")){
                    str = no.substring(0, no.length() - 3) + "***";
                }else if(hide.equals("hidecenter")){
                    str = no.substring(0, 3) + "***" + no.substring(6, no.length());
                }
            }else if(no.length() == 9){
                if(hide.equals("hideall")){
                    str = no.replaceAll(no, "*********");
                }else if(hide.equals("hideleft")){
                    str = "***" + no.substring(3);
                }else if(hide.equals("hideright")){
                    str = no.substring(0, no.length() - 3) + "***";
                }else if(hide.equals("hidecenter")){
                    str = no.substring(0, 2) + "***" + no.substring(5, no.length());
                }
            }
        }
        
        return str;
    }
    
    public Date getContactDate() {
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    public Integer getContactHistoryId() {
        return contactHistoryId;
    }

    public void setContactHistoryId(Integer contactHistoryId) {
        this.contactHistoryId = contactHistoryId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getContactResult() {
        return contactResult;
    }

    public void setContactResult(String contactResult) {
        this.contactResult = contactResult;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getContactTo() {
        return contactTo;
    }

    public void setContactTo(String contactTo) {
        this.contactTo = contactTo;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStrTalkTime() {
        return strTalkTime;
    }

    public void setStrTalkTime(String strTalkTime) {
        this.strTalkTime = strTalkTime;
    }

    public Integer getTalkTime() {
        return talkTime;
    }

    public void setTalkTime(Integer talkTime) {
        this.talkTime = talkTime;
    }

    public Integer getCreateById() {
        return createById;
    }

    public void setCreateById(Integer createById) {
        this.createById = createById;
    }
    
    public Long getCntSale() {
        return cntSale;
    }

    public void setCntSale(Long cntSale) {
        this.cntSale = cntSale;
    }
    
    public Long getCntCase() {
        return cntCase;
    }

    public void setCntCase(Long cntCase) {
        this.cntCase = cntCase;
    }
    
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
    
    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }    
    
    //18 Fields
    public ContactRecordValue(Integer contactHistoryId
            , Date contactDate
            , Integer customerId
            , String customerName
            , String contactTo
            , String contactStatus
            , String contactResult
            , String trackId
            , String remark
            , String createBy
            , Date createDate
            , Integer talkTime
            , Integer createById
            , String channel
            , String channelType
            , Long cntCase
            , Long cntSale
            , String stationNo){
        this.contactHistoryId = contactHistoryId;
        this.contactDate = contactDate;
        this.customerId = customerId;
        this.customerName = customerName;
        if(contactTo != null && !contactTo.equals("")){
            this.contactTo = getHidePhoneNo(contactTo);
        }else{
            this.contactTo = "";            
        }
        this.contactStatus = contactStatus;
        this.contactResult = contactResult;
        this.trackId = trackId;
        this.remark = remark;
        this.createBy = createBy;
        this.createDate = createDate;
        this.mediaPath = toMediaPath();
        this.filePath = toFilePath();
        this.talkTime = talkTime;
        if(talkTime != null && talkTime != 0){
            strTalkTime = JSFUtil.convertSecondToHHMMSS(talkTime);
        }else{
            strTalkTime = "";
        }
        this.createById = createById;
        this.channel = channel;
        this.channelType = channelType;
        this.cntCase = cntCase;
        this.cntSale = cntSale;
        this.stationNo = stationNo;
        this.fileName = (this.stationNo != null ? this.stationNo : "0000") + convertDateToStr(this.createDate) + this.contactHistoryId;
    }
    
    //19 Fields for contact record on admin menu
    public ContactRecordValue(Integer contactHistoryId
            , Date contactDate
            , Integer customerId
            , String customerName
            , String contactTo
            , String contactStatus
            , String contactResult
            , String trackId
            , String remark
            , String createBy
            , Date createDate
            , Integer talkTime
            , Integer createById
            , String channel
            , String channelType
            , String voiceSource
            , Long cntCase
            , Long cntSale
            , String stationNo){
        this.contactHistoryId = contactHistoryId;
        this.contactDate = contactDate;
        this.customerId = customerId;
        this.customerName = customerName;
        if(contactTo != null && !contactTo.equals("")){
            this.contactTo = getHidePhoneNo(contactTo);
        }else{
            this.contactTo = "";            
        }
        this.contactStatus = contactStatus;
        this.contactResult = contactResult;
        this.trackId = trackId;
        this.remark = remark;
        this.createBy = createBy;
        this.createDate = createDate;
        this.mediaPath = toMediaPath();
        this.filePath = toFilePath();
        this.talkTime = talkTime;
        if(talkTime != null && talkTime != 0){
            strTalkTime = JSFUtil.convertSecondToHHMMSS(talkTime);
        }else{
            strTalkTime = "";
        }
        this.createById = createById;
        this.channel = channel;
        this.channelType = channelType;
        this.cntCase = cntCase;
        this.cntSale = cntSale;
        this.stationNo = stationNo;
        this.fileName = (this.stationNo != null ? this.stationNo : "0000") + convertDateToStr(this.createDate) + this.contactHistoryId;
        if(voiceSource != null && !voiceSource.equals("")) {
            this.voiceSource = voiceSource;
        } else {
            this.voiceSource = JSFUtil.getApplication().getVoiceSource();
        }
    }

    public String getVoiceSource() {
        return voiceSource;
    }

    public void setVoiceSource(String voiceSource) {
        this.voiceSource = voiceSource;
    }
    
}
