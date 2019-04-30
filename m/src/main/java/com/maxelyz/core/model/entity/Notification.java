/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ukritj
 */
@Entity
@Table(name = "notification")
@NamedQueries({
    @NamedQuery(name = "Notification.findAll", query = "SELECT n FROM Notification n")})
public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    //@Basic(optional = false)
    @JoinColumn(name = "purchase_order_id", referencedColumnName = "id")
    @ManyToOne
    private PurchaseOrder purchaseOrder;
    //@Basic(optional = false)
    @JoinColumn(name = "contact_case_id", referencedColumnName = "id")
    @ManyToOne
    private ContactCase contactCase;
    @Column(name = "ref_no")
    private String refNo;
    @Column(name = "detail")
    private String detail;
    @JoinColumn(name = "to_user_id", referencedColumnName = "id")
    @ManyToOne
    private Users toUser;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @JoinColumn(name = "create_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users createByUser;
    @Column(name = "create_by")
    private String createByName;
    @Column(name = "type") //followup, message
    private String type;
    //@Column(name = "assignment_detail_id")
    //private Integer assignmentDetailId;
    @JoinColumn(name = "assignment_detail_id", referencedColumnName = "id")
    @ManyToOne
    private AssignmentDetail assignmentDetail;
    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "followup_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date followupDate;
    @Column(name = "enable")
    private Boolean enable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Users getToUser() {
        return toUser;
    }

    public void setToUser(Users toUser) {
        this.toUser = toUser;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Users getCreateByUser() {
        return createByUser;
    }

    public void setCreateByUser(Users createByUser) {
        this.createByUser = createByUser;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getType() {
        String str = "";
        if(type.equals("message"))
            str = "Message";
        else if(type.equals("followup"))
            str = "Follow Up";
        else if(type.equals("reconfirm"))
            str = "Re-Confirm";
        return str;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AssignmentDetail getAssignmentDetail() {
        return assignmentDetail;
    }

    public void setAssignmentDetail(AssignmentDetail assignmentDetail) {
        this.assignmentDetail = assignmentDetail;
    }

//    public Integer getAssignmentDetailId() {
//        return assignmentDetailId;
//    }
//
//    public void setAssignmentDetailId(Integer assignmentDetailId) {
//        this.assignmentDetailId = assignmentDetailId;
//    }

    public Date getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(Date followupDate) {
        this.followupDate = followupDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public ContactCase getContactCase() {
        return contactCase;
    }

    public void setContactCase(ContactCase contactCase) {
        this.contactCase = contactCase;
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
        if (!(object instanceof Notification)) {
            return false;
        }
        Notification other = (Notification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Notification[ id=" + id + " ]";
    }
    
}
