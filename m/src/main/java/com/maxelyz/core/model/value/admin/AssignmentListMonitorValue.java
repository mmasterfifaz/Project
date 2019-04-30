package com.maxelyz.core.model.value.admin;


public class AssignmentListMonitorValue {
    private String campaign;
    private String name;
    private String surname;
    private String status;
    private Long nostatus;
    private int createBy;
    private int campaignId;
    private Long grossSale;
    private Long netSale;

    public AssignmentListMonitorValue(String campaign, String name, String surname, String status, Long nostatus) {
        this.campaign = campaign;
        this.name = name;
        this.surname = surname;
        this.status = status;
        this.nostatus = nostatus;
    }
    
    public AssignmentListMonitorValue(String campaign, int campaignId, String name, String surname, String status, Long nostatus, int createBy) {
        this.campaign = campaign;
        this.campaignId = campaignId;
        this.name = name;
        this.surname = surname;
        this.status = status;
        this.nostatus = nostatus;
        this.createBy = createBy; 
    }
    
    public AssignmentListMonitorValue(String name, String surname, String status, Long nostatus, int createBy, Long grossSale, long netSale) {
        this.name = name;
        this.surname = surname;
        this.status = status;
        this.nostatus = nostatus;
        this.createBy = createBy;
        this.grossSale = grossSale;    
        this.netSale = netSale;
    }
        
    public AssignmentListMonitorValue(String campaign, int campaignId, String name, String surname, String status, Long nostatus, int createBy, Long grossSale, long netSale) {
        this.campaign = campaign;
        this.campaignId = campaignId;
        this.name = name;
        this.surname = surname;
        this.status = status;
        this.nostatus = nostatus;
        this.createBy = createBy;
        this.grossSale = grossSale;    
        this.netSale = netSale;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSurname() {
        return surname;
    }

    public Long getNostatus() {
        return nostatus;
    }

    public String getStatus() {
        return status;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
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

    public Long getGrossSale() {
        return grossSale;
    }

    public void setGrossSale(Long grossSale) {
        this.grossSale = grossSale;
    }

    public Long getNetSale() {
        return netSale;
    }

    public void setNetSale(Long netSale) {
        this.netSale = netSale;
    }

   
}
