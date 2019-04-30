
package com.maxelyz.core.model.value.admin;

public class AssignmentListMonitorByStatusValue {
    private String campaign;
    private String name;
    private String surname;
    private int createBy;
    private int campaignId;
    private int assigned;
    private int viewed;
    private int opened;
    private int followup;
    private int closed;
    private int grossSale;
    private int netSale;

    public AssignmentListMonitorByStatusValue(String campaign, String name, String surname) {
        this.campaign = campaign;
        this.name = name;
        this.surname = surname;
    }
    
    public AssignmentListMonitorByStatusValue(String campaign, int campaignId, String name, String surname, int createBy) {
        this.campaign = campaign;
        this.campaignId = campaignId;
        this.name = name;
        this.surname = surname;
        this.createBy = createBy;
    }
        
    public AssignmentListMonitorByStatusValue(String campaign, String name, String surname, int assigned, int viewed, int opened, int followup, int closed) {
        this.campaign = campaign;
        this.name = name;
        this.surname = surname;
        this.assigned = assigned;
        this.viewed = viewed;
        this.opened = opened;
        this.followup = followup;
        this.closed = closed;
    }
    
    public int getAssigned() {
        return assigned;
    }

    public void setAssigned(int assigned) {
        this.assigned = assigned;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public int getFollowup() {
        return followup;
    }

    public void setFollowup(int followup) {
        this.followup = followup;
    }
    
    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOpened() {
        return opened;
    }

    public void setOpened(int opened) {
        this.opened = opened;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public int getGrossSale() {
        return grossSale;
    }

    public void setGrossSale(int grossSale) {
        this.grossSale = grossSale;
    }

    public int getNetSale() {
        return netSale;
    }

    public void setNetSale(int netSale) {
        this.netSale = netSale;
    }


}
