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
@Table(name = "product_workflow")
@NamedQueries({
    @NamedQuery(name = "ProductWorkflow.findAll", query = "SELECT p FROM ProductWorkflow p")})
public class ProductWorkflow implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "step_no")
    private int stepNo;
    @Basic(optional = false)
    @Column(name = "approval_role_name")
    private String approvalRoleName;
    @Column(name = "require")
    private Boolean require;
    @Column(name = "notify")
    private Boolean notify;
    @Column(name = "sent_email")
    private Boolean sentEmail;
    @Column(name = "email")
    private String email;
    @Column(name = "auto_approve")
    private Boolean autoApprove;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by")
    private String updateBy;
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = true, updatable = true)
    @ManyToOne
    private Product product;

    public ProductWorkflow() {
    }

    public ProductWorkflow(Integer id) {
        this.id = id;
    }

    public ProductWorkflow(Integer id, int stepNo, String approvalRoleName) {
        this.id = id;
        this.stepNo = stepNo;
        this.approvalRoleName = approvalRoleName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStepNo() {
        return stepNo;
    }

    public void setStepNo(int stepNo) {
        this.stepNo = stepNo;
    }

    public String getApprovalRoleName() {
        return approvalRoleName;
    }

    public void setApprovalRoleName(String approvalRoleName) {
        this.approvalRoleName = approvalRoleName;
    }

    public Boolean getRequire() {
        return require;
    }

    public void setRequire(Boolean require) {
        this.require = require;
    }

    public Boolean getNotify() {
        return notify;
    }

    public void setNotify(Boolean notify) {
        this.notify = notify;
    }

    public Boolean getSentEmail() {
        return sentEmail;
    }

    public void setSentEmail(Boolean sentEmail) {
        this.sentEmail = sentEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(Boolean autoApprove) {
        this.autoApprove = autoApprove;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        if (!(object instanceof ProductWorkflow)) {
            return false;
        }
        ProductWorkflow other = (ProductWorkflow) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ProductWorkflow[ id=" + id + " ]";
    }
    
}
