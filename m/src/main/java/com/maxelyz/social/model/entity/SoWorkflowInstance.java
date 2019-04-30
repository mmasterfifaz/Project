/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.model.entity;

import com.maxelyz.core.model.entity.Users;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ukritj
 */
@Entity
@Table(name = "so_workflow_instance")
@NamedQueries({
    @NamedQuery(name = "SoWorkflowInstance.findAll", query = "SELECT s FROM SoWorkflowInstance s")})
public class SoWorkflowInstance implements Serializable {
    @OneToMany(mappedBy = "soWorkflowInstance")
    private Collection<SoWorkflowInstanceLog> soWorkflowInstanceLogCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "status")
    private String status;
    @Lob
    @Column(name = "note")
    private String note;
    @Basic(optional = false)
    @NotNull
    @Column(name = "approval_id")
    private int approvalId;
    @JoinColumn(name = "create_by", referencedColumnName = "id")
    @ManyToOne
    private Users createByUsers;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "update_by")
    private int updateBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @JoinColumn(name = "so_message_id", referencedColumnName = "id")
    @ManyToOne
    private SoMessage soMessage;

    public SoWorkflowInstance() {
    }

    public SoWorkflowInstance(Integer id) {
        this.id = id;
    }

    public SoWorkflowInstance(Integer id, String status, int approvalId, Users createByUsers, int updateBy) {
        this.id = id;
        this.status = status;
        this.approvalId = approvalId;
        this.createByUsers = createByUsers;
        this.updateBy = updateBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(int approvalId) {
        this.approvalId = approvalId;
    }

    public Users getCreateByUsers() {
        return createByUsers;
    }

    public void setCreateByUsers(Users createByUsers) {
        this.createByUsers = createByUsers;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public SoMessage getSoMessage() {
        return soMessage;
    }

    public void setSoMessage(SoMessage soMessage) {
        this.soMessage = soMessage;
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
        if (!(object instanceof SoWorkflowInstance)) {
            return false;
        }
        SoWorkflowInstance other = (SoWorkflowInstance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.social.model.entity.SoWorkflowInstance[ workflowId=" + id + " ]";
    }

    public Collection<SoWorkflowInstanceLog> getSoWorkflowInstanceLogCollection() {
        return soWorkflowInstanceLogCollection;
    }

    public void setSoWorkflowInstanceLogCollection(Collection<SoWorkflowInstanceLog> soWorkflowInstanceLogCollection) {
        this.soWorkflowInstanceLogCollection = soWorkflowInstanceLogCollection;
    }
    
}
