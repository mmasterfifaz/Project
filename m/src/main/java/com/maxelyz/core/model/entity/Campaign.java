/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "campaign")
@NamedQueries({
    @NamedQuery(name = "Campaign.findAll", query = "SELECT c FROM Campaign c")})
public class Campaign implements Serializable {
    @Basic(optional =     false)
    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Basic(optional =     false)
    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @JoinColumn(name = "marketing_inbound_id", referencedColumnName = "id")
    @ManyToOne
    private Marketing marketingInbound;
    @JoinColumn(name = "assignment_inbound_id", referencedColumnName = "id")
    @ManyToOne
    private Assignment assignmentInbound;
    @Column(name = "inbound")
    private Boolean inbound;
    @OneToMany(mappedBy = "campaign")
    private Collection<Marketing> marketingCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "campaign")
    private Collection<Unassignment> unassignmentCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "max_call")
    private int maxCall;
    @Basic(optional = false)
    @Column(name = "max_call2")
    private int maxCall2;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_by")
    private String updateBy;
    @JoinTable(name = "campaign_product", joinColumns = {
        @JoinColumn(name = "campaign_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "product_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Product> productCollection;
    @OneToMany(mappedBy = "campaign")
    private Collection<CampaignCustomer> campaignCustomerCollection;
//  @OneToMany(cascade = CascadeType.ALL, mappedBy = "campaign") //Remark because it's make drop performance select all customer join contact_case
//  private Collection<Assignment> assignmentCollection;

    @JoinColumn(name = "max_call_contact_result_id", referencedColumnName = "id")
    @ManyToOne
    private ContactResult maxCallContactResult; 
    @JoinColumn(name = "max_call2_contact_result_id", referencedColumnName = "id")
    @ManyToOne
    private ContactResult maxCall2ContactResult; 
    
    @JoinColumn(name = "campaign_channel_id", referencedColumnName = "id")
    @ManyToOne
    private CampaignChannel campaignChannel;
    
    @JoinColumn(name = "default_manager_user_id", referencedColumnName = "id")
    @ManyToOne
    private Users users;
      
    public Campaign() {
    }

    public Campaign(Integer id) {
        this.id = id;
    }

    public Campaign(Integer id, String name, String description, Date startDate, Date endDate, int maxCall) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxCall = maxCall;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getMaxCall() {
        return maxCall;
    }

    public void setMaxCall(int maxCall) {
        this.maxCall = maxCall;
    }

    public int getMaxCall2() {
        return maxCall2;
    }

    public void setMaxCall2(int maxCall2) {
        this.maxCall2 = maxCall2;
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

    public Collection<Product> getProductCollection() {
        return productCollection;
    }

    public void setProductCollection(Collection<Product> productCollection) {
        this.productCollection = productCollection;
    }

    public Collection<Marketing> getMarketingCollection() {
        return marketingCollection;
    }

    public void setMarketingCollection(Collection<Marketing> marketingCollection) {
        this.marketingCollection = marketingCollection;
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
        if (!(object instanceof Campaign)) {
            return false;
        }
        Campaign other = (Campaign) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Campaign[id=" + id + "]";
    }

//    public Collection<Assignment> getAssignmentCollection() {
//        return assignmentCollection;
//    }
//
//    public void setAssignmentCollection(Collection<Assignment> assignmentCollection) {
//        this.assignmentCollection = assignmentCollection;
//    }

    public Collection<Unassignment> getUnassignmentCollection() {
        return unassignmentCollection;
    }

    public void setUnassignmentCollection(Collection<Unassignment> unassignmentCollection) {
        this.unassignmentCollection = unassignmentCollection;
    }

    public Boolean getInbound() {
        return inbound;
    }

    public void setInbound(Boolean inbound) {
        this.inbound = inbound;
    }

    public Assignment getAssignmentInbound() {
        return assignmentInbound;
    }

    public void setAssignmentInbound(Assignment assignmentInbound) {
        this.assignmentInbound = assignmentInbound;
    }

    public Marketing getMarketingInbound() {
        return marketingInbound;
    }

    public void setMarketingInbound(Marketing marketingInbound) {
        this.marketingInbound = marketingInbound;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Collection<CampaignCustomer> getCampaignCustomerCollection() {
        return campaignCustomerCollection;
    }

    public void setCampaignCustomerCollection(Collection<CampaignCustomer> campaignCustomerCollection) {
        this.campaignCustomerCollection = campaignCustomerCollection;
    }
    
    public ContactResult getMaxCallContactResult() {
        return maxCallContactResult;
    }
    
    public void setMaxCallContactResult(ContactResult maxCallContactResult) {
        this.maxCallContactResult = maxCallContactResult;
    }

    public ContactResult getMaxCall2ContactResult() {
        return maxCall2ContactResult;
    }

    public void setMaxCall2ContactResult(ContactResult maxCall2ContactResult) {
        this.maxCall2ContactResult = maxCall2ContactResult;
    }

    public CampaignChannel getCampaignChannel() {
        return campaignChannel;
    }

    public void setCampaignChannel(CampaignChannel campaignChannel) {
        this.campaignChannel = campaignChannel;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
    
    
    
    
    
    
}
