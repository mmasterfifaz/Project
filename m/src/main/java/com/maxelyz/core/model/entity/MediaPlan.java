/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nuttakarn
 */
@Entity
@Table(name = "media_plan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MediaPlan.findAll", query = "SELECT m FROM MediaPlan m"),
    @NamedQuery(name = "MediaPlan.findById", query = "SELECT m FROM MediaPlan m WHERE m.id = :id")})
public class MediaPlan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "spot_ref_id")
    private String spotRefId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "spot_type")
    private String spotType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "day_of_spot")
    private String dayOfSpot;
    @Column(name = "date_of_spot")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfSpot;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "spot_telephone_number")
    private String spotTelephoneNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "channel")
    private String channel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "program_name")
    private String programName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "program_type")
    private String programType;
    @Size(max = 16)
    @Column(name = "show_time_start")
    private String showTimeStart;
    @Size(max = 16)
    @Column(name = "show_time_end")
    private String showTimeEnd;
    @Size(max = 16)
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
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "net_cost_per_spot")
    private BigDecimal netCostPerSpot;
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
    @Column(name = "total_inbound_call")
    private Integer totalInboundCall;
    @Column(name = "voice_box")
    private Integer voiceBox;
    @Column(name = "abandon")
    private Integer abandon;
    @Column(name = "accepted_call")
    private Integer acceptedCall;
    @Column(name = "unqualified_leads")
    private Integer unqualifiedLeads;
    @Column(name = "qualified_leads")
    private Integer qualifiedLeads;
    @Column(name = "referred_leads")
    private Integer referredLeads;
    @Column(name = "online_leads")
    private Integer onlineLeads;
    @Column(name = "duplication_list")
    private Integer duplicationList;
    @Column(name = "black_list")
    private Integer blackList;
    @Column(name = "exclusion_leads")
    private Integer exclusionLeads;
    @Column(name = "effective_leads")
    private Integer effectiveLeads;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Size(max = 50)
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 50)
    @Column(name = "update_by")
    private String updateBy;

    public MediaPlan() {
    }

    public MediaPlan(Integer id) {
        this.id = id;
    }

    public MediaPlan(Integer id, String spotRefId, String spotType, String dayOfSpot, String spotTelephoneNumber, String channel, String programName, String programType) {
        this.id = id;
        this.spotRefId = spotRefId;
        this.spotType = spotType;
        this.dayOfSpot = dayOfSpot;
        this.spotTelephoneNumber = spotTelephoneNumber;
        this.channel = channel;
        this.programName = programName;
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

    public Date getDateOfSpot() {
        return dateOfSpot;
    }

    public void setDateOfSpot(Date dateOfSpot) {
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

    public BigDecimal getNetCostPerSpot() {
        return netCostPerSpot;
    }

    public void setNetCostPerSpot(BigDecimal netCostPerSpot) {
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
    

    public Integer getTotalInboundCall() {
        return totalInboundCall;
    }

    public void setTotalInboundCall(Integer totalInboundCall) {
        this.totalInboundCall = totalInboundCall;
    }

    public Integer getVoiceBox() {
        return voiceBox;
    }

    public void setVoiceBox(Integer voiceBox) {
        this.voiceBox = voiceBox;
    }

    public Integer getAbandon() {
        return abandon;
    }

    public void setAbandon(Integer abandon) {
        this.abandon = abandon;
    }

    public Integer getAcceptedCall() {
        return acceptedCall;
    }

    public void setAcceptedCall(Integer acceptedCall) {
        this.acceptedCall = acceptedCall;
    }

    public Integer getUnqualifiedLeads() {
        return unqualifiedLeads;
    }

    public void setUnqualifiedLeads(Integer unqualifiedLeads) {
        this.unqualifiedLeads = unqualifiedLeads;
    }

    public Integer getQualifiedLeads() {
        return qualifiedLeads;
    }

    public void setQualifiedLeads(Integer qualifiedLeads) {
        this.qualifiedLeads = qualifiedLeads;
    }

    public Integer getReferredLeads() {
        return referredLeads;
    }

    public void setReferredLeads(Integer referredLeads) {
        this.referredLeads = referredLeads;
    }

    public Integer getOnlineLeads() {
        return onlineLeads;
    }

    public void setOnlineLeads(Integer onlineLeads) {
        this.onlineLeads = onlineLeads;
    }

    public Integer getDuplicationList() {
        return duplicationList;
    }

    public void setDuplicationList(Integer duplicationList) {
        this.duplicationList = duplicationList;
    }

    public Integer getBlackList() {
        return blackList;
    }

    public void setBlackList(Integer blackList) {
        this.blackList = blackList;
    }

    public Integer getExclusionLeads() {
        return exclusionLeads;
    }

    public void setExclusionLeads(Integer exclusionLeads) {
        this.exclusionLeads = exclusionLeads;
    }

    public Integer getEffectiveLeads() {
        return effectiveLeads;
    }

    public void setEffectiveLeads(Integer effectiveLeads) {
        this.effectiveLeads = effectiveLeads;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MediaPlan)) {
            return false;
        }
        MediaPlan other = (MediaPlan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.MediaPlan[ id=" + id + " ]";
    }
    
}
