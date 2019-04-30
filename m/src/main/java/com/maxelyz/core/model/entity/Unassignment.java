/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "unassignment")
@NamedQueries({
    @NamedQuery(name = "Unassignment.findAll", query = "SELECT u FROM Unassignment u")})
public class Unassignment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "unassignment_mode")
    private String unassignmentMode;
    @Column(name = "list_type")
    private String listType;
    @Column(name = "marketing_list")
    private String marketingList;
    @Column(name = "product")
    private String product;
    @Column(name = "contact_status")
    private String contactStatus;
    @Column(name = "users")
    private String users;
    @Column(name = "no_record")
    private Integer noRecord;
    @Column(name = "no_user")
    private Integer noUser;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unassignment")
    private Collection<UnassignmentDetail> unassignmentDetailCollection;
    @JoinColumn(name = "campaign_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Campaign campaign;

    public Unassignment() {
    }

    public Unassignment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getUnassignmentMode() {
        return unassignmentMode;
    }

    public void setUnassignmentMode(String unassignmentMode) {
        this.unassignmentMode = unassignmentMode;
    }
    
    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }    
    
    public String getMarketingList() {
        return marketingList;
    }

    public void setMarketingList(String marketingList) {
        this.marketingList = marketingList;
    }
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public Integer getNoRecord() {
        return noRecord;
    }

    public void setNoRecord(Integer noRecord) {
        this.noRecord = noRecord;
    }

 
    public Integer getNoUser() {
        return noUser;
    }

    public void setNoUser(Integer noUser) {
        this.noUser = noUser;
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

    public Collection<UnassignmentDetail> getUnassignmentDetailCollection() {
        return unassignmentDetailCollection;
    }

    public void setUnassignmentDetailCollection(Collection<UnassignmentDetail> unassignmentDetailCollection) {
        this.unassignmentDetailCollection = unassignmentDetailCollection;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
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
        if (!(object instanceof Unassignment)) {
            return false;
        }
        Unassignment other = (Unassignment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Unassignment[id=" + id + "]";
    }

}
