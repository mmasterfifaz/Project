/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nuttakarn
 */
@Entity
@Table(name = "media_plan_temp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MediaPlanTemp.findAll", query = "SELECT m FROM MediaPlanTemp m")})
public class MediaPlanTemp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "spot_ref_id")
    private String spotRefId;
    @Size(max = 10)
    @Column(name = "spot_type")
    private String spotType;
    @Size(max = 10)
    @Column(name = "day_of_spot")
    private String dayOfSpot;
    @Size(max = 100)
    @Column(name = "date_of_spot")
    private String dateOfSpot;
    @Size(max = 20)
    @Column(name = "spot_telephone_number")
    private String spotTelephoneNumber;
    @Size(max = 20)
    @Column(name = "channel")
    private String channel;
    @Size(max = 100)
    @Column(name = "program_name")
    private String programName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "program_type")
    private String programType;
    @Size(max = 100)
    @Column(name = "show_time_start")
    private String showTimeStart;
    @Size(max = 100)
    @Column(name = "show_time_end")
    private String showTimeEnd;
    @Size(max = 100)
    @Column(name = "actual_on_air_time")
    private String actualOnAirTime;
    @Size(max = 50)
    @Column(name = "section_break")
    private String sectionBreak;
    @Column(name = "copy_length")
    private Integer copyLength;
    @Size(max = 100)
    @Column(name = "tape")
    private String tape;
    @Size(max = 100)
    @Column(name = "net_cost_per_spot")
    private String netCostPerSpot;
    @Size(max = 15)
    @Column(name = "status")
    private String status;
    @Size(max = 15)
    @Column(name = "media_agency_remark")
    private String mediaAgencyRemark;
    @Size(max = 500)
    @Column(name = "mtl_remark1")
    private String mtlRemark1;
    @Size(max = 500)
    @Column(name = "mtl_remark2")
    private String mtlRemark2;
    @Size(max = 500)
    @Column(name = "mtl_remark3")
    private String mtlRemark3;
    @Size(max = 500)
    @Column(name = "mtl_remark4")
    private String mtlRemark4;
    @Size(max = 500)
    @Column(name = "Product_Assign")
    private String productAssign;
    @Size(max = 500)
    @Column(name = "ProductCode")
    private String productCode;
    @Size(max = 100)
    @Column(name = "total_inbound_call")
    private String totalInboundCall;
    @Size(max = 100)
    @Column(name = "voice_box")
    private String voiceBox;
    @Size(max = 100)
    @Column(name = "abandon")
    private String abandon;
    @Size(max = 100)
    @Column(name = "accepted_call")
    private String acceptedCall;
    @Size(max = 100)
    @Column(name = "unqualified_leads")
    private String unqualifiedLeads;
    @Size(max = 100)
    @Column(name = "qualified_leads")
    private String qualifiedLeads;
    @Size(max = 100)
    @Column(name = "referred_leads")
    private String referredLeads;
    @Size(max = 100)
    @Column(name = "online_leads")
    private String onlineLeads;
    @Size(max = 100)
    @Column(name = "duplication_list")
    private String duplicationList;
    @Size(max = 100)
    @Column(name = "black_list")
    private String blackList;
    @Size(max = 100)
    @Column(name = "exclusion_leads")
    private String exclusionLeads;
    @Size(max = 100)
    @Column(name = "effective_leads")
    private String effectiveLeads;
    @Size(max = 20)
    @Column(name = "import_status")
    private String importStatus;
    @Size(max = 255)
    @Column(name = "reason")
    private String reason;
    
    @Transient
    private boolean badFormat;

    public MediaPlanTemp() {
    }

    public MediaPlanTemp(Integer id) {
        this.id = id;
    }

    public MediaPlanTemp(Integer id, String programType) {
        this.id = id;
        this.programType = programType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpotRefId() {
        return spotRefId;
    }

    public void setSpotRefId(String spotRefId) {
        this.spotRefId = spotRefId;
    }

    public String getSpotType() {
        return spotType;
    }

    public void setSpotType(String spotType) {
        this.spotType = spotType;
    }

    public String getDayOfSpot() {
        return dayOfSpot;
    }

    public void setDayOfSpot(String dayOfSpot) {
        this.dayOfSpot = dayOfSpot;
    }

    public String getDateOfSpot() {
        return dateOfSpot;
    }

    public void setDateOfSpot(String dateOfSpot) {
        this.dateOfSpot = dateOfSpot;
    }

    public String getSpotTelephoneNumber() {
        return spotTelephoneNumber;
    }

    public void setSpotTelephoneNumber(String spotTelephoneNumber) {
        this.spotTelephoneNumber = spotTelephoneNumber;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getMtlRemark1() {
        return mtlRemark1;
    }

    public void setMtlRemark1(String mtlRemark1) {
        this.mtlRemark1 = mtlRemark1;
    }

    public String getMtlRemark2() {
        return mtlRemark2;
    }

    public void setMtlRemark2(String mtlRemark2) {
        this.mtlRemark2 = mtlRemark2;
    }

    public String getMtlRemark3() {
        return mtlRemark3;
    }

    public void setMtlRemark3(String mtlRemark3) {
        this.mtlRemark3 = mtlRemark3;
    }

    public String getMtlRemark4() {
        return mtlRemark4;
    }

    public void setMtlRemark4(String mtlRemark4) {
        this.mtlRemark4 = mtlRemark4;
    }

    public String getProductAssign() {
        return productAssign;
    }

    public void setProductAssign(String productAssign) {
        this.productAssign = productAssign;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public String getShowTimeStart() {
        return showTimeStart;
    }

    public void setShowTimeStart(String showTimeStart) {
        this.showTimeStart = showTimeStart;
    }

    public String getShowTimeEnd() {
        return showTimeEnd;
    }

    public void setShowTimeEnd(String showTimeEnd) {
        this.showTimeEnd = showTimeEnd;
    }

    public String getActualOnAirTime() {
        return actualOnAirTime;
    }

    public void setActualOnAirTime(String actualOnAirTime) {
        this.actualOnAirTime = actualOnAirTime;
    }

    public String getSectionBreak() {
        return sectionBreak;
    }

    public void setSectionBreak(String sectionBreak) {
        this.sectionBreak = sectionBreak;
    }

    public Integer getCopyLength() {
        return copyLength;
    }

    public void setCopyLength(Integer copyLength) {
        this.copyLength = copyLength;
    }

    public String getTape() {
        return tape;
    }

    public void setTape(String tape) {
        this.tape = tape;
    }

    public String getNetCostPerSpot() {
        return netCostPerSpot;
    }

    public void setNetCostPerSpot(String netCostPerSpot) {
        this.netCostPerSpot = netCostPerSpot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMediaAgencyRemark() {
        return mediaAgencyRemark;
    }

    public void setMediaAgencyRemark(String mediaAgencyRemark) {
        this.mediaAgencyRemark = mediaAgencyRemark;
    }

    public String getTotalInboundCall() {
        return totalInboundCall;
    }

    public void setTotalInboundCall(String totalInboundCall) {
        this.totalInboundCall = totalInboundCall;
    }

    public String getVoiceBox() {
        return voiceBox;
    }

    public void setVoiceBox(String voiceBox) {
        this.voiceBox = voiceBox;
    }

    public String getAbandon() {
        return abandon;
    }

    public void setAbandon(String abandon) {
        this.abandon = abandon;
    }

    public String getAcceptedCall() {
        return acceptedCall;
    }

    public void setAcceptedCall(String acceptedCall) {
        this.acceptedCall = acceptedCall;
    }

    public String getUnqualifiedLeads() {
        return unqualifiedLeads;
    }

    public void setUnqualifiedLeads(String unqualifiedLeads) {
        this.unqualifiedLeads = unqualifiedLeads;
    }

    public String getQualifiedLeads() {
        return qualifiedLeads;
    }

    public void setQualifiedLeads(String qualifiedLeads) {
        this.qualifiedLeads = qualifiedLeads;
    }

    public String getReferredLeads() {
        return referredLeads;
    }

    public void setReferredLeads(String referredLeads) {
        this.referredLeads = referredLeads;
    }

    public String getOnlineLeads() {
        return onlineLeads;
    }

    public void setOnlineLeads(String onlineLeads) {
        this.onlineLeads = onlineLeads;
    }

    public String getDuplicationList() {
        return duplicationList;
    }

    public void setDuplicationList(String duplicationList) {
        this.duplicationList = duplicationList;
    }

    public String getBlackList() {
        return blackList;
    }

    public void setBlackList(String blackList) {
        this.blackList = blackList;
    }

    public String getExclusionLeads() {
        return exclusionLeads;
    }

    public void setExclusionLeads(String exclusionLeads) {
        this.exclusionLeads = exclusionLeads;
    }

    public String getEffectiveLeads() {
        return effectiveLeads;
    }

    public void setEffectiveLeads(String effectiveLeads) {
        this.effectiveLeads = effectiveLeads;
    }

    public String getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(String importStatus) {
        this.importStatus = importStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isBadFormat() {
        return badFormat;
    }

    public void setBadFormat(boolean badFormat) {
        this.badFormat = badFormat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MediaPlanTemp)) {
            return false;
        }
        MediaPlanTemp other = (MediaPlanTemp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.MediaPlanTemp[ id=" + id + " ]";
    }
    
}
