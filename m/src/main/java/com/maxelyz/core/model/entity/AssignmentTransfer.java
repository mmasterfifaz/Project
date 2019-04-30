/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "assignment_transfer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AssignmentTransfer.findAll", query = "SELECT a FROM AssignmentTransfer a")})
public class AssignmentTransfer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "transfer_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date transferDate;
    @Basic(optional = false)
    @JoinColumn(name = "campaign_id", referencedColumnName = "id")
    @ManyToOne
    private Campaign campaign;
    @Basic(optional = false)
    @JoinColumn(name = "marketing_id", referencedColumnName = "id")
    @ManyToOne
    private Marketing marketing;
    @Basic(optional = false)
    @JoinColumn(name = "from_user_id", referencedColumnName = "id")
    @ManyToOne
    private Users fromUser;
    @Basic(optional = false)
    @JoinColumn(name = "to_user_id", referencedColumnName = "id")
    @ManyToOne
    private Users toUser;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @Column(name = "no_customer")
    private Integer noCustomer;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @JoinColumn(name = "create_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users createByUser;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignmentTransfer")
    private Collection<AssignmentTransferDetail> assignmentTransferDetailCollection;

    public AssignmentTransfer() {
    }

    public AssignmentTransfer(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public Users getFromUser() {
        return fromUser;
    }

    public void setFromUser(Users fromUser) {
        this.fromUser = fromUser;
    }

    public Users getToUser() {
        return toUser;
    }

    public void setToUser(Users toUser) {
        this.toUser = toUser;
    }
    
    public Integer getNoCustomer() {
        return noCustomer;
    }

    public void setNoCustomer(Integer noCustomer) {
        this.noCustomer = noCustomer;
    }
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Marketing getMarketing() {
        return marketing;
    }

    public void setMarketing(Marketing marketing) {
        this.marketing = marketing;
    }
    

    @XmlTransient
    public Collection<AssignmentTransferDetail> getAssignmentTransferDetailCollection() {
        return assignmentTransferDetailCollection;
    }

    public void setAssignmentTransferDetailCollection(Collection<AssignmentTransferDetail> assignmentTransferDetailCollection) {
        this.assignmentTransferDetailCollection = assignmentTransferDetailCollection;
    }

    public Users getCreateByUser() {
        return createByUser;
    }

    public void setCreateByUser(Users createByUser) {
        this.createByUser = createByUser;
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
        if (!(object instanceof AssignmentTransfer)) {
            return false;
        }
        AssignmentTransfer other = (AssignmentTransfer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.AssignmentTransfer[ id=" + id + " ]";
    }
    
}
