/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.admin;

import java.util.Date;

/**
 *
 * @author Administrator
 */
public class CampaignValue {
    private Integer id;
    private String campaignCode;
    private String campaignName;
    private Date startDate;
    private Date endDate;
    private Boolean status;
    private Long totalAssign;
    private Long listUsed;
    private Long listReused;
    private String description;
    
    public CampaignValue(Integer id, String campaignCode, String campaignName, Date startDate, Date endDate, Boolean status, Long totalAssign, Long listUsed, Long listReused, String description){
        this.id = id;
        this.campaignCode = campaignCode;
        this.campaignName = campaignName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.totalAssign = totalAssign;
        this.listUsed = listUsed;
        this.listReused = listReused;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
   
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getListReused() {
        return listReused;
    }

    public void setListReused(Long listReused) {
        this.listReused = listReused;
    }

    public Long getListUsed() {
        return listUsed;
    }

    public void setListUsed(Long listUsed) {
        this.listUsed = listUsed;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getTotalAssign() {
        return totalAssign;
    }

    public void setTotalAssign(Long totalAssign) {
        this.totalAssign = totalAssign;
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
    }
    
}
