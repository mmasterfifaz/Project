/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import com.maxelyz.core.model.dao.ContactResultDAO;
import java.io.Serializable;
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

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "contact_result")
@NamedQueries({
    @NamedQuery(name = "ContactResult.findAll", query = "SELECT c FROM ContactResult c")})
public class ContactResult implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Column(name = "status_id")
    private String statusId;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "contact_status")
    private String contactStatus;
    @Basic(optional = false)
    @Column(name = "enable")
    private Boolean enable;
    @Basic(optional = false)
    @Column(name = "seq_no")
    private Integer seqNo;
    
    @Column(name = "yes_sale_related")
    private Boolean yesSaleRelated;

    @Column(name = "system_contact_result")
    private Boolean systemContactResult;
    
    @JoinColumn(name = "contact_result_group_id", referencedColumnName = "id")
    @ManyToOne
    private ContactResultGroup contactResultGroup; 
    @Column(name = "close_assignment_detail")
    private Boolean closeAssignmentDetail;    
    @JoinColumn(name = "eoc_contact_result_id", referencedColumnName = "id")
    @ManyToOne
    private ContactResult eocContactResult; 

    public ContactResult() {
    }

    public ContactResult(Integer id) {
        this.id = id;
    }

    public ContactResult(Integer id, String code, String name, String contactStatus) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.contactStatus = contactStatus;
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

    public String getContactStatus() {
        return contactStatus;
    }

    public String getContactStatusLabel() {
        return ContactResultDAO.getContactResultLabel(contactStatus);
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public ContactResultGroup getContactResultGroup() {
        return contactResultGroup;
    }

    public void setContactResultGroup(ContactResultGroup contactResultGroup) {
        this.contactResultGroup = contactResultGroup;
    }

    public Boolean getCloseAssignmentDetail() {
        return closeAssignmentDetail;
    }

    public void setCloseAssignmentDetail(Boolean closeAssignmentDetail) {
        this.closeAssignmentDetail = closeAssignmentDetail;
    }

    public ContactResult getEocContactResult() {
        return eocContactResult;
    }

    public void setEocContactResult(ContactResult eocContactResult) {
        this.eocContactResult = eocContactResult;
    }

    public Boolean getYesSaleRelated() {
        return yesSaleRelated;
    }

    public void setYesSaleRelated(Boolean yesSaleRelated) {
        this.yesSaleRelated = yesSaleRelated;
    }

    public Boolean getSystemContactResult() {
        return systemContactResult;
    }

    public void setSystemContactResult(Boolean systemContactResult) {
        this.systemContactResult = systemContactResult;
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
        if (!(object instanceof ContactResult)) {
            return false;
        }
        ContactResult other = (ContactResult) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ContactResult[id=" + id + "]";
    }

}
